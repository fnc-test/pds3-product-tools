//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label.parser;

import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.dict.parser.DictionaryParser;
import gov.nasa.pds.tools.dict.type.UnsupportedTypeException;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.GroupStatement;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.MalformedSFDULabel;
import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.tools.label.SFDULabel;
import gov.nasa.pds.tools.label.Statement;
import gov.nasa.pds.tools.label.antlr.ODLLexer;
import gov.nasa.pds.tools.label.antlr.ODLParser;
import gov.nasa.pds.tools.label.validate.DefinitionNotFoundException;
import gov.nasa.pds.tools.label.validate.ElementValidator;
import gov.nasa.pds.tools.label.validate.GroupValidator;
import gov.nasa.pds.tools.label.validate.ObjectValidator;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import antlr.ANTLRException;

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
        InputStream input = file.openStream();
        
        //Set a place where the input stream can be reset to.
        input.mark(40);      
        List sfdus = consumeSFDUHeader(input);
        for (Iterator i = sfdus.iterator(); i.hasNext();) {
            log.info("Found SFDU Label: " + i.next().toString());
        }
        
        ODLLexer lexer = new ODLLexer(input);
        ODLParser parser = new ODLParser(lexer);
        try {
            label = parser.label();
        } catch (ANTLRException ex) {
            log.error(ex.getMessage());
            throw new ParseException(ex.getMessage());
        }

        return label;
    }
    
    private List consumeSFDUHeader(InputStream input) throws IOException {
        List sfdus = new ArrayList();
        boolean foundHeader = false;
        
        byte[] sfduLabel = new byte[20];
        int count = input.read(sfduLabel);
        if (count == 20) {
            try {
                SFDULabel sfdu = new SFDULabel(sfduLabel);
                if ("CCSD".equals(sfdu.getControlAuthorityId())) {
                    foundHeader = true;
                    sfdus.add(sfdu);
                    //Read in second SFDU label
                    input.read(sfduLabel);
                    sfdus.add(new SFDULabel(sfduLabel));
                }
            } catch (MalformedSFDULabel e) {
                foundHeader = false;
            }
            
        }
        
        if (!foundHeader)
            input.reset();
        
        return sfdus;
    }

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.parser.LabelParser#parse(java.net.URL, gov.nasa.jpl.pds.tools.dict.Dictionary)
     */
    public Label parse(URL file, Dictionary dictionary) throws ParseException, IOException {
        Label label = null;
        
        //First parse the file and get back the label object
        label = parse(file);
        
        //Check all the statements
        List statements = label.getStatements();
        Collections.sort(statements);
        for (Iterator i = statements.iterator(); i.hasNext();) {
            Statement statement = (Statement) i.next();
            if (statement instanceof AttributeStatement) {
                try {
                    ElementValidator.isValid(dictionary, (AttributeStatement) statement);
                } catch (DefinitionNotFoundException dnfe) {
                    log.error("line " + statement.getLineNumber() + ": " + dnfe.getMessage());
                } catch (UnsupportedTypeException ute) {
                    log.error("line " + statement.getLineNumber() + ": " + ute.getMessage());
                }
            } else if (statement instanceof ObjectStatement) {
                try {
                    ObjectValidator.isValid(dictionary, (ObjectStatement) statement);
                } catch (DefinitionNotFoundException dnfe) {
                    log.error("line " + statement.getLineNumber() + ": " + dnfe.getMessage());
                }
            } else if (statement instanceof GroupStatement) {
                try {
                    GroupValidator.isValid(dictionary, (GroupStatement) statement);
                } catch (DefinitionNotFoundException dnfe) {
                    log.error("line " + statement.getLineNumber() + ": " + dnfe.getMessage());
                }
            }
        }
        
        return label;
    }

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.parser.LabelParser#parse(java.net.URL, gov.nasa.jpl.pds.tools.dict.Dictionary, boolean)
     */
    public Label parse(URL file, Dictionary dictionary, boolean dataObjectValidation)  throws ParseException, IOException {
        // TODO Auto-generated method stub
        return parse(file, dictionary);
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
        if (args.length == 1)
            parser.parse(new URL(args[0]));
        else
            parser.parse(new URL(args[0]), DictionaryParser.parse(new URL(args[1])));
    }

}
