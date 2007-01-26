//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a pointer statement that references an external file.
 * @author pramirez
 * @version $Revision$
 * 
 */
public class ExternalPointer extends PointerStatement {
    
    /**
     * Constructs a pointer statment that references an external file.
     * @param lineNumber of statement
     * @param identifier of statement
     * @param value of the assignment
     */
    public ExternalPointer(int pointerType, int lineNumber, String identifier, Value value) {
        super(pointerType, lineNumber, identifier, value);
    }
    
    /**
     * This method resolves the URL to the file pointed to by looking in the given include paths. Will throw
     * an error if it can not resolve an URL. Otherwise the URL is returned.
     * @param includePaths are the directories in which to look for the file
     * @return URL to the pointed to file
     * @throws IOException Thrown if pointed to file can not be resolved
     */
    public URL resolveURL(List includePaths) throws IOException {
        URL resolvedURL = null;
        String filename = "";
        
        //The name of the file will be the first element in the sequence or the value if not 
        //contained in a sequence
        if (value instanceof Sequence)
            filename = ((Sequence) value).get(0).toString();
        else
            filename = value.toString();
        
        //Go through the list of directories and see if pointed to file can be resolved
        for (Iterator i = includePaths.iterator(); i.hasNext() && resolvedURL == null;) {
            URL baseURL = (URL) i.next();
            String url = baseURL.toString();
            if (!url.endsWith("/"))
                url += "/";
            URL fileURL = new URL(url + filename);
            //Check to see if this is the right URL for the file
            try {
                fileURL.openStream();
                resolvedURL = fileURL;
            } catch (IOException ioEx) {
                //Ignore this must not be the path to the pointed file
            }
        }
        
        if (resolvedURL == null)
            throw new IOException("Could not find referenced pointer " + filename);
        
        return resolvedURL;
    }
}
