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
    protected String objectType;
    
    public Definition(String identifier) {
        this.identifier = identifier;
        description = "";
        statusType = "PROPOSED";
        objectType = null;
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
    public void addAlias(Alias alias) {
        if (!aliases.contains(alias) && !identifier.equals(alias.getIdentifier()))
            aliases.add(alias);
    }

    /**
     * @param aliases The aliases to set.
     */
    public void setAliases(List aliases) {
        this.aliases = aliases;
    }
    
    public void addAliases(List aliases) {
        this.aliases.addAll(aliases);
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
    
    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }
    
    public String getObjectType() {
        return objectType;
    }

}
