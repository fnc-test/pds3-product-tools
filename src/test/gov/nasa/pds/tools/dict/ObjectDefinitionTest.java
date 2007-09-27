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

import gov.nasa.pds.tools.dict.ObjectDefinition;

import java.util.ArrayList;
import java.util.List;

import junit.framework.*;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class ObjectDefinitionTest extends TestCase {
    
    public ObjectDefinitionTest(String name) {
        super(name);
    }
    
    public void testCtor() {
        ObjectDefinition definition = new ObjectDefinition("TABLE");
        
        assertEquals("TABLE", definition.getIdentifier());
        assertEquals("", definition.getDescription());
        assertEquals("", definition.getStatusType());
        assertEquals(0, definition.getAliases().size());
        assertEquals(0, definition.getRequiredElements().size());
        assertEquals(0, definition.getOptionalElements().size());
        assertEquals(0, definition.getRequiredObjects().size());
        assertEquals(0, definition.getOptionalObjects().size());
    }
    
    public void testSetters() {
        ObjectDefinition definition = new ObjectDefinition("TABLE");
        
        assertEquals("TABLE", definition.getIdentifier());
        definition.setIdentifier("IMAGE");
        assertEquals("IMAGE", definition.getIdentifier());
        
        assertEquals("", definition.getDescription());
        definition.setDescription("description");
        assertEquals("description", definition.getDescription());
        
        assertEquals("", definition.getStatusType());
        definition.setStatusType("APPROVED");
        assertEquals("APPROVED", definition.getStatusType());
        
        assertEquals(0, definition.getAliases().size());
        List aliases = new ArrayList();
        aliases.add("XYZ_IMAGE");
        definition.setAliases(aliases);
        assertEquals("XYZ_IMAGE", definition.getAliases().get(0));
        
        assertEquals(0, definition.getRequiredElements().size());
        List required = new ArrayList();
        required.add("TARGET_NAME");
        definition.setRequiredElements(required);
        assertEquals("TARGET_NAME", definition.getRequiredElements().get(0));
        
        assertEquals(0, definition.getOptionalElements().size());
        List optional = new ArrayList();
        optional.add("MISSION_NAME");
        definition.setOptionalElements(optional);
        assertEquals("MISSION_NAME", definition.getOptionalElements().get(0));
        
        assertEquals(0, definition.getRequiredObjects().size());
        definition.setRequiredObjects(required);
        assertEquals("TARGET_NAME", definition.getRequiredObjects().get(0));
        
        assertEquals(0, definition.getOptionalObjects().size());
        definition.setOptionalObjects(optional);
        assertEquals("MISSION_NAME", definition.getOptionalElements().get(0));
    }
    
    public void testObjectMethods() {
        ObjectDefinition definition = new ObjectDefinition("IMAGE");
        
        assertEquals(0, definition.getAliases().size());
        definition.addAlias(new Alias("XYZ_IMAGE"));
        assertEquals("XYZ_IMAGE", definition.getAliases().get(0).toString());
        
        assertEquals(0, definition.getRequiredElements().size());
        List required = new ArrayList();
        required.add("TARGET_NAME");
        definition.setRequiredElements(required);
        assertTrue(definition.isElementRequired("TARGET_NAME"));
        assertTrue(definition.isElementPossible("TARGET_NAME"));
        assertTrue(!definition.isElementRequired("BLAH"));
        assertTrue(!definition.isElementPossible("BLAH"));
        
        assertEquals(0, definition.getOptionalElements().size());
        List optional = new ArrayList();
        optional.add("MISSION_NAME");
        definition.setOptionalElements(optional);
        assertTrue(definition.isElementOptional("MISSION_NAME"));
        assertTrue(!definition.isElementRequired("MISSION_NAME"));
        
        assertEquals(0, definition.getRequiredObjects().size());
        definition.setRequiredObjects(required);
        assertTrue(definition.isObjectRequired("TARGET_NAME"));
        assertTrue(definition.isObjectPossible("TARGET_NAME"));
        assertTrue(!definition.isObjectRequired("BLAH"));
        assertTrue(!definition.isObjectPossible("BLAH"));
        
        assertEquals(0, definition.getOptionalObjects().size());
        definition.setOptionalObjects(optional);
        assertTrue(definition.isObjectOptional("MISSION_NAME"));
        assertTrue(!definition.isObjectRequired("MISSION_NAME"));
    }

}
