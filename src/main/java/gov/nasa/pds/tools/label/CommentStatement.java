// Copyright 2006-2010, by the California Institute of Technology.
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

import gov.nasa.pds.tools.dict.parser.DictIDFactory;

import org.antlr.runtime.Token;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class CommentStatement extends Statement {

    public final static String COMMENT_ID = "COMMENT-"; //$NON-NLS-1$

    private String text;

    public CommentStatement(final Label sourcelabel, final int lineNumber,
            final String identifier, final String text) {
        super(sourcelabel, lineNumber, DictIDFactory
                .createCommentDefId(identifier));
        this.text = text;
    }

    public CommentStatement(final Label sourcelabel, final Token commentToken,
            final String identifier) {
        super(sourcelabel, commentToken.getLine(), DictIDFactory
                .createCommentDefId(identifier));
        this.text = commentToken.getText();
    }

    /**
     * @param lineNumber
     * @param identifier
     */
    public CommentStatement(final Label sourcelabel, int lineNumber,
            String identifier) {
        this(sourcelabel, lineNumber, identifier, ""); //$NON-NLS-1$
    }

    public CommentStatement(final Label sourcelabel, int lineNumber) {
        this(sourcelabel, lineNumber, COMMENT_ID + lineNumber, ""); //$NON-NLS-1$
    }

    public String getText() {
        return this.text;
    }

    @Override
    public void attachComment(final Token commentToken) {
        throw new RuntimeException("Comments may not be added to comments");
    }
}
