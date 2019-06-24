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
public class GroupDefinitionTest extends TestCase {
    public GroupDefinitionTest(String name) {
        super(name);
    }

    public void testCtor() {
        GroupDefinition group = new GroupDefinition(new Dictionary(), 1,
                DictIDFactory.createGroupDefId("PARAMETERS"));

        assertEquals("PARAMETERS", group.getIdentifier().toString());
        assertEquals(0, group.getRequiredElements().size());
        assertEquals(0, group.getOptionalElements().size());
    }

    public void testSetters() {
        GroupDefinition group = new GroupDefinition(new Dictionary(), 1,
                DictIDFactory.createGroupDefId("PARAMETERS"));

        assertEquals("PARAMETERS", group.getIdentifier().toString());
        group.setIdentifier(DictIDFactory.createGroupDefId("PARAMETERS_TEST"));
        assertEquals("PARAMETERS_TEST", group.getIdentifier().toString());

        assertEquals(0, group.getRequiredElements().size());
        DictIdentifier elementDef = DictIDFactory.createElementDefId("TEST");
        group.addRequired(elementDef);
        assertEquals("TEST", group.getRequiredElements().get(0).toString());

        assertEquals(0, group.getOptionalElements().size());
        group.addOptional(elementDef);
        assertEquals("TEST", group.getOptionalElements().get(0).toString());
    }

    public void testObjectMethods() {
        GroupDefinition group = new GroupDefinition(new Dictionary(), 1,
                DictIDFactory.createGroupDefId("PARAMETERS"));

        List<DictIdentifier> elements = new ArrayList<DictIdentifier>();
        DictIdentifier testElement = DictIDFactory.createElementDefId("TEST");
        elements.add(testElement);
        group.addRequired(elements);
        assertTrue(group.isAllowed(testElement));
        assertTrue(group.isRequired(testElement));

        assertTrue(!group.allowsAnyElement());
        DictIdentifier blahElement = DictIDFactory.createElementDefId("blah");
        List<DictIdentifier> wildList = new ArrayList<DictIdentifier>();
        wildList.add(Definition.WILDCARD_ELEMENT);
        group.addOptional(wildList);
        assertTrue(group.isAllowed(blahElement));
        assertTrue(group.allowsAnyElement());
    }
}
