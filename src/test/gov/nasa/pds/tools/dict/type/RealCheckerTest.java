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
