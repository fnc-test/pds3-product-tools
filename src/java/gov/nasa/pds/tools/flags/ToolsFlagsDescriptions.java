package gov.nasa.pds.tools.flags;

public interface ToolsFlagsDescriptions {
	public final String WHATIS_ALIAS = "Enables aliasing";
	
	public final String WHATIS_CONFIG = "Specify a configuration file to set the default values for VTool";
	
/*	public final String WHATIS_DATAOBJ = ""*/
	
	public final String WHATIS_DICT = "Specify the Planetary Science Data Dictionary full file name/URL"
		  							+ " and any local dictionaries to include for validation. Separate"
		  							+ " each file name with a space";
	
	public final String WHATIS_FOLLOW = "Do not follow or check for the existence of files referenced by" 
									  + " pointer statements in a label";
	
	public final String WHATIS_HELP = "Display usage";
	
	public final String WHATIS_IGNOREDIR = "Specify directory patterns to ignore. Separate each with a space."
		  								 + " Patterns should be surrounded by quotes"
		  								 + " (i.e. -D \"EXTRAS\" \"LABEL\" or -D \"EXTRAS LABEL\")";
	
	public final String WHATIS_IGNOREFILE = "Specify file patterns to ignore from validation. Separate each"
										  + " with a space. Patterns should be surrounded by quotes"
										  + " (i.e. -X \"*TAB\" \"*IMG\" or -X \"*TAB *IMG\")";
	
	public final String WHATIS_INCLUDES = "Specify the paths to look for files referenced by pointers in a"
		  								+ " label. Separate each with a space. Default is to always look"
		  								+ " at the same directory as the label";
	
	public final String WHATIS_LOCAL = "Validate files only in the target directory rather than recursively" 
									 + " traversing down the subdirectories.";
	
	public final String WHATIS_LOG = "Specify the file name for the machine-readable log. A file"
									 + " specification is optional. If no file name is given, then"
									 + " the log will be written to standard out.";
	
	public final String WHATIS_PARTIAL = "Force VTool to validate a label fragment";
	
	public final String WHATIS_PROGRESS = "Enable progress reporting";
	
	public final String WHATIS_REGEXP = "Specify file patterns to look for when validating a directory."
		  							  + " Separate each with a space. Patterns should be surrounded by"
		  							  + " quotes (i.e. -e \"*.LBL\" \"*.FMT\" or -e \"*.LBL *.FMT\")";
	
	public final String WHATIS_REPORT = "Specify the file name for the human-readable report. Default is"
		  							  + " to write to standard out if this flag is not specified. This"
		  							  + " report, however, will not print to standard out if this flag is"
		  							  + " missing AND the log file flag is specified with no file name";
	
	public final String WHATIS_RPTSTYLE = "Specify the level of detail for the reporting. Valid values are"
										+ " 'full' for a full view, 'min' for a minimal view and 'sum' for"
										+ " a summary view. Default is to see a full report if this flag is"
										+ " not specified";
	
	public final String WHATIS_TARGET = "Explicitly specify the targets (label files, URLs and directories)"
									  + " to validate. Targets can be specified implicitly as well."
									  + " (example: VTool label.lbl)";
	
	public final String WHATIS_VERBOSE = "Specify the severity level and above to include in the"
									   + "human-readable report: (1=Info, 2=Warning, 3=Error)."
									   + " Default is Warning and above (level 2)";
	
	public final String WHATIS_VERSION = "Displays VTool version";
}
