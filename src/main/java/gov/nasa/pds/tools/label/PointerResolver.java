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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Defines an interface for defining how to resolve a pointer. This was
 * abstracted to an interface as there are different contexts which may
 * determine how to resolve a pointer. .
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public interface PointerResolver {

    public List<URI> resolveURIs(PointerStatement pointer) throws IOException;

    public Map<Numeric, URI> resolveURIMap(PointerStatement pointer)
            throws IOException;

    public List<File> resolveFiles(PointerStatement pointer);

    public Map<Numeric, File> resolveFileMap(PointerStatement pointer);

    public File getBaseFile();

    public URI getBaseURI();

    public String getBaseString();
}
