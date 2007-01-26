package gov.nasa.pds.tools;

/**
 * Contains the valid keys for a VTool configuration file
 * 
 * Current keys are:
 *      vtool.alias
 *      vtool.dataobject
 *      vtool.dict
 *      vtool.follow
 *      vtool.ignoredir
 *      vtool.ignorefile
 *      vtool.includepaths
 *      vtool.logfile
 *      vtool.recursive
 *      vtool.regexp
 *      vtool.style
 *      vtool.target
 *      vtool.verbose
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
	public final String STYLE = "vtool.style";
	public final String TARGET = "vtool.target";
	public final String VERBOSE = "vtool.verbose";
}
