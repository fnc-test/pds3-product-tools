package gov.nasa.pds.tools.util;

import gov.nasa.arc.pds.tools.util.StrUtils;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.Token;

public class AntlrUtils {

    public static String toSeparatedString(final List<Token> tokens) {
        List<String> vals = new ArrayList<String>();
        for (final Token token : tokens) {
            vals.add(token.getText());
        }
        return StrUtils.toSeparatedString(vals);
    }
}
