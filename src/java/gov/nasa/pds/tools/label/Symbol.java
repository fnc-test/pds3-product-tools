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

package gov.nasa.pds.tools.label;

import gov.nasa.pds.tools.dict.type.Types;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class Symbol extends Scalar implements Types {

    /**
     * @param value
     */
    public Symbol(String value) {
        super(value.replaceAll("'",""));
    }

	public boolean isSupportedPDSType(String type) {
		if (Types.IDENTIFIER.equals(type) || Types.CHARACTER.equals(type) || Types.DATA_SET.equals(type) ||
				Types.ALPHABET.equals(type) || Types.ALPHANUMERIC.equals(type) || Types.CONTEXT_DEPENDENT.equals(type) ||
				Types.CONTEXTDEPENDENT.equals(type))
			return true;
		return false;
	}

}
