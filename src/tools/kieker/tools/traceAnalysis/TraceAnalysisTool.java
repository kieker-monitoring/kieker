/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

import kieker.analysis.AnalysisController;
import kieker.analysis.analysisComponent.AbstractAnalysisComponent;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.filter.flow.EventRecordTraceReconstructionFilter;
import kieker.analysis.plugin.filter.forward.StringBufferFilter;
import kieker.analysis.plugin.filter.select.TimestampFilter;
import kieker.analysis.plugin.filter.select.TraceIdFilter;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.util.filesystem.FSConstants;
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
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.AbstractNodeDecorator;
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.ComponentDependencyGraphAllocationFilter;
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.ComponentDependencyGraphAssemblyFilter;
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.ContainerDependencyGraphFilter;
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.OperationDependencyGraphAllocationFilter;
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.OperationDependencyGraphAssemblyFilter;
import kieker.tools.traceAnalysis.filter.visualization.descriptions.DescriptionDecoratorFilter;
import kieker.tools.traceAnalysis.filter.visualization.sequenceDiagram.SequenceDiagramFilter;
import kieker.tools.traceAnalysis.filter.visualization.traceColoring.TraceColoringFilter;
import kieker.tools.traceAnalysis.repository.DescriptionRepository;
import kieker.tools.traceAnalysis.repository.TraceColorRepository;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.tools.util.LoggingTimestampConverter;

/**
 * 
 * This is the main class to start Tpan - the model synthesis and analysis
 * server to process the monitoring data that comes from the instrumented
 * system, or from a file that contains Kieker monitoring data. Tpan can produce
 * output such as sequence diagrams, dependency graphs on demand. Alternatively
 * it can be used continuously for online performance analysis, anomaly
 * detection or live visualization of system behavior.
 * 
 * A Tpan is started via ant-script or command line. Visualization and output
 * should be implemented as plugins. These plugins must be implemented to be
 * loaded at runtime (Class.forName...) in order to keep compile-time
 * dependencies low.
 * 
 * 
 * @author Andre van Hoorn, Matthias Rohr, Nils Christian Ehmke
 */
// TODO: Fix JavaDoc comment
public final class TraceAnalysisTool {
	public static final String DATE_FORMAT_PATTERN_CMD_USAGE_HELP = Constants.DATE_FORMAT_PATTERN.replaceAll("'", ""); // only for usage info
	private static final Log LOG = LogFactory.getLog(TraceAnalysisTool.class);
	private static final AnalysisController ANALYSIS_INSTANCE = new AnalysisController();
	private static final SystemModelRepository SYSTEM_ENTITY_FACTORY = new SystemModelRepository(new Configuration(), ANALYSIS_INSTANCE);
	private static final CommandLineParser CMDL_PARSER = new BasicParser();
	private static CommandLine cmdl;
	private static String[] inputDirs;
	private static String outputDir;
	private static String outputFnPrefix;
	private static Set<Long> selectedTraces; // null means select all
	private static boolean shortLabels = true;
	private static boolean includeSelfLoops; // false
	private static boolean ignoreInvalidTraces; // false
	private static int maxTraceDurationMillis = 10 * 60 * 1000; // 10 minutes default
	private static long ignoreExecutionsBeforeTimestamp = Long.parseLong(TimestampFilter.CONFIG_PROPERTY_VALUE_MIN_TIMESTAMP);
	private static long ignoreExecutionsAfterTimestamp = Long.parseLong(TimestampFilter.CONFIG_PROPERTY_VALUE_MAX_TIMESTAMP);

	private static final String ENCODING = "UTF-8";

	/**
	 * Private constructor to avoid instantiation. An object of this class should not be created.
	 */
	private TraceAnalysisTool() {}

	/**
	 * This method parses the given command line arguments and stores them in the class field.
	 * 
	 * @param args
	 *            The command line arguments-
	 * @return true if and only if the arguments have been parsed succesfully.
	 */
	private static boolean parseArgs(final String[] args) {
		try {
			TraceAnalysisTool.cmdl = CMDL_PARSER.parse(Constants.CMDL_OPTIONS, args);
		} catch (final ParseException e) {
			TraceAnalysisTool.printUsage();
			System.err.println("\nError parsing arguments: " + e.getMessage()); // NOPMD (System.out)
			return false;
		}
		return true;
	}

	/**
	 * This method prints some information to show the user how to use this tool.
	 */
	private static void printUsage() {
		Constants.CMD_HELP_FORMATTER.printHelp(80, TraceAnalysisTool.class.getName(), "", Constants.CMDL_OPTIONS, "", true);
	}

	/**
	 * This method uses the (already parsed and stored) command line arguments to initialize the tool.
	 * 
	 * @return true if and only if the tool has been initialized correctly.
	 */
	private static boolean initFromArgs() {
		TraceAnalysisTool.inputDirs = TraceAnalysisTool.cmdl.getOptionValues(Constants.CMD_OPT_NAME_INPUTDIRS);

		TraceAnalysisTool.outputDir = TraceAnalysisTool.cmdl.getOptionValue(Constants.CMD_OPT_NAME_OUTPUTDIR) + File.separator;
		TraceAnalysisTool.outputFnPrefix = TraceAnalysisTool.cmdl.getOptionValue(Constants.CMD_OPT_NAME_OUTPUTFNPREFIX, "");
		if (TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_SELECTTRACES)) { // Parse list of trace Ids
			final String[] traceIdList = TraceAnalysisTool.cmdl.getOptionValues(Constants.CMD_OPT_NAME_SELECTTRACES);
			TraceAnalysisTool.selectedTraces = new TreeSet<Long>();
			final int numSelectedTraces = traceIdList.length;
			try {
				for (final String idStr : traceIdList) {
					TraceAnalysisTool.selectedTraces.add(Long.valueOf(idStr));
				}
				LOG.info(numSelectedTraces + " trace" + (numSelectedTraces > 1 ? "s" : "") + " selected"); // NOCS
			} catch (final Exception e) { // NOPMD NOCS (IllegalCatchCheck)
				System.err.println("\nFailed to parse list of trace IDs: " + Arrays.toString(traceIdList) + "(" + e.getMessage() + ")"); // NOPMD (System.out)
				LOG.error("Failed to parse list of trace IDs: " + Arrays.toString(traceIdList), e);
				return false;
			}
		}

