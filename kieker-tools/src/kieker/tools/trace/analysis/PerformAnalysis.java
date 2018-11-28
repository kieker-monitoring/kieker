/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.trace.analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import kieker.analysis.AnalysisController;
import kieker.analysis.analysisComponent.AbstractAnalysisComponent;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.filter.flow.EventRecordTraceReconstructionFilter;
import kieker.analysis.plugin.filter.flow.ThreadEvent2TraceEventFilter;
import kieker.analysis.plugin.filter.select.TimestampFilter;
import kieker.analysis.plugin.filter.select.TraceIdFilter;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.tools.common.ConvertLegacyValues;
import kieker.tools.trace.analysis.filter.AbstractGraphProducingFilter;
import kieker.tools.trace.analysis.filter.AbstractMessageTraceProcessingFilter;
import kieker.tools.trace.analysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.trace.analysis.filter.AbstractTraceProcessingFilter;
import kieker.tools.trace.analysis.filter.IGraphOutputtingFilter;
import kieker.tools.trace.analysis.filter.executionRecordTransformation.ExecutionRecordTransformationFilter;
import kieker.tools.trace.analysis.filter.flow.EventRecordTraceCounter;
import kieker.tools.trace.analysis.filter.flow.TraceEventRecords2ExecutionAndMessageTraceFilter;
import kieker.tools.trace.analysis.filter.systemModel.SystemModel2FileFilter;
import kieker.tools.trace.analysis.filter.traceFilter.TraceEquivalenceClassFilter;
import kieker.tools.trace.analysis.filter.traceFilter.TraceEquivalenceClassFilter.TraceEquivalenceClassModes;
import kieker.tools.trace.analysis.filter.traceReconstruction.TraceReconstructionFilter;
import kieker.tools.trace.analysis.filter.traceWriter.ExecutionTraceWriterFilter;
import kieker.tools.trace.analysis.filter.traceWriter.InvalidExecutionTraceWriterFilter;
import kieker.tools.trace.analysis.filter.traceWriter.MessageTraceWriterFilter;
import kieker.tools.trace.analysis.filter.visualization.AbstractGraphFilter;
import kieker.tools.trace.analysis.filter.visualization.GraphWriterPlugin;
import kieker.tools.trace.analysis.filter.visualization.callTree.AbstractAggregatedCallTreeFilter;
import kieker.tools.trace.analysis.filter.visualization.callTree.AggregatedAllocationComponentOperationCallTreeFilter;
import kieker.tools.trace.analysis.filter.visualization.callTree.AggregatedAssemblyComponentOperationCallTreeFilter;
import kieker.tools.trace.analysis.filter.visualization.callTree.TraceCallTreeFilter;
import kieker.tools.trace.analysis.filter.visualization.dependencyGraph.AbstractDependencyGraphFilter;
import kieker.tools.trace.analysis.filter.visualization.dependencyGraph.ComponentDependencyGraphAllocationFilter;
import kieker.tools.trace.analysis.filter.visualization.dependencyGraph.ComponentDependencyGraphAssemblyFilter;
import kieker.tools.trace.analysis.filter.visualization.dependencyGraph.ContainerDependencyGraphFilter;
import kieker.tools.trace.analysis.filter.visualization.dependencyGraph.OperationDependencyGraphAllocationFilter;
import kieker.tools.trace.analysis.filter.visualization.dependencyGraph.OperationDependencyGraphAssemblyFilter;
import kieker.tools.trace.analysis.filter.visualization.dependencyGraph.ResponseTimeColorNodeDecorator;
import kieker.tools.trace.analysis.filter.visualization.dependencyGraph.ResponseTimeNodeDecorator;
import kieker.tools.trace.analysis.filter.visualization.descriptions.DescriptionDecoratorFilter;
import kieker.tools.trace.analysis.filter.visualization.sequenceDiagram.SequenceDiagramFilter;
import kieker.tools.trace.analysis.filter.visualization.traceColoring.TraceColoringFilter;
import kieker.tools.trace.analysis.repository.DescriptionRepository;
import kieker.tools.trace.analysis.repository.TraceColorRepository;
import kieker.tools.trace.analysis.systemModel.ExecutionTrace;
import kieker.tools.trace.analysis.systemModel.repository.SystemModelRepository;

/**
 * This is the main class to start the Kieker TraceAnalysisTool - the model synthesis and analysis tool to process the
 * monitoring data that comes from the instrumented system, or from a file that contains Kieker monitoring data. The
 * Kieker TraceAnalysisTool can produce output such as sequence diagrams, dependency graphs on demand. Alternatively it
 * can be used continuously for online performance analysis, anomaly detection or live visualization of system behavior.
 *
 * @author Andre van Hoorn, Matthias Rohr, Nils Christian Ehmke, Reiner Jung
 *
 * @since 0.95a
 */
public class PerformAnalysis {

	private static final String ENCODING = "UTF-8";
	private static final String TXT_SUFFIX = ".txt";

	private final AnalysisController analysisController = new AnalysisController();

	private final Logger logger;
	private final TraceAnalysisConfiguration settings;

	/**
	 * Create analysis configuration.
	 *
	 * @param logger
	 *            logger to be used
	 * @param settings
	 *            configuration settings
	 */
	public PerformAnalysis(final Logger logger, final TraceAnalysisConfiguration settings) {
		this.logger = logger;
		this.settings = settings;
	}

