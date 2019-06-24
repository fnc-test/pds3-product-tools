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

package gov.nasa.pds.tools;

import gov.nasa.arc.pds.tools.util.StrUtils;
import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.containers.SimpleDictionaryChange;
import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.dict.parser.DictionaryParser;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.StandardPathResolver;
import gov.nasa.pds.tools.label.parser.DefaultLabelParser;
import gov.nasa.pds.tools.label.validate.Validator;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
@SuppressWarnings("nls")
public abstract class BaseTestCase extends TestCase {

  public static final File TEST_DIR = new File("src/test-data"); //$NON-NLS-1$

  protected static final StandardPathResolver RESOLVER = new StandardPathResolver();

  protected static final DefaultLabelParser PARSER = new DefaultLabelParser(
      RESOLVER);

  protected static final File LABEL_DIR = new File(TEST_DIR, "labels");

  private static Dictionary dictionary;

  public static void assertNegative(Number number) {
    assertTrue(number != null && number.doubleValue() < 0);
  }

  public static void assertPositive(Number number) {
    assertTrue(number != null && number.doubleValue() > 0);
  }

  public static void assertZero(Number number) {
    assertTrue(number != null && number.doubleValue() == 0);
  }

  public static LabelParserException assertHasProblem(Label label,
      ProblemType type) {
    return assertHasProblem(label, type, null);
  }

  @SuppressWarnings("null")
  public static LabelParserException assertHasProblem(final Label label,
      final ProblemType type, final Integer line) {
    if (label == null) {
      fail("Label is null");
    }
    List<LabelParserException> problems = label.getProblems();
    for (final LabelParserException problem : problems) {
      if (problem.getType().equals(type)) {
        if (line == null || problem.getLineNumber().equals(line)) {
          return problem;
        }
      }
    }
    fail("Label did not contain a problem of type \"" + type.toString()
        + "\" on line " + line + ".");
    return null;
  }

  public void assertProblemEquals(final LabelParserException problem,
      Integer lineNumber, Integer column, String key, ProblemType type,
      Object... arguments) {
    assertEquals("Lines do not match: ", lineNumber, problem.getLineNumber());
    assertEquals("Columns do not match: ", column, problem.getColumn());
    assertEquals("Keys do not match: ", key, problem.getKey());
    assertEquals("Problem types do not match: ", type, problem.getType());
    final Object[] probArgs = problem.getArguments();
    assertEquals(StrUtils.toSeparatedString(probArgs) + " vs "
        + StrUtils.toSeparatedString(arguments), arguments.length,
        probArgs.length);
    for (int i = 0; i < arguments.length; i++) {
      assertEquals(arguments[i].toString(), probArgs[i].toString());
    }
  }

  public static void assertDoesntHaveProblem(Label label, ProblemType type) {
    List<LabelParserException> problems = label.getProblems();
    for (final LabelParserException problem : problems) {
      problem.getType().equals(type);
      fail("Label contains a problem of type \"" + type.toString()
          + "\" when it should not.");
    }
  }

  public Dictionary getDictionary() {
    if (dictionary == null) {
      try {
        dictionary = DictionaryParser.parse(new File(TEST_DIR, "pdsdd.full"));
        final Dictionary testDictionary = DictionaryParser.parse(new File(
            TEST_DIR, "testdd.full"));
        if (testDictionary.getProblems().size() > 0) {
          throw new RuntimeException("Test dictionary had "
              + testDictionary.getProblems().size() + " problems.");
        }
        dictionary.merge(testDictionary);
        for (final SimpleDictionaryChange change : dictionary.getMergeChanges()) {
          System.out.println("change: " + change.toString());
        }
      } catch (LabelParserException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return dictionary;
  }

  public void validate(final Label label) {
    Validator validator = new Validator();
    validator.validate(label, getDictionary());
  }
}
