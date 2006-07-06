//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class CommentStatement extends Statement {
    public final static String COMMENT_ID = "COMMENT-";
    private String comment;

    public CommentStatement(int lineNumber, String identifier, String comment) {
        super(lineNumber, identifier);
        this.comment = comment;
    }
    
    /**
     * @param lineNumber
     * @param identifier
     */
    public CommentStatement(int lineNumber, String identifier) {
        this(lineNumber, identifier, "");
    }
    
    public CommentStatement(int lineNumber) {
        this(lineNumber, COMMENT_ID + lineNumber, "");
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public String getComment() {
        return comment;
    }

    public void attachComment(CommentStatement comment) {
        //TODO: throw some error as one should not be able to attach comments to comments
    }
}
