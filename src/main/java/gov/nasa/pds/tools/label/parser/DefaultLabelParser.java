// Copyright 2006-2010, by the California Institute of Technology.
// ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
// Any commercial use must be negotiated with the Office of Technology Transfer
// at the California Institute of Technology.
//
// This software is subject to U. S. export control laws and regulations
// (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
// is subject to U.S. export control laws and regulations, the recipient has
// the responsibility to obtain export licenses or other export authority as
// may be required before exporting such information to foreign countries or
// providing access to foreign nationals.
//
// $Id$ 
//

package gov.nasa.pds.tools.label.parser;

import gov.nasa.arc.pds.tools.util.FileUtils;
import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.label.CommentStatement;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.MalformedSFDULabel;
import gov.nasa.pds.tools.label.ManualPathResolver;
import gov.nasa.pds.tools.label.PointerResolver;
import gov.nasa.pds.tools.label.SFDULabel;
import gov.nasa.pds.tools.label.antlr.ODLLexer;
import gov.nasa.pds.tools.label.antlr.ODLParser;
import gov.nasa.pds.tools.label.validate.Validator;
import gov.nasa.pds.tools.util.MessageUtils;
import gov.nasa.pds.tools.util.VersionInfo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;

/**
 * Default implementation
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */

public class DefaultLabelParser implements LabelParser {

    private final boolean loadIncludes;

    private final boolean captureProblems;

    private final boolean allowExternalProblems;

    private final PointerResolver resolver;

    // default constructor, assumes you want to load included statements and
    // capture parse errors
    public DefaultLabelParser(final PointerResolver resolver) {
        this(true, true, resolver);
    }

    public DefaultLabelParser(final boolean loadIncludes,
            final boolean captureProblems, final PointerResolver resolver) {
        this(loadIncludes, captureProblems, false, resolver);
    }

    public DefaultLabelParser(final boolean loadIncludes,
            final boolean captureProblems, final boolean allowExternalProblems,
            final PointerResolver resolver) {
        this.loadIncludes = loadIncludes;
        this.captureProblems = captureProblems;
        this.allowExternalProblems = allowExternalProblems;
        this.resolver = resolver;

        // make sure resolver exists if loading includes
        if (loadIncludes && resolver == null) {
            // statement not externalized since only for internal development
            // use
            throw new RuntimeException(
                    "A PointerResolver is required to load include statements"); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.nasa.jpl.pds.tools.label.parser.LabelParser#parse(java.net.URL)
     */

    public Label parseLabel(final URL url) throws LabelParserException,
            IOException {
        return parseLabel(url, false);
    }

    public Label parseLabel(final File file) throws LabelParserException,
            IOException {
        return parseLabel(file, false);
    }

    public Label parseLabel(final URL url, final boolean forceParse)
            throws LabelParserException, IOException {
        final BufferedInputStream inputStream = new BufferedInputStream(url
                .openStream());
        inputStream.mark(100);
        URI labelURI = null;
        try {
            labelURI = url.toURI();
        } catch (URISyntaxException e) {
            throw new LabelParserException(
                    "bad url", ProblemType.INVALID_LABEL, //$NON-NLS-1$
                    url.getFile());
        }

        final Label label = new Label(labelURI);
        label.setCaptureProblems(this.captureProblems);
        label.setAllowExternalProblems(this.allowExternalProblems);
        return parseLabel(inputStream, label, forceParse);
    }

    public Label parseLabel(final File file, final boolean forceParse)
            throws LabelParserException, IOException {
        BufferedInputStream inputStream;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            inputStream.mark(100);
        } catch (FileNotFoundException e) {
            throw new LabelParserException(file, null, null,
                    "parser.error.missingFile", ProblemType.INVALID_LABEL, file //$NON-NLS-1$
                            .getName());
        }
        final Label label = new Label(file);
        label.setCaptureProblems(this.captureProblems);
        label.setAllowExternalProblems(this.allowExternalProblems);
        return parseLabel(inputStream, label, forceParse);
    }

    private Label parseLabel(final BufferedInputStream inputStream,
            final Label label, final boolean forceParse)
            throws LabelParserException, IOException {

        consumeSFDUHeader(inputStream);

        // Now look for PDS_VERSION_ID to ensure that this is a file we want to
        // validate
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));

        String versionLine = null;
        boolean hasExtraNewLines = false;
        do {
            versionLine = reader.readLine();
            if (versionLine != null && versionLine.trim().length() == 0) {
                hasExtraNewLines = true;

            }
        } while (versionLine != null && versionLine.trim().length() == 0);

        if (hasExtraNewLines) {
            label.addProblem(new CommentStatement(label, 1),
                    "parser.error.mislocatedVersion", //$NON-NLS-1$
                    ProblemType.PARSE_ERROR);
        }

        String[] line = new String[] { "" }; //$NON-NLS-1$
        if (versionLine != null) {
            line = versionLine.trim().split("="); //$NON-NLS-1$
        }

