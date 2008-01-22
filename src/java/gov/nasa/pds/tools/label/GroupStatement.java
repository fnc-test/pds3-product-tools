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

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class GroupStatement extends Statement {
    private Map statements;
    private List comments;
    
    /**
     * Constructs an empty group statement
     * @param lineNumber at which this statement occurs
     * @param identifier of the statement
     */
    public GroupStatement(int lineNumber, String identifier) {
    	this(lineNumber, identifier, new HashMap());
    }
    
    /**
     * Constructs a group statement that contains the given statements
     * @param lineNumber at which this statement occurs
     * @param indentifier of the statement
     * @param statements contained within this group statement
     */
    public GroupStatement(int lineNumber, String identifier, Map statements) {
    	super(lineNumber, identifier);
    	this.statements = statements;
    	comments = new ArrayList();
    }
    
    /**
     * Retrieves the named attribute.
     * @param identifier
     * @return The named AttributeStatement or null if not found.
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
     * Retrieves the attributes of this group.
     * @return The list of AttributeStatment.
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
        else if (statement instanceof PointerStatement || statement instanceof AttributeStatement)
            stmnts.add(statement);
        //else TODO Throw error
    }

    public boolean hasAttribute(String identifier) {
        if (getAttribute(identifier) == null)
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
