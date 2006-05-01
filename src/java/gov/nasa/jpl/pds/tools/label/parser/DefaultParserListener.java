//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools.label.parser;

import antlr.debug.MessageEvent;
import antlr.debug.ParserListener;
import antlr.debug.ParserMatchEvent;
import antlr.debug.ParserTokenEvent;
import antlr.debug.SemanticPredicateEvent;
import antlr.debug.SyntacticPredicateEvent;
import antlr.debug.TraceEvent;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class DefaultParserListener implements ParserListener {

    /* (non-Javadoc)
     * @see antlr.debug.SemanticPredicateListener#semanticPredicateEvaluated(antlr.debug.SemanticPredicateEvent)
     */
    public void semanticPredicateEvaluated(SemanticPredicateEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see antlr.debug.ListenerBase#doneParsing(antlr.debug.TraceEvent)
     */
    public void doneParsing(TraceEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see antlr.debug.ListenerBase#refresh()
     */
    public void refresh() {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see antlr.debug.ParserMatchListener#parserMatch(antlr.debug.ParserMatchEvent)
     */
    public void parserMatch(ParserMatchEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see antlr.debug.ParserMatchListener#parserMatchNot(antlr.debug.ParserMatchEvent)
     */
    public void parserMatchNot(ParserMatchEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see antlr.debug.ParserMatchListener#parserMismatch(antlr.debug.ParserMatchEvent)
     */
    public void parserMismatch(ParserMatchEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see antlr.debug.ParserMatchListener#parserMismatchNot(antlr.debug.ParserMatchEvent)
     */
    public void parserMismatchNot(ParserMatchEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see antlr.debug.MessageListener#reportError(antlr.debug.MessageEvent)
     */
    public void reportError(MessageEvent event) {
        System.out.println(event);
        
    }

    /* (non-Javadoc)
     * @see antlr.debug.MessageListener#reportWarning(antlr.debug.MessageEvent)
     */
    public void reportWarning(MessageEvent event) {
        System.out.println(event);
    }

    /* (non-Javadoc)
     * @see antlr.debug.ParserTokenListener#parserConsume(antlr.debug.ParserTokenEvent)
     */
    public void parserConsume(ParserTokenEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see antlr.debug.ParserTokenListener#parserLA(antlr.debug.ParserTokenEvent)
     */
    public void parserLA(ParserTokenEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see antlr.debug.TraceListener#enterRule(antlr.debug.TraceEvent)
     */
    public void enterRule(TraceEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see antlr.debug.TraceListener#exitRule(antlr.debug.TraceEvent)
     */
    public void exitRule(TraceEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see antlr.debug.SyntacticPredicateListener#syntacticPredicateFailed(antlr.debug.SyntacticPredicateEvent)
     */
    public void syntacticPredicateFailed(SyntacticPredicateEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see antlr.debug.SyntacticPredicateListener#syntacticPredicateStarted(antlr.debug.SyntacticPredicateEvent)
     */
    public void syntacticPredicateStarted(SyntacticPredicateEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see antlr.debug.SyntacticPredicateListener#syntacticPredicateSucceeded(antlr.debug.SyntacticPredicateEvent)
     */
    public void syntacticPredicateSucceeded(SyntacticPredicateEvent arg0) {
        // TODO Auto-generated method stub
        
    }

}
