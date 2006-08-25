//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class represents a PDS label.
 * @author pramirez
 * @version $Revision$
 * 
 */
public class Label {
    private int labelType;
    private Map statements;
    private List pointers;
    
    /**
     * Constructs an object representation of a PDS label.
     *
     */
    public Label() {
        statements = new HashMap();
        pointers = new ArrayList();
    }

    /**
     * Retrieves a statement with the identifier
     * @param identifier Identifies the statement to retrieve
     * @return The named statement or null if not found
     */
    public Statement getStatement(String identifier) {
        return (Statement) statements.get(identifier);
    }
    
    /**
     * Retrieves the attribute with the identifier or null if not found
     * @param identifier of attribute to find
     * @return attribute or null
     */
    public AttributeStatement getAttribute(String identifier) {
        Statement statement = (Statement) statements.get(identifier);
        if (statement instanceof AttributeStatement)
            return (AttributeStatement) statement;
        return null;
    }
    
    /**
     * Retrieves the groups with the identifier or null if not found
     * @param identifier of group to find
     * @return group or null
     */
    public GroupStatement getGroup(String identifier) {
        Statement statement = (Statement) statements.get(identifier);
        if (statement instanceof GroupStatement)
            return (GroupStatement) statement;
        return null;
    }
  
    /**
     * Retrieves the object with the identifier or null if not found
     * @param identifier of object to find
     * @return object or null
     */
    public ObjectStatement getObject(String identifier) {
        Statement statement = (Statement) statements.get(identifier);
        if (statement instanceof ObjectStatement)
            return (ObjectStatement) statement;
        return null;
    }
 
    /**
     * Retrieves the statements associated with this label
     * @return list of {@link Statement}
     */
    public List getStatements() { 
        return new ArrayList(statements.values());
    }
 
    /**
     * Retrieves objects associated with this label
     * @return List of {@link ObjectStatement}
     */
    public List getObjects() {
        List objects = new ArrayList(); 
        
        for (Iterator i = statements.values().iterator(); i.hasNext();) {
            Statement s = (Statement) i.next();
            if (s instanceof ObjectStatement)
                objects.add(s);
        }
        
        return objects;
    }
    
    /**
     * Retrieves groups associated with this label
     * @return list of {@link GroupStatement}
     */
    public List getGroups() {
        List groups = new ArrayList(); 
        
        for (Iterator i = statements.values().iterator(); i.hasNext();) {
            Statement s = (Statement) i.next();
            if (s instanceof GroupStatement)
                groups.add(s);
        }
        
        return groups;
    }
    
    /**
     * Retrieves attributes associated with this label
     * @return list of {@link AttributeStatement}
     */
    public List getAttributes() {
        List attributes = new ArrayList(); 
        
        for (Iterator i = statements.values().iterator(); i.hasNext();) {
            Statement s = (Statement) i.next();
            if (s instanceof AttributeStatement)
                attributes.add(s);
        }
        
        return attributes;
    }
    
    /**
     * Retrieves pointers associated with this label
     * @return list of {@link PointerStatement}
     */
    public List getPointers() {
        return pointers;
    }
 
    /**
     * Associates a statement with this label
     * @param statement to be added to label
     */
    public void addStatement(Statement statement) {
        if (statement instanceof PointerStatement)
            pointers.add(statement);
        else
            statements.put(statement.getIdentifier(), statement);
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
    public void setLabelType(int labelType) {
        this.labelType = labelType;
    }
}
