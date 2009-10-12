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

import gov.nasa.pds.tools.constants.Constants.DictionaryType;
import gov.nasa.pds.tools.dict.ElementDefinition;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class TypeCheckerFactory {
    private static TypeCheckerFactory factory = null;

    private TypeCheckerFactory() {
        // TODO: Consider supporting dynamic types. This could
        // loading a mapping at this point
    }

    public static synchronized TypeCheckerFactory getInstance() {
        if (factory == null) {
            factory = new TypeCheckerFactory();
        }
        return factory;
    }

    // TODO: DictionaryType has more values than are listed below, are the
    // others ones that should not appear in labels? for example "BIBLIO" is not
    // listed below. If they are allowed, why have this at all since the
    // creation of instance of DictionaryType will complain if enum not found
    public TypeChecker newInstance(ElementDefinition definition)
            throws UnsupportedTypeException {
        TypeChecker checker = null;
        DictionaryType type = definition.getDataType();

        // DOUBLE, DECIMAL, EXPONENTIAL are bad, treat as REAL
        if (type.equals(DictionaryType.REAL)
                || type.equals(DictionaryType.DOUBLE)
                || type.equals(DictionaryType.EXPONENTIAL)
                || type.equals(DictionaryType.DECIMAL)) {
            checker = new RealChecker();
        } else if (type.equals(DictionaryType.INTEGER)
                || type.equals(DictionaryType.ASCII_INTEGER)) {
            checker = new IntegerChecker();
            // IDENTIFIER, BIBLIO, DATA_SET are bad, treat as CHARACTER
        } else if (type.equals(DictionaryType.CHARACTER)
                || type.equals(DictionaryType.IDENTIFIER)
                || type.equals(DictionaryType.DATA_SET)
                || type.equals(DictionaryType.BIBLIO)) {
            checker = new CharacterChecker();
        } else if (type.equals(DictionaryType.ALPHABET)) {
            checker = new AlphabetChecker();
        } else if (type.equals(DictionaryType.ALPHANUMERIC)) {
            checker = new AlphaNumericChecker();
        } else if (type.equals(DictionaryType.DATE)) {
            checker = new DateChecker();
        } else if (type.equals(DictionaryType.TIME)) {
            checker = new TimeChecker();
        } else if (type.equals(DictionaryType.NONDECIMAL)
                || type.equals(DictionaryType.NON_DECIMAL)) {
            checker = new NonDecimalChecker();
        } else if (type.equals(DictionaryType.CONTEXT_DEPENDENT)
                || type.equals(DictionaryType.CONTEXTDEPENDENT)) {
            checker = new ContextDependentChecker();
        }

        if (checker == null) {
            throw new UnsupportedTypeException(definition);
        }

        return checker;
    }
}
