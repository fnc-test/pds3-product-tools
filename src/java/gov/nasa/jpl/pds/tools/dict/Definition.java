//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools.dict;

import java.util.List;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public abstract class Definition {
    private String identifier;
    private String statusType;
    private String statusNote;
    private String description;
    private String sourceName;
    private List aliases;
    

    /**
     * The aliases for this definition
     * @return Returns the aliases.
     */
    public List getAliases() {
        return aliases;
    }
    
    /**
     * Add an alias for this Definition
     * @param alias The alias to add
     */
    public void addAlias(String alias) {
        aliases.add(alias);
    }

    /**
     * @param aliases The aliases to set.
     */
    public void setAliases(List aliases) {
        this.aliases = aliases;
    }

    /**
     * @return Returns the identifier.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Returns the sourceName.
     */
    public String getSourceName() {
        return sourceName;
    }

    /**
     * @param sourceName The sourceName to set.
     */
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    /**
     * @return Returns the statusNote.
     */
    public String getStatusNote() {
        return statusNote;
    }

    /**
     * @param statusNote The statusNote to set.
     */
    public void setStatusNote(String statusNote) {
        this.statusNote = statusNote;
    }

    /**
     * @return Returns the statusType.
     */
    public String getStatusType() {
        return statusType;
    }

    /**
     * @param statusType The statusType to set.
     */
    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    /**
     * @param identifier The identifier to set.
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

}
