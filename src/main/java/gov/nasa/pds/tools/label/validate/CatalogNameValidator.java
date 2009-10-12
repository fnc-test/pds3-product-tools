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
