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

import gov.nasa.pds.tools.label.Identifier;

import java.io.Serializable;

/**
 * This class represents an Alias to a Definition. The alias is valid within the
 * context of some object or group.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class Alias implements Serializable {

    private static final long serialVersionUID = 1L;

    private String context;

    private Identifier identifier;

    public Alias(String identifier) {
        this("", new Identifier(identifier)); //$NON-NLS-1$
    }

    public Alias(Identifier identifier) {
        this("", identifier); //$NON-NLS-1$
    }

    public Alias(String context, String identifier) {
        this(context, new Identifier(identifier));
    }

    public Alias(String context, Identifier identifier) {
        this.context = context;
        this.identifier = identifier;
    }

    public boolean hasContext() {
        return ("".equals(this.context)) ? false : true; //$NON-NLS-1$
    }

    public String getContext() {
        return this.context;
    }

    public Identifier getIdentifier() {
        return this.identifier;
    }

    @Override
    public String toString() {
        if (!"".equals(this.context)) { //$NON-NLS-1$
            return this.context + "." + this.identifier.toString(); //$NON-NLS-1$
        }
        return this.identifier.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Alias)) {
            return false;
        }
        Alias oId = (Alias) o;
        if (!this.identifier.equals(oId.getIdentifier())) {
            return false;
        } else if (!this.context.equals(oId.getContext())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        String name = this.toString();
        return name.hashCode();
    }
}
