// Copyright 2006-2010, by the California Institute of Technology.
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

  public static final File TEST_DIR = new File("test-data"); //$NON-NLS-1$

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
