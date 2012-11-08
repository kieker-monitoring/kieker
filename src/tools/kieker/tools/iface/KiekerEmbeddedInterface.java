/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.iface;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.executionRecordTransformation.ExecutionRecordTransformationFilter;
import kieker.tools.traceAnalysis.filter.sessionReconstruction.SessionReconstructionFilter;
import kieker.tools.traceAnalysis.filter.sessionReconstruction.SessionReconstructionFilterConfiguration;
import kieker.tools.traceAnalysis.filter.traceReconstruction.TraceReconstructionFilter;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.ExecutionTraceBasedSession;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * The Kieker embedded interface provides functions to conveniently access the Kieker analysis engine
 * from other applications.
 * 
 * @author Holger Knoche
 * 
 */
public class KiekerEmbeddedInterface {

	/**
	 * Prepares a runnable Kieker setup which reads execution traces from the given directories using the given
	 * trace reconstruction parameters.
	 * 
	 * @param maxTraceDuration
	 *            The maximum trace duration (in ms) to use for trace reconstruction
	 * @param inputPaths
	 *            The input directories to use
	 * @return A runnable Kieker setup which produces execution traces from the input files
	 * @throws AnalysisConfigurationException
	 *             If an erroneous configuration is detected
	 */
	public static RunnableKiekerSetup<ExecutionTrace> readExecutionTracesFromFilesystem(final int maxTraceDuration, final String... inputPaths)
			throws AnalysisConfigurationException {
		final AnalysisController analysisController = new AnalysisController();

		// Initialize and register the system model repository
		final SystemModelRepository systemModelRepository = new SystemModelRepository(new Configuration());
		analysisController.registerRepository(systemModelRepository);

		// Initialize and register the file system reader
		final Configuration fileSystemReaderConfiguration = new Configuration();
		fileSystemReaderConfiguration.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, Configuration.toProperty(inputPaths));
		final FSReader fileSystemReader = new FSReader(fileSystemReaderConfiguration);
		analysisController.registerReader(fileSystemReader);

		// Initialize, register, and connect the execution record reader
		final ExecutionRecordTransformationFilter executionRecordFilter = new ExecutionRecordTransformationFilter(new Configuration());
		analysisController.registerFilter(executionRecordFilter);
		analysisController.connect(executionRecordFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemModelRepository);
		analysisController.connect(fileSystemReader, FSReader.OUTPUT_PORT_NAME_RECORDS,
				executionRecordFilter, ExecutionRecordTransformationFilter.INPUT_PORT_NAME_RECORDS);

		// Initialize, register, and connect the trace reconstruction filter
		final Configuration traceReconstructorConfiguration = new Configuration();
		traceReconstructorConfiguration.setProperty(TraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION_MILLIS, String.valueOf(maxTraceDuration));
		final TraceReconstructionFilter traceReconstructionFilter = new TraceReconstructionFilter(traceReconstructorConfiguration);
		analysisController.registerFilter(traceReconstructionFilter);
		analysisController.connect(traceReconstructionFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemModelRepository);
		analysisController.connect(executionRecordFilter, ExecutionRecordTransformationFilter.OUTPUT_PORT_NAME_EXECUTIONS,
				traceReconstructionFilter, TraceReconstructionFilter.INPUT_PORT_NAME_EXECUTIONS);

		// Initialize, register and connect the list collection filter
		final ListCollectionFilter<ExecutionTrace> listCollectionFilter = new ListCollectionFilter<ExecutionTrace>(new Configuration());
		analysisController.registerFilter(listCollectionFilter);
		analysisController.connect(traceReconstructionFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
				listCollectionFilter, ListCollectionFilter.INPUT_PORT_NAME);

