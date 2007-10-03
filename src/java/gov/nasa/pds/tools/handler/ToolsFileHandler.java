// Copyright 2006-2007, by the California Institute of Technology.
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

package gov.nasa.pds.tools.handler;

import gov.nasa.pds.tools.logging.ToolsLogFormatter;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;

/**
 * Class to setup a file handler for the tools logging capability.
 * 
 * @author mcayanan
 *
 */
public class ToolsFileHandler extends FileHandler {
	
	/**
	 * Constructor. Automatically sets the log level to 'ALL'.
	 * 
	 * @param file A file name to store the logging messages. If the file 
	 * exists, it will overwrite the existing contents.
	 * 
	 * @throws SecurityException
	 * @throws IOException
	 */
	public ToolsFileHandler(String file) throws SecurityException, IOException {
		this(file, false);
	}
	
	/**
	 * Constructor. Automatically sets the log level to 'ALL'.
	 * 
	 * @param file A file name to store the logging messages.
	 * @param append A flag to tell the handler to append to the file or
	 * to overwrite the existing contents.
	 * 
	 * @throws SecurityException
	 * @throws IOException
	 */
	public ToolsFileHandler(String file, boolean append) throws SecurityException, IOException {
		super(file, append);
		setLevel(Level.ALL);
		setFormatter(new ToolsLogFormatter());
	}
}
