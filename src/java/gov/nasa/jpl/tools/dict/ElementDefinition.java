//Copyright 2006, by the California Institute of 
//Technology. ALL RIGHTS RESERVED. United States Government 
//Sponsorship acknowledged. Any commercial use must be negotiated with 
//the Office of Technology Transfer at the California Institute of 
//Technology.
//
//This software may be subject to U.S. export control laws. By 
//accepting this software, the user agrees to comply with all 
//applicable U.S. export laws and regulations. User has the 
//responsibility to obtain export licenses, or other export authority 
//as may be required before exporting such information to foreign 
//countries or providing access to foreign persons.
//
// $Id$
//

package gov.nasa.pds.tools.dict;

import java.util.ArrayList;
import java.util.Collection;

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
    private Collection values;
    private Number minimum;
    private Number maximum;
    
    public ElementDefinition(String identifier) {
        super(identifier);
        dataType = null;
        units = null;
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
     * @return Returns the units.
     */
    public String getUnits() {
        return units;
    }
    
    /**
     * @param units The units to set.
     */
    public void setUnits(String units) {
        this.units = units;
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

}
