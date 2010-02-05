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

import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.dict.Alias;
import gov.nasa.pds.tools.dict.Definition;
import gov.nasa.pds.tools.dict.DictIdentifier;
import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.dict.ElementDefinition;
import gov.nasa.pds.tools.dict.GroupDefinition;
import gov.nasa.pds.tools.dict.ObjectDefinition;
import gov.nasa.pds.tools.dict.parser.DictIDFactory;
import gov.nasa.pds.tools.dict.type.UnsupportedTypeException;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.DescriptionPointer;
import gov.nasa.pds.tools.label.GroupStatement;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.ObjectStatement;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class ObjectValidator {

    public static boolean validate(final ObjectStatement object,
            final Dictionary dictionary, final Label label)
            throws DefinitionNotFoundException, UnsupportedTypeException {
        boolean valid = true;

        // Find definition then validate
        ObjectDefinition definition = dictionary
                .findObjectClassDefinition(object.getIdentifier());
        if (definition == null) {
            label.addProblem(new DefinitionNotFoundException(object));
        } else {

            // First check that required elements are captured in object
            for (Iterator<DictIdentifier> i = definition.getRequiredElements()
                    .iterator(); i.hasNext();) {
                DictIdentifier required = i.next();
                // First check to see if attribute is found by its identifier
                // TODO: Figure out if related information pointers can satisfy
                // the required elements of an object definition
                if (!object.hasAttribute(required)
                        && !(object.getPointer(DictIDFactory
                                .createPointerDefId(required.getId())) instanceof DescriptionPointer)) {
                    boolean foundAlias = false;
                    // Next check to see if the attribute is present as an alias
                    // Lookup definition for required element
                    ElementDefinition elementDefinition = dictionary
                            .getElementDefinition(required);
                    // Now loop through aliases to see if the element appears
                    if (elementDefinition != null) {
                        for (Iterator<Alias> a = elementDefinition.getAliases()
                                .iterator(); a.hasNext() && !foundAlias;) {
                            // All element aliases take the form
                            // <object_identifier>.<element_identifier>
                            String[] identifier = a.next().toString().split(
                                    "\\."); //$NON-NLS-1$
                            DictIdentifier objectID = DictIDFactory
                                    .createObjectDefId(identifier[0]);
                            DictIdentifier elementID = DictIDFactory
                                    .createElementDefId(identifier[1]);
                            if (objectID.equals(definition.getIdentifier())
                                    && object.hasAttribute(elementID))
                                foundAlias = true;
                        }
                    }
                    // Didn't find anything time to log
                    if (!foundAlias) {
                        valid = false;
                        label.addProblem(object,
                                "parser.error.missingRequiredElement", //$NON-NLS-1$
                                ProblemType.MISSING_PROPERTY, object
                                        .getIdentifier(), required);
                    }
                }
            }

            // Check to make sure that all required objects are present
            for (Iterator<DictIdentifier> i = definition.getRequiredObjects()
                    .iterator(); i.hasNext();) {
                DictIdentifier required = i.next();
                // First see if object is present
                DictIdentifier groupTest = DictIDFactory
                        .createGroupDefId(required.toString());
                if (!object.hasObject(required) && !object.hasGroup(groupTest)) {
                    boolean foundAlias = false;
                    // Next check to see if the object is present as an alias
                    // Lookup definition for required object
                    Definition def = dictionary.getObjectDefinition(required);
                    if (def == null)
                        def = dictionary.getGroupDefinition(required);
                    // Now loop through aliases to see if the object appears
                    if (def != null) {
                        for (Iterator<Alias> a = def.getAliases().iterator(); a
                                .hasNext()
                                && !foundAlias;) {
                            String alias = a.next().toString();
                            DictIdentifier objectId = DictIDFactory
                                    .createObjectDefId(alias);
                            DictIdentifier groupId = DictIDFactory
                                    .createGroupDefId(alias);
                            if (object.hasObject(objectId)
                                    || object.hasGroup(groupId)) {
                                foundAlias = true;
                            }
                        }
                    }
                    // Check to see if this is a reference to a catalog object
                    if (!foundAlias) {
                        valid = false;
                        if (def != null && def instanceof GroupDefinition) {
                            label.addProblem(object,
                                    "parser.error.missingRequiredGroup", //$NON-NLS-1$
                                    ProblemType.MISSING_MEMBER, object
                                            .getIdentifier(), required);
                        } else {
                            label.addProblem(object,
                                    "parser.error.missingRequiredObject", //$NON-NLS-1$
                                    ProblemType.MISSING_MEMBER, object
                                            .getIdentifier(), required);
                        }
                    }
                }
            }
        }

        // Run through nested objects and check them
        List<ObjectStatement> objects = object.getObjects();
        Collections.sort(objects);
        for (Iterator<ObjectStatement> i = objects.iterator(); i.hasNext();) {
            ObjectStatement obj = i.next();
            // Check to make sure object is allowed within this definition
            if (definition != null
                    && !definition.isAllowed(obj.getIdentifier())) {
                // Next check to see if the object is allowed as an alias
                // Lookup definition object by its alias
                ObjectDefinition objectDefinition = dictionary
                        .getObjectDefinition(obj.getIdentifier());
                if (objectDefinition == null
                        || !definition.isAllowed(objectDefinition
                                .getIdentifier())) {
                    valid = false;
                    label.addProblem(object,
                            "parser.error.invalidObject", //$NON-NLS-1$
                            ProblemType.INVALID_MEMBER, object.getIdentifier(),
                            obj.getIdentifier());
                }
            }
            // Validate nested object
            if (!ObjectValidator.validate(obj, dictionary, label)) {
                valid = false;
            }
        }

        List<GroupStatement> groups = object.getGroups();
        Collections.sort(groups);
        for (Iterator<GroupStatement> i = groups.iterator(); i.hasNext();) {
            GroupStatement group = i.next();
            // Check to make sure object is allowed within this definition
            if (definition != null
                    && !definition.isAllowed(group.getIdentifier())) {
                // Next check to see if the object is allowed as an alias
                // Lookup definition object by its alias
                GroupDefinition groupDefinition = dictionary
                        .getGroupDefinition(group.getIdentifier());
                if (groupDefinition == null
                        || !definition.isAllowed(groupDefinition
                                .getIdentifier())) {
                    valid = false;
                    label.addProblem(object,
                            "parser.error.invalidGroup", //$NON-NLS-1$
                            ProblemType.INVALID_MEMBER, object.getIdentifier(),
                            group.getIdentifier());
                }
            }
            // Validate nested object
            if (!GroupValidator.validate(group, dictionary, label)) {
                valid = false;
            }
        }

        // Run through and validate all attributes
        List<AttributeStatement> attributes = object.getAttributes();
        Collections.sort(attributes);
        for (Iterator<AttributeStatement> i = object.getAttributes().iterator(); i
                .hasNext();) {
            AttributeStatement attribute = i.next();
            // Check to make sure element is allowed within this definition
            if (definition != null
                    && !definition.isAllowed(attribute.getIdentifier())) {
                // Next check to see if the attribute is allowed as an alias
                // Lookup definition for element by its alias
                ElementDefinition elementDefinition = dictionary
                        .getElementDefinition(attribute.getIdentifier());
                if (elementDefinition == null
                        || !definition.isAllowed(elementDefinition
                                .getIdentifier())) {
                    valid = false;
                    label.addProblem(attribute,
                            "parser.error.invalidElement", //$NON-NLS-1$
                            ProblemType.INVALID_MEMBER, object.getIdentifier(),
                            attribute.getIdentifier());
                }
            }
            // Validate attribute
            DictIdentifier context = (definition == null) ? object
                    .getIdentifier() : definition.getIdentifier();
            try {
                if (!ElementValidator.validate(attribute, label, dictionary,
                        context.toString())) {
                    valid = false;
                }
            } catch (LabelParserException lpe) {
                label.addProblem(lpe);
                valid = false;
            }
        }
        return valid;
    }

}
