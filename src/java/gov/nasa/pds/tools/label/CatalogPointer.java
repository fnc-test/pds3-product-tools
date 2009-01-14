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

import gov.nasa.pds.tools.label.parser.LabelParser;
import gov.nasa.pds.tools.label.parser.LabelParserFactory;
import gov.nasa.pds.tools.label.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a pointer to a catalog file. This is different than normal
 * pointers in that it can reference several files.
 * 
 * @author pramirez
 * @version $Revision$
 * 
 */
public class CatalogPointer extends IncludePointer {

	public CatalogPointer(int lineNumber, String identifier, Value value) {
		super(lineNumber, identifier, value);
	}

	/**
     * This method attempts to load the referenced statements. If unsuccessful will throw an error.
     * Once loaded the statements are held in the class so they may be accessed at a later time.
     * @param includePaths An list of {@link URL} in which to search for the referenced file
     * @throws ParseException thrown if the file can not be properly loaded
     * @throws IOException thrown if file can not be accessed
     */
    public synchronized void loadReferencedStatements(List includePaths) throws ParseException, IOException {
        if (!loaded) {
        	for (Iterator n = getExternalFileReferences().iterator(); n.hasNext();) {
        		String filename = (String) n.next();
	            URL labelURL = URLResolver.resolveURL(includePaths, this, filename);
	            loaded = true;
	            LabelParser parser = LabelParserFactory.getInstance().newLabelParser();
	            for (Iterator i = includePaths.iterator(); i.hasNext();)
	                parser.addIncludePath((URL) i.next());
	            String labelContext = context;
	            if (labelContext == null)
	                labelContext = filename;
	            Label label = parser.parsePartial(labelContext, labelURL);
	            loadStatus = label.getStatus();
	            statements = label.getStatements();
	            numErrors = label.getNumErrors();
	            numWarnings = label.getNumWarnings();
        	}
        }
    }
}
