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

}
