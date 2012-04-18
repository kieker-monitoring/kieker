/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.filter.select.TimestampFilter;
import kieker.analysis.plugin.filter.trace.TraceIdFilter;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.traceAnalysis.filter.AbstractMessageTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.executionRecordTransformation.ExecutionRecordTransformationFilter;
import kieker.tools.traceAnalysis.filter.flow.EventRecordTraceReconstructionFilter;
import kieker.tools.traceAnalysis.filter.flow.TraceEventRecords2ExecutionAndMessageTraceFilter;
import kieker.tools.traceAnalysis.filter.systemModel.SystemModel2FileFilter;
import kieker.tools.traceAnalysis.filter.traceFilter.TraceEquivalenceClassFilter;
import kieker.tools.traceAnalysis.filter.traceFilter.TraceEquivalenceClassFilter.TraceEquivalenceClassModes;
import kieker.tools.traceAnalysis.filter.traceReconstruction.TraceReconstructionFilter;
import kieker.tools.traceAnalysis.filter.traceWriter.ExecutionTraceWriterFilter;
import kieker.tools.traceAnalysis.filter.traceWriter.InvalidExecutionTraceWriterFilter;
import kieker.tools.traceAnalysis.filter.traceWriter.MessageTraceWriterFilter;
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
import kieker.tools.traceAnalysis.filter.visualization.sequenceDiagram.SequenceDiagramFilter;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.repository.AllocationComponentOperationPairFactory;
import kieker.tools.traceAnalysis.systemModel.repository.AssemblyComponentOperationPairFactory;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.tools.util.LoggingTimestampConverter;

/**
 * 
 * TODO: Fix JavaDoc comment
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
 * @author Andre van Hoorn, Matthias Rohr
 */
public final class TraceAnalysisTool {
	public static final String DATE_FORMAT_PATTERN_CMD_USAGE_HELP = Constants.DATE_FORMAT_PATTERN.replaceAll("'", ""); // only for usage info
	private static final Log LOG = LogFactory.getLog(TraceAnalysisTool.class);
	private static final SystemModelRepository SYSTEM_ENTITY_FACTORY = new SystemModelRepository(new Configuration());
	private static final AllocationComponentOperationPairFactory ALLOCATION_COMPONENT_OPERATION_PAIR_FACTORY = new AllocationComponentOperationPairFactory(
			SYSTEM_ENTITY_FACTORY);
	private static final AssemblyComponentOperationPairFactory ASSEMBLY_COMPONENT_OPERATION_PAIR_FACTORY = new AssemblyComponentOperationPairFactory(
			SYSTEM_ENTITY_FACTORY);
	private static final CommandLineParser CMDL_PARSER = new BasicParser();
	private static CommandLine cmdl = null;
	private static String[] inputDirs = null;
	private static String outputDir = null;
	private static String outputFnPrefix = null;
	private static Set<Long> selectedTraces = null; // null means select all
	private static boolean shortLabels = true;
	private static boolean includeSelfLoops = false;
	private static boolean ignoreInvalidTraces = false;
	private static int maxTraceDurationMillis = 10 * 60 * 1000; // 10 minutes default
	private static long ignoreExecutionsBeforeTimestamp = TimestampFilter.CONFIG_PROPERTY_VALUE_MIN_TIMESTAMP;
	private static long ignoreExecutionsAfterTimestamp = TimestampFilter.CONFIG_PROPERTY_VALUE_MAX_TIMESTAMP;

	private static final String ENCODING = "UTF-8";

	private TraceAnalysisTool() {}

	private static boolean parseArgs(final String[] args) {
		try {
			TraceAnalysisTool.cmdl = CMDL_PARSER.parse(Constants.CMDL_OPTIONS, args);
		} catch (final ParseException e) {
			TraceAnalysisTool.printUsage();
			System.err.println("\nError parsing arguments: " + e.getMessage());
			return false;
		}
		return true;
	}

