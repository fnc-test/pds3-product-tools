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
