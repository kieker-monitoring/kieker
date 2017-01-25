package kieker.analysisteetime.trace.traversal;

import java.util.Collection;
import java.util.Collections;

import kieker.analysisteetime.model.analysismodel.trace.OperationCall;
import kieker.analysisteetime.model.analysismodel.trace.Trace;

public class TraceTraverser {

	// TODO Make this generic for aggregated traces

	public TraceTraverser() {}

	public void traverse(final Trace trace, final OperationCallVisitor visitor) {
		final Collection<OperationCallVisitor> visitors = Collections.singleton(visitor);
		this.handleOperationCallsRecursively(trace.getRootOperationCall(), visitors);
	}

	public void traverse(final Trace trace, final Collection<OperationCallVisitor> visitors) {
		this.handleOperationCallsRecursively(trace.getRootOperationCall(), visitors);
	}

	private void handleOperationCallsRecursively(final OperationCall operationCall, final Collection<OperationCallVisitor> visitors) {
		for (final OperationCallVisitor visitor : visitors) {
			visitor.visit(operationCall);
		}

		for (final OperationCall childOperationCall : operationCall.getChildren()) {
			this.handleOperationCallsRecursively(childOperationCall, visitors);
		}
	}

}
