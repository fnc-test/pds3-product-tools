//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.dict.parser;

/**
 * This excpetion will be thrown if the definition does not follow as 
 * defined in the PDS Data dictionary document.
 * 
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
