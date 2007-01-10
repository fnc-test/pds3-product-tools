//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import gov.nasa.pds.tools.label.parser.LabelParser;
import gov.nasa.pds.tools.label.parser.LabelParserFactory;
import gov.nasa.pds.tools.label.parser.ParseException;

/**
 * This class represents a pointer that is a set of external statements that can and should
 * be included in label containing this statement when performing validation.
 * @author pramirez
 * @version $Revision$
 * 
 */
public class StructurePointer extends ExternalPointer {
    public static String IDENTIFIER = "STRUCTURE";
    private List statements;
    private boolean loaded = false;

    /**
     * Constructs a pointer that can be resolved to a set of statements.
     * @param lineNumber of statement
     * @param identifier of statment
     * @param value assigned to statment
     */
    public StructurePointer(int lineNumber, String identifier, Value value) {
        super(lineNumber, identifier, value);
        statements = new ArrayList();
    }
    
    /**
     * This method attempts to load the referenced statements. If unsuccessful will throw an error.
     * Once loaded the statements are held in the class so they may be accessed at a later time.
     * @param includePaths An list of {@link URL} in which to search for the referenced file
     * @throws ParseException thrown if the file can not be properly loaded
     * @throws IOException thrown if file can not be accessed
     */
    public synchronized void loadReferencedStatements(List includePaths) throws ParseException, IOException {
        URL labelURL = resolveURL(includePaths);
        if (!loaded) {
            loaded = true;
            LabelParser parser = LabelParserFactory.getInstance().newLabelParser();
            Label label = parser.parsePartial(labelURL);
            statements = label.getStatements();
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
     * Indicates whether or not the statments pointed to have been loaded.
     * @return flag indicating load status
     */
    public boolean isLoaded() {
        return loaded;
    }

}
