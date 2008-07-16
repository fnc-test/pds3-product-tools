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

import gov.nasa.pds.tools.label.validate.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class represents a PDS label.
 * @author pramirez
 * @version $Revision$
 * 
 */
public class Label implements LabelType, Status {
    private volatile int labelType;
    private Map statements;
    private String filename;
    private String status;
    private int numErrors;
    private int numWarnings;
    
    /**
     * Constructs an object representation of a PDS label.
     *
     */
    public Label() {
        statements = new HashMap();
        labelType = UNDEFINED;
        filename = null;
        status = Status.UNKNOWN;
        numErrors = 0;
        numWarnings = 0;
    }

    /**
     * Retrieves a statement with the identifier
     * @param identifier Identifies the statement to retrieve
     * @return The named statement or null if not found
     */
    public List getStatement(String identifier) {
        return (List) statements.get(identifier);
    }
    
    /**
     * Retrieves the attribute with the identifier or null if not found
     * @param identifier of attribute to find
     * @return attribute or null
     */
    public AttributeStatement getAttribute(String identifier) {
        AttributeStatement attribute = null;
        
        if (statements.get(identifier) != null) {
            for (Iterator i = ((List) statements.get(identifier)).iterator(); i.hasNext() && attribute == null;) {
                Statement statement = (Statement) i.next();
                if (statement instanceof AttributeStatement)
                    attribute = (AttributeStatement) statement;
            }
        }
        
        return attribute;
    }
    
    /**
     * Retrieves the groups with the identifier or null if not found
     * @param identifier of group to find
     * @return {@link List} of {@link GroupStatement}
     */
    public List getGroups(String identifier) {
        List groups = new ArrayList();
        
        if (statements.get(identifier) != null) {
            for (Iterator i = ((List) statements.get(identifier)).iterator(); i.hasNext();) {
                Statement statement = (Statement) i.next();
                if (statement instanceof GroupStatement)
                    groups.add(statement);
            }
        }
        
        return groups;
    }
  
    /**
     * Retrieves the object with the identifier or null if not found
     * @param identifier of object to find
     * @return {@link List} of {@link ObjectStatement}
     */
    public List getObjects(String identifier) {
        List objects = new ArrayList();
        
        if (statements.get(identifier) != null) {
            for (Iterator i = ((List) statements.get(identifier)).iterator(); i.hasNext();) {
                Statement statement = (Statement) i.next();
                if (statement instanceof ObjectStatement)
                    objects.add(statement);
            }
        }
        
        return objects;
    }
 
    /**
     * Retrieves the statements associated with this label
     * @return {@link List} of {@link Statement}
     */
    public List getStatements() { 
        List results = new ArrayList();
        for (Iterator i = statements.values().iterator(); i.hasNext();)
            results.addAll((List) i.next());
        return results;
    }
 
    /**
     * Retrieves objects associated with this label
     * @return List of {@link ObjectStatement}
     */
    public List getObjects() {
        List objects = new ArrayList(); 
        for (Iterator i = statements.values().iterator(); i.hasNext();) {
            for (Iterator s = ((List) i.next()).iterator(); s.hasNext();) {
                Statement statement = (Statement) s.next();
                if (statement instanceof ObjectStatement)
                    objects.add(statement);
            }
        }
        return objects;
    }
    
    /**
     * Retrieves groups associated with this label
     * @return list of {@link GroupStatement}
     */
    public List getGroups() {
        List groups = new ArrayList(); 
        for (Iterator i = statements.values().iterator(); i.hasNext();) {
            for (Iterator s = ((List) i.next()).iterator(); s.hasNext();) {
                Statement statement = (Statement) s.next();
                if (statement instanceof GroupStatement)
                    groups.add(statement);
            }
        }
        return groups;
    }
    
