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

/**
 * This enum class is used to determine the type of value that was parsed from
 * the label. It is used to determine whether or not to retain single or double
 * quotes in a value when providing the string representation of the value.
 * 
 * 
 * @author mcayanan
 * @version $Revision$
 * 
 */
public enum ValueType {
  SINGLE_QUOTED, DOUBLE_QUOTED, QUOTED_UNTERMINATED
}
