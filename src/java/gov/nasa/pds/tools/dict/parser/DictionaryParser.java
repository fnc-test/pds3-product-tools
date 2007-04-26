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

package gov.nasa.pds.tools.dict.parser;

import gov.nasa.pds.tools.dict.Definition;
import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.dict.DictionaryTokens;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.CommentStatement;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.tools.label.Sequence;
import gov.nasa.pds.tools.label.Statement;
import gov.nasa.pds.tools.label.antlr.ODLLexer;
import gov.nasa.pds.tools.label.antlr.ODLParser;
import gov.nasa.pds.tools.label.antlr.ODLTokenTypes;
import gov.nasa.pds.tools.label.parser.ParseException;
import gov.nasa.pds.tools.label.validate.Status;
import gov.nasa.pds.tools.logging.ToolsLogRecord;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class provides the means to parse a PDS compliant data dictionary. 
 * The {@link Dictionary} created can be used for validation purposes or just 
 * to examine the contents programmatically. To parse a dictionary use the following:
 * <p>
 * <code>
 * Dictionary dictionary = DictionaryParser.parse(new URL("<url to dictionar>"));
 * </code>
 * <p>If you wanted to turn of aliases the alternative parse method could be used:
 * <p>
 * <code>
 * Dictionary dictionary = DictionaryParser.parse(new URL("<url to dictionar>"), false);
 * </code>
 * 
 * @author pramirez
 * @version $Revision$
 * 
 */
public class DictionaryParser implements ODLTokenTypes, DictionaryTokens, Status {
    private static Logger log = Logger.getLogger(DictionaryParser.class.getName());
    
    /**
     * Parses a {@link URL} that is compliant with the PDS Data Dictionary document
     * and formulates a {@link Dictionary} with aliases turned off.
     * @param url points to the location of the dictionary
     * @return a data dictionary with element, group, and object definitions
     * @throws ParseException thrown when dictionary can not be parsed correctly
     * @throws IOException thrown when dictionary can not be accessed
     */
    public static Dictionary parse(URL url) throws ParseException, IOException {
        return parse(url, false);
    }
    
