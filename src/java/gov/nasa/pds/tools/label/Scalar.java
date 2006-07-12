//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label;

/**
 * This class represents a Scalar which can be assigned to an 
 * attribute or be a part of a Set or Sequence.
 * @author pramirez
 * @version $Revision$
 * 
 */
public abstract class Scalar implements Value {
    private String value;
    
    /**
     * Constructs a Scalar
     * @param value
     */
    protected Scalar(String value) {
        this.value = value;
    }
    
    /**
     * Retrieves the value
     * @return the value
     */
    public String getValue() {
        return value;
    }
    
    public String toString() {
        return value;
    }
}
