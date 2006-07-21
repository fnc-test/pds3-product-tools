//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label.parser;

import java.net.URL;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;
import gov.nasa.pds.tools.label.antlr.ODLLexer;
import gov.nasa.pds.tools.label.antlr.ODLParser;
import java.io.IOException;
import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.parser.ParseException;

/**
 * Default implementation
 * @author pramirez
 * @version $Revision$
 * 
 */
public class DefaultLabelParser implements LabelParser {
    private Properties properties;
    private static Logger log = Logger.getLogger(new DefaultLabelParser().getClass());
    
    public DefaultLabelParser() {
    }

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.parser.LabelParser#parse(java.net.URL)
     */
    public Label parse(URL file) throws ParseException, IOException {
        Label label = null;
        ODLLexer lexer = new ODLLexer(file.openStream());
        ODLParser parser = new ODLParser(lexer);
        try {
            label = parser.label();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new ParseException(ex.getMessage());
        }

        return label;
    }

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.parser.LabelParser#parse(java.net.URL, gov.nasa.jpl.pds.tools.dict.Dictionary)
     */
    public Label parse(URL file, Dictionary dictionary) throws ParseException, IOException {
        // TODO Auto-generated method stub
        return parse(file);
    }

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.parser.LabelParser#parse(java.net.URL, gov.nasa.jpl.pds.tools.dict.Dictionary, boolean)
     */
    public Label parse(URL file, Dictionary dictionary,
            boolean dataObjectValidation)  throws ParseException, IOException {
        // TODO Auto-generated method stub
        return parse(file);
    }

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.parser.LabelParser#setProperties(java.util.Properties)
     */
    public void setProperties(Properties properties) {
        this.properties.putAll(properties);
    }

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.parser.LabelParser#getProperties()
     */
    public Properties getProperties() {
        return (Properties) properties.clone();
    }

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.parser.LabelParser#getPDSVersion()
     */
    public String getPDSVersion() {
        return "PDS3";
    }

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.parser.LabelParser#getODLVersion()
     */
    public String getODLVersion() {
        return "2.1";
    }
    
    /**
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String [] args) throws Exception {
        BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("%-5p %m%n")));
        LabelParserFactory factory = LabelParserFactory.getInstance();
        LabelParser parser = factory.newLabelParser();
        parser.parse(new URL(args[0]));
    }

}
