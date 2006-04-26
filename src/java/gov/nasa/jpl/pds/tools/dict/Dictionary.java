//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools.dict;

import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import gov.nasa.jpl.pds.tools.label.Label;
import gov.nasa.jpl.pds.tools.label.parser.LabelParser;
import gov.nasa.jpl.pds.tools.label.parser.LabelParserFactory;
import gov.nasa.jpl.pds.tools.label.parser.ParseException;

/**
 * This class represents a PDS data dictionary. 
 * @author pramirez
 * @version $Revision$
 * 
 */
public class Dictionary {
    private Map definitions;
    
    /**
     * Constructs a dictionary from a {@link Label}
     * @param label representation of the dictionary
     */
    public Dictionary(Label label) throws InvalidDictionaryException {
        definitions = new HashMap();
        loadDictionary(label);
    }

    /**
     * Constructs a dictionary from a URL
     * @param file
     */
    public Dictionary(URL file) throws InvalidDictionaryException, IOException {
        definitions = new HashMap();
        LabelParserFactory factory = LabelParserFactory.newInstance();
        LabelParser parser = factory.newLabelParser();
        try {
            loadDictionary(parser.parse(file));
        } catch (ParseException pe) {
            throw new InvalidDictionaryException(pe.getMessage());
        }
    }
    
    private void loadDictionary(Label label) throws InvalidDictionaryException {
        //TODO: Read label and create definitions
    }
    
    /**
     * Merges dictionary without overwriting
     * @param dictionary to be merged
     */
    public void merge(Dictionary dictionary) {
        merge(dictionary, false);
    }
    
    /**
     * Merges the definitions in the dictionaries
     * @param dictionary to be merged
     * @param overwrite flag
     */
    public void merge(Dictionary dictionary, boolean overwrite) {
        if (overwrite)
            definitions.putAll(dictionary.definitions);
        else {
            Map d = new HashMap(dictionary.definitions);
            d.putAll(definitions);
            definitions = d;
        }
    }
    
    /**
     * Tests to see whether or not a defintion exists
     * @param identifier of the definition
     * @return flag indicating existence
     */
    public boolean containsDefinition(String identifier) {
        return ((definitions.get(identifier) == null) ? false : true);
    }
    
    /**
     * Tests to see whether or not an object is defined
     * @param identifier of the object
     * @return flag indicating existence
     */
    public boolean containsObjectDefinition(String identifier) {
        Definition definition = (Definition) definitions.get(identifier);
        if (definition == null || !(definition instanceof ObjectDefinition))
            return false;
        return true;
    }
    
    /**
     * Tests to see whether or not a group is defined 
     * @param identifier of the the group
     * @return flag indicating existence
     */
    public boolean containsGroupDefinition(String identifier) {
        Definition definition = (Definition) definitions.get(identifier);
        if (definition == null || !(definition instanceof GroupDefinition))
            return false;
        return true;
    }
    
    /**
     * Tests to see whether or not an element is defined
     * @param identifier of the element
     * @return flag indicating existence
     */
    public boolean containsElementDefinition(String identifier) {
        Definition definition = (Definition) definitions.get(identifier);
        if (definition == null || !(definition instanceof ElementDefinition))
            return false;
        return true;
    }
    
    /**
     * Retrieves the definition from the dictionary or null if not found
     * @param identifier of the definition
     * @return the definition
     */
    public Definition getDefinition(String identifier) {
        return (Definition) definitions.get(identifier);
    }
    
    /**
     * Retrieves the object definition from the dictionary or null if not found
     * @param identifier of the definition
     * @return the object definition
     */
    public ObjectDefinition getObjectDefinition(String identifier) {
        Definition definition = (Definition) definitions.get(identifier);
        if (definition != null && definition instanceof ObjectDefinition)
            return (ObjectDefinition) definition;
        return null;
    }
    
    /**
     * Retrieves the group definition from the dictionary or null if not found
     * @param identifier of the definition
     * @return the group definition
     */
    public GroupDefinition getGroupDefinition(String identifier) {
        Definition definition = (Definition) definitions.get(identifier);
        if (definition != null && definition instanceof GroupDefinition)
            return (GroupDefinition) definition;
        return null;
    }
    
    /**
     * Retrieves the element definition from the dictionary or null if not found.
     * @param identifier of the definition
     * @return the element definition
     */
    public ElementDefinition getElementDefiniton(String identifier) {
        Definition definition = (Definition) definitions.get(identifier);
        if (definition != null && definition instanceof ElementDefinition)
            return (ElementDefinition) definition;
        return null;
    }
}
