//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.dict;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public abstract class Definition {
    private String identifier;
    private String statusType;
    private String description;
    private List aliases;
    
    public Definition(String identifier) {
        this.identifier = identifier;
        description = "";
        statusType = "";
        aliases = new ArrayList();
    }

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
        if (!aliases.contains(alias) && !identifier.equals(alias))
            aliases.add(alias);
    }

    /**
     * @param aliases The aliases to set.
     */
    public void setAliases(List aliases) {
        this.aliases = aliases;
    }
    
    public void addAliases(List aliases) {
        for (Iterator i = aliases.iterator(); i.hasNext();) {
            String alias = i.next().toString();
            if (!this.aliases.contains(alias) && !identifier.equals(alias))
                this.aliases.add(alias);
        }
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
