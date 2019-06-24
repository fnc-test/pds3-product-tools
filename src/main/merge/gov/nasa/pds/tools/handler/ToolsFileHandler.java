// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// * Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// * Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// * Neither the name of Caltech nor its operating division, the Jet Propulsion
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

package gov.nasa.pds.tools.handler;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;

/**
 * Class to setup a file handler for the tools logging capability.
 * 
 * @author mcayanan
 *
 */
public class ToolsFileHandler extends FileHandler {
	
	/**
	 * Constructor that does not append to a file and automatically
	 * sets the log level to 'ALL'.
	 * 
	 * @param file A file name to store the logging messages. If the file 
	 * exists, it will overwrite the existing contents.
	 * @param formatter Formatter to be used to format the log messages.
	 * 
	 * @throws SecurityException
	 * @throws IOException
	 */
	public ToolsFileHandler(String file, Formatter formatter) throws SecurityException, IOException {
		this(file, false, Level.ALL, formatter);
	}
	
	/**
	 * Constructor that does not append to a file.
	 * 
	 * @param file A file name to store the logging messages.
	 * @param level Sets the logging level.
	 * @param formatter Formatter to be used to format the log messages.
	 * 
	 * @throws SecurityException
	 * @throws IOException
	 */
	public ToolsFileHandler(String file, Level level, Formatter formatter) throws SecurityException, IOException {
		this(file, false, level, formatter);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param file A file name to store the logging messages.
	 * @param append A flag to tell the handler to append to the file or
	 * to overwrite the existing contents.
	 * @param level Sets the logging level.
	 * @param formatter Formatter to be used to format the log messages.
	 * 
	 * @throws SecurityException
	 * @throws IOException
	 */
	public ToolsFileHandler(String file, boolean append, Level level, Formatter formatter) throws SecurityException, IOException {
		super(file, append);
		setLevel(level);
		setFormatter(formatter);
	}

}
