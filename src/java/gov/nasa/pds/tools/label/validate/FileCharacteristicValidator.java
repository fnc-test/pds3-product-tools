// Copyright 2006-2007, by the California Institute of Technology.
// ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
// Any commercial use must be negotiated with the Office of Technology Transfer
// at the California Institute of Technology.
//
// This software is subject to U. S. export control laws and regulations
// (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
// is subject to U.S. export control laws and regulations, the recipient has
// the responsibility to obtain export licenses or other export authority as
// may be required before exporting such information to foreign countries or
// providing access to foreign nationals.
//
// $Id$ 
//

package gov.nasa.pds.tools.label.validate;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.LabelType;
import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.tools.logging.ToolsLogRecord;

/**
 * This class validates that all file characteristics are found in a label;
 * @author pramirez
 * @version $Revision$
 * 
 */
public class FileCharacteristicValidator implements LabelValidator, LabelType, RecordType {
    private static Logger log = Logger.getLogger(FileCharacteristicValidator.class.getName());
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
            log.log(new ToolsLogRecord(Level.WARNING, "File characteristics will not be checked as the type of label is UNDEFINED.", label.getFilename()));
            return true;
        }
        else if (label.getLabelType() == COMBINED_DETACHED)
            return checkCombinedDetached(label);
        else if (label.getLabelType() == ATTACHED || label.getLabelType() == DETACHED){
            if (label.getAttribute(RECORD_TYPE) == null) {
                log.log(new ToolsLogRecord(Level.SEVERE, "Label does not contain the required RECORD_TYPE element.", label.getFilename()));
                return false;
            }
            
            String recordType = label.getAttribute(RECORD_TYPE).getValue().toString();
            if (!FIXED_LENGTH.equals(recordType) && !VARIABLE_LENGTH.equals(recordType) && 
                    !STREAM.equals(recordType) && !RecordType.UNDEFINED.equals(recordType)) {
                log.log(new ToolsLogRecord(Level.WARNING, "Could not determine RECORD_TYPE file characteristics will not be checked", label.getFilename()));
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
            log.log(new ToolsLogRecord(Level.WARNING, "Could not check file characteristics as the type of label could not be determined.", label.getFilename()));
        
        return false;
    }

    private boolean checkAttached(Label label) {
        boolean pass = true;
        String recordType = label.getAttribute(RECORD_TYPE).getValue().toString();
        if (FIXED_LENGTH.equals(recordType) || VARIABLE_LENGTH.equals(recordType)) {
            if (label.getAttribute(RECORD_BYTES) == null) {
                log.log(new ToolsLogRecord(Level.SEVERE, "Label does not contain required element " + RECORD_BYTES, label.getFilename()));
                pass = false;
            }
            if (label.getAttribute(FILE_RECORDS) == null) {
                log.log(new ToolsLogRecord(Level.SEVERE, "Label does not contain required element " + FILE_RECORDS, label.getFilename()));
                pass = false;
            }
            if (label.getAttribute(LABEL_RECORDS) == null) {
                log.log(new ToolsLogRecord(Level.SEVERE, "Label does not contain required element " + LABEL_RECORDS, label.getFilename()));
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
                log.log(new ToolsLogRecord(Level.SEVERE, "Label does not contain required element " + RECORD_BYTES, label.getFilename()));
                pass = false;
            }
            if (label.getAttribute(FILE_RECORDS) == null) {
                log.log(new ToolsLogRecord(Level.SEVERE, "Label does not contain required element " + FILE_RECORDS, label.getFilename()));
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
            log.log(new ToolsLogRecord(Level.SEVERE, "File object does not contain the required RECORD_TYPE element.", 
                    object.getFilename(), object.getContext(), object.getLineNumber()));
            return false;
        }
        
        String recordType = object.getAttribute(RECORD_TYPE).getValue().toString();
        if (!FIXED_LENGTH.equals(recordType) && !VARIABLE_LENGTH.equals(recordType) && 
                !STREAM.equals(recordType) && !RecordType.UNDEFINED.equals(recordType)) {
            log.log(new ToolsLogRecord(Level.WARNING, "Could not determine RECORD_TYPE file characteristics will not be checked", 
                    object.getFilename(), object.getContext(), object.getLineNumber()));
            return false;
        }
        
        if (FIXED_LENGTH.equals(recordType) || VARIABLE_LENGTH.equals(recordType)) {
            if (object.getAttribute(RECORD_BYTES) == null) {
                log.log(new ToolsLogRecord(Level.SEVERE, "File object does not contain required element " + RECORD_BYTES, 
                        object.getFilename(), object.getContext(), object.getLineNumber()));
                pass = false;
            }
            if (object.getAttribute(FILE_RECORDS) == null) {
                log.log(new ToolsLogRecord(Level.SEVERE, "File object does not contain required element " + FILE_RECORDS, 
                        object.getFilename(), object.getContext(), object.getLineNumber()));
                pass = false;
            }
        } 
        return pass;
    }
}
