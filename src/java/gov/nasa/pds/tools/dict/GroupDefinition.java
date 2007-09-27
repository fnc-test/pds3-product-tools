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

package gov.nasa.pds.tools.dict;

import java.util.List;
import java.util.ArrayList;

/**
 * This class models a group definition. Groups can only contain optional
 * and required elements. This class will only contain the identifiers of these 
 * elements.
 * @author pramirez
 * @version $Revision$
 * 
 */
public class GroupDefinition extends Definition implements DictionaryTokens {
    private List requiredElements;
    private List optionalElements;
    
    public GroupDefinition(String identifier) {
        super(identifier);
        requiredElements = new ArrayList();
        optionalElements = new ArrayList();
        objectType = OBJECT_TYPE_GENERIC_GROUP;
    }
    
    /**
     * Lists the optional elements that can appear in this group
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
     * 
     * @param identifier
     * @return Returns the required elements.
     */
    public boolean mustHaveElement(String identifier) {
        return requiredElements.contains(identifier);
    }
    
    /**
     * 
     * @param identifier
     * @return Returns the possible elements.
     */
    public boolean canHaveElement(String identifier) {
        boolean exists = false;
        exists = requiredElements.contains(identifier);
        if (exists)
            return exists;
        return optionalElements.contains(identifier);
    }
    
    public boolean hasElement(String identifier) {
        if (requiredElements.contains(identifier) || optionalElements.contains(identifier)) 
            return true;
        return false;
    }
}
