// Copyright 2006-2008, by the California Institute of 
// Technology. ALL RIGHTS RESERVED. United States Government 
// Sponsorship acknowledged. Any commercial use must be negotiated with 
// the Office of Technology Transfer at the California Institute of 
// Technology.
//
// This software may be subject to U.S. export control laws. By 
// accepting this software, the user agrees to comply with all 
// applicable U.S. export laws and regulations. User has the 
// responsibility to obtain export licenses, or other export authority 
// as may be required before exporting such information to foreign 
// countries or providing access to foreign persons.
//
// $Id$

package gov.nasa.pds.tools.dict;

import gov.nasa.pds.tools.label.Identifier;
import junit.framework.TestCase;

/**
 * Unit test case for {@link Alias}
 * @author pramirez
 * @version $Revision$
 * 
 */
public class AliasTest extends TestCase {
	private String identifier = "identifier";
	private String context = "context";
	
	public AliasTest(String name) {
		super(name);
	}
	
	public void testCtors() {
		Alias alias = new Alias(identifier);
		assertFalse(alias.hasContext());
		assertEquals(identifier, alias.getIdentifier().toString());
		
		Identifier ident = new Identifier(identifier);
		alias = new Alias(ident);
		assertFalse(alias.hasContext());
		assertEquals(ident, alias.getIdentifier());
		
		alias = new Alias(context, identifier);
		assertEquals(context, alias.getContext());
		assertEquals(identifier, alias.getIdentifier().toString());
		
		alias = new Alias(context, ident);
		assertEquals(context, alias.getContext());
		assertEquals(ident, alias.getIdentifier());
	}

}
