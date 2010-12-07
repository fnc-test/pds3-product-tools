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
