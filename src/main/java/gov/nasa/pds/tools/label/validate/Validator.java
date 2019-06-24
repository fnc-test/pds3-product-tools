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

package gov.nasa.pds.tools.label.validate;

import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.dict.parser.DictionaryParser;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.GroupStatement;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.tools.label.Statement;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class Validator {
  private Map<String, Boolean> properties = new HashMap<String, Boolean>();
  private List<LabelValidator> otherValidators = new ArrayList<LabelValidator>();

  public static final String LINE_LEN_CHECK = "gov.nasa.pds.tools.label.validate.LineCheck"; //$NON-NLS-1$
  public static final String DUP_ID_CHECK = "gov.nasa.pds.tools.label.validate.DuplicateIdentifierValidator"; //$NON-NLS-1$
  public static final String CAT_NAME_CHECK = "gov.nasa.pds.tools.label.validate.CatalogNameValidator"; //$NON-NLS-1$
  public static final String FILE_CHAR_CHECK = "gov.nasa.pds.tools.label.validate.FileCharacteristicValidator"; //$NON-NLS-1$
  public static final String DICTIONARY_CHECK = "gov.nasa.pds.tools.label.validate.DictionaryValidator"; //$NON-NLS-1$
  public static final String TABLE_CHECK = "gov.nasa.pds.tools.label.validate.TableValidator"; //$NON-NLS-1$

  private static Dictionary DEFAULT_DICTIONARY;

  public Validator() {
    this.properties.put(LINE_LEN_CHECK, true);
    this.properties.put(DICTIONARY_CHECK, true);
  }

  // take a label instance and add problems to it
  // Accumulate different types of tests here

  // TODO: determine what other tests to add
  // encompassed by higher level things in the

  protected static synchronized void initDefaultDictionary() {
    if (DEFAULT_DICTIONARY == null) {
      InputStream dictionaryStream = Validator.class.getClassLoader()
          .getResourceAsStream("pdsdd.full"); //$NON-NLS-1$
      try {
        try {
          DEFAULT_DICTIONARY = DictionaryParser.parse(
              dictionaryStream,
              new Dictionary(Validator.class.getClassLoader()
                  .getResource("pdsdd.full") //$NON-NLS-1$
                  .toURI()), false, false);
        } catch (URISyntaxException e) {
          e.printStackTrace();
        }
      } catch (LabelParserException e) {
        // TODO: consider what to do with parse issues, note that won't
        // be thrown in figure, just added to container
        e.printStackTrace();
      } catch (IOException e) {
        // shouldn't happen
        e.printStackTrace();
      }
    }
  }

  public void validate(final Label label) {
    if (performsDictionaryCheck()) {
      initDefaultDictionary();
    }
    validate(label, DEFAULT_DICTIONARY);
  }

  public void validate(final Label label, final Dictionary dictionary) {
    if (label == null) {
      // TODO: report an error?
      return;
    }

    if (performsLineLengthCheck()) {
      label.checkLineLengths();
    }

    if (performsDictionaryCheck()) {
      List<Statement> statements = label.getStatements();
      Collections.sort(statements);
      for (Iterator<Statement> i = statements.iterator(); i.hasNext();) {
        Statement statement = i.next();
        if (statement instanceof AttributeStatement) {
          try {
            ElementValidator.validate((AttributeStatement) statement, label,
                dictionary);
          } catch (LabelParserException lpe) {
            label.addProblem(lpe);
          }
        } else if (statement instanceof ObjectStatement) {
          try {
            ObjectValidator.validate((ObjectStatement) statement, dictionary,
                label);
          } catch (LabelParserException lpe) {
            label.addProblem(lpe);
          }
        } else if (statement instanceof GroupStatement) {
          try {
            GroupValidator.validate((GroupStatement) statement, dictionary,
                label);
          } catch (LabelParserException lpe) {
            label.addProblem(lpe);
          }
        }
      }
    }

    if (performsCatalogNameCheck()) {
      LabelValidator cnv = new CatalogNameValidator();
      cnv.validate(label);
    }

    if (performsDuplicateIdCheck()) {
      LabelValidator div = new DuplicateIdentifierValidator();
      div.validate(label);
    }

    if (performsDuplicateIdCheck()) {
      // TODO: Integrate back file characteristic check if needed
    }

    if (performsTableCheck()) {
      LabelValidator tv = new TableValidator();
      tv.validate(label);
    }

    // Perform any additional checks that were loaded
    for (LabelValidator ov : otherValidators) {
      ov.validate(label);
    }
  }

  public boolean getProperty(String property) {
    return this.properties.containsKey(property) ? this.properties
        .get(property) : false;
  }

  public void setProperty(String property, Boolean flag) {
    this.properties.put(property, flag);
  }

  public void setProperties(Map<String, Boolean> properties) {
    this.properties = properties;
  }

  public void setLineLengthCheck(Boolean flag) {
    setProperty(LINE_LEN_CHECK, flag);
  }

  public boolean performsLineLengthCheck() {
    return getProperty(LINE_LEN_CHECK);
  }

  public void setDictionaryCheck(Boolean flag) {
    setProperty(DICTIONARY_CHECK, flag);
  }

  public boolean performsDictionaryCheck() {
    return getProperty(DICTIONARY_CHECK);
  }

  public void setCatalogNameCheck(Boolean flag) {
    setProperty(CAT_NAME_CHECK, flag);
  }

  public boolean performsCatalogNameCheck() {
    return getProperty(CAT_NAME_CHECK);
  }

  public void setDuplicateIdCheck(Boolean flag) {
    setProperty(DUP_ID_CHECK, flag);
  }

  public boolean performsDuplicateIdCheck() {
    return getProperty(DUP_ID_CHECK);
  }

  public void setFileCharacteristicCheck(Boolean flag) {
    setProperty(FILE_CHAR_CHECK, flag);
  }

  public boolean performsCharacteristicCheck() {
    return getProperty(FILE_CHAR_CHECK);
  }

  public void setTableCheck(Boolean flag) {
    setProperty(TABLE_CHECK, flag);
  }

  public boolean performsTableCheck() {
    return getProperty(TABLE_CHECK);
  }

  public void addValidator(LabelValidator validator) {
    otherValidators.add(validator);
  }
}
