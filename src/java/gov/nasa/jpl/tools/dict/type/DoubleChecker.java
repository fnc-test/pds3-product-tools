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
public class DoubleChecker extends LengthChecker implements NumericTypeChecker {

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.validate.NumericTypeChecker#checkMinValue(java.lang.String, java.lang.String)
     */
    public void checkMinValue(String value, String min) 
            throws OutOfRangeException, InvalidTypeException {
        try{
            double minValue = Double.parseDouble(min);
            double doubleValue = Double.parseDouble(value);
            if (doubleValue < minValue) 
                throw new OutOfRangeException(value + " is below the accepted minimum " + min);
        } catch (NumberFormatException nfe) {
            throw new InvalidTypeException ("Either the value " + value + " or " + min + " is not the correct type.");
        }
    }

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.validate.NumericTypeChecker#checkMaxValue(java.lang.String, java.lang.String)
     */
    public void checkMaxValue(String value, String max) 
            throws OutOfRangeException, InvalidTypeException {
        try{
            double maxValue = Double.parseDouble(max);
            double doubleValue = Double.parseDouble(value);
            if (doubleValue > maxValue) 
                throw new OutOfRangeException(value + " exceeds maximum of " + max);
        } catch (NumberFormatException nfe) {
            throw new InvalidTypeException ("Either the value " + value + " or " + max + " is not the correct type.");
        }
    }

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.validate.TypeChecker#cast(java.lang.String)
     */
    public Object cast(String value) throws InvalidTypeException {
        Double doubleValue = null;
        try {
            doubleValue = Double.valueOf(value);
        } catch(NumberFormatException nfe) {
            throw new InvalidTypeException(nfe.getMessage());
        }
        return doubleValue;
    }

}