		return new RunnableKiekerSetup<ExecutionTrace>(analysisController, listCollectionFilter);
	}

	/**
	 * Prepares a runnable Kieker setup which reads execution trace-based sessions from the given directories using the given
	 * reconstruction parameters.
	 * 
	 * @param maxTraceDuration
	 *            The maximum trace duration (in ms) to use for trace reconstruction
	 * @param maxThinkTime
	 *            The maximum think time (in ms) to use for session reconstruction
	 * @param inputPaths
	 *            The input directories to use
	 * @return A runnable Kieker setup which produces execution trace-based sessions from the input files
	 * @throws AnalysisConfigurationException
	 *             If an erroneous configuration is detected
	 */
	public static RunnableKiekerSetup<ExecutionTraceBasedSession> readExecutionBasedSessionsFromFilesystem(final int maxTraceDuration, final long maxThinkTime,
			final String... inputPaths) throws AnalysisConfigurationException {
		final AnalysisController analysisController = new AnalysisController();

		// Initialize and register the file system reader
		final Configuration fileSystemReaderConfiguration = new Configuration();
		fileSystemReaderConfiguration.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, Configuration.toProperty(inputPaths));
		final FSReader fileSystemReader = new FSReader(fileSystemReaderConfiguration);
		analysisController.registerReader(fileSystemReader);

		// Initialize and register the system model repository
		final SystemModelRepository systemModelRepository = new SystemModelRepository(new Configuration());
		analysisController.registerRepository(systemModelRepository);

		// Initialize, register and connect the execution record transformation filter
		final ExecutionRecordTransformationFilter executionRecordTransformationFilter = new ExecutionRecordTransformationFilter(new Configuration());
		analysisController.registerFilter(executionRecordTransformationFilter);
		analysisController.connect(executionRecordTransformationFilter,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemModelRepository);
		analysisController.connect(fileSystemReader, FSReader.OUTPUT_PORT_NAME_RECORDS,
				executionRecordTransformationFilter, ExecutionRecordTransformationFilter.INPUT_PORT_NAME_RECORDS);

		// Initialize, register and connect the trace reconstruction filter
		final Configuration traceReconstructionFilterConfiguration = new Configuration();

		traceReconstructionFilterConfiguration.setProperty(TraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION_MILLIS,
				String.valueOf(maxTraceDuration));

		final TraceReconstructionFilter traceReconstructionFilter = new TraceReconstructionFilter(new Configuration());
		analysisController.registerFilter(traceReconstructionFilter);
		analysisController.connect(traceReconstructionFilter,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemModelRepository);
		analysisController.connect(executionRecordTransformationFilter, ExecutionRecordTransformationFilter.OUTPUT_PORT_NAME_EXECUTIONS,
				traceReconstructionFilter, TraceReconstructionFilter.INPUT_PORT_NAME_EXECUTIONS);

		// Initialize, register and connect the session reconstruction filter
		final SessionReconstructionFilterConfiguration sessionReconstructionFilterConfiguration =
				new SessionReconstructionFilterConfiguration(new Configuration());

		sessionReconstructionFilterConfiguration.setMaxThinkTime(maxThinkTime);

		final SessionReconstructionFilter sessionReconstructionFilter = new SessionReconstructionFilter(
				sessionReconstructionFilterConfiguration.getWrappedConfiguration());
		analysisController.registerFilter(sessionReconstructionFilter);
		analysisController.connect(traceReconstructionFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
				sessionReconstructionFilter, SessionReconstructionFilter.INPUT_PORT_NAME_EXECUTION_TRACES);

		// Initialize, register and connect the list collection filter
		final ListCollectionFilter<ExecutionTraceBasedSession> listCollectionFilter = new ListCollectionFilter<ExecutionTraceBasedSession>(new Configuration());
		analysisController.registerFilter(listCollectionFilter);
		analysisController.connect(sessionReconstructionFilter, SessionReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE_SESSIONS,
				listCollectionFilter, ListCollectionFilter.INPUT_PORT_NAME);

		return new RunnableKiekerSetup<ExecutionTraceBasedSession>(analysisController, listCollectionFilter);
	}

}
