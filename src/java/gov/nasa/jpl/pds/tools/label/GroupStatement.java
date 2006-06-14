//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools.label;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class GroupStatement extends Statement {
    private Map attributes;
    
    /**
     * @param lineNumber
     * @param identifier
     */
    protected GroupStatement(int lineNumber, String identifier) {
        super(lineNumber, identifier);
        attributes = new HashMap();
    }
    
    /**
     * Retrieves the named attribute.
     * @param identifier
     * @return The named AttributeStatement or null if not found.
     */
    public AttributeStatement getAttribute(String identifier) {
        return (AttributeStatement) attributes.get(identifier);
    }
    
    /**
     * Retrieves the attributes of this group.
     * @return The list of AttributeStatment.
     */
    public List getAttributes() {
        return new ArrayList(attributes.values());
    }

    /**
     * 
     * @param attribute
     */
    public void addAttribute(AttributeStatement attribute) {
        attributes.put(attribute.getIdentifier(), attribute);
    }
    
    public boolean hasAttribute(String identifier) {
        if (attributes.get(identifier) != null)
            return true;
        return false;
    }
    
}
