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
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;

/**
 * @author pramirez
 * @version $Revision$
 * 
 * Class to replace LVTool functionality
 * 
 */
public class VTool {
	
	final private String version_id = "0.1.0"; 
	
	private Options options;
	private CommandLineParser parser;
	private CommandLine cmd;
	
	private boolean alias;
	private File config;
	private boolean dataObj;
	private List dictionaries;
	private File exclude;
	private List fInput;
	private List files;
	private boolean followPtrs;
	private File includePath;
	private int max_errors;
	private boolean partial;
	private List patterns;
	private boolean recursive;
	private File output;
	private String outDetail;
	private short verbose;
	private boolean xml;
	

	/** 
	 * Default constructor
	 */
	public VTool(){
		
		options = new Options();
		parser = new GnuParser();
		
		alias = true;
		config = null;
		dictionaries = null;
		dataObj = true;
		exclude = null;
		followPtrs = true;
		fInput = null;
		files = null;
		includePath = null;
		max_errors = 300;
		partial = false;
		patterns = null;
		recursive = true;
		output = null;
		outDetail = "full";
		verbose = 2;
		xml = false;
	}
	
	/**
	 * Show the version and disclaimer notice for VTool 
	 *
	 */	
	private void showVersion() {
		System.out.println("PDS Validation Tool (VTool) BETA " + version_id);
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
	}
	
