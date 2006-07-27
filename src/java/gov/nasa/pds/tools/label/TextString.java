//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class TextString extends Scalar {

    /**
     * @param value
     */
    public TextString(String value) {
        super(value.replaceAll("\"", ""));
    }
    
}
