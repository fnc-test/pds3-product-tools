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
