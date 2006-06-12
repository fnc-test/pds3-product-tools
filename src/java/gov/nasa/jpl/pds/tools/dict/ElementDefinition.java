//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools.dict;

import java.util.List;
import java.util.ArrayList;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class ElementDefinition extends Definition {
    private String dataType;
    private String units;
    private String valueType;
    private int minLength;
    private int maxLength;
    private List values;
    private double minimum;
    private double maximum;
    
    public ElementDefinition(String identifier) {
        super(identifier);
        dataType = null;
        units = null;
        valueType = null;
        minLength = 0;
        maxLength = Integer.MAX_VALUE;
        values = new ArrayList();
        minimum = Double.MIN_VALUE;
        maximum = Double.MAX_VALUE;
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
    public double getMaximum() {
        return maximum;
    }
    
    /**
     * @param maximum The maximum to set.
     */
    public void setMaximum(double maximum) {
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
    public double getMinimum() {
        return minimum;
    }
    
    /**
     * @param minimum The minimum to set.
     */
    public void setMinimum(double minimum) {
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
     * @return Returns the unitId.
     */
    public String getUnits() {
        return units;
    }
    
    /**
     * @param unitId The unitId to set.
     */
    public void setUnits(String units) {
        this.units = units;
    }

    /**
     * @return Returns the values.
     */
    public List getValues() {
        return values;
    }
    
    /**
     * @param values The values to set.
     */
    public void setValues(List values) {
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

}
