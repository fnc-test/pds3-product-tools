package gov.nasa.pds.tools.util;

import gov.nasa.arc.pds.tools.util.LocaleUtils;
import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.constants.Constants;

import java.util.ResourceBundle;

public class MessageUtils {

    private static final LocaleUtils LOCALE_UTILS = new LocaleUtils(
            Constants.DEFAULT_LOCALE, true, "resources"); //$NON-NLS-1$

    private static LocaleUtils OVERRIDE_LOCALE_UTILS;

    public static void setOverride(final ResourceBundle bundle) {
        new LocaleUtils(Constants.DEFAULT_LOCALE, true, bundle);
    }

    public static String getText(final String key, final Object... arguments) {
        // has override, try there first
        if (OVERRIDE_LOCALE_UTILS != null) {
            try {
                return OVERRIDE_LOCALE_UTILS.getText(key, arguments);
            } catch (final Exception e) {
                // not found in override, try default
                return LOCALE_UTILS.getText(key, arguments);
            }
        }
        return LOCALE_UTILS.getText(key, arguments);
    }

    public static String getNumber(final Number number) {
        return LOCALE_UTILS.getNumber(number);
    }

    public static String formatBytes(final double memory) {
        return LOCALE_UTILS.formatBytes(memory);
    }

    public static String getProblemMessage(final LabelParserException problem) {
        return getText(problem.getKey(), problem.getArguments());
    }

}
