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

import gov.nasa.pds.tools.constants.Constants;
import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.tools.label.PointerStatement;

import java.util.Iterator;
import java.util.List;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class CatalogNameValidator implements LabelValidator {

    public boolean validate(Label label) {
        boolean valid = true;

        if (!checkPointers(label))
            valid = false;

        if (!checkPointersInObjects(label))
            valid = false;

        return valid;
    }

    private boolean checkPointersInObjects(final Label label) {
        return checkPointersInObjects(label, null);
    }

    private boolean checkPointersInObjects(final Label label,
            final List<ObjectStatement> objs) {

        List<ObjectStatement> objects = objs;
        if (objects == null) {
            objects = label.getObjects();
        }
        boolean valid = true;

        for (Iterator<ObjectStatement> i = objects.iterator(); i.hasNext();) {
            ObjectStatement object = i.next();
            if (!checkPointers(label, object.getPointers())) {
                valid = false;
            }
            if (!checkPointersInObjects(label, object.getObjects())) {
                valid = false;
            }
        }

        return valid;
    }

    private boolean checkPointers(final Label label) {
        return checkPointers(label, null);
    }

    private boolean checkPointers(final Label label,
            List<PointerStatement> pntrs) {
        List<PointerStatement> pointers = pntrs;
        if (pointers == null) {
            pointers = label.getPointers();
        }

        boolean valid = true;

        for (Iterator<PointerStatement> i = pointers.iterator(); i.hasNext();) {
            PointerStatement statement = i.next();

            if (statement.getIdentifier().toString().endsWith(
                    "_" + Constants.CATALOG)) { //$NON-NLS-1$
                boolean found = false;
                for (int n = 0; !found && n < Constants.CATALOG_NAMES.length; n++) {
                    if (statement.getIdentifier().equals(
                            Constants.CATALOG_NAMES[n]))
                        found = true;
                }
                if (!found) {
                    label.addProblem(statement,
                            "parser.error.badCatalogPointerConvention", //$NON-NLS-1$
                            ProblemType.BAD_CATALOG_NAME, statement
                                    .getIdentifier());
                    valid = false;
                }
            }
        }

        return valid;
    }

}
