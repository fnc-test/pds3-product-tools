grammar ODL;
options { 
    backtrack = false;
    memoize = false;
}
tokens {
    // We need to use these in predicates, so we need to give them names instead
    // of just using them literally later.
    END = 'END';
    END_GROUP = 'END_GROUP';
    END_OBJECT = 'END_OBJECT';
}

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

@parser::header {
package gov.nasa.pds.tools.label.antlr;

import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.CommentStatement;
import gov.nasa.pds.tools.label.DateTime;
import gov.nasa.pds.tools.label.GroupStatement;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.Numeric;
import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.tools.label.PointerResolver;
import gov.nasa.pds.tools.label.PointerStatement;
import gov.nasa.pds.tools.label.PointerStatementFactory;
import gov.nasa.pds.tools.label.Scalar;
import gov.nasa.pds.tools.label.Sequence;
import gov.nasa.pds.tools.label.Set;
import gov.nasa.pds.tools.label.Statement;
import gov.nasa.pds.tools.label.IncludePointer;
import gov.nasa.pds.tools.label.Symbol;
import gov.nasa.pds.tools.label.TextString;
import gov.nasa.pds.tools.label.Value;
import gov.nasa.pds.tools.util.AntlrUtils;

import java.io.File;
import java.io.IOException;
}

@lexer::header {
package gov.nasa.pds.tools.label.antlr;

import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.constants.Constants.ProblemType;
}

@parser::members {

    private PointerResolver pointerResolver;

    public void setPointerResolver(final PointerResolver pointerResolver) {
        this.pointerResolver = pointerResolver;
    }
    
    private Label label = null;
    
    @Override
    public void displayRecognitionError(String[] tokenNames,
                                        RecognitionException e) {
        if (label != null) {
        	if(e instanceof NoViableAltException) {
                label.addProblem(e.line, e.charPositionInLine, "parser.error.noViableAlternative", ProblemType.PARSE_ERROR, e.token.getText());
            } else if(e instanceof MissingTokenException) {
                final MissingTokenException mte = (MissingTokenException) e;
                String missing = tokenNames[mte.expecting];
                label.addProblem(e.line, e.charPositionInLine, "parser.error.missingToken", ProblemType.PARSE_ERROR, missing, mte.token.getText());
            } else if(e instanceof UnwantedTokenException) {
                final UnwantedTokenException ute = (UnwantedTokenException) e;
                String expectingToken = tokenNames[ute.expecting];
                label.addProblem(e.line, e.charPositionInLine, "parser.error.extraToken", ProblemType.PARSE_ERROR, e.token.getText(), expectingToken);
            } else {
            	// unhandled, if example found, do another instanceof case
            	String msg = getErrorMessage(e, tokenNames);
            	label.addProblem(e.line, e.charPositionInLine, "parser.error.unhandledException", ProblemType.PARSE_ERROR, msg, e.getClass().getName());
            }
        }
    }
    
    public void reportExtraTokens(final List<Token> extraTokens, final Value value, final String idText) {
    	if(extraTokens.size() > 0) {
    		final Token first = extraTokens.get(0);
            String extraTokensString = value.toString() + ", ";
            extraTokensString+= AntlrUtils.toSeparatedString(extraTokens);
            label.addProblem(first.getLine(), first.getCharPositionInLine(), "parser.error.tooManyTokens", ProblemType.BAD_VALUE, extraTokensString, idText);
        }
    }
}

@lexer::members {

	private boolean stopAtEND = true;
	private boolean foundEND = false;
	private boolean pastEndLine = false;
	
	private Stack<String> paraphrase = new Stack<String>();

	private Label label;

	public void setStopAtEND(boolean stopAtEND) {
		this.stopAtEND = stopAtEND;
	}
	
	@Override
	public Token nextToken() {
		final Token tok = super.nextToken();

		final int type = tok.getType();
		if (this.stopAtEND && this.foundEND) {
			// only say has attached if there's actual content
			if(type == EOL) {
				this.pastEndLine = true;
			} else if(type != COMMENT && type != EOF) {
				// should only occur once at most since custom stream does not surface more than one byte of attached content
				return Token.EOF_TOKEN;
			}
		}
		
		if (tok.getType() == END) {
			this.foundEND = true;
		}
		
		return tok;
	}
	
	@Override
    public void reportError(RecognitionException e) {
		if (this.label != null && !this.foundEND) {
			this.label.addProblem(e.line, e.charPositionInLine, "parser.error.illegalCharacter", ProblemType.PARSE_ERROR, displayableString(e.c));
		}
    }    
    
    protected String displayableString(final int c) {
    	StringBuffer result = new StringBuffer();
    	if (' ' <= c && c <= '~') {
    		result.append((char) c);
    	} else {
    	    result.append("\\x");
    	    result.append(Integer.toHexString(c));
    	}
    	return result.toString();
    }

	@Override
    public void recover(RecognitionException re) {
    	if (input.LA(1)!='\n' && input.LA(1)!='\r' && input.LA(1)!=CharStream.EOF) {
    		input.consume();
    	}
    }
	
	public void setLabel(Label label) {
		this.label = label;
	}
}

