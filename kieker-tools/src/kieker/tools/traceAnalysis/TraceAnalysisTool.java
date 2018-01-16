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

package kieker.tools.traceAnalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import kieker.analysis.AnalysisController;
import kieker.analysis.analysisComponent.AbstractAnalysisComponent;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.filter.flow.EventRecordTraceReconstructionFilter;
import kieker.analysis.plugin.filter.flow.ThreadEvent2TraceEventFilter;
import kieker.analysis.plugin.filter.forward.StringBufferFilter;
import kieker.analysis.plugin.filter.select.TimestampFilter;
import kieker.analysis.plugin.filter.select.TraceIdFilter;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.util.filesystem.FSUtil;
import kieker.tools.AbstractCommandLineTool;
import kieker.tools.traceAnalysis.filter.AbstractGraphProducingFilter;
import kieker.tools.traceAnalysis.filter.AbstractMessageTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.IGraphOutputtingFilter;
import kieker.tools.traceAnalysis.filter.executionRecordTransformation.ExecutionRecordTransformationFilter;
import kieker.tools.traceAnalysis.filter.flow.EventRecordTraceCounter;
import kieker.tools.traceAnalysis.filter.flow.TraceEventRecords2ExecutionAndMessageTraceFilter;
import kieker.tools.traceAnalysis.filter.systemModel.SystemModel2FileFilter;
import kieker.tools.traceAnalysis.filter.traceFilter.TraceEquivalenceClassFilter;
import kieker.tools.traceAnalysis.filter.traceFilter.TraceEquivalenceClassFilter.TraceEquivalenceClassModes;
import kieker.tools.traceAnalysis.filter.traceReconstruction.TraceReconstructionFilter;
import kieker.tools.traceAnalysis.filter.traceWriter.ExecutionTraceWriterFilter;
import kieker.tools.traceAnalysis.filter.traceWriter.InvalidExecutionTraceWriterFilter;
import kieker.tools.traceAnalysis.filter.traceWriter.MessageTraceWriterFilter;
import kieker.tools.traceAnalysis.filter.visualization.AbstractGraphFilter;
import kieker.tools.traceAnalysis.filter.visualization.GraphWriterPlugin;
import kieker.tools.traceAnalysis.filter.visualization.callTree.AbstractAggregatedCallTreeFilter;
import kieker.tools.traceAnalysis.filter.visualization.callTree.AggregatedAllocationComponentOperationCallTreeFilter;
import kieker.tools.traceAnalysis.filter.visualization.callTree.AggregatedAssemblyComponentOperationCallTreeFilter;
import kieker.tools.traceAnalysis.filter.visualization.callTree.TraceCallTreeFilter;
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.AbstractDependencyGraphFilter;
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.ComponentDependencyGraphAllocationFilter;
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.ComponentDependencyGraphAssemblyFilter;
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.ContainerDependencyGraphFilter;
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.OperationDependencyGraphAllocationFilter;
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.OperationDependencyGraphAssemblyFilter;
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.ResponseTimeColorNodeDecorator;
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.ResponseTimeNodeDecorator;
import kieker.tools.traceAnalysis.filter.visualization.descriptions.DescriptionDecoratorFilter;
import kieker.tools.traceAnalysis.filter.visualization.sequenceDiagram.SequenceDiagramFilter;
import kieker.tools.traceAnalysis.filter.visualization.traceColoring.TraceColoringFilter;
import kieker.tools.traceAnalysis.repository.DescriptionRepository;
import kieker.tools.traceAnalysis.repository.TraceColorRepository;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.tools.util.CLIHelpFormatter;
import kieker.tools.util.LoggingTimestampConverter;

/**
 * This is the main class to start the Kieker TraceAnalysisTool - the model synthesis and analysis tool to process the
 * monitoring data that comes from the instrumented system, or from a file that contains Kieker monitoring data. The
 * Kieker TraceAnalysisTool can produce output such as sequence diagrams, dependency graphs on demand. Alternatively it
 * can be used continuously for online performance analysis, anomaly detection or live visualization of system behavior.
 *
 * @author Andre van Hoorn, Matthias Rohr, Nils Christian Ehmke
 *
 * @since 0.95a
 */
public final class TraceAnalysisTool extends AbstractCommandLineTool { // NOPMD (long class)
	public static final String DATE_FORMAT_PATTERN_CMD_USAGE_HELP = Constants.DATE_FORMAT_PATTERN.replaceAll("'", "")
			+ " | timestamp"; // only for usage info

	private static final Log LOG = LogFactory.getLog(TraceAnalysisTool.class);
	private static final String ENCODING = "UTF-8";

	private final AnalysisController analysisController = new AnalysisController();
	private String[] inputDirs;
	private String outputDir;
	private String outputFnPrefix;
	private Set<Long> selectedTraces; // null means select all
	private boolean invertTraceIdFilter;
	private boolean ignoreAssumedCalls;
	private boolean shortLabels = true;
	private boolean includeSelfLoops; // false
	private boolean ignoreInvalidTraces; // false
	private boolean repairEventBasedTraces; // false
	private int maxTraceDurationMillis = 10 * 60 * 1000; // 10 minutes default
	private long ignoreExecutionsBeforeTimestamp = Long.parseLong(TimestampFilter.CONFIG_PROPERTY_VALUE_MIN_TIMESTAMP);
	private long ignoreExecutionsAfterTimestamp = Long.parseLong(TimestampFilter.CONFIG_PROPERTY_VALUE_MAX_TIMESTAMP);

	private CommandLine cmdl;

	private TraceAnalysisTool(final boolean useSystemExit) {
		super(useSystemExit);
	}

	public static void main(final String[] args) {
		TraceAnalysisTool.mainHelper(args, true);
	}

	public static void mainHelper(final String[] args, final boolean useSystemExit) {
		new TraceAnalysisTool(useSystemExit).start(args);
	}

	@Override
	protected void addAdditionalOptions(final Options options) {
		// Remember the inherited options for the help formatter
		final List<Option> inheritedOptions = new ArrayList<>();
		inheritedOptions.addAll(options.getOptions());

		for (final Object option : Constants.CMDL_OPTIONS.getOptions()) {
			options.addOption((Option) option);
		}

		for (final Option option : inheritedOptions) {
			if (!Constants.SORTED_OPTION_LIST.contains(option)) {
				Constants.SORTED_OPTION_LIST.add(option);
			}
		}
	}

	@Override
	protected boolean readPropertiesFromCommandLine(final CommandLine commandLine) {
		this.cmdl = commandLine;
		return (this.initFromArgs(commandLine) && this.assertOutputDirExists()
				&& this.assertInputDirsExistsAndAreMonitoringLogs());
	}

	@Override
	protected boolean performTask() {
		this.dumpConfiguration();
		return this.dispatchTasks();
	}

