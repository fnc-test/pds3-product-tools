package gov.nasa.pds.tools.label.validate;

import gov.nasa.pds.tools.BaseTestCase;
import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.label.Label;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("nls")
public class ValidatorTest extends BaseTestCase {

    // test that passing in no dictionary gets you default dictionary
    public void testDefaultDictionary() throws LabelParserException,
            IOException {
        // minimize contents
        final File testFile = new File(LABEL_DIR, "newValue.lbl");

        final Label label = PARSER.parseLabel(testFile);
        Validator validator = new Validator();
        validator.validate(label);

        // label has two entries, one should be known and one not
        LabelParserException lpe = assertHasProblem(label,
                ProblemType.UNKNOWN_VALUE, 3);
        assertProblemEquals(lpe, 3, null, "parser.warning.unknownValue",
                ProblemType.UNKNOWN_VALUE,
                "SATURN SATELLITE ASTROMETRY DATA FROM WFP2 2005",
                "VOLUME_SET_NAME",
                "SATURN SATELLITE ASTROMETRY DATA FROM WFP2 2005");
        assertEquals(1, label.getProblems().size());
    }

    // TODO: test flags on which tests to run is working

}
