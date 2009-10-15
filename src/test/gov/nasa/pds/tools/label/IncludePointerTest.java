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
public class IncludePointerTest extends BaseTestCase {

    public void testSelfReferences() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "selfpointer.lbl");

        Label label = PARSER.parseLabel(testFile);
        assertHasProblem(label, ProblemType.CIRCULAR_POINTER_REF);
        assertEquals(1, label.getProblems().size());

    }

    public void testCircularReferences() throws LabelParserException,
            IOException {
        final File testFile = new File(LABEL_DIR, "circular1.lbl");

        Label label = PARSER.parseLabel(testFile);
        assertHasProblem(label, ProblemType.CIRCULAR_POINTER_REF);
        assertEquals(1, label.getProblems().size());
    }

    public void testProblemsSuppressed() throws LabelParserException,
            IOException {
        final File testFile = new File(LABEL_DIR, "parent.lbl");

        Label label = PARSER.parseLabel(testFile);
        validate(label);

        assertDoesntHaveProblem(label, ProblemType.EXCESSIVE_VALUE_LENGTH);
        assertEquals(0, label.getProblems().size());
    }
}
