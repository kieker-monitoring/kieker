/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.sink.EquivalenceClassWriter;
import kieker.analysis.sink.NullStage;
import kieker.analysis.stage.DynamicEventDispatcher;
import kieker.analysis.stage.IEventMatcher;
import kieker.analysis.stage.ImplementsEventMatcher;
import kieker.analysis.stage.flow.EventRecordTraceReconstructionStage;
import kieker.analysis.stage.flow.ThreadEvent2TraceEventStage;
import kieker.analysis.stage.flow.TraceEventRecords;
import kieker.analysis.stage.select.timestampfilter.TimestampFilter;
import kieker.analysis.stage.select.traceidfilter.TraceIdFilter;
import kieker.analysis.trace.InvalidEventRecordTraceCounter;
import kieker.analysis.trace.TraceEventRecords2ExecutionAndMessageTraceStage;
import kieker.analysis.trace.ValidEventRecordTraceCounter;
import kieker.analysis.trace.execution.ExecutionRecordTransformationStage;
import kieker.analysis.trace.execution.TraceEquivalenceClassFilter;
import kieker.analysis.trace.execution.TraceEquivalenceClassFilter.TraceEquivalenceClassModes;
import kieker.analysis.trace.reconstruction.TraceReconstructionStage;
import kieker.analysis.trace.sink.ExecutionTraceWriterFilter;
import kieker.analysis.trace.sink.InvalidExecutionTraceWriterSink;
import kieker.analysis.trace.sink.MessageTraceWriterFilter;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.flow.IFlowRecord;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.ExecutionTrace;
import kieker.model.system.model.InvalidExecutionTrace;
import kieker.model.system.model.MessageTrace;
import kieker.tools.source.LogsReaderCompositeStage;
import kieker.tools.trace.analysis.filter.visualization.VisualizationConstants;
import kieker.visualization.trace.GraphWriterPlugin;
import kieker.visualization.trace.SequenceDiagramFilter;
import kieker.visualization.trace.SequenceDiagramFilter.SDModes;
import kieker.visualization.trace.call.tree.AggregatedAllocationComponentOperationCallTreeFilter;
import kieker.visualization.trace.call.tree.AggregatedAssemblyComponentOperationCallTreeFilter;
import kieker.visualization.trace.call.tree.TraceCallTreeFilter;
import kieker.visualization.trace.dependency.graph.AbstractDependencyGraphFilter;
import kieker.visualization.trace.dependency.graph.ComponentDependencyGraphAllocationFilter;
import kieker.visualization.trace.dependency.graph.ComponentDependencyGraphAssemblyFilter;
import kieker.visualization.trace.dependency.graph.ContainerDependencyGraphFilter;
import kieker.visualization.trace.dependency.graph.OperationDependencyGraphAllocationFilter;
import kieker.visualization.trace.dependency.graph.OperationDependencyGraphAssemblyFilter;
import kieker.visualization.trace.dependency.graph.ResponseTimeColorNodeDecorator;
import kieker.visualization.trace.dependency.graph.ResponseTimeNodeDecorator;

import teetime.framework.Configuration;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;
import teetime.stage.basic.merger.Merger;

/**
 * Central teetime pipe and filter configuration for trace analysis.
 *
 * @author Reiner Jung
 * @since 1.15
 *
 */
public class TraceAnalysisConfiguration extends Configuration {

	private static final Logger LOGGER = LoggerFactory.getLogger(TraceAnalysisConfiguration.class);

	/** The prefix for the call tree files. */
	private static final String CALL_TREE_FN_PREFIX = "callTree";
	/** The name prefix for the message traces files. */
	private static final String MESSAGE_TRACES_FN_PREFIX = "messageTraces";
	/** The name prefix for the execution traces files. */
	private static final String EXECUTION_TRACES_FN_PREFIX = "executionTraces";
	/** The name prefix for the invalid traces files. */
	private static final String INVALID_TRACES_FN_PREFIX = "invalidTraceArtifacts";
	private static final String TRACE_ALLOCATION_EQUIV_CLASSES_FN_PREFIX = "traceDeploymentEquivClasses";
	private static final String TRACE_ASSEMBLY_EQUIV_CLASSES_FN_PREFIX = "traceAssemblyEquivClasses";
	private static final String RESPONSE_TIME_COLORING_DECORATOR_FLAG = "responseTimeColoring";

