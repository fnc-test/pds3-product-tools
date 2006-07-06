//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label;

/**
 * This class represents a statement in a PDS label.
 * 
 * @author pramirez
 * @version $Revision$
 * 
 */
public abstract class Statement {  
    protected int lineNumber;
    protected String identifier;
    
    /**
     * Constructs a statement
     * @param lineNumber on which this statement begins
     * @param identifier which uniquely identifies this statement
     */
    public Statement(int lineNumber, String identifier) {
        this.lineNumber = lineNumber;
        this.identifier = identifier;
    }
    
    /**
     * Retrieves the line for this statement
     * @return The line on which the statment starts
     */
    public int getLineNumber() {
        return lineNumber;
    }
    
    /**
     * Retrieves the identifier for the statement
     * @return unique identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    public abstract void attachComment(CommentStatement commet);
}
