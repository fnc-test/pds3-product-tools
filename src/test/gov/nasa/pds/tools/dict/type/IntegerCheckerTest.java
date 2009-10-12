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
