//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label.validate;

import gov.nasa.pds.tools.label.Label;

/**
 * This interface is a means to define an extension point for the parser to perform further validation.
 * This type of validation is outside the realm of the syntax defined by the grammar and data dictionary.
 * An example of this would be required file characteristic elements.
 * 
 * @author pramirez
 * @version $Revision$
 * 
 */
public interface LabelValidator {
    /**
     * Method signature for checking to see if a label is valid.
     * @param label object returned from parsing that reprsents the PDS label
     * @return flag indicating whether or not the step in validation was passed.
     */
    public boolean isValid(Label label);
}
