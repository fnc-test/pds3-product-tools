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
