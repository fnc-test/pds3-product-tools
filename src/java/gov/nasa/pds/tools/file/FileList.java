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
	
	public void addToFileList(Object o) {
		files.add(o);
	}
	
	public void addToFileList(Collection c) {
		files.addAll(c);
	}
	
	public void addToDirList(Object o) {
		dirs.add(o);
	}
	
	public void addToDirList(Collection c) {
		dirs.addAll(c);
	}
	
	public List getFiles() {
		return files;
	}
	
	public List getDirs() {
		return dirs;
	}
}
