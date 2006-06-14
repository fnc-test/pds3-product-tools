// Copyright 2006, by the California Institute of 
// Technology. ALL RIGHTS RESERVED. United States Government 
// Sponsorship acknowledged. Any commercial use must be negotiated with 
// the Office of Technology Transfer at the California Institute of 
// Technology.
//
// This software may be subject to U.S. export control laws. By 
// accepting this software, the user agrees to comply with all 
// applicable U.S. export laws and regulations. User has the 
// responsibility to obtain export licenses, or other export authority 
// as may be required before exporting such information to foreign 
// countries or providing access to foreign persons.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools.dict;

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
    public static final String REQUIRED_ELEMENTS = "REQUIRED_ELEMENTS";
    public static final String OPTIONAL_OBJECTS = "OPTIONAL_OBJECTS";
    public static final String OPTIONAL_ELEMENTS = "OPTIONAL_ELEMENTS";
}