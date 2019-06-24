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
