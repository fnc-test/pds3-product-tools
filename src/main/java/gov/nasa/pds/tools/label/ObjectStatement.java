// Copyright 2006-2010, by the California Institute of Technology.
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

import gov.nasa.pds.tools.dict.DictIdentifier;
import gov.nasa.pds.tools.dict.parser.DictIDFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class ObjectStatement extends Statement {

    private Map<DictIdentifier, List<Statement>> statements;

    /**
     * Constructs a new object statement with no attributes or nested objects
     * 
     * @param lineNumber
     *            Line number of the statement.
     * @param identifier
     *            Identifier for the statement.
     */
    public ObjectStatement(final Label sourceLabel, int lineNumber,
            String identifier) {
        this(sourceLabel, lineNumber, identifier,
                new HashMap<DictIdentifier, List<Statement>>());
    }

    /**
     * Constructs an ObjectStatement with only an identifier
     * 
     * @param identifier
     *            Identifier of the statement
     */
    public ObjectStatement(final Label sourceLabel, String identifier) {
        this(sourceLabel, -1, identifier);
    }

    /**
     * Constructs an ObjectStatement
     * 
     * @param lineNumber
     *            Line number of statement
     * @param identifier
     *            Identifier of statement
     * @param statements
     *            Map of {@link Statement} associated with this object
     */
    public ObjectStatement(final Label sourceLabel, int lineNumber,
            String identifier, Map<DictIdentifier, List<Statement>> statements) {
        super(sourceLabel, lineNumber, DictIDFactory
                .createObjectDefId(identifier));
        this.statements = statements;
    }

    private List<? extends Statement> getElements(final DictIdentifier key) {
        List<Statement> returnStatements = this.statements.get(key);
        if (returnStatements == null) {
            returnStatements = new ArrayList<Statement>();
        }
        return returnStatements;
    }

    private List<? extends Statement> getElements(final Class<?> classType) {
        final List<Statement> returnStatements = new ArrayList<Statement>();
        for (List<Statement> stmts : this.statements.values()) {
            for (Statement statement : stmts) {
                if (statement.getClass().equals(classType)) {
                    returnStatements.add(statement);
                }
            }
        }
        return returnStatements;
    }

    /**
     * Retrieves the list of attributes associated with the ObjectStatement
     * 
     * @return The list of AttributeStatement
     */
    @SuppressWarnings("unchecked")
    public List<AttributeStatement> getAttributes() {
        return (List<AttributeStatement>) getElements(AttributeStatement.class);
    }

    /**
     * Retrieves pointers associated with this object
     * 
     * @return list of {@link PointerStatement}
     */
    public List<PointerStatement> getPointers() {
        List<PointerStatement> returnPointers = new ArrayList<PointerStatement>();
        for (List<Statement> stmts : this.statements.values()) {
            for (Statement statement : stmts) {
                if (statement instanceof PointerStatement) {
                    returnPointers.add((PointerStatement) statement);
                }
            }
        }
        return returnPointers;
    }

    /**
     * Retrieves groups associated with this object
     * 
     * @return list of {@link GroupStatement}
     */
    @SuppressWarnings("unchecked")
    public List<GroupStatement> getGroups() {
        return (List<GroupStatement>) getElements(GroupStatement.class);
    }

    /**
     * Looks to see if this object contains a pointer with the given identifier
     * 
     * @param id
     *            of pointer statement to look for
     * @return flag indicating whether or not the pointer was found
     */
    public boolean hasPointer(DictIdentifier id) {
        validatePointerType(id);
        return this.statements.get(id) != null;
    }

    /**
     * Retrieves the named attribute
     * 
     * @param id
     * @return The named AttributeStatement or null if not found
     */
    @SuppressWarnings("unchecked")
    public AttributeStatement getAttribute(DictIdentifier id) {
        validateElementType(id);
        List<AttributeStatement> attribs = (List<AttributeStatement>) getElements(id);
        if (attribs.size() > 0) {
            return attribs.get(0);
        }
        return null;
    }

    /**
     * Retrieves the named pointer
     * 
     * @param id
     * @return The named PointerStatement or null if not found
     */
    @SuppressWarnings("unchecked")
    public PointerStatement getPointer(DictIdentifier id) {
        validatePointerType(id);
        List<PointerStatement> pointers = (List<PointerStatement>) getElements(id);
        if (pointers.size() > 0) {
            return pointers.get(0);
        }
        return null;
    }

    // convenience method - to be used sparingly
    public AttributeStatement getAttribute(String id) {
        return getAttribute(DictIDFactory.createElementDefId(id));
    }

    /**
     * Retrieves the list of objects associated with this object
     * 
     * @return The list of ObjectStatement
     */
    @SuppressWarnings("unchecked")
    public List<ObjectStatement> getObjects() {
        return (List<ObjectStatement>) getElements(ObjectStatement.class);
    }

    /**
     * Retrieves the named object
     * 
     * @param id
     * @return The {@link List} of named objects
     */
    @SuppressWarnings("unchecked")
    public List<ObjectStatement> getObjects(DictIdentifier id) {
        validateObjectType(id);
        return (List<ObjectStatement>) getElements(id);
    }

    // convenience method, use sparingly
    @SuppressWarnings("unchecked")
    public List<ObjectStatement> getObjects(String id) {
        return (List<ObjectStatement>) getElements(DictIDFactory
                .createObjectDefId(id));
    }

    /**
     * Retrieves the named group
     * 
     * @param id
     *            of the group
     * @return the {@link List} of named groups
     */
    @SuppressWarnings("unchecked")
    public List<GroupStatement> getGroups(DictIdentifier id) {
        validateGroupType(id);
        return (List<GroupStatement>) getElements(id);
    }

    // convenience method, use sparingly
    @SuppressWarnings("unchecked")
    public List<GroupStatement> getGroups(String id) {
        return (List<GroupStatement>) getElements(DictIDFactory
                .createGroupDefId(id));
    }

    /**
     * Associates a statement with this object
     * 
     * @param statement
     *            to be added to object
     */
    public void addStatement(Statement statement) {
        DictIdentifier id = statement.getIdentifier();
        List<Statement> stmnts = this.statements.get(id);
        if (stmnts == null) {
            stmnts = new ArrayList<Statement>();
            this.statements.put(id, stmnts);
        }
        if (statement instanceof IncludePointer) {
            stmnts.add(statement);
            for (Statement stmt : ((IncludePointer) statement).getStatements()) {
                addStatement(stmt);
            }
        } else {
            stmnts.add(statement);
        }
    }

    public boolean hasAttribute(DictIdentifier id) {
        validateElementType(id);
        return (getAttribute(id) == null) ? false : true;
    }

    public boolean hasObject(DictIdentifier id) {
        validateObjectType(id);
        return (getObjects(id).size() == 0) ? false : true;
    }

    public boolean hasGroup(DictIdentifier id) {
        validateGroupType(id);
        return (getGroups(id).size() == 0) ? false : true;
    }

    public List<Statement> getStatements() {
        List<Statement> results = new ArrayList<Statement>();
        for (List<Statement> stmts : this.statements.values()) {
            results.addAll(stmts);
        }
        return results;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if ((object == null) || (object.getClass() != this.getClass())) {
            return false;
        }
        ObjectStatement thatObject = (ObjectStatement) object;
        List<AttributeStatement> thoseAttributes = thatObject.getAttributes();
        for (AttributeStatement thisAttribute : getAttributes()) {
            AttributeStatement thatAttribute = thatObject
                    .getAttribute(thisAttribute.getIdentifier());
            if (thatAttribute == null) {
                return false;
            } else {
                if (!thisAttribute.equals(thatAttribute)) {
                    return false;
                } else {
                    thoseAttributes.remove(thatAttribute);
                }
            }
        }
        if (!thoseAttributes.isEmpty()) {
            return false;
        }
        List<PointerStatement> thosePointers = thatObject.getPointers();
        for (PointerStatement thisPointer : getPointers()) {
            boolean foundMatch = false;
            for (PointerStatement thatPointer : thosePointers) {
                if ((foundMatch = thisPointer.equals(thatPointer))) {
                    thosePointers.remove(thatPointer);
                    break;
                }
            }
            if (foundMatch == false) {
                return false;
            }
        }
        // Check if the object passed in has any left over pointers
        if (!thosePointers.isEmpty()) {
            return false;
        }
        List<ObjectStatement> thoseObjects = thatObject.getObjects();
        for (ObjectStatement thisObject : getObjects()) {
            boolean foundMatch = false;
            for (ObjectStatement o : thoseObjects) {
                if ((foundMatch = thisObject.equals(o))) {
                    thoseObjects.remove(o);
                    break;
                }
            }
            if (foundMatch == false) {
                return false;
            }
        }
        if (!thoseObjects.isEmpty()) {
            return false;
        }
        List<GroupStatement> thoseGroups = thatObject.getGroups();
        for (GroupStatement thisGroup : getGroups()) {
            boolean foundMatch = false;
            for (GroupStatement thatGroup : thoseGroups) {
                if ((foundMatch = thisGroup.equals(thatGroup))) {
                    thoseGroups.remove(thatGroup);
                    break;
                }
            }
            if (foundMatch == false) {
                return false;
            }
        }
        if (!thoseGroups.isEmpty()) {
            return false;
        }
        return true;
    }

    public int hashcode() {
        int hash = 7;

        hash = 31 * hash
                + (null == getAttributes() ? 0 : getAttributes().hashCode());
        hash = 31 * hash
                + (null == getPointers() ? 0 : getPointers().hashCode());
        hash = 31 * hash + (null == getObjects() ? 0 : getObjects().hashCode());
        hash = 31 * hash + (null == getGroups() ? 0 : getGroups().hashCode());

        return hash;
    }

}
