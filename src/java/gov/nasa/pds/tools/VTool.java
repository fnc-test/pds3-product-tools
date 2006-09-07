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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
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
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
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
	
	private boolean alias;
	private File config;
	private boolean dataObj;
	private List dictionaries;
	private List noFile;
	private List noDir;
	private List files;
	private boolean followPtrs;
	private File includePath;
	private int maxErrors;
	private boolean partial;
	private List patterns;
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
		noFile = null;
		noDir = null;
		followPtrs = true;
		targets = null;
		files = null;
		includePath = null;
		maxErrors = 300;
		partial = false;
		patterns = null;
		recursive = true;
		output = null;
		outputFmt = "full";
		verbose = 2;
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
		options.addOption("a", "alias", false, "Enable aliasing feature when validating label file(s)");
		options.addOption("F", "no-follow", false, "Do not follow ^STRUCTURE pointers in a label");
		options.addOption("h", "help", false, "Display usage");
		options.addOption("OBJ", "no-obj", false, "Do not perform data object validation");
//		options.addOption("p", "partial", false, "Validate as a partial label file");
		options.addOption("l", "local", false, "Validate files only in the input directory rather than " + 
				                               "recursively traversing down the subdirectories.");	
		options.addOption("V", "version", false, "Display VTool version");
		options.addOption("x", "xml-output", false, "Output the report in XML format");
		
		
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
		OptionBuilder.withDescription("Specify the Planetary Science Data Dictionary full file name " +
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
		OptionBuilder.withArgName("file");
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
		OptionBuilder.withLongOpt("pattern");
		OptionBuilder.withDescription("Specify a pattern to match against the input directory to be validated. " +
				                      "Each Pattern must be surrounded by quotes. (i.e. -p \"*.LBL\" \"*FMT\")");
		OptionBuilder.hasArgs();
		OptionBuilder.withArgName("expressions");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("p"));
		
		// Option to specify a path to the Pointer files		
		OptionBuilder.withLongOpt("include");
		OptionBuilder.withDescription("Specify the starting path to search for pointer files. " + 
															"Default is the directory of the label being validated");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("path");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("I"));

		//Option to specify a file containing a list of directories to ignore
		OptionBuilder.withLongOpt("ignore-dir");
		OptionBuilder.withDescription("Specify a text file containing a list of directories and/or directory patterns to ignore");
		OptionBuilder.hasArgs();
		OptionBuilder.withArgName("file");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("D"));
		
		// Option to specify the maximum number of ERROR type messages that will be printed to the report
		OptionBuilder.withLongOpt("max-errors");
		OptionBuilder.withDescription("Specify the maximum number of ERROR type messages that VTool will " +
									"print to the report file. Default is 300 errors");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("integer");
		OptionBuilder.withType(int.class);
		options.addOption(OptionBuilder.create("m"));
		
		// Option to specify the report file name
		OptionBuilder.withLongOpt("output");
		OptionBuilder.withDescription("Specify the file name for the report. Default is to print to the terminal");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("file");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("o"));

		// Option to specify how detail the reporting should be
		OptionBuilder.withLongOpt("output-detail");
		OptionBuilder.withDescription("Specify the level of detail for the reporting. " +
										"Valid values are 'full' for full details, 'min' for minimal detail " +
										"and 'sum' for a summary. Default is set to 'full'");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("full|sum|min");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("od"));
		
		// Option to specify the severity level and above
		OptionBuilder.withLongOpt("verbose");
		OptionBuilder.withDescription("Specify the message severity level and above to include in the reporting: " + 
				                             "(0=Debug, 1=Info, 2=Warning, 3=Error or Fatal). " + 
				                             "Default is Warnings and above (level 2)");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("0|1|2|3");
		OptionBuilder.withType(short.class);
		options.addOption(OptionBuilder.create("v"));

	}
	
	/**
	 * Parses the command line
	 * @param argv
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
			// verbose flag
			if(cmd.hasOption("v")) {
				try {
					setVerbose(Short.parseShort(cmd.getOptionValue("v")));
				}
				catch( Exception e) {
					throw new NumberFormatException("Problems parsing value set for the 'v' flag: " + cmd.getOptionValue("v"));
				}
		
			}
			// Check for the 'o' flag to specify a file where the reporting will be written to
			// This flag must be queried before we can configure the logging
			if(cmd.hasOption("o")) {
				setOutput(new File(cmd.getOptionValue("o")));
				setFileLogger();	
			}
			// Check if the -t flag was set in either the config file or the command line
			if(cmd.hasOption("t")) {
				setTargets(Arrays.asList(cmd.getOptionValues("t")));
			}
			else if(targets == null)
				throw new MissingOptionException("The 't' flag is required or must be set in the configuration file");
			
			// Check to see if aliasing is turned on
			if(cmd.hasOption("a"))
				setAlias(false);
			// Check for the flag that indicates whether to follow ^STRUCTURE pointers
			if(cmd.hasOption("F"))
				setFollowPtrs(false);
			// Check for the include path to indicate the starting path to search for 
			// non ^STRUCTURE pointers
			if(cmd.hasOption("I"))
				setIncludePath(new File(cmd.getOptionValue("I")));			
			// Check to see if data object validation is set
			if(cmd.hasOption("OBJ")) 
				setDataObj(false);
			// Check to see if VTool will not recurse down a directory tree
			if(cmd.hasOption("l"))
				setRecursive(false);
			// Check to see if patterns were set
			if(cmd.hasOption("p"))
				setPatterns(Arrays.asList(cmd.getOptionValues("p")));
			// Check to see if the report will be in xml format
			if(cmd.hasOption("x"))
				setXml(true);
			// Check to get file that contains file patterns to ignore
			if(cmd.hasOption("X"))
				setNoFile(Arrays.asList(cmd.getOptionValues("X")));
			// Check to get file that contains directory patterns to ignore
			if(cmd.hasOption("D"))
				setNoDir(Arrays.asList(cmd.getOptionValues("D")));
			// Check to get the dictionary file(s) 
			if(cmd.hasOption("d"))
				setDictionaries(Arrays.asList(cmd.getOptionValues("d")));
			if(cmd.hasOption("m")) {
				try {
					setMaxErrors(Integer.parseInt(cmd.getOptionValue("m")));
				}
				catch( Exception e ) {
					throw new NumberFormatException("Problems parsing value set for 'm' flag: " + cmd.getOptionValue("m"));
				}
			}
			// Check to see what type of reporting style the report will have
			if(cmd.hasOption("od"))				
				setOutputFmt(cmd.getOptionValue("od"));

		}
		catch(NumberFormatException nfe) {
			System.out.println(nfe.getMessage());
			System.exit(1);
		}
		catch(IllegalArgumentException iae) {
			System.out.println(iae.getMessage());
			System.exit(1);
		}
		catch(MissingOptionException moe) {
			System.out.println(moe.getMessage());
			System.exit(1);
		}
		catch(UnrecognizedOptionException uoe) {
			System.out.println(uoe.getMessage());
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
		printDebug("Set alias flag");
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
		printDebug("Set data object flag");
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
		printDebug("Retrieved " + d.size() + " dictionary file(s)");
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
	public File getIncludePath() {
		return includePath;
	}
	
	/**
	 * Set the starting path to search for non-STRUCTURE pointers. Default directory is
	 * the current working directory
	 * @param i a start path
	 */
	public void setIncludePath(File i) {
		includePath = i;
	}
	
	/**
	 * Get the maximum errors allowed to be reported in the validation
	 * @return an integer value
	 */
	public int getMaxErrors() {
		return maxErrors;
	}
	
	/**
	 * Set the maximum errors allowed to be reported in VTool before quitting
	 * @param m an integer value
	 */
	public void setMaxErrors(int m) {
		if( m <= 0 )
			throw new IllegalArgumentException( "Max Errors Value must be a positive integer number");
		
		maxErrors = m;
		printDebug("Max error messages set to: " + maxErrors);
	}
	
	/**
	 * Get the file name that contains the list of directories and/or directory patterns to ignore
	 * during validation
	 * @return a list of directory names/patterns to exclude from validation
	 */
	public List getNoDir() {
		return noDir;
	}
	
	/**
	 * Set the flag to ignore specified directories
	 * @param f a text file containing a list of directories and/or directory patterns to ignore
	 * during validation. The names must be listed one name per line.
	 */
	public void setNoDir(List f) {
		noDir = f;
		printDebug("Set noDir: " + noDir);
	}
	
	/**
	 * Get the file name that contains the list of files and/or file patterns to ignore during
	 * validation
	 * @return a list of files/file patterns to exclude from validation
	 */
	public List getNoFile() {
		return noFile;
	}
	
	/**
	 * Set the flag to ignore specified files
	 * @param f a list of files/file patterns to ignore during validation.
	 */
	public void setNoFile(List f) {
		noFile = f;
		printDebug("Set noFile: " + noFile);
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
		printDebug("Ouptut format set to: " + outputFmt);
	}
	
	/**
	 * Get the patterns to be matched when searching for files to validate in a directory
	 * @return a List object of patterns
	 */
	public List getPatterns() {
		return patterns;
	}
	
	/**
	 * Set the patterns flag
	 * @param p a List of patterns to be matched when searching for files to validate in a directory
	 */
	public void setPatterns(List p) {
		printDebug("Set patterns");
		patterns = p;
	}
	
	/**
	 * Set the recursive flag
	 * @param r 'true' to recursively traverse down a directory and all its sub-directories, 'false' otherwise
	 */
	public void setRecursive(boolean r) {
		printDebug("Set recursive flag");
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
		printDebug("Set xml flag");
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
			System.out.println(ce.getMessage());
			System.exit(1);
		}
		
		try {
			if(config.isEmpty())
				throw new Exception("Configuration file is empty or does not exist: " + file.toString());
			if(config.containsKey("vtool.verbose"))
				setVerbose(config.getShort("vtool.verbose"));
			if(config.containsKey("vtool.output")) {
				setOutput(new File(config.getString("vtool.output")));
				setFileLogger();
			}
			if(config.containsKey("vtool.dataobjects"))
				setDataObj(config.getBoolean("vtool.dataobjects"));
			if(config.containsKey("vtool.dict"))
				setDictionaries(config.getList("vtool.dict"));
			if(config.containsKey("vtool.follow"))
				setFollowPtrs(config.getBoolean("vtool.follow"));
			if(config.containsKey("vtool.ignorefiles")) {
				setNoFile(config.getList("vtool.ignorefiles"));
				// Removes quotes surrounding each pattern being specified
				for(int i=0; i < noFile.size(); i++)
					noFile.set(i, noFile.get(i).toString().replace('"',' ').trim());
			}
			if(config.containsKey("vtool.includepath"))
				setIncludePath(new File(config.getString("vtool.includepath")));
			if(config.containsKey("vtool.outputformat"))
				setOutputFmt(config.getString("vtool.outputformat"));
			if(config.containsKey("vtool.patterns")) {
				setPatterns(config.getList("vtool.patterns"));
				// Removes quotes surrounding each pattern being specified
				for(int i=0; i < patterns.size(); i++)
					patterns.set(i, patterns.get(i).toString().replace('"',' ').trim());
			}
			if(config.containsKey("vtool.recursive"))
				setRecursive(config.getBoolean("vtool.recursive"));
			if(config.containsKey("vtool.target"))
				setTargets(config.getList("vtool.target"));
			if(config.containsKey("vtool.xmloutput"))
				setXml(config.getBoolean("vtool.xmloutput"));
		} catch(ConversionException ce) {
			System.out.println(ce.getMessage());
			System.exit(1);
		} catch(Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Configures the logger from the log4j class to print to a file
	 *
	 */
	
	private void setFileLogger() {
		BasicConfigurator.resetConfiguration();
		if(output != null) {
			try {
				BasicConfigurator.configure(new FileAppender(new PatternLayout("%-5p %m%n"), output.toString(), false));
			}
			catch (IOException ioe) {
				System.out.println( ioe.getMessage() );
				System.exit(1);
			}
		}
	}
	
	/**
	 * Print debugging statement if debug flag is set
	 * @param s
	 */
	
	private void printDebug(String s) {
		if(verbose == 0) 
			log.debug(s);
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
		File dd = null;
		Iterator i = dictionary.iterator();
		
		printDebug( "Parsing dictionary file: " + dictionary.get(0) );
		dd = new File( i.next().toString() );
		
		try {
			dict = DictionaryParser.parse( dd.toURL() );

			while( i.hasNext() ) {
				dd = new File( i.next().toString() );
				printDebug("Parsing dictionary file: " + dd);
				dict.merge( DictionaryParser.parse(dd.toURL()), true );
			}
		} catch( MalformedURLException uex) {
			System.out.println("Dictionary file does not exist: " + dd);
			System.exit(1);
		} catch( IOException iex) {
			System.out.println(iex.getMessage());
			System.exit(1);
		} catch( gov.nasa.pds.tools.label.parser.ParseException pe) {
			System.out.println("Problems parsing Dictionary file");
			System.exit(1);
		}

		return dict;
		
	}
	
	/**
	 * Read and parse the label files passed into the VTool command line.
	 * Performs both syntactic and semantic validation.
	 * 
	 * @param files a List of label files to be validated
	 * @param dict a Dictionary object needed for semantic validation. If null,
	 *             only syntactic validation will be performed.
	 */
	
	public void validateLabels(List files, Dictionary dict) {
		String target;
		URL url = null;
		LabelParserFactory factory = LabelParserFactory.getInstance();
		LabelParser parser = factory.newLabelParser();
/*		
		if(includePath != null) {
			try {
				parser.addIncludePath(includePath.toURL());
			}
			catch(MalformedURLException e) {
				System.out.println(e.getMessage());
				System.exit(1);
			}
		}
*/
		if(followPtrs == false)
			parser.getProperties().setProperty("parser.pointers", "false");
		
		for( Iterator i = files.iterator(); i.hasNext(); ) {
			target = new String( i.next().toString());
			
			try {
				if(new File(target).isFile() || new File(target).isDirectory())
					url = new File(target).toURL();
				else 
					url = new URL(target);
			}
			catch(MalformedURLException mex) {
				System.out.println("file/URL does not exist: " + target);
				System.exit(1);
			}
			
			System.out.println("\nValidating " + target);
			
			try {
				if(dict == null)
					parser.parse(url);
				else
					parser.parse(url,dict);
			} catch(IOException iex) {
				System.out.println(iex.getMessage());
				System.exit(1);
			} catch( gov.nasa.pds.tools.label.parser.ParseException pe ) {
				continue;
			}
		}
	}
	
	/**
	 * Read and parse the label files passed into the VTool command line. 
	 * Only does syntactic validation. No semantic validation is performed.
	 * 
	 * @param files a List of label files to be validated
	 */
	
	public void validateLabels(List files) {
		validateLabels(files, null);
	}
	
	/**
	 * Create a list of files based on supplied list of files and/or directories
	 * 
	 * @param input a list of files and/or directories to be validated
	 * @param recurse 'True' if recursing through the supplied directories. 'False'
	 *    to only look in the supplied directory for files, ignoring subdirectories.
	 * @param wildcards a list of wildcards to enable filtering of files through 
	 *    a supplied directory. Set to 'null' if no pattern matching is to be used.
	 * @param noF file containing a list of files and/or file patterns to ignore. Set to 'null'
	 *    if no files are to be ignored.
	 */
	
	public void createFileList(List input, boolean recurse, List wildcards, List noF) {
		FileListGenerator list = new FileListGenerator(input);
		
		// Set the appropriate filters
		if(wildcards != null)
			list.setFileFilter(wildcards);
		if(noF != null)
			list.setNoFileFilter(noF);
		files = list.visitFilesAndDirs(recurse);
	}
	
	public static void main(String[] argv) {
		VTool vtool = new VTool();
		Dictionary mainDictionary = null;
		BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("%-5p %m%n")));
		
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
		
		vtool.createFileList(vtool.targets, vtool.recursive, vtool.patterns, vtool.noFile);

		if(vtool.dictionaries != null) {
			mainDictionary = vtool.readDictionaries(vtool.dictionaries);
			vtool.validateLabels(vtool.files, mainDictionary);
		}
		else 
			vtool.validateLabels(vtool.files);
 
		System.exit(0);
	}
}
