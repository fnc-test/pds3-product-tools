//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools;

import java.lang.Object;
import org.apache.commons.cli.*;


/**
 * @author pramirez
 * @version $Revision$
 * 
 * Class to replace LVTool functionality
 * 
 */
public class VTool {
	
	private Options options;
	private CommandLineParser parser;
	private CommandLine cmd;
	
	private boolean alias;
	private boolean data_obj;
	private boolean debug;
	private String dict;
	private String local_dict;
	private String no_dir;
	private String no_ext;
	private String include_path;
	private String label_file;
	private int max_errors;
	private boolean partial;
	private boolean prune;
	private String report;
	private String report_detail;
	private short verbose;
	private boolean xml;


	/** Default constructor */
	public VTool(){
		alias = true;
		data_obj = false;
		debug = false;
		max_errors = 300;
		partial = false;
		prune = false;
		report_detail = "full";
		/* Verbosity level of 4 means that we want to output both INFO and WARNING messages */
		verbose = 4;
		xml = false;
	}
	
	/**
	 * Builds the set of options that are available for VTool
	 */
	private void buildOpts() {
		
		options = new Options();
		
		options.addOption("d", "debug", false, "Run in debug mode");
		options.addOption("h", "help", false, "Display usage");
		options.addOption("p", "partial", false, "Validate as a partial label file");
		options.addOption("u", "unalias", false, "Disable aliasing feature when validating label file(s).");
		options.addOption("V", "version", false, "Display VTool version");
		options.addOption("x", "xml-format", false, "Output the report in XML format");
		options.addOption("obj", "data-obj", false, "Validate the data of a label");
		options.addOption("P", "prune", false, "Don't validate any label files below the level of the input directory. " + 
				                               "Only applies when validating more than 1 label file");		
		/* These are options that require an argument */

		/* Option to specify the PSDD file */
		OptionBuilder.withLongOpt("dict");
		OptionBuilder.withDescription("Specify the Planetary Science Data Dictionary full file name");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("dictname");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("dd"));

