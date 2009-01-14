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
//

package gov.nasa.pds.tools.label.validate;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.tools.label.PointerStatement;
import gov.nasa.pds.tools.label.PointerType;
import gov.nasa.pds.tools.label.Statement;
import gov.nasa.pds.tools.logging.ToolsLogRecord;

public class CatalogNameValidator implements LabelValidator, PointerType {
    private static Logger log = Logger.getLogger(CatalogNameValidator.class.getName());

	public boolean isValid(Label label) {
		return isValid(label, new DefaultValidationListener());
	}

	public boolean isValid(Label label, ValidationListener listener) {
		boolean valid = true;
		
		if (!checkPointers(label.getPointers(), listener))
			valid = false;
		
        if (!checkPointersInObjects(label.getObjects(), listener))
        	valid = false;
        
        return valid;
	}
	
	private boolean checkPointersInObjects(List objects, ValidationListener listener) {
		boolean valid = true;
		
		for (Iterator i = objects.iterator(); i.hasNext();) {
			ObjectStatement object = (ObjectStatement) i.next();
			System.out.println("OBJECT: " + object.getIdentifier());
			if (!checkPointers(object.getPointers(), listener))
				valid = false;
			if (!checkPointersInObjects(object.getObjects(), listener))
				valid = false;
		}
		
		return valid;
	}
	
	private boolean checkPointers(List pointers, ValidationListener listener) {
		boolean valid  = true;
		
		for (Iterator i = pointers.iterator(); i.hasNext();) {
            Statement statement = (Statement) i.next();
            System.out.println("Pointer: " + statement.getIdentifier());
            
            if (statement.getIdentifier().endsWith("_" + CATALOG)) {
            	boolean found = false;
            	for (int n = 0; !found && n < CATALOG_NAMES.length; n++) {
            		if (statement.getIdentifier().equals(CATALOG_NAMES[n]))
            			found = true;
            	}
            	if (!found) {
            		listener.reportError(statement.getIdentifier() + " does not following catalog pointer naming convention.");
            		log.log(new ToolsLogRecord(Level.SEVERE, statement.getIdentifier() + " does not following catalog pointer naming convention.",
            				statement.getFilename(), statement.getContext(), statement.getLineNumber()));
            		valid = false;
            	}
            }
            
        }
        
        return valid;
	}

}
