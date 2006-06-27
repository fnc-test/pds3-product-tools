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
public class RealChecker implements NumericTypeChecker {

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.validate.NumericTypeChecker#checkMinValue(java.lang.String, java.lang.String)
     */
    public void checkMinValue(String value, String min)
            throws OutOfRangeException, InvalidTypeException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.validate.NumericTypeChecker#checkMaxValue(java.lang.String, java.lang.String)
     */
    public void checkMaxValue(String value, String max)
            throws OutOfRangeException, InvalidTypeException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.validate.TypeChecker#cast(java.lang.String)
     */
    public Object cast(String value) throws InvalidTypeException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.validate.TypeChecker#checkMinLength(java.lang.String, int)
     */
    public void checkMinLength(String value, int min)
            throws InvalidLengthException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.validate.TypeChecker#checkMaxLength(java.lang.String, int)
     */
    public void checkMaxLength(String value, int max)
            throws InvalidLengthException {
        // TODO Auto-generated method stub

    }

}
