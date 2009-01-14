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

package gov.nasa.pds.tools.label;

/**
 * This class hides the construction of pointers. It helps determine the exact type of pointer and
 * constructs it. If modifications need to be made to this behavior it will be hidden here.
 * 
 * @author pramirez
 * @version $Revision$
 * 
 */
public class PointerStatementFactory implements PointerType {
	
    public static PointerStatement newInstance(int line, String identifier, Value value) {
    	int pointerType = resolvePointerType(identifier, value);
    	PointerStatement newPointer = null;
    	
    	if (pointerType == DATA_LOCATION) {
    		newPointer = new DataLocationPointer(line, identifier, value);
    	} else if (pointerType == INCLUDE) {
    		if (identifier.equals(CATALOG))
    			newPointer = new CatalogPointer(line, identifier, value);
    		else 
    			newPointer = new IncludePointer(line, identifier, value);
    	} else if (pointerType == DESCRIPTION) {
    		newPointer = new DescriptionPointer(line, identifier, value);
    	} else
    		newPointer = new PointerStatement(UNDEFINED, line, identifier, value);
    	
    	return newPointer;
    }
    
    private static int resolvePointerType(String identifier, Value value) {
        int pointerType = UNDEFINED;
        for (int i = 0; i < DESCRIPTION_NAMES.length && pointerType == UNDEFINED; i++) {
            if (identifier.endsWith(DESCRIPTION_NAMES[i]))
                pointerType = DESCRIPTION;
        }
        if (value instanceof TextString) {
	        for (int i = 0; i < INCLUDE_NAMES.length && pointerType == UNDEFINED; i++) {
	        	if (identifier.endsWith(INCLUDE_NAMES[i]))
	        		pointerType = INCLUDE;
	        }
	        if (identifier.equals(CATALOG))
	        	pointerType = INCLUDE;
        }
        if (pointerType == UNDEFINED)
            pointerType = DATA_LOCATION;
        
        return pointerType;
    }
}
