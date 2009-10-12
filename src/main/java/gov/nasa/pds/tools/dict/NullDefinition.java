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

package gov.nasa.pds.tools.dict;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class NullDefinition extends Definition {

    private static final long serialVersionUID = 1L;

    public NullDefinition(Dictionary sourceDictionary, int lineNumber,
            DictIdentifier identifier) {
        super(sourceDictionary, lineNumber, identifier);
    }

}
