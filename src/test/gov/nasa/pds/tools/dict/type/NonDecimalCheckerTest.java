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

package gov.nasa.pds.tools.dict.type;

import gov.nasa.pds.tools.BaseTestCase;
import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.label.Label;

import java.io.File;
import java.io.IOException;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
@SuppressWarnings("nls")
public class NonDecimalCheckerTest extends BaseTestCase {

    public void testMismatch() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "nonDecimal.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label,
                ProblemType.TYPE_MISMATCH);
        assertProblemEquals(lpe, 3, null, "parser.error.typeMismatch",
                ProblemType.TYPE_MISMATCH, "BIT_MASK", "NONDECIMAL",
                "TextString", "should be non decimal");
    }

    public void testBadChars() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "nonDecimal.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        // unable to match value to a valid token
        LabelParserException lpe = assertHasProblem(label,
                ProblemType.BAD_VALUE, 13);
        assertProblemEquals(lpe, 13, null, "parser.error.badValue",
                ProblemType.BAD_VALUE, "BIT_MASK", "2#1-01#");

        // value, defaulting to string string, didn't match definition
        // constraints
        lpe = assertHasProblem(label, ProblemType.TYPE_MISMATCH, 13);
        assertProblemEquals(lpe, 13, null, "parser.error.typeMismatch",
                ProblemType.TYPE_MISMATCH, "BIT_MASK", "NONDECIMAL",
                "TextString", "2#1-01#");
    }

    public void testBadCharsLong() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "nonDecimal.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        // unable to match value to a valid token
        LabelParserException lpe = assertHasProblem(label,
                ProblemType.BAD_VALUE, 19);
        assertProblemEquals(lpe, 19, null, "parser.error.badValue",
                ProblemType.BAD_VALUE, "BIT_MASK",
                "2#00000000000000000000000000-#");

        // value, defaulting to string string, didn't match definition
        // constraints
        lpe = assertHasProblem(label, ProblemType.TYPE_MISMATCH, 19);
        assertProblemEquals(lpe, 19, null, "parser.error.typeMismatch",
                ProblemType.TYPE_MISMATCH, "BIT_MASK", "NONDECIMAL",
                "TextString", "2#00000000000000000000000000-#");
    }

    // TODO: should handle better in lexer but unable to at this time
    public void testValueWSpaces() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "nonDecimal.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        // unable to match value to a valid token
        LabelParserException lpe = assertHasProblem(label,
                ProblemType.BAD_VALUE, 9);
        assertProblemEquals(lpe, 9, 15, "parser.error.tooManyTokens",
                ProblemType.BAD_VALUE, "2#1, 0, 0, 1#", "BIT_MASK");

        // value, defaulting to string string, didn't match definition
        // constraints
        lpe = assertHasProblem(label, ProblemType.TYPE_MISMATCH, 9);
        assertProblemEquals(lpe, 9, null, "parser.error.typeMismatch",
                ProblemType.TYPE_MISMATCH, "BIT_MASK", "NONDECIMAL",
                "TextString", "2#1");
    }

    public void testNoBase() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "nonDecimal.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        // unable to match value to a valid token
        LabelParserException lpe = assertHasProblem(label,
                ProblemType.BAD_VALUE, 10);
        assertProblemEquals(lpe, 10, null, "parser.error.badValue",
                ProblemType.BAD_VALUE, "BIT_MASK", "#1001#");

        // value, defaulting to string string, didn't match definition
        // constraints
        lpe = assertHasProblem(label, ProblemType.TYPE_MISMATCH, 10);
        assertProblemEquals(lpe, 10, null, "parser.error.typeMismatch",
                ProblemType.TYPE_MISMATCH, "BIT_MASK", "NONDECIMAL",
                "TextString", "#1001#");
    }

    public void testNoEnd() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "nonDecimal.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        // unable to match value to a valid token
        LabelParserException lpe = assertHasProblem(label,
                ProblemType.BAD_VALUE, 11);
        assertProblemEquals(lpe, 11, null, "parser.error.badValue",
                ProblemType.BAD_VALUE, "BIT_MASK", "2#1001");

        // value, defaulting to string string, didn't match definition
        // constraints
        lpe = assertHasProblem(label, ProblemType.TYPE_MISMATCH, 11);
        assertProblemEquals(lpe, 11, null, "parser.error.typeMismatch",
                ProblemType.TYPE_MISMATCH, "BIT_MASK", "NONDECIMAL",
                "TextString", "2#1001");
    }

    public void testNoStart() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "nonDecimal.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        // unable to match value to a valid token
        LabelParserException lpe = assertHasProblem(label,
                ProblemType.BAD_VALUE, 12);
        assertProblemEquals(lpe, 12, null, "parser.error.badValue",
                ProblemType.BAD_VALUE, "BIT_MASK", "21001#");

        // value, defaulting to string string, didn't match definition
        // constraints
        lpe = assertHasProblem(label, ProblemType.TYPE_MISMATCH, 12);
        assertProblemEquals(lpe, 12, null, "parser.error.typeMismatch",
                ProblemType.TYPE_MISMATCH, "BIT_MASK", "NONDECIMAL",
                "TextString", "21001#");
    }

    public void testTrailingDigit() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "nonDecimal.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        // unable to match value to a valid token
        LabelParserException lpe = assertHasProblem(label,
                ProblemType.BAD_VALUE, 17);
        assertProblemEquals(lpe, 17, null, "parser.error.badValue",
                ProblemType.BAD_VALUE, "BIT_MASK", "2#0#3");

        // value, defaulting to string string, didn't match definition
        // constraints
        lpe = assertHasProblem(label, ProblemType.TYPE_MISMATCH, 17);
        assertProblemEquals(lpe, 17, null, "parser.error.typeMismatch",
                ProblemType.TYPE_MISMATCH, "BIT_MASK", "NONDECIMAL",
                "TextString", "2#0#3");
    }

    public void testBadRadix() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "nonDecimal.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        // unable to match value to a valid token
        LabelParserException lpe = assertHasProblem(label,
                ProblemType.INVALID_TYPE, 8);
        assertProblemEquals(lpe, 8, null, "parser.error.badNonDecimalRadix",
                ProblemType.INVALID_TYPE, "3#1001#", "3");
    }

    public void testInvalidBase() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "nonDecimal.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        // unable to match value to a valid token
        LabelParserException lpe = assertHasProblem(label,
                ProblemType.INVALID_TYPE, 14);
        assertProblemEquals(lpe, 14, null, "parser.error.badNonDecimal",
                ProblemType.INVALID_TYPE, "2#3001#");
    }

    // NOTE: grammar is more forgiving, allowing any letter or digit
    public void testBadContents() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "nonDecimal.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        // unable to match value to a valid token
        LabelParserException lpe = assertHasProblem(label,
                ProblemType.INVALID_TYPE, 18);
        assertProblemEquals(lpe, 18, null, "parser.error.badNonDecimal",
                ProblemType.INVALID_TYPE, "2#234G#");
    }

    public void testExceedsMax() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "nonDecimal.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label, ProblemType.OOR, 15);
        assertProblemEquals(lpe, 15, null, "parser.error.exceedsMax",
                ProblemType.OOR, "1048575", "1048574", "NONDEC", "NONDECIMAL");
    }

    public void testLessThanMin() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "nonDecimal.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label, ProblemType.OOR, 16);
        assertProblemEquals(lpe, 16, null, "parser.error.lessThanMin",
                ProblemType.OOR, "0", "5.0", "NONDEC", "NONDECIMAL");
    }

    public void testSigned() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "nonDecimal.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label,
                ProblemType.INVALID_TYPE, 5);
        assertProblemEquals(lpe, 5, null, "parser.error.signedNonDecimal",
                ProblemType.INVALID_TYPE, "2#-1001#");

        lpe = assertHasProblem(label, ProblemType.INVALID_TYPE, 6);
        assertProblemEquals(lpe, 6, null, "parser.error.signedNonDecimal",
                ProblemType.INVALID_TYPE, "8#+567#");
    }

    // For convenience, all test parts in one file but tested in different
    // methods. This test just confirms we covered all tests
    public void testNumErrors() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "nonDecimal.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        assertEquals(23, label.getProblems().size());
    }

}
