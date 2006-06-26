//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools.dict.parser;

import java.io.IOException;
import java.net.URL;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import antlr.collections.AST;
import gov.nasa.jpl.pds.tools.label.antlr.ODLTokenTypes;
import gov.nasa.jpl.pds.tools.label.antlr.ODLLexer;
import gov.nasa.jpl.pds.tools.label.antlr.ODLParser;
import gov.nasa.jpl.pds.tools.label.parser.ParseException;
import gov.nasa.jpl.pds.tools.label.StatementFactory;
import gov.nasa.jpl.pds.tools.label.CommentStatement;
import gov.nasa.jpl.pds.tools.label.ObjectStatement;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Iterator;
import gov.nasa.jpl.pds.tools.dict.Definition;
import gov.nasa.jpl.pds.tools.dict.Dictionary;


/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class DictionaryParser implements ODLTokenTypes {
    private static Logger log = Logger.getLogger(new DictionaryParser().getClass().getName());
    
    public Dictionary parse(URL file) throws ParseException, IOException {
        ODLLexer lexer = new ODLLexer(file.openStream());
        ODLParser parser = new ODLParser(lexer);
        try {
            //Attempt to parse a dictionary
            parser.dictionary();
            //Now grab the AST tree and build a dictionary from it
            AST ast = parser.getAST();
            Dictionary dictionary = new Dictionary();
            
            //First grab off the comments at the top of the dictionary as
            //this could include version information. 
            StringBuffer information = new StringBuffer(); 
            while (ast != null && ast.getType() == COMMENT) {
                CommentStatement comment = StatementFactory.createComment(ast);
                information.append(comment.getComment());
            }
            dictionary.setInformation(information.toString());
            
            //Now process the objects in the rest of the file.
            //These objects can be an alias list, units list, or
            //definition (element, object, or group)
            List definitions = new ArrayList();
            Map aliases = new HashMap();
            Map units = new HashMap();
            
            while (ast != null) {
                //Only pick up something from the dictionary that is an object
                if (ast.getType() == LITERAL_OBJECT) {
                    ObjectStatement object = StatementFactory.createObject(ast);
                    //At this point the object statement should be an ALIAS_LIST,
                    //UNIT_LIST, or definition.
                    if (object.getIdentifier().equals("ALIAS_LIST")) {
                        //FIXME: Support aliases being read in
                        //aliases.putAll(DefinitionFactory.generateAliasMap(object)); 
                    } else if (object.getIdentifier().equals("UNIT_LIST")) {
                        //FIXME: Support units being read in
                        //units.putAll(DefinitionFactory.generateUnitsMap(object));
                    } else if (object.getIdentifier().endsWith("DEFINITION")) {
                        try {
                            Definition d = DefinitionFactory.createDefinition(object);
                        } catch (UnknownDefinitionException ude) {
                            //TODO: figure out if we want to exit upon encountering this error
                            System.out.println(ude.getMessage());
                        }
                    }
                }
                
                //dictionary.addDefinition(DefinitionFactory.createDefinition(ast));
                ast = ast.getNextSibling();
            }
            
            //Go through the definitions and set all aliases that were found
            for (Iterator i = definitions.iterator(); i.hasNext();) {
                Definition d = (Definition) i.next();
                if (aliases.containsKey(d.getIdentifier())) {
                    d.setAliases((List) aliases.get(d.getIdentifier()));
                }
            }
            
            //Add all definitions to the dictionary
            dictionary.addDefinitions(definitions);
            //TODO: Update to catch thrown exception not all exceptions
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new ParseException(ex.getMessage());
        }

        return null;
    }
    
    public static void main(String [] args) throws Exception {
        DictionaryParser parser = new DictionaryParser();
        parser.parse(new URL(args[0]));
    }
}