	private static void printUsage() {
		Constants.CMD_HELP_FORMATTER.printHelp(80, TraceAnalysisTool.class.getName(), "", Constants.CMDL_OPTIONS, "", true);
	}

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
				System.err.println("\nFailed to parse list of trace IDs: " + Arrays.toString(traceIdList) + "(" + e.getMessage() + ")");
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
			System.err.println("\nFailed to parse int value of property " + Constants.CMD_OPT_NAME_MAXTRACEDURATION + " (must be an integer): "
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
			System.err.println(errorMsg);
			LOG.error(errorMsg, ex);
			return false;
		}
		return true;
	}

	private static void dumpConfiguration() {
		System.out.println("#");
		System.out.println("# Configuration");
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
			} else {
				val = Arrays.toString(TraceAnalysisTool.cmdl.getOptionValues(longOpt));
				LOG.warn("Unformatted confguration output for option " + longOpt);
			}
			System.out.println("--" + longOpt + ": " + val);
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

	private static boolean dispatchTasks() {
		boolean retVal = true;
		int numRequestedTasks = 0;

		TraceReconstructionFilter mtReconstrFilter = null;
		EventRecordTraceReconstructionFilter eventTraceReconstructionFilter = null;
		TraceEventRecords2ExecutionAndMessageTraceFilter traceEvents2ExecutionAndMessageTraceFilter = null;
		try {
			final AnalysisController analysisInstance = new AnalysisController();

			FSReader reader;
			{ // NOCS (NestedBlock)
				final Configuration conf = new Configuration(null);
				conf.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, Configuration.toProperty(TraceAnalysisTool.inputDirs));
				conf.setProperty(FSReader.CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES, Boolean.TRUE.toString());
				reader = new FSReader(conf);
				analysisInstance.registerReader(reader);
			}

			/*
			 * This map can be used within the constructor for all following plugins which use the repository with the name defined in the
			 * AbstractTraceAnalysisPlugin.
			 */
			analysisInstance.registerRepository(SYSTEM_ENTITY_FACTORY);

			final TimestampFilter timestampFilter;
			{ // NOCS (nested block)
				/*
				 * Create the timestamp filter and connect to the reader's output port
				 */
				final Configuration configTimestampFilter = new Configuration();
				configTimestampFilter.setProperty(kieker.analysis.plugin.filter.select.TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_BEFORE_TIMESTAMP,
						Long.toString(TraceAnalysisTool.ignoreExecutionsBeforeTimestamp));
				configTimestampFilter.setProperty(kieker.analysis.plugin.filter.select.TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_AFTER_TIMESTAMP,
						Long.toString(TraceAnalysisTool.ignoreExecutionsAfterTimestamp));

				timestampFilter =
						new TimestampFilter(configTimestampFilter);
				analysisInstance.registerFilter(timestampFilter);
				analysisInstance.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS, timestampFilter, TimestampFilter.INPUT_PORT_NAME_EXECUTION);
				analysisInstance.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS, timestampFilter, TimestampFilter.INPUT_PORT_NAME_FLOW);
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

				traceIdFilter =
						new TraceIdFilter(configTraceIdFilterFlow);
				analysisInstance.registerFilter(traceIdFilter);
				analysisInstance.connect(timestampFilter, kieker.analysis.plugin.filter.select.TimestampFilter.OUTPUT_PORT_NAME_WITHIN_PERIOD,
						traceIdFilter, TraceIdFilter.INPUT_PORT_NAME_COMBINED);
			}

			final ExecutionRecordTransformationFilter execRecTransformer;
			{ // NOCS (nested block)
				/*
				 * Create the execution record transformation filter and connect to the trace ID filter's output port
				 */
				final Configuration execRecTransformerConfig = new Configuration();
				execRecTransformerConfig.setProperty(AbstractPlugin.CONFIG_NAME, Constants.EXEC_TRACE_RECONSTR_COMPONENT_NAME);
				execRecTransformer =
						new ExecutionRecordTransformationFilter(execRecTransformerConfig);
				analysisInstance.registerFilter(execRecTransformer);
				analysisInstance.connect(traceIdFilter, TraceIdFilter.OUTPUT_PORT_NAME_MATCH, execRecTransformer,
						ExecutionRecordTransformationFilter.INPUT_PORT_NAME_RECORDS);
				analysisInstance.connect(execRecTransformer, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, SYSTEM_ENTITY_FACTORY);
			}

			{ // NOCS (nested block)
				/*
				 * Create the trace reconstruction filter and connect to the record transformation filter's output port
				 */
				final Configuration mtReconstrFilterConfig = new Configuration();
				mtReconstrFilterConfig.setProperty(AbstractPlugin.CONFIG_NAME, Constants.TRACERECONSTR_COMPONENT_NAME);
				mtReconstrFilterConfig.setProperty(TraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION_MILLIS,
						Integer.toString(TraceAnalysisTool.maxTraceDurationMillis));
				mtReconstrFilterConfig.setProperty(TraceReconstructionFilter.CONFIG_PROPERTY_NAME_IGNORE_INVALID_TRACES,
						Boolean.toString(TraceAnalysisTool.ignoreInvalidTraces));
				mtReconstrFilter = new TraceReconstructionFilter(mtReconstrFilterConfig);
				analysisInstance.registerFilter(mtReconstrFilter);
				analysisInstance.connect(mtReconstrFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, SYSTEM_ENTITY_FACTORY);
				analysisInstance.connect(execRecTransformer, ExecutionRecordTransformationFilter.OUTPUT_PORT_NAME_EXECUTIONS,
						mtReconstrFilter, TraceReconstructionFilter.INPUT_PORT_NAME_EXECUTIONS);
			}

			{ // NOCS (nested block)
				/*
				 * Create the event record trace generation filter and connect to the trace ID filter's output port
				 */
				final Configuration configurationEventRecordTraceGenerationFilter = new Configuration();
				configurationEventRecordTraceGenerationFilter.setProperty(AbstractPlugin.CONFIG_NAME,
						Constants.EVENTRECORDTRACERECONSTR_COMPONENT_NAME);
				// configurationEventRecordTraceGenerationFilter.setProperty(EventRecordTraceGenerationFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION_MILLIS,
				// Integer.toString(TraceAnalysisTool.maxTraceDurationMillis));
				eventTraceReconstructionFilter = new EventRecordTraceReconstructionFilter(configurationEventRecordTraceGenerationFilter);
				analysisInstance.registerFilter(eventTraceReconstructionFilter);
				analysisInstance.connect(traceIdFilter, kieker.analysis.plugin.filter.trace.TraceIdFilter.OUTPUT_PORT_NAME_MATCH,
						eventTraceReconstructionFilter, EventRecordTraceReconstructionFilter.INPUT_PORT_NAME_TRACE_RECORDS);
				// analysisInstance.connect(eventTraceReconstructionFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
				// SYSTEM_ENTITY_FACTORY);
			}

			{ // NOCS (nested block)
				/*
				 * Create the event trace to execution/message trace transformation filter and connect its input to the
				 * event record trace generation filter's output port
				 */
				final Configuration configurationEventTrace2ExecutionTraceFilter = new Configuration();
				configurationEventTrace2ExecutionTraceFilter.setProperty(AbstractPlugin.CONFIG_NAME,
						Constants.EXECTRACESFROMEVENTTRACES_COMPONENT_NAME);
				// EventTrace2ExecutionTraceFilter has no configuration properties
				traceEvents2ExecutionAndMessageTraceFilter = new TraceEventRecords2ExecutionAndMessageTraceFilter(configurationEventTrace2ExecutionTraceFilter);
				analysisInstance.registerFilter(traceEvents2ExecutionAndMessageTraceFilter);
				analysisInstance.connect(eventTraceReconstructionFilter, EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_VALID,
						traceEvents2ExecutionAndMessageTraceFilter, TraceEventRecords2ExecutionAndMessageTraceFilter.INPUT_PORT_NAME_EVENT_TRACE);
				analysisInstance.connect(traceEvents2ExecutionAndMessageTraceFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
			}

			final List<AbstractTraceProcessingFilter> allTraceProcessingComponents = new ArrayList<AbstractTraceProcessingFilter>();

			final Configuration traceAllocationEquivClassFilterConfig = new Configuration();
			traceAllocationEquivClassFilterConfig.setProperty(AbstractPlugin.CONFIG_NAME,
					Constants.TRACEALLOCATIONEQUIVCLASS_COMPONENT_NAME);
			final TraceEquivalenceClassFilter traceAllocationEquivClassFilter = new TraceEquivalenceClassFilter(traceAllocationEquivClassFilterConfig);
			traceAllocationEquivClassFilter.setTraceEquivalenceCallMode(TraceEquivalenceClassModes.ALLOCATION);
			if (TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_ALLOCATIONEQUIVCLASSREPORT)) {
				/**
				 * Currently, this filter is only used to print an equivalence
				 * report. That's why we only activate it in case this options
				 * is requested.
				 */
				analysisInstance.registerFilter(traceAllocationEquivClassFilter);
				analysisInstance.connect(traceAllocationEquivClassFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
						traceAllocationEquivClassFilter, TraceEquivalenceClassFilter.INPUT_PORT_NAME_EXECUTION_TRACE);
				analysisInstance.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
						traceAllocationEquivClassFilter, TraceEquivalenceClassFilter.INPUT_PORT_NAME_EXECUTION_TRACE);
				allTraceProcessingComponents.add(traceAllocationEquivClassFilter);
			}

			final Configuration traceAssemblyEquivClassFilterConfig = new Configuration();
			traceAssemblyEquivClassFilterConfig.setProperty(AbstractPlugin.CONFIG_NAME, Constants.TRACEASSEMBLYEQUIVCLASS_COMPONENT_NAME);
			final TraceEquivalenceClassFilter traceAssemblyEquivClassFilter = new TraceEquivalenceClassFilter(traceAssemblyEquivClassFilterConfig);
			traceAssemblyEquivClassFilter.setTraceEquivalenceCallMode(TraceEquivalenceClassModes.ASSEMBLY);
			if (TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_ASSEMBLYEQUIVCLASSREPORT)) {
				/**
				 * Currently, this filter is only used to print an equivalence
				 * report. That's why we only activate it in case this options
				 * is requested.
				 */
				analysisInstance.registerFilter(traceAssemblyEquivClassFilter);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
						traceAssemblyEquivClassFilter, TraceEquivalenceClassFilter.INPUT_PORT_NAME_EXECUTION_TRACE);
				analysisInstance.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
						traceAssemblyEquivClassFilter, TraceEquivalenceClassFilter.INPUT_PORT_NAME_EXECUTION_TRACE);
				analysisInstance.connect(traceAssemblyEquivClassFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(traceAssemblyEquivClassFilter);
			}

			// fill list of msgTraceProcessingComponents:
			MessageTraceWriterFilter componentPrintMsgTrace = null;
			if (TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PRINTMSGTRACES)) {
				numRequestedTasks++;
				final Configuration componentPrintMsgTraceConfig = new Configuration();
				componentPrintMsgTraceConfig.setProperty(AbstractPlugin.CONFIG_NAME, Constants.PRINTMSGTRACE_COMPONENT_NAME);
				componentPrintMsgTraceConfig.setProperty(MessageTraceWriterFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN, new File(TraceAnalysisTool.outputDir
						+ File.separator
						+ TraceAnalysisTool.outputFnPrefix + Constants.MESSAGE_TRACES_FN_PREFIX + ".txt")
						.getCanonicalPath());
				componentPrintMsgTrace = new MessageTraceWriterFilter(componentPrintMsgTraceConfig);
				analysisInstance.registerFilter(componentPrintMsgTrace);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPrintMsgTrace, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				analysisInstance.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPrintMsgTrace, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				analysisInstance.connect(componentPrintMsgTrace, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPrintMsgTrace);
			}
			ExecutionTraceWriterFilter componentPrintExecTrace = null;
			if (TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PRINTEXECTRACES)) {
				numRequestedTasks++;
				final Configuration componentPrintExecTraceConfig = new Configuration();
				componentPrintExecTraceConfig.setProperty(AbstractPlugin.CONFIG_NAME, Constants.PRINTEXECTRACE_COMPONENT_NAME);
				componentPrintExecTraceConfig.setProperty(ExecutionTraceWriterFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN, new File(TraceAnalysisTool.outputDir
						+ File.separator
						+ TraceAnalysisTool.outputFnPrefix + Constants.EXECUTION_TRACES_FN_PREFIX + ".txt")
						.getCanonicalPath());
				componentPrintExecTrace = new ExecutionTraceWriterFilter(componentPrintExecTraceConfig);
				analysisInstance.registerFilter(componentPrintExecTrace);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
						componentPrintExecTrace, ExecutionTraceWriterFilter.INPUT_PORT_NAME_EXECUTION_TRACES);
				analysisInstance.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
						componentPrintExecTrace, ExecutionTraceWriterFilter.INPUT_PORT_NAME_EXECUTION_TRACES);
				analysisInstance.connect(componentPrintExecTrace, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPrintExecTrace);
			}
			InvalidExecutionTraceWriterFilter componentPrintInvalidTrace = null;
			if (TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES)) {
				numRequestedTasks++;
				final Configuration componentPrintInvalidTraceConfig = new Configuration();
				componentPrintInvalidTraceConfig.setProperty(AbstractPlugin.CONFIG_NAME,
						Constants.PRINTINVALIDEXECTRACE_COMPONENT_NAME);
				componentPrintInvalidTraceConfig.setProperty(InvalidExecutionTraceWriterFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN, new File(TraceAnalysisTool.outputDir
						+ File.separator
						+ TraceAnalysisTool.outputFnPrefix
						+ Constants.INVALID_TRACES_FN_PREFIX + ".txt").getCanonicalPath());
				componentPrintInvalidTrace = new InvalidExecutionTraceWriterFilter(componentPrintInvalidTraceConfig);
				analysisInstance.registerFilter(componentPrintInvalidTrace);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_INVALID_EXECUTION_TRACE,
						componentPrintInvalidTrace, InvalidExecutionTraceWriterFilter.INPUT_PORT_NAME_INVALID_EXECUTION_TRACES);
				analysisInstance.connect(componentPrintInvalidTrace, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
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
				componentPlotAllocationSeqDiagrConfig.setProperty(AbstractPlugin.CONFIG_NAME, Constants.PLOTALLOCATIONSEQDIAGR_COMPONENT_NAME);
				componentPlotAllocationSeqDiagrConfig.setProperty(SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN_BASE,
						TraceAnalysisTool.outputDir + File.separator + TraceAnalysisTool.outputFnPrefix + Constants.ALLOCATION_SEQUENCE_DIAGRAM_FN_PREFIX);
				componentPlotAllocationSeqDiagrConfig.setProperty(SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_SDMODE,
						SequenceDiagramFilter.SDModes.ALLOCATION.toString());
				componentPlotAllocationSeqDiagrConfig.setProperty(SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_SHORTLABES,
						Boolean.toString(TraceAnalysisTool.shortLabels));
				componentPlotAllocationSeqDiagr = new SequenceDiagramFilter(componentPlotAllocationSeqDiagrConfig);
				analysisInstance.registerFilter(componentPlotAllocationSeqDiagr);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAllocationSeqDiagr, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				analysisInstance.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAllocationSeqDiagr, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				analysisInstance.connect(componentPlotAllocationSeqDiagr, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotAllocationSeqDiagr);
			}
			SequenceDiagramFilter componentPlotAssemblySeqDiagr = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYSEQDS)) {
				numRequestedTasks++;
				final Configuration componentPlotAssemblySeqDiagrConfig = new Configuration();
				componentPlotAssemblySeqDiagrConfig.setProperty(AbstractPlugin.CONFIG_NAME, Constants.PLOTASSEMBLYSEQDIAGR_COMPONENT_NAME);
				componentPlotAssemblySeqDiagrConfig.setProperty(SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN_BASE,
						TraceAnalysisTool.outputDir + File.separator + TraceAnalysisTool.outputFnPrefix + Constants.ASSEMBLY_SEQUENCE_DIAGRAM_FN_PREFIX);
				componentPlotAssemblySeqDiagrConfig.setProperty(SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_SDMODE,
						SequenceDiagramFilter.SDModes.ASSEMBLY.toString());
				componentPlotAssemblySeqDiagrConfig.setProperty(SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_SHORTLABES,
						Boolean.toString(TraceAnalysisTool.shortLabels));
				componentPlotAssemblySeqDiagr = new SequenceDiagramFilter(componentPlotAssemblySeqDiagrConfig);
				analysisInstance.registerFilter(componentPlotAssemblySeqDiagr);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblySeqDiagr, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				analysisInstance.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblySeqDiagr, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				analysisInstance.connect(componentPlotAssemblySeqDiagr, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotAssemblySeqDiagr);
			}
			ComponentDependencyGraphAllocationFilter componentPlotAllocationComponentDepGraph = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONCOMPONENTDEPG)) {
				numRequestedTasks++;
				final Configuration componentPlotAllocationComponentDepGraphConfig = new Configuration();
				componentPlotAllocationComponentDepGraphConfig.setProperty(AbstractPlugin.CONFIG_NAME,
						Constants.PLOTALLOCATIONCOMPONENTDEPGRAPH_COMPONENT_NAME);
				componentPlotAllocationComponentDepGraphConfig.setProperty(ComponentDependencyGraphAllocationFilter.CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE,
						TraceAnalysisTool.outputDir + File.separator + TraceAnalysisTool.outputFnPrefix + Constants.ALLOCATION_COMPONENT_DEPENDENCY_GRAPH_FN_PREFIX);
				componentPlotAllocationComponentDepGraphConfig.setProperty(ComponentDependencyGraphAllocationFilter.CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS,
						Boolean.TRUE.toString());
				componentPlotAllocationComponentDepGraphConfig.setProperty(ComponentDependencyGraphAllocationFilter.CONFIG_PROPERTY_NAME_SHORTLABELS,
						Boolean.toString(TraceAnalysisTool.shortLabels));
				componentPlotAllocationComponentDepGraphConfig.setProperty(ComponentDependencyGraphAllocationFilter.CONFIG_PROPERTY_NAME_SELFLOOPS,
						Boolean.toString(TraceAnalysisTool.includeSelfLoops));

				componentPlotAllocationComponentDepGraph = new ComponentDependencyGraphAllocationFilter(
						componentPlotAllocationComponentDepGraphConfig);

				final String[] nodeDecorations = TraceAnalysisTool.cmdl.getOptionValues(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONCOMPONENTDEPG);
				TraceAnalysisTool.addDecorators(nodeDecorations, componentPlotAllocationComponentDepGraph);

				analysisInstance.registerFilter(componentPlotAllocationComponentDepGraph);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAllocationComponentDepGraph, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				analysisInstance.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAllocationComponentDepGraph, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				analysisInstance.connect(componentPlotAllocationComponentDepGraph, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);

				allTraceProcessingComponents.add(componentPlotAllocationComponentDepGraph);
			}
			ComponentDependencyGraphAssemblyFilter componentPlotAssemblyComponentDepGraph = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYCOMPONENTDEPG)) {
				numRequestedTasks++;
				final Configuration componentPlotAssemblyComponentDepGraphConfig = new Configuration();
				componentPlotAssemblyComponentDepGraphConfig.setProperty(AbstractPlugin.CONFIG_NAME,
						Constants.PLOTASSEMBLYCOMPONENTDEPGRAPH_COMPONENT_NAME);
				componentPlotAssemblyComponentDepGraphConfig.setProperty(ComponentDependencyGraphAssemblyFilter.CONFIG_PROPERTY_NAME_SHORT_LABELS,
						Boolean.toString(TraceAnalysisTool.shortLabels));
				componentPlotAssemblyComponentDepGraphConfig.setProperty(ComponentDependencyGraphAssemblyFilter.CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS,
						Boolean.toString(true));
				componentPlotAssemblyComponentDepGraphConfig.setProperty(ComponentDependencyGraphAssemblyFilter.CONFIG_PROPERTY_NAME_INCLUDE_SELF_LOOPS,
						Boolean.toString(TraceAnalysisTool.includeSelfLoops));
				componentPlotAssemblyComponentDepGraphConfig.setProperty(ComponentDependencyGraphAssemblyFilter.CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE, new File(
						TraceAnalysisTool.outputDir
								+ File.separator + TraceAnalysisTool.outputFnPrefix
								+ Constants.ASSEMBLY_COMPONENT_DEPENDENCY_GRAPH_FN_PREFIX).getAbsolutePath());

				componentPlotAssemblyComponentDepGraph = new ComponentDependencyGraphAssemblyFilter(componentPlotAssemblyComponentDepGraphConfig);

				final String[] nodeDecorations = TraceAnalysisTool.cmdl.getOptionValues(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYCOMPONENTDEPG);
				TraceAnalysisTool.addDecorators(nodeDecorations, componentPlotAssemblyComponentDepGraph);

				analysisInstance.registerFilter(componentPlotAssemblyComponentDepGraph);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblyComponentDepGraph, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				analysisInstance.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblyComponentDepGraph, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				analysisInstance.connect(componentPlotAssemblyComponentDepGraph, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotAssemblyComponentDepGraph);
			}
			ContainerDependencyGraphFilter componentPlotContainerDepGraph = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG)) {
				numRequestedTasks++;
				final Configuration componentPlotContainerDepGraphConfig = new Configuration();
				componentPlotContainerDepGraphConfig.setProperty(AbstractPlugin.CONFIG_NAME,
						Constants.PLOTCONTAINERDEPGRAPH_COMPONENT_NAME);
				componentPlotContainerDepGraphConfig.setProperty(ContainerDependencyGraphFilter.CONFIG_PROPERTY_NAME_SHORT_LABELS,
						Boolean.toString(TraceAnalysisTool.shortLabels));
				componentPlotContainerDepGraphConfig.setProperty(ContainerDependencyGraphFilter.CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, Boolean.toString(true));
				componentPlotContainerDepGraphConfig.setProperty(ContainerDependencyGraphFilter.CONFIG_PROPERTY_NAME_INCLUDE_SELF_LOOPS,
						Boolean.toString(TraceAnalysisTool.includeSelfLoops));
				componentPlotContainerDepGraphConfig.setProperty(ContainerDependencyGraphFilter.CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE, new File(
						TraceAnalysisTool.outputDir
								+ File.separator + TraceAnalysisTool.outputFnPrefix
								+ Constants.CONTAINER_DEPENDENCY_GRAPH_FN_PREFIX).getAbsolutePath());

				componentPlotContainerDepGraph = new ContainerDependencyGraphFilter(componentPlotContainerDepGraphConfig);
				analysisInstance.registerFilter(componentPlotContainerDepGraph);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotContainerDepGraph, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				analysisInstance.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotContainerDepGraph, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				analysisInstance.connect(componentPlotContainerDepGraph, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotContainerDepGraph);
			}
			OperationDependencyGraphAllocationFilter componentPlotAllocationOperationDepGraph = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONOPERATIONDEPG)) {
				numRequestedTasks++;

				final Configuration componentPlotAllocationOperationDepGraphConfig = new Configuration();
				componentPlotAllocationOperationDepGraphConfig
						.setProperty(AbstractPlugin.CONFIG_NAME, Constants.PLOTALLOCATIONOPERATIONDEPGRAPH_COMPONENT_NAME);
				componentPlotAllocationOperationDepGraphConfig
						.setProperty(OperationDependencyGraphAllocationFilter.CONFIG_PROPERTY_NAME_SHORT_LABELS, Boolean.toString(TraceAnalysisTool.shortLabels));
				componentPlotAllocationOperationDepGraphConfig.setProperty(OperationDependencyGraphAllocationFilter.CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS,
						Boolean.toString(true));
				componentPlotAllocationOperationDepGraphConfig.setProperty(OperationDependencyGraphAllocationFilter.CONFIG_PROPERTY_NAME_INCLUDE_SELF_LOOPS,
						Boolean.toString(TraceAnalysisTool.includeSelfLoops));
				componentPlotAllocationOperationDepGraphConfig.setProperty(OperationDependencyGraphAllocationFilter.CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE, new File(
						TraceAnalysisTool.outputDir + File.separator + TraceAnalysisTool.outputFnPrefix
								+ Constants.ALLOCATION_OPERATION_DEPENDENCY_GRAPH_FN_PREFIX).getAbsolutePath());

				componentPlotAllocationOperationDepGraph = new OperationDependencyGraphAllocationFilter(componentPlotAllocationOperationDepGraphConfig);

				final String[] nodeDecorations = TraceAnalysisTool.cmdl.getOptionValues(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONOPERATIONDEPG);
				TraceAnalysisTool.addDecorators(nodeDecorations, componentPlotAllocationOperationDepGraph);

				analysisInstance.registerFilter(componentPlotAllocationOperationDepGraph);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAllocationOperationDepGraph, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				analysisInstance.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAllocationOperationDepGraph, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				analysisInstance.connect(componentPlotAllocationOperationDepGraph, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotAllocationOperationDepGraph);
			}
			OperationDependencyGraphAssemblyFilter componentPlotAssemblyOperationDepGraph = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYOPERATIONDEPG)) {
				numRequestedTasks++;

				final Configuration componentPlotAssemblyOperationDepGraphConfig = new Configuration();
				componentPlotAssemblyOperationDepGraphConfig
						.setProperty(AbstractPlugin.CONFIG_NAME, Constants.PLOTASSEMBLYOPERATIONDEPGRAPH_COMPONENT_NAME);
				componentPlotAssemblyOperationDepGraphConfig.setProperty(ComponentDependencyGraphAssemblyFilter.CONFIG_PROPERTY_NAME_SHORT_LABELS,
						Boolean.toString(TraceAnalysisTool.shortLabels));
				componentPlotAssemblyOperationDepGraphConfig.setProperty(OperationDependencyGraphAssemblyFilter.CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS,
						Boolean.toString(true));
				componentPlotAssemblyOperationDepGraphConfig.setProperty(OperationDependencyGraphAssemblyFilter.CONFIG_PROPERTY_NAME_INCLUDE_SELF_LOOPS,
						Boolean.toString(TraceAnalysisTool.includeSelfLoops));
				componentPlotAssemblyOperationDepGraphConfig.setProperty(OperationDependencyGraphAssemblyFilter.CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE, new File(
						TraceAnalysisTool.outputDir
								+ File.separator + TraceAnalysisTool.outputFnPrefix
								+ Constants.ASSEMBLY_OPERATION_DEPENDENCY_GRAPH_FN_PREFIX).getAbsolutePath());

				componentPlotAssemblyOperationDepGraph = new OperationDependencyGraphAssemblyFilter(componentPlotAssemblyOperationDepGraphConfig);

				final String[] nodeDecorations = TraceAnalysisTool.cmdl.getOptionValues(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYOPERATIONDEPG);
				TraceAnalysisTool.addDecorators(nodeDecorations, componentPlotAssemblyOperationDepGraph);

				analysisInstance.registerFilter(componentPlotAssemblyOperationDepGraph);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblyOperationDepGraph, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				analysisInstance.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblyOperationDepGraph, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				analysisInstance.connect(componentPlotAssemblyOperationDepGraph, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotAssemblyOperationDepGraph);
			}
			TraceCallTreeFilter componentPlotTraceCallTrees = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTCALLTREES)) {
				numRequestedTasks++;
				final Configuration componentPlotTraceCallTreesConfig = new Configuration();
				componentPlotTraceCallTreesConfig.setProperty(TraceCallTreeFilter.CONFIG_PROPERTY_NAME_OUTPUT_FILENAME, new File(TraceAnalysisTool.outputDir
						+ File.separator + TraceAnalysisTool.outputFnPrefix + Constants.CALL_TREE_FN_PREFIX).getCanonicalPath());
				componentPlotTraceCallTreesConfig
						.setProperty(TraceCallTreeFilter.CONFIG_PROPERTY_NAME_SHORT_LABELS, Boolean.toString(TraceAnalysisTool.shortLabels));
				componentPlotTraceCallTreesConfig.setProperty(AbstractPlugin.CONFIG_NAME, Constants.PLOTCALLTREE_COMPONENT_NAME);
				componentPlotTraceCallTrees = new TraceCallTreeFilter(componentPlotTraceCallTreesConfig);
				analysisInstance.registerFilter(componentPlotTraceCallTrees);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotTraceCallTrees, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				analysisInstance.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotTraceCallTrees, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				analysisInstance.connect(componentPlotTraceCallTrees, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotTraceCallTrees);
			}
			AggregatedAllocationComponentOperationCallTreeFilter componentPlotAggregatedCallTree = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTAGGREGATEDALLOCATIONCALLTREE)) {
				numRequestedTasks++;
				final Configuration componentPlotAggregatedCallTreeConfig = new Configuration();
				componentPlotAggregatedCallTreeConfig
						.setProperty(AbstractPlugin.CONFIG_NAME,
								Constants.PLOTAGGREGATEDALLOCATIONCALLTREE_COMPONENT_NAME);
				componentPlotAggregatedCallTreeConfig.setProperty(AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, Boolean.toString(true));
				componentPlotAggregatedCallTreeConfig.setProperty(AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_SHORT_LABELS,
						Boolean.toString(TraceAnalysisTool.shortLabels));
				componentPlotAggregatedCallTreeConfig.setProperty(AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_OUTPUT_FILENAME, TraceAnalysisTool.outputDir
						+ File.separator + TraceAnalysisTool.outputFnPrefix
						+ Constants.AGGREGATED_ALLOCATION_CALL_TREE_FN_PREFIX + ".dot");
				componentPlotAggregatedCallTree = new AggregatedAllocationComponentOperationCallTreeFilter(componentPlotAggregatedCallTreeConfig);
				componentPlotAggregatedCallTree.setAllocationComponentOperationPairFactory(ALLOCATION_COMPONENT_OPERATION_PAIR_FACTORY);
				analysisInstance.registerFilter(componentPlotAggregatedCallTree);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAggregatedCallTree, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				analysisInstance.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAggregatedCallTree, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				analysisInstance.connect(componentPlotAggregatedCallTree, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotAggregatedCallTree);
			}
			AggregatedAssemblyComponentOperationCallTreeFilter componentPlotAssemblyCallTree = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTAGGREGATEDASSEMBLYCALLTREE)) {
				numRequestedTasks++;
				final Configuration componentPlotAssemblyCallTreeConfig = new Configuration();
				componentPlotAssemblyCallTreeConfig.setProperty(AbstractPlugin.CONFIG_NAME,
						Constants.PLOTAGGREGATEDASSEMBLYCALLTREE_COMPONENT_NAME);
				componentPlotAssemblyCallTreeConfig.setProperty(AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, Boolean.toString(true));
				componentPlotAssemblyCallTreeConfig.setProperty(AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_SHORT_LABELS,
						Boolean.toString(TraceAnalysisTool.shortLabels));
				componentPlotAssemblyCallTreeConfig.setProperty(AbstractAggregatedCallTreeFilter.CONFIG_PROPERTY_NAME_OUTPUT_FILENAME, TraceAnalysisTool.outputDir
						+ File.separator + TraceAnalysisTool.outputFnPrefix + Constants.AGGREGATED_ASSEMBLY_CALL_TREE_FN_PREFIX + ".dot");
				componentPlotAssemblyCallTree = new AggregatedAssemblyComponentOperationCallTreeFilter(componentPlotAssemblyCallTreeConfig);
				componentPlotAssemblyCallTree.setAssemblyComponentOperationPairFactory(ASSEMBLY_COMPONENT_OPERATION_PAIR_FACTORY);
				analysisInstance.registerFilter(componentPlotAssemblyCallTree);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblyCallTree, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				analysisInstance.connect(traceEvents2ExecutionAndMessageTraceFilter,
						TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblyCallTree, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
				analysisInstance.connect(componentPlotAssemblyCallTree, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotAssemblyCallTree);
			}
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_ALLOCATIONEQUIVCLASSREPORT)) {
				numRequestedTasks++;
				// the actual execution of the task is performed below
			}

			if (numRequestedTasks == 0) {
				LOG.warn("No task requested");
				TraceAnalysisTool.printUsage();
				System.err.println("");
				System.err.println("No task requested");
				System.err.println("");
				return false;
			}

			if (retVal) {
				final String systemEntitiesHtmlFn =
						TraceAnalysisTool.outputDir + File.separator + TraceAnalysisTool.outputFnPrefix + "system-entities.html";
				final Configuration systemModel2FileFilterConfig = new Configuration();
				systemModel2FileFilterConfig.setProperty(SystemModel2FileFilter.CONFIG_PROPERTY_NAME_HTML_OUTPUT_FN, systemEntitiesHtmlFn);
				final SystemModel2FileFilter systemModel2FileFilter = new SystemModel2FileFilter(systemModel2FileFilterConfig);
				// note that this plugin is (currently) not connected to any other filter
				analysisInstance.registerFilter(systemModel2FileFilter);
				analysisInstance.connect(systemModel2FileFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
						SYSTEM_ENTITY_FACTORY);
			}

			int numErrorCount = 0;
			try {
				analysisInstance.run();
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
					analysisInstance.saveToFile(kaxOutputFile);
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
				System.err.println("A task failed");
			}
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			System.err.println("An error occured: " + ex.getMessage());
			System.err.println("");
			LOG.error("Exception", ex);
			retVal = false;
		} finally {
			if (numRequestedTasks > 0) {
				if (mtReconstrFilter != null) {
					mtReconstrFilter.printStatusMessage();
				}
				if (eventTraceReconstructionFilter != null) {
					// TODO add status message
					// eventTraceReconstructionFilter.printStatusMessage();
				}
				if (traceEvents2ExecutionAndMessageTraceFilter != null) {
					traceEvents2ExecutionAndMessageTraceFilter.printStatusMessage();
				}
			}

			System.out.println("");
			System.out.println("See 'kieker.log' for details");
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
				System.err.println("");
				System.err.println("The specified output directory '" + outputDirFile.getCanonicalPath() + "' does not exist");
				return false;
			}

			if (!outputDirFile.isDirectory()) {
				System.err.println("");
				System.err.println("The specified output directory '" + outputDirFile.getCanonicalPath() + "' is not a directory");
				return false;
			}

		} catch (final IOException e) { // thrown by File.getCanonicalPath()
			System.err.println("");
			System.err.println("Error resolving name of output directory: '" + TraceAnalysisTool.outputDir + "'");
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
					System.err.println("");
					System.err.println("The specified input directory '" + inputDirFile.getCanonicalPath() + "' does not exist");
					return false;
				}

				if (!inputDirFile.isDirectory()) {
					System.err.println("");
					System.err.println("The specified input directory '" + inputDirFile.getCanonicalPath() + "' is not a directory");
					return false;
				}

				/* check whether inputDirFile contains a (kieker|tpmon).map file; the latter for legacy reasons */
				final File[] mapFiles = { new File(inputDir + "/kieker.map"), new File(inputDir + "/tpmon.map") };
				boolean mapFileExists = false;
				for (final File potentialMapFile : mapFiles) {
					if (potentialMapFile.isFile()) {
						mapFileExists = true;
						break;
					}
				}
				if (!mapFileExists) {
					System.err.println("");
					System.err.println("The specified input directory '" + inputDirFile.getCanonicalPath() + "' is not a kieker log directory");
					return false;
				}
			} catch (final IOException e) { // thrown by File.getCanonicalPath()
				System.err.println("");
				System.err.println("Error resolving name of input directory: '" + inputDir + "'");
			}
		}

		return true;
	}

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
			System.err.println("An error occured. See 'kieker.log' for details");
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
			System.out.println("");
			System.out.println("#");
			System.out.println("# Plugin: " + "Trace equivalence report");
			System.out.println("Wrote " + numClasses + " equivalence class" + (numClasses > 1 ? "es" : "") + " to file '" + outputFn + "'"); // NOCS
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
