// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// • Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// • Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// • Neither the name of Caltech nor its operating division, the Jet Propulsion
// Laboratory, nor the names of its contributors may be used to endorse or
// promote products derived from this software without specific prior written
// permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package gov.nasa.pds.tools.dict;

import static gov.nasa.pds.tools.dict.DictionaryTokens.NOT_APPLICABLE;
import static gov.nasa.pds.tools.dict.DictionaryTokens.OBJECT_TYPE_SPECIFIC;
import static gov.nasa.pds.tools.dict.DictionaryTokens.OBJECT_TYPE_SPECIFIC_GROUP;
import gov.nasa.pds.tools.dict.parser.DictionaryParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
// TODO: remove nls stuff and externalize strings. suppressed now so other
// warnings show up
@SuppressWarnings("nls")
public class DictionaryWriter {

    public static void writeDictionary(Dictionary dictionary, File file)
            throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(dictionary.getInformation());
        Map<DictIdentifier, Definition> definitions = dictionary
                .getDefinitions();
        writer.write("OBJECT = ALIAS_LIST\n");
        writer.write("  OBJECT_ALIAS_SEQUENCE = (");
        for (Iterator<Definition> i = definitions.values().iterator(); i
                .hasNext();) {
            Definition definition = i.next();
            if (definition instanceof ObjectDefinition
                    && definition.getAliases().size() > 0) {
                writer.write("\n    (");
                for (Iterator<Alias> a = definition.getAliases().iterator(); a
                        .hasNext();) {
                    writer.write("'" + a.next().toString() + "',");
                }
                writer.write("'" + definition.getIdentifier() + "')");
            }
        }
        writer.write(")\n");
        writer.write("  ELEMENT_ALIAS_SEQUENCE = (");
        for (Iterator<Definition> i = definitions.values().iterator(); i
                .hasNext();) {
            Definition definition = i.next();
            if (definition instanceof ElementDefinition
                    && definition.getAliases().size() > 0) {
                for (Iterator<Alias> a = definition.getAliases().iterator(); a
                        .hasNext();) {
                    Alias alias = a.next();
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
        Map<String, String> units = dictionary.getUnits();
        for (Iterator<Entry<String, String>> i = units.entrySet().iterator(); i
                .hasNext();) {
            Entry<String, String> value = i.next();
            final String unit = value.getKey();
            final String desc = value.getValue();
            writer.write("\n    ('" + unit + "','" + desc + "')");
        }
        writer.write(")\n");
        writer.write("END_OBJECT = UNIT_LIST\n");
        writer.write("END\n");

        writeObjectDefinitions(dictionary, writer);
        writeGroupDefinitions(dictionary, writer);
        writeElementDefinitions(dictionary, writer);

        writer.close();
    }

    private static void writeObjectDefinitions(Dictionary dictionary,
            BufferedWriter writer) throws IOException {
        for (Iterator<Definition> i = dictionary.getDefinitions().values()
                .iterator(); i.hasNext();) {
            Definition definition = i.next();
            if (definition instanceof ObjectDefinition) {
                ObjectDefinition object = (ObjectDefinition) definition;
                if (object.getObjectType().equals(OBJECT_TYPE_SPECIFIC)) {
                    writer.write("OBJECT = SPECIFIC_OBJECT_DEFINITION\n");
                } else {
                    writer.write("OBJECT = GENERIC_OBJECT_DEFINITION\n");
                }

                if (object.getIdentifier().toString().indexOf(":") == -1)
                    writer.write("  NAME = " + object.getIdentifier() + "\n");
                else
                    writer.write("  NAME = \"" + object.getIdentifier()
                            + "\"\n");
                writer
                        .write("  STATUS_TYPE = " + object.getStatusType()
                                + "\n");
                writer.write("  DESCRIPTION = \"" + object.getDescription()
                        + "\"\n");
                if (object.getRequiredElements().size() > 0) {
                    writer.write("  REQUIRED_ELEMENT_SET = {");
                    for (Iterator<DictIdentifier> re = object
                            .getRequiredElements().iterator(); re.hasNext();) {
                        writer.write("\n     " + re.next().toString());
                        if (re.hasNext())
                            writer.write(",");
                    }

                    writer.write("}\n");
                }
                if (object.getOptionalElements().size() > 0) {
                    writer.write("  OPTIONAL_ELEMENT_SET = {");
                    for (Iterator<DictIdentifier> oe = object
                            .getOptionalElements().iterator(); oe.hasNext();) {
                        writer.write("\n     " + oe.next().toString());
                        if (oe.hasNext())
                            writer.write(",");
                    }
                    writer.write("}\n");
                }
                if (object.getRequiredObjects().size() > 0) {
                    writer.write("  REQUIRED_OBJECT_SET = {");
                    for (Iterator<DictIdentifier> ro = object
                            .getRequiredObjects().iterator(); ro.hasNext();) {
                        writer.write("\n     " + ro.next().toString());
                        if (ro.hasNext())
                            writer.write(",");
                    }
                    writer.write("}\n");
                }
                if (object.getOptionalObjects().size() > 0) {
                    writer.write("  OPTIONAL_OBJECT_SET = {");
                    for (Iterator<DictIdentifier> oo = object
                            .getOptionalObjects().iterator(); oo.hasNext();) {
                        writer.write("\n     " + oo.next().toString());
                        if (oo.hasNext())
                            writer.write(",");
                    }
                    writer.write("}\n");
                }
                writer
                        .write("  OBJECT_TYPE = " + object.getObjectType()
                                + "\n");

                if (object.getObjectType().equals(OBJECT_TYPE_SPECIFIC)) {
                    writer.write("END_OBJECT = SPECIFIC_OBJECT_DEFINITION\n");
                } else {
                    writer.write("END_OBJECT = GENERIC_OBJECT_DEFINITION\n");
                }
                writer.write("END\n");
            }
        }
    }

    private static void writeGroupDefinitions(Dictionary dictionary,
            BufferedWriter writer) throws IOException {
        for (Iterator<Definition> i = dictionary.getDefinitions().values()
                .iterator(); i.hasNext();) {
            Definition definition = i.next();
            if (definition instanceof GroupDefinition) {
                GroupDefinition group = (GroupDefinition) definition;
                if (group.getObjectType().equals(OBJECT_TYPE_SPECIFIC_GROUP)) {
                    writer.write("OBJECT = SPECIFIC_OBJECT_DEFINITION\n");
                } else {
                    writer.write("OBJECT = GENERIC_OBJECT_DEFINITION\n");
                }

                if (group.getIdentifier().toString().indexOf(":") == -1)
                    writer.write("  NAME = " + group.getIdentifier() + "\n");
                else
                    writer
                            .write("  NAME = \"" + group.getIdentifier()
                                    + "\"\n");
                writer.write("  STATUS_TYPE = " + group.getStatusType() + "\n");
                writer.write("  DESCRIPTION = \"" + group.getDescription()
                        + "\"\n");
                if (group.getRequiredElements().size() > 0) {
                    writer.write("  REQUIRED_ELEMENT_SET = {");
                    for (Iterator<DictIdentifier> re = group
                            .getRequiredElements().iterator(); re.hasNext();) {
                        writer.write("\n     " + re.next().toString());
                        if (re.hasNext())
                            writer.write(",");
                    }

                    writer.write("}\n");
                }
                if (group.getOptionalElements().size() > 0) {
                    writer.write("  OPTIONAL_ELEMENT_SET = {");
                    for (Iterator<DictIdentifier> oe = group
                            .getOptionalElements().iterator(); oe.hasNext();) {
                        writer.write("\n     " + oe.next().toString());
                        if (oe.hasNext())
                            writer.write(",");
                    }
                    writer.write("}\n");
                }
                writer.write("  OBJECT_TYPE = " + group.getObjectType() + "\n");

                if (group.getObjectType().equals(OBJECT_TYPE_SPECIFIC_GROUP)) {
                    writer.write("END_OBJECT = SPECIFIC_OBJECT_DEFINITION\n");
                } else {
                    writer.write("END_OBJECT = GENERIC_OBJECT_DEFINITION\n");
                }
                writer.write("END\n");
            }
        }
    }

    private static void writeElementDefinitions(Dictionary dictionary,
            BufferedWriter writer) throws IOException {
        for (Iterator<Definition> i = dictionary.getDefinitions().values()
                .iterator(); i.hasNext();) {
            Definition definition = i.next();
            if (definition instanceof ElementDefinition) {
                ElementDefinition element = (ElementDefinition) definition;
                writer.write("OBJECT = ELEMENT_DEFINITION\n");
                if (element.getIdentifier().toString().indexOf(":") == -1)
                    writer.write("  NAME = " + element.getIdentifier() + "\n");
                else
                    writer.write("  NAME = \"" + element.getIdentifier()
                            + "\"\n");
                writer.write("  STATUS_TYPE = " + element.getStatusType()
                        + "\n");
                writer.write("  GENERAL_DATA_TYPE = " + element.getDataType()
                        + "\n");
                writer.write("  UNIT_ID = '" + element.getUnits() + "'\n");
                if (element.getValueType().equals(NOT_APPLICABLE))
                    writer.write("  STANDARD_VALUE_TYPE = 'N/A'\n");
                else
                    writer.write("  STANDARD_VALUE_TYPE = "
                            + element.getValueType() + "\n");
                if (element.getMinimum() != null)
                    writer.write("  MINIMUM = "
                            + element.getMinimum().toString() + "\n");
                if (element.getMaximum() != null)
                    writer.write("  MAXIMUM = "
                            + element.getMaximum().toString() + "\n");
                if (element.getMinLength() != 0)
                    writer.write("  MINIMUM_LENGTH = " + element.getMinLength()
                            + "\n");
                if (element.getMaxLength() != Integer.MAX_VALUE)
                    writer.write("  MAXIMUM_LENGTH = " + element.getMaxLength()
                            + "\n");
                writer.write("  DESCRIPTION = \"" + element.getDescription()
                        + "\"\n");
                if (element.getValues().size() > 0) {
                    writer.write("  STANDARD_VALUE_SET = {");
                    for (Iterator<String> v = element.getValues().iterator(); v
                            .hasNext();) {
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

    public static void main(String[] args) throws Exception {
        Dictionary dictionary = DictionaryParser.parse(new URL(args[0]), true);
        writeDictionary(dictionary, new File("dictionary.out"));
    }
}
