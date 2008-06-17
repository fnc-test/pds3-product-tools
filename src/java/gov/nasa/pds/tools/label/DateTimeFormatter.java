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
    private static SimpleDateFormat month = new SimpleDateFormat("-MM");
    private static SimpleDateFormat doy = new SimpleDateFormat("-DDD");
    private static SimpleDateFormat day = new SimpleDateFormat("-dd");
    private static SimpleDateFormat hour = new SimpleDateFormat("'T'HH");
    private static SimpleDateFormat minute = new SimpleDateFormat(":mm");
    private static SimpleDateFormat second = new SimpleDateFormat(":ss");
    private static SimpleDateFormat millisecond = new SimpleDateFormat(".SSS");
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
    
    static {
        month.setLenient(false);
        day.setLenient(false);
        doy.setLenient(false);
        hour.setLenient(false);
        minute.setLenient(false);
        second.setLenient(false);
        millisecond.setLenient(false);
        formatter.setLenient(false);
    }
    
    public static Date parse(String datetime) throws ParseException {
    	int previousPosition = 0;
    	int years = -1;
    	int months = -1;
    	int days = -1;
    	int hours = -1;
    	int minutes = -1;
    	int seconds = -1;
    	int milli = -1;
        ParsePosition position = new ParsePosition(0);
        Date date = null;
        Calendar workingCalendar = Calendar.getInstance();
        
        //Parse year first
        date = year.parse(datetime, position);
        if (date == null || position.getIndex() - previousPosition != 4)
            throw new ParseException("Could not create a date from " + datetime, previousPosition);
        else {
        	workingCalendar.setTime(date);
        	years = workingCalendar.get(Calendar.YEAR);
        	previousPosition = position.getIndex();
        }

        //Check to see if there is more of a date string to process
        if (position.getIndex() < datetime.length()) {
            date = month.parse(datetime, position);
            
            //If it was not parseable check to see if it is doy format other
            if (date == null || position.getIndex() - previousPosition != 3) {
            	position.setIndex(previousPosition);
                date = doy.parse(datetime, position);
                if (date == null || position.getIndex() - previousPosition != 4)
                    throw new ParseException("Could not create a date from " + datetime, previousPosition);
                else {
                    //Date was doy format. Set day of year and move on.
                    workingCalendar.setTime(date);
                    months = workingCalendar.get(Calendar.MONTH) + 1;
                    days = workingCalendar.get(Calendar.DAY_OF_MONTH);
                    previousPosition = position.getIndex();
                }
            } else {
                //Date was month based format. Set month.
                workingCalendar.setTime(date);
                months = workingCalendar.get(Calendar.MONTH) + 1;
                previousPosition = position.getIndex();
                //Check to see if there is a day of the month present
                if (position.getIndex() < datetime.length()) {
                    date = day.parse(datetime, position);
                    if (date == null || position.getIndex() - previousPosition != 3)
                        throw new ParseException("Could not create a date from "  + datetime, previousPosition);
                    else {
                        //Date had a day of month. Set day of month and move on.
                        workingCalendar.setTime(date);
                        days = workingCalendar.get(Calendar.DAY_OF_MONTH);
                        previousPosition = position.getIndex();
                    }
                }
            }
        }
        
        //Now we should be at the time string if there is any
        //First will come the hours
        if (position.getIndex() < datetime.length()) {
            date = hour.parse(datetime, position);
            if (date == null || position.getIndex() - previousPosition != 3)
                throw new ParseException("Could not create a date from " + datetime, previousPosition);
            else {
                //Date had an hour. Set and move on
                workingCalendar.setTime(date);
                hours = workingCalendar.get(Calendar.HOUR_OF_DAY);  
                previousPosition = position.getIndex();
            }
        }
        
        //Check for minutes
        if (position.getIndex() < datetime.length() && datetime.charAt(position.getIndex()) != 'Z') {
            date = minute.parse(datetime, position);
            if (date == null || position.getIndex() - previousPosition != 3)
                throw new ParseException("Could not create a date from " + datetime, previousPosition);
            else {
                //Date had minutes. Set and move on
                workingCalendar.setTime(date);
                minutes = workingCalendar.get(Calendar.MINUTE);
                previousPosition = position.getIndex();
            }
        }
        
        //Check for seconds
        if (position.getIndex() < datetime.length() && datetime.charAt(position.getIndex()) != 'Z') {
            date = second.parse(datetime, position);
            if (date == null || position.getIndex() - previousPosition != 3)
                throw new ParseException("Could not create a date from " + datetime, previousPosition);
            else {
                //Date had seconds. Set and move on
                workingCalendar.setTime(date);
                seconds = workingCalendar.get(Calendar.SECOND);
                previousPosition = position.getIndex();
            }
        }
            
        //Check for milliseconds
        if (position.getIndex() < datetime.length() && datetime.charAt(position.getIndex()) != 'Z') {
            date = millisecond.parse(datetime, position);
            if (date == null || position.getIndex() - previousPosition != 4)
                throw new ParseException("Could not create a date from " + datetime, previousPosition);
            else {
                //Date had milliseconds. Set and move on
                workingCalendar.setTime(date);
                milli = workingCalendar.get(Calendar.MILLISECOND);
                previousPosition = position.getIndex();
            }
        }
        
        //Have we parsed the full date? If not throw an error
        if (position.getIndex() < (datetime.length() - 2) || (position.getIndex() == (datetime.length() - 1) && datetime.charAt(position.getIndex()) != 'Z')) {
            throw new ParseException("Could not create a date from " + datetime, position.getIndex());
        }
        
        //Date is not actually calculated in a calendar until you make a call to getTime.
        //Must check that date is fine.
        Date returnDate = null;
        GregorianCalendar calendar = new GregorianCalendar(years, months, days, hours, minutes, seconds);
        try {
        	if (milli != -1)
        		calendar.set(Calendar.MILLISECOND, milli);
        	if (days != 0)
        		formatter.parse(years + "/" + months + "/" + days);
            returnDate = calendar.getTime();
        } catch (ParseException pe) {
            throw new ParseException("Could not create a date from " + datetime, position.getIndex());
        }
        
        return returnDate;
    }
}
