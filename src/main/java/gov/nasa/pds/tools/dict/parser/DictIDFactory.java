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

package gov.nasa.pds.tools.dict.parser;

import gov.nasa.pds.tools.dict.DictIdentifier;
import gov.nasa.pds.tools.dict.ElementDefinition;
import gov.nasa.pds.tools.dict.GroupDefinition;
import gov.nasa.pds.tools.dict.NullDefinition;
import gov.nasa.pds.tools.dict.ObjectDefinition;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class DictIDFactory {

    public static DictIdentifier createObjectDefId(final String identifier) {
        return new DictIdentifier(identifier, ObjectDefinition.class);
    }

    public static DictIdentifier createGroupDefId(final String identifier) {
        return new DictIdentifier(identifier, GroupDefinition.class);
    }

    // NOTE: ties to an attribute statement
    public static DictIdentifier createElementDefId(final String identifier) {
        return new DictIdentifier(identifier, ElementDefinition.class);
    }

    // not a dictionary id but provides context
    public static DictIdentifier createCommentDefId(final String identifier) {
        return new DictIdentifier(identifier, NullDefinition.class);
    }

    // not a dictionary id but provides context
    public static DictIdentifier createPointerDefId(final String identifier) {
        return new DictIdentifier(identifier, NullDefinition.class);
    }
}
