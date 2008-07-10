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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class SummaryLogFormatter extends Formatter {
	private int numPassed;
	private int numFailed;
	private int numSkipped;
	private StringBuffer config;
	private StringBuffer parameters;
	private boolean headerPrinted;
	private Map errorMessageRecords;
	private Map warningMessageRecords;
	private Map infoMessageRecords;
	private static String padding = "      ";
	private static String lineFeed = System.getProperty("line.separator", "\n");
	private static String doubleLineFeed = System.getProperty("line.separator", "\n") + System.getProperty("line.separator", "\n");
	
	public SummaryLogFormatter() {
		numPassed = 0;
		numFailed = 0;
		numSkipped = 0;
		headerPrinted = false;
		errorMessageRecords = new HashMap();
		warningMessageRecords = new HashMap();
		infoMessageRecords = new HashMap();
		config = new StringBuffer();
		parameters = new StringBuffer("Parameter Settings:" + doubleLineFeed);
	}

	/* (non-Javadoc)
     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
     */
	public String format(LogRecord record) {
		ToolsLogRecord toolsRecord = (ToolsLogRecord) record;
		if (toolsRecord.getLevel() == ToolsLevel.CONFIGURATION) {
			config.append("  " + toolsRecord.getMessage() + lineFeed);
		} else if (toolsRecord.getLevel() == ToolsLevel.PARAMETER) {
			parameters.append("  " + toolsRecord.getMessage() + lineFeed);
		} else if (toolsRecord.getLevel() == ToolsLevel.NOTIFICATION) {
			if (record.getMessage().equals("PASS"))
				numPassed++;
			else if (record.getMessage().equals("FAIL"))
				numFailed++;
			else
				numSkipped++;
			
			if (!headerPrinted) {
				StringBuffer report = new StringBuffer();
				report.append("PDS Validation Tool Report" + doubleLineFeed);
				report.append(config);
				report.append(lineFeed);
				report.append(parameters);
				report.append(lineFeed);
				headerPrinted = true;
				return report.toString();
			}
		} else {
			processRecord(toolsRecord);
		}
		
		return "";
	}
	
	private void processRecord(ToolsLogRecord record) {
		Map messageRecords = null;
		
		if (record.getLevel() == ToolsLevel.SEVERE)
			messageRecords = errorMessageRecords;
		else if (record.getLevel() == ToolsLevel.WARNING)
			messageRecords = warningMessageRecords;
		else if (record.getLevel() == ToolsLevel.INFO)
			messageRecords = infoMessageRecords;
		
		if (messageRecords != null) {
			MessageRecord mr = (MessageRecord) messageRecords.get(record.getMessage());
			if (mr == null) {
				mr = new MessageRecord();
				messageRecords.put(record.getMessage(), mr);
				mr.setMessage(record.getMessage());
			    mr.setFile(record.getFile());
				mr.setLine(record.getLine());
			}
			
			mr.seen();
		}
	}
	
	public String getTail(Handler handler) {
		StringBuffer report = new StringBuffer();
		int totalFiles = numPassed + numFailed + numSkipped;
		int totalValidated = numPassed + numFailed;
		
		report.append(doubleLineFeed + "Errors Found:" + doubleLineFeed);
		for (Iterator i = errorMessageRecords.values().iterator(); i.hasNext();) {
			MessageRecord mr = (MessageRecord) i.next();
			report.append("  ERROR  " + mr.getMessage() + lineFeed);
			if (mr.hasFile()) {
				report.append("  Example: ");
				if (mr.hasLine()) {
					report.append("line " + mr.getLine() + " of ");
				}
				report.append(mr.getFile() + lineFeed);
			}
			report.append("  " + mr.getTimesSeen() + " occurence(s)" + doubleLineFeed);
		}
		
		report.append(doubleLineFeed + "Warnings Found:" + doubleLineFeed);
		for (Iterator i = warningMessageRecords.values().iterator(); i.hasNext();) {
			MessageRecord mr = (MessageRecord) i.next();
			report.append("  WARN  " + mr.getMessage() + lineFeed);
			if (mr.hasFile()) {
				report.append("  Example: ");
				if (mr.hasLine()) {
					report.append("line " + mr.getLine() + " of ");
				}
				report.append(mr.getFile() + lineFeed);
			}
			report.append("  " + mr.getTimesSeen() + " occurence(s)" + doubleLineFeed);
		}
		
		report.append(doubleLineFeed + "Info Found:" + doubleLineFeed);
		for (Iterator i = infoMessageRecords.values().iterator(); i.hasNext();) {
			MessageRecord mr = (MessageRecord) i.next();
			report.append("  INFO  " + mr.getMessage() + lineFeed);
			if (mr.hasFile()) {
				report.append("  Example: ");
				if (mr.hasLine()) {
					report.append("line " + mr.getLine() + " of ");
				}
				report.append(mr.getFile() + lineFeed);
			}
			report.append("  " + mr.getTimesSeen() + " occurence(s)" + doubleLineFeed);
		}
		
		report.append(doubleLineFeed + "Summary:" + doubleLineFeed);
		report.append("  " + totalValidated + " of " + totalFiles + " validated, " + numSkipped + " skipped" + lineFeed);
		report.append("  " + numPassed + " of " + totalValidated + " passed" + doubleLineFeed);
		report.append("End of Report" + lineFeed);
		return report.toString();
	}

}
