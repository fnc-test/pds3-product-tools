// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// * Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// * Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// * Neither the name of Caltech nor its operating division, the Jet Propulsion
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

import gov.nasa.pds.tools.BaseTestCase;
import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.constants.Constants.DictionaryType;
import gov.nasa.pds.tools.dict.parser.DictIDFactory;
import gov.nasa.pds.tools.dict.parser.DictionaryParser;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * JUnit test for dictionary class. The dictionary class represents a PDS
 * Compliant data dictionary.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
@SuppressWarnings("nls")
public class DictionaryTest extends BaseTestCase {

  public void testCtors() throws URISyntaxException {
    Dictionary dictionary = new Dictionary();
    assertEquals("", dictionary.getInformation());
    assertEquals(0, dictionary.getDefinitions().size());
    assertNull(dictionary.getSourceString());

    dictionary = new Dictionary(new File("pdsdd.full"));
    assertEquals("", dictionary.getInformation());
    assertEquals(0, dictionary.getDefinitions().size());
    assertEquals("pdsdd.full", dictionary.getSourceString());
    assertEquals("pdsdd.full", dictionary.getDictionaryFile().toString());

    dictionary = new Dictionary(new URI("pdsdd.full"));
    assertEquals("", dictionary.getInformation());
    assertEquals(0, dictionary.getDefinitions().size());
    assertEquals("pdsdd.full", dictionary.getSourceString());
    assertEquals("pdsdd.full", dictionary.getDictionaryURI().toString());
  }

  public void testSetters() {
    Dictionary dictionary = new Dictionary();
    dictionary.setInformation("INFORMATION");
    assertEquals("INFORMATION", dictionary.getInformation());
  }

  public void testObjectMethods() {
    DictIdentifier targetName = DictIDFactory.createElementDefId("TARGET_NAME");
    DictIdentifier missionName = DictIDFactory
        .createElementDefId("MISSION_NAME");
    DictIdentifier parameters = DictIDFactory.createGroupDefId("PARAMETERS");
    DictIdentifier table = DictIDFactory.createObjectDefId("TABLE");
    DictIdentifier column = DictIDFactory.createObjectDefId("COLUMN");

    Dictionary dictionary = new Dictionary();

    assertNull(dictionary.getDefinition(table));
    assertNull(dictionary.getElementDefinition(targetName));
    assertNull(dictionary.getObjectDefinition(table));
    assertNull(dictionary.getGroupDefinition(parameters));
    assertNull(dictionary.getElementDefinition("context", targetName));

    ElementDefinition element = new ElementDefinition(dictionary, 1, targetName);
    element.addAlias(new Alias("SHARED_ALIAS"));
    GroupDefinition group = new GroupDefinition(dictionary, 1, parameters);
    ObjectDefinition object = new ObjectDefinition(dictionary, 1, table);

    dictionary.addDefinition(element);

    List<Definition> defs = new ArrayList<Definition>();
    defs.add(group);
    defs.add(object);
    defs.add(element);
    dictionary.addDefinitions(defs);

    assertEquals(4, dictionary.getDefinitions().size());
    assertEquals(element, dictionary.getDefinition(DictIDFactory
        .createElementDefId("TARGET_NAME")));
    assertEquals(element.getIdentifier(), targetName);
    assertEquals(group, dictionary.getDefinition(parameters));
    assertEquals(group, dictionary.getGroupDefinition(parameters));
    assertEquals(object, dictionary.getDefinition(table));
    assertEquals(object, dictionary.getObjectDefinition(table));

    Dictionary mergeDictionary = new Dictionary();
    ElementDefinition dataset = new ElementDefinition(dictionary, 1,
        DictIDFactory.createElementDefId("DATA_SET_NAME"));
    dataset.addAlias(new Alias("SHARED_ALIAS"));
    dataset.addAlias(new Alias("UNSHARED_ALIAS"));
    dataset.setDescription("DESCRIPTION");
    dataset.setStatusType("APPROVED");
    dataset.setDataType(DictionaryType.INTEGER);
    dataset.setMaximum(Integer.MAX_VALUE);
    dataset.setMinimum(Integer.MIN_VALUE);
    dataset.setMaxLength(10);
    dataset.setMinLength(1);
    dataset.setUnits("M");
    dataset.addValue("1");
    dataset.setValueType("STRING");
    mergeDictionary.addDefinition(dataset);

    ObjectDefinition tableObj = new ObjectDefinition(dictionary, 1, table);
    tableObj.addOptional(targetName);
    tableObj.addRequired(missionName);
    tableObj.addOptional(table);
    tableObj.addRequired(column);
    mergeDictionary.addDefinition(tableObj);

    GroupDefinition paramObj = new GroupDefinition(dictionary, 1, parameters);
    paramObj.addOptional(targetName);
    paramObj.addRequired(missionName);

    dictionary.merge(mergeDictionary);
    assertEquals(dictionary.getDefinition(dataset.getIdentifier()), dataset);

    assertTrue(dictionary.containsDefinition(dataset.getIdentifier()));
    assertTrue(dictionary.containsElementDefinition(element.getIdentifier()));
    assertTrue(dictionary.containsElementDefinition("TABLE",
        element.getIdentifier()));
    assertTrue(dictionary.containsGroupDefinition(group.getIdentifier()));
    assertTrue(dictionary.containsObjectDefinition(object.getIdentifier()));

    dictionary.addDefinition(new ObjectDefinition(dictionary, 1, DictIDFactory
        .createObjectDefId("IMAGE")));
    dictionary.addDefinition(new ElementDefinition(dictionary, 1, DictIDFactory
        .createElementDefId("ABCD_IMAGE")));
    assertEquals(
        "IMAGE",
        dictionary
            .findObjectClassDefinition(
                DictIDFactory.createObjectDefId("XYZ_IMAGE")).getIdentifier()
            .toString());
  }

  public void testVersion() throws LabelParserException, IOException {
    Dictionary dictionary = DictionaryParser.parse(new File(TEST_DIR,
        "pdsdd.full"));
    assertEquals("1.77", dictionary.getVersion());
  }

  public void testMergeUnits() throws LabelParserException, IOException {
    Dictionary dictionary = DictionaryParser.parse(new File(TEST_DIR,
        "pdsdd.full"));
    final Dictionary testDictionary = DictionaryParser.parse(new File(TEST_DIR,
        "testdd.full"));
    dictionary.merge(testDictionary);
    assertEquals("test unit", dictionary.getUnits().get("test-unit"));
  }
}
