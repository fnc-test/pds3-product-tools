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

package gov.nasa.pds.tools;

import gov.nasa.arc.pds.tools.util.StrUtils;
import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.Statement;

import java.io.File;
import java.net.URI;

/**
 * This class captures any exceptions that arise during label parsing. If
 * possible the exception retains the context in which it occurred.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class LabelParserException extends Exception {

    private static final long serialVersionUID = -1;

    private final Integer lineNumber;

    private final Integer column;

    private final ProblemType type;

    private final Object[] arguments;

    private final String key;

    protected final URI sourceURI;

    protected final File sourceFile;

    /**
     * Use of this constructor is encouraged where possible as it retains the
     * context of the exception.
     * 
     * @param statement
     *            in which the exception occurred
     * @param column
     *            within the file where the exception started
     * @param key
     *            used to quickly associate lookup or filter this type of
     *            exception
     * @param type
     *            classification of the problem
     * @param arguments
     *            to be used in generating problem messages
     * 
     */
    public LabelParserException(final Statement statement,
            final Integer column, final String key, final ProblemType type,
            final Object... arguments) {
        super(key);
        this.sourceFile = statement.getSourceFile();
        this.sourceURI = statement.getSourceURI();
        this.lineNumber = statement.getLineNumber();
        this.column = column;
        this.type = type;
        this.key = key;
        this.arguments = arguments;
    }

    /**
     * Use this constructor for exceptions not associated to a specific file
     * such as not being able to parse due to missing file or bad URL
     * 
     * @param key
     *            used to quickly associate lookup or filter this type of
     *            exception
     * @param type
     *            classification of the problem
     * @param arguments
     *            to be used in generating problem messages
     */
    public LabelParserException(final String key, final ProblemType type,
            final Object... arguments) {
        super(key);
        this.sourceFile = null;
        this.sourceURI = null;
        this.lineNumber = null;
        this.column = null;
        this.type = type;
        this.key = key;
        this.arguments = arguments;
    }

    /**
     * Use this constructor for exceptions that are associated with a specific
     * file but not on a given line (e.g. missing statements)
     * 
     * @param label
     *            within which the exception occurred
     * @param lineNumber
     *            within the file where the exception started
     * @param column
     *            within the file where the exception started
     * @param key
     *            used to quickly associate lookup or filter this type of
     *            exception
     * @param type
     *            classification of the problem
     * @param arguments
     *            to be used in generating problem messages
     */
    public LabelParserException(final Label label, final Integer lineNumber,
            final Integer column, final String key, final ProblemType type,
            final Object... arguments) {
        super(key);
        this.sourceFile = label.getLabelFile();
        this.sourceURI = label.getLabelURI();
        this.lineNumber = lineNumber;
        this.column = column;
        this.type = type;
        this.key = key;
        this.arguments = arguments;
    }

    /**
     * Use this constructor for exceptions to be reported about a file that can
     * be traced back to a line and column
     * 
     * @param sourceFile
     *            within which the exception occurred
     * @param lineNumber
     *            within the file where the exception started
     * @param column
     *            within the file where the exception started
     * @param key
     *            used to quickly associate lookup or filter this type of
     *            exception
     * @param type
     *            classification of the problem
     * @param arguments
     *            to be used in generating problem messages
     */
    public LabelParserException(final File sourceFile,
            final Integer lineNumber, final Integer column, final String key,
            final ProblemType type, final Object... arguments) {
        super(key);
        this.sourceFile = sourceFile;
        this.sourceURI = null;
        this.lineNumber = lineNumber;
        this.column = column;
        this.type = type;
        this.key = key;
        this.arguments = arguments;
    }

    /**
     * Use this constructor for exceptions to be reported about a URI that can
     * be traced back to a line and column
     * 
     * @param sourceURI
     *            within which the exception occurred
     * @param lineNumber
     *            within the file where the exception started
     * @param column
     *            within the file where the exception started
     * @param key
     *            used to quickly associate lookup or filter this type of
     *            exception
     * @param type
     *            classification of the problem
     * @param arguments
     *            to be used in generating problem messages
     */
    public LabelParserException(final URI sourceURI, final Integer lineNumber,
            final Integer column, final String key, final ProblemType type,
            final Object... arguments) {
        super(key);
        this.sourceFile = null;
        this.sourceURI = sourceURI;
        this.lineNumber = lineNumber;
        this.column = column;
        this.type = type;
        this.key = key;
        this.arguments = arguments;
    }

    /**
     * 
     * @param sourceDictionary
     * @param lineNumber
     * @param column
     * @param key
     * @param type
     * @param arguments
     */
    // this should only be thrown from the creation of a dictionary, while it's
    // structurally possible to throw from ElementValidator, the definitions are
    // found from an already created dictionary so, presumably, each definition
    // will already have been checked for invalid type
    public LabelParserException(final Dictionary sourceDictionary,
            final Integer lineNumber, final Integer column, final String key,
            final ProblemType type, final Object... arguments) {
        super(key);
        this.sourceFile = sourceDictionary.getDictionaryFile();
        this.sourceURI = sourceDictionary.getDictionaryURI();
        this.lineNumber = lineNumber;
        this.column = column;
        this.type = type;
        this.key = key;
        this.arguments = arguments;
    }

    /**
     * 
     * @param e
     * @param lineNumber
     * @param column
     * @param type
     */
    // to be used as little as possible
    public LabelParserException(final Exception e, final Integer lineNumber,
            final Integer column, final ProblemType type) {
        super(e);
        this.sourceFile = null;
        this.sourceURI = null;
        this.lineNumber = lineNumber;
        this.column = column;
        this.type = type;
        this.key = null;
        this.arguments = null;
    }

    /**
     * 
     * @return URI of the file in which the exception occurred
     */
    public URI getSourceURI() {
        return this.sourceURI;
    }

    /**
     * 
     * @return File in which the exception occurred
     */
    public File getSourceFile() {
        return this.sourceFile;
    }

    /**
     * 
     * @return Line number of the exception if applicable
     */
    public Integer getLineNumber() {
        return this.lineNumber;
    }

    /**
     * 
     * @return Column number of the exception if applicable
     */
    public Integer getColumn() {
        return this.column;
    }

    /**
     * 
     * @return classification of the problem
     */
    public ProblemType getType() {
        return this.type;
    }

    /**
     * 
     * @return arguments to be used in producing a message about this exception
     */
    public Object[] getArguments() {
        return this.arguments;
    }

    /**
     * 
     * @return key of this exception which is useful for filtering
     */
    public String getKey() {
        return this.key;
    }

    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return "[" + this.type.toString() + " - \"" + this.key + "\" on line "
                + this.lineNumber + "] ("
                + StrUtils.toSeparatedString(this.arguments) + ")";
    }
}
