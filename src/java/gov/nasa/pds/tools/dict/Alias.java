//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
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
