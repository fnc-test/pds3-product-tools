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

import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.dict.DictIdentifier;
import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.dict.GroupDefinition;
import gov.nasa.pds.tools.dict.type.UnsupportedTypeException;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.GroupStatement;
import gov.nasa.pds.tools.label.Label;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class GroupValidator {

    public static boolean validate(final GroupStatement group,
            final Dictionary dictionary, final Label label)
            throws DefinitionNotFoundException, UnsupportedTypeException {
        boolean valid = true;

        // Lookup group definition, can't do anything without it
        GroupDefinition definition = dictionary.findGroupClassDefinition(group
                .getIdentifier());
        if (definition == null) {
            label.addProblem(new DefinitionNotFoundException(group));
        } else {

            // First check that required elements are captured in object
            for (Iterator<DictIdentifier> i = definition.getRequiredElements()
                    .iterator(); i.hasNext();) {
                DictIdentifier required = i.next();
                if (!group.hasAttribute(required.toString())) {
                    valid = false;
                    label.addProblem(
                            group,
                            "parser.error.missingRequiredElement", //$NON-NLS-1$
                            ProblemType.MISSING_PROPERTY,
                            group.getIdentifier(), required);
                }
            }

            // Check to make sure all attributes are allowed within this
            // definition
            // If the definition contains the element PSDD then anything is
            // allowed and this check can be skipped
            if (!definition.allowsAnyElement()) {
                for (Iterator<AttributeStatement> i = group.getAttributes()
                        .iterator(); i.hasNext();) {
                    AttributeStatement attribute = i.next();
                    if (!definition.isAllowed(attribute.getIdentifier())) {
                        valid = false;
                        label.addProblem(attribute,
                                "parser.error.invalidElement", //$NON-NLS-1$
                                ProblemType.INVALID_MEMBER, group
                                        .getIdentifier(), attribute
                                        .getIdentifier());
                    }
                }
            }
        }

        // Validate all attributes
        List<AttributeStatement> attributes = group.getAttributes();
        Collections.sort(attributes);
        for (Iterator<AttributeStatement> i = group.getAttributes().iterator(); i
                .hasNext();) {
            AttributeStatement attribute = i.next();
            if (!ElementValidator.validate(attribute, label, dictionary)) {
                valid = false;
            }
        }

        return valid;
    }
}
