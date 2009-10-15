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
import gov.nasa.pds.tools.label.PointerResolver;
import gov.nasa.pds.tools.label.SFDULabel;
import gov.nasa.pds.tools.label.antlr.ODLLexer;
import gov.nasa.pds.tools.label.antlr.ODLParser;

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

    private final PointerResolver resolver;

    // default constructor, assumes you want to load included statements and
    // capture parse errors
    public DefaultLabelParser(final PointerResolver resolver) {
        this(true, true, resolver);
    }

    public DefaultLabelParser(final boolean loadIncludes,
            final boolean captureProblems, final PointerResolver resolver) {
        this.loadIncludes = loadIncludes;
        this.captureProblems = captureProblems;
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
        return parseLabel(inputStream, label, forceParse);
    }

    private Label parseLabel(final BufferedInputStream inputStream,
            final Label label, final boolean forceParse)
            throws LabelParserException, IOException {

        List<SFDULabel> sfdus = consumeSFDUHeader(inputStream);
        int numConsumed = sfdus.size();

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

        parseLabel(inputStream, label, numConsumed);

        // this is a label so it should end in END
        if (!label.hasEndStatement()) {
            label.addProblem(new CommentStatement(label, 1),
                    "parser.error.missingEndStatement", //$NON-NLS-1$
                    ProblemType.PARSE_ERROR);
        }
        return label;
    }

    private Label parseLabel(final BufferedInputStream inputStream,
            final Label label, final int sfdusConsumed)
            throws LabelParserException, IOException {

        // Skip 20 bytes per header consumed and 2 more bytes for carriage
        // return
        // TODO: deal with case where EOL is not two chars
        if (sfdusConsumed != 0) {
            inputStream.skip(sfdusConsumed * 20 + 2);
        }

        CharStream antlrInput = new ANTLRInputStream(inputStream);

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

        label.setAttachedStartByte(lexer.getAttachedContentStartByte());
        label.setHasBlankFill(lexer.hasBlankFill());

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

    /*
     * (non-Javadoc)
     * 
     * @see gov.nasa.jpl.pds.tools.label.parser.LabelParser#getPDSVersion()
     */
    public String getPDSVersion() {
        return "PDS3"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.nasa.jpl.pds.tools.label.parser.LabelParser#getODLVersion()
     */
    public String getODLVersion() {
        return "2.1"; //$NON-NLS-1$
    }

    public Label parsePartial(final File file, final Label parent)
            throws IOException, LabelParserException {
        return parsePartial(file, parent, this.captureProblems);
    }

    public Label parsePartial(final File file, final Label parent,
            final boolean captureProbs) throws IOException,
            LabelParserException {
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
        return parsePartial(inputStream, label, parent);
    }

    public Label parsePartial(final URL url, final Label parent)
            throws IOException, LabelParserException {
        return parsePartial(url, parent, this.captureProblems);
    }

    public Label parsePartial(final URL url, final Label parent,
            final boolean captureProbs) throws IOException,
            LabelParserException {
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
        return parsePartial(inputStream, label, parent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * gov.nasa.pds.tools.label.parser.LabelParser#parsePartial(java.net.URL,
     * boolean)
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
                label.addProblem(new LabelParserException(label, null,
                        null,
                        "parser.warning.versionPresent", //$NON-NLS-1$
                        ProblemType.FRAGMENT_HAS_VERSION,
                        getRelativePath(label)));
            }
        }

        inputStream.reset();

        if (numConsumed != 0) {
            // TODO: when pds utils library updated, use getRelativePath(String,
            // String)
            label.addProblem(new LabelParserException(label, null, null,
                    "parser.warning.sfduPresent", //$NON-NLS-1$
                    ProblemType.FRAGMENT_HAS_SFDU, getRelativePath(label)));
        }

        return parseLabel(inputStream, label, numConsumed);
    }

    private String getRelativePath(final Label label) {
        String relativePath = ""; //$NON-NLS-1$
        if (label.getLabelFile() != null) {
            relativePath = FileUtils.getRelativePath(this.resolver
                    .getBaseFile(), label.getLabelFile());
        } else {
            try {
                relativePath = FileUtils.getRelativePath(this.resolver
                        .getBaseURI().toURL(), label.getLabelURI().toURL());
            } catch (MalformedURLException e) {
                // noop
            }
        }
        return relativePath;
    }
}
