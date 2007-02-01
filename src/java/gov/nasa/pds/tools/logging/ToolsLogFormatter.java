//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.logging;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

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
