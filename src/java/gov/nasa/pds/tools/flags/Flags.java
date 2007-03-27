package gov.nasa.pds.tools.flags;

/**
 * Class containing command-line option flags
 * 
 * @author mcayanan
 *
 */
public interface ToolsFlags {
	public final int SHORT = 0;
	public final int LONG = 1;
	
	public final String ALIAS[] = {"a", "alias"};
	public final String CONFIG[] = {"c", "config"};
	public final String DATAOBJ[] = {"O", "no-obj"};
	public final String DICT[] = {"d", "dict"};
	public final String FOLLOW[] = {"F", "no-follow"};
	public final String HELP[] = {"h", "help"};
	public final String IGNOREDIR[] = {"D", "ignore-dir"};
	public final String IGNOREFILE[] = {"X", "ignore-file"};
	public final String INCLUDES[] = {"I", "include"};
	public final String LOCAL[] = {"L", "local"};	
	public final String LOG[] = {"l", "log-file"};
	public final String PARTIAL[] = {"f", "force"};
	public final String PROGRESS[] = {"p", "progress"};
	public final String REGEXP[] = {"e", "regexp"};
	public final String REPORT[] = {"r", "report-file"};
	public final String RPTSTYLE[] = {"s", "report-style"};
	public final String TARGET[] = {"t", "target"};
	public final String VERBOSE[] = {"v", "verbose"};
	public final String VERSION[] = {"V", "version"};
}
