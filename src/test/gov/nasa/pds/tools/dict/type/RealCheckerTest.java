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
import gov.nasa.pds.tools.constants.Constants;
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
public class RealCheckerTest extends BaseTestCase {

    public void testTypeMismatch() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "real.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label,
                ProblemType.TYPE_MISMATCH);
        assertProblemEquals(lpe, 3, null, "parser.error.typeMismatch",
                ProblemType.TYPE_MISMATCH, "A_AXIS_RADIUS", "REAL",
                "TextString", "should be real not text");
    }

    public void testBadReal() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "real.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label,
                ProblemType.INVALID_TYPE);
        assertProblemEquals(lpe, 6, null, "parser.error.badReal",
                ProblemType.INVALID_TYPE, "2#10001#");
    }

    public void testExceedsMax() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "real.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = label.getProblems().get(2);
        assertProblemEquals(lpe, 5, null, "parser.error.exceedsMax",
                ProblemType.OOR, "361.0", "360.0", "APXS_MECHANISM_ANGLE",
                "REAL");
    }

    public void testLessThanMin() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "real.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = label.getProblems().get(1);
        assertProblemEquals(lpe, 4, null, "parser.error.lessThanMin",
                ProblemType.OOR, "-181.0", "-180.0", "APXS_MECHANISM_ANGLE",
                "REAL");
    }

    // For convenience, all test parts in one file but tested in different
    // methods. This test just confirms we covered all tests
    public void testNumErrors() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "real.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        assertEquals(4, label.getProblems().size());
    }

    public void testASCII_REAL_REGEX() {
        // empty
        assertFalse("".matches(Constants.ASCII_REAL_REGEX));
        // quoted
        assertFalse("\"1231231\"".matches(Constants.ASCII_REAL_REGEX));
        // string
        assertFalse("asdf".matches(Constants.ASCII_REAL_REGEX));
        // missing numeric data
        assertFalse("e1".matches(Constants.ASCII_REAL_REGEX));

        // trailing decimal allowed to indicate zeros preceding decimal are
        // significant
        assertTrue("12310.".matches(Constants.ASCII_REAL_REGEX));
        // while not good form, appears can have more than one digit to left of
        // decimal
        assertTrue("12312.1234E1".matches(Constants.ASCII_REAL_REGEX));
        // no need for preceding 0
        assertTrue(".1234E1".matches(Constants.ASCII_REAL_REGEX));
        // not necessary to have decimal
        assertTrue("1".matches(Constants.ASCII_REAL_REGEX));
        // not necessary to have power of 10 multiplier if decimal
        assertTrue("1.1".matches(Constants.ASCII_REAL_REGEX));
        // simple power of ten modifier
        assertTrue("1.1E10".matches(Constants.ASCII_REAL_REGEX));
        // negative power of ten
        assertTrue("1.1E-01".matches(Constants.ASCII_REAL_REGEX));
        // multiple digits on left with power of ten
        assertTrue("12312E10".matches(Constants.ASCII_REAL_REGEX));
        // whitespace in front
        assertTrue("     12312E10".matches(Constants.ASCII_REAL_REGEX));
        // whitespace in back
        assertTrue("1.1E10       ".matches(Constants.ASCII_REAL_REGEX));
        // whitespace in front and back
        assertTrue("    1.1E10     ".matches(Constants.ASCII_REAL_REGEX));
        // negative value
        assertTrue("-12312E10".matches(Constants.ASCII_REAL_REGEX));
        // explicitly positive power of ten
        assertTrue("3.3476E+01".matches(Constants.ASCII_REAL_REGEX));
        // lowercase E
        assertTrue("12312.1234e10".matches(Constants.ASCII_REAL_REGEX));
    }
}
