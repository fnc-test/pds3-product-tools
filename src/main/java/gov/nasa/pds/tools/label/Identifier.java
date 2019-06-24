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

package gov.nasa.pds.tools.label;

import java.io.Serializable;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class Identifier implements Serializable {

    private static final long serialVersionUID = 1L;

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
