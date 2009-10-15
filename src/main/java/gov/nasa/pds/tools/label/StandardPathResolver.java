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

import gov.nasa.arc.pds.tools.util.FileUtils;
import gov.nasa.arc.pds.tools.util.URLUtils;
import gov.nasa.pds.tools.containers.FileReference;
import gov.nasa.pds.tools.containers.VolumeContainerSimple;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class StandardPathResolver implements PointerResolver {

    private VolumeContainerSimple volume;

    public void setVolumeContext(VolumeContainerSimple volume) {
        this.volume = volume;
    }

    private File getBaseForPointer(final PointerStatement pointer) {
        if (pointer instanceof CatalogPointer) {
            return this.volume.getCatalogFolder();
        } else if (pointer instanceof StructurePointer) {
            return this.volume.getLabelFolder();
        } else if (pointer instanceof DescriptionPointer) {
            return this.volume.getDocumentFolder();
        } else if (pointer instanceof IndexPointer) {
            return this.volume.getIndexFolder();
        }
        return null;
    }

    private URI getBaseURIForPointer(final PointerStatement pointer) {
        if (pointer instanceof CatalogPointer) {
            return this.volume.getCatalogURI();
        } else if (pointer instanceof StructurePointer) {
            return this.volume.getLabelURI();
        } else if (pointer instanceof DescriptionPointer) {
            return this.volume.getDocumentURI();
        } else if (pointer instanceof IndexPointer) {
            return this.volume.getIndexURI();
        }
        return null;
    }

    public Map<Numeric, URI> resolveURIMap(PointerStatement pointer) {
        HashMap<Numeric, URI> files = new HashMap<Numeric, URI>();
        URL sourceDirectory = URLUtils.getParentURL(pointer.getSourceURI());
        // get refs from pointer
        List<FileReference> fileRefs = pointer.getFileRefs();
        URI altDirectory = null;
        if (this.volume != null && pointer instanceof SpecialPointer) {
            altDirectory = getBaseURIForPointer(pointer);
        }
        for (FileReference fileRef : fileRefs) {
            try {
                final String definedPath = fileRef.getPath();
                URL testFile = URLUtils.newURL(sourceDirectory, definedPath);
                // file not found, try alternate case
                if (!URLUtils.exists(testFile)) {
                    URL testFile2 = URLUtils.getAlternateCaseURL(testFile);
                    if (URLUtils.exists(testFile2)) {
                        // found but we want to complain about case of file?
                        testFile = testFile2;
                    } else if (altDirectory != null) {
                        testFile = URLUtils.newURL(altDirectory.toURL(),
                                definedPath);
                        // file not found, try alternate case
                        if (!URLUtils.exists(testFile)) {
                            URL testFile3 = URLUtils
                                    .getAlternateCaseURL(testFile);
                            if (URLUtils.exists(testFile3)) {
                                // found but we want to complain about case of
                                // file?
                                testFile = testFile3;
                            }
                        }

                    }
                }
                final Numeric startPosition = fileRef.getStartPosition();
                URI testURI = testFile.toURI();
                files.put(startPosition, testURI);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }

        return files;
    }

    public Map<Numeric, File> resolveFileMap(PointerStatement pointer) {
        HashMap<Numeric, File> files = new HashMap<Numeric, File>();
        File sourceDirectory = FileUtils.getBaseFile(pointer.getSourceFile());
        // get refs from pointer
        List<FileReference> fileRefs = pointer.getFileRefs();
        File altDirectory = null;
        if (this.volume != null && pointer instanceof SpecialPointer) {
            altDirectory = getBaseForPointer(pointer);
        }
        for (FileReference fileRef : fileRefs) {
            final String definedPath = fileRef.getPath();
            File testFile = new File(sourceDirectory, definedPath);
            // file not found, try alternate case
            if (!FileUtils.exists(testFile)) {
                File testFile2 = FileUtils.getAlternateCaseFile(
                        sourceDirectory, definedPath);
                if (FileUtils.exists(testFile2)) {
                    // found but we want to complain about case of file?
                    testFile = testFile2;
                }
            }
            // file doesn't exist with found case, try alternate
            if (!testFile.exists() && altDirectory != null) {
                testFile = new File(altDirectory, definedPath);
                // file not found, try alternate case
                if (!FileUtils.exists(testFile)) {
                    File testFile3 = FileUtils.getAlternateCaseFile(
                            altDirectory, definedPath);
                    if (FileUtils.exists(testFile3)) {
                        // found but we want to complain about case of file?
                        testFile = testFile3;
                    }
                }
            }
            final Numeric startPosition = fileRef.getStartPosition();
            files.put(startPosition, testFile);
        }

        return files;
    }

    public File getBaseFile() {
        if (this.volume == null) {
            return null;
        }
        return this.volume.getBaseFolder();
    }

    public URI getBaseURI() {
        if (this.volume == null) {
            return null;
        }
        return this.volume.getBaseURI();
    }

    public String getBaseString() {
        if (this.volume == null) {
            return null;
        }
        if (this.getBaseFile() != null) {
            return this.getBaseFile().toString();
        }
        return this.getBaseURI().toString();
    }

    public List<File> resolveFiles(PointerStatement pointer) {
        final Map<Numeric, File> fileMap = resolveFileMap(pointer);
        Iterator<Entry<Numeric, File>> it = fileMap.entrySet().iterator();
        List<File> fileList = new ArrayList<File>();
        while (it.hasNext()) {
            Entry<Numeric, File> entry = it.next();
            final File file = entry.getValue();
            fileList.add(file);
        }
        return fileList;
    }

    public List<URI> resolveURIs(PointerStatement pointer) {
        return new ArrayList<URI>();
    }

}
