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
package gov.nasa.pds.tools.constants;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * This class is a gathering of Constants and Enums that get used across
 * packages.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
@SuppressWarnings("nls")
public class Constants {

    public static final String DEFAULT_LOCALE_KEY = "en_US";

    public static final String DEFAULT_LANGUAGE = "en";

    public static final Locale DEFAULT_LOCALE = new Locale(DEFAULT_LOCALE_KEY);

    public final static String[] DESCRIPTION_NAMES = { "DESCRIPTION", "DESC" };

    public final static String[] INCLUDE_NAMES = { "STRUCTURE" };

    public final static String[] CATALOG_NAMES = { "DATA_SET_CATALOG",
            "DATA_SET_COLLECTION_CATALOG", "INSTRUMENT_CATALOG",
            "INSTRUMENT_HOST_CATALOG", "MISSION_CATALOG",
            "DATA_SET_MAP_PROJECTION_CATALOG", "PERSONNEL_CATALOG",
            "REFERENCE_CATALOG", "SOFTWARE_CATALOG", "TARGET_CATALOG" };

    public final static String CATALOG = "CATALOG";

    public final static String MAP_PROJECTION = "MAP_PROJECTION";

    // skip values
    public final static String UNKNOWN_VAL_CONST = "UNK";

    public final static String NULL_VAL_CONST = "NULL";

    public final static String NOT_APPLICABLE_VAL_CONST = "N/A";

    public enum PointerType {
        UNDEFINED, DATA_LOCATION, INCLUDE, DESCRIPTION
    }

    public enum DictionaryType {
        // ALLOWED - NOT USED
        ALPHABET, //
        ALPHANUMERIC, //
        // ALLOWED
        CHARACTER, //
        CONTEXT_DEPENDENT, // unused alias
        CONTEXTDEPENDENT, //
        DATE, //
        INTEGER, //
        REAL, //
        TIME, //
        NONDECIMAL, // 
        NON_DECIMAL, //
        // NOT ALLOWED - IN DICTIONARY
        ASCII_INTEGER, // -> TREAT AS INTEGER
        BIBLIO, // -> TREAT AS CHARACTER
        DATA_SET, // -> TREAT AS CHARACTER
        DECIMAL, // -> TREAT AS REAL
        DOUBLE, // -> TREAT AS REAL
        EXPONENTIAL, // -> TREAT AS REAL
        IDENTIFIER, // -> TREAT AS CHARACTER
    }

    public enum ProblemType {
        // invalid label problems
        INVALID_LABEL(Severity.ERROR), // unable to parse label or cat file
        // TODO: used?
        INVALID_LABEL_WARNING(Severity.WARNING), // non-fatal parse error
        PARSE_ERROR(Severity.ERROR), // generic parse error
        UNKNOWN_KEY(Severity.ERROR), // key not in dictionary
        UNKNOWN_VALUE_TYPE(Severity.ERROR), // value type not in dictionary
        UNKNOWN_VALUE(Severity.WARNING), // id doesn't exist (yet?)
        TYPE_MISMATCH(Severity.ERROR), // type of found value is not correct
        // TODO: use? needed?
        NEW_VALUE(Severity.INFO), // value to add to dictionary
        INVALID_VALUE(Severity.ERROR), // value doesn't exist, likely a mistake
        MANIPULATED_VALUE(Severity.WARNING), // value valid only if modified
        INVALID_TYPE(Severity.ERROR), // ex. not a number when it should be
        INVALID_MEMBER(Severity.ERROR), // child property not allowed in object
        MISSING_MEMBER(Severity.ERROR), // missing required child obj
        MISSING_PROPERTY(Severity.ERROR), // missing required property
        POTENTIAL_POINTER_PROBLEM(Severity.WARNING), // may be wrong link
        EXCESSIVE_LINE_LENGTH(Severity.WARNING), // line is too long
        WRONG_LINE_LENGTH(Severity.WARNING), // line length doesn't match
        // RECORD_SIZE
        ILLEGAL_LINE_ENDING(Severity.ERROR), // not CRLF
        MISSING_ID(Severity.ERROR), // no id
        EXCESSIVE_NAMESPACE_LENGTH(Severity.ERROR), // namespace too long
        EXCESSIVE_IDENTIFIER_LENGTH(Severity.ERROR), // identifier too long
        MISSING_VALUE(Severity.WARNING), // no val for key
        SHORT_VALUE(Severity.ERROR), // value too short
        BAD_VALUE(Severity.ERROR), // value is malformed somehow
        EXCESSIVE_VALUE_LENGTH(Severity.ERROR), // value too long
        OOR(Severity.ERROR), // out of range
        INVALID_DESCRIPTION(Severity.ERROR), // no idea
        INVALID_DATE(Severity.ERROR), // bad date
        CIRCULAR_POINTER_REF(Severity.ERROR), // circular file ref
        BAD_CATALOG_NAME(Severity.ERROR), // bad catalog pointer name
        DUPLICATE_IDENTIFIER(Severity.ERROR), // duplicate identifier
        PLACEHOLDER_VALUE(Severity.INFO), // placeholder 'NULL' instead of 'UNK'
        START_BYTE_MISMATCH(Severity.ERROR), // pointer val != actual start byte
        START_BYTE_POSSIBLE_MISMATCH(Severity.WARNING), // WS start?

        // invalid label fragment problems
        INVALID_LABEL_FRAGMENT(Severity.WARNING), //
        FRAGMENT_HAS_VERSION(Severity.WARNING), //
        FRAGMENT_HAS_SFDU(Severity.WARNING), //

