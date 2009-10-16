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

import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.constants.Constants.DictionaryType;
import gov.nasa.pds.tools.containers.SimpleDictionaryChange;
import gov.nasa.pds.tools.dict.parser.DictIDFactory;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents a PDS data dictionary.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class Dictionary implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Pattern VERSION_REGEX = Pattern
            .compile("Online Database: pdscat(.+) \\*"); //$NON-NLS-1$

    private Map<DictIdentifier, Definition> definitions = new HashMap<DictIdentifier, Definition>();

    private String information = ""; //$NON-NLS-1$

    private final URI dictionaryURI;

    private final File dictionaryFile;

    private Map<String, String> units = new HashMap<String, String>();

    private final List<SimpleDictionaryChange> mergeChanges = new ArrayList<SimpleDictionaryChange>();

    private final List<LabelParserException> problems = new ArrayList<LabelParserException>();

    public Dictionary() {
        this.dictionaryURI = null;
        this.dictionaryFile = null;
    }

    public Dictionary(URI dictionaryURI) {
        this.dictionaryURI = dictionaryURI;
        this.dictionaryFile = null;
    }

    public Dictionary(final File dictionaryFile) {
        this.dictionaryFile = dictionaryFile;
        this.dictionaryURI = null;
    }

    public URI getDictionaryURI() {
        return this.dictionaryURI;
    }

    public File getDictionaryFile() {
        return this.dictionaryFile;
    }

    public String getSourceString() {
        if (this.dictionaryFile != null) {
            return this.dictionaryFile.toString();
        }
        if (this.dictionaryURI != null) {
            return this.dictionaryURI.toString();
        }
        return null;
    }

    public String getVersion() {
        // TODO: have a better way of extracting version info than munged
        // comment parsing
        final String info = this.getInformation();
        final Matcher matcher = VERSION_REGEX.matcher(info);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;

    }

    /**
     * Merges the definitions in the dictionaries
     * 
     * @param dictionary
     *            to be merged into this dictionary. dictionary merged in will
     *            override values in this dictionary if scalars or be added if
     *            lists
     */

    public void merge(final Dictionary dictionary) {
        // iterate over passed in dictionary and put defs in local
        final Iterator<Entry<DictIdentifier, Definition>> it = dictionary.definitions
                .entrySet().iterator();
        while (it.hasNext()) {
            final Entry<DictIdentifier, Definition> entry = it.next();
            final DictIdentifier key = entry.getKey();
            final Definition overwrite = entry.getValue();
            // only try to do override if the def exists in the local dict
            if (this.definitions.containsKey(key)) {

                final Definition local = this.definitions.get(key);

                // first do stuff common to all types
                // merge aliases
                if (overwrite.hasAliases()) {
                    final List<Alias> newVals = overwrite.getAliases();
                    final List<Alias> oldVals = local.getAliases();

                    // iterate over local
                    for (final Alias alias : newVals) {
                        // alias didn't already exist
                        if (!oldVals.contains(alias) && !key.equals(alias)) {
                            local.addAliasSimple(alias);
                            // TODO: move out of loop and consolidate message?
                            addMerge(overwrite, "dictionary.text.aliasAdded", //$NON-NLS-1$
                                    alias);
                        }
                    }
                }

                // update description
                if (overwrite.hasDescription()) {
                    final String oldVal = local.getDescription();
                    final String newVal = overwrite.getDescription();
                    if (!newVal.equals(oldVal)) {
                        local.setDescription(newVal);
                        addMerge(overwrite,
                                "dictionary.text.descriptionChanged", newVal, //$NON-NLS-1$
                                oldVal);
                    }
                }

                // update status type
                // NOTE: not sure this should be overridden but... why not?
                // NOTE: could set to not approved since you're modding it but
                // not sure if worth it since falsly reports a value change you
                // didn't make
                if (overwrite.hasStatusType()) {
                    final String oldVal = local.getStatusType();
                    final String newVal = overwrite.getStatusType();
                    if (!newVal.equals(oldVal)) {
                        local.setStatusType(overwrite.getStatusType());
                        addMerge(overwrite,
                                "dictionary.text.statusTypeChanged", newVal, //$NON-NLS-1$
                                oldVal);
                    }
                }

                // TODO: skip and add problem if definition types disagree
                // update element definition type defs
                if (overwrite instanceof ElementDefinition) {
                    ElementDefinition overwriteElement = (ElementDefinition) overwrite;
                    ElementDefinition localElement = (ElementDefinition) local;

                    // update data type
                    if (overwriteElement.getDataType() != null) {
                        final DictionaryType oldVal = localElement
                                .getDataType();
                        final DictionaryType newVal = overwriteElement
                                .getDataType();
                        if (!newVal.equals(oldVal)) {
                            localElement.setDataType(newVal);
                            addMerge(overwrite,
                                    "dictionary.text.dataTypeChanged", newVal, //$NON-NLS-1$
                                    oldVal);
                        }
                    }

                    // update max value (ie 100 is below max of 101)
                    if (overwriteElement.hasMaximum()) {
                        final Number oldVal = localElement.getMaximum();
                        final Number newVal = overwriteElement.getMaximum();
                        if (!newVal.equals(oldVal)) {
                            localElement.setMaximum(newVal);
                            addMerge(overwrite,
                                    "dictionary.text.maximumChanged", newVal, //$NON-NLS-1$
                                    oldVal);
                        }
                    }

                    // update max length of value (ie FOO has length of 3)
                    if (overwriteElement.hasMaxLength()) {
                        final Integer oldVal = localElement.getMaxLength();
                        final Integer newVal = overwriteElement.getMaxLength();
                        if (!newVal.equals(oldVal)) {
                            localElement.setMaxLength(newVal);
                            addMerge(overwrite,
                                    "dictionary.text.maxLengthChanged", newVal, //$NON-NLS-1$
                                    oldVal);
                        }
                    }

                    // update min value
                    if (overwriteElement.hasMinimum()) {
                        final Number oldVal = localElement.getMinimum();
                        final Number newVal = overwriteElement.getMinimum();
                        if (!newVal.equals(oldVal)) {
                            localElement.setMinimum(newVal);
                            addMerge(overwrite,
                                    "dictionary.text.minimumChanged", newVal, //$NON-NLS-1$
                                    oldVal);
                        }
                    }

                    // update min length
                    if (overwriteElement.hasMinLength()) {
                        final Integer oldVal = localElement.getMinLength();
                        final Integer newVal = overwriteElement.getMinLength();
                        if (!newVal.equals(oldVal)) {
                            localElement.setMinLength(newVal);
                            addMerge(overwrite,
                                    "dictionary.text.minLengthChanged", newVal, //$NON-NLS-1$
                                    oldVal);
                        }
                    }

                    // update unit type
                    if (overwriteElement.getUnits() != null) {
                        final String oldVal = localElement.getUnits();
                        final String newVal = overwriteElement.getUnits();
                        if (!newVal.equals(oldVal)) {
                            localElement.setUnits(newVal);
                            addMerge(overwrite, "dictionary.text.unitsChanged", //$NON-NLS-1$
                                    newVal, oldVal);
                        }
                    }

                    // merge values
                    if (overwriteElement.hasValidValues()) {
                        final Collection<String> newVals = overwriteElement
                                .getValues();
                        final Collection<String> oldVals = localElement
                                .getValues();

                        // iterate over local
                        for (final String value : newVals) {
                            // alias didn't already exist
                            if (!oldVals.contains(value)) {
                                localElement.addValue(value);
                                // TODO: move out of loop and consolidate
                                // message?
                                addMerge(overwriteElement,
                                        "dictionary.text.valueAdded", //$NON-NLS-1$
                                        value);
                            }
                        }
                    }

                    // update value type
                    if (overwriteElement.getValueType() != null) {
                        final String oldVal = localElement.getValueType();
                        final String newVal = overwriteElement.getValueType();
                        if (!newVal.equals(oldVal)) {
                            localElement.setValueType(newVal);
                            addMerge(overwrite,
                                    "dictionary.text.valueTypeChanged", //$NON-NLS-1$
                                    newVal, oldVal);
                        }

                    }

                } else if (overwrite instanceof GroupDefinition
                        || overwrite instanceof ObjectDefinition) {
                    ContainerDefinition overwriteElement = (ContainerDefinition) overwrite;
                    ContainerDefinition localElement = (ContainerDefinition) local;

                    // merge option elements
                    if (overwriteElement.hasOptionalElements()) {
                        final List<DictIdentifier> newVals = overwriteElement
                                .getOptionalElements();
                        final List<DictIdentifier> oldVals = localElement
                                .getOptionalElements();

                        // iterate over local
                        for (final DictIdentifier value : newVals) {
                            // value didn't already exist
                            if (!oldVals.contains(value)) {
                                localElement.addOptional(value);
                                // TODO: move out of loop and consolidate
                                // message?
                                addMerge(overwriteElement,
                                        "dictionary.text.optionalElementAdded", //$NON-NLS-1$
                                        value);
                            }
                        }
                    }

                    // merge required elements
                    if (overwriteElement.hasRequiredElements()) {
                        final List<DictIdentifier> newVals = overwriteElement
                                .getRequiredElements();
                        final List<DictIdentifier> oldVals = localElement
                                .getRequiredElements();

                        // iterate over local
                        for (final DictIdentifier value : newVals) {
                            // value didn't already exist
                            if (!oldVals.contains(value)) {
                                localElement.addRequired(value);
                                // TODO: move out of loop and consolidate
                                // message?
                                addMerge(overwriteElement,
                                        "dictionary.text.requiredElementAdded", //$NON-NLS-1$
                                        value);
                            }
                        }
                    }

                }
                if (overwrite instanceof ObjectDefinition) {
                    ObjectDefinition overwriteElement = (ObjectDefinition) overwrite;
                    ObjectDefinition localElement = (ObjectDefinition) local;

                    // merge optional objects
                    if (overwriteElement.hasOptionalObjects()) {
                        final List<DictIdentifier> newVals = overwriteElement
                                .getOptionalObjects();
                        final List<DictIdentifier> oldVals = localElement
                                .getOptionalObjects();

                        // iterate over local
                        for (final DictIdentifier value : newVals) {
                            // value didn't already exist
                            if (!oldVals.contains(value)) {
                                localElement.addOptional(value);
                                // TODO: move out of loop and consolidate
                                // message?
                                addMerge(overwriteElement,
                                        "dictionary.text.optionalObjectAdded", //$NON-NLS-1$
                                        value);
                            }
                        }
                    }

                    // merge optional objects
                    if (overwriteElement.hasRequiredObjects()) {
                        final List<DictIdentifier> newVals = overwriteElement
                                .getRequiredObjects();
                        final List<DictIdentifier> oldVals = localElement
                                .getRequiredObjects();

                        // iterate over local
                        for (final DictIdentifier value : newVals) {
                            // value didn't already exist
                            if (!oldVals.contains(value)) {
                                localElement.addRequired(value);
                                // TODO: move out of loop and consolidate
                                // message?
                                addMerge(overwriteElement,
                                        "dictionary.text.requiredObjectAdded", //$NON-NLS-1$
                                        value);
                            }
                        }
                    }

                }
            } else {
                this.definitions.put(key, overwrite);
                addMerge(overwrite, "dictionary.text.definitionAdded", //$NON-NLS-1$
                        key);
            }
        }
    }

    /**
     * Tests to see whether or not a definition exists
     * 
     * @param identifier
     *            of the definition
     * @return flag indicating existence
     */
    public boolean containsDefinition(DictIdentifier identifier) {
        return this.definitions.containsKey(identifier);
    }

    /**
     * Tests to see whether or not an object is defined
     * 
     * @param identifier
     *            of the object
     * @return flag indicating existence
     */
    public boolean containsObjectDefinition(DictIdentifier identifier) {
        return containsDefinition(identifier);
    }

    /**
     * Tests to see whether or not a group is defined
     * 
     * @param identifier
     *            of the the group
     * @return flag indicating existence
     */
    public boolean containsGroupDefinition(DictIdentifier identifier) {
        return containsDefinition(identifier);
    }

    /**
     * Tests to see whether or not an element is defined
     * 
     * @param identifier
     *            of the element
     * @return flag indicating existence
     */
    public boolean containsElementDefinition(DictIdentifier identifier) {
        return containsElementDefinition(null, identifier);
    }

    public boolean containsElementDefinition(String objectContext,
            DictIdentifier identifier) {

        // try with context first
        if (objectContext != null) {
            DictIdentifier id = DictIDFactory.createElementDefId(objectContext
                    + "." + identifier.toString()); //$NON-NLS-1$
            if (containsDefinition(id)) {
                return true;
            }
        }

        return containsDefinition(identifier);
    }

    /**
     * Retrieves the definition from the dictionary or null if not found
     * 
     * @param identifier
     *            of the definition
     * @return the definition
     */
    public Definition getDefinition(DictIdentifier identifier) {
        return this.definitions.get(identifier);
    }

    /**
     * Retrieves the object definition from the dictionary or null if not found
     * 
     * @param identifier
     *            of the definition
     * @return the object definition
     */
    public ObjectDefinition getObjectDefinition(DictIdentifier identifier) {
        Definition definition = this.definitions.get(identifier);
        if (definition != null && definition instanceof ObjectDefinition) {
            return (ObjectDefinition) definition;
        }
        return null;
    }

    /**
     * Retrieves the group definition from the dictionary or null if not found
     * 
     * @param identifier
     *            of the definition
     * @return the group definition
     */
    public GroupDefinition getGroupDefinition(DictIdentifier identifier) {
        Definition definition = this.definitions.get(identifier);
        if (definition != null && definition instanceof GroupDefinition) {
            return (GroupDefinition) definition;
        }
        return null;
    }

    /**
     * Retrieves the element definition from the dictionary or null if not
     * found.
     * 
     * @param identifier
     *            of the definition
     * @return the element definition
     */
    public ElementDefinition getElementDefinition(DictIdentifier identifier) {
        return getElementDefinition(null, identifier);
    }

    public ElementDefinition getElementDefinition(String objectContext,
            DictIdentifier identifier) {
        Definition definition = null;

        // try with context first
        if (objectContext != null) {
            DictIdentifier id = DictIDFactory.createElementDefId(objectContext
                    + "." + identifier.toString());//$NON-NLS-1$
            definition = this.definitions.get(id);
            if (definition != null) {
                return (ElementDefinition) definition;
            }
        }

        definition = this.definitions.get(identifier);
        if (definition != null) {
            return (ElementDefinition) definition;
        }

        return null;
    }

    /**
     * Adds a definition to this dictionary.
     * 
     * @param definition
     *            to be added to the dictionary
     */
    public void addDefinition(Definition definition) {
        final DictIdentifier id = definition.getIdentifier();
        if (!this.definitions.containsKey(id)) {
            this.definitions.put(id, definition);
            for (Iterator<Alias> i = definition.getAliases().iterator(); i
                    .hasNext();) {
                Alias alias = i.next();
                final DictIdentifier aliasId = new DictIdentifier(alias,
                        definition.getClass());
                this.definitions.put(aliasId, definition);
            }
        } else {
            System.out.println(definition.getIdentifier() + " of type " //$NON-NLS-1$
                    + definition.getClass() + " definition already exists"); //$NON-NLS-1$
        }
    }

    /**
     * Sets the description information for a dictionary. This is often captured
     * informally in comments at the top of a dictionary file.
     * 
     * @param information
     */
    public void setInformation(String information) {
        this.information = information;
    }

    /**
     * Return the dictionary's descriptive information.
     * 
     * @return the information
     */
    public String getInformation() {
        return this.information;
    }

    /**
     * Adds a list of definitions to this dictionary. The flag indicates whether
     * the definitions should be overwritten.
     * 
     * @param defs
     *            to be added to the dictionary
     */
    public void addDefinitions(Collection<Definition> defs) {
        for (Iterator<Definition> i = defs.iterator(); i.hasNext();) {
            addDefinition(i.next());
        }
    }

    /**
     * Retrieves the class definition for an object with the given identifier.
     * This method will search the dictionary for an ObjectDefinition whose
     * identifier is the greatest length and matches the end of the given
     * identifier
     * 
     * @param identifier
     *            to lookup up class of
     * @return {@link ObjectDefinition} of class that will constrain object with
     *         given identifier. Returns null if not found.
     */
    public ObjectDefinition findObjectClassDefinition(DictIdentifier identifier) {
        ObjectDefinition definition = null;
        DictIdentifier curID = identifier;
        String className = identifier.toString();
        boolean done = false;

        while (definition == null && !done) {
            if (containsDefinition(curID)) {
                definition = (ObjectDefinition) this.definitions.get(curID);
            } else {
                if (className.indexOf("_") == -1 //$NON-NLS-1$
                        || className.indexOf("_") == className.length() - 1) { //$NON-NLS-1$
                    done = true;
                } else {
                    className = className.substring(className.indexOf("_") + 1); //$NON-NLS-1$
                    curID = DictIDFactory.createObjectDefId(className);
                }
            }
        }

        return definition;
    }

    /**
     * Retrieves the map of definitions
     * 
     * @return the map of definitions.
     */
    protected Map<DictIdentifier, Definition> getDefinitions() {
        return this.definitions;
    }

    /**
     * Retrieves the class definition for a group with the given identifier.
     * This method will search the dictionary for a GroupDefinition whose
     * identifier is the greatest length and matches the end of the given
     * identifier
     * 
     * @param identifier
     *            to lookup up class of
     * @return {@link GroupDefinition} of class that will constrain object with
     *         given identifier. Returns null if not found.
     */
    public GroupDefinition findGroupClassDefinition(DictIdentifier identifier) {
        GroupDefinition definition = null;
        String className = identifier.toString();
        boolean done = false;

        while (definition == null && !done) {
            if (containsGroupDefinition(identifier)) {
                definition = getGroupDefinition(identifier);
            } else {
                if (className.indexOf("_") == -1 || className.indexOf("_") == className.length() - 1) {//$NON-NLS-1$ //$NON-NLS-2$
                    done = true;
                } else {
                    className = className.substring(className.indexOf("_") + 1); //$NON-NLS-1$
                }
            }
        }

        return definition;
    }

    public List<SimpleDictionaryChange> getMergeChanges() {
        return this.mergeChanges;
    }

    public void addProblems(final List<LabelParserException> exceptions) {
        this.problems.addAll(exceptions);
    }

    public List<LabelParserException> getProblems() {
        return this.problems;
    }

    // use this for the master DD since it's not the user's responsibility to
    // correct errors there
    public void clearProblems() {
        this.problems.clear();
    }

    public Map<String, String> getUnits() {
        return this.units;
    }

    public void setUnits(Map<String, String> units) {
        this.units = units;
    }

    private void addMerge(final Definition definition, final String messageKey,
            final Object... arguments) {
        final SimpleDictionaryChange change = new SimpleDictionaryChange(
                definition, messageKey, arguments);
        this.mergeChanges.add(change);
    }
}
