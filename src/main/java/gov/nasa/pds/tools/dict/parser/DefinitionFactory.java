// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// • Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// • Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// • Neither the name of Caltech nor its operating division, the Jet Propulsion
// Laboratory, nor the names of its contributors may be used to endorse or
// promote products derived from this software without specific prior written
// permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package gov.nasa.pds.tools.dict.parser;

import static gov.nasa.pds.tools.dict.DictionaryTokens.DEFINITION;
import static gov.nasa.pds.tools.dict.DictionaryTokens.ELEMENT_DEFINITION;
import static gov.nasa.pds.tools.dict.DictionaryTokens.GENERIC_OBJECT;
import static gov.nasa.pds.tools.dict.DictionaryTokens.OBJECT_TYPE_GENERIC;
import static gov.nasa.pds.tools.dict.DictionaryTokens.OBJECT_TYPE_GENERIC_GROUP;
import static gov.nasa.pds.tools.dict.DictionaryTokens.OBJECT_TYPE_SPECIFIC;
import static gov.nasa.pds.tools.dict.DictionaryTokens.OBJECT_TYPE_SPECIFIC_GROUP;
import static gov.nasa.pds.tools.dict.DictionaryTokens.SPECIFIC_OBJECT;
import gov.nasa.pds.tools.constants.Constants.DictionaryType;
import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.dict.Alias;
import gov.nasa.pds.tools.dict.Definition;
import gov.nasa.pds.tools.dict.DictIdentifier;
import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.dict.ElementDefinition;
import gov.nasa.pds.tools.dict.GroupDefinition;
import gov.nasa.pds.tools.dict.ObjectDefinition;
import gov.nasa.pds.tools.dict.type.InvalidTypeException;
import gov.nasa.pds.tools.dict.type.NonDecimalChecker;
import gov.nasa.pds.tools.dict.type.NumericTypeChecker;
import gov.nasa.pds.tools.dict.type.RealChecker;
import gov.nasa.pds.tools.dict.type.TypeChecker;
import gov.nasa.pds.tools.dict.type.TypeCheckerFactory;
import gov.nasa.pds.tools.dict.type.UnsupportedTypeException;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.tools.label.Scalar;
import gov.nasa.pds.tools.label.Set;

import java.util.ArrayList;
import java.util.List;

