package gov.nasa.pds.tools.config;

/**
 * Contains the valid keys for a VTool configuration file <br><br>
 * 
 * Current keys are:<br>
 *      vtool.alias<br>
 *      vtool.dataobject<br>
 *      vtool.dict<br>
 *      vtool.follow<br>
 *      vtool.force<br>
 *      vtool.ignoredir<br>
 *      vtool.ignorefile<br>
 *      vtool.includepaths<br>
 *      vtool.logfile<br>
 *      vtool.progress<br>
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
	public final String ALIASKEY = "vtool.alias";
	public final String DATAOBJKEY = "vtool.dataobject";
	public final String DICTKEY = "vtool.dict";
	public final String FORCEKEY = "vtool.force";
	public final String FOLLOWKEY = "vtool.follow";
	public final String IGNOREDIRKEY = "vtool.ignoredir";
	public final String IGNOREFILEKEY = "vtool.ignorefile";
	public final String INCLUDESKEY = "vtool.includepaths";
	public final String LOGKEY = "vtool.logfile";
	public final String PROGRESSKEY = "vtool.progress";
	public final String RECURSEKEY = "vtool.recursive";
	public final String REGEXPKEY = "vtool.regexp";
	public final String REPORTKEY = "vtool.report";
	public final String STYLEKEY = "vtool.style";
	public final String TARGETKEY = "vtool.target";
	public final String VERBOSEKEY = "vtool.verbose";
}
