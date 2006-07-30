//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.dict.parser;


import gov.nasa.pds.tools.label.antlr.ODLTokenTypes;
import gov.nasa.pds.tools.dict.Definition;
import gov.nasa.pds.tools.dict.DictionaryTokens;
import gov.nasa.pds.tools.dict.ElementDefinition;
import gov.nasa.pds.tools.dict.GroupDefinition;
import gov.nasa.pds.tools.dict.ObjectDefinition;
import gov.nasa.pds.tools.dict.type.InvalidTypeException;
import gov.nasa.pds.tools.dict.type.TypeChecker;
import gov.nasa.pds.tools.dict.type.TypeCheckerFactory;
import gov.nasa.pds.tools.dict.type.UnsupportedTypeException;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.tools.label.Set;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class builds definitions from ObjectStatements. The format of the object 
 * statement should be in compliance standard PDS dictionary.
 * @author pramirez
 * @version $Revision$
 * 
 */
public class DefinitionFactory implements ODLTokenTypes, DictionaryTokens {

    public static Definition createDefinition(ObjectStatement object) throws UnknownDefinitionException {
        Definition definition = null;
        
        if (object.getIdentifier().endsWith(DEFINITION)) {
            if (object.getIdentifier().equals(GENERIC_OBJECT) || 
                    object.getIdentifier().equals(SPECIFIC_OBJECT)) {
                definition = createObjectDefinition(object);
            } else if (object.getIdentifier().equals(GENERIC_GROUP) || 
                    object.getIdentifier().equals(SPECIFIC_GROUP)) {
                definition = createGroupDefinition(object);
            } else if (object.getIdentifier().equals(ELEMENT_DEFINITION)) {
                definition = createElementDefinition(object);
            } 
        } 
        
        if (definition == null)
            throw new UnknownDefinitionException("Can not resolve entry " + object.getIdentifier());

        return definition;
    }
    
    public static ObjectDefinition createObjectDefinition(ObjectStatement object) throws UnknownDefinitionException {
        ObjectDefinition definition = null;
        
        if (object.getIdentifier().equals(GENERIC_OBJECT) || 
                object.getIdentifier().equals(SPECIFIC_OBJECT)) {
            definition = new ObjectDefinition(object.getAttribute(NAME).getValue().toString());
           
            //Find and set the description
            AttributeStatement attribute = object.getAttribute(DESCRIPTION);
            if (attribute != null)
                definition.setDescription(attribute.getValue().toString());
            
            //Find and set the status type
            attribute = object.getAttribute(STATUS_TYPE);
            if (attribute != null)
                definition.setStatusType(attribute.getValue().toString());
            
            //Find and set the names of the required objects
            attribute = object.getAttribute(REQUIRED_OBJECTS);
            if (attribute != null) {
                List requiredObjects = new ArrayList();
                Set values = (Set) attribute.getValue();
                for (Iterator i = values.iterator(); i.hasNext();) {
                    requiredObjects.add(i.next().toString());
                }
                definition.setRequiredObjects(requiredObjects);
            }

            //Find and set the names of the optional objects
            attribute = object.getAttribute(OPTIONAL_OBJECTS);
            if (attribute != null) {
                List optionalObjects = new ArrayList();
                Set values = (Set) attribute.getValue();
                for (Iterator i = values.iterator(); i.hasNext();) {
                    optionalObjects.add(i.next().toString());
                }
                definition.setOptionalObjects(optionalObjects);
            }
            
            //Find and set the names of the required elements
            attribute = object.getAttribute(REQUIRED_ELEMENTS);
            if (attribute != null) {
                List requiredElements = new ArrayList();
                Set values = (Set) attribute.getValue();
                for (Iterator i = values.iterator(); i.hasNext();) {
                    requiredElements.add(i.next().toString());
                }
                definition.setRequiredElements(requiredElements);
            }
            
            //Find and set the names of the required elements
            attribute = object.getAttribute(OPTIONAL_ELEMENTS);
            if (attribute != null) {
                List optionalElements = new ArrayList();
                Set values = (Set) attribute.getValue();
                for (Iterator i = values.iterator(); i.hasNext();) {
                    optionalElements.add(i.next().toString());
                }
                definition.setOptionalElements(optionalElements);
            }
        } 
        
        if (definition == null)
            throw new UnknownDefinitionException(object.getIdentifier() + " is not an object definition");
        
        return definition;
    }
    
