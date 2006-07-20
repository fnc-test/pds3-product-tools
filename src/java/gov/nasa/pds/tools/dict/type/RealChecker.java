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
public class RealChecker extends LengthChecker implements NumericTypeChecker {

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.validate.TypeChecker#cast(java.lang.String)
     */
    public Object cast(String value) throws InvalidTypeException {
        // FIXME: return Real value of string
        return value;
    }

    /* (non-Javadoc)
     * @see gov.nasa.pds.tools.dict.type.NumericTypeChecker#checkMinValue(java.lang.Number, java.lang.Number)
     */
    public void checkMinValue(Number value, Number min) throws OutOfRangeException {
        if (value.longValue() < min.longValue())
            throw new OutOfRangeException(value.toString() + " is less than the acceptable minimum of " + min.toString());
    }

    /* (non-Javadoc)
     * @see gov.nasa.pds.tools.dict.type.NumericTypeChecker#checkMaxValue(java.lang.Number, java.lang.Number)
     */
    public void checkMaxValue(Number value, Number max) throws OutOfRangeException {
        if (value.longValue() > max.longValue())
            throw new OutOfRangeException(value.toString() + " exceeds acceptable maximum of " + max.toString());
    }

}
