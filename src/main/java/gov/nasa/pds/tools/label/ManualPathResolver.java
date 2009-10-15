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

import gov.nasa.pds.tools.containers.FileReference;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * This is a utility class to resolve URLs for pointers.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class ManualPathResolver implements PointerResolver {

    private static Logger log = Logger.getLogger(ManualPathResolver.class);

    private List<URL> includePaths = new ArrayList<URL>();

    public void setIncludePaths(final List<URL> paths) {
        this.includePaths.addAll(paths);
    }

    @SuppressWarnings("nls")
    private URI resolveURI(final FileReference fileRef,
            final PointerStatement pointer) throws URISyntaxException {
        final String path = fileRef.getPath();
        for (Iterator<URL> i = this.includePaths.iterator(); i.hasNext();) {
            URL baseURL = i.next();
            String url = baseURL.toString();

            // add trailing slash if necessary - MUST be a directory
            if (!url.endsWith("/")) //$NON-NLS-1$
                url += "/"; //$NON-NLS-1$

            URL fileURL = null;
            // Check to see if this is the right URL for the file
            try {
                fileURL = new URL(url + path);
                fileURL.openStream();
                return new URI(fileURL.toString());
            } catch (IOException ioEx) {
                // Ignore this must not be the path to the pointed file
            }

            // Check to see if we can find the file as upper case
            try {
                fileURL = new URL(url + path.toUpperCase());
                fileURL.openStream();
                log.warn("In order to resolve the pointer the filename "
                        + "had to be forced to upper case. "
                        + pointer.getSourceURI() + " line "
                        + pointer.getLineNumber());
                return new URI(fileURL.toString());
            } catch (IOException ioEx) {
                // Ignore this must not be the path to the pointed file
            }

            // Check to see if we can find the file as lower case

            try {
                fileURL = new URL(url + path.toLowerCase());
                fileURL.openStream();
                log.warn("In order to resolve the pointer the filename "
                        + "had to be forced to lower case. "
                        + pointer.getSourceURI() + " line "
                        + pointer.getLineNumber());
                return new URI(fileURL.toString());
            } catch (IOException ioEx) {
                // Ignore this must not be the path to the pointed file
            }

        }
        log.error("Could not find referenced pointer " + path + " in pointer "
                + pointer.getSourceURI() + " on line "
                + pointer.getLineNumber());
        return null;
    }

    public List<URI> resolveURIs(PointerStatement pointer) throws IOException {

        List<URI> resolvedURIs = new ArrayList<URI>();

        List<FileReference> fileRefs = pointer.getFileRefs();

        for (final FileReference fileRef : fileRefs) {
            URI foundURI = null;
            try {
                foundURI = resolveURI(fileRef, pointer);
            } catch (URISyntaxException e) {
                // noop
            }
            if (foundURI != null) {
                resolvedURIs.add(foundURI);
            }
        }

        return resolvedURIs;
    }

    public Map<Numeric, URI> resolveURIMap(PointerStatement pointer)
            throws IOException {
        Map<Numeric, URI> resolvedURIs = new HashMap<Numeric, URI>();

        List<FileReference> fileRefs = pointer.getFileRefs();

        for (final FileReference fileRef : fileRefs) {
            URI foundURI = null;
            try {
                foundURI = resolveURI(fileRef, pointer);
            } catch (URISyntaxException e) {
                // noop
            }
            if (foundURI != null) {
                resolvedURIs.put(fileRef.getStartPosition(), foundURI);
            }
        }

        return resolvedURIs;
    }

    public File getBaseFile() {
        // TODO Auto-generated method stub
        return null;
    }

    public URI getBaseURI() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getBaseString() {
        // TODO Auto-generated method stub
        return null;
    }

    public Map<Numeric, File> resolveFileMap(PointerStatement pointer) {
        return new HashMap<Numeric, File>();
    }

    public List<File> resolveFiles(PointerStatement pointer) {
        return new ArrayList<File>();
    }
}
