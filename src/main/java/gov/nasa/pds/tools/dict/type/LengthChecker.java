// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// • Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// • Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// • Neither the name of Caltech nor its operating division, the Jet Propulsion
// Laboratory, nor the names of its contributors may be used to endorse or
// promote products derived from this software without specific prior written
// permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

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
        int cutOffLength = 64;
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
