//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools;

import gov.nasa.jpl.pds.tools.dict.Dictionary;
import gov.nasa.jpl.pds.tools.dict.parser.DictionaryParser;
import gov.nasa.jpl.pds.tools.label.parser.LabelParser;
import gov.nasa.jpl.pds.tools.label.parser.LabelParserFactory;
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
	private boolean data_obj;
	private List dictionaries;
	private File exclude;
	private File include_path;
	private boolean follow_ptrs;
	private List file_input;
	private int max_errors;
	private boolean partial;
	private boolean recursive;
	private File output;
	private String output_detail;
	private short verbose;
	private boolean xml;
	

	/** Default constructor */
	public VTool(){
		alias = true;
		data_obj = true;
		max_errors = 300;
		partial = false;
		recursive = true;
		follow_ptrs = true;
		output_detail = "full";
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
		
		options = new Options();
		
		options.addOption("F", "no-follow", false, "Do not follow ^STRUCTURE pointers in a label");
		options.addOption("h", "help", false, "Display usage");
		options.addOption("OBJ", "no-obj", false, "Do not perform data object validation");
		options.addOption("p", "partial", false, "Validate as a partial label file");
		options.addOption("R", "no-recursive", false, "Don't validate any label files below the level of the input directory. " + 
				                               "Only applies when validating more than 1 label file");	
		options.addOption("u", "unalias", false, "Disable aliasing feature when validating label file(s)");
		options.addOption("V", "version", false, "Display VTool version");
		options.addOption("xml", "xml-output", false, "Output the report in XML format");
		
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
		OptionBuilder.withDescription("Specify the label file(s) to validate (required option)");
		OptionBuilder.hasArgs();
		OptionBuilder.withArgName("label(s)");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("f"));
		
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
		parser = new GnuParser();
		
		try {
			cmd = parser.parse(options, argv);
		}
		catch( ParseException exp ) {
			System.err.println( "Command line parser failed.\n\nReason: " + exp.getMessage() );
		}
	}
	
	/** 
	 * Queries the command line of VTool 
	 * @throws MissingOptionException 
	 *
	 */
	private void queryCmdLine() {

		int i = 0;
		
		try {
			
			for(i=0; i < cmd.getArgs().length; i++) {
				throw new UnrecognizedOptionException( "Unrecognized option/argument: " + cmd.getArgs()[i]);
			}
		
			/* verbose flag must be queried first in order to determine whether to print
			 * debug statements
			 */
			if(cmd.hasOption("v")) {
				
				try {
					verbose = Short.parseShort(cmd.getOptionValue("v"));
					printDebug("Verbosity level has been set to: " + verbose);
				}
				catch( NumberFormatException nfe ) {
					System.err.println("Problems parsing value set for the -v flag");
					nfe.printStackTrace();
					return;
				}
				
				if(verbose < 0 || verbose > 3) {
					throw new IllegalArgumentException("Invalid value entered for -v flag." + 
																" Valid values can only be 0, 1, 2, or 3");
				}
			}
			
			if(cmd.hasOption("h")) {
				printDebug("Display usage to terminal");
				showHelp();
				return;
			}
		
			if(cmd.hasOption("V")) {
				printDebug("Display version number and disclaimer notice");
				showVersion();
				return;
			}
		
			if(cmd.hasOption("f")) {
		        //TODO: handle multiple file inputs (wildcarded inputs)
				file_input = Arrays.asList(cmd.getOptionValues("f"));
			}
			else
				throw new MissingOptionException("The -f flag is required");
			
			if(cmd.hasOption("c")) {
				config = new File(cmd.getOptionValue("c"));
				printDebug("Got configuration file: " + config);
				System.out.println("Call method to set default behaviors based on configuration file contents");
			}
			
			if(cmd.hasOption("F")) {
				follow_ptrs = false;
			}
			
			if(cmd.hasOption("I")) {
				include_path = new File(cmd.getOptionValue("I"));
				printDebug("Got path to pointer files: " + include_path);
			}
			
			if(cmd.hasOption("o")) {
				output = new File(cmd.getOptionValue("o"));
				printDebug("Report file name has been set to: " + output);
			}
			
			if(cmd.hasOption("OBJ")) {
				data_obj = false;
				printDebug("Setting data object flag to false");
			}
			
			if(cmd.hasOption("R")) {
				recursive = false;
				printDebug("Setting recursive flag to false.");
			}
			
			if(cmd.hasOption("u")) {
				alias = false;
				printDebug("Setting alias flag to false");
			}
			
			if(cmd.hasOption("xml")) {
				xml = true;
				printDebug("Report will be in XML format");
			}
		
			if(cmd.hasOption("X")) {
				exclude = new File(cmd.getOptionValue("X"));
				printDebug("Obtained text file to ignore files: " + exclude);
				System.out.println("Call method to read file and store file patterns to skip");
			}
			
			if(cmd.hasOption("d")) {

				printDebug("Retrieved " + cmd.getOptionValues("d").length + " dictionary files");
				dictionaries = Arrays.asList(cmd.getOptionValues("d"));
				
				System.out.println("Call method to merge and parse data dictionary file(s)");
			}

			if(cmd.hasOption("m")) {
				try {
					max_errors = Integer.parseInt(cmd.getOptionValue("m"));
					printDebug("Max error messages has been set to: " + max_errors);
				}
				catch( NumberFormatException nfe ) {
					System.err.println("Problem parsing value set for -m flag");
					nfe.printStackTrace();
					return;
				}
				if( max_errors <= 0 ) {
					throw new IllegalArgumentException( "Max Errors Value must be a positive integer number");
				}
			}
		
			if(cmd.hasOption("od")) {
				
				output_detail = cmd.getOptionValue("od");
				printDebug("Report detail has been set to: " + output_detail);
				
				if( (output_detail.equalsIgnoreCase("sum") == false) &&
					(output_detail.equalsIgnoreCase("min") == false) &&
					(output_detail.equalsIgnoreCase("full") == false) ) {
					throw new IllegalArgumentException("Invalid value entered for -od flag" + 
														" Value can only be either 'full', 'sum', or 'min");
				}
			}

		}
		catch( IllegalArgumentException iae ) {
			iae.printStackTrace();
			return;
		}
		catch( MissingOptionException moe ) {
			moe.printStackTrace();
			return;
		}
		catch( UnrecognizedOptionException uoe ) {
			uoe.printStackTrace();
			return;
		}
		catch( SecurityException se ) {
			se.printStackTrace();
			return;
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
	 * @param dictionary - a List object contain the data dictionary files
	 * @return A Dictionary object that includes all the dictionary information from
	 *  all the dictionary files passed in
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws gov.nasa.jpl.pds.tools.label.parser.ParseException
	 */
	
	public Dictionary readDictionaries(List dictionary) throws MalformedURLException, IOException, gov.nasa.jpl.pds.tools.label.parser.ParseException {
		
		DictionaryParser dictionaryParser = new DictionaryParser();
		Dictionary dict;
		
		dict = dictionaryParser.parse( new File(dictionary.get(0).toString()).toURL() );
		dictionary.remove(0);
		
		for( Iterator i = dictionary.iterator(); i.hasNext(); ) {
			dict.merge( dictionaryParser.parse( new File (i.next().toString()).toURL() ) );
		}
		
		return dict;
		
	}
	
	/**
	 * Read and parse the label files passed into the VTool command line
	 * 
	 * @param files - A List of label files to be validated
	 * @param dict - A Dictionary object needed for semantic validation
	 * @throws MalformedURLException
	 * @throws gov.nasa.jpl.pds.tools.label.parser.ParseException
	 * @throws IOException
	 */
	
	public void readLabels(List files, Dictionary dict) throws MalformedURLException, gov.nasa.jpl.pds.tools.label.parser.ParseException, IOException {
		
		LabelParserFactory factory = LabelParserFactory.newInstance();
		LabelParser parser = factory.newLabelParser();
		
		
		for( Iterator i = files.iterator(); i.hasNext(); ) {
			parser.parse( new File (i.next().toString()).toURL(), dict );
		}
	}
	
	public static void main(String[] argv) {
	
		VTool vtool = new VTool();
		Dictionary mainDictionary;
		
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
		
		try {
			mainDictionary = vtool.readDictionaries(vtool.dictionaries);
			vtool.readLabels(vtool.file_input, mainDictionary);
		} 
		catch (MalformedURLException mue) {
			mue.printStackTrace();
		} 
		catch (IOException ioe) {
			ioe.printStackTrace();
		} 
		catch (gov.nasa.jpl.pds.tools.label.parser.ParseException pe) {
			pe.printStackTrace();
		}
	}
	
	


}
