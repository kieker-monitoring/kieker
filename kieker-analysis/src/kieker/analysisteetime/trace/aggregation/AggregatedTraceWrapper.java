package kieker.analysisteetime.trace.aggregation;

import kieker.analysisteetime.model.analysismodel.trace.Trace;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public final class AggregatedTraceWrapper {

	private final Trace aggregatedTrace; // BETTER use own class for aggregated traces
	private final Trace trace;
	private final boolean isFirst;

	public AggregatedTraceWrapper(final Trace aggregatedTrace, final Trace trace, final boolean isFirst) {
		this.aggregatedTrace = aggregatedTrace;
		this.trace = trace;
		this.isFirst = isFirst;
	}

	public Trace getAggregatedTrace() {
		return this.aggregatedTrace;
	}

	public Trace getTrace() {
		return this.trace;
	}

	public boolean isFirst() {
		return this.isFirst;
	}
}