		TraceAnalysisTool.shortLabels = TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_SHORTLABELS);
		TraceAnalysisTool.includeSelfLoops = TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_INCLUDESELFLOOPS);
		TraceAnalysisTool.ignoreInvalidTraces = TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_IGNOREINVALIDTRACES);

		final String maxTraceDurationStr = TraceAnalysisTool.cmdl.getOptionValue(Constants.CMD_OPT_NAME_MAXTRACEDURATION,
				Integer.toString(TraceAnalysisTool.maxTraceDurationMillis));
		try {
			TraceAnalysisTool.maxTraceDurationMillis = Integer.parseInt(maxTraceDurationStr);
		} catch (final NumberFormatException exc) {
			System.err.println("\nFailed to parse int value of property " + Constants.CMD_OPT_NAME_MAXTRACEDURATION + " (must be an integer): " // NOPMD (System.out)
					+ maxTraceDurationStr);
			LOG.error("Failed to parse int value of property " + Constants.CMD_OPT_NAME_MAXTRACEDURATION + " (must be an integer):"
					+ maxTraceDurationStr, exc);
			return false;
		}

		final DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_PATTERN, Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		try {
			final String ignoreRecordsBeforeTimestampString = TraceAnalysisTool.cmdl.getOptionValue(Constants.CMD_OPT_NAME_IGNOREEXECUTIONSBEFOREDATE, null);
			final String ignoreRecordsAfterTimestampString = TraceAnalysisTool.cmdl.getOptionValue(Constants.CMD_OPT_NAME_IGNOREEXECUTIONSAFTERDATE, null);
			if (ignoreRecordsBeforeTimestampString != null) {
				final Date ignoreBeforeDate = dateFormat.parse(ignoreRecordsBeforeTimestampString);
				TraceAnalysisTool.ignoreExecutionsBeforeTimestamp = ignoreBeforeDate.getTime() * (1000 * 1000);
				LOG.info("Ignoring records before " + dateFormat.format(ignoreBeforeDate) + " ("
						+ TraceAnalysisTool.ignoreExecutionsBeforeTimestamp + ")");
			}
			if (ignoreRecordsAfterTimestampString != null) {
				final Date ignoreAfterDate = dateFormat.parse(ignoreRecordsAfterTimestampString);
				TraceAnalysisTool.ignoreExecutionsAfterTimestamp = ignoreAfterDate.getTime() * (1000 * 1000);
				LOG.info("Ignoring records after " + dateFormat.format(ignoreAfterDate) + " ("
						+ TraceAnalysisTool.ignoreExecutionsAfterTimestamp + ")");
			}
		} catch (final java.text.ParseException ex) {
			final String errorMsg = "Error parsing date/time string. Please use the following pattern: " + DATE_FORMAT_PATTERN_CMD_USAGE_HELP;
			System.err.println(errorMsg); // NOPMD (System.out)
			LOG.error(errorMsg, ex);
			return false;
		}
		return true;
	}

	/**
	 * This method dumps the configuration on the screen.
	 */
	private static void dumpConfiguration() {
		System.out.println("#"); // NOPMD (System.out)
		System.out.println("# Configuration"); // NOPMD (System.out)
		for (final Option o : Constants.SORTED_OPTION_LIST) {
			final String longOpt = o.getLongOpt();
			String val = "<null>";
			if (longOpt.equals(Constants.CMD_OPT_NAME_INPUTDIRS)) {
				val = Constants.stringArrToStringList(TraceAnalysisTool.inputDirs);
			} else if (longOpt.equals(Constants.CMD_OPT_NAME_OUTPUTDIR)) {
				val = TraceAnalysisTool.outputDir;
			} else if (longOpt.equals(Constants.CMD_OPT_NAME_OUTPUTFNPREFIX)) {
				val = TraceAnalysisTool.outputFnPrefix;
			} else if (longOpt.equals(Constants.CMD_OPT_NAME_TASK_ALLOCATIONEQUIVCLASSREPORT)
					|| longOpt.equals(Constants.CMD_OPT_NAME_TASK_ASSEMBLYEQUIVCLASSREPORT) || longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONSEQDS)
					|| longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYSEQDS) || longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONCOMPONENTDEPG)
					|| longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYCOMPONENTDEPG) || longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG)
					|| longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONOPERATIONDEPG)
					|| longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYOPERATIONDEPG)
					|| longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTAGGREGATEDALLOCATIONCALLTREE)
					|| longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTAGGREGATEDASSEMBLYCALLTREE) || longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTCALLTREES)
					|| longOpt.equals(Constants.CMD_OPT_NAME_TASK_PRINTEXECTRACES) || longOpt.equals(Constants.CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES)
					|| longOpt.equals(Constants.CMD_OPT_NAME_TASK_PRINTMSGTRACES)) {
				val = TraceAnalysisTool.cmdl.hasOption(longOpt) ? "true" : "false"; // NOCS
			} else if (longOpt.equals(Constants.CMD_OPT_NAME_SELECTTRACES)) {
				if (TraceAnalysisTool.selectedTraces != null) {
					val = TraceAnalysisTool.selectedTraces.toString();
				} else {
					val = "<select all>";
				}

			} else if (longOpt.equals(Constants.CMD_OPT_NAME_SHORTLABELS)) {
				val = TraceAnalysisTool.shortLabels ? "true" : "false"; // NOCS
			} else if (longOpt.equals(Constants.CMD_OPT_NAME_INCLUDESELFLOOPS)) {
				val = TraceAnalysisTool.includeSelfLoops ? "true" : "false"; // NOCS
			} else if (longOpt.equals(Constants.CMD_OPT_NAME_IGNOREINVALIDTRACES)) {
				val = TraceAnalysisTool.ignoreInvalidTraces ? "true" : "false"; // NOCS
			} else if (longOpt.equals(Constants.CMD_OPT_NAME_MAXTRACEDURATION)) {
				val = TraceAnalysisTool.maxTraceDurationMillis + " ms";
			} else if (longOpt.equals(Constants.CMD_OPT_NAME_IGNOREEXECUTIONSBEFOREDATE)) {
				val = LoggingTimestampConverter.convertLoggingTimestampToUTCString(TraceAnalysisTool.ignoreExecutionsBeforeTimestamp) + " ("
						+ LoggingTimestampConverter.convertLoggingTimestampLocalTimeZoneString(TraceAnalysisTool.ignoreExecutionsBeforeTimestamp) + ")";
			} else if (longOpt.equals(Constants.CMD_OPT_NAME_IGNOREEXECUTIONSAFTERDATE)) {
				val = LoggingTimestampConverter.convertLoggingTimestampToUTCString(TraceAnalysisTool.ignoreExecutionsAfterTimestamp) + " ("
						+ LoggingTimestampConverter.convertLoggingTimestampLocalTimeZoneString(TraceAnalysisTool.ignoreExecutionsAfterTimestamp) + ")";
			} else if (Constants.CMD_OPT_NAME_TRACE_COLORING.equals(longOpt)) {
				val = cmdl.getOptionValue(Constants.CMD_OPT_NAME_TRACE_COLORING);
				if (val == null) {
					val = "";
				}
			} else if (Constants.CMD_OPT_NAME_ADD_DESCRIPTIONS.equals(longOpt)) {
				val = cmdl.getOptionValue(Constants.CMD_OPT_NAME_ADD_DESCRIPTIONS);
				if (val == null) {
					val = "";
				}
			} else {
				val = Arrays.toString(TraceAnalysisTool.cmdl.getOptionValues(longOpt));
				LOG.warn("Unformatted configuration output for option " + longOpt);
			}
			System.out.println("--" + longOpt + ": " + val); // NOPMD (System.out)
		}
	}

	private static void addDecorators(final String[] decoratorNames, final AbstractDependencyGraphFilter<?> plugin) {
		if (decoratorNames == null) {
			return;
		}

		for (final String currentDecoratorName : decoratorNames) {
			final AbstractNodeDecorator currentDecorator = AbstractNodeDecorator.createFromName(currentDecoratorName);

			if (currentDecorator == null) {
				LOG.warn("Unknown decoration name '" + currentDecoratorName + "'.");
				continue;
			}

			plugin.addDecorator(currentDecorator);
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
	private static <P extends AbstractPlugin & IGraphOutputtingFilter<?>> void attachGraphWriter(final P plugin,
			final AbstractGraphProducingFilter<?> producer, final AnalysisController controller) throws IllegalStateException, AnalysisConfigurationException {

		final Configuration configuration = new Configuration();
		configuration.setProperty(GraphWriterPlugin.CONFIG_PROPERTY_NAME_OUTPUT_PATH_NAME, outputDir + File.separator + outputFnPrefix);
		configuration.setProperty(GraphWriterPlugin.CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, String.valueOf(true));
		configuration.setProperty(GraphWriterPlugin.CONFIG_PROPERTY_NAME_SHORTLABELS, String.valueOf(shortLabels));
		configuration.setProperty(GraphWriterPlugin.CONFIG_PROPERTY_NAME_SELFLOOPS, String.valueOf(includeSelfLoops));
		configuration.setProperty(AbstractAnalysisComponent.CONFIG_NAME, producer.getConfigurationName());
		final GraphWriterPlugin graphWriter = new GraphWriterPlugin(configuration, controller);
		controller.connect(plugin, plugin.getGraphOutputPortName(), graphWriter, GraphWriterPlugin.INPUT_PORT_NAME_GRAPHS);
	}

	private static <P extends AbstractPlugin & IGraphOutputtingFilter<?>> void connectGraphFilters(final P predecessor,
			final AbstractGraphFilter<?, ?, ?, ?> filter, final AnalysisController controller) throws IllegalStateException, AnalysisConfigurationException {
		controller.connect(predecessor, predecessor.getGraphOutputPortName(), filter, filter.getGraphInputPortName());
	}

	private static <P extends AbstractPlugin & IGraphOutputtingFilter<?>> TraceColoringFilter<?, ?> createTraceColoringFilter(final P predecessor,
			final String coloringFileName, final AnalysisController controller) throws IOException, IllegalStateException, AnalysisConfigurationException {
		final TraceColorRepository colorRepository = TraceColorRepository.createFromFile(coloringFileName, controller);

		@SuppressWarnings("rawtypes")
		final TraceColoringFilter<?, ?> coloringFilter = new TraceColoringFilter(new Configuration(), controller);
		TraceAnalysisTool.connectGraphFilters(predecessor, coloringFilter, controller);
		controller.connect(coloringFilter, TraceColoringFilter.COLOR_REPOSITORY_PORT_NAME, colorRepository);

		return coloringFilter;
	}

	private static <P extends AbstractPlugin & IGraphOutputtingFilter<?>> DescriptionDecoratorFilter<?, ?, ?> createDescriptionDecoratorFilter(
			final P predecessor, final String descriptionsFileName, final AnalysisController controller) throws IOException, IllegalStateException,
			AnalysisConfigurationException {
		final DescriptionRepository descriptionRepository = DescriptionRepository.createFromFile(descriptionsFileName, controller);

		@SuppressWarnings("rawtypes")
		final DescriptionDecoratorFilter<?, ?, ?> descriptionFilter = new DescriptionDecoratorFilter(new Configuration(), controller);
		TraceAnalysisTool.connectGraphFilters(predecessor, descriptionFilter, controller);
		controller.connect(descriptionFilter, DescriptionDecoratorFilter.DESCRIPTION_REPOSITORY_PORT_NAME, descriptionRepository);

		return descriptionFilter;
	}

	/**
	 * Attaches graph processors and a writer to the given graph producers depending on the given
	 * command line.
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
	private static void attachGraphProcessors(final List<AbstractGraphProducingFilter<?>> graphProducers, final AnalysisController controller,
			final CommandLine commandLine) throws IllegalStateException, AnalysisConfigurationException, IOException {

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
					lastFilter = TraceAnalysisTool.createDescriptionDecoratorFilter(lastFilter, descriptionsFileName, controller);
				} else {
					lastFilter = TraceAnalysisTool.createDescriptionDecoratorFilter(producer, descriptionsFileName, controller);
				}
			}

			if (lastFilter != null) {
				TraceAnalysisTool.attachGraphWriter(lastFilter, producer, controller);
			} else {
				TraceAnalysisTool.attachGraphWriter(producer, producer, controller);
			}
		}
	}

	private static boolean dispatchTasks() {
		boolean retVal = true;
		int numRequestedTasks = 0;

		TraceReconstructionFilter mtReconstrFilter = null;
		EventRecordTraceCounter eventRecordTraceCounter = null;
		EventRecordTraceReconstructionFilter eventTraceReconstructionFilter = null;
		TraceEventRecords2ExecutionAndMessageTraceFilter traceEvents2ExecutionAndMessageTraceFilter = null;
		try {
			FSReader reader;
			{ // NOCS (NestedBlock)
				final Configuration conf = new Configuration(null);
				conf.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, Configuration.toProperty(TraceAnalysisTool.inputDirs));
				conf.setProperty(FSReader.CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES, Boolean.TRUE.toString());
				reader = new FSReader(conf, ANALYSIS_INSTANCE);
			}

			/*
			 * Unify Strings
			 */
			final StringBufferFilter stringBufferFilter = new StringBufferFilter(new Configuration(), ANALYSIS_INSTANCE);
			ANALYSIS_INSTANCE.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS, stringBufferFilter, StringBufferFilter.INPUT_PORT_NAME_EVENTS);

			/*
			 * This map can be used within the constructor for all following plugins which use the repository with the name defined in the
			 * AbstractTraceAnalysisPlugin.
			 */

			final TimestampFilter timestampFilter;
			{ // NOCS (nested block)
				/*
				 * Create the timestamp filter and connect to the reader's output port
				 */
				final Configuration configTimestampFilter = new Configuration();
				configTimestampFilter.setProperty(TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_BEFORE_TIMESTAMP,
						Long.toString(TraceAnalysisTool.ignoreExecutionsBeforeTimestamp));
				configTimestampFilter.setProperty(TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_AFTER_TIMESTAMP,
						Long.toString(TraceAnalysisTool.ignoreExecutionsAfterTimestamp));

				timestampFilter = new TimestampFilter(configTimestampFilter, ANALYSIS_INSTANCE);
				ANALYSIS_INSTANCE.connect(stringBufferFilter, StringBufferFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS,
						timestampFilter, TimestampFilter.INPUT_PORT_NAME_EXECUTION);
				ANALYSIS_INSTANCE.connect(stringBufferFilter, StringBufferFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS,
						timestampFilter, TimestampFilter.INPUT_PORT_NAME_FLOW);
			}

			final TraceIdFilter traceIdFilter;
			{ // NOCS (nested block)
				/*
				 * Create the trace ID filter and connect to the timestamp filter's output port
				 */
				final Configuration configTraceIdFilterFlow = new Configuration();
				if (TraceAnalysisTool.selectedTraces == null) {
					configTraceIdFilterFlow.setProperty(TraceIdFilter.CONFIG_PROPERTY_NAME_SELECT_ALL_TRACES, Boolean.TRUE.toString());
				} else {
					configTraceIdFilterFlow.setProperty(TraceIdFilter.CONFIG_PROPERTY_NAME_SELECT_ALL_TRACES, Boolean.FALSE.toString());
					configTraceIdFilterFlow.setProperty(TraceIdFilter.CONFIG_PROPERTY_NAME_SELECTED_TRACES,
							Configuration.toProperty(TraceAnalysisTool.selectedTraces.toArray(new Long[TraceAnalysisTool.selectedTraces.size()])));
				}

				traceIdFilter = new TraceIdFilter(configTraceIdFilterFlow, ANALYSIS_INSTANCE);

				ANALYSIS_INSTANCE.connect(timestampFilter, TimestampFilter.OUTPUT_PORT_NAME_WITHIN_PERIOD,
						traceIdFilter, TraceIdFilter.INPUT_PORT_NAME_COMBINED);
			}

			final ExecutionRecordTransformationFilter execRecTransformer;
			{ // NOCS (nested block)
				/*
				 * Create the execution record transformation filter and connect to the trace ID filter's output port
				 */
				final Configuration execRecTransformerConfig = new Configuration();
				execRecTransformerConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME, Constants.EXEC_TRACE_RECONSTR_COMPONENT_NAME);
				execRecTransformer = new ExecutionRecordTransformationFilter(execRecTransformerConfig, ANALYSIS_INSTANCE);
				ANALYSIS_INSTANCE.connect(traceIdFilter, TraceIdFilter.OUTPUT_PORT_NAME_MATCH, execRecTransformer,
						ExecutionRecordTransformationFilter.INPUT_PORT_NAME_RECORDS);
				ANALYSIS_INSTANCE.connect(execRecTransformer, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, SYSTEM_ENTITY_FACTORY);
			}

			{ // NOCS (nested block)
				/*
				 * Create the trace reconstruction filter and connect to the record transformation filter's output port
				 */
				final Configuration mtReconstrFilterConfig = new Configuration();
				mtReconstrFilterConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME, Constants.TRACERECONSTR_COMPONENT_NAME);
				mtReconstrFilterConfig.setProperty(TraceReconstructionFilter.CONFIG_PROPERTY_NAME_TIMEUNIT,
						TimeUnit.MILLISECONDS.name());
				mtReconstrFilterConfig.setProperty(TraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION,
						Integer.toString(TraceAnalysisTool.maxTraceDurationMillis));
				mtReconstrFilterConfig.setProperty(TraceReconstructionFilter.CONFIG_PROPERTY_NAME_IGNORE_INVALID_TRACES,
						Boolean.toString(TraceAnalysisTool.ignoreInvalidTraces));
				mtReconstrFilter = new TraceReconstructionFilter(mtReconstrFilterConfig, ANALYSIS_INSTANCE);
				ANALYSIS_INSTANCE.connect(mtReconstrFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, SYSTEM_ENTITY_FACTORY);
				ANALYSIS_INSTANCE.connect(execRecTransformer, ExecutionRecordTransformationFilter.OUTPUT_PORT_NAME_EXECUTIONS,
						mtReconstrFilter, TraceReconstructionFilter.INPUT_PORT_NAME_EXECUTIONS);
			}

			{ // NOCS (nested block)
				/*
				 * Create the event record trace generation filter and connect to the trace ID filter's output port
				 */
				final Configuration configurationEventRecordTraceGenerationFilter = new Configuration();
				configurationEventRecordTraceGenerationFilter.setProperty(AbstractAnalysisComponent.CONFIG_NAME, Constants.EVENTRECORDTRACERECONSTR_COMPONENT_NAME);
				configurationEventRecordTraceGenerationFilter.setProperty(EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_TIMEUNIT,
						TimeUnit.MILLISECONDS.name());
				configurationEventRecordTraceGenerationFilter.setProperty(EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION,
						Long.toString(TraceAnalysisTool.maxTraceDurationMillis));
				eventTraceReconstructionFilter = new EventRecordTraceReconstructionFilter(configurationEventRecordTraceGenerationFilter, ANALYSIS_INSTANCE);

				ANALYSIS_INSTANCE.connect(traceIdFilter, TraceIdFilter.OUTPUT_PORT_NAME_MATCH,
						eventTraceReconstructionFilter, EventRecordTraceReconstructionFilter.INPUT_PORT_NAME_TRACE_RECORDS);
			}

			{ // NOCS (nested block)
				/*
				 * Create the counter for valid/invalid event record traces
				 */
				final Configuration configurationEventRecordTraceCounter = new Configuration();
				configurationEventRecordTraceCounter.setProperty(AbstractAnalysisComponent.CONFIG_NAME, Constants.EXECEVENTRACESFROMEVENTTRACES_COMPONENT_NAME);
				eventRecordTraceCounter = new EventRecordTraceCounter(configurationEventRecordTraceCounter, ANALYSIS_INSTANCE);

				ANALYSIS_INSTANCE.connect(
						eventTraceReconstructionFilter, EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_VALID,
						eventRecordTraceCounter, EventRecordTraceCounter.INPUT_PORT_NAME_VALID);
				ANALYSIS_INSTANCE.connect(
						eventTraceReconstructionFilter, EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_INVALID,
						eventRecordTraceCounter, EventRecordTraceCounter.INPUT_PORT_NAME_INVALID);
			}

			{ // NOCS (nested block)
				/*
				 * Create the event trace to execution/message trace transformation filter and connect its input to the
				 * event record trace generation filter's output port
				 */
				final Configuration configurationEventTrace2ExecutionTraceFilter = new Configuration();
				configurationEventTrace2ExecutionTraceFilter.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
						Constants.EXECTRACESFROMEVENTTRACES_COMPONENT_NAME);
				// EventTrace2ExecutionTraceFilter has no configuration properties
				traceEvents2ExecutionAndMessageTraceFilter = new TraceEventRecords2ExecutionAndMessageTraceFilter(configurationEventTrace2ExecutionTraceFilter,
						ANALYSIS_INSTANCE);

				ANALYSIS_INSTANCE.connect(eventTraceReconstructionFilter, EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_VALID,
						traceEvents2ExecutionAndMessageTraceFilter, TraceEventRecords2ExecutionAndMessageTraceFilter.INPUT_PORT_NAME_EVENT_TRACE);
				ANALYSIS_INSTANCE.connect(traceEvents2ExecutionAndMessageTraceFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
			}

			final List<AbstractTraceProcessingFilter> allTraceProcessingComponents = new ArrayList<AbstractTraceProcessingFilter>();
			final List<AbstractGraphProducingFilter<?>> allGraphProducers = new ArrayList<AbstractGraphProducingFilter<?>>();

			final Configuration traceAllocationEquivClassFilterConfig = new Configuration();
			traceAllocationEquivClassFilterConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME, Constants.TRACEALLOCATIONEQUIVCLASS_COMPONENT_NAME);
			traceAllocationEquivClassFilterConfig.setProperty(TraceEquivalenceClassFilter.CONFIG_PROPERTY_NAME_EQUIVALENCE_MODE,
					TraceEquivalenceClassModes.ALLOCATION.toString());
			TraceEquivalenceClassFilter traceAllocationEquivClassFilter = null; // must not be instantiate it here, due to side-effects in the constructor
			if (TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_ALLOCATIONEQUIVCLASSREPORT)) {
				/**
				 * Currently, this filter is only used to print an equivalence
				 * report. That's why we only activate it in case this options
				 * is requested.
				 */
				traceAllocationEquivClassFilter = new TraceEquivalenceClassFilter(traceAllocationEquivClassFilterConfig, ANALYSIS_INSTANCE);
				ANALYSIS_INSTANCE.connect(traceAllocationEquivClassFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				ANALYSIS_INSTANCE.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
						traceAllocationEquivClassFilter, TraceEquivalenceClassFilter.INPUT_PORT_NAME_EXECUTION_TRACE);
				ANALYSIS_INSTANCE.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
						traceAllocationEquivClassFilter, TraceEquivalenceClassFilter.INPUT_PORT_NAME_EXECUTION_TRACE);
				allTraceProcessingComponents.add(traceAllocationEquivClassFilter);
			}

			final Configuration traceAssemblyEquivClassFilterConfig = new Configuration();
			traceAssemblyEquivClassFilterConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME, Constants.TRACEASSEMBLYEQUIVCLASS_COMPONENT_NAME);
			traceAssemblyEquivClassFilterConfig.setProperty(TraceEquivalenceClassFilter.CONFIG_PROPERTY_NAME_EQUIVALENCE_MODE,
					TraceEquivalenceClassModes.ASSEMBLY.toString());
			TraceEquivalenceClassFilter traceAssemblyEquivClassFilter = null; // must not be instantiate it here, due to side-effects in the constructor
			if (TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_ASSEMBLYEQUIVCLASSREPORT)) {
				/**
				 * Currently, this filter is only used to print an equivalence
				 * report. That's why we only activate it in case this options
				 * is requested.
				 */
				traceAssemblyEquivClassFilter = new TraceEquivalenceClassFilter(traceAssemblyEquivClassFilterConfig, ANALYSIS_INSTANCE);
				ANALYSIS_INSTANCE.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
						traceAssemblyEquivClassFilter, TraceEquivalenceClassFilter.INPUT_PORT_NAME_EXECUTION_TRACE);
				ANALYSIS_INSTANCE.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
						traceAssemblyEquivClassFilter, TraceEquivalenceClassFilter.INPUT_PORT_NAME_EXECUTION_TRACE);
				ANALYSIS_INSTANCE.connect(traceAssemblyEquivClassFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(traceAssemblyEquivClassFilter);
			}

			// fill list of msgTraceProcessingComponents:
			MessageTraceWriterFilter componentPrintMsgTrace = null;
			if (TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PRINTMSGTRACES)) {
				numRequestedTasks++;
				final Configuration componentPrintMsgTraceConfig = new Configuration();
				componentPrintMsgTraceConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME, Constants.PRINTMSGTRACE_COMPONENT_NAME);
				componentPrintMsgTraceConfig.setProperty(MessageTraceWriterFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN, new File(TraceAnalysisTool.outputDir
						+ File.separator
						+ TraceAnalysisTool.outputFnPrefix + Constants.MESSAGE_TRACES_FN_PREFIX + ".txt")
						.getCanonicalPath());
				componentPrintMsgTrace = new MessageTraceWriterFilter(componentPrintMsgTraceConfig, ANALYSIS_INSTANCE);

				ANALYSIS_INSTANCE.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPrintMsgTrace, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				ANALYSIS_INSTANCE.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPrintMsgTrace, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				ANALYSIS_INSTANCE.connect(componentPrintMsgTrace, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPrintMsgTrace);
			}
			ExecutionTraceWriterFilter componentPrintExecTrace = null;
			if (TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PRINTEXECTRACES)) {
				numRequestedTasks++;
				final Configuration componentPrintExecTraceConfig = new Configuration();
				componentPrintExecTraceConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME, Constants.PRINTEXECTRACE_COMPONENT_NAME);
				componentPrintExecTraceConfig.setProperty(ExecutionTraceWriterFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN, new File(TraceAnalysisTool.outputDir
						+ File.separator
						+ TraceAnalysisTool.outputFnPrefix + Constants.EXECUTION_TRACES_FN_PREFIX + ".txt")
						.getCanonicalPath());
				componentPrintExecTrace = new ExecutionTraceWriterFilter(componentPrintExecTraceConfig, ANALYSIS_INSTANCE);

				ANALYSIS_INSTANCE.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
						componentPrintExecTrace, ExecutionTraceWriterFilter.INPUT_PORT_NAME_EXECUTION_TRACES);
				ANALYSIS_INSTANCE.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
						componentPrintExecTrace, ExecutionTraceWriterFilter.INPUT_PORT_NAME_EXECUTION_TRACES);
				ANALYSIS_INSTANCE.connect(componentPrintExecTrace, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPrintExecTrace);
			}
			InvalidExecutionTraceWriterFilter componentPrintInvalidTrace = null;
			if (TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES)) {
				numRequestedTasks++;
				final Configuration componentPrintInvalidTraceConfig = new Configuration();
				componentPrintInvalidTraceConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
						Constants.PRINTINVALIDEXECTRACE_COMPONENT_NAME);
				componentPrintInvalidTraceConfig.setProperty(InvalidExecutionTraceWriterFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN, new File(TraceAnalysisTool.outputDir
						+ File.separator
						+ TraceAnalysisTool.outputFnPrefix
						+ Constants.INVALID_TRACES_FN_PREFIX + ".txt").getCanonicalPath());
				componentPrintInvalidTrace = new InvalidExecutionTraceWriterFilter(componentPrintInvalidTraceConfig, ANALYSIS_INSTANCE);

				ANALYSIS_INSTANCE.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_INVALID_EXECUTION_TRACE,
						componentPrintInvalidTrace, InvalidExecutionTraceWriterFilter.INPUT_PORT_NAME_INVALID_EXECUTION_TRACES);
				ANALYSIS_INSTANCE.connect(componentPrintInvalidTrace, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				// TODO: We haven't such port for the EventTrace2ExecutionTraceFilter, yet
				LOG.warn("EventTrace2ExecutionTraceFilter doesn't provide an output port for invalid execution traces, yet");
				// AbstractPlugin.connect(eventTrace2ExecutionTraceFilter, EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_INVALID_EXECUTION_TRACE,
				// componentPrintInvalidTrace, InvalidExecutionTraceWriterPlugin.INVALID_EXECUTION_TRACES_INPUT_PORT_NAME);
				allTraceProcessingComponents.add(componentPrintInvalidTrace);
			}
			SequenceDiagramFilter componentPlotAllocationSeqDiagr = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONSEQDS)) {
				numRequestedTasks++;
				final Configuration componentPlotAllocationSeqDiagrConfig = new Configuration();
				componentPlotAllocationSeqDiagrConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME, Constants.PLOTALLOCATIONSEQDIAGR_COMPONENT_NAME);
				componentPlotAllocationSeqDiagrConfig.setProperty(SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN_BASE,
						TraceAnalysisTool.outputDir + File.separator + TraceAnalysisTool.outputFnPrefix + Constants.ALLOCATION_SEQUENCE_DIAGRAM_FN_PREFIX);
				componentPlotAllocationSeqDiagrConfig.setProperty(SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_SDMODE,
						SequenceDiagramFilter.SDModes.ALLOCATION.toString());
				componentPlotAllocationSeqDiagrConfig.setProperty(SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_SHORTLABES,
						Boolean.toString(TraceAnalysisTool.shortLabels));
				componentPlotAllocationSeqDiagr = new SequenceDiagramFilter(componentPlotAllocationSeqDiagrConfig, ANALYSIS_INSTANCE);

				ANALYSIS_INSTANCE.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAllocationSeqDiagr, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				ANALYSIS_INSTANCE.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAllocationSeqDiagr, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				ANALYSIS_INSTANCE.connect(componentPlotAllocationSeqDiagr, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotAllocationSeqDiagr);
			}
			SequenceDiagramFilter componentPlotAssemblySeqDiagr = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYSEQDS)) {
				numRequestedTasks++;
				final Configuration componentPlotAssemblySeqDiagrConfig = new Configuration();
				componentPlotAssemblySeqDiagrConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME, Constants.PLOTASSEMBLYSEQDIAGR_COMPONENT_NAME);
				componentPlotAssemblySeqDiagrConfig.setProperty(SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN_BASE,
						TraceAnalysisTool.outputDir + File.separator + TraceAnalysisTool.outputFnPrefix + Constants.ASSEMBLY_SEQUENCE_DIAGRAM_FN_PREFIX);
				componentPlotAssemblySeqDiagrConfig.setProperty(SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_SDMODE,
						SequenceDiagramFilter.SDModes.ASSEMBLY.toString());
				componentPlotAssemblySeqDiagrConfig.setProperty(SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_SHORTLABES,
						Boolean.toString(TraceAnalysisTool.shortLabels));
				componentPlotAssemblySeqDiagr = new SequenceDiagramFilter(componentPlotAssemblySeqDiagrConfig, ANALYSIS_INSTANCE);

				ANALYSIS_INSTANCE.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblySeqDiagr, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				ANALYSIS_INSTANCE.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblySeqDiagr, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				ANALYSIS_INSTANCE.connect(componentPlotAssemblySeqDiagr, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotAssemblySeqDiagr);
			}

			ComponentDependencyGraphAllocationFilter componentPlotAllocationComponentDepGraph = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONCOMPONENTDEPG)) {
				numRequestedTasks++;
				componentPlotAllocationComponentDepGraph = new ComponentDependencyGraphAllocationFilter(new Configuration(), ANALYSIS_INSTANCE);

				final String[] nodeDecorations = TraceAnalysisTool.cmdl.getOptionValues(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONCOMPONENTDEPG);
				TraceAnalysisTool.addDecorators(nodeDecorations, componentPlotAllocationComponentDepGraph);

				ANALYSIS_INSTANCE.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAllocationComponentDepGraph, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				ANALYSIS_INSTANCE.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAllocationComponentDepGraph, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				ANALYSIS_INSTANCE.connect(componentPlotAllocationComponentDepGraph, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);

				allTraceProcessingComponents.add(componentPlotAllocationComponentDepGraph);
				allGraphProducers.add(componentPlotAllocationComponentDepGraph);
			}

			ComponentDependencyGraphAssemblyFilter componentPlotAssemblyComponentDepGraph = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYCOMPONENTDEPG)) {
				numRequestedTasks++;
				componentPlotAssemblyComponentDepGraph = new ComponentDependencyGraphAssemblyFilter(new Configuration(), ANALYSIS_INSTANCE);

				final String[] nodeDecorations = TraceAnalysisTool.cmdl.getOptionValues(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYCOMPONENTDEPG);
				TraceAnalysisTool.addDecorators(nodeDecorations, componentPlotAssemblyComponentDepGraph);

				ANALYSIS_INSTANCE.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblyComponentDepGraph, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				ANALYSIS_INSTANCE.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblyComponentDepGraph, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				ANALYSIS_INSTANCE.connect(componentPlotAssemblyComponentDepGraph, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotAssemblyComponentDepGraph);
				allGraphProducers.add(componentPlotAssemblyComponentDepGraph);
			}

			ContainerDependencyGraphFilter componentPlotContainerDepGraph = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG)) {
				numRequestedTasks++;
				componentPlotContainerDepGraph = new ContainerDependencyGraphFilter(new Configuration(), ANALYSIS_INSTANCE);
				ANALYSIS_INSTANCE.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotContainerDepGraph, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				ANALYSIS_INSTANCE.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotContainerDepGraph, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				ANALYSIS_INSTANCE.connect(componentPlotContainerDepGraph, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotContainerDepGraph);
				allGraphProducers.add(componentPlotContainerDepGraph);
			}

			OperationDependencyGraphAllocationFilter componentPlotAllocationOperationDepGraph = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONOPERATIONDEPG)) {
				numRequestedTasks++;
				componentPlotAllocationOperationDepGraph = new OperationDependencyGraphAllocationFilter(new Configuration(), ANALYSIS_INSTANCE);

				final String[] nodeDecorations = TraceAnalysisTool.cmdl.getOptionValues(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONOPERATIONDEPG);
				TraceAnalysisTool.addDecorators(nodeDecorations, componentPlotAllocationOperationDepGraph);

				ANALYSIS_INSTANCE.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAllocationOperationDepGraph, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				ANALYSIS_INSTANCE.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAllocationOperationDepGraph, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				ANALYSIS_INSTANCE.connect(componentPlotAllocationOperationDepGraph, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotAllocationOperationDepGraph);
				allGraphProducers.add(componentPlotAllocationOperationDepGraph);
			}

			OperationDependencyGraphAssemblyFilter componentPlotAssemblyOperationDepGraph = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYOPERATIONDEPG)) {
				numRequestedTasks++;
				componentPlotAssemblyOperationDepGraph = new OperationDependencyGraphAssemblyFilter(new Configuration(), ANALYSIS_INSTANCE);

				final String[] nodeDecorations = TraceAnalysisTool.cmdl.getOptionValues(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYOPERATIONDEPG);
				TraceAnalysisTool.addDecorators(nodeDecorations, componentPlotAssemblyOperationDepGraph);

				ANALYSIS_INSTANCE.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblyOperationDepGraph, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				ANALYSIS_INSTANCE.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblyOperationDepGraph, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				ANALYSIS_INSTANCE.connect(componentPlotAssemblyOperationDepGraph, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotAssemblyOperationDepGraph);
				allGraphProducers.add(componentPlotAssemblyOperationDepGraph);
			}

			TraceCallTreeFilter componentPlotTraceCallTrees = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTCALLTREES)) {
				numRequestedTasks++;
				final Configuration componentPlotTraceCallTreesConfig = new Configuration();
				componentPlotTraceCallTreesConfig.setProperty(TraceCallTreeFilter.CONFIG_PROPERTY_NAME_OUTPUT_FILENAME, new File(TraceAnalysisTool.outputDir
						+ File.separator + TraceAnalysisTool.outputFnPrefix + Constants.CALL_TREE_FN_PREFIX).getCanonicalPath());
				componentPlotTraceCallTreesConfig
						.setProperty(TraceCallTreeFilter.CONFIG_PROPERTY_NAME_SHORT_LABELS, Boolean.toString(TraceAnalysisTool.shortLabels));
				componentPlotTraceCallTreesConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME, Constants.PLOTCALLTREE_COMPONENT_NAME);
				componentPlotTraceCallTrees = new TraceCallTreeFilter(componentPlotTraceCallTreesConfig, ANALYSIS_INSTANCE);

				ANALYSIS_INSTANCE.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotTraceCallTrees, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				ANALYSIS_INSTANCE.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotTraceCallTrees, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				ANALYSIS_INSTANCE.connect(componentPlotTraceCallTrees, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotTraceCallTrees);
			}
			AggregatedAllocationComponentOperationCallTreeFilter componentPlotAggregatedCallTree = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTAGGREGATEDALLOCATIONCALLTREE)) {
				numRequestedTasks++;
				final Configuration componentPlotAggregatedCallTreeConfig = new Configuration();
				componentPlotAggregatedCallTreeConfig
						.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
								Constants.PLOTAGGREGATEDALLOCATIONCALLTREE_COMPONENT_NAME);
				componentPlotAggregatedCallTreeConfig.setProperty(AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, Boolean.toString(true));
				componentPlotAggregatedCallTreeConfig.setProperty(AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_SHORT_LABELS,
						Boolean.toString(TraceAnalysisTool.shortLabels));
				componentPlotAggregatedCallTreeConfig.setProperty(AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_OUTPUT_FILENAME, TraceAnalysisTool.outputDir
						+ File.separator + TraceAnalysisTool.outputFnPrefix
						+ Constants.AGGREGATED_ALLOCATION_CALL_TREE_FN_PREFIX + ".dot");
				componentPlotAggregatedCallTree = new AggregatedAllocationComponentOperationCallTreeFilter(componentPlotAggregatedCallTreeConfig, ANALYSIS_INSTANCE);

				ANALYSIS_INSTANCE.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAggregatedCallTree, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				ANALYSIS_INSTANCE.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAggregatedCallTree, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				ANALYSIS_INSTANCE.connect(componentPlotAggregatedCallTree, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotAggregatedCallTree);
			}
			AggregatedAssemblyComponentOperationCallTreeFilter componentPlotAssemblyCallTree = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTAGGREGATEDASSEMBLYCALLTREE)) {
				numRequestedTasks++;
				final Configuration componentPlotAssemblyCallTreeConfig = new Configuration();
				componentPlotAssemblyCallTreeConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME,
						Constants.PLOTAGGREGATEDASSEMBLYCALLTREE_COMPONENT_NAME);
				componentPlotAssemblyCallTreeConfig.setProperty(AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, Boolean.toString(true));
				componentPlotAssemblyCallTreeConfig.setProperty(AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_SHORT_LABELS,
						Boolean.toString(TraceAnalysisTool.shortLabels));
				componentPlotAssemblyCallTreeConfig.setProperty(AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_OUTPUT_FILENAME, TraceAnalysisTool.outputDir
						+ File.separator + TraceAnalysisTool.outputFnPrefix + Constants.AGGREGATED_ASSEMBLY_CALL_TREE_FN_PREFIX + ".dot");
				componentPlotAssemblyCallTree = new AggregatedAssemblyComponentOperationCallTreeFilter(componentPlotAssemblyCallTreeConfig, ANALYSIS_INSTANCE);

				ANALYSIS_INSTANCE.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblyCallTree, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				ANALYSIS_INSTANCE.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblyCallTree, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				ANALYSIS_INSTANCE.connect(componentPlotAssemblyCallTree, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotAssemblyCallTree);
			}
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_ALLOCATIONEQUIVCLASSREPORT)) {
				numRequestedTasks++;
				// the actual execution of the task is performed below
			}

			// Attach graph processors to the graph producers
			TraceAnalysisTool.attachGraphProcessors(allGraphProducers, ANALYSIS_INSTANCE, cmdl);

			if (numRequestedTasks == 0) {
				LOG.warn("No task requested");
				TraceAnalysisTool.printUsage();
				System.err.println(""); // NOPMD (System.out)
				System.err.println("No task requested"); // NOPMD (System.out)
				System.err.println(""); // NOPMD (System.out)
				return false;
			}

			if (retVal) {
				final String systemEntitiesHtmlFn =
						TraceAnalysisTool.outputDir + File.separator + TraceAnalysisTool.outputFnPrefix + "system-entities.html";
				final Configuration systemModel2FileFilterConfig = new Configuration();
				systemModel2FileFilterConfig.setProperty(SystemModel2FileFilter.CONFIG_PROPERTY_NAME_HTML_OUTPUT_FN, systemEntitiesHtmlFn);
				final SystemModel2FileFilter systemModel2FileFilter = new SystemModel2FileFilter(systemModel2FileFilterConfig, ANALYSIS_INSTANCE);
				// note that this plugin is (currently) not connected to any other filters
				ANALYSIS_INSTANCE.connect(systemModel2FileFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
			}

			int numErrorCount = 0;
			try {
				ANALYSIS_INSTANCE.run();
			} catch (final Exception ex) { // NOPMD NOCS (FindBugs reports that Exception is never thrown; but wontfix (#44)!)
				throw new Exception("Error occured while running analysis", ex);
			} finally {
				for (final AbstractTraceProcessingFilter c : allTraceProcessingComponents) {
					numErrorCount += c.getErrorCount();
					c.printStatusMessage();
				}
				final String kaxOutputFn = TraceAnalysisTool.outputDir + File.separator + TraceAnalysisTool.outputFnPrefix + "traceAnalysis.kax";
				final File kaxOutputFile = new File(kaxOutputFn);
				try { // NOCS (nested try)
						// Try to serialize analysis configuration to .kax file
					ANALYSIS_INSTANCE.saveToFile(kaxOutputFile);
					LOG.info("Saved analysis configuration to file '" + kaxOutputFile.getCanonicalPath() + "'");
				} catch (final IOException ex) {
					LOG.error("Failed to save analysis configuration to file '" + kaxOutputFile.getCanonicalPath() + "'");
				}
			}
			if (!TraceAnalysisTool.ignoreInvalidTraces && (numErrorCount > 0)) {
				throw new Exception(numErrorCount + " errors occured in trace processing components");
			}

			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_ALLOCATIONEQUIVCLASSREPORT)) {
				retVal = TraceAnalysisTool.writeTraceEquivalenceReport(TraceAnalysisTool.outputDir + File.separator + TraceAnalysisTool.outputFnPrefix
						+ Constants.TRACE_ALLOCATION_EQUIV_CLASSES_FN_PREFIX + ".txt", traceAllocationEquivClassFilter);
			}

			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_ASSEMBLYEQUIVCLASSREPORT)) {
				retVal = TraceAnalysisTool.writeTraceEquivalenceReport(TraceAnalysisTool.outputDir + File.separator + TraceAnalysisTool.outputFnPrefix
						+ Constants.TRACE_ASSEMBLY_EQUIV_CLASSES_FN_PREFIX + ".txt", traceAssemblyEquivClassFilter);
			}

			if (!retVal) {
				System.err.println("A task failed"); // NOPMD (System.out)
			}
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			System.err.println("An error occured: " + ex.getMessage()); // NOPMD (System.out)
			System.err.println(""); // NOPMD (System.out)
			LOG.error("Exception", ex);
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

			System.out.println(""); // NOPMD (System.out)
			System.out.println("See 'kieker.log' for details"); // NOPMD (System.out)
		}

		return retVal;
	}

	/**
	 * Returns if the specified output directory {@link #outputDir} exists. If
	 * the directory does not exist, an error message is printed to stderr.
	 * 
	 * @return true if {@link #outputDir} is exists and is a directory; false
	 *         otherwise
	 */
	private static boolean assertOutputDirExists() {
		final File outputDirFile = new File(TraceAnalysisTool.outputDir);
		try {
			if (!outputDirFile.exists()) {
				System.err.println(""); // NOPMD (System.out)
				System.err.println("The specified output directory '" + outputDirFile.getCanonicalPath() + "' does not exist"); // NOPMD (System.out)
				return false;
			}

			if (!outputDirFile.isDirectory()) {
				System.err.println(""); // NOPMD (System.out)
				System.err.println("The specified output directory '" + outputDirFile.getCanonicalPath() + "' is not a directory"); // NOPMD (System.out)
				return false;
			}

		} catch (final IOException e) { // thrown by File.getCanonicalPath()
			System.err.println(""); // NOPMD (System.out)
			System.err.println("Error resolving name of output directory: '" + TraceAnalysisTool.outputDir + "'"); // NOPMD (System.out)
		}

		return true;
	}

	/**
	 * Returns if the specified input directories {@link #inputDirs} exist and that
	 * each one is a monitoring log. If this is not the case for one of the directories,
	 * an error message is printed to stderr.
	 * 
	 * @return true if {@link #outputDir} is exists and is a directory; false
	 *         otherwise
	 */
	private static boolean assertInputDirsExistsAndAreMonitoringLogs() {
		for (final String inputDir : TraceAnalysisTool.inputDirs) {
			final File inputDirFile = new File(inputDir);
			try {
				if (!inputDirFile.exists()) {
					System.err.println(""); // NOPMD (System.out)
					System.err.println("The specified input directory '" + inputDirFile.getCanonicalPath() + "' does not exist"); // NOPMD (System.out)
					return false;
				}

				if (!inputDirFile.isDirectory()) {
					System.err.println(""); // NOPMD (System.out)
					System.err.println("The specified input directory '" + inputDirFile.getCanonicalPath() + "' is not a directory"); // NOPMD (System.out)
					return false;
				}

				/* check whether inputDirFile contains a (kieker|tpmon).map file; the latter for legacy reasons */
				final File[] mapFiles = { new File(inputDir + File.separatorChar + FSConstants.MAP_FILENAME),
					new File(inputDir + File.separatorChar + FSConstants.LEGACY_MAP_FILENAME), };
				boolean mapFileExists = false;
				for (final File potentialMapFile : mapFiles) {
					if (potentialMapFile.isFile()) {
						mapFileExists = true;
						break;
					}
				}
				if (!mapFileExists) {
					System.err.println(""); // NOPMD (System.out)
					System.err.println("The specified input directory '" + inputDirFile.getCanonicalPath() + "' is not a kieker log directory"); // NOPMD
																																					// (System.out)
					return false;
				}
			} catch (final IOException e) { // thrown by File.getCanonicalPath()
				System.err.println(""); // NOPMD (System.out)
				System.err.println("Error resolving name of input directory: '" + inputDir + "'"); // NOPMD (System.out)
			}
		}

		return true;
	}

	/**
	 * This is the main method of the tool.
	 * 
	 * @param args
	 *            The command line arguments.
	 */
	public static void main(final String[] args) {
		try {
			if (!TraceAnalysisTool.parseArgs(args) || !TraceAnalysisTool.initFromArgs()
					|| !TraceAnalysisTool.assertOutputDirExists() || !TraceAnalysisTool.assertInputDirsExistsAndAreMonitoringLogs()) {
				System.exit(1);
			}

			TraceAnalysisTool.dumpConfiguration();

			if (!TraceAnalysisTool.dispatchTasks()) {
				System.exit(1);
			}

		} catch (final Exception exc) { // NOPMD NOCS (IllegalCatchCheck)
			System.err.println("An error occured. See 'kieker.log' for details"); // NOPMD (System.out)
			LOG.error(Arrays.toString(args), exc);
		}
	}

	private static boolean writeTraceEquivalenceReport(final String outputFnPrefixL, final TraceEquivalenceClassFilter traceEquivFilter) throws IOException {
		boolean retVal = true;
		final String outputFn = new File(outputFnPrefixL).getCanonicalPath();
		PrintStream ps = null;
		try {
			ps = new PrintStream(new FileOutputStream(outputFn), false, ENCODING);
			int numClasses = 0;
			final Map<ExecutionTrace, Integer> classMap = traceEquivFilter.getEquivalenceClassMap(); // NOPMD (UseConcurrentHashMap)
			for (final Entry<ExecutionTrace, Integer> e : classMap.entrySet()) {
				final ExecutionTrace t = e.getKey();
				ps.println("Class " + numClasses++ + " ; cardinality: " + e.getValue() + "; # executions: " + t.getLength() + "; representative: " + t.getTraceId()
						+ "; max. stack depth: " + t.getMaxEss());
			}
			System.out.println(""); // NOPMD (System.out)
			System.out.println("#"); // NOPMD (System.out)
			System.out.println("# Plugin: " + "Trace equivalence report"); // NOPMD (System.out)
			System.out.println("Wrote " + numClasses + " equivalence class" + (numClasses > 1 ? "es" : "") + " to file '" + outputFn + "'"); // NOCS // NOPMD
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
}
