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
