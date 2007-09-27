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

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class ObjectStatement extends Statement {
    private Map statements;
    private List comments;

    /**
     * Constructs a new object statement with no attributes or nested objects
     * @param lineNumber Line number of the statement.
     * @param identifier Identifier for the statement.
     */
    public ObjectStatement(int lineNumber, String identifier) {
        this(lineNumber, identifier, new HashMap());
    }
    
    /**
     * Constructs an ObjectStatement with only an identifier
     * @param identifier Identifier of the statement
     */
    public ObjectStatement(String identifier) {
        this(-1, identifier);
    }
    
    /**
     * Constructs an ObjectStatement
     * @param lineNumber Line number of statement
     * @param identifier Identifier of statement
     * @param statements Map of {@link Statement} associated with this object
     */
    public ObjectStatement(int lineNumber, String identifier, Map statements) {
        super(lineNumber, identifier);
        this.statements = statements;
        comments = new ArrayList();
    }
    
    /**
     * Retrieves the list of attributes associated with the ObjectStatement
     * @return The list of AttributeStatement
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
     * Retrieves pointers associated with this object
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
     * Looks to see if this object contains a pointer with the given identifier
     * @param identifier of pointer statement to look for
     * @return flag indicating whether or not the pointer was found
     */
    public boolean hasPointer(String identifier) {
        if (statements.get(identifier) != null) {
            for (Iterator i = ((List) statements.get(identifier)).iterator(); i.hasNext();) {
                Statement statement = (Statement) i.next();
                if (statement instanceof PointerStatement)
                    return true;
            }
        }
        return false;
    }
    
    /**
     * Retrieves the named attribute
     * @param identifier
     * @return The named AttributeStatement or null if not found
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
     * Retrieves the list of objects associated with this object
     * @return The list of ObjectStatement
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
     * Retrieves the named object
     * @param identifier
     * @return The {@link List} of named objects
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
     * Associates a statement with this object
     * @param statement to be added to object
     */
    public void addStatement(Statement statement) { 
        List stmnts = (List) statements.get(statement.getIdentifier());
        if (stmnts == null) {
            stmnts = new ArrayList();
            statements.put(statement.getIdentifier(), stmnts);
        }
        if (statement instanceof IncludePointer) {
            stmnts.add(statement);
            for (Iterator i = ((IncludePointer) statement).getStatements().iterator(); i.hasNext();)
                addStatement((Statement) i.next());
        }
        else 
            stmnts.add(statement);
    }
    
    public boolean hasAttribute(String identifier) {
        if (getAttribute(identifier) == null)
            return false;
        return true;
    }
    
    public boolean hasObject(String identifier) {
        if (getObjects(identifier).size() == 0)
            return false;
        return true;
    }

    public void attachComment(CommentStatement comment) {
        comments.add(comment);
    }
    
    public List getStatements() {
        List statementList = new ArrayList();
        for (Iterator i = statements.values().iterator(); i.hasNext();) {
            statementList.addAll((List) i.next());
        }
        return statementList;
    }

}
