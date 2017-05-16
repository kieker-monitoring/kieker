package kieker.analysisteetime.trace.aggregation;

import kieker.analysisteetime.model.analysismodel.trace.Trace;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.basic.ITransformation;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class TraceAggregatorStage extends CompositeStage implements ITransformation<Trace, Trace> {

	private final InputPort<Trace> inputPort;
	private final OutputPort<Trace> outputPort;

	public TraceAggregatorStage() {
		final AggregatedTraceCreatorStage aggregatedTraceCreator = new AggregatedTraceCreatorStage();
		// TODO Statistics stages here
		final AggregatedTraceUnwrapperStage aggregatedTraceUnwrapper = new AggregatedTraceUnwrapperStage();

		this.inputPort = aggregatedTraceCreator.getInputPort();
		this.outputPort = aggregatedTraceUnwrapper.getOutputPort();

		this.connectPorts(aggregatedTraceCreator.getOutputPort(), aggregatedTraceUnwrapper.getInputPort());
	}

	@Override
	public InputPort<Trace> getInputPort() {
		return this.inputPort;
	}

	@Override
	public OutputPort<Trace> getOutputPort() {
		return this.outputPort;
	}

}