	@Override
	protected HelpFormatter getHelpFormatter() {
		final HelpFormatter helpFormatter = new CLIHelpFormatter();

		helpFormatter.setOptionComparator(new OptionComparator());

		return helpFormatter;
	}

	/**
	 * This method uses the (already parsed and stored) command line arguments to initialize the tool.
	 *
	 * @param commandLine
	 *
	 * @return true if and only if the tool has been initialized correctly.
	 */
	private boolean initFromArgs(final CommandLine commandLine) {
		this.inputDirs = commandLine.getOptionValues(Constants.CMD_OPT_NAME_INPUTDIRS);
		this.outputDir = commandLine.getOptionValue(Constants.CMD_OPT_NAME_OUTPUTDIR);
		this.outputFnPrefix = this.cmdl.getOptionValue(Constants.CMD_OPT_NAME_OUTPUTFNPREFIX, "");

		if (this.cmdl.hasOption(Constants.CMD_OPT_NAME_SELECTTRACES)
				&& this.cmdl.hasOption(Constants.CMD_OPT_NAME_FILTERTRACES)) {
			LOG.error("Trace Id selection and filtering are mutually exclusive");
			return false;
		}

		if (this.cmdl.hasOption(Constants.CMD_OPT_NAME_SELECTTRACES)
				|| this.cmdl.hasOption(Constants.CMD_OPT_NAME_FILTERTRACES)) { // Parse list of trace Ids
			this.invertTraceIdFilter = this.cmdl.hasOption(Constants.CMD_OPT_NAME_FILTERTRACES);
			final String[] traceIdList = this.cmdl
					.getOptionValues(this.invertTraceIdFilter ? Constants.CMD_OPT_NAME_FILTERTRACES // NOCS (Short if
																									// operator)
							: Constants.CMD_OPT_NAME_SELECTTRACES);

			this.selectedTraces = new TreeSet<>();

			final int numSelectedTraces = traceIdList.length;
			try {
				for (final String idStr : traceIdList) {
					this.selectedTraces.add(Long.valueOf(idStr));
				}
				LOG.info(numSelectedTraces + " trace" + (numSelectedTraces > 1 ? "s" : "") // NOCS
						+ (this.invertTraceIdFilter ? " filtered" : " selected")); // NOCS
			} catch (final Exception e) { // NOPMD NOCS (IllegalCatchCheck)
				LOG.error("Failed to parse list of trace IDs: " + Arrays.toString(traceIdList), e);
				return false;
			}
		}

		this.shortLabels = commandLine.hasOption(Constants.CMD_OPT_NAME_SHORTLABELS);
		this.includeSelfLoops = commandLine.hasOption(Constants.CMD_OPT_NAME_INCLUDESELFLOOPS);
		this.ignoreInvalidTraces = commandLine.hasOption(Constants.CMD_OPT_NAME_IGNOREINVALIDTRACES);
		this.ignoreAssumedCalls = commandLine.hasOption(Constants.CMD_OPT_NAME_IGNORE_ASSUMED);
		this.repairEventBasedTraces = commandLine.hasOption(Constants.CMD_OPT_NAME_REPAIR_EVENT_BASED_TRACES);

		final String maxTraceDurationStr = commandLine.getOptionValue(Constants.CMD_OPT_NAME_MAXTRACEDURATION,
				Integer.toString(this.maxTraceDurationMillis));
		try {
			this.maxTraceDurationMillis = Integer.parseInt(maxTraceDurationStr);
		} catch (final NumberFormatException exc) {
			LOG.error("Failed to parse int value of property " + Constants.CMD_OPT_NAME_MAXTRACEDURATION
					+ " (must be an integer):" + maxTraceDurationStr, exc);
			return false;
		}

		final DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_PATTERN, Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		try {
			final String ignoreRecordsBeforeTimestampString = commandLine
					.getOptionValue(Constants.CMD_OPT_NAME_IGNOREEXECUTIONSBEFOREDATE, null);
			final String ignoreRecordsAfterTimestampString = commandLine
					.getOptionValue(Constants.CMD_OPT_NAME_IGNOREEXECUTIONSAFTERDATE, null);
			if (ignoreRecordsBeforeTimestampString != null) {
				long ignoreExecutionsBeforeTimestampTemp;
				try {
					ignoreExecutionsBeforeTimestampTemp = Long.parseLong(ignoreRecordsBeforeTimestampString);
					LOG.info("Ignoring records before " + ignoreExecutionsBeforeTimestampTemp);
				} catch (final NumberFormatException ex) {
					final Date ignoreBeforeDate = dateFormat.parse(ignoreRecordsBeforeTimestampString);
					ignoreExecutionsBeforeTimestampTemp = ignoreBeforeDate.getTime() * (1000 * 1000);
					LOG.info("Ignoring records before " + dateFormat.format(ignoreBeforeDate) + " ("
							+ ignoreExecutionsBeforeTimestampTemp + ")");
				}
				this.ignoreExecutionsBeforeTimestamp = ignoreExecutionsBeforeTimestampTemp;
			}
			if (ignoreRecordsAfterTimestampString != null) {
				long ignoreExecutionsAfterTimestampTemp;
				try {
					ignoreExecutionsAfterTimestampTemp = Long.parseLong(ignoreRecordsAfterTimestampString);
					LOG.info("Ignoring records after " + ignoreExecutionsAfterTimestampTemp);
				} catch (final NumberFormatException ex) {
					final Date ignoreAfterDate = dateFormat.parse(ignoreRecordsAfterTimestampString);
					ignoreExecutionsAfterTimestampTemp = ignoreAfterDate.getTime() * (1000 * 1000);
					LOG.info("Ignoring records after " + dateFormat.format(ignoreAfterDate) + " ("
							+ ignoreExecutionsAfterTimestampTemp + ")");
				}
				this.ignoreExecutionsAfterTimestamp = ignoreExecutionsAfterTimestampTemp;

			}
		} catch (final java.text.ParseException ex) {
			final String errorMsg = "Error parsing date/time string. Please use the following pattern: "
					+ DATE_FORMAT_PATTERN_CMD_USAGE_HELP;
			LOG.error(errorMsg, ex);
			return false;
		}
		return true;
	}

	/**
	 * Returns if the specified output directory {@link #outputDir} exists. If the directory does not exist, an error
	 * message is printed to stderr.
	 *
	 * @return true if {@link #outputDir} is exists and is a directory; false otherwise
	 */
	private boolean assertOutputDirExists() {
		if ((this.outputDir == null) || this.outputDir.isEmpty()) {
			LOG.error("No output directory configured");
			return false;
		}

		final File outputDirFile = new File(this.outputDir);

		try {
			if (!outputDirFile.exists()) {
				LOG.error("The specified output directory '" + outputDirFile.getCanonicalPath() + "' does not exist");
				return false;
			}

			if (!outputDirFile.isDirectory()) {
				LOG.error(
						"The specified output directory '" + outputDirFile.getCanonicalPath() + "' is not a directory");
				return false;
			}

		} catch (final IOException e) { // thrown by File.getCanonicalPath()
			LOG.error("Error resolving name of output directory: '" + this.outputDir + "'");
		}

		return true;
	}

