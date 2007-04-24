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
// $Id$ 
//

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
            buffer.append("   <level>" + record.getLevel() + "</level>\n");
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