    /**
     * Retrieves attributes associated with this label
     * @return list of {@link AttributeStatement}
     */
    public List getAttributes() {
        List attributes = new ArrayList(); 
        for (Iterator i = statements.values().iterator(); i.hasNext();) {
            for (Iterator s = ((List) i.next()).iterator(); s.hasNext();) {
                Statement statement = (Statement) s.next();
                if (statement instanceof AttributeStatement)
                    attributes.add(statement);
            }
        }
        return attributes;
    }
    
    /**
     * Retrieves pointers associated with this label
     * @return list of {@link PointerStatement}
     */
    public List getPointers() {
        List pointers = new ArrayList();
        for (Iterator i = statements.values().iterator(); i.hasNext();) {
            for (Iterator s = ((List) i.next()).iterator(); s.hasNext();) {
                Statement statement = (Statement) s.next();
                if (statement instanceof PointerStatement)
                    pointers.add(statement);
            }
        }
        return pointers;
    }
 
    /**
     * Associates a statement with this label
     * @param statement to be added to label
     */
    public synchronized void addStatement(Statement statement) { 
        labelType = UNDEFINED;
        List stmnts = (List) statements.get(statement.getIdentifier());
        if (stmnts == null) {
            stmnts = new ArrayList();
            statements.put(statement.getIdentifier(), stmnts);
        }
        if (statement instanceof IncludePointer) {
            stmnts.add(statement);
            IncludePointer ip = (IncludePointer) statement;
            setStatus(ip.getLoadStatus());
            for (Iterator i = ip.getStatements().iterator(); i.hasNext();) {
            	Statement stmnt = (Statement) i.next();
            	List subStmnts = (List) statements.get(stmnt.getIdentifier());
                if (subStmnts == null) {
                    subStmnts = new ArrayList();
                    statements.put(stmnt.getIdentifier(), subStmnts);
                }
                subStmnts.add(stmnt);
            }
        }
        else 
            stmnts.add(statement);
    }

    /**
     * Returns the type of label, see {@link LabelType} for the types of label.
     * @return type of label
     */
    public synchronized int getLabelType() {
        //Check to see if flag has been set if not figure it out and set it
        if (labelType == UNDEFINED) {
            List pointers = getPointers();
            for (Iterator i = pointers.iterator(); i.hasNext() && labelType == UNDEFINED;) {
                PointerStatement pointer = (PointerStatement) i.next();
                if (pointer.getPointerType() == PointerType.DATA_LOCATION && pointer.getValue() instanceof Numeric)
                    labelType = ATTACHED;
                else
                    labelType = DETACHED;
            }
            //If label type is still undefined we need to check if its a combined detached label
            if (labelType == UNDEFINED && getObjects("FILE").size() != 0)
                labelType = COMBINED_DETACHED;
            //If all fails default to attached label
            if (labelType == UNDEFINED)
                labelType = ATTACHED;
        }
        
        return labelType;
    }
    
    /**
     * Sets the type of label
     * @param labelType of this label
     */
    public void setLabelType(int labelType) {
        this.labelType = labelType;
    }
    
    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    public String getFilename() {
        return filename;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        //Make sure we aren't trying to set status to unknown
        if (!UNKNOWN.equals(status)) {
           //Set to pass if unknown 
           //Set to fail if that is the status being passed in
           //Drop everything else
           if (PASS.equals(status) && UNKNOWN.equals(this.status))
              this.status = PASS;
           else if (SKIP.equals(status) && UNKNOWN.equals(this.status))
              this.status = SKIP;
           else if (FAIL.equals(status))
        	  this.status = FAIL;
        }
    }
    
    public void incrementErrors() {
        numErrors++;
    }
    
    public void incrementWarnings() {
        numWarnings++;
    }
    
    public void incrementErrors(int numErrors) {
        this.numErrors += numErrors;
    }
    
    public void incrementWarnings(int numWarnings) {
        this.numWarnings += numWarnings;
    }
    
    public int getNumErrors() {return numErrors;}
    public int getNumWarnings() {return numWarnings;}
}
