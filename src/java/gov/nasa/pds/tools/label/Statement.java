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
 * This class represents a statement in a PDS label.
 * 
 * @author pramirez
 * @version $Revision$
 * 
 */
public abstract class Statement implements Comparable {  
    protected int lineNumber;
    protected String identifier;
    protected String filename;
    protected String context;
    
    /**
     * Constructs a statement
     * @param lineNumber on which this statement begins
     * @param identifier which uniquely identifies this statement
     */
    public Statement(int lineNumber, String identifier) {
        this(null, lineNumber, identifier);
    }
    
    public Statement(String filename, int lineNumber, String identifier) {
        this(null, filename, lineNumber, identifier);
    }
    
    public Statement(String context, String filename, int lineNumber, String identifier) {
        this.lineNumber = lineNumber;
        this.identifier = identifier;
        this.filename = filename;
        this.context = context;
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
    
    public String getFilename() {
        return filename;
    }
    
    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContext() {
        return context;
    }
    
    public void setContext(String context) {
        this.context = context;
    }
    
    public abstract void attachComment(CommentStatement commet);
    
    public int compareTo(Object o) {
        return this.getLineNumber() - ((Statement) o).getLineNumber();
    }
}
