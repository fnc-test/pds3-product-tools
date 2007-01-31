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
import gov.nasa.pds.tools.label.parser.LabelParser;
import gov.nasa.pds.tools.label.parser.LabelParserFactory;
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

import javax.swing.text.BadLocationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.StreamHandler;

import gov.nasa.pds.tools.logging.ToolsLevel;
import gov.nasa.pds.tools.logging.ToolsLogRecord;
import gov.nasa.pds.tools.logging.ToolsLogFormatter;

/**
 * @author mcayanan
 * @version $Revision$
 * 
 * Class to replace LVTool functionality
 * 
 */
public class VTool implements VToolConfigKeys {
	
	final private String versionId = "0.4.0"; 
	private static Logger log = Logger.getLogger(VTool.class.getName());
	private Handler logHandler = null;
	
	private Options options;
	private CommandLineParser parser;
	private CommandLine cmd;
	
	//TODO: Flags to be implemented: partial (-f,--force), data object(-O,--no-obj),
	
	private boolean alias;
	private URL config;
	private boolean dataObj;
	private List dictionaries;
	private List noFiles;
	private List noDirs;
	private boolean followPtrs;
	private List includePaths;
	private boolean progress;
	private List regexp;
	private boolean recursive;
	private List targets;
	private File logFile;
	private File rptFile;
	private String rptStyle;
	private short verbose;
	private String severity;
	
	private String currDir;
	
	/** 
	 * Default constructor
	 */
	public VTool() {
		alias = false;
		config = null;
		dictionaries = null;
		dataObj = true;
		noFiles = null;
		noDirs = null;
		followPtrs = true;
		targets = null;
		includePaths = null;
		progress = false;
		regexp = null;
		recursive = true;
		rptFile = null;
		rptStyle = "full";
		severity = null;
		logFile = null;
		currDir = null;
		options = new Options();
		parser = new GnuParser();
	}
	
	/**
	 * Show the version and disclaimer notice for VTool 
	 *
	 */	
	public void showVersion() {
		System.out.println("PDS Validation Tool (VTool) " + versionId);
		System.out.println("\nDISCLAIMER:\n" + 
				           "THIS SOFTWARE AND ANY RELATED MATERIALS WERE CREATED BY THE CALIFORNIA\n" + 
				           "INSTITUTE OF TECHNOLOGY (CALTECH) UNDER A U.S. GOVERNMENT CONTRACT WITH THE\n" +
				           "NATIONAL AERONAUTICS AND SPACE ADMINISTRATION (NASA). THE SOFTWARE IS\n" + 
				           "TECHNOLOGY AND SOFTWARE PUBLICLY AVAILABLE UNDER U.S. EXPORT LAWS AND IS\n" + 
						   "PROVIDED \"AS-IS\" TO THE RECIPIENT WITHOUT WARRANTY OF ANY KIND, INCLUDING\n" + 
					       "ANY WARRANTIES OF PERFORMANCE OR MERCHANTABILITY OR FITNESS FOR A PARTICULAR\n" + 
						   "USE OR PURPOSE (AS SET FORTH IN UNITED STATES UCC2312-2313) OR FOR ANY\n" + 
						   "PURPOSE WHATSOEVER, FOR THE SOFTWARE AND RELATED MATERIALS, HOWEVER USED. IN\n" + 
						   "NO EVENT SHALL CALTECH, ITS JET PROPULSION LABORATORY, OR NASA BE LIABLE FOR\n" + 
						   "ANY DAMAGES AND/OR COSTS, INCLUDING, BUT NOT LIMITED TO, INCIDENTAL OR\n" + 
						   "CONSEQUENTIAL DAMAGES OF ANY KIND, INCLUDING ECONOMIC DAMAGE OR INJURY TO\n" +
						   "PROPERTY AND LOST PROFITS, REGARDLESS OF WHETHER CALTECH, JPL, OR NASA BE\n" +
						   "ADVISED, HAVE REASON TO KNOW, OR, IN FACT, SHALL KNOW OF THE POSSIBILITY.\n" +
						   "RECIPIENT BEARS ALL RISK RELATING TO QUALITY AND PERFORMANCE OF THE SOFTWARE\n" +
						   "AND ANY RELATED MATERIALS, AND AGREES TO INDEMNIFY CALTECH AND NASA FOR ALL\n" +
						   "THIRD-PARTY CLAIMS RESULTING FROM THE ACTIONS OF RECIPIENT IN THE USE OF THE\n" +
						   "SOFTWARE.");
		System.exit(0);
	}
	