        // tabular data problem
        COLUMN_NUMBER_MISMATCH(Severity.ERROR), // wrong num columns
        COLUMN_LENGTH_MISMATCH(Severity.ERROR), // wrong value length
        INVALID_VALUE_FOR_COLUMN(Severity.ERROR), // wrong value type

        // file problems
        MISSING_RESOURCE(Severity.ERROR), // missing file from pointer
        MISSING_INDEX_RESOURCE(Severity.ERROR), // missing file from index
        MISSING_REQUIRED_RESOURCE(Severity.ERROR), // missing file or folder
        UNKNOWN_FILE(Severity.WARNING), // unexpected file without label
        LABEL_NOT_INDEXED(Severity.WARNING), // label not listed in an index
        LABEL_NOT_TO_BE_INDEXED(Severity.WARNING), // shouldn't be in index
        // TODO: use?
        UNKNOWN_FOLDER(Severity.INFO), // unexpected folder
        EMPTY_FILE(Severity.WARNING), // empty file
        EMPTY_FOLDER(Severity.WARNING), // empty folder
        MISMATCHED_CASE(Severity.WARNING), // path cases don't match

        // product problems
        // TODO: use
        BAD_FORMAT(Severity.ERROR); // product doesn't match format specified in
        // format file or in meta info

        private final Severity severity;

        private ProblemType(final Severity severity) {
            this.severity = severity;
        }

        public Severity getSeverity() {
            return this.severity;
        }

    }

    public enum Severity {
        NONE(4, "NONE"), //
        INFO(3, "INFO"), //
        WARNING(2, "WARNING"), //
        ERROR(1, "ERROR"); //

        // used for sorting
        private final int value;

        // used for display
        private final String name;

        private Severity(final int value, final String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return this.value;
        }

        public String getName() {
            return this.name;
        }
    }

    public enum LabelType {
        UNDEFINED, ATTACHED, DETACHED, COMBINED_DETACHED
    }

    public static final String README_NAME = "AAREADME.TXT";

    public static final String INDEX_FOLDER_NAME = "INDEX";

    public static final String INDEX_INFO_FILE_NAME = "INDXINFO.TXT";

    // public static final String INDEX_FILE_NAME = "index.tab";

    public static final String BROWSE_FOLDER_NAME = "BROWSE";

    public static final String BROWSE_INFO_FILE_NAME = "BROWINFO.TXT";

    public static final String CATALOG_FOLDER_NAME = "CATALOG";

    public static final String CATALOG_INFO_FILE_NAME = "CATINFO.TXT";

    public static final String CALIB_FOLDER_NAME = "CALIB";

    public static final String CALIB_INFO_FILE_NAME = "CALINFO.TXT";

    public static final String VOLUME_DESC_FILE_NAME = "VOLDESC.CAT";

    public static final String DOCUMENT_FOLDER_NAME = "DOCUMENT";

    public static final String DOCUMENT_INFO_FILE_NAME = "DOCINFO.TXT";

    public static final String EXTRAS_FOLDER_NAME = "EXTRAS";

    public static final String EXTRAS_INFO_FILE_NAME = "EXTRINFO.TXT";

    public static final String ERRATA_FILE_NAME = "ERRATA.TXT";

    public static final String GAZETTEER_FOLDER_NAME = "GAZETTEER";

    public static final String GAZETTEER_INFO_FILE_NAME = "GAZINFO.TXT";

    public static final String GAZETTEER_DESC_FILE_NAME = "GAZETTER.TXT";

    public static final String GAZETTEER_LABEL_FILE_NAME = "GAZETTER.LBL";

    public static final String GAZETTEER_TAB_FILE_NAME = "GAZETTER.TAB";

    public static final String GEOMETRY_FOLDER_NAME = "GEOMETRY";

    public static final String GEOMETRY_INFO_FILE_NAME = "GEOMINFO.TXT";

    public static final String LABEL_FOLDER_NAME = "LABEL";

    public static final String LABEL_INFO_FILE_NAME = "LABINFO.TXT";

    public static final String SOFTWARE_FOLDER_NAME = "SOFTWARE";

    public static final String SOFTWARE_INFO_FILE_NAME = "SOFTINFO.TXT";

    public static final String DATA_FOLDER_NAME = "DATA";

    public static final String TEXT_FILE_ENCODING = "ASCII";

    public static final String[] NON_INDEXED_FOLDERS = new String[] { "INDEX",
            "DOCUMENT", "CATALOG", "CALIB", "CALIBRATION", "GEOMETRY",
            "BROWSE", "EXTRAS" };

    public static final String[] ILLEGAL_INDEXED_FOLDERS = new String[] {
            "DOCUMENT", "CATALOG" };

    // regular expressions for data types
    // ex "1.12E-12 "
    public static final String ASCII_REAL_REGEX = "^\\s*[-+]?((\\d+(\\.\\d*)?)|(\\.\\d+))([Ee][-+]?\\d+)?\\s*$";

    // ex "-13 "
    public static final String ASCII_INTEGER_REGEX = "^\\s*[-+]?\\d+\\s*$";

    // ex 16#FFF# - note that doesn't check that number valid for base...
    public static final String NON_DECIMAL_REGEX = "^(\\d{1,2})#([-+]?)([0-9a-fA-F]+)#$";

    public static final Pattern NON_DECIMAL_PATTERN = Pattern
            .compile(NON_DECIMAL_REGEX);

    // public static final String CHARACTER_REGEX =
    // "([^\"][^\\s]*[^\"])|(\".*\")";
    public static final String CHARACTER_REGEX = ".+";

}
