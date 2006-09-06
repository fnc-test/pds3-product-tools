//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gov.nasa.pds.tools.label.parser.LabelParser;
import gov.nasa.pds.tools.label.parser.LabelParserFactory;
import gov.nasa.pds.tools.label.parser.ParseException;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class StructurePointer extends PointerStatement {
    public static String IDENTIFIER = "STRUCTURE";
    private List statements;
    private boolean loaded = false;
    private URL labelURL;

    public StructurePointer(int lineNumber, String identifier, Value value, URL base) throws MalformedURLException {
        super(lineNumber, identifier, value);
        statements = new ArrayList();
        labelURL = null;
        if (base != null) {
	        String url = base.toString();
	        if (!url.endsWith("/"))
	        	url += "/";
	        labelURL = new URL(url + value.toString());
        }
    }
    
    public synchronized void loadReferencedStatements() throws ParseException, IOException {
        if (!loaded) {
            loaded = true;
            LabelParser parser = LabelParserFactory.getInstance().newLabelParser();
            Label label = parser.parsePartial(labelURL);
            statements = label.getStatements();
        }
    }
    
    public List getStatements() {
        return statements;
    }

}
