// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// • Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// • Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// • Neither the name of Caltech nor its operating division, the Jet Propulsion
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

package gov.nasa.pds.tools.label;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements a parser of PDS3 date/time values. The PDS3 date/time format is a
 * subset of the ISO8601 standard. The PDS3 date/time formats are defined in
 * Chapter 7 of the PDS Standards Reference.
 * 
 * <p>
 * According to section 7.1 of the PDS Standards reference, these two formats
 * are supported:
 * </p>
 * <ul>
 * <li><em>year-month-day format:</em> yyyy-mm-ddTHH:MM:SS.NNN
 * <li><em>year-day format:</em> yyyy-dddTHH:MM:SS.NNN
 * </ul>
 * 
 * <p>
 * In the second format, the day is a day-of-year number from 1 to 366 (in leap
 * years). All numeric values in the two formats must have exactly the number of
 * digits shown, except for the fractional second value (NNNNNNNNN). That value
 * may be from 1 to 3 digitis in length. Each format may be abbreviated to
 * indicate a lower precision, in these ways:
 * </p>
 * yyyy-mm-ddTHH:MM:SS.NNNNNNNNNZ<br />
 * yyyy-mm-ddTHH:MM:SS.NNNNNNNNN<br />
 * yyyy-mm-ddTHH:MM:SSZ<br />
 * yyyy-mm-ddTHH:MM:SS<br />
 * yyyy-mm-ddTHH:MMZ<br />
 * yyyy-mm-ddTHH:MM<br />
 * yyyy-mm-ddTHHZ<br />
 * yyyy-mm-ddTHH<br />
 * yyyy-mm-dd<br />
 * yyyy-mm<br />
 * yyyy<br />
 * yyyy-dddTHH:MM:SS.NNNNNNNNNZ<br />
 * yyyy-dddTHH:MM:SS.NNNNNNNNN<br />
 * yyyy-dddTHH:MM:SSZ<br />
 * yyyy-dddTHH:MM:SS<br />
 * yyyy-dddTHH:MMZ<br />
 * yyyy-dddTHH:MM<br />
 * yyyy-dddTHHZ<br />
 * yyyy-dddTHH<br />
 * yyyy-ddd<br />
 * 
 * @author merose, pramirez
 * 
 */
class ParseDateTime {

  // A regular expression describing the format of the time portion of a
  // PDS date/time value. All fields other than the hour are optional.
  // The optional 'Z' is for historic purposes
  private static final String TIME_PATTERN_STRING = // HH : mm : ss .nnn Z
  "(\\d{2})(?::(\\d{2})(?::(\\d{2})(\\.\\d{1,9})?)?)?(Z)?"; //$NON-NLS-1$

  // Lenient version of time. Allows for comparison of datetimes as problems
  // will already be logged. Accounts for shorter versions of fields and a
  // backwards compatible longer milliseconds.
  private static final String LENIENT_TIME_PATTERN_STRING = "(\\d{1,2})(?::(\\d{1,2})(?::(\\d{1,2})(\\.\\d{1,10})?)?)?(Z)?";

  // A regular expression (in 3 parts) describing a PDS3 date/time string
  // with a year, month, and day.
  private static final String YMD_PATTERN_STRING_PART1 = // yyyy - MM - DD
  "(\\d{4})(?:-(\\d{2})(?:-(\\d{2})(?:T"; //$NON-NLS-1$
  private static final String YMD_PATTERN_STRING_PART2 = ")?)?)?"; //$NON-NLS-1$
  // Lenient version of YMD pattern
  private static final String LENIENT_YMD_PATTERN_STRING_PART1 = "(\\d{1,4})(?:-(\\d{1,2})(?:-(\\d{1,2})(?:T";

  // A regular expression (in 3 parts) describing a PDS3 date/time string
  // with a year and day of year.
  private static final String YD_PATTERN_STRING_PART1 = // yyyy - DDD
  "(\\d{4})-(\\d{3})(?:T"; //$NON-NLS-1$
  private static final String YD_PATTERN_STRING_PART2 = ")?"; //$NON-NLS-1$
  // Lenient version of YD pattern
  private static final String LENIENT_YD_PATTERN_STRING_PART1 = "(\\d{1,4})-(\\d{1,3})(?:T";

  // Compiled regular expressions for PDS3 date/time formats.
  private static final Pattern TIME_PATTERN = Pattern
      .compile(TIME_PATTERN_STRING);
  private static final Pattern YEAR_MONTH_DAY_PATTERN = Pattern
      .compile(YMD_PATTERN_STRING_PART1 + TIME_PATTERN
          + YMD_PATTERN_STRING_PART2);
  private static final Pattern YEAR_DAY_PATTERN = Pattern
      .compile(YD_PATTERN_STRING_PART1 + TIME_PATTERN + YD_PATTERN_STRING_PART2);

  // Compiled lenient regular expressions for PDS3 date/time formats.
  private static final Pattern LENIENT_TIME_PATTERN = Pattern
      .compile(LENIENT_TIME_PATTERN_STRING);
  private static final Pattern LENIENT_YEAR_MONTH_DAY_PATTERN = Pattern
      .compile(LENIENT_YMD_PATTERN_STRING_PART1 + LENIENT_TIME_PATTERN
          + YMD_PATTERN_STRING_PART2);
  private static final Pattern LENIENT_YEAR_DAY_PATTERN = Pattern
      .compile(LENIENT_YD_PATTERN_STRING_PART1 + LENIENT_TIME_PATTERN
          + YD_PATTERN_STRING_PART2);

