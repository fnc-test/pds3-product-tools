//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
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
     * @param lineNumber
     * @param identifier
     */
    public GroupStatement(int lineNumber, String identifier) {
        super(lineNumber, identifier);
        statements = new HashMap();
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
 
}
