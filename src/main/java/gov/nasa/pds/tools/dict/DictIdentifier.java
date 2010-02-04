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

import java.io.Serializable;

import org.apache.log4j.Logger;

public class DictIdentifier implements Serializable {
    private static final Logger log = Logger.getLogger(DictIdentifier.class
            .getName());

    private static final long serialVersionUID = 5818359213098660993L;

    private final String id;

    private final Class<? extends Definition> clazz;

    public DictIdentifier(final Alias alias,
            final Class<? extends Definition> clazz) {
        this(alias.toString(), clazz);
    }

    public DictIdentifier(final String id,
            final Class<? extends Definition> clazz) {
        this.id = id == null ? "" : id; //$NON-NLS-1$
        this.clazz = clazz;
    }

    public Class<? extends Definition> getType() {
        return this.clazz;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DictIdentifier)) {
            log.info(o.toString() + " is not an instance of DictIdentifier"); //$NON-NLS-1$
            return false;
        }
        DictIdentifier oId = (DictIdentifier) o;
        if (!this.clazz.equals(oId.getType())) {
            return false;
        } else if (!this.id.equals(oId.getId())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        String name = this.id + this.clazz.getSimpleName();
        return name.hashCode();
    }

    @Override
    public String toString() {
        return this.id;
    }

}
