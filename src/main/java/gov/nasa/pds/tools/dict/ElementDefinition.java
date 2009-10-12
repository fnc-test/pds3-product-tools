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
