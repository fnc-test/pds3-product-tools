// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// • Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// • Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// • Neither the name of Caltech nor its operating division, the Jet Propulsion
// Laboratory, nor the names of its contributors may be used to endorse or
// promote products derived from this software without specific prior written
// permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

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
