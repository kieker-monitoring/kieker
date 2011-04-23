package kieker.tools.traceAnalysis.systemModel;

import kieker.analysis.plugin.IAnalysisEvent;

/**
 * @author Andre van Hoorn
 */
public class InvalidExecutionTrace implements IAnalysisEvent {
    private final ExecutionTrace invalidExecutionTraceArtifacts;

    public ExecutionTrace getInvalidExecutionTraceArtifacts() {
        return invalidExecutionTraceArtifacts;
    }

    public InvalidExecutionTrace(final ExecutionTrace invalidExecutionTrace){
        this.invalidExecutionTraceArtifacts = invalidExecutionTrace;
    }

    @Override
    public String toString() {
        StringBuilder strBuild =
                new StringBuilder("Invalid Trace: ");
        strBuild.append(this.invalidExecutionTraceArtifacts.toString());
        return strBuild.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InvalidExecutionTrace)){
            return false;
        }
        if (this == obj) {
            return true;
        }
        InvalidExecutionTrace other = (InvalidExecutionTrace)obj;

        return other.getInvalidExecutionTraceArtifacts().equals(this.invalidExecutionTraceArtifacts);
    }
}
