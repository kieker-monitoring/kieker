package kieker.tools.traceAnalysis.plugins.traceFilter;

import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;

/**
 *
 * @author Andre van Hoorn
 */
abstract class AbstractExecutionTraceHashContainer {

    private final ExecutionTrace executionTrace;

    @SuppressWarnings("unused")
	private AbstractExecutionTraceHashContainer() {
        this.executionTrace = null;
    }

    public AbstractExecutionTraceHashContainer(final ExecutionTrace t) {
        this.executionTrace = t;
    }

    public ExecutionTrace getExecutionTrace() {
        return this.executionTrace;
    }
}

