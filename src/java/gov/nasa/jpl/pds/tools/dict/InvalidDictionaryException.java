//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools.dict;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class InvalidDictionaryException extends Exception {
    private static final long serialVersionUID = -5603994319558334287L;

    public InvalidDictionaryException(String message){
        super(message);
    }
}