dictionary returns [List<Label> labels = new ArrayList<Label>()]
	: d=dictionary_section {labels.add(d);}
	  (END d=dictionary_section {labels.add(d);})*
	;

dictionary_section returns [Label label = null]
@init {File file = null; label = new Label(file);this.label = label;}
    : ( s=statement[label] {if (s != null) {label.addStatement(s);}} )*
	;

// a label is a series of one or more expressions followed by an END
label[Label label]
@init {this.label = label;}
    : ( s=statement[label] {if (s != null) {label.addStatement(s);}} )*
      (END {label.setHasEndStatement();} | EOF )
    ;
    
// an expression is an assignment like identifer = value
//      or an object block
statement[Label label] returns [Statement result = null]
    : s=simple_statement[label]
        {result = s;}
    | g=group_statement[label]
        {result = g;}
    | o=object_statement[label]
        {result = o;}
    ;
    catch [RecognitionException re] {
	    while(input.LA(1)!= EOL && input.LA(1) != CharStream.EOF) {
			input.consume();
			
		}
		if(input.LA(1) == EOL) {
			input.consume();
		}
	    label.addProblem(re.line, re.charPositionInLine, re.getMessage(), ProblemType.PARSE_ERROR);
    }
    
simple_statement[Label label] returns [Statement result = null]
    : /*empty statement*/ (c=COMMENT)? EOL
        {
           if (c == null) {
           	result = null;
           } else {
              result = new CommentStatement(label, c, null);
           }
        }
    | s=assignment_statement[label]
        {
           result = s;
        }
    | p=pointer_statement[label]
        {
           result = p;
        }
    ;
    
// an object block
object_statement[Label label] returns [ObjectStatement result = null]
    : 'OBJECT' nl '=' nl id=IDENTIFIER (c=COMMENT)? EOL
      {
         result = new ObjectStatement(label, id.getLine(), id.getText());
         result.attachComment(c);
      }
      (
        (~(END_OBJECT)) => s=statement[label] {if (s != null) {result.addStatement(s);}}
      	|
        (~(END_OBJECT|END|EOF|'OBJECT') (~(EOL|END|EOF))* EOL) => t = . (~(EOL|END|EOF))* EOL
          {label.addProblem(t.getLine(),t.getCharPositionInLine(), "parser.error.illegalStatementStart", ProblemType.PARSE_ERROR, t.getText());}
          
      )*
      // TODO: complain if id mismatch
      (END_OBJECT|END|EOF) => ( (END_OBJECT) => (t = END_OBJECT ('=' id2 = IDENTIFIER)? (c2 = COMMENT)? EOL)
	      {
	         if (result != null && c2 != null) {
	            result.attachComment(c2);
	         }
	      }
	      | {label.addProblem(id.getLine(),id.getCharPositionInLine(), "parser.error.missingEndObject", ProblemType.PARSE_ERROR, id.getText(), id.getLine());}
	   )
    ;

