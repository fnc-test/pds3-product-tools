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

package gov.nasa.pds.tools.dict.parser;

import gov.nasa.pds.tools.label.antlr.ODLTokenTypes;
import gov.nasa.pds.tools.dict.Definition;
import gov.nasa.pds.tools.dict.DictionaryTokens;
import gov.nasa.pds.tools.dict.ElementDefinition;
import gov.nasa.pds.tools.dict.GroupDefinition;
import gov.nasa.pds.tools.dict.ObjectDefinition;
import gov.nasa.pds.tools.dict.type.InvalidTypeException;
import gov.nasa.pds.tools.dict.type.NumericTypeChecker;
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
 * statement should be in compliance standard PDS dictionary. These definitions 
 * can then be added to a {@link Dictionary}.
 * 
 * @author pramirez
 * @version $Revision$
 * 
 */
public class DefinitionFactory implements ODLTokenTypes, DictionaryTokens {

    /**
     * This method will determine the type of definition and created it. If it can not 
     * determine the type of definition that should be generated an error will be thrown.
     * @param object from which the defintion will be created
     * @return a {@link Definition} that represents an entry in a PDS data dictionary
     * @throws UnknownDefinitionException thrown when the type of definition can not be recognized.
     */
    public static Definition createDefinition(ObjectStatement object) throws UnknownDefinitionException {
        Definition definition = null;
        
        if (object.getIdentifier().endsWith(DEFINITION)) {
            if (object.getIdentifier().equals(GENERIC_OBJECT) || 
                    object.getIdentifier().equals(SPECIFIC_OBJECT)) {
                if (!OBJECT_TYPE_GENERIC_GROUP.equals(object.getAttribute(OBJECT_TYPE).getValue().toString()))
                    definition = createObjectDefinition(object);
                else
                    definition = createGroupDefinition(object);
            } else if (object.getIdentifier().equals(ELEMENT_DEFINITION)) {
                definition = createElementDefinition(object);
            } 
        } 
        
        if (definition == null)
            throw new UnknownDefinitionException("Can not resolve entry " + object.getIdentifier());

        return definition;
    }
    
    /**
     * This method creates an {@link ObjectDefinition} by gathering the attributes required 
     * from the {@link ObjectStatement} as specified in the PDS Data Dictionary document.
     * @param object that has the information to form an {@link ObjectDefinition}
     * @return a {@link Definition} that represents an entry in a PDS data dictionary
     * @throws UnknownDefinitionException thrown when the type of definition can not be recognized.
     */
    public static ObjectDefinition createObjectDefinition(ObjectStatement object) throws UnknownDefinitionException {
        ObjectDefinition definition = null;
        
        if (object.getIdentifier().equals(GENERIC_OBJECT) || 
                object.getIdentifier().equals(SPECIFIC_OBJECT) &&
                !OBJECT_TYPE_GENERIC_GROUP.equals(object.getAttribute(OBJECT_TYPE).getValue().toString())) {
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
    
    /**
     * This method creates an {@link GroupDefinition} by gathering the attributes required 
     * from the {@link ObjectStatement} as specified in the PDS Data Dictionary document.
     * @param object that has the information to form an {@link GroupDefinition}
     * @return a {@link Definition} that represents an entry in a PDS data dictionary
     * @throws UnknownDefinitionException thrown when the type of definition can not be recognized.
     */
    public static GroupDefinition createGroupDefinition(ObjectStatement object) throws UnknownDefinitionException {
        GroupDefinition definition = null;
        
        if (object.getIdentifier().equals(GENERIC_OBJECT) || 
                object.getIdentifier().equals(SPECIFIC_OBJECT) &&
                OBJECT_TYPE_GENERIC_GROUP.equals(object.getAttribute(OBJECT_TYPE).getValue().toString())) {
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
    
    /**
     * This method creates an {@link ElementDefinition} by gathering the attributes required 
     * from the {@link ObjectStatement} as specified in the PDS Data Dictionary document.
     * @param object that has the information to form an {@link ElementDefinition}
     * @return a {@link Definition} that represents an entry in a PDS data dictionary
     * @throws UnknownDefinitionException thrown when the type of definition can not be recognized.
     */
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
                definition.setUnitId(attribute.getValue().toString());
            
            //Find and set min length
            attribute = object.getAttribute(MIN_LENGTH);
            if (attribute != null && !"NULL".equals(attribute.getValue().toString())) {
                try {
                    definition.setMinLength(Integer.parseInt(attribute.getValue().toString()));
                } catch (NumberFormatException nfe) {}
            }
            
            //Find and set max length
            attribute = object.getAttribute(MAX_LENGTH);
            if (attribute != null && !"NULL".equals(attribute.getValue().toString())) {
                try {
                    definition.setMaxLength(Integer.parseInt(attribute.getValue().toString()));
                } catch (NumberFormatException nfe) {}
            }
            
            //Find and set min value
            attribute = object.getAttribute(MINIMUM);
            if (attribute != null && !"NULL".equals(attribute.getValue().toString())) {
                try {
                    TypeChecker checker = TypeCheckerFactory.getInstance().newInstance(definition.getDataType());
                    if (checker instanceof NumericTypeChecker)
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
            if (attribute != null && !"NULL".equals(attribute.getValue().toString())) {
                try {
                    TypeChecker checker = TypeCheckerFactory.getInstance().newInstance(definition.getDataType());
                    if (checker instanceof NumericTypeChecker)
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
