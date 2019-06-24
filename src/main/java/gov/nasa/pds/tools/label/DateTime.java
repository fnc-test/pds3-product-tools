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
   *          represented as a string which is in a PDS compatible format
   * @param lineNumber
   *          in which this value was found
   */
  public DateTime(final Label label, final String value, final int lineNumber)
      throws LabelParserException {
    this(label, value, lineNumber, false);
  }

  /**
   * @param value
   *          represented as a string which is in a PDS compatible format
   * @param lineNumber
   *          in which this value was found
   * @param lenient
   *          a flag to indicate how to parse the date
   */
  public DateTime(final Label label, final String value, final int lineNumber,
      final boolean lenient) throws LabelParserException {
    super(value);
    if (lenient) {
      this.date = DateTimeFormatter.lenientParse(label, value, lineNumber);
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
    if (DictionaryType.DATE.equals(type) || DictionaryType.TIME.equals(type)
        || DictionaryType.CONTEXT_DEPENDENT.equals(type)
        || DictionaryType.CONTEXTDEPENDENT.equals(type)) {
      return true;
    }
    return false;
  }

}
