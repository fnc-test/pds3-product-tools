package gov.nasa.pds.tools.util;

import java.io.IOException;
import java.util.Properties;

public final class VersionInfo {
  public final static String ODL_VERSION = "odl.version";

  public final static String LIBRARY_VERSION = "product-tools.version";

  public final static String STANDARDS_VERSION = "standards-ref.version";

  private final static Properties props = new Properties();

  static {
    try {
      props.load(VersionInfo.class
          .getResourceAsStream("product-tools.properties"));
    } catch (IOException e) {
      // TODO: Should we do something other than consume error?
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

}