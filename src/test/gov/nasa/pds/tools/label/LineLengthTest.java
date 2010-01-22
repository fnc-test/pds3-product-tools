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
