// Copyright 2006-2008, by the California Institute of 
// Technology. ALL RIGHTS RESERVED. United States Government 
// Sponsorship acknowledged. Any commercial use must be negotiated with 
// the Office of Technology Transfer at the California Institute of 
// Technology.
//
// This software may be subject to U.S. export control laws. By 
// accepting this software, the user agrees to comply with all 
// applicable U.S. export laws and regulations. User has the 
// responsibility to obtain export licenses, or other export authority 
// as may be required before exporting such information to foreign 
// countries or providing access to foreign persons.
//
// $Id$ 
//

package gov.nasa.pds.tools.label.validate;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class CountListener implements ValidationListener {
    private int numErrors = 0;
    private int numWarnings = 0;

    /* (non-Javadoc)
     * @see gov.nasa.pds.tools.label.validate.ValidationListener#reportError(java.lang.String)
     */
    public void reportError(String error) {
        numErrors++;
    }

    /* (non-Javadoc)
     * @see gov.nasa.pds.tools.label.validate.ValidationListener#reportWarning(java.lang.String)
     */
    public void reportWarning(String warning) {
        numWarnings++;
    }
    
    public int getNumErrors() {return numErrors;}
    public int getNumWarnings() {return numWarnings;}

}
