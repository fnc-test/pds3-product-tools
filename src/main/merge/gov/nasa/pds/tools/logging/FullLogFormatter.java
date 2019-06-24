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

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import gov.nasa.pds.tools.label.Label;

public class FullLogFormatter extends Formatter {
	private int numPassed;
	private int numFailed;
	private int numSkipped;
	private List records;
	private StringBuffer config;
	private StringBuffer parameters;
	private boolean headerPrinted;
	private static String lineFeed = System.getProperty("line.separator", "\n");
	private static String doubleLineFeed = System.getProperty("line.separator", "\n") + System.getProperty("line.separator", "\n");
	
	public FullLogFormatter() {
		numPassed = 0;
		numFailed = 0;
		numSkipped = 0;
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
		StringBuffer report = new StringBuffer("");
		
		if (!headerPrinted) {
			headerPrinted = true;
			report.append("PDS Validation Tool Report" + doubleLineFeed);
			report.append(config);
			report.append(lineFeed);
			report.append(parameters);
			report.append(lineFeed);
			report.append("Validation Details:" + lineFeed);
		}
		
		if (record.getMessage().equals("PASS"))
			numPassed++;
		else if (record.getMessage().equals("FAIL"))
			numFailed++;
		else
			numSkipped++;
		
		report.append(lineFeed + "  " + record.getMessage() + ": " + record.getFile() + lineFeed);
		
		for (Iterator i = records.iterator(); i.hasNext();) {
			ToolsLogRecord tlr = (ToolsLogRecord) i.next();
			if (tlr.getFile() != null && (record.getFile().equals(tlr.getFile()) || record.getFile().equals(tlr.getContext()))) {
				if (tlr.getLevel() == ToolsLevel.NOTIFICATION) {
					if (tlr.getContext() != null) {
						if ("BEGIN".equals(tlr.getMessage()))
							report.append("    Begin Fragment: " + tlr.getFile() + lineFeed);
						else if ("END".equals(tlr.getMessage()))
							report.append("    End Fragment: " + tlr.getFile() + lineFeed);		
					}
				} else if (tlr.getLevel() != ToolsLevel.SEVERE) {
					report.append("      " + tlr.getLevel().getName() + "  ");
					if (tlr.getLine() != -1)
						report.append("line " + tlr.getLine() + ": ");
					report.append(tlr.getMessage() + lineFeed);
				} else {
					report.append("      ERROR  ");
					if (tlr.getLine() != -1)
						report.append("line " + tlr.getLine() + ": ");
					report.append(tlr.getMessage() + lineFeed);
				}
			}
		}
		
		records = new ArrayList();
		return report.toString();
	}
	
	public String getTail(Handler handler) {
		StringBuffer report = new StringBuffer(doubleLineFeed + "Summary:" + doubleLineFeed);
		int totalFiles = numPassed + numFailed + numSkipped;
		int totalValidated = numPassed + numFailed;
		report.append("  " + totalValidated + " of " + totalFiles + " validated, " + numSkipped + " skipped" + lineFeed);
		report.append("  " + numPassed + " of " + totalValidated + " passed" + doubleLineFeed);
		report.append("End of Report" + lineFeed);
		return report.toString();
	}

}
