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

package gov.nasa.pds.tools.dict.type;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class TypeCheckerFactory implements Types{
    private static TypeCheckerFactory factory = null;
    
    private TypeCheckerFactory() {
        //TODO: Consider supporting dynamic types. This could
        //loading a mapping at this point
    }
    
    public static synchronized TypeCheckerFactory getInstance() {
        if (factory == null)
            factory = new TypeCheckerFactory();
        return factory;
    }
    
    public TypeChecker newInstance(String type) throws UnsupportedTypeException {
        TypeChecker checker = null;
        
        if (type.equals(REAL)) {
            checker = new RealChecker();
        } else if (type.equals(DOUBLE)) {
            checker = new DoubleChecker();
        } else if (type.equals(INTEGER) || type.equals(ASCII_INTEGER)) {
            checker = new IntegerChecker();
        } else if (type.equals(CHARACTER)) {
            checker = new CharacterChecker();
        } else if (type.equals(ALPHABET)) {
            checker = new AlphabetChecker();
        } else if (type.equals(ALPHANUMERIC)) {
            checker = new AlphaNumericChecker();
        } else if (type.equals(DATE)) {
            checker = new DateChecker();
        } else if (type.equals(TIME)) {
            checker = new TimeChecker();
        } else if (type.equals(NONDECIMAL) || type.equals(NON_DECIMAL)) {
            checker = new NonDecimalChecker();
        } else if (type.equals(DATA_SET)) {
            checker = new DataSetChecker();
        } else if (type.equals(IDENTIFIER)) {
            checker = new IdentifierChecker();
        } else if (type.equals(CONTEXT_DEPENDENT) || type.equals(CONTEXTDEPENDENT)) {
            checker = new ContextDependentChecker();
        }
        
        if (checker == null)
            throw new UnsupportedTypeException(type + " is not a supported type.");
        
        return checker;
    }
}