// a group block
group_statement[Label label] returns [GroupStatement result = null]
    : 'GROUP' nl '=' nl id=IDENTIFIER (c=COMMENT)? EOL
      {
         result = new GroupStatement(label, id.getLine(), id.getText());
         result.attachComment(c);
      }
      (
        (~(END_GROUP)) => s=simple_statement[label] {if (s != null) {result.addStatement(s);}}
      	| 
        (~(END_GROUP|END|EOF)) => t = . (~EOL)* EOL
          {label.addProblem(t.getLine(), t.getCharPositionInLine(), "parser.error.illegalStatementStart", ProblemType.PARSE_ERROR, t.getText());}
        
      )*
      (END_GROUP|END|EOF) => ( (END_GROUP) => (END_GROUP ('=' id2=IDENTIFIER)? (c3=COMMENT)? EOL)
     | 
	  {label.addProblem(id.getLine(),id.getCharPositionInLine(), "parser.error.missingEndGroup", ProblemType.PARSE_ERROR, id.getText(), id.getLine());}
	  )
	;

// a pointer
pointer_statement[Label label] returns [PointerStatement result = null]
    : '^' a=assignment_statement[label]
      {
        if(a != null) {
        	result = PointerStatementFactory.newInstance(label, a.getLineNumber(), a.getIdentifier().getId(), a.getValue());
	         if (result != null && result instanceof IncludePointer) {
	            IncludePointer sp = (IncludePointer) result;
	            try {
	               // When using ManualPathResolver problems will be added at this time to the pointer's label.
	               // This is a different behavior than the StandardPathResolver and reference checking will need 
	               // at a higher level to be done. 
	               sp.loadReferencedStatements(label, this.pointerResolver);
	            } catch (LabelParserException lpe) {
	               // don't add problem since for a different file
	               //label.addProblem(lpe);
	            } catch (IOException ioe) {
	              // for now, missing files are tested elsewhere
	              // label.addProblem(a.getLineNumber(), ioe.getMessage(), ProblemType.PARSE_ERROR);
	            } 
	         }
         }
      }
    ;

// an attribute assignment
assignment_statement[Label label] returns [AttributeStatement result = null]
@init {
    List<Token> extraTokens=new ArrayList<Token>();
}
	// same line
    :   (IDENTIFIER? '=' (value[label]|bad_value[label]) ~(COMMENT|EOL|UNITS)* COMMENT? EOL) =>
            ((id = IDENTIFIER)? eq = '=' (v = value[label]|b = bad_value[label]) (extra = ~(COMMENT|EOL|UNITS) {extraTokens.add(extra);} )* (c = COMMENT)? EOL) {
                int line = eq.getLine();
                Value val = v != null ? v : b;
                String idText = id != null ? id.getText() : "";
                
                result = new AttributeStatement(label, line, idText, val);
                
                reportExtraTokens(extraTokens, val, idText);
                
                result.attachComment(c);
                
                // report if bad value
                if(b != null) {
                 	label.addProblem(line, null, "parser.error.badValue", ProblemType.BAD_VALUE, idText, b.getValue());
                }
            }
        // multiline statement (comment allowed before value as long as value on next line, no '=' allowed on next line since indicates new statement)
        | (IDENTIFIER? '=' COMMENT? EOL+ (value[label]|bad_value[label]) ~(COMMENT|EOL|'='|UNITS)* COMMENT? EOL) =>
            ((id = IDENTIFIER)? eq = '=' EOL+ (v = value[label]|b = bad_value[label]) (extra = ~(COMMENT|EOL|'='|UNITS) {extraTokens.add(extra);} )* (c = COMMENT)? EOL) {
                int line = eq.getLine();
                Value val = v != null ? v : b;
                String idText = id != null ? id.getText() : "";
                
                result = new AttributeStatement(label, line, idText, val);
		        
		        reportExtraTokens(extraTokens, val, idText);
		        
		        result.attachComment(c);
            }
        // missing value
        | ((id = IDENTIFIER)? eq = '=' (c = COMMENT)? EOL) {
            int line = eq.getLine();
            String idText = id != null ? id.getText() : "";
	        
	        result = new AttributeStatement(label, line, idText, null);
	        result.attachComment(c);
        }
        // unterminated quoted string
        // TODO: missing equals?
        | (IDENTIFIER '=' QUOTED_UNTERMINATED EOF ) =>
        ((id = IDENTIFIER) eq = '=' txt = text_string_value_unterminated[label]) {
            int line = eq.getLine();
	        
	        String idText = id != null ? id.getText() : "";
	        
	        label.addProblem(line, null, "parser.error.missingEndQuote", ProblemType.BAD_VALUE, idText);
	        result = new AttributeStatement(label, line, idText, txt);
        }
        ;

// a value is a scalar, sequence, or set
value[Label label] returns [Value result = null]
    : v1=scalar_value[label]
        {result = v1;} 
    | v2=sequence_value[label]
        {result = v2;}
    | v3=set_value[label]
        {result = v3;}
    ;

// a scalar is a numeric, date time, text string, or symbol
scalar_value[Label label] returns [Scalar result = null]
    : n=numeric_value[label]
        {result = n;}
    | d=date_time_value[label]
        {result = d;}
    | t=text_string_value[label]
        {result = t;}
    | s=symbol_value[label]
        {result = s;}
    ;
    
// a numeric is an integer, based integer, or real followed by optional units
numeric_value[Label label] returns [Numeric result = null]
    : i=INTEGER (u=UNITS)?
        {
          result = new Numeric(i.getText());
          if (u != null) {result.setUnits(u.getText());}
        }
    | b=BASED_INTEGER (u1=UNITS)?
        {
          result = new Numeric(b.getText());
          if (u1 != null) {result.setUnits(u1.getText());}
        }
    | r=REAL (u2=UNITS)?
        {
          result = new Numeric(r.getText());
          if (u2 != null) {result.setUnits(u2.getText());}
        }
    ;

// a text string is quoted with double quotes
text_string_value[Label label] returns [TextString result = null]
    : q=QUOTED
        {result = new TextString(q.getText());}
    ;
    
bad_value[Label label] returns [TextString result = null]
	: v=BAD_TOKEN
	{result = new TextString(v.getText());}
	;
    
// a text string is quoted with double quotes
text_string_value_unterminated[Label label] returns [TextString result = null]
    : q=QUOTED_UNTERMINATED
    	{result = new TextString(q.getText());}
    ;

// a date time formatted to PDS specification 
date_time_value[Label label] returns [DateTime result = null]
    : dt=DATETIME
        {
           try {
              result = new DateTime(label, dt.getText(), dt.getLine());
           } catch (LabelParserException pe) {
           	  label.addProblem(pe);
           	  try {
           	      // Try again to parse the date, leniently,
           	      // so we have a valid date value, even though
           	      // we've recorded a problem. (That way if the
           	      // date is close to correct, we can still get
           	      // the right value, so we can do comparisons on
           	      // different date values.)
	              result = new DateTime(label, dt.getText(), dt.getLine(), true);
	          } catch (LabelParserException pe2) {
	              // ignore
	          }
           }
        }
    ;
    
// a symbol  
symbol_value[Label label] returns [Symbol result = null]
    : id=IDENTIFIER
        {result = new Symbol(id.getText());}
    | qs=SYMBOL
        {result = new Symbol(qs.getText());}
    ;

sequence_value[Label label] returns [Sequence result = null]
    : ('(' nl '(') => s1=sequence_2d[label]
        {result = s1;}
    | s2=sequence_1d[label]
        {result = s2;}
    ;
    
sequence_1d[Label label] returns [Sequence result = null]
    : '(' nl s=scalar_list[label] ')'
        {result = s;}
    ;
    
scalar_list[Label label] returns [Sequence result=new Sequence()]
    : /*empty*/
    | s=scalar_value[label] {result.add(s);} nl
        ((',' nl)? s2=scalar_value[label] nl {result.add(s2);})*
    ;

sequence_2d[Label label] returns [Sequence result = null]
    : '(' nl s=sequence_list[label] ')'
        {result = s;}
    ;

sequence_list[Label label] returns [Sequence result = new Sequence()]
    : /*empty*/
    | s=sequence_1d[label] {result.add(s);} nl
        ((',' nl)? s2=sequence_1d[label] nl {result.add(s2);})*
    ;

set_value[Label label] returns [Set result = null;]
    : '{' nl s=item_list[label] {result=s;} '}'
    ;
    
item_list[Label label] returns [Set result = new Set()]
    : /*empty*/
    | s=scalar_value[label] {result.add(s);} nl
        ((',' nl)? s2=scalar_value[label] nl {result.add(s2);})*
    ;

/* An arbitrary number of newlines, used within sets and sequences, which
 * can span multiple lines.
 */
nl
    : EOL*
    ;

// LEXER

// Helper definitions

fragment
SIGN
    : '+' | '-'
    ;

// digits are only numbers 
fragment
DIGIT
    : ('0'..'9')
    ;
    
