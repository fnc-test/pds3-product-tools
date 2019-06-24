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

package gov.nasa.pds.tools;

import gov.nasa.arc.pds.tools.util.StrUtils;
import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.Statement;

import java.io.File;
import java.net.URI;

/**
 * This class captures any exceptions that arise during label parsing. If
 * possible the exception retains the context in which it occurred.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class LabelParserException extends Exception {

  private static final long serialVersionUID = -1;

  private final Integer lineNumber;

  private final Integer column;

  private final ProblemType type;

  private final Object[] arguments;

  private final String key;

  private final Statement statement;

  protected final URI sourceURI;

  protected final File sourceFile;

  /**
   * Use of this constructor is encouraged where possible as it retains the
   * context of the exception.
   * 
   * @param statement
   *          in which the exception occurred
   * @param column
   *          within the file where the exception started
   * @param key
   *          used to quickly associate lookup or filter this type of exception
   * @param type
   *          classification of the problem
   * @param arguments
   *          to be used in generating problem messages
   * 
   */
  public LabelParserException(final Statement statement, final Integer column,
      final String key, final ProblemType type, final Object... arguments) {
    super(key);
    this.sourceFile = statement.getSourceFile();
    this.sourceURI = statement.getSourceURI();
    this.lineNumber = statement.getLineNumber();
    this.column = column;
    this.type = type;
    this.key = key;
    this.arguments = arguments;
    this.statement = statement;
  }

  /**
   * Use this constructor for exceptions not associated to a specific file such
   * as not being able to parse due to missing file or bad URL
   * 
   * @param key
   *          used to quickly associate lookup or filter this type of exception
   * @param type
   *          classification of the problem
   * @param arguments
   *          to be used in generating problem messages
   */
  public LabelParserException(final String key, final ProblemType type,
      final Object... arguments) {
    super(key);
    this.sourceFile = null;
    this.sourceURI = null;
    this.lineNumber = null;
    this.column = null;
    this.type = type;
    this.key = key;
    this.arguments = arguments;
    this.statement = null;
  }

  /**
   * Use this constructor for exceptions that are associated with a specific
   * file but not on a given line (e.g. missing statements)
   * 
   * @param label
   *          within which the exception occurred
   * @param lineNumber
   *          within the file where the exception started
   * @param column
   *          within the file where the exception started
   * @param key
   *          used to quickly associate lookup or filter this type of exception
   * @param type
   *          classification of the problem
   * @param arguments
   *          to be used in generating problem messages
   */
  public LabelParserException(final Label label, final Integer lineNumber,
      final Integer column, final String key, final ProblemType type,
      final Object... arguments) {
    super(key);
    this.sourceFile = label.getLabelFile();
    this.sourceURI = label.getLabelURI();
    this.lineNumber = lineNumber;
    this.column = column;
    this.type = type;
    this.key = key;
    this.arguments = arguments;
    this.statement = null;
  }

  /**
   * Use this constructor for exceptions to be reported about a file that can be
   * traced back to a line and column
   * 
   * @param sourceFile
   *          within which the exception occurred
   * @param lineNumber
   *          within the file where the exception started
   * @param column
   *          within the file where the exception started
   * @param key
   *          used to quickly associate lookup or filter this type of exception
   * @param type
   *          classification of the problem
   * @param arguments
   *          to be used in generating problem messages
   */
  public LabelParserException(final File sourceFile, final Integer lineNumber,
      final Integer column, final String key, final ProblemType type,
      final Object... arguments) {
    super(key);
    this.sourceFile = sourceFile;
    this.sourceURI = null;
    this.lineNumber = lineNumber;
    this.column = column;
    this.type = type;
    this.key = key;
    this.arguments = arguments;
    this.statement = null;
  }

  /**
   * Use this constructor for exceptions to be reported about a URI that can be
   * traced back to a line and column
   * 
   * @param sourceURI
   *          within which the exception occurred
   * @param lineNumber
   *          within the file where the exception started
   * @param column
   *          within the file where the exception started
   * @param key
   *          used to quickly associate lookup or filter this type of exception
   * @param type
   *          classification of the problem
   * @param arguments
   *          to be used in generating problem messages
   */
  public LabelParserException(final URI sourceURI, final Integer lineNumber,
      final Integer column, final String key, final ProblemType type,
      final Object... arguments) {
    super(key);
    this.sourceFile = null;
    this.sourceURI = sourceURI;
    this.lineNumber = lineNumber;
    this.column = column;
    this.type = type;
    this.key = key;
    this.arguments = arguments;
    this.statement = null;
  }

  /**
   * 
   * @param sourceDictionary
   * @param lineNumber
   * @param column
   * @param key
   * @param type
   * @param arguments
   */
  // this should only be thrown from the creation of a dictionary, while it's
  // structurally possible to throw from ElementValidator, the definitions are
  // found from an already created dictionary so, presumably, each definition
  // will already have been checked for invalid type
  public LabelParserException(final Dictionary sourceDictionary,
      final Integer lineNumber, final Integer column, final String key,
      final ProblemType type, final Object... arguments) {
    super(key);
    this.sourceFile = sourceDictionary.getDictionaryFile();
    this.sourceURI = sourceDictionary.getDictionaryURI();
    this.lineNumber = lineNumber;
    this.column = column;
    this.type = type;
    this.key = key;
    this.arguments = arguments;
    this.statement = null;
  }

  /**
   * 
   * @param e
   * @param lineNumber
   * @param column
   * @param type
   */
  // to be used as little as possible
  public LabelParserException(final Exception e, final Integer lineNumber,
      final Integer column, final ProblemType type) {
    super(e);
    this.sourceFile = null;
    this.sourceURI = null;
    this.lineNumber = lineNumber;
    this.column = column;
    this.type = type;
    this.key = e.getMessage();
    this.arguments = null;
    this.statement = null;
  }

  /**
   * 
   * @return URI of the file in which the exception occurred
   */
  public URI getSourceURI() {
    return this.sourceURI;
  }

  /**
   * 
   * @return File in which the exception occurred
   */
  public File getSourceFile() {
    return this.sourceFile;
  }

  /**
   * 
   * @return Line number of the exception if applicable
   */
  public Integer getLineNumber() {
    return this.lineNumber;
  }

  /**
   * 
   * @return Column number of the exception if applicable
   */
  public Integer getColumn() {
    return this.column;
  }

  /**
   * 
   * @return classification of the problem
   */
  public ProblemType getType() {
    return this.type;
  }

  /**
   * 
   * @return arguments to be used in producing a message about this exception
   */
  public Object[] getArguments() {
    return this.arguments;
  }

  /**
   * 
   * @return key of this exception which is useful for filtering
   */
  public String getKey() {
    return this.key;
  }

  /**
   * 
   * @return statement in which the exception occurred.
   */
  public Statement getStatement() {
    return this.statement;
  }

  @SuppressWarnings("nls")
  @Override
  public String toString() {
    return "[" + this.type.toString() + " - \"" + this.key + "\" on line "
        + this.lineNumber + "] (" + StrUtils.toSeparatedString(this.arguments)
        + ")";
  }
}
