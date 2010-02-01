//Copyright 2006-2007, by the California Institute of Technology.
//ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
//Any commercial use must be negotiated with the Office of Technology Transfer
//at the California Institute of Technology.
//
//This software is subject to U. S. export control laws and regulations
//(22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
//is subject to U.S. export control laws and regulations, the recipient has
//the responsibility to obtain export licenses or other export authority as
//may be required before exporting such information to foreign countries or
//providing access to foreign nationals.
//
//	 $Id$ 
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
 * @version $Revision$
 * 
 */

public class WildcardOSFilter extends AbstractFileFilter {

	private List wildcards = null;
	
	/**
	 * Constructor for a single wildcard
	 * 
	 * @param wc a single filter to set
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
	 * Returns list of filters that were set
	 * @return a list of filters
	 */
	public List getWildcards() {
		return wildcards;
	}
	
	/**
	 * Constructor for a list of wildcards
	 * 
	 * @param wc a list of filters to set
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
	
	/**
	 * Checks to see if the filename matches one of the wildcards. Matching is 
	 * case-insensitive for Windows and case-sensitive for Unix.
	 * 
	 * @param dir the directory to check
	 * @param name the file name within the directory to check
	 * @return true if the filename matches one of the wildcards, false otherwise
	 * @throws NullPointerException if the file is null
	 */
	public boolean accept(File dir, String name) {
		return accept(new File(dir, name));
	}
	
}