/***************************************************************************
 * Copyright (C) 2024 Kieker Project (https://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

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

/**
 * A configuration for exporting Kieker data to OpenTelemetry.
 * 
 * @author DaGeRe
 */
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

		final TraceReconstructionStage traceReconstructionStage = new TraceReconstructionStage(repository, TimeUnit.MILLISECONDS, true, 10000L);
		this.connectPorts(executionRecordTransformationStage.getOutputPort(), traceReconstructionStage.getInputPort());

		final OpenTelemetryExporterStage otstage = new OpenTelemetryExporterStage(configuration);
		this.connectPorts(traceReconstructionStage.getExecutionTraceOutputPort(), otstage.getInputPort());

	}
}
