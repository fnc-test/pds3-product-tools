//	Copyright (c) 2005, California Institute of Technology.
//	ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
//	 $Id: WildcardOSFilter.java 1429 2006-07-14 22:25:54Z mcayanan $ 
//

package gov.nasa.pds.tools.file.filefilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.FilenameUtils;

/**
 * Filters files using supplied wildcard(s). Based on the Apache
 * WildcardFilter class in the Commons IO package. Difference is
 * that in this class, it uses the 
 * org.apache.commons.io.FilenameUtils.wildcardMatchOnSystem()
 * for its matching rules, which means that pattern matching using
 * this class is OS dependent (case-insensitive on Windows and
 * case-sensitive on Unix, Linux, MAC)
 * 
 * @author mcayanan
 * @version $Revision: $
 * 
 */

public class WildcardOSFilter extends AbstractFileFilter {

	private List wildcards = null;
	
	/**
	 * Constructor for a single wildcard
	 * 
	 * @param wc
	 * @throws NullPointerException if the pattern is null
	 */
	public WildcardOSFilter(String wc) {
		if(wc == null) {
			throw new NullPointerException();
		}
		
		this.wildcards = new ArrayList();
		this.wildcards.add(wc); 
	}

	/**
	 * Constructor for a list of wildcards
	 * 
	 * @param wc
	 * @throws NullPointerException if the pattern list is null
	 */
	public WildcardOSFilter(List wc) {
		if(wc == null) {
			throw new NullPointerException();
		}
		
		this.wildcards = new ArrayList();
		this.wildcards.addAll(wc);
	}
	
	/**
	 * Checks to see if the filename matches one of the wildcards. Matching is 
	 * case-insensitive for Windows and case-sensitive for Unix.
	 * 
	 * @param file the file to check
	 * @return true if the filename matches one of the wildcards
	 * @throws NullPointerException if the file is null
	 */
	
	public boolean accept(File file) {
		
		if(file == null)
			throw new NullPointerException("No file specified");
		
		for(Iterator i = wildcards.iterator(); i.hasNext(); ) {
			if(FilenameUtils.wildcardMatchOnSystem(file.getName(), i.next().toString())) {
				return true;
			}
		}
		
		return false;
	}
	
	
	
}
