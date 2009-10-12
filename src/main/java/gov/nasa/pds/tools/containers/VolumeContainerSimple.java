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

package gov.nasa.pds.tools.containers;

import gov.nasa.arc.pds.tools.util.FileUtils;
import gov.nasa.arc.pds.tools.util.URLUtils;
import gov.nasa.pds.tools.constants.Constants;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class VolumeContainerSimple implements BaseContainerInterface {

    final File baseFolder;

    final File documentFolder; // ^DESCRIPTION or ^TEXT includes

    final File labelFolder; // ^STRUCTURE includes

    final File softwareFolder;

    final File calibFolder;

    final File geometryFolder;

    final File indexFolder; // ^INDEX_TABLE includes

    final File dataFolder;

    final File extrasFolder;

    final File gazetteerFolder;

    /*
     * Note that cat pointers may end in _CATALOG. When this is the case, the
     * pointer name and pointed to file name should follow the conventions in
     * 19.3.2.1
     */
    final File catalogFolder; // ^CATALOG or ^DATA_SET_MAP_PROJECTION includes

    final URI baseURI;

    final URI documentURI; // ^DESCRIPTION or ^TEXT includes

    final URI labelURI; // ^STRUCTURE includes

    final URI softwareURI;

    final URI calibURI;

    final URI geometryURI;

    final URI indexURI; // ^INDEX_TABLE includes

    final URI dataURI;

    final URI extrasURI;

    final URI gazetteerURI;

    final URI catalogURI;

    public VolumeContainerSimple(final File baseFolder) {
        this.baseFolder = baseFolder;
        this.documentFolder = getFile(Constants.DOCUMENT_FOLDER_NAME);
        this.labelFolder = getFile(Constants.LABEL_FOLDER_NAME);
        this.softwareFolder = getFile(Constants.SOFTWARE_FOLDER_NAME);
        this.calibFolder = getFile(Constants.CALIB_FOLDER_NAME);
        this.geometryFolder = getFile(Constants.GEOMETRY_FOLDER_NAME);
        this.indexFolder = getFile(Constants.INDEX_FOLDER_NAME);
        this.dataFolder = getFile(Constants.DATA_FOLDER_NAME);
        this.extrasFolder = getFile(Constants.EXTRAS_FOLDER_NAME);
        this.gazetteerFolder = getFile(Constants.GAZETTEER_FOLDER_NAME);
        this.catalogFolder = getFile(Constants.CATALOG_FOLDER_NAME);
        this.baseURI = null;
        this.documentURI = null;
        this.labelURI = null;
        this.softwareURI = null;
        this.calibURI = null;
        this.geometryURI = null;
        this.indexURI = null;
        this.dataURI = null;
        this.extrasURI = null;
        this.gazetteerURI = null;
        this.catalogURI = null;
    }

    public VolumeContainerSimple(final URI baseURI) {
        this.baseFolder = null;
        this.documentFolder = null;
        this.labelFolder = null;
        this.softwareFolder = null;
        this.calibFolder = null;
        this.geometryFolder = null;
        this.indexFolder = null;
        this.dataFolder = null;
        this.extrasFolder = null;
        this.gazetteerFolder = null;
        this.catalogFolder = null;
        this.baseURI = baseURI;
        this.documentURI = getURI(Constants.DOCUMENT_FOLDER_NAME);
        this.labelURI = getURI(Constants.LABEL_FOLDER_NAME);
        this.softwareURI = getURI(Constants.SOFTWARE_FOLDER_NAME);
        this.calibURI = getURI(Constants.CALIB_FOLDER_NAME);
        this.geometryURI = getURI(Constants.GEOMETRY_FOLDER_NAME);
        this.indexURI = getURI(Constants.INDEX_FOLDER_NAME);
        this.dataURI = getURI(Constants.DATA_FOLDER_NAME);
        this.extrasURI = getURI(Constants.EXTRAS_FOLDER_NAME);
        this.gazetteerURI = getURI(Constants.GAZETTEER_FOLDER_NAME);
        this.catalogURI = getURI(Constants.CATALOG_FOLDER_NAME);
    }

    private File getFile(final String path) {
        return FileUtils.getCaseUnknownFile(this.baseFolder, path);
    }

    private URI getURI(final String path) {
        try {
            return URLUtils.getCaseUnknownURL(this.baseURI, path).toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public File getBaseFolder() {
        return this.baseFolder;
    }

    public URI getBaseURI() {
        return this.baseURI;
    }

    public File getDocumentFolder() {
        return this.documentFolder;
    }

    public URI getDocumentURI() {
        return this.documentURI;
    }

    public URI getLabelURI() {
        return this.labelURI;
    }

    public URI getSoftwareURI() {
        return this.softwareURI;
    }

    public URI getCalibURI() {
        return this.calibURI;
    }

    public URI getGeometryURI() {
        return this.geometryURI;
    }

    public URI getIndexURI() {
        return this.indexURI;
    }

    public URI getDataURI() {
        return this.dataURI;
    }

    public URI getExtrasURI() {
        return this.extrasURI;
    }

    public URI getGazetteerURI() {
        return this.gazetteerURI;
    }

    public URI getCatalogURI() {
        return this.catalogURI;
    }

    public File getLabelFolder() {
        return this.labelFolder;
    }

    public File getSoftwareFolder() {
        return this.softwareFolder;
    }

    public File getCalibFolder() {
        return this.calibFolder;
    }

    public File getGeometryFolder() {
        return this.geometryFolder;
    }

    public File getIndexFolder() {
        return this.indexFolder;
    }

    public File getDataFolder() {
        return this.dataFolder;
    }

    public File getExtrasFolder() {
        return this.extrasFolder;
    }

    public File getGazetteerFolder() {
        return this.gazetteerFolder;
    }

    public File getCatalogFolder() {
        return this.catalogFolder;
    }
}
