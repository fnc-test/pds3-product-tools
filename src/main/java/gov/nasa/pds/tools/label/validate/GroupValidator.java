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

import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.dict.DictIdentifier;
import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.dict.GroupDefinition;
import gov.nasa.pds.tools.dict.type.UnsupportedTypeException;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.GroupStatement;
import gov.nasa.pds.tools.label.Label;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class GroupValidator {

  public static boolean validate(final GroupStatement group,
      final Dictionary dictionary, final Label label)
      throws DefinitionNotFoundException, UnsupportedTypeException {
    boolean valid = true;

    // Lookup group definition, can't do anything without it
    GroupDefinition definition = dictionary.findGroupClassDefinition(group
        .getIdentifier());
    if (definition == null) {
      label.addProblem(new DefinitionNotFoundException(group));
    } else {

      // First check that required elements are captured in object
      for (Iterator<DictIdentifier> i = definition.getRequiredElements()
          .iterator(); i.hasNext();) {
        DictIdentifier required = i.next();
        if (!group.hasAttribute(required.toString())) {
          valid = false;
          label.addProblem(group, "parser.error.missingRequiredElement", //$NON-NLS-1$
              ProblemType.MISSING_PROPERTY, group.getIdentifier(), required);
        }
      }

      // Check to make sure all attributes are allowed within this
      // definition
      // If the definition contains the element PSDD then anything is
      // allowed and this check can be skipped
      if (!definition.allowsAnyElement()) {
        for (Iterator<AttributeStatement> i = group.getAttributes().iterator(); i
            .hasNext();) {
          AttributeStatement attribute = i.next();
          if (!definition.isAllowed(attribute.getIdentifier())) {
            valid = false;
            label.addProblem(attribute,
                "parser.error.invalidElement", //$NON-NLS-1$
                ProblemType.INVALID_MEMBER, group.getIdentifier(),
                attribute.getIdentifier());
          }
        }
      }
    }

    // Validate all attributes
    List<AttributeStatement> attributes = group.getAttributes();
    Collections.sort(attributes);
    for (Iterator<AttributeStatement> i = group.getAttributes().iterator(); i
        .hasNext();) {
      AttributeStatement attribute = i.next();
      try {
        if (!ElementValidator.validate(attribute, label, dictionary)) {
          valid = false;
        }
      } catch (LabelParserException lpe) {
        label.addProblem(lpe);
        valid = false;
      }
    }

    return valid;
  }
}
