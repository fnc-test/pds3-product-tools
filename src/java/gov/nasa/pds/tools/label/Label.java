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
public class Label implements LabelType {
    private volatile int labelType;
    private Map statements;
    
    /**
     * Constructs an object representation of a PDS label.
     *
     */
    public Label() {
        statements = new HashMap();
        labelType = UNDEFINED;
    }

    /**
     * Retrieves a statement with the identifier
     * @param identifier Identifies the statement to retrieve
     * @return The named statement or null if not found
     */
    public List getStatement(String identifier) {
        return (List) statements.get(identifier);
    }
    
    /**
     * Retrieves the attribute with the identifier or null if not found
     * @param identifier of attribute to find
     * @return attribute or null
     */
    public AttributeStatement getAttribute(String identifier) {
        AttributeStatement attribute = null;
        
        if (statements.get(identifier) != null) {
            for (Iterator i = ((List) statements.get(identifier)).iterator(); i.hasNext() && attribute == null;) {
                Statement statement = (Statement) i.next();
                if (statement instanceof AttributeStatement)
                    attribute = (AttributeStatement) statement;
            }
        }
        
        return attribute;
    }
    
    /**
     * Retrieves the groups with the identifier or null if not found
     * @param identifier of group to find
     * @return {@link List} of {@link GroupStatement}
     */
    public List getGroups(String identifier) {
        List groups = new ArrayList();
        
        if (statements.get(identifier) != null) {
            for (Iterator i = ((List) statements.get(identifier)).iterator(); i.hasNext();) {
                Statement statement = (Statement) i.next();
                if (statement instanceof GroupStatement)
                    groups.add(statement);
            }
        }
        
        return groups;
    }
  
    /**
     * Retrieves the object with the identifier or null if not found
     * @param identifier of object to find
     * @return {@link List} of {@link ObjectStatement}
     */
    public List getObjects(String identifier) {
        List objects = new ArrayList();
        
        if (statements.get(identifier) != null) {
            for (Iterator i = ((List) statements.get(identifier)).iterator(); i.hasNext();) {
                Statement statement = (Statement) i.next();
                if (statement instanceof ObjectStatement)
                    objects.add(statement);
            }
        }
        
        return objects;
    }
 
    /**
     * Retrieves the statements associated with this label
     * @return {@link List} of {@link Statement}
     */
    public List getStatements() { 
        List results = new ArrayList();
        for (Iterator i = statements.values().iterator(); i.hasNext();)
            results.addAll((List) i.next());
        return results;
    }
 
    /**
     * Retrieves objects associated with this label
     * @return List of {@link ObjectStatement}
     */
    public List getObjects() {
        List objects = new ArrayList(); 
        for (Iterator i = statements.values().iterator(); i.hasNext();) {
            for (Iterator s = ((List) i.next()).iterator(); s.hasNext();) {
                Statement statement = (Statement) s.next();
                if (statement instanceof ObjectStatement)
                    objects.add(statement);
            }
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
            for (Iterator s = ((List) i.next()).iterator(); s.hasNext();) {
                Statement statement = (Statement) s.next();
                if (statement instanceof GroupStatement)
                    groups.add(statement);
            }
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
            for (Iterator s = ((List) i.next()).iterator(); s.hasNext();) {
                Statement statement = (Statement) s.next();
                if (statement instanceof AttributeStatement)
                    attributes.add(statement);
            }
        }
        return attributes;
    }
    
    /**
     * Retrieves pointers associated with this label
     * @return list of {@link PointerStatement}
     */
    public List getPointers() {
        List pointers = new ArrayList();
        for (Iterator i = statements.values().iterator(); i.hasNext();) {
            for (Iterator s = ((List) i.next()).iterator(); s.hasNext();) {
                Statement statement = (Statement) s.next();
                if (statement instanceof PointerStatement)
                    pointers.add(statement);
            }
        }
        return pointers;
    }
 
    /**
     * Associates a statement with this label
     * @param statement to be added to label
     */
    public synchronized void addStatement(Statement statement) { 
        labelType = UNDEFINED;
        List stmnts = (List) statements.get(statement.getIdentifier());
        if (stmnts == null) {
            stmnts = new ArrayList();
            statements.put(statement.getIdentifier(), stmnts);
        }
        if (statement instanceof StructurePointer) {
            stmnts.add(statement);
            for (Iterator i = ((StructurePointer) statement).getStatements().iterator(); i.hasNext();)
                addStatement((Statement) i.next());
        }
        else 
            stmnts.add(statement);
    }

    /**
     * Returns the type of label, see {@link LabelType} for the types of label.
     * @return type of label
     */
    public synchronized int getLabelType() {
        //Check to see if flag has been set if not figure it out and set it
        if (labelType == UNDEFINED) {
            List pointers = getPointers();
            for (Iterator i = pointers.iterator(); i.hasNext() && labelType == UNDEFINED;) {
                PointerStatement pointer = (PointerStatement) i.next();
                if (pointer.getPointerType() == PointerType.DATA_LOCATION && pointer.getValue() instanceof Numeric)
                    labelType = LabelType.ATTACHED;
                else
                    labelType = LabelType.DETACHED;
            }
            //If label type is still undefined we need to check if its a combined detached label
            if (labelType == UNDEFINED && getObjects("FILE").size() != 0)
                labelType = LabelType.COMBINED_DETACHED;
        }
        
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
