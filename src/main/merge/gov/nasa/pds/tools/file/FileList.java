// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// * Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// * Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// * Neither the name of Caltech nor its operating division, the Jet Propulsion
// Laboratory, nor the names of its contributors may be used to endorse or
// promote products derived from this software without specific prior written
// permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

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
	 * @param o a single file to add
	 */
	public void addToFileList(Object o) {
		files.add(o);
	}
	
	/**
	 * Adds a list of objects to the end of the file list
	 * @param c a list of files to add
	 */
	public void addToFileList(Collection c) {
		files.addAll(c);
	}
	
	/**
	 * Adds a single object to the end of the directory list
	 * @param o a single directory to add
	 */
	public void addToDirList(Object o) {
		dirs.add(o);
	}
	
	/**
	 * Adds a list of objects to the end of the directory list
	 * @param c a list of directories to add
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
