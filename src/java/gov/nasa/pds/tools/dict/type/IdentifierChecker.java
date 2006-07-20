// Copyright 2006, by the California Institute of 
// Technology. ALL RIGHTS RESERVED. United States Government 
// Sponsorship acknowledged. Any commercial use must be negotiated with 
// the Office of Technology Transfer at the California Institute of 
// Technology.
//
// This software may be subject to U.S. export control laws. By 
// accepting this software, the user agrees to comply with all 
// applicable U.S. export laws and regulations. User has the 
// responsibility to obtain export licenses, or other export authority 
// as may be required before exporting such information to foreign 
// countries or providing access to foreign persons.
//
// $Id$ 
//

package gov.nasa.pds.tools.dict.type;

import gov.nasa.pds.tools.label.validate.ElementValidator;
import gov.nasa.pds.tools.dict.DictionaryTokens;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class IdentifierChecker extends LengthChecker implements TypeChecker, DictionaryTokens {

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.validate.TypeChecker#cast(java.lang.String)
     */
    public Object cast(String value) throws InvalidTypeException {
        if (!value.matches("[a-zA-Z][a-zA-Z0-9_]*"))
            throw new InvalidTypeException(value + " is not a valid identifier.");
        if (value.length() > ELEMENT_IDENT_LENGTH) 
            throw new InvalidTypeException(value + " can not exceed " + ElementValidator.ELEMENT_IDENT_LENGTH + " characters.");
        
        return value;
    }

}
