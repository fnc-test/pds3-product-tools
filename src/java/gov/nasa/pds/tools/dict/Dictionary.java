// Copyright 2006-2007, by the California Institute of Technology.
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

import gov.nasa.pds.tools.label.validate.Status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class represents a PDS data dictionary. 
 * @author pramirez
 * @version $Revision$
 * 
 */
public class Dictionary implements Status {
    private Map definitions;
    private Map aliases;
    private String information;
    private Map units;
    private List unitList;
    private String status;
    
    public Dictionary() {
        definitions = new HashMap();
        aliases = new HashMap();
        units = new HashMap();
        unitList = new ArrayList();
        information = "";
        status = Status.UNKNOWN;
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
        if (overwrite) {
            definitions.putAll(dictionary.definitions);
            aliases.putAll(dictionary.aliases);
        } else {
            Map d = new HashMap(dictionary.definitions);
            d.putAll(definitions);
            definitions = d;
            Map a = new HashMap(dictionary.aliases);
            a.putAll(aliases);
            aliases = a;
        }
    }
    
    /**
     * Tests to see whether or not a definition exists
     * @param identifier of the definition
     * @return flag indicating existence
     */
    public boolean containsDefinition(String identifier) {
        if (definitions.containsKey(identifier) || aliases.containsKey(identifier))
            return true;
        return false;
    }
    
    /**
     * Tests to see whether or not an object is defined
     * @param identifier of the object
     * @return flag indicating existence
     */
    public boolean containsObjectDefinition(String identifier) {
        Definition definition = (Definition) definitions.get(identifier);
        if (definition == null)
            definition = (Definition) aliases.get(identifier);
        if (definition != null && definition instanceof ObjectDefinition)
            return true;
        return false;
    }
    
    /**
     * Tests to see whether or not a group is defined 
     * @param identifier of the the group
     * @return flag indicating existence
     */
    public boolean containsGroupDefinition(String identifier) {
        Definition definition = (Definition) definitions.get(identifier);
        if (definition == null)
            definition = (Definition) aliases.get(identifier);
        if (definition != null && definition instanceof GroupDefinition)
            return true;
        return false;
    }
    
    /**
     * Tests to see whether or not an element is defined
     * @param identifier of the element
     * @return flag indicating existence
     */
    public boolean containsElementDefinition(String identifier) {
        return containsElementDefinition(null, identifier);
    }
    
    public boolean containsElementDefinition(String objectContext, String identifier) {
        Definition definition = null;
        
        if (objectContext != null) {
            definition = (Definition) aliases.get(objectContext + "." + identifier);
            if (definition != null && definition instanceof ElementDefinition)
                return true;
        }
        
        definition = (Definition) definitions.get(identifier);
        if (definition != null && definition instanceof ElementDefinition)
            return true;
        
        return false;
    }
    
    /**
     * Retrieves the definition from the dictionary or null if not found
     * @param identifier of the definition
     * @return the definition
     */
    public Definition getDefinition(String identifier) {
        Definition definition = (Definition) definitions.get(identifier);
        if (definition == null)
            definition = (Definition) aliases.get(identifier);
        return definition;
    }
    
    /**
     * Retrieves the object definition from the dictionary or null if not found
     * @param identifier of the definition
     * @return the object definition
     */
    public ObjectDefinition getObjectDefinition(String identifier) {
        Definition definition = (Definition) definitions.get(identifier);
        if (definition == null)
            definition = (Definition) aliases.get(identifier);
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
        if (definition == null)
            definition = (Definition) aliases.get(identifier);
        if (definition != null && definition instanceof GroupDefinition)
            return (GroupDefinition) definition;
        return null;
    }
    
    /**
     * Retrieves the element definition from the dictionary or null if not found.
     * @param identifier of the definition
     * @return the element definition
     */
    public ElementDefinition getElementDefinition(String identifier) {
        return getElementDefinition(null, identifier);
    }
    
    public ElementDefinition getElementDefinition(String objectContext, String identifier) {
        Definition definition = null;
        
        if (objectContext != null) {
            definition = (Definition) aliases.get(objectContext + "." + identifier);
            if (definition != null && definition instanceof ElementDefinition)
                return (ElementDefinition) definition;
        }
        
        definition = (Definition) definitions.get(identifier);
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
            if (definition instanceof ElementDefinition) {
                ElementDefinition elementDefinition = (ElementDefinition) definition;
                elementDefinition.setUnitList(unitList);
            }
            for (Iterator i = definition.getAliases().iterator(); i.hasNext();) {
                String alias = i.next().toString();
                if (overwrite || (!overwrite && !aliases.containsKey(alias)))
                   aliases.put(alias, definition);
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
    public void addDefinitions(Collection definitions, boolean overwrite) {
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
    public void addDefinitions(Collection definitions) {
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
        String className = identifier;
        boolean done = false;
        
        while (definition == null && !done) {
            if (containsObjectDefinition(className)) {
                definition = getObjectDefinition(className);
            } else {
                if (className.indexOf("_") == -1 || className.indexOf("_") == className.length()-1)
                    done = true;
                else
                    className = className.substring(className.indexOf("_") + 1);
            }      
        }
        
        return definition;
    }
    
    /**
     * Retrieves the class definition for a group with the given identifier. 
     * This method will search the dictionary for a GroupDefinition whose 
     * identifier is the greatest length and matches the end of the given identifier
     * @param identifier to lookup up class of
     * @return {@link GroupDefinition} of class that will constrain object with 
     * given identifier. Returns null if not found.
     */
    public GroupDefinition findGroupClassDefinition(String identifier) {
        GroupDefinition definition = null;
        String className = identifier;
        boolean done = false;
        
        while (definition == null && !done) {
            if (containsGroupDefinition(className))
                definition = getGroupDefinition(className);
            else {
                if (className.indexOf("_") == -1 || className.indexOf("_") == className.length()-1)
                    done = true;
                else
                    className = className.substring(className.indexOf("_") + 1);
            }      
        }
        
        return definition;
    }
    
    /**
     * Retrieves the map of definitions
     * @return map of definitions.
     */
    public Map getDefinitions() {
        return definitions;
    }
    
    /**
     * Retrieves map of valid units.
     * @return map of UNIT_ID to list of valid units
     */
    public Map getUnits() {
        return units;
    }
    
    /**
     * Sets the valid units for use when performing validation against this dictionary
     * @param units mapped set of units of the form UNIT_ID to units list
     *              (A) -> ('A', 'AMPERE')
     */
    public void setUnits(Map units) {
        this.units = units;
        unitList = new ArrayList();
        for (Iterator i = units.values().iterator(); i.hasNext(); ) {
            unitList.addAll((List) i.next());
        }
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        //Make sure we aren't trying to set status to unknown
        if (!UNKNOWN.equals(status)) {
           //Set to pass if unknown 
           //Set to fail if that is the status being passed in
           //Drop everything else
           if (PASS.equals(status) && UNKNOWN.equals(this.status))
              this.status = PASS;
           else if (FAIL.equals(status))
              this.status = FAIL;
        }
    }
}