	private static final String TXT_SUFFIX = ".txt";

	private final TraceReconstructionStage traceReconstructionStage;

	private final ValidEventRecordTraceCounter validEventRecordTraceCounter;

	private final InvalidEventRecordTraceCounter invalidEventRecordTraceCounter;

	private final TraceEventRecords2ExecutionAndMessageTraceStage traceEventRecords2ExecutionAndMessageTraceStage;

	public TraceAnalysisConfiguration(final TraceAnalysisParameters parameters, final SystemModelRepository systemRepository) {
		final String pathPrefix = this.computePrefix(parameters);

		final LogsReaderCompositeStage readerStage = new LogsReaderCompositeStage(parameters.getInputDirs(), parameters.isVerbose(), parameters.getReadBufferSize());
		final ThreadEvent2TraceEventStage threadEvent2TraceEventStage = new ThreadEvent2TraceEventStage();
		final TimestampFilter timestampFilter = new TimestampFilter(parameters.getIgnoreExecutionsBeforeDate(), parameters.getIgnoreExecutionsAfterDate());

		final TraceIdFilter traceIdFilter = new TraceIdFilter(parameters.getSelectedTraces().isEmpty(), parameters.getSelectedTraces());

		final DynamicEventDispatcher dispatcher = new DynamicEventDispatcher(null, false, true, false);
		final IEventMatcher<? extends IFlowRecord> flowRecordMatcher = new ImplementsEventMatcher<>(IFlowRecord.class, null);
		final IEventMatcher<? extends OperationExecutionRecord> operationExecutionRecordMatcher = new ImplementsEventMatcher<>(OperationExecutionRecord.class, null);
		dispatcher.registerOutput(flowRecordMatcher);
		dispatcher.registerOutput(operationExecutionRecordMatcher);

		/** Execution trace. */
		final ExecutionRecordTransformationStage executionRecordTransformationStage = new ExecutionRecordTransformationStage(systemRepository);
		executionRecordTransformationStage.declareActive();
		this.traceReconstructionStage = new TraceReconstructionStage(systemRepository, TimeUnit.MILLISECONDS,
				parameters.isIgnoreInvalidTraces(), parameters.getMaxTraceDuration());

		/** Event trace. */
		final EventRecordTraceReconstructionStage eventRecordTraceReconstructionStage = new EventRecordTraceReconstructionStage(TimeUnit.MILLISECONDS,
				parameters.isRepairEventBasedTraces(), Long.MAX_VALUE, Long.MAX_VALUE);
		eventRecordTraceReconstructionStage.declareActive();

		final Distributor<TraceEventRecords> validTracesDistributor = new Distributor<>(new CopyByReferenceStrategy());
		validTracesDistributor.declareActive();

		this.traceEventRecords2ExecutionAndMessageTraceStage = new TraceEventRecords2ExecutionAndMessageTraceStage(
				systemRepository, false, false, parameters.isIgnoreAssumedCalls());

		this.validEventRecordTraceCounter = new ValidEventRecordTraceCounter();
		this.invalidEventRecordTraceCounter = new InvalidEventRecordTraceCounter(parameters.isIgnoreInvalidTraces());

		/** default sinks. */
		final NullStage execNullStage = new NullStage(false, 1);
		final NullStage invalidNullStage = new NullStage(false, 1);
		final NullStage messageNullStage = new NullStage(false, 1);

		/** Merge traces from different analyses. */
		final Merger<ExecutionTrace> executionTraceMerger = new Merger<>();
		executionTraceMerger.declareActive();
		final Merger<InvalidExecutionTrace> invalidExecutionTraceMerger = new Merger<>();
		invalidExecutionTraceMerger.declareActive();
		final Merger<MessageTrace> messageTraceMerger = new Merger<>();
		messageTraceMerger.declareActive();

		/** Trace distributors to support multiple sinks. */
		final Distributor<ExecutionTrace> executionTraceDistributor = new Distributor<>(new CopyByReferenceStrategy());
		final Distributor<InvalidExecutionTrace> invalidExecutionTraceDistributor = new Distributor<>(new CopyByReferenceStrategy());
		final Distributor<MessageTrace> messageTraceDistributor = new Distributor<>(new CopyByReferenceStrategy());

		/**
		 * Connecting filters.
		 */
		this.connectPorts(readerStage.getOutputPort(), threadEvent2TraceEventStage.getInputPort());
		this.connectPorts(threadEvent2TraceEventStage.getOutputPort(), timestampFilter.getMonitoringRecordsCombinedInputPort());

		this.connectPorts(timestampFilter.getRecordsWithinTimePeriodOutputPort(), traceIdFilter.getInputPort());

		if (parameters.isInvertTraceIdFilter()) {
			this.connectPorts(traceIdFilter.getMismatchingTraceIdOutputPort(), dispatcher.getInputPort());
		} else {
			this.connectPorts(traceIdFilter.getMatchingTraceIdOutputPort(), dispatcher.getInputPort());
		}

		this.connectPorts(operationExecutionRecordMatcher.getOutputPort(), executionRecordTransformationStage.getInputPort());
		this.connectPorts(executionRecordTransformationStage.getOutputPort(), this.traceReconstructionStage.getInputPort());

		this.connectPorts(flowRecordMatcher.getOutputPort(), eventRecordTraceReconstructionStage.getTraceRecordsInputPort());

		this.connectPorts(eventRecordTraceReconstructionStage.getInvalidTracesOutputPort(), this.invalidEventRecordTraceCounter.getInputPort());

		this.connectPorts(eventRecordTraceReconstructionStage.getValidTracesOutputPort(), validTracesDistributor.getInputPort());
		this.connectPorts(validTracesDistributor.getNewOutputPort(), this.validEventRecordTraceCounter.getInputPort());

		this.connectPorts(validTracesDistributor.getNewOutputPort(), this.traceEventRecords2ExecutionAndMessageTraceStage.getInputPort());

		this.connectPorts(this.traceReconstructionStage.getExecutionTraceOutputPort(), executionTraceMerger.getNewInputPort());
		this.connectPorts(this.traceReconstructionStage.getInvalidExecutionTraceOutputPort(), invalidExecutionTraceMerger.getNewInputPort());
		this.connectPorts(this.traceReconstructionStage.getMessageTraceOutputPort(), messageTraceMerger.getNewInputPort());

		/** prepare the connection of reporting sinks. */
		this.connectPorts(this.traceEventRecords2ExecutionAndMessageTraceStage.getExecutionTraceOutputPort(), executionTraceMerger.getNewInputPort());
		this.connectPorts(this.traceEventRecords2ExecutionAndMessageTraceStage.getInvalidExecutionTraceOutputPort(), invalidExecutionTraceMerger.getNewInputPort());
		this.connectPorts(this.traceEventRecords2ExecutionAndMessageTraceStage.getMessageTraceOutputPort(), messageTraceMerger.getNewInputPort());

		this.connectPorts(executionTraceMerger.getOutputPort(), executionTraceDistributor.getInputPort());
		this.connectPorts(invalidExecutionTraceMerger.getOutputPort(), invalidExecutionTraceDistributor.getInputPort());
		this.connectPorts(messageTraceMerger.getOutputPort(), messageTraceDistributor.getInputPort());

		/** connect reporting sinks. */
		this.connectPorts(executionTraceDistributor.getNewOutputPort(), execNullStage.getInputPort());
		this.connectPorts(invalidExecutionTraceDistributor.getNewOutputPort(), invalidNullStage.getInputPort());
		this.connectPorts(messageTraceDistributor.getNewOutputPort(), messageNullStage.getInputPort());

		if (parameters.isPrintDeploymentEquivalenceClasses()) {
			this.createPrintTraceEquivalenceClasses(systemRepository, executionTraceDistributor, pathPrefix, TraceEquivalenceClassModes.ALLOCATION);
		}

		if (parameters.isPrintAssemblyEquivalenceClasses()) {
			this.createPrintTraceEquivalenceClasses(systemRepository, executionTraceDistributor, pathPrefix, TraceEquivalenceClassModes.ASSEMBLY);
		}

		// fill list of msgTraceProcessingComponents:
		if (parameters.isPrintMessageTraces()) {
			this.createComponentPrintMsgTrace(systemRepository, messageTraceDistributor, pathPrefix);
		}

		if (parameters.isPrintExecutionTraces()) {
			this.createComponentPrintExecTrace(systemRepository, executionTraceDistributor, pathPrefix);
		}

		if (parameters.isPrintInvalidExecutionTraces()) {
			this.createInvalidExecutionTraceWriterSink(systemRepository, invalidExecutionTraceDistributor, pathPrefix);
		}

		if (parameters.isPlotDeploymentSequenceDiagrams()) {
			this.createPlotSequenceDiagrams(systemRepository, messageTraceDistributor, pathPrefix,
					VisualizationConstants.ALLOCATION_SEQUENCE_DIAGRAM_FN_PREFIX, SDModes.ALLOCATION, parameters.isShortLabels());
		}

		if (parameters.isPlotAssemblySequenceDiagrams()) {
			this.createPlotSequenceDiagrams(systemRepository, messageTraceDistributor, pathPrefix,
					VisualizationConstants.ASSEMBLY_SEQUENCE_DIAGRAM_FN_PREFIX, SDModes.ASSEMBLY, parameters.isShortLabels());
		}

		if (parameters.getPlotDeploymentComponentDependencyGraph() != null) {
			this.createPlotDeploymentComponentDependencyGraph(systemRepository, messageTraceDistributor, parameters.getPlotDeploymentComponentDependencyGraph(),
					pathPrefix, "", true, parameters.isShortLabels(), parameters.isIncludeSelfLoops());
		}

		if (parameters.getPlotAssemblyComponentDependencyGraph() != null) {
			this.createPlotAssemblyComponentDependencyGraph(systemRepository, messageTraceDistributor, parameters.getPlotAssemblyComponentDependencyGraph(),
					pathPrefix, "", true, parameters.isShortLabels(), parameters.isIncludeSelfLoops());
		}

		if (parameters.isPlotContainerDependencyGraph()) {
			this.createPlotContainerDependencyGraph(systemRepository, messageTraceDistributor, pathPrefix, "", true, parameters.isShortLabels(),
					parameters.isIncludeSelfLoops());
		}

		if (parameters.getPlotDeploymentOperationDependencyGraph() != null) {
			this.createPlotDeploymentOperationDependencyGraph(systemRepository, messageTraceDistributor, parameters.getPlotDeploymentOperationDependencyGraph(),
					pathPrefix, "", true, parameters.isShortLabels(), parameters.isIncludeSelfLoops());
		}

		if (parameters.getPlotAssemblyOperationDependencyGraph() != null) {
			this.createPlotAssemblyOperationDependencyGraph(systemRepository, messageTraceDistributor, parameters.getPlotAssemblyOperationDependencyGraph(),
					pathPrefix, "", true, parameters.isShortLabels(), parameters.isIncludeSelfLoops());
		}

		if (parameters.isPlotCallTrees()) {
			this.createTraceCallTreeFilter(systemRepository, messageTraceDistributor, pathPrefix, parameters.isShortLabels());
		}

		if (parameters.isPlotAggregatedDeploymentCallTree()) {
			this.createPlotAggregatedDeploymentCallTree(systemRepository, messageTraceDistributor, pathPrefix, true, parameters.isShortLabels());
		}

		if (parameters.isPlotAggregatedAssemblyCallTree()) {
			this.createAggrAssemblyCompOpCallTreeFilter(systemRepository, messageTraceDistributor, pathPrefix, true, parameters.isShortLabels());
		}
	}

