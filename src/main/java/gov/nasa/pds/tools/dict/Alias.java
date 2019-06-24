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
