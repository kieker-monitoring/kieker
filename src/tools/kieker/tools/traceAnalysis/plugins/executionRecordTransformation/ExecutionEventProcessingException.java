/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tools.traceAnalysis.plugins.executionRecordTransformation;

import kieker.analysis.plugin.util.event.EventProcessingException;

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
