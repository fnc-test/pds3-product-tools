package gov.nasa.pds.tools.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Class that can get the date and time
 * 
 * @author mcayanan
 *
 */
public class ToolsTime {
	
	private Calendar calendar;
	private Date date;
	private String datetime;
	
	public ToolsTime() {
		calendar = Calendar.getInstance();
		date = null;
		datetime = null;
	}

	/**
	 * Get the current Date and Time
	 * 
	 * @param format How the date and time will be represented. Must use
	 * the patterns and letters defined in Java's SimpleDateFormat class.
	 * 
	 * @return the current date and time
	 * @throws IllegalArgumentException If the format being passed in does
	 * not match the patterns defined in the SimpleDateFormat class
	 */
	public String getTime(String format) throws IllegalArgumentException {
		SimpleDateFormat df = new SimpleDateFormat(format);
		date = calendar.getTime();
		datetime = df.format(date);
		return datetime;
	}
}
