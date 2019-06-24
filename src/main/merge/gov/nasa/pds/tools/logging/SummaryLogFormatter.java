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

import gov.nasa.pds.tools.label.Label;

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
		} else if (toolsRecord.getLevel() == ToolsLevel.NOTIFICATION&& (Label.PASS.equals(toolsRecord.getMessage()) || 
				Label.SKIP.equals(toolsRecord.getMessage()) || Label.FAIL.equals(toolsRecord.getMessage()))) {
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
