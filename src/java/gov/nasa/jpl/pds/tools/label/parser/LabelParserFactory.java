//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools.label.parser;

/**
 * This class will be used to generate label parsers.
 * @author pramirez
 * @version $Revision$
 * 
 */
public class LabelParserFactory {
    private static LabelParserFactory factory = null;
    
    private LabelParserFactory() {
    }
    
    /**
     * Retrieve a factory which will create
     * @return parser factory that will generate parsers
     */
    public synchronized static LabelParserFactory newInstance() {
        if (factory == null) 
            factory = new LabelParserFactory();
        
        return factory;
    }
    
    /**
     * Retrieves a parser that will read in PDS label files.
     * @return The parser
     */
    public LabelParser newLabelParser() {
        // TODO: Change to dynamic class loading based upon configuration
        return new DefaultLabelParser();
    }

}
