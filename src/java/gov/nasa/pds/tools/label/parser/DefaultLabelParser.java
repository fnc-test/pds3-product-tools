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
import gov.nasa.pds.tools.label.validate.LabelValidator;
import gov.nasa.pds.tools.label.validate.ObjectValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    private List includePaths;
    private List validators;
    
    public DefaultLabelParser() {
        properties = new Properties();
        includePaths = new ArrayList();
        validators = new ArrayList();
    }

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.parser.LabelParser#parse(java.net.URL)
     */
    public Label parse(URL file) throws ParseException, IOException {
        Label label = null;
        
        //Not all streams can support marking so stream will be open multiple times to look for header
        //First time to process the SFDUs
        InputStream sfduCheck = file.openStream();
        
        List sfdus = consumeSFDUHeader(sfduCheck);
        for (Iterator i = sfdus.iterator(); i.hasNext();) {
            log.info("Found SFDU Label: " + i.next().toString());
        }
        
        //On the next input stream we will need to skip 20 bytes for every SFDULabel
        int skip = sfdus.size()*20;
        //Also add 2 for carriage return line feed if there is a header
        if (skip != 0)
            skip += 2;
        
        sfduCheck.close();
        
        InputStream pdsCheck = file.openStream();
        //Now look for PDS_VERSION_ID to ensure that this is a file we want to validate
        BufferedReader reader = new BufferedReader(new InputStreamReader(pdsCheck));
        reader.skip(skip);
        String version = reader.readLine().trim();
        String[] line = version.split("=");  
        
        if (line.length != 2) {
            log.warn(file.toString() + " is not a label. Could not find the PDS_VERSION_ID in the first line.");
            throw new ParseException(file.toString() + " is not a label. Could not find the PDS_VERSION_ID in the first line.");
        }
        
        String name = line[0].trim();
        String value = line[1].trim();
          
        if (!"PDS_VERSION_ID".equals(name)) {
            log.warn(file.toString() + " is not a label. Could not find the PDS_VERSION_ID in the first line.");
            throw new ParseException(file.toString() + " is not a label. Could not find the PDS_VERSION_ID in the first line.");
        }
        
        pdsCheck.close();
        InputStream input = file.openStream();
        input.skip(skip);
        ODLLexer lexer = new ODLLexer(input);
        ODLParser parser = new ODLParser(lexer);
        log.info("Parsing label " + file.toString() + " with PDS_VERSION_ID = " + value);
        
        if (Boolean.valueOf(properties.getProperty("parser.pointers", "true")).booleanValue()) {
            URL base = new URL(file.toString().substring(0, file.toString().lastIndexOf("/")));
            includePaths.add(0, base);
            parser.setIncludePaths(includePaths);
        } else {
            log.info("Pointers disabled. Pointers will not be followed.");
        }
        
        try {
            label = parser.label();
        } catch (ANTLRException ex) {
            log.error(ex.getMessage());
            throw new ParseException(ex.getMessage());
        }
        
        log.info("Finished parsing label " + file.toString());
        
        for (Iterator i = validators.iterator(); i.hasNext();) {
            LabelValidator validator = (LabelValidator) i.next();
            validator.isValid(label);
        }

        return label;
    }
    
    private List consumeSFDUHeader(InputStream input) throws IOException {
        List sfdus = new ArrayList();
        
        byte[] sfduLabel = new byte[20];
        int count = input.read(sfduLabel);
        if (count == 20) {
            try {
                SFDULabel sfdu = new SFDULabel(sfduLabel);
                if ("CCSD".equals(sfdu.getControlAuthorityId())) {
                    sfdus.add(sfdu);
                    //Read in second SFDU label
                    input.read(sfduLabel);
                    sfdus.add(new SFDULabel(sfduLabel));
                }
            } catch (MalformedSFDULabel e) {
                //For now we can ignore this error as there is likely not a header.
            }
            
        }
        
        return sfdus;
    }

    /* (non-Javadoc)
     * @see gov.nasa.jpl.pds.tools.label.parser.LabelParser#parse(java.net.URL, gov.nasa.jpl.pds.tools.dict.Dictionary)
     */
    public Label parse(URL file, Dictionary dictionary) throws ParseException, IOException {
        Label label = null;
        
        //First parse the file and get back the label object
        label = parse(file);
        
        log.info("Starting semantic validation on " + file.toString());
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
        
        log.info("Finished semantic validation on " + file.toString());
        
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
        return properties;
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

    /* (non-Javadoc)
     * @see gov.nasa.pds.tools.label.parser.LabelParser#parsePartial(java.net.URL)
     */
    public Label parsePartial(URL file) throws ParseException, IOException {
        Label label = null;
        InputStream input = file.openStream();
        ODLLexer lexer = new ODLLexer(input);
        ODLParser parser = new ODLParser(lexer);
        log.info("Parsing label fragment " + file.toString());
        
        if (Boolean.valueOf(properties.getProperty("parser.pointers", "true")).booleanValue()) {
            URL base = new URL(file.toString().substring(0, file.toString().lastIndexOf("/")));
            includePaths.add(0, base);
            parser.setIncludePaths(includePaths);
        } else {
            log.info("Pointers disabled. Pointers will not be followed");
        }
        
        try {
            label = parser.label();
        } catch (ANTLRException ex) {
            log.error(ex.getMessage());
            throw new ParseException(ex.getMessage());
        }

        log.info("Finished parsing label fragment " + file.toString());
        
        return label;
    }

    /* (non-Javadoc)
     * @see gov.nasa.pds.tools.label.parser.LabelParser#parsePartial(java.net.URL, gov.nasa.pds.tools.dict.Dictionary)
     */
    public Label parsePartial(URL file, Dictionary dictionary) throws ParseException, IOException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see gov.nasa.pds.tools.label.parser.LabelParser#parsePartial(java.net.URL, gov.nasa.pds.tools.dict.Dictionary, boolean)
     */
    public Label parsePartial(URL file, Dictionary dictionary, boolean dataObjectValidation) throws ParseException, IOException {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String [] args) throws Exception {
        BasicConfigurator.resetConfiguration();
        BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("%-5p %m%n")));
        LabelParserFactory factory = LabelParserFactory.getInstance();
        LabelParser parser = factory.newLabelParser();
        Label label = null;
        URL labelURL = null;
        URL dictionaryURL = null;
        URL includePathURL = null;
        Boolean pointers = null;
        
        if (args.length%2 == 0) {
            for (int i=0; i<args.length; i+=2) {
                if (args[i].equals("--label") || args[i].equals("--l"))
                    labelURL = new URL(args[i+1]);
                else if (args[i].equals("--dictionary") || args[i].equals("--d"))
                    dictionaryURL = new URL(args[i+1]);
                else if (args[i].equals("--include") || args[i].equals("--i"))
                    includePathURL = new URL(args[i+1]);
                else if (args[i].equals("--pointers") || args[i].equals("--p"))
                    pointers = Boolean.valueOf(args[i+1]);
                else {
                    System.out.println("Invalid flag " + args[i]);
                    System.exit(1);
                }
            }
        }
        
        if (pointers != null) {
            parser.getProperties().setProperty("parser.pointers", pointers.toString());
        }
        
        if (includePathURL != null)
            parser.addIncludePath(includePathURL);
        
        if (dictionaryURL == null)
            label = parser.parse(labelURL);
        else
            label = parser.parse(labelURL, DictionaryParser.parse(dictionaryURL));
        
        System.out.println("Label:");
        System.out.println(label.toString());
    }

	public void addIncludePath(URL includePath) {
		includePaths.add(includePath);
	}

    /* (non-Javadoc)
     * @see gov.nasa.pds.tools.label.parser.LabelParser#addValidator(gov.nasa.pds.tools.label.validate.LabelValidator)
     */
    public void addValidator(LabelValidator validator) {
        validators.add(validator);
    }

}
