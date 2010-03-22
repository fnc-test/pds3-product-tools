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

package gov.nasa.pds.tools.label;

import gov.nasa.arc.pds.tools.util.FileUtils;
import gov.nasa.arc.pds.tools.util.URLUtils;
import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.dict.DictIdentifier;
import gov.nasa.pds.tools.label.parser.DefaultLabelParser;
import gov.nasa.pds.tools.label.parser.LabelParser;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
@SuppressWarnings("nls")
public abstract class IncludePointer extends SpecialPointer {

    private final List<Statement> statements = new ArrayList<Statement>();

    private boolean loaded = false;

    public IncludePointer(final Label sourceLabel, final int lineNumber,
            final DictIdentifier identifier, final Value value) {
        super(sourceLabel, lineNumber, identifier, value);
    }

    public synchronized void loadReferencedStatements(final Label parentLabel,
            final PointerResolver resolver) throws LabelParserException,
            IOException {
        if (!this.loaded) {
            if (this.sourceFile != null) {
                loadByFiles(parentLabel, resolver);
            } else {
                loadByURLs(parentLabel, resolver);
            }
            this.loaded = true;
        }
    }

    private void loadByFiles(final Label parentLabel,
            final PointerResolver resolver) throws LabelParserException,
            IOException {
        Map<Numeric, File> files = resolver.resolveFileMap(this);
        final Iterator<Entry<Numeric, File>> it = files.entrySet().iterator();
        while (it.hasNext()) {
            final Entry<Numeric, File> entry = it.next();
            final File foundFile = entry.getValue();
            // don't attempt to parse missing files, errors handled elsewhere
            if (!foundFile.exists()) {
                // System.out.println(foundFile.toString() +
                // " not found for include");
                continue;
            }
            // final Numeric start = entry.getKey();
            // TODO: make sure file is not in the stack of files pointed
            // from check if in labels list
            if (!parentLabel.hasIncludePointer(foundFile)) {
                parentLabel.addIncludePointer(foundFile);
                LabelParser parser = new DefaultLabelParser(true, parentLabel
                        .getCaptureProblems(), parentLabel
                        .getAllowExternalProblems(), resolver);
                // pass label into parsePartial
                Label partial = parser.parsePartial(foundFile, parentLabel);
                synchLabels(partial, parentLabel);
            } else {
                final String targetPath = FileUtils.getRelativePath(resolver
                        .getBaseFile(), foundFile);
                final String currentPath = FileUtils.getRelativePath(resolver
                        .getBaseFile(), this.sourceFile);
                parentLabel.addProblem(this, "parser.error.circularReference",
                        ProblemType.CIRCULAR_POINTER_REF, currentPath,
                        targetPath);
            }
        }
    }

    private void loadByURLs(final Label parentLabel,
            final PointerResolver resolver) throws LabelParserException,
            IOException {
        // init files
        Map<Numeric, URI> uris = resolver.resolveURIMap(this);

        // try to load by uris
        if (uris.size() > 0) {
            final Iterator<Entry<Numeric, URI>> it = uris.entrySet().iterator();
            while (it.hasNext()) {
                final Entry<Numeric, URI> entry = it.next();
                final URI foundFile = entry.getValue();
                // don't attempt to parse missing files, errors handled
                // elsewhere
                if (!URLUtils.exists(foundFile)) {
                    continue;
                }
                // final Numeric start = entry.getKey();
                // TODO: make sure file is not in the stack of files pointed
                // from check if in labels list
                if (!parentLabel.hasIncludePointer(foundFile)) {
                    parentLabel.addIncludePointer(foundFile);
                    LabelParser parser = new DefaultLabelParser(true,
                            parentLabel.getCaptureProblems(), parentLabel
                                    .getAllowExternalProblems(), resolver);
                    // pass label into parsePartial
                    Label partial = parser.parsePartial(foundFile.toURL(),
                            parentLabel);
                    synchLabels(partial, parentLabel);
                } else {
                    final String targetPath = FileUtils.getRelativePath(
                            resolver.getBaseURI().toURL(), foundFile.toURL());
                    final String currentPath = FileUtils.getRelativePath(
                            resolver.getBaseURI().toURL(), this.sourceURI
                                    .toURL());
                    parentLabel.addProblem(this,
                            "parser.error.circularReference",
                            ProblemType.CIRCULAR_POINTER_REF, currentPath,
                            targetPath);
                }
            }
        }
    }

    private void synchLabels(final Label partial, final Label parent) {
        this.statements.addAll(partial.getStatements());
        // include exempted problems - currently only circular ref
        if (parent != null) {
            for (final LabelParserException e : partial.getProblems()) {
                parent.addProblem(e);
            }
        }
    }

    public List<Statement> getStatements() {
        return this.statements;
    }
}
