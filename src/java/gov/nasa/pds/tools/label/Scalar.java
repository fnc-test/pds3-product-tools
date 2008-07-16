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
    
    public abstract boolean isSupportedPDSType(String type);
}