fragment
LETTER
    : ('a'..'z'|'A'..'Z')
    ;

fragment  
EOF
: '\uFFFF'
;
  
// extended digit is a digit or letter
fragment
EXTENDED_DIGIT
    : DIGIT | LETTER
    ;
   
fragment
DIGITS
    : (DIGIT)+
    ;

// any special characters that can pop up in value strings, including
// forward slash or dots seen in filenames, hash sign seen in bit masks,
// underscores seen in keywords and values, dashes and colons seen in
// dates, plus or minus signs in numbers.
fragment
SPECIALCHAR
    : ('_' | '$' | '#' | '.' | '-' | ':' | '+' | '*')
    ;


// Multiple-line comments
COMMENT
@init {paraphrase.push("comment");}
@after {paraphrase.pop();}
    // TODO: Check line length
    : '/*' ( options {greedy=false;} : (~('\r'|'\n') | EOL))* '*/'
    ;


// Whitespace - ignore it
WS
    : (   ' '
      |   '\t'
      |   '\f'
      )+
      { $channel = HIDDEN; }
    ;

IDENTIFIER
    : LETTER (LETTER|DIGIT|'_')* ( ':' LETTER (LETTER|DIGIT|'_')* )?
    ;

UNITS
@init {paraphrase.push("units");}
@after {paraphrase.pop();}
    : '<' (LETTER | DIGIT | SPECIALCHAR | '(' | ')' | '/' | WS)* '>'
    ;
 
// real numbers
REAL
@init {paraphrase.push("real");}
@after {paraphrase.pop();}
    : INTEGER '.' DIGITS? (('E'|'e') SIGN? DIGITS)?
    | SIGN? '.' DIGITS (('E'|'e') SIGN? DIGITS)?
    ;

// based integer
// NOTE: doing nasty predicate since not enough lookahead on this before commiting to match
BASED_INTEGER
@init {paraphrase.push("integer with base");}
@after {paraphrase.pop();}
    //: DIGITS '#' SIGN? EXTENDED_DIGIT (EXTENDED_DIGIT|' ')+ '#'
    : DIGITS '#' SIGN? EXTENDED_DIGIT+ '#'
    ;
    
// Dates and times
DATETIME
@init {paraphrase.push("date-time");}
@after {paraphrase.pop();}
    : DIGITS '-' DIGITS '-' DIGITS 'T' DIGITS 'Z'?
    | DIGITS '-' DIGITS '-' DIGITS 'T' TIME
    | DIGITS '-' DIGITS 'T' DIGITS 'Z'?
    | DIGITS '-' DIGITS 'T' TIME
    | DIGITS '-' DIGITS '-' DIGITS
    | DIGITS '-' DIGITS
    ;

// One weird rule: we have to look ahead to a ':' to determine
// whether we have an integer or a time.
INTEGER_OR_TIME
	: (DIGITS ':') => TIME {$type = TIME;}
	| INTEGER {$type = INTEGER;}
	;

fragment
TIME
/*options {greedy=FALSE;}*/
    : DIGITS ':' DIGITS (':' DIGITS ('.' DIGITS)?)? 'Z'?
    ;
    
// integer positive or negative no commas
fragment
INTEGER
    : SIGN? DIGITS
    ;
    
// string literals
QUOTED
    : '"' (EOL|~('"'|'\uFFFF'|'\r'|'\n'))* '"'
    ;
    
// string literals
QUOTED_UNTERMINATED
    : '"' (EOL|~('"'|'\uFFFF'|'\r'|'\n'))*
    ;

SYMBOL
    : '\'' (~('\''|'\\'|'\r'|'\n'))* '\''
    ;

EOL
@init {int startColumn = -1;}
    : {startColumn = getCharPositionInLine();} '\r\n'
        {
          if (label != null && this.pastEndLine == false) {
              label.addLineLength($line, startColumn);
          }
        }
    | {startColumn = getCharPositionInLine();} ('\n\r' | '\r' | '\n')
        {
          if (label != null && this.pastEndLine == false) {
              label.addLineLength($line, startColumn);
          }
          if (label != null)
            label.addProblem($line, "parser.error.badLineEnding", ProblemType.ILLEGAL_LINE_ENDING);
        }
    ;
    
BAD_TOKEN
	:(LETTER|DIGIT|SPECIALCHAR)+
	;
