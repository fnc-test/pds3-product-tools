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
public class PointerStatement extends Statement {
    private Value value;

    /**
     * @param lineNumber
     * @param identifier
     */
    protected PointerStatement(int lineNumber, String identifier) {
        this(lineNumber, identifier, null);
    }
    
    public PointerStatement(int lineNumber, String identifier, Value value) {
        super(lineNumber, identifier);
        this.value = value;   
    }
    
    public PointerStatement(String identifier, Value value) {
        this(-1, identifier, value);
    }
    
    public Value getValue() { 
        return value;
    }

}
