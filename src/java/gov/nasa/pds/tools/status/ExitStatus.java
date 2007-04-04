//Copyright 2006-2007, by the California Institute of 
//Technology. ALL RIGHTS RESERVED. United States Government 
//Sponsorship acknowledged. Any commercial use must be negotiated with 
//the Office of Technology Transfer at the California Institute of 
//Technology.

//This software may be subject to U.S. export control laws. By 
//accepting this software, the user agrees to comply with all 
//applicable U.S. export laws and regulations. User has the 
//responsibility to obtain export licenses, or other export authority 
//as may be required before exporting such information to foreign 
//countries or providing access to foreign persons.

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
