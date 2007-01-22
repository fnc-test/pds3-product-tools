//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public interface PointerType {
    public final static int DATA_LOCATION = 0;
    public final static int INCLUDE = 1;
    public final static int DESCRIPTION = 2;
    public final static int UNDEFINED = -1;
    public final static String [] DESCRIPTION_NAMES = {"DESCRIPTION", "DESC"};
    public final static String [] INCLUDE_NAMES = {"STRUCTURE", "CATALOG", "MAP_PROJECTION"};
}
