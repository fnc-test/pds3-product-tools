//Copyright 2006, by the California Institute of 
//Technology. ALL RIGHTS RESERVED. United States Government 
//Sponsorship acknowledged. Any commercial use must be negotiated with 
//the Office of Technology Transfer at the California Institute of 
//Technology.
//
//This software may be subject to U.S. export control laws. By 
//accepting this software, the user agrees to comply with all 
//applicable U.S. export laws and regulations. User has the 
//responsibility to obtain export licenses, or other export authority 
//as may be required before exporting such information to foreign 
//countries or providing access to foreign persons.
//
// $Id$ 
//

header {
package gov.nasa.pds.tools.label.antlr;

import org.apache.log4j.Logger;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.CommentStatement;
import gov.nasa.pds.tools.label.DateTime;
import gov.nasa.pds.tools.label.GroupStatement;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.Numeric;
import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.tools.label.PointerStatement;
import gov.nasa.pds.tools.label.PointerStatementFactory;
import gov.nasa.pds.tools.label.Scalar;
import gov.nasa.pds.tools.label.Sequence;
import gov.nasa.pds.tools.label.Set;
import gov.nasa.pds.tools.label.Statement;
import gov.nasa.pds.tools.label.StructurePointer;
import gov.nasa.pds.tools.label.Symbol;
import gov.nasa.pds.tools.label.TextString;
import gov.nasa.pds.tools.label.Value;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.text.ParseException;
}

class ODLParser extends Parser;
options {
    exportVocab = ODL;
    k = 2;
}

tokens {
    // We need to use this in a predicate, so we need to give it a name instead
    // of just using it literally later.
    END = "END";
}

{
    private static Logger log = Logger.getLogger("gov.nasa.pds.tools.label.antlr.ODLParser"); 
    private static URL base = null;
    
    public void reportError(RecognitionException re) {
        log.error(re.toString());
    }
    
    public void reportError(RecognitionException re, String s) {
        log.error(re.toString());
    }
    
    public void reportError(String s) {
        log.error(s);
    }
    
    public void reportWarning(String s) {
        log.warn(s);
    }
    
    public void setBaseURL(URL base) {
        this.base = base;
    }
    
    /**
     * Return a string containing the names of a set of tokens.
     *
     * @param set a set of token types
     * @return a string containing the names of the tokens separated by spaces
     */
    public String getTokenNames(BitSet set) {
        int[] tokenTypes = set.toArray();
        TreeSet tokenNames = new TreeSet();
       
        // First put all the names into a set, so we don't get duplicates. 
        for (int i=0; i<tokenTypes.length; ++i) {
            String name = getTokenName(tokenTypes[i]);
            
            // Replace any double quotes with single quotes, so that the error messages
            // consistently use single quotes.
            tokenNames.add(name.replace('"', '\''));
        }
        
        // Now concatenate the names together, separated by spaces.
        StringBuffer result = new StringBuffer();
        for (Iterator it=tokenNames.iterator(); it.hasNext(); ) {
            String name = (String) it.next();
            if (result.length() > 0) {
                result.append(' ');
            }
            result.append(name);
        }
        
        return result.toString();
    }
}

// a label is a series of one or more expressions followed by an END
label returns [Label label = new Label();]
{Statement s = null;}
    : (
        s=statement {if (s != null) {label.addStatement(s);}}
      | 
        (~ END) => t:.
          {reportError("line " + t.getLine() + ":" + t.getColumn() + ": "
                       + "expecting one of: " + getTokenNames($FIRST(statement)));}
        (~ EOL)* EOL
      )* (END)?
    ;
    


// an expression is an assignment like identifer = value
//      or an object block
statement returns [Statement result = null]
{Statement s = null;}
    : s=simple_statement
        {result = s;}
    | s=group_statement
        {result = s;}
    | s=object_statement
        {result = s;}
    ;
    
simple_statement returns [Statement result = null]
{Statement s = null;}
    : /*empty statement*/ (c:COMMENT)? EOL
        {
           if (c == null) {result = null;}
           else {
              CommentStatement comment = new CommentStatement(c.getLine());
              comment.setComment(c.getText());
              result = comment;
           }
        }
    | s=assignment_statement (c1:COMMENT)? EOL
        {
           if (c1 != null) {
              CommentStatement comment = new CommentStatement(c1.getLine());
              comment.setComment(c1.getText());
              s.attachComment(comment);
           }
           result = s;
        }
    | s=pointer_statement (c2:COMMENT)? EOL
        {
           if (c2 != null) {
              CommentStatement comment = new CommentStatement(c2.getLine());
              comment.setComment(c2.getText());
              s.attachComment(comment);
           }
           result = s;
        }
    ;
    
