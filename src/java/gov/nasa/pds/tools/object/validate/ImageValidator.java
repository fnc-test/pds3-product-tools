// Copyright 2006, by the California Institute of 
// Technology. ALL RIGHTS RESERVED. United States Government 
// Sponsorship acknowledged. Any commercial use must be negotiated with 
// the Office of Technology Transfer at the California Institute of 
// Technology.
//
// This software may be subject to U.S. export control laws. By 
// accepting this software, the user agrees to comply with all 
// applicable U.S. export laws and regulations. User has the 
// responsibility to obtain export licenses, or other export authority 
// as may be required before exporting such information to foreign 
// countries or providing access to foreign persons.
//
// $Id$ 
//

package gov.nasa.pds.tools.object.validate;

import java.util.logging.Logger;

import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.tools.label.parser.InvalidDescriptionException;
import gov.nasa.pds.tools.label.parser.InvalidObjectException;
import gov.nasa.pds.tools.label.validate.DataObjectValidator;
import gov.nasa.pds.tools.object.io.DataObjectInputStream;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class ImageValidator implements DataObjectValidator {
    private static Logger log = Logger.getLogger(ImageValidator.class.getName());

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.validate.DataObjectValidator#validate(gov.nasa.jpl.pds.tools.object.io.DataObjectInputStream, gov.nasa.jpl.pds.tools.label.ObjectStatement)
     */
    public void validate(DataObjectInputStream input, ObjectStatement object)
            throws InvalidObjectException, InvalidDescriptionException {
        // TODO Auto-generated method stub

    }

}
