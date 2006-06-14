// Copyright 2006, by the California Institute of 
// Technology. ALL RIGHTS RESERVED. United States Government 
// Sponsorship acknowledged. Any commercial use must be negotiated with 
// the Office of Technology Transfer at the California Institute of 
// Technology.
//
// This software may be subject to U.S. export control laws. By 
// accepting this software, the user agrees to comply with all 
// applicable U.S. export laws and regulations. User has the 
// responsibility to obtain export licenses, or other export authority 
// as may be required before exporting such information to foreign 
// countries or providing access to foreign persons.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools.label.validate;

import gov.nasa.jpl.pds.tools.dict.ElementDefinition;
import gov.nasa.jpl.pds.tools.label.AttributeStatement;
import gov.nasa.jpl.pds.tools.dict.Dictionary;
import org.apache.log4j.Logger;

/**
 * This class will validate an element value or set of values against 
 * an ElementDefinition.
 * @author pramirez
 * @version $Revision$
 * 
 */
public class ElementValidator {
    
    public static boolean isValid(ElementDefinition definition, AttributeStatement attribute, Logger log) {
        return false;
    }
    
    public static boolean isValid(ElementDefinition definition, String identifier, String value, Logger log) {
        return false;
    }
    
    public static boolean isValid(Dictionary dictionary, AttributeStatement attribute, Logger log) {
        return false;
    }
    
    public static boolean isValid(Dictionary dictionary, String identifier, String value, Logger log) {
        return false;
    }

}
