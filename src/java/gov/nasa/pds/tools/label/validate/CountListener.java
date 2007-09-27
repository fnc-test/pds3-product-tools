//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
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
