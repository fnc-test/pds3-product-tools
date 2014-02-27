// Copyright 2006-2014, by the California Institute of Technology.
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
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.tools.label.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * The TableValidator class performs validation on TABLE objects in a label.
 * 
 * @author mcayanan
 * 
 */
public class TableValidator implements LabelValidator {

  public boolean validate(Label label) {
    boolean valid = true;
    List<ObjectStatement> objects = label.getObjects();
    for (ObjectStatement object : objects) {
      if (object.getIdentifier().getId().endsWith("TABLE")) {
        // Determine if at least one COLUMN exists
        List<ObjectStatement> columns = getStatementsRecursively(object,
            "COLUMN");
        if (columns.isEmpty()) {
          valid = false;
          label.addProblem(object, "parser.error.missingColumn",
              ProblemType.MISSING_MEMBER, object.getIdentifier());
        }
      }
    }
    return valid;
  }

  /**
   * Recursively searches down a given PDS object to find sub-objects that match
   * a user supplied list of keywords.
   * 
   * @param object
   *          An ObjectStatement.
   * @param identifier
   *          The statement to find.
   * 
   * @return A list containing the sub-object that were found in the given
   *         object that match the given identifier.
   */
  private List<ObjectStatement> getStatementsRecursively(
      ObjectStatement object, String identifier) {
    List<ObjectStatement> results = new ArrayList<ObjectStatement>();
    List<Statement> unprocessed = new ArrayList<Statement>(
        object.getStatements());
    while (unprocessed.size() > 0) {
      Statement statement = unprocessed.remove(0);
      // Check that the statement belongs to the parent label.
      if (statement instanceof ObjectStatement
          && identifier.equals((statement.getIdentifier().getId()))) {
        results.add((ObjectStatement) statement);
      } else if (statement instanceof ObjectStatement) {
        ObjectStatement o = (ObjectStatement) statement;
        unprocessed.addAll(o.getStatements());
      }
    }
    return results;
  }
}
