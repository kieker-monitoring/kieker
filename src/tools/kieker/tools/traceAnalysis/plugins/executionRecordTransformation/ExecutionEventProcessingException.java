package kieker.tools.traceAnalysis.plugins.executionRecordTransformation;

import kieker.analysis.plugin.EventProcessingException;

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
