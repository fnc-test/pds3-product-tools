package gov.nasa.pds.tools.config;

/**
 * Contains the valid keys for a VTool configuration file <br><br>
 * 
 * Current keys are:<br>
 *      vtool.alias<br>
 *      vtool.dataobject<br>
 *      vtool.dict<br>
 *      vtool.follow<br>
 *      vtool.ignoredir<br>
 *      vtool.ignorefile<br>
 *      vtool.includepaths<br>
 *      vtool.logfile<br>
 *      vtool.recursive<br>
 *      vtool.regexp<br>
 *      vtool.report<br>
 *      vtool.style<br>
 *      vtool.target<br>
 *      vtool.verbose<br>
 *      
 * @author mcayanan
 *
 */
public interface VToolConfigKeys {
	public final String ALIAS = "vtool.alias";
	public final String DATAOBJ = "vtool.dataobject";
	public final String DICT = "vtool.dict";
	public final String FOLLOW = "vtool.follow";
	public final String IGNOREDIR = "vtool.ignoredir";
	public final String IGNOREFILE = "vtool.ignorefile";
	public final String INCLUDES = "vtool.includepaths";
	public final String LOG = "vtool.logfile";
	public final String RECURSE = "vtool.recursive";
	public final String REGEXP = "vtool.regexp";
	public final String REPORT = "vtool.report";
	public final String STYLE = "vtool.style";
	public final String TARGET = "vtool.target";
	public final String VERBOSE = "vtool.verbose";
}
