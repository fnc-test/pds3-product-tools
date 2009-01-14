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
    public final static String [] INCLUDE_NAMES = {"STRUCTURE"};
    public final static String [] CATALOG_NAMES = {"DATA_SET_CATALOG", "DATA_SET_COLLECTION_CATALOG",
    	"INSTRUMENT_CATALOG", "INSTRUMENT_HOST_CATALOG", "MISSION_CATALOG", "PERSONNEL_CATALOG",
    	"REFERENCE_CATALOG", "SOFTWARE_CATALOG", "TARGET_CATALOG"};
    public final static String CATALOG = "CATALOG";
}
