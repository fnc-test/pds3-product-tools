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
        this.sourceString = definition.getSourceString();
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
