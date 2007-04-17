//Copyright 2006-2007, by the California Institute of Technology.
//ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
//Any commercial use must be negotiated with the Office of Technology Transfer
//at the California Institute of Technology.
//
//This software is subject to U. S. export control laws and regulations
//(22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
//is subject to U.S. export control laws and regulations, the recipient has
//the responsibility to obtain export licenses or other export authority as
//may be required before exporting such information to foreign countries or
//providing access to foreign nationals.

package gov.nasa.pds.tools.config;

/**
 * Contains the valid keys for a VTool configuration file. <br><br>
 * 
 * Current keys are:<br>
 * <ul>
 *      vtool.alias<br>
 *      vtool.dict<br>
 *      vtool.follow<br>
 *      vtool.force<br>
 *      vtool.ignoredir<br>
 *      vtool.ignorefile<br>
 *      vtool.includepaths<br>
 *      vtool.log<br>
 *      vtool.progress<br>
 *      vtool.recursive<br>
 *      vtool.regexp<br>
 *      vtool.report<br>
 *      vtool.showlog<br>
 *      vtool.style<br>
 *      vtool.target<br>
 *      vtool.verbose<br>
 * </ul>     
 * @author mcayanan
 *
 */
public interface VToolConfigKeys {
	public final String ALIASKEY = "vtool.alias";
//	TODO: Add data object key to turn ON/OFF object validation when it is implemented
//	public final String DATAOBJKEY = "vtool.dataobject";
	public final String DICTKEY = "vtool.dict";
	public final String FORCEKEY = "vtool.force";
	public final String FOLLOWKEY = "vtool.follow";
	public final String IGNOREDIRKEY = "vtool.ignoredir";
	public final String IGNOREFILEKEY = "vtool.ignorefile";
	public final String INCLUDESKEY = "vtool.includepaths";
	public final String LOGKEY = "vtool.log";
	public final String PROGRESSKEY = "vtool.progress";
	public final String RECURSEKEY = "vtool.recursive";
	public final String REGEXPKEY = "vtool.regexp";
	public final String REPORTKEY = "vtool.report";
	public final String SHOWLOGKEY = "vtool.showlog";
	public final String STYLEKEY = "vtool.style";
	public final String TARGETKEY = "vtool.target";
	public final String VERBOSEKEY = "vtool.verbose";
}
