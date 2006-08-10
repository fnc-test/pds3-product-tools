//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label;

import java.text.ParseException;
import java.util.Date;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class DateTime extends Scalar {
    private Date date;
    
    /**
     * @param value
     */
    public DateTime(String value) throws ParseException {
        super(value);
        date = DateTimeFormatter.parse(value);
    }
    
    public DateTime(Date date) {
        super(date.toString());
        this.date = date;
    }
    
    public Date getDate() {return date;}

}
