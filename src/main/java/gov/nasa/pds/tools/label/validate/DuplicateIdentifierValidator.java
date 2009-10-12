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
