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

package gov.nasa.pds.tools.label.validate;

import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.GroupStatement;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.tools.label.Statement;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class DuplicateIdentifierValidator implements LabelValidator {

    /*
     * (non-Javadoc)
     * 
     * @see
     * gov.nasa.pds.tools.label.validate.LabelValidator#isValid(gov.nasa.pds
     * .tools.label.Label, gov.nasa.pds.tools.label.validate.ValidationListener)
     */
    public boolean validate(Label label) {
        return validate(label, label.getStatements());
    }

    private boolean validate(final Label label, List<Statement> statements) {
        boolean valid = true;

        Set<String> seenIdentifiers = new HashSet<String>();

        for (Iterator<Statement> i = statements.iterator(); i.hasNext();) {
            Statement statement = i.next();
            if (statement instanceof AttributeStatement) {
                if (seenIdentifiers.contains(statement.getSourceURI() + "#" //$NON-NLS-1$
                        + statement.getIdentifier())) {
                    valid = false;
                    label.addProblem(statement,
                            "parser.error.duplicateIdentifier", //$NON-NLS-1$
                            ProblemType.DUPLICATE_IDENTIFIER, statement
                                    .getIdentifier());
                } else {
                    seenIdentifiers.add(statement.getSourceURI() + "#" //$NON-NLS-1$
                            + statement.getIdentifier());
                }
            } else if (statement instanceof ObjectStatement) {
                ObjectStatement object = (ObjectStatement) statement;
                boolean pass = validate(label, object.getStatements());
                if (!pass) {
                    valid = pass;
                }
            } else if (statement instanceof GroupStatement) {
                GroupStatement group = (GroupStatement) statement;
                boolean pass = validate(label, group.getStatements());
                if (!pass) {
                    valid = pass;
                }
            }
        }

        return valid;
    }

}
