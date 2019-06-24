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

package gov.nasa.pds.tools.dict;

/**
 * This class is used to capture tokens that are found in element, object, and
 * group definitions within a PDS dictionary.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
@SuppressWarnings("nls")
public class DictionaryTokens {
    private DictionaryTokens() {
        // noop
    }

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
    public static final String OBJECT_TYPE_SPECIFIC_GROUP = "SPECIFIC_GROUP";
    public static final String OBJECT_TYPE_SPECIFIC = "SPECIFIC";
    public static final String OBJECT_TYPE_GENERIC = "GENERIC";
    public static final String OBJECT_TYPE = "OBJECT_TYPE";
    public static final String NOT_APPLICABLE = "N/A";
    public static final int ELEMENT_IDENT_LENGTH = 30;
    public static final int NAMESPACE_LENGTH = 30;
}
