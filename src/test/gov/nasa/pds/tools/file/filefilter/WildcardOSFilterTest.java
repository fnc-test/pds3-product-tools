package gov.nasa.pds.tools.file.filefilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class WildcardOSFilterTest extends TestCase {
	
	public WildcardOSFilterTest(String name) {
		super(name);
	}
	
	public void testSetters() {
		WildcardOSFilter filter = new WildcardOSFilter("TEST*");
		assertEquals("TEST*", filter.getWildcards().get(0));
		
		List patterns = new ArrayList();
		patterns.add("TEST*");
		patterns.add("TEST2*");
		WildcardOSFilter filter2 = new WildcardOSFilter(patterns);
		assertEquals(patterns, filter2.getWildcards());
	}
	
	public void testObjectMethods() {
		List pattern = new ArrayList();
		pattern.add("*TEST");
		WildcardOSFilter filter = new WildcardOSFilter(pattern);
		assertTrue(filter.accept(new File("GOODTEST")));
		assertTrue(filter.accept(new File("/HOME/DIR"), "GOODTEST"));
	}
}