	private void createPlotSequenceDiagrams(final SystemModelRepository systemRepository, final Distributor<MessageTrace> messageTraceDistributor,
			final String pathPrefix, final String outputFilename, final SDModes sequenceDiagramMode, final boolean shortLabels) {
		final SequenceDiagramFilter sequenceDiagramFilter = new SequenceDiagramFilter(systemRepository, sequenceDiagramMode,
				pathPrefix + File.separator + outputFilename, shortLabels);
		this.connectPorts(messageTraceDistributor.getNewOutputPort(), sequenceDiagramFilter.getInputPort());
	}

	private void createPlotDeploymentComponentDependencyGraph(final SystemModelRepository systemRepository,
			final Distributor<MessageTrace> messageTraceDistributor, final List<String> decoratorList, final String outputPathName, final String outputFileName,
			final boolean includeWeights, final boolean shortLabels, final boolean plotLoops) {
		final ComponentDependencyGraphAllocationFilter graphFilter = new ComponentDependencyGraphAllocationFilter(systemRepository,
				TimeUnit.MILLISECONDS);
		this.addDecorators(decoratorList, graphFilter);
		final GraphWriterPlugin writerStage = new GraphWriterPlugin(outputPathName, outputFileName, includeWeights, shortLabels, plotLoops);

		this.connectPorts(messageTraceDistributor.getNewOutputPort(), graphFilter.getInputPort());
		this.connectPorts(graphFilter.getOutputPort(), writerStage.getInputPort());
	}

