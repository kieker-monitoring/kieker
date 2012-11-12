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

import java.util.List;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
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
	 * Produces a runnable Kieker setup which reads execution traces using the given reader.
	 * 
	 * @param reader
	 *            The reader to read the execution records from
	 * @param outputPortName
	 *            The reader's output port name
	 * @param maxTraceDuration
	 *            The maximal trace duration (in ms) to use for trace reconstruction
	 * @return A runnable Kieker setup using the given reader
	 * @throws AnalysisConfigurationException
	 *             If an erroneous configuration is detected
	 */
	private static RunnableKiekerSetup<ExecutionTrace> readExecutionTraces(final AbstractReaderPlugin reader, final String outputPortName,
			final int maxTraceDuration) throws AnalysisConfigurationException {
		final AnalysisController analysisController = new AnalysisController();

		// Initialize and register the system model repository
		final SystemModelRepository systemModelRepository = new SystemModelRepository(new Configuration());
		analysisController.registerRepository(systemModelRepository);

		// Register the given reader
		analysisController.registerReader(reader);

		// Initialize, register, and connect the execution record reader
		final ExecutionRecordTransformationFilter executionRecordFilter = new ExecutionRecordTransformationFilter(new Configuration());
		analysisController.registerFilter(executionRecordFilter);
		analysisController.connect(executionRecordFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemModelRepository);
		analysisController.connect(reader, outputPortName,
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
	 * Prepares a runnable Kieker setup which reads execution traces from the given directories using the given
	 * trace reconstruction parameters.
	 * 
	 * @param maxTraceDuration
	 *            The maximal trace duration (in ms) to use for trace reconstruction
	 * @param inputPaths
	 *            The input directories to use
	 * @return A runnable Kieker setup which produces execution traces from the input files
	 * @throws AnalysisConfigurationException
	 *             If an erroneous configuration is detected
	 */
	public static RunnableKiekerSetup<ExecutionTrace> readExecutionTracesFromFilesystem(final int maxTraceDuration, final String... inputPaths)
			throws AnalysisConfigurationException {
		// Initialize the file system reader
		final Configuration fileSystemReaderConfiguration = new Configuration();
		fileSystemReaderConfiguration.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, Configuration.toProperty(inputPaths));
		final FSReader fileSystemReader = new FSReader(fileSystemReaderConfiguration);

		return KiekerEmbeddedInterface.readExecutionTraces(fileSystemReader, FSReader.OUTPUT_PORT_NAME_RECORDS, maxTraceDuration);
	}

	/**
	 * Prepares a runnable Kieker setup which reads execution traces from the given list of operation execution
	 * records using the given trace reconstruction parameters.
	 * 
	 * @param maxTraceDuration
	 *            The maximal trace duration (in ms) to use for trace reconstruction
	 * @param records
	 *            The operation execution records to process
	 * @return A runnable Kieker setup which produces execution traces from the given records
	 * @throws AnalysisConfigurationException
	 *             If an erroneous configuration is detected
	 */
	public static RunnableKiekerSetup<ExecutionTrace> readExecutionTracesFromList(final int maxTraceDuration, final List<OperationExecutionRecord> records)
			throws AnalysisConfigurationException {
		final ListReader<OperationExecutionRecord> listReader = new ListReader<OperationExecutionRecord>(new Configuration());
		listReader.addAllObjects(records);

		return KiekerEmbeddedInterface.readExecutionTraces(listReader, ListReader.OUTPUT_PORT_NAME, maxTraceDuration);
	}

	/**
	 * Prepares a runnable Kieker setup which reads execution trace-based sessions using the given reader.
	 * 
	 * @param reader
	 *            The reader to read the operation execution records from
	 * @param outputPortName
	 *            The reader's output port name
	 * @param maxTraceDuration
	 *            The maximal trace duration (in ms) to use for trace reconstruction
	 * @param maxThinkTime
	 *            The maximal think time (in ms) to use for session reconstruction
	 * @return A runnable Kieker setup using the given reader
	 * @throws AnalysisConfigurationException
	 *             If an erroneous configuration is detected
	 */
	private static RunnableKiekerSetup<ExecutionTraceBasedSession> readExecutionBasedSessions(final AbstractReaderPlugin reader, final String outputPortName,
			final int maxTraceDuration, final long maxThinkTime) throws AnalysisConfigurationException {
		final AnalysisController analysisController = new AnalysisController();

		// Register the reader
		analysisController.registerReader(reader);

		// Initialize and register the system model repository
		final SystemModelRepository systemModelRepository = new SystemModelRepository(new Configuration());
		analysisController.registerRepository(systemModelRepository);

		// Initialize, register and connect the execution record transformation filter
		final ExecutionRecordTransformationFilter executionRecordTransformationFilter = new ExecutionRecordTransformationFilter(new Configuration());
		analysisController.registerFilter(executionRecordTransformationFilter);
		analysisController.connect(executionRecordTransformationFilter,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemModelRepository);
		analysisController.connect(reader, outputPortName,
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

	/**
	 * Prepares a runnable Kieker setup which reads execution trace-based sessions from the given directories using the given
	 * reconstruction parameters.
	 * 
	 * @param maxTraceDuration
	 *            The maximal trace duration (in ms) to use for trace reconstruction
	 * @param maxThinkTime
	 *            The maximal think time (in ms) to use for session reconstruction
	 * @param inputPaths
	 *            The input directories to use
	 * @return A runnable Kieker setup which produces execution trace-based sessions from the input files
	 * @throws AnalysisConfigurationException
	 *             If an erroneous configuration is detected
	 */
	public static RunnableKiekerSetup<ExecutionTraceBasedSession> readExecutionBasedSessionsFromFilesystem(final int maxTraceDuration, final long maxThinkTime,
			final String... inputPaths) throws AnalysisConfigurationException {
		// Initialize the file system reader
		final Configuration fileSystemReaderConfiguration = new Configuration();
		fileSystemReaderConfiguration.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, Configuration.toProperty(inputPaths));
		final FSReader fileSystemReader = new FSReader(fileSystemReaderConfiguration);

		return KiekerEmbeddedInterface.readExecutionBasedSessions(fileSystemReader, FSReader.OUTPUT_PORT_NAME_RECORDS, maxTraceDuration, maxThinkTime);
	}

	/**
	 * Prepares a runnable Kieker setup which reads execution trace-based sessios from the given list of operation execution records
	 * and using the given session reconstruction parameters.
	 * 
	 * @param maxTraceDuration
	 *            The maximal trace duration (in ms) to use for trace reconstruction
	 * @param maxThinkTime
	 *            The maximal think time (in ms) to use for session reconstruction
	 * @param records
	 *            The operation execution records to process
	 * @return A runnable Kieker setup which produces execution trace-based sessions from the given records
	 * @throws AnalysisConfigurationException
	 *             If an erroneous configuration is detected
	 */
	public static RunnableKiekerSetup<ExecutionTraceBasedSession> readExecutionBasedSessionsFromList(final int maxTraceDuration, final long maxThinkTime,
			final List<OperationExecutionRecord> records) throws AnalysisConfigurationException {
		// Initialize the list reader
		final ListReader<OperationExecutionRecord> listReader = new ListReader<OperationExecutionRecord>(new Configuration());
		listReader.addAllObjects(records);

		return KiekerEmbeddedInterface.readExecutionBasedSessions(listReader, ListReader.OUTPUT_PORT_NAME, maxTraceDuration, maxThinkTime);
	}
}
