// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// • Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// • Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// • Neither the name of Caltech nor its operating division, the Jet Propulsion
// Laboratory, nor the names of its contributors may be used to endorse or
// promote products derived from this software without specific prior written
// permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

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

    private final String sourceString;

    /*
     * public Definition(final int lineNumber, DictIdentifier identifier) {
     * this(null, lineNumber, identifier); }
     */

    public Definition(final Dictionary sourceDictionary, final int lineNumber,
            DictIdentifier identifier) {
        this.sourceDictionary = sourceDictionary;
        this.sourceString = sourceDictionary.getSourceString();
        this.lineNumber = lineNumber;
        this.identifier = identifier;
    }

    public Dictionary getSourceDictionary() {
        return this.sourceDictionary;
    }

    public String getSourceString() {
        return this.sourceString;
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
