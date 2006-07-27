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

import org.apache.log4j.Logger;

import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.dict.ElementDefinition;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.Value;
import gov.nasa.pds.tools.dict.type.InvalidLengthException;
import gov.nasa.pds.tools.dict.type.NumericTypeChecker;
import gov.nasa.pds.tools.dict.type.OutOfRangeException;
import gov.nasa.pds.tools.dict.type.TypeChecker;
import gov.nasa.pds.tools.dict.type.TypeCheckerFactory;
import gov.nasa.pds.tools.dict.type.UnsupportedTypeException;
import gov.nasa.pds.tools.dict.type.InvalidTypeException;
import gov.nasa.pds.tools.dict.DictionaryTokens;

/**
 * This class will validate an element value or set of values against 
 * an ElementDefinition.
 * @author pramirez
 * @version $Revision$
 * 
 */
public class ElementValidator implements DictionaryTokens {
    private static Logger log = Logger.getLogger(new ElementValidator().getClass().getName());
    
    public static boolean isValid(ElementDefinition definition, AttributeStatement attribute) 
            throws UnsupportedTypeException {
        boolean valid = true;
        Value value = attribute.getValue();
        Object castedValue = null;
        
        //Check length of namespace
        if (attribute.hasNamespace()) {
            if (attribute.getNamespace().length() > NAMESPACE_LENGTH) {
                valid = false;
                log.error("line " + attribute.getLineNumber() + ": Namespace exceeds max length of " + 
                    ELEMENT_IDENT_LENGTH + " characters.");
            }
        }
        
        //Check length of identifier
        if (attribute.getElementIdentifier().length() > ELEMENT_IDENT_LENGTH) {
            valid = false;
            log.error("line " + attribute.getLineNumber() + ": Identifier exceeds max length of " + 
                    ELEMENT_IDENT_LENGTH + " characters.");
        }

        //Check against valid values if there are any
        if (definition.hasValidValues()) {
            if (!definition.getValues().contains(value.toString())) {
                valid = false;
                log.error("line " + attribute.getLineNumber() + ": " + value.toString() + 
                          " is not in the list of valid values for " + attribute.getIdentifier());
            }
        }
        
        //Load the type checker
        TypeChecker checker = TypeCheckerFactory.getInstance().newInstance(definition.getDataType());
        
        //Try to cast to an instance of the type
        try {
            castedValue = checker.cast(value.toString());
        } catch (InvalidTypeException ite) {
            valid = false;
            log.error("line " + attribute.getLineNumber() + ": " + ite.getMessage());
        }
        
        //Check min length
        try {
            checker.checkMinLength(value.toString(), definition.getMinLength());
        } catch (InvalidLengthException ile) {
            valid = false;
            log.error("line " + attribute.getLineNumber() + ": " + ile.getMessage());
        }
        
        //Check max length
        try {
            checker.checkMaxLength(value.toString(), definition.getMaxLength());
        } catch (InvalidLengthException ile) {
            valid = false;
            log.error("line " + attribute.getLineNumber() + ": " + ile.getMessage());
        }
        
        //Check to see if this is a numeric type checker if so then do further checking
        if (checker instanceof NumericTypeChecker && castedValue instanceof Number && castedValue != null) {
            NumericTypeChecker numericChecker = (NumericTypeChecker) checker;
            
            //Check min value
            if (definition.hasMinimum()) {
                try {
                    numericChecker.checkMinValue((Number) castedValue, definition.getMinimum());
                } catch (OutOfRangeException oor) {
                    valid = false;
                    log.error("line " + attribute.getLineNumber() + ": " + oor.getMessage());
                }
            }
            
            //Check max value
            if (definition.hasMaximum()) {
                try {
                    numericChecker.checkMaxValue((Number) castedValue, definition.getMaximum());
                } catch (OutOfRangeException oor) {
                    valid = false;
                    log.error("line " + attribute.getLineNumber() + ": " + oor.getMessage());
                }
            }
            
            //Check units if found
            //FIXME: Support units
        }
        
        return valid;
    }
    
    public static boolean isValid(Dictionary dictionary, AttributeStatement attribute) 
            throws DefinitionNotFoundException, UnsupportedTypeException {
        ElementDefinition definition = dictionary.getElementDefiniton(attribute.getIdentifier());
        
        if (definition == null)
            throw new DefinitionNotFoundException("Undefined Element: " + attribute.getIdentifier());
   
        return isValid(definition, attribute);
    }

}
