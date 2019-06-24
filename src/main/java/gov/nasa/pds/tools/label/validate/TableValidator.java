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
      } else if (statement instanceof ObjectStatement
          && statement.getIdentifier().getId().equals("CONTAINER")) {
        ObjectStatement o = (ObjectStatement) statement;
        unprocessed.addAll(o.getStatements());
      }
    }
    return results;
  }
}
