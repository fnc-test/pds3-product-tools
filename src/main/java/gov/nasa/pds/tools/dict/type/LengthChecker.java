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

import gov.nasa.arc.pds.tools.util.StrUtils;
import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.dict.ElementDefinition;
import gov.nasa.pds.tools.label.AttributeStatement;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class LengthChecker {

    /*
     * (non-Javadoc)
     * 
     * @see gov.nasa.jpl.pds.tools.label.validate.TypeChecker#checkMinLength(java
     *      .lang.String, int)
     */
    public void checkMinLength(String value, ElementDefinition def,
            AttributeStatement attribute) throws InvalidLengthException {
        final int min = def.getMinLength();
        final String compressedVal = StrUtils.stripPadding(value);
        if (compressedVal.length() < min) {
            throw new InvalidLengthException(attribute,
                    "parser.error.tooShort", ProblemType.SHORT_VALUE, value, //$NON-NLS-1$
                    compressedVal.length(), min, def.getIdentifier(), def
                            .getDataType());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.nasa.jpl.pds.tools.label.validate.TypeChecker#checkMaxLength(java
     *      .lang.String, int)
     */
    public void checkMaxLength(String value, ElementDefinition def,
            AttributeStatement attribute) throws InvalidLengthException {
        final int max = def.getMaxLength();
        final String compressedVal = StrUtils.stripPadding(value);
        int cutOffLength = 65;
        if (compressedVal.length() > max) {
            String truncatedVal = null;
            // Perform truncation so that a really long value does not have to
            // be thrown in its entirety into the error message.
            if (compressedVal.length() > cutOffLength) {
                boolean done = false;
                // If the truncation lands in the middle of the word, capture
                // the rest of the word
                while (!done) {
                    try {
                        if (Character.isLetterOrDigit(compressedVal
                                .charAt(cutOffLength)))
                            ++cutOffLength;
                        else {
                            done = true;
                            truncatedVal = compressedVal.substring(0,
                                    cutOffLength)
                                    + "..."; //$NON-NLS-1$
                        }
                    } catch (IndexOutOfBoundsException e) {
                        // We've reached the end of the string value
                        done = true;
                        truncatedVal = compressedVal;
                    }
                }
            } else {
                truncatedVal = compressedVal;
            }
            throw new InvalidLengthException(attribute,
                    "parser.error.tooLong", ProblemType.EXCESSIVE_VALUE_LENGTH, //$NON-NLS-1$
                    truncatedVal, compressedVal.length(), max, def
                            .getIdentifier(), def.getDataType());
        }
    }
}
