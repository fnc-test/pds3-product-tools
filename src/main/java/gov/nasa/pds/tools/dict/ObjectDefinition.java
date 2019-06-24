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

import static gov.nasa.pds.tools.dict.DictionaryTokens.OBJECT_TYPE_GENERIC;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an object definition in the PDS data dictionary.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class ObjectDefinition extends ContainerDefinition {

    private static final long serialVersionUID = 5594367121498069617L;

    public ObjectDefinition(final Dictionary sourceDictionary,
            final int lineNumber, final DictIdentifier identifier) {
        super(sourceDictionary, lineNumber, identifier);
        this.objectType = OBJECT_TYPE_GENERIC;
    }

    public List<DictIdentifier> getRequiredObjects() {
        List<DictIdentifier> requiredObjects = new ArrayList<DictIdentifier>();
        for (DictIdentifier identifier : this.getRequired()) {
            if (identifier.getType().equals(ObjectDefinition.class)
                    || identifier.getType().equals(GroupDefinition.class)) {
                requiredObjects.add(identifier);
            }
        }
        return requiredObjects;
    }

    public boolean hasRequiredObjects() {
        return !this.getRequiredObjects().isEmpty();
    }

    public List<DictIdentifier> getOptionalObjects() {
        List<DictIdentifier> optionalObjects = new ArrayList<DictIdentifier>();
        for (DictIdentifier identifier : this.getOptional()) {
            if (identifier.getType().equals(ObjectDefinition.class)
                    || identifier.getType().equals(GroupDefinition.class)) {
                optionalObjects.add(identifier);
            }
        }
        return optionalObjects;
    }

    public boolean hasOptionalObjects() {
        return !this.getOptionalObjects().isEmpty();
    }
}
