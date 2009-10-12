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

package gov.nasa.pds.tools.label;

import gov.nasa.arc.pds.tools.util.StrUtils;
import gov.nasa.pds.tools.constants.Constants.DictionaryType;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class Symbol extends Scalar {

    /**
     * @param value
     */
    public Symbol(String value) {
        super(value.replaceAll("'", "")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public String normalize() {
        return StrUtils.normalize(this.toString());
    }

    @Override
    public boolean isSupportedPDSType(DictionaryType type) {
        if (DictionaryType.IDENTIFIER.equals(type)
                || DictionaryType.CHARACTER.equals(type)
                || DictionaryType.DATA_SET.equals(type)
                || DictionaryType.ALPHABET.equals(type)
                || DictionaryType.ALPHANUMERIC.equals(type)
                || DictionaryType.CONTEXT_DEPENDENT.equals(type)
                || DictionaryType.CONTEXTDEPENDENT.equals(type)) {
            return true;
        }
        return false;
    }

}
