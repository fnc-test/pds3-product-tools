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