    /**
     * Parses a {@link URL} that is compliant with the PDS Data Dictionary document
     * and formulates a {@link Dictionary} with a flag to indicated whether aliases
     * should be read in.
     * @param url points to the location of the dictionary
     * @param aliasing indicates if aliases should be read in
     * @return a data dictionary with element, group, and object definitions
     * @throws ParseException thrown when dictionary can not be parsed correctly
     * @throws IOException thrown when dictionary can not be accessed
     */
    public static Dictionary parse(URL url, boolean aliasing) throws ParseException, IOException {
        Dictionary dictionary = new Dictionary();
        InputStream input = url.openStream();
        ODLLexer lexer = new ODLLexer(input);
        lexer.setFilename(url.toString());
        ODLParser parser = new ODLParser(lexer);
        parser.setFilename(url.toString());
        
        log.log(new ToolsLogRecord(Level.INFO, "Parsing dictionary.", url.toString()));
        try {
            List labels = new ArrayList();
            dictionary.setStatus(PASS);
            //Attempt to parse a dictionary
            while (input.available() > 0) {
                Label label = parser.label();
                dictionary.setStatus(lexer.getStatus());
                dictionary.setStatus(parser.getStatus());
                if (label != null)
                    labels.add(label);
            }
            
            if (labels != null && labels.size() > 0) {
                //First grab off the comments at the top of the dictionary as
                //this could include version information. 
                Label headerLabel = (Label) labels.get(0);
                
                StringBuffer information = new StringBuffer(); 
                //TODO: Sort list of statements that way comments get appended in proper order
                for (Iterator i = headerLabel.getStatements().iterator(); i.hasNext();) {
                    Statement statement = (Statement) i.next();
                    if (statement instanceof CommentStatement)
                        information.append(((CommentStatement) statement).getComment() + "\n");
                }
                dictionary.setInformation(information.toString());
                
                //Now process the objects in the rest of the file.
                //These objects can be an alias list, units list, or
                //definition (element, object, or group)
                Map definitions = new HashMap();
                Map aliases = new HashMap();
                Map units = new HashMap();
                
                //Go through statements in the label and start formulating the different parts of 
                //the dictinary. Aliases, units, and definitions will be pulled out.
                for (Iterator i = labels.iterator(); i.hasNext();) {
                    for (Iterator s = ((Label) i.next()).getStatements().iterator(); s.hasNext();) {
                        Statement statement = (Statement) s.next();
                        if (statement instanceof ObjectStatement) {
                            if (ALIAS_LIST.equals(statement.getIdentifier())) {
                                if (aliasing)
                                    aliases = generateAliases((ObjectStatement) statement);
                            } else if (UNIT_LIST.equals(statement.getIdentifier()))
                                units = generateUnits((ObjectStatement) statement);
                            else {
                                Definition definition = DefinitionFactory.createDefinition((ObjectStatement) statement);
                                definitions.put(definition.getIdentifier(), definition);
                            }
                        }
                    }
                }
                
                //Put units in the dictionary
                dictionary.setUnits(units);
                
                //If aliasing is turned on then they need to added to the definitions
                if (aliasing) {
                    //Go through the aliases and add to appropriate deifnitions
                    for (Iterator i = aliases.keySet().iterator(); i.hasNext();) {
                        String alias = i.next().toString();
                        Definition d = (Definition) definitions.get(aliases.get(alias));
                        d.addAlias(alias);
                    }
                }
                
                //Add all definitions to the dictionary
                dictionary.addDefinitions(definitions.values());
            }
            //TODO: Update to catch thrown exception not all exceptions
        } catch (Exception ex) {
            dictionary.setStatus(FAIL);
            log.log(new ToolsLogRecord(Level.SEVERE, ex.getMessage(), url.toString()));
            throw new ParseException(ex.getMessage());
        }
        
        log.log(new ToolsLogRecord(Level.INFO, "Finshed parsing dictionary.", url.toString()));

        return dictionary;
    }
    
    private static Map generateAliases(ObjectStatement object) {
        Map aliases = new HashMap();
        
        //Process object aliases
        //They take the form (alias, identifier)
        AttributeStatement objectAliases = object.getAttribute(OBJECT_ALIASES);
        if (objectAliases != null) {
            for (Iterator i = ((Sequence) objectAliases.getValue()).iterator(); i.hasNext();) {
                Sequence values = (Sequence) i.next();
                if (values.size() == 2) {
                    String alias = values.get(0).toString();
                    String identifier = values.get(1).toString();
                    aliases.put(alias, identifier);
                }
            }
        }
        
        //Process element aliases
        //They take the form (alias, object, identifier)
        AttributeStatement elementAliases = object.getAttribute(ELEMENT_ALIASES);
        if (elementAliases != null) {
            for (Iterator i = ((Sequence) elementAliases.getValue()).iterator(); i.hasNext();) {
                Sequence values = (Sequence) i.next();
                if (values.size() == 3) {
                    String alias = values.get(0).toString();
                    String objectContext = values.get(1).toString();
                    String identifier = values.get(2).toString();
                    aliases.put(objectContext + "." + alias, identifier);
                }
            }
        }
        
        return aliases;
    }
    
    private static Map generateUnits(ObjectStatement object) {
        Map units = new HashMap();
        //Process unit lists 
        //They are just are just a list of lists of valid units (('A','ampere'), ('A/m', 'ampere/meter') ....)
        AttributeStatement unitSequence = object.getAttribute(UNIT_SEQUENCE);
        if (unitSequence != null) {
            for (Iterator i = ((Sequence) unitSequence.getValue()).iterator(); i.hasNext();) {
                List unitList = new ArrayList();
                Sequence values = (Sequence) i.next();
                for (Iterator v = values.iterator(); v.hasNext();) {
                    String unit = v.next().toString();
                    unitList.add(unit);
                    units.put(unit, unitList);
                }
            }
        }
        return units;
    }
    
    public static void main(String [] args) throws Exception {
        DictionaryParser.parse(new URL(args[0]));
    }
}
