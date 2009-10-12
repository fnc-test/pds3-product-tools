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

import static gov.nasa.pds.tools.dict.DictionaryTokens.OBJECT_TYPE_GENERIC;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an object definition in the PDS data dictionary.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class ObjectDefinition extends ContainerDefinition {

    private static final long serialVersionUID = 5594367121498069617L;

    public ObjectDefinition(final Dictionary sourceDictionary,
            final int lineNumber, final DictIdentifier identifier) {
        super(sourceDictionary, lineNumber, identifier);
        this.objectType = OBJECT_TYPE_GENERIC;
    }

    public List<DictIdentifier> getRequiredObjects() {
        List<DictIdentifier> requiredObjects = new ArrayList<DictIdentifier>();
        for (DictIdentifier identifier : this.getRequired()) {
            if (identifier.getType().equals(ObjectDefinition.class)
                    || identifier.getType().equals(GroupDefinition.class)) {
                requiredObjects.add(identifier);
            }
        }
        return requiredObjects;
    }

    public boolean hasRequiredObjects() {
        return !this.getRequiredObjects().isEmpty();
    }

    public List<DictIdentifier> getOptionalObjects() {
        List<DictIdentifier> optionalObjects = new ArrayList<DictIdentifier>();
        for (DictIdentifier identifier : this.getOptional()) {
            if (identifier.getType().equals(ObjectDefinition.class)
                    || identifier.getType().equals(GroupDefinition.class)) {
                optionalObjects.add(identifier);
            }
        }
        return optionalObjects;
    }

    public boolean hasOptionalObjects() {
        return !this.getOptionalObjects().isEmpty();
    }
}
