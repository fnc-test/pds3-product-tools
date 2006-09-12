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

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.FileUtils;
import gov.nasa.pds.tools.file.filefilter.WildcardOSFilter;

/**
 * Class that can generate a list of files from a supplied directory and optionally, a specified
 * filter. Class allows recursive searching of files down a given directory or a local search
 * (grabbing only files located in a directory).
 * 
 * @author mcayanan
 * @version $Revision $
 */
public class FileListGenerator {

	private NotFileFilter noFileFilter;
	private IOFileFilter noDirFilter;
	private IOFileFilter fileFilter;
	private List list;
	
	/**
	 * Constructor that takes in a list of files and/or directories
	 * @param list a list of files and/or directories
	 *
	 */
	public FileListGenerator(List list) {
		fileFilter = new WildcardOSFilter("*");
		noFileFilter = null;
		noDirFilter = null;
		
		try {
			this.list = new ArrayList(list);
		}
		catch(NullPointerException ex) {
			System.out.println( "Parameter 'list' is null. Cannot initialize class FileListGenerator." );
			System.exit(1);
		}
	}
	
	/**
	 * Sets the filter to be used when searching for files in a directory
	 * @param wildcards a list of files and/or file patterns to match
	 */
	public void setFileFilter(List wildcards) {
		fileFilter = new WildcardOSFilter(wildcards);
	}
	
	/**
	 * Sets the filter to be used when searching for files to ignore in a directory
	 * @param wildcards a list of files and/or file patterns to ignore
	 */
	public void setNoFileFilter(List wildcards) {
		noFileFilter = new NotFileFilter(new WildcardOSFilter(wildcards));
	}
	
	/**
	 * Sets the filter to be used when searching for files to ignore in a directory.
	 * @param file a file containing a list of files and/or file patterns to ignore
	 */
	public void setNoFileFilter(File file) {
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
		setNoFileFilter(filters);
	}
	
	/**
	 * Sets the filter to be used when searching for directories to ignore
	 * @param wildcards a list of directory and/or directory patterns to ignore
	 */
	public void setNoDirFilter(List wildcards) {
		noDirFilter = new NotFileFilter(new WildcardOSFilter(wildcards));
	}
	
	/**
	 * Sets the filter to be used when searching for directories to ignore
	 * @param file a file containing a list of directories and/or directory patterns to ignore
	 */
	public void setNoDirFilter(File file) {
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
		setNoDirFilter(filters);
	}
	
	/**
	 * Generates a list of files based on a supplied list of inputs. Accepted
	 * inputs are files and directories.
	 * 
	 * @param recurse 'true' to recursively search down subdirectories for files to add onto the list.
	 *  'false' to just search at the level of the supplied directory.
	 * @return A list of files based on the supplied inputs
	 */
	public List visitFilesAndDirs(boolean recurse) {
		List files = new ArrayList();
		File file = null;
		
		for(Iterator i = list.iterator(); i.hasNext(); ) {
			file = new File( i.next().toString() );
			if(file.isDirectory())
				files.addAll( visitDirs(file, recurse) );
			else 
				files.add(file);
		}
		return files;
	}
	
	/**
	 * Gets a list of files under a given directory.
	 * 
	 * @param dir the name of the directory
	 * @param recurse 'true' to recursively traverse down the directory, 'false' otherwise
	 * @return A list of all files that were found underneath the given directory. If a
	 * filter was set via the setFileFilter and/or setNoFileFilter methods, then this list
	 * will be comprised of files that matched.
	 */
	public List visitDirs(File dir, boolean recurse) {
		List children = new ArrayList();
		IOFileFilter realFileFilter;
		
		if( !dir.isDirectory() )
			throw new IllegalArgumentException("parameter 'dir' is not a directory: " + dir);
		
		// If we have specified filters for files to exclude, then we need to add this to the
		// filters
		if(noFileFilter != null)
			realFileFilter = FileFilterUtils.andFileFilter(fileFilter, noFileFilter);
		else
			realFileFilter = fileFilter;
		
		if(recurse)
			children.addAll(FileUtils.listFiles(dir, realFileFilter, TrueFileFilter.INSTANCE));
		else
			children.addAll(FileUtils.listFiles(dir, realFileFilter, null));
		
		return children;
	}
	
}