	private void createPlotAssemblyComponentDependencyGraph(final SystemModelRepository systemRepository, final Distributor<MessageTrace> messageTraceDistributor,
			final List<String> decoratorList, final String outputPathName, final String outputFileName, final boolean includeWeights, final boolean shortLabels,
			final boolean plotLoops) {
		final ComponentDependencyGraphAssemblyFilter graphFilter = new ComponentDependencyGraphAssemblyFilter(systemRepository, TimeUnit.MILLISECONDS);
		this.addDecorators(decoratorList, graphFilter);
		final GraphWriterPlugin writerStage = new GraphWriterPlugin(outputPathName, outputFileName, includeWeights, shortLabels, plotLoops);

		this.connectPorts(messageTraceDistributor.getNewOutputPort(), graphFilter.getInputPort());
		this.connectPorts(graphFilter.getOutputPort(), writerStage.getInputPort());
	}

	private void createPlotContainerDependencyGraph(final SystemModelRepository systemRepository, final Distributor<MessageTrace> messageTraceDistributor,
			final String outputPathName, final String outputFileName, final boolean includeWeights, final boolean shortLabels, final boolean plotLoops) {
		final ContainerDependencyGraphFilter graphFilter = new ContainerDependencyGraphFilter(systemRepository, TimeUnit.MILLISECONDS);
		final GraphWriterPlugin writerStage = new GraphWriterPlugin(outputPathName, outputFileName, includeWeights, shortLabels, plotLoops);

		this.connectPorts(messageTraceDistributor.getNewOutputPort(), graphFilter.getInputPort());
		this.connectPorts(graphFilter.getOutputPort(), writerStage.getInputPort());
	}

