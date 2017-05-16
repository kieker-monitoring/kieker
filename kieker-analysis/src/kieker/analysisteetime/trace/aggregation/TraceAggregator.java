package kieker.analysisteetime.trace.aggregation;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Equivalence;

import kieker.analysisteetime.model.analysismodel.trace.OperationCall;
import kieker.analysisteetime.model.analysismodel.trace.Trace;
import kieker.analysisteetime.model.analysismodel.trace.TraceFactory;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class TraceAggregator {

	private final TraceFactory traceFactory = TraceFactory.eINSTANCE;
	private final boolean considerFailed = true; // TODO Temp
	private final TraceEquivalence traceEquivalence = new TraceEquivalence(this.considerFailed);
	private final Map<Equivalence.Wrapper<Trace>, Trace> aggregatedTraces = new HashMap<>();

	public AggregatedTraceWrapper handleTrace(final Trace trace) {
		final Equivalence.Wrapper<Trace> equalityWrappedTrace = this.traceEquivalence.wrap(trace);
		Trace aggregatedTrace = this.aggregatedTraces.get(equalityWrappedTrace);
		boolean isFirst = false;
		if (aggregatedTrace == null) {
			aggregatedTrace = this.createAggregatedTrace(trace);
			this.aggregatedTraces.put(equalityWrappedTrace, aggregatedTrace);
			isFirst = true;
		}
		return new AggregatedTraceWrapper(aggregatedTrace, trace, isFirst);
	}

	private Trace createAggregatedTrace(final Trace trace) {
		final OperationCall aggregatedRootOperationCall = this.createOperationCall(trace.getRootOperationCall());
		final Trace aggregatedTrace = this.traceFactory.createTrace();
		aggregatedTrace.setRootOperationCall(aggregatedRootOperationCall);
		return aggregatedTrace;
	}

	private OperationCall createOperationCall(final OperationCall call) {
		final OperationCall aggregatedCall = this.traceFactory.createOperationCall();
		if (this.considerFailed) {
			aggregatedCall.setFailed(call.isFailed());
			aggregatedCall.setFailedCause(call.getFailedCause());
		}
		call.getChildren().forEach(c -> aggregatedCall.getChildren().add(this.createOperationCall(c)));
		return aggregatedCall;
	}

}
