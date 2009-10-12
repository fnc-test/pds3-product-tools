// Copyright 2006-2010, by the California Institute of Technology.
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

import gov.nasa.pds.tools.label.parser.UnsupportedDataObjectException;

/**
 * This class will dynamically load data object validators from property
 * settings. The property mapping should take the form object.validator.{TYPE}
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class DataObjectValidatorFactory {
    private static DataObjectValidatorFactory factory = null;

    /**
     * Constructs a parser factory following the Singleton pattern
     */
    private DataObjectValidatorFactory() {
        // TODO: Load type to class mappings
    }

    /**
     * This will provide access to a {@link DataObjectValidatorFactory}
     * 
     * @return factory to generate data object validators
     */
    public synchronized static DataObjectValidatorFactory getInstance() {
        if (factory == null)
            factory = new DataObjectValidatorFactory();
        return factory;
    }

    /**
     * Retrieves a validator for the given type
     * 
     * @param type
     *            The type of data object to be validated
     * @return a validator for the given type
     * @throws UnsupportedDataObjectException
     *             if there is no mapping to a validator for the type
     */
    public DataObjectValidator newInstance(String type)
            throws UnsupportedDataObjectException {
        String className = System.getProperty("object.validator." + type); //$NON-NLS-1$

        if (className == null) {
            throw new UnsupportedDataObjectException(
                    "There is no mapping available. Property "
                            + "object.validator." + type + " was not set.");
        }

        DataObjectValidator validator = null;

        try {
            validator = (DataObjectValidator) Class.forName(className)
                    .newInstance();
        } catch (InstantiationException e) {
            throw new UnsupportedDataObjectException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new UnsupportedDataObjectException(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new UnsupportedDataObjectException(e.getMessage());
        }

        if (validator == null) {
            throw new UnsupportedDataObjectException(
                    "Validator could not be loaded for type " + type
                            + " with class " + className);
        }

        return validator;
    }
}
