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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
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
 * Class to perform automated validation to determine if a given data product
 * is PDS compliant. This replaces LVTool functionality.
 *  
 * @author mcayanan
 * @version $Revision$
 *
 *
 */
public class VTool implements VToolConfigKeys, ToolsFlags, ExitStatus, 
                              Status, StyleSheet {
	private final String VERSION_ID = "0.5.0";
	private final String FILE_REP = "*";
	private final String FRAG_EXT = "FMT";
	
	private static Logger log = Logger.getLogger(VTool.class.getName());
	private ByteArrayOutputStream byteStream;
	private String currDir;
	private boolean dictPassed;
	
	private List logHandlers;
	private Options options;
	private CommandLineParser parser;
	private CommandLine cmd;
	
	//TODO: Flags to be implemented: data object(-O,--no-obj),
	
	private boolean alias;
	private URL config;
//	private boolean dataObj;
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
//		dataObj = true;
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
		severity = "Warning";
		logFile = null;
		showLog = false;
		verbose = 2;
		
		currDir = null;
		options = new Options();
		parser = new GnuParser();
		logHandlers = new ArrayList();
		
		goodLbls = 0;
		badLbls = 0;
		unkLbls = 0;
	}
	
	/**
	 * Show the version and disclaimer notice for VTool 
	 *
	 */	
	public void showVersion() {
		System.out.println("\nPDS Validation Tool (VTool) " + VERSION_ID);
		System.out.println("\nCopyright 2006-2007, by the California Institute of Technology. ALL\n"
				           + "RIGHTS RESERVED. United States Government Sponsorship acknowledged.\n"
				           + "Any commercial use must be negotiated with the Office of Technology\n"
				           + "Transfer at the California Institute of Technology. This software\n"
				           + "may be subject to U.S. export control laws. By accepting this\n"
				           + "software, the user agrees to comply with all applicable U.S. export\n"
				           + "laws and regulations. User has the responsibility to obtain export\n"
				           + "licenses, or other export authority as may be required before\n"
				           + "exporting such information to foreign countries or providing access\n"
				           + "to foreign persons.");
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
		  + "Default is Warning and above (level 2)");
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
/*
	public boolean getDataObj() {
		return this.dataObj;
	}
*/	
	/**
	 * Set data object flag
	 * @param d 'true' if data object validation is to be performed, 
	 * 'false' otherwise
	 */
