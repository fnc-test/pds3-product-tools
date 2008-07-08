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

package gov.nasa.pds.tools.logging;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class FullLogFormatter extends Formatter {
	private int numPassed;
	private int numFailed;
	private int numSkipped;
	private List records;
	private StringBuffer config;
	private StringBuffer parameters;
	private boolean headerPrinted;
	
	public FullLogFormatter() {
		numPassed = 0;
		numFailed = 0;
		numSkipped = 0;
		headerPrinted = false;
		records = new ArrayList();
		config = new StringBuffer();
		parameters = new StringBuffer("Parameter Settings:\n\r\n\r");
	}

	/* (non-Javadoc)
     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
     */
	public String format(LogRecord record) {
		ToolsLogRecord toolsRecord = (ToolsLogRecord) record;
		
		if (toolsRecord.getLevel() == ToolsLevel.CONFIG) {
			config.append("  " + toolsRecord.getMessage() + "\n\r\n\r");
		} else if (toolsRecord.getLevel() == ToolsLevel.PARAMETER) {
			parameters.append("  " + toolsRecord.getMessage() + "\n\r\n\r");
		} else if (toolsRecord.getLevel() == ToolsLevel.NOTIFICATION) {
			return processRecords(toolsRecord);
		} else {
			records.add(toolsRecord);
		}
		
		return "";
	}
	
	private String processRecords(ToolsLogRecord record) {
		StringBuffer report = new StringBuffer("");
		
		if (!headerPrinted) {
			headerPrinted = true;
			report.append("PDS Validation Tool Report\n\r\n\r");
			report.append(config);
			report.append("\n\r");
			report.append(parameters);
			report.append("\n\r");
			report.append("Validation Details:\n\r\n\r");
		}
		
		if (record.getMessage().equals("PASS"))
			numPassed++;
		else if (record.getMessage().equals("FAIL"))
			numFailed++;
		else
			numSkipped++;
		
		report.append("  " + record.getMessage() + ": " + record.getFile() + "\n\r\n\r");
		
		for (Iterator i = records.iterator(); i.hasNext();) {
			ToolsLogRecord tlr = (ToolsLogRecord) i.next();
			if (tlr.getFile() != null && (record.getFile().equals(tlr.getFile()) || record.getFile().equals(tlr.getContext()))) {
				if (tlr.getContext() != null && tlr.getMessage().equals("Parsing label fragment."))
					report.append("    Begin Fragment: " + tlr.getFile() + "\n\r\n\r");
				
				if (tlr.getLevel() != ToolsLevel.SEVERE)
					report.append("      " + tlr.getLevel().getName() + "  " + tlr.getMessage() + "\n\r\n\r");
				else
					report.append("      ERROR  " + tlr.getMessage() + "\n\r\n\r");
				
				if (tlr.getContext() != null && tlr.getMessage().equals("Finished parsing label fragment."))
					report.append("    End Fragment: " + tlr.getFile() + "\n\r\n\r");
			}
		}
		
		records = new ArrayList();
		return report.toString();
	}
	
	public String getTail(Handler handler) {
		StringBuffer report = new StringBuffer("\n\r\n\rSummary:\n\r\n\r");
		int totalFiles = numPassed + numFailed + numSkipped;
		int totalValidated = numPassed + numFailed;
		report.append("  " + totalValidated + " of " + totalFiles + " validated, " + numSkipped + " skipped\n\r\n\r");
		report.append("  " + numPassed + " of " + totalValidated + " passed\n\r\n\r");
		report.append("End of Report\n\r");
		return report.toString();
	}

}
