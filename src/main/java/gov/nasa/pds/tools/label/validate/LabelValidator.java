// Copyright 2006-2010, by the California Institute of Technology.
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

import gov.nasa.pds.tools.label.Label;

/**
 * This interface is a means to define an extension point for the parser to
 * perform further validation. This type of validation is outside the realm of
 * the syntax defined by the grammar and data dictionary. An example of this
 * would be required file characteristic elements.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public interface LabelValidator {
    /**
     * Method signature for checking to see if a label is valid.
     * 
     * @param label
     *            object returned from parsing that represents the PDS label
     * @return flag indicating whether or not the step in validation was passed.
     */
    public boolean validate(Label label);
}
