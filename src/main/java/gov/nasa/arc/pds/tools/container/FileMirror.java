// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// • Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// • Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// • Neither the name of Caltech nor its operating division, the Jet Propulsion
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

package gov.nasa.arc.pds.tools.container;

import gov.nasa.arc.pds.tools.util.FileUtils;

import java.io.File;
import java.io.Serializable;

/**
 * A representation of a File object when the file is no longer accessible. For
 * example, an applet may collect information about files on a user's system.
 * Analysis of these files may be necessary once the results are sent back to a
 * server. In order to do so, certain file characteristics need to be burned in
 * (such as whether the file is a directory).
 * 
 * @author jagander
 * @version $Revision: $
 * 
 */
public class FileMirror implements Serializable, BaseContainerInterface {

	private static final long serialVersionUID = 6829391263115726395L;

	protected final String name;

	protected final Boolean isDirectory;

	protected final String relativePath;

	protected final String parent;

	protected final long length;

	public FileMirror(final File file, final File root) {
		this.name = file.getName();
		this.isDirectory = file.isDirectory();
		this.relativePath = FileUtils.getRelativePath(root, file);
		if (this.relativePath != "") { //$NON-NLS-1$
			this.parent = FileUtils.getRelativePath(root, file.getParentFile());
		} else {
			this.parent = null;
		}
		this.length = file.length();
	}

	public String getName() {
		return this.name;
	}

	public Boolean isDirectory() {
		return this.isDirectory;
	}

	public String getRelativePath() {
		return this.relativePath;
	}

	public String getParent() {
		return this.parent;
	}

	public long length() {
		return this.length;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		String className = ""; //$NON-NLS-1$
		if (obj != null) {
			className = obj.getClass().getName();
		}
		// TODO: figure out how to best test equality with subclasses
		if ((obj == null) || (!className.contains("FileMirror") && !className //$NON-NLS-1$
				.contains("FileNode"))) { //$NON-NLS-1$
			return false;
		}
		FileMirror node = (FileMirror) obj;
		return this.relativePath.equals(node.getRelativePath())
				&& this.isDirectory.equals(node.isDirectory());
	}
}
