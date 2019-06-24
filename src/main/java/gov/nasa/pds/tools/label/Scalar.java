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

import gov.nasa.pds.tools.constants.Constants.DictionaryType;

/**
 * This class represents a Scalar which can be assigned to an attribute or be a
 * part of a Set or Sequence.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public abstract class Scalar implements Value {

  private String value;

  private ValueType type;

  protected Scalar(String value) {
    this(value, null);
  }

  /**
   * Constructs a Scalar
   * 
   * @param value
   */
  protected Scalar(String value, ValueType type) {
    this.value = value;
    this.type = type;
  }

  /**
   * Retrieves the value
   * 
   * @return the value
   */
  public String getValue() {
    return this.value;
  }

  @Override
  public String toString() {
    return this.value;
  }

  /**
   * Provides a string representation of the Scalar value with single or double
   * quotes surrounding it if the retainQuotes flag is set to true and the
   * ValueType was SINGLE_QUOTED or DOUBLE_QUOTED.
   * 
   * @param retainQuotes
   *          Set to true to return the value with quotes surrounding it if it
   *          was originally there in the label.
   * 
   * @return The string.
   */
  public String toString(boolean retainQuotes) {
    String result = "";
    if (type != null) {
      if (type == ValueType.SINGLE_QUOTED) {
        result = "'" + this.value + "'";
      } else if (type == ValueType.DOUBLE_QUOTED) {
        result = "\"" + this.value + "\"";
      }
    } else {
      result = this.value;
    }
    return result;
  }

  public abstract boolean isSupportedPDSType(DictionaryType type);

  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Scalar)) {
      return false;
    }
    Scalar that = (Scalar) object;
    return this.value.equals(that.getValue());
  }

  public int hashcode() {
    return this.value.hashCode();
  }
}
