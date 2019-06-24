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
public class AlphabetCheckerTest extends BaseTestCase {

    public void testTypeMismatch() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "alphabetic.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label,
                ProblemType.TYPE_MISMATCH);
        assertProblemEquals(lpe, 5, null, "parser.error.typeMismatch",
                ProblemType.TYPE_MISMATCH, "ALPHA", "ALPHABET", "Numeric",
                "777");
    }

    public void testNonAlphabetic() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "alphabetic.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label,
                ProblemType.INVALID_TYPE);
        assertProblemEquals(lpe, 6, null, "parser.error.nonAlphabetic",
                ProblemType.INVALID_TYPE, "a7a");
    }

    public void testLong() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "alphabetic.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label,
                ProblemType.EXCESSIVE_VALUE_LENGTH);
        assertProblemEquals(lpe, 4, null, "parser.error.tooLong",
                ProblemType.EXCESSIVE_VALUE_LENGTH, "XXXXX", "5", "4", "ALPHA",
                "ALPHABET");
    }

    public void testShort() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "alphabetic.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label,
                ProblemType.SHORT_VALUE);
        assertProblemEquals(lpe, 3, null, "parser.error.tooShort",
                ProblemType.SHORT_VALUE, "X", "1", "2", "ALPHA", "ALPHABET");
    }

    // For convenience, all test parts in one file but tested in different
    // methods. This test just confirms we covered all tests
    public void testNumErrors() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "alphabetic.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        assertEquals(4, label.getProblems().size());
    }

}
