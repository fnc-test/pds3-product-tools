//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collections;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class ObjectStatement extends Statement {
    private Map attributes;
    private Map objects;
    private List pointers;
    private List comments;

    /**
     * Constructs a new object statement with no attributes or nested objects
     * @param lineNumber Line number of the statement.
     * @param identifier Identifier for the statement.
     */
    public ObjectStatement(int lineNumber, String identifier) {
        this(lineNumber, identifier, Collections.EMPTY_LIST, Collections.EMPTY_LIST, new ArrayList());
    }
    
    /**
     * Constructs an ObjectStatement with only an identifier
     * @param identifier Identifier of the statement
     */
    public ObjectStatement(String identifier) {
        this(identifier, Collections.EMPTY_LIST, Collections.EMPTY_LIST, new ArrayList());
    }
    
    /**
     * Constructs an ObjectStatement with no line number
     * @param identifier
     * @param attributes
     * @param objects
     */
    public ObjectStatement(String identifier, List attributes, List objects, List pointers) {
        this(-1, identifier, attributes, objects, pointers);
    }
    
    /**
     * Constructs an ObjectStatement
     * @param lineNumber Line number of statement
     * @param identifier Identifier of statement
     * @param attributes List of {@link AttributeStatement} associated with this statement
     * @param objects List of {@link ObjectStatement} associated with this statement
     */
    public ObjectStatement(int lineNumber, String identifier, List attributes, List objects, List pointers) {
        super(lineNumber, identifier);
        this.attributes = new HashMap();
        this.objects = new HashMap();
        this.pointers = pointers;
        
        for (Iterator i = attributes.iterator(); i.hasNext();) {
            AttributeStatement attribute = (AttributeStatement) i.next();
            this.attributes.put(attribute.getIdentifier(), attribute);
        }
        
        for (Iterator i = objects.iterator(); i.hasNext();) {
            ObjectStatement object = (ObjectStatement) i.next();
            this.objects.put(object.getIdentifier(), object);
        }
        
        comments = new ArrayList();
    }
    
    /**
     * Retrieves the list of attributes associated with the ObjectStatement
     * @return The list of AttributeStatement
     */
    public List getAttributes() {
        return new ArrayList(attributes.values());
    }
    
    /**
     * Retrieves the named attribute
     * @param identifier
     * @return The named AttributeStatement or null if not found
     */
    public AttributeStatement getAttribute(String identifier) {
        return (AttributeStatement) attributes.get(identifier);
    }
    
    /**
     * Retrieves the list of objects associated with this object
     * @return The list of ObjectStatement
     */
    public List getObjects() {
        return new ArrayList(objects.values());
    }
    
    /**
     * Retrieves the named object
     * @param identifier
     * @return The named ObjectStatement or null if not found
     */
    public ObjectStatement getObject(String identifier) {
        return (ObjectStatement) objects.get(identifier);
    }
    
    /**
     * Add an attribute to this object. Will overwrite an 
     * attribute with the same identifier.
     * @param attribute The AttributeStatment to add
     */
    public void addAttribute(AttributeStatement attribute) {
        attributes.put(attribute.getIdentifier(), attribute);
    }
    
    /**
     * Add an child object to this object. Willl overwrite an
     * object with the same identifier.
     * @param object The ObjectStatment to add
     */
    public void addObject(ObjectStatement object) {
        objects.put(object.getIdentifier(), object);
    }
    
    public void addStatement(Statement statement) {
        if (statement instanceof ObjectStatement)
            objects.put(statement.getIdentifier(), statement);
        else if (statement instanceof AttributeStatement)
            attributes.put(statement.getIdentifier(), statement);
        else if (statement instanceof PointerStatement)
            pointers.add(statement);
        //TODO throw illegal argument exception
    }
    
    public boolean hasAttribute(String identifier) {
        if (attributes.get(identifier) == null)
            return false;
        return true;
    }
    
    public boolean hasObject(String identifier) {
        if (objects.get(identifier) == null)
            return false;
        return true;
    }

    public void attachComment(CommentStatement comment) {
        comments.add(comment);
    }

}
