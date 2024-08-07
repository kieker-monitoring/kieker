package kieker.tools.oteltransformer;

import java.util.concurrent.TimeUnit;

import kieker.analysis.architecture.trace.execution.ExecutionRecordTransformationStage;
import kieker.analysis.architecture.trace.reconstruction.TraceReconstructionStage;
import kieker.analysis.generic.CountingStage;
import kieker.analysis.generic.DynamicEventDispatcher;
import kieker.analysis.generic.IEventMatcher;
import kieker.analysis.generic.ImplementsEventMatcher;
import kieker.analysis.generic.source.rewriter.NoneTraceMetadataRewriter;
import kieker.analysis.generic.source.tcp.MultipleConnectionTcpSourceStage;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.model.repository.SystemModelRepository;
import kieker.tools.oteltransformer.stages.OpenTelemetryExporterStage;

import teetime.framework.Configuration;

public class OpenTelemetryExportConfiguration extends Configuration {

	public OpenTelemetryExportConfiguration(final int inputPort, final int bufferSize, final kieker.common.configuration.Configuration configuration) {
		final MultipleConnectionTcpSourceStage source = new MultipleConnectionTcpSourceStage(inputPort, bufferSize, new NoneTraceMetadataRewriter());

		final CountingStage<IMonitoringRecord> counter = new CountingStage<>(true, 1000);

		connectPorts(source.getOutputPort(), counter.getInputPort());

		final DynamicEventDispatcher dispatcher = new DynamicEventDispatcher(null, false, true, false);
		final IEventMatcher<? extends OperationExecutionRecord> operationExecutionRecordMatcher = new ImplementsEventMatcher<>(OperationExecutionRecord.class, null);
		dispatcher.registerOutput(operationExecutionRecordMatcher);
		this.connectPorts(counter.getRelayedEventsOutputPort(), dispatcher.getInputPort());

		final SystemModelRepository repository = new SystemModelRepository();
		final ExecutionRecordTransformationStage executionRecordTransformationStage = new ExecutionRecordTransformationStage(repository);

		this.connectPorts(operationExecutionRecordMatcher.getOutputPort(), executionRecordTransformationStage.getInputPort());

		final TraceReconstructionStage traceReconstructionStage = new TraceReconstructionStage(repository, TimeUnit.MILLISECONDS, false, Long.MAX_VALUE);
		this.connectPorts(executionRecordTransformationStage.getOutputPort(), traceReconstructionStage.getInputPort());

		final OpenTelemetryExporterStage otstage = new OpenTelemetryExporterStage(configuration);
		this.connectPorts(traceReconstructionStage.getExecutionTraceOutputPort(), otstage.getInputPort());

	}
}
