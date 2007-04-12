// Copyright 2006-2007, by the California Institute of Technology.
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

import gov.nasa.pds.tools.util.Utility;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class LengthChecker {
    
    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.validate.TypeChecker#checkMinLength(java.lang.String, int)
     */
    public void checkMinLength(String value, int min) throws InvalidLengthException {
        if (value.length() < min)
            throw new InvalidLengthException(Utility.trimString(value, 40) + " is less than the acceptable minimum length of " + min);
    }
    
    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.validate.TypeChecker#checkMaxLength(java.lang.String, int)
     */
    public void checkMaxLength(String value, int max) throws InvalidLengthException {
        if (value.length() > max && Utility.stripWhitespace(value).length() > max) {
            throw new InvalidLengthException(Utility.trimString(value, 40) + " exceeds the maximum length of " + max);
        }
    }
}
