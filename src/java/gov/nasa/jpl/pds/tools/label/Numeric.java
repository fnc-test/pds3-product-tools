//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools.label;

/**
 * This class represents a numeric scalar. It is not type 
 * specific so everything is represented as a String. 
 * @author pramirez
 * @version $Revision$
 * 
 */
public class Numeric extends Scalar {
    private String units;
    private String base;
    
    /**
     * Constructs a Numeric with the given value
     * @param value of numeric
     */
    public Numeric(String value) {
        super(value);
        this.units = null;
        this.base = null;
    }

    /**
     * Sets the units
     * @param units of the numeric
     */
    public void setUnits(String units) {
        this.units = units;
    }
    
    /**
     * Retrieves the units
     * @return units
     */
    public String units() {
        return units;
    }
    
    /**
     * Sets the base
     * @param base of the numeric
     */
    public void setBase(String base) {
        this.base = base;
    }
    
    /**
     * Retrieves the base
     * @return base
     */
    public String base() {
        return base;
    }

}
