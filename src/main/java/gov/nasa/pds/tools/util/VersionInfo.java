package gov.nasa.pds.tools.util;

import java.io.IOException;
import java.util.Properties;

public final class VersionInfo {

    public final static String ODL_VERSION = "odl.version"; //$NON-NLS-1$

    public final static String LIBRARY_VERSION = "product-tools.version"; //$NON-NLS-1$

    public final static String STANDARDS_VERSION = "standards-ref.version"; //$NON-NLS-1$

    private final static Properties props = new Properties();

    static {
        try {
            props.load(VersionInfo.class
                    .getResourceAsStream("product-tools.properties")); //$NON-NLS-1$
        } catch (IOException e) {
            // re-throw as runtime so that it needn't be explicitly caught
            throw new RuntimeException(e);
        }
    }

    public static String getODLVersion() {
        return props.getProperty(ODL_VERSION);
    }

    public static String getLibraryVersion() {
        return props.getProperty(LIBRARY_VERSION);
    }

    public static String getStandardsRefVersion() {
        return props.getProperty(STANDARDS_VERSION);
    }

    public static String getPDSVersion() {
        return "PDS3"; //$NON-NLS-1$
    }

}