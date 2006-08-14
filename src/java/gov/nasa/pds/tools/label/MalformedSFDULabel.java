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
public class MalformedSFDULabel extends Exception {
    private static final long serialVersionUID = -5700147636995751822L;

    public MalformedSFDULabel(String message) {
        super(message);
    }
}