	/**
	 * Display VTool usage and help information
	 *
	 */
	public void showHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(37, "VTool", null, options, null);
		System.exit(0);
	}
	
	/**
	 * Builds the set of configurable parameters for VTool
	 */
	public void buildOpts() {
		options.addOption("a", "alias", false, "Enable aliasing");
		options.addOption("F", "no-follow", false, "Do not follow or check for the existence of files referenced by pointer statements in a label");
		options.addOption("h", "help", false, "Display usage");
		options.addOption("O", "no-obj", false, "Do not perform data object validation");
		options.addOption("f", "force", false, "Force VTool to validate a label fragment");
		options.addOption("L", "local", false, "Validate files only in the input directory rather than " + 
				                               "recursively traversing down the subdirectories.");
		options.addOption("p", "progress", false, "Enable progress reporting");
		options.addOption("V", "version", false, "Display VTool version");
		
		
		// These are options that require an argument

		// Option to specify a configuration file
		OptionBuilder.withLongOpt("config");
		OptionBuilder.withDescription("Specify a configuration file to set the default values for VTool");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("file");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("c"));
		
		// Option to specify the PSDD and any local dictionaries
		OptionBuilder.withLongOpt("dict");
		OptionBuilder.withDescription("Specify the Planetary Science Data Dictionary full file name/URL " +
				                      "and any local dictionaries to include for validation. Separate each file name with a space.");
		OptionBuilder.hasArgs();
		OptionBuilder.withArgName(".full files");
		OptionBuilder.withValueSeparator();
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("d"));
		
		// Option to specify a file containing a list of file extensions to ignore
		OptionBuilder.withLongOpt("ignore-file");
		OptionBuilder.withDescription("Specify file patterns to ignore from validation. Each pattern " + 
				                      "must be surrounded by quotes. (i.e. -X \"*TAB\" \"*IMG\")");
		OptionBuilder.hasArgs();
		OptionBuilder.withArgName("patterns");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("X"));
		
		// Option to specify the label(s) to validate
		OptionBuilder.withLongOpt("target");
		OptionBuilder.withDescription("Specify the label file(s), URL(s) and/or directories to validate (required option)");
		OptionBuilder.hasArgs();
		OptionBuilder.withArgName("labels,URLs,dirs");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("t"));
		
		// Option to specify a pattern to match against the input directory to be validated
		OptionBuilder.withLongOpt("regexp");
		OptionBuilder.withDescription("Specify file patterns to look for when validating a directory. " +
				                      "Each Pattern must be surrounded by quotes. (i.e. -e \"*.LBL\" \"*FMT\")");
		OptionBuilder.hasArgs();
		OptionBuilder.withArgName("patterns");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("e"));
		
		// Option to specify a path to the Pointer files		
		OptionBuilder.withLongOpt("include");
		OptionBuilder.withDescription("Specify the paths to look for files referenced by pointers in a label. Separate each with a space. " + 
                                       "Default is to always look at the same directory as the label");
		OptionBuilder.hasArgs();
		OptionBuilder.withArgName("paths");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("I"));

		//Option to specify a file containing a list of directories to ignore
		OptionBuilder.withLongOpt("ignore-dir");
		OptionBuilder.withDescription("Specify directory patterns to ignore. Each listing must be surrounded by quotes. " +
				                       "(i.e. -D \"EXTRAS\" \"LABEL\")");
		OptionBuilder.hasArgs();
		OptionBuilder.withArgName("patterns");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("D"));
		
		// Option to specify the log file name
		OptionBuilder.withLongOpt("log-file");
		OptionBuilder.withDescription("Specify the file name for the machine-readable report. Default is to print to the terminal");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("file");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("l"));

		
		//Option to specify the report file name
		OptionBuilder.withLongOpt("report-file");
		OptionBuilder.withDescription("Specify the file name for the human-readable report. Default is to print to the terminal");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("file");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("r"));
		
		// Option to specify how detail the reporting should be
		OptionBuilder.withLongOpt("report-style");
		OptionBuilder.withDescription("Specify the level of detail for the reporting. " +
										"Valid values are 'full' for full details, 'min' for minimal detail " +
										"and 'sum' for a summary. Default is set to 'full'");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("full|sum|min");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("s"));
		
		// Option to specify the severity level and above
		OptionBuilder.withLongOpt("verbose");
		OptionBuilder.withDescription("Specify the severity level and above to include in the human-readable report: " + 
				                             "(1=Info, 2=Warning, 3=Severe). " + 
				                             "Default is Warnings and above (level 2)");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("0|1|2|3");
		OptionBuilder.withType(short.class);
		options.addOption(OptionBuilder.create("v"));

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
			System.err.println( "Command line parser failed.\n\nReason: " + exp.getMessage() );
			System.exit(1);
		}
	}
	
	/** 
	 * Queries the VTool command-line 
	 *
	 */
	public void queryCmdLine() {
		try {			
			// Check for unrecognized arguments on the command-line
			for(Iterator i = cmd.getArgList().iterator(); i.hasNext();)
				throw new UnrecognizedOptionException( "Unrecognized option/argument: " + i.next().toString());
			
			// Check if the help flag was set
			if(cmd.hasOption("h"))
				showHelp();
			// Check if the flag to display the version number and disclaimer notice was set
			if(cmd.hasOption("V")) 
				showVersion();
			// Check if a configuration file was specified
			if(cmd.hasOption("c")) {
				String file = cmd.getOptionValue("c");
				if(isURL(file))
					config = new URL(file);
				else
					config = new File(file).toURL();
				readConfigFile(config);
			}
			// Check for the 'l' flag to specify a file where the log will be written to
			if(cmd.hasOption("l"))
				setLogFile(new File(cmd.getOptionValue("l")));
			// verbose flag
			if(cmd.hasOption("v")) {
				try {
					setVerbose(Short.parseShort(cmd.getOptionValue("v")));
				}
				catch( NumberFormatException ne) {
					throw new Exception("Problems parsing value for the 'v' flag: " + cmd.getOptionValue("v"));
				}
				catch(IllegalArgumentException ae) {
					throw new Exception(ae.getMessage());
				}
		
			}
			// Check if the -t flag was set in either the config file or the command line
			if(cmd.hasOption("t")) {
				setTargets(Arrays.asList(cmd.getOptionValues("t")));
			}
			else if(targets == null)
				throw new MissingOptionException("The 't' flag is required or must be set in the configuration file");
			
			// Check to see if aliasing is turned on
			if(cmd.hasOption("a"))
				setAlias(true);
			// Check for the flag that indicates whether to follow ^STRUCTURE pointers
			if(cmd.hasOption("F"))
				setFollowPtrs(false);
			// Check for the include paths to indicate the paths to search for 
			// when following pointers
			if(cmd.hasOption("I"))
				setIncludePaths(Arrays.asList(cmd.getOptionValues("I")));			
			// Check to see if data object validation is set
			if(cmd.hasOption("O")) 
				setDataObj(false);
			if(cmd.hasOption("p"))
				setProgress(true);
			// Check to see if VTool will not recurse down a directory tree
			if(cmd.hasOption("L"))
				setRecursive(false);
			// Check to see if regular expressions were set
			if(cmd.hasOption("e"))
				setRegexp(Arrays.asList(cmd.getOptionValues("e")));
			// Check to get file that contains file patterns to ignore
			if(cmd.hasOption("X"))
				setNoFiles(Arrays.asList(cmd.getOptionValues("X")));
			// Check to get file that contains directory patterns to ignore
			if(cmd.hasOption("D"))
				setNoDirs(Arrays.asList(cmd.getOptionValues("D")));
			// Check to get the dictionary file(s) 
			if(cmd.hasOption("d"))
				setDictionaries(Arrays.asList(cmd.getOptionValues("d")));
			// Check to see what type of reporting style the report will have
			if(cmd.hasOption("s"))				
				setRptStyle(cmd.getOptionValue("s"));
			// Check to see where the human-readable report will go
			if(cmd.hasOption("r"))
				setRptFile(new File(cmd.getOptionValue("r")));

		}
		catch(MissingOptionException moe) {
			System.err.println(moe.getMessage());
			System.exit(1);
		}
		catch(UnrecognizedOptionException uoe) {
			System.err.println(uoe.getMessage());
			System.exit(1);
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
	
	/**
	 * Get aliasing flag
	 * @return 'true' if aliasing is ON, 'false' otherwise
	 */
	public boolean getAlias() {
		return alias;
	}
	
	/**
	 * Set aliasing flag
	 * @param a 'false' if aliasing should be turned off, 'true' otherwise
	 */
	public void setAlias(boolean a) {
		alias = a;
	}
	
	/**
	 * Get data object validation flag
	 */
	public boolean getDataObj() {
		return dataObj;
	}
	
	/**
	 * Set data object flag
	 * @param d 'true' if data object validation is to be performed, 'false' otherwise
	 */
	public void setDataObj(boolean d) {
		dataObj = d;
	}

	/**
	 * Get a list of dictionary files passed into VTool
	 * @return a List object of dictionary file names
	 */
	public List getDictionaries() {
		return dictionaries;
	}
	
	/**
	 * Set the dictionary file names passed into VTool 
	 * @param d a List object of dictionary files
	 */
	public void setDictionaries(List d) {
		dictionaries = new ArrayList();
		//Transform all dictionary inputs to URL
		for(Iterator i = d.iterator(); i.hasNext();) {
			String dd = i.next().toString();
			try {
				if(isURL(dd))
					dictionaries.add(new URL(dd));
				else
					dictionaries.add(new File(dd).toURI().toURL());
			}catch(MalformedURLException uEx) {
				System.err.println(uEx.getMessage());
				System.exit(1);
			}
		}
	}
	
	/**
	 * Get flag status that determines whether to follow pointers found in a label
	 * @return 'true' to follow, 'false' otherwise
	 */
	public boolean getFollowPtrs() {
		return followPtrs;
	}
	
	/**
	 * Set the flag that determines whether to follow pointers found in a label
	 * @param f 'true' to follow, 'false' otherwise
	 */
	public void setFollowPtrs(boolean f) {
		followPtrs = f;
	}
	
	/**
	 * Get the paths to search for files referenced by pointers in a label.
	 * @return a start path
	 */
	public List getIncludePaths() {
		return includePaths;
	}
	
	/**
	 * Set the paths to search for files referenced by pointers. Default is to always look
	 * first in the same directory as the label, then search specified directories.
	 * @param i List of paths
	 */
	public void setIncludePaths(List i) {
		includePaths = i;
	}
	
	/**
	 * Get the file name that contains the list of directories and/or directory patterns to ignore
	 * during validation
	 * @return a list of directory names/patterns to exclude from validation
	 */
	public List getNoDirs() {
		return noDirs;
	}
	
	/**
	 * Set the flag to ignore specified directories
	 * @param f a text file containing a list of directories and/or directory patterns to ignore
	 * during validation. The names must be listed one name per line.
	 */
	public void setNoDirs(List f) {
		noDirs = f;
	}
	
	/**
	 * Get the file name that contains the list of files and/or file patterns to ignore during
	 * validation
	 * @return a list of files/file patterns to exclude from validation
	 */
	public List getNoFiles() {
		return noFiles;
	}
	
	/**
	 * Set the flag to ignore specified files
	 * @param f a list of files/file patterns to ignore during validation.
	 */
	public void setNoFiles(List f) {
		noFiles = f;
	}
	
	/**
	 * Get the machine-readable log file name
	 * @return log file
	 */
	public File getLogFile() {
		return logFile;
	}
	
	/**
	 * Set the file name for the machine-readable log
	 * @param f file name of the log
	 */
	public void setLogFile(File f) {
		logFile = f;
	}
	
	/**
	 * Get the report file name for the human-readable report
	 * @return Report file name
	 */
	public File getRptFile() {
		return rptFile;
	}
	
	/**
	 * Set the file for the human-readable report
	 * @param f file name
	 */
	public void setRptFile(File f) {
		rptFile = f;
	}
	
	/**
	 * Get the output style that was set for the validation report
	 * @return 'full' for a full report, 'sum' for a summary report or 'min' for minimal detail
	 */
	public String getRptStyle() {
		return rptStyle;
	}
	
	/**
	 * Set the output style for the report
	 * @param style 'sum' for a summary report, 'min' for a minimal report, and 'full' for a complete
	 * report
	 */
	public void setRptStyle(String style) {
		if( (style.equalsIgnoreCase("sum") == false) &&
				(style.equalsIgnoreCase("min") == false) &&
				(style.equalsIgnoreCase("full") == false) ) {
				throw new IllegalArgumentException("Invalid value entered for 's' flag." + 
													" Value can only be either 'full', 'sum', or 'min'");
		}
		rptStyle = style;
	}
	
	/**
	 * Get the progress reporting flag
	 * @return
	 */
	
	public boolean getProgress() {
		return progress;
	}
	
	/**
	 * Set the progress reporting flag
	 * @param p
	 */
	public void setProgress(boolean p) {
		progress = p;
	}
	
	/**
	 * Get the patterns to be matched when searching for files to validate in a directory
	 * @return a List object of patterns
	 */
	public List getRegexp() {
		return regexp;
	}
	
	/**
	 * Set the patterns flag
	 * @param p a List of patterns to be matched when searching for files to validate in a directory
	 */
	public void setRegexp(List e) {
		regexp = e;
	}
	
	/**
	 * Set the recursive flag
	 * @param r 'true' to recursively traverse down a directory and all its sub-directories, 'false' otherwise
	 */
	public void setRecursive(boolean r) {
		recursive = r;
	}
	
	/**
	 * Get the list of targets
	 * @return a List object of files, URLs, and/or directories
	 */
	public List getTargets() {
		return targets;
	}
	
	/**
	 * Set the targets flag
	 * @param t a List of files, URLs, and/or directories to be validated
	 */
	public void setTargets(List t) {
		targets = t;
	}
	
	/**
	 * Get the verbosity level
	 * @return an integer value where '0' is debug, '1' for info, '2' for warnings'
	 * and '3' for errors/fatal errors
	 */
	public short getVerbose() {
		return verbose;
	}
	
	/**
	 * Set the verbosity level and above to include in the reporting
	 * @param v '0' for debug, '1' for info, '2' for warnings, and '3' for errors/fatal errors
	 */
	public void setVerbose(short v) {
		verbose = v;
		if(verbose < 1 || verbose > 3) {
			throw new IllegalArgumentException("Invalid value entered for 'v' flag." + 
														" Valid values can only be 1, 2, or 3");
		}
/*		if(verbose == 0)
			severity = new String("Debug");*/
		else if(verbose == 1)
			severity = new String("Info");
		else if(verbose == 2)
			severity = new String("Warning");
		else if(verbose == 3)
			severity = new String("Severe");
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
			System.exit(1);
		}
		
		try {
			if(config.isEmpty())
				throw new Exception("Configuration file is empty: " + file.toString());
			if(config.containsKey(ALIAS))
				setAlias(config.getBoolean(ALIAS));
			if(config.containsKey(DATAOBJ))
				setDataObj(config.getBoolean(DATAOBJ));
			if(config.containsKey(DICT))
				setDictionaries(config.getList(DICT));
			if(config.containsKey(FOLLOW))
				setFollowPtrs(config.getBoolean(FOLLOW));
			if(config.containsKey(IGNOREDIR)) {
				setNoDirs(config.getList(IGNOREDIR));
				// Removes quotes surrounding each pattern being specified
				for(int i=0; i < noDirs.size(); i++)
					noDirs.set(i, noDirs.get(i).toString().replace('"',' ').trim());
			}
			if(config.containsKey(IGNOREFILE)) {
				setNoFiles(config.getList(IGNOREFILE));
				// Removes quotes surrounding each pattern being specified
				for(int i=0; i < noFiles.size(); i++)
					noFiles.set(i, noFiles.get(i).toString().replace('"',' ').trim());
			}
			if(config.containsKey(INCLUDES))
				setIncludePaths(config.getList(INCLUDES));
			if(config.containsKey(LOG))
				setLogFile(new File(config.getString(LOG)));
			if(config.containsKey(PROGRESS))
				setProgress(config.getBoolean(PROGRESS));
			if(config.containsKey(RECURSE))
				setRecursive(config.getBoolean(RECURSE));
			if(config.containsKey(REGEXP)) {
				setRegexp(config.getList(REGEXP));
				// Removes quotes surrounding each pattern being specified
				for(int i=0; i < regexp.size(); i++)
					regexp.set(i, regexp.get(i).toString().replace('"',' ').trim());
			}
			if(config.containsKey(REPORT))
				setRptFile(new File(config.getString(REPORT)));
			if(config.containsKey(STYLE))
				setRptStyle(config.getString(STYLE));
			if(config.containsKey(TARGET))
				setTargets(config.getList(TARGET));
			if(config.containsKey(VERBOSE))
				setVerbose(config.getShort(VERBOSE));
		} catch(ConversionException ce) {
			System.err.println(ce.getMessage());
			System.exit(1);
		} catch(Exception ex) {
			System.err.println(ex.getMessage());
			System.exit(1);
		}
	}
	
	/**
	 * Logs the various VTool command-line options into the logger
	 *
	 */
	public void logParams() {
		//TODO: Print out data object validation, standalone fragment validation
		log.log(new ToolsLogRecord(Level.CONFIG, "VTool Version             " + versionId));
		log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, "Target(s)                 " + targets));
		if(dictionaries != null)
			log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, "Dictionary file(s)        " + dictionaries));
		log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, "Aliasing                  " + alias));
		log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, "Directory Recursion       " + recursive));
		log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, "Follow Fragment Pointers  " + followPtrs));
		if(logFile != null)
			log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, "Log File                  " + logFile));
		if(rptFile != null)
			log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, "Report File               " + rptFile));
		if(rptStyle != null)
		    log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, "Report Style              " + rptStyle));
		if(severity != null)
			log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, "Severity Level            " + severity));
		if(includePaths != null)
			log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, "Include Path(s)           " + includePaths));
		if(config != null)
			log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, "Configuration File        " + config));
		if(regexp != null)
			log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, "Files Patterns            " + regexp));
		if(noDirs != null)
			log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, "Excluded Directories      " + noDirs));
		if(noFiles != null)
			log.log(new ToolsLogRecord(ToolsLevel.PARAMETER, "Excluded Files            " + noFiles));
	}
	
	/**
	 * Configures the java logging
	 *
	 */
	public void setupLogger() {
		Logger logger = Logger.getLogger("");
		logger.setLevel(Level.ALL);
		Handler []handler = logger.getHandlers();
		for(int i=0; i < logger.getHandlers().length; i++) 
			logger.removeHandler(handler[i]);
		
		if(logFile == null) {
			StreamHandler stream = new StreamHandler(System.out, new ToolsLogFormatter());
			logHandler = stream;
			logger.addHandler(stream);
			stream.setLevel(Level.ALL);
			log.log(new ToolsLogRecord(Level.INFO, "Log being directed to standard out. Report-related options will be ignored"));
			severity = null;
			rptFile = null;
			rptStyle = null;
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
				System.exit(1);
			} catch (IOException iEx) {
				System.err.println(iEx.getMessage());
				System.exit(1);
			}
		}
	}
	
	/**
	 * Determines whether a string is a URL
	 * 
	 * @param s
	 * @return
	 */
	private boolean isURL(String s) {	
		try {
			URL url = new URL(s);
		}
		catch(MalformedURLException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Parse the dictionary files.
	 * 
	 * @param dictionary a list of dictionary URLs
	 * @return a Dictionary object that includes all the dictionary information from
	 *  all the dictionary files passed in.
	 */
	public Dictionary readDictionaries(List dictionary) {
		Dictionary dict = null;
		URL dd = null;
		Iterator i = dictionary.iterator();
		
		try {
			//Parse the first dictionary
			dd = new URL(i.next().toString());
			dict = DictionaryParser.parse(dd, alias);
            log.log(new ToolsLogRecord(Level.CONFIG, "Dictionary version        \n" + dict.getInformation(), dd.toString()));
			log.log(new ToolsLogRecord(ToolsLevel.NOTIFICATION, "PASS", dd.toString()));
			//Parse the rest of the dictionaries
			while( i.hasNext() ) {
				dd = new URL(i.next().toString());
				dict.merge( DictionaryParser.parse(dd, alias), true );
				log.log(new ToolsLogRecord(Level.CONFIG, "Dictionary version        \n" + dict.getInformation(), dd.toString()));
				log.log(new ToolsLogRecord(ToolsLevel.NOTIFICATION, "PASS", dd.toString()));
			}
		} catch( MalformedURLException uex) {
			System.err.println(uex.getMessage());
			System.exit(1);
		} catch( IOException iex) {
			System.err.println(iex.getMessage());
			System.exit(1);
		} catch( gov.nasa.pds.tools.label.parser.ParseException pe) {
			log.log(new ToolsLogRecord(ToolsLevel.NOTIFICATION, "FAIL", dd.toString()));
			System.exit(1);
		}
		return dict;	
	}
	
	/**
	 * Sets up the include paths and flag to follow pointers in
	 * the parser
	 * @param parser
	 */
	private void setParserProps(LabelParser parser) {
		if(includePaths != null) {
			for( Iterator i = includePaths.iterator(); i.hasNext(); ) {
				String path = new String(i.next().toString());
				
				try {
					if(isURL(path))
						parser.addIncludePath(new URL(path));
					else
						parser.addIncludePath(new File(path).toURL());
				} catch (MalformedURLException f) {
					System.err.println("Cannot add file to include path: " + path);
					System.exit(1);
				}
				
			}
		}

		if(followPtrs == false)
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
		for(Iterator i1 = targets.iterator(); i1.hasNext();) {
			FileList fileList = new FileList();
			fileList = processTarget(i1.next().toString(), recursive);
			for(Iterator i2 = fileList.getFiles().iterator(); i2.hasNext();) {
				String target = i2.next().toString();				
				try {
					validateLabel(new URL(target), dict);
				}catch(MalformedURLException uEx) {
					System.err.println(uEx.getMessage());
					System.exit(1);
				}
			}
			if(!fileList.getDirs().isEmpty())
				validateLabels(fileList.getDirs(), dict);
		}
		
	}
	
	/**
	 * Process the specified target. A file list and directory list object are passed
	 * into the method in order to retrieve the list of files and sub-directories found
	 * as a result of visiting the target.
	 * 
	 * @param target The file or URL to process
	 * @param fileGen A FileListGenerator object that will contain the list of files found
	 * @param getSubDirs 'True' to look for sub-directories, 'false' otherwise.
	 */
	private FileList processTarget(String target, boolean getSubDirs) {		
		FileListGenerator fileGen = new FileListGenerator();
		FileList list = new FileList();
		fileGen.setFilters(regexp, noFiles, noDirs);
		
		try {
			list = fileGen.visitTarget(target, getSubDirs);
		} catch (MalformedURLException uEx) {
			System.err.println(uEx.getMessage());
			System.exit(1);
		} catch (IOException iEx) {
			System.err.println(iEx.getMessage());
			System.exit(1);
		} catch (BadLocationException bEx) {
			System.err.println(bEx.getMessage());
			System.exit(1);
		}
		return list;
	}
	
	/**
	 * Validate a label file. Can perform both syntactic and semantic validation.
	 * 
	 * @param files The URL of the file to be validated
	 * @param dict a Dictionary object needed for semantic validation. If null,
	 *             only syntactic validation will be performed.
	 */
	
	public void validateLabel(URL file, Dictionary dict) {		
		LabelParserFactory factory = LabelParserFactory.getInstance();
		LabelParser parser = factory.newLabelParser();
		setParserProps(parser);
		
		if(progress)
			showProgress(file);
		try {
			if(dict == null)
				parser.parse(file);
			else
				parser.parse(file, dict);
		} catch (gov.nasa.pds.tools.label.parser.ParseException pEx) {
			log.log(new ToolsLogRecord(ToolsLevel.NOTIFICATION, "SKIP", file.toString()));
			return;
		}catch (IOException iEx) {
			System.err.println(iEx.getMessage());
			System.exit(1);			
		}
		log.log(new ToolsLogRecord(ToolsLevel.NOTIFICATION, "PASS", file.toString()));
	}
	
	/**
	 * When progress reporting is enabled, this method will print out the current directory
	 * being validated and represent each file being validated by a an asterisk.
	 * 
	 * @param file The URL of the file being validated
	 */
	public void showProgress(URL file) {
		URL dir = null;
		try {
			dir = new URL(file.toString().substring(0, file.toString().lastIndexOf("/")));
		} catch(MalformedURLException uEx) {
			System.err.println(uEx.getMessage());
			System.exit(1);
		}
		
		if(dir.toString().equals(currDir)) {
			System.err.print("*");
		}
		else {
			currDir = new String(dir.toString());
			System.err.println("\nValidating file(s) in: " + currDir);
			System.err.print("*");
		}
	}
	
	/**
	 * Creates a human-readable report
	 * 
	 * @param log The xml log
	 * @param report Where the human-readable report will be written to. If null, then
	 *        it goes to standard out.
	 * @param level The severity level to include in the warning. Can be "INFO", "WARNING", or "SEVERE".
	 * @param style The reporting style to generate. Can be either "full", "sum", or "min" for a
	 *        full, summary, or minimal report, respectively.
	 */
	public void doReporting(File log, File report, String level, String style) {
		String rptType = null;
		
		if( (level.equalsIgnoreCase("info") == false) && 
            (level.equalsIgnoreCase("warning") == false) &&
            (level.equalsIgnoreCase("severe") == false) )
				throw new IllegalArgumentException("Invalid severity level: " + level + ". Must be 'info', 'warning', or 'severe'.");
		
		if(style.equalsIgnoreCase("full"))
			rptType = new String("report-full.xsl");
		else if(style.equalsIgnoreCase("sum"))
			rptType = new String("report-summary.xsl");
		else if(style.equalsIgnoreCase("min"))
			rptType = new String("report-minimal.xsl");
		else
			throw new IllegalArgumentException("Invalid style specified: " + style + ". Must be 'full', 'sum' or 'min'");
		
		try {
			if(report == null)
				generateReport(log, rptType, level.toUpperCase(), System.out);
			else
				generateReport(log, rptType, level.toUpperCase(), new FileOutputStream(report));
		} catch(TransformerException tEx) {
			System.err.println(tEx.getMessage());
			System.exit(1);
		} catch (FileNotFoundException fEx) {
			System.err.println(fEx.getMessage());
			System.exit(1);
		}
	}
	
    private void generateReport(File logFile, String report, String level, OutputStream output) throws TransformerException {
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
	 * The setupLogger method is called after parsing and querying the command-line
	 * since we can accept a configuration file as input and by rule, anything on
	 * the command-line overrides parameters set in the configuration file. So, by
	 * design, VTool would not know where to direct its logs until after it queries
	 * the command-line.  
	 * 
	 * @param argv Arguments passed on the command-line
	 */
	public static void main(String[] argv) {
		VTool vtool = new VTool();
		Dictionary mainDictionary = null;

		if(argv.length == 0) {
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
		// Log parameter settings
		vtool.logParams();	
		
		if(vtool.dictionaries != null) {
			mainDictionary = vtool.readDictionaries(vtool.dictionaries);
			vtool.validateLabels(vtool.targets, mainDictionary);
		}
		else 
			vtool.validateLabels(vtool.targets, null);
		
		vtool.closeHandler();
		
		if(vtool.logFile != null)
			vtool.doReporting(vtool.logFile, vtool.rptFile, vtool.severity, vtool.rptStyle);
		
		System.exit(0);
	}
}
