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

import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.constants.Constants.ProblemType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class DateTimeFormatter {
    @SuppressWarnings("nls")
    private static SimpleDateFormat[] formats = {
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm"),
            new SimpleDateFormat("yyyy-MM-dd'T'HH'Z'"),
            new SimpleDateFormat("yyyy-MM-dd'T'HH"),

            // Must have day-of-year formats after formats with month,
            // so that yyyy-mm is not misinterpreted as a day-of-year.
            new SimpleDateFormat("yyyy-DDD'T'HH:mm:ss.SSS'Z'"),
            new SimpleDateFormat("yyyy-DDD'T'HH:mm:ss.SSS"),
            new SimpleDateFormat("yyyy-DDD'T'HH:mm:ss'Z'"),
            new SimpleDateFormat("yyyy-DDD'T'HH:mm:ss"),
            new SimpleDateFormat("yyyy-DDD'T'HH:mm'Z'"),
            new SimpleDateFormat("yyyy-DDD'T'HH:mm"),
            new SimpleDateFormat("yyyy-DDD'T'HH'Z'"),
            new SimpleDateFormat("yyyy-DDD'T'HH"),

            // do ones without 'T' last as they are less specific
            new SimpleDateFormat("yyyy-MM-dd"), //
            new SimpleDateFormat("yyyy-MM"), //
            new SimpleDateFormat("yyyy-DDD"), //
            new SimpleDateFormat("yyyy"), //
    };

    private static SimpleDateFormat hackDateFormat = new SimpleDateFormat(
            "yyyy-DDD"); //$NON-NLS-1$

    @SuppressWarnings("nls")
    private static SimpleDateFormat[] dateFormats = {
            new SimpleDateFormat("yyyy-MM-dd"), //
            new SimpleDateFormat("yyyy-MM"), //
            new SimpleDateFormat("yyyy-DDD"), //
            new SimpleDateFormat("yyyy"), //
    };

    static {
        for (int i = 0; i < formats.length; ++i) {
            formats[i].setLenient(false);
        }
        hackDateFormat.setLenient(false);
    }

    public static Date parse(final Label label, final String dateTime,
            final int lineNumber) throws LabelParserException {
        checkComponents(label, dateTime, lineNumber);
        Date d = lenientParse(label, dateTime, lineNumber);

        // Now format the date part back, to see if we get the same result, or a
        // prefix.
        String[] dateParts = dateTime.split("T"); //$NON-NLS-1$
        for (int i = 0; i < dateFormats.length; ++i) {
            String checkDate = dateFormats[i].format(d);
            if (dateParts[0].equals(checkDate)) {
                return d;
            }
        }

        // If we get here, then we didn't get back the same digits we put in,
        // which means the label author used something like "13" for a month,
        // or other value out of normal range, which SimpleDateFormat accepts,
        // and adjusts the date accordingly.
        throw new LabelParserException(label, lineNumber, null,
                "parser.error.dateOutOfRange", ProblemType.INVALID_DATE, //$NON-NLS-1$
                dateTime);
    }

    protected static Date lenientParse(final Label label,
            final String dateTime, final int lineNumber)
            throws LabelParserException {
        // hack to deal with date format stuff being too lenient on input
        if (dateTime.matches("[0-9]{4}-[0-9]{3}")) { //$NON-NLS-1$
            try {
                Date d = hackDateFormat.parse(dateTime);
                return d;
            } catch (ParseException ex) {
                // ignore
            } catch (NumberFormatException ex) {
                // ignore
            }
        }

        for (int i = 0; i < formats.length; ++i) {
            try {
                Date d = formats[i].parse(dateTime);
                return d;
            } catch (ParseException ex) {
                // ignore
            } catch (NumberFormatException ex) {
                // ignore
            }
        }

        // If we get here, no format matched.
        throw new LabelParserException(label, lineNumber, null,
                "parser.error.invalidDate", ProblemType.INVALID_DATE, dateTime); //$NON-NLS-1$
    }

    protected static void checkComponents(final Label label,
            final String dateTime, final int lineNumber)
            throws LabelParserException {
        String[] dateAndTime = dateTime.split("T"); //$NON-NLS-1$
        checkDateComponents(label, dateAndTime[0], lineNumber);
        if (dateAndTime.length > 1) {
            checkTimeComponents(label, dateAndTime[1], lineNumber);
        }
    }

    protected static void checkDateComponents(final Label label,
            final String dateStr, final int lineNumber)
            throws LabelParserException {
        String[] dateComponents = dateStr.split("-"); //$NON-NLS-1$
        if (dateComponents.length < 1) {
            throw new LabelParserException(label, lineNumber, null,
                    "parser.error.missingDateParts", ProblemType.INVALID_DATE, //$NON-NLS-1$
                    dateStr);
        }
        if (dateComponents.length > 3) {
            throw new LabelParserException(label, lineNumber, null,
                    "parser.error.extraDateParts", ProblemType.INVALID_DATE, //$NON-NLS-1$
                    dateStr);
        }

        checkComponent(label, dateComponents[0], 4, 4,
                "parser.error.badYearLength", //$NON-NLS-1$
                lineNumber);

        if (dateComponents.length == 2) {
            // yyyy-doy or yyyy-mm
            checkComponent(label, dateComponents[1], 2, 3,
                    "parser.error.badyMonthDayLength", lineNumber); //$NON-NLS-1$
        }

        if (dateComponents.length == 3) {
            // yyyy-mm-dd
            checkComponent(label, dateComponents[1], 2, 2,
                    "parser.error.badyMonthLength", lineNumber); //$NON-NLS-1$
            checkComponent(label, dateComponents[2], 2, 2,
                    "parser.error.badyDayLength", lineNumber); //$NON-NLS-1$
        }
    }

    protected static void checkComponent(final Label label,
            final String component, final int minLength, int maxLength,
            String key, int lineNumber) throws LabelParserException {
        if (component.length() < minLength || component.length() > maxLength) {
            throw new LabelParserException(label, lineNumber, null, key,
                    ProblemType.INVALID_DATE, component);
        }

    }

    protected static void checkTimeComponents(final Label label,
            final String timeStr, final int lineNumber)
            throws LabelParserException {
        String timeString = timeStr;
        if (timeString.endsWith("Z")) { //$NON-NLS-1$
            timeString = timeString.substring(0, timeString.length() - 1);
        }

        String[] timeAndFraction = timeString.split("\\."); //$NON-NLS-1$
        if (timeAndFraction.length > 1) {
            // Check fraction. (It's guaranteed to be of length >=1, because of
            // the way split() works.)
            if (timeAndFraction[1].length() > 3) {
                throw new LabelParserException(label, lineNumber, null,
                        "parser.error.badFractionalDate", //$NON-NLS-1$
                        ProblemType.INVALID_DATE, timeAndFraction[1]);
            }
        }

        String[] timeComponents = timeAndFraction[0].split(":"); //$NON-NLS-1$
        for (int i = 0; i < timeComponents.length; ++i) {
            if (timeComponents[i].length() != 2) {
                throw new LabelParserException(label, lineNumber, null,
                        "parser.error.badTimeSection", //$NON-NLS-1$
                        ProblemType.INVALID_DATE, timeString);
            }
        }
    }
}
