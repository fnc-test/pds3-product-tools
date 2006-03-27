// PARSER
header {
    package gov.nasa.jpl.pds.tools.label.parser;
}

class ODLParser extends Parser;
options {
    exportVocab = ODL;
    buildAST = true;
}

// a label is a series of one or more expressions
//      followed by an END
label
    : (expr)* (ending!)
    ;

ending
    : END
    ;

// an expression is an assignment like identifer = value
//      or an object block
expr
    : (assignment) | (object) | (group) | (attachment) | (comment)
    ;

// an object block
object
    : "OBJECT"^ ASSIGNMENT_OPERATOR! IDENT^
      (expr)*
      "END_OBJECT"!
      (endobject)*
    ;

endobject
    :
      ASSIGNMENT_OPERATOR! IDENT!
    ;

// a group block
group
    : "GROUP"^ ASSIGNMENT_OPERATOR! IDENT^
      (expr)*
      "END_GROUP"!
      (endgroup)*
    ;

endgroup
    :
      ASSIGNMENT_OPERATOR! IDENT!
    ;

attachment
    : (POINT_OPERATOR^) (assignment)
    ;

comment
    : (COMMENT^)
    ;

assignment
    : (IDENT) (ASSIGNMENT_OPERATOR^) (value)
    ;

// a value is an identifier or a string literal
value
    : (IDENT) (units)* | (QUOTED) (units)* | (SYMBOL) | (list) | (set)
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
    : (IDENT) (units)* | (QUOTED) (units)*
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
//          |   EOL
            |   ~('*'|'\n'|'\r')
      )*
      "*/"
    ;


// Whitespace - ignore it
WS
    : (   ' '
      |   '\t'
      |   '\f'
      |   EOL
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
      |   ~('\n'|'\r'|'"'|'\''|'\\')
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
    