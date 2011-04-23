package kieker.tools.traceAnalysis.systemModel;

import kieker.analysis.plugin.IAnalysisEvent;

/** @author Andre van Hoorn
 */
public abstract class Trace implements IAnalysisEvent {

    private final long traceId; // convenience field. All executions have this traceId.

    protected Trace(){
        this.traceId = -1;
    }

    public Trace(final long traceId) {
        this.traceId = traceId;
    }

    public final long getTraceId() {
        return traceId;
    }
}
