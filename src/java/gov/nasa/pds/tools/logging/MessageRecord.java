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

package gov.nasa.pds.tools.logging;

public class MessageRecord {
	private int timesSeen;
	private String message;
	private String file;
	private int line;
	
	public MessageRecord() {
		timesSeen = 0;
		message = null;
		file = null;
		line = 0;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void seen() {
		timesSeen++;
	}
	
	public int getTimesSeen() {
		return timesSeen;
	}
	
	public void setFile(String file) {
		this.file = file;
	}
	
	public String getFile() {
		return file;
	}
	
	public void setLine(int line) {
		this.line = line;
	}
	
	public int getLine() {
		return line;
	}
	
	public boolean hasLine() {
		return (line == -1) ? false : true;
	}
	
	public boolean hasFile() {
		return (file == null) ? false : true;
	}
}
