// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// • Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// • Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// • Neither the name of Caltech nor its operating division, the Jet Propulsion
// Laboratory, nor the names of its contributors may be used to endorse or
// promote products derived from this software without specific prior written
// permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

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
            if (!parentLabel.hasAncestor(foundFile)) {
                LabelParser parser = null;
                if (parentLabel.getAllowExternalProblems()) {
                    parser = new DefaultLabelParser(true, parentLabel
                            .getCaptureProblems(), true, resolver);
                } else {
                    parser = new DefaultLabelParser(true, false, false,
                            resolver);
                }
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
                if (!parentLabel.hasAncestor(foundFile)) {
                    LabelParser parser = null;
                    if (parentLabel.getAllowExternalProblems()) {
                        parser = new DefaultLabelParser(true, parentLabel
                                .getCaptureProblems(), true, resolver);
                    } else {
                        parser = new DefaultLabelParser(true, false, false,
                                resolver);
                    }
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
