//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label.validate;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import gov.nasa.pds.tools.label.GroupStatement;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.tools.label.Statement;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.logging.ToolsLogRecord;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class DuplicateIdentifierValidator implements LabelValidator {
    private static Logger log = Logger.getLogger(DuplicateIdentifierValidator.class.getName());

    /* (non-Javadoc)
     * @see gov.nasa.pds.tools.label.validate.LabelValidator#isValid(gov.nasa.pds.tools.label.Label)
     */
    public boolean isValid(Label label) {
        return isValid(label, new DefaultValidationListener());
    }

    /* (non-Javadoc)
     * @see gov.nasa.pds.tools.label.validate.LabelValidator#isValid(gov.nasa.pds.tools.label.Label, gov.nasa.pds.tools.label.validate.ValidationListener)
     */
    public boolean isValid(Label label, ValidationListener listener) {
        return isValid(label.getStatements(), listener);
    }
    
    private boolean isValid(List statements, ValidationListener listener) {
        boolean valid = true;
        Set seenIdentifiers = new HashSet();
        
        for (Iterator i = statements.iterator(); i.hasNext();) {
            Statement statement = (Statement) i.next();
            if (statement instanceof AttributeStatement) {
                if (seenIdentifiers.contains(statement.getIdentifier())) {
                    valid = false;
                    listener.reportError("Duplicate " + statement.getIdentifier() + "found.");
                    log.log(new ToolsLogRecord(Level.SEVERE, "Duplicate " + statement.getIdentifier() + " found.", 
                            statement.getFilename(), statement.getContext(), statement.getLineNumber()));
                } else {
                    seenIdentifiers.add(statement.getIdentifier());
                }
            } else if (statement instanceof ObjectStatement) {
                ObjectStatement object = (ObjectStatement) statement;
                boolean pass = isValid(object.getStatements(), listener);
                if (!pass)
                    valid = pass;
            } else if (statement instanceof GroupStatement) {
                GroupStatement group = (GroupStatement) statement;
                boolean pass = isValid(group.getStatements(), listener);
                if (!pass)
                    valid = pass;
            }
        }
        
        return valid;
    }

}
