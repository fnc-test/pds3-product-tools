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
 * This class represents a PDS label.
 * @author pramirez
 * @version $Revision$
 * 
 */
public class Label {
    private int labelType;
    private Map objects;
    private Map groups;
    private Map attributes;
    private Map pointers;
    
    /**
     * Constructs an object representation of a PDS label.
     *
     */
    public Label() {
        objects = new HashMap();
        groups = new HashMap();
        attributes = new HashMap();
        pointers = new HashMap();
    }

    /**
     * Retrieves a statement that with the identifier
     * @param identifier Identifies the statement to retrieve
     * @return The named statement or null if not found
     */
    public Statement getStatement(String identifier) {
        Statement statement = null;
        //Check all statements if found return
        statement = (Statement) attributes.get(identifier);
        if (statement != null)
            return statement;
        statement = (Statement) objects.get(identifier);
        if (statement != null)
            return statement;
        statement = (Statement) groups.get(identifier);
        if (statement != null)
            return statement;
        //At this point the either it is a pointer or null
        return (Statement) pointers.get(identifier);
    }
    
    /**
     * Retrieves the attribute with the identifier or null if not found
     * @param identifier of attribute to find
     * @return attribute or null
     */
    public AttributeStatement getAttribute(String identifier) {
        return (AttributeStatement) attributes.get(identifier);
    }
    
    /**
     * Retrieves the groups with the identifier or null if not found
     * @param identifier of group to find
     * @return group or null
     */
    public GroupStatement getGroup(String identifier) {
        return (GroupStatement) groups.get(identifier);
    }
  
    /**
     * Retrieves the object with the identifier or null if not found
     * @param identifier of object to find
     * @return object or null
     */
    public ObjectStatement getObject(String identifier) {
        return (ObjectStatement) objects.get(identifier);
    }
 
    /**
     * Retrieves the pointer with the identifier or null if not found
     * @param identifier of pointer to find
     * @return pointer or null
     */
    public PointerStatement getPointer(String identifier) {
        return (PointerStatement) pointers.get(identifier);
    }
 
    /**
     * Retrieves the statements associated with this label
     * @return list of {@link Statement}
     */
    public List getStatements() { 
        List statements = new ArrayList();
        
        statements.addAll(attributes.values());
        statements.addAll(objects.values());
        statements.addAll(pointers.values());
        statements.addAll(groups.values());
        
        return statements;
    }
 
    /**
     * Retrieves objects associated with this label
     * @return List of {@link ObjectStatement}
     */
    public List getObjects() {
        return new ArrayList(objects.values());
    }
    
    /**
     * Retrieves groups associated with this label
     * @return list of {@link GroupStatement}
     */
    public List getGroups() {
        return new ArrayList(groups.values());
    }
    
    /**
     * Retrieves attributes associated with this label
     * @return list of {@link AttributeStatement}
     */
    public List getAttributes() {
        return new ArrayList(attributes.values());
    }
    
    /**
     * Retrieves pointers associated with this label
     * @return list of {@link PointerStatement}
     */
    public List getPointers() {
        return new ArrayList(pointers.values());
    }
 
    /**
     * Associates a statement with this label
     * @param statement to be added to label
     */
    public void addStatement(Statement statement) {
        if (statement instanceof AttributeStatement)
            attributes.put(statement.getIdentifier(), statement);
        else if (statement instanceof ObjectStatement)
            objects.put(statement.getIdentifier(), statement);
        else if (statement instanceof GroupStatement)
            groups.put(statement.getIdentifier(), statement);
        else if (statement instanceof PointerStatement)
            pointers.put(statement.getIdentifier(), statement);
    }

    /**
     * Returns the type of label, see {@link LabelType} for the types of label.
     * @return type of label
     */
    public int getLabelType() {
        return labelType;
    }
    
    /**
     * Sets the type of label
     * @param labelType of this label
     */
    public void setType(int labelType) {
        this.labelType = labelType;
    }
}