    public static GroupDefinition createGroupDefinition(ObjectStatement object) throws UnknownDefinitionException {
        GroupDefinition definition = null;
        
        if (object.getIdentifier().equals(GENERIC_GROUP) || 
                object.getIdentifier().equals(SPECIFIC_GROUP)) {
            definition = new GroupDefinition(object.getAttribute(NAME).getValue().toString());
            
            //Find and set the description
            AttributeStatement attribute = object.getAttribute(DESCRIPTION);
            if (attribute != null)
                definition.setDescription(attribute.getValue().toString());
            
            //Find and set the status type
            attribute = object.getAttribute(STATUS_TYPE);
            if (attribute != null)
                definition.setStatusType(attribute.getValue().toString());
            //Find and set the names of the required elements
            attribute = object.getAttribute(REQUIRED_ELEMENTS);
            if (attribute != null) {
                List requiredElements = new ArrayList();
                Set values = (Set) attribute.getValue();
                for (Iterator i = values.iterator(); i.hasNext();) {
                    requiredElements.add(i.next().toString());
                }
                definition.setRequiredElements(requiredElements);
            }
            
            //Find and set the names of the required elements
            attribute = object.getAttribute(OPTIONAL_ELEMENTS);
            if (attribute != null) {
                List optionalElements = new ArrayList();
                Set values = (Set) attribute.getValue();
                for (Iterator i = values.iterator(); i.hasNext();) {
                    optionalElements.add(i.next().toString());
                }
                definition.setOptionalElements(optionalElements);
            }
        }
        
        if (definition == null)
            throw new UnknownDefinitionException(object.getIdentifier() + " is not an group definition");
        
        return definition;
    }
    
    public static ElementDefinition createElementDefinition(ObjectStatement object) throws UnknownDefinitionException {
        ElementDefinition definition = null;
        
        if (object.getIdentifier().equals(ELEMENT_DEFINITION)) {
            definition = new ElementDefinition(object.getAttribute(NAME).getValue().toString());
            
            //Find and set status
            AttributeStatement attribute = object.getAttribute(STATUS_TYPE);
            if (attribute != null)
                definition.setStatusType(attribute.getValue().toString());
            
            //Find and set description
            attribute = object.getAttribute(DESCRIPTION);
            if (attribute != null) 
                definition.setDescription(attribute.getValue().toString());
            
            //Find and set data type
            attribute = object.getAttribute(DATA_TYPE);
            if (attribute != null)
                definition.setDataType(attribute.getValue().toString());
            
            //Find and set units
            attribute = object.getAttribute(DictionaryTokens.UNITS);
            if (attribute != null) 
                definition.setUnits(attribute.getValue().toString());
            
            //Find and set min length
            attribute = object.getAttribute(MIN_LENGTH);
            if (attribute != null) {
                try {
                    definition.setMinLength(Integer.parseInt(attribute.getValue().toString()));
                } catch (NumberFormatException nfe) {}
            }
            
            //Find and set max length
            attribute = object.getAttribute(MAX_LENGTH);
            if (attribute != null) {
                try {
                    definition.setMaxLength(Integer.parseInt(attribute.getValue().toString()));
                } catch (NumberFormatException nfe) {}
            }
            
            //Find and set min value
            attribute = object.getAttribute(MINIMUM);
            if (attribute != null) {
                try {
                    TypeChecker checker = TypeCheckerFactory.getInstance().newInstance(definition.getDataType());
                    definition.setMinimum((Number) checker.cast(attribute.getValue().toString()));
                } catch (InvalidTypeException e) {
                    // TODO Auto-generated catch block
                    //FIXME: throw an InvalidDefinitionException
                } catch (UnsupportedTypeException e) {
                    // TODO Auto-generated catch block
                    //FIXME: throw an InvalidDefinitionException
                }
            }
            
            //Find and set max value
            attribute = object.getAttribute(MAXIMUM);
            if (attribute != null) {
                try {
                    TypeChecker checker = TypeCheckerFactory.getInstance().newInstance(definition.getDataType());
                    definition.setMaximum((Number) checker.cast(attribute.getValue().toString()));
                } catch (InvalidTypeException e) {
                    // TODO Auto-generated catch block
                    //FIXME: throw an InvalidDefinitionException
                } catch (UnsupportedTypeException e) {
                    // TODO Auto-generated catch block
                    //FIXME: throw an InvalidDefinitionException
                }
            }
            
            //Find and set value type
            attribute = object.getAttribute(VALUE_TYPE);
            if (attribute != null)
                definition.setValueType(attribute.getValue().toString());
            
            //Find and set value set
            attribute = object.getAttribute(VALUES);
            List values = new ArrayList();
            if (attribute != null) {
                Set s = (Set) attribute.getValue();
                for (Iterator i = s.iterator(); i.hasNext();) {
                    String value = i.next().toString();
                    if (!"N/A".equals(value))
                        values.add(value);
                }
                definition.setValues(values);
            }
            
        }
        
        if (definition == null) 
            throw new UnknownDefinitionException(object.getIdentifier() + " is not an element definition");
        
        return definition;
    }
}
