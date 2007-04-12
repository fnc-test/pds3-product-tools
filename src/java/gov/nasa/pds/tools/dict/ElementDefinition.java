// Copyright 2006-2007, by the California Institute of Technology.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class ElementDefinition extends Definition {
    private String dataType;
    private String unitId;
    private List unitList;
    private String valueType;
    private int minLength;
    private int maxLength;
    private Collection values;
    private Number minimum;
    private Number maximum;
    
    public ElementDefinition(String identifier) {
        super(identifier);
        dataType = null;
        unitList = new ArrayList();
        unitId = null;
        valueType = null;
        minLength = 0;
        maxLength = Integer.MAX_VALUE;
        values = new ArrayList();
        minimum = null;
        maximum = null;
    }
    
    /**
     * @return Returns the dataType.
     */
    public String getDataType() {
        return dataType;
    }
    
    /**
     * @param dataType The dataType to set.
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    
    /**
     * @return Returns the maximum.
     */
    public Number getMaximum() {
        return maximum;
    }
    
    /**
     * @param maximum The maximum to set.
     */
    public void setMaximum(Number maximum) {
        this.maximum = maximum;
    }
    
    /**
     * @return Returns the maxLength.
     */
    public int getMaxLength() {
        return maxLength;
    }
    
    /**
     * @param maxLength The maxLength to set.
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
    
    /**
     * @return Returns the minimum.
     */
    public Number getMinimum() {
        return minimum;
    }
    
    /**
     * @param minimum The minimum to set.
     */
    public void setMinimum(Number minimum) {
        this.minimum = minimum;
    }
    
    /**
     * @return Returns the minLength.
     */
    public int getMinLength() {
        return minLength;
    }
    
    /**
     * @param minLength The minLength to set.
     */
    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }
    
    /**
     * @return Returns the unit id that can be looked up in a dictionary.
     */
    public String getUnitId() {
        return unitId;
    }
    
    /**
     * @param unitId
     */
    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }
    
    public void setUnitList(List unitList) {
        this.unitList = unitList;
    }

    /**
     * @return Returns the values.
     */
    public Collection getValues() {
        return values;
    }
    
    /**
     * @param values The values to set.
     */
    public void setValues(Collection values) {
        this.values = values;
    }
    
    /**
     * @return Returns the valueType.
     */
    public String getValueType() {
        return valueType;
    }
    
    /**
     * @param valueType The valueType to set.
     */
    public void setValueType(String valueType) {
        this.valueType = valueType;
    }
    
    public boolean hasValidValues() {
        if (values.size() > 0)
            return true;
        return false;
    }
    
    public boolean hasMaximum() {
        if (maximum != null)
            return true;
        return false;
    }
    
    public boolean hasMinimum() {
        if (minimum != null)
            return true;
        return false;
    }
    
    public boolean isUnitAllowed(String unit) {
        return unitList.contains(unit);
    }
 
}
