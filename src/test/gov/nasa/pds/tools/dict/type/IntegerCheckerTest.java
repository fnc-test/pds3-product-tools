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
public class IntegerCheckerTest extends BaseTestCase {

    public void testTypeMismatch() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "integer.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label,
                ProblemType.TYPE_MISMATCH);
        assertProblemEquals(lpe, 3, null, "parser.error.typeMismatch",
                ProblemType.TYPE_MISMATCH, "AXES", "INTEGER", "TextString",
                "should be integer not text");
    }

    public void testBadReal() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "integer.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label,
                ProblemType.INVALID_TYPE);
        assertProblemEquals(lpe, 6, null, "parser.error.badInteger",
                ProblemType.INVALID_TYPE, "+.2e3");
    }

    public void testExceedsMax() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "integer.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = label.getProblems().get(2);
        assertProblemEquals(lpe, 5, null, "parser.error.exceedsMax",
                ProblemType.OOR, "7", "6", "AXES", "INTEGER");
    }

    public void testLessThanMin() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "integer.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = label.getProblems().get(1);
        assertProblemEquals(lpe, 4, null, "parser.error.lessThanMin",
                ProblemType.OOR, "0", "1", "AXES", "INTEGER");
    }

    // For convenience, all test parts in one file but tested in different
    // methods. This test just confirms we covered all tests
    public void testNumErrors() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "integer.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        assertEquals(4, label.getProblems().size());
    }

    public void testASCII_INTEGER_REGEX() {
        assertFalse("".matches(Constants.ASCII_INTEGER_REGEX));
        assertFalse("\"1\"".matches(Constants.ASCII_INTEGER_REGEX));
        assertFalse("1.1".matches(Constants.ASCII_INTEGER_REGEX));
        assertFalse("12312.1234E1".matches(Constants.ASCII_INTEGER_REGEX));

        assertTrue("1".matches(Constants.ASCII_INTEGER_REGEX));
        assertTrue("123123    ".matches(Constants.ASCII_INTEGER_REGEX));
        assertTrue("-123123".matches(Constants.ASCII_INTEGER_REGEX));
    }
}
