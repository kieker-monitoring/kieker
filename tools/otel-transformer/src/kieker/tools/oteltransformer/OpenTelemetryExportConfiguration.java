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

import java.util.List;

import kieker.analysis.generic.CountingStage;
import kieker.analysis.generic.DynamicEventDispatcher;
import kieker.analysis.generic.IEventMatcher;
import kieker.analysis.generic.ImplementsEventMatcher;
import kieker.analysis.generic.source.rewriter.NoneTraceMetadataRewriter;
import kieker.analysis.generic.source.tcp.MultipleConnectionTcpSourceStage;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.flow.IFlowRecord;
import kieker.tools.oteltransformer.stages.FlowTraceCollector;
import kieker.tools.oteltransformer.stages.OTelExecution;
import kieker.tools.oteltransformer.stages.OpenTelemetryExporterStage;
import kieker.tools.oteltransformer.stages.OperationExecutionTraceCollector;
import teetime.framework.Configuration;
import teetime.stage.basic.merger.Merger;

/**
 * A configuration for exporting Kieker data to OpenTelemetry.
 * 
 * @author DaGeRe
 */
public class OpenTelemetryExportConfiguration extends Configuration {

	public OpenTelemetryExportConfiguration(final int inputPort, final int bufferSize, final kieker.common.configuration.Configuration configuration) {
		final long timeout = 1000*1000*60; // 60 seconds timeout for traces
		final MultipleConnectionTcpSourceStage source = new MultipleConnectionTcpSourceStage(inputPort, bufferSize, new NoneTraceMetadataRewriter());

		final CountingStage<IMonitoringRecord> counter = new CountingStage<>(true, 1000);

		connectPorts(source.getOutputPort(), counter.getInputPort());

		final DynamicEventDispatcher eventDispatcher = new DynamicEventDispatcher(null, false, true, false);
		final IEventMatcher<IFlowRecord> flowEventMatcher = new ImplementsEventMatcher<>(IFlowRecord.class, null);
		final IEventMatcher<OperationExecutionRecord> operationExecutionRecordMatcher = new ImplementsEventMatcher<>(
				OperationExecutionRecord.class, null);

		eventDispatcher.registerOutput(flowEventMatcher);
		eventDispatcher.registerOutput(operationExecutionRecordMatcher);

		final Merger<List<OTelExecution>> merger = new Merger<>();
		merger.declareActive();
		
		final FlowTraceCollector flowTraceCollector = new FlowTraceCollector();
		final OperationExecutionTraceCollector operationExecutionTraceCollector = new OperationExecutionTraceCollector(timeout);
		
		final OpenTelemetryExporterStage otstage = new OpenTelemetryExporterStage(configuration);

		this.connectPorts(counter.getRelayedEventsOutputPort(), eventDispatcher.getInputPort());
		this.connectPorts(flowEventMatcher.getOutputPort(), flowTraceCollector.getInputPort());
		this.connectPorts(flowTraceCollector.getOuputPort(), merger.getNewInputPort());
		
		this.connectPorts(operationExecutionRecordMatcher.getOutputPort(),
				operationExecutionTraceCollector.getInputPort());
		this.connectPorts(operationExecutionTraceCollector.getOutputPort(), merger.getNewInputPort());
		this.connectPorts(merger.getOutputPort(), otstage.getInputPort());
	}
}
