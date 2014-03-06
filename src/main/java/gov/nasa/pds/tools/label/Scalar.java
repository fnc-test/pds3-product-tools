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
