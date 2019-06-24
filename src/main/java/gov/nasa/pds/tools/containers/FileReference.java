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

package gov.nasa.pds.tools.containers;

import gov.nasa.pds.tools.dict.DictIdentifier;
import gov.nasa.pds.tools.label.Numeric;

/**
 * This class represents a reference to a file in a label
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class FileReference {

    private final String path;

    private final int lineNumber;

    private final DictIdentifier identifier;

    // TODO: determine if there's a need for parent
    private final String parent;

    private final Numeric startPosition;

    public FileReference(final String path, final int lineNumber) {
        this(path, lineNumber, null, null, null);
    }

    public FileReference(final String path, final int lineNumber,
            final DictIdentifier identifier) {
        this(path, lineNumber, identifier, null, null);
    }

    public FileReference(final String path, final int lineNumber,
            final DictIdentifier identifier, final Numeric startPosition) {
        this(path, lineNumber, identifier, null, startPosition);
    }

    public FileReference(final String path, final int lineNumber,
            final DictIdentifier identifier, final String parent,
            final Numeric startPosition) {
        this.path = path;
        this.lineNumber = lineNumber;
        this.identifier = identifier;
        this.parent = parent;
        this.startPosition = startPosition == null ? new Numeric("0") : startPosition; //$NON-NLS-1$
    }

    public String getPath() {
        return this.path;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public DictIdentifier getIdentifier() {
        return this.identifier;
    }

    public String getParent() {
        return this.parent;
    }

    public Numeric getStartPosition() {
        return this.startPosition;
    }
}
