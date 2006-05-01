//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools.label.parser;

import antlr.debug.MessageEvent;
import antlr.debug.MessageListener;
import antlr.debug.TraceEvent;

/**
 * @author pramirez
 * @version $Revision$
 * 
 */
public class DefaultMessageListener implements MessageListener {

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
     * @see antlr.debug.ListenerBase#doneParsing(antlr.debug.TraceEvent)
     */
    public void doneParsing(TraceEvent event) {
        System.out.println(event);
    }

    /* (non-Javadoc)
     * @see antlr.debug.ListenerBase#refresh()
     */
    public void refresh() {
    }

}
