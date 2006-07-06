//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label.validate;

import gov.nasa.pds.tools.label.parser.UnsupportedTypeException;

/**
 * This class will dynamically load data object validators from property settings.
 * The property mapping should take the form object.validator.{TYPE}
 * @author pramirez
 * @version $Revision$
 * 
 */
public class DataObjectValidatorFactory {
    private static DataObjectValidatorFactory factory = null;
    
    /**
     *  Constructs a parser factory following the Singleton pattern
     */
    private DataObjectValidatorFactory() {
        //TODO: Load type to class mappings
    }
    
    /**
     * This will provide access to a {@link DataObjectValidatorFactory}
     * @return factory to generate data object validators
     */
    public synchronized static DataObjectValidatorFactory getInstance() {
        if (factory == null)
            factory = new DataObjectValidatorFactory();
        return factory;
    }
    
    /**
     * Retrieves a validator for the given type
     * @param type The type of data object to be validated
     * @return a validator for the given type
     * @throws UnsupportedTypeException if there is no mapping to a validator for the type
     */
    public DataObjectValidator newInstance(String type) throws UnsupportedTypeException {
        String className = System.getProperty("object.validator." + type);
        
        if (className == null) 
            throw new UnsupportedTypeException("There is no mapping available. Property "
                    + "object.validator." + type + " was not set.");
        
        DataObjectValidator validator = null;
        
        try {
            validator = (DataObjectValidator) Class.forName(className).newInstance();
        } catch (InstantiationException e) {
            throw new UnsupportedTypeException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new UnsupportedTypeException(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new UnsupportedTypeException(e.getMessage());
        }
       
        
        if (validator == null)
            throw new UnsupportedTypeException("Validator could not be loaded for type " + type 
                    + " with class " + className);
        
        return validator;
    }
}
