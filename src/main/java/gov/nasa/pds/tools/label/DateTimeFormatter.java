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
