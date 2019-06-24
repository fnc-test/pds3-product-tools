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

package gov.nasa.pds.tools.label;

import gov.nasa.arc.pds.tools.util.StrUtils;
import gov.nasa.pds.tools.constants.Constants.DictionaryType;

/**
 * This class represents a numeric scalar. It is not type specific so everything
 * is represented as a String.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class Numeric extends Scalar {
    // TODO: map to units in dictionary?
    private String units;

    private int radix;

    /**
     * Constructs a Numeric with the given value
     * 
     * @param value
     *            of numeric
     */
    public Numeric(String value) {
        this(value, null, 10);
    }

    /**
     * Constructs a Numeric with the given value and units
     * 
     * @param value
     *            of numeric
     * @param units
     *            of numeric
     */
    public Numeric(String value, String units) {
        this(value, units, 10);
    }

    /**
     * Constructs a Numeric with the given value and radix
     * 
     * @param value
     *            of numeric
     * @param radix
     *            of numeric
     */
    public Numeric(String value, int radix) {
        this(value, null, radix);
    }

    /**
     * Constructs a Numeric with the given value, units and radix
     * 
     * @param value
     *            of numeric
     * @param units
     *            of numeric
     * @param radix
     *            of numeric
     */
    public Numeric(String value, String units, int radix) {
        super(value);
        this.units = units;
        this.radix = radix;
    }

    /**
     * Sets the units
     * 
     * @param units
     *            of the numeric
     */
    public void setUnits(String units) {
        // Remove angle brackets if found
        if (units.startsWith("<") && units.endsWith(">")) //$NON-NLS-1$//$NON-NLS-2$
            this.units = units.substring(1, units.length() - 1).trim();
        else
            this.units = units.trim();
    }

    /**
     * Retrieves the units
     * 
     * @return units
     */
    public String getUnits() {
        return this.units;
    }

    /**
     * Sets the radix
     * 
     * @param radix
     *            of the numeric
     */
    public void setRadix(int radix) {
        this.radix = radix;
    }

    /**
     * Retrieves the base
     * 
     * @return base
     */
    public int getRadix() {
        return this.radix;
    }

    public String normalize() {
        return StrUtils.normalize(this.toString());
    }

    @Override
    public boolean isSupportedPDSType(DictionaryType type) {
        if (DictionaryType.DOUBLE.equals(type)
                || DictionaryType.INTEGER.equals(type)
                || DictionaryType.REAL.equals(type)
                || DictionaryType.EXPONENTIAL.equals(type)
                || DictionaryType.NONDECIMAL.equals(type)
                || DictionaryType.ASCII_INTEGER.equals(type)
                || DictionaryType.NON_DECIMAL.equals(type)
                || DictionaryType.DECIMAL.equals(type)
                || DictionaryType.CONTEXT_DEPENDENT.equals(type)
                || DictionaryType.CONTEXTDEPENDENT.equals(type)) {
            return true;
        }
        // special case for ambiguous date values
        if (DictionaryType.DATE.equals(type)
                || DictionaryType.TIME.equals(type)) {
            try {
                Integer intVal = Integer.valueOf(this.getValue());
                // make sure same value after transformation, ie no decimals
                // ignored and right length for years
                if (intVal.toString().equals(this.getValue())
                        && this.getValue().length() == 4) {

                    return true;
                }
                // NOTE: there are at least a few missions that have a start
                // date far in the past. Believe this is projected trajectories
                // or similar. Not doing test beyond the value being numeric
            } catch (final NumberFormatException e) {
                // not able to cast to int, can't be a year
            }
        }
        return false;
    }

}
