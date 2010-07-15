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

package gov.nasa.pds.tools.dict.parser;

import static gov.nasa.pds.tools.dict.DictionaryTokens.ALIAS_LIST;
import static gov.nasa.pds.tools.dict.DictionaryTokens.DATA_TYPE;
import static gov.nasa.pds.tools.dict.DictionaryTokens.DESCRIPTION;
import static gov.nasa.pds.tools.dict.DictionaryTokens.ELEMENT_ALIASES;
import static gov.nasa.pds.tools.dict.DictionaryTokens.ELEMENT_DEFINITION;
import static gov.nasa.pds.tools.dict.DictionaryTokens.GENERIC_OBJECT;
import static gov.nasa.pds.tools.dict.DictionaryTokens.MAXIMUM;
import static gov.nasa.pds.tools.dict.DictionaryTokens.MAX_LENGTH;
import static gov.nasa.pds.tools.dict.DictionaryTokens.MINIMUM;
import static gov.nasa.pds.tools.dict.DictionaryTokens.MIN_LENGTH;
import static gov.nasa.pds.tools.dict.DictionaryTokens.NAME;
import static gov.nasa.pds.tools.dict.DictionaryTokens.OBJECT_ALIASES;
import static gov.nasa.pds.tools.dict.DictionaryTokens.OBJECT_TYPE;
import static gov.nasa.pds.tools.dict.DictionaryTokens.OPTIONAL_ELEMENTS;
import static gov.nasa.pds.tools.dict.DictionaryTokens.OPTIONAL_OBJECTS;
import static gov.nasa.pds.tools.dict.DictionaryTokens.REQUIRED_ELEMENTS;
import static gov.nasa.pds.tools.dict.DictionaryTokens.REQUIRED_OBJECTS;
import static gov.nasa.pds.tools.dict.DictionaryTokens.SPECIFIC_OBJECT;
import static gov.nasa.pds.tools.dict.DictionaryTokens.STATUS_TYPE;
import static gov.nasa.pds.tools.dict.DictionaryTokens.UNIT_LIST;
import static gov.nasa.pds.tools.dict.DictionaryTokens.UNIT_SEQUENCE;
import static gov.nasa.pds.tools.dict.DictionaryTokens.VALUES;
import static gov.nasa.pds.tools.dict.DictionaryTokens.VALUE_TYPE;
import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.dict.Alias;
import gov.nasa.pds.tools.dict.Definition;
import gov.nasa.pds.tools.dict.DictIdentifier;
import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.dict.DictionaryTokens;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.CommentStatement;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.tools.label.Sequence;
import gov.nasa.pds.tools.label.Statement;
import gov.nasa.pds.tools.label.Value;
import gov.nasa.pds.tools.label.antlr.ODLLexer;
import gov.nasa.pds.tools.label.antlr.ODLParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * This class provides the means to parse a PDS compliant data dictionary. The
 * {@link Dictionary} created can be used for validation purposes or just to
 * examine the contents programmatically. To parse a dictionary use the
 * following:
 * <p>
 * <code>
 * Dictionary dictionary = DictionaryParser.parse(new URL("<url to dictionary>"));
 * </code>
 * <p>
 * If you wanted to turn of aliases the alternative parse method could be used:
 * <p>
 * <code>
 * Dictionary dictionary = DictionaryParser.parse(new URL("<url to dictionary>"), false);
 * </code>
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class DictionaryParser {

    private static Logger log = Logger.getLogger(DictionaryParser.class
            .getName());

    protected final static DictIdentifier OBJECT_TYPE_ID = DictIDFactory
            .createElementDefId(OBJECT_TYPE);

    protected final static DictIdentifier DESCRIPTION_ID = DictIDFactory
            .createElementDefId(DESCRIPTION);

    protected final static DictIdentifier STATUS_TYPE_ID = DictIDFactory
            .createElementDefId(STATUS_TYPE);

    protected final static DictIdentifier REQUIRED_OBJECTS_ID = DictIDFactory
            .createElementDefId(REQUIRED_OBJECTS);

    protected final static DictIdentifier OPTIONAL_OBJECTS_ID = DictIDFactory
            .createElementDefId(OPTIONAL_OBJECTS);

    protected final static DictIdentifier REQUIRED_ELEMENTS_ID = DictIDFactory
            .createElementDefId(REQUIRED_ELEMENTS);

    protected final static DictIdentifier OPTIONAL_ELEMENTS_ID = DictIDFactory
            .createElementDefId(OPTIONAL_ELEMENTS);

    protected final static DictIdentifier NAME_ID = DictIDFactory
            .createElementDefId(NAME);

    protected final static DictIdentifier DATA_TYPE_ID = DictIDFactory
            .createElementDefId(DATA_TYPE);

    protected final static DictIdentifier UNITS_ID = DictIDFactory
            .createElementDefId(DictionaryTokens.UNITS);

    protected final static DictIdentifier MIN_LENGTH_ID = DictIDFactory
            .createElementDefId(MIN_LENGTH);

    protected final static DictIdentifier MAX_LENGTH_ID = DictIDFactory
            .createElementDefId(MAX_LENGTH);

    protected final static DictIdentifier VALUES_ID = DictIDFactory
            .createElementDefId(VALUES);

    protected final static DictIdentifier VALUE_TYPE_ID = DictIDFactory
            .createElementDefId(VALUE_TYPE);

    protected final static DictIdentifier MINIMUM_ID = DictIDFactory
            .createElementDefId(MINIMUM);

    protected final static DictIdentifier MAXIMUM_ID = DictIDFactory
            .createElementDefId(MAXIMUM);

    protected final static DictIdentifier UNIT_SEQUENCE_ID = DictIDFactory
            .createElementDefId(UNIT_SEQUENCE);

    protected final static DictIdentifier OBJECT_ALIASES_ID = DictIDFactory
            .createElementDefId(OBJECT_ALIASES);

    protected final static DictIdentifier ELEMENT_ALIASES_ID = DictIDFactory
            .createElementDefId(ELEMENT_ALIASES);

    protected final static DictIdentifier ALIAS_LIST_ID = DictIDFactory
            .createObjectDefId(ALIAS_LIST);

    protected final static DictIdentifier UNIT_LIST_ID = DictIDFactory
            .createObjectDefId(UNIT_LIST);

    protected final static DictIdentifier GENERIC_OBJECT_ID = DictIDFactory
            .createObjectDefId(GENERIC_OBJECT);

    protected final static DictIdentifier SPECIFIC_OBJECT_ID = DictIDFactory
            .createObjectDefId(SPECIFIC_OBJECT);

    protected final static DictIdentifier ELEMENT_DEFINITION_ID = DictIDFactory
            .createObjectDefId(ELEMENT_DEFINITION);

    /**
     * Parses a {@link URL} that is compliant with the PDS Data Dictionary
     * document and formulates a {@link Dictionary} with aliases turned off.
     * 
     * @param url
     *            points to the location of the dictionary
     * @return a data dictionary with element, group, and object definitions
     * @throws LabelParserException
     *             thrown when dictionary can not be parsed correctly
     * @throws IOException
     *             thrown when dictionary can not be accessed
     */
    public static Dictionary parse(final URL url) throws LabelParserException,
            IOException {
        return parse(url, false, false);
    }

    public static Dictionary parse(final URL url, final boolean aliasing)
            throws LabelParserException, IOException {
        return parse(url, aliasing, false);
    }

    public static Dictionary parse(final URL url, final boolean aliasing,
            final boolean storeProblems) throws LabelParserException,
            IOException {
        URI uri;
        try {
            uri = url.toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
        Dictionary dictionary = new Dictionary(uri);
        InputStream is = url.openStream();
        return parse(is, dictionary, aliasing, storeProblems);
    }

    public static Dictionary parse(final File file)
            throws LabelParserException, IOException {
        return parse(file, false);
    }

    public static Dictionary parse(final File file, final boolean aliasing)
            throws LabelParserException, IOException {
        return parse(file, aliasing, false);
    }

    public static Dictionary parse(final File file, final boolean aliasing,
            final boolean storeProblems) throws LabelParserException,
            IOException {
        Dictionary dictionary = new Dictionary(file);
        InputStream is = new FileInputStream(file);
        return parse(is, dictionary, aliasing, storeProblems);
    }

    /**
     * Parses a {@link URL} that is compliant with the PDS Data Dictionary
     * document and formulates a {@link Dictionary} with a flag to indicated
     * whether aliases should be read in.
     * 
     * @param input
     *            input stream to dictionary
     * @param aliasing
     *            indicates if aliases should be read in
     * @return a data dictionary with element, group, and object definitions
     * @throws LabelParserException
     *             thrown when dictionary can not be parsed correctly
     * @throws IOException
     *             thrown when dictionary can not be accessed
     */
    public static Dictionary parse(final InputStream input,
            final Dictionary dictionary, boolean aliasing, boolean storeProblems)
            throws LabelParserException, IOException {

        CharStream antlrInput = new ANTLRInputStream(input);
        ODLLexer lexer = new ODLLexer(antlrInput);
        lexer.setStopAtEND(false); // We're parsing a dictionary.
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ODLParser parser = new ODLParser(tokens);

        log.info("Parsing dictionary " + dictionary.getSourceString()); //$NON-NLS-1$

        try {
            List<Label> labels = parser.dictionary();

            if (labels.size() > 0) {
                Label headerLabel = labels.get(0);

                StringBuffer information = new StringBuffer();
                // TODO: Sort list of statements that way comments get appended
                // in proper order
                for (Statement statement : headerLabel.getStatements()) {
                    if (statement instanceof CommentStatement)
                        information.append(((CommentStatement) statement)
                                .getText()
                                + "\n"); //$NON-NLS-1$
                }

                dictionary.setInformation(information.toString());

                // Now process the objects in the rest of the file.
                // These objects can be an alias list, units list, or
                // definition (element, object, or group)
                List<Definition> definitions = new ArrayList<Definition>();
                Map<DictIdentifier, List<Alias>> aliases = new HashMap<DictIdentifier, List<Alias>>();
                Map<String, String> units = new HashMap<String, String>();

                // Go through statements in the label and start formulating the
                // different parts of
                // the dictionary. Aliases, units, and definitions will be
                // pulled
                // out.
                for (Label label : labels) {
                    // pass through problems if flag is set
                    if (storeProblems) {
                        dictionary.addProblems(label.getProblems());
                    }

                    for (Statement statement : label.getStatements()) {
                        if (statement instanceof ObjectStatement) {
                            if (ALIAS_LIST_ID.equals(statement.getIdentifier())) {
                                if (aliasing)
                                    aliases = generateAliases((ObjectStatement) statement);
                            } else if (UNIT_LIST_ID.equals(statement
                                    .getIdentifier())) {
                                units = generateUnits((ObjectStatement) statement);
                            } else {
                                try {
                                    definitions
                                            .add(DefinitionFactory
                                                    .createDefinition(
                                                            dictionary,
                                                            (ObjectStatement) statement));
                                } catch (UnknownDefinitionException e) {
                                    if (storeProblems) {
                                        dictionary.addProblem(e);
                                    }
                                }
                            }
                        }
                    }
                }

                // Put units in the dictionary
                dictionary.setUnits(units);

                // If aliasing is turned on then they need to added to the
                // definitions
                if (aliasing) {
                    // Go through the aliases and add to appropriate definitions
                    for (Definition d : definitions) {
                        final DictIdentifier identifier = d.getIdentifier();
                        if (aliases.containsKey(identifier)) {
                            d.addAliases(aliases.get(identifier));
                        }
                    }
                }

                // Add all definitions to the dictionary
                dictionary.addDefinitions(definitions);
            }
            // TODO: Update to catch thrown exception not all exceptions
        } catch (RecognitionException ex) {
            log.error(ex.getMessage());
            throw new LabelParserException(ex, ex.line, ex.charPositionInLine,
                    ProblemType.PARSE_ERROR);
        }

        log.info("Finshed parsing dictionary " + dictionary.getSourceString()); //$NON-NLS-1$

        return dictionary;
    }

    private static Map<DictIdentifier, List<Alias>> generateAliases(
            ObjectStatement object) {
        Map<DictIdentifier, List<Alias>> aliases = new HashMap<DictIdentifier, List<Alias>>();

        // Process object aliases
        // They take the form (alias, identifier)
        AttributeStatement objectAliases = object
                .getAttribute(OBJECT_ALIASES_ID);
        if (objectAliases != null) {
            for (Iterator<Value> i = ((Sequence) objectAliases.getValue())
                    .iterator(); i.hasNext();) {
                Sequence values = (Sequence) i.next();
                if (values.size() == 2) {
                    Alias alias = new Alias(values.get(0).toString());
                    DictIdentifier identifier = DictIDFactory
                            .createObjectDefId(values.get(1).toString());
                    List<Alias> as = aliases.get(identifier);
                    if (as == null) {
                        as = new ArrayList<Alias>();
                    }
                    as.add(alias);
                    aliases.put(identifier, as);
                }
            }
        }

        // Process element aliases
        // They take the form (alias, object, identifier)
        AttributeStatement elementAliases = object
                .getAttribute(ELEMENT_ALIASES_ID);
        if (elementAliases != null) {
            for (Iterator<Value> i = ((Sequence) elementAliases.getValue())
                    .iterator(); i.hasNext();) {
                Sequence values = (Sequence) i.next();
                if (values.size() == 3) {
                    Alias alias = new Alias(values.get(1).toString(), values
                            .get(0).toString());
                    DictIdentifier identifier = DictIDFactory
                            .createElementDefId(values.get(2).toString());
                    List<Alias> as = aliases.get(identifier);
                    if (as == null) {
                        as = new ArrayList<Alias>();
                    }
                    as.add(alias);
                    aliases.put(identifier, as);
                }
            }
        }

        return aliases;
    }

    private static Map<String, String> generateUnits(ObjectStatement object) {
        Map<String, String> units = new HashMap<String, String>();
        // Process unit lists
        // They are just are just a list valid units and their long name (also
        // a valid way to reference a unit)
        // (('A','ampere'), ('A/m', 'ampere/meter') ....)
        AttributeStatement unitSequence = object.getAttribute(UNIT_SEQUENCE_ID);
        if (unitSequence != null) {
            for (Iterator<Value> i = ((Sequence) unitSequence.getValue())
                    .iterator(); i.hasNext();) {
                String unit = null;
                String name = null;
                Sequence value = (Sequence) i.next();
                try {
                    unit = value.get(0).toString();
                } catch (IndexOutOfBoundsException e) {
                    //
                }
                try {
                    name = value.get(1).toString();
                } catch (IndexOutOfBoundsException e) {
                    //
                }
                units.put(unit, name);
            }
        }
        return units;
    }

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure(new ConsoleAppender(new PatternLayout(
                "%-5p %m%n"))); //$NON-NLS-1$
        DictionaryParser.parse(new URL(args[0]));
    }
}
