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
// $Id: LineEndingTest.java 5675 2010-01-22 20:22:50Z jagander $
//

package gov.nasa.pds.tools.label;

import gov.nasa.pds.tools.BaseTestCase;
import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.constants.Constants.ProblemType;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author jagander
 * @version $Revision: 5675 $
 * 
 */
@SuppressWarnings("nls")
public class LineEndingTest extends BaseTestCase {

    // test one line being over limit in stream
    public void testLFOnly() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "badLineEndings.img");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        final List<LabelParserException> probs = label.getProblems();
        LabelParserException prob;

        prob = probs.get(0);
        assertProblemEquals(prob, 2, null, "parser.error.badLineEnding",
                ProblemType.ILLEGAL_LINE_ENDING);

        prob = probs.get(1);
        assertProblemEquals(prob, 9, null, "parser.error.badLineEnding",
                ProblemType.ILLEGAL_LINE_ENDING);

        // extra prob not checked in this label
        assertEquals(3, label.getProblems().size());
    }
}