		/* Option to specify the local dictionary file */
		OptionBuilder.withLongOpt("local-dict");
		OptionBuilder.withDescription("Specify the name of the local data dictionary file");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("dictname");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("ldd"));
		
		/* Option to specify the verbosity level */
		OptionBuilder.withLongOpt("verbose");
		OptionBuilder.withDescription("Set the verbose level (1 = no INFO or WARNING, 2 = only INFO, 3 = only WARNING)");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("1|2|3");
		OptionBuilder.withType(short.class);
		options.addOption(OptionBuilder.create("v"));
		

		/* Option to specify the label(s) to validate */
		OptionBuilder.withLongOpt("file");
		OptionBuilder.withDescription("Specify the label file(s) to validate (required option)");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("label");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("f"));
		
		/* Option to specify the maximum number of ERROR type messages that will be printed to the report */
		OptionBuilder.withLongOpt("max-errors");
		OptionBuilder.withDescription("Specify the maximum number of ERROR type messages that VTool will " +
									"print to the report file. Default is 300 errors");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("int value");
		OptionBuilder.withType(int.class);
		options.addOption(OptionBuilder.create("m"));
				
		/* Option to specify a path to the Pointer files */		
		OptionBuilder.withLongOpt("include-path");
		OptionBuilder.withDescription("Specify the path to search for pointer files. " + 
															"Default is the current working directory.");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("pathRef");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("I"));
		
		/* Option to specify the report file name */
		OptionBuilder.withLongOpt("report");
		OptionBuilder.withDescription("Specify the report file name. Default is to print to the terminal");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("filename");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("r"));

		/* Option to specify how detail the reporting should be */
		OptionBuilder.withLongOpt("report-detail");
		OptionBuilder.withDescription("Specify the level of detail for the reporting. " +
										"Default is a full report. Valid values are 'min' for minimal detail " +
										"and 'sum' for a summarized report");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("min|sum");
		OptionBuilder.withType(String.class);
		options.addOption(OptionBuilder.create("rd"));
		
		/* Option to specify a file containing a list of file extensions to ignore */
		OptionBuilder.withLongOpt("no-ext");
		OptionBuilder.withDescription("Specify a text file containing a list of file extensions to ignore");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("filename");
		options.addOption(OptionBuilder.create("NE"));
		
		/* Option to specify a file containing a list of directories to ignore */
		OptionBuilder.withLongOpt("no-dir");
		OptionBuilder.withDescription("Specify a text file containing a list of directories to ignore");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("filename");
		options.addOption(OptionBuilder.create("ND"));
		
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
			System.err.println( "Command line parser failed. Reason: " + exp.getMessage() );
		}
	}
	
	/** 
	 * Queries the command line of VTool 
	 * @throws MissingOptionException 
	 *
	 */
	private void queryCmdLine() throws MissingOptionException {

		if(cmd.hasOption("d")) {
			debug = true;
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
			label_file = cmd.getOptionValue("f");
			printDebug("Got label file: " + label_file);
		}
		else
			throw new MissingOptionException("The -f flag is required");
	
		if(cmd.hasOption("u")) {
			alias = false;
			printDebug("Setting alias flag to false");
		}
		
		if(cmd.hasOption("obj")) {
			data_obj = true;
			printDebug("Setting data object flag to true");
		}
		
		if(cmd.hasOption("ND")) {
			no_dir = cmd.getOptionValue("ND");
			
			printDebug("Obtained text file to ignore some directories: " + no_dir);
			System.out.println("Call method to read file and store directory names to skip");
		}
		
		if(cmd.hasOption("NE")) {
			no_ext = cmd.getOptionValue("NE");
			
			printDebug("Obtained text file to ignore some file extensions: " + no_ext);
			System.out.println("Call method to read file and store extensions to skip");
		}

		if(cmd.hasOption("I")) {
			include_path = cmd.getOptionValue("I");
			printDebug("Got path to pointer files: " + include_path);
		}
		
		if(cmd.hasOption("m")) {
			try {
				max_errors = Integer.parseInt(cmd.getOptionValue("m"));
				printDebug("Max error messages has been set to: " + max_errors);
			}
			catch( NumberFormatException nfe ) {
				System.err.println("Problem parsing value for max_errors flag.");
				System.err.println("Reason: " + nfe.getMessage());
				return;
			}
		}
		
		if(cmd.hasOption("P")) {
			prune = true;
			printDebug("Setting prune flag to true.");
		}
		
		if(cmd.hasOption("dd")) {
			dict = cmd.getOptionValue("dd");
			
			printDebug("Obtained PSDD: " + dict);
			System.out.println("Call method to load PDS data dictionary");
		}
		
		if(cmd.hasOption("ldd")) {
			local_dict = cmd.getOptionValue("ldd");
			
			printDebug("Obtained Local dictionary file: " + local_dict);
			System.out.println("Call method to load local dictionary");
		}
		
		if(cmd.hasOption("r")) {
			report = cmd.getOptionValue("r");
			printDebug("Report file name has been set to: " + report);
		}
		
		if(cmd.hasOption("rd")) {
			try {
				report_detail = cmd.getOptionValue("rd");
				printDebug("Report detail has been set to: " + report_detail);
				
				if( (report_detail.equalsIgnoreCase("sum") == false) &&
					(report_detail.equalsIgnoreCase("min") == false) ) {
					throw new IllegalArgumentException();
				}
			}
			catch( IllegalArgumentException ae ) {
				System.err.println("Invalid value entered for -rd flag");
				System.err.println("Value can only be either 'sum' or 'min'");
				return;
			}
		}
		
		if(cmd.hasOption("v")) {
			try {
				verbose = Short.parseShort(cmd.getOptionValue("v"));
				printDebug("Verbosity level has been set to: " + verbose);
				
				if(verbose < 1 || verbose > 3) {
					throw new IllegalArgumentException();
				}
			}
			catch( NumberFormatException nfe ) {
				System.err.println("Problems parsing value for verbosity level flag");
				System.err.println("Reason: " + nfe.getMessage());
				return;
			}
			catch( IllegalArgumentException ae ) {
				System.err.println("Invalid value entered for -v flag");
				System.err.println("Value for verbosity level flag can only be 1, 2, or 3");
				return;
			}
		}
		
		if(cmd.hasOption("x")) {
			xml = true;
			printDebug("Report will be in XML format");
		}
	}
	/**
	 * Display usage and help information
	 *
	 */
	private void showHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(35, "VTool", null, options, null);
	}
	
	/**
	 * Print debugging statement if debug flag is set
	 * @param s
	 */
	
	private void printDebug(String s) {
		if(debug) 
			System.out.println("DEBUG: " + s);
	}
	
	/**
	 * Show the version and disclaimer notice for VTool 
	 *
	 */	
	private void showVersion() {
		System.out.println("PDS Validation Tool (VTool) BETA\n");
		System.out.println("DISCLAIMER:");
		System.out.println("THIS SOFTWARE AND ANY RELATED MATERIALS WERE CREATED BY THE CALIFORNIA");
		System.out.println("INSTITUTE OF TECHNOLOGY (CALTECH) UNDER A U.S. GOVERNMENT CONTRACT WITH THE");
		System.out.println("NATIONAL AERONAUTICS AND SPACE ADMINISTRATION (NASA). THE SOFTWARE IS");
		System.out.println("TECHNOLOGY AND SOFTWARE PUBLICLY AVAILABLE UNDER U.S. EXPORT LAWS AND IS");
		System.out.println("PROVIDED \"AS-IS\" TO THE RECIPIENT WITHOUT WARRANTY OF ANY KIND, INCLUDING");
		System.out.println("ANY WARRANTIES OF PERFORMANCE OR MERCHANTABILITY OR FITNESS FOR A PARTICULAR");
		System.out.println("USE OR PURPOSE (AS SET FORTH IN UNITED STATES UCC2312-2313) OR FOR ANY");
		System.out.println("PURPOSE WHATSOEVER, FOR THE SOFTWARE AND RELATED MATERIALS, HOWEVER USED. IN");
		System.out.println("NO EVENT SHALL CALTECH, ITS JET PROPULSION LABORATORY, OR NASA BE LIABLE FOR");
		System.out.println("ANY DAMAGES AND/OR COSTS, INCLUDING, BUT NOT LIMITED TO, INCIDENTAL OR");
		System.out.println("CONSEQUENTIAL DAMAGES OF ANY KIND, INCLUDING ECONOMIC DAMAGE OR INJURY TO");
		System.out.println("PROPERTY AND LOST PROFITS, REGARDLESS OF WHETHER CALTECH, JPL, OR NASA BE");
		System.out.println("ADVISED, HAVE REASON TO KNOW, OR, IN FACT, SHALL KNOW OF THE POSSIBILITY.");
		System.out.println("RECIPIENT BEARS ALL RISK RELATING TO QUALITY AND PERFORMANCE OF THE SOFTWARE");
		System.out.println("AND ANY RELATED MATERIALS, AND AGREES TO INDEMNIFY CALTECH AND NASA FOR ALL");
		System.out.println("THIRD-PARTY CLAIMS RESULTING FROM THE ACTIONS OF RECIPIENT IN THE USE OF THE");
		System.out.println("SOFTWARE.\n");
	}
	
	
	public static void main(String[] argv) {
	
		VTool vtool = new VTool();
		
		if(argv.length == 0) {
			System.out.println("\nType 'VTool -h' for usage");
			return;
		}
		
		/* Define options */
		vtool.buildOpts();

		/* Parse the command line */
		vtool.parseLine(argv);
		
		/* Query the command line */
		try {
			vtool.queryCmdLine();
		}
		catch( MissingOptionException moe ) {
			moe.printStackTrace();
			return;
		}
		
	}
	
	


}
