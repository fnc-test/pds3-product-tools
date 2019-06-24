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