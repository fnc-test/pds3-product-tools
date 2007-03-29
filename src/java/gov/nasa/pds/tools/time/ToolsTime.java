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
	
	public ToolsTime() {
		calendar = Calendar.getInstance();
	}

	/**
	 * Get the current Date and Time
	 * 
	 * @param format How the date and time will be represented. Use
	 * the patterns and letters defined in Java's SimpleDateFormat class.
	 * 
	 * @return the current date and time
	 * @throws IllegalArgumentException If the format being passed in does
	 * not match the patterns defined in the SimpleDateFormat class
	 */
	public String getTime(SimpleDateFormat df) throws IllegalArgumentException {
		Date date = calendar.getTime();
		return df.format(date);
	}
}
