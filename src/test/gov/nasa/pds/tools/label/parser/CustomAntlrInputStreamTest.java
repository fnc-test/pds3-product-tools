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

import gov.nasa.arc.pds.tools.util.StrUtils;
import gov.nasa.pds.tools.BaseTestCase;
import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.StandardPathResolver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SuppressWarnings("nls")
public class CustomAntlrInputStreamTest extends BaseTestCase {

    final File sampleDir = new File(TEST_DIR, "labels");

    public void testReadByte() throws IOException {
        final File testFile = new File(this.sampleDir, "attachedBlankFill.lbl");

        FileInputStream inputStream = new FileInputStream(testFile);
        BufferedInputStream bis = new BufferedInputStream(inputStream);

        final CustomAntlrInputStream is = new CustomAntlrInputStream(bis);
        byte b = (byte) is.read();
        String expected = "P";

        String result = new String(new byte[] { b });

        assertEquals(expected, result);
    }

    public void testReadBytes() throws IOException {
        final File testFile = new File(this.sampleDir, "attachedBlankFill.lbl");

        FileInputStream inputStream = new FileInputStream(testFile);
        BufferedInputStream bis = new BufferedInputStream(inputStream);

        final CustomAntlrInputStream is = new CustomAntlrInputStream(bis);
        byte[] b = new byte[21];
        is.read(b);
        String expected = "PDS_VERSION_ID = PDS3";
        String result = new String(b);

        assertEquals(expected, result);
    }

    public void testParseValid() throws LabelParserException, IOException {
        final File testFile = new File(this.sampleDir, "attachedBlankFill.lbl");

        StandardPathResolver resolver = new StandardPathResolver();
        DefaultLabelParser parser = new DefaultLabelParser(resolver);
        Label label = parser.parseLabel(testFile);
        assertTrue(label.isValid());

        assertEquals(0, label.getProblems().size());
        assertTrue(label.hasAttachedContent());
    }

    public void testValidPadded() throws Exception {
        final File testFile = new File(this.sampleDir, "attachedBlankFill.lbl");

        FileInputStream inputStream = new FileInputStream(testFile);
        BufferedInputStream bis = new BufferedInputStream(inputStream);

        final CustomAntlrInputStream is = new CustomAntlrInputStream(bis);
        final String contents = StrUtils.toString(is);
        // subtract num lines in this case since StrUtils.toString(is) converts
        // any newline sequences to '\n'
        int numLines = 8;
        assertEquals(569 - numLines, contents.length());
    }

    public void testBinaryAttached() throws Exception {
        final File testFile = new File(this.sampleDir,
                "attachedBlankBinary.img");

        StandardPathResolver resolver = new StandardPathResolver();
        DefaultLabelParser parser = new DefaultLabelParser(resolver);
        Label label = parser.parseLabel(testFile);
        assertTrue(label.isValid());

        assertTrue(label.hasAttachedContent());
        assertEquals(label.getAttachedStartByte(), 20481);
        assertEquals(0, label.getProblems().size());

    }
}
