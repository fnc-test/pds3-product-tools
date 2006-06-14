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
    private int radix;
    
    /**
     * Constructs a Numeric with the given value
     * @param value of numeric
     */
    public Numeric(String value) {
        this(value, null, 10);
    }
    
    public Numeric(String value, String units) {
        this(value, units, 10);
    }
    
    public Numeric(String value, int radix) {
        this(value, null, radix);
    }
    
    public Numeric(String value, String units, int radix) {
        super(value);
        this.units = units;
        this.radix = radix;
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
    public void setRadix(int radix) {
        this.radix = radix;
    }
    
    /**
     * Retrieves the base
     * @return base
     */
    public int radix() {
        return radix;
    }

}
