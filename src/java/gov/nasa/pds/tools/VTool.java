//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools;

import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.dict.parser.DictionaryParser;
import gov.nasa.pds.tools.file.FileListGenerator;
import gov.nasa.pds.tools.label.parser.LabelParser;
import gov.nasa.pds.tools.label.parser.LabelParserFactory;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.text.BadLocationException;

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
import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * @author pramirez
 * @version $Revision$
 * 
 * Class to replace LVTool functionality
 * 
 */
public class VTool {
	
	final private String version_id = "0.3.0"; 
	private static Logger log = Logger.getLogger(new VTool().getClass());
	
	private Options options;
	private CommandLineParser parser;
	private CommandLine cmd;
	
	//TODO: Flags to be implemented: partial (-f,--force), data object(-O,--no-obj),
	//progress(-p,--show-progress), alias(-a,--alias), include (-i,--include),
	//xml output(-x,--xml), reporting style (-s,--output-style) 
	private boolean alias;
	private File config;
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
	private File output;
	private String outputFmt;
	private short verbose;
	private boolean xml;
	
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
		output = null;
		outputFmt = "full";
		xml = false;
		options = new Options();
		parser = new GnuParser();
	}
	
	/**
	 * Show the version and disclaimer notice for VTool 
	 *
	 */	
	public void showVersion() {
		System.out.println("PDS Validation Tool (VTool) " + version_id);
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
	private void showHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(37, "VTool", null, options, null);
		System.exit(0);
	}
	
	/**
	 * Builds the set of options that are available for VTool
	 */
	private void buildOpts() {
		options.addOption("a", "alias", false, "Enable aliasing");
		options.addOption("F", "no-follow", false, "Do not follow ^STRUCTURE pointers in a label");
		options.addOption("h", "help", false, "Display usage");
		options.addOption("O", "no-obj", false, "Do not perform data object validation");
//		options.addOption("f", "force", false, "Force VTool to validate a label fragment");
		options.addOption("l", "local", false, "Validate files only in the input directory rather than " + 
				                               "recursively traversing down the subdirectories.");
		options.addOption("p", "progress", false, "Enable progress reporting");
		options.addOption("V", "version", false, "Display VTool version");
		options.addOption("x", "xml", false, "Output the report in XML format");
		
		
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
		OptionBuilder.withLongOpt("ignore-files");
		OptionBuilder.withDescription("Specify a list of files and/or file patterns to ignore from validation. Each pattern " + 
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
		OptionBuilder.withDescription("Specify files or file patterns to look for when validating a directory. " +
				                      "Each Pattern must be surrounded by quotes. (i.e. -e \"*.LBL\" \"*FMT\")");
		OptionBuilder.hasArgs();
		OptionBuilder.withArgName("patterns");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("e"));
		
		// Option to specify a path to the Pointer files		
		OptionBuilder.withLongOpt("include");
		OptionBuilder.withDescription("Specify the path(s) to search for files referenced by pointers in a label. Separate each with a space. " + 
															"Default is to always look at the same directory as the label");
		OptionBuilder.hasArgs();
		OptionBuilder.withArgName("paths");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("I"));

		//Option to specify a file containing a list of directories to ignore
		OptionBuilder.withLongOpt("ignore-dirs");
		OptionBuilder.withDescription("Specify a list of directories and/or directory patterns to ignore. " + 
				                      "Each listing must be surrounded by quotes. (i.e. -D \"EXTRAS\" \"LABEL\")");
		OptionBuilder.hasArgs();
		OptionBuilder.withArgName("patterns");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("D"));
		
		// Option to specify the report file name
		OptionBuilder.withLongOpt("output");
		OptionBuilder.withDescription("Specify the file name for the report. Default is to print to the terminal");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("file");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("o"));

		// Option to specify how detail the reporting should be
		OptionBuilder.withLongOpt("output-style");
		OptionBuilder.withDescription("Specify the level of detail for the reporting. " +
										"Valid values are 'full' for full details, 'min' for minimal detail " +
										"and 'sum' for a summary. Default is set to 'full'");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("full|sum|min");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("s"));
		
		// Option to specify the severity level and above
		OptionBuilder.withLongOpt("verbose");
		OptionBuilder.withDescription("Specify the message severity level and above to include in the reporting: " + 
				                             "(0=Debug, 1=Info, 2=Warning, 3=Error or Fatal). " + 
				                             "Default is Info and above (level 1)");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("0|1|2|3");
		OptionBuilder.withType(short.class);
		options.addOption(OptionBuilder.create("v"));

	}
	
	/**
	 * Parses the command line
	 * @param argv arguments given on the command-line
	 */
	private void parseLine(String[] argv) {
		try {
			cmd = parser.parse(options, argv);
		}
		catch( ParseException exp ) {
			System.err.println( "Command line parser failed.\n\nReason: " + exp.getMessage() );
			System.exit(1);
		}
	}
	
	/** 
	 * Queries the command line of VTool 
	 *
	 */
	private void queryCmdLine() {

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
				config = new File(cmd.getOptionValue("c"));
				readConfigFile(config);
			}
			// Check for the 'o' flag to specify a file where the reporting will be written to
			if(cmd.hasOption("o"))
				setOutput(new File(cmd.getOptionValue("o")));
			// verbose flag
			if(cmd.hasOption("v")) {
				try {
					setVerbose(Short.parseShort(cmd.getOptionValue("v")));
				}
				catch( NumberFormatException ne) {
					throw new Exception("Problems parsing value set for the 'v' flag: " + cmd.getOptionValue("v"));
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
			if(cmd.hasOption("l"))
				setRecursive(false);
			// Check to see if regular expressions were set
			if(cmd.hasOption("e"))
				setRegexp(Arrays.asList(cmd.getOptionValues("e")));
			// Check to see if the report will be in xml format
			if(cmd.hasOption("x"))
				setXml(true);
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
				setOutputFmt(cmd.getOptionValue("s"));

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
		dictionaries = d;
	}
	
	/**
	 * Get flag status that determines whether to follow ^STRUCTURE pointers in validation
	 * @return 'true' to follow, 'false' otherwise
	 */
	public boolean getFollowPtrs() {
		return followPtrs;
	}
	
	/**
	 * Set the flag that determines whether to follow ^STRUCTURE pointers in validation
	 * @param f 'true' to follow, 'false' otherwise
	 */
	public void setFollowPtrs(boolean f) {
		followPtrs = f;
	}
	
	/**
	 * Get the starting path to search for the non-STRUCTURE pointers
	 * @return a start path
	 */
	public List getIncludePaths() {
		return includePaths;
	}
	
	/**
	 * Set the starting path to search for non-STRUCTURE pointers. Default directory is
	 * the current working directory
	 * @param i a start path
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
	 * Get the output file name
	 * @return an output file name for the validation report
	 */
	public File getOutput() {
		return output;
	}
	
	/**
	 * Set the output file name where the validation report will go. Default is the standard out.
	 * @param o a report file name
	 */
	public void setOutput(File o) {
		output = o;
	}
	
	/**
	 * Get the output style that was set for the validation report
	 * @return 'full' for a full report, 'sum' for a summary report or 'min' for minimal detail
	 */
	public String getOuputFmt() {
		return outputFmt;
	}
	
	/**
	 * Set the output style for the report
	 * @param f 'sum' for a summary report, 'min' for a minimal report, and 'full' for a complete
	 * report
	 */
	public void setOutputFmt(String f) {
		if( (f.equalsIgnoreCase("sum") == false) &&
				(f.equalsIgnoreCase("min") == false) &&
				(f.equalsIgnoreCase("full") == false) ) {
				throw new IllegalArgumentException("Invalid value entered for 'od' flag." + 
													" Value can only be either 'full', 'sum', or 'min'");
		}
		outputFmt = f;
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
		if(verbose < 0 || verbose > 3) {
			throw new IllegalArgumentException("Invalid value entered for 'v' flag." + 
														" Valid values can only be 0, 1, 2, or 3");
		}
		if(verbose == 0) 
			Logger.getRootLogger().setLevel(Level.DEBUG);
		else if(verbose == 1)
			Logger.getRootLogger().setLevel(Level.INFO);
		else if(verbose == 2)
			Logger.getRootLogger().setLevel(Level.WARN);
		else if(verbose == 3)
			Logger.getRootLogger().setLevel(Level.ERROR);
	}
	
	/**
	 * Get the XML flag
	 * @return 'true' if format will be printed in xml, 'false' otherwise
	 */
	public boolean getXml() {
		return xml;
	}
	
	/**
	 * Set the xml flag to determine whether to format the report in xml
	 * @param x 'true' to set report format in xml, 'false' otherwise
	 */
	public void setXml(boolean x) {
		xml = x;
	}
	
	/**
	 * Reads a configuration file to set the default behaviors for VTool.
	 * Flags set on the command-line will override flags set in the 
	 * configuration file
	 * 
	 * @param file a file containing keyword/value statements
	 */
	
	public void readConfigFile(File file) {
		Configuration config = null;
		Logger configlog = Logger.getLogger(ConfigurationUtils.class);
		configlog.addAppender(new ConsoleAppender(new PatternLayout("%m%n")));
		configlog.setAdditivity(false);
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
				throw new Exception("Configuration file is empty or does not exist: " + file.toString());
			if(config.containsKey("vtool.output"))
				setOutput(new File(config.getString("vtool.output")));
			if(config.containsKey("vtool.verbose"))
				setVerbose(config.getShort("vtool.verbose"));
			if(config.containsKey("vtool.dataobjects"))
				setDataObj(config.getBoolean("vtool.dataobjects"));
			if(config.containsKey("vtool.dict"))
				setDictionaries(config.getList("vtool.dict"));
			if(config.containsKey("vtool.follow"))
				setFollowPtrs(config.getBoolean("vtool.follow"));
			if(config.containsKey("vtool.ignorefiles")) {
				setNoFiles(config.getList("vtool.ignorefiles"));
				// Removes quotes surrounding each pattern being specified
				for(int i=0; i < noFiles.size(); i++)
					noFiles.set(i, noFiles.get(i).toString().replace('"',' ').trim());
			}
			if(config.containsKey("vtool.ignoredirs")) {
				setNoDirs(config.getList("vtool.ignoredirs"));
				// Removes quotes surrounding each pattern being specified
				for(int i=0; i < noDirs.size(); i++)
					noDirs.set(i, noDirs.get(i).toString().replace('"',' ').trim());
			}
			if(config.containsKey("vtool.includepaths"))
				setIncludePaths(config.getList("vtool.includepaths"));
			if(config.containsKey("vtool.outputstyle"))
				setOutputFmt(config.getString("vtool.outputstyle"));
			if(config.containsKey("vtool.patterns")) {
				setRegexp(config.getList("vtool.patterns"));
				// Removes quotes surrounding each pattern being specified
				for(int i=0; i < regexp.size(); i++)
					regexp.set(i, regexp.get(i).toString().replace('"',' ').trim());
			}
			if(config.containsKey("vtool.recursive"))
				setRecursive(config.getBoolean("vtool.recursive"));
			if(config.containsKey("vtool.target"))
				setTargets(config.getList("vtool.target"));
			if(config.containsKey("vtool.xmloutput"))
				setXml(config.getBoolean("vtool.xmloutput"));
		} catch(ConversionException ce) {
			System.err.println(ce.getMessage());
			System.exit(1);
		} catch(Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Prints DEBUG messages of the various VTool command-line options
	 *
	 */
	private void printDebug() {
		log.debug("Target(s): " + targets);
		log.debug("Dictionary file(s): " + dictionaries);
		log.debug("Output file set to: " + output);
		log.debug("Message severity level set to: " + verbose);
		log.debug("Files to look for: " + regexp);
		log.debug("Excluded directories: " + noDirs);
		log.debug("Excluded files: " + noFiles);
		log.debug("Alias flag set to: " + alias);
		log.debug("Data object flag set to: " + dataObj);
		log.debug("Follow pointers flag set to: " + followPtrs);
		log.debug("Included path(s):" + includePaths);
		log.debug("Ouptut format set to: " + outputFmt);
		log.debug("Recursive flag set to: " + recursive);
		log.debug("Xml flag set to: " + xml);
	}
	
	/**
	 * Configures the logger from the log4j class
	 *
	 */
	
	private void setupLogger() {

		if(output != null) {
			try {
				BasicConfigurator.configure(new FileAppender(new PatternLayout("%-5p %m%n"), output.toString(), false));
			}
			catch (IOException ioe) {
				System.out.println( ioe.getMessage() );
				System.exit(1);
			}
		}
		else
			BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("%-5p %m%n")));
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
	 * Read and parse the dictionary file(s) passed into the VTool command line
	 * 
	 * @param dictionary a List object containing the data dictionary files
	 * @return a Dictionary object that includes all the dictionary information from
	 *  all the dictionary files passed in. Returns null if dictionary has a null value.
	 */
	
	public Dictionary readDictionaries(List dictionary) {
		Dictionary dict = null;
		String dd = null;
		Iterator i = dictionary.iterator();
		
		dd = new String( i.next().toString() );
		
		try {
			if(isURL(dd))
				dict = DictionaryParser.parse(new URL(dd));
			else
				dict = DictionaryParser.parse(new File(dd).toURL());

			while( i.hasNext() ) {
				dd = new String(i.next().toString());
				if(isURL(dd))
					dict.merge( DictionaryParser.parse(new URL(dd)), true );
				else
					dict.merge( DictionaryParser.parse(new File(dd).toURL()), true );
			}
		} catch( MalformedURLException uex) {
			System.err.println("Dictionary file does not exist: " + dd);
			System.exit(1);
		} catch( IOException iex) {
			System.err.println(iex.getMessage());
			System.exit(1);
		} catch( gov.nasa.pds.tools.label.parser.ParseException pe) {
			System.err.println("Problems parsing Dictionary file");
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
	 * Validate labels found within the specified targets. If the target
	 * is a directory, this method will validate all labels found within
	 * the directory and its sub-directories. If recursion is turned OFF,
	 * then sub-directories will not be looked into.
	 * 
	 * @param targets a list of files, directories, and/or URLs
	 * @param dict the dictionary file
	 */
	
	public void validateLabels(List targets, Dictionary dict) {
		for(Iterator i1 = targets.iterator(); i1.hasNext();) {
			List fileList = new ArrayList();
			List dirList = new ArrayList();
			processTarget(i1.next().toString(), recursive, fileList, dirList);
			
			for(Iterator i2 = fileList.iterator(); i2.hasNext();) {
				String target = i2.next().toString();
				
				try {
					validateLabel(new URL(target), dict);
				}catch(MalformedURLException uEx) {
					System.err.println(uEx.getMessage());
					System.exit(1);
				}
			}
			if(!dirList.isEmpty())
				validateLabels(dirList, dict);
		}
		
	}
	
	/**
	 * Process the specified target. A file list and directory list object are passed
	 * into the method in order to retrieve the list of files and sub-directories found
	 * as a result of visiting the target.
	 * 
	 * @param target The file or URL to process
	 * @param files A list of files found after processing the target
	 * @param dirs A list of sub directories found after processing the target 
	 */
	private void processTarget(String target, boolean getSubDirs, List files, List dirs) {
		
		FileListGenerator fileGen = new FileListGenerator();
		fileGen.setFilters(regexp, noFiles, noDirs);
		
		try {
			fileGen.visitTarget(target, getSubDirs);
		} catch (HttpException hEx) {
			System.err.println(hEx.getMessage());
			System.exit(1);
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
		
		if( !fileGen.getFiles().isEmpty() )
			files.addAll(fileGen.getFiles());
		
		if( !fileGen.getSubDirs().isEmpty() )
			dirs.addAll(fileGen.getSubDirs());
	}
	
	/**
	 * Validate a label file. Can perform both syntactic and semantic validation.
	 * 
	 * @param files a list of label files to be validated
	 * @param dict a Dictionary object needed for semantic validation. If null,
	 *             only syntactic validation will be performed.
	 */
	
	public void validateLabel(URL file, Dictionary dict) {
		
		LabelParserFactory factory = LabelParserFactory.getInstance();
		LabelParser parser = factory.newLabelParser();
		setParserProps(parser);
		
		System.out.println("\nValidating: " + file);
		
		try {
			if(dict == null)
				parser.parse(file);
			else
				parser.parse(file, dict);
		} catch (gov.nasa.pds.tools.label.parser.ParseException pEx) {
			return;
		}catch (IOException iEx) {
			System.err.println(iEx.getMessage());
			System.exit(1);			
		}
	}
	
	public static void main(String[] argv) {
		VTool vtool = new VTool();
		Dictionary mainDictionary = null;
		vtool.setVerbose(Short.parseShort("1")); // Set severity level to INFO and above
		
		if(argv.length == 0) {
			System.out.println("\nType 'VTool -h' for usage");
			return;
		}
		// Define options
		vtool.buildOpts();
		// Parse the command line
		vtool.parseLine(argv);
		// Query the command line
		vtool.queryCmdLine();
		// Setup the logger
		vtool.setupLogger();
		// Print debug messages if severity level is set to '0'
		vtool.printDebug();	
		
		if(vtool.dictionaries != null) {
			mainDictionary = vtool.readDictionaries(vtool.dictionaries);
			vtool.validateLabels(vtool.targets, mainDictionary);
		}
		else 
			vtool.validateLabels(vtool.targets, null);
 
		System.exit(0);
	}
}
