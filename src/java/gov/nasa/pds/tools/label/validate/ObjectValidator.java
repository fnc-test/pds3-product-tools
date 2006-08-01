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
import gov.nasa.pds.tools.dict.type.UnsupportedTypeException;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.ObjectStatement;

import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class ObjectValidator {
    private static Logger log = Logger.getLogger(new ObjectValidator().getClass().getName());

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
        
        //Run through and validate all attributes
        List attributes = object.getAttributes();
        Collections.sort(attributes);
        for (Iterator i = object.getAttributes().iterator(); i.hasNext();) {
            AttributeStatement attribute = (AttributeStatement) i.next();
            //Check to make sure object is allowed within this definition
            if (!definition.isElementPossible(attribute.getIdentifier())) {
                valid = false;
                log.error("Object " + object.getIdentifier() +  " contains the element " +
                        attribute.getIdentifier() + " which is neither required nor optional.");
            }
            //Validate attribute
            boolean elementValid = false;
            try {
                elementValid = ElementValidator.isValid(dictionary, object.getIdentifier(), attribute);
            } catch (UnsupportedTypeException ute) {
                log.error("line " + attribute.getLineNumber() + ": " + ute.getMessage());
            } catch (DefinitionNotFoundException dnfe) {
                log.error("line " + attribute.getLineNumber() + ": " + dnfe.getMessage());
            }
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
        
        //Run through nested objects and check them
        List objects = object.getObjects();
        Collections.sort(objects);
        for (Iterator i = objects.iterator(); i.hasNext();) {
            ObjectStatement obj = (ObjectStatement) i.next();
            //Check to make sure object is allowed within this definition
            if (!definition.isObjectPossible(obj.getIdentifier())) {
                valid = false;
                log.error("Object " + object.getIdentifier() +  " contains the object " +
                        obj.getIdentifier() + " which is neither required nor optional.");
            }
            //Validate nested object
            boolean objValid = false;
            try {
                ObjectValidator.isValid(dictionary, obj);
            } catch (DefinitionNotFoundException dnfe) {
                log.error("line " + obj.getLineNumber() + ": " + dnfe.getMessage());
            }
            if (!objValid)
                valid = false;
        }
        
        return valid;
    }
    
}
