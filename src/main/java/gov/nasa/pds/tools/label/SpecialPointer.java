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

import gov.nasa.pds.tools.dict.DictIdentifier;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public abstract class SpecialPointer extends PointerStatement {

    public static String[] DESCRIPTION_KEY = {
            "DESCRIPTION", "DESC", "TEXT", "NOTE" }; //$NON-NLS-1$ //$NON-NLS-2$

    public static String[] STRUCTURE_KEY = { "STRUCTURE" }; //$NON-NLS-1$

    public static String[] INDEX_KEY = { "INDEX_TABLE" }; //$NON-NLS-1$

    public static String[] CATALOG_KEY = { "CATALOG", "DATA_SET_MAP_PROJECTION" }; //$NON-NLS-1$ //$NON-NLS-2$

    public SpecialPointer(final Label sourceLabel, final int lineNumber,
            final DictIdentifier identifier, final Value value) {
        super(sourceLabel, lineNumber, identifier, value);
    }
}