	/**
	 *
	 * @return false iff an error occurred
	 */
	public boolean dispatchTasks() {
		final String pathPrefix = this.computePrefix();

		int numRequestedTasks = 0;
		boolean successfulExecution = true;

		final SystemModelRepository systemEntityFactory = new SystemModelRepository(new Configuration(),
				this.analysisController);

		TraceReconstructionFilter mtReconstrFilter = null;
		EventRecordTraceCounter eventRecordTraceCounter = null;
		TraceEventRecords2ExecutionAndMessageTraceFilter traceEvents2ExecutionAndMessageTraceFilter = null;

		try {
			final TraceIdFilter traceIdFilter = this
					.createTraceIdFilter(this.createTimestampFilter(this.createThreadEvent2TraceEventFilter(this.createReader())));

			final ExecutionRecordTransformationFilter execRecTransformer = this.createExecutionRecordTransformationFilter(traceIdFilter, systemEntityFactory);
			mtReconstrFilter = this.createTraceReconstruction(execRecTransformer, systemEntityFactory);

			final EventRecordTraceReconstructionFilter eventTraceReconstructionFilter = this.createEventRecordTraceReconstruction(traceIdFilter);

			eventRecordTraceCounter = this.createEventRecordTraceCounter(eventTraceReconstructionFilter);

			traceEvents2ExecutionAndMessageTraceFilter = this.createTraceEvents2ExecutionAndMessageTraceFilter(eventTraceReconstructionFilter, systemEntityFactory);

			final List<AbstractTraceProcessingFilter> allTraceProcessingComponents = new ArrayList<>();
			final List<AbstractGraphProducingFilter<?>> allGraphProducers = new ArrayList<>();

			final TraceEquivalenceClassFilter traceAllocationEquivClassFilter = this.createPrintDeploymentEquivalenceClasses(allTraceProcessingComponents,
					mtReconstrFilter, traceEvents2ExecutionAndMessageTraceFilter,
					systemEntityFactory);

			final TraceEquivalenceClassFilter traceAssemblyEquivClassFilter = this.createPrintAssemblyEquivalenceClasses(allTraceProcessingComponents,
					mtReconstrFilter, traceEvents2ExecutionAndMessageTraceFilter,
					systemEntityFactory);

			// fill list of msgTraceProcessingComponents:
			if (this.settings.isPrintMessageTraces()) {
				numRequestedTasks++;
				this.createComponentPrintMsgTrace(allTraceProcessingComponents, pathPrefix, mtReconstrFilter,
						traceEvents2ExecutionAndMessageTraceFilter, systemEntityFactory);
			}

			if (this.settings.isPrintExecutionTraces()) {
				numRequestedTasks++;
				this.createComponentPrintExecTrace(allTraceProcessingComponents, pathPrefix, mtReconstrFilter,
						traceEvents2ExecutionAndMessageTraceFilter, systemEntityFactory);
			}

			if (this.settings.isPrintInvalidExecutionTraces()) {
				numRequestedTasks++;
				this.createPrintInvalidExecutionTraces(allTraceProcessingComponents, pathPrefix, mtReconstrFilter,
						traceEvents2ExecutionAndMessageTraceFilter, systemEntityFactory);
			}

			if (this.settings.isPlotDeploymentSequenceDiagrams()) {
				numRequestedTasks++;
				this.createPlotDeploymentSequenceDiagrams(allTraceProcessingComponents, pathPrefix, mtReconstrFilter,
						traceEvents2ExecutionAndMessageTraceFilter, systemEntityFactory);
			}

			if (this.settings.isPlotAssemblySequenceDiagrams()) {
				numRequestedTasks++;
				this.createPlotAssemblySequenceDiagrams(allTraceProcessingComponents, pathPrefix, mtReconstrFilter,
						traceEvents2ExecutionAndMessageTraceFilter, systemEntityFactory);

			}

			if (!this.settings.getPlotDeploymentComponentDependencyGraph().isEmpty()) {
				numRequestedTasks++;
				this.createPlotDeploymentComponentDependencyGraph(allTraceProcessingComponents, allGraphProducers, mtReconstrFilter,
						traceEvents2ExecutionAndMessageTraceFilter, systemEntityFactory);
			}

			if (!this.settings.getPlotAssemblyComponentDependencyGraph().isEmpty()) {
				numRequestedTasks++;
				this.createPlotAssemblyComponentDependencyGraph(allTraceProcessingComponents, allGraphProducers, mtReconstrFilter,
						traceEvents2ExecutionAndMessageTraceFilter, systemEntityFactory);

			}

			if (this.settings.isPlotContainerDependencyGraph()) {
				numRequestedTasks++;
				this.createPlotContainerDependencyGraph(allTraceProcessingComponents, allGraphProducers, mtReconstrFilter,
						traceEvents2ExecutionAndMessageTraceFilter, systemEntityFactory);
			}

			if (!this.settings.getPlotDeploymentOperationDependencyGraph().isEmpty()) {
				numRequestedTasks++;
				this.createPlotDeploymentOperationDependencyGraph(allTraceProcessingComponents, allGraphProducers, mtReconstrFilter,
						traceEvents2ExecutionAndMessageTraceFilter, systemEntityFactory);
			}

			if (!this.settings.getPlotAssemblyOperationDependencyGraph().isEmpty()) {
				numRequestedTasks++;
				this.createPlotAssemblyOperationDependencyGraph(allTraceProcessingComponents, allGraphProducers, mtReconstrFilter,
						traceEvents2ExecutionAndMessageTraceFilter, systemEntityFactory);
			}

			if (this.settings.isPlotCallTrees()) {
				numRequestedTasks++;
				final TraceCallTreeFilter componentPlotTraceCallTrees = this.createTraceCallTreeFilter(pathPrefix, systemEntityFactory, mtReconstrFilter,
						traceEvents2ExecutionAndMessageTraceFilter);

				allTraceProcessingComponents.add(componentPlotTraceCallTrees);
			}

			if (this.settings.isPlotAggregatedDeploymentCallTree()) {
				numRequestedTasks++;
				this.createPlotAggregatedDeploymentCallTree(allTraceProcessingComponents, pathPrefix, mtReconstrFilter,
						traceEvents2ExecutionAndMessageTraceFilter, systemEntityFactory);

			}

			if (this.settings.isPlotAggregatedAssemblyCallTree()) {
				numRequestedTasks++;
				final AggregatedAssemblyComponentOperationCallTreeFilter componentPlotAssemblyCallTree = this.createAggrAssemblyCompOpCallTreeFilter(pathPrefix,
						systemEntityFactory,
						mtReconstrFilter, traceEvents2ExecutionAndMessageTraceFilter);

				allTraceProcessingComponents.add(componentPlotAssemblyCallTree);
			}
			if (this.settings.isPrintDeploymentEquivalenceClasses()) {
				numRequestedTasks++;
				// the actual execution of the task is performed below
			}
			if (this.settings.isPrintSystemModel()) {
				numRequestedTasks++;
			}

			// Attach graph processors to the graph producers
			this.attachGraphProcessors(pathPrefix, allGraphProducers);

			if (numRequestedTasks == 0) {
				this.logger.error("No task requested");
				this.logger.info("Use the option `--help` for usage information");
				return false;
			}

			this.printSystemEntities(pathPrefix, systemEntityFactory);

			successfulExecution = this.checkTerminationState(pathPrefix, allTraceProcessingComponents);

			if (successfulExecution && this.settings.isPrintDeploymentEquivalenceClasses()) {
				successfulExecution = this.writeTraceEquivalenceReport(
						pathPrefix + Constants.TRACE_ALLOCATION_EQUIV_CLASSES_FN_PREFIX + TXT_SUFFIX,
						traceAllocationEquivClassFilter);
			}

			if (successfulExecution && this.settings.isPrintAssemblyEquivalenceClasses()) {
				successfulExecution = this.writeTraceEquivalenceReport(
						pathPrefix + Constants.TRACE_ASSEMBLY_EQUIV_CLASSES_FN_PREFIX + TXT_SUFFIX,
						traceAssemblyEquivClassFilter);
			}
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			this.logger.error("An error occured", ex);
			successfulExecution = false;
		} finally {
			if (numRequestedTasks > 0) {
				if (mtReconstrFilter != null) {
					mtReconstrFilter.printStatusMessage();
				}
				if (eventRecordTraceCounter != null) {
					eventRecordTraceCounter.printStatusMessage();
				}
				if (traceEvents2ExecutionAndMessageTraceFilter != null) {
					traceEvents2ExecutionAndMessageTraceFilter.printStatusMessage();
				}
			}
		}

		return successfulExecution;
	}

	private boolean checkTerminationState(final String pathPrefix, final List<AbstractTraceProcessingFilter> allTraceProcessingComponents)
			throws Exception {
		boolean retVal = true;

		int numErrorCount = 0;
		try {
			this.analysisController.run();
			if (this.analysisController.getState() != AnalysisController.STATE.TERMINATED) {
				// Analysis did not terminate successfully
				retVal = false; // Error message referring to log will be printed later
				this.logger.error("Analysis instance terminated in state other than {}: {}", AnalysisController.STATE.TERMINATED,
						this.analysisController.getState());
			}
		} finally {
			for (final AbstractTraceProcessingFilter c : allTraceProcessingComponents) {
				numErrorCount += c.getErrorCount();
				c.printStatusMessage();
			}
			final String kaxOutputFn = pathPrefix + "traceAnalysis.kax";
			final File kaxOutputFile = new File(kaxOutputFn);
			try { // NOCS (nested try)
					// Try to serialize analysis configuration to .kax file
				this.analysisController.saveToFile(kaxOutputFile);
				this.logger.info("Saved analysis configuration to file '{}'", kaxOutputFile.getCanonicalPath());
			} catch (final IOException ex) {
				this.logger.error("Failed to save analysis configuration to file '{}'", kaxOutputFile.getCanonicalPath());
			}
		}
		if (!this.settings.isIgnoreInvalidTraces() && (numErrorCount > 0)) {
			throw new Exception(numErrorCount + " errors occured in trace processing components");
		}

		return retVal;
	}

	private void printSystemEntities(final String pathPrefix, final SystemModelRepository systemEntityFactory)
			throws IllegalStateException, AnalysisConfigurationException {
		final String systemEntitiesHtmlFn = pathPrefix + "system-entities.html";
		final Configuration systemModel2FileFilterConfig = new Configuration();
		systemModel2FileFilterConfig.setProperty(SystemModel2FileFilter.CONFIG_PROPERTY_NAME_HTML_OUTPUT_FN,
				systemEntitiesHtmlFn);
		final SystemModel2FileFilter systemModel2FileFilter = new SystemModel2FileFilter(
				systemModel2FileFilterConfig, this.analysisController);
		// note that this plugin is (currently) not connected to any other filters
		this.analysisController.connect(systemModel2FileFilter,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);
	}

	private String computePrefix() {
		if (this.settings.getOutputDir() != null) {
			if (this.settings.getPrefix() != null) {
				return this.settings.getOutputDir() + File.separator + this.settings.getPrefix();
			} else {
				return this.settings.getOutputDir() + File.separator;
			}
		} else if (this.settings.getPrefix() != null) {
			return this.settings.getPrefix();
		} else {
			return "";
		}
	}

