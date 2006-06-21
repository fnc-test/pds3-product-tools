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

package gov.nasa.jpl.pds.tools.dict.type;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public interface TypeChecker {
    /**
     * Trys to cast the value to the appropriate type and return the value
     * @param value which must be cast
     * @return value after it has been cast
     * @throws InvalidTypeException thrown when value can not be cast
     */
    public Object cast(String value) throws InvalidTypeException;
    
    /**
     * Checks to make sure that the value does fall below the minimum length
     * length
     * @param value to be checked
     * @param min acts as the bound
     * @throws InvalidLengthException thrown when value falls below minimum
     */
    public void checkMinLength(String value, int min) throws InvalidLengthException;
    
    /**
     * Checks to make sure that the value does not exceed the maximu length
     * @param value to be checked
     * @param max acts as the bound
     * @throws InvalidLengthException thrown when value exceeds maximum
     */
    public void checkMaxLength(String value, int max) throws InvalidLengthException;
}
