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

package gov.nasa.pds.tools.label;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class Identifier {

    private String namespace;

    private String localName;

    public Identifier(String namespace, String localName) {
        this.namespace = namespace;
        this.localName = localName;
    }

    public Identifier(String identifier) {
        this.namespace = ""; //$NON-NLS-1$
        if (identifier.indexOf(":") != -1) { //$NON-NLS-1$
            this.namespace = identifier.substring(0, identifier.indexOf(":")); //$NON-NLS-1$
        }

        if (identifier.indexOf(":") == -1) { //$NON-NLS-1$
            this.localName = identifier;
        } else {
            this.localName = identifier.substring(identifier.indexOf(":") + 1); //$NON-NLS-1$
        }
    }

    @Override
    public String toString() {
        if ("".equals(this.namespace)) { //$NON-NLS-1$
            return this.localName;
        }
        return this.namespace + ":" + this.localName; //$NON-NLS-1$
    }

    public String getLocalName() {
        return this.localName;
    }

    public String getNamespace() {
        return this.namespace;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Identifier)) {
            return false;
        }
        Identifier oId = (Identifier) o;
        if (!this.namespace.equals(oId.getNamespace())) {
            return false;
        } else if (!this.localName.equals(oId.getLocalName())) {
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
