/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.architecture.trace.InvalidEventRecordTraceCounter;
import kieker.analysis.architecture.trace.TraceEventRecords2ExecutionAndMessageTraceStage;
import kieker.analysis.architecture.trace.TraceIdFilter;
import kieker.analysis.architecture.trace.ValidEventRecordTraceCounter;
import kieker.analysis.architecture.trace.execution.ExecutionRecordTransformationStage;
import kieker.analysis.architecture.trace.execution.TraceEquivalenceClassFilter;
import kieker.analysis.architecture.trace.execution.TraceEquivalenceClassFilter.TraceEquivalenceClassModes;
import kieker.analysis.architecture.trace.flow.EventRecordTraceReconstructionStage;
import kieker.analysis.architecture.trace.flow.ThreadEvent2TraceEventStage;
import kieker.analysis.architecture.trace.flow.TraceEventRecords;
import kieker.analysis.architecture.trace.reconstruction.TraceReconstructionStage;
import kieker.analysis.architecture.trace.sink.ExecutionTraceWriterFilter;
import kieker.analysis.architecture.trace.sink.InvalidExecutionTraceWriterSink;
import kieker.analysis.architecture.trace.sink.MessageTraceWriterFilter;
import kieker.analysis.generic.DynamicEventDispatcher;
import kieker.analysis.generic.IEventMatcher;
import kieker.analysis.generic.ImplementsEventMatcher;
import kieker.analysis.generic.sink.EquivalenceClassWriter;
import kieker.analysis.generic.sink.NullSink;
import kieker.analysis.generic.time.TimestampFilter;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.flow.IFlowRecord;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.ExecutionTrace;
import kieker.model.system.model.InvalidExecutionTrace;
import kieker.model.system.model.MessageTrace;
import kieker.tools.common.TraceAnalysisParameters;
import kieker.tools.settings.converters.DateConverter;
import kieker.tools.source.LogsReaderCompositeStage;
import kieker.tools.trace.analysis.filter.visualization.VisualizationConstants;
import kieker.visualization.trace.SequenceDiagramFilter.SDModes;
import kieker.visualization.trace.dependency.graph.AbstractDependencyGraphFilter;
import kieker.visualization.trace.dependency.graph.ResponseTimeColorNodeDecorator;
import kieker.visualization.trace.dependency.graph.ResponseTimeNodeDecorator;
import teetime.framework.Configuration;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;
import teetime.stage.basic.merger.Merger;

/**
 * Abstract base teetime pipe and filter configuration for trace analysis.
 * Subclasses must implement abstract methods to create diagrams and graphs.
 *
 * @author Yorrick Josuttis
 */
