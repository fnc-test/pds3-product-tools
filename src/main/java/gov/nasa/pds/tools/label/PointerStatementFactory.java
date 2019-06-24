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

import gov.nasa.pds.tools.dict.DictIdentifier;
import gov.nasa.pds.tools.dict.parser.DictIDFactory;

/**
 * This class hides the construction of pointers. It helps determine the exact
 * type of pointer and constructs it. If modifications need to be made to this
 * behavior it will be hidden here.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class PointerStatementFactory {

    public static PointerStatement newInstance(final Label sourceLabel,
            final int line, final String identifier, final Value value) {
        DictIdentifier pointerId = DictIDFactory.createPointerDefId(identifier);
        if (matchesIdentifier(pointerId, SpecialPointer.DESCRIPTION_KEY)) {
            return new DescriptionPointer(sourceLabel, line, pointerId, value);
        } else if (matchesIdentifier(pointerId, SpecialPointer.STRUCTURE_KEY)) {
            return new StructurePointer(sourceLabel, line, pointerId, value);
        } else if (matchesIdentifier(pointerId, SpecialPointer.INDEX_KEY)) {
            return new IndexPointer(sourceLabel, line, pointerId, value);
        } else if (matchesIdentifier(pointerId, SpecialPointer.CATALOG_KEY)) {
            return new CatalogPointer(sourceLabel, line, pointerId, value);
        }
        return new PointerStatement(sourceLabel, line, pointerId, value);
    }

    @SuppressWarnings("nls")
    // comparing to strings since statements are always AttributeStatements at
    // this stage
    private static boolean matchesIdentifier(final DictIdentifier id,
            final String[] testIds) {
        for (final String testId : testIds) {
            final String idString = id.toString();
            if (idString.equals(testId) || idString.endsWith("_" + testId)) {
                return true;
            }
        }
        return false;
    }
}
