//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label.validate;

import java.util.Iterator;

import org.apache.log4j.Logger;

import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.LabelType;
import gov.nasa.pds.tools.label.ObjectStatement;

/**
 * This class validates that all file characteristics are found in a label;
 * @author pramirez
 * @version $Revision$
 * 
 */
public class FileCharacteristicValidator implements LabelValidator, LabelType, RecordType {
    private static Logger log = Logger.getLogger(ElementValidator.class.getName());
    //This is the set of predefined required file characteristic elements
    private final static String RECORD_TYPE = "RECORD_TYPE";
    private final static String RECORD_BYTES = "RECORD_BYTES";
    private final static String FILE_RECORDS = "FILE_RECORDS";
    private final static String LABEL_RECORDS = "LABEL_RECORDS";

    /** (non-Javadoc)
     * @see gov.nasa.pds.tools.label.validate.LabelValidator#isValid(gov.nasa.pds.tools.label.Label)
     */
    public boolean isValid(Label label) {
        if (label.getLabelType() == LabelType.UNDEFINED) {
            log.warn("File characteristics will not be checked as the type of label is UNDEFINED.");
            return true;
        }
        else if (label.getLabelType() == COMBINED_DETACHED)
            return checkCombinedDetached(label);
        else if (label.getLabelType() == ATTACHED || label.getLabelType() == DETACHED){
            if (label.getAttribute(RECORD_TYPE) == null) {
                log.error("Label does not contain the required RECORD_TYPE element.");
                return false;
            }
            
            String recordType = label.getAttribute(RECORD_TYPE).getValue().toString();
            if (!FIXED_LENGTH.equals(recordType) && !VARIABLE_LENGTH.equals(recordType) && 
                    !STREAM.equals(recordType) && !RecordType.UNDEFINED.equals(recordType)) {
                log.warn("Could not determine RECORD_TYPE file characteristics will not be checked");
                return false;
            }
            
            if (RecordType.UNDEFINED.equals(recordType)) {
                return true;
            }
            
            if (label.getLabelType() == ATTACHED)
                return checkAttached(label);
            else if (label.getLabelType() == DETACHED)
                return checkDetached(label);
        } else
            log.warn("Could not check file characteristics as the type of label could not be determined.");
        
        return false;
    }

    private boolean checkAttached(Label label) {
        boolean pass = true;
        String recordType = label.getAttribute(RECORD_TYPE).getValue().toString();
        if (FIXED_LENGTH.equals(recordType) || VARIABLE_LENGTH.equals(recordType)) {
            if (label.getAttribute(RECORD_BYTES) == null) {
                log.error("Label does not contain required element " + RECORD_BYTES);
                pass = false;
            }
            if (label.getAttribute(FILE_RECORDS) == null) {
                log.error("Label does not contain required element " + FILE_RECORDS);
                pass = false;
            }
            if (label.getAttribute(LABEL_RECORDS) == null) {
                log.error("Label does not contain required element " + LABEL_RECORDS);
                pass = false;
            }
        } 
        
        return pass;
    }
    
    private boolean checkDetached(Label label) {
        boolean pass = true;
        String recordType = label.getAttribute(RECORD_TYPE).getValue().toString();
        if (FIXED_LENGTH.equals(recordType) || VARIABLE_LENGTH.equals(recordType)) {
            if (label.getAttribute(RECORD_BYTES) == null) {
                log.error("Label does not contain required element " + RECORD_BYTES);
                pass = false;
            }
            if (label.getAttribute(FILE_RECORDS) == null) {
                log.error("Label does not contain required element " + FILE_RECORDS);
                pass = false;
            }
        } 
        return pass;
    }
    
    private boolean checkCombinedDetached(Label label) {
        boolean pass = true;
        
        for (Iterator i = label.getObjects("FILE").iterator(); i.hasNext();) {
            ObjectStatement fileObject = (ObjectStatement) i.next();
            pass = checkFileObject(fileObject);
        }
        
        return pass;
    }
    
    private boolean checkFileObject(ObjectStatement object) {
        boolean pass = true;
        
        if (object.getAttribute(RECORD_TYPE) == null) {
            log.error("File object does not contain the required RECORD_TYPE element.");
            return false;
        }
        
        String recordType = object.getAttribute(RECORD_TYPE).getValue().toString();
        if (!FIXED_LENGTH.equals(recordType) && !VARIABLE_LENGTH.equals(recordType) && 
                !STREAM.equals(recordType) && !RecordType.UNDEFINED.equals(recordType)) {
            log.warn("Could not determine RECORD_TYPE file characteristics will not be checked");
            return false;
        }
        
        if (FIXED_LENGTH.equals(recordType) || VARIABLE_LENGTH.equals(recordType)) {
            if (object.getAttribute(RECORD_BYTES) == null) {
                log.error("File object does not contain required element " + RECORD_BYTES);
                pass = false;
            }
            if (object.getAttribute(FILE_RECORDS) == null) {
                log.error("File object does not contain required element " + FILE_RECORDS);
                pass = false;
            }
        } 
        return pass;
    }
}
