//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$
//

package gov.nasa.pds.tools;

import gov.nasa.pds.tools.config.VToolConfigKeys;
import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.dict.parser.DictionaryParser;
import gov.nasa.pds.tools.file.FileList;
import gov.nasa.pds.tools.file.FileListGenerator;
import gov.nasa.pds.tools.flags.ToolsFlags;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.parser.LabelParser;
import gov.nasa.pds.tools.label.parser.LabelParserFactory;
import gov.nasa.pds.tools.label.validate.Status;
import gov.nasa.pds.tools.logging.ToolsLevel;
import gov.nasa.pds.tools.logging.ToolsLogFormatter;
import gov.nasa.pds.tools.logging.ToolsLogRecord;
import gov.nasa.pds.tools.status.ExitStatus;
import gov.nasa.pds.tools.time.ToolsTime;
import gov.nasa.pds.tools.xsl.StyleSheet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import javax.swing.text.BadLocationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FilenameUtils;

/**
 * Class to replace LVTool functionality
 *  
 * @author mcayanan
 * @version $Revision$
 *
 *
 */
public class VTool implements VToolConfigKeys, ToolsFlags, ExitStatus, 
                              Status, StyleSheet {
	private final String versionID = "0.4.0";
	private final String fileRep = "*";
	private final String partialExt = "FMT";
	private final String timeFormat = "EEE, MMM dd yyyy 'at' HH:mm:ss a";
	
	private static Logger log = Logger.getLogger(VTool.class.getName());
	private Handler logHandler;
	private String currDir;
	private boolean dictPassed;
	
	private Options options;
	private CommandLineParser parser;
	private CommandLine cmd;
	
	//TODO: Flags to be implemented: data object(-O,--no-obj),
	
	private boolean alias;
	private URL config;
	private boolean dataObj;
	private List dictionaries;
	private List noFiles;
	private List noDirs;
	private boolean followPtrs;
	private List includePaths;
	private boolean force;
	private boolean progress;
	private List regexp;
	private boolean recursive;
	private List targets;
	private File logFile;
	private boolean showLog;
	private File rptFile;
	private String rptStyle;
	private short verbose;
	private String severity;
	
	private int goodLbls;
	private int badLbls;
	private int unkLbls;
	
	/** 
	 * Default constructor
	 */
	public VTool() {
		alias = false;
		config = null;
		dictionaries = null;
		dataObj = true;
		dictPassed = true;
		noFiles = null;
		noDirs = null;
		followPtrs = true;
		targets = null;
		includePaths = null;
		force = false;
		progress = false;
		regexp = null;
		recursive = true;
		rptFile = null;
		rptStyle = "full";
		severity = null;
		logFile = null;
		showLog = false;
		
		currDir = null;
		logHandler = null;
		options = new Options();
		parser = new GnuParser();
		
		goodLbls = 0;
		badLbls = 0;
		unkLbls = 0;
	}
	
	/**
	 * Show the version and disclaimer notice for VTool 
	 *
	 */	
	public void showVersion() {
		System.out.println("PDS Validation Tool (VTool) " + versionID);
		System.out.println("\nDISCLAIMER:\n"
				           + "THIS SOFTWARE AND ANY RELATED MATERIALS WERE CREATED BY THE CALIFORNIA\n"
				           + "INSTITUTE OF TECHNOLOGY (CALTECH) UNDER A U.S. GOVERNMENT CONTRACT WITH THE\n"
				           + "NATIONAL AERONAUTICS AND SPACE ADMINISTRATION (NASA). THE SOFTWARE IS\n"
				           + "TECHNOLOGY AND SOFTWARE PUBLICLY AVAILABLE UNDER U.S. EXPORT LAWS AND IS\n"
				           + "PROVIDED \"AS-IS\" TO THE RECIPIENT WITHOUT WARRANTY OF ANY KIND, INCLUDING\n"
				           + "ANY WARRANTIES OF PERFORMANCE OR MERCHANTABILITY OR FITNESS FOR A PARTICULAR\n"
				           + "USE OR PURPOSE (AS SET FORTH IN UNITED STATES UCC2312-2313) OR FOR ANY\n"
				           + "PURPOSE WHATSOEVER, FOR THE SOFTWARE AND RELATED MATERIALS, HOWEVER USED. IN\n"
				           + "NO EVENT SHALL CALTECH, ITS JET PROPULSION LABORATORY, OR NASA BE LIABLE FOR\n"
				           + "ANY DAMAGES AND/OR COSTS, INCLUDING, BUT NOT LIMITED TO, INCIDENTAL OR\n"
				           + "CONSEQUENTIAL DAMAGES OF ANY KIND, INCLUDING ECONOMIC DAMAGE OR INJURY TO\n"
				           + "PROPERTY AND LOST PROFITS, REGARDLESS OF WHETHER CALTECH, JPL, OR NASA BE\n"
				           + "ADVISED, HAVE REASON TO KNOW, OR, IN FACT, SHALL KNOW OF THE POSSIBILITY.\n"
				           + "RECIPIENT BEARS ALL RISK RELATING TO QUALITY AND PERFORMANCE OF THE SOFTWARE\n"
				           + "AND ANY RELATED MATERIALS, AND AGREES TO INDEMNIFY CALTECH AND NASA FOR ALL\n"
				           + "THIRD-PARTY CLAIMS RESULTING FROM THE ACTIONS OF RECIPIENT IN THE USE OF THE\n"
				           + "SOFTWARE.");
		System.exit(GOODRUN);
	}
	
	/**
	 * Display VTool usage and help information
	 *
	 */
	public void showHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(37, "VTool", null, options, null);
		System.exit(GOODRUN);
	}
	
	/**
	 * Builds the set of configurable parameters for VTool
	 */
	public void buildOpts() {
		options.addOption(ALIAS[SHORT], ALIAS[LONG], false, "Enable aliasing");
		options.addOption(FOLLOW[SHORT], FOLLOW[LONG], false, 
				"Do not follow or check for the existence of files referenced "
				+ "by pointer statements in a label");
		options.addOption(HELP[SHORT], HELP[LONG], false, "Display usage");
//		options.addOption("O", "no-obj", false, "Do not perform data object validation");
		options.addOption(PARTIAL[SHORT], PARTIAL[LONG], false, 
				"Force VTool to validate a label fragment");
		options.addOption(LOCAL[SHORT], LOCAL[LONG], false, 
				"Validate files only in the target directory rather than "
				+ "recursively traversing down the subdirectories.");
		options.addOption(PROGRESS[SHORT], PROGRESS[LONG], false, 
				"Enable progress reporting");
		options.addOption(VERSION[SHORT], VERSION[LONG], false, 
				"Display VTool version");
		
		
		// These are options that require an argument

		// Option to specify a configuration file
		OptionBuilder.withLongOpt(CONFIG[LONG]);
		OptionBuilder.withDescription(
		  "Specify a configuration file to set the default values for VTool");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("file");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create(CONFIG[SHORT]));
		
		// Option to specify the PSDD and any local dictionaries
		OptionBuilder.withLongOpt(DICT[LONG]);
		OptionBuilder.withDescription(
		  "Specify the Planetary Science Data Dictionary full file name/URL "
		  + "and any local dictionaries to include for validation. Separate "
		  + "each file name with a space");
		OptionBuilder.hasArgs();
		OptionBuilder.withArgName(".full files");
		OptionBuilder.withValueSeparator(' ');
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create(DICT[SHORT]));
		
		// Option to specify a file containing a list of file extensions to ignore
		OptionBuilder.withLongOpt(IGNOREFILE[LONG]);
		OptionBuilder.withDescription(
		  "Specify file patterns to ignore from validation. Separate each "
		  + "with a space. Patterns should be surrounded by quotes "
		  + "(i.e. -X \"*TAB\" \"*IMG\" or -X \"*TAB *IMG\")");
		OptionBuilder.hasArgs();
		OptionBuilder.withArgName("patterns");
		OptionBuilder.withValueSeparator(' ');
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create(IGNOREFILE[SHORT]));
		
		// Option to specify the label(s) to validate

		OptionBuilder.withLongOpt(TARGET[LONG]);
		OptionBuilder.withDescription(
		  "Explicitly specify the targets (label files, URLs and directories) "
		  + "to validate. Targets can be specified implicitly as well. "
		  + "(example: VTool label.lbl)");
		OptionBuilder.hasArgs();
		OptionBuilder.withArgName("labels,URLs,dirs");
		OptionBuilder.withType(String.class);
		OptionBuilder.withValueSeparator(' ');
		options.addOption(OptionBuilder.create(TARGET[SHORT]));
		
		// Option to specify a pattern to match against the input directory to be validated
		OptionBuilder.withLongOpt(REGEXP[LONG]);
		OptionBuilder.withDescription(
		  "Specify file patterns to look for when validating a directory. "
		  + "Separate each with a space. Patterns should be surrounded by "
		  + "quotes (i.e. -e \"*.LBL\" \"*.FMT\" or -e \"*.LBL *.FMT\")");
		OptionBuilder.hasArgs();
		OptionBuilder.withArgName("patterns");
		OptionBuilder.withValueSeparator(' ');
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create(REGEXP[SHORT]));
		
		// Option to specify a path to the Pointer files		
		OptionBuilder.withLongOpt(INCLUDES[LONG]);
		OptionBuilder.withDescription(
		  "Specify the paths to look for files referenced by pointers in a "
		  + "label. Separate each with a space. Default is to always look "
		  + "at the same directory as the label");
		OptionBuilder.hasArgs();
		OptionBuilder.withArgName("paths");
		OptionBuilder.withType(String.class);
		OptionBuilder.withValueSeparator(' ');
		options.addOption(OptionBuilder.create(INCLUDES[SHORT]));

		//Option to specify a file containing a list of directories to ignore
		OptionBuilder.withLongOpt(IGNOREDIR[LONG]);
		OptionBuilder.withDescription(
		  "Specify directory patterns to ignore. Separate each with a space. "
		  + "Patterns should be surrounded by quotes "
		  + "(i.e. -D \"EXTRAS\" \"LABEL\" or -D \"EXTRAS LABEL\")");
		OptionBuilder.hasArgs();
		OptionBuilder.withArgName("patterns");
		OptionBuilder.withValueSeparator(' ');
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create(IGNOREDIR[SHORT]));
		
		// Option to specify the log file name
		OptionBuilder.withLongOpt(LOG[LONG]);
		OptionBuilder.withDescription(
		  "Specify the file name for the machine-readable log. A file "
		  + "specification is optional. If no file name is given, then "
		  + "the log will be written to standard out");
		OptionBuilder.hasOptionalArg();
		OptionBuilder.withArgName("file (optional)");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create(LOG[SHORT]));

		
		//Option to specify the report file name
		OptionBuilder.withLongOpt(REPORT[LONG]);
		OptionBuilder.withDescription(
		  "Specify the file name for the human-readable report. Default is "
		  + "to write to standard out if this flag is not specified. This "
		  + "report, however, will not print to standard out if this flag is "
		  + "missing AND the log file flag is specified with no file name");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("file");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create(REPORT[SHORT]));
		
		// Option to specify how detail the reporting should be
		OptionBuilder.withLongOpt(RPTSTYLE[LONG]);
		OptionBuilder.withDescription(
		  "Specify the level of detail for the reporting. Valid values are "
		  + "'full' for a full view, 'min' for a minimal view and 'sum' for "
		  + "a summary view. Default is to see a full report if this flag is "
		  + "not specified");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("full|sum|min");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create(RPTSTYLE[SHORT]));
		
		// Option to specify the severity level and above
		OptionBuilder.withLongOpt(VERBOSE[LONG]);
		OptionBuilder.withDescription(
		  "Specify the severity level and above to include in the "
		  + "human-readable report: (1=Info, 2=Warning, 3=Error). "
		  + "Default is Warnings and above (level 2)");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("1|2|3");
		OptionBuilder.withType(short.class);
		options.addOption(OptionBuilder.create(VERBOSE[SHORT]));
		
	}
	
	/**
	 * Parses the VTool command-line
	 * @param argv arguments given on the command-line
	 */
	public void parseLine(String[] argv) {
		try {
			cmd = parser.parse(options, argv);
		}
		catch( ParseException exp ) {
			System.err.println(
					"Command line parser failed.\n\nReason: "
					+ exp.getMessage() );
			System.exit(TOOLFAILURE);
		}
	}
	
	/** 
	 * Queries the VTool command-line 
	 *
	 */
	public void queryCmdLine() {
		List targetList = new ArrayList();
		
		try {
			// Check if the help flag was set
			if (cmd.hasOption(HELP[SHORT]))
				showHelp();
			// Check if the flag to display the version number and disclaimer
			// notice was set
			if (cmd.hasOption(VERSION[SHORT])) 
				showVersion();
			// Check if a configuration file was specified
			if (cmd.hasOption(CONFIG[SHORT])) {
				URL file = toURL(cmd.getOptionValue(CONFIG[SHORT]));
				readConfigFile(file);
			}
			// Check for the 'l' flag to specify a file where the log will be
			// written to
			if (cmd.hasOption(LOG[SHORT])) {
				if (cmd.getOptionValue(LOG[SHORT]) != null)
					setLogFile(new File(cmd.getOptionValue(LOG[SHORT])));
				else
					setShowLog(true);
			}
			// verbose flag
			if (cmd.hasOption(VERBOSE[SHORT])) {
				try {
					setVerbose(Short.parseShort(cmd.getOptionValue(VERBOSE[SHORT])));
				}
				catch( NumberFormatException ne) {
					throw new Exception("Problems parsing value for the 'v' flag: " 
							+ cmd.getOptionValue(VERBOSE[SHORT]));
				}
				catch(IllegalArgumentException ae) {
					throw new Exception(ae.getMessage());
				}
		
			}
			
			//Grab the targets specified implicitly
			if (cmd.getArgList().size() != 0)
				targetList.addAll(cmd.getArgList());
			//Grab the targets specified explicitly
			if (cmd.hasOption(TARGET[SHORT])) {
				targetList.addAll(
						Arrays.asList(cmd.getOptionValues(TARGET[SHORT])));
			}
			setTargets(targetList);
			
			// Check to see if aliasing is turned on
			if (cmd.hasOption(ALIAS[SHORT]))
				setAlias(true);
			// Check for the flag that indicates whether to follow pointers
			if (cmd.hasOption(FOLLOW[SHORT]))
				setFollowPtrs(false);
			// Check for the include paths to indicate the paths to search for 
			// when following pointers
			if (cmd.hasOption(INCLUDES[SHORT]))
				setIncludePaths(
						Arrays.asList(cmd.getOptionValues(INCLUDES[SHORT])));
			// TODO: Check later when data object validation is implemented
			// Check to see if data object validation is set
//			if (cmd.hasOption("O")) 
//				setDataObj(false);
			if (cmd.hasOption(PROGRESS[SHORT]))
				setProgress(true);
			// Check to see if VTool will not recurse down a directory tree
			if (cmd.hasOption(LOCAL[SHORT]))
				setRecursive(false);
			// Check to see if regular expressions were set
			if (cmd.hasOption(REGEXP[SHORT]))
				setRegexp(Arrays.asList(cmd.getOptionValues(REGEXP[SHORT])));
			// Check to get file that contains file patterns to ignore
			if (cmd.hasOption(IGNOREFILE[SHORT])) {
				setNoFiles(Arrays.asList(
						cmd.getOptionValues(IGNOREFILE[SHORT])));
			}
			// Check to get file that contains directory patterns to ignore
			if (cmd.hasOption(IGNOREDIR[SHORT])) {
				setNoDirs(Arrays.asList(cmd.getOptionValues(IGNOREDIR[SHORT])));
			}
			// Check to get the dictionary file(s) 
			if (cmd.hasOption(DICT[SHORT]))
				setDictionaries(Arrays.asList(cmd.getOptionValues(DICT[SHORT])));
			// Check to see what type of reporting style the report will have
			if (cmd.hasOption(RPTSTYLE[SHORT]))				
				setRptStyle(cmd.getOptionValue(RPTSTYLE[SHORT]));
			// Check to see where the human-readable report will go
			if (cmd.hasOption(REPORT[SHORT]))
				setRptFile(new File(cmd.getOptionValue(REPORT[SHORT])));
			// Check to see if standalone label fragment validation is enabled
			if (cmd.hasOption(PARTIAL[SHORT]))
				setForcePartial(true);

		}
		catch(MissingOptionException moe) {
			System.err.println(moe.getMessage());
			System.exit(TOOLFAILURE);
		}
		catch(UnrecognizedOptionException uoe) {
			System.err.println(uoe.getMessage());
			System.exit(TOOLFAILURE);
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
			System.exit(TOOLFAILURE);
		}
	}
	
	/**
	 * Get aliasing flag
	 * @return 'true' if aliasing is ON, 'false' otherwise
	 */
	public boolean getAlias() {
		return this.alias;
	}
	
	/**
	 * Set aliasing flag
	 * @param a 'false' if aliasing should be turned off, 
	 * 'true' otherwise
	 */
	public void setAlias(boolean a) {
		this.alias = a;
	}
	
	/**
	 * Get data object validation flag
	 */
	public boolean getDataObj() {
		return this.dataObj;
	}
	
	/**
	 * Set data object flag
	 * @param d 'true' if data object validation is to be performed, 
	 * 'false' otherwise
	 */
	public void setDataObj(boolean d) {
		this.dataObj = d;
	}

	/**
	 * Get a list of dictionary files passed into VTool
	 * @return a List object of dictionary file names
	 */
	public List getDictionaries() {
		return this.dictionaries;
	}
	
	/**
	 * Set the dictionary file names passed into VTool 
	 * @param d a List object of dictionary files
	 */
	public void setDictionaries(List d) {
		this.dictionaries = d;
	}
	
	/**
	 * Get flag status that determines whether to follow pointers found
	 * in a label
	 * @return 'true' to follow, 'false' otherwise
	 */
	public boolean getFollowPtrs() {
		return this.followPtrs;
	}
	
	/**
	 * Set the flag that determines whether to follow pointers found in
	 * a label
	 * @param f 'true' to follow, 'false' otherwise
	 */
	public void setFollowPtrs(boolean f) {
		this.followPtrs = f;
	}
	
	/**
	 * Get flag status that determines whether standalone label fragment
	 * validation is enabled
	 * @return 'true' to enable, 'false' otherwise
	 */
	public boolean getForcePartial() {
		return this.force;
	}
	
	/**
	 * Set the flag that determines whether to validate standalone label
	 * fragments
	 * @param f 'true' to enable, 'false' otherwise
	 */
	public void setForcePartial(boolean f) {
		this.force = f;
	}
	
	/**
	 * Get the paths to search for files referenced by pointers in a label
	 * @return Start paths
	 */
	public List getIncludePaths() {
		return this.includePaths;
	}
	
	/**
	 * Set the paths to search for files referenced by pointers. Default is
	 * to always look first in the same directory as the label, then search
	 * specified directories.
	 * @param i List of paths
	 */
	public void setIncludePaths(List i) {
		this.includePaths = i;
	}
	
	/**
	 * Get the file name that contains the list of directories and/or
	 * directory patterns to ignore during validation
	 * @return a list of directory names/patterns to exclude from validation
	 */
	public List getNoDirs() {
		return this.noDirs;
	}
	
	/**
	 * Set the flag to ignore specified directories
	 * @param f a text file containing a list of directories and/or directory
	 *  patterns to ignore during validation. The names must be listed one
	 *  name per line.
	 */
	public void setNoDirs(List f) {
		this.noDirs = f;
	}
	
	/**
	 * Get the file name that contains the list of files and/or file patterns
	 * to ignore during validation
	 * @return a list of files/file patterns to exclude from validation
	 */
	public List getNoFiles() {
		return this.noFiles;
	}
	
	/**
	 * Set the flag to ignore specified files
	 * @param f a list of files/file patterns to ignore during validation.
	 */
	public void setNoFiles(List f) {
		this.noFiles = f;
	}
	
	/**
	 * Get the machine-readable log file name
	 * @return log file
	 */
	public File getLogFile() {
		return this.logFile;
	}
	
	/**
	 * Set the file name for the machine-readable log
	 * @param f file name of the log
	 */
	public void setLogFile(File f) {
		this.logFile = f;
	}
	
	/**
	 * Get the flag that determines whether to write the log to standard out
	 * @return 'true' to show log, 'false' to not show log
	 */
	public boolean getShowLog() {
		return this.showLog;
	}
	
	/**
	 * Set the flag to write the log to standard out
	 * @param l 
	 */
	public void setShowLog(boolean l) {
		this.showLog = l;
	}
	
	/**
	 * Get the report file name for the human-readable report
	 * @return Report file name
	 */
	public File getRptFile() {
		return this.rptFile;
	}
	
	/**
	 * Set the file for the human-readable report
	 * @param f file name
	 */
	public void setRptFile(File f) {
		this.rptFile = f;
	}
	
	/**
	 * Get the output style that was set for the validation report
	 * @return 'full' for a full report, 'sum' for a summary report or
	 *  'min' for minimal detail
	 */
	public String getRptStyle() {
		return this.rptStyle;
	}
	
	/**
	 * Set the output style for the report
	 * @param style 'sum' for a summary report, 'min' for a minimal report,
	 *  and 'full' for a full report
	 */
	public void setRptStyle(String style) {
		if ( (style.equalsIgnoreCase("sum") == false) &&
				(style.equalsIgnoreCase("min") == false) &&
				(style.equalsIgnoreCase("full") == false) ) {
				throw new IllegalArgumentException(
						"Invalid value entered for 's' flag. Value can only "
						+ "be either 'full', 'sum', or 'min'");
		}
		this.rptStyle = style;
	}
	
	/**
	 * Get the progress reporting flag
	 * @return 'true' if progress reporting is enabled, 'false' otherwise
	 */
	
	public boolean getProgress() {
		return this.progress;
	}
	
	/**
	 * Set the progress reporting flag
	 * @param p
	 */
	public void setProgress(boolean p) {
		this.progress = p;
	}
	
	/**
	 * Get the patterns to be matched when searching for files to validate in
	 * a directory
	 * @return a List object of patterns
	 */
	public List getRegexp() {
		return this.regexp;
	}
	
	/**
	 * Set the patterns flag
	 * @param e a List of patterns to be matched when searching for files to
	 *  validate in a directory
	 */
	public void setRegexp(List e) {
		this.regexp = e;
	}
	
	/**
	 * Set the recursive flag
	 * @param r 'true' to recursively traverse down a directory and all its
	 *  sub-directories, 'false' otherwise
	 */
	public void setRecursive(boolean r) {
		this.recursive = r;
	}
	
	/**
	 * Get the list of targets
	 * @return a List object of files, URLs, and/or directories
	 */
	public List getTargets() {
		return this.targets;
	}
	
	/**
	 * Set the targets flag
	 * @param t a List of files, URLs, and/or directories to be validated
	 */
	public void setTargets(List t) {
		this.targets = t;
	}
	
	/**
	 * Get the verbosity level
	 * @return an integer value where '1' for info, '2' for warnings'
	 * and '3' for errors
	 */
	public short getVerbose() {
		return this.verbose;
	}
	
	/**
	 * Set the verbosity level and above to include in the reporting
	 * @param v '1' for info, '2' for warnings, and '3' for errors
	 */
	public void setVerbose(short v) {
		verbose = v;
		if (verbose < 1 || verbose > 3) {
			throw new IllegalArgumentException(
					"Invalid value entered for 'v' flag. "
					+ "Valid values can only be 1, 2, or 3");
		}
/*		if (verbose == 0)
			severity = new String("Debug");*/
		if (verbose == 1)
			severity = new String("Info");
		else if (verbose == 2)
			severity = new String("Warning");
		else if (verbose == 3)
			severity = new String("Error");
	}
	
	/**
	 * Reads a configuration file to set the default behaviors for VTool.
	 * Flags set on the command-line will override flags set in the 
	 * configuration file
	 * 
	 * @param file a file containing keyword/value statements
	 */
	public void readConfigFile(URL file) {
		Configuration config = null;
		AbstractConfiguration.setDelimiter(' ');
		
		try {	
			config = new PropertiesConfiguration(file);
		}
		catch (ConfigurationException ce) {
			System.err.println(ce.getMessage());
			System.exit(TOOLFAILURE);
		}
		
		try {
			if (config.isEmpty())
				throw new Exception("Configuration file is empty: "
						+ file.toString());
			if (config.containsKey(ALIASKEY))
				setAlias(config.getBoolean(ALIASKEY));
			//TODO: Get Data Object key value when data object validation is implemented
//			if (config.containsKey(DATAOBJKEY))
//				setDataObj(config.getBoolean(DATAOBJKEY));
			if (config.containsKey(DICTKEY))
				setDictionaries(config.getList(DICTKEY));
			if (config.containsKey(FORCEKEY))
				setForcePartial(config.getBoolean(FORCEKEY));
			if (config.containsKey(FOLLOWKEY))
				setFollowPtrs(config.getBoolean(FOLLOWKEY));
			if (config.containsKey(IGNOREDIRKEY)) {
				setNoDirs(config.getList(IGNOREDIRKEY));
				// Removes quotes surrounding each pattern being specified
				for(int i=0; i < this.noDirs.size(); i++) {
					this.noDirs.set(i, 
							this.noDirs.get(i).toString().replace('"', ' ').trim());
				}
			}
			if (config.containsKey(IGNOREFILEKEY)) {
				setNoFiles(config.getList(IGNOREFILEKEY));
				// Removes quotes surrounding each pattern being specified
				for(int i=0; i < noFiles.size(); i++) {
					this.noFiles.set(i, 
							this.noFiles.get(i).toString().replace('"', ' ').trim());
				}
			}
			if (config.containsKey(INCLUDESKEY))
				setIncludePaths(config.getList(INCLUDESKEY));
			if (config.containsKey(LOGKEY))
				setLogFile(new File(config.getString(LOGKEY)));
			if (config.containsKey(SHOWLOGKEY)) {
				if ((config.getBoolean(SHOWLOGKEY)) 
					&& (config.containsKey(LOGKEY))) {
					throw new Exception(
							"'vtool.showlog' is set to 'true'. Cannot define "
							+ "'vtool.logfile' also.");
				}
				else
					setShowLog(config.getBoolean(SHOWLOGKEY));
			}
			if (config.containsKey(PROGRESSKEY))
				setProgress(config.getBoolean(PROGRESSKEY));
			if (config.containsKey(RECURSEKEY))
				setRecursive(config.getBoolean(RECURSEKEY));
			if (config.containsKey(REGEXPKEY)) {
				setRegexp(config.getList(REGEXPKEY));
				// Removes quotes surrounding each pattern being specified
				for(int i=0; i < this.regexp.size(); i++) {
					this.regexp.set(i, 
							this.regexp.get(i).toString().replace('"', ' ').trim());
				}
			}
			if (config.containsKey(REPORTKEY))
				setRptFile(new File(config.getString(REPORTKEY)));
			if (config.containsKey(STYLEKEY))
				setRptStyle(config.getString(STYLEKEY));
			if (config.containsKey(TARGETKEY))
				setTargets(config.getList(TARGETKEY));
			if (config.containsKey(VERBOSEKEY))
				setVerbose(config.getShort(VERBOSEKEY));
		} catch(ConversionException ce) {
			System.err.println(ce.getMessage());
			System.exit(TOOLFAILURE);
		} catch(Exception ex) {
			System.err.println(ex.getMessage());
			System.exit(TOOLFAILURE);
		}
	}
	
	/**
	 * Logs report header information such as version of the tool, 
	 * execution time, and flag settings
	 *
	 */
	public void logRptHeader() {
		ToolsTime time = new ToolsTime();

		//TODO: Print out data object validation
		log.log(new ToolsLogRecord(Level.CONFIG, 
				"VTool Version             " + versionID));
		try {
			log.log(new ToolsLogRecord(Level.CONFIG, 
				"Execution Date            " + time.getTime(timeFormat)));
		} catch(IllegalArgumentException eX) {
			System.out.println(eX.getMessage());
			System.exit(TOOLFAILURE);
		}
		
		log.log(new ToolsLogRecord(ToolsLevel.PARAMETER,
				"Target(s)                      " + targets));
		if (dictionaries != null) {
			log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, 
				"Dictionary File(s)             " + dictionaries));
		}
		
		log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, 
				"Aliasing                       " + alias));
		log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, 
				"Directory Recursion            " + recursive));
		log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, 
				"Follow Fragment Pointers       " + followPtrs));
		log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, 
				"Validate Standalone Fragments  " + force));
		
		if (logFile != null) {
			log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, 
					"Log File                       " + logFile));
		}
		if (rptFile != null) {
			log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, 
					"Report File                    " + rptFile));
		}
		if (rptStyle != null) {
		    log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, 
		    		"Report Style                   " + rptStyle));
		}
		if (severity != null) {
			log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, 
					"Severity Level                 " + severity));
		}
		if (includePaths != null) {
			log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, 
					"Include Path(s)                " + includePaths));
		}
		if (config != null) {
			log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, 
					"Configuration File             " + config));
		}
		if (regexp != null) {
			log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, 
					"Files Patterns                 " + regexp));
		}
		if (noDirs != null) {
			log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, 
					"Excluded Directories           " + noDirs));
		}
		if (noFiles != null) {
			log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, 
					"Excluded Files                 " + noFiles));
		}
	}
	
	/**
	 * Configures the java logging
	 *
	 */
	public void setupLogger() {
		Logger logger = Logger.getLogger("");
		logger.setLevel(Level.ALL);
		Handler []handler = logger.getHandlers();
		for(int i = 0; i < logger.getHandlers().length; i++) 
			logger.removeHandler(handler[i]);
		
		if (showLog == true) {
			StreamHandler stream = new StreamHandler(System.out, 
					                            new ToolsLogFormatter());
			logHandler = stream;
			logger.addHandler(stream);
			stream.setLevel(Level.ALL);
			log.log(new ToolsLogRecord(Level.INFO, 
					"Log being directed to standard out. Report-related "
					+ "options will be ignored"));
		}
		else {
			try {
				FileHandler file = new FileHandler(logFile.toString(), false);
				logHandler = file;
				file.setLevel(Level.ALL);
				file.setFormatter(new ToolsLogFormatter());
				logger.addHandler(file);
			} catch (SecurityException s) {
				System.err.println(s.getMessage());
				System.exit(TOOLFAILURE);
			} catch (IOException iEx) {
				System.err.println(iEx.getMessage());
				System.exit(TOOLFAILURE);
			}
		}
	}
	
	/**
	 * Convert a string to a URL
	 * @param s The string to convert
	 * @return A URL of the input string
	 */
	private URL toURL(String s) {
		URL url = null;		
		try {
			url = new URL(s);
		} catch (MalformedURLException ex) {
			try {
				url = new File(s).toURI().toURL();
			} catch (MalformedURLException mEx) {
				System.err.println(mEx.getMessage());
				System.exit(TOOLFAILURE);
			}
		}
		return url;
	}
	
	/**
	 * Parse the dictionary files.
	 * 
	 * @param dictionary a list of dictionary URLs
	 * @return a Dictionary object that includes all the dictionary
	 *  information from all the dictionary files passed in.
	 */
	public Dictionary readDictionaries(List dictionary) {
		Dictionary dict = null;
		URL dd = null;
		Iterator i = dictionary.iterator();
		
		try {
			//Parse the first dictionary
			dd = toURL(i.next().toString());
			dict = DictionaryParser.parse(dd, this.alias);
            log.log(new ToolsLogRecord(Level.CONFIG, 
            		"Dictionary version        \n"
            		+ dict.getInformation(), dd.toString()));
			log.log(new ToolsLogRecord(ToolsLevel.NOTIFICATION, 
					dict.getStatus(), dd.toString()));
			//Parse the rest of the dictionaries
			while (i.hasNext()) {
				dd = toURL(i.next().toString());
				Dictionary mergeDict = DictionaryParser.parse(dd, alias);
				dict.merge( mergeDict, true );
				log.log(new ToolsLogRecord(Level.CONFIG, 
						"Dictionary version        \n"
						+ dict.getInformation(), dd.toString()));
				log.log(new ToolsLogRecord(ToolsLevel.NOTIFICATION, 
						mergeDict.getStatus(), dd.toString()));
			}
		} catch (MalformedURLException uex) {
			log.log(new ToolsLogRecord(ToolsLevel.NOTIFICATION, 
					"FAIL", dd.toString()));
			System.err.println(uex.getMessage());
			System.exit(TOOLFAILURE);
		} catch (IOException iex) {
			System.err.println(iex.getMessage());
			System.exit(TOOLFAILURE);
		} catch (gov.nasa.pds.tools.label.parser.ParseException pe) {
			log.log(new ToolsLogRecord(ToolsLevel.NOTIFICATION, 
					"FAIL", dd.toString()));
			dictPassed = false;
		}
		return dict;	
	}
	
	/**
	 * Sets up the include paths and flag to follow pointers in
	 * the parser
	 * @param parser The label parser to set properties for
	 */
	private void setParserProps(LabelParser parser) {
		URL path = null;
		if (includePaths != null) {
			for( Iterator i = includePaths.iterator(); i.hasNext(); ) {
				path = toURL(i.next().toString());
				parser.addIncludePath(path);
			}
		}

		if (followPtrs == false)
			parser.getProperties().setProperty("parser.pointers", "false");
	}
	
	/**
	 * Validate labels found in the specified targets. If the target
	 * is a directory, this method will validate all labels found within
	 * the directory and its sub-directories. If recursion is turned OFF,
	 * then sub-directories will not be looked into.
	 * 
	 * @param targets a list of files, directories, and/or URLs
	 * @param dict the dictionary file
	 */	
	public void validateLabels(List targets, Dictionary dict) {
		
		for (Iterator i1 = targets.iterator(); i1.hasNext();) {
			FileList fileList = new FileList();
			fileList = processTarget(i1.next().toString(), recursive);
			
			for (Iterator i2 = fileList.getFiles().iterator(); i2.hasNext();) {
				URL target = toURL(i2.next().toString());				
				String result = validateLabel(target, dict);
				saveResults(result);
			}
			
			if (!fileList.getDirs().isEmpty())
				validateLabels(fileList.getDirs(), dict);
		}
		
	}
	
	/**
	 * Record the results of a label validation to determine the success
	 * of the tool run
	 * @param result 'PASS' for a good label, 'FAIL' for a bad label, or
	 * 'UNKNOWN' for a label that skipped validation
	 */
	private void saveResults(String result) {
		if (PASS.equals(result))
			++goodLbls;
		else if (FAIL.equals(result))
			++badLbls;
		else
			++unkLbls;
	}
	
	/**
	 * Returns an exit status code based on the validation results
	 * @return '0' if files have passed and a '1' if any of the following
	 * occurs:
	 * <ul>
	 * a file had a validation error<br>
	 * a file skipped validation<br>
	 * dictionary validation failed<br>
	 * </ul>
	 */
	private int getExitStatus() {
		int status = 0;
		
		if (badLbls > 0)
			status = BADRUN;
		else if (unkLbls > 0 && goodLbls == 0)
			status = BADRUN;
		else if (dictPassed == false)
			status = BADRUN;
		else
			status = GOODRUN;
		
		return status;
	}
	
	/**
	 * Processes a target
	 * 
	 * @param target The file or URL to process
	 * @param getSubDirs 'True' to look for sub-directories, 'false' otherwise
	 * @return A FileList object containing the sub-directories and files
	 *  found in the target, if any
	 */
	public FileList processTarget(String target, boolean getSubDirs) {		
		FileListGenerator fileGen = new FileListGenerator();
		FileList list = new FileList();
		fileGen.setFilters(regexp, noFiles, noDirs);
		
		try {
			list = fileGen.visitTarget(target, getSubDirs);
		} catch (MalformedURLException uEx) {
			System.err.println(uEx.getMessage());
			System.exit(TOOLFAILURE);
		} catch (IOException iEx) {
			System.err.println(iEx.getMessage());
			System.exit(TOOLFAILURE);
		} catch (BadLocationException bEx) {
			System.err.println(bEx.getMessage());
			System.exit(TOOLFAILURE);
		} catch (NullPointerException nEx) {
			System.err.println(nEx.getMessage());
			System.exit(TOOLFAILURE);
		}
		return list;
	}
	
	/**
	 * Validate a label file.
	 * 
	 * @param file The URL of the file to be validated
	 * @param dict a Dictionary object needed for semantic validation. If null,
	 *             only syntactic validation will be performed.
	 * @return 'PASS' if the label passed the PDS validation step, 
	 *         'FAIL' if the label failed the PDS validation step, or
	 *         'UNKOWN' if the label skipped the PDS validation step
	 */
	
	public String validateLabel(URL file, Dictionary dict) {		
		LabelParserFactory factory = LabelParserFactory.getInstance();
		LabelParser parser = factory.newLabelParser();
		Label label = null;
		setParserProps(parser);
		
		if (progress)
			showProgress(file);
		try {
			//Check to see if the file is a fragment and if standalone fragment
			//validation was enabled
			if (FilenameUtils.getExtension(
					file.getFile()).equalsIgnoreCase(partialExt) && force) {
				if (dict == null)
					label = parser.parsePartial(file);
				else
					label = parser.parsePartial(file, dict);
			}
			else {
				if (dict == null)
					label = parser.parse(file);
				else
					label = parser.parse(file, dict);
			}
		} catch (gov.nasa.pds.tools.label.parser.ParseException pEx) {
			log.log(new ToolsLogRecord(ToolsLevel.NOTIFICATION, 
					"SKIP", file.toString()));
			return UNKNOWN;
		} catch (IOException iEx) {
			log.log(new ToolsLogRecord(ToolsLevel.NOTIFICATION, 
					"SKIP", file.toString()));
			log.log(new ToolsLogRecord(ToolsLevel.WARNING, 
					iEx.getMessage(), file.toString()));
			return UNKNOWN;			
		}
		log.log(new ToolsLogRecord(ToolsLevel.NOTIFICATION, 
				label.getStatus(), file.toString()));
		
		if (PASS.equals(label.getStatus()))
			return PASS;
		else if(FAIL.equals(label.getStatus()))
			return FAIL;
		else
			return UNKNOWN;
	}
	
	/**
	 *  Prints out the current directory being validated and
	 *  represents each file being validated by an asterisk.
	 *  This is used when progress reporting is enabled.
	 * 
	 * @param file The URL being validated
	 */
	public void showProgress(URL file) {
		URL dir = null;
		try {
			dir = new URL(file.toString().
					substring(0, file.toString().lastIndexOf("/")));
		} catch (MalformedURLException uEx) {
			System.err.println(uEx.getMessage());
			System.exit(TOOLFAILURE);
		}
		
		if (dir.toString().equals(currDir)) {
			System.err.print(fileRep);
		}
		else {
			currDir = new String(dir.toString());
			System.err.println("\nValidating file(s) in: " + currDir);
			System.err.print(fileRep);
		}
	}
	
	/**
	 * Creates a human-readable report
	 * 
	 * @param log The xml log
	 * @param report Where the human-readable report will be written to.
	 *  If null, then it goes to standard out.
	 * @param level The severity level to include in the warning. Can be
	 *  "INFO", "WARNING", or "ERROR".
	 * @param style The reporting style to generate. Can be either "full",
	 *  "sum", or "min" for a full, summary, or minimal report, respectively.
	 */
	public void doReporting(File log, File report, String level, String style) {
		String rptType = null;
		
		if ( (level.equalsIgnoreCase("info") == false) 
			  && (level.equalsIgnoreCase("warning") == false)
			  && (level.equalsIgnoreCase("error") == false) ) {
				throw new IllegalArgumentException("Invalid severity level: "
						+ level + ". Must be 'info', 'warning', or 'error'.");
		}
		
		if (style.equalsIgnoreCase("full"))
			rptType = FULLXSL;
		else if (style.equalsIgnoreCase("sum"))
			rptType = SUMXSL;
		else if (style.equalsIgnoreCase("min"))
			rptType = MINXSL;
		else {
			throw new IllegalArgumentException("Invalid style specified: "
					+ style + ". Must be 'full', 'sum' or 'min'");
		}
		try {
			if (report == null)
				generateReport(log, rptType, level.toUpperCase(), System.out);
			else {
				generateReport(log, rptType, level.toUpperCase(), 
						new FileOutputStream(report));
			}
		} catch (TransformerException tEx) {
			System.err.println(tEx.getMessage());
			System.exit(TOOLFAILURE);
		} catch (FileNotFoundException fEx) {
			System.err.println(fEx.getMessage());
			System.exit(TOOLFAILURE);
		}
	}
	
    private void generateReport(File logFile, String report, String level, 
    							OutputStream output) throws TransformerException {
        Source xmlSource = new StreamSource(logFile);
        Source xsltSource = new StreamSource(getClass().getResourceAsStream(report));
        Result result = new StreamResult(output);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xsltSource);
        
        transformer.setParameter("level", level);
        transformer.transform(xmlSource, result);
    }
    
    private void closeHandler() {
    	logHandler.close();
    }
	
	/**
	 * The main calls the following methods (in this order):<br><br>
	 * 
	 * buildOpts<br>
	 * parseLine<br>
	 * queryCmdLine<br>
	 * setupLogger<br>
	 * logParams<br>
	 * readDictionaries<br>
	 * validateLabels<br>
	 * doReporting<br>
	 * <br>
	 * The setupLogger method is called after parsing and querying the
	 * command-line since we can accept a configuration file as input
	 * and by rule, anything on the command-line overrides parameters
	 * set in the configuration file. So, by design, VTool would not
	 * know where to direct its logs until after it queries the
	 * command-line.  
	 * 
	 * @param argv Arguments passed on the command-line
	 */
	public static void main(String[] argv) {
		VTool vtool = new VTool();
		Dictionary dictionary = null;

		if (argv.length == 0) {
			System.out.println("\nType 'VTool -h' for usage");
			return;
		}
		vtool.setVerbose(Short.parseShort("2"));
		// Define options
		vtool.buildOpts();
		// Parse the command line
		vtool.parseLine(argv);
		// Query the command line
		vtool.queryCmdLine();
		//Setup the logger
		vtool.setupLogger();
		// Log the report header
		vtool.logRptHeader();	
		
		if (vtool.dictionaries != null) {
			dictionary = vtool.readDictionaries(vtool.dictionaries);
			if (vtool.dictPassed)
				vtool.validateLabels(vtool.targets, dictionary);
		}
		else
			vtool.validateLabels(vtool.targets, null);
			
		vtool.closeHandler();
		
		if (vtool.logFile != null) {
			vtool.doReporting(vtool.logFile, vtool.rptFile,
								vtool.severity, vtool.rptStyle);
		}
		
		System.exit(vtool.getExitStatus());
	}
}
