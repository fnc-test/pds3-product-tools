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

import static gov.nasa.pds.tools.dict.DictionaryTokens.ELEMENT_IDENT_LENGTH;
import static gov.nasa.pds.tools.dict.DictionaryTokens.NAMESPACE_LENGTH;
import static gov.nasa.pds.tools.dict.DictionaryTokens.VALUE_TYPE_STATIC;
import gov.nasa.arc.pds.tools.util.StrUtils;
import gov.nasa.pds.tools.constants.Constants;
import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.dict.ElementDefinition;
import gov.nasa.pds.tools.dict.type.InvalidLengthException;
import gov.nasa.pds.tools.dict.type.InvalidTypeException;
import gov.nasa.pds.tools.dict.type.NumericTypeChecker;
import gov.nasa.pds.tools.dict.type.OutOfRangeException;
import gov.nasa.pds.tools.dict.type.TypeChecker;
import gov.nasa.pds.tools.dict.type.TypeCheckerFactory;
import gov.nasa.pds.tools.dict.type.UnsupportedTypeException;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.Numeric;
import gov.nasa.pds.tools.label.Scalar;
import gov.nasa.pds.tools.label.Sequence;
import gov.nasa.pds.tools.label.Set;
import gov.nasa.pds.tools.label.Value;

import java.util.Collection;
import java.util.Map;

