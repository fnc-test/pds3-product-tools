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
import gov.nasa.pds.tools.label.parser.DefaultLabelParser;

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
        final File sampleDir = new File(TEST_DIR, "labels");
        final File testFile = new File(sampleDir, "selfpointer.lbl");

        StandardPathResolver resolver = new StandardPathResolver();
        DefaultLabelParser parser = new DefaultLabelParser(resolver);
        Label label = parser.parseLabel(testFile);
        assertHasProblem(label, ProblemType.CIRCULAR_POINTER_REF);

    }

    public void testCircularReferences() throws LabelParserException,
            IOException {
        final File sampleDir = new File(TEST_DIR, "labels");
        final File testFile = new File(sampleDir, "circular1.lbl");

        StandardPathResolver resolver = new StandardPathResolver();
        DefaultLabelParser parser = new DefaultLabelParser(resolver);
        Label label = parser.parseLabel(testFile);
        assertHasProblem(label, ProblemType.CIRCULAR_POINTER_REF);
    }

    public void testProblemsSuppressed() throws LabelParserException,
            IOException {
        final File sampleDir = new File(TEST_DIR, "labels");
        final File testFile = new File(sampleDir, "parent.lbl");

        StandardPathResolver resolver = new StandardPathResolver();
        DefaultLabelParser parser = new DefaultLabelParser(resolver);
        Label label = parser.parseLabel(testFile);
        validate(label);

        assertDoesntHaveProblem(label, ProblemType.EXCESSIVE_VALUE_LENGTH);
    }
}