  /**
   * The calendar fields that should be filled in from a PDS3 date/time string
   * in the year-month-day format.
   */
  private static final int[] YEAR_MONTH_DAY_FIELDS = { Calendar.YEAR,
      Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY,
      Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND };

  /**
   * The calendar fields that should be filled in from a PDS3 date/time string
   * in the year, day-of-year format.
   */
  private static final int[] YEAR_DAY_FIELDS = { Calendar.YEAR,
      Calendar.DAY_OF_YEAR, Calendar.HOUR_OF_DAY, Calendar.MINUTE,
      Calendar.SECOND, Calendar.MILLISECOND };

  /**
   * Parses a PDS3 date/time string and returns the Java {@link Date}
   * corresponding to that date/time.
   * 
   * @param s
   *          a PDS3 date/time string
   * @return the {@link Date} represented by the date/time string
   * @throws ParseException
   *           if there is any error parsing the date/time string
   */
  public static Date parse(String s) {
    Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT")); //$NON-NLS-1$
    calendar.clear();
    Matcher yearMonthDayMatcher = YEAR_MONTH_DAY_PATTERN.matcher(s);
    Matcher yearDayMatcher = YEAR_DAY_PATTERN.matcher(s);

    // First try yyyy-MM-dd
    if (yearMonthDayMatcher.matches()) {
      parseMatches(yearMonthDayMatcher, YEAR_MONTH_DAY_FIELDS, calendar, false);
    } else if (yearDayMatcher.matches()) {
      parseMatches(yearDayMatcher, YEAR_DAY_FIELDS, calendar, false);
    } else {
      throw new ParseException("Improper date format", -1, null); //$NON-NLS-1$
    }

    return calendar.getTime();
  }

  public static Date lenientParse(String s) {
    Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT")); //$NON-NLS-1$
    calendar.clear();
    Matcher lenientYearMonthDayMatcher = LENIENT_YEAR_MONTH_DAY_PATTERN
        .matcher(s);
    Matcher lenientYearDayMatcher = LENIENT_YEAR_DAY_PATTERN.matcher(s);

    // First try yyyy-MM-dd
    if (lenientYearMonthDayMatcher.matches()) {
      parseMatches(lenientYearMonthDayMatcher, YEAR_MONTH_DAY_FIELDS, calendar,
          true);
    } else if (lenientYearDayMatcher.matches()) {
      parseMatches(lenientYearDayMatcher, YEAR_DAY_FIELDS, calendar, true);
    } else {
      throw new ParseException("Improper date format", -1, null); //$NON-NLS-1$
    }

    return calendar.getTime();
  }

  /**
   * Fills in the fields of a {@link Calendar} from substrings of a PDS3
   * date/time string matched by a regular expression grouping patterns. Each
   * field is checked to see if the substring value is in the proper range for
   * that field.
   * 
   * @param matcher
   *          a regular expression matcher whose groups correspond to substrings
   *          for calendar fields
   * @param fields
   *          an array of field indices in a {@link Calendar}, in the order they
   *          should have been matched
   * @param calendar
   *          a calendar whose fields should be filled in by the matched
   *          substrings
   * @param lenient
   *          indicates whether a check to see if field is out of range is done
   *          and throws an exception
   * @throws ParseException
   *           if any calendar field is out of range
   */
  static void parseMatches(Matcher matcher, int[] fields, Calendar calendar,
      final Boolean lenient) {
    // Groups are numbered from 1..n. (Group 0 is the entire matched string.)
    for (int i = 1; i < matcher.groupCount(); ++i) {
      String part = matcher.group(i);

      // If we've reached a group that didn't match, then nothing else
      // will match, because of the way we've constructed the patterns.
      if (part == null) {
        break;
      }

      int field = fields[i - 1];
      int value;

      // The month calendar field is zero-relative, which is different
      // than all other fields. Also, the fractional second part may
      // be 1 to 3 digits, so we have to convert it to a floating
      // point value before converting to an integer, so that the
      // digit positions are interpreted correctly.
      if (field == Calendar.MONTH) {
        value = Integer.parseInt(part) - 1;
      } else if (field == Calendar.MILLISECOND) {
        value = (int) Math.round(1000 * Double.parseDouble(part));
      } else {
        value = Integer.parseInt(part);
      }

      if (!lenient
          && (value < calendar.getActualMinimum(field) || value > calendar
              .getActualMaximum(field))) {
        throw new ParseException("Calendar field out of range", field, part); //$NON-NLS-1$
      }

      calendar.set(field, value);
    }
  }

  /**
   * Implements an exception generated while parsing a date/time string that
   * keeps track of the calendar field which had the wrong format.
   * 
   * @author merose
   * 
   */
  public static class ParseException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /** The field index of a {@link Calendar} field which didn't parse. */
    private int field;

    /** The string value that was out of range for a calendar field. */
    private String fieldValue;

    /**
     * Creates a new parse exception.
     * 
     * @param msg
     *          the exception message
     * @param field
     *          the calendar field that was in error, or -1 if the exception is
     *          not specific to a calendar field
     * @param fieldValue
     *          the string value that was to be stored in the calendar field
     */
    public ParseException(String msg, int field, String fieldValue) {
      super(msg);
      this.field = field;
      this.fieldValue = fieldValue;
    }

    /**
     * Gets the calendar field index for which a portion of the date/time string
     * was out of range.
     * 
     * @return the index of a calendar field, or -1 if no specific field was in
     *         error
     */
    public int getField() {
      return this.field;
    }

    /**
     * Gets the string value to be stored in a calendar field.
     * 
     * @return the string value of a portion of a date/time string
     */
    public String getFieldValue() {
      return this.fieldValue;
    }
  }

}