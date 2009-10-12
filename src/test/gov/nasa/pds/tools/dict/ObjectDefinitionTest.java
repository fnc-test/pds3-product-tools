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

import gov.nasa.pds.tools.dict.parser.DictIDFactory;

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
public class ObjectDefinitionTest extends TestCase {

    public ObjectDefinitionTest(String name) {
        super(name);
    }

    public void testCtor() {
        ObjectDefinition definition = new ObjectDefinition(new Dictionary(), 1,
                DictIDFactory.createObjectDefId("TABLE"));
        assertEquals("TABLE", definition.getIdentifier().toString());
        assertEquals("", definition.getDescription());
        assertEquals("", definition.getStatusType());
        assertEquals(0, definition.getAliases().size());
        assertEquals(0, definition.getRequiredElements().size());
        assertEquals(0, definition.getOptionalElements().size());
        assertEquals(0, definition.getRequiredObjects().size());
        assertEquals(0, definition.getOptionalObjects().size());
    }

    public void testSetters() {
        ObjectDefinition definition = new ObjectDefinition(new Dictionary(), 1,
                DictIDFactory.createObjectDefId("TABLE"));

        assertEquals("TABLE", definition.getIdentifier().toString());
        definition.setIdentifier(DictIDFactory.createObjectDefId("IMAGE"));
        assertEquals("IMAGE", definition.getIdentifier().toString());

        assertEquals("", definition.getDescription());
        definition.setDescription("description");
        assertEquals("description", definition.getDescription());

        assertEquals("", definition.getStatusType());
        definition.setStatusType("APPROVED");
        assertEquals("APPROVED", definition.getStatusType());

        assertEquals(0, definition.getAliases().size());
        List<Alias> aliases = new ArrayList<Alias>();
        aliases.add(new Alias("XYZ_IMAGE"));
        definition.setAliases(aliases);
        assertEquals(new Alias("XYZ_IMAGE"), definition.getAliases().get(0));

        assertEquals(0, definition.getRequiredElements().size());
        DictIdentifier requiredElement = DictIDFactory
                .createElementDefId("TARGET_NAME");
        definition.addRequired(requiredElement);
        assertEquals("TARGET_NAME", definition.getRequiredElements().get(0)
                .toString());

        assertEquals(0, definition.getOptionalElements().size());
        DictIdentifier optionalElement = DictIDFactory
                .createElementDefId("MISSION_NAME");
        definition.addOptional(optionalElement);
        assertEquals("MISSION_NAME", definition.getOptionalElements().get(0)
                .toString());

        assertEquals(0, definition.getRequiredObjects().size());
        DictIdentifier requiredObj = DictIDFactory
                .createObjectDefId("TARGET_NAME");
        definition.addRequired(requiredObj);
        assertEquals("TARGET_NAME", definition.getRequiredObjects().get(0)
                .toString());

        assertEquals(0, definition.getOptionalObjects().size());
        DictIdentifier optionalObject = DictIDFactory
                .createObjectDefId("MISSION_NAME");
        definition.addOptional(optionalObject);
        assertEquals("MISSION_NAME", definition.getOptionalObjects().get(0)
                .toString());
    }

    public void testObjectMethods() {
        ObjectDefinition definition = new ObjectDefinition(new Dictionary(), 1,
                DictIDFactory.createObjectDefId("IMAGE"));

        assertEquals(0, definition.getAliases().size());
        definition.addAlias(new Alias("XYZ_IMAGE"));
        assertEquals("XYZ_IMAGE", definition.getAliases().get(0).toString());

        assertTrue(!definition.hasOptionalElements());
        assertTrue(!definition.hasRequiredElements());
        assertEquals(0, definition.getRequiredElements().size());
        List<DictIdentifier> required = new ArrayList<DictIdentifier>();
        DictIdentifier targetName = DictIDFactory
                .createElementDefId("TARGET_NAME");
        DictIdentifier blah = DictIDFactory.createElementDefId("BLAH");
        required.add(targetName);
        definition.addRequired(required);
        assertTrue(definition.isRequired(targetName));
        assertTrue(definition.isAllowed(targetName));
        assertTrue(!definition.isRequired(blah));
        assertTrue(!definition.isAllowed(blah));

        assertEquals(0, definition.getOptionalElements().size());
        DictIdentifier optionalElement = DictIDFactory
                .createElementDefId("MISSION_NAME");
        definition.addOptional(optionalElement);
        assertTrue(definition.isOptional(optionalElement));
        assertTrue(!definition.isRequired(optionalElement));
        assertTrue(definition.hasRequiredElements());
        assertTrue(definition.hasOptionalElements());

        assertTrue(!definition.hasRequiredObjects());
        assertEquals(0, definition.getRequiredObjects().size());
        DictIdentifier requiredObject = DictIDFactory
                .createObjectDefId("TARGET_NAME");
        definition.addRequired(requiredObject);
        assertTrue(definition.isRequired(requiredObject));
        assertTrue(definition.isAllowed(requiredObject));
        assertTrue(!definition.isRequired(blah));
        assertTrue(!definition.isAllowed(blah));
        assertTrue(definition.hasRequiredObjects());

        assertTrue(!definition.hasOptionalObjects());
        assertEquals(0, definition.getOptionalObjects().size());
        DictIdentifier optionalObject = DictIDFactory
                .createObjectDefId("MISSION_NAME");
        definition.addOptional(optionalObject);
        assertTrue(definition.isOptional(optionalObject));
        assertTrue(!definition.isRequired(optionalObject));
        assertTrue(definition.hasOptionalObjects());
    }

}
