//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.dict.parser;

import java.io.IOException;
import java.net.URL;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import antlr.collections.AST;
import gov.nasa.pds.tools.label.antlr.ODLTokenTypes;
import gov.nasa.pds.tools.label.antlr.ODLLexer;
import gov.nasa.pds.tools.label.antlr.ODLParser;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Iterator;

import gov.nasa.pds.tools.dict.Definition;
import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.dict.DictionaryTokens;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.Scalar;
import gov.nasa.pds.tools.label.Statement;
import gov.nasa.pds.tools.label.Sequence;
import gov.nasa.pds.tools.label.CommentStatement;
import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.tools.label.StatementFactory;
import gov.nasa.pds.tools.label.parser.ParseException;


/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class DictionaryParser implements ODLTokenTypes, DictionaryTokens {
    private static Logger log = Logger.getLogger(new DictionaryParser().getClass().getName());
    
    public static Dictionary parse(URL file) throws ParseException, IOException {
        Dictionary dictionary = new Dictionary();
        ODLLexer lexer = new ODLLexer(file.openStream());
        ODLParser parser = new ODLParser(lexer);
        try {
            //Attempt to parse a dictionary
            List labels = parser.dictionary();
            
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
                            if (ALIAS_LIST.equals(statement.getIdentifier())) 
                                aliases = generateAliases((ObjectStatement) statement);
                            else if (UNIT_LIST.equals(statement.getIdentifier()))
                                units = generateUnits((ObjectStatement) statement);
                            else
                                definitions.add(DefinitionFactory.createDefinition((ObjectStatement) statement));
                        }
                    }
                }
                
                dictionary.setUnits(units);
                
                //Go through the definitions and set all aliases that were found
                for (Iterator i = definitions.iterator(); i.hasNext();) {
                    Definition d = (Definition) i.next();
                    if (aliases.containsKey(d.getIdentifier())) {
                        d.addAliases((List) aliases.get(d.getIdentifier()));
                    }
                }
                
                //Add all definitions to the dictionary
                dictionary.addDefinitions(definitions);
            }
            //TODO: Update to catch thrown exception not all exceptions
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new ParseException(ex.getMessage());
        }

        return dictionary;
    }
    
    private static Map generateAliases(ObjectStatement object) {
        Map aliases = new HashMap();
        
        //Process object aliases
        AttributeStatement objectAliases = object.getAttribute(OBJECT_ALIASES);
        if (objectAliases != null) {
            for (Iterator i = ((Sequence) objectAliases.getValue()).iterator(); i.hasNext();) {
                Sequence values = (Sequence) i.next();
                for (Iterator v = values.iterator(); v.hasNext();) {
                    String value = ((Scalar) v.next()).getValue();
                    aliases.put(value, values);
                }
            }
        }
        
        //Process element aliases
        AttributeStatement elementAliases = object.getAttribute(ELEMENT_ALIASES);
        if (elementAliases != null) {
            for (Iterator i = ((Sequence) elementAliases.getValue()).iterator(); i.hasNext();) {
                Sequence values = (Sequence) i.next();
                for (Iterator v = values.iterator(); v.hasNext();) {
                    String value = ((Scalar) v.next()).getValue();
                    aliases.put(value, values);
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
