//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools.object.validator;

import org.apache.log4j.Logger;
import gov.nasa.jpl.pds.tools.label.ObjectStatement;
import gov.nasa.jpl.pds.tools.label.parser.InvalidDescriptionException;
import gov.nasa.jpl.pds.tools.label.parser.InvalidObjectException;
import gov.nasa.jpl.pds.tools.label.validate.DataObjectValidator;
import gov.nasa.jpl.pds.tools.object.io.DataObjectInputStream;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class TableValidator implements DataObjectValidator {

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.parser.DataObjectValidator#validate(byte[], gov.nasa.jpl.pds.tools.label.ObjectStatement)
     */
    public void validate(DataObjectInputStream input, ObjectStatement object)
            throws InvalidObjectException, InvalidDescriptionException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.parser.DataObjectValidator#setLogger(java.util.logging.Logger)
     */
    public void setLogger(Logger log) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.parser.DataObjectValidator#getLooger()
     */
    public Logger getLooger() {
        // TODO Auto-generated method stub
        return null;
    }

}
