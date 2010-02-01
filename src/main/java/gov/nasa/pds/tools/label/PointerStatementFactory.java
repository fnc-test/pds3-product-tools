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

package gov.nasa.pds.tools.label;

import gov.nasa.pds.tools.dict.DictIdentifier;

/**
 * This class hides the construction of pointers. It helps determine the exact
 * type of pointer and constructs it. If modifications need to be made to this
 * behavior it will be hidden here.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class PointerStatementFactory {

    public static PointerStatement newInstance(final Label sourceLabel,
            final int line, final DictIdentifier identifier, final Value value) {
        if (matchesIdentifier(identifier, SpecialPointer.DESCRIPTION_KEY)) {
            return new DescriptionPointer(sourceLabel, line, identifier, value);
        } else if (matchesIdentifier(identifier, SpecialPointer.STRUCTURE_KEY)) {
            return new StructurePointer(sourceLabel, line, identifier, value);
        } else if (matchesIdentifier(identifier, SpecialPointer.INDEX_KEY)) {
            return new IndexPointer(sourceLabel, line, identifier, value);
        } else if (matchesIdentifier(identifier, SpecialPointer.CATALOG_KEY)) {
            return new CatalogPointer(sourceLabel, line, identifier, value);
        }
        return new PointerStatement(sourceLabel, line, identifier, value);
    }

    @SuppressWarnings("nls")
    // comparing to strings since statements are always AttributeStatements at
    // this stage
    private static boolean matchesIdentifier(final DictIdentifier id,
            final String[] testIds) {
        for (final String testId : testIds) {
            final String idString = id.toString();
            if (idString.equals(testId) || idString.endsWith("_" + testId)) {
                return true;
            }
        }
        return false;
    }
}