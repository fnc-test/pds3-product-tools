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

package gov.nasa.pds.tools.label.validate;

import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.dict.parser.DictionaryParser;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.GroupStatement;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.tools.label.Statement;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class Validator {
    private Map<String, Boolean> properties = new HashMap<String, Boolean>();

    public static final String LINE_LEN_CHECK = "gov.nasa.pds.tools.label.validate.LineCheck"; //$NON-NLS-1$
    public static final String DUP_ID_CHECK = "gov.nasa.pds.tools.label.validate.DuplicateIdentifierValidator"; //$NON-NLS-1$
    public static final String CAT_NAME_CHECK = "gov.nasa.pds.tools.label.validate.CatalogNameValidator"; //$NON-NLS-1$
    public static final String FILE_CHAR_CHECK = "gov.nasa.pds.tools.label.validate.FileCharacteristicValidator"; //$NON-NLS-1$
    public static final String DICTIONARY_CHECK = "gov.nasa.pds.tools.label.validate.DictionaryValidator"; //$NON-NLS-1$

    private static Dictionary DEFAULT_DICTIONARY;

    public Validator() {
        this.properties.put(LINE_LEN_CHECK, true);
        this.properties.put(DICTIONARY_CHECK, true);
    }

    // take a label instance and add problems to it
    // Accumulate different types of tests here

    // TODO: determine what other tests to add
    // encompassed by higher level things in the

    protected static synchronized void initDefaultDictionary() {
        if (DEFAULT_DICTIONARY == null) {
            InputStream dictionaryStream = Validator.class.getClassLoader()
                    .getResourceAsStream("pdsdd.full"); //$NON-NLS-1$
            try {
                try {
                    DEFAULT_DICTIONARY = DictionaryParser.parse(
                            dictionaryStream, new Dictionary(Validator.class
                                    .getClassLoader().getResource("pdsdd.full") //$NON-NLS-1$
                                    .toURI()), false, false);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            } catch (LabelParserException e) {
                // TODO: consider what to do with parse issues, note that won't
                // be thrown in figure, just added to container
                e.printStackTrace();
            } catch (IOException e) {
                // shouldn't happen
                e.printStackTrace();
            }
        }
    }

    public void validate(final Label label) {
        if (performsDictionaryCheck()) {
            initDefaultDictionary();
        }
        validate(label, DEFAULT_DICTIONARY);
    }

    public void validate(final Label label, final Dictionary dictionary) {
        if (label == null) {
            // TODO: report an error?
            return;
        }

        if (performsLineLengthCheck()) {
            label.checkLineLengths();
        }

        if (performsDictionaryCheck()) {
            List<Statement> statements = label.getStatements();
            Collections.sort(statements);
            for (Iterator<Statement> i = statements.iterator(); i.hasNext();) {
                Statement statement = i.next();
                if (statement instanceof AttributeStatement) {
                    try {
                        ElementValidator.validate(
                                (AttributeStatement) statement, label,
                                dictionary);
                    } catch (LabelParserException lpe) {
                        label.addProblem(lpe);
                    }
                } else if (statement instanceof ObjectStatement) {
                    try {
                        ObjectValidator.validate((ObjectStatement) statement,
                                dictionary, label);
                    } catch (LabelParserException lpe) {
                        label.addProblem(lpe);
                    }
                } else if (statement instanceof GroupStatement) {
                    try {
                        GroupValidator.validate((GroupStatement) statement,
                                dictionary, label);
                    } catch (LabelParserException lpe) {
                        label.addProblem(lpe);
                    }
                }
            }
        }

        if (performsCatalogNameCheck()) {
            LabelValidator cnv = new CatalogNameValidator();
            cnv.validate(label);
        }

        if (performsDuplicateIdCheck()) {
            LabelValidator div = new DuplicateIdentifierValidator();
            div.validate(label);
        }

        if (performsDuplicateIdCheck()) {
            // TODO: Integrate back file characteristic check if needed
        }
    }

    public boolean getProperty(String property) {
        return this.properties.containsKey(property) ? this.properties
                .get(property) : false;
    }

    public void setProperty(String property, Boolean flag) {
        this.properties.put(property, flag);
    }

    public void setProperties(Map<String, Boolean> properties) {
        this.properties = properties;
    }

    public void setLineLengthCheck(Boolean flag) {
        setProperty(LINE_LEN_CHECK, flag);
    }

    public boolean performsLineLengthCheck() {
        return getProperty(LINE_LEN_CHECK);
    }

    public void setDictionaryCheck(Boolean flag) {
        setProperty(DICTIONARY_CHECK, flag);
    }

    public boolean performsDictionaryCheck() {
        return getProperty(DICTIONARY_CHECK);
    }

    public void setCatalogNameCheck(Boolean flag) {
        setProperty(CAT_NAME_CHECK, flag);
    }

    public boolean performsCatalogNameCheck() {
        return getProperty(CAT_NAME_CHECK);
    }

    public void setDuplicateIdCheck(Boolean flag) {
        setProperty(DUP_ID_CHECK, flag);
    }

    public boolean performsDuplicateIdCheck() {
        return getProperty(DUP_ID_CHECK);
    }

    public void setFileCharacteristicCheck(Boolean flag) {
        setProperty(FILE_CHAR_CHECK, flag);
    }

    public boolean performsCharacteristicCheck() {
        return getProperty(FILE_CHAR_CHECK);
    }

}
