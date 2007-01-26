//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label;

import java.net.MalformedURLException;

/**
 * This class hides the construction of pointers. It helps determine the exact type of pointer and
 * contstructs it. If modifications need to be made to this behavior it will be hidden here.
 * 
 * @author pramirez
 * @version $Revision$
 * 
 */
public class PointerStatementFactory implements PointerType {
    public static PointerStatement newInstance(int line, String identifier, Value value) throws MalformedURLException {
        //Essentially the pointer will be an internal or external pointer
        //The two classes of external pointers we need to differentiate between are structure pointers which
        //should allow us to load statments and just external references where we only care of the existence
        //of the file which is pointed too
        
        if (value instanceof TextString) {
            for (int i = 0; i < INCLUDE_NAMES.length; i++) {
                if (identifier.endsWith(INCLUDE_NAMES[i]))
                    return new IncludePointer(line, identifier, value);
            }
            return new ExternalPointer(resolvePointerType(identifier), line, identifier, value);
        } else if (value instanceof Sequence) {
            return new ExternalPointer(resolvePointerType(identifier), line, identifier, value);
        }
        else
            return new PointerStatement(resolvePointerType(identifier), line, identifier, value);
    }
    
    private static int resolvePointerType(String identifier) {
        int pointerType = UNDEFINED;
        for (int i = 0; i < DESCRIPTION_NAMES.length && pointerType == UNDEFINED; i++) {
            if (identifier.endsWith(DESCRIPTION_NAMES[i]))
                pointerType = DESCRIPTION;
        }
        if (pointerType == UNDEFINED)
            pointerType = DATA_LOCATION;
        
        return pointerType;
    }
}
