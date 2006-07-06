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

package gov.nasa.pds.tools.label.validate;

import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.dict.ObjectDefinition;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.ObjectStatement;

import org.apache.log4j.Logger;
import java.util.Iterator;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class ObjectValidator {
    private static Logger log = Logger.getLogger("gov.nasa.pds.label.ObjectStatement");

    public static boolean isValid(Dictionary dictionary, ObjectStatement object) throws 
       DefinitionNotFoundException {
        boolean valid = true;
        
        //Find definition then validate
        ObjectDefinition definition = dictionary.findObjectClassDefinition(object.getIdentifier());
        if (definition == null)
            throw new DefinitionNotFoundException("Could not find object definition for " +
                    object.getIdentifier());
        
        //First check that required elements are captured in object
        for (Iterator i = definition.getRequiredElements().iterator(); i.hasNext();) {
            String required = (String) i.next();
            if (!object.hasAttribute(required)) {
                valid = false;
                log.error("Object " + object.getIdentifier() + 
                        " does not contain required element " + required);
            }
        }
        
        //Check to make sure all attributes are allowed within this definition
        //If the definition contains the element PSDD then anything is allowed
        //and this check can be skipped
        //TODO: put magic string PSDD in an interface as a static final String
        if (!definition.isElementPossible("PSDD")) {
            for (Iterator i = object.getAttributes().iterator(); i.hasNext();) {
                AttributeStatement attribute = (AttributeStatement) i.next();
                if (!definition.isElementPossible(attribute.getIdentifier())) {
                    valid = false;
                    log.error("Object " + object.getIdentifier() +  " contains the element " +
                            attribute.getIdentifier() + " which is neither required nor optional.");
                }
            }
        }
        
        //Validate all attributes
        for (Iterator i = object.getAttributes().iterator(); i.hasNext();) {
            AttributeStatement attribute = (AttributeStatement) i.next();
            boolean elementValid = ElementValidator.isValid(dictionary, attribute);
            if (!elementValid)
                valid = false;
        }
        
        //Check to make sure that all required objects are present
        for (Iterator i = definition.getRequiredObjects().iterator(); i.hasNext();) {
            String required = (String) i.next();
            if (!object.hasObject(required)) {
                valid = false;
                log.error("Object " + object.getIdentifier() + 
                        " does not contain required object " + required);
            }
        }
        
        //Check to make sure all objects are allowed within this definition
        for (Iterator i = object.getAttributes().iterator(); i.hasNext();) {
            ObjectStatement obj = (ObjectStatement) i.next();
            if (!definition.isObjectPossible(obj.getIdentifier())) {
                valid = false;
                log.error("Object " + object.getIdentifier() +  " contains the object " +
                        obj.getIdentifier() + " which is neither required nor optional.");
            }
        }

        
        //Validate all nested objects
        for (Iterator i = object.getObjects().iterator(); i.hasNext();) {
            ObjectStatement obj = (ObjectStatement) i.next();
            boolean objValid = ObjectValidator.isValid(dictionary, obj);
            if (!objValid)
                valid = false;
        }
        
        return valid;
    }
    
}