/**
 * This class builds definitions from ObjectStatements. The format of the object
 * statement should be in compliance standard PDS dictionary. These definitions
 * can then be added to a {@link gov.nasa.pds.tools.dict.Dictionary}.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class DefinitionFactory {

    /**
     * This method will determine the type of definition and created it. If it
     * can not determine the type of definition that should be generated an
     * error will be thrown.
     * 
     * @param sourceDictionary
     *            with which this definition will be associated with. This call
     *            will NOT add the definition to the given source dictionary as
     *            {@link Alias}es have not been added to the definition at this
     *            point.
     * @param object
     *            from which the definition will be created
     * @return a {@link gov.nasa.pds.tools.dict.Definition} that represents an
     *         entry in a PDS data dictionary
     * @throws UnknownDefinitionException
     *             thrown when the type of definition can not be recognized.
     */
    public static Definition createDefinition(Dictionary sourceDictionary,
            ObjectStatement object) throws UnknownDefinitionException {
        Definition definition = null;

        final String idString = object.getIdentifier().toString();
        if (idString.endsWith(DEFINITION)) {
            if (idString.equals(GENERIC_OBJECT)
                    || idString.equals(SPECIFIC_OBJECT)) {
                if (OBJECT_TYPE_GENERIC_GROUP.equals(object.getAttribute(
                        DictionaryParser.OBJECT_TYPE_ID).getValue().toString())
                        || OBJECT_TYPE_SPECIFIC_GROUP.equals(object
                                .getAttribute(DictionaryParser.OBJECT_TYPE_ID)
                                .getValue().toString())) {
                    definition = createGroupDefinition(sourceDictionary, object);
                } else {
                    definition = createObjectDefinition(sourceDictionary,
                            object);
                }
            } else if (idString.equals(ELEMENT_DEFINITION)) {
                definition = createElementDefinition(sourceDictionary, object);
            }
        }

        if (definition == null) {
            throw new UnknownDefinitionException(sourceDictionary,
                    (Integer) object.getLineNumber(), null,
                    "parser.error.unresolvedEntry",
                    ProblemType.INVALID_DEFINITION, object.getIdentifier());
        }

        return definition;
    }

    /**
     * This method creates an {@link ObjectDefinition} by gathering the
     * attributes required from the {@link ObjectStatement} as specified in the
     * PDS Data Dictionary document.
     * 
     * @param sourceDictionary
     *            with which this definition will be associated with. This call
     *            will NOT add the definition to the given source dictionary as
     *            {@link Alias}es have not been added to the definition at this
     *            point.
     * @param object
     *            that has the information to form an {@link ObjectDefinition}
     * @return a {@link Definition} that represents an entry in a PDS data
     *         dictionary
     * @throws UnknownDefinitionException
     *             thrown when the type of definition can not be recognized.
     */
    public static ObjectDefinition createObjectDefinition(
            Dictionary sourceDictionary, ObjectStatement object)
            throws UnknownDefinitionException {
        ObjectDefinition definition = null;

        if ((object.getIdentifier().equals(DictionaryParser.GENERIC_OBJECT_ID) || object
                .getIdentifier().equals(DictionaryParser.SPECIFIC_OBJECT_ID))
                && (OBJECT_TYPE_GENERIC.equals(object.getAttribute(
                        DictionaryParser.OBJECT_TYPE_ID).getValue().toString()) || OBJECT_TYPE_SPECIFIC
                        .equals(object.getAttribute(
                                DictionaryParser.OBJECT_TYPE_ID).getValue()
                                .toString()))) {
            DictIdentifier id = DictIDFactory.createObjectDefId(object
                    .getAttribute(DictionaryParser.NAME_ID).getValue()
                    .toString());
            definition = new ObjectDefinition(sourceDictionary, object
                    .getLineNumber(), id);

            // Find and set the description
            AttributeStatement attribute = object
                    .getAttribute(DictionaryParser.DESCRIPTION_ID);
            if (attribute != null)
                definition.setDescription(attribute.getValue().toString());

            // Find and set the status type
            attribute = object.getAttribute(DictionaryParser.STATUS_TYPE_ID);
            if (attribute != null)
                definition.setStatusType(attribute.getValue().toString());

            // Find and set the object type
            attribute = object.getAttribute(DictionaryParser.OBJECT_TYPE_ID);
            if (attribute != null)
                definition.setObjectType(attribute.getValue().toString());

            // Find and set the names of the required objects
            attribute = object
                    .getAttribute(DictionaryParser.REQUIRED_OBJECTS_ID);
            if (attribute != null) {
                Set values = (Set) attribute.getValue();
                for (Scalar scalar : values) {
                    definition.addRequired(DictIDFactory
                            .createObjectDefId(scalar.toString()));
                }
            }

            // Find and set the names of the optional objects
            attribute = object
                    .getAttribute(DictionaryParser.OPTIONAL_OBJECTS_ID);
            if (attribute != null) {
                Set values = (Set) attribute.getValue();
                for (Scalar scalar : values) {
                    definition.addOptional(DictIDFactory
                            .createObjectDefId(scalar.toString()));
                }
            }

            // Find and set the names of the required elements
            attribute = object
                    .getAttribute(DictionaryParser.REQUIRED_ELEMENTS_ID);
            if (attribute != null) {
                Set values = (Set) attribute.getValue();
                for (Scalar scalar : values) {
                    definition.addRequired(DictIDFactory
                            .createElementDefId(scalar.toString()));
                }
            }

            // Find and set the names of the required elements
            attribute = object
                    .getAttribute(DictionaryParser.OPTIONAL_ELEMENTS_ID);
            if (attribute != null) {
                Set values = (Set) attribute.getValue();
                for (Scalar scalar : values) {
                    definition.addOptional(DictIDFactory
                            .createElementDefId(scalar.toString()));
                }
            }
        }

        if (definition == null) {
            DictIdentifier id = DictIDFactory.createElementDefId(object
                    .getAttribute(DictionaryParser.NAME_ID).getValue()
                    .toString());
            Object[] arguments = { "object", id };
            throw new UnknownDefinitionException(sourceDictionary,
                    (Integer) object.getLineNumber(), null,
                    "parser.error.badDefinition",
                    ProblemType.INVALID_DEFINITION, arguments);
        }

        return definition;
    }

    /**
     * This method creates an {@link GroupDefinition} by gathering the
     * attributes required from the {@link ObjectStatement} as specified in the
     * PDS Data Dictionary document.
     * 
     * @param sourceDictionary
     *            with which this definition will be associated with. This call
     *            will NOT add the definition to the given source dictionary as
     *            {@link Alias}es have not been added to the definition at this
     *            point.
     * @param object
     *            that has the information to form an {@link GroupDefinition}
     * @return a {@link Definition} that represents an entry in a PDS data
     *         dictionary
     * @throws UnknownDefinitionException
     *             thrown when the type of definition can not be recognized.
     */
    public static GroupDefinition createGroupDefinition(
            Dictionary sourceDictionary, ObjectStatement object)
            throws UnknownDefinitionException {
        GroupDefinition definition = null;

        if ((object.getIdentifier().equals(DictionaryParser.GENERIC_OBJECT_ID) || object
                .getIdentifier().equals(DictionaryParser.SPECIFIC_OBJECT_ID))
                && (OBJECT_TYPE_GENERIC_GROUP.equals(object.getAttribute(
                        DictionaryParser.OBJECT_TYPE_ID).getValue().toString()) || OBJECT_TYPE_SPECIFIC_GROUP
                        .equals(object.getAttribute(
                                DictionaryParser.OBJECT_TYPE_ID).getValue()
                                .toString()))) {
            DictIdentifier id = DictIDFactory.createGroupDefId(object
                    .getAttribute(DictionaryParser.NAME_ID).getValue()
                    .toString());
            definition = new GroupDefinition(sourceDictionary, object
                    .getLineNumber(), id);

            // Find and set the description
            AttributeStatement attribute = object
                    .getAttribute(DictionaryParser.DESCRIPTION_ID);
            if (attribute != null)
                definition.setDescription(attribute.getValue().toString());

            // Find and set the status type
            attribute = object.getAttribute(DictionaryParser.STATUS_TYPE_ID);
            if (attribute != null)
                definition.setStatusType(attribute.getValue().toString());

            // Find and set the object type
            attribute = object.getAttribute(DictionaryParser.OBJECT_TYPE_ID);
            if (attribute != null)
                definition.setObjectType(attribute.getValue().toString());

            // Find and set the names of the required elements
            attribute = object
                    .getAttribute(DictionaryParser.REQUIRED_ELEMENTS_ID);
            if (attribute != null) {
                Set values = (Set) attribute.getValue();
                for (Scalar scalar : values) {
                    definition.addRequired(DictIDFactory
                            .createElementDefId(scalar.toString()));
                }
            }

            // Find and set the names of the required elements
            attribute = object
                    .getAttribute(DictionaryParser.OPTIONAL_ELEMENTS_ID);
            if (attribute != null) {
                Set values = (Set) attribute.getValue();
                for (Scalar scalar : values) {
                    definition.addOptional(DictIDFactory
                            .createElementDefId(scalar.toString()));
                }
            }
        }

        if (definition == null) {
            DictIdentifier id = DictIDFactory.createElementDefId(object
                    .getAttribute(DictionaryParser.NAME_ID).getValue()
                    .toString());
            Object[] arguments = { "group", id };
            throw new UnknownDefinitionException(sourceDictionary,
                    (Integer) object.getLineNumber(), null,
                    "parser.error.badDefinition",
                    ProblemType.INVALID_DEFINITION, arguments);
        }

        return definition;
    }

    /**
     * This method creates an {@link ElementDefinition} by gathering the
     * attributes required from the {@link ObjectStatement} as specified in the
     * PDS Data Dictionary document.
     * 
     * @param sourceDictionary
     *            with which this definition will be associated with. This call
     *            will NOT add the definition to the given source dictionary as
     *            {@link Alias}es have not been added to the definition at this
     *            point.
     * @param object
     *            that has the information to form an {@link ElementDefinition}
     * @return a {@link Definition} that represents an entry in a PDS data
     *         dictionary
     * @throws UnknownDefinitionException
     *             thrown when the type of definition can not be recognized.
     */
    public static ElementDefinition createElementDefinition(
            Dictionary sourceDictionary, ObjectStatement object)
            throws UnknownDefinitionException {
        ElementDefinition definition = null;

        if (object.getIdentifier().equals(
                DictionaryParser.ELEMENT_DEFINITION_ID)) {
            DictIdentifier id = DictIDFactory.createElementDefId(object
                    .getAttribute(DictionaryParser.NAME_ID).getValue()
                    .toString());
            definition = new ElementDefinition(sourceDictionary, object
                    .getLineNumber(), id);

            // Find and set status
            AttributeStatement attribute = object
                    .getAttribute(DictionaryParser.STATUS_TYPE_ID);
            if (attribute != null)
                definition.setStatusType(attribute.getValue().toString());

            // Find and set description
            attribute = object.getAttribute(DictionaryParser.DESCRIPTION_ID);
            if (attribute != null) {
                definition.setDescription(attribute.getValue().toString());
            }

            // Find and set data type
            attribute = object.getAttribute(DictionaryParser.DATA_TYPE_ID);
            if (attribute != null) {
                definition.setDataType(DictionaryType.valueOf(attribute
                        .getValue().toString()));
            }

            // Find and set units
            attribute = object.getAttribute(DictionaryParser.UNITS_ID);
            if (attribute != null) {
                definition.setUnits(attribute.getValue().toString());
            }

            // Find and set minimum length
            attribute = object.getAttribute(DictionaryParser.MIN_LENGTH_ID);
            if (attribute != null
                    && !"NULL".equals(attribute.getValue().toString())) { //$NON-NLS-1$
                try {
                    definition.setMinLength(Integer.parseInt(attribute
                            .getValue().toString()));
                } catch (NumberFormatException nfe) {
                    // noop
                }
            }

            // Find and set max length
            attribute = object.getAttribute(DictionaryParser.MAX_LENGTH_ID);
            if (attribute != null
                    && !"NULL".equals(attribute.getValue().toString())) { //$NON-NLS-1$
                try {
                    definition.setMaxLength(Integer.parseInt(attribute
                            .getValue().toString()));
                } catch (NumberFormatException nfe) {
                    // noop
                }
            }

            // Find and set minimum value
            attribute = object.getAttribute(DictionaryParser.MINIMUM_ID);
            if (attribute != null
                    && !"NULL".equals(attribute.getValue().toString())) { //$NON-NLS-1$
                try {
                    TypeChecker checker = TypeCheckerFactory.getInstance()
                            .newInstance(definition);
                    if (checker instanceof NumericTypeChecker) {
                        Number number = null;
                        try {
                            number = (Number) checker.cast(attribute.getValue()
                                    .toString(), attribute);
                        } catch (InvalidTypeException e) {
                            // allow non decimal types to use normal max and
                            // mins
                            if (checker instanceof NonDecimalChecker) {
                                checker = new RealChecker();
                                try {
                                    number = (Number) checker.cast(attribute
                                            .getValue().toString(), attribute);
                                } catch (InvalidTypeException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                        if (number != null) {
                            definition.setMinimum(number);
                        }
                    }
                } catch (UnsupportedTypeException e) {
                    e.printStackTrace();
                    // FIXME: throw an InvalidDefinitionException
                }
            }

            // Find and set max value
            attribute = object.getAttribute(DictionaryParser.MAXIMUM_ID);
            if (attribute != null
                    && !"NULL".equals(attribute.getValue().toString())) { //$NON-NLS-1$
                try {
                    TypeChecker checker = TypeCheckerFactory.getInstance()
                            .newInstance(definition);
                    if (checker instanceof NumericTypeChecker) {
                        Number number = null;
                        try {
                            number = (Number) checker.cast(attribute.getValue()
                                    .toString(), attribute);
                        } catch (InvalidTypeException e) {
                            // allow non decimal types to use normal max and
                            // mins
                            if (checker instanceof NonDecimalChecker) {
                                checker = new RealChecker();
                                try {
                                    number = (Number) checker.cast(attribute
                                            .getValue().toString(), attribute);
                                } catch (InvalidTypeException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                        if (number != null) {
                            definition.setMaximum(number);
                        }
                    }
                } catch (UnsupportedTypeException e) {
                    e.printStackTrace();
                    // FIXME: throw an InvalidDefinitionException
                }
            }

            // Find and set value type
            attribute = object.getAttribute(DictionaryParser.VALUE_TYPE_ID);
            if (attribute != null)
                definition.setValueType(attribute.getValue().toString());

            // Find and set value set
            attribute = object.getAttribute(DictionaryParser.VALUES_ID);
            List<String> values = new ArrayList<String>();
            if (attribute != null) {
                Set s = (Set) attribute.getValue();
                for (Scalar scalar : s) {
                    String value = scalar.normalize();
                    if (!"N/A".equals(value)) //$NON-NLS-1$
                        values.add(value);
                }
                definition.setValues(values);
            }

        }

        if (definition == null) {
            DictIdentifier id = DictIDFactory.createElementDefId(object
                    .getAttribute(DictionaryParser.NAME_ID).getValue()
                    .toString());
            Object[] arguments = { "element", id };
            throw new UnknownDefinitionException(sourceDictionary,
                    (Integer) object.getLineNumber(), null,
                    "parser.error.badDefinition",
                    ProblemType.INVALID_DEFINITION, arguments);
        }

        return definition;
    }
}
