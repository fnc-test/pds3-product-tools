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
