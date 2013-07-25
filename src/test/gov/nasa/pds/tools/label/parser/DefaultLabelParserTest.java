// Copyright 2006-2013, by the California Institute of Technology.
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

package gov.nasa.pds.tools.label.parser;

import gov.nasa.pds.tools.BaseTestCase;
import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.StandardPathResolver;

import java.io.File;
import java.io.IOException;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
@SuppressWarnings("nls")
public class DefaultLabelParserTest extends BaseTestCase {

    public void testPlaceholderTest() throws LabelParserException, IOException {
        final File sampleDir = new File(TEST_DIR, "labels");

        final File testFile = new File(sampleDir, "ITEMS.LBL");

        StandardPathResolver resolver = new StandardPathResolver();
        DefaultLabelParser parser = new DefaultLabelParser(resolver);
        Label label = parser.parseLabel(testFile);
        assertTrue(label.isValid());

    }

    public void testSFDU() throws LabelParserException, IOException {
        final File sampleDir = new File(TEST_DIR, "labels");

        final File testFile = new File(sampleDir, "SFDULabel.lbl");

        StandardPathResolver resolver = new StandardPathResolver();
        DefaultLabelParser parser = new DefaultLabelParser(resolver);
        Label label = parser.parseLabel(testFile);
        assertTrue(label.isValid());

        assertEquals(0, label.getProblems().size());
    }

    public void testMissingLabel() throws IOException {
        final File sampleDir = new File(TEST_DIR, "labels");

        final File testFile = new File(sampleDir, "MISSING.LBL");

        StandardPathResolver resolver = new StandardPathResolver();
        DefaultLabelParser parser = new DefaultLabelParser(resolver);
        try {
            parser.parseLabel(testFile);
            fail("missing file should have thrown exception");
        } catch (LabelParserException e) {
            e.getKey().equals("parser.error.missingFile");
        }
    }

}
