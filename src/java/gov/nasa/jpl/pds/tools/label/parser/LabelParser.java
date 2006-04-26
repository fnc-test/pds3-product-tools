//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools.label.parser;

import java.net.URL;
import gov.nasa.jpl.pds.tools.label.Label;
import gov.nasa.jpl.pds.tools.dict.Dictionary;
import org.apache.log4j.Logger;
import java.util.Properties;
import java.io.IOException;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public interface LabelParser {
    
    /**
     * Parses the given file
     * @param file File to 
     * @return {@link Label} representation of the file
     * @throws ParseException - If any syntatic errors are encountered.  
     *                          All errors will be written to the Logger.
     */
    public Label parse(URL file) throws ParseException, IOException;
    
    /**
     * Parses the given file and validates against the dictionary.
     * @param file 
     * @param dictionary
     * @return
     */
    public Label parse(URL file, Dictionary dictionary) throws ParseException, IOException;
    
    /**
     * Parses the given file, validates against dictionary, and may perform dataObjectValidation
     * @param file
     * @param dictionary
     * @param dataObjectValidation
     * @return
     */
    public Label parse(URL file, Dictionary dictionary, boolean dataObjectValidation) throws ParseException, IOException;
    
    /**
     * Sets the logger for which to write errors to. A default logger will be created if not set.
     * @param log The log where errors will be written
     */
    public void setLogger(Logger log);
    
    /**
     * Retrieves the logger for the parser.
     * @return The logger where errors are written
     */
    public Logger getLogger();
    
    /**
     * Passes properties to the parser. 
     * @param props Set of properties.
     */
    public void setProperties(Properties properties);
    
    /**
     * Retrieves parser properties.
     * @return parser properties
     */
    public Properties getProperties();
    
    /**
     * Returns the version of the PDS specification that this parser is compliant with.
     * @return The PDS version string
     */
    public String getPDSVersion();
    
    /**
     * Returns the version of ODL that this parser is compliant with.
     * @return The ODL version string
     */
    public String getODLVersion();
}