	private void createPlotDeploymentOperationDependencyGraph(final SystemModelRepository systemRepository,
			final Distributor<MessageTrace> messageTraceDistributor, final List<String> decoratorList, final String outputPathName, final String outputFileName,
			final boolean includeWeights, final boolean shortLabels,
			final boolean plotLoops) {
		final OperationDependencyGraphAllocationFilter graphFilter = new OperationDependencyGraphAllocationFilter(systemRepository, TimeUnit.MILLISECONDS);
		this.addDecorators(decoratorList, graphFilter);
		final GraphWriterPlugin writerStage = new GraphWriterPlugin(outputPathName, outputFileName, includeWeights, shortLabels, plotLoops);

		this.connectPorts(messageTraceDistributor.getNewOutputPort(), graphFilter.getInputPort());
		this.connectPorts(graphFilter.getOutputPort(), writerStage.getInputPort());
	}

	private void createPlotAssemblyOperationDependencyGraph(final SystemModelRepository systemRepository, final Distributor<MessageTrace> messageTraceDistributor,
			final List<String> decoratorList, final String outputPathName, final String outputFileName,
			final boolean includeWeights, final boolean shortLabels,
			final boolean plotLoops) {
		final OperationDependencyGraphAssemblyFilter graphFilter = new OperationDependencyGraphAssemblyFilter(systemRepository, TimeUnit.MILLISECONDS);
		this.addDecorators(decoratorList, graphFilter);
		final GraphWriterPlugin writerStage = new GraphWriterPlugin(outputPathName, outputFileName, includeWeights, shortLabels, plotLoops);

		this.connectPorts(messageTraceDistributor.getNewOutputPort(), graphFilter.getInputPort());
		this.connectPorts(graphFilter.getOutputPort(), writerStage.getInputPort());
	}

