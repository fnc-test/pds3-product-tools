// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// * Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// * Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// * Neither the name of Caltech nor its operating division, the Jet Propulsion
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

import gov.nasa.pds.tools.constants.Constants.DictionaryType;
import gov.nasa.pds.tools.dict.parser.DictIDFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
@SuppressWarnings("nls")
public class ElementDefinitionTest extends TestCase {
    public ElementDefinitionTest(String name) {
        super(name);
    }

    public void testCtors() {
        ElementDefinition definition = new ElementDefinition(new Dictionary(),
                1, DictIDFactory.createElementDefId("TEST"));

        assertEquals("TEST", definition.getIdentifier().toString());
        assertNull(definition.getDataType());
        assertNull(definition.getUnits());
        assertNull(definition.getValueType());
        assertEquals(new Integer(0), definition.getMinLength());
        assertEquals(new Integer(Integer.MAX_VALUE), definition.getMaxLength());
        assertEquals(0, definition.getValues().size());

        definition = new ElementDefinition(new Dictionary(
                new File("pdsdd.full")), 1, DictIDFactory
                .createElementDefId("TEST"));

        assertEquals("pdsdd.full", definition.getSourceDictionary()
                .getSourceString());
        assertEquals("TEST", definition.getIdentifier().toString());
        assertNull(definition.getDataType());
        assertNull(definition.getUnits());
        assertNull(definition.getValueType());
        assertEquals(new Integer(0), definition.getMinLength());
        assertEquals(new Integer(Integer.MAX_VALUE), definition.getMaxLength());
        assertEquals(0, definition.getValues().size());
    }

    public void testSetters() {
        ElementDefinition definition = new ElementDefinition(new Dictionary(),
                1, DictIDFactory.createElementDefId("TEST"));

        assertEquals("TEST : null", definition.toString());

        assertEquals("TEST", definition.getIdentifier().toString());
        definition.setIdentifier(DictIDFactory.createElementDefId("TEST_NEW"));
        assertEquals("TEST_NEW", definition.getIdentifier().toString());

        assertNull(definition.getDataType());
        definition.setDataType(DictionaryType.valueOf("ALPHANUMERIC"));
        assertEquals("ALPHANUMERIC", definition.getDataType().toString());

        assertNull(definition.getUnits());
        definition.setUnits("m**2");
        assertEquals("m**2", definition.getUnits());

        assertNull(definition.getValueType());
        definition.setValueType("INTEGER");
        assertEquals("INTEGER", definition.getValueType());

        assertFalse(definition.hasMinLength());
        assertEquals(new Integer(0), definition.getMinLength());
        definition.setMinLength(10);
        assertTrue(definition.hasMinLength());
        assertEquals(new Integer(10), definition.getMinLength());

        assertFalse(definition.hasMaxLength());
        assertEquals(new Integer(Integer.MAX_VALUE), definition.getMaxLength());
        definition.setMaxLength(100);
        assertTrue(definition.hasMaxLength());
        assertEquals(new Integer(100), definition.getMaxLength());

        assertFalse(definition.hasMinimum());
        assertNull(definition.getMinimum());
        definition.setMinimum(0);
        assertTrue(definition.hasMinimum());
        assertEquals(0, definition.getMinimum());

        assertFalse(definition.hasMaximum());
        assertNull(definition.getMaximum());
        definition.setMaximum(Integer.MAX_VALUE);
        assertTrue(definition.hasMaximum());
        assertEquals(Integer.MAX_VALUE, definition.getMaximum());

        assertNull(definition.getObjectType());
        definition.setObjectType("TEST");
        assertEquals("TEST", definition.getObjectType());

        assertEquals(0, definition.getValues().size());
        assertFalse(definition.hasValidValues());
        definition.addValue("TEST");
        assertTrue(definition.hasValidValues());
        assertEquals("TEST", ((ArrayList<String>) definition.getValues())
                .get(0));
        List<String> values = new ArrayList<String>();
        values.add("TEST");
        definition.setValues(values);
        assertEquals("TEST", ((ArrayList<String>) definition.getValues())
                .get(0));

        assertEquals(0, definition.getAliases().size());
        assertFalse(definition.hasAliases());
        List<Alias> aliases = new ArrayList<Alias>();
        aliases.add(new Alias("ALIAS1"));
        aliases.add(new Alias("CONTEXT2", "ALIAS2"));
        definition.addAliases(aliases);
        assertTrue(definition.hasAliases());
        assertEquals(2, definition.getAliases().size());
    }
}
