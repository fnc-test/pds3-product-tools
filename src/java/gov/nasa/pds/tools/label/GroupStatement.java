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
    private Map attributes;
    private List pointers;
    private List comments;
    
    /**
     * @param lineNumber
     * @param identifier
     */
    public GroupStatement(int lineNumber, String identifier) {
        super(lineNumber, identifier);
        attributes = new HashMap();
        pointers = new ArrayList();
        comments = new ArrayList();
    }
    
    /**
     * Retrieves the named attribute.
     * @param identifier
     * @return The named AttributeStatement or null if not found.
     */
    public AttributeStatement getAttribute(String identifier) {
        return (AttributeStatement) attributes.get(identifier);
    }
    
    /**
     * Retrieves the attributes of this group.
     * @return The list of AttributeStatment.
     */
    public List getAttributes() {
        return new ArrayList(attributes.values());
    }

    public void addStatement(Statement statement) {
        if (statement instanceof AttributeStatement) 
           attributes.put(statement.getIdentifier(), statement);
        else if (statement instanceof StructurePointer) {
            pointers.add(statement);
            for (Iterator i = ((StructurePointer) statement).getStatements().iterator(); i.hasNext();)
                addStatement((Statement) i.next());
        }
        else if (statement instanceof PointerStatement)
           pointers.add(statement);
        // TODO: else throw error
    }

    /**
     * 
     * @param attribute
     */
    public void addAttribute(AttributeStatement attribute) {
        attributes.put(attribute.getIdentifier(), attribute);
    }
    
    public boolean hasAttribute(String identifier) {
        if (attributes.get(identifier) != null)
            return true;
        return false;
    }
   
    public void attachComment(CommentStatement comment) {
        comments.add(comment);
    }
 
}
