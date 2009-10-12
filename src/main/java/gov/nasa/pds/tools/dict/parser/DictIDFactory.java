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

package gov.nasa.pds.tools.dict.parser;

import gov.nasa.pds.tools.dict.DictIdentifier;
import gov.nasa.pds.tools.dict.ElementDefinition;
import gov.nasa.pds.tools.dict.GroupDefinition;
import gov.nasa.pds.tools.dict.NullDefinition;
import gov.nasa.pds.tools.dict.ObjectDefinition;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class DictIDFactory {

    public static DictIdentifier createObjectDefId(final String identifier) {
        return new DictIdentifier(identifier, ObjectDefinition.class);
    }

    public static DictIdentifier createGroupDefId(final String identifier) {
        return new DictIdentifier(identifier, GroupDefinition.class);
    }

    // NOTE: ties to an attribute statement
    public static DictIdentifier createElementDefId(final String identifier) {
        return new DictIdentifier(identifier, ElementDefinition.class);
    }

    // not a dictionary id but provides context
    public static DictIdentifier createCommentDefId(final String identifier) {
        return new DictIdentifier(identifier, NullDefinition.class);
    }

    // not a dictionary id but provides context
    public static DictIdentifier createPointerDefId(final String identifier) {
        return new DictIdentifier(identifier, NullDefinition.class);
    }
}
