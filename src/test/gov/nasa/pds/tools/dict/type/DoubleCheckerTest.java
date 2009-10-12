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
public class DoubleCheckerTest extends BaseTestCase {

    public void testTypeMismatch() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "double.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label,
                ProblemType.TYPE_MISMATCH);
        assertProblemEquals(lpe, 3, null, "parser.error.typeMismatch",
                ProblemType.TYPE_MISMATCH, "DUB", "DOUBLE", "TextString",
                "should be double");
    }

    public void testShort() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "double.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label,
                ProblemType.SHORT_VALUE);
        assertProblemEquals(lpe, 4, null, "parser.error.tooShort",
                ProblemType.SHORT_VALUE, "5", "1", "2", "DUB", "DOUBLE");
    }

    public void testLong() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "double.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label,
                ProblemType.EXCESSIVE_VALUE_LENGTH);
        assertProblemEquals(lpe, 5, null, "parser.error.tooLong",
                ProblemType.EXCESSIVE_VALUE_LENGTH, "5.000000009", "11", "9",
                "DUB", "DOUBLE");
    }

    public void testLessThanMin() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "double.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = label.getProblems().get(3);
        assertProblemEquals(lpe, 6, null, "parser.error.lessThanMin",
                ProblemType.OOR, "0.01", "5.0", "DUB", "DOUBLE");
    }

    public void testExceedsMax() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "double.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = label.getProblems().get(4);
        assertProblemEquals(lpe, 7, null, "parser.error.exceedsMax",
                ProblemType.OOR, "100.1", "100.0", "DUB", "DOUBLE");
    }

    public void testInvalidValue() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "double.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label,
                ProblemType.INVALID_TYPE);
        assertProblemEquals(lpe, 8, null, "parser.error.badReal",
                ProblemType.INVALID_TYPE, "2#10001#");
    }

    // For convenience, all test parts in one file but tested in different
    // methods. This test just confirms we covered all tests
    public void testNumErrors() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "double.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        assertEquals(6, label.getProblems().size());
    }

}
