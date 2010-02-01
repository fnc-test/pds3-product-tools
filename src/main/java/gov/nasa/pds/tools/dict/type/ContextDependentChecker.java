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

package gov.nasa.pds.tools.dict.type;

import gov.nasa.pds.tools.label.AttributeStatement;

/**
 * This class is used to provide a means to perform checks on values in the
 * dictionary that are specified as ContextDependent.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class ContextDependentChecker extends LengthChecker implements
        TypeChecker {

    /**
     * This method does no checking right now and simply passes back the
     * original value. It should follow the rules set forth in the pdsdd and
     * data dictionary document for what is an allowable ContextDependent value.
     * 
     * @throws InvalidTypeException
     *             if the value can not be interpreted as ContextDependent
     * 
     * @see gov.nasa.pds.tools.dict.type.TypeChecker#cast(String,
     *      AttributeStatement)
     * 
     */
    public Object cast(String value, AttributeStatement attribute)
            throws InvalidTypeException {
        return value;
    }

}