package kieker.analysisteetime.trace.aggregation;

import kieker.analysisteetime.model.analysismodel.trace.Trace;

import teetime.stage.basic.AbstractTransformation;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class AggregatedTraceUnwrapperStage extends AbstractTransformation<AggregatedTraceWrapper, Trace> {

	@Override
	protected void execute(final AggregatedTraceWrapper wrapper) {
		if (wrapper.isFirst()) {
			final Trace trace = wrapper.getTrace();
			this.outputPort.send(trace);
		}
	}

}
