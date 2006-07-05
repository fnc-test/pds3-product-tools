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

import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.dict.ElementDefinition;
import gov.nasa.pds.tools.dict.GroupDefinition;
import gov.nasa.pds.tools.dict.ObjectDefinition;
import junit.framework.*;

/**
 * JUnit test for dictionary class. The dictionary class represents a PDS
 * Compliant data dictionary.
 * @author pramirez
 * @version $Revision$
 * 
 */
public class DictionaryTest extends TestCase {
    
    public DictionaryTest(String name) {
        super(name);
    }
    
    public void testNoArgsCtor() {
        Dictionary dictionary = new Dictionary();
        assertEquals("", dictionary.getInformation());
        assertEquals(0, dictionary.getDefinitions().size());
    }

    public void testSetters() {
        Dictionary dictionary = new Dictionary();
        dictionary.setInformation("INFORMATION");
        assertEquals("INFORMATION", dictionary.getInformation());
    }
    
    public void testObjectMethods() {
        Dictionary dictionary = new Dictionary();
        ElementDefinition element = new ElementDefinition("TARGET_NAME");
        GroupDefinition group = new GroupDefinition("PARAMETERS");
        ObjectDefinition object = new ObjectDefinition("TABLE");
        
        dictionary.addDefinition(element);
        dictionary.addDefinition(group);
        dictionary.addDefinition(object);
        
        assertEquals(3, dictionary.getDefinitions().size());  
        assertEquals(element, dictionary.getDefinition("TARGET_NAME"));
        assertEquals(element, dictionary.getElementDefiniton("TARGET_NAME"));  
        assertEquals(group, dictionary.getDefinition("PARAMETERS"));
        assertEquals(group, dictionary.getGroupDefinition("PARAMETERS"));
        assertEquals(object, dictionary.getDefinition("TABLE"));
        assertEquals(object, dictionary.getObjectDefinition("TABLE"));
        
        Dictionary mergeDictionary = new Dictionary();
        ElementDefinition dataset = new ElementDefinition("DATA_SET_NAME");
        mergeDictionary.addDefinition(dataset);
        
        dictionary.merge(mergeDictionary);
        assertEquals(dictionary.getDefinition(dataset.getIdentifier()),dataset);
        
        assertTrue(dictionary.containsDefinition(dataset.getIdentifier()));
        assertTrue(dictionary.containsElementDefinition(element.getIdentifier()));
        assertTrue(dictionary.containsGroupDefinition(group.getIdentifier()));
        assertTrue(dictionary.containsObjectDefinition(object.getIdentifier()));
        
        dictionary.addDefinition(new ObjectDefinition("IMAGE"));
        dictionary.addDefinition(new ElementDefinition("ABCD_IMAGE"));
        assertEquals("IMAGE", dictionary.findObjectClassDefinition("XYZ_IMAGE").getIdentifier());
    }
}
