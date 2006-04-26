//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools.label.parser;

import gov.nasa.jpl.pds.tools.label.ObjectStatement;
import org.apache.log4j.Logger;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public interface DataObjectValidator {
    /**
     * Validates the bytes in the object against the decription in the {@link ObjectStatement}
     * @param bytes of the data object
     * @param object The description of the object consists of a set of {@link AttributeStatement}
     * @throws InvalidObjectException if there is a problem with the bytes in accordance with the description
     * @throws InvalidDescriptionException if the description is insuffcient for the validation to take place
     */
    public void validate(byte [] bytes, ObjectStatement object) throws InvalidObjectException, InvalidDescriptionException;
    
    /**
     * The log that errors will be written to 
     * @param log
     */
    public void setLogger(Logger log);
    
    /**
     * Retrieves the log
     * @return logger
     */
    public Logger getLooger();
}
