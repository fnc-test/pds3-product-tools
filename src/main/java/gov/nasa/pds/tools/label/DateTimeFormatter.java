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

import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.constants.Constants.ProblemType;

import java.util.Date;

/**
 * Implements a parser for a PDS3 date/time string.
 * 
 * @author pramirez
 * @author jagander
 * @author merose
 * @version $Revision$
 */
public class DateTimeFormatter {
  /**
   * Parses a date/time string into a Java {@link Date}. Throws error exceptions
   * with respect to a designated label, if there are parse errors.
   * 
   * @param label
   *          the label in which the date/time string appears
   * @param dateTime
   *          the date/time string from the label
   * @param lineNumber
   *          the line number within the label where the date/time string
   *          appears
   * @return the date corresponding to the date/time string
   * @throws LabelParserException
   *           if there are any parse errors
   */
  public static Date parse(final Label label, final String dateTime,
      final int lineNumber) throws LabelParserException {
    try {
      return ParseDateTime.parse(dateTime);
    } catch (ParseDateTime.ParseException ex) {
      if (ex.getField() < 0) {
        throw new LabelParserException(label, lineNumber, null,
            "parser.error.invalidDate", //$NON-NLS-1$
            ProblemType.INVALID_DATE, dateTime);
      }

      throw new LabelParserException(label, lineNumber, null,
          "parser.error.dateOutOfRange", //$NON-NLS-1$
          ProblemType.INVALID_DATE, dateTime);
    }
  }

  public static Date lenientParse(final Label label, final String dateTime,
      final int lineNumber) throws LabelParserException {
    try {
      return ParseDateTime.lenientParse(dateTime);
    } catch (ParseDateTime.ParseException ex) {
      // ignore
    }

    throw new LabelParserException(label, lineNumber, null,
        "parser.error.invalidDate", //$NON-NLS-1$
        ProblemType.INVALID_DATE, dateTime);
  }
}
