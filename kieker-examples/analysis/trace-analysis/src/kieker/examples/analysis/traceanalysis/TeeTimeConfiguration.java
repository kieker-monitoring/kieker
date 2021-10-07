/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
package kieker.examples.analysis.traceanalysis;

import java.util.concurrent.TimeUnit;
import kieker.analysis.stage.flow.EventRecordTraceReconstructionStage;
import kieker.analysis.stage.general.ListCollectionFilter;
import kieker.analysis.stage.DynamicEventDispatcher;
import kieker.analysis.stage.IEventMatcher;
import kieker.analysis.stage.ImplementsEventMatcher;
import kieker.common.exception.ConfigurationException;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.tools.source.LogsReaderCompositeStage;
import kieker.analysis.trace.execution.ExecutionRecordTransformationStage;
import kieker.analysis.trace.TraceEventRecords2ExecutionAndMessageTraceStage;
import kieker.analysis.trace.reconstruction.TraceReconstructionStage;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.AbstractTrace;
import teetime.framework.Configuration;
import teetime.stage.basic.merger.Merger;
import teetime.framework.InputPort;

/**
 * Analysis configuration for the data collector.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 *
 */
public class TeeTimeConfiguration extends Configuration {

	/**
	 * Configure analysis.
	 *
	 * @param configuration
	 *            configuration for the collector
	 * @throws ConfigurationException
	 *             on configuration error
	 */
	public TeeTimeConfiguration(final kieker.common.configuration.Configuration configuration)
			throws ConfigurationException {
		SystemModelRepository systemModelRepository = new SystemModelRepository();
		
		LogsReaderCompositeStage reader = new LogsReaderCompositeStage(configuration);
				
		IEventMatcher<? extends AbstractTraceEvent>  traceEventMatcher = new ImplementsEventMatcher<>(AbstractTraceEvent.class, null);
		IEventMatcher<? extends OperationExecutionRecord> operationExecutionEventMatcher = new ImplementsEventMatcher<>(OperationExecutionRecord.class, null);
		
		DynamicEventDispatcher dispatcher = new DynamicEventDispatcher(operationExecutionEventMatcher, true, false, false);
		
		dispatcher.registerOutput(traceEventMatcher);
		
		ExecutionRecordTransformationStage executionRecordTransformationFilter = new ExecutionRecordTransformationStage(systemModelRepository);
		
		boolean ignoreInvalidTraces = true;
		long maxTraceDuration = Long.MAX_VALUE;
		
		TraceReconstructionStage traceReconstructionFilter = new TraceReconstructionStage(systemModelRepository, 
				TimeUnit.NANOSECONDS, ignoreInvalidTraces, maxTraceDuration);

		boolean repairEventBasedTraces = true;
		long maxTraceTimeout = Long.MAX_VALUE; // deactivate timeout and time input port

		EventRecordTraceReconstructionStage eventRecordTraceReconstructionFilter = new EventRecordTraceReconstructionStage(TimeUnit.NANOSECONDS, 
				repairEventBasedTraces, maxTraceDuration, maxTraceTimeout);

		boolean enhanceJavaContructors = true;
		boolean enhanceCallDetection = true;
		boolean ignoreAssumedCalls = false;

		TraceEventRecords2ExecutionAndMessageTraceStage ter2eamt = new TraceEventRecords2ExecutionAndMessageTraceStage(systemModelRepository, 
				enhanceJavaContructors, enhanceCallDetection, ignoreAssumedCalls);
		
		Merger<AbstractTrace> merger = new Merger<>();
		InputPort<AbstractTrace> traceReconstructorInputPort = merger.getNewInputPort();
		InputPort<AbstractTrace> ter2eamtExecutionInputPort = merger.getNewInputPort();
		InputPort<AbstractTrace> ter2eamtMessageInputPort = merger.getNewInputPort();
		
		int maxNumberOfEntries = 10000;
		// output
		ListCollectionFilter<AbstractTrace> listCollectionStage = new ListCollectionFilter<AbstractTrace>(maxNumberOfEntries, 
				kieker.analysis.stage.general.ListCollectionFilter.ListFullBehavior.DROP_OLDEST);	
		
		PrintOutputStage printOutputStage = new PrintOutputStage();
		
		
		// configuration
		this.connectPorts(reader.getOutputPort(), dispatcher.getInputPort());
		
		// OperationExecutionRecord
		this.connectPorts(operationExecutionEventMatcher.getOutputPort(), executionRecordTransformationFilter.getInputPort());
		this.connectPorts(executionRecordTransformationFilter.getOutputPort(), traceReconstructionFilter.getInputPort());
		this.connectPorts(traceReconstructionFilter.getExecutionTraceOutputPort(), traceReconstructorInputPort);
		
		// AbstractTraceEVent
		this.connectPorts(traceEventMatcher.getOutputPort(), eventRecordTraceReconstructionFilter.getTraceRecordsInputPort());
		this.connectPorts(eventRecordTraceReconstructionFilter.getValidTracesOutputPort(), ter2eamt.getInputPort());

		this.connectPorts(ter2eamt.getExecutionTraceOutputPort(), ter2eamtExecutionInputPort);
		this.connectPorts(ter2eamt.getMessageTraceOutputPort(), ter2eamtMessageInputPort);
		
		this.connectPorts(merger.getOutputPort(), listCollectionStage.getInputPort());
		this.connectPorts(listCollectionStage.getOutputPort(), printOutputStage.getInputPort());
	}

}
