//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.logging;

import java.util.logging.Level;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class ToolsLevel extends Level {
    public static final Level NOTIFICATION = new ToolsLevel("NOTIFICATION", Level.SEVERE.intValue() + 2);
    public static final Level PARAMETER = new ToolsLevel("PARAMETER", Level.SEVERE.intValue() + 1);
    
    protected ToolsLevel(String name, int value) {
        super(name, value);
    }

}
