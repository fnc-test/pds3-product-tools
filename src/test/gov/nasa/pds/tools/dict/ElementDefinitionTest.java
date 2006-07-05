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

import gov.nasa.pds.tools.dict.ElementDefinition;

import java.util.ArrayList;
import java.util.List;

import junit.framework.*;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class ElementDefinitionTest extends TestCase {
    public ElementDefinitionTest(String name) {
        super(name);
    }
    
    public void testCtor() {
        ElementDefinition definition = new ElementDefinition("TEST");
      
        assertEquals("TEST", definition.getIdentifier());
        assertEquals(null, definition.getDataType());
        assertEquals(null, definition.getUnits());
        assertEquals(null, definition.getValueType());
        assertEquals(0, definition.getMinLength());
        assertEquals(Integer.MAX_VALUE, definition.getMaxLength());
        assertEquals(0, definition.getValues().size());
    }
    
    public void testSetters() {
        ElementDefinition definition = new ElementDefinition("TEST");
        
        assertEquals("TEST", definition.getIdentifier());
        definition.setIdentifier("TEST_NEW");
        assertEquals("TEST_NEW", definition.getIdentifier());
        
        assertEquals(null, definition.getDataType());
        definition.setDataType("DATA_TYPE");
        assertEquals("DATA_TYPE", definition.getDataType());
        
        assertEquals(null, definition.getUnits());
        definition.setUnits("m**2");
        assertEquals("m**2", definition.getUnits());
        
        assertEquals(null, definition.getValueType());
        definition.setValueType("INTEGER");
        assertEquals("INTEGER", definition.getValueType());
        
        assertEquals(0, definition.getMinLength());
        definition.setMinLength(10);
        assertEquals(10, definition.getMinLength());
        
        assertEquals(Integer.MAX_VALUE, definition.getMaxLength());
        definition.setMaxLength(100);
        assertEquals(100, definition.getMaxLength());
        
        assertEquals(0, definition.getValues().size());
        List values = new ArrayList();
        values.add("TEST");
        definition.setValues(values);
        assertEquals("TEST", ((ArrayList) definition.getValues()).get(0));
    }
}
