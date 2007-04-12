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

package gov.nasa.pds.tools.dict;

/**
 * This interface is used to capture tokens that are found in 
 * element, object, and group defintions within a PDS dictionary.
 * @author pramirez
 * @version $Revision$
 * 
 */
public interface DictionaryTokens {
    public static final String GENERIC_GROUP = "GENERIC_GROUP_DEFINITION";
    public static final String SPECIFIC_GROUP = "SPECIFIC_GROUP_DEFINITION";
    public static final String DEFINITION = "DEFINITION";
    public static final String GENERIC_OBJECT = "GENERIC_OBJECT_DEFINITION";
    public static final String SPECIFIC_OBJECT = "SPECIFIC_OBJECT_DEFINITION";
    public static final String ELEMENT_DEFINITION = "ELEMENT_DEFINITION";
    public static final String NAME = "NAME";
    public static final String STATUS_TYPE = "STATUS_TYPE";
    public static final String DATA_TYPE = "GENERAL_DATA_TYPE";
    public static final String UNITS = "UNIT_ID";
    public static final String VALUE_TYPE = "STANDARD_VALUE_TYPE";
    public static final String MAX_LENGTH = "MAXIMUM_LENGTH";
    public static final String MIN_LENGTH = "MINIMUM_LENGTH";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String VALUES = "STANDARD_VALUE_SET";
    public static final String MINIMUM = "MINIMUM";
    public static final String MAXIMUM = "MAXIMUM";
    public static final String REQUIRED_OBJECTS = "REQUIRED_OBJECT_SET";
    public static final String REQUIRED_ELEMENTS = "REQUIRED_ELEMENT_SET";
    public static final String OPTIONAL_OBJECTS = "OPTIONAL_OBJECT_SET";
    public static final String OPTIONAL_ELEMENTS = "OPTIONAL_ELEMENT_SET";
    public static final String OBJECT_ALIASES = "OBJECT_ALIAS_SEQUENCE";
    public static final String ELEMENT_ALIASES = "ELEMENT_ALIAS_SEQUENCE";
    public static final String UNIT_SEQUENCE = "UNIT_SEQUENCE";
    public static final String ALIAS_LIST = "ALIAS_LIST";
    public static final String UNIT_LIST = "UNIT_LIST";
    public static final String VALUE_TYPE_SUGGESTED = "SUGGESTED";
    public static final String VALUE_TYPE_STATIC = "STATIC";
    public static final String OBJECT_TYPE_GENERIC_GROUP = "GENERIC_GROUP";
    public static final String OBJECT_TYPE = "OBJECT_TYPE";
    public static final int ELEMENT_IDENT_LENGTH = 30;
    public static final int NAMESPACE_LENGTH = 30;
}
