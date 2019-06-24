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

import java.util.regex.Matcher;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class NonDecimalChecker extends LengthChecker implements NumericTypeChecker {

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nasa.jpl.pds.tools.dict.type.TypeChecker#cast(java.lang.String)
	 */
	@SuppressWarnings("nls")
	public Object cast(String value, AttributeStatement attribute) throws InvalidTypeException {

		String startVal = value != null ? value.trim() : "";
		final Matcher matcher = Constants.NON_DECIMAL_PATTERN.matcher(startVal);

		if (!matcher.matches()) {
			throw new InvalidTypeException(attribute, "parser.error.badNonDecimal", value); //$NON-NLS-1$
		}

		// get parts of match
		final String radixStr = matcher.group(1);
		final String sign = matcher.group(2);
		final String number = matcher.group(3);

		// despite data dictionary document, signs are illegal
		if (!sign.equals("")) {
			throw new InvalidTypeException(attribute, "parser.error.signedNonDecimal", value); //$NON-NLS-1$
		}

		// check for valid radix
		if (!(radixStr.equals("2") || radixStr.equals("8") || radixStr.equals("16"))) {
			throw new InvalidTypeException(attribute, "parser.error.badNonDecimalRadix", value, radixStr); //$NON-NLS-1$
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
			throw new InvalidTypeException(attribute, "parser.error.badNonDecimal", value);
		}
		return longValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nasa.pds.tools.dict.type.NumericTypeChecker#checkMinValue(java.lang
	 * .Number, java.lang.Number)
	 */
	public void checkMinValue(Number value, ElementDefinition def, AttributeStatement attribute)
			throws OutOfRangeException {
		final Number min = def.getMinimum();
		if (value.doubleValue() < min.doubleValue())
			throw new OutOfRangeException(attribute, value, min, false, def.getIdentifier(), def.getDataType());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nasa.pds.tools.dict.type.NumericTypeChecker#checkMaxValue(java.lang
	 * .Number, java.lang.Number)
	 */
	public void checkMaxValue(Number value, ElementDefinition def, AttributeStatement attribute)
			throws OutOfRangeException {
		final Number max = def.getMaximum();
		if (value.doubleValue() > max.doubleValue())
			throw new OutOfRangeException(attribute, value, max, true, def.getIdentifier(), def.getDataType());
	}

}
