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
public class GroupStatement extends Statement {

    private Map<DictIdentifier, List<Statement>> statements;

    /**
     * Constructs an empty group statement
     * 
     * @param lineNumber
     *            at which this statement occurs
     * @param identifier
     *            of the statement
     * @param sourceLabel
     *            in which this statement occurs
     */
    public GroupStatement(final Label sourceLabel, int lineNumber,
            String identifier) {
        this(sourceLabel, lineNumber, identifier,
                new HashMap<DictIdentifier, List<Statement>>());
    }

    /**
     * Constructs a group statement that contains the given statements
     * 
     * @param lineNumber
     *            at which this statement occurs
     * @param identifier
     *            of the statement
     * @param statements
     *            contained within this group statement
     */
    public GroupStatement(final Label sourceLabel, int lineNumber,
            String identifier, Map<DictIdentifier, List<Statement>> statements) {
        super(sourceLabel, lineNumber, DictIDFactory
                .createGroupDefId(identifier));
        this.statements = statements;
    }

    /**
     * Retrieves the named attribute.
     * 
     * @param id
     * @return The named AttributeStatement or null if not found.
     */
    public AttributeStatement getAttribute(String id) {
        if (this.statements.get(DictIDFactory.createGroupDefId(id)) != null) {
            for (Statement stmt : this.statements.get(DictIDFactory
                    .createGroupDefId(id))) {
                if (stmt instanceof AttributeStatement)
                    return (AttributeStatement) stmt;
            }
        }
        return null;
    }

    /**
     * Retrieves the attributes of this group.
     * 
     * @return The list of AttributeStatment nested within this group
     */
    public List<AttributeStatement> getAttributes() {
        List<AttributeStatement> attributes = new ArrayList<AttributeStatement>();
        for (List<Statement> statementList : this.statements.values()) {
            for (Statement statement : statementList) {
                if (statement instanceof AttributeStatement)
                    attributes.add((AttributeStatement) statement);
            }
        }
        return attributes;
    }

    public void addStatement(Statement statement) {
        List<Statement> stmnts = this.statements.get(statement.getIdentifier());
        if (stmnts == null) {
            stmnts = new ArrayList<Statement>();
            this.statements.put(statement.getIdentifier(), stmnts);
        }
        if (statement instanceof IncludePointer) {
            stmnts.add(statement);
            for (Statement stmt : ((IncludePointer) statement).getStatements()) {
                addStatement(stmt);
            }
        } else if (statement instanceof PointerStatement
                || statement instanceof AttributeStatement) {
            stmnts.add(statement);
        }
        // else TODO Throw error
    }

    public boolean hasAttribute(String id) {
        if (getAttribute(id) == null) {
            return false;
        }
        return true;
    }

    public List<Statement> getStatements() {
        List<Statement> statementList = new ArrayList<Statement>();
        for (List<Statement> stmts : this.statements.values())
            statementList.addAll(stmts);
        return statementList;
    }
}
