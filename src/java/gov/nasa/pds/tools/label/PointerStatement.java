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
 * This class is the object representation of a pointer statement in a label.
 * 
 * @author pramirez
 * @version $Revision$
 * 
 */
public class PointerStatement extends Statement implements PointerType {
    protected Value value;
    private CommentStatement comment;
    private int pointerType;

    /**
     * Constructs essentially a null pointer
     * @param lineNumber at which the statement occurs
     * @param identifier of the statement
     */
    protected PointerStatement(int pointerType, int lineNumber, String identifier) {
        this(pointerType, lineNumber, identifier, null);
    }
    
    /**
     * Constructs a pointer with a value on the right hand side
     * @param lineNumber at which the statement occurs
     * @param identifier of the statement
     * @param value of the assignment
     */
    protected PointerStatement(int pointerType, int lineNumber, String identifier, Value value) {
        super(lineNumber, identifier);
        this.value = value; 
        this.pointerType = pointerType;
        comment = null;  
    }
    
    /**
     * Constructs a pointer with an unknown line number.
     * @param identifier of the statement
     * @param value of the assignment
     */
    protected PointerStatement(String identifier, Value value) {
        this(UNDEFINED, -1, identifier, value);
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
    
    /**
     * Indicates the type of pointer that this pointer statement represents. See {@link PointerType}
     * @return type of pointer
     */
    public int getPointerType() {
        return pointerType;
    }

}
