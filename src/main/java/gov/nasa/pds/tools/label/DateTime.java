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
import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.constants.Constants.DictionaryType;

import java.util.Date;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class DateTime extends Scalar {

    private Date date;

    /**
     * @param value
     *            represented as a string which is in a PDS compatible format
     * @param lineNumber
     *            in which this value was found
     */
    public DateTime(final Label label, final String value, final int lineNumber)
            throws LabelParserException {
        this(label, value, lineNumber, false);
    }

    /**
     * @param value
     *            represented as a string which is in a PDS compatible format
     * @param lineNumber
     *            in which this value was found
     * @param lenient
     *            a flag to indicate how to parse the date
     */
    public DateTime(final Label label, final String value,
            final int lineNumber, final boolean lenient)
            throws LabelParserException {
        super(value);
        if (lenient) {
            this.date = DateTimeFormatter
                    .lenientParse(label, value, lineNumber);
        } else {
            this.date = DateTimeFormatter.parse(label, value, lineNumber);
        }
    }

    public DateTime(Date date) {
        super(date.toString());
        this.date = date;
    }

    public Date getDate() {
        return this.date;
    }

    public String normalize() {
        return StrUtils.normalize(this.toString());
    }

    @Override
    public boolean isSupportedPDSType(DictionaryType type) {
        if (DictionaryType.DATE.equals(type)
                || DictionaryType.TIME.equals(type)
                || DictionaryType.CONTEXT_DEPENDENT.equals(type)
                || DictionaryType.CONTEXTDEPENDENT.equals(type)) {
            return true;
        }
        return false;
    }

}
