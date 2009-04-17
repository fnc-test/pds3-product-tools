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

package gov.nasa.pds.tools.label.parser;

import gov.nasa.pds.tools.label.validate.CatalogNameValidator;
import gov.nasa.pds.tools.label.validate.FileCharacteristicValidator;
import gov.nasa.pds.tools.label.validate.DuplicateIdentifierValidator;

/**
 * This class will be used to generate label parsers.
 * @author pramirez
 * @version $Revision$
 * 
 */
public class LabelParserFactory {
    private static LabelParserFactory factory = null;
    
    /**
     * Constructs a parser factory following the Singleton pattern
     */
    private LabelParserFactory() {
    }
    
    /**
     * Retrieve a factory which will create
     * @return parser factory that will generate parsers
     */
    public synchronized static LabelParserFactory getInstance() {
        if (factory == null) 
            factory = new LabelParserFactory();
        
        return factory;
    }
    
    /**
     * Returns a default label parser that will read in PDS label files.
     * 
     * @return The parser.
     */
    public LabelParser newLabelParser() {
    	return newLabelParser(LabelParser.LABEL);
    }
    
    /**
     * Returns a label parser that can either read in PDS label files,
     * PDS catalogs, or PDS label products.
     * 
     * @param type Set to 'CATALOG' to read catalog files, 'LABEL' to
     * read label files, or 'PRODUCT' to read label products.
     * 
     * @return The parser.
     */
    public LabelParser newLabelParser(String type) {
        // TODO: Change to dynamic class loading based upon configuration
        LabelParser parser = new DefaultLabelParser();
        loadValidators(parser, type);
        return parser;
    }
    
    /**
     * Loads the appropriate LabelValidator classes into the parser based
     * on the supplied type.
     * 
     * @param parser The parser.
     * @param type 'CATALOG' for catalog files, 'LABEL' for label files,
     * or 'PRODUCT' for label products.
     */
    private void loadValidators(LabelParser parser, String type) {
    	if(LabelParser.CATALOG.equals(type)) {
            parser.addLabelValidator(new DuplicateIdentifierValidator());
            parser.addLabelValidator(new CatalogNameValidator());
    	}
    	else if(LabelParser.LABEL.equals(type)) {
            parser.addLabelValidator(new FileCharacteristicValidator());
            parser.addLabelValidator(new DuplicateIdentifierValidator());
            parser.addFragmentValidator(new DuplicateIdentifierValidator());  		
    	}
    	else if(LabelParser.PRODUCT.equals(type)) {
            parser.addLabelValidator(new FileCharacteristicValidator());
            parser.addLabelValidator(new DuplicateIdentifierValidator());
            parser.addFragmentValidator(new DuplicateIdentifierValidator());     		
    	}
    }

}
