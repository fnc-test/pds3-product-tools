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

package gov.nasa.pds.tools.containers;

import gov.nasa.arc.pds.tools.util.StrUtils;
import gov.nasa.pds.tools.dict.Definition;
import gov.nasa.pds.tools.dict.DictIdentifier;
import gov.nasa.pds.tools.dict.Dictionary;

import java.io.Serializable;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class SimpleDictionaryChange implements Serializable {

    private static final long serialVersionUID = -3695138194805548830L;

    private final Integer lineNumber; // line number in file

    private final DictIdentifier id;

    private final String messageKey;

    private final Object[] arguments;

    private final transient Dictionary sourceDictionary;

    private final Definition definition;

    private final String sourceString;

    // file needed? all going to be to the same file...
    // send in type? or just have different messages
    public SimpleDictionaryChange(final Definition definition,
            final String messageKey, final Object... arguments) {
        this.definition = definition;
        this.sourceDictionary = definition.getSourceDictionary();
        this.sourceString = this.sourceDictionary.getSourceString();
        this.lineNumber = definition.getLineNumber();
        this.id = definition.getIdentifier();
        this.arguments = arguments;
        this.messageKey = messageKey;
    }

    public Integer getLineNumber() {
        return this.lineNumber;
    }

    public DictIdentifier getId() {
        return this.id;
    }

    public String getMessageKey() {
        return this.messageKey;
    }

    public Object[] getArguments() {
        return this.arguments;
    }

    public Dictionary getDictionary() {
        return this.sourceDictionary;
    }

    public String getSourceString() {
        return this.sourceString;
    }

    public Definition getDefinition() {
        return this.definition;
    }

    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return this.definition.getIdentifier().toString() + " "
                + this.messageKey + " " + StrUtils.toString(this.arguments);
    }
}
