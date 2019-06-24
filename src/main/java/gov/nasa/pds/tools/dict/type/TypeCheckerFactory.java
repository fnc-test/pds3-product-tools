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
