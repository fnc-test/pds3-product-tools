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

package gov.nasa.pds.tools.dict.parser;

import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.dict.Dictionary;

/**
 * This exception will be thrown when the type of definition can not be
 * determined.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class UnknownDefinitionException extends LabelParserException {
    private static final long serialVersionUID = 3768577117762341629L;

    public UnknownDefinitionException(final Dictionary sourceDictionary,
            final Integer lineNumber, final Integer column, final String key,
            final ProblemType type, final Object... arguments) {
        super(sourceDictionary, lineNumber, column, key, type, arguments);
    }
}
