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

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Level;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class ToolsLogFormatter extends Formatter {

    /* (non-Javadoc)
     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
     */
    public String format(LogRecord record) {
        StringBuffer buffer = new StringBuffer();
        ToolsLogRecord toolsRecord = (ToolsLogRecord) record;
        buffer.append(" <record>\n");
        if (record.getLevel() == Level.SEVERE)
            buffer.append("  <level>ERROR</level>\n");
        else
            buffer.append("  <level>" + record.getLevel() + "</level>\n");
        if (toolsRecord.getFile() != null)
            buffer.append("  <file>" + toolsRecord.getFile() + "</file>\n");
        if (toolsRecord.getLine() != -1)
            buffer.append("  <line>" + toolsRecord.getLine() + "</line>\n");
        if (toolsRecord.getContext() != null)
            buffer.append("  <context>" + toolsRecord.getContext() + "</context>\n");
        buffer.append("  <message><![CDATA[" + record.getMessage() + "]]></message>\n");
        buffer.append(" </record>\n");
        return buffer.toString();
    }
    
    public String getHead(Handler handler) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<?xml version=\"1.0\" ?>\n");
        buffer.append("<log>\n");
        return buffer.toString();
    }

    public String getTail(Handler handler) {
        return "</log>";
    }
}