/**
 * This class will validate an element value or set of values against an
 * Dictionary.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class ElementValidator {

    public static boolean validate(AttributeStatement attribute, Label label,
            Dictionary dictionary, ElementDefinition definition)
            throws UnsupportedTypeException {

        boolean valid = true;

        Value value = attribute.getValue();

        // Check length of namespace
        // NOTE: Won't get here if id doesn't exist
        if (attribute.hasNamespace()) {
            if (attribute.getNamespace().length() > NAMESPACE_LENGTH) {
                valid = false;
                label.addProblem(attribute,
                        "parser.error.longNamespace", //$NON-NLS-1$
                        ProblemType.EXCESSIVE_NAMESPACE_LENGTH,
                        ELEMENT_IDENT_LENGTH);
            }
        }

        // Check length of identifier
        if (attribute.getElementIdentifier().length() > ELEMENT_IDENT_LENGTH) {
            valid = false;
            label.addProblem(attribute,
                    "parser.error.longIdentifier", //$NON-NLS-1$
                    ProblemType.EXCESSIVE_IDENTIFIER_LENGTH,
                    ELEMENT_IDENT_LENGTH);
        }

        // Load the type checker
        TypeChecker checker = TypeCheckerFactory.getInstance().newInstance(
                definition);

        // Don't want to set to true if has already been set to false
        if (!validate(attribute, label, dictionary, definition, checker, value)) {
            valid = false;
        }

        return valid;
    }

    @SuppressWarnings("unchecked")
    private static boolean validate(final AttributeStatement attribute,
            final Label label, final Dictionary dictionary,
            final ElementDefinition definition, final TypeChecker checker,
            final Value value) throws UnsupportedTypeException {
        boolean valid = true;
        if (value == null) {
            label.addProblem(attribute, "parser.error.missingValue", //$NON-NLS-1$
                    ProblemType.MISSING_VALUE, attribute.getIdentifier());
        } else if (value instanceof Set || value instanceof Sequence) {
            boolean validValues = true;
            for (Value v : ((Collection<Value>) value)) {
                validValues = validate(attribute, label, dictionary,
                        definition, checker, v);
                // Don't want to set to true if has already been set to false
                if (!validValues)
                    valid = false;
            }
        } else {
            if (!skipValue(attribute, label, value)) {
                // Check to see if type defined in dictionary matches that found
                // by the parser
                Scalar scalar = (Scalar) value;
                // ex. dict says real and parser says string
                if (!scalar.isSupportedPDSType(definition.getDataType())) {
                    valid = false;

                    label
                            .addProblem(
                                    attribute,
                                    "parser.error.typeMismatch", //$NON-NLS-1$
                                    ProblemType.TYPE_MISMATCH,
                                    attribute.getIdentifier(),
                                    definition.getDataType(),
                                    scalar.getClass().getName().substring(
                                            scalar.getClass().getName()
                                                    .lastIndexOf(".") + 1), scalar.toString()); //$NON-NLS-1$
                } else {
                    // Check against valid values if there are any
                    if (definition.hasValidValues()) {
                        final String normalizedValue = value.normalize();
                        if (normalizedValue.trim().equals("")) { //$NON-NLS-1$
                            label.addProblem(attribute,
                                    "parser.error.missingValue", //$NON-NLS-1$
                                    ProblemType.MISSING_VALUE, attribute
                                            .getIdentifier());
                        } else if (!definition.getValues().contains(
                                normalizedValue)) {
                            if (definition.getValues().contains(
                                    normalizedValue.toUpperCase())) {
                                label.addProblem(attribute,
                                        "parser.warning.manipulatedValue", //$NON-NLS-1$
                                        ProblemType.MANIPULATED_VALUE, // whitespace
                                        normalizedValue, normalizedValue
                                                .toUpperCase(), attribute
                                                .getIdentifier());
                            } else if (!VALUE_TYPE_STATIC.equals(definition
                                    .getValueType())) {
                                // Only produce a warning if the standard value
                                // list is anything other than static
                                label.addProblem(
                                        attribute,
                                        "parser.warning.unknownValue", //$NON-NLS-1$
                                        ProblemType.UNKNOWN_VALUE, StrUtils
                                                .truncate(normalizedValue),
                                        attribute.getIdentifier(),
                                        normalizedValue);
                            } else {
                                valid = false;
                                label.addProblem(
                                        attribute,
                                        "parser.error.invalidValue", //$NON-NLS-1$
                                        ProblemType.INVALID_VALUE, StrUtils
                                                .truncate(normalizedValue),
                                        attribute.getIdentifier(),
                                        normalizedValue);
                            }
                        }
                    }

                    Object castedValue = null;
                    // Try to cast to an instance of the type
                    try {
                        castedValue = checker.cast(value.toString(), attribute);
                    } catch (InvalidTypeException ite) {
                        valid = false;
                        label.addProblem(attribute, ite);
                    }

                    // Check min length
                    try {
                        checker.checkMinLength(value.toString(), definition,
                                attribute);
                    } catch (InvalidLengthException ile) {
                        valid = false;
                        label.addProblem(attribute, ile);
                    }

                    // Check max length
                    try {
                        checker.checkMaxLength(value.toString(), definition,
                                attribute);
                    } catch (InvalidLengthException ile) {
                        valid = false;
                        label.addProblem(attribute, ile);
                    }

                    // Check to see if this is a numeric type checker if so then
                    // do further checking
                    if (checker instanceof NumericTypeChecker
                            && castedValue != null
                            && castedValue instanceof Number) {
                        NumericTypeChecker numericChecker = (NumericTypeChecker) checker;

                        // Check min value
                        if (definition.hasMinimum()) {
                            try {
                                numericChecker.checkMinValue(
                                        (Number) castedValue, definition,
                                        attribute);
                            } catch (OutOfRangeException oor) {
                                valid = false;
                                label.addProblem(attribute, oor);
                            }
                        }

                        // Check max value
                        if (definition.hasMaximum()) {
                            try {
                                numericChecker.checkMaxValue(
                                        (Number) castedValue, definition,
                                        attribute);
                            } catch (OutOfRangeException oor) {
                                valid = false;
                                label.addProblem(attribute, oor);
                            }
                        }

                        // Check units if found and definition required.
                        // Look at definition to see if we should check units
                        if (value instanceof Numeric) {
                            Numeric number = (Numeric) value;
                            if (number.getUnits() != null
                                    && !isUnitAllowed(number, dictionary)) {
                                label.addProblem(
                                        attribute,
                                        "parser.warning.unknownUnits", //$NON-NLS-1$
                                        ProblemType.UNKNOWN_VALUE_TYPE,
                                        attribute.getIdentifier(), number
                                                .getUnits());
                            }
                        }
                    }
                }
            }
        }

        return valid;
    }

    private static boolean skipValue(final AttributeStatement attribute,
            final Label label, final Value value) {
        final String strVal = value.toString();
        if (Constants.NULL_VAL_CONST.equals(strVal)) {
            // NOTE: const, null value, added to message since it's _possible_
            // value could change in spec so should not be burned into message
            label.addProblem(attribute,
                    "parser.info.placeholderValue", //$NON-NLS-1$
                    ProblemType.PLACEHOLDER_VALUE, attribute.getIdentifier(),
                    Constants.NULL_VAL_CONST);
            return true;
        } else if (Constants.UNKNOWN_VAL_CONST.equals(strVal)
                || Constants.NOT_APPLICABLE_VAL_CONST.equals(strVal)) {
            return true;
        }
        return false;
    }

    // TODO: support derived values that are not in the list. for example,
    // switching km for m in a combined unit should be valid
    private static boolean isUnitAllowed(Numeric number, Dictionary dictionary) {
        final String units = number.getUnits();
        Map<String, String> unitList = dictionary.getUnits();
        if (unitList.containsKey(units)) {
            return true;
        }

        // long description for value may be case insensitive
        for (final String val : unitList.values()) {
            if (units.equalsIgnoreCase(val)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks to see whether an {@link AttributeStatement} is correct. Will look
     * up the definition in the given dictionary. An object context may be
     * supplied as elements can have aliases that are appropriate within an
     * object. Set objectContext to null if there if the lookup should be
     * performed without care to the surrounding object.
     * 
     * @param dictionary
     *            where to look up the element
     * @param objectContext
     *            enclosing the element to be looked up
     * @param attribute
     *            statement to be validated
     * @return flag indicating whether or not the statement was valid against
     *         the definition found
     * @throws DefinitionNotFoundException
     *             if definition for element is not found
     * @throws UnsupportedTypeException
     *             if type of element is not supported
     */
    public static boolean validate(AttributeStatement attribute, Label label,
            Dictionary dictionary, String objectContext)
            throws DefinitionNotFoundException, UnsupportedTypeException {
        ElementDefinition definition = dictionary.getElementDefinition(
                objectContext, attribute.getIdentifier());

        if (definition == null) {
            throw new DefinitionNotFoundException(attribute);
        }

        return validate(attribute, label, dictionary, definition);
    }

    public static boolean validate(AttributeStatement attribute, Label label,
            Dictionary dictionary) throws DefinitionNotFoundException,
            UnsupportedTypeException {
        return validate(attribute, label, dictionary, new String());
    }
}
