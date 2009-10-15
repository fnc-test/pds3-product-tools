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

import gov.nasa.pds.tools.constants.Constants;
import gov.nasa.pds.tools.dict.ElementDefinition;
import gov.nasa.pds.tools.label.AttributeStatement;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class RealChecker extends LengthChecker implements NumericTypeChecker {

    /*
     * (non-Javadoc)
     * 
     * @see
     * gov.nasa.jpl.pds.tools.label.validate.TypeChecker#cast(java.lang.String)
     */
    public Object cast(String value, AttributeStatement attribute)
            throws InvalidTypeException {
        if (!value.matches(Constants.ASCII_REAL_REGEX)) {
            throw new InvalidTypeException(attribute,
                    "parser.error.badReal", value); //$NON-NLS-1$
        }
        Double doubleValue = null;
        try {
            doubleValue = Double.valueOf(value);
        } catch (NumberFormatException nfe) {
            // noop, already tested
        }
        return doubleValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * gov.nasa.pds.tools.dict.type.NumericTypeChecker#checkMinValue(java.lang
     * .Number, java.lang.Number)
     */
    public void checkMinValue(Number value, ElementDefinition def,
            AttributeStatement attribute) throws OutOfRangeException {
        final Number min = def.getMinimum();
        if (value.doubleValue() < min.doubleValue())
            throw new OutOfRangeException(attribute, value, min, false, def
                    .getIdentifier(), def.getDataType());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * gov.nasa.pds.tools.dict.type.NumericTypeChecker#checkMaxValue(java.lang
     * .Number, java.lang.Number)
     */
    public void checkMaxValue(Number value, ElementDefinition def,
            AttributeStatement attribute) throws OutOfRangeException {
        final Number max = def.getMaximum();
        if (value.doubleValue() > max.doubleValue())
            throw new OutOfRangeException(attribute, value, max, true, def
                    .getIdentifier(), def.getDataType());
    }

}
