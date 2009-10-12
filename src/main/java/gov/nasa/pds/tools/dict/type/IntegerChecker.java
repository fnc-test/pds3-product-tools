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
public class IntegerChecker extends LengthChecker implements NumericTypeChecker {

    /*
     * (non-Javadoc)
     * 
     * @see
     * gov.nasa.jpl.pds.tools.label.validate.TypeChecker#cast(java.lang.String)
     */
    public Object cast(String value, AttributeStatement attribute)
            throws InvalidTypeException {
        if (!value.matches(Constants.ASCII_INTEGER_REGEX)) {
            throw new InvalidTypeException(attribute,
                    "parser.error.badInteger", value); //$NON-NLS-1$
        }
        Long longValue = null;
        try {
            longValue = Long.valueOf(value);
        } catch (NumberFormatException nfe) {
            // TODO: this is probably a valid integer but out of range for
            // java... leave undocumented limit in test or use Long?
            throw new InvalidTypeException(attribute,
                    "parser.error.badInteger", value); //$NON-NLS-1$
        }
        return longValue;
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
        if (value.intValue() < min.intValue()) {
            throw new OutOfRangeException(attribute, value.toString(), min,
                    false, def.getIdentifier(), def.getDataType());
        }
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
        if (value.intValue() > max.intValue()) {
            throw new OutOfRangeException(attribute, value.toString(), max,
                    true, def.getIdentifier(), def.getDataType());
        }
    }

}
