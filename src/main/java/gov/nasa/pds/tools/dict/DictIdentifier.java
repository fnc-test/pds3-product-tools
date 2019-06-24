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
