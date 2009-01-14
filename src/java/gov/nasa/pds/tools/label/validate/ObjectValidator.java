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

package gov.nasa.pds.tools.label.validate;

import gov.nasa.pds.tools.dict.Definition;
import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.dict.ElementDefinition;
import gov.nasa.pds.tools.dict.GroupDefinition;
import gov.nasa.pds.tools.dict.ObjectDefinition;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.GroupStatement;
import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.tools.label.PointerType;
import gov.nasa.pds.tools.logging.ToolsLogRecord;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class ObjectValidator implements PointerType {
    private static Logger log = Logger.getLogger(ObjectValidator.class.getName());
    
    public static boolean isValid(Dictionary dictionary, ObjectStatement object) {
        return isValid(dictionary, object, new DefaultValidationListener());
    }
    
    public static boolean isValid(Dictionary dictionary, ObjectStatement object, ValidationListener listener) {
        boolean valid = true;
        
        //Find definition then validate
        ObjectDefinition definition = dictionary.findObjectClassDefinition(object.getIdentifier());
        if (definition == null) {
        	listener.reportError("Undefined Object: " + object.getIdentifier());
        	log.log(new ToolsLogRecord(Level.SEVERE, "Undefined Object: " + object.getIdentifier(), object.getFilename(), object.getContext(), object.getLineNumber()));
        	valid = false;
        }
        
        if (definition != null) {
	        //First check that required elements are captured in object
	        for (Iterator i = definition.getRequiredElements().iterator(); i.hasNext();) {
	            String required = (String) i.next();
	            //First check to see if attribute is found by its identifier or as a pointer
	            if (!object.hasAttribute(required) && !object.hasPointer(required)) {
	                boolean foundAlias = false;
	                //Next check to see if the attribute is present as an alias
	                //Lookup definition for required element
	                ElementDefinition elementDefinition = dictionary.getElementDefinition(required);
	                //Now loop through aliases to see if the element appears
	                if (elementDefinition != null) {
		                for (Iterator a = elementDefinition.getAliases().iterator(); a.hasNext() && !foundAlias;) {
		                    //All element aliases take the form <object_identifier>.<element_identifier>
		                    String [] identifier = a.next().toString().split("\\.");
		                    if (identifier[0].equals(definition.getIdentifier()) && object.hasAttribute(identifier[1]))
		                        foundAlias = true;
		                }
	                }
	                //Didn't find anything time to log
	                if (!foundAlias) {
	                    valid = false;
	                    listener.reportError("Object " + object.getIdentifier() + 
	                            " does not contain required element " + required);
	                    log.log(new ToolsLogRecord(Level.SEVERE, "Object " + object.getIdentifier() + 
	                            " does not contain required element " + required, object.getFilename(), 
	                            object.getContext(), object.getLineNumber()));
	                }
	            }
	        }
        }
	        
	    if (definition != null) {
	        //Check to make sure that all required objects are present
	        for (Iterator i = definition.getRequiredObjects().iterator(); i.hasNext();) {
	            String required = (String) i.next();
	            //First see if object is present
	            if (!object.hasObject(required) && !object.hasGroup(required)) {
	                boolean foundAlias = false;
	                //Next check to see if the object is present as an alias
	                //Lookup definition for required object
	                Definition def = dictionary.getObjectDefinition(required);
	                if (def == null)
	                	def = dictionary.getGroupDefinition(required);
	                //Now loop through aliases to see if the object appears
	                if (def != null) {
		                for (Iterator a = def.getAliases().iterator(); a.hasNext() && !foundAlias;) {
		                    String alias = a.next().toString();
		                    if (object.hasObject(alias) || object.hasGroup(alias))
		                        foundAlias = true;
		                }
	                }
	                //Check to see if this is a reference to a catalog object
	                for (int n = 0; !foundAlias && n < CATALOG_NAMES.length; n++) {
	                	if (CATALOG_NAMES[n].equals(required.toUpperCase() + "_" + CATALOG)) {
	                		if (object.hasPointer(CATALOG_NAMES[n])) {
	                			foundAlias = true;
	                		}
	                	}
	                }
	                //Didn't find anything time to log
	                if (!foundAlias) {
	                    valid = false;
	                    if (def != null && def instanceof GroupDefinition) {
		                    listener.reportError("Object " + object.getIdentifier() + 
		                            " does not contain required group " + required);
		                    log.log(new ToolsLogRecord(Level.SEVERE, "Object " + object.getIdentifier() + 
		                            " does not contain required group " + required, object.getFilename(), 
		                            object.getContext(), object.getLineNumber()));
	                    } else {
	                    	listener.reportError("Object " + object.getIdentifier() + 
		                            " does not contain required object " + required);
		                    log.log(new ToolsLogRecord(Level.SEVERE, "Object " + object.getIdentifier() + 
		                            " does not contain required object " + required, object.getFilename(), 
		                            object.getContext(), object.getLineNumber()));
	                    }
	                }
	            }
	        }
        }
        
        //Run through nested objects and check them
        List objects = object.getObjects();
        Collections.sort(objects);
        for (Iterator i = objects.iterator(); i.hasNext();) {
            ObjectStatement obj = (ObjectStatement) i.next();
            //Check to make sure object is allowed within this definition
            if (definition != null && !definition.isObjectPossible(obj.getIdentifier())) {
                //Next check to see if the object is allowed as an alias
                //Lookup definition object by its alias
                ObjectDefinition objectDefinition = dictionary.getObjectDefinition(obj.getIdentifier());
                if (objectDefinition == null || !definition.isObjectPossible(objectDefinition.getIdentifier())) {
                    valid = false;
                    listener.reportError("Object " + object.getIdentifier() +  " contains the object " +
                            obj.getIdentifier() + " which is neither required nor optional.");
                    log.log(new ToolsLogRecord(Level.SEVERE, "Object " + object.getIdentifier() +  " contains the object " +
                            obj.getIdentifier() + " which is neither required nor optional.", obj.getFilename(), 
                            obj.getContext(), obj.getLineNumber()));
                }
            }
            //Validate nested object
            if (!ObjectValidator.isValid(dictionary, obj, listener))
                valid = false;
        }
        
        List groups = object.getGroups();
        Collections.sort(groups);
        for (Iterator i = groups.iterator(); i.hasNext();) {
            GroupStatement group = (GroupStatement) i.next();
            //Check to make sure object is allowed within this definition
            if (definition != null && !definition.isObjectPossible(group.getIdentifier())) {
                //Next check to see if the object is allowed as an alias
                //Lookup definition object by its alias
                GroupDefinition groupDefinition = dictionary.getGroupDefinition(group.getIdentifier());
                if (groupDefinition == null || !definition.isObjectPossible(groupDefinition.getIdentifier())) {
                    valid = false;
                    listener.reportError("Object " + object.getIdentifier() +  " contains the group " +
                            group.getIdentifier() + " which is neither required nor optional.");
                    log.log(new ToolsLogRecord(Level.SEVERE, "Object " + object.getIdentifier() +  " contains the group " +
                            group.getIdentifier() + " which is neither required nor optional.", group.getFilename(), 
                            group.getContext(), group.getLineNumber()));
                }
            }
            //Validate nested object
            if (!GroupValidator.isValid(dictionary, group, listener))
                valid = false;
        }
        
        //Run through and validate all attributes
        List attributes = object.getAttributes();
        Collections.sort(attributes);
        for (Iterator i = object.getAttributes().iterator(); i.hasNext();) {
            AttributeStatement attribute = (AttributeStatement) i.next();
            //Check to make sure element is allowed within this definition
            if (definition != null && !definition.isElementPossible(attribute.getIdentifier())) {
                //Next check to see if the attribute is allowed as an alias
                //Lookup definition for element by its alias
                ElementDefinition elementDefinition = dictionary.getElementDefinition(attribute.getIdentifier());
                if (elementDefinition == null || !definition.isElementPossible(elementDefinition.getIdentifier())) {
                    valid = false;
                    listener.reportError("Object " + object.getIdentifier() +  " contains the element " +
                            attribute.getIdentifier() + " which is neither required nor optional.");
                    log.log(new ToolsLogRecord(Level.SEVERE, "Object " + object.getIdentifier() +  " contains the element " +
                            attribute.getIdentifier() + " which is neither required nor optional.", attribute.getFilename(),
                            attribute.getContext(), attribute.getLineNumber()));
                }
            }
            //Validate attribute
            String context = (definition == null) ? object.getIdentifier() : definition.getIdentifier();
            if (!ElementValidator.isValid(dictionary, context, attribute, listener))
                valid = false;
        }
        return valid;
    }
    
}
