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

package gov.nasa.pds.tools.dict.type;

import gov.nasa.pds.tools.label.AttributeStatement;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class TimeChecker extends LengthChecker implements TypeChecker {

    /*
     * (non-Javadoc)
     * 
     * @see gov.nasa.jpl.pds.tools.dict.type.TypeChecker#cast(java.lang.String)
     */
    public Object cast(String value, AttributeStatement attribute)
            throws InvalidTypeException {
        // FIXME: return Time converted from value
        return value;
    }

}
