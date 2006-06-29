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
    import gov.nasa.pds.tools.label.Label;
    import gov.nasa.pds.tools.label.ObjectStatement;
    import gov.nasa.pds.tools.label.AttributeStatement;
    import gov.nasa.pds.tools.label.GroupStatement;
    import gov.nasa.pds.tools.label.PointerStatement;
    import gov.nasa.pds.tools.label.CommentStatement;
    import gov.nasa.pds.tools.label.Statement;
}

class ODLParser extends Parser;
options {
    exportVocab = ODL;
}

dictionary
    : (label)*
    ;

// a label is a series of one or more expressions
//      followed by an END
label returns [Label label = new Label();}
    : ( s=statement {if (s != null) {label.addStatement(s);}} )* "END"?
    ;

// an expression is an assignment like identifer = value
//      or an object block
statement returns [Statement result]
{Statement s = null;}
    : s=group_statement
        {result = s;}
    | s=nongroup_statement
        {result = s;}
    ;
    
nongroup_statement returns [Statement result]
{Statement s= null;}
    : s=simple_statement
        {result = s;}
    | s=object_statemnt
        {result = s;}
    ;

simple_statement returns [Statement result]
{Statement s = null;}
    : /*empty statement*/ (c:COMMENT)? EOL
        {
           if (c == null) {result = null;}
           else {
              CommentStatement comment = new CommentStatement(c.getLine());
              c.setComment(c.getText());
              result = comment;
           }
        }
    | s=assignment_statement (c:COMMENT)? EOL
        {
           if (c != null) {
              CommentStatement comment = new CommentStatement(c.getLine());
              comment.setComment(c.getText());
              s.attachComment(comment);
           }
           result = s;
        }
    | s=pointer_statement (c:COMMENT)? EOL
        {
           if (c != null) {
              CommentStatement comment = new CommentStatement(c.getLine());
              comment.setComment(c.getText());
              s.attachComment(comment);
           }
           result = s;
        }
    ;
    
// an object block
object_statement returns [ObjectStatement result = null]
{Statement s = null;}
    : "OBJECT" EQUALS id:IDENT (c:COMMENT)? EOL
      {
         result = new ObjectStatement(id.getLine(), id.getText());
         if (c != null) {
            CommentStatement comment = new CommentStatement(c.getLine());
            comment.setComment(c.getText());
            result.attachComment(comment);
         }
      }
      (s=nogroup_statement {if (s != null) {result.addStatement(s);}})*
      "END_OBJECT" (EQUALS id2:IDENT)?
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
    : "GROUP" EQUALS  id:IDENT (c:COMMENT)? EOL
      {
         result = new GroupStatement(id.getLine(), id.getText());
         if (c != null) {
            CommentStatement comment = new CommentStatement(c.getLine());
            comment.setComment(c.getText());
            result.attachComment(comment);
         }
      }
      (s=simple_statement {if (s != null) {result.addStatement(s)}})*
      "END_GROUP" (EQUALS id2:IDENT)?
      (c3:COMMENT)? EOL
    ;

pointer_statement
    : (POINT_OPERATOR) (assignment_statement)
    ;

assignment_statement
    : (IDENT) EQUALS (value)
    ;

// a value is an identifier or a string literal
value
    : (IDENT) (units)* | (QUOTED) | (SYMBOL) | (list) | (set)
    ;

units
    : (UNITS)
    ;

list
    : (SEQUENCE_OPENING^) (listelements)* (SEQUENCE_CLOSING!)
    ;

set
    : (SET_OPENING^) (listelements)* (SET_CLOSING!)
    ;

listelements
    : (listitem | list) (LIST_SEPERATOR!)*
    ;

listitem
    : (IDENT) (units)* | (QUOTED) | (SYMBOL)
    ;


// LEXER
class ODLLexer extends Lexer;

options {
    exportVocab = ODL;
    charVocabulary = '\0'..'\377';
    testLiterals = false;   // don't automatically test for literals
    k = 6;                  // four characters of lookahead
    caseSensitive = false;
    caseSensitiveLiterals = false;
}


// literal keywords
tokens {
    END       = "end";
}



// operators
SET_OPENING : '{' ;
SET_CLOSING : '}' ;
SEQUENCE_OPENING : '(' ;
SEQUENCE_CLOSING : ')' ;
LIST_SEPERATOR : ',' ;
POINT_OPERATOR : '^' ;
ASSIGNMENT_OPERATOR : '=' ;


// Multiple-line comments
COMMENT
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
IDENT
    options {testLiterals=true;}
    : ({ LA(2) != '*' }? '/' | ('a'..'z'|'0'..'9'|SPECIALCHAR))
      ('a'..'z'|'0'..'9'|'/'|SPECIALCHAR)*
    ;

UNITS
    options {testLiterals=true;}
    : '<'!
      ('a'..'z'|'0'..'9'|SPECIALCHAR)
      ('a'..'z'|'0'..'9'|SPECIALCHAR)*
      '>'!
    ;

// any special characters that can pop up in value strings, including
// forward slash or dots seen in filenames, hash sign seen in bit masks,
// underscores seen in keywords and values, dashes and colons seen in
// dates, plus or minus signs in numbers.
SPECIALCHAR
    : ('_' | '$' | '#' | '.' | '-' | ':' | '+' | '*')
    ;

// string literals
QUOTED
    : '"'!
      (
          EOL
      |   ~('\n'|'\r'|'\''|'\\') 
      |   '\'' (~('\''|'\n'|'\r'))* '\''
      )*
      '"'!
    ;

SYMBOL
    : '\''! (~('\''|'\\'|'\r'|'\n'))* '\''!
    ;

EOL
    :
          (   "\r\n"      // DOS
          |   '\r'        // Macintosh
          |   '\n'        // Unix
          )   { newline(); }
    ;
    
