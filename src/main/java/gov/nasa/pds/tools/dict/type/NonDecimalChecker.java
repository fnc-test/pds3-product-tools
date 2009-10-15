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

import java.util.regex.Matcher;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class NonDecimalChecker extends LengthChecker implements
        NumericTypeChecker {

    /*
     * (non-Javadoc)
     * 
     * @see gov.nasa.jpl.pds.tools.dict.type.TypeChecker#cast(java.lang.String)
     */
    @SuppressWarnings("nls")
    public Object cast(String value, AttributeStatement attribute)
            throws InvalidTypeException {

        String startVal = value != null ? value.trim() : "";
        final Matcher matcher = Constants.NON_DECIMAL_PATTERN.matcher(startVal);

        if (!matcher.matches()) {
            throw new InvalidTypeException(attribute,
                    "parser.error.badNonDecimal", value); //$NON-NLS-1$
        }

        // get parts of match
        final String radixStr = matcher.group(1);
        final String sign = matcher.group(2);
        final String number = matcher.group(3);

        // despite data dictionary document, signs are illegal
        if (!sign.equals("")) {
            throw new InvalidTypeException(attribute,
                    "parser.error.signedNonDecimal", value); //$NON-NLS-1$
        }

        // check for valid radix
        if (!(radixStr.equals("2") || radixStr.equals("8") || radixStr
                .equals("16"))) {
            throw new InvalidTypeException(attribute,
                    "parser.error.badNonDecimalRadix", value, radixStr); //$NON-NLS-1$
        }

        // boolean isNegative = sign.equals("-");
        Long longValue = null;

        try {
            int radix = Integer.parseInt(radixStr);

            longValue = Long.valueOf(number, radix);
            /*
             * if (isNegative) { longValue = longValue * -1; }
             */
        } catch (NumberFormatException nfe) {
            throw new InvalidTypeException(attribute,
                    "parser.error.badNonDecimal", value);
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
