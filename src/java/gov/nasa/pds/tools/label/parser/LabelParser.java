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

package gov.nasa.pds.tools.label.parser;

import java.net.URL;

import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.validate.LabelValidator;

import java.util.Properties;
import java.io.IOException;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public interface LabelParser {
	public static final String CATALOG = "CATALOG";
    public static final String LABEL = "LABEL";
    public static final String PRODUCT = "PRODUCT";
	
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
     * @param skipVersion
     * @return {@link Label} representation of the file
     */
    public Label parse(URL file, Dictionary dictionary, boolean dataObjectValidation) throws ParseException, IOException;
    
    /**
     * Parses the given partial label.
     * @param file
     * @return {@link Label} representation of the file
     * @throws ParseException
     * @throws IOException
     */
    public Label parsePartial(URL file) throws ParseException, IOException;
    
    /**
     * Parses the given partial label.
     * @param context
     * @param file
     * @return {@link Label} representation of the file
     * @throws ParseException
     * @throws IOException
     */
    public Label parsePartial(String context, URL file) throws ParseException, IOException;
    
    /**
     * Parses the given partial label and validates against the dictionary.
     * @param file
     * @param dictionary
     * @return {@link Label} representation of the file
     * @throws ParseException
     * @throws IOException
     */
    public Label parsePartial(URL file, Dictionary dictionary) throws ParseException, IOException;
    
    /**
     * Parses the given partial label, validates against dictionary, and may perform dataObjectValidation
     * @param file
     * @param dictionary
     * @param skipVersion
     * @return {@link Label} representation of the file
     * @throws ParseException
     * @throws IOException
     */
    public Label parsePartial(URL file, Dictionary dictionary, boolean skipVersion) throws ParseException, IOException;
    
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
     * Returns the version of ODL that this parser is complient with.
     * @return The ODL version string
     */
    public String getODLVersion();
    
    /**
     * Adds an URL where references will be searched for when found in a label.
     * @param includePath points to a directory that will be searched.
     */
    public void addIncludePath(URL includePath);
    
    /**
     * Adds a {@link LabelValidator} that will perform some extra validation.
     * @param validator which will be run as a step in the validation pipeline
     */
    public void addLabelValidator(LabelValidator validator);
    
    /**
     * Adds a {@link LabelValidator} that will perform some extra validation when 
     * validating a label fragment.
     * @param validator which will be run as a step in the validation pipeline
     */
    public void addFragmentValidator(LabelValidator validator);
}
