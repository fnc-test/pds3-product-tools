package gov.nasa.pds.tools.util;

import gov.nasa.pds.tools.BaseTestCase;
import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.label.Label;

import java.io.File;
import java.io.IOException;

// TODO: flesh out tests. 
@SuppressWarnings("nls")
public class MessageUtilsTest extends BaseTestCase {

    public void testProblemMessage() throws LabelParserException, IOException {
        final File testFile = new File(LABEL_DIR, "alphabetic.lbl");

        final Label label = PARSER.parseLabel(testFile);
        validate(label);

        LabelParserException lpe = assertHasProblem(label,
                ProblemType.TYPE_MISMATCH, 5);
        assertEquals(
                "\"ALPHA\" only accepts a type of \"ALPHABET\" and value \"777\" was interpreted as a \"Numeric\".",
                MessageUtils.getProblemMessage(lpe));
    }
}
