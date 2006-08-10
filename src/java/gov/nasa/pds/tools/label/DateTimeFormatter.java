//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    private static SimpleDateFormat hour = new SimpleDateFormat("'T'hh");
    private static SimpleDateFormat minute = new SimpleDateFormat(":mm");
    private static SimpleDateFormat second = new SimpleDateFormat(":ss");
    private static SimpleDateFormat millisecond = new SimpleDateFormat(".SSS");
    
    static {
        month.setLenient(false);
        day.setLenient(false);
        doy.setLenient(false);
        hour.setLenient(false);
        minute.setLenient(false);
        second.setLenient(false);
        millisecond.setLenient(false);
    }
    
    public static Date parse(String datetime) throws ParseException {
        ParsePosition position = new ParsePosition(0);
        Date date = null;
        Calendar calendar = null;
        Calendar workingCalendar = Calendar.getInstance();
        
        //Parse year first
        date = year.parse(datetime, position);
        if (date == null)
            throw new ParseException("Could not create a date from " + datetime, position.getIndex());
        
        calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.setTime(date);
        //Check to see if there is more of a date string to process
        if (position.getIndex() < datetime.length()) {
            date = month.parse(datetime, position);
            
            //If it was not parseable check to see if it is doy format otherw
            if (date == null) {
                date = doy.parse(datetime, position);
                if (date == null)
                    throw new ParseException("Could not create a date from " + datetime, position.getIndex());
                else {
                    //Date was doy format. Set day of year and move on.
                    workingCalendar.setTime(date);
                    calendar.set(Calendar.DAY_OF_YEAR, workingCalendar.get(Calendar.DAY_OF_YEAR));
                }
            } else {
                //Date was month based format. Set month.
                workingCalendar.setTime(date);
                calendar.set(Calendar.MONTH, workingCalendar.get(Calendar.MONTH));
                
                //Check to see if there is a day of the month present
                if (position.getIndex() < datetime.length()) {
                    date = day.parse(datetime, position);
                    if (date == null)
                        throw new ParseException("Could not create a date from " + datetime, position.getIndex());
                    else {
                        //Date had a month. Set month and move on.
                        workingCalendar.setTime(date);
                        calendar.set(Calendar.DAY_OF_MONTH, workingCalendar.get(Calendar.DAY_OF_MONTH));
                    }
                }
            }
        }
        
        //Now we should be at the time string if there is any
        //First will come the hours
        if (position.getIndex() < datetime.length()) {
            date = hour.parse(datetime, position);
            if (date == null)
                throw new ParseException("Could not create a date from " + datetime, position.getIndex());
            else {
                //Date had an hour. Set and move on
                workingCalendar.setTime(date);
                calendar.set(Calendar.HOUR_OF_DAY, workingCalendar.get(Calendar.HOUR_OF_DAY));
            }
        }
        
        //Check for minutes
        if (position.getIndex() < datetime.length()) {
            date = minute.parse(datetime, position);
            if (date == null)
                throw new ParseException("Could not create a date from " + datetime, position.getIndex());
            else {
                //Date had minutes. Set and move on
                workingCalendar.setTime(date);
                calendar.set(Calendar.MINUTE, workingCalendar.get(Calendar.MINUTE));
            }
        }
        
        //Check for seconds
        if (position.getIndex() < datetime.length()) {
            date = second.parse(datetime, position);
            if (date == null)
                throw new ParseException("Could not create a date from " + datetime, position.getIndex());
            else {
                //Date had minutes. Set and move on
                workingCalendar.setTime(date);
                calendar.set(Calendar.SECOND, workingCalendar.get(Calendar.SECOND));
            }
        }
            
        //Check for seconds
        if (position.getIndex() < datetime.length()) {
            date = millisecond.parse(datetime, position);
            if (date == null)
                throw new ParseException("Could not create a date from " + datetime, position.getIndex());
            else {
                //Date had minutes. Set and move on
                workingCalendar.setTime(date);
                calendar.set(Calendar.MILLISECOND, workingCalendar.get(Calendar.MILLISECOND));
            }
        }
        
        //Date is not actually calculated in a calendar until you make a call to getTime.
        //Must check that date is fine.
        Date returnDate = null;
        try {
            returnDate = calendar.getTime();
        } catch (IllegalArgumentException ia) {
            throw new ParseException("Could not create a date from " + datetime, position.getIndex());
        }
        
        return returnDate;
    }
}
