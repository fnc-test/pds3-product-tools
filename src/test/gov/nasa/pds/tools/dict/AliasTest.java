// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// * Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// * Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// * Neither the name of Caltech nor its operating division, the Jet Propulsion
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
import junit.framework.TestCase;

/**
 * Unit test case for {@link Alias}
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class AliasTest extends TestCase {
    private String identifier = "identifier"; //$NON-NLS-1$
    private String context = "context"; //$NON-NLS-1$
    private String odd = "odd"; //$NON-NLS-1$
    private String even = "even"; //$NON-NLS-1$

    public AliasTest(String name) {
        super(name);
    }

    public void testCtors() {
        Alias alias = new Alias(this.identifier);
        assertFalse(alias.hasContext());
        assertEquals(this.identifier, alias.getIdentifier().toString());

        Identifier ident = new Identifier(this.identifier);
        alias = new Alias(ident);
        assertFalse(alias.hasContext());
        assertEquals(ident, alias.getIdentifier());

        alias = new Alias(this.context, this.identifier);
        assertEquals(this.context, alias.getContext());
        assertEquals(this.identifier, alias.getIdentifier().toString());
        assertTrue(alias.hasContext());

        alias = new Alias(this.context, ident);
        assertEquals(this.context, alias.getContext());
        assertEquals(ident, alias.getIdentifier());
    }

    public void testObjMethods() {
        Alias alias = new Alias(this.identifier);

        assertEquals(this.identifier, alias.toString());

        alias = new Alias(this.context, this.identifier);
        assertEquals(this.context + "." + this.identifier, alias.toString()); //$NON-NLS-1$

        assertFalse(alias.equals(this.odd));
        Alias oddAlias = new Alias(this.context, this.odd);
        Alias evenAlias = new Alias(this.context, this.even);
        assertFalse(oddAlias.equals(evenAlias));
        oddAlias = new Alias(this.odd, this.identifier);
        evenAlias = new Alias(this.even, this.identifier);
        assertFalse(oddAlias.equals(evenAlias));
        Alias sameAlias = new Alias(this.context, this.identifier);
        assertTrue(alias.equals(sameAlias));

        assertEquals(new String(this.context + "." + this.identifier) //$NON-NLS-1$
                .hashCode(), alias.hashCode());
    }

}
