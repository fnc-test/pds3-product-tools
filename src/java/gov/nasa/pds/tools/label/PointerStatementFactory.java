//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class PointerStatementFactory {
    public static PointerStatement newInstance(int line, String identifier, Value value, URL base) throws MalformedURLException {
        if (identifier.endsWith(StructurePointer.IDENTIFIER))
            return new StructurePointer(line, identifier, value, base);
        return new PointerStatement(line, identifier, value);
    }
}
