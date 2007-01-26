//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.util;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class Utility {
    
    public static String stripWhitespace(String value) {
        return filterString(stripNewLines(value));
    }
    
    public static String stripNewLines(String value) {
        String filteredValue = value; 
        //Perform whitespace stripping
        //First replace all hyphen line.separator with ""
        filteredValue = filteredValue.replaceAll("-" + System.getProperty("line.separator"), "");
        //Next replace all line.separator with " "
        filteredValue = filteredValue.replaceAll(System.getProperty("line.separator"), " ");
        return filteredValue;
    }
    
    public static String filterString(String value, boolean upperCase) {
        String filteredValue = value;
        //Replace all '_' with ' '
        filteredValue = filteredValue.replaceAll("_", " ");
        //Replace multiple spaces with a single space
        filteredValue = filteredValue.replaceAll("\\s+", " ");
        //Uppercase everything
        if (upperCase)
            filteredValue = filteredValue.toUpperCase();
        //Trim whitespace
        filteredValue = filteredValue.trim();
        return filteredValue;
    }
    
    public static String filterString(String value) {
        return filterString(value, true);
    }
    
    public static String trimString(String value, int length) {
        String trimmedString = value;
        
        if (trimmedString.length() > length*3)
            trimmedString = trimmedString.substring(0, length*3);
        trimmedString = stripNewLines(trimmedString);
        trimmedString = filterString(trimmedString, false);
        if (trimmedString.length() > length)
            trimmedString = trimmedString.substring(0, length-1);
        
        return trimmedString;
    }
}
