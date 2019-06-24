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

package gov.nasa.pds.tools.label;

import gov.nasa.pds.tools.BaseTestCase;
import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.constants.Constants.ProblemType;

import java.io.File;
import java.io.IOException;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
@SuppressWarnings("nls")
public class LineLengthTest extends BaseTestCase {

    // test one line being over limit in stream
    public void testDetachedStream() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "detachedStream.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label,
                ProblemType.EXCESSIVE_LINE_LENGTH);
        assertProblemEquals(lpe, 3, null, "parser.error.lineTooLong",
                ProblemType.EXCESSIVE_LINE_LENGTH, "79");

        assertEquals(1, label.getProblems().size());
    }

    // test error bubbling that fixed length needs a record length
    public void testDetachedFixedNoRecordLength() throws LabelParserException,
            IOException {
        final File testFile = new File(LABEL_DIR,
                "detachedFixedNoRecordLength.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label,
                ProblemType.MISSING_MEMBER);
        assertProblemEquals(lpe, 2, null, "parser.error.missingRecordBytes",
                ProblemType.MISSING_MEMBER);

        lpe = assertHasProblem(label, ProblemType.EXCESSIVE_LINE_LENGTH);
        assertProblemEquals(lpe, 3, null, "parser.error.lineTooLong",
                ProblemType.EXCESSIVE_LINE_LENGTH, "79");

        assertEquals(2, label.getProblems().size());
    }

    // test normal attached with line padding
    public void testAttachedPadded() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "attachedPadded.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = label.getProblems().get(0);
        assertProblemEquals(lpe, 4, null, "parser.error.wrongLineLength",
                ProblemType.WRONG_LINE_LENGTH, 79, 80);

        lpe = label.getProblems().get(1);
        assertProblemEquals(lpe, 5, null, "parser.error.wrongLineLength",
                ProblemType.WRONG_LINE_LENGTH, 79, 78);

        assertEquals(2, label.getProblems().size());
    }

    // test attached with line padding and data that starts with whitespace
    public void testAttachedPaddedWWhite() throws LabelParserException,
            IOException {
        final File testFile = new File(LABEL_DIR, "attachedPaddedWWhite.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label,
                ProblemType.START_BYTE_POSSIBLE_MISMATCH);
        assertProblemEquals(lpe, 6, null,
                "parser.warning.startBytePossibleMismatch",
                ProblemType.START_BYTE_POSSIBLE_MISMATCH, "573", "568");

        lpe = label.getProblems().get(1);
        assertProblemEquals(lpe, 4, null, "parser.error.wrongLineLength",
                ProblemType.WRONG_LINE_LENGTH, 79, 80);

        lpe = label.getProblems().get(2);
        assertProblemEquals(lpe, 5, null, "parser.error.wrongLineLength",
                ProblemType.WRONG_LINE_LENGTH, 79, 78);

        assertEquals(3, label.getProblems().size());
    }

    // test attached with blank fill after end
    public void testAttachedFill() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "attachedBlankFill.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label,
                ProblemType.EXCESSIVE_LINE_LENGTH);
        assertProblemEquals(lpe, 3, null, "parser.error.lineTooLong",
                ProblemType.EXCESSIVE_LINE_LENGTH, "79");

        assertEquals(1, label.getProblems().size());
    }

    // test attached label with stream type
    public void testAttachedStream() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "attachedStream.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label,
                ProblemType.EXCESSIVE_LINE_LENGTH);
        assertProblemEquals(lpe, 3, null, "parser.error.lineTooLong",
                ProblemType.EXCESSIVE_LINE_LENGTH, "79");

        assertEquals(1, label.getProblems().size());
    }

    // test attached with blank fill after end and data that starts with
    // whitespace
    public void testAttachedFillWWhite() throws LabelParserException,
            IOException {
        final File testFile = new File(LABEL_DIR, "attachedBlankFillWWhite.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label,
                ProblemType.EXCESSIVE_LINE_LENGTH);
        assertProblemEquals(lpe, 3, null, "parser.error.lineTooLong",
                ProblemType.EXCESSIVE_LINE_LENGTH, "79");

        lpe = assertHasProblem(label, ProblemType.START_BYTE_POSSIBLE_MISMATCH);
        assertProblemEquals(lpe, 6, null,
                "parser.warning.startBytePossibleMismatch",
                ProblemType.START_BYTE_POSSIBLE_MISMATCH, "573", "568");

        assertEquals(2, label.getProblems().size());
    }

    // incorrect start byte
    public void testInvalidAttachedPadded() throws LabelParserException,
            IOException {
        final File testFile = new File(LABEL_DIR, "attachedPaddedInvalid.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label,
                ProblemType.ATTACHED_START_BYTE_MISMATCH);
        assertProblemEquals(lpe, 6, null, "parser.error.startByteMismatch",
                ProblemType.ATTACHED_START_BYTE_MISMATCH, "568", "569");

        assertEquals(1, label.getProblems().size());
    }

    // lines over limit and start byte incorrect
    public void testInvalidAttachedFill() throws LabelParserException,
            IOException {
        final File testFile = new File(LABEL_DIR,
                "attachedBlankFillInvalid.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label,
                ProblemType.ATTACHED_START_BYTE_MISMATCH);
        assertProblemEquals(lpe, 6, null, "parser.error.startByteMismatch",
                ProblemType.ATTACHED_START_BYTE_MISMATCH, "568", "569");

        assertEquals(1, label.getProblems().size());
    }

    // TODO: determine if there are more errors that should be surfaced here,
    // appears attached but no pointer and fixed...
    public void testAttachedMissingPointer() throws LabelParserException,
            IOException {
        final File testFile = new File(LABEL_DIR,
                "attachedBlankFillNoPointer.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label,
                ProblemType.EXCESSIVE_LINE_LENGTH);
        assertProblemEquals(lpe, 3, null, "parser.error.lineTooLong",
                ProblemType.EXCESSIVE_LINE_LENGTH, "79");

        assertEquals(1, label.getProblems().size());
    }

    // make sure that a valid label with multiple internal pointers gets the
    // right pointer when testing for start byte
    public void testLowestPointer() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR,
                "attachedMultiplePointers.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        assertEquals(0, label.getProblems().size());
    }
}
