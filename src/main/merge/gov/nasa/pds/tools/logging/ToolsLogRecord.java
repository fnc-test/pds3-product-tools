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

package gov.nasa.pds.tools.logging;

import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class ToolsLogRecord extends LogRecord {
    private String file;
    private String context;
    private int line;

    public ToolsLogRecord(Level level, String message) {
        this(level, message, null, null);
    }
    
    /**
     * Constructs a log record 
     * @param level of error
     * @param message describing error
     * @param file in which error occured
     */
    public ToolsLogRecord(Level level, String message, String file) {
        this(level, message, file, null);
    }
    
    /**
     * Construct a log record 
     * @param level of error
     * @param message describing error
     * @param file in which error occured
     * @param line number at which occured
     */
    public ToolsLogRecord(Level level, String message, String file, int line) {
        this(level, message, file, null, line);
    }
    
    /**
     * Construct a log record 
     * @param level of error
     * @param message describing error
     * @param file in which error occured
     * @param context file which referenced file where error occured
     */
    public ToolsLogRecord(Level level, String message, String file, String context) {
        this(level, message, file, context, -1);
    }
    
    /**
     * Construct a log record
     * @param level of error
     * @param message describing error
     * @param file in which error occured
     * @param context file which referenced file where error occured
     * @param line number at which occured
     */
    public ToolsLogRecord(Level level, String message, String file, String context, int line) {
        super(level, message);
        this.file = file;
        this.context = context;
        this.line = line;
    }

    public String getFile() {return file;}
    
    public String getContext() {return context;}
    
    public int getLine() {return line;}
 
}
