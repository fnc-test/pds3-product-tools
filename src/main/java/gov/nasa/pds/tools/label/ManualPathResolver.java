// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// • Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// • Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// • Neither the name of Caltech nor its operating division, the Jet Propulsion
// Laboratory, nor the names of its contributors may be used to endorse or
// promote products derived from this software without specific prior written
// permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

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
      log.error("Problem using pointer's label URI " + pointerLabelBaseURI);
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
        fileURL.openStream().close();
        return new URI(fileURL.toString());
      } catch (IOException ioEx) {
        // Ignore this must not be the path to the pointed file
      }

      // Check to see if we can find the file as upper case.
      try {
        fileURL = new URL(url + path.toUpperCase());
        fileURL.openStream().close();
        // Found the file by upper casing the name so report it
        pointer.label.addProblem(new LabelParserException(pointer, null,
            "parser.error.mismatchedPointerReference",
            ProblemType.POTENTIAL_POINTER_PROBLEM));
        return new URI(fileURL.toString());
      } catch (IOException ioEx) {
        // Ignore this must not be the path to the pointed file
      }

      // Check to see if we can find the file as lower case
      try {
        fileURL = new URL(url + path.toLowerCase());
        fileURL.openStream().close();
        // Found the file by lower casing the name so report it
        pointer.label.addProblem(new LabelParserException(pointer, null,
            "parser.error.mismatchedPointerReference",
            ProblemType.POTENTIAL_POINTER_PROBLEM));
        return new URI(fileURL.toString());
      } catch (IOException ioEx) {
        // Ignore this must not be the path to the pointed file
      }

    }

    // The file just can not be found so now report it
    pointer.label.addProblem(new LabelParserException(pointer, null,
        "parser.error.missingRefFile", ProblemType.MISSING_RESOURCE, path));
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
    if (uri.getPath().endsWith("/")) {
      return uri.resolve("..");
    } else {
      return uri.resolve(".");
    }
  }

  public Map<Numeric, File> resolveFileMap(PointerStatement pointer) {
    return new HashMap<Numeric, File>();
  }

  public List<File> resolveFiles(PointerStatement pointer) {
    return new ArrayList<File>();
  }
}
