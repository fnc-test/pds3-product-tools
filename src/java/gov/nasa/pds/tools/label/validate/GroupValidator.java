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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.dict.GroupDefinition;
import gov.nasa.pds.tools.dict.type.UnsupportedTypeException;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.GroupStatement;
import gov.nasa.pds.tools.logging.ToolsLogRecord;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class GroupValidator {
    private static Logger log = Logger.getLogger(GroupValidator.class.getName());
    
    public static boolean isValid(Dictionary dictionary, GroupStatement group) 
       throws DefinitionNotFoundException {
        return isValid(dictionary, group, null);
    }
    
    public static boolean isValid(Dictionary dictionary, GroupStatement group, String filename) 
       throws DefinitionNotFoundException {
        boolean valid = true;
        
        //Lookup group definition, can't do anything without it
        GroupDefinition definition = dictionary.findGroupClassDefinition(group.getIdentifier());
        if (definition == null)
            throw new DefinitionNotFoundException("Could not find group definition for " + group.getIdentifier());
  
        //First check that required elements are captured in object
        for (Iterator i = definition.getRequiredElements().iterator(); i.hasNext();) {
            String required = (String) i.next();
            if (!group.hasAttribute(required)) {
                valid = false;
                log.log(new ToolsLogRecord(Level.SEVERE, "Group " + group.getIdentifier() + 
                        " does not contain required element " + required,
                        filename, group.getLineNumber()));
            }
        }
        
        //Check to make sure all attributes are allowed within this definition
        //If the definition contains the element PSDD then anything is allowed
        //and this check can be skipped
        //TODO: put magic string PSDD in an interface as a static final String
        if (!definition.hasElement("PSDD")) {
            for (Iterator i = group.getAttributes().iterator(); i.hasNext();) {
                AttributeStatement attribute = (AttributeStatement) i.next();
                if (!definition.canHaveElement(attribute.getIdentifier())) {
                    valid = false;
                    log.log(new ToolsLogRecord(Level.SEVERE, "Group " + group.getIdentifier() +  
                            " contains the element " + attribute.getIdentifier() + 
                            " which is neither required nor optional.",
                            filename, group.getLineNumber()));
                }
            }
        }
        
        //Validate all attributes
        List attributes = group.getAttributes();
        Collections.sort(attributes);
        for (Iterator i = group.getAttributes().iterator(); i.hasNext();) {
            AttributeStatement attribute = (AttributeStatement) i.next();
            boolean elementValid = false;
            try {
                elementValid = ElementValidator.isValid(dictionary, attribute);
            } catch (UnsupportedTypeException ute) {
                log.log(new ToolsLogRecord(Level.SEVERE, ute.getMessage(), filename, attribute.getLineNumber()));
            } catch (DefinitionNotFoundException dnfe) {
                log.log(new ToolsLogRecord(Level.SEVERE, dnfe.getMessage(), filename, attribute.getLineNumber()));
            }
            if (!elementValid)
                valid = false;
        }
        
        return valid;
    }
}
