package gov.nasa.pds.tools.file;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class that can hold a list of files and directories. Used to store files
 * and directories found when traversing a directory.
 * 
 * @author mcayanan
 *
 */
public class FileList {
	
	private List files;
	private List dirs;
	
	public FileList() {
		files = new ArrayList();
		dirs = new ArrayList();
	}
	
	/**
	 * Adds a single object to the end of the file list
	 * @param o
	 */
	public void addToFileList(Object o) {
		files.add(o);
	}
	
	/**
	 * Adds a list of objects to the end of the file list
	 * @param c
	 */
	public void addToFileList(Collection c) {
		files.addAll(c);
	}
	
	/**
	 * Adds a single object to the end of the directory list
	 * @param o
	 */
	public void addToDirList(Object o) {
		dirs.add(o);
	}
	
	/**
	 * Adds a list of objects to the end of the directory list
	 * @param c
	 */
	public void addToDirList(Collection c) {
		dirs.addAll(c);
	}
	
	/**
	 * Gets files that were added to the list
	 * @return a list of files
	 */
	public List getFiles() {
		return files;
	}
	
	/**
	 * Gets directories that were added to the list
	 * @return a list of directories
	 */
	public List getDirs() {
		return dirs;
	}
}
