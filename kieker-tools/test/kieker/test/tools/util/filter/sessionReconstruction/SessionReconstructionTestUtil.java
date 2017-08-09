/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.util.filter.sessionReconstruction;

import java.util.List;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.executionRecordTransformation.ExecutionRecordTransformationFilter;
import kieker.tools.traceAnalysis.filter.sessionReconstruction.SessionReconstructionFilter;
import kieker.tools.traceAnalysis.filter.traceReconstruction.TraceReconstructionFilter;
import kieker.tools.traceAnalysis.systemModel.ExecutionTraceBasedSession;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * Utility functions for the test of the {@link SessionReconstructionFilter}.
 * 
 * @author Holger Knoche
 * @since 1.10
 * 
 */
public class SessionReconstructionTestUtil { // NOPMD (UseSingleton) NOCS (MissingCtorCheck)

	private SessionReconstructionTestUtil() {
		// Util class should not be instantiated
	}

	/**
	 * Creates a new test setup that produces execution trace-based sessions from a list of execution records.
	 * 
	 * @param records
	 *            The records to process
	 * @param maxThinkTime
	 *            The maximum think time for session reconstruction
	 * @return A fully configured test setup
	 * @throws AnalysisConfigurationException
	 *             If an error occurs during configuration
	 */
	public static SessionReconstructionTestSetup<ExecutionTraceBasedSession> createSetup(final List<OperationExecutionRecord> records, final long maxThinkTime)
			throws AnalysisConfigurationException {
		final AnalysisController analysisController = new AnalysisController();

		// Initialize and register the list reader
		final ListReader<OperationExecutionRecord> listReader = new ListReader<OperationExecutionRecord>(new Configuration(), analysisController);
		listReader.addAllObjects(records);

		// Initialize and register the system model repository
		final SystemModelRepository systemModelRepository = new SystemModelRepository(new Configuration(), analysisController);

		// Initialize, register and connect the execution record transformation filter
		final ExecutionRecordTransformationFilter executionRecordTransformationFilter = new ExecutionRecordTransformationFilter(new Configuration(),
				analysisController);
		analysisController.connect(executionRecordTransformationFilter,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemModelRepository);
		analysisController.connect(listReader, ListReader.OUTPUT_PORT_NAME,
				executionRecordTransformationFilter, ExecutionRecordTransformationFilter.INPUT_PORT_NAME_RECORDS);

		// Initialize, register and connect the trace reconstruction filter
		final TraceReconstructionFilter traceReconstructionFilter = new TraceReconstructionFilter(new Configuration(), analysisController);
		analysisController.connect(traceReconstructionFilter,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemModelRepository);
		analysisController.connect(executionRecordTransformationFilter, ExecutionRecordTransformationFilter.OUTPUT_PORT_NAME_EXECUTIONS,
				traceReconstructionFilter, TraceReconstructionFilter.INPUT_PORT_NAME_EXECUTIONS);

		// Initialize, register and connect the session reconstruction filter
		final Configuration bareSessionReconstructionFilterConfiguration = new Configuration();
		bareSessionReconstructionFilterConfiguration.setProperty(SessionReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_THINK_TIME, Long.toString(maxThinkTime));

		final SessionReconstructionFilter sessionReconstructionFilter = new SessionReconstructionFilter(bareSessionReconstructionFilterConfiguration,
				analysisController);
		analysisController.connect(traceReconstructionFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
				sessionReconstructionFilter, SessionReconstructionFilter.INPUT_PORT_NAME_EXECUTION_TRACES);

		// Initialize, register and connect the list collection filter
		final ListCollectionFilter<ExecutionTraceBasedSession> listCollectionFilter = new ListCollectionFilter<ExecutionTraceBasedSession>(new Configuration(),
				analysisController);
		analysisController.connect(sessionReconstructionFilter, SessionReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE_SESSIONS,
				listCollectionFilter, ListCollectionFilter.INPUT_PORT_NAME);

		return new SessionReconstructionTestSetup<ExecutionTraceBasedSession>(analysisController, listCollectionFilter);
	}
}
