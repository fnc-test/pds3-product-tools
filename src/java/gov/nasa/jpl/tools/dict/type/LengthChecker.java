// Copyright 2006, by the California Institute of 
// Technology. ALL RIGHTS RESERVED. United States Government 
// Sponsorship acknowledged. Any commercial use must be negotiated with 
// the Office of Technology Transfer at the California Institute of 
// Technology.
//
// This software may be subject to U.S. export control laws. By 
// accepting this software, the user agrees to comply with all 
// applicable U.S. export laws and regulations. User has the 
// responsibility to obtain export licenses, or other export authority 
// as may be required before exporting such information to foreign 
// countries or providing access to foreign persons.
//
// $Id$ 
//

package gov.nasa.pds.tools.dict.type;

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
            throw new InvalidLengthException(value + " is less than the acceptable minimum length of " + min);
    }
    
    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.validate.TypeChecker#checkMaxLength(java.lang.String, int)
     */
    public void checkMaxLength(String value, int max) throws InvalidLengthException {
        if (value.length() > max)
            throw new InvalidLengthException(value + " exceed the maximum length of " + max);
    }
}
