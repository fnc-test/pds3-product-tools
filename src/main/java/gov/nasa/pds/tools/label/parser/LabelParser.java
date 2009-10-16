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
