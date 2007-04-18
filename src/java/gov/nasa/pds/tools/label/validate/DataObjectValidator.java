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

package gov.nasa.pds.tools.label.validate;

import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.tools.label.parser.InvalidDescriptionException;
import gov.nasa.pds.tools.label.parser.InvalidObjectException;
import gov.nasa.pds.tools.object.io.DataObjectInputStream;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public interface DataObjectValidator {
    /**
     * Validates the bytes in the object against the decription in the {@link ObjectStatement}
     * @param input bytes of the data object
     * @param object The description of the object consists of a set of attribute statements
     * @throws InvalidObjectException if there is a problem with the bytes in accordance with the description
     * @throws InvalidDescriptionException if the description is insuffcient for the validation to take place
     */
    public void validate(DataObjectInputStream input, ObjectStatement object) throws InvalidObjectException, InvalidDescriptionException;
    
}
