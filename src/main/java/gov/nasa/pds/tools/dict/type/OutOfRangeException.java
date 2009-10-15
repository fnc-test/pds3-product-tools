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

import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.constants.Constants.DictionaryType;
import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.dict.DictIdentifier;
import gov.nasa.pds.tools.label.Statement;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class OutOfRangeException extends LabelParserException {
    private static final long serialVersionUID = -300278035096004819L;

    public OutOfRangeException(final Statement statement, final Number value,
            final Number limit, final boolean exceeds,
            final DictIdentifier fieldName, final DictionaryType dataType) {
        super(statement, null, exceeds ? "parser.error.exceedsMax" //$NON-NLS-1$
                : "parser.error.lessThanMin", ProblemType.OOR, value, limit, //$NON-NLS-1$
                fieldName.toString(), dataType);
    }
}
