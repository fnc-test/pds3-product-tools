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

import static gov.nasa.pds.tools.dict.DictionaryTokens.OBJECT_TYPE_GENERIC_GROUP;

/**
 * This class models a group definition. Groups can only contain optional and
 * required elements. This class will only contain the identifiers of these
 * elements.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class GroupDefinition extends ContainerDefinition {

    private static final long serialVersionUID = 243477744455770834L;

    public GroupDefinition(final Dictionary sourceDictionary, int lineNumber,
            DictIdentifier identifier) {
        super(sourceDictionary, lineNumber, identifier);
        this.objectType = OBJECT_TYPE_GENERIC_GROUP;
    }

}
