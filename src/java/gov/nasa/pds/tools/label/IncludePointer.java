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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gov.nasa.pds.tools.label.parser.LabelParser;
import gov.nasa.pds.tools.label.parser.LabelParserFactory;
import gov.nasa.pds.tools.label.parser.ParseException;
import gov.nasa.pds.tools.label.validate.Status;

/**
 * This class represents a pointer that is a set of external statements that can and should
 * be included in label containing this statement when performing validation.
 * @author pramirez
 * @version $Revision$
 * 
 */
public class IncludePointer extends PointerStatement implements PointerType, Status {
    protected List statements;
    protected boolean loaded = false;
    protected String loadStatus = null;
    protected int numErrors;
    protected int numWarnings;

    /**
     * Constructs a pointer that can be resolved to a set of statements.
     * @param lineNumber of statement
     * @param identifier of statement
     * @param value assigned to statement
     */
    public IncludePointer(int lineNumber, String identifier, Value value) {
        super(INCLUDE, lineNumber, identifier, value);
        statements = new ArrayList();
        numWarnings = 0;
        numErrors = 0;
        loadStatus = UNKNOWN;
		this.externalReference = true;
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
    
    /**
     * Retrieves the list of statements pointed to by this structure pointer
     * @return external list of statements
     */
    public List getStatements() {
        return statements;
    }
    
    /**
     * Indicates whether or not the statements pointed to have been loaded.
     * @return flag indicating load status
     */
    public boolean isLoaded() {
        return loaded;
    }
    
    public String getLoadStatus() {
        return loadStatus;
    }
    
    public int getNumErrors() {return numErrors;}
    public int getNumWarnings() {return numWarnings;}

}
