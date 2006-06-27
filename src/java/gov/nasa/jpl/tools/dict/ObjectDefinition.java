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

package gov.nasa.pds.tools.dict;

import java.util.List;
import java.util.ArrayList;

/**
 * This class represents an object definition in the PDS data
 * dictionary. 
 * 
 * @author pramirez
 * @version $Revision$
 * 
 */
public class ObjectDefinition extends Definition {
    private List requiredElements;
    private List optionalElements;
    private List requiredObjects;
    private List optionalObjects;
    
    public ObjectDefinition(String identifier) {
        super(identifier);
        requiredElements = new ArrayList();
        optionalElements = new ArrayList();
        requiredObjects = new ArrayList();
        optionalObjects = new ArrayList();
    }
    
    /**
     * @return Returns the names optional elements.
     */
    public List getOptionalElements() {
        return optionalElements;
    }
    
    /**
     * @param optionalElements The names of optional elements.
     */
    public void setOptionalElements(List optionalElements) {
        this.optionalElements = optionalElements;
    }
    
    /**
     * @return Returns the name of optional objects.
     */
    public List getOptionalObjects() {
        return optionalObjects;
    }
    
    /**
     * @param optionalObjects The names of optional objects.
     */
    public void setOptionalObjects(List optionalObjects) {
        this.optionalObjects = optionalObjects;
    }
    
    /**
     * @return Returns the names of required elements.
     */
    public List getRequiredElements() {
        return requiredElements;
    }
    
    /**
     * @param requiredElements The names of required elements.
     */
    public void setRequiredElements(List requiredElements) {
        this.requiredElements = requiredElements;
    }
    
    /**
     * @return Returns the names of required objects.
     */
    public List getRequiredObjects() {
        return requiredObjects;
    }
    
    /**
     * @param requiredObjects The names of required objects.
     */
    public void setRequiredObjects(List requiredObjects) {
        this.requiredObjects = requiredObjects;
    }
    
  
    /**
     * 
     * @param identifier
     * @return true if element is required otherwise false.
     */
    public boolean isElementRequired(String identifier) {
        return requiredElements.contains(identifier);
    }
    
    /**
     * 
     * @param identifier
     * @return true if element can occur otherwise false.
     */
    public boolean isElementPossible(String identifier) {
        boolean exists = requiredElements.contains(identifier);
        if (exists)
            return true;
        return optionalElements.contains(identifier);
    }
    
    /**
     * 
     * @param identifier
     * @return true if element is optional otherwise false.
     */
    public boolean isElementOptional(String identifier) {
        return optionalElements.contains(identifier);
    }
    
    /**
     * 
     * @param identifier
     * @return true if the object is required otherwise false.
     */
    public boolean isObjectRequired(String identifier) {
        return requiredObjects.contains(identifier);
    }
    
    /**
     * 
     * @param identifier
     * @return true if the object is optional otherwise false.
     */
    public boolean isObjectOptional(String identifier) {
        return optionalObjects.contains(identifier);
    }
    
    /**
     * 
     * @param identifier
     * @return true if the object can occur.
     */
    public boolean isObjectPossible(String identifier) {
        boolean exists = requiredObjects.contains(identifier);
        if (exists)
            return true;
        return optionalObjects.contains(identifier);
    }
    
}