/*
	public void setDataObj(boolean d) {
		this.dataObj = d;
	}
*/
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
		if (v < 1 || v > 3) {
			throw new IllegalArgumentException(
					"Invalid value entered for 'v' flag. "
					+ "Valid values can only be 1, 2, or 3");
		}
		verbose = v;

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
			if (config.containsKey(LOGKEY)) {
				String logValue = new String(config.getProperty(LOGKEY).toString());
				// Check for a boolean value or a file name
				if(logValue.equalsIgnoreCase("true") 
						|| logValue.equalsIgnoreCase("false")) {
					setShowLog(Boolean.valueOf(logValue).booleanValue());
				}
				else
					setLogFile(new File(logValue));
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
				"VTool Version             " + VERSION_ID));
		try {
			log.log(new ToolsLogRecord(Level.CONFIG, 
				"Execution Date            " 
				+ time.getTime(new SimpleDateFormat("EEE, MMM dd yyyy 'at' HH:mm:ss a"))));
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
				"Follow Pointers                " + followPtrs));
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
		log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, 
				"Progress Reporting             " + progress));
	}
	
	/**
	 * Configures the logger. If a log file was specified on the 
	 * command-line, the log will be written to that file. If the
	 * log flag was specified with no file spec, then the log will
	 * be written to standard out. Otherwise, the log will be
	 * written to memory (ByteArrayOutputStream). 
	 *
	 */
	public void setupLogger() {
		Logger logger = Logger.getLogger("");
		logger.setLevel(Level.ALL);
		
		Handler []handler = logger.getHandlers();
		for(int i = 0; i < logger.getHandlers().length; i++) 
			logger.removeHandler(handler[i]);

		
		//Write to a file if a log file was specified
		if(logFile != null) {
			FileHandler file = setupFileHandler(logFile);
			logger.addHandler(file);
			logHandlers.add(file);
		}
		//Write to standard out if the log option flag was specified
		else if (showLog == true && rptFile == null) {
			StreamHandler stream = setupStreamHandler(System.out);
			logger.addHandler(stream);
			logHandlers.add(stream);
		}
		//Write to both standard out and memory if the log option flag
		//was specified and no report file was specified
		else if (showLog == true && rptFile != null) {
			byteStream = new ByteArrayOutputStream();
			StreamHandler byteArray = setupStreamHandler(byteStream);
			logger.addHandler(byteArray);
			logHandlers.add(byteArray);
			
			StreamHandler stream = setupStreamHandler(System.out);
			logger.addHandler(stream);
			logHandlers.add(stream);
		}
		//Write to memory if none of the above conditions exist
		else {
			byteStream = new ByteArrayOutputStream();
			StreamHandler byteArray = setupStreamHandler(byteStream);
			logger.addHandler(byteArray);
			logHandlers.add(byteArray);
		}
			
	}
	
	/**
	 * Sets up a handler to an outputstream
	 * @param out An outputstream to write the logger to
	 * @return a stream handler
	 */
	private StreamHandler setupStreamHandler(OutputStream out) {
		StreamHandler stream = new StreamHandler(out, new ToolsLogFormatter());
		stream.setLevel(Level.ALL);
		
		return stream;
	}
	
	/**
	 * Sets up a file handler
	 * @param log The name of the file to write the logger to
	 * @return a file handler to the input file
	 */
	private FileHandler setupFileHandler(File log) {
		FileHandler file = null;
		try {
			file = new FileHandler(log.toString(), false);
			file.setLevel(Level.ALL);
			file.setFormatter(new ToolsLogFormatter());
		} catch (SecurityException s) {
			System.err.println(s.getMessage());
			System.exit(TOOLFAILURE);
		} catch (IOException iEx) {
			System.err.println(iEx.getMessage());
			System.exit(TOOLFAILURE);
		}
		return file;
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
            //Log dictionary status only upon a FAIL
            if(dict.getStatus().equals(FAIL)) {
            	log.log(new ToolsLogRecord(ToolsLevel.NOTIFICATION, 
					dict.getStatus(), dd.toString()));
            }
			//Parse the rest of the dictionaries
			while (i.hasNext()) {
				dd = toURL(i.next().toString());
				Dictionary mergeDict = DictionaryParser.parse(dd, alias);
				dict.merge( mergeDict, true );
				log.log(new ToolsLogRecord(Level.CONFIG, 
				    "Dictionary version        \n"
					+ mergeDict.getInformation(), dd.toString()));
				//Log dictionary status only upon a FAIL
				if(mergeDict.getStatus().equals(FAIL)) {
					log.log(new ToolsLogRecord(ToolsLevel.NOTIFICATION, 
						mergeDict.getStatus(), dd.toString()));
				}
			}
		} catch (MalformedURLException uex) {
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
	 * 
	 * @return '0' if files "passed" and a '1' if any of the following
	 * occurs:<br>
	 * <ul>
	 * <li>one or more files failed validation</li>
	 * <li>one or more files skipped validation and in addition,  
	 *   no other files passed validation</li>
	 * <li>dictionary validation failed</li>
	 * </ul>
	 */
	public int getExitStatus() {
		int status = 0;
		
		//Bad run if there were one or more bad labels
		if (badLbls > 0)
			status = BADRUN;
		//Bad run if there were one or more files that 
		//skipped validation and in addition, no other
		//files passed
		else if (unkLbls > 0 && goodLbls == 0)
			status = BADRUN;
		//Bad run if there was a dictionary error
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
			if ( (FilenameUtils.getExtension(file.getFile()).equalsIgnoreCase(FRAG_EXT))
				&& (force) ) {
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
		
		return label.getStatus();
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
			System.err.print(FILE_REP);
		}
		else {
			currDir = new String(dir.toString());
			System.err.println("\nValidating file(s) in: " + currDir);
			System.err.print(FILE_REP);
		}
	}
	
	/**
	 * Transforms an XML log to a human-readable report
	 * 
	 * @param in inputstream of the XML log
	 * @param xsl the stylesheet to use in creating the human-readable report
	 * @param level the severity level to set for the report (info, warning, or error)
	 * @param output The stream type for the final report
	 * @throws TransformerException If there was an error during the transformation
	 *  of the log to the final report
	 */
    private void generateReport(InputStream in, String xsl, String level, 
    							OutputStream output) throws TransformerException {

        Source xmlSource = new StreamSource(in);
        Source xsltSource = new StreamSource(getClass().getResourceAsStream(xsl));
        Result result = new StreamResult(output);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xsltSource);
        
        transformer.setParameter("level", level);
        transformer.transform(xmlSource, result);
    }
	
	/**
	 * Creates a human-readable report
	 * 
	 * @param log Where the xml log is located. If null, then the xml log
	 *  will be read from memory.
	 * @param report Where the human-readable report will be written to.
	 *  If null, then it goes to standard out.
	 * @param level The severity level to include in the report. Can be
	 *  "INFO", "WARNING", or "ERROR".
	 * @param style The reporting style to generate. Can be either "full",
	 *  "sum", or "min" for a full, summary, or minimal report, respectively.
	 */
	public void doReporting(File log, File report, String level, String style) {
		String xsl = null;
		InputStream input = null;
		OutputStream output = null;
		
		if ( (level.equalsIgnoreCase("info") == false) 
			  && (level.equalsIgnoreCase("warning") == false)
			  && (level.equalsIgnoreCase("error") == false) ) {
				throw new IllegalArgumentException("Invalid severity level: "
						+ level + ". Must be 'info', 'warning', or 'error'.");
		}
		
		xsl = getXSL(style);

		try {
			input = getRptInStream(log);
			output = getRptOutStream(report);
		} catch (FileNotFoundException ex) {
			System.err.println(ex.getMessage());
			System.exit(TOOLFAILURE);
		}
		
		try {
			generateReport(input, xsl, level.toUpperCase(), output);
		} catch (TransformerException t) {
			System.err.println(t.getMessage());
			System.exit(TOOLFAILURE);
		}
		//If report is written to standard out, add an extra line terminator
		if(report == null)
			System.out.println();
	}
	
	/**
	 * Gets the proper XSL stylesheet
	 * 
	 * @param style 'full' for a full XSL, 'sum' for a summary XSL, or 'min'
	 *  for a minimal XSL 
	 * @return The XSL stylesheet name
	 */
	private String getXSL(String style) {
		if (style.equalsIgnoreCase("full"))
			return FULLXSL;
		else if (style.equalsIgnoreCase("sum"))
			return SUMXSL;
		else if (style.equalsIgnoreCase("min"))
			return MINXSL;
		else {
			throw new IllegalArgumentException("Invalid style specified: "
				+ style + ". Must be 'full', 'sum' or 'min'");
		}
	}
	
	/**
	 * Returns the input stream of the XML log for the final report.
	 * 
	 * @param file A file name. If null, then a ByteArrayInputStream is returned. 
	 * 
	 * @return The proper input stream 
	 * @throws FileNotFoundException 
	 */
	private InputStream getRptInStream(File file) throws FileNotFoundException {	
		if(file != null)
				return new FileInputStream(file);
		else
			return new ByteArrayInputStream(byteStream.toByteArray());
	}
	
	/**
	 * Returns the appropriate output stream for the final report
	 * @param rpt The report file name. If null, then the standard out
	 *  stream is returned.
	 * 
	 * @return The proper output stream
	 * @throws FileNotFoundException
	 */
	private OutputStream getRptOutStream(File rpt) throws FileNotFoundException {
		if(rpt != null) 
			return new FileOutputStream(rpt);
		else
			return System.out;
	}
	  
    /**
     * Closes the handlers that were set for the logger
     *
     */
	public void closehandle() {
		for(Iterator i=logHandlers.iterator(); i.hasNext();) {
			Handler handler = (Handler) i.next();
			handler.close();
		}
	}
	/**
	 * The main calls the following methods (in this order):<br><br>
	 * 
	 * To setup the flags and parse the command-line options:
	 * <ul> 
	 * <li>buildOpts</li>
	 * <li>parseLine</li>
	 * <li>queryCmdLine</li>
	 * </ul>
	 * <br>
	 * To setup the logger and log the report header information:
	 * <ul>
	 * <li>setupLogger</li>
	 * <li>logRptHeader</li>
	 * </ul>
	 * <br>
	 * To perform validation:
	 * <ul>
	 * <li>readDictionaries (if the PSDD was passed in)</li>
	 * <li>validateLabels</li>
	 * </ul>
	 * <br>
	 * To create the final report:
	 * <ul>
	 * <li>closehandle (to flush the buffers)</li>
	 * <li>doReporting</li>
	 * </ul>
	 * <br>
	 * Reporting is not generated if the log flag was specified with
	 * no file spec and the report file flag was not specified
	 * <br><br>
	 * VTool returns an appropriate exit status based on validation results.
	 * <br>
	 * In general, the following is returned:
	 * <ul>
	 * <li>'0' upon "successful" validation</li>
	 * <li>'1' upon a valiadation "failure"</li>
	 * <li>'-1' upon application failure</li>
	 * </ul>
	 *  
	 * @param argv Arguments passed on the command-line
	 */
	public static void main(String[] argv) {
		VTool vtool = new VTool();
		Dictionary dictionary = null;

		if (argv.length == 0) {
			System.out.println("\nType 'VTool -h' for usage");
			System.exit(GOODRUN);
		}
		
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
			
			//Validate labels only if the dictionary passed
			if (vtool.dictPassed)
				vtool.validateLabels(vtool.targets, dictionary);
		}
		else
			vtool.validateLabels(vtool.targets, null);
		
		vtool.closehandle();
		
		//Add extra line terminator if progress reporting was enabled
		if(vtool.progress == true)
			System.err.println();
		
		//If the log flag with no file spec was specified AND the
		//report file flag is not specified, then do not create a report
		if( !(vtool.showLog && vtool.rptFile == null) ) {
			vtool.doReporting(vtool.logFile, vtool.rptFile, vtool.severity, vtool.rptStyle);
		}
		
		System.exit(vtool.getExitStatus());
	}
}
