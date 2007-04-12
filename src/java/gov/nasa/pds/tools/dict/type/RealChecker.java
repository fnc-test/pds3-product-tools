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
        Double doubleValue = null;
        try {
            doubleValue = Double.valueOf(value);
        } catch(NumberFormatException nfe) {
            throw new InvalidTypeException(nfe.getMessage());
        }
        return doubleValue;
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
