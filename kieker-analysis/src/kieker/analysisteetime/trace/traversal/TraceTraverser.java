package kieker.analysisteetime.trace.traversal;

import kieker.analysisteetime.model.analysismodel.trace.OperationCall;
import kieker.analysisteetime.model.analysismodel.trace.TraceRoot;

public class TraceTraverser {

	// TODO Make this generic for aggregated traces

	public TraceTraverser() {}

	public void traverse(final TraceRoot trace, final OperationCallVisitor visitor) {
		this.handleOperationCallsRecursively(trace.getRootOperationCall(), visitor);
	}

	private void handleOperationCallsRecursively(final OperationCall operationCall, final OperationCallVisitor visitor) {
		visitor.visit(operationCall);
		for (final OperationCall childOperationCall : operationCall.getChildren()) {
			this.handleOperationCallsRecursively(childOperationCall, visitor);
		}
	}

}
