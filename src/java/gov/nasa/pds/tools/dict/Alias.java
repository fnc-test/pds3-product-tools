// Copyright 2006-2008, by the California Institute of 
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

package gov.nasa.pds.tools.dict;

import gov.nasa.pds.tools.label.Identifier;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class Alias {
    private String context;
    private Identifier identifier;
    
    public Alias(String identifier) {
        this("", new Identifier(identifier));
    }
    
    public Alias(Identifier identifier) {
        this("", identifier);
    }
    
    public Alias(String context, String identifier) {
        this(context, new Identifier(identifier));
    }
    
    public Alias(String context, Identifier identifier) {
        this.context = context;
        this.identifier = identifier;
    }
    
    public boolean hasContext() {
        return ("".equals(context)) ? false : true;
    }
    
    public String getContext() {
        return context;
    }
    
    public Identifier getIdentifier() {
        return identifier;
    }
    
    public String toString() {
        if (!"".equals(context))
            return context + "." + identifier.toString();
        return identifier.toString();
    }
}
