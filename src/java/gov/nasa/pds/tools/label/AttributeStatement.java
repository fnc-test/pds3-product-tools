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

package gov.nasa.pds.tools.label;

/**
 * This class represents an attribute assignment in a PDS label file.
 * @author pramirez
 * @version $Revision$
 * 
 */
public class AttributeStatement extends Statement {
    private Value value;
    private String namespace;
    private String elementIdentifier;
    private CommentStatement comment;
    
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
        
        namespace = "";
        if (identifier.indexOf(":") != -1)
            namespace = identifier.substring(0, identifier.indexOf(":"));
        
        if (identifier.indexOf(":") == -1)
            elementIdentifier = identifier;
        else
            elementIdentifier = identifier.substring(identifier.indexOf(":") + 1);
        
        this.value = value;

        comment = null;
    }
    
    /**
     * Gets the namespace for this attribute
     * @return The namespace or "" if none is found.
     */
    public String getNamespace() {
        return namespace;
    }
    
    /**
     * Gets the unqualified identifier for the att
     * @return Returns the element identifier.
     */
    public String getElementIdentifier() {
        return elementIdentifier;
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

    public void attachComment(CommentStatement comment) {
        this.comment = comment;
    }
    
    public boolean hasNamespace() {
        if ("".equals(namespace))
            return true;
        return false;
    }

}
