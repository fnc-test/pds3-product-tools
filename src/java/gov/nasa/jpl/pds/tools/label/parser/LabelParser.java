//Copyright 2006, by the California Institute of 
//Technology. ALL RIGHTS RESERVED. United States Government 
//Sponsorship acknowledged. Any commercial use must be negotiated with 
//the Office of Technology Transfer at the California Institute of 
//Technology.
//
//This software may be subject to U.S. export control laws. By 
//accepting this software, the user agrees to comply with all 
//applicable U.S. export laws and regulations. User has the 
//responsibility to obtain export licenses, or other export authority 
//as may be required before exporting such information to foreign 
//countries or providing access to foreign persons.
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
     * @return {@link Label} representation of the file
     */
    public Label parse(URL file, Dictionary dictionary) throws ParseException, IOException;
    
    /**
     * Parses the given file, validates against dictionary, and may perform dataObjectValidation
     * @param file
     * @param dictionary
     * @param dataObjectValidation
     * @return {@link Label} representation of the file
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
     * @param properties Set of properties.
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
