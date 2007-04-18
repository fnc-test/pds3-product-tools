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

package gov.nasa.pds.tools.label;

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
    
    /**
     * Constructs a Numeric with the given value and units
     * @param value of numeric
     * @param units of numeric
     */
    public Numeric(String value, String units) {
        this(value, units, 10);
    }
    
    /**
     * Constructs a Numeric with the given value and radix
     * @param value of numeric
     * @param radix of numeric
     */
    public Numeric(String value, int radix) {
        this(value, null, radix);
    }
    
    /**
     * Constructs a Numeric with the given value, units and radix
     * @param value of numeric
     * @param units of numeric
     * @param radix of numeric
     */
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
        //Remove angle brackets if found
        if (units.startsWith("<") && units.endsWith(">"))
           this.units = units.substring(1, units.length()-1).trim();
        else
            this.units = units.trim();
    }
    
    /**
     * Retrieves the units
     * @return units
     */
    public String getUnits() {
        return units;
    }
    
    /**
     * Sets the radix
     * @param radix of the numeric
     */
    public void setRadix(int radix) {
        this.radix = radix;
    }
    
    /**
     * Retrieves the base
     * @return base
     */
    public int getRadix() {
        return radix;
    }

}