	private void createTraceCallTreeFilter(final SystemModelRepository systemRepository, final Distributor<MessageTrace> messageTraceDistributor,
			final String pathPrefix, final boolean shortLabels) {
		final TraceCallTreeFilter componentPlotTraceCallTrees = new TraceCallTreeFilter(systemRepository, shortLabels,
				pathPrefix + CALL_TREE_FN_PREFIX);
		this.connectPorts(messageTraceDistributor.getNewOutputPort(), componentPlotTraceCallTrees.getInputPort());
	}

	private void createPlotAggregatedDeploymentCallTree(final SystemModelRepository systemRepository, final Distributor<MessageTrace> messageTraceDistributor,
			final String pathPrefix, final boolean includeWeights, final boolean shortLabels) {
		final AggregatedAllocationComponentOperationCallTreeFilter componentPlotAggregatedCallTree = new AggregatedAllocationComponentOperationCallTreeFilter(
				systemRepository, includeWeights, shortLabels, pathPrefix + VisualizationConstants.AGGREGATED_ALLOCATION_CALL_TREE_FN_PREFIX + ".dot");
		this.connectPorts(messageTraceDistributor.getNewOutputPort(), componentPlotAggregatedCallTree.getInputPort());
	}

	private void createAggrAssemblyCompOpCallTreeFilter(final SystemModelRepository systemRepository, final Distributor<MessageTrace> messageTraceDistributor,
			final String pathPrefix, final boolean includeWeights, final boolean shortLabels) {
		final AggregatedAssemblyComponentOperationCallTreeFilter componentPlotAssemblyCallTree = new AggregatedAssemblyComponentOperationCallTreeFilter(
				systemRepository, includeWeights, shortLabels, pathPrefix + VisualizationConstants.AGGREGATED_ASSEMBLY_CALL_TREE_FN_PREFIX + ".dot");
		this.connectPorts(messageTraceDistributor.getNewOutputPort(), componentPlotAssemblyCallTree.getInputPort());
	}

	private void createPrintTraceEquivalenceClasses(final SystemModelRepository systemRepository, final Distributor<ExecutionTrace> executionTraceDistributor,
			final String pathPrefix, final TraceEquivalenceClassModes equivalenceClassMode) {
		final String filePrefix;
		switch (equivalenceClassMode) {
		case ASSEMBLY:
			filePrefix = TRACE_ASSEMBLY_EQUIV_CLASSES_FN_PREFIX;
			break;
		case ALLOCATION:
			filePrefix = TRACE_ALLOCATION_EQUIV_CLASSES_FN_PREFIX;
			break;
		case DISABLED:
		default:
			return;
		}
		final TraceEquivalenceClassFilter traceAllocationEquivalenceClassFilter = new TraceEquivalenceClassFilter(systemRepository, equivalenceClassMode);
		final EquivalenceClassWriter equivalenceClassWriter = new EquivalenceClassWriter(new File(pathPrefix + filePrefix + TXT_SUFFIX));

		this.connectPorts(executionTraceDistributor.getNewOutputPort(), traceAllocationEquivalenceClassFilter.getInputPort());
		this.connectPorts(traceAllocationEquivalenceClassFilter.getEquivalenceMapOutputPort(), equivalenceClassWriter.getInputPort());
	}

	private void createComponentPrintMsgTrace(final SystemModelRepository systemRepository, final Distributor<MessageTrace> messageTraceDistributor,
			final String pathPrefix) {
		try {
			final MessageTraceWriterFilter messageWriterStink = new MessageTraceWriterFilter(systemRepository,
					new File(pathPrefix + MESSAGE_TRACES_FN_PREFIX + TXT_SUFFIX));
			this.connectPorts(messageTraceDistributor.getNewOutputPort(), messageWriterStink.getInputPort());
		} catch (final IOException e) {
			LOGGER.error(String.format("Error initializing %s cause %s", MessageTraceWriterFilter.class, e.getLocalizedMessage()));
		}
	}

