//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class Identifier {
    private String namespace;
    private String localName;
    
    public Identifier(String namespace, String localName) {
        this.namespace = namespace;
        this.localName = localName;
    }
    
    public Identifier(String identifier) {
        namespace = "";
        if (identifier.indexOf(":") != -1)
            namespace = identifier.substring(0, identifier.indexOf(":"));
        
        if (identifier.indexOf(":") == -1)
            localName = identifier;
        else
            localName = identifier.substring(identifier.indexOf(":") + 1);
    }
    
    public String toString() {
        if ("".equals(namespace))
            return localName;
        else
            return namespace + ":" + localName;
    }
    
    public String getLocalName() {
        return localName;
    }
    
    public String getNamespace() {
        return namespace;
    }
}

