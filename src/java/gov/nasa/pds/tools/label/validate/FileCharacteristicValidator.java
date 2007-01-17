//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label.validate;

import org.apache.log4j.Logger;

import gov.nasa.pds.tools.label.Label;

/**
 * This class validates that all file characteristics are found in a label;
 * @author pramirez
 * @version $Revision$
 * 
 */
public class FileCharacteristicValidator implements LabelValidator {
    private static Logger log = Logger.getLogger(ElementValidator.class.getName());
    //This is the set of predefined required file characteristic elements
    private final static String [] characteristics = {"RECORD_TYPE", "RECORD_BYTES","FILE_RECORDS","LABEL_RECORDS"};

    /** (non-Javadoc)
     * @see gov.nasa.pds.tools.label.validate.LabelValidator#isValid(gov.nasa.pds.tools.label.Label)
     */
    public boolean isValid(Label label) {
        boolean passed = true;
        //Run through and check to make sure all are in the label.
        for (int i = 0; i < characteristics.length; i++) {
            if (label.getAttribute(characteristics[i]) == null) {
                passed = false;
                log.error("Label is missing element " + characteristics[i]);
            }
        }
        return passed;
    }

}
