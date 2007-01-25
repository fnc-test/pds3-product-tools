//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
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
 * @author pramirez
 * @version $Revision$
 * 
 */
public class DictionaryParser implements ODLTokenTypes, DictionaryTokens {
    private static Logger log = Logger.getLogger(DictionaryParser.class.getName());
    
    public static Dictionary parse(URL file) throws ParseException, IOException {
        return parse(file, false);
    }
    
    public static Dictionary parse(URL file, boolean aliasing) throws ParseException, IOException {
        Dictionary dictionary = new Dictionary();
        InputStream input = file.openStream();
        ODLLexer lexer = new ODLLexer(input);
        ODLParser parser = new ODLParser(lexer);
        
        log.log(new ToolsLogRecord(Level.INFO, "Parsing dictionary.", file.toString()));
        try {
            List labels = new ArrayList();
            //Attempt to parse a dictionary
            while (input.available() > 0) {
                Label label = parser.label();
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
                List definitions = new ArrayList();
                Map aliases = new HashMap();
                Map units = new HashMap();
                
                for (Iterator i = labels.iterator(); i.hasNext();) {
                    for (Iterator s = ((Label) i.next()).getStatements().iterator(); s.hasNext();) {
                        Statement statement = (Statement) s.next();
                        if (statement instanceof ObjectStatement) {
                            if (ALIAS_LIST.equals(statement.getIdentifier())) {
                                if (aliasing)
                                    aliases = generateAliases((ObjectStatement) statement);
                            } else if (UNIT_LIST.equals(statement.getIdentifier()))
                                units = generateUnits((ObjectStatement) statement);
                            else
                                definitions.add(DefinitionFactory.createDefinition((ObjectStatement) statement));
                        }
                    }
                }
                
                dictionary.setUnits(units);
                if (aliasing) {
                    //Go through the definitions and set all aliases that were found
                    for (Iterator i = definitions.iterator(); i.hasNext();) {
                        Definition d = (Definition) i.next();
                        if (aliases.containsKey(d.getIdentifier())) {
                            d.addAlias(aliases.get(d.getIdentifier()).toString());
                        }
                    }
                }
                
                //Add all definitions to the dictionary
                dictionary.addDefinitions(definitions);
            }
            //TODO: Update to catch thrown exception not all exceptions
        } catch (Exception ex) {
            log.log(new ToolsLogRecord(Level.SEVERE, ex.getMessage()));
            throw new ParseException(ex.getMessage());
        }
        
        log.log(new ToolsLogRecord(Level.INFO, "Finshed parsing dictionary.", file.toString()));

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
                    aliases.put(identifier, alias);
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
                    aliases.put(identifier, objectContext + "." + alias);
                }
            }
        }
        
        return aliases;
    }
    
    private static Map generateUnits(ObjectStatement object) {
        //FIXME: support
        return new HashMap();
    }
    
    public static void main(String [] args) throws Exception {
        DictionaryParser.parse(new URL(args[0]));
    }
}
