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

import gov.nasa.arc.pds.tools.util.StrUtils;
import gov.nasa.pds.tools.containers.FileReference;
import gov.nasa.pds.tools.dict.DictIdentifier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class is the object representation of a pointer statement in a label.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class PointerStatement extends Statement {

    protected Value value;

    protected boolean externalReference;

    private List<FileReference> fileRefs;

    /**
     * Constructs essentially a null pointer
     * 
     * @param pointerType
     *            indicates whether it is data location, include, or description
     *            pointer
     * @param lineNumber
     *            at which the statement occurs
     * @param identifier
     *            of the statement
     */
    protected PointerStatement(final Label sourceLabel, int lineNumber,
            DictIdentifier identifier) {
        this(sourceLabel, lineNumber, identifier, null);
    }

    /**
     * Constructs a pointer with a value on the right hand side
     * 
     * @param pointerType
     *            indicates whether it is data location, include, or description
     *            pointer
     * @param lineNumber
     *            at which the statement occurs
     * @param identifier
     *            of the statement
     * @param value
     *            of the assignment
     */
    protected PointerStatement(final Label sourceLabel, int lineNumber,
            DictIdentifier identifier, Value value) {
        super(sourceLabel, lineNumber, identifier);
        this.value = value;
        if (value instanceof TextString || value instanceof Sequence) {
            this.externalReference = true;
        }
    }

    /**
     * Returns the value portion (right hand side) of the statement.
     * 
     * @return value
     */
    public Value getValue() {
        return this.value;
    }

    public boolean hasMultipleReferences() {
        if (this.value instanceof Set) {
            return true;
        }
        return false;
    }

    public List<FileReference> getFileRefs() {
        if (this.fileRefs == null) {
            this.fileRefs = new ArrayList<FileReference>();
            if (this.value instanceof Set) {
                Iterator<Scalar> it = ((Set) this.value).iterator();
                while (it.hasNext()) {
                    final String fileName = it.next().toString();
                    FileReference fileRef = new FileReference(fileName,
                            getLineNumber(), getIdentifier());
                    this.fileRefs.add(fileRef);
                }
            } else if (this.value instanceof Sequence) {
                final Sequence seqValue = (Sequence) this.value;
                final String fileName = seqValue.get(0).toString();
                // get position in file
                Value startVal = null;
                try {
                    startVal = seqValue.get(1);
                } catch (Exception e) {
                    // TODO: bubble up some sort of error that this is a bad
                    // pointer
                }
                FileReference fileRef = null;
                if (startVal instanceof Numeric) {
                    fileRef = new FileReference(fileName, getLineNumber(),
                            getIdentifier(), (Numeric) startVal);
                } else {
                    fileRef = new FileReference(fileName, getLineNumber(),
                            getIdentifier());
                }
                this.fileRefs.add(fileRef);
            } else if (this.value instanceof Numeric) {
                FileReference fileRef = null;
                if (this.sourceFile != null) {
                    fileRef = new FileReference(this.sourceFile.getName(),
                            getLineNumber(), getIdentifier(),
                            (Numeric) this.value);
                } else {
                    fileRef = new FileReference(
                            StrUtils.getURIFilename(this.sourceURI),
                            getLineNumber(), getIdentifier(),
                            (Numeric) this.value);
                }
                this.fileRefs.add(fileRef);
            } else {
                final String fileName = this.value.toString();
                FileReference fileRef = new FileReference(fileName,
                        getLineNumber(), getIdentifier());
                this.fileRefs.add(fileRef);
            }
        }
        return this.fileRefs;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if ((object == null) || (object.getClass() != this.getClass())) {
            return false;
        }
        PointerStatement ps = (PointerStatement) object;
        if (value == null) {
            return (identifier == ps.identifier || (identifier != null && identifier
                    .equals(ps.identifier)));
        } else {
            return (identifier == ps.identifier || (identifier != null && identifier
                    .equals(ps.identifier)))
                    && (value == ps.value || (value != null && value.toString()
                            .equals(ps.value.toString())));
        }
    }

    public int hashcode() {
        int hash = 7;

        hash = 31 * hash + (null == identifier ? 0 : identifier.hashCode());
        if (value != null)
            hash = 31
                    * hash
                    + (null == value.toString() ? 0 : value.toString()
                            .hashCode());

        return hash;
    }

}
