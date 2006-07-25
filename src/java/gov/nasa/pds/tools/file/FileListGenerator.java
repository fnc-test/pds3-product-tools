//	Copyright (c) 2005, California Institute of Technology.
//	ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
//	 $Id: FileListGenerator.java 1429 2006-07-14 22:25:54Z mcayanan $ 
//

package gov.nasa.pds.tools.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFilter;

/**
 * Class that can generate a list of files from a given directory and optionally, a specified
 * filter. Class allows recursive searching of files down a given directory or a local search
 * (grabbing only files located in a directory).
 * 
 * @author mcayanan
 * @version $Revision
 */
public class FileListGenerator {

	private IOFileFilter wildCardFilter;
	
	/**
	 * Constructor
	 *
	 */
	public FileListGenerator() {
		wildCardFilter = new WildcardFilter ("*");
	}
	
	/**
	 * Sets the filter to be used when searching for files in a directory
	 * @param wildcards - pattern(s) to match
	 */
	public void setFileFilter(List wildcards) {
		wildCardFilter = new WildcardFilter(wildcards);
	}
	
	/**
	 * Sets the filter to be used when searching for files in a directory.
	 * Difference with this method is that it accepts a file of patterns
	 * as input.
	 * @param file - A file containing a list of patterns
	 */
	public void setFileFilter(File file) {
		
		List filters = new ArrayList();
		String filter = null;
		
		try {
			BufferedReader reader = new BufferedReader (new FileReader(file));
		
			while( (filter = reader.readLine()) != null ) {
				filters.add(filter);
			}
		}
		catch( FileNotFoundException fex ) {
			System.out.println( fex.getMessage() );
			return;
		}
		catch( IOException iex ) {
			System.out.println( iex.getMessage() );
			return;
		}
		
		setFileFilter(filters);
		
	}
	
	/**
	 * Generates a list of files based on a List object of inputs given
	 * @param inputs - A list of files and/or directories 
	 * @param recurse - 'True' to recursively search down subdirectories for files to add onto the list.
	 *  'False' to just search at the level of the directory given.
	 * @return A List of files based on the inputs given
	 */
	public List visitFilesAndDirs(List inputs, boolean recurse) {
		List list = new ArrayList();
		File file = null;
		
		if(inputs == null) throw new NullPointerException("Parameter 'inputs' is null");
		
		for(Iterator i = inputs.iterator(); i.hasNext(); ) {
			file = new File( i.next().toString() );
			if(file.isDirectory()) {
				if(recurse)
					list.addAll( visitAllDirs(file) );
				else
					list.addAll( visitDir(file) );
			}
			else {
				list.add(file);
			}
		}
		return list;
	}
	
	/**
	 * Gets a list of files only at the level of the directory passed in. 
	 * @param dir - The name of the directory
	 * @return A list of all files that were found underneath the given directory (excluding files
	 * under subdirectories). In addition, if a filter was set via the setFileFilter method, then
	 * this list will be comprised of files that were 
	 * accepted by the filter. 
	 */
	public List visitDir(File dir) {
		
		List children = new ArrayList();
		
		try {
			if( !dir.isDirectory() )
				throw new Exception("parameter 'dir' is not a directory: " + dir);
		}
		catch( Exception ex ) {
			System.out.println( ex.getMessage() );
			return null;
		}
		
		children.addAll(FileUtils.listFiles(dir, wildCardFilter, null));
		return children;
	}
		
	/**
	 * Recursively gets a list of files under the specified directory and all of its subdirectories
	 * @param dir - The name of the directory to visit
	 * @return A List of all files that were found (including files under subdirectories) under the
	 * given directory
	 */
	public List visitAllDirs(File dir) {
		
		List children = new ArrayList();
		
		try {
			if( !dir.isDirectory() )
				throw new Exception("parameter 'dir' is not a directory: " + dir);
		}
		catch( Exception ex ) {
			System.out.println( ex.getMessage() );
			return null;
		}
		
		children.addAll(FileUtils.listFiles(dir, wildCardFilter, TrueFileFilter.INSTANCE)) ;
			
		return children;

	}
	
}
