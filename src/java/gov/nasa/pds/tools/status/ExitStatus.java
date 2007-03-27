package gov.nasa.pds.tools.status;

/**
 * Exit Status values
 * <br><br>
 *  0 = Success<br>
 *  1 = Failure<br>
 * -1 = Tool Application Failure<br>
 * @author mcayanan
 *
 */
public interface ExitStatus {
	public final int GOODRUN = 0;
	public final int BADRUN = 1;
	public final int TOOLFAILURE = -1;
}
