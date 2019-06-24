// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// • Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// • Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// • Neither the name of Caltech nor its operating division, the Jet Propulsion
// Laboratory, nor the names of its contributors may be used to endorse or
// promote products derived from this software without specific prior written
// permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package gov.nasa.pds.tools.label;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import gov.nasa.pds.tools.dict.DictIdentifier;
import gov.nasa.pds.tools.dict.parser.DictIDFactory;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class GroupStatement extends Statement {

  private LinkedHashMap<DictIdentifier, List<Statement>> statements;

  /**
   * Constructs an empty group statement
   * 
   * @param lineNumber
   *          at which this statement occurs
   * @param identifier
   *          of the statement
   * @param sourceLabel
   *          in which this statement occurs
   */
  public GroupStatement(final Label sourceLabel, int lineNumber,
      String identifier) {
    this(sourceLabel, lineNumber, identifier,
        new LinkedHashMap<DictIdentifier, List<Statement>>());
  }

  /**
   * Constructs a group statement that contains the given statements
   * 
   * @param lineNumber
   *          at which this statement occurs
   * @param identifier
   *          of the statement
   * @param statements
   *          contained within this group statement
   */
  public GroupStatement(final Label sourceLabel, int lineNumber,
      String identifier,
      LinkedHashMap<DictIdentifier, List<Statement>> statements) {
    super(sourceLabel, lineNumber, DictIDFactory.createGroupDefId(identifier));
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
      for (Statement stmt : this.statements
          .get(DictIDFactory.createGroupDefId(id))) {
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

  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if ((object == null) || (object.getClass() != this.getClass())) {
      return false;
    }

    GroupStatement thatGroup = (GroupStatement) object;
    List<AttributeStatement> thoseAttributes = thatGroup.getAttributes();

    for (AttributeStatement thisAttribute : getAttributes()) {
      AttributeStatement thatAttribute = thatGroup
          .getAttribute(thisAttribute.getIdentifier().getId());
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
    return true;
  }

  public int hashcode() {
    int hash = 7;

    hash = 31 * hash
        + (null == getAttributes() ? 0 : getAttributes().hashCode());

    return hash;
  }

}
