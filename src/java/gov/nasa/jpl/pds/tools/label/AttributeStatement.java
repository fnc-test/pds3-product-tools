//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools.label;

/**
 * This class represents an attribute assignment in a PDS label file.
 * @author pramirez
 * @version $Revision$
 * 
 */
public class AttributeStatement extends Statement {
    private Value value;
    
    /**
     * Constructs a new attribute statement with no value
     * @param lineNumber Line on which the statement starts
     * @param identifier Uniquely identifies the statement
     */
    protected AttributeStatement(int lineNumber, String identifier) {
        this(lineNumber, identifier, null);
    }
    
    /**
     * Constructs a new attribute statement with no line number or value
     * @param identifier Uniquely identifies the statement
     */
    public AttributeStatement(String identifier) {
        this(identifier, null);
    }
    
    /**
     * Constructs a new attribute statement with no line number
     * @param identifier Uniquely identifies the statement
     * @param value {@link Value} of the attribute
     */
    public AttributeStatement(String identifier, Value value) {
        this(-1, identifier, value);
    }
    
    /**
     * 
     * @param lineNumber Line on which the statement starts
     * @param identifier Uniquely identifies the statement
     * @param value {@link Value} of the attribute
     */
    public AttributeStatement(int lineNumber, String identifier, Value value) {
        super(lineNumber, identifier);
        this.value = value;
    }
    
    /**
     * Gets the namespace for this attribute
     * @return The namespace or "" if none is found.
     */
    public String getNamespace() {
        String namespace = "";
        
        if (identifier.indexOf(":") != -1)
            namespace = identifier.substring(0, identifier.indexOf(":"));
        
        return namespace;
    }
    
    /**
     * Gets the unqualified identifier for the att
     * @return
     */
    public String getElementIdentifier() {
        if (identifier.indexOf(":") == -1)
            return identifier;
        return identifier.substring(identifier.indexOf(":") + 1);
    }

    /**
     * Retrieves the value of the attribute
     * @return {@link Value} of the attribute
     */
    public Value getValue() {
        return value;
    }
    
    /**
     * Sets the value for this attribute
     * @param value {@link Value} of the attribute
     */
    public void setValue(Value value) {
        this.value = value;
    }
}
