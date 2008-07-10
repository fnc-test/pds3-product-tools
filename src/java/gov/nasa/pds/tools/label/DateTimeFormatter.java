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
//

package gov.nasa.pds.tools.label;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class DateTimeFormatter {
    private static SimpleDateFormat year = new SimpleDateFormat("yyyy");
    private static SimpleDateFormat ym = new SimpleDateFormat("yyyy-MM");
    private static SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat doy = new SimpleDateFormat("yyyy-DDD");
    private static SimpleDateFormat hour = new SimpleDateFormat("HH");
    private static SimpleDateFormat minute = new SimpleDateFormat(":mm");
    private static SimpleDateFormat second = new SimpleDateFormat(":ss");
    private static SimpleDateFormat millisecond = new SimpleDateFormat(".SSS");
    
    static {
    	year.setLenient(false);
    	ym.setLenient(false);
    	ymd.setLenient(false);
        doy.setLenient(false);
        hour.setLenient(false);
        minute.setLenient(false);
        second.setLenient(false);
        millisecond.setLenient(false);
    }
    
    public static Date parse(String datetime) throws ParseException {
    	int previousPosition = 0;
    	long time = 0;
        ParsePosition position = new ParsePosition(0);
        Date workingDate = null;
        Calendar workingCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormatter = null;
        
        String[] date = datetime.split("T");
        String currentDate = date[0];
        String currentTime = null;
        if (date.length == 2)
        	currentTime = date[1];
        
        if (currentDate.length() == 4)
        	dateFormatter = year;
        else if (currentDate.length() == 7)
        	dateFormatter = ym;
        else if (currentDate.length() == 10)
        	dateFormatter = ymd;
        else if (currentDate.length() == 8)
        	dateFormatter = doy;
        else
        	throw new ParseException("Could not create a date from " + datetime, previousPosition);
        
        workingDate = dateFormatter.parse(currentDate, position);
        if (workingDate == null)
        	throw new ParseException("Could not create a date from " + datetime, previousPosition);
        else {
            workingCalendar.setTime(workingDate);
            time += workingCalendar.getTimeInMillis();
        }
        
        position.setIndex(0);
        //Now we should be at the time string if there is any
        if (currentTime != null) {
	        //First will come the hours
	        if (position.getIndex() < currentTime.length()) {
	            workingDate = hour.parse(currentTime, position);
	            if (workingDate == null || position.getIndex() - previousPosition != 2)
	                throw new ParseException("Could not create a date from " + datetime, previousPosition);
	            else {
	                //Date had an hour. Set and move on
	                workingCalendar.setTime(workingDate);
	                time += workingCalendar.get(Calendar.HOUR_OF_DAY)*60*60*1000;
	                previousPosition = position.getIndex();
	            }
	        }

	        //Check for minutes
	        if (position.getIndex() < currentTime.length() && currentTime.charAt(position.getIndex()) != 'Z') {
	            workingDate = minute.parse(currentTime, position);
	            if (workingDate == null || position.getIndex() - previousPosition != 3)
	                throw new ParseException("Could not create a date from " + datetime, previousPosition);
	            else {
	                //Date had minutes. Set and move on
	                workingCalendar.setTime(workingDate);
	                time += workingCalendar.get(Calendar.MINUTE)*60*1000;
	                previousPosition = position.getIndex();
	            }
	        }

	        //Check for seconds
	        if (position.getIndex() < currentTime.length() && currentTime.charAt(position.getIndex()) != 'Z') {
	            workingDate = second.parse(currentTime, position);
	            if (workingDate == null || position.getIndex() - previousPosition != 3)
	                throw new ParseException("Could not create a date from " + datetime, previousPosition);
	            else {
	                //Date had seconds. Set and move on
	                workingCalendar.setTime(workingDate);
	                time += workingCalendar.get(Calendar.SECOND)*1000;
	                previousPosition = position.getIndex();
	            }
	        }

	        //Check for milliseconds
	        if (position.getIndex() < currentTime.length() && currentTime.charAt(position.getIndex()) != 'Z') {
	            workingDate = millisecond.parse(currentTime, position);
	            if (workingDate == null || position.getIndex() - previousPosition != 4)
	                throw new ParseException("Could not create a date from " + datetime, previousPosition);
	            else {
	                //Date had milliseconds. Set and move on
	                workingCalendar.setTime(workingDate);
	                time += workingCalendar.get(Calendar.MILLISECOND);
	                previousPosition = position.getIndex();
	            }
	        }
        }
        
        //Date is not actually calculated in a calendar until you make a call to getTime.
        //Must check that date is fine.
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time);
        
        return calendar.getTime();
    }
}
