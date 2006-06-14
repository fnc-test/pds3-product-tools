//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools.dict.parser;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class MalformedDefinitionException extends Exception {
    private static final long serialVersionUID = 6035832581904662968L;

    public MalformedDefinitionException(String message) {
        super(message);
    }
}
