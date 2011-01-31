package gov.nasa.pds.tools.label;

import gov.nasa.pds.tools.BaseTestCase;
import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.constants.Constants.ProblemType;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("nls")
public class DateTimeFormatterTest extends BaseTestCase {

  public void testTooManyMilliseconds() throws LabelParserException,
      IOException {
    final File testFile = new File(LABEL_DIR, "datetime.lbl");

    final Label label = PARSER.parseLabel(testFile);
    validate(label);

    LabelParserException lpe = assertHasProblem(label,
        ProblemType.INVALID_DATE, 28);
    assertProblemEquals(lpe, 28, null, "parser.error.invalidDate",
        ProblemType.INVALID_DATE, "2000-12-22T23:59:59.9999");
  }

  public void testSecondsOOR() throws LabelParserException, IOException {
    final File testFile = new File(LABEL_DIR, "datetime.lbl");

    final Label label = PARSER.parseLabel(testFile);
    validate(label);

    LabelParserException lpe = assertHasProblem(label,
        ProblemType.INVALID_DATE, 29);
    assertProblemEquals(lpe, 29, null, "parser.error.dateOutOfRange",
        ProblemType.INVALID_DATE, "2000-12-22T23:59:60.999");
  }

  public void testMinutesOOR() throws LabelParserException, IOException {
    final File testFile = new File(LABEL_DIR, "datetime.lbl");

    final Label label = PARSER.parseLabel(testFile);
    validate(label);

    LabelParserException lpe = assertHasProblem(label,
        ProblemType.INVALID_DATE, 30);
    assertProblemEquals(lpe, 30, null, "parser.error.dateOutOfRange",
        ProblemType.INVALID_DATE, "2000-12-22T23:60:59.999");
  }

  public void testHoursOOR() throws LabelParserException, IOException {
    final File testFile = new File(LABEL_DIR, "datetime.lbl");

    final Label label = PARSER.parseLabel(testFile);
    validate(label);

    LabelParserException lpe = assertHasProblem(label,
        ProblemType.INVALID_DATE, 31);
    assertProblemEquals(lpe, 31, null, "parser.error.dateOutOfRange",
        ProblemType.INVALID_DATE, "2000-12-22T24:59:59.999");
  }

  public void testDaysOOR() throws LabelParserException, IOException {
    final File testFile = new File(LABEL_DIR, "datetime.lbl");

    final Label label = PARSER.parseLabel(testFile);
    validate(label);

    LabelParserException lpe = assertHasProblem(label,
        ProblemType.INVALID_DATE, 32);
    assertProblemEquals(lpe, 32, null, "parser.error.dateOutOfRange",
        ProblemType.INVALID_DATE, "2000-12-32T23:59:59.999");
  }

  public void testMonthsOOR() throws LabelParserException, IOException {
    final File testFile = new File(LABEL_DIR, "datetime.lbl");

    final Label label = PARSER.parseLabel(testFile);
    validate(label);

    LabelParserException lpe = assertHasProblem(label,
        ProblemType.INVALID_DATE, 33);
    assertProblemEquals(lpe, 33, null, "parser.error.dateOutOfRange",
        ProblemType.INVALID_DATE, "2000-13-22T23:59:59.999");
  }

  public void testDayOfYearOOR() throws LabelParserException, IOException {
    final File testFile = new File(LABEL_DIR, "datetime.lbl");

    final Label label = PARSER.parseLabel(testFile);
    validate(label);

    LabelParserException lpe = assertHasProblem(label,
        ProblemType.INVALID_DATE, 34);
    assertProblemEquals(lpe, 34, null, "parser.error.dateOutOfRange",
        ProblemType.INVALID_DATE, "2000-380T23:59:59.999");
  }

  public void testShortSeconds() throws LabelParserException, IOException {
    final File testFile = new File(LABEL_DIR, "datetime.lbl");

    final Label label = PARSER.parseLabel(testFile);
    validate(label);

    LabelParserException lpe = assertHasProblem(label,
        ProblemType.INVALID_DATE, 35);
    assertProblemEquals(lpe, 35, null, "parser.error.invalidDate",
        ProblemType.INVALID_DATE, "2000-12-22T23:59:1.999");
  }

  public void testShortMinutes() throws LabelParserException, IOException {
    final File testFile = new File(LABEL_DIR, "datetime.lbl");

    final Label label = PARSER.parseLabel(testFile);
    validate(label);

    LabelParserException lpe = assertHasProblem(label,
        ProblemType.INVALID_DATE, 36);
    assertProblemEquals(lpe, 36, null, "parser.error.invalidDate",
        ProblemType.INVALID_DATE, "2000-12-22T23:1:59.999");
  }

  public void testShortHours() throws LabelParserException, IOException {
    final File testFile = new File(LABEL_DIR, "datetime.lbl");

    final Label label = PARSER.parseLabel(testFile);
    validate(label);

    LabelParserException lpe = assertHasProblem(label,
        ProblemType.INVALID_DATE, 37);
    assertProblemEquals(lpe, 37, null, "parser.error.invalidDate",
        ProblemType.INVALID_DATE, "2000-12-22T1:59:59.999");
  }

  public void testShortDays() throws LabelParserException, IOException {
    final File testFile = new File(LABEL_DIR, "datetime.lbl");

    final Label label = PARSER.parseLabel(testFile);
    validate(label);

    LabelParserException lpe = assertHasProblem(label,
        ProblemType.INVALID_DATE, 38);
    assertProblemEquals(lpe, 38, null, "parser.error.invalidDate",
        ProblemType.INVALID_DATE, "2000-12-1T23:59:59.999");
  }

  public void testShortMonths() throws LabelParserException, IOException {
    final File testFile = new File(LABEL_DIR, "datetime.lbl");

    final Label label = PARSER.parseLabel(testFile);
    validate(label);

    LabelParserException lpe = assertHasProblem(label,
        ProblemType.INVALID_DATE, 39);
    assertProblemEquals(lpe, 39, null, "parser.error.invalidDate",
        ProblemType.INVALID_DATE, "2000-1-22T23:59:59.999");
  }

  public void testShortYear() throws LabelParserException, IOException {
    final File testFile = new File(LABEL_DIR, "datetime.lbl");

    final Label label = PARSER.parseLabel(testFile);
    validate(label);

    LabelParserException lpe = assertHasProblem(label,
        ProblemType.INVALID_DATE, 40);
    assertProblemEquals(lpe, 40, null, "parser.error.invalidDate",
        ProblemType.INVALID_DATE, "1-12-22T23:59:59.999");
  }

  public void testInvalidYear() throws LabelParserException, IOException {
    final File testFile = new File(LABEL_DIR, "datetime.lbl");

    final Label label = PARSER.parseLabel(testFile);
    validate(label);

    LabelParserException lpe = assertHasProblem(label,
        ProblemType.TYPE_MISMATCH, 41);
    assertProblemEquals(lpe, 41, null, "parser.error.typeMismatch",
        ProblemType.TYPE_MISMATCH, "START_TIME", "TIME", "Numeric", "999");
  }

  // For convenience, all test parts in one file but tested in different
  // methods. This test just confirms we covered all tests
  public void testNumErrors() throws LabelParserException, IOException {
    final File testFile = new File(LABEL_DIR, "datetime.lbl");

    final Label label = PARSER.parseLabel(testFile);
    validate(label);

    assertEquals(14, label.getProblems().size());
  }

}