// an object block
object_statement returns [ObjectStatement result = null]
{Statement s = null;}
    : "OBJECT" nl EQUALS nl id:IDENTIFIER (c:COMMENT)? EOL
      {
         result = new ObjectStatement(id.getLine(), id.getText());
         if (c != null) {
            CommentStatement comment = new CommentStatement(c.getLine());
            comment.setComment(c.getText());
            result.attachComment(comment);
         }
      }
      (s=statement {if (s != null) {result.addStatement(s);}})*
      "END_OBJECT" (EQUALS id2:IDENTIFIER)?
      (c2:COMMENT)? EOL
      {
         if (c2 != null) {
            CommentStatement comment = new CommentStatement(c2.getLine());
            comment.setComment(c.getText());
            result.attachComment(comment);
         }
      }
    ;

// a group block
group_statement returns [GroupStatement result = null]
{Statement s = null;}
    : "GROUP" nl EQUALS nl id:IDENTIFIER (c:COMMENT)? EOL
      {
         result = new GroupStatement(id.getLine(), id.getText());
         if (c != null) {
            CommentStatement comment = new CommentStatement(c.getLine());
            comment.setComment(c.getText());
            result.attachComment(comment);
         }
      }
      (s=simple_statement {if (s != null) {result.addStatement(s);}})*
      "END_GROUP" (EQUALS id2:IDENTIFIER)?
      (c3:COMMENT)? EOL
    ;

// a pointer
pointer_statement returns [PointerStatement result = null]
{AttributeStatement a = null;}
    : POINT_OPERATOR a=assignment_statement
      {
         try {
            result = PointerStatementFactory.newInstance(a.getLineNumber(), a.getIdentifier(), a.getValue(), base);
         } catch (MalformedURLException mue) {
            result = null;
            log.error("line " + a.getLineNumber() + ": " + mue.getMessage());
         }
         if (result != null && result instanceof StructurePointer && base != null) {
            StructurePointer sp = (StructurePointer) result;
            try {
               sp.loadReferencedStatements();
            } catch (gov.nasa.pds.tools.label.parser.ParseException pe) {
               log.error("line " + a.getLineNumber() + ": " + pe.getMessage());
            } catch (IOException ioe) {
               log.error("line " + a.getLineNumber() + ": " + ioe.getMessage());
            } 
         }
      }
    ;

// an attribute assignment
assignment_statement returns [AttributeStatement result = null]
{AttributeStatement a = null; Value v = null;}
    : (eid:ELEMENT_IDENT|id:IDENTIFIER) nl EQUALS nl v=value
      { 
        if (eid != null) 
           result = new AttributeStatement(eid.getLine(), eid.getText(), v);
        else 
           result = new AttributeStatement(id.getLine(), id.getText(), v);
      }
    ;

// a value is a scalar, sequence, or set
value returns [Value result = null]
{Value v = null;}
    : v=scalar_value
        {result = v;} 
    | v=sequence_value 
        {result = v;}
    | v=set_value
        {result = v;}
    ;

// a scalar is a numeric, date time, text string, or symbol
scalar_value returns [Scalar result = null]
{Scalar s = null;}
    : s=numeric_value
        {result = s;}
    | s=date_time_value
        {result = s;}
    | s=text_string_value
        {result = s;}
    | s=symbol_value
        {result = s;}
    ;
    
// a numeric is an integer, based integer, or real followed by optional units
numeric_value returns [Numeric result = null]
    : i:INTEGER (u:UNITS)?
        {
          result = new Numeric(i.getText());
          if (u != null) {result.setUnits(u.getText());}
        }
    | b:BASED_INTEGER (u1:UNITS)?
        {
          result = new Numeric(b.getText());
          if (u1 != null) {result.setUnits(u1.getText());}
        }
    | r:REAL (u2:UNITS)?
        {
          result = new Numeric(r.getText());
          if (u2 != null) {result.setUnits(u2.getText());}
        }
    ;

// a text string is quoted with double quotes
text_string_value returns [TextString result = null]
    : q:QUOTED
        {result = new TextString(q.getText());}
    ;