	/**
	 *
	 * @param reader
	 * @return
	 * @throws IllegalStateException
	 * @throws AnalysisConfigurationException
	 */
	private ThreadEvent2TraceEventFilter createThreadEvent2TraceEventFilter(final FSReader reader) throws IllegalStateException, AnalysisConfigurationException {
		// transforms thread-based events to trace-based events
		final ThreadEvent2TraceEventFilter threadEvent2TraceEventFilter = new ThreadEvent2TraceEventFilter(new Configuration(),
				this.analysisController);

		this.analysisController.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS, threadEvent2TraceEventFilter,
				ThreadEvent2TraceEventFilter.INPUT_PORT_NAME_DEFAULT);

		return threadEvent2TraceEventFilter;
	}

	/**
	 *
	 * @param allTraceProcessingComponents
	 * @param pathPrefix
	 * @param mtReconstrFilter
	 * @param traceEvents2ExecutionAndMessageTraceFilter
	 * @param systemEntityFactory
	 * @throws IllegalStateException
	 * @throws AnalysisConfigurationException
	 */
	private void createPlotAggregatedDeploymentCallTree(final List<AbstractTraceProcessingFilter> allTraceProcessingComponents,
			final String pathPrefix, final TraceReconstructionFilter mtReconstrFilter,
			final TraceEventRecords2ExecutionAndMessageTraceFilter traceEvents2ExecutionAndMessageTraceFilter, final SystemModelRepository systemEntityFactory)
			throws IllegalStateException, AnalysisConfigurationException {
		final Configuration componentPlotAggregatedCallTreeConfig = new Configuration();

		componentPlotAggregatedCallTreeConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
				Constants.PLOTAGGREGATEDALLOCATIONCALLTREE_COMPONENT_NAME);
		componentPlotAggregatedCallTreeConfig.setProperty(
				AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, Boolean.toString(true));
		componentPlotAggregatedCallTreeConfig.setProperty(
				AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_SHORT_LABELS,
				this.booleanToString(this.settings.isShortLabels()));
		componentPlotAggregatedCallTreeConfig.setProperty(
				AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_OUTPUT_FILENAME,
				pathPrefix + Constants.AGGREGATED_ALLOCATION_CALL_TREE_FN_PREFIX + ".dot");

		final AggregatedAllocationComponentOperationCallTreeFilter componentPlotAggregatedCallTree = new AggregatedAllocationComponentOperationCallTreeFilter(
				componentPlotAggregatedCallTreeConfig, this.analysisController);

		this.analysisController.connect(mtReconstrFilter,
				TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, componentPlotAggregatedCallTree,
				AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
		this.analysisController.connect(traceEvents2ExecutionAndMessageTraceFilter,
				TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
				componentPlotAggregatedCallTree,
				AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
		this.analysisController.connect(componentPlotAggregatedCallTree,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);

		allTraceProcessingComponents.add(componentPlotAggregatedCallTree);
	}

	private void createPlotAssemblyOperationDependencyGraph(final List<AbstractTraceProcessingFilter> allTraceProcessingComponents,
			final List<AbstractGraphProducingFilter<?>> allGraphProducers, final TraceReconstructionFilter mtReconstrFilter,
			final TraceEventRecords2ExecutionAndMessageTraceFilter traceEvents2ExecutionAndMessageTraceFilter, final SystemModelRepository systemEntityFactory)
			throws IllegalStateException, AnalysisConfigurationException {
		final Configuration configuration = new Configuration();

		final OperationDependencyGraphAssemblyFilter componentPlotAssemblyOperationDepGraph = new OperationDependencyGraphAssemblyFilter(configuration,
				this.analysisController);

		this.addDecorators(this.settings.getPlotAssemblyOperationDependencyGraph(), componentPlotAssemblyOperationDepGraph);

		this.analysisController.connect(mtReconstrFilter,
				TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
				componentPlotAssemblyOperationDepGraph,
				AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
		this.analysisController.connect(traceEvents2ExecutionAndMessageTraceFilter,
				TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
				componentPlotAssemblyOperationDepGraph,
				AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
		this.analysisController.connect(componentPlotAssemblyOperationDepGraph,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);

		allTraceProcessingComponents.add(componentPlotAssemblyOperationDepGraph);
		allGraphProducers.add(componentPlotAssemblyOperationDepGraph);
	}

	/**
	 *
	 * @param allTraceProcessingComponents
	 * @param allGraphProducers
	 * @param mtReconstrFilter
	 * @param traceEvents2ExecutionAndMessageTraceFilter
	 * @param systemEntityFactory
	 * @throws IllegalStateException
	 * @throws AnalysisConfigurationException
	 */
	private void createPlotDeploymentOperationDependencyGraph(final List<AbstractTraceProcessingFilter> allTraceProcessingComponents,
			final List<AbstractGraphProducingFilter<?>> allGraphProducers, final TraceReconstructionFilter mtReconstrFilter,
			final TraceEventRecords2ExecutionAndMessageTraceFilter traceEvents2ExecutionAndMessageTraceFilter, final SystemModelRepository systemEntityFactory)
			throws IllegalStateException, AnalysisConfigurationException {
		final Configuration configuration = new Configuration();
		final OperationDependencyGraphAllocationFilter componentPlotAllocationOperationDepGraph = new OperationDependencyGraphAllocationFilter(configuration,
				this.analysisController);

		this.addDecorators(this.settings.getPlotDeploymentOperationDependencyGraph(), componentPlotAllocationOperationDepGraph);

		this.analysisController.connect(mtReconstrFilter,
				TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
				componentPlotAllocationOperationDepGraph,
				AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
		this.analysisController.connect(traceEvents2ExecutionAndMessageTraceFilter,
				TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
				componentPlotAllocationOperationDepGraph,
				AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
		this.analysisController.connect(componentPlotAllocationOperationDepGraph,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);

		allTraceProcessingComponents.add(componentPlotAllocationOperationDepGraph);
		allGraphProducers.add(componentPlotAllocationOperationDepGraph);
	}

	/**
	 *
	 * @param allTraceProcessingComponents
	 * @param allGraphProducers
	 * @param mtReconstrFilter
	 * @param traceEvents2ExecutionAndMessageTraceFilter
	 * @param systemEntityFactory
	 * @throws IllegalStateException
	 * @throws AnalysisConfigurationException
	 */
	private void createPlotContainerDependencyGraph(final List<AbstractTraceProcessingFilter> allTraceProcessingComponents,
			final List<AbstractGraphProducingFilter<?>> allGraphProducers, final TraceReconstructionFilter mtReconstrFilter,
			final TraceEventRecords2ExecutionAndMessageTraceFilter traceEvents2ExecutionAndMessageTraceFilter, final SystemModelRepository systemEntityFactory)
			throws IllegalStateException, AnalysisConfigurationException {
		final Configuration configuration = new Configuration();

		final ContainerDependencyGraphFilter componentPlotContainerDepGraph = new ContainerDependencyGraphFilter(configuration,
				this.analysisController);

		this.analysisController.connect(mtReconstrFilter,
				TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, componentPlotContainerDepGraph,
				AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
		this.analysisController.connect(traceEvents2ExecutionAndMessageTraceFilter,
				TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
				componentPlotContainerDepGraph,
				AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
		this.analysisController.connect(componentPlotContainerDepGraph,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);
		allTraceProcessingComponents.add(componentPlotContainerDepGraph);
		allGraphProducers.add(componentPlotContainerDepGraph);
	}

	/**
	 *
	 * @param allTraceProcessingComponents
	 * @param allGraphProducers
	 * @param mtReconstrFilter
	 * @param traceEvents2ExecutionAndMessageTraceFilter
	 * @param systemEntityFactory
	 * @throws IllegalStateException
	 * @throws AnalysisConfigurationException
	 */
	private void createPlotAssemblyComponentDependencyGraph(final List<AbstractTraceProcessingFilter> allTraceProcessingComponents,
			final List<AbstractGraphProducingFilter<?>> allGraphProducers, final TraceReconstructionFilter mtReconstrFilter,
			final TraceEventRecords2ExecutionAndMessageTraceFilter traceEvents2ExecutionAndMessageTraceFilter, final SystemModelRepository systemEntityFactory)
			throws IllegalStateException, AnalysisConfigurationException {
		final Configuration configuration = new Configuration();

		final ComponentDependencyGraphAssemblyFilter componentPlotAssemblyComponentDepGraph = new ComponentDependencyGraphAssemblyFilter(configuration,
				this.analysisController);

		this.addDecorators(this.settings.getPlotAssemblyComponentDependencyGraph(), componentPlotAssemblyComponentDepGraph);

		this.analysisController.connect(mtReconstrFilter,
				TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
				componentPlotAssemblyComponentDepGraph,
				AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
		this.analysisController.connect(traceEvents2ExecutionAndMessageTraceFilter,
				TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
				componentPlotAssemblyComponentDepGraph,
				AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
		this.analysisController.connect(componentPlotAssemblyComponentDepGraph,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);
		allTraceProcessingComponents.add(componentPlotAssemblyComponentDepGraph);
		allGraphProducers.add(componentPlotAssemblyComponentDepGraph);
	}

	/**
	 *
	 * @param allTraceProcessingComponents
	 * @param allGraphProducers
	 * @param mtReconstrFilter
	 * @param traceEvents2ExecutionAndMessageTraceFilter
	 * @param systemEntityFactory
	 * @throws IllegalStateException
	 * @throws AnalysisConfigurationException
	 */
	private void createPlotDeploymentComponentDependencyGraph(final List<AbstractTraceProcessingFilter> allTraceProcessingComponents,
			final List<AbstractGraphProducingFilter<?>> allGraphProducers,
			final TraceReconstructionFilter mtReconstrFilter, final TraceEventRecords2ExecutionAndMessageTraceFilter traceEvents2ExecutionAndMessageTraceFilter,
			final SystemModelRepository systemEntityFactory) throws IllegalStateException, AnalysisConfigurationException {
		final Configuration configuration = new Configuration();
		final ComponentDependencyGraphAllocationFilter componentPlotAllocationComponentDepGraph = new ComponentDependencyGraphAllocationFilter(configuration,
				this.analysisController);

		this.addDecorators(this.settings.getPlotDeploymentComponentDependencyGraph(), componentPlotAllocationComponentDepGraph);

		this.analysisController.connect(mtReconstrFilter,
				TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
				componentPlotAllocationComponentDepGraph,
				AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
		this.analysisController.connect(traceEvents2ExecutionAndMessageTraceFilter,
				TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
				componentPlotAllocationComponentDepGraph,
				AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
		this.analysisController.connect(componentPlotAllocationComponentDepGraph,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);

		allTraceProcessingComponents.add(componentPlotAllocationComponentDepGraph);
		allGraphProducers.add(componentPlotAllocationComponentDepGraph);
	}

	/**
	 *
	 * @param allTraceProcessingComponents
	 * @param pathPrefix
	 * @param mtReconstrFilter
	 * @param traceEvents2ExecutionAndMessageTraceFilter
	 * @param systemEntityFactory
	 * @throws IllegalStateException
	 * @throws AnalysisConfigurationException
	 */
	private void createPlotAssemblySequenceDiagrams(final List<AbstractTraceProcessingFilter> allTraceProcessingComponents, final String pathPrefix,
			final TraceReconstructionFilter mtReconstrFilter, final TraceEventRecords2ExecutionAndMessageTraceFilter traceEvents2ExecutionAndMessageTraceFilter,
			final SystemModelRepository systemEntityFactory) throws IllegalStateException, AnalysisConfigurationException {
		final Configuration componentPlotAssemblySeqDiagrConfig = new Configuration();

		componentPlotAssemblySeqDiagrConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
				Constants.PLOTASSEMBLYSEQDIAGR_COMPONENT_NAME);
		componentPlotAssemblySeqDiagrConfig
				.setProperty(SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN_BASE, pathPrefix + Constants.ASSEMBLY_SEQUENCE_DIAGRAM_FN_PREFIX);
		componentPlotAssemblySeqDiagrConfig.setProperty(
				SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_SDMODE,
				SequenceDiagramFilter.SDModes.ASSEMBLY.toString());
		componentPlotAssemblySeqDiagrConfig.setProperty(
				SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_SHORTLABES,
				this.booleanToString(this.settings.isShortLabels()));

		final SequenceDiagramFilter componentPlotAssemblySeqDiagr = new SequenceDiagramFilter(componentPlotAssemblySeqDiagrConfig,
				this.analysisController);

		this.analysisController.connect(mtReconstrFilter,
				TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, componentPlotAssemblySeqDiagr,
				AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
		this.analysisController.connect(traceEvents2ExecutionAndMessageTraceFilter,
				TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
				componentPlotAssemblySeqDiagr,
				AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
		this.analysisController.connect(componentPlotAssemblySeqDiagr,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);

		allTraceProcessingComponents.add(componentPlotAssemblySeqDiagr);
	}

	private void createPlotDeploymentSequenceDiagrams(final List<AbstractTraceProcessingFilter> allTraceProcessingComponents, final String pathPrefix,
			final TraceReconstructionFilter mtReconstrFilter,
			final TraceEventRecords2ExecutionAndMessageTraceFilter traceEvents2ExecutionAndMessageTraceFilter, final SystemModelRepository systemEntityFactory)
			throws IllegalStateException, AnalysisConfigurationException {
		final Configuration componentPlotAllocationSeqDiagrConfig = new Configuration();
		componentPlotAllocationSeqDiagrConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
				Constants.PLOTALLOCATIONSEQDIAGR_COMPONENT_NAME);
		componentPlotAllocationSeqDiagrConfig.setProperty(
				SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN_BASE, pathPrefix + Constants.ALLOCATION_SEQUENCE_DIAGRAM_FN_PREFIX);
		componentPlotAllocationSeqDiagrConfig.setProperty(
				SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_SDMODE,
				SequenceDiagramFilter.SDModes.ALLOCATION.toString());
		componentPlotAllocationSeqDiagrConfig.setProperty(
				SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_SHORTLABES,
				this.booleanToString(this.settings.isShortLabels()));

		final SequenceDiagramFilter componentPlotAllocationSeqDiagr = new SequenceDiagramFilter(componentPlotAllocationSeqDiagrConfig,
				this.analysisController);

		this.analysisController.connect(mtReconstrFilter,
				TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, componentPlotAllocationSeqDiagr,
				AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
		this.analysisController.connect(traceEvents2ExecutionAndMessageTraceFilter,
				TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
				componentPlotAllocationSeqDiagr,
				AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
		this.analysisController.connect(componentPlotAllocationSeqDiagr,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);
		allTraceProcessingComponents.add(componentPlotAllocationSeqDiagr);
	}

	/**
	 *
	 * @param allTraceProcessingComponents
	 * @param pathPrefix
	 * @param mtReconstrFilter
	 * @param traceEvents2ExecutionAndMessageTraceFilter
	 * @param systemEntityFactory
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws AnalysisConfigurationException
	 */
	private void createPrintInvalidExecutionTraces(final List<AbstractTraceProcessingFilter> allTraceProcessingComponents, final String pathPrefix,
			final TraceReconstructionFilter mtReconstrFilter,
			final TraceEventRecords2ExecutionAndMessageTraceFilter traceEvents2ExecutionAndMessageTraceFilter, final SystemModelRepository systemEntityFactory)
			throws IOException, IllegalStateException, AnalysisConfigurationException {
		final Configuration componentPrintInvalidTraceConfig = new Configuration();
		componentPrintInvalidTraceConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
				Constants.PRINTINVALIDEXECTRACE_COMPONENT_NAME);
		componentPrintInvalidTraceConfig.setProperty(
				InvalidExecutionTraceWriterFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN,
				new File(pathPrefix + Constants.INVALID_TRACES_FN_PREFIX + TXT_SUFFIX).getCanonicalPath());

		final InvalidExecutionTraceWriterFilter componentPrintInvalidTrace = new InvalidExecutionTraceWriterFilter(componentPrintInvalidTraceConfig,
				this.analysisController);

		this.analysisController.connect(mtReconstrFilter,
				TraceReconstructionFilter.OUTPUT_PORT_NAME_INVALID_EXECUTION_TRACE, componentPrintInvalidTrace,
				InvalidExecutionTraceWriterFilter.INPUT_PORT_NAME_INVALID_EXECUTION_TRACES);
		this.analysisController.connect(componentPrintInvalidTrace,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);
		this.analysisController.connect(traceEvents2ExecutionAndMessageTraceFilter,
				TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_INVALID_EXECUTION_TRACE,
				componentPrintInvalidTrace,
				InvalidExecutionTraceWriterFilter.INPUT_PORT_NAME_INVALID_EXECUTION_TRACES);

		allTraceProcessingComponents.add(componentPrintInvalidTrace);
	}

	/**
	 *
	 * @param allTraceProcessingComponents
	 * @param pathPrefix
	 * @param mtReconstrFilter
	 * @param traceEvents2ExecutionAndMessageTraceFilter
	 * @param systemEntityFactory
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws AnalysisConfigurationException
	 */
	private void createComponentPrintExecTrace(final List<AbstractTraceProcessingFilter> allTraceProcessingComponents, final String pathPrefix,
			final TraceReconstructionFilter mtReconstrFilter,
			final TraceEventRecords2ExecutionAndMessageTraceFilter traceEvents2ExecutionAndMessageTraceFilter, final SystemModelRepository systemEntityFactory)
			throws IOException, IllegalStateException, AnalysisConfigurationException {
		final Configuration componentPrintExecTraceConfig = new Configuration();
		componentPrintExecTraceConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
				Constants.PRINTEXECTRACE_COMPONENT_NAME);
		componentPrintExecTraceConfig.setProperty(ExecutionTraceWriterFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN,
				new File(pathPrefix + Constants.EXECUTION_TRACES_FN_PREFIX + TXT_SUFFIX).getCanonicalPath());

		final ExecutionTraceWriterFilter componentPrintExecTrace = new ExecutionTraceWriterFilter(componentPrintExecTraceConfig,
				this.analysisController);

		this.analysisController.connect(mtReconstrFilter,
				TraceReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE, componentPrintExecTrace,
				ExecutionTraceWriterFilter.INPUT_PORT_NAME_EXECUTION_TRACES);
		this.analysisController.connect(traceEvents2ExecutionAndMessageTraceFilter,
				TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
				componentPrintExecTrace, ExecutionTraceWriterFilter.INPUT_PORT_NAME_EXECUTION_TRACES);
		this.analysisController.connect(componentPrintExecTrace,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);

		allTraceProcessingComponents.add(componentPrintExecTrace);
	}

	/**
	 *
	 * @param allTraceProcessingComponents
	 * @param pathPrefix
	 * @param mtReconstrFilter
	 * @param traceEvents2ExecutionAndMessageTraceFilter
	 * @param systemEntityFactory
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws AnalysisConfigurationException
	 */
	private void createComponentPrintMsgTrace(final List<AbstractTraceProcessingFilter> allTraceProcessingComponents, final String pathPrefix,
			final TraceReconstructionFilter mtReconstrFilter, final TraceEventRecords2ExecutionAndMessageTraceFilter traceEvents2ExecutionAndMessageTraceFilter,
			final SystemModelRepository systemEntityFactory)
			throws IOException, IllegalStateException, AnalysisConfigurationException {
		final Configuration componentPrintMsgTraceConfig = new Configuration();
		componentPrintMsgTraceConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
				Constants.PRINTMSGTRACE_COMPONENT_NAME);
		componentPrintMsgTraceConfig.setProperty(MessageTraceWriterFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN,
				new File(pathPrefix + Constants.MESSAGE_TRACES_FN_PREFIX + TXT_SUFFIX).getCanonicalPath());

		final MessageTraceWriterFilter componentPrintMsgTrace = new MessageTraceWriterFilter(componentPrintMsgTraceConfig,
				this.analysisController);

		this.analysisController.connect(mtReconstrFilter,
				TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, componentPrintMsgTrace,
				AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
		this.analysisController.connect(traceEvents2ExecutionAndMessageTraceFilter,
				TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
				componentPrintMsgTrace, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
		this.analysisController.connect(componentPrintMsgTrace,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);

		allTraceProcessingComponents.add(componentPrintMsgTrace);
	}

	/**
	 *
	 * @param allTraceProcessingComponents
	 * @param mtReconstrFilter
	 * @param transformTraceEventsFilter
	 * @param systemEntityFactory
	 * @throws IllegalStateException
	 * @throws AnalysisConfigurationException
	 */
	private TraceEquivalenceClassFilter createPrintAssemblyEquivalenceClasses(final List<AbstractTraceProcessingFilter> allTraceProcessingComponents,
			final TraceReconstructionFilter mtReconstrFilter,
			final TraceEventRecords2ExecutionAndMessageTraceFilter transformTraceEventsFilter, final SystemModelRepository systemEntityFactory)
			throws IllegalStateException, AnalysisConfigurationException {
		final Configuration traceAssemblyEquivClassFilterConfig = new Configuration();
		traceAssemblyEquivClassFilterConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
				Constants.TRACEASSEMBLYEQUIVCLASS_COMPONENT_NAME);
		traceAssemblyEquivClassFilterConfig.setProperty(
				TraceEquivalenceClassFilter.CONFIG_PROPERTY_NAME_EQUIVALENCE_MODE,
				TraceEquivalenceClassModes.ASSEMBLY.toString());

		TraceEquivalenceClassFilter traceAssemblyEquivClassFilter = null; // must not be instantiate it here, due to
																			// side-effects in the constructor
		if (this.settings.isPrintAssemblyEquivalenceClasses()) {
			/**
			 * Currently, this filter is only used to print an equivalence report. That's why we only activate it in
			 * case this options is requested.
			 */
			traceAssemblyEquivClassFilter = new TraceEquivalenceClassFilter(traceAssemblyEquivClassFilterConfig,
					this.analysisController);
			this.analysisController.connect(mtReconstrFilter,
					TraceReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE, traceAssemblyEquivClassFilter,
					TraceEquivalenceClassFilter.INPUT_PORT_NAME_EXECUTION_TRACE);
			this.analysisController.connect(transformTraceEventsFilter,
					TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
					traceAssemblyEquivClassFilter, TraceEquivalenceClassFilter.INPUT_PORT_NAME_EXECUTION_TRACE);
			this.analysisController.connect(traceAssemblyEquivClassFilter,
					AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);
			allTraceProcessingComponents.add(traceAssemblyEquivClassFilter);
		}

		return traceAssemblyEquivClassFilter;
	}

	/**
	 *
	 * @param allTraceProcessingComponents
	 * @param mtReconstrFilter
	 * @param traceEvents2ExecutionAndMessageTraceFilter
	 * @param systemEntityFactory
	 *
	 * @throws AnalysisConfigurationException
	 * @throws IllegalStateException
	 */
	private TraceEquivalenceClassFilter createPrintDeploymentEquivalenceClasses(final List<AbstractTraceProcessingFilter> allTraceProcessingComponents,
			final TraceReconstructionFilter mtReconstrFilter,
			final TraceEventRecords2ExecutionAndMessageTraceFilter traceEvents2ExecutionAndMessageTraceFilter, final AbstractRepository systemEntityFactory)
			throws IllegalStateException, AnalysisConfigurationException {
		final Configuration traceAllocationEquivClassFilterConfig = new Configuration();

		traceAllocationEquivClassFilterConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
				Constants.TRACEALLOCATIONEQUIVCLASS_COMPONENT_NAME);
		traceAllocationEquivClassFilterConfig.setProperty(
				TraceEquivalenceClassFilter.CONFIG_PROPERTY_NAME_EQUIVALENCE_MODE,
				TraceEquivalenceClassModes.ALLOCATION.toString());
		TraceEquivalenceClassFilter traceAllocationEquivClassFilter = null; // must not be instantiate it here, due
																			// to side-effects in the constructor
		if (this.settings.isPrintDeploymentEquivalenceClasses()) {
			/**
			 * Currently, this filter is only used to print an equivalence report. That's why we only activate it in
			 * case this options is requested.
			 */
			traceAllocationEquivClassFilter = new TraceEquivalenceClassFilter(traceAllocationEquivClassFilterConfig,
					this.analysisController);

			this.analysisController.connect(traceAllocationEquivClassFilter,
					AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);
			this.analysisController.connect(mtReconstrFilter,
					TraceReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE, traceAllocationEquivClassFilter,
					TraceEquivalenceClassFilter.INPUT_PORT_NAME_EXECUTION_TRACE);
			this.analysisController.connect(traceEvents2ExecutionAndMessageTraceFilter,
					TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
					traceAllocationEquivClassFilter, TraceEquivalenceClassFilter.INPUT_PORT_NAME_EXECUTION_TRACE);

			allTraceProcessingComponents.add(traceAllocationEquivClassFilter);
		}

		return traceAllocationEquivClassFilter;
	}

	/**
	 *
	 * @param eventTraceReconstructionFilter
	 * @param systemEntityFactory
	 * @return
	 * @throws AnalysisConfigurationException
	 * @throws IllegalStateException
	 */
	private TraceEventRecords2ExecutionAndMessageTraceFilter createTraceEvents2ExecutionAndMessageTraceFilter(
			final EventRecordTraceReconstructionFilter eventTraceReconstructionFilter, final AbstractRepository systemEntityFactory)
			throws IllegalStateException, AnalysisConfigurationException {
		// Create the event trace to execution/message trace transformation filter and connect its input to the
		// event record trace generation filter's output
		// port
		final Configuration configurationEventTrace2ExecutionTraceFilter = new Configuration();

		configurationEventTrace2ExecutionTraceFilter.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
				Constants.EXECTRACESFROMEVENTTRACES_COMPONENT_NAME);
		configurationEventTrace2ExecutionTraceFilter.setProperty(
				TraceEventRecords2ExecutionAndMessageTraceFilter.CONFIG_IGNORE_ASSUMED,
				this.booleanToString(this.settings.isIgnoreAssumedCalls()));

		// EventTrace2ExecutionTraceFilter has no configuration properties
		final TraceEventRecords2ExecutionAndMessageTraceFilter traceEvents2ExecutionAndMessageTraceFilter = new TraceEventRecords2ExecutionAndMessageTraceFilter(
				configurationEventTrace2ExecutionTraceFilter, this.analysisController);

		this.analysisController.connect(eventTraceReconstructionFilter,
				EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_VALID,
				traceEvents2ExecutionAndMessageTraceFilter,
				TraceEventRecords2ExecutionAndMessageTraceFilter.INPUT_PORT_NAME_EVENT_TRACE);
		this.analysisController.connect(traceEvents2ExecutionAndMessageTraceFilter,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);

		return traceEvents2ExecutionAndMessageTraceFilter;
	}

	/**
	 *
	 * @param eventTraceReconstructionFilter
	 * @return
	 * @throws IllegalStateException
	 * @throws AnalysisConfigurationException
	 */
	private EventRecordTraceCounter createEventRecordTraceCounter(final EventRecordTraceReconstructionFilter eventTraceReconstructionFilter)
			throws IllegalStateException, AnalysisConfigurationException {
		// Create the counter for valid/invalid event record traces
		final Configuration configurationEventRecordTraceCounter = new Configuration();
		configurationEventRecordTraceCounter.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
				Constants.EXECEVENTRACESFROMEVENTTRACES_COMPONENT_NAME);
		configurationEventRecordTraceCounter.setProperty(
				EventRecordTraceCounter.CONFIG_PROPERTY_NAME_LOG_INVALID,
				this.booleanToString(!this.settings.isIgnoreInvalidTraces()));

		final EventRecordTraceCounter eventRecordTraceCounter = new EventRecordTraceCounter(configurationEventRecordTraceCounter,
				this.analysisController);

		this.analysisController.connect(eventTraceReconstructionFilter,
				EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_VALID, eventRecordTraceCounter,
				EventRecordTraceCounter.INPUT_PORT_NAME_VALID);
		this.analysisController.connect(eventTraceReconstructionFilter,
				EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_INVALID, eventRecordTraceCounter,
				EventRecordTraceCounter.INPUT_PORT_NAME_INVALID);

		return eventRecordTraceCounter;
	}

	/**
	 *
	 * @param execRecTransformer
	 * @param systemEntityFactory
	 * @return
	 * @throws IllegalStateException
	 * @throws AnalysisConfigurationException
	 */
	private TraceReconstructionFilter createTraceReconstruction(final AbstractPlugin execRecTransformer, final AbstractRepository systemEntityFactory)
			throws IllegalStateException, AnalysisConfigurationException {
		// Create the trace reconstruction filter and connect to the record transformation filter's output port
		final Configuration mtReconstrFilterConfig = new Configuration();
		mtReconstrFilterConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
				Constants.TRACERECONSTR_COMPONENT_NAME);
		mtReconstrFilterConfig.setProperty(TraceReconstructionFilter.CONFIG_PROPERTY_NAME_TIMEUNIT,
				TimeUnit.MILLISECONDS.name());
		mtReconstrFilterConfig.setProperty(TraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION,
				this.longToString(this.settings.getMaxTraceDuration()));
		mtReconstrFilterConfig.setProperty(TraceReconstructionFilter.CONFIG_PROPERTY_NAME_IGNORE_INVALID_TRACES,
				this.booleanToString(this.settings.isIgnoreInvalidTraces()));

		final TraceReconstructionFilter mtReconstrFilter = new TraceReconstructionFilter(mtReconstrFilterConfig, this.analysisController);

		this.analysisController.connect(mtReconstrFilter,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);
		this.analysisController.connect(execRecTransformer,
				ExecutionRecordTransformationFilter.OUTPUT_PORT_NAME_EXECUTIONS, mtReconstrFilter,
				TraceReconstructionFilter.INPUT_PORT_NAME_EXECUTIONS);

		return mtReconstrFilter;
	}

	/**
	 *
	 * @param traceIdFilter
	 * @return
	 * @throws IllegalStateException
	 * @throws AnalysisConfigurationException
	 */
	private EventRecordTraceReconstructionFilter createEventRecordTraceReconstruction(final TraceIdFilter traceIdFilter)
			throws IllegalStateException, AnalysisConfigurationException {
		// Create the event record trace generation filter and connect to the trace ID filter's output port
		final Configuration configurationEventRecordTraceGenerationFilter = new Configuration();
		configurationEventRecordTraceGenerationFilter.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
				Constants.EVENTRECORDTRACERECONSTR_COMPONENT_NAME);
		configurationEventRecordTraceGenerationFilter.setProperty(
				EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_TIMEUNIT,
				TimeUnit.MILLISECONDS.name());
		configurationEventRecordTraceGenerationFilter.setProperty(
				EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION,
				this.longToString(this.settings.getMaxTraceDuration()));
		configurationEventRecordTraceGenerationFilter.setProperty(
				EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_REPAIR_EVENT_BASED_TRACES,
				this.booleanToString(this.settings.isRepairEventBasedTraces()));

		final EventRecordTraceReconstructionFilter eventTraceReconstructionFilter = new EventRecordTraceReconstructionFilter(
				configurationEventRecordTraceGenerationFilter, this.analysisController);

		final String outputPortName;
		if (this.settings.isInvertTraceIdFilter()) {
			outputPortName = TraceIdFilter.OUTPUT_PORT_NAME_MISMATCH;
		} else {
			outputPortName = TraceIdFilter.OUTPUT_PORT_NAME_MATCH;
		}
		this.analysisController.connect(traceIdFilter, outputPortName, eventTraceReconstructionFilter,
				EventRecordTraceReconstructionFilter.INPUT_PORT_NAME_TRACE_RECORDS);

		return eventTraceReconstructionFilter;
	}

	/**
	 *
	 * @param traceIdFilter
	 * @param systemEntityFactory
	 * @return
	 * @throws IllegalStateException
	 * @throws AnalysisConfigurationException
	 */
	private ExecutionRecordTransformationFilter createExecutionRecordTransformationFilter(final TraceIdFilter traceIdFilter,
			final AbstractRepository systemEntityFactory) throws IllegalStateException, AnalysisConfigurationException {
		// Create the execution record transformation filter and connect to the trace ID filter's output port
		final Configuration execRecTransformerConfig = new Configuration();
		execRecTransformerConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
				Constants.EXEC_TRACE_RECONSTR_COMPONENT_NAME);
		final ExecutionRecordTransformationFilter execRecTransformer = new ExecutionRecordTransformationFilter(execRecTransformerConfig,
				this.analysisController);
		if (this.settings.isInvertTraceIdFilter()) {
			this.analysisController.connect(traceIdFilter, TraceIdFilter.OUTPUT_PORT_NAME_MISMATCH,
					execRecTransformer, ExecutionRecordTransformationFilter.INPUT_PORT_NAME_RECORDS);
		} else {
			this.analysisController.connect(traceIdFilter, TraceIdFilter.OUTPUT_PORT_NAME_MATCH,
					execRecTransformer, ExecutionRecordTransformationFilter.INPUT_PORT_NAME_RECORDS);
		}

		this.analysisController.connect(execRecTransformer,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);
		return execRecTransformer;
	}

	/**
	 *
	 * @param timestampFilter
	 * @return
	 * @throws IllegalStateException
	 * @throws AnalysisConfigurationException
	 */
	private TraceIdFilter createTraceIdFilter(final TimestampFilter timestampFilter) throws IllegalStateException, AnalysisConfigurationException {
		// Create the trace ID filter and connect to the timestamp filter's output port
		final Configuration configTraceIdFilterFlow = new Configuration();
		if (this.settings.getSelectedTraces().isEmpty()) {
			configTraceIdFilterFlow.setProperty(TraceIdFilter.CONFIG_PROPERTY_NAME_SELECT_ALL_TRACES,
					Boolean.TRUE.toString());
		} else {
			configTraceIdFilterFlow.setProperty(TraceIdFilter.CONFIG_PROPERTY_NAME_SELECT_ALL_TRACES,
					Boolean.FALSE.toString());
			configTraceIdFilterFlow.setProperty(TraceIdFilter.CONFIG_PROPERTY_NAME_SELECTED_TRACES,
					Configuration
							.toProperty(this.settings.getSelectedTraces().toArray(new Long[this.settings.getSelectedTraces().size()])));
		}

		final TraceIdFilter traceIdFilter = new TraceIdFilter(configTraceIdFilterFlow, this.analysisController);

		this.analysisController.connect(timestampFilter, TimestampFilter.OUTPUT_PORT_NAME_WITHIN_PERIOD,
				traceIdFilter, TraceIdFilter.INPUT_PORT_NAME_COMBINED);
		return traceIdFilter;
	}

	private TimestampFilter createTimestampFilter(final ThreadEvent2TraceEventFilter sourceStage)
			throws IllegalStateException, AnalysisConfigurationException {
		// Create the timestamp filter and connect to the reader's output port
		final Configuration configTimestampFilter = new Configuration();
		configTimestampFilter.setProperty(TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_BEFORE_TIMESTAMP,
				this.longToString(this.settings.getIgnoreExecutionsBeforeDate()));

		configTimestampFilter.setProperty(TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_AFTER_TIMESTAMP,
				this.longToString(this.settings.getIgnoreExecutionsAfterDate()));

		final TimestampFilter timestampFilter = new TimestampFilter(configTimestampFilter, this.analysisController);

		this.analysisController.connect(sourceStage, ThreadEvent2TraceEventFilter.OUTPUT_PORT_NAME_DEFAULT, timestampFilter,
				TimestampFilter.INPUT_PORT_NAME_EXECUTION);
		this.analysisController.connect(sourceStage, ThreadEvent2TraceEventFilter.OUTPUT_PORT_NAME_DEFAULT, timestampFilter,
				TimestampFilter.INPUT_PORT_NAME_FLOW);

		return timestampFilter;
	}

	private FSReader createReader() {
		final Configuration conf = new Configuration(null);
		conf.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, Configuration.toProperty(ConvertLegacyValues.fileListToStringArray(this.settings.getInputDirs())));
		conf.setProperty(FSReader.CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES, Boolean.TRUE.toString());
		return new FSReader(conf, this.analysisController);
	}

	private String booleanToString(final Boolean value) {
		if (value == null) {
			return "false";
		} else {
			return Boolean.toString(value);
		}
	}

	private String longToString(final Long value) {
		if (value == null) {
			return "0";
		} else {
			return Long.toString(value);
		}
	}

	/**
	 *
	 * @param decoratorList
	 * @param plugin
	 */
	private void addDecorators(final List<String> decoratorList, final AbstractDependencyGraphFilter<?> plugin) {
		if (decoratorList != null) {
			final Iterator<String> decoratorIterator = decoratorList.iterator();

			// cannot use for loop iterator, as response time coloring uses multiple elements.
			while (decoratorIterator.hasNext()) {
				final String currentDecoratorStr = decoratorIterator.next();
				if (Constants.RESPONSE_TIME_DECORATOR_FLAG_NS.equals(currentDecoratorStr)) {
					plugin.addDecorator(new ResponseTimeNodeDecorator(TimeUnit.NANOSECONDS));
					continue;
				} else if (Constants.RESPONSE_TIME_DECORATOR_FLAG_US.equals(currentDecoratorStr)) {
					plugin.addDecorator(new ResponseTimeNodeDecorator(TimeUnit.MICROSECONDS));
					continue;
				} else if (Constants.RESPONSE_TIME_DECORATOR_FLAG_MS.equals(currentDecoratorStr)) {
					plugin.addDecorator(new ResponseTimeNodeDecorator(TimeUnit.MILLISECONDS));
					continue;
				} else if (Constants.RESPONSE_TIME_DECORATOR_FLAG_S.equals(currentDecoratorStr)) {
					plugin.addDecorator(new ResponseTimeNodeDecorator(TimeUnit.SECONDS));
					continue;
				} else if (Constants.RESPONSE_TIME_COLORING_DECORATOR_FLAG.equals(currentDecoratorStr)) {
					// if decorator is responseColoring, next value should be the threshold
					final String thresholdStringStr = decoratorIterator.next();

					try {
						final int threshold = Integer.parseInt(thresholdStringStr);

						plugin.addDecorator(new ResponseTimeColorNodeDecorator(threshold));
					} catch (final NumberFormatException exc) {
						this.logger.error("Failed to parse int value of property " + "threshold(ms) : " + thresholdStringStr);
					}
				} else {
					this.logger.warn("Unknown decoration name '{}'.", currentDecoratorStr);
					return;
				}
			}
		}
	}

	/**
	 * @param pathPrefix
	 *            prefix path for all files
	 * @param systemEntityFactory
	 *            the consumer filter
	 * @param mtReconstrFilter
	 *            one of the producer filters
	 * @param traceEvents2ExecutionAndMessageTraceFilter
	 *            one of the producer filters
	 * @return
	 * @throws IOException
	 * @throws AnalysisConfigurationException
	 */
	private TraceCallTreeFilter createTraceCallTreeFilter(final String pathPrefix, final SystemModelRepository systemEntityFactory,
			final TraceReconstructionFilter mtReconstrFilter,
			final TraceEventRecords2ExecutionAndMessageTraceFilter traceEvents2ExecutionAndMessageTraceFilter)
			throws IOException, AnalysisConfigurationException {
		// build config
		final Configuration componentPlotTraceCallTreesConfig = new Configuration();

		componentPlotTraceCallTreesConfig.setProperty(TraceCallTreeFilter.CONFIG_PROPERTY_NAME_OUTPUT_FILENAME,
				new File(pathPrefix + Constants.CALL_TREE_FN_PREFIX)
						.getCanonicalPath());
		componentPlotTraceCallTreesConfig.setProperty(TraceCallTreeFilter.CONFIG_PROPERTY_NAME_SHORT_LABELS,
				this.booleanToString(this.settings.isShortLabels()));
		componentPlotTraceCallTreesConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
				Constants.PLOTCALLTREE_COMPONENT_NAME);

		// create filter
		final TraceCallTreeFilter componentPlotTraceCallTrees = new TraceCallTreeFilter(componentPlotTraceCallTreesConfig,
				this.analysisController);

		// connect filter
		this.analysisController.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
				componentPlotTraceCallTrees, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
		this.analysisController.connect(traceEvents2ExecutionAndMessageTraceFilter,
				TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
				componentPlotTraceCallTrees, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
		this.analysisController.connect(componentPlotTraceCallTrees,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);

		return componentPlotTraceCallTrees;
	}

	/**
	 * @param pathPrefix
	 * @param systemEntityFactory
	 *            the consumer filter
	 * @param traceReconstructionFilter
	 *            one of the producer filters
	 * @param traceEvents2ExecutionAndMessageTraceFilter
	 *            one of the producer filters
	 * @return
	 * @throws AnalysisConfigurationException
	 */
	private AggregatedAssemblyComponentOperationCallTreeFilter createAggrAssemblyCompOpCallTreeFilter(
			final String pathPrefix,
			final SystemModelRepository systemEntityFactory, final TraceReconstructionFilter traceReconstructionFilter,
			final TraceEventRecords2ExecutionAndMessageTraceFilter traceEvents2ExecutionAndMessageTraceFilter)
			throws AnalysisConfigurationException {
		// build configuration
		final Configuration componentPlotAssemblyCallTreeConfig = new Configuration();
		componentPlotAssemblyCallTreeConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
				Constants.PLOTAGGREGATEDASSEMBLYCALLTREE_COMPONENT_NAME);
		componentPlotAssemblyCallTreeConfig.setProperty(
				AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, Boolean.toString(true));
		componentPlotAssemblyCallTreeConfig.setProperty(
				AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_SHORT_LABELS, this.booleanToString(this.settings.isShortLabels()));
		componentPlotAssemblyCallTreeConfig.setProperty(
				AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_OUTPUT_FILENAME, pathPrefix + Constants.AGGREGATED_ASSEMBLY_CALL_TREE_FN_PREFIX + ".dot");

		// create filter
		final AggregatedAssemblyComponentOperationCallTreeFilter componentPlotAssemblyCallTree = new AggregatedAssemblyComponentOperationCallTreeFilter(
				componentPlotAssemblyCallTreeConfig, this.analysisController);

		// connect filter
		// the input port of this filter is connected with two source filters
		final String inputPort = AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES;

		this.analysisController.connect(traceReconstructionFilter,
				TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, componentPlotAssemblyCallTree, inputPort);
		this.analysisController.connect(traceEvents2ExecutionAndMessageTraceFilter,
				TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
				componentPlotAssemblyCallTree, inputPort);
		this.analysisController.connect(componentPlotAssemblyCallTree,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);

		return componentPlotAssemblyCallTree;
	}

	/**
	 * Attaches graph processors and a writer to the given graph producers depending on the given command line.
	 *
	 * @param graphProducers
	 *            The graph producers to connect processors to
	 * @param controller
	 *            The analysis controller to use for the connection of the plugins
	 * @param commandLine
	 *            The command line to determine the desired processors
	 *
	 * @throws IllegalStateException
	 *             If the connection of plugins is not possible at the moment
	 * @throws AnalysisConfigurationException
	 *             If some plugins cannot be connected
	 */
	private void attachGraphProcessors(final String pathPrefix, final List<AbstractGraphProducingFilter<?>> graphProducers)
			throws IllegalStateException, AnalysisConfigurationException, IOException {
		for (final AbstractGraphProducingFilter<?> producer : graphProducers) {
			AbstractGraphFilter<?, ?, ?, ?> lastFilter = null;

			// Add a trace coloring filter, if necessary
			if (this.settings.getTraceColoringFile() != null) {
				final String coloringFileName = this.settings.getTraceColoringFile().getAbsolutePath();
				lastFilter = PerformAnalysis.createTraceColoringFilter(producer, coloringFileName, this.analysisController);
			}

			// Add a description filter, if necessary
			if (this.settings.getAddDescriptions() != null) {
				final String descriptionsFileName = this.settings.getAddDescriptions().getAbsolutePath();
				if (lastFilter != null) {
					lastFilter = PerformAnalysis.createDescriptionDecoratorFilter(lastFilter, descriptionsFileName,
							this.analysisController);
				} else {
					lastFilter = PerformAnalysis.createDescriptionDecoratorFilter(producer, descriptionsFileName,
							this.analysisController);
				}
			}

			if (lastFilter != null) {
				this.attachGraphWriter(pathPrefix, lastFilter, producer);
			} else {
				this.attachGraphWriter(pathPrefix, producer, producer);
			}
		}
	}

	/**
	 * Attaches a graph writer plugin to the given plugin.
	 *
	 * @param plugin
	 *            The plugin which delivers the graph to write
	 * @param producer
	 *            The producer which originally produced the graph
	 * @throws IllegalStateException
	 *             If the connection of the plugins is not possible at the moment
	 * @throws AnalysisConfigurationException
	 *             If the plugins cannot be connected
	 *
	 * @param <P>
	 *            The type of the plugin.
	 */
	private <P extends AbstractPlugin & IGraphOutputtingFilter<?>> void attachGraphWriter(final String pathPrefix, final P plugin,
			final AbstractGraphProducingFilter<?> producer)
			throws IllegalStateException, AnalysisConfigurationException {
		final Configuration configuration = new Configuration();
		configuration.setProperty(GraphWriterPlugin.CONFIG_PROPERTY_NAME_OUTPUT_PATH_NAME, pathPrefix);
		configuration.setProperty(GraphWriterPlugin.CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, String.valueOf(true));
		configuration.setProperty(GraphWriterPlugin.CONFIG_PROPERTY_NAME_SHORTLABELS, String.valueOf(this.settings.isShortLabels()));
		configuration.setProperty(GraphWriterPlugin.CONFIG_PROPERTY_NAME_SELFLOOPS,
				String.valueOf(this.settings.isIncludeSelfLoops()));
		configuration.setProperty(AbstractAnalysisComponent.CONFIG_NAME, producer.getConfigurationName());
		final GraphWriterPlugin graphWriter = new GraphWriterPlugin(configuration, this.analysisController);
		this.analysisController.connect(plugin, plugin.getGraphOutputPortName(), graphWriter,
				GraphWriterPlugin.INPUT_PORT_NAME_GRAPHS);
	}

	/**
	 *
	 * @param predecessor
	 * @param filter
	 * @param controller
	 * @throws IllegalStateException
	 * @throws AnalysisConfigurationException
	 */
	private static <P extends AbstractPlugin & IGraphOutputtingFilter<?>> void connectGraphFilters(final P predecessor,
			final AbstractGraphFilter<?, ?, ?, ?> filter, final AnalysisController controller)
			throws IllegalStateException, AnalysisConfigurationException {
		controller.connect(predecessor, predecessor.getGraphOutputPortName(), filter, filter.getGraphInputPortName());
	}

	private static <P extends AbstractPlugin & IGraphOutputtingFilter<?>> TraceColoringFilter<?, ?> createTraceColoringFilter(
			final P predecessor, final String coloringFileName, final AnalysisController controller)
			throws IOException, IllegalStateException, AnalysisConfigurationException {
		final TraceColorRepository colorRepository = TraceColorRepository.createFromFile(coloringFileName, controller);

		@SuppressWarnings("rawtypes")
		final TraceColoringFilter<?, ?> coloringFilter = new TraceColoringFilter(new Configuration(), controller);
		PerformAnalysis.connectGraphFilters(predecessor, coloringFilter, controller);
		controller.connect(coloringFilter, TraceColoringFilter.COLOR_REPOSITORY_PORT_NAME, colorRepository);

		return coloringFilter;
	}

	/**
	 *
	 * @param predecessor
	 * @param descriptionsFileName
	 * @param controller
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws AnalysisConfigurationException
	 */
	private static <P extends AbstractPlugin & IGraphOutputtingFilter<?>> DescriptionDecoratorFilter<?, ?, ?> createDescriptionDecoratorFilter(
			final P predecessor, final String descriptionsFileName, final AnalysisController controller)
			throws IOException, IllegalStateException, AnalysisConfigurationException {
		final DescriptionRepository descriptionRepository = DescriptionRepository.createFromFile(descriptionsFileName,
				controller);

		@SuppressWarnings("rawtypes")
		final DescriptionDecoratorFilter<?, ?, ?> descriptionFilter = new DescriptionDecoratorFilter(
				new Configuration(), controller);
		PerformAnalysis.connectGraphFilters(predecessor, descriptionFilter, controller);
		controller.connect(descriptionFilter, DescriptionDecoratorFilter.DESCRIPTION_REPOSITORY_PORT_NAME,
				descriptionRepository);

		return descriptionFilter;
	}

	/**
	 * Write trace equivalent report.
	 *
	 * @param outputFnPrefixL
	 *            path prefix
	 * @param traceEquivFilter
	 *            filter
	 * @return true on success
	 * @throws IOException
	 *             on error
	 */
	private boolean writeTraceEquivalenceReport(final String outputFnPrefixL,
			final TraceEquivalenceClassFilter traceEquivFilter) throws IOException {
		boolean retVal = true;
		final String outputFn = new File(outputFnPrefixL).getCanonicalPath();
		PrintStream ps = null;
		try {
			ps = new PrintStream(new FileOutputStream(outputFn), false, ENCODING);
			int numClasses = 0;
			final Map<ExecutionTrace, Integer> classMap = traceEquivFilter.getEquivalenceClassMap(); // NOPMD
																										// (UseConcurrentHashMap)
			for (final Entry<ExecutionTrace, Integer> e : classMap.entrySet()) {
				final ExecutionTrace t = e.getKey();
				ps.println("Class " + numClasses++ + " ; cardinality: " + e.getValue() + "; # executions: " + t.getLength() + "; representative: " + t.getTraceId()
						+ "; max. stack depth: " + t.getMaxEss());
			}
			this.logger.debug("");
			this.logger.debug("#");
			this.logger.debug("# Plugin: Trace equivalence report");
			this.logger.debug("Wrote {} equivalence class{} to file '{}'", numClasses, (numClasses > 1 ? "es" : ""), outputFn); // NOCS
		} catch (final FileNotFoundException e) {
			this.logger.error("File not found", e);
			retVal = false;
		} finally {
			if (ps != null) {
				ps.close();
			}
		}

		return retVal;
	}

}
