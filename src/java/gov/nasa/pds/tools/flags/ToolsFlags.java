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

package gov.nasa.pds.tools.flags;

/**
 * Class containing command-line option flags
 * 
 * @author mcayanan
 *
 */
public interface ToolsFlags {
	public final int SHORT = 0;
	public final int LONG = 1;
	public final int ARGNAME = 2;
	
	public final String ALIAS[] = {"a", "alias"};
	public final String CONFIG[] = {"c", "config", "file"};
	/*public final String DATAOBJ[] = {"O", "no-obj", null};*/
	public final String DICT[] = {"d", "dict", ".full files"};
	public final String FOLLOW[] = {"F", "no-follow"};
	public final String HELP[] = {"h", "help"};
	public final String IGNOREDIR[] = {"D", "ignore-dir", "patterns"};
	public final String IGNOREFILE[] = {"X", "ignore-file", "patterns"};
	public final String INCLUDES[] = {"I", "include", "paths"};
	public final String LOCAL[] = {"L", "local"};	
	public final String LOG[] = {"l", "log-file", "file (optional)"};
	public final String PARTIAL[] = {"f", "force"};
	public final String PROGRESS[] = {"p", "progress"};
	public final String REGEXP[] = {"e", "regexp", "patterns"};
	public final String REPORT[] = {"r", "report-file", "file"};
	public final String RPTSTYLE[] = {"s", "report-style", "full|sum|min"};
	public final String TARGET[] = {"t", "target", "labels,URLs,dirs"};
	public final String VERBOSE[] = {"v", "verbose", "1|2|3"};
	public final String VERSION[] = {"V", "version"};
}
