//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools.dict;

import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import gov.nasa.jpl.pds.tools.label.Label;
import gov.nasa.jpl.pds.tools.label.parser.LabelParser;
import gov.nasa.jpl.pds.tools.label.parser.LabelParserFactory;
import gov.nasa.jpl.pds.tools.label.parser.ParseException;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class Dictionary {
    private Map definitions;
    
    /**
     * 
     * @param label
     */
    public Dictionary(Label label) throws InvalidDictionaryException {
        definitions = new HashMap();
        loadDictionary(label);
    }

    /**
     * 
     * @param file
     */
    public Dictionary(URL file) throws InvalidDictionaryException {
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
     * 
     * @param dictionary
     */
    public void merge(Dictionary dictionary) {
        merge(dictionary, false);
    }
    
    /**
     * 
     * @param dictionary
     * @param overwrite
     */
    public void merge(Dictionary dictionary, boolean overwrite) {
        if (overwrite)
            definitions.putAll(dictionary.definitions);
        else
            dictionary.definitions.putAll(definitions);
    }
    
    /**
     * 
     * @param identifier
     * @return
     */
    public boolean containsDefinition(String identifier) {
        return false;
    }
    
    /**
     * 
     * @param identifier
     * @return
     */
    public boolean containsObjectDefinition(String identifier) {
        return false;
    }
    
    /**
     * 
     * @param identifier
     * @return
     */
    public boolean containsGroupDefinition(String identifier) {
        return false;
    }
    
    /**
     * 
     * @param identifier
     * @return
     */
    public boolean containsElementDefinition(String identifier) {
        return false;
    }
    
    /**
     * 
     * @param identifier
     * @return
     */
    public Definition getDefinition(String identifier) {
        return null;
    }
    
    /**
     * 
     * @param identifier
     * @return
     */
    public ObjectDefinition getObjectDefinition(String identifier) {
        return null;
    }
    
    /**
     * 
     * @param identifier
     * @return
     */
    public GroupDefinition getGroupDefinition(String identifier) {
        return null;
    }
    
    /**
     * 
     * @param identifier
     * @return
     */
    public ElementDefinition getElementDefiniton(String identifier) {
        return null;
    }
}
