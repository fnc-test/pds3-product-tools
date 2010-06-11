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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public abstract class Definition implements Serializable {

    private static final long serialVersionUID = 1L;

    public final static String WILDCARD = "PSDD"; //$NON-NLS-1$

    public final static DictIdentifier WILDCARD_ELEMENT = DictIDFactory
            .createElementDefId(WILDCARD);

    protected int lineNumber;

    private DictIdentifier identifier;

    private String statusType; // TODO: Convert this to an enum

    private String description;

    private List<Alias> aliases = new ArrayList<Alias>();

    protected String objectType;

    private transient Dictionary sourceDictionary;

    /*
     * public Definition(final int lineNumber, DictIdentifier identifier) {
     * this(null, lineNumber, identifier); }
     */

    public Definition(final Dictionary sourceDictionary, final int lineNumber,
            DictIdentifier identifier) {
        this.sourceDictionary = sourceDictionary;
        this.lineNumber = lineNumber;
        this.identifier = identifier;
    }

    public Dictionary getSourceDictionary() {
        return this.sourceDictionary;
    }

    /**
     * The aliases for this definition
     * 
     * @return Returns the aliases.
     */
    public List<Alias> getAliases() {
        return this.aliases;
    }

    /**
     * Add an alias for this Definition
     * 
     * @param alias
     *            The alias to add
     */
    public void addAlias(final Alias alias) {
        if (!this.aliases.contains(alias)
                && !this.identifier.toString().equals(
                        alias.getIdentifier().toString()))
            this.aliases.add(alias);
    }

    // convenience method since dictionary already tests for dupes in merge
    public void addAliasSimple(final Alias alias) {
        this.aliases.add(alias);
    }

    /**
     * @param aliases
     *            The aliases to set.
     */
    public void setAliases(List<Alias> aliases) {
        this.aliases = aliases;
    }

    public void addAliases(List<Alias> newAliases) {
        for (Alias alias : newAliases) {
            if (!this.aliases.contains(alias))
                this.aliases.add(alias);
        }
    }

    public boolean hasAliases() {
        return this.aliases.size() > 0;
    }

    /**
     * @return Returns the identifier.
     */
    public DictIdentifier getIdentifier() {
        return this.identifier;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        if (this.description == null) {
            return ""; //$NON-NLS-1$
        }
        return this.description;
    }

    public boolean hasDescription() {
        return this.description != null;
    }

    /**
     * @param description
     *            The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Returns the statusType.
     */
    public String getStatusType() {
        if (this.statusType == null) {
            return ""; //$NON-NLS-1$
        }
        return this.statusType;
    }

    public boolean hasStatusType() {
        return this.statusType != null;
    }

    /**
     * @param statusType
     *            The statusType to set.
     */
    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    /**
     * @param identifier
     *            The identifier to set.
     */
    public void setIdentifier(DictIdentifier identifier) {
        this.identifier = identifier;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectType() {
        return this.objectType;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return this.identifier.toString() + " : " + this.description;
    }
}
