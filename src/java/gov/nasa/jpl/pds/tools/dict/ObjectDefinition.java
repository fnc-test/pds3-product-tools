//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools.dict;

import java.util.List;
import java.util.ArrayList;

/**
 * This class represents an object defintion in the PDS data
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
    
    public ObjectDefinition() {
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
    
}