	private static void addDecorators(final String[] decoratorNames, final AbstractDependencyGraphFilter<?> plugin) {
		if (decoratorNames == null) {
			return;
		}
		final List<String> decoratorList = Arrays.asList(decoratorNames);
		final Iterator<String> decoratorIterator = decoratorList.iterator();

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
					System.err.println(
							"\nFailed to parse int value of property " + "threshold(ms) : " + thresholdStringStr); // NOPMD
																													// (System.out)
				}
			} else {
				LOG.warn("Unknown decoration name '" + currentDecoratorStr + "'.");
				return;
			}
		}
	}

	/**
	 * Returns if the specified input directories {@link #inputDirs} exist and that each one is a monitoring log. If
	 * this is not the case for one of the directories, an error message is printed to stderr.
	 *
	 * @return true if {@link #outputDir} is exists and is a directory; false otherwise
	 */
	private boolean assertInputDirsExistsAndAreMonitoringLogs() {
		if (this.inputDirs == null) {
			LOG.error("No input directories configured");
			return false;
		}

		for (final String inputDir : this.inputDirs) {
			final File inputDirFile = new File(inputDir);
			try {
				if (!inputDirFile.exists()) {
					LOG.error("The specified input directory '" + inputDirFile.getCanonicalPath() + "' does not exist");
					return false;
				}
				if (!inputDirFile.isDirectory() && !inputDir.endsWith(FSUtil.ZIP_FILE_EXTENSION)) {
					LOG.error("The specified input directory '" + inputDirFile.getCanonicalPath()
							+ "' is neither a directory nor a zip file");
					return false;
				}
				// check whether inputDirFile contains a (kieker|tpmon).map file; the latter for legacy reasons
				if (inputDirFile.isDirectory()) { // only check for dirs
					final File[] mapFiles = { new File(inputDir + File.separatorChar + FSUtil.MAP_FILENAME),
							new File(inputDir + File.separatorChar + FSUtil.LEGACY_MAP_FILENAME), };
					boolean mapFileExists = false;
					for (final File potentialMapFile : mapFiles) {
						if (potentialMapFile.isFile()) {
							mapFileExists = true;
							break;
						}
					}
					if (!mapFileExists) {
						LOG.error("The specified input directory '" + inputDirFile.getCanonicalPath()
								+ "' is not a kieker log directory");
						return false;
					}
				}
			} catch (final IOException e) { // thrown by File.getCanonicalPath()
				LOG.error("Error resolving name of input directory: '" + inputDir + "'");
			}
		}

		return true;
	}

	/**
	 *
	 * @return false iff an error occurred
	 */
	private boolean dispatchTasks() {
		boolean retVal = true;
		int numRequestedTasks = 0;

		final SystemModelRepository systemEntityFactory = new SystemModelRepository(new Configuration(),
				this.analysisController);

		TraceReconstructionFilter mtReconstrFilter = null;
		EventRecordTraceCounter eventRecordTraceCounter = null;
		EventRecordTraceReconstructionFilter eventTraceReconstructionFilter = null;
		TraceEventRecords2ExecutionAndMessageTraceFilter traceEvents2ExecutionAndMessageTraceFilter = null;
		try {
			FSReader reader;
			{ // NOCS (NestedBlock)
				final Configuration conf = new Configuration(null);
				conf.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, Configuration.toProperty(this.inputDirs));
				conf.setProperty(FSReader.CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES, Boolean.TRUE.toString());
				reader = new FSReader(conf, this.analysisController);
			}

			// Unify Strings
			final StringBufferFilter stringBufferFilter = new StringBufferFilter(new Configuration(),
					this.analysisController);
			this.analysisController.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS, stringBufferFilter,
					StringBufferFilter.INPUT_PORT_NAME_EVENTS);

			AbstractPlugin sourceStage;
			String sourcePort;

			sourceStage = stringBufferFilter;
			sourcePort = StringBufferFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS;

			// transforms thread-based events to trace-based events
			final Configuration config = new Configuration();
			final ThreadEvent2TraceEventFilter threadEvent2TraceEventFilter = new ThreadEvent2TraceEventFilter(config,
					this.analysisController);
			this.analysisController.connect(sourceStage, sourcePort, threadEvent2TraceEventFilter,
					ThreadEvent2TraceEventFilter.INPUT_PORT_NAME_DEFAULT);

			sourceStage = threadEvent2TraceEventFilter;
			sourcePort = ThreadEvent2TraceEventFilter.OUTPUT_PORT_NAME_DEFAULT;

			// This map can be used within the constructor for all following plugins which use the repository with the
			// name defined in the
			// AbstractTraceAnalysisPlugin.
			final TimestampFilter timestampFilter;
			{ // NOCS (nested block)
				// Create the timestamp filter and connect to the reader's output port
				final Configuration configTimestampFilter = new Configuration();
				configTimestampFilter.setProperty(TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_BEFORE_TIMESTAMP,
						Long.toString(this.ignoreExecutionsBeforeTimestamp));
				configTimestampFilter.setProperty(TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_AFTER_TIMESTAMP,
						Long.toString(this.ignoreExecutionsAfterTimestamp));

				timestampFilter = new TimestampFilter(configTimestampFilter, this.analysisController);
				this.analysisController.connect(sourceStage, sourcePort, timestampFilter,
						TimestampFilter.INPUT_PORT_NAME_EXECUTION);
				this.analysisController.connect(sourceStage, sourcePort, timestampFilter,
						TimestampFilter.INPUT_PORT_NAME_FLOW);
			}

			final TraceIdFilter traceIdFilter;
			{ // NOCS (nested block)
				// Create the trace ID filter and connect to the timestamp filter's output port
				final Configuration configTraceIdFilterFlow = new Configuration();
				if (this.selectedTraces == null) {
					configTraceIdFilterFlow.setProperty(TraceIdFilter.CONFIG_PROPERTY_NAME_SELECT_ALL_TRACES,
							Boolean.TRUE.toString());
				} else {
					configTraceIdFilterFlow.setProperty(TraceIdFilter.CONFIG_PROPERTY_NAME_SELECT_ALL_TRACES,
							Boolean.FALSE.toString());
					configTraceIdFilterFlow.setProperty(TraceIdFilter.CONFIG_PROPERTY_NAME_SELECTED_TRACES,
							Configuration
									.toProperty(this.selectedTraces.toArray(new Long[this.selectedTraces.size()])));
				}

				traceIdFilter = new TraceIdFilter(configTraceIdFilterFlow, this.analysisController);

				this.analysisController.connect(timestampFilter, TimestampFilter.OUTPUT_PORT_NAME_WITHIN_PERIOD,
						traceIdFilter, TraceIdFilter.INPUT_PORT_NAME_COMBINED);
			}

			final ExecutionRecordTransformationFilter execRecTransformer;
			{ // NOCS (nested block)
				// Create the execution record transformation filter and connect to the trace ID filter's output port
				final Configuration execRecTransformerConfig = new Configuration();
				execRecTransformerConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
						Constants.EXEC_TRACE_RECONSTR_COMPONENT_NAME);
				execRecTransformer = new ExecutionRecordTransformationFilter(execRecTransformerConfig,
						this.analysisController);
				if (this.invertTraceIdFilter) {
					this.analysisController.connect(traceIdFilter, TraceIdFilter.OUTPUT_PORT_NAME_MISMATCH,
							execRecTransformer, ExecutionRecordTransformationFilter.INPUT_PORT_NAME_RECORDS);
				} else {
					this.analysisController.connect(traceIdFilter, TraceIdFilter.OUTPUT_PORT_NAME_MATCH,
							execRecTransformer, ExecutionRecordTransformationFilter.INPUT_PORT_NAME_RECORDS);
				}

				this.analysisController.connect(execRecTransformer,
						AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);
			}

			{ // NOCS (nested block)
				// Create the trace reconstruction filter and connect to the record transformation filter's output port
				final Configuration mtReconstrFilterConfig = new Configuration();
				mtReconstrFilterConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
						Constants.TRACERECONSTR_COMPONENT_NAME);
				mtReconstrFilterConfig.setProperty(TraceReconstructionFilter.CONFIG_PROPERTY_NAME_TIMEUNIT,
						TimeUnit.MILLISECONDS.name());
				mtReconstrFilterConfig.setProperty(TraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION,
						Integer.toString(this.maxTraceDurationMillis));
				mtReconstrFilterConfig.setProperty(TraceReconstructionFilter.CONFIG_PROPERTY_NAME_IGNORE_INVALID_TRACES,
						Boolean.toString(this.ignoreInvalidTraces));
				mtReconstrFilter = new TraceReconstructionFilter(mtReconstrFilterConfig, this.analysisController);
				this.analysisController.connect(mtReconstrFilter,
						AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);
				this.analysisController.connect(execRecTransformer,
						ExecutionRecordTransformationFilter.OUTPUT_PORT_NAME_EXECUTIONS, mtReconstrFilter,
						TraceReconstructionFilter.INPUT_PORT_NAME_EXECUTIONS);
			}

			{ // NOCS (nested block)
				// Create the event record trace generation filter and connect to the trace ID filter's output port
				final Configuration configurationEventRecordTraceGenerationFilter = new Configuration();
				configurationEventRecordTraceGenerationFilter.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
						Constants.EVENTRECORDTRACERECONSTR_COMPONENT_NAME);
				configurationEventRecordTraceGenerationFilter.setProperty(
						EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_TIMEUNIT,
						TimeUnit.MILLISECONDS.name());
				configurationEventRecordTraceGenerationFilter.setProperty(
						EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION,
						Long.toString(this.maxTraceDurationMillis));
				configurationEventRecordTraceGenerationFilter.setProperty(
						EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_REPAIR_EVENT_BASED_TRACES,
						Boolean.toString(this.repairEventBasedTraces));
				eventTraceReconstructionFilter = new EventRecordTraceReconstructionFilter(
						configurationEventRecordTraceGenerationFilter, this.analysisController);

				String outputPortName;
				if (this.invertTraceIdFilter) {
					outputPortName = TraceIdFilter.OUTPUT_PORT_NAME_MISMATCH;
				} else {
					outputPortName = TraceIdFilter.OUTPUT_PORT_NAME_MATCH;
				}
				this.analysisController.connect(traceIdFilter, outputPortName, eventTraceReconstructionFilter,
						EventRecordTraceReconstructionFilter.INPUT_PORT_NAME_TRACE_RECORDS);
			}

			{ // NOCS (nested block)
				// Create the counter for valid/invalid event record traces
				final Configuration configurationEventRecordTraceCounter = new Configuration();
				configurationEventRecordTraceCounter.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
						Constants.EXECEVENTRACESFROMEVENTTRACES_COMPONENT_NAME);
				configurationEventRecordTraceCounter.setProperty(
						EventRecordTraceCounter.CONFIG_PROPERTY_NAME_LOG_INVALID,
						Boolean.toString(!this.ignoreInvalidTraces));
				eventRecordTraceCounter = new EventRecordTraceCounter(configurationEventRecordTraceCounter,
						this.analysisController);

				this.analysisController.connect(eventTraceReconstructionFilter,
						EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_VALID, eventRecordTraceCounter,
						EventRecordTraceCounter.INPUT_PORT_NAME_VALID);
				this.analysisController.connect(eventTraceReconstructionFilter,
						EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_INVALID, eventRecordTraceCounter,
						EventRecordTraceCounter.INPUT_PORT_NAME_INVALID);
			}

			{ // NOCS (nested block)
				// Create the event trace to execution/message trace transformation filter and connect its input to the
				// event record trace generation filter's output
				// port
				final Configuration configurationEventTrace2ExecutionTraceFilter = new Configuration();
				configurationEventTrace2ExecutionTraceFilter.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
						Constants.EXECTRACESFROMEVENTTRACES_COMPONENT_NAME);
				configurationEventTrace2ExecutionTraceFilter.setProperty(
						TraceEventRecords2ExecutionAndMessageTraceFilter.CONFIG_IGNORE_ASSUMED,
						Boolean.toString(this.ignoreAssumedCalls));
				// EventTrace2ExecutionTraceFilter has no configuration properties
				traceEvents2ExecutionAndMessageTraceFilter = new TraceEventRecords2ExecutionAndMessageTraceFilter(
						configurationEventTrace2ExecutionTraceFilter, this.analysisController);

				this.analysisController.connect(eventTraceReconstructionFilter,
						EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_VALID,
						traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.INPUT_PORT_NAME_EVENT_TRACE);
				this.analysisController.connect(traceEvents2ExecutionAndMessageTraceFilter,
						AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);
			}

			final List<AbstractTraceProcessingFilter> allTraceProcessingComponents = new ArrayList<>();
			final List<AbstractGraphProducingFilter<?>> allGraphProducers = new ArrayList<>();

			final Configuration traceAllocationEquivClassFilterConfig = new Configuration();
			traceAllocationEquivClassFilterConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
					Constants.TRACEALLOCATIONEQUIVCLASS_COMPONENT_NAME);
			traceAllocationEquivClassFilterConfig.setProperty(
					TraceEquivalenceClassFilter.CONFIG_PROPERTY_NAME_EQUIVALENCE_MODE,
					TraceEquivalenceClassModes.ALLOCATION.toString());
			TraceEquivalenceClassFilter traceAllocationEquivClassFilter = null; // must not be instantiate it here, due
																				// to side-effects in the constructor
			if (this.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_ALLOCATIONEQUIVCLASSREPORT)) {
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

			final Configuration traceAssemblyEquivClassFilterConfig = new Configuration();
			traceAssemblyEquivClassFilterConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
					Constants.TRACEASSEMBLYEQUIVCLASS_COMPONENT_NAME);
			traceAssemblyEquivClassFilterConfig.setProperty(
					TraceEquivalenceClassFilter.CONFIG_PROPERTY_NAME_EQUIVALENCE_MODE,
					TraceEquivalenceClassModes.ASSEMBLY.toString());
			TraceEquivalenceClassFilter traceAssemblyEquivClassFilter = null; // must not be instantiate it here, due to
																				// side-effects in the constructor
			if (this.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_ASSEMBLYEQUIVCLASSREPORT)) {
				/**
				 * Currently, this filter is only used to print an equivalence report. That's why we only activate it in
				 * case this options is requested.
				 */
				traceAssemblyEquivClassFilter = new TraceEquivalenceClassFilter(traceAssemblyEquivClassFilterConfig,
						this.analysisController);
				this.analysisController.connect(mtReconstrFilter,
						TraceReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE, traceAssemblyEquivClassFilter,
						TraceEquivalenceClassFilter.INPUT_PORT_NAME_EXECUTION_TRACE);
				this.analysisController.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
						traceAssemblyEquivClassFilter, TraceEquivalenceClassFilter.INPUT_PORT_NAME_EXECUTION_TRACE);
				this.analysisController.connect(traceAssemblyEquivClassFilter,
						AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);
				allTraceProcessingComponents.add(traceAssemblyEquivClassFilter);
			}

			// fill list of msgTraceProcessingComponents:
			MessageTraceWriterFilter componentPrintMsgTrace = null;
			if (this.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PRINTMSGTRACES)) {
				numRequestedTasks++;
				final Configuration componentPrintMsgTraceConfig = new Configuration();
				componentPrintMsgTraceConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
						Constants.PRINTMSGTRACE_COMPONENT_NAME);
				componentPrintMsgTraceConfig.setProperty(MessageTraceWriterFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN,
						new File(this.outputDir + File.separator + this.outputFnPrefix
								+ Constants.MESSAGE_TRACES_FN_PREFIX + ".txt").getCanonicalPath());
				componentPrintMsgTrace = new MessageTraceWriterFilter(componentPrintMsgTraceConfig,
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
			ExecutionTraceWriterFilter componentPrintExecTrace = null;
			if (this.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PRINTEXECTRACES)) {
				numRequestedTasks++;
				final Configuration componentPrintExecTraceConfig = new Configuration();
				componentPrintExecTraceConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
						Constants.PRINTEXECTRACE_COMPONENT_NAME);
				componentPrintExecTraceConfig.setProperty(ExecutionTraceWriterFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN,
						new File(this.outputDir + File.separator + this.outputFnPrefix
								+ Constants.EXECUTION_TRACES_FN_PREFIX + ".txt").getCanonicalPath());
				componentPrintExecTrace = new ExecutionTraceWriterFilter(componentPrintExecTraceConfig,
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
			InvalidExecutionTraceWriterFilter componentPrintInvalidTrace = null;
			if (this.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES)) {
				numRequestedTasks++;
				final Configuration componentPrintInvalidTraceConfig = new Configuration();
				componentPrintInvalidTraceConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
						Constants.PRINTINVALIDEXECTRACE_COMPONENT_NAME);
				componentPrintInvalidTraceConfig.setProperty(
						InvalidExecutionTraceWriterFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN,
						new File(this.outputDir + File.separator + this.outputFnPrefix
								+ Constants.INVALID_TRACES_FN_PREFIX + ".txt").getCanonicalPath());
				componentPrintInvalidTrace = new InvalidExecutionTraceWriterFilter(componentPrintInvalidTraceConfig,
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
			SequenceDiagramFilter componentPlotAllocationSeqDiagr = null;
			if (retVal && this.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONSEQDS)) {
				numRequestedTasks++;
				final Configuration componentPlotAllocationSeqDiagrConfig = new Configuration();
				componentPlotAllocationSeqDiagrConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
						Constants.PLOTALLOCATIONSEQDIAGR_COMPONENT_NAME);
				componentPlotAllocationSeqDiagrConfig.setProperty(
						SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN_BASE, this.outputDir + File.separator
								+ this.outputFnPrefix + Constants.ALLOCATION_SEQUENCE_DIAGRAM_FN_PREFIX);
				componentPlotAllocationSeqDiagrConfig.setProperty(
						SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_SDMODE,
						SequenceDiagramFilter.SDModes.ALLOCATION.toString());
				componentPlotAllocationSeqDiagrConfig.setProperty(
						SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_SHORTLABES,
						Boolean.toString(this.shortLabels));
				componentPlotAllocationSeqDiagr = new SequenceDiagramFilter(componentPlotAllocationSeqDiagrConfig,
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
			SequenceDiagramFilter componentPlotAssemblySeqDiagr = null;
			if (retVal && this.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYSEQDS)) {
				numRequestedTasks++;
				final Configuration componentPlotAssemblySeqDiagrConfig = new Configuration();
				componentPlotAssemblySeqDiagrConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
						Constants.PLOTASSEMBLYSEQDIAGR_COMPONENT_NAME);
				componentPlotAssemblySeqDiagrConfig
						.setProperty(SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN_BASE, this.outputDir
								+ File.separator + this.outputFnPrefix + Constants.ASSEMBLY_SEQUENCE_DIAGRAM_FN_PREFIX);
				componentPlotAssemblySeqDiagrConfig.setProperty(
						SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_SDMODE,
						SequenceDiagramFilter.SDModes.ASSEMBLY.toString());
				componentPlotAssemblySeqDiagrConfig.setProperty(
						SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_SHORTLABES,
						Boolean.toString(this.shortLabels));
				componentPlotAssemblySeqDiagr = new SequenceDiagramFilter(componentPlotAssemblySeqDiagrConfig,
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

			ComponentDependencyGraphAllocationFilter componentPlotAllocationComponentDepGraph = null;
			if (retVal && this.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONCOMPONENTDEPG)) {
				numRequestedTasks++;
				final Configuration configuration = new Configuration();
				componentPlotAllocationComponentDepGraph = new ComponentDependencyGraphAllocationFilter(configuration,
						this.analysisController);

				final String[] nodeDecorations = this.cmdl
						.getOptionValues(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONCOMPONENTDEPG);
				TraceAnalysisTool.addDecorators(nodeDecorations, componentPlotAllocationComponentDepGraph);

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

			ComponentDependencyGraphAssemblyFilter componentPlotAssemblyComponentDepGraph = null;
			if (retVal && this.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYCOMPONENTDEPG)) {
				numRequestedTasks++;
				final Configuration configuration = new Configuration();
				componentPlotAssemblyComponentDepGraph = new ComponentDependencyGraphAssemblyFilter(configuration,
						this.analysisController);

				final String[] nodeDecorations = this.cmdl
						.getOptionValues(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYCOMPONENTDEPG);
				TraceAnalysisTool.addDecorators(nodeDecorations, componentPlotAssemblyComponentDepGraph);

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

			ContainerDependencyGraphFilter componentPlotContainerDepGraph = null;
			if (retVal && this.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG)) {
				numRequestedTasks++;
				final Configuration configuration = new Configuration();
				componentPlotContainerDepGraph = new ContainerDependencyGraphFilter(configuration,
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

			OperationDependencyGraphAllocationFilter componentPlotAllocationOperationDepGraph = null;
			if (retVal && this.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONOPERATIONDEPG)) {
				numRequestedTasks++;
				final Configuration configuration = new Configuration();
				componentPlotAllocationOperationDepGraph = new OperationDependencyGraphAllocationFilter(configuration,
						this.analysisController);

				final String[] nodeDecorations = this.cmdl
						.getOptionValues(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONOPERATIONDEPG);
				TraceAnalysisTool.addDecorators(nodeDecorations, componentPlotAllocationOperationDepGraph);

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

			OperationDependencyGraphAssemblyFilter componentPlotAssemblyOperationDepGraph = null;
			if (retVal && this.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYOPERATIONDEPG)) {
				numRequestedTasks++;
				final Configuration configuration = new Configuration();
				componentPlotAssemblyOperationDepGraph = new OperationDependencyGraphAssemblyFilter(configuration,
						this.analysisController);

				final String[] nodeDecorations = this.cmdl
						.getOptionValues(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYOPERATIONDEPG);
				TraceAnalysisTool.addDecorators(nodeDecorations, componentPlotAssemblyOperationDepGraph);

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

			TraceCallTreeFilter componentPlotTraceCallTrees = null;
			if (retVal && this.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTCALLTREES)) {
				numRequestedTasks++;

				final Configuration componentPlotTraceCallTreesConfig = new Configuration();

				componentPlotTraceCallTreesConfig.setProperty(TraceCallTreeFilter.CONFIG_PROPERTY_NAME_OUTPUT_FILENAME,
						new File(this.outputDir + File.separator + this.outputFnPrefix + Constants.CALL_TREE_FN_PREFIX)
								.getCanonicalPath());
				componentPlotTraceCallTreesConfig.setProperty(TraceCallTreeFilter.CONFIG_PROPERTY_NAME_SHORT_LABELS,
						Boolean.toString(this.shortLabels));
				componentPlotTraceCallTreesConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
						Constants.PLOTCALLTREE_COMPONENT_NAME);
				componentPlotTraceCallTrees = new TraceCallTreeFilter(componentPlotTraceCallTreesConfig,
						this.analysisController);

				this.analysisController.connect(mtReconstrFilter,
						TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, componentPlotTraceCallTrees,
						AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				this.analysisController.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotTraceCallTrees,
						AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				this.analysisController.connect(componentPlotTraceCallTrees,
						AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);
				allTraceProcessingComponents.add(componentPlotTraceCallTrees);
			}
			AggregatedAllocationComponentOperationCallTreeFilter componentPlotAggregatedCallTree = null;
			if (retVal && this.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTAGGREGATEDALLOCATIONCALLTREE)) {
				numRequestedTasks++;
				final Configuration componentPlotAggregatedCallTreeConfig = new Configuration();
				componentPlotAggregatedCallTreeConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
						Constants.PLOTAGGREGATEDALLOCATIONCALLTREE_COMPONENT_NAME);
				componentPlotAggregatedCallTreeConfig.setProperty(
						AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, Boolean.toString(true));
				componentPlotAggregatedCallTreeConfig.setProperty(
						AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_SHORT_LABELS,
						Boolean.toString(this.shortLabels));
				componentPlotAggregatedCallTreeConfig.setProperty(
						AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_OUTPUT_FILENAME,
						this.outputDir + File.separator + this.outputFnPrefix
								+ Constants.AGGREGATED_ALLOCATION_CALL_TREE_FN_PREFIX + ".dot");
				componentPlotAggregatedCallTree = new AggregatedAllocationComponentOperationCallTreeFilter(
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
			AggregatedAssemblyComponentOperationCallTreeFilter componentPlotAssemblyCallTree = null;
			if (retVal && this.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTAGGREGATEDASSEMBLYCALLTREE)) {
				numRequestedTasks++;
				final Configuration componentPlotAssemblyCallTreeConfig = new Configuration();
				componentPlotAssemblyCallTreeConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
						Constants.PLOTAGGREGATEDASSEMBLYCALLTREE_COMPONENT_NAME);
				componentPlotAssemblyCallTreeConfig.setProperty(
						AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, Boolean.toString(true));
				componentPlotAssemblyCallTreeConfig.setProperty(
						AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_SHORT_LABELS,
						Boolean.toString(this.shortLabels));
				componentPlotAssemblyCallTreeConfig.setProperty(
						AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_OUTPUT_FILENAME,
						this.outputDir + File.separator + this.outputFnPrefix
								+ Constants.AGGREGATED_ASSEMBLY_CALL_TREE_FN_PREFIX + ".dot");
				componentPlotAssemblyCallTree = new AggregatedAssemblyComponentOperationCallTreeFilter(
						componentPlotAssemblyCallTreeConfig, this.analysisController);

				this.analysisController.connect(mtReconstrFilter,
						TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, componentPlotAssemblyCallTree,
						AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				this.analysisController.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblyCallTree,
						AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				this.analysisController.connect(componentPlotAssemblyCallTree,
						AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);
				allTraceProcessingComponents.add(componentPlotAssemblyCallTree);
			}
			if (retVal && this.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_ALLOCATIONEQUIVCLASSREPORT)) {
				numRequestedTasks++;
				// the actual execution of the task is performed below
			}
			if (this.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PRINTSYSTEMMODEL)) {
				numRequestedTasks++;
			}

			// Attach graph processors to the graph producers
			this.attachGraphProcessors(allGraphProducers, this.analysisController, this.cmdl);

			if (numRequestedTasks == 0) {
				LOG.error("No task requested");
				LOG.info("Use the option `--" + CMD_OPT_NAME_HELP_LONG + "` for usage information");
				return false;
			}

			if (retVal) {
				final String systemEntitiesHtmlFn = this.outputDir + File.separator + this.outputFnPrefix
						+ "system-entities.html";
				final Configuration systemModel2FileFilterConfig = new Configuration();
				systemModel2FileFilterConfig.setProperty(SystemModel2FileFilter.CONFIG_PROPERTY_NAME_HTML_OUTPUT_FN,
						systemEntitiesHtmlFn);
				final SystemModel2FileFilter systemModel2FileFilter = new SystemModel2FileFilter(
						systemModel2FileFilterConfig, this.analysisController);
				// note that this plugin is (currently) not connected to any other filters
				this.analysisController.connect(systemModel2FileFilter,
						AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemEntityFactory);
			}

			int numErrorCount = 0;
			try {
				this.analysisController.run();
				if (this.analysisController.getState() != AnalysisController.STATE.TERMINATED) {
					// Analysis did not terminate successfully
					retVal = false; // Error message referring to log will be printed later
					LOG.error("Analysis instance terminated in state other than" + AnalysisController.STATE.TERMINATED
							+ ":" + this.analysisController.getState());
				}
			} finally {
				for (final AbstractTraceProcessingFilter c : allTraceProcessingComponents) {
					numErrorCount += c.getErrorCount();
					c.printStatusMessage();
				}
				final String kaxOutputFn = this.outputDir + File.separator + this.outputFnPrefix + "traceAnalysis.kax";
				final File kaxOutputFile = new File(kaxOutputFn);
				try { // NOCS (nested try)
						// Try to serialize analysis configuration to .kax file
					this.analysisController.saveToFile(kaxOutputFile);
					LOG.info("Saved analysis configuration to file '" + kaxOutputFile.getCanonicalPath() + "'");
				} catch (final IOException ex) {
					LOG.error(
							"Failed to save analysis configuration to file '" + kaxOutputFile.getCanonicalPath() + "'");
				}
			}
			if (!this.ignoreInvalidTraces && (numErrorCount > 0)) {
				throw new Exception(numErrorCount + " errors occured in trace processing components");
			}

			if (retVal && this.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_ALLOCATIONEQUIVCLASSREPORT)) {
				retVal = this.writeTraceEquivalenceReport(
						this.outputDir + File.separator + this.outputFnPrefix
								+ Constants.TRACE_ALLOCATION_EQUIV_CLASSES_FN_PREFIX + ".txt",
						traceAllocationEquivClassFilter);
			}

			if (retVal && this.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_ASSEMBLYEQUIVCLASSREPORT)) {
				retVal = this.writeTraceEquivalenceReport(
						this.outputDir + File.separator + this.outputFnPrefix
								+ Constants.TRACE_ASSEMBLY_EQUIV_CLASSES_FN_PREFIX + ".txt",
						traceAssemblyEquivClassFilter);
			}
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			LOG.error("An error occured", ex);
			retVal = false;
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

		return retVal;
	}

	/**
	 * This method dumps the configuration on the screen.
	 */
	private void dumpConfiguration() {
		LOG.debug("#");
		LOG.debug("# Configuration");
		for (final Option o : Constants.SORTED_OPTION_LIST) {
			final String longOpt = o.getLongOpt();
			String val = "<null>";
			if (longOpt.equals(Constants.CMD_OPT_NAME_INPUTDIRS)) {
				val = Constants.stringArrToStringList(this.inputDirs);
			} else if (longOpt.equals(Constants.CMD_OPT_NAME_OUTPUTDIR)) {
				val = this.outputDir;
			} else if (longOpt.equals(Constants.CMD_OPT_NAME_OUTPUTFNPREFIX)) {
				val = this.outputFnPrefix;
			} else if (longOpt.equals(Constants.CMD_OPT_NAME_TASK_ALLOCATIONEQUIVCLASSREPORT)
					|| longOpt.equals(Constants.CMD_OPT_NAME_TASK_ASSEMBLYEQUIVCLASSREPORT)
					|| longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONSEQDS)
					|| longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYSEQDS)
					|| longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONCOMPONENTDEPG)
					|| longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYCOMPONENTDEPG)
					|| longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG)
					|| longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONOPERATIONDEPG)
					|| longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYOPERATIONDEPG)
					|| longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTAGGREGATEDALLOCATIONCALLTREE)
					|| longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTAGGREGATEDASSEMBLYCALLTREE)
					|| longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTCALLTREES)
					|| longOpt.equals(Constants.CMD_OPT_NAME_TASK_PRINTEXECTRACES)
					|| longOpt.equals(Constants.CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES)
					|| longOpt.equals(Constants.CMD_OPT_NAME_TASK_PRINTMSGTRACES)
					|| longOpt.equals(Constants.CMD_OPT_NAME_TASK_PRINTSYSTEMMODEL)
					|| longOpt.equals(AbstractCommandLineTool.CMD_OPT_NAME_DEBUG_LONG)
					|| longOpt.equals(AbstractCommandLineTool.CMD_OPT_NAME_VERBOSE_LONG)
					|| longOpt.equals(AbstractCommandLineTool.CMD_OPT_NAME_HELP_LONG)) {
				val = this.cmdl.hasOption(longOpt) ? "true" : "false"; // NOCS
			} else if (longOpt.equals(Constants.CMD_OPT_NAME_SELECTTRACES)) {
				if (this.selectedTraces != null) {
					val = this.selectedTraces.toString();
				} else {
					val = "<select all>";
				}

			} else if (longOpt.equals(Constants.CMD_OPT_NAME_FILTERTRACES)) {
				if (this.selectedTraces != null) {
					val = this.selectedTraces.toString();
				} else {
					val = "<filter none>";
				}

			} else if (longOpt.equals(Constants.CMD_OPT_NAME_SHORTLABELS)) {
				val = this.shortLabels ? "true" : "false"; // NOCS
			} else if (longOpt.equals(Constants.CMD_OPT_NAME_INCLUDESELFLOOPS)) {
				val = this.includeSelfLoops ? "true" : "false"; // NOCS
			} else if (longOpt.equals(Constants.CMD_OPT_NAME_IGNORE_ASSUMED)) {
				val = this.ignoreAssumedCalls ? "true" : "false"; // NOCS
			} else if (longOpt.equals(Constants.CMD_OPT_NAME_IGNOREINVALIDTRACES)) {
				val = this.ignoreInvalidTraces ? "true" : "false"; // NOCS
			} else if (longOpt.equals(Constants.CMD_OPT_NAME_REPAIR_EVENT_BASED_TRACES)) {
				val = this.repairEventBasedTraces ? "true" : "false"; // NOCS
			} else if (longOpt.equals(Constants.CMD_OPT_NAME_MAXTRACEDURATION)) {
				val = this.maxTraceDurationMillis + " ms";
			} else if (longOpt.equals(Constants.CMD_OPT_NAME_IGNOREEXECUTIONSBEFOREDATE)) {
				val = LoggingTimestampConverter.convertLoggingTimestampToUTCString(this.ignoreExecutionsBeforeTimestamp)
						+ " (" + LoggingTimestampConverter
								.convertLoggingTimestampLocalTimeZoneString(this.ignoreExecutionsBeforeTimestamp)
						+ ")";
			} else if (longOpt.equals(Constants.CMD_OPT_NAME_IGNOREEXECUTIONSAFTERDATE)) {
				val = LoggingTimestampConverter.convertLoggingTimestampToUTCString(this.ignoreExecutionsAfterTimestamp)
						+ " (" + LoggingTimestampConverter
								.convertLoggingTimestampLocalTimeZoneString(this.ignoreExecutionsAfterTimestamp)
						+ ")";
			} else if (Constants.CMD_OPT_NAME_TRACE_COLORING.equals(longOpt)) {
				val = this.cmdl.getOptionValue(Constants.CMD_OPT_NAME_TRACE_COLORING);
				if (val == null) {
					val = "";
				}
			} else if (Constants.CMD_OPT_NAME_ADD_DESCRIPTIONS.equals(longOpt)) {
				val = this.cmdl.getOptionValue(Constants.CMD_OPT_NAME_ADD_DESCRIPTIONS);
				if (val == null) {
					val = "";
				}
			} else {
				val = Arrays.toString(this.cmdl.getOptionValues(longOpt));
				LOG.warn("Unformatted configuration output for option " + longOpt);
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug("--" + longOpt + ": " + val);
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
	 * @param controller
	 *            The analysis controller to use for the connection of the plugins
	 * @throws IllegalStateException
	 *             If the connection of the plugins is not possible at the moment
	 * @throws AnalysisConfigurationException
	 *             If the plugins cannot be connected
	 *
	 * @param <P>
	 *            The type of the plugin.
	 */
	private <P extends AbstractPlugin & IGraphOutputtingFilter<?>> void attachGraphWriter(final P plugin,
			final AbstractGraphProducingFilter<?> producer, final AnalysisController controller)
			throws IllegalStateException, AnalysisConfigurationException {

		final Configuration configuration = new Configuration();
		configuration.setProperty(GraphWriterPlugin.CONFIG_PROPERTY_NAME_OUTPUT_PATH_NAME,
				this.outputDir + File.separator + this.outputFnPrefix);
		configuration.setProperty(GraphWriterPlugin.CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, String.valueOf(true));
		configuration.setProperty(GraphWriterPlugin.CONFIG_PROPERTY_NAME_SHORTLABELS, String.valueOf(this.shortLabels));
		configuration.setProperty(GraphWriterPlugin.CONFIG_PROPERTY_NAME_SELFLOOPS,
				String.valueOf(this.includeSelfLoops));
		configuration.setProperty(AbstractAnalysisComponent.CONFIG_NAME, producer.getConfigurationName());
		final GraphWriterPlugin graphWriter = new GraphWriterPlugin(configuration, controller);
		controller.connect(plugin, plugin.getGraphOutputPortName(), graphWriter,
				GraphWriterPlugin.INPUT_PORT_NAME_GRAPHS);
	}

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
		TraceAnalysisTool.connectGraphFilters(predecessor, coloringFilter, controller);
		controller.connect(coloringFilter, TraceColoringFilter.COLOR_REPOSITORY_PORT_NAME, colorRepository);

		return coloringFilter;
	}

	private static <P extends AbstractPlugin & IGraphOutputtingFilter<?>> DescriptionDecoratorFilter<?, ?, ?> createDescriptionDecoratorFilter(
			final P predecessor, final String descriptionsFileName, final AnalysisController controller)
			throws IOException, IllegalStateException, AnalysisConfigurationException {
		final DescriptionRepository descriptionRepository = DescriptionRepository.createFromFile(descriptionsFileName,
				controller);

		@SuppressWarnings("rawtypes")
		final DescriptionDecoratorFilter<?, ?, ?> descriptionFilter = new DescriptionDecoratorFilter(
				new Configuration(), controller);
		TraceAnalysisTool.connectGraphFilters(predecessor, descriptionFilter, controller);
		controller.connect(descriptionFilter, DescriptionDecoratorFilter.DESCRIPTION_REPOSITORY_PORT_NAME,
				descriptionRepository);

		return descriptionFilter;
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
	private void attachGraphProcessors(final List<AbstractGraphProducingFilter<?>> graphProducers,
			final AnalysisController controller, final CommandLine commandLine)
			throws IllegalStateException, AnalysisConfigurationException, IOException {

		for (final AbstractGraphProducingFilter<?> producer : graphProducers) {
			AbstractGraphFilter<?, ?, ?, ?> lastFilter = null;

			// Add a trace coloring filter, if necessary
			if (commandLine.hasOption(Constants.CMD_OPT_NAME_TRACE_COLORING)) {
				final String coloringFileName = commandLine.getOptionValue(Constants.CMD_OPT_NAME_TRACE_COLORING);
				lastFilter = TraceAnalysisTool.createTraceColoringFilter(producer, coloringFileName, controller);
			}

			// Add a description filter, if necessary
			if (commandLine.hasOption(Constants.CMD_OPT_NAME_ADD_DESCRIPTIONS)) {
				final String descriptionsFileName = commandLine.getOptionValue(Constants.CMD_OPT_NAME_ADD_DESCRIPTIONS);
				if (lastFilter != null) {
					lastFilter = TraceAnalysisTool.createDescriptionDecoratorFilter(lastFilter, descriptionsFileName,
							controller);
				} else {
					lastFilter = TraceAnalysisTool.createDescriptionDecoratorFilter(producer, descriptionsFileName,
							controller);
				}
			}

			if (lastFilter != null) {
				this.attachGraphWriter(lastFilter, producer, controller);
			} else {
				this.attachGraphWriter(producer, producer, controller);
			}
		}
	}

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
				ps.println(
						"Class " + numClasses++ + " ; cardinality: " + e.getValue() + "; # executions: " + t.getLength()
								+ "; representative: " + t.getTraceId() + "; max. stack depth: " + t.getMaxEss());
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug("");
				LOG.debug("#");
				LOG.debug("# Plugin: " + "Trace equivalence report");
				LOG.debug("Wrote " + numClasses + " equivalence class" + (numClasses > 1 ? "es" : "") + " to file '" // NOCS
						+ outputFn + "'");
			}
		} catch (final FileNotFoundException e) {
			LOG.error("File not found", e);
			retVal = false;
		} finally {
			if (ps != null) {
				ps.close();
			}
		}

		return retVal;
	}

	private static class OptionComparator implements Comparator<Option>, Serializable {

		private static final long serialVersionUID = 1L;

		public OptionComparator() {
			// No code necessary
		}

		@Override
		public int compare(final Option o1, final Option o2) {
			if (o1 == o2) { // NOPMD
				return 0;
			}
			final int posO1 = Constants.SORTED_OPTION_LIST.indexOf(o1);
			final int posO2 = Constants.SORTED_OPTION_LIST.indexOf(o2);
			if (posO1 < posO2) {
				return -1;
			}
			if (posO1 > posO2) {
				return 1;
			}
			return 0;
		}
	}

}
