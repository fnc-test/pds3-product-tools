//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools.label;

import gov.nasa.jpl.pds.tools.label.antlr.ODLTokenTypes;
import antlr.collections.AST;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class StatementFactory implements ODLTokenTypes {
    
    /**
     * 
     * @param ast
     * @return
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
     * @return
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
     * @return
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
     * @return
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
     * @return
     * @throws UnknownStatementException
     */
    public static PointerStatement createPointer(AST ast) throws UnknownStatementException {
        PointerStatement pointer = null;
        
        return pointer;
    }
    
    /**
     * 
     * @param ast
     * @return
     * @throws UnknownStatementException
     */
    public static CommentStatement createComment(AST ast) throws UnknownStatementException {
        CommentStatement comment = null;
        
        return comment;
    }
    
    public static Value createValue(AST ast) throws UnknownValueException {
        Value value = null;
        
        if (ast.getType() == QUOTED)
            value = createTextString(ast);
        else if (ast.getType() == IDENT)
            value = createTextString(ast);
        else if (ast.getType() == SEQUENCE_OPENING)
            value = createSequence(ast);
        else if (ast.getType() == SET_OPENING)
            value = createSet(ast);
        else 
            throw new UnknownValueException("Found an unknown value of type: " + ast.getType());
        
        return value;
    }
    
    public static TextString createTextString(AST ast) throws UnknownValueException {
        if (ast.getType() != QUOTED && ast.getType() != IDENT) 
            throw new UnknownValueException("Value is not a text string it is type: " + ast.getType());
        return new TextString(ast.getText());
    }
    
    public static Sequence createSequence(AST ast) throws UnknownValueException {
        if (ast.getType() != SEQUENCE_OPENING)
            throw new UnknownValueException("Value is not a sequence it is type: " + ast.getType());
        Sequence sequence = new Sequence();
        ast = ast.getFirstChild();
        while (ast != null) {
            if (ast.getType() != UNITS)
                sequence.add(createValue(ast));
            ast = ast.getNextSibling();
        }
        
        return sequence;
    }
    
    public static Set createSet(AST ast) throws UnknownValueException {
        if (ast.getType() != SET_OPENING)
            throw new UnknownValueException("Value is not a set it is type: " + ast.getType());
        Set set = new Set();
        ast = ast.getFirstChild();
        while (ast != null) {
            //TODO: Handle units
            if (ast.getType() != UNITS)
                set.add(createValue(ast));
            ast = ast.getNextSibling();
        }
        
        return set;
    }
}
