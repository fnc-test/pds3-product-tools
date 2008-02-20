//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.dict;

import gov.nasa.pds.tools.dict.parser.DictionaryParser;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class DictionaryWriter implements DictionaryTokens {
    public static void writeDictionary(Dictionary dictionary, File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(dictionary.getInformation());
        Map definitions = dictionary.getDefinitions();
        writer.write("OBJECT = ALIAS_LIST\n");
        writer.write("  OBJECT_ALIAS_SEQUENCE = (");
        for (Iterator i = definitions.values().iterator(); i.hasNext();) {
            Definition definition = (Definition) i.next();
            if (definition instanceof ObjectDefinition && definition.getAliases().size() > 0) {
                writer.write("\n    (");
                for (Iterator a = definition.getAliases().iterator(); a.hasNext();) {
                    writer.write("'" + a.next().toString() + "',");
                }
                writer.write("'" + definition.getIdentifier() + "')");
            }
        }
        writer.write(")\n");
        writer.write("  ELEMENT_ALIAS_SEQUENCE = (");
        for (Iterator i = definitions.values().iterator(); i.hasNext();) {
            Definition definition = (Definition) i.next();
            if (definition instanceof ElementDefinition && definition.getAliases().size() > 0) {
                for (Iterator a = definition.getAliases().iterator(); a.hasNext();) {
                    Alias alias = (Alias) a.next();
                    writer.write("\n    (");
                    writer.write("'" + alias.getIdentifier().toString() + "',");
                    writer.write("'" + alias.getContext() + "',");
                    writer.write("'" + definition.getIdentifier() + "')");
                }
            }
        }
        writer.write(")\n");
        writer.write("END_OBJECT = ALIAS_LIST\n");
        writer.write("END\n");
        writer.write("OBJECT = UNIT_LIST\n");
        writer.write("  UNIT_SEQUENCE = (");
        Map units = dictionary.getUnits();
        for (Iterator i = units.keySet().iterator(); i.hasNext();) {
            String unit = (String) i.next();
            writer.write("\n    (");
            for (Iterator u = ((List) units.get(unit)).iterator(); u.hasNext();) {
                writer.write("'" + u.next().toString() + "'");
                if (u.hasNext())
                    writer.write(",");
            }
            writer.write(")");
        }
        writer.write(")\n");
        writer.write("END_OBJECT = UNIT_LIST\n");
        writer.write("END\n");
        
        writeObjectDefinitions(dictionary, writer);
        writeGroupDefinitions(dictionary, writer);
        writeElementDefinitions(dictionary, writer);
        
        writer.close();
    }
    
    private static void writeObjectDefinitions(Dictionary dictionary, BufferedWriter writer) throws IOException {
        for (Iterator i = dictionary.getDefinitions().values().iterator(); i.hasNext();) {
            Definition definition = (Definition) i.next();
            if (definition instanceof ObjectDefinition) {
                ObjectDefinition object = (ObjectDefinition) definition;
                if (object.getObjectType().equals(OBJECT_TYPE_SPECIFIC)) {
                    writer.write("OBJECT = SPECIFIC_OBJECT_DEFINITION\n");
                } else {
                    writer.write("OBJECT = GENERIC_OBJECT_DEFINITION\n");
                }
                
                writer.write("  NAME = " + object.getIdentifier() + "\n");
                writer.write("  STATUS_TYPE = " + object.getStatusType() + "\n");
                writer.write("  DESCRIPTION = \"" + object.getDescription() + "\"\n");
                if (object.getRequiredElements().size() > 0) {
                    writer.write("  REQUIRED_ELEMENT_SET = {");
                    for (Iterator re = object.getRequiredElements().iterator(); re.hasNext();) {
                        writer.write("\n     " + re.next().toString());
                        if (re.hasNext())
                            writer.write(",");
                    }
                
                    writer.write("}\n");
                }
                if (object.getOptionalElements().size() > 0) {
                writer.write("  OPTIONAL_ELEMENT_SET = {");
                    for (Iterator oe = object.getOptionalElements().iterator(); oe.hasNext();) {
                        writer.write("\n     " + oe.next().toString());
                        if (oe.hasNext())
                            writer.write(",");
                    }
                    writer.write("}\n");
                }
                if (object.getRequiredObjects().size() > 0) {
                    writer.write("  REQUIRED_OBJECT_SET = {");
                    for (Iterator ro = object.getRequiredObjects().iterator(); ro.hasNext();) {
                        writer.write("\n     " + ro.next().toString());
                        if (ro.hasNext())
                            writer.write(",");
                    }
                    writer.write("}\n");
                }
                if (object.getOptionalObjects().size() > 0) {
                writer.write("  OPTIONAL_OBJECT_SET = {");
                    for (Iterator oo = object.getOptionalObjects().iterator(); oo.hasNext();) {
                        writer.write("\n     " + oo.next().toString());
                        if (oo.hasNext())
                            writer.write(",");
                    }
                    writer.write("}\n");
                }
                writer.write("  OBJECT_TYPE = " + object.getObjectType() +"\n");
                
                if (object.getObjectType().equals(OBJECT_TYPE_SPECIFIC)) {
                    writer.write("END_OBJECT = SPECIFIC_OBJECT_DEFINITION\n");
                } else {
                    writer.write("END_OBJECT = GENERIC_OBJECT_DEFINITION\n");
                }
                writer.write("END\n");
            }
        }
    }
    
    private static void writeGroupDefinitions(Dictionary dictionary, BufferedWriter writer) throws IOException {
        for (Iterator i = dictionary.getDefinitions().values().iterator(); i.hasNext();) {
            Definition definition = (Definition) i.next();
            if (definition instanceof GroupDefinition) {
                GroupDefinition group = (GroupDefinition) definition;
                if (group.getObjectType().equals(OBJECT_TYPE_SPECIFIC_GROUP)) {
                    writer.write("OBJECT = SPECIFIC_OBJECT_DEFINITION\n");
                } else {
                    writer.write("OBJECT = GENERIC_OBJECT_DEFINITION\n");
                }
                
                writer.write("  NAME = " + group.getIdentifier() + "\n");
                writer.write("  STATUS_TYPE = " + group.getStatusType() + "\n");
                writer.write("  DESCRIPTION = \"" + group.getDescription() + "\"\n");
                if (group.getRequiredElements().size() > 0) {
                    writer.write("  REQUIRED_ELEMENT_SET = {");
                    for (Iterator re = group.getRequiredElements().iterator(); re.hasNext();) {
                        writer.write("\n     " + re.next().toString());
                        if (re.hasNext())
                            writer.write(",");
                    }
                
                    writer.write("}\n");
                }
                if (group.getOptionalElements().size() > 0) {
                writer.write("  OPTIONAL_ELEMENT_SET = {");
                    for (Iterator oe = group.getOptionalElements().iterator(); oe.hasNext();) {
                        writer.write("\n     " + oe.next().toString());
                        if (oe.hasNext())
                            writer.write(",");
                    }
                    writer.write("}\n");
                }
                writer.write("  OBJECT_TYPE = " + group.getObjectType() +"\n");
                
                if (group.getObjectType().equals(OBJECT_TYPE_SPECIFIC_GROUP)) {
                    writer.write("END_OBJECT = SPECIFIC_OBJECT_DEFINITION\n");
                } else {
                    writer.write("END_OBJECT = GENERIC_OBJECT_DEFINITION\n");
                }
                writer.write("END\n");
            }
        }
    }
    
    private static void writeElementDefinitions(Dictionary dictionary, BufferedWriter writer) throws IOException {
        for (Iterator i = dictionary.getDefinitions().values().iterator(); i.hasNext();) {
            Definition definition = (Definition) i.next();
            if (definition instanceof ElementDefinition) {
                ElementDefinition element = (ElementDefinition) definition;
                writer.write("OBJECT = ELEMENT_DEFINITION\n");
                if (element.getIdentifier().indexOf(":") == -1)
                    writer.write("  NAME = " + element.getIdentifier() + "\n");
                else
                	writer.write("  NAME = \"" + element.getIdentifier() + "\"\n");
                writer.write("  STATUS_TYPE = " + element.getStatusType() + "\n");
                writer.write("  GENERAL_DATA_TYPE = " + element.getDataType() + "\n");
                writer.write("  UNIT_ID = '" + element.getUnitId() + "'\n");
                if (element.getValueType().equals(NOT_APPLICABLE))
                    writer.write("  STANDARD_VALUE_TYPE = 'N/A'\n");
                else
                    writer.write("  STANDARD_VALUE_TYPE = " + element.getValueType() + "\n");
                if (element.getMinimum() != null)
                    writer.write("  MINIMUM = " + element.getMinimum().toString() + "\n");
                if (element.getMaximum() != null)
                    writer.write("  MAXIMUM = " + element.getMaximum().toString() + "\n");
                if (element.getMinLength() != 0)
                    writer.write("  MINIMUM_LENGTH = " + element.getMinLength() + "\n");
                if (element.getMaxLength() != Integer.MAX_VALUE)
                    writer.write("  MAXIMUM_LENGTH = " + element.getMaxLength() + "\n");
                writer.write("  DESCRIPTION = \"" + element.getDescription() + "\"\n");
                if (element.getValues().size() > 0) {
                    writer.write("  STANDARD_VALUE_SET = {");
                    for (Iterator v = element.getValues().iterator(); v.hasNext();) {
                        writer.write("\n     \"" + v.next().toString() + "\"");
                        if (v.hasNext())
                            writer.write(",");
                    }
                    writer.write("}\n");
                }
                writer.write("END_OBJECT = ELEMENT_DEFINITION\n");
                writer.write("END\n");
            }
        }
    }
    
    public static void main(String [] args) throws Exception {
        Dictionary dictionary = DictionaryParser.parse(new URL(args[0]), true);
        writeDictionary(dictionary, new File("dictionary.out"));
    }
}
