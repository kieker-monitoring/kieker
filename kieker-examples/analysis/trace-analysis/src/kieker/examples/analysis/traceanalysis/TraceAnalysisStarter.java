/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.flow.EventRecordTraceReconstructionFilter;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.executionRecordTransformation.ExecutionRecordTransformationFilter;
import kieker.tools.traceAnalysis.filter.flow.TraceEventRecords2ExecutionAndMessageTraceFilter;
import kieker.tools.traceAnalysis.filter.traceReconstruction.TraceReconstructionFilter;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * This class shows an example on how to do a trace analysis with different inputs (OperationExecutionRecords, TraceEvents).
 * Depending on the input the data is processed differently.
 *
 * @author Thomas Duellmann
 *
 * @since 1.12
 */
public class TraceAnalysisStarter {

	// Depending on the input, you have to change the input path and the reader configuration (commented with ALTERNATIVE)
	private static final String INPUT_MONITORING_LOG_OER = "./input-data/operation-execution-log/";
	// ALTERNATIVE
	// private static final String INPUT_MONITORING_LOG_TE = "./input-data/trace-event-log/";

	public static void main(final String[] args) throws IllegalStateException, AnalysisConfigurationException, InterruptedException {
		final AnalysisController analysisController = new AnalysisController();

		// OPTIONAL: Instead of using the ListCollectionFilter, you can define your own filter that reads the traces.
		final ListCollectionFilter<MessageTrace> list = new ListCollectionFilter<MessageTrace>(new Configuration(), analysisController);

		// Initialize and register the list reader
		final Configuration fsReaderConfig = new Configuration();

		// When using the OperationExecution log as input:
		fsReaderConfig.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, INPUT_MONITORING_LOG_OER);

		// ALTERNATIVE when using the TraceEvent log as input:
		// fsReaderConfig.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, INPUT_MONITORING_LOG_TE);

		final FSReader reader = new FSReader(fsReaderConfig, analysisController);

		// Initialize and register the system model repository
		final SystemModelRepository systemModelRepository = new SystemModelRepository(new Configuration(), analysisController);

		//
		// BEGIN OperationExecutionRecord path
		//

		// Initialize, register and connect the execution record transformation filter
		final ExecutionRecordTransformationFilter executionRecordTransformationFilter = new ExecutionRecordTransformationFilter(new Configuration(),
				analysisController);
		analysisController.connect(executionRecordTransformationFilter,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemModelRepository);
		analysisController.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS,
				executionRecordTransformationFilter, ExecutionRecordTransformationFilter.INPUT_PORT_NAME_RECORDS);

		// Initialize, register and connect the trace reconstruction filter
		final TraceReconstructionFilter traceReconstructionFilter = new TraceReconstructionFilter(new Configuration(), analysisController);
		analysisController.connect(traceReconstructionFilter,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemModelRepository);
		analysisController.connect(executionRecordTransformationFilter, ExecutionRecordTransformationFilter.OUTPUT_PORT_NAME_EXECUTIONS,
				traceReconstructionFilter, TraceReconstructionFilter.INPUT_PORT_NAME_EXECUTIONS);

		analysisController.connect(traceReconstructionFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, list, ListCollectionFilter.INPUT_PORT_NAME);
		//
		// END OperationExecutionRecord path
		//

		//
		// BEGIN TraceEventRecord path
		//
		final EventRecordTraceReconstructionFilter eventRecordTraceReconstructionFilter = new EventRecordTraceReconstructionFilter(new Configuration(),
				analysisController);
		analysisController.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS, eventRecordTraceReconstructionFilter,
				EventRecordTraceReconstructionFilter.INPUT_PORT_NAME_TRACE_RECORDS);

		// Read the TraceEventRecords and write the Execution- and MessageTraces to the output
		final TraceEventRecords2ExecutionAndMessageTraceFilter ter2eamt = new TraceEventRecords2ExecutionAndMessageTraceFilter(new Configuration(),
				analysisController);
		analysisController.connect(ter2eamt, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemModelRepository);
		analysisController.connect(eventRecordTraceReconstructionFilter, EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_VALID, ter2eamt,
				TraceEventRecords2ExecutionAndMessageTraceFilter.INPUT_PORT_NAME_EVENT_TRACE);

		// This can be added to also to also output the ExectionTraces:
		// analysisController.connect(ter2eamt, TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE, list,
		// ListCollectionFilter.INPUT_PORT_NAME);

		analysisController.connect(ter2eamt, TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, list,
				ListCollectionFilter.INPUT_PORT_NAME);
		//
		// END TraceEventRecord path
		//

		analysisController.run();

		for (final MessageTrace mt : list.getList()) {
			System.out.println(mt);
		}

	}
}
