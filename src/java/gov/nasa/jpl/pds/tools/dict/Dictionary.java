//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools.dict;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents a PDS data dictionary. 
 * @author pramirez
 * @version $Revision$
 * 
 */
public class Dictionary {
    private Map definitions;
    private String information;
    
    public Dictionary() {
        definitions = new HashMap();
        information = "";
        //TODO: add support for units
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
    
    /**
     * Adds a definition to this dictionary to. Overwrites any existing definition.
     * @param definition to be added to the dictionary
     */
    public void addDefinition(Definition definition) {
        addDefinition(definition, true);
    }
    
    /**
     * Adds a defintion to this dictionary. The flag indicates whether a definition 
     * should be overwriten.
     * @param definition to be added to the dictionary
     * @param overwrite indicates if definition should be overwriten
     */
    public void addDefinition(Definition definition, boolean overwrite) {
        if (overwrite || (!overwrite && !definitions.containsKey(definition.getIdentifier()))) {
            definitions.put(definition.getIdentifier(), definition);
            for (Iterator i = definition.getAliases().iterator(); i.hasNext();) {
                String alias = (String) i.next();
                definitions.put(alias, definition);
            }
        }
    }
    
    /**
     * Sets the description information for a dictionary. This is often captured informally in 
     * comments at the top of a dictionary file.
     * @param information
     */
    public void setInformation(String information) {
        this.information = information;
    }
    
    /**
     * Return the dictionary's descriptive information.
     * @return the information
     */
    public String getInformation() {
        return information;
    }
    
    /**
     * Adds a list of defintions to this dictionary. The flag indicates whether the 
     * definitions should be overwriten.
     * @param definitions to be added to the dictionary
     * @param overwrite
     */
    public void addDefinitions(List definitions, boolean overwrite) {
        for (Iterator i = definitions.iterator(); i.hasNext();) {
            Definition d = (Definition) i.next();
            addDefinition(d, overwrite);
        }
    }
    
    /**
     * Adds a list of defintions to this dictionary. By default definitions will be 
     * overwritten.
     * @param definitions to be added to the dictionary
     */
    public void addDefinitions(List definitions) {
        addDefinitions(definitions, true);
    }
    
    /**
     * Retrieves the class definition for an object with the given identifier. 
     * This method will search the dictionary for an ObjectDefinition whose 
     * identifier is the greatest length and matches the end of the given identifier
     * @param identifier to lookup up class of
     * @return {@link ObjectDefinition} of class that will constrain object with 
     * given identifier. Returns null if not found.
     */
    public ObjectDefinition findObjectClassDefinition(String identifier) {
        ObjectDefinition definition = null;
        
        //Look for maximum length match of object definition identifier with given identifier
        for (Iterator i = definitions.keySet().iterator(); i.hasNext();) {
            String definitionIdentifier = (String) i.next();
            if (identifier.endsWith(definitionIdentifier) &&
                    definitions.get(definitionIdentifier) instanceof ObjectDefinition) {
                ObjectDefinition d = (ObjectDefinition) definitions.get(definitionIdentifier);
                if (definition == null || d.getIdentifier().length() > definition.getIdentifier().length()) {
                    definition = d;
                }
            }
        }
        
        return definition;
    }
    
    /**
     * Retrieves the map of definitions
     * @return the map of definitions.
     */
    protected Map getDefinitions() {
        return definitions;
    }
}
