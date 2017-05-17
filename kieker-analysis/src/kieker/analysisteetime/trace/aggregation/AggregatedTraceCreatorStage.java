package kieker.analysisteetime.trace.aggregation;

import kieker.analysisteetime.model.analysismodel.trace.Trace;

import teetime.stage.basic.AbstractTransformation;

public class AggregatedTraceCreatorStage extends AbstractTransformation<Trace, AggregatedTraceWrapper> {

	private final TraceAggregator aggregatedTraceCreator;

	public AggregatedTraceCreatorStage(final boolean considerFailed) {
		this.aggregatedTraceCreator = new TraceAggregator(considerFailed);
	}

	@Override
	protected void execute(final Trace trace) {
		final AggregatedTraceWrapper aggregatedTrace = this.aggregatedTraceCreator.handleTrace(trace);
		this.outputPort.send(aggregatedTrace);
	}

}