// a date time formatted to PDS specification 
date_time_value returns [DateTime result = null]
    : d:DATE
        {
           try {
              result = new DateTime(d.getText());
           } catch (ParseException pe) {log.error("line " + d.getLine() + ": " + pe.getMessage());}
        }
    | t:TIME
        {
           try {
              result = new DateTime(t.getText());
           } catch (ParseException pe) {log.error("line " + t.getLine() + ": " + pe.getMessage());}
        }
    | dt:DATETIME
        {
           try {
              result = new DateTime(dt.getText());
           } catch (ParseException pe) {log.error("line " + dt.getLine() + ": " + pe.getMessage());}
        }
    ;
    
// a symbol  
symbol_value returns [Symbol result = null]
    : id:IDENTIFIER
        {result = new Symbol(id.getText());}
    | qs:SYMBOL
        {result = new Symbol(qs.getText());}
    ;

sequence_value returns [Sequence result = null]
{Sequence s = null;}
    : (SEQUENCE_OPENING nl SEQUENCE_OPENING) => s=sequence_2d 
        {result = s;}
    | s=sequence_1d
        {result = s;}
    ;
    
sequence_1d returns [Sequence result = null]
{Sequence s = null;}
    : SEQUENCE_OPENING nl s=scalar_list SEQUENCE_CLOSING
        {result = s;}
    ;
    
scalar_list returns [Sequence result=new Sequence()]
{Scalar s = null; Scalar s2 = null;}
    : /*empty*/
    | s=scalar_value {result.add(s);} nl
        ((LIST_SEPARATOR nl)? s2=scalar_value nl {result.add(s2);})*
    ;

sequence_2d returns [Sequence result = null]
{Sequence s = null; Sequence s2 = null;}
    : SEQUENCE_OPENING nl s=sequence_list SEQUENCE_CLOSING
        {result = s;}
    ;

sequence_list returns [Sequence result = new Sequence()]
{Sequence s = null; Sequence s2 = null;}
    : /*empty*/
    | s=sequence_1d {result.add(s);} nl
        ((LIST_SEPARATOR nl)? s2=sequence_1d nl {result.add(s2);})*
    ;

set_value returns [Set result = null;]
{Set s = null;}
    : SET_OPENING nl s=item_list {result=s;} SET_CLOSING
    ;
    
item_list returns [Set result = new Set()]
{Scalar s = null; Scalar s2 = null;}
    : /*empty*/
    | s=scalar_value {result.add(s);} nl
        ((LIST_SEPARATOR nl)? s2=scalar_value nl {result.add(s2);})*
    ;

/* An arbitrary number of newlines, used within sets and sequences, which
 * can span multiple lines.
 */
nl
    : (EOL)*
    ;

// LEXER
class ODLLexer extends Lexer;

options {
    exportVocab = ODL;
    charVocabulary = '\0'..'\377';
    testLiterals = false;   // don't automatically test for literals
    k = 2;                  // 2 characters of lookahead
    //caseSensitive = false;
    caseSensitiveLiterals = false;
    filter = IGNORE;
}

{
    private static Logger log = Logger.getLogger("gov.nasa.pds.tools.label.antlr.ODLLexer"); 
    
    public void reportError(RecognitionException re) {
        log.error(re.toString());
    }

    public void reportError(String s) {
        log.error(s);
    }
    
    public void reportWarning(String s) {
        log.warn(s);
    }
}

// operators
SET_OPENING
options {paraphrase = "'{'";}
    : '{' ;
    
SET_CLOSING
options {paraphrase = "'}'";}
    : '}' ;
    
SEQUENCE_OPENING
options {paraphrase = "'('";}
    : '(' ;
    
SEQUENCE_CLOSING
options {paraphrase = "')'";}
    : ')' ;
    
LIST_SEPARATOR
options {paraphrase = "','";}
    : ',' ;
    
POINT_OPERATOR
options {paraphrase = "'^'";}
    : '^' ;
    
EQUALS
options {paraphrase = "'='";}
    : '=' ;

// Integer


// Multiple-line comments
COMMENT
options {paraphrase = "comment";}
    : "/*"
      (
          options {
                generateAmbigWarnings=false;
          }
          :
            {   LA(2)!='/' }? '*'
            |   EOL
            |   ~('*'|'\n'|'\r')
      )*
      "*/"
    ;


// Whitespace - ignore it
WS
    : (   ' '
      |   '\t'
      |   '\f'
      )
      { _ttype = Token.SKIP; }
    ;

// an identifier.  Note that testLiterals is set to true!  This means
// that after we match the rule, we look in the literals table to see
// if it's a literal or really an identifer
protected
IDENTIFIER
    options {testLiterals=true; paraphrase="identifier";}
    : l:LETTER (LETTER|DIGIT|'_')*
    ;

