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

import gov.nasa.arc.pds.tools.util.StrUtils;
import gov.nasa.arc.pds.tools.util.URLUtils;
import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.dict.DictIdentifier;
import gov.nasa.pds.tools.dict.parser.DictIDFactory;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class represents a PDS label.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class Label {

    // able to be parsed as label
    private boolean valid = true;

    private Map<DictIdentifier, List<Statement>> statements;

    // may have source URL or file but not both
    private final URI labelURI;

    private final File labelFile;

    private String labelPath;

    // used to determine if problems should be captured. This is turned off for
    // included files since they are evaluated separately. However, since
    // circular pointer problems will always be swallowed, that type is
    // exempted.
    // TODO: figure out how to suppress circular ref problem when you intended
    // to capture no problems at all
    private boolean captureProblems = true;

    // Used to determine whether or not problems whose source is a different
    // label should be allowed to be captured in this label. By default this is
    // turned off as it can lead to double reporting of problems in the cases
    // where the referenced statements are included more than once or parsed
    // independently. Use caution when setting this flag to true as filtering of
    // problems would then need to be done at a higher level.
    private boolean allowExternalProblems = false;

    // Start byte of attached content if any was found. Note that this may be
    // inaccurate if data started with whitespace.
    private Integer attachedStartByte;

    // Whether or not blank space was found after END line. Used to determine
    // line length checking behavior. May not be entirely accurate and is
    // currently used in conjunction with a check to see how similar line
    // lengths are.
    private boolean hasBlankFill = false;

    private final List<LabelParserException> problems = new ArrayList<LabelParserException>();

    private final List<String> ancestorPaths = new ArrayList<String>();

    // indication as to whether label terminates with END statement. It should
    // not if it's a label fragment. Note that this is not impacted by attached
    // content following the END statement.
    private boolean hasEndStatement = false;

    public static class LineLength {
        int line;
        int length;

        public LineLength(int line, int length) {
            this.line = line;
            this.length = length;
        }

        public int getLine() {
            return this.line;
        }

        public int getLength() {
            return this.length;
        }
    }

    private List<LineLength> lineLengths = new ArrayList<LineLength>();

    public void setCaptureProblems(final boolean captureProblems) {
        this.captureProblems = captureProblems;
    }

    public boolean getCaptureProblems() {
        return this.captureProblems;
    }

    public void setAllowExternalProblems(final boolean allowExternalProblems) {
        this.allowExternalProblems = allowExternalProblems;
    }

    public boolean getAllowExternalProblems() {
        return this.allowExternalProblems;
    }

    public void setHasBlankFill(final boolean hasBlankFill) {
        this.hasBlankFill = hasBlankFill;
    }

    /**
     * Constructs an object representation of a PDS label.
     * 
     */
    public Label(final URI labelURI) {
        this.statements = new HashMap<DictIdentifier, List<Statement>>();
        this.labelURI = labelURI;
        if (labelURI != null) {
            this.labelPath = labelURI.toString();
        }
        this.labelFile = null;
    }

    public Label(final File labelFile) {
        this.statements = new HashMap<DictIdentifier, List<Statement>>();
        this.labelURI = null;
        this.labelFile = labelFile;
        if (labelFile != null) {
            this.labelPath = labelFile.toString();
        }
    }

    public boolean isValid() {
        return this.valid;
    }

    public void setInvalid() {
        this.valid = false;
    }

    public void setAttachedStartByte(final Integer startByte) {
        this.attachedStartByte = startByte;
    }

    public long getAttachedStartByte() {
        return this.attachedStartByte;
    }

    public boolean hasAttachedContent() {
        return this.attachedStartByte != null;
    }

    public String getLabelPath() {
        return this.labelPath;
    }

    // should only be used when you don't have a statement since statements
    // retain knowledge of their source file and we don't want to double report
    // an error when statements are imported
    public void addProblem(final int lineNumber, final String key,
            final ProblemType type, final Object... arguments) {
        addProblem(this.labelURI, lineNumber, null, key, type, arguments);
    }

    public void addProblem(final int lineNumber, final Integer column,
            final String key, final ProblemType type, final Object... arguments) {
        addProblem(this.labelURI, lineNumber, column, key, type, arguments);
    }

    public void addProblem(final Statement statement, final String key,
            final ProblemType type, final Object... arguments) {
        addProblem(statement, null, key, type, arguments);
    }

    public void addProblem(final Statement statement, final Integer column,
            final String key, final ProblemType type, final Object... arguments) {
        if (statement.getSourceFile() != null) {
            addProblem(statement.getSourceFile(), statement.getLineNumber(),
                    column, key, type, arguments);
        } else {
            addProblem(statement.getSourceURI(), statement.getLineNumber(),
                    column, key, type, arguments);
        }
    }

    public void addProblem(final URI sourceURI, final int lineNumber,
            final Integer column, final String key, final ProblemType type,
            final Object... arguments) {
        if (this.labelURI == null || this.labelURI.equals(sourceURI)) {
            final LabelParserException e = new LabelParserException(this,
                    lineNumber, column, key, type, arguments);
            addProblemLocal(e);
        } else if (this.allowExternalProblems) {
            final LabelParserException e = new LabelParserException(sourceURI,
                    lineNumber, column, key, type, arguments);
            addProblemLocal(e);
        }
    }

    public void addProblem(final File sourceFile, final int lineNumber,
            final Integer column, final String key, final ProblemType type,
            final Object... arguments) {
        final LabelParserException e = new LabelParserException(this,
                lineNumber, column, key, type, arguments);
        addProblem(sourceFile, e);
    }

    // for internal use so you can skip the test against the lpe source file or
    // url
    private void addProblemLocal(final LabelParserException e) {
        this.problems.add(e);
    }

    // try to only use when exception has context
    public void addProblem(final LabelParserException e) {
        // if capture problems and (same context add or allowing external
        // problems) else, if non-suppresable error, pass through anyway
        if (this.captureProblems
                && ((this.labelFile != null && (this.allowExternalProblems || this.labelFile
                        .equals(e.getSourceFile()))) || (this.labelURI != null && (this.allowExternalProblems || this.labelURI
                        .equals(e.getSourceURI()))))) {
            this.problems.add(e);
        } else if (e.getType().equals(ProblemType.CIRCULAR_POINTER_REF)) {
            this.problems.add(e);
        }
    }

    public void addProblem(final URI sourceURI, final LabelParserException e) {
        if (((this.allowExternalProblems || this.labelURI.equals(sourceURI)) && this.captureProblems)
                || e.getType().equals(ProblemType.CIRCULAR_POINTER_REF)) {
            addProblemLocal(e);
        }
    }

    public void addProblem(final File sourceFile, final LabelParserException e) {
        if (((this.allowExternalProblems || this.labelFile.equals(sourceFile)) && this.captureProblems)
                || e.getType().equals(ProblemType.CIRCULAR_POINTER_REF)) {
            addProblemLocal(e);
        }
    }

    public void addProblem(final Statement statement,
            final LabelParserException e) {
        if (statement.getSourceURI() != null) {
            addProblem(statement.getSourceURI(), e);
        } else {
            addProblem(statement.getSourceFile(), e);
        }
    }

    /**
     * Retrieves a statement with the identifier
     * 
     * @param identifier
     *            Identifies the statement(s) to retrieve
     * @return The named statement or null if not found
     */
    public List<Statement> getStatement(final DictIdentifier identifier) {
        return this.statements.get(identifier);
    }

    private Statement getElement(final DictIdentifier key) {
        if (key != null) {
            List<Statement> foundStatements = this.statements.get(key);
            if (foundStatements != null && foundStatements.size() > 0) {
                return foundStatements.get(0);
            }
        }
        return null;
    }

    private List<? extends Statement> getElements(final DictIdentifier key) {
        List<Statement> foundStatements = this.statements.get(key);
        if (foundStatements == null) {
            foundStatements = new ArrayList<Statement>();
        }
        return foundStatements;
    }

    private List<? extends Statement> getElements(final Class<?> classType) {
        final List<Statement> returnStatements = new ArrayList<Statement>();
        for (List<Statement> stmts : this.statements.values()) {
            for (Statement statement : stmts) {
                if (statement.getClass().equals(classType)) {
                    returnStatements.add(statement);
                }
            }
        }
        return returnStatements;
    }

    /**
     * Retrieves the attribute with the identifier or null if not found
     * 
     * @param identifier
     *            of attribute to find
     * @return attribute or null
     */
    public AttributeStatement getAttribute(String identifier) {
        DictIdentifier id = DictIDFactory.createElementDefId(identifier);
        return (AttributeStatement) getElement(id);
    }

    /**
     * Retrieves the groups with the identifier or null if not found
     * 
     * @param identifier
     *            of group to find
     * @return {@link List} of {@link GroupStatement}
     */
    @SuppressWarnings("unchecked")
    public List<GroupStatement> getGroups(String identifier) {
        DictIdentifier id = DictIDFactory.createGroupDefId(identifier);
        return (List<GroupStatement>) getElements(id);
    }

    /**
     * Retrieves the object with the identifier or null if not found
     * 
     * @param identifier
     *            of object to find
     * @return {@link List} of {@link ObjectStatement}
     */
    @SuppressWarnings("unchecked")
    public List<ObjectStatement> getObjects(String identifier) {
        DictIdentifier id = DictIDFactory.createObjectDefId(identifier);
        return (List<ObjectStatement>) getElements(id);
    }

    /**
     * Retrieves the statements associated with this label
     * 
     * @return {@link List} of {@link Statement}
     */
    public List<Statement> getStatements() {
        List<Statement> results = new ArrayList<Statement>();
        for (List<Statement> stmts : this.statements.values()) {
            results.addAll(stmts);
        }
        return results;
    }

    /**
     * Retrieves objects associated with this label
     * 
     * @return List of {@link ObjectStatement}
     */
    @SuppressWarnings("unchecked")
    public List<ObjectStatement> getObjects() {
        return (List<ObjectStatement>) getElements(ObjectStatement.class);
    }

    /**
     * Retrieves groups associated with this label
     * 
     * @return list of {@link GroupStatement}
     */
    @SuppressWarnings("unchecked")
    public List<GroupStatement> getGroups() {
        return (List<GroupStatement>) getElements(GroupStatement.class);
    }

    /**
     * Retrieves attributes associated with this label
     * 
     * @return list of {@link AttributeStatement}
     */
    @SuppressWarnings("unchecked")
    public List<AttributeStatement> getAttributes() {
        return (List<AttributeStatement>) getElements(AttributeStatement.class);
    }

    /**
     * Retrieves pointers associated with this label
     * 
     * @return list of {@link PointerStatement}
     */
    public List<PointerStatement> getPointers() {
        List<PointerStatement> returnPointers = new ArrayList<PointerStatement>();
        for (List<Statement> stmts : this.statements.values()) {
            for (Statement statement : stmts) {
                if (statement instanceof PointerStatement) {
                    returnPointers.add((PointerStatement) statement);
                }
            }
        }
        return returnPointers;
    }

    /**
     * Associates a statement with this label
     * 
     * @param statement
     *            to be added to label
     */
    public synchronized void addStatement(Statement statement) {
        List<Statement> stmnts = this.statements.get(statement.getIdentifier());
        if (stmnts == null) {
            stmnts = new ArrayList<Statement>();
            this.statements.put(statement.getIdentifier(), stmnts);
        }
        if (statement instanceof IncludePointer) {
            stmnts.add(statement);
            for (Statement stmt : ((IncludePointer) statement).getStatements()) {
                addStatement(stmt);
            }
        } else {
            stmnts.add(statement);
        }
    }

    public List<LabelParserException> getProblems() {
        return this.problems;
    }

    public void addAncestor(final String path) {
        if (!this.ancestorPaths.contains(path)) {
            this.ancestorPaths.add(path);
        }
    }

    public void addAncestors(final List<String> pointers) {
        for (final String pointer : pointers) {
            addAncestor(pointer);
        }
    }

    public boolean hasAncestor(final URI pointer) {
        if (pointer.equals(this.labelURI)) {
            return true;
        }
        return this.ancestorPaths.contains(pointer.toString());
    }

    public boolean hasAncestor(final File pointer) {
        if (pointer.equals(this.labelFile)) {
            return true;
        }
        return this.ancestorPaths.contains(pointer.toString());
    }

    public boolean hasIncludePointer(final String path) {
        return this.ancestorPaths.contains(path);
    }

    public List<String> getAncestors() {
        return this.ancestorPaths;
    }

    public URI getLabelURI() {
        return this.labelURI;
    }

    public File getLabelFile() {
        return this.labelFile;
    }

    public String getSourceString() {
        if (this.labelFile != null) {
            return this.labelFile.toString();
        }
        return this.labelURI.toString();
    }

    public String getSourceNameString() {
        if (this.labelFile != null) {
            return this.labelFile.getName();
        }
        return URLUtils.getFileName(this.labelURI);
    }

    /**
     * Remember a line length for a line within a label. The line length
     * includes the <CR><LF> line terminator.
     * 
     * @param line
     *            the line number for which we found a length
     * @param length
     *            the line length found
     */
    public void addLineLength(int line, int length) {
        this.lineLengths.add(new LineLength(line, length));
    }

    /**
     * Return the line lengths for all label lines.
     * 
     * @return a list of the line lengths found
     */
    public List<LineLength> getLineLengths() {
        return this.lineLengths;
    }

    // TODO: make separate check for existence of attached content when internal
    // pointer because A) pointer resolution is encapsulated elsewhere and B)
    // this handles only the start of all attached content, not each section (ie
    // if there is a header section AND a data section)
    public void checkLineLengths() {
        // type (fixed vs stream || variable) required to determine checking
        // behavior
        String recordType = null;
        Integer recordBytes = null;

        AttributeStatement typeAttribute = getAttribute("RECORD_TYPE"); //$NON-NLS-1$
        if (typeAttribute != null) {
            final Value type = typeAttribute.getValue();
            if (type != null) {
                recordType = type.toString().trim().toUpperCase();
            }

            // record bytes required for fixed length records to check length
            if ("FIXED_LENGTH".equals(recordType)) { //$NON-NLS-1$
                AttributeStatement byteAttribute = getAttribute("RECORD_BYTES"); //$NON-NLS-1$
                if (byteAttribute != null) {
                    final Value bytesValue = byteAttribute.getValue();
                    if (bytesValue != null) {
                        recordBytes = StrUtils.getNumberLoose(
                                bytesValue.toString()).intValue();
                    }
                }
                // error and stop line eval if no value found
                if (recordBytes == null) {
                    // TODO: is MISSING_MEMBER the right error type? not
                    // technically a child property but is required to make
                    // sense of value
                    addProblem(typeAttribute.getLineNumber(),
                            "parser.error.missingRecordBytes", //$NON-NLS-1$
                            ProblemType.MISSING_MEMBER);
                }
            }
        }

        // only do special check if attached content found, surfaces as missing
        // pointer target if has internal pointer but no attached content
        if (hasAttachedContent() && recordBytes != null) {

            // get first internal pointer to check where attached content starts
            final PointerStatement internalPointer = getLowestInternalPointer(recordBytes);

            // if has internal pointer - get lowest value internal pointer
            if (internalPointer != null) {
                // resolve pointer to bytes if not already
                Numeric startPos = (Numeric) internalPointer.getValue();
                int startByte = getStartByte(startPos, recordBytes);
                // report if actual start byte and pointer claimed start byte
                // don't agree
                int foundStartByte = this.attachedStartByte.intValue();
                if (startByte > foundStartByte) {
                    addProblem(
                            internalPointer.getLineNumber(),
                            "parser.error.startByteMismatch", //$NON-NLS-1$
                            ProblemType.ATTACHED_START_BYTE_MISMATCH,
                            this.attachedStartByte, startByte);
                } else if (startByte < foundStartByte) {
                    // if found start is after listed, might be that data starts
                    // with whitespace
                    addProblem(
                            internalPointer.getLineNumber(),
                            "parser.warning.startBytePossibleMismatch", //$NON-NLS-1$
                            ProblemType.START_BYTE_POSSIBLE_MISMATCH,
                            this.attachedStartByte, startByte);
                }

                // recommended line length is only ignored for line padding
                if ("FIXED_LENGTH".equals(recordType) //$NON-NLS-1$
                        && (!this.hasBlankFill || linesSimilar())) {
                    checkGivenLineLengths(recordBytes);
                } else {
                    checkDefaultLineLengths();
                }
            } else {
                // no internal pointer, data should just follow assume stream
                // since fixed would require a start record and record bytes
                // TODO: this correct?
                checkDefaultLineLengths();
            }
        } else {
            checkDefaultLineLengths();
        }

    }

    // Necessary alternate test for having blank fill since data could start
    // with whitespace or lines could be variable even with no fill by
    // coincidence. Currently calling 50% same good enough.
    private boolean linesSimilar() {
        final Map<Integer, Integer> frequencies = new HashMap<Integer, Integer>();
        for (LineLength l : getLineLengths()) {
            Integer key = l.getLength();
            if (frequencies.containsKey(key)) {
                Integer found = frequencies.get(key);
                frequencies.put(key, found + 1);
            } else {
                frequencies.put(key, 1);
            }
        }
        Integer maxFound = 0;
        for (Iterator<Entry<Integer, Integer>> it = frequencies.entrySet()
                .iterator(); it.hasNext();) {
            Entry<Integer, Integer> entry = it.next();
            if (entry.getValue().intValue() > maxFound.intValue()) {
                maxFound = entry.getValue();
            }
        }
        final boolean similar = (maxFound.doubleValue() / getLineLengths()
                .size()) > .5;
        return similar;
    }

    // Verify that all line lengths <= 78 (excl CR LF).
    private void checkDefaultLineLengths() {
        for (LineLength l : getLineLengths()) {
            if (l.getLength() > 78) {
                addProblem(l.getLine(), "parser.error.lineTooLong", //$NON-NLS-1$
                        ProblemType.EXCESSIVE_LINE_LENGTH, l.getLength());
            }
        }
    }

    private void checkGivenLineLengths(int recordBytes) {
        for (LineLength l : getLineLengths()) {
            if ((l.getLength() + 2) != recordBytes) {
                addProblem(l.getLine(), "parser.error.wrongLineLength", //$NON-NLS-1$
                        ProblemType.WRONG_LINE_LENGTH, recordBytes - 2, l
                                .getLength());
            }
        }
    }

    private PointerStatement getLowestInternalPointer(final Integer recordBytes) {
        Long lowWaterMark = null;
        PointerStatement lowest = null;
        for (final PointerStatement pointer : this.getPointers()) {
            final Value value = pointer.getValue();
            if (value instanceof Numeric) {
                long start = getStartByte((Numeric) value, recordBytes);
                if (lowWaterMark == null || start < lowWaterMark.longValue()) {
                    lowWaterMark = start;
                    lowest = pointer;
                }
            }
        }
        return lowest;
    }

    // duplicated functionality since slightly different requirements in
    // behavior from getSkipBytes()
    private Integer getStartByte(final Numeric startPosition,
            final Integer recordBytes) {
        if (startPosition == null || startPosition.getValue().equals("0")) { //$NON-NLS-1$
            return null;
        }
        final String units = startPosition.getUnits();
        final String number = startPosition.getValue();
        Integer numericValue = StrUtils.getNumberLoose(number).intValue();
        if (numericValue < 0) {
            // TODO: error that needs to be positive?
            return null;
        }
        if ("bytes".equalsIgnoreCase(units) || "<bytes>".equalsIgnoreCase(units)) { //$NON-NLS-1$ //$NON-NLS-2$
            return numericValue;
        }
        if (recordBytes == null) {
            return null;
        }
        // start at beginning of record so subtract 1 from records, add 1 byte
        // back to indicate that it starts on that byte, not before it
        return (numericValue - 1) * recordBytes + 1;
    }

    public static long getSkipBytes(Label label, Numeric startPosition) {
        long startByte = 0;
        if (startPosition == null) {
            return 0;
        }
        final String units = startPosition.getUnits();
        final String number = startPosition.getValue();
        long numericValue = StrUtils.getNumberLoose(number).longValue();
        // modify it to be what you want to skip rather than first byte...
        // TODO: this the right way to handle?
        numericValue = Math.max(0, numericValue - 1);
        if ("bytes".equalsIgnoreCase(units) || "<bytes>".equalsIgnoreCase(units)) { //$NON-NLS-1$ //$NON-NLS-2$
            return numericValue;
        }

        // TODO: support non-fixed-length records, not sure how yet
        // label.getAttribute("RECORD_TYPE").getValue().toString().equalsIgnoreCase("FIXED_LENGTH");
        AttributeStatement attribute = label.getAttribute("RECORD_BYTES"); //$NON-NLS-1$
        if (attribute != null) {
            final String bytesString = attribute.getValue().toString();
            startByte = StrUtils.getNumberLoose(bytesString).longValue()
                    * numericValue;
            startByte = Math.max(0, startByte);
        }

        return startByte;
    }

    public void setHasEndStatement() {
        this.hasEndStatement = true;
    }

    public boolean hasEndStatement() {
        return this.hasEndStatement;
    }

    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return StrUtils.getNonNull(this.labelFile, this.labelURI) + " "
                + this.problems.size() + " problems and "
                + this.statements.size() + " statements";
    }

}
