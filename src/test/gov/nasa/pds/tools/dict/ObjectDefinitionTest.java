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
