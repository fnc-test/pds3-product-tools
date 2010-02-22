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

        BufferedInputStream inputStream;
        inputStream = new BufferedInputStream(new FileInputStream(testFile));
        inputStream.mark(100);

        final CustomAntlrInputStream is = new CustomAntlrInputStream(
                inputStream);
        final String contents = StrUtils.toString(is);
        // subtract num lines in this case since StrUtils.toString(is) converts
        // any newline sequences to '\n'
        int numLines = 8;
        assertEquals(569 - numLines, contents.length());
    }

}
