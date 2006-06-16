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

package gov.nasa.jpl.pds.tools.label;

import gov.nasa.jpl.pds.tools.label.antlr.ODLTokenTypes;
import antlr.collections.AST;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class StatementFactory implements ODLTokenTypes {
    
    /**
     * 
     * @param ast
     * @return Returns the statement.
     * @throws UnknownStatementException
     */
    public static Statement createStatement(AST ast) throws UnknownStatementException, UnknownValueException {
        Statement statement = null;
        
        if (ast.getType() == ASSIGNMENT_OPERATOR)
            statement = createAttribute(ast);
        else if (ast.getType() == POINT_OPERATOR)
            statement = createPointer(ast);
        else if (ast.getType() == COMMENT)
            statement = createComment(ast);
        else if (ast.getType() == IDENT && ast.getFirstChild().getType() == LITERAL_OBJECT)
            statement = createObject(ast);
        else if (ast.getType() == IDENT && ast.getFirstChild().getType() == LITERAL_GROUP)
            statement = createGroup(ast);
        else 
            throw new UnknownStatementException("Unknown Statement Type: " + ast.getType());
        
        return statement;
    }
    
    /**
     * 
     * @param ast
     * @return Returns the attribute statement.
     * @throws UnknownStatementException
     */
    public static AttributeStatement createAttribute(AST ast) throws UnknownStatementException, UnknownValueException {
        if (ast.getType() != ASSIGNMENT_OPERATOR)
            throw new UnknownStatementException("Not an attribute statement! Has type = " + ast.getType());
        
        AttributeStatement attribute = new AttributeStatement(ast.getLine(), ast.getFirstChild().getText().trim());
        attribute.setValue(createValue(ast.getFirstChild().getNextSibling()));
        
        return attribute;
    }
    
    /**
     * 
     * @param ast
     * @return Returns the object statement.
     * @throws UnknownStatementException
     */
    public static ObjectStatement createObject(AST ast) throws UnknownStatementException, UnknownValueException {
        if (ast.getType() != IDENT || ast.getFirstChild().getType() != LITERAL_OBJECT)
            throw new UnknownStatementException("Not an object statement it is type: " + ast.getType());
        
        ObjectStatement object = new ObjectStatement(ast.getLine(), ast.getText().trim());
        ast = ast.getFirstChild().getNextSibling();
        while (ast != null && ast.getType() != LITERAL_END_OBJECT) {
            object.addStatement(createStatement(ast));
            ast = ast.getNextSibling();
        }
        
        return object;
    }
    
    /**
     * 
     * @param ast
     * @return Returns the group statement.
     * @throws UnknownStatementException
     */
    public static GroupStatement createGroup(AST ast) throws UnknownStatementException, UnknownValueException {
        if (ast.getType() != IDENT || ast.getFirstChild().getType() != LITERAL_GROUP)
            throw new UnknownStatementException("Not an group statement it is type: " + ast.getType());
        
        GroupStatement group = new GroupStatement(ast.getLine(), ast.getText().trim());
        ast = ast.getFirstChild().getNextSibling();
        while (ast != null && ast.getType() != LITERAL_END_GROUP) {
            group.addAttribute(createAttribute(ast));
            ast = ast.getNextSibling();
        }
        
        return group;
    }
    
    /**
     * 
     * @param ast
     * @return Returns the pointer statement.
     * @throws UnknownStatementException
     */
    public static PointerStatement createPointer(AST ast) throws UnknownStatementException, UnknownValueException {
        if (ast.getType() != POINT_OPERATOR)
            throw new UnknownStatementException("Not a pointer statement it is type: " + ast.getType());
        return new PointerStatement(ast.getLine(), ast.getText().trim(), createValue(ast.getFirstChild()));
    }
    
    /**
     * 
     * @param ast
     * @return Returns the comment statement.
     * @throws UnknownStatementException
     */
    public static CommentStatement createComment(AST ast) throws UnknownStatementException {
        if (ast.getType() != COMMENT)
            throw new UnknownStatementException("Not a comment statement it is type: " + ast.getType());
        CommentStatement comment = new CommentStatement(ast.getLine());
        comment.setComment(ast.getText());
        return comment;
    }
    
    /**
     * 
     * @param ast
     * @return Returns the value.
     * @throws UnknownValueException
     */
    public static Value createValue(AST ast) throws UnknownValueException {
        Value value = null;
        
        if (ast.getType() == QUOTED)
            value = createTextString(ast);
        else if (ast.getType() == SYMBOL)
            value = createSymbol(ast);
        else if (ast.getType() == IDENT)
            value = createIdentifier(ast);
        else if (ast.getType() == SEQUENCE_OPENING)
            value = createSequence(ast);
        else if (ast.getType() == SET_OPENING)
            value = createSet(ast);
        else 
            throw new UnknownValueException("Found an unknown value of type: " + ast.getType());
        
        return value;
    }
    
    /**
     * 
     * @param ast
     * @return Returns the text string.
     * @throws UnknownValueException
     */
    public static TextString createTextString(AST ast) throws UnknownValueException {
        if (ast.getType() != QUOTED) 
            throw new UnknownValueException("Value is not a text string it is type: " + ast.getType());
        return new TextString(ast.getText());
    }
    
    /**
     * 
     * @param ast
     * @return Returns the sequence.
     * @throws UnknownValueException
     */
    public static Sequence createSequence(AST ast) throws UnknownValueException {
        if (ast.getType() != SEQUENCE_OPENING)
            throw new UnknownValueException("Value is not a sequence it is type: " + ast.getType());
        Sequence sequence = new Sequence();
        ast = ast.getFirstChild();
        while (ast != null) {
            sequence.add(createValue(ast));
            ast = ast.getNextSibling();
            //Check to see if there were units associated with value
            //If so skip the next sibling
            if (ast != null && ast.getType() == UNITS)
                ast = ast.getNextSibling();
        }
        
        return sequence;
    }
    
    /**
     * 
     * @param ast
     * @return Returns the set.
     * @throws UnknownValueException
     */
    public static Set createSet(AST ast) throws UnknownValueException {
        if (ast.getType() != SET_OPENING)
            throw new UnknownValueException("Value is not a set it is type: " + ast.getType());
        Set set = new Set();
        ast = ast.getFirstChild();
        while (ast != null) {
            set.add(createValue(ast));
            ast = ast.getNextSibling();
            //Check to see if there were units associated with value
            //If so skip the next sibling
            if (ast != null && ast.getType() == UNITS)
                ast = ast.getNextSibling();
        }
        
        return set;
    }
    
    /**
     * 
     * @param ast
     * @return Returns the identifier.
     * @throws UnknownValueException
     */
    public static Value createIdentifier(AST ast) throws UnknownValueException {
        Value value = null;
        
        //TODO: handle based values
        //If it has units then we know it is a Numeric value
        if (ast.getNextSibling() != null && ast.getNextSibling().getType() == UNITS)
            return new Numeric(ast.getText(), ast.getNextSibling().getText());
        
        NumberFormat format = NumberFormat.getInstance();
        try {
            format.parse(ast.getText());
            value = new Numeric(ast.getText());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return value;
    }
    
    /**
     * 
     * @param ast
     * @return Returns the symbol.
     * @throws UnknownValueException
     */
    public static Symbol createSymbol(AST ast) throws UnknownValueException {
        if (ast.getType() != SYMBOL)
            throw new UnknownValueException("Value is not a symbol it is type:" + ast.getType());
        return new Symbol(ast.getText());
    }
}
