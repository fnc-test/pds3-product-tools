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

package gov.nasa.pds.tools.label.parser;

import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.label.Label;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public interface LabelParser {

    /**
     * Parses the given file
     * 
     * @param url
     *            File to
     * @return {@link Label} representation of the file
     * @throws LabelParserException
     *             - If any syntactic errors are encountered. All errors will be
     *             written to the Logger.
     */
    public Label parseLabel(final URL url, final boolean forceParse)
            throws LabelParserException, IOException;

    public Label parseLabel(final File file, final boolean forceParse)
            throws LabelParserException, IOException;

    /**
     * Parses the given partial label.
     * 
     * @param url
     * @return {@link Label} representation of the file
     * @throws LabelParserException
     * @throws IOException
     */
    public Label parsePartial(final URL url, final Label parent)
            throws LabelParserException, IOException;

    public Label parsePartial(final File file, final Label parent)
            throws LabelParserException, IOException;
}
