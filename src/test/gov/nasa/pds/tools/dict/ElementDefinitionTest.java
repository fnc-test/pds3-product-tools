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
