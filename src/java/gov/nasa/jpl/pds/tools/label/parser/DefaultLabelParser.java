//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools.label.parser;

import gov.nasa.jpl.pds.tools.dict.Dictionary;
import gov.nasa.jpl.pds.tools.label.Label;
import java.net.URL;
import java.util.Properties;
import org.apache.log4j.Logger;
import gov.nasa.jpl.pds.tools.label.antlr.ODLLexer;
import gov.nasa.jpl.pds.tools.label.antlr.ODLParser;
import java.io.IOException;
import gov.nasa.jpl.pds.tools.label.parser.ParseException;
import antlr.collections.AST;
import gov.nasa.jpl.pds.tools.label.StatementFactory;

/**
 * Default implementation
 * @author pramirez
 * @version $Revision$
 * 
 */
public class DefaultLabelParser implements LabelParser {
    private Properties properties;
    private Logger log;
    
    public DefaultLabelParser() {
        properties = new Properties();
        log = Logger.getLogger(this.getClass().getName());
    }

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.parser.LabelParser#parse(java.net.URL)
     */
    public Label parse(URL file) throws ParseException, IOException {
        ODLLexer lexer = new ODLLexer(file.openStream());
        ODLParser parser = new ODLParser(lexer);
        try {
            parser.label();
            AST ast = parser.getAST();
            Label label = new Label();
            while (ast != null) {
                label.addStatement(StatementFactory.createStatement(ast));
                ast = ast.getNextSibling();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new ParseException(ex.getMessage());
        }

        return null;
    }

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.parser.LabelParser#parse(java.net.URL, gov.nasa.jpl.pds.tools.dict.Dictionary)
     */
    public Label parse(URL file, Dictionary dictionary) throws ParseException, IOException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.parser.LabelParser#parse(java.net.URL, gov.nasa.jpl.pds.tools.dict.Dictionary, boolean)
     */
    public Label parse(URL file, Dictionary dictionary,
            boolean dataObjectValidation)  throws ParseException, IOException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.parser.LabelParser#setLogger(java.util.logging.Logger)
     */
    public void setLogger(Logger log) {
        this.log = log;
    }

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.parser.LabelParser#getLogger()
     */
    public Logger getLogger() {
        return log;
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
    
    public static void main(String [] args) throws Exception {
        DefaultLabelParser parser = new DefaultLabelParser();
        parser.parse(new URL(args[0]));
    }

}
