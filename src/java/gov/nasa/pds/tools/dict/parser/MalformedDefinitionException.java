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
