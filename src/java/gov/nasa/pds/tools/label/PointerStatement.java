//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label;

/**
 * This class is the object representation of a pointer statement in a label.
 * 
 * @author pramirez
 * @version $Revision$
 * 
 */
public class PointerStatement extends Statement {
    protected Value value;
    private CommentStatement comment;

    /**
     * Constructs essentially a null pointer
     * @param lineNumber at which the statement occurs
     * @param identifier of the statement
     */
    protected PointerStatement(int lineNumber, String identifier) {
        this(lineNumber, identifier, null);
    }
    
    /**
     * Constructs a pointer with a value on the right hand side
     * @param lineNumber at which the statement occurs
     * @param identifier of the statement
     * @param value of the assignment
     */
    public PointerStatement(int lineNumber, String identifier, Value value) {
        super(lineNumber, identifier);
        this.value = value; 
        comment = null;  
    }
    
    /**
     * Constructs a pointer with an unknown line number.
     * @param identifier of the statement
     * @param value of the assignment
     */
    public PointerStatement(String identifier, Value value) {
        this(-1, identifier, value);
    }
    
    /**
     * Returns the value portion (right hand side) of the statement.
     * @return value
     */
    public Value getValue() { 
        return value;
    }

    /**
     * Attaches a comment to this pointer
     * @param comment that occurs on same line as this pointer statement
     */
    public void attachComment(CommentStatement comment) {
        this.comment = comment;
    }
    
    /**
     * Returns the comment that occurs on the same line as this pointer assigment
     * @return comment
     */
    public CommentStatement getComment() {
        return comment;
    }

}