	private void createComponentPrintExecTrace(final SystemModelRepository systemRepository, final Distributor<ExecutionTrace> executionTraceDistributor,
			final String pathPrefix) {
		try {
			final ExecutionTraceWriterFilter executionWriterStink = new ExecutionTraceWriterFilter(systemRepository,
					new File(pathPrefix + EXECUTION_TRACES_FN_PREFIX + TXT_SUFFIX));
			this.connectPorts(executionTraceDistributor.getNewOutputPort(), executionWriterStink.getInputPort());
		} catch (final IOException e) {
			LOGGER.error(String.format("Error initializing %s cause %s", ExecutionTraceWriterFilter.class, e.getLocalizedMessage()));
		}
	}

	private void createInvalidExecutionTraceWriterSink(final SystemModelRepository systemRepository,
			final Distributor<InvalidExecutionTrace> invalidExecutionTraceDistributor, final String pathPrefix) {
		try {
			final InvalidExecutionTraceWriterSink invalidExecutionTraceWriterSink = new InvalidExecutionTraceWriterSink(systemRepository,
					new File(pathPrefix + INVALID_TRACES_FN_PREFIX + TXT_SUFFIX));
			this.connectPorts(invalidExecutionTraceDistributor.getNewOutputPort(), invalidExecutionTraceWriterSink.getInputPort());
		} catch (final IOException e) {
			LOGGER.error(String.format("Error initializing %s cause %s", InvalidExecutionTraceWriterSink.class, e.getLocalizedMessage()));
		}
	}

	private String computePrefix(final TraceAnalysisParameters parameters) {
		if (parameters.getOutputDir() != null) {
			if (parameters.getPrefix() != null) {
				return parameters.getOutputDir() + File.separator + parameters.getPrefix();
			} else {
				return parameters.getOutputDir() + File.separator;
			}
		} else if (parameters.getPrefix() != null) {
			return parameters.getPrefix();
		} else {
			return "";
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
				if (VisualizationConstants.RESPONSE_TIME_DECORATOR_FLAG_NS.equals(currentDecoratorStr)) {
					plugin.addDecorator(new ResponseTimeNodeDecorator(TimeUnit.NANOSECONDS));
					continue;
				} else if (VisualizationConstants.RESPONSE_TIME_DECORATOR_FLAG_US.equals(currentDecoratorStr)) {
					plugin.addDecorator(new ResponseTimeNodeDecorator(TimeUnit.MICROSECONDS));
					continue;
				} else if (VisualizationConstants.RESPONSE_TIME_DECORATOR_FLAG_MS.equals(currentDecoratorStr)) {
					plugin.addDecorator(new ResponseTimeNodeDecorator(TimeUnit.MILLISECONDS));
					continue;
				} else if (VisualizationConstants.RESPONSE_TIME_DECORATOR_FLAG_S.equals(currentDecoratorStr)) {
					plugin.addDecorator(new ResponseTimeNodeDecorator(TimeUnit.SECONDS));
					continue;
				} else if (RESPONSE_TIME_COLORING_DECORATOR_FLAG.equals(currentDecoratorStr)) {
					// if decorator is responseColoring, next value should be the threshold
					final String thresholdStringStr = decoratorIterator.next();

					try {
						final int threshold = Integer.parseInt(thresholdStringStr);

						plugin.addDecorator(new ResponseTimeColorNodeDecorator(threshold));
					} catch (final NumberFormatException exc) {
						LOGGER.error("Failed to parse int value of property threshold(ms) : {}", thresholdStringStr);
					}
				} else {
					LOGGER.warn("Unknown decoration name '{}'.", currentDecoratorStr);
					return;
				}
			}
		}
	}

	public TraceReconstructionStage getTraceReconstructionStage() {
		return this.traceReconstructionStage;
	}

	public ValidEventRecordTraceCounter getValidEventRecordTraceCounter() {
		return this.validEventRecordTraceCounter;
	}

	public InvalidEventRecordTraceCounter getInvalidEventRecordTraceCounter() {
		return this.invalidEventRecordTraceCounter;
	}

	public TraceEventRecords2ExecutionAndMessageTraceStage getTraceEventRecords2ExecutionAndMessageTraceStage() {
		return this.traceEventRecords2ExecutionAndMessageTraceStage;
	}

}