ELEMENT_IDENT 
    options {testLiterals=true; paraphrase="identifier";}
    : (IDENTIFIER ':') => IDENTIFIER ':' IDENTIFIER {$setType(ELEMENT_IDENT);}
    | id:IDENTIFIER
        {$setType(IDENTIFIER);}
    ;

UNITS
    options {testLiterals=true; paraphrase="units";}
    : '<' UNITS_FACTOR (MULT_OP UNITS_FACTOR)* '>'
    ;
 
protected   
UNITS_FACTOR
    : IDENTIFIER (EXP_OP INTEGER)?
    ;
   
protected 
MULT_OP
    : ('*'|'/')
    ;
   
protected 
EXP_OP
    : "**"
    ;

// any special characters that can pop up in value strings, including
// forward slash or dots seen in filenames, hash sign seen in bit masks,
// underscores seen in keywords and values, dashes and colons seen in
// dates, plus or minus signs in numbers.
protected
SPECIALCHAR
    : ('_' | '$' | '#' | '.' | '-' | ':' | '+' | '*')
    ;

NUMBER_OR_DATETIME
    : (DIGITS '#') => BASED_INTEGER {$setType(BASED_INTEGER);}
    | (DIGITS '-') => DATETIME {$setType(DATETIME);}
    | (DIGITS ':') => TIME {$setType(TIME);}
    | (SIGN DIGITS '.') => REAL {$setType(REAL);}
    | (DIGITS '.') => REAL {$setType(REAL);}
    | (SIGN '.') => REAL {$setType(REAL);}
    | ('.' DIGIT) => REAL {$setType(REAL);}
    | INTEGER {$setType(INTEGER);}
    ;

// integer positive or negative no commas
protected
INTEGER
    : (SIGN)? DIGITS
    ;
    
protected
SIGN
    : '+' | '-'
    ;

protected
DIGITS
    : (DIGIT)+
    ;

// based integer
protected
BASED_INTEGER
options {paraphrase = "integer";}
    : DIGITS '#' ('+'|'-')? (EXTENDED_DIGIT)+ '#'
    ;
    
// real numbers
protected
REAL
options {paraphrase = "real";}
    : (SIGN)? (DIGITS)? '.' (DIGITS)? (('E'|'e') INTEGER)?
    ;

// digits are only numbers 
protected
DIGIT
    : ('0'..'9')
    ;
    
// extended digit is a digit or letter
protected
EXTENDED_DIGIT
    : DIGIT | LETTER
    ;
   
protected
LETTER
    : ('a'..'z'|'A'..'Z')
    ;
  
// Dates and times
protected
DATETIME
options {paraphrase = "date-time";}
    : (YEAR '-' MONTH '-' DAY 'T') => YEAR '-' MONTH '-' DAY 'T' TIME {$setType(DATETIME);}
    | (YEAR '-' DOY 'T') => YEAR '-' DOY 'T' TIME {$setType(DATETIME);}
    | DATE {$setType(DATE);}
    ;

protected
DATE
    : (YEAR '-' DOY) => YEAR '-' DOY {$setType(DATE);}
    | YEAR ('-' MONTH ('-' DAY)?)? {$setType(DATE);}
    ;
    
protected
YEAR
    : DIGIT DIGIT DIGIT DIGIT
    ;
 
protected
DOY
    : DIGIT DIGIT DIGIT
    ;
    
protected
MONTH
    : DIGIT DIGIT  
    ;
    
protected 
DAY
    : DIGIT DIGIT
    ;
    
protected
TIME
    : HOUR (':' MINUTE (':' SECOND ('.' FRACTION)?)?)? ('Z')?
    ;

protected
HOUR
    : DIGIT DIGIT
    ;
   
protected
MINUTE
    : DIGIT DIGIT
    ;
    
protected
SECOND
    : DIGIT DIGIT
    ;
    
protected
FRACTION
    : DIGIT (DIGIT (DIGIT)?)?
    ;
    
// string literals
QUOTED
    : '"' (EOL | (~('"'|'\r'|'\n')))* '"'
    ;

SYMBOL
    : '\'' (~('\''|'\\'|'\r'|'\n'))* '\''
    ;

EOL
options {paraphrase = "end-of-line";}
    : ('\r' (options {greedy=true;}: '\n')?  // DOS, old Macintosh
      |   '\n'                                 // Unix, Mac OS X
      )
        { newline(); }
    ;

protected
IGNORE
	: c:.
		{int column = (getColumn() > 2) ? getColumn()-2 : 1;
		 reportError("line " + getLine() + ":" + column + ": "
					+ "ignoring unexpected character: '" + $getText + "'");}
	;

