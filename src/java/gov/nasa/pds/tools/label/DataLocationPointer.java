// Copyright 2006-2007, by the California Institute of Technology.
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

/**
 * This class represents a data location pointer which typically points to the 
 * data that the label is describing. This pointer can point to something internal
 * or external to the file.
 * 
 * @author pramirez
 * @version $Revision$
 * 
 */
public class DataLocationPointer extends PointerStatement {

	protected DataLocationPointer(int lineNumber, String identifier, Value value) {
		super(DATA_LOCATION, lineNumber, identifier, value);
		if (value instanceof TextString || value instanceof Sequence)
			this.externalReference = true;
	}

}