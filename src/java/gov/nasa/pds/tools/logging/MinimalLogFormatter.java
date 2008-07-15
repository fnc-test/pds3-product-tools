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

import gov.nasa.pds.tools.label.Label;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MinimalLogFormatter extends Formatter {
	private int numPassed;
	private int numFailed;
	private int numSkipped;
	private int totalErrors;
	private int totalWarnings;
	private int totalInfos;
	private List records;
	private StringBuffer config;
	private StringBuffer parameters;
	private boolean headerPrinted;
	private static String padding = "      ";
	private static String lineFeed = System.getProperty("line.separator", "\n");
	private static String doubleLineFeed = System.getProperty("line.separator", "\n") + System.getProperty("line.separator", "\n");
	
	public MinimalLogFormatter() {
		numPassed = 0;
		numFailed = 0;
		numSkipped = 0;
		totalErrors = 0;
		totalWarnings = 0;
		totalInfos = 0;
		headerPrinted = false;
		records = new ArrayList();
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
		} else if (toolsRecord.getLevel() == ToolsLevel.NOTIFICATION && (Label.PASS.equals(toolsRecord.getMessage()) || 
				Label.SKIP.equals(toolsRecord.getMessage()) || Label.FAIL.equals(toolsRecord.getMessage()))) {
			return processRecords(toolsRecord);
		} else {
			records.add(toolsRecord);
		}
		
		return "";
	}
	
	private String processRecords(ToolsLogRecord record) {
		StringBuffer report = new StringBuffer();
		int numErrors = 0;
		int numWarnings = 0;
		int numInfos = 0;

		if (!headerPrinted) {
			headerPrinted = true;
			report.append("PDS Validation Tool Report" + doubleLineFeed);
			report.append(config);
			report.append(lineFeed);
			report.append(parameters);
			report.append(lineFeed);
			report.append("Message Counts:" + doubleLineFeed);
			report.append(" ERROR    WARN    INFO    FILE" + lineFeed);
		}
		
		if (record.getMessage().equals("PASS"))
			numPassed++;
		else if (record.getMessage().equals("FAIL"))
			numFailed++;
		else
			numSkipped++;
		
		for (Iterator i = records.iterator(); i.hasNext();) {
			ToolsLogRecord tlr = (ToolsLogRecord) i.next();
			if (tlr.getFile() != null && (record.getFile().equals(tlr.getFile()) || record.getFile().equals(tlr.getContext()))) {
				if (tlr.getLevel() == ToolsLevel.SEVERE)
					numErrors++;
				else if (tlr.getLevel() == ToolsLevel.WARNING)
					numWarnings++;
				else if (tlr.getLevel() == ToolsLevel.INFO)
					numInfos++;
			}
		}
		
		totalErrors += numErrors;
		totalWarnings += numWarnings;
		totalInfos += numInfos;
		
		String errors = "" + numErrors;
		String warnings = "" + numWarnings;
		String infos = "" + numInfos;
		
		report.append(padding.substring(errors.length()) + errors + "  ");
		report.append(padding.substring(warnings.length()) + warnings + "  ");
		report.append(padding.substring(infos.length()) + infos + "    ");
		report.append(record.getFile() + lineFeed);
		
		records = new ArrayList();
		return report.toString();
	}

	public String getTail(Handler handler) {
		StringBuffer report = new StringBuffer();
		int totalFiles = numPassed + numFailed + numSkipped;
		int totalValidated = numPassed + numFailed;
		
		report.append("----------------------" + lineFeed);
		String errors = "" + totalErrors;
		String warnings = "" + totalWarnings;
		String infos = "" + totalInfos;
		
		report.append(padding.substring(errors.length()) + errors + "  ");
		report.append(padding.substring(warnings.length()) + warnings + "  ");
		report.append(padding.substring(infos.length()) + infos + doubleLineFeed);
		
		report.append(doubleLineFeed + "Summary:" + doubleLineFeed);
		report.append("  " + totalValidated + " of " + totalFiles + " validated, " + numSkipped + " skipped" + lineFeed);
		report.append("  " + numPassed + " of " + totalValidated + " passed" + doubleLineFeed);
		report.append("End of Report" + lineFeed);
		return report.toString();
	}
}
