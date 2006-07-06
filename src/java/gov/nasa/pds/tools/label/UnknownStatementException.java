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
public class UnknownStatementException extends Exception {
    private static final long serialVersionUID = -8845631683783154548L; 

    public UnknownStatementException(String message) {
        super(message);
    }
}
