//Copyright 2006, by the California Institute of 
//Technology. ALL RIGHTS RESERVED. United States Government 
//Sponsorship acknowledged. Any commercial use must be negotiated with 
//the Office of Technology Transfer at the California Institute of 
//Technology.
//
//This software may be subject to U.S. export control laws. By 
//accepting this software, the user agrees to comply with all 
//applicable U.S. export laws and regulations. User has the 
//responsibility to obtain export licenses, or other export authority 
//as may be required before exporting such information to foreign 
//countries or providing access to foreign persons.
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
     * @param object The description of the object consists of a set of attribute statements
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
