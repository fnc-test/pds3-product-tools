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

package gov.nasa.pds.tools.dict;

import gov.nasa.pds.tools.constants.Constants.DictionaryType;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class ElementDefinition extends Definition {

    private static final long serialVersionUID = 3043666890673858660L;

    private DictionaryType dataType;

    private String units;

    private String valueType;

    private Integer minLength;

    private Integer maxLength;

    private Collection<String> values = new ArrayList<String>();

    private Number minimum;

    private Number maximum;

    public ElementDefinition(Dictionary sourceDictionary, int lineNumber,
            DictIdentifier identifier) {
        super(sourceDictionary, lineNumber, identifier);
    }

    /**
     * @return Returns the dataType.
     */
    public DictionaryType getDataType() {
        return this.dataType;
    }

    /**
     * @param dataType
     *            The dataType to set.
     */
    public void setDataType(DictionaryType dataType) {
        this.dataType = dataType;
    }

    /**
     * @return Returns the maximum.
     */
    public Number getMaximum() {
        return this.maximum;
    }

    /**
     * @param maximum
     *            The maximum to set.
     */
    public void setMaximum(Number maximum) {
        this.maximum = maximum;
    }

    /**
     * @return Returns the maxLength.
     */
    public Integer getMaxLength() {
        if (this.maxLength == null)
            return Integer.MAX_VALUE;
        return this.maxLength;
    }

    /**
     * @param maxLength
     *            The maxLength to set.
     */
    public void setMaxLength(final int maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * @return Returns the minimum.
     */
    public Number getMinimum() {
        return this.minimum;
    }

    /**
     * @param minimum
     *            The minimum to set.
     */
    public void setMinimum(Number minimum) {
        this.minimum = minimum;
    }

    /**
     * @return Returns the minLength.
     */
    public Integer getMinLength() {
        if (this.minLength == null)
            return 0;
        return this.minLength;
    }

    /**
     * @param minLength
     *            The minLength to set.
     */
    public void setMinLength(final int minLength) {
        this.minLength = minLength;
    }

    /**
     * @return Returns the unit id that can be looked up in a dictionary.
     */
    public String getUnits() {
        return this.units;
    }

    /**
     * @param units
     */
    public void setUnits(final String units) {
        this.units = units;
    }

    /**
     * @return Returns the values.
     */
    public Collection<String> getValues() {
        return this.values;
    }

    /**
     * @param values
     *            The values to set.
     */
    public void setValues(final Collection<String> values) {
        this.values = values;
    }

    public void addValue(final String value) {
        this.values.add(value);
    }

    /**
     * @return Returns the valueType.
     */
    public String getValueType() {
        return this.valueType;
    }

    /**
     * @param valueType
     *            The valueType to set.
     */
    public void setValueType(final String valueType) {
        this.valueType = valueType;
    }

    public boolean hasValidValues() {
        return this.values.size() > 0;
    }

    public boolean hasMaximum() {
        return this.maximum != null;
    }

    public boolean hasMinimum() {
        return this.minimum != null;
    }

    public boolean hasMinLength() {
        return this.minLength != null;
    }

    public boolean hasMaxLength() {
        return this.maxLength != null;
    }

}
