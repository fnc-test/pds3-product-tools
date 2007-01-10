//	Copyright (c) 2005, California Institute of Technology.
//	ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
//	 $Id: FileListGenerator.java 1429 2006-07-14 22:25:54Z mcayanan $ 
//

package gov.nasa.pds.tools.file;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
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
	 * Constructor that takes in a list of supplied targets (files, URLs, directories)
	 * @param list a list of files, URLs, and directories
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
	 * Sets the filter to be used when searching for directories to ignore
	 * @param patterns a list of directory/directory patterns to ignore
	 */
	public void setNoDirFilter(List patterns) {
		noDirFilter = new NotFileFilter(new WildcardOSFilter(patterns));
	}
	
	/**
	 * Generates a list of files based on a supplied list of targets. Accepted
	 * inputs are files, URLs and directories.
	 * 
	 * @param recurse 'true' to recursively search down subdirectories for files to add onto the list.
	 *  'false' to just search at the level of the supplied directory.
	 * @return A list of files based on the supplied list of targets
	 */
	public List visitTargets(boolean recurse) {
		List files = new ArrayList();
		Object target = null;
		
		for(Iterator i = list.iterator(); i.hasNext(); ) {
			target = i.next();
			
			try {
				files.add(new URL(target.toString()));
			}
			catch(MalformedURLException e) {
				if(new File(target.toString()).isDirectory())
					files.addAll( visitDir(new File(target.toString()), recurse) );
				else 
					files.add(target);
			}
		}
		return files;
	}
	
	/**
	 * Gets a list of files under a given directory. If file and/or directory filters are wish to be
	 * used, then they must be set prior to calling this method.
	 * 
	 * @param dir the name of the directory
	 * @param recurse 'true' to recursively traverse down the directory, 'false' otherwise
	 * @return A list of all files that were found underneath the supplied directory.
	 */
	public List visitDir(File dir, boolean recurse) {
		List children = new ArrayList();
		List subdirs = new ArrayList();
		IOFileFilter realFileFilter;
		FileFilter realDirFilter;
		
		if( !dir.isDirectory() )
			throw new IllegalArgumentException("parameter 'dir' is not a directory: " + dir);
		
		// If we have specified filters for files to exclude, then we need to add this to the
		// filters
		if(noFileFilter != null)
			realFileFilter = FileFilterUtils.andFileFilter(fileFilter, noFileFilter);
		else
			realFileFilter = fileFilter;
		if(noDirFilter != null)
			realDirFilter = FileFilterUtils.andFileFilter(noDirFilter, FileFilterUtils.directoryFileFilter());
		else
			realDirFilter = FileFilterUtils.directoryFileFilter();
		
		//Find files only first
		children.addAll(FileUtils.listFiles(dir, realFileFilter, null));
		
		//Visit sub-directories if the recurse flag is set
		if(recurse) {
			//Grab all sub-directories first
			subdirs.addAll(Arrays.asList(dir.listFiles(realDirFilter)));
			//Visit each sub-directory
			for(Iterator i = subdirs.iterator(); i.hasNext();)
				children.addAll( visitDir( new File(i.next().toString()), recurse) );
		}
		
		return children;
	}
	
}
