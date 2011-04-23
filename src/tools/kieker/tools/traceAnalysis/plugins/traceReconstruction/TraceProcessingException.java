package kieker.tools.traceAnalysis.plugins.traceReconstruction;

import kieker.analysis.plugin.EventProcessingException;

/**
 *
 * @author Andre van Hoorn
 */
public class TraceProcessingException extends EventProcessingException {
    private static final long serialVersionUID = 189899L;

    public TraceProcessingException (String msg){
        super(msg);
    }

    public TraceProcessingException (String msg, Throwable t){
        super(msg, t);
    }
}