        if (line.length != 2) {
            label.setInvalid();
            final LabelParserException lpe = new LabelParserException(label,
                    null, null,
                    "parser.error.missingVersion", ProblemType.INVALID_LABEL, //$NON-NLS-1$
                    label.getSourceNameString());
            if (!forceParse) {
                throw lpe;
            }
            label.addProblem(lpe);
        } else {
            String name = line[0].trim();
            // String value = line[1].trim();

            if (!"PDS_VERSION_ID".equals(name)) { //$NON-NLS-1$
                label.setInvalid();
                final LabelParserException lpe = new LabelParserException(
                        label,
                        null,
                        null,
                        "parser.error.missingVersion", ProblemType.INVALID_LABEL, //$NON-NLS-1$
                        label.getSourceNameString());
                if (!forceParse) {
                    throw lpe;
                }
                label.addProblem(lpe);
            }
        }

        inputStream.reset();

        parseLabel(inputStream, label);

        // this is a label so it should end in END
        if (!label.hasEndStatement()) {
            label.addProblem(new CommentStatement(label, 1),
                    "parser.error.missingEndStatement", //$NON-NLS-1$
                    ProblemType.PARSE_ERROR);
        }
        return label;
    }

    private Label parseLabel(final BufferedInputStream inputStream,
            final Label label) throws LabelParserException, IOException {

        CustomAntlrInputStream customIs = new CustomAntlrInputStream(
                inputStream);
        CharStream antlrInput = new ANTLRInputStream(customIs);

        ODLLexer lexer = new ODLLexer(antlrInput);
        lexer.setLabel(label);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ODLParser parser = new ODLParser(tokens);

        if (this.loadIncludes) {
            parser.setPointerResolver(this.resolver);
        }

        try {
            parser.label(label);
        } catch (RecognitionException ex) {
            label.setInvalid();
            throw new LabelParserException(ex, ex.line, ex.charPositionInLine,
                    ProblemType.INVALID_LABEL);
        }

        inputStream.close();

        label.setAttachedStartByte(customIs.getAttachedContentStartByte());
        label.setHasBlankFill(customIs.hasBlankFill());

        return label;
    }

    private List<SFDULabel> consumeSFDUHeader(InputStream input)
            throws IOException {
        List<SFDULabel> sfdus = new ArrayList<SFDULabel>();
        boolean foundHeader = false;

        byte[] sfduLabel = new byte[20];
        int count = input.read(sfduLabel);
        if (count == 20) {
            try {
                SFDULabel sfdu = new SFDULabel(sfduLabel);
                if ("CCSD".equals(sfdu.getControlAuthorityId())) { //$NON-NLS-1$
                    foundHeader = true;
                    sfdus.add(sfdu);
                    // Read in second SFDU label
                    input.read(sfduLabel);
                    sfdus.add(new SFDULabel(sfduLabel));
                    consumePDSNewline(input);
                }
            } catch (MalformedSFDULabel e) {
                // For now we can ignore this error as there is likely not a
                // header.
            }

        }

        if (!foundHeader) {
            input.reset();
        }

        return sfdus;
    }

    // consume up to 2 newline type characters to conform to the PDS newline
    // NOTE: will consume CR || LF || CRLF || LFCR... etc
    private void consumePDSNewline(final InputStream input) throws IOException {
        // consume CR if present
        consumeNewline(input);
        // consume LF if present
        consumeNewline(input);
        // marking location so not rewound to just prior to last newline char
        input.mark(100);
    }

    private void consumeNewline(final InputStream input) throws IOException {
        byte[] newline = new byte[1];
        input.mark(1);

        int count = input.read(newline);
        if (count == 1) {
            String nl1 = new String(newline);
            if (!(nl1.equals("\n") || nl1.equals("\r"))) { //$NON-NLS-1$//$NON-NLS-2$
                // did not find newline char, reset
                input.reset();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.nasa.jpl.pds.tools.label.parser.LabelParser#getODLVersion()
     */
    public String getODLVersion() {
        return VersionInfo.getODLVersion();
    }

    public Label parsePartial(final File file, final Label parent)
            throws IOException, LabelParserException {
        return parsePartial(file, parent, this.captureProblems);
    }

    public Label parsePartial(final File file, final Label parent,
            final boolean captureProbs) throws IOException,
            LabelParserException {
        return parsePartial(file, parent, captureProbs,
                this.allowExternalProblems);
    }

    public Label parsePartial(final File file, final Label parent,
            final boolean captureProbs, final boolean allowExternalProbs)
            throws IOException, LabelParserException {
        BufferedInputStream inputStream;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            inputStream.mark(100);
        } catch (FileNotFoundException e) {
            // TODO: handle exception more appropriately
            System.out.println(file.toString() + " not found."); //$NON-NLS-1$
            return null;
        }
        final Label label = new Label(file);
        label.setCaptureProblems(captureProbs);
        label.setAllowExternalProblems(allowExternalProbs);
        return parsePartial(inputStream, label, parent);
    }

    public Label parsePartial(final URL url, final Label parent)
            throws IOException, LabelParserException {
        return parsePartial(url, parent, this.captureProblems);
    }

    public Label parsePartial(final URL url, final Label parent,
            final boolean captureProbs) throws IOException,
            LabelParserException {
        return parsePartial(url, parent, captureProbs,
                this.allowExternalProblems);
    }

    public Label parsePartial(final URL url, final Label parent,
            final boolean captureProbs, final boolean allowExternalProbs)
            throws IOException, LabelParserException {
        final BufferedInputStream inputStream = new BufferedInputStream(url
                .openStream());
        inputStream.mark(100);
        URI labelURI = null;
        try {
            labelURI = url.toURI();
        } catch (URISyntaxException e) {
            throw new LabelParserException(
                    "bad url", ProblemType.INVALID_LABEL, //$NON-NLS-1$
                    url.getFile());
        }

        final Label label = new Label(labelURI);
        label.setCaptureProblems(captureProbs);
        label.setAllowExternalProblems(allowExternalProbs);
        return parsePartial(inputStream, label, parent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.nasa.pds.tools.label.parser.LabelParser#parsePartial(java.net.URL,
     *      boolean)
     */
    public Label parsePartial(final BufferedInputStream inputStream,
            final Label label, final Label parent) throws IOException,
            LabelParserException {

        // add include pointers to label to be able to test for circular
        // references
        if (parent != null) {
            label.addIncludePointers(parent.getIncludePointers());
        }

        List<SFDULabel> sfdus = consumeSFDUHeader(inputStream);
        int numConsumed = sfdus.size();

        // Now look for PDS_VERSION_ID to ensure that this is a file we want to
        // validate
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        String versionLine = null;

        do {
            versionLine = reader.readLine();
        } while (versionLine != null && versionLine.trim().length() == 0);

        String[] line = new String[] { "" }; //$NON-NLS-1$
        if (versionLine != null) {
            line = versionLine.trim().split("="); //$NON-NLS-1$
        }

        if (line.length == 2) {
            String name = line[0].trim();

            // Label fragments should not have PDS_VERSION_ID
            if ("PDS_VERSION_ID".equals(name)) { //$NON-NLS-1$
                label
                        .addProblem(new LabelParserException(label, null,
                                null,
                                "parser.warning.versionPresent", //$NON-NLS-1$
                                ProblemType.FRAGMENT_HAS_VERSION,
                                getDisplayPath(label)));
            }
        }

        inputStream.reset();

        if (numConsumed != 0) {
            // TODO: when pds utils library updated, use getRelativePath(String,
            // String)
            label.addProblem(new LabelParserException(label, null, null,
                    "parser.warning.sfduPresent", //$NON-NLS-1$
                    ProblemType.FRAGMENT_HAS_SFDU, getDisplayPath(label)));
        }

        return parseLabel(inputStream, label);
    }

    // This function returns null if there if no relative path can be found.
    private String getRelativePath(final Label label) {
        String relativePath = null;
        if (label.getLabelFile() != null) {
            try {
                relativePath = FileUtils.getRelativePath(this.resolver
                        .getBaseFile(), label.getLabelFile());
            } catch (RuntimeException re) {
                // If the files don't share a path
                // no op
            }
        } else {
            try {
                relativePath = FileUtils.getRelativePath(this.resolver
                        .getBaseURI().toURL(), label.getLabelURI().toURL());
            } catch (MalformedURLException e) {
                // noop
            } catch (RuntimeException re) {
                // If the files don't share a path
                // no op
            }
        }
        return relativePath;
    }

    private String getDisplayPath(final Label label) {
        String displayPath = getRelativePath(label);

        if (displayPath == null) {
            if (label.getLabelFile() != null) {
                displayPath = label.getLabelFile().getPath();
            } else {
                displayPath = label.getLabelURI().getPath();
            }
        }
        return displayPath;
    }

    public static void main(String[] args) throws Exception {
        ConsoleAppender console = new ConsoleAppender(new PatternLayout(
                "%-5p %m%n")); //$NON-NLS-1$
        console.setThreshold(Level.FATAL);
        BasicConfigurator.configure(console);
        ManualPathResolver resolver = new ManualPathResolver();
        URL labelURL = new URL(args[0]);
        resolver.setBaseURI(ManualPathResolver.getBaseURI(labelURL.toURI()));
        DefaultLabelParser parser = new DefaultLabelParser(true, true, true,
                resolver);
        Label label = parser.parseLabel(labelURL, true);
        Validator validator = new Validator();
        validator.validate(label);
        System.out.println("Found " + label.getProblems().size() //$NON-NLS-1$
                + " problem(s):"); //$NON-NLS-1$
        for (LabelParserException problem : label.getProblems()) {
            if (problem.getLineNumber() != null) {
                System.out.print("Line " + problem.getLineNumber()); //$NON-NLS-1$
                if (problem.getColumn() != null) {
                    System.out.print(", " + problem.getColumn()); //$NON-NLS-1$
                }
                System.out.print(": "); //$NON-NLS-1$
            }
            System.out.println(MessageUtils.getProblemMessage(problem));
        }
    }
}
