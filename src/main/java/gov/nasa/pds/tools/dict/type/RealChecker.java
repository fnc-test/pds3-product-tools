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
