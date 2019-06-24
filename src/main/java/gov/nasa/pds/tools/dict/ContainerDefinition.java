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

import java.util.ArrayList;
import java.util.List;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class ContainerDefinition extends Definition {

    private static final long serialVersionUID = 5276472073799175326L;

    private final List<DictIdentifier> required = new ArrayList<DictIdentifier>();

    private final List<DictIdentifier> optional = new ArrayList<DictIdentifier>();

    public ContainerDefinition(Dictionary sourceDictionary, int lineNumber,
            DictIdentifier identifier) {
        super(sourceDictionary, lineNumber, identifier);
    }

    public boolean isRequired(final DictIdentifier identifier) {
        return this.required.contains(identifier);
    }

    public boolean isOptional(final DictIdentifier identifier) {
        return this.optional.contains(identifier);
    }

    public boolean isAllowed(final DictIdentifier identifier) {
        if (identifier.getType().equals(ElementDefinition.class)
                && this.optional.contains(WILDCARD_ELEMENT)) {
            return true;
        }
        return this.required.contains(identifier)
                | this.optional.contains(identifier);
    }

    public List<DictIdentifier> getRequired() {
        return this.required;
    }

    public List<DictIdentifier> getOptional() {
        return this.optional;
    }

    public void addOptional(final DictIdentifier identifier) {
        if (!this.optional.contains(identifier)) {
            this.optional.add(identifier);
        }
    }

    public void addOptional(final List<DictIdentifier> optionalList) {
        for (final DictIdentifier identifier : optionalList) {
            addOptional(identifier);
        }
    }

    public void addRequired(final DictIdentifier identifier) {
        if (!this.required.contains(identifier)) {
            this.required.add(identifier);
        }

    }

    public void addRequired(final List<DictIdentifier> requiredList) {
        for (final DictIdentifier identifier : requiredList) {
            addRequired(identifier);
        }
    }

    public List<DictIdentifier> getRequiredElements() {
        List<DictIdentifier> requiredElements = new ArrayList<DictIdentifier>();
        for (DictIdentifier identifier : this.required) {
            if (identifier.getType().equals(ElementDefinition.class)) {
                requiredElements.add(identifier);
            }
        }
        return requiredElements;
    }

    public boolean hasRequiredElements() {
        return !this.getRequiredElements().isEmpty();
    }

    public List<DictIdentifier> getOptionalElements() {
        List<DictIdentifier> optionalElements = new ArrayList<DictIdentifier>();
        for (DictIdentifier identifier : this.optional) {
            if (identifier.getType().equals(ElementDefinition.class)) {
                optionalElements.add(identifier);
            }
        }
        return optionalElements;
    }

    public boolean hasOptionalElements() {
        return !this.getOptionalElements().isEmpty();
    }

    public boolean allowsAnyElement() {
        return this.optional.contains(WILDCARD_ELEMENT);
    }
}
