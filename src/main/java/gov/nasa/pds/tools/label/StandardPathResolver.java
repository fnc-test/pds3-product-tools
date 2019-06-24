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
