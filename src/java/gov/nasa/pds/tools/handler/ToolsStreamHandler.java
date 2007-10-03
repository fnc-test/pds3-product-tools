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

import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.StreamHandler;

/**
 * This class sets up a stream handler for the tools logging capability.
 * 
 * @author mcayanan
 *
 */
public class ToolsStreamHandler extends StreamHandler {
	
	/**
	 * Constructor. Automatically sets the log level to 'ALL'.
	 * 
	 * @param out An output stream.
	 */
	public ToolsStreamHandler(OutputStream out) {
		this(out, Level.ALL);
	}
	
	/**
	 * Constructor.
	 * @param out An output stream.
	 * @param level Sets the log level, specifying which message levels will
	 * be logged by this handler.
	 */
	public ToolsStreamHandler(OutputStream out, Level level) {
		super(out, new ToolsLogFormatter());
		setLevel(level);
	}
}