	/**
	 * Display VTool usage and help information
	 *
	 */
	private void showHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(35, "VTool", null, options, null);
	}
	
	/**
	 * Builds the set of options that are available for VTool
	 */
	private void buildOpts() {
		options.addOption("F", "no-follow", false, "Do not follow ^STRUCTURE pointers in a label");
		options.addOption("h", "help", false, "Display usage");
		options.addOption("OBJ", "no-obj", false, "Do not perform data object validation");
		options.addOption("p", "partial", false, "Validate as a partial label file");
		options.addOption("l", "local", false, "Validate files only in the input directory rather than " + 
				                               "recursively traversing down the subdirectories.");	
		options.addOption("u", "unalias", false, "Disable aliasing feature when validating label file(s)");
		options.addOption("V", "version", false, "Display VTool version");
		options.addOption("x", "xml-output", false, "Output the report in XML format");
		
		
		/* These are options that require an argument */

		/* Option to specify a configuration file */
		OptionBuilder.withLongOpt("config");
		OptionBuilder.withDescription("Specify a configuration file to set the default values for VTool");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("filename");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("c"));
		
		/* Option to specify the PSDD and any local dictionaries */
		OptionBuilder.withLongOpt("dict");
		OptionBuilder.withDescription("Specify the Planetary Science Data Dictionary full file name " +
				                      "and any local dictionaries to include for validation. Separate each file name with a space.");
		OptionBuilder.hasArgs();
		OptionBuilder.withArgName(".full file(s)");
		OptionBuilder.withValueSeparator();
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("d"));
		
		/* Option to specify the label(s) to validate */
		OptionBuilder.withLongOpt("file");
		OptionBuilder.withDescription("Specify the label file(s) and/or directories to validate (required option)");
		OptionBuilder.hasArgs();
		OptionBuilder.withArgName("label(s) and/or dir(s)");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("f"));
		
		/* Option to specify a pattern to match against the input directory to be validated */
		OptionBuilder.withLongOpt("pattern");
		OptionBuilder.withDescription("Specify a pattern to match against the input directory to be validated. " +
				                      "Each Pattern must be surrounded by quotes. (i.e. -p \"*.LBL\" \"*FMT\")");
		OptionBuilder.hasArgs();
		OptionBuilder.withArgName("expression(s)");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("p"));
		
		/* Option to specify a path to the Pointer files */		
		OptionBuilder.withLongOpt("include");
		OptionBuilder.withDescription("Specify the starting path to search for pointer files. " + 
															"Default is the current working directory");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("pathRef");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("I"));

		/* Option to specify the maximum number of ERROR type messages that will be printed to the report */
		OptionBuilder.withLongOpt("max-errors");
		OptionBuilder.withDescription("Specify the maximum number of ERROR type messages that VTool will " +
									"print to the report file. Default is 300 errors");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("int value");
		OptionBuilder.withType(int.class);
		options.addOption(OptionBuilder.create("m"));
		
		/* Option to specify the report file name */
		OptionBuilder.withLongOpt("output");
		OptionBuilder.withDescription("Specify the file name for the report. Default is to print to the terminal");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("filename");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("o"));

		/* Option to specify how detail the reporting should be */
		OptionBuilder.withLongOpt("output-detail");
		OptionBuilder.withDescription("Specify the level of detail for the reporting. " +
										"Valid values are 'full' for full details, 'min' for minimal detail " +
										"and 'sum' for a summary. Default is set to 'full'");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("full|sum|min");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("od"));
		
		/* Option to specify the severity level and above*/
		OptionBuilder.withLongOpt("verbose");
		OptionBuilder.withDescription("Specify the message severity level and above to include in the reporting: " + 
				                             "(0=Debug, 1=Info, 2=Warning, 3=Error or Fatal). " + 
				                             "Default is Warnings and above (level 2)");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("0|1|2|3");
		OptionBuilder.withType(short.class);
		options.addOption(OptionBuilder.create("v"));
		
		/* Option to specify a file containing a list of file extensions to ignore */
		OptionBuilder.withLongOpt("exclude");
		OptionBuilder.withDescription("Specify a text file containing a list of file patterns or extensions to ignore");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("filename");
		options.addOption(OptionBuilder.create("X"));
		
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
			
			for(Iterator i = cmd.getArgList().iterator(); i.hasNext();) {
				throw new UnrecognizedOptionException( "Unrecognized option/argument: " + i.next().toString());
			}
		
			/* verbose flag must be queried first in order to determine whether to print
			 * debug statements
			 */
			if(cmd.hasOption("v")) {
				
				try {
					verbose = Short.parseShort(cmd.getOptionValue("v"));
					printDebug("Verbosity level has been set to: " + verbose);
				}
				catch( Exception e) {
					throw new NumberFormatException("Problems parsing value set for the 'v' flag: " + cmd.getOptionValue("v"));
				}
			
				if(verbose < 0 || verbose > 3) {
					throw new IllegalArgumentException("Invalid value entered for 'v' flag." + 
																" Valid values can only be 0, 1, 2, or 3");
				}
			}
			
			if(cmd.hasOption("h")) {
				printDebug("Display usage to terminal");
				showHelp();
				System.exit(0);
			}
		
			if(cmd.hasOption("V")) {
				printDebug("Display version number and disclaimer notice");
				showVersion();
				System.exit(0);
			}
		
			if(cmd.hasOption("f")) {
				fInput = Arrays.asList(cmd.getOptionValues("f"));
			}
			else
				throw new MissingOptionException("The 'f' flag is required");
			
			if(cmd.hasOption("c")) {
				//TODO: Read and process configuration file
				config = new File(cmd.getOptionValue("c"));
				printDebug("Got configuration file: " + config);
				System.out.println("Call method to set default behaviors based on configuration file contents");
			}
			
			if(cmd.hasOption("F")) {
				followPtrs = false;
			}
			
			if(cmd.hasOption("I")) {
				if(followPtrs == false)
					throw new IllegalArgumentException("Option was selected to not follow pointers. 'I' flag cannot be specified"); 
				
				includePath = new File(cmd.getOptionValue("I"));
				printDebug("Got path to pointer files: " + includePath);
			}
			
			if(cmd.hasOption("o")) {
				//TODO: Setup messages to write to a file
				output = new File(cmd.getOptionValue("o"));
				printDebug("Report file name has been set to: " + output);
			}
			
			if(cmd.hasOption("OBJ")) {
				dataObj = false;
				printDebug("Setting data object flag to false");
			}
			
			if(cmd.hasOption("l")) {
				recursive = false;
				printDebug("Setting recursive flag to false.");
			}
			
			if(cmd.hasOption("p")) {
				patterns = Arrays.asList(cmd.getOptionValues("p"));
			}
			
			if(cmd.hasOption("u")) {
				alias = false;
				printDebug("Setting alias flag to false");
			}
			
			if(cmd.hasOption("x")) {
				xml = true;
				printDebug("Report will be in XML format");
			}
		
			if(cmd.hasOption("X")) {
				exclude = new File(cmd.getOptionValue("X"));
				printDebug("Obtained text file to ignore files: " + exclude);
				//TODO: Call method to read file and store file patterns to skip
			}
			
			if(cmd.hasOption("d")) {
				printDebug("Retrieved " + cmd.getOptionValues("d").length + " dictionary file(s)");
				dictionaries = Arrays.asList(cmd.getOptionValues("d"));
			}

			if(cmd.hasOption("m")) {
				try {
					max_errors = Integer.parseInt(cmd.getOptionValue("m"));
					printDebug("Max error messages has been set to: " + max_errors);
				}
				catch( Exception e ) {
					throw new NumberFormatException("Problems parsing value set for 'm' flag: " + cmd.getOptionValue("m"));
				}

				if( max_errors <= 0 ) {
					throw new IllegalArgumentException( "Max Errors Value must be a positive integer number");
				}
			}
		
			if(cmd.hasOption("od")) {
				
				outDetail = cmd.getOptionValue("od");
				printDebug("Report detail has been set to: " + outDetail);
				
				if( (outDetail.equalsIgnoreCase("sum") == false) &&
					(outDetail.equalsIgnoreCase("min") == false) &&
					(outDetail.equalsIgnoreCase("full") == false) ) {
					throw new IllegalArgumentException("Invalid value entered for 'od' flag." + 
														" Value can only be either 'full', 'sum', or 'min'");
				}
			}

		}
		catch( NumberFormatException nfe ) {
			System.out.println( nfe.getMessage() );
			System.exit(1);
		}
		catch( IllegalArgumentException iae ) {
			System.out.println( iae.getMessage() );
			System.exit(1);
		}
		catch( MissingOptionException moe ) {
			System.out.println( moe.getMessage() );
			System.exit(1);
		}
		catch( UnrecognizedOptionException uoe ) {
			System.out.println( uoe.getMessage() );
			System.exit(1);
		}
	}
	
	/**
	 * Print debugging statement if debug flag is set
	 * @param s
	 */
	
	private void printDebug(String s) {
		if(verbose == 0) 
			System.out.println("DEBUG: " + s);
	}
	
	
	/**
	 * Read and parse the dictionary files passed into the VTool command line
	 * 
	 * @param dictionary - a List object containing the data dictionary files
	 * @return A Dictionary object that includes all the dictionary information from
	 *  all the dictionary files passed in. Returns null if dictionary has a null value.
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	
	public Dictionary readDictionaries(List dictionary) throws MalformedURLException, IOException {
		Dictionary dict = null;
		File dd = null;
		Iterator i = dictionary.iterator();
		
		try {
			printDebug( "Parsing dictionary file: " + dictionary.get(0) );
			dict = DictionaryParser.parse( new File(i.next().toString()).toURL() );
			while( i.hasNext() ) {
				dd = new File( i.next().toString() );
				printDebug("Parsing dictionary file: " + dd);
				dict.merge( DictionaryParser.parse( dd.toURL() ) );
			}
		}
		catch( gov.nasa.pds.tools.label.parser.ParseException pe) {
			System.out.println("Error parsing Dictionary");
			System.exit(1);
		}

		return dict;
		
	}
	
	/**
	 * Read and parse the label files passed into the VTool command line
	 * 
	 * @param files - A List of label files to be validated
	 * @param dict - A Dictionary object needed for semantic validation
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	
	public void readLabels(List files, Dictionary dict) throws MalformedURLException, IOException {
		LabelParserFactory factory = LabelParserFactory.getInstance();
		LabelParser parser = factory.newLabelParser();
		File label;
		
		for( Iterator i = files.iterator(); i.hasNext(); ) {
			label = new File( i.next().toString() );
			System.out.println("\nValidating " + label);
			
			try {
				parser.parse( label.toURL(), dict );
			} 
			catch( gov.nasa.pds.tools.label.parser.ParseException pe ) {
				continue;
			}
		}
	}
	
	/**
	 * Create a list of files based on supplied list of files and/or directories.
	 * 
	 * @param input - A list of files and/or directories to be validated
	 * @param recurse - 'True' if recursing through the supplied directories. 'False'
	 *    to only look in the supplied directory for files, ignoring subdirectories.
	 * @param wildcards - A list of wildcards to enable filtering of files through 
	 *    a supplied directory. 
	 */
	
	public void createFileList(List input, boolean recurse, List wildcards) {
		FileListGenerator fileList = new FileListGenerator(input);
		
		if(wildcards != null) {
			fileList.setFileFilter(wildcards);
		}
		files = fileList.visitFilesAndDirs(recurse);

	}
	
	public static void main(String[] argv) {
		VTool vtool = new VTool();
		Dictionary mainDictionary = null;
		
		if(argv.length == 0) {
			System.out.println("\nType 'VTool -h' for usage");
			return;
		}
		
		/* Define options */
		vtool.buildOpts();
		/* Parse the command line */
		vtool.parseLine(argv);
		/* Query the command line */
		vtool.queryCmdLine();
		
		BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("%-5p %m%n")));
		try {
			if(vtool.dictionaries != null) {
				mainDictionary = vtool.readDictionaries(vtool.dictionaries);
			}
			vtool.createFileList(vtool.fInput, vtool.recursive, vtool.patterns);
			vtool.readLabels(vtool.files, mainDictionary);
		} 
		catch (MalformedURLException mue) {
			System.out.println( mue.getMessage() );
			System.exit(1);
		} 
		catch (IOException ioe) {
			System.out.println( ioe.getMessage() );
			System.exit(1);
		} 
		System.exit(0);
	}
}
