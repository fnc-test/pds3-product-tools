//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

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
    private String root;
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
     * @param root file which referenced file where error occured
     */
    public ToolsLogRecord(Level level, String message, String file, String root) {
        this(level, message, file, root, -1);
    }
    
    /**
     * Construct a log record
     * @param level of error
     * @param message describing error
     * @param file in which error occured
     * @param root file which referenced file where error occured
     * @param line number at which occured
     */
    public ToolsLogRecord(Level level, String message, String file, String root, int line) {
        super(level, message);
        this.file = file;
        this.root = root;
        this.line = line;
    }

    public String getFile() {return file;}
    
    public String getRoot() {return root;}
    
    public int getLine() {return line;}
 
}
