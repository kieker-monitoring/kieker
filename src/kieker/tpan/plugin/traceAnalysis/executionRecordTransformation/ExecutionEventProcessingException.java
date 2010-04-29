/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tpan.plugin.traceAnalysis.executionRecordTransformation;

import kieker.tpan.plugins.util.event.EventProcessingException;

/**
 *
 * @author Andre van Hoorn
 */
public class ExecutionEventProcessingException extends EventProcessingException {
    private static final long serialVersionUID = 1136L;
    public ExecutionEventProcessingException (String msg){
        super(msg);
    }

    public ExecutionEventProcessingException (String msg, Throwable t){
        super(msg, t);
    }
}
