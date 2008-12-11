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

import gov.nasa.pds.tools.logging.ToolsLogRecord;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is a utility class to resolve URLs for pointers.
 *  
 * @author pramirez
 * @version $Revision$
 *
 */
public class URLResolver {
	private static Logger log = Logger.getLogger(URLResolver.class.getName());
	
	public static URL resolveURL(List includePaths, PointerStatement pointer, String externalFilename) throws IOException {
		URL resolvedURL = null;
		String filename = externalFilename;
        
        //Test and warn if filename is mixed case.
        if (!filename.equals(filename.toLowerCase()) && !filename.equals(filename.toUpperCase()))
            log.log(new ToolsLogRecord(Level.WARNING, "A pointer reference should not contain mixed case.", 
                    pointer.getFilename(), pointer.getContext(), pointer.getLineNumber()));
        
        //Go through the list of directories and see if pointed to file can be resolved
        for (Iterator i = includePaths.iterator(); i.hasNext() && resolvedURL == null;) {
            URL baseURL = (URL) i.next();
            String url = baseURL.toString();
            if (!url.endsWith("/"))
                url += "/";
            URL fileURL = null;
            //Check to see if this is the right URL for the file
            try {
            	fileURL = new URL(url + filename);
                fileURL.openStream();
                resolvedURL = fileURL;
            } catch (IOException ioEx) {
                //Ignore this must not be the path to the pointed file
            }
            
            //Check to see if we can find the file as upper case
            if (resolvedURL == null) {
                try {
                    fileURL = new URL(url + filename.toUpperCase());
                    fileURL.openStream();
                    log.log(new ToolsLogRecord(Level.WARNING, "In order to resolve the pointer the filename " +
                            "had to be forced to upper case.", pointer.getFilename(), pointer.getContext(), pointer.getLineNumber()));
                    resolvedURL = fileURL;
                } catch (IOException ioEx) {
                    //Ignore this must not be the path to the pointed file
                }
            }

            //Check to see if we can find the file as lower case
            if (resolvedURL == null) {
                try {
                    fileURL = new URL(url + filename.toLowerCase());
                    fileURL.openStream();
                    log.log(new ToolsLogRecord(Level.WARNING, "In order to resolve the pointer the filename " +
                            "had to be forced lower case.", pointer.getFilename(), pointer.getContext(), pointer.getLineNumber()));
                    resolvedURL = fileURL;
                } catch (IOException ioEx) {
                    //Ignore this must not be the path to the pointed file
                }
            }
        }
        
        if (resolvedURL == null)
            throw new IOException("Could not find referenced pointer " + filename);
        
        return resolvedURL;
	}
}
