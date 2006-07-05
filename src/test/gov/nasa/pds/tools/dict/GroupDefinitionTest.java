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

package gov.nasa.pds.tools.dict;

import gov.nasa.pds.tools.dict.GroupDefinition;

import java.util.ArrayList;
import java.util.List;

import junit.framework.*;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class GroupDefinitionTest extends TestCase {
    public GroupDefinitionTest(String name) {
        super(name);
    }
    
    public void testCtor() {
        GroupDefinition group = new GroupDefinition("PARAMETERS");
        
        assertEquals("PARAMETERS", group.getIdentifier());
        assertEquals(0, group.getRequiredElements().size());
        assertEquals(0, group.getOptionalElements().size());
    }
    
    public void testSetters() {
        GroupDefinition group = new GroupDefinition("PARAMETERS");
        
        assertEquals("PARAMETERS", group.getIdentifier());
        group.setIdentifier("PARAMETERS_TEST");
        assertEquals("PARAMETERS_TEST", group.getIdentifier());
        
        assertEquals(0, group.getRequiredElements().size());
        List elements = new ArrayList();
        elements.add("TEST");
        group.setRequiredElements(elements);
        assertEquals("TEST", group.getRequiredElements().get(0));
        
        assertEquals(0, group.getOptionalElements().size());
        group.setOptionalElements(elements);
        assertEquals("TEST", group.getOptionalElements().get(0));
    }
    
    public void testObjectMethods() {
        GroupDefinition group = new GroupDefinition("PARAMETERS");
        
        List elements = new ArrayList();
        elements.add("TEST");
        group.setRequiredElements(elements);
        assertTrue(group.canHaveElement("TEST"));
        assertTrue(group.mustHaveElement("TEST"));
    }
}
