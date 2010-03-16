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

import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.containers.FileReference;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
    private static Logger log = Logger.getLogger(ManualPathResolver.class
            .getName());

    private List<URL> includePaths = new ArrayList<URL>();
    private URI baseURI = null;

    public void setIncludePaths(final List<URL> paths) {
        this.includePaths.addAll(paths);
    }

    @SuppressWarnings("nls")
    private URI resolveURI(final FileReference fileRef,
            final PointerStatement pointer) throws URISyntaxException {
        final String path = fileRef.getPath();
        List<URL> searchPaths = new ArrayList<URL>(this.includePaths);

        // Add the base URI into the areas to search and default it to the
        // first place to look
        try {
            if (this.baseURI != null) {
                searchPaths.add(0, this.baseURI.toURL());
            }
        } catch (MalformedURLException e) {
            log.error("Problem using base URI " + this.baseURI);
        }

        // Add in the base URI of the label this pointer resides in to the
        // list of include paths if it is not equal to the base URI for this
        // resolver
        URI pointerLabelBaseURI = null;
        try {
            pointerLabelBaseURI = ManualPathResolver.getBaseURI(pointer.label
                    .getLabelURI());
            if (!pointerLabelBaseURI.equals(this.baseURI)) {
                searchPaths.add(0, pointerLabelBaseURI.toURL());
            }

        } catch (MalformedURLException e) {
            log.error("Problem using pointer's label URI "
                    + pointerLabelBaseURI);
        }

        for (Iterator<URL> i = searchPaths.iterator(); i.hasNext();) {
            URL baseURL = i.next();
            String url = baseURL.toString();

            // add trailing slash if necessary - MUST be a directory
            if (!url.endsWith("/")) //$NON-NLS-1$
                url += "/"; //$NON-NLS-1$

            URL fileURL = null;
            // Check to see if this is the right URL for the file.
            // Depending on the OS and/or protocol this may never get called as
            // the underlying OS may be case insensitive
            try {
                fileURL = new URL(url + path);
                fileURL.openStream();
                return new URI(fileURL.toString());
            } catch (IOException ioEx) {
                // Ignore this must not be the path to the pointed file
            }

            // Check to see if we can find the file as upper case.
            try {
                fileURL = new URL(url + path.toUpperCase());
                fileURL.openStream();
                // Found the file by upper casing the name so report it
                pointer.label.addProblem(new LabelParserException(pointer,
                        null, "parser.error.mismatchedPointerReference",
                        ProblemType.POTENTIAL_POINTER_PROBLEM));
                return new URI(fileURL.toString());
            } catch (IOException ioEx) {
                // Ignore this must not be the path to the pointed file
            }

            // Check to see if we can find the file as lower case
            try {
                fileURL = new URL(url + path.toLowerCase());
                fileURL.openStream();
                // Found the file by lower casing the name so report it
                pointer.label.addProblem(new LabelParserException(pointer,
                        null, "parser.error.mismatchedPointerReference",
                        ProblemType.POTENTIAL_POINTER_PROBLEM));
                return new URI(fileURL.toString());
            } catch (IOException ioEx) {
                // Ignore this must not be the path to the pointed file
            }

        }

        // The file just can not be found so now report it
        pointer.label.addProblem(new LabelParserException(pointer, null,
                "parser.error.missingRefFile", ProblemType.MISSING_RESOURCE,
                path));
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
        if (this.baseURI == null) {
            return null;
        }
        return new File(this.baseURI);
    }

    public URI getBaseURI() {
        return this.baseURI;
    }

    public String getBaseString() {
        if (this.baseURI == null) {
            return null;
        }
        return this.baseURI.getPath();
    }

    public void setBaseURI(URI baseURI) {
        this.baseURI = baseURI;
    }

    public static URI getBaseURI(URI uri) {
        if (uri == null) {
            return null;
        }
        File uriFilePath = new File(uri.getPath());
        return uriFilePath.getParentFile().toURI();
    }

    public Map<Numeric, File> resolveFileMap(PointerStatement pointer) {
        return new HashMap<Numeric, File>();
    }

    public List<File> resolveFiles(PointerStatement pointer) {
        return new ArrayList<File>();
    }
}
