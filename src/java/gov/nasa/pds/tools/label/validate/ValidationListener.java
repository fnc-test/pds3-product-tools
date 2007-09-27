//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label.validate;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public interface ValidationListener {
    public void reportError(String error);
    public void reportWarning(String warning);
}
