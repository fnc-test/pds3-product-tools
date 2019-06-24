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

package gov.nasa.pds.tools.flags;

/**
 * Class containing command-line option flags that can be used for
 * the PDS Tools.
 * 
 * @author mcayanan
 *
 */
public interface ToolsFlags {
	public final static int SHORT = 0;
	public final static int LONG = 1;
	public final static int ARGNAME = 2;
	
	public final static String CONFIG[] = {"c", "config", "file"};
	public final static String DICT[] = {"d", "dict",".full file(s)"};
	public final static String HELP[] = {"h", "help"};
	public final static String REPORT[] = {"r", "report-file", "file"};
	public final static String VERSION[] = {"V", "version"};
	
	public final static String WHATIS_CONFIG = "Specify a configuration file"
			+ " to set the default values.";
	
	public final static String WHATIS_DICT = "Specify PDS-compliant dictionary"
			+ " file(s)";
	
	public final static String WHATIS_HELP = "Display usage.";	
	public final static String WHATIS_REPORT = "Specify report file name.";
	public final static String WHATIS_VERSION = "Display application version.";

}
