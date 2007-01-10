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
public class PointerStatementFactory {
    public static PointerStatement newInstance(int line, String identifier, Value value) throws MalformedURLException {
        //Essentially the pointer will be an internal or external pointer
        //The two classes of external pointers we need to differentiate between are structure pointers which
        //should allow us to load statments and just external references where we only care of the existence
        //of the file which is pointed too
        if (identifier.endsWith(StructurePointer.IDENTIFIER))
            return new StructurePointer(line, identifier, value);
        else if (value instanceof TextString || value instanceof Sequence)
            return new ExternalPointer(line, identifier, value);
        
        //We have just found a plain old pointer.
        return new PointerStatement(line, identifier, value);
    }
}
