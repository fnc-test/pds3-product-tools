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

import gov.nasa.arc.pds.tools.util.StrUtils;
import gov.nasa.pds.tools.dict.Definition;
import gov.nasa.pds.tools.dict.DictIdentifier;
import gov.nasa.pds.tools.dict.ElementDefinition;
import gov.nasa.pds.tools.dict.GroupDefinition;
import gov.nasa.pds.tools.dict.NullDefinition;
import gov.nasa.pds.tools.dict.ObjectDefinition;

import java.io.File;
import java.net.URI;

import org.antlr.runtime.Token;

/**
 * This class represents a statement in a PDS label.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public abstract class Statement implements Comparable<Object> {

  protected final int lineNumber;

  protected final DictIdentifier identifier;

  protected final URI sourceURI;

  protected final File sourceFile;

  protected final Label label;

  protected CommentStatement comment;

  public Statement(final Label sourceLabel, final int lineNumber,
      final DictIdentifier identifier) {
    this.label = sourceLabel;
    this.lineNumber = lineNumber;
    this.identifier = identifier;
    if (sourceLabel == null) {
      this.sourceURI = null;
      this.sourceFile = null;
    } else {
      final URI uri = sourceLabel.getLabelURI();
      if (uri != null) {
        this.sourceURI = uri;
        this.sourceFile = null;
      } else {
        this.sourceURI = null;
        this.sourceFile = sourceLabel.getLabelFile();
      }
    }
  }

  /**
   * Retrieves the label associated with this statement
   * 
   * @return label
   */
  public Label getLabel() {
    return this.label;
  }

  /**
   * Retrieves the line for this statement
   * 
   * @return The line on which the statement starts
   */
  public int getLineNumber() {
    return this.lineNumber;
  }

  /**
   * Retrieves the identifier for the statement
   * 
   * @return unique identifier
   */
  public DictIdentifier getIdentifier() {
    return this.identifier;
  }

  public URI getSourceURI() {
    return this.sourceURI;
  }

  public File getSourceFile() {
    return this.sourceFile;
  }

  public String getSourceString() {
    if (this.sourceFile != null) {
      return this.sourceFile.toString();
    }
    return this.sourceURI.toString();
  }

  // TODO: determine if multiple comments allowed for a single statement
  public void attachComment(final Token commentToken) {
    if (commentToken != null) {
      this.comment = new CommentStatement(this.label, commentToken,
          this.identifier.getId());
    }
  }

  /**
   * Returns the comment associated with statement
   * 
   * @return comment
   */
  public CommentStatement getComment() {
    return this.comment;
  }

  public int compareTo(Object o) {
    return this.getLineNumber() - ((Statement) o).getLineNumber();
  }

  protected void validateType(final DictIdentifier id,
      final Class<? extends Definition> clazz) {
    if (!id.getType().equals(clazz)) {
      throw new RuntimeException("ID type mismatch! Expected \"" //$NON-NLS-1$
          + clazz.toString() + "\" but got \"" //$NON-NLS-1$
          + id.getType().toString() + "\"."); //$NON-NLS-1$
    }
  }

  protected void validateObjectType(final DictIdentifier id) {
    validateType(id, ObjectDefinition.class);
  }

  protected void validateGroupType(final DictIdentifier id) {
    validateType(id, GroupDefinition.class);
  }

  protected void validateElementType(final DictIdentifier id) {
    validateType(id, ElementDefinition.class);
  }

  protected void validateCommentType(final DictIdentifier id) {
    validateType(id, NullDefinition.class);
  }

  protected void validatePointerType(final DictIdentifier id) {
    validateType(id, NullDefinition.class);
  }

  @SuppressWarnings("nls")
  @Override
  public String toString() {
    return this.identifier.toString() + " "
        + StrUtils.getNonNull(this.sourceFile, this.sourceURI);
  }
}