public abstract class AbstractTraceAnalysisConfiguration extends Configuration {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTraceAnalysisConfiguration.class);

	protected TraceAnalysisParameters parameters;

	/** The prefix for the call tree files. */
	protected static final String CALL_TREE_FN_PREFIX = "callTree";
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

	/*
	 * Base configuration for trace analysis.
	 * @param parameters       configuration parameters
	 * @param systemRepository the system model repository to use
	 */
	public AbstractTraceAnalysisConfiguration(final TraceAnalysisParameters parameters,
			final SystemModelRepository systemRepository) {
		this.parameters = parameters;
		
		final String pathPrefix = this.computePrefix(parameters);

		final DateFormat dateFormat = new SimpleDateFormat(DateConverter.DATE_FORMAT_PATTERN, Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		if (parameters.getIgnoreExecutionsBeforeDate() == null) {
			parameters
					.setIgnoreExecutionsBeforeDate(Long.parseLong(TimestampFilter.CONFIG_PROPERTY_VALUE_MIN_TIMESTAMP));
		} else {
			if (AbstractTraceAnalysisConfiguration.LOGGER.isInfoEnabled()) {
				AbstractTraceAnalysisConfiguration.LOGGER.info("Ignoring records before {} ({})",
						dateFormat.format(parameters.getIgnoreExecutionsBeforeDate()),
						parameters.getIgnoreExecutionsBeforeDate());
			}
		}

		if (parameters.getIgnoreExecutionsAfterDate() == null) {
			parameters
					.setIgnoreExecutionsAfterDate(Long.parseLong(TimestampFilter.CONFIG_PROPERTY_VALUE_MAX_TIMESTAMP));
		} else {
			if (AbstractTraceAnalysisConfiguration.LOGGER.isInfoEnabled()) {
				AbstractTraceAnalysisConfiguration.LOGGER.info("Ignoring records after {} ({})",
						dateFormat.format(parameters.getIgnoreExecutionsAfterDate()),
						parameters.getIgnoreExecutionsAfterDate());
			}
		}

		final LogsReaderCompositeStage readerStage = new LogsReaderCompositeStage(parameters.getInputDirs(),
				parameters.isVerbose(), parameters.getReadBufferSize());
		final ThreadEvent2TraceEventStage threadEvent2TraceEventStage = new ThreadEvent2TraceEventStage();
		final TimestampFilter timestampFilter = new TimestampFilter(parameters.getIgnoreExecutionsBeforeDate(),
				parameters.getIgnoreExecutionsAfterDate());

		final TraceIdFilter traceIdFilter = new TraceIdFilter(parameters.getSelectedTraces().isEmpty(),
				parameters.getSelectedTraces());

		final DynamicEventDispatcher dispatcher = new DynamicEventDispatcher(null, false, true, false);
		final IEventMatcher<? extends IFlowRecord> flowRecordMatcher = new ImplementsEventMatcher<>(IFlowRecord.class,
				null);
		final IEventMatcher<? extends OperationExecutionRecord> operationExecutionRecordMatcher = new ImplementsEventMatcher<>(
				OperationExecutionRecord.class, null);
		dispatcher.registerOutput(flowRecordMatcher);
		dispatcher.registerOutput(operationExecutionRecordMatcher);

		/** Execution trace. */
		final ExecutionRecordTransformationStage executionRecordTransformationStage = new ExecutionRecordTransformationStage(
				systemRepository);
		executionRecordTransformationStage.declareActive();
		this.traceReconstructionStage = new TraceReconstructionStage(systemRepository, TimeUnit.MILLISECONDS,
				parameters.isIgnoreInvalidTraces(), parameters.getMaxTraceDuration());

		/** Event trace. */
		final EventRecordTraceReconstructionStage eventRecordTraceReconstructionStage = new EventRecordTraceReconstructionStage(
				TimeUnit.MILLISECONDS,
				parameters.isRepairEventBasedTraces(), Long.MAX_VALUE, Long.MAX_VALUE);
		eventRecordTraceReconstructionStage.declareActive();

		final Distributor<TraceEventRecords> validTracesDistributor = new Distributor<>(new CopyByReferenceStrategy());
		validTracesDistributor.declareActive();

		this.traceEventRecords2ExecutionAndMessageTraceStage = new TraceEventRecords2ExecutionAndMessageTraceStage(
				systemRepository, false, false, parameters.isIgnoreAssumedCalls());

		this.validEventRecordTraceCounter = new ValidEventRecordTraceCounter();
		this.invalidEventRecordTraceCounter = new InvalidEventRecordTraceCounter(parameters.isIgnoreInvalidTraces());

		/** default sinks. */
		final NullSink execNullStage = new NullSink(false, 1);
		final NullSink invalidNullStage = new NullSink(false, 1);
		final NullSink messageNullStage = new NullSink(false, 1);

		/** Merge traces from different analyses. */
		final Merger<ExecutionTrace> executionTraceMerger = new Merger<>();
		executionTraceMerger.declareActive();
		final Merger<InvalidExecutionTrace> invalidExecutionTraceMerger = new Merger<>();
		invalidExecutionTraceMerger.declareActive();
		final Merger<MessageTrace> messageTraceMerger = new Merger<>();
		messageTraceMerger.declareActive();

		/** Trace distributors to support multiple sinks. */
		final Distributor<ExecutionTrace> executionTraceDistributor = new Distributor<>(new CopyByReferenceStrategy());
		final Distributor<InvalidExecutionTrace> invalidExecutionTraceDistributor = new Distributor<>(
				new CopyByReferenceStrategy());
		final Distributor<MessageTrace> messageTraceDistributor = new Distributor<>(new CopyByReferenceStrategy());

		/**
		 * Connecting filters.
		 */
		this.connectPorts(readerStage.getOutputPort(), threadEvent2TraceEventStage.getInputPort());
		this.connectPorts(threadEvent2TraceEventStage.getOutputPort(),
				timestampFilter.getMonitoringRecordsCombinedInputPort());

		this.connectPorts(timestampFilter.getRecordsWithinTimePeriodOutputPort(), traceIdFilter.getInputPort());

		if (parameters.isInvertTraceIdFilter()) {
			this.connectPorts(traceIdFilter.getMismatchingTraceIdOutputPort(), dispatcher.getInputPort());
		} else {
			this.connectPorts(traceIdFilter.getOutputPort(), dispatcher.getInputPort());
		}

		this.connectPorts(operationExecutionRecordMatcher.getOutputPort(),
				executionRecordTransformationStage.getInputPort());
		this.connectPorts(executionRecordTransformationStage.getOutputPort(),
				this.traceReconstructionStage.getInputPort());

		this.connectPorts(flowRecordMatcher.getOutputPort(),
				eventRecordTraceReconstructionStage.getTraceRecordsInputPort());

		this.connectPorts(eventRecordTraceReconstructionStage.getInvalidTracesOutputPort(),
				this.invalidEventRecordTraceCounter.getInputPort());

		this.connectPorts(eventRecordTraceReconstructionStage.getValidTracesOutputPort(),
				validTracesDistributor.getInputPort());
		this.connectPorts(validTracesDistributor.getNewOutputPort(), this.validEventRecordTraceCounter.getInputPort());

		this.connectPorts(validTracesDistributor.getNewOutputPort(),
				this.traceEventRecords2ExecutionAndMessageTraceStage.getInputPort());

		this.connectPorts(this.traceReconstructionStage.getExecutionTraceOutputPort(),
				executionTraceMerger.getNewInputPort());
		this.connectPorts(this.traceReconstructionStage.getInvalidExecutionTraceOutputPort(),
				invalidExecutionTraceMerger.getNewInputPort());
		this.connectPorts(this.traceReconstructionStage.getMessageTraceOutputPort(),
				messageTraceMerger.getNewInputPort());

		/** prepare the connection of reporting sinks. */
		this.connectPorts(this.traceEventRecords2ExecutionAndMessageTraceStage.getExecutionTraceOutputPort(),
				executionTraceMerger.getNewInputPort());
		this.connectPorts(this.traceEventRecords2ExecutionAndMessageTraceStage.getInvalidExecutionTraceOutputPort(),
				invalidExecutionTraceMerger.getNewInputPort());
		this.connectPorts(this.traceEventRecords2ExecutionAndMessageTraceStage.getMessageTraceOutputPort(),
				messageTraceMerger.getNewInputPort());

		this.connectPorts(executionTraceMerger.getOutputPort(), executionTraceDistributor.getInputPort());
		this.connectPorts(invalidExecutionTraceMerger.getOutputPort(), invalidExecutionTraceDistributor.getInputPort());
		this.connectPorts(messageTraceMerger.getOutputPort(), messageTraceDistributor.getInputPort());

		/** connect reporting sinks. */
		this.connectPorts(executionTraceDistributor.getNewOutputPort(), execNullStage.getInputPort());
		this.connectPorts(invalidExecutionTraceDistributor.getNewOutputPort(), invalidNullStage.getInputPort());
		this.connectPorts(messageTraceDistributor.getNewOutputPort(), messageNullStage.getInputPort());

		if (parameters.isPrintDeploymentEquivalenceClasses()) {
			this.createPrintTraceEquivalenceClasses(systemRepository, executionTraceDistributor, pathPrefix,
					TraceEquivalenceClassModes.ALLOCATION);
		}

		if (parameters.isPrintAssemblyEquivalenceClasses()) {
			this.createPrintTraceEquivalenceClasses(systemRepository, executionTraceDistributor, pathPrefix,
					TraceEquivalenceClassModes.ASSEMBLY);
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
					VisualizationConstants.ALLOCATION_SEQUENCE_DIAGRAM_FN_PREFIX, SDModes.ALLOCATION,
					parameters.isShortLabels());
		}

		if (parameters.isPlotAssemblySequenceDiagrams()) {
			this.createPlotSequenceDiagrams(systemRepository, messageTraceDistributor, pathPrefix,
					VisualizationConstants.ASSEMBLY_SEQUENCE_DIAGRAM_FN_PREFIX, SDModes.ASSEMBLY,
					parameters.isShortLabels());
		}

		if (parameters.getPlotDeploymentComponentDependencyGraph() != null) {
			this.createPlotDeploymentComponentDependencyGraph(systemRepository, messageTraceDistributor,
					parameters.getPlotDeploymentComponentDependencyGraph(),
					pathPrefix, "", true, parameters.isShortLabels(), parameters.isIncludeSelfLoops());
		}

		if (parameters.getPlotAssemblyComponentDependencyGraph() != null) {
			this.createPlotAssemblyComponentDependencyGraph(systemRepository, messageTraceDistributor,
					parameters.getPlotAssemblyComponentDependencyGraph(),
					pathPrefix, "", true, parameters.isShortLabels(), parameters.isIncludeSelfLoops());
		}

		if (parameters.isPlotContainerDependencyGraph()) {
			this.createPlotContainerDependencyGraph(systemRepository, messageTraceDistributor, pathPrefix, "", true,
					parameters.isShortLabels(),
					parameters.isIncludeSelfLoops());
		}

		if (parameters.getPlotDeploymentOperationDependencyGraph() != null) {
			this.createPlotDeploymentOperationDependencyGraph(systemRepository, messageTraceDistributor,
					parameters.getPlotDeploymentOperationDependencyGraph(),
					pathPrefix, "", true, parameters.isShortLabels(), parameters.isIncludeSelfLoops());
		}

		if (parameters.getPlotAssemblyOperationDependencyGraph() != null) {
			this.createPlotAssemblyOperationDependencyGraph(systemRepository, messageTraceDistributor,
					parameters.getPlotAssemblyOperationDependencyGraph(),
					pathPrefix, "", true, parameters.isShortLabels(), parameters.isIncludeSelfLoops());
		}

		if (parameters.isPlotCallTrees()) {
			this.createTraceCallTreeFilter(systemRepository, messageTraceDistributor, pathPrefix,
					parameters.isShortLabels());
		}

		if (parameters.isPlotAggregatedDeploymentCallTree()) {
			this.createPlotAggregatedDeploymentCallTree(systemRepository, messageTraceDistributor, pathPrefix, true,
					parameters.isShortLabels());
		}

		if (parameters.isPlotAggregatedAssemblyCallTree()) {
			this.createAggrAssemblyCompOpCallTreeFilter(systemRepository, messageTraceDistributor, pathPrefix, true,
					parameters.isShortLabels());
		}
	}

	protected abstract void createPlotSequenceDiagrams(final SystemModelRepository systemRepository,
			final Distributor<MessageTrace> messageTraceDistributor,
			final String pathPrefix, final String outputFilename, final SDModes sequenceDiagramMode,
			final boolean shortLabels);

	protected abstract void createPlotDeploymentComponentDependencyGraph(final SystemModelRepository systemRepository,
			final Distributor<MessageTrace> messageTraceDistributor, final List<String> decoratorList,
			final String outputPathName, final String outputFileName,
			final boolean includeWeights, final boolean shortLabels, final boolean plotLoops);

	protected abstract void createPlotAssemblyComponentDependencyGraph(final SystemModelRepository systemRepository,
			final Distributor<MessageTrace> messageTraceDistributor,
			final List<String> decoratorList, final String outputPathName, final String outputFileName,
			final boolean includeWeights, final boolean shortLabels,
			final boolean plotLoops);

	protected abstract void createPlotContainerDependencyGraph(final SystemModelRepository systemRepository,
			final Distributor<MessageTrace> messageTraceDistributor,
			final String outputPathName, final String outputFileName, final boolean includeWeights,
			final boolean shortLabels, final boolean plotLoops);

	protected abstract void createPlotDeploymentOperationDependencyGraph(final SystemModelRepository systemRepository,
			final Distributor<MessageTrace> messageTraceDistributor, final List<String> decoratorList,
			final String outputPathName, final String outputFileName,
			final boolean includeWeights, final boolean shortLabels,
			final boolean plotLoops);

	protected abstract void createPlotAssemblyOperationDependencyGraph(final SystemModelRepository systemRepository,
			final Distributor<MessageTrace> messageTraceDistributor,
			final List<String> decoratorList, final String outputPathName, final String outputFileName,
			final boolean includeWeights, final boolean shortLabels,
			final boolean plotLoops);

	protected abstract void createTraceCallTreeFilter(final SystemModelRepository systemRepository,
			final Distributor<MessageTrace> messageTraceDistributor,
			final String pathPrefix, final boolean shortLabels);

	protected abstract void createPlotAggregatedDeploymentCallTree(final SystemModelRepository systemRepository,
			final Distributor<MessageTrace> messageTraceDistributor,
			final String pathPrefix, final boolean includeWeights, final boolean shortLabels);

	protected abstract void createAggrAssemblyCompOpCallTreeFilter(final SystemModelRepository systemRepository,
			final Distributor<MessageTrace> messageTraceDistributor,
			final String pathPrefix, final boolean includeWeights, final boolean shortLabels);

	private void createPrintTraceEquivalenceClasses(final SystemModelRepository systemRepository,
			final Distributor<ExecutionTrace> executionTraceDistributor,
			final String pathPrefix, final TraceEquivalenceClassModes equivalenceClassMode) {
		final String filePrefix;
		switch (equivalenceClassMode) {
			case ASSEMBLY:
				filePrefix = AbstractTraceAnalysisConfiguration.TRACE_ASSEMBLY_EQUIV_CLASSES_FN_PREFIX;
				break;
			case ALLOCATION:
				filePrefix = AbstractTraceAnalysisConfiguration.TRACE_ALLOCATION_EQUIV_CLASSES_FN_PREFIX;
				break;
			case DISABLED:
			default:
				return;
		}
		final TraceEquivalenceClassFilter traceAllocationEquivalenceClassFilter = new TraceEquivalenceClassFilter(
				systemRepository, equivalenceClassMode);
		final EquivalenceClassWriter equivalenceClassWriter = new EquivalenceClassWriter(
				new File(pathPrefix + filePrefix + AbstractTraceAnalysisConfiguration.TXT_SUFFIX));

		this.connectPorts(executionTraceDistributor.getNewOutputPort(),
				traceAllocationEquivalenceClassFilter.getInputPort());
		this.connectPorts(traceAllocationEquivalenceClassFilter.getEquivalenceMapOutputPort(),
				equivalenceClassWriter.getInputPort());
	}

	private void createComponentPrintMsgTrace(final SystemModelRepository systemRepository,
			final Distributor<MessageTrace> messageTraceDistributor,
			final String pathPrefix) {
		try {
			final MessageTraceWriterFilter messageWriterStink = new MessageTraceWriterFilter(systemRepository,
					new File(pathPrefix + AbstractTraceAnalysisConfiguration.MESSAGE_TRACES_FN_PREFIX
							+ AbstractTraceAnalysisConfiguration.TXT_SUFFIX));
			this.connectPorts(messageTraceDistributor.getNewOutputPort(), messageWriterStink.getInputPort());
		} catch (final IOException e) {
			if (AbstractTraceAnalysisConfiguration.LOGGER.isErrorEnabled()) {
				AbstractTraceAnalysisConfiguration.LOGGER.error(String.format("Error initializing %s cause %s",
						MessageTraceWriterFilter.class, e.getLocalizedMessage()));
			}
		}
	}

	private void createComponentPrintExecTrace(final SystemModelRepository systemRepository,
			final Distributor<ExecutionTrace> executionTraceDistributor,
			final String pathPrefix) {
		try {
			final ExecutionTraceWriterFilter executionWriterStink = new ExecutionTraceWriterFilter(systemRepository,
					new File(pathPrefix + AbstractTraceAnalysisConfiguration.EXECUTION_TRACES_FN_PREFIX
							+ AbstractTraceAnalysisConfiguration.TXT_SUFFIX));
			this.connectPorts(executionTraceDistributor.getNewOutputPort(), executionWriterStink.getInputPort());
		} catch (final IOException e) {
			if (AbstractTraceAnalysisConfiguration.LOGGER.isErrorEnabled()) {
				AbstractTraceAnalysisConfiguration.LOGGER.error(String.format("Error initializing %s cause %s",
						ExecutionTraceWriterFilter.class, e.getLocalizedMessage()));
			}
		}
	}

	private void createInvalidExecutionTraceWriterSink(final SystemModelRepository systemRepository,
			final Distributor<InvalidExecutionTrace> invalidExecutionTraceDistributor, final String pathPrefix) {
		try {
			final InvalidExecutionTraceWriterSink invalidExecutionTraceWriterSink = new InvalidExecutionTraceWriterSink(
					systemRepository,
					new File(pathPrefix + AbstractTraceAnalysisConfiguration.INVALID_TRACES_FN_PREFIX
							+ AbstractTraceAnalysisConfiguration.TXT_SUFFIX));
			this.connectPorts(invalidExecutionTraceDistributor.getNewOutputPort(),
					invalidExecutionTraceWriterSink.getInputPort());
		} catch (final IOException e) {
			if (AbstractTraceAnalysisConfiguration.LOGGER.isErrorEnabled()) {
				AbstractTraceAnalysisConfiguration.LOGGER
						.error(String.format("Error initializing %s cause %s", InvalidExecutionTraceWriterSink.class,
								e.getLocalizedMessage()));
			}
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
	 * @param decoratorList list of decorators to add
	 * @param plugin        the plugin to add the decorators to
	 */
	protected void addDecorators(final List<String> decoratorList, final AbstractDependencyGraphFilter<?> plugin) {
		if (decoratorList != null) {
			final Iterator<String> decoratorIterator = decoratorList.iterator();

			// cannot use for loop iterator, as response time coloring uses multiple
			// elements.
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
				} else if (AbstractTraceAnalysisConfiguration.RESPONSE_TIME_COLORING_DECORATOR_FLAG
						.equals(currentDecoratorStr)) {
					// if decorator is responseColoring, next value should be the threshold
					final String thresholdStringStr = decoratorIterator.next();

					try {
						final int threshold = Integer.parseInt(thresholdStringStr);

						plugin.addDecorator(new ResponseTimeColorNodeDecorator(threshold));
					} catch (final NumberFormatException exc) {
						if (AbstractTraceAnalysisConfiguration.LOGGER.isErrorEnabled()) {
							AbstractTraceAnalysisConfiguration.LOGGER.error(
									"Failed to parse int value of property threshold(ms) : {}", thresholdStringStr);
						}
					}
				} else {
					if (AbstractTraceAnalysisConfiguration.LOGGER.isWarnEnabled()) {
						AbstractTraceAnalysisConfiguration.LOGGER.warn("Unknown decoration name '{}'.",
								currentDecoratorStr);
					}
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
