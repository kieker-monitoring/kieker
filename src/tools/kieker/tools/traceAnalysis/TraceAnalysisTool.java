/***************************************************************************
 * Copyright 2011 by
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
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.flow.trace.SplitEvent;
import kieker.common.record.flow.trace.Trace;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.flow.trace.operation.CallOperationEvent;
import kieker.tools.traceAnalysis.plugins.AbstractMessageTraceProcessingPlugin;
import kieker.tools.traceAnalysis.plugins.AbstractTraceAnalysisPlugin;
import kieker.tools.traceAnalysis.plugins.AbstractTraceProcessingPlugin;
import kieker.tools.traceAnalysis.plugins.executionFilter.TimestampFilter;
import kieker.tools.traceAnalysis.plugins.executionFilter.TraceIdFilter;
import kieker.tools.traceAnalysis.plugins.executionRecordTransformation.ExecutionRecordTransformationFilter;
import kieker.tools.traceAnalysis.plugins.flow.EventRecordTraceGenerationFilter;
import kieker.tools.traceAnalysis.plugins.flow.EventTrace2ExecutionTraceFilter;
import kieker.tools.traceAnalysis.plugins.traceFilter.TraceEquivalenceClassFilter;
import kieker.tools.traceAnalysis.plugins.traceFilter.TraceEquivalenceClassFilter.TraceEquivalenceClassModes;
import kieker.tools.traceAnalysis.plugins.traceReconstruction.TraceReconstructionFilter;
import kieker.tools.traceAnalysis.plugins.traceWriter.ExecutionTraceWriterPlugin;
import kieker.tools.traceAnalysis.plugins.traceWriter.InvalidExecutionTraceWriterPlugin;
import kieker.tools.traceAnalysis.plugins.traceWriter.MessageTraceWriterPlugin;
import kieker.tools.traceAnalysis.plugins.visualization.callTree.AggregatedAllocationComponentOperationCallTreePlugin;
import kieker.tools.traceAnalysis.plugins.visualization.callTree.AggregatedAssemblyComponentOperationCallTreePlugin;
import kieker.tools.traceAnalysis.plugins.visualization.callTree.AggregatedCallTreePlugin;
import kieker.tools.traceAnalysis.plugins.visualization.callTree.TraceCallTreePlugin;
import kieker.tools.traceAnalysis.plugins.visualization.dependencyGraph.AbstractDependencyGraphPlugin;
import kieker.tools.traceAnalysis.plugins.visualization.dependencyGraph.ComponentDependencyGraphPluginAllocation;
import kieker.tools.traceAnalysis.plugins.visualization.dependencyGraph.ComponentDependencyGraphPluginAssembly;
import kieker.tools.traceAnalysis.plugins.visualization.dependencyGraph.ContainerDependencyGraphPlugin;
import kieker.tools.traceAnalysis.plugins.visualization.dependencyGraph.NodeDecorator;
import kieker.tools.traceAnalysis.plugins.visualization.dependencyGraph.OperationDependencyGraphPluginAllocation;
import kieker.tools.traceAnalysis.plugins.visualization.dependencyGraph.OperationDependencyGraphPluginAssembly;
import kieker.tools.traceAnalysis.plugins.visualization.sequenceDiagram.SequenceDiagramPlugin;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.repository.AllocationComponentOperationPairFactory;
import kieker.tools.traceAnalysis.systemModel.repository.AssemblyComponentOperationPairFactory;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.tools.util.LoggingTimestampConverter;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

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
			TraceAnalysisTool.SYSTEM_ENTITY_FACTORY);
	private static final AssemblyComponentOperationPairFactory ASSEMBLY_COMPONENT_OPERATION_PAIR_FACTORY = new AssemblyComponentOperationPairFactory(
			TraceAnalysisTool.SYSTEM_ENTITY_FACTORY);
	private static final CommandLineParser CMDL_PARSER = new BasicParser();
	private static CommandLine cmdl = null;
	private static String[] inputDirs = null;
	private static String outputDir = null;
	private static String outputFnPrefix = null;
	private static Set<Long> selectedTraces = null; // null means select all
	private static boolean shortLabels = true;
	private static boolean includeSelfLoops = false;
	private static boolean ignoreInvalidTraces = false;
	private static int maxTraceDurationMillis = 10 * 60 * 1000; // 10 minutes default // NOCS (MagicNumberCheck)
	private static long ignoreExecutionsBeforeTimestamp = TimestampFilter.MIN_TIMESTAMP;
	private static long ignoreExecutionsAfterTimestamp = TimestampFilter.MAX_TIMESTAMP;

	private static final String ENCODING = "UTF-8";

	private TraceAnalysisTool() {}

	private static boolean parseArgs(final String[] args) {
		try {
			TraceAnalysisTool.cmdl = TraceAnalysisTool.CMDL_PARSER.parse(Constants.CMDL_OPTIONS, args);
		} catch (final ParseException e) {
			TraceAnalysisTool.printUsage();
			System.err.println("\nError parsing arguments: " + e.getMessage());
			return false;
		}
		return true;
	}

	private static void printUsage() {
		Constants.CMD_HELP_FORMATTER.printHelp(80, TraceAnalysisTool.class.getName(), "", Constants.CMDL_OPTIONS, "", true); // NOCS (MagicNumberCheck)
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
				TraceAnalysisTool.LOG.info(numSelectedTraces + " trace" + (numSelectedTraces > 1 ? "s" : "") + " selected"); // NOCS
			} catch (final Exception e) { // NOCS (IllegalCatchCheck) // NOPMD
				System.err.println("\nFailed to parse list of trace IDs: " + Arrays.toString(traceIdList) + "(" + e.getMessage() + ")");
				TraceAnalysisTool.LOG.error("Failed to parse list of trace IDs: " + Arrays.toString(traceIdList), e);
				return false;
			}
		}

		TraceAnalysisTool.shortLabels = TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_SHORTLABELS);
		TraceAnalysisTool.ignoreInvalidTraces = TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_IGNOREINVALIDTRACES);

		final String maxTraceDurationStr = TraceAnalysisTool.cmdl.getOptionValue(Constants.CMD_OPT_NAME_MAXTRACEDURATION,
				Integer.toString(TraceAnalysisTool.maxTraceDurationMillis));
		try {
			TraceAnalysisTool.maxTraceDurationMillis = Integer.parseInt(maxTraceDurationStr);
		} catch (final NumberFormatException exc) {
			System.err.println("\nFailed to parse int value of property " + Constants.CMD_OPT_NAME_MAXTRACEDURATION + " (must be an integer): "
					+ maxTraceDurationStr);
			TraceAnalysisTool.LOG.error("Failed to parse int value of property " + Constants.CMD_OPT_NAME_MAXTRACEDURATION + " (must be an integer):"
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
				TraceAnalysisTool.ignoreExecutionsBeforeTimestamp = ignoreBeforeDate.getTime() * (1000 * 1000); // NOCS (MagicNumberCheck)
				TraceAnalysisTool.LOG.info("Ignoring records before " + dateFormat.format(ignoreBeforeDate) + " ("
						+ TraceAnalysisTool.ignoreExecutionsBeforeTimestamp + ")");
			}
			if (ignoreRecordsAfterTimestampString != null) {
				final Date ignoreAfterDate = dateFormat.parse(ignoreRecordsAfterTimestampString);
				TraceAnalysisTool.ignoreExecutionsAfterTimestamp = ignoreAfterDate.getTime() * (1000 * 1000); // NOCS (MagicNumberCheck)
				TraceAnalysisTool.LOG.info("Ignoring records after " + dateFormat.format(ignoreAfterDate) + " ("
						+ TraceAnalysisTool.ignoreExecutionsAfterTimestamp + ")");
			}
		} catch (final java.text.ParseException ex) {
			final String errorMsg = "Error parsing date/time string. Please use the following pattern: " + TraceAnalysisTool.DATE_FORMAT_PATTERN_CMD_USAGE_HELP;
			System.err.println(errorMsg);
			TraceAnalysisTool.LOG.error(errorMsg, ex);
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
				TraceAnalysisTool.LOG.warn("Unformatted confguration output for option " + longOpt);
			}
			System.out.println("--" + longOpt + ": " + val);
		}
	}

	private static void addDecorators(final String[] decoratorNames, final AbstractDependencyGraphPlugin<?> plugin) {
		if (decoratorNames == null) {
			return;
		}

		for (final String currentDecoratorName : decoratorNames) {
			final NodeDecorator currentDecorator = NodeDecorator.createFromName(currentDecoratorName);

			if (currentDecorator == null) {
				TraceAnalysisTool.LOG.warn("Unknown decoration name '" + currentDecoratorName + "'.");
				continue;
			}

			plugin.addDecorator(currentDecorator);
		}
	}

	private static boolean dispatchTasks() {
		boolean retVal = true;
		int numRequestedTasks = 0;

		TraceReconstructionFilter mtReconstrFilter = null;
		EventRecordTraceGenerationFilter eventRecordTraceGenerationFilter = null;
		EventTrace2ExecutionTraceFilter eventTrace2ExecutionTraceFilter = null;
		try {
			final AnalysisController analysisInstance = new AnalysisController();

			FSReader reader;
			{ // NOCS (NestedBlock)
				/*
				 * Register an FSReader which only reads records of the following types.
				 * TODO: Currently, these need to be exactly these types and no subtype!
				 */
				final Collection<Class<? extends IMonitoringRecord>> recordTypeSelectorSet = new CopyOnWriteArrayList<Class<? extends IMonitoringRecord>>();
				recordTypeSelectorSet.add(OperationExecutionRecord.class);
				recordTypeSelectorSet.add(BeforeOperationEvent.class);
				recordTypeSelectorSet.add(AfterOperationEvent.class);
				recordTypeSelectorSet.add(AfterOperationFailedEvent.class);
				recordTypeSelectorSet.add(CallOperationEvent.class);
				recordTypeSelectorSet.add(SplitEvent.class);
				recordTypeSelectorSet.add(Trace.class);
				final Configuration conf = new Configuration(null);
				conf.setProperty(FSReader.CONFIG_INPUTDIRS, Configuration.toProperty(TraceAnalysisTool.inputDirs));

				final Collection<String> recordTypeSelectorNameSet = new CopyOnWriteArrayList<String>();
				for (final Class<? extends IMonitoringRecord> c : recordTypeSelectorSet) {
					recordTypeSelectorNameSet.add(c.getName());
				}
				conf.setProperty(FSReader.CONFIG_ONLYRECORDS,
						Configuration.toProperty(recordTypeSelectorNameSet.toArray(new String[recordTypeSelectorNameSet.size()])));
				reader = new FSReader(conf);
				analysisInstance.registerReader(reader);
			}

			/*
			 * This map can be used within the constructor for all following plugins which use the repository with the name defined in the
			 * AbstractTraceAnalysisPlugin.
			 */
			analysisInstance.registerRepository(TraceAnalysisTool.SYSTEM_ENTITY_FACTORY);

			final Configuration execRecTransformerConfig = new Configuration();
			execRecTransformerConfig.setProperty(AbstractPlugin.CONFIG_NAME, Constants.EXEC_TRACE_RECONSTR_COMPONENT_NAME);
			final ExecutionRecordTransformationFilter execRecTransformer =
					new ExecutionRecordTransformationFilter(execRecTransformerConfig);
			analysisInstance.registerFilter(execRecTransformer);
			/* Make sure that the execRecTransformer gets the output from the reader! */
			analysisInstance.connect(reader, FSReader.OUTPUT_PORT_NAME, execRecTransformer, ExecutionRecordTransformationFilter.INPUT_PORT_NAME);
			analysisInstance.connect(execRecTransformer, AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, TraceAnalysisTool.SYSTEM_ENTITY_FACTORY);

			/* Create the configuration for both timestamp filters (which will be connected differently!) */
			final Configuration configTimestampFilter = new Configuration();
			final Configuration configTimestampFilterFlow = new Configuration();
			configTimestampFilter.setProperty(TimestampFilter.CONFIG_IGNORE_EXECUTIONS_BEFORE_TIMESTAMP,
					Long.toString(TraceAnalysisTool.ignoreExecutionsBeforeTimestamp));
			configTimestampFilterFlow.setProperty(kieker.analysis.filter.TimestampFilter.CONFIG_IGNORE_EXECUTIONS_BEFORE_TIMESTAMP,
					Long.toString(TraceAnalysisTool.ignoreExecutionsBeforeTimestamp));
			configTimestampFilter.setProperty(TimestampFilter.CONFIG_IGNORE_EXECUTIONS_AFTER_TIMESTAMP,
					Long.toString(TraceAnalysisTool.ignoreExecutionsAfterTimestamp));
			configTimestampFilterFlow.setProperty(kieker.analysis.filter.TimestampFilter.CONFIG_IGNORE_EXECUTIONS_AFTER_TIMESTAMP,
					Long.toString(TraceAnalysisTool.ignoreExecutionsAfterTimestamp));

			final TimestampFilter executionFilterByTimestamp = new TimestampFilter(configTimestampFilter);
			analysisInstance.registerFilter(executionFilterByTimestamp);
			analysisInstance.connect(execRecTransformer, ExecutionRecordTransformationFilter.OUTPUT_PORT_NAME,
					executionFilterByTimestamp, TimestampFilter.INPUT_PORT_NAME);

			final kieker.analysis.filter.TimestampFilter timestampFilterFlow =
					new kieker.analysis.filter.TimestampFilter(configTimestampFilterFlow);
			analysisInstance.registerFilter(timestampFilterFlow);
			analysisInstance.connect(reader, FSReader.OUTPUT_PORT_NAME, timestampFilterFlow,
					kieker.analysis.filter.TimestampFilter.INPUT_PORT_NAME);

			/* Create the configuration for both trace ID filters (which will be connected differently!) */
			final Configuration configTraceIdFilter = new Configuration();
			final Configuration configTraceIdFilterFlow = new Configuration();
			if (TraceAnalysisTool.selectedTraces == null) {
				configTraceIdFilter.setProperty(TraceIdFilter.CONFIG_SELECT_ALL_TRACES, Boolean.TRUE.toString());
				configTraceIdFilterFlow.setProperty(kieker.analysis.filter.trace.TraceIdFilter.CONFIG_SELECT_ALL_TRACES, Boolean.TRUE.toString());
			} else {
				configTraceIdFilter.setProperty(TraceIdFilter.CONFIG_SELECT_ALL_TRACES, Boolean.FALSE.toString());
				configTraceIdFilterFlow.setProperty(kieker.analysis.filter.trace.TraceIdFilter.CONFIG_SELECT_ALL_TRACES, Boolean.FALSE.toString());
				configTraceIdFilter.setProperty(TraceIdFilter.CONFIG_SELECTED_TRACES,
						Configuration.toProperty(TraceAnalysisTool.selectedTraces.toArray(new Long[] {})));
				configTraceIdFilterFlow.setProperty(kieker.analysis.filter.trace.TraceIdFilter.CONFIG_SELECTED_TRACES,
						Configuration.toProperty(TraceAnalysisTool.selectedTraces.toArray(new Long[] {})));
			}
			final TraceIdFilter executionFilterByTraceId = new TraceIdFilter(configTraceIdFilter);
			analysisInstance.registerFilter(executionFilterByTraceId);
			analysisInstance.connect(executionFilterByTimestamp, TimestampFilter.OUTPUT_PORT_NAME, executionFilterByTraceId, TraceIdFilter.INPUT_PORT_NAME);

			final kieker.analysis.filter.trace.TraceIdFilter traceIdFilterFlow =
					new kieker.analysis.filter.trace.TraceIdFilter(configTraceIdFilterFlow);
			analysisInstance.registerFilter(traceIdFilterFlow);
			analysisInstance.connect(timestampFilterFlow, kieker.analysis.filter.TimestampFilter.OUTPUT_PORT_NAME,
					traceIdFilterFlow, kieker.analysis.filter.trace.TraceIdFilter.INPUT_PORT_NAME);

			final Configuration mtReconstrFilterConfig = new Configuration();
			mtReconstrFilterConfig.setProperty(AbstractPlugin.CONFIG_NAME, Constants.TRACERECONSTR_COMPONENT_NAME);
			mtReconstrFilterConfig.setProperty(TraceReconstructionFilter.CONFIG_MAX_TRACE_DURATION_MILLIS,
					Integer.toString(TraceAnalysisTool.maxTraceDurationMillis));
			mtReconstrFilterConfig.setProperty(TraceReconstructionFilter.CONFIG_IGNORE_INVALID_TRACES, Boolean.toString(TraceAnalysisTool.ignoreInvalidTraces));
			mtReconstrFilter = new TraceReconstructionFilter(mtReconstrFilterConfig);
			analysisInstance.registerFilter(mtReconstrFilter);
			analysisInstance.connect(mtReconstrFilter, AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, TraceAnalysisTool.SYSTEM_ENTITY_FACTORY);
			analysisInstance.connect(executionFilterByTraceId, TraceIdFilter.OUTPUT_PORT_NAME,
					mtReconstrFilter, TraceReconstructionFilter.INPUT_PORT_NAME);

			final Configuration configurationEventRecordTraceGenerationFilter = new Configuration();
			configurationEventRecordTraceGenerationFilter.setProperty(AbstractPlugin.CONFIG_NAME,
					Constants.EVENTRECORDTRACERECONSTR_COMPONENT_NAME);
			configurationEventRecordTraceGenerationFilter.setProperty(EventRecordTraceGenerationFilter.CONFIG_MAX_TRACE_DURATION_MILLIS,
					Integer.toString(TraceAnalysisTool.maxTraceDurationMillis));
			eventRecordTraceGenerationFilter = new EventRecordTraceGenerationFilter(configurationEventRecordTraceGenerationFilter);
			analysisInstance.registerFilter(eventRecordTraceGenerationFilter);
			analysisInstance.connect(traceIdFilterFlow, kieker.analysis.filter.trace.TraceIdFilter.OUTPUT_PORT_NAME,
					eventRecordTraceGenerationFilter, EventRecordTraceGenerationFilter.INPUT_PORT_NAME);
			analysisInstance.connect(eventRecordTraceGenerationFilter, AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME,
					TraceAnalysisTool.SYSTEM_ENTITY_FACTORY);

			final Configuration configurationEventTrace2ExecutionTraceFilter = new Configuration();
			configurationEventTrace2ExecutionTraceFilter.setProperty(AbstractPlugin.CONFIG_NAME,
					Constants.EXECTRACESFROMEVENTTRACES_COMPONENT_NAME);
			// EventTrace2ExecutionTraceFilter has no configuration properties
			eventTrace2ExecutionTraceFilter = new EventTrace2ExecutionTraceFilter(configurationEventTrace2ExecutionTraceFilter);
			analysisInstance.registerFilter(eventTrace2ExecutionTraceFilter);
			analysisInstance.connect(eventRecordTraceGenerationFilter, EventRecordTraceGenerationFilter.OUTPUT_PORT_NAME,
					eventTrace2ExecutionTraceFilter, EventTrace2ExecutionTraceFilter.INPUT_PORT_NAME);
			analysisInstance.connect(eventTrace2ExecutionTraceFilter, AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME,
					TraceAnalysisTool.SYSTEM_ENTITY_FACTORY);

			final List<AbstractTraceProcessingPlugin> allTraceProcessingComponents = new ArrayList<AbstractTraceProcessingPlugin>();

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
				analysisInstance.connect(traceAllocationEquivClassFilter, AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME,
						TraceAnalysisTool.SYSTEM_ENTITY_FACTORY);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
						traceAllocationEquivClassFilter, TraceEquivalenceClassFilter.EXECUTION_TRACES_INPUT_PORT_NAME);
				analysisInstance.connect(eventTrace2ExecutionTraceFilter, EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
						traceAllocationEquivClassFilter, TraceEquivalenceClassFilter.EXECUTION_TRACES_INPUT_PORT_NAME);
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
						traceAssemblyEquivClassFilter, TraceEquivalenceClassFilter.EXECUTION_TRACES_INPUT_PORT_NAME);
				analysisInstance.connect(eventTrace2ExecutionTraceFilter, EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
						traceAssemblyEquivClassFilter, TraceEquivalenceClassFilter.EXECUTION_TRACES_INPUT_PORT_NAME);
				analysisInstance.connect(traceAssemblyEquivClassFilter, AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME,
						TraceAnalysisTool.SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(traceAssemblyEquivClassFilter);
			}

			// fill list of msgTraceProcessingComponents:
			MessageTraceWriterPlugin componentPrintMsgTrace = null;
			if (TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PRINTMSGTRACES)) {
				numRequestedTasks++;
				final Configuration componentPrintMsgTraceConfig = new Configuration();
				componentPrintMsgTraceConfig.setProperty(AbstractPlugin.CONFIG_NAME, Constants.PRINTMSGTRACE_COMPONENT_NAME);
				componentPrintMsgTraceConfig.setProperty(MessageTraceWriterPlugin.CONFIG_OUTPUT_FN, new File(TraceAnalysisTool.outputDir + File.separator
						+ TraceAnalysisTool.outputFnPrefix + Constants.MESSAGE_TRACES_FN_PREFIX + ".txt") // NOPMD
						.getCanonicalPath());
				componentPrintMsgTrace = new MessageTraceWriterPlugin(componentPrintMsgTraceConfig);
				analysisInstance.registerFilter(componentPrintMsgTrace);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPrintMsgTrace, MessageTraceWriterPlugin.MSG_TRACES_INPUT_PORT_NAME);
				analysisInstance.connect(eventTrace2ExecutionTraceFilter, EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPrintMsgTrace, MessageTraceWriterPlugin.MSG_TRACES_INPUT_PORT_NAME);
				analysisInstance.connect(componentPrintMsgTrace, AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, TraceAnalysisTool.SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPrintMsgTrace);
			}
			ExecutionTraceWriterPlugin componentPrintExecTrace = null;
			if (TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PRINTEXECTRACES)) {
				numRequestedTasks++;
				final Configuration componentPrintExecTraceConfig = new Configuration();
				componentPrintExecTraceConfig.setProperty(AbstractPlugin.CONFIG_NAME, Constants.PRINTEXECTRACE_COMPONENT_NAME);
				componentPrintExecTraceConfig.setProperty(ExecutionTraceWriterPlugin.CONFIG_OUTPUT_FN, new File(TraceAnalysisTool.outputDir + File.separator
						+ TraceAnalysisTool.outputFnPrefix + Constants.EXECUTION_TRACES_FN_PREFIX + ".txt")
						.getCanonicalPath());
				componentPrintExecTrace = new ExecutionTraceWriterPlugin(componentPrintExecTraceConfig);
				analysisInstance.registerFilter(componentPrintExecTrace);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
						componentPrintExecTrace, ExecutionTraceWriterPlugin.EXECUTION_TRACES_INPUT_PORT_NAME);
				analysisInstance.connect(eventTrace2ExecutionTraceFilter, EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
						componentPrintExecTrace, ExecutionTraceWriterPlugin.EXECUTION_TRACES_INPUT_PORT_NAME);
				analysisInstance.connect(componentPrintExecTrace, AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, TraceAnalysisTool.SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPrintExecTrace);
			}
			InvalidExecutionTraceWriterPlugin componentPrintInvalidTrace = null;
			if (TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES)) {
				numRequestedTasks++;
				final Configuration componentPrintInvalidTraceConfig = new Configuration();
				componentPrintInvalidTraceConfig.setProperty(AbstractPlugin.CONFIG_NAME,
						Constants.PRINTINVALIDEXECTRACE_COMPONENT_NAME);
				componentPrintInvalidTraceConfig.setProperty(InvalidExecutionTraceWriterPlugin.CONFIG_OUTPUT_FN, new File(TraceAnalysisTool.outputDir
						+ File.separator
						+ TraceAnalysisTool.outputFnPrefix
						+ Constants.INVALID_TRACES_FN_PREFIX + ".txt").getCanonicalPath());
				componentPrintInvalidTrace = new InvalidExecutionTraceWriterPlugin(componentPrintInvalidTraceConfig);
				analysisInstance.registerFilter(componentPrintInvalidTrace);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_INVALID_EXECUTION_TRACE,
						componentPrintInvalidTrace, InvalidExecutionTraceWriterPlugin.INVALID_EXECUTION_TRACES_INPUT_PORT_NAME);
				analysisInstance.connect(componentPrintInvalidTrace, AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME,
						TraceAnalysisTool.SYSTEM_ENTITY_FACTORY);
				// TODO: We haven't such port for the EventTrace2ExecutionTraceFilter, yet
				TraceAnalysisTool.LOG.warn("EventTrace2ExecutionTraceFilter doesn't provide an output port for invalid execution traces, yet");
				// AbstractPlugin.connect(eventTrace2ExecutionTraceFilter, EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_INVALID_EXECUTION_TRACE,
				// componentPrintInvalidTrace, InvalidExecutionTraceWriterPlugin.INVALID_EXECUTION_TRACES_INPUT_PORT_NAME);
				allTraceProcessingComponents.add(componentPrintInvalidTrace);
			}
			SequenceDiagramPlugin componentPlotAllocationSeqDiagr = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONSEQDS)) {
				numRequestedTasks++;
				final Configuration componentPlotAllocationSeqDiagrConfig = new Configuration();
				componentPlotAllocationSeqDiagrConfig.setProperty(AbstractPlugin.CONFIG_NAME, Constants.PLOTALLOCATIONSEQDIAGR_COMPONENT_NAME);
				componentPlotAllocationSeqDiagrConfig.setProperty(SequenceDiagramPlugin.CONFIG_OUTPUT_FN_BASE,
						TraceAnalysisTool.outputDir + File.separator + TraceAnalysisTool.outputFnPrefix + Constants.ALLOCATION_SEQUENCE_DIAGRAM_FN_PREFIX);
				componentPlotAllocationSeqDiagrConfig.setProperty(SequenceDiagramPlugin.CONFIG_OUTPUT_SDMODE, SequenceDiagramPlugin.SDModes.ALLOCATION.toString());
				componentPlotAllocationSeqDiagrConfig.setProperty(SequenceDiagramPlugin.CONFIG_OUTPUT_SHORTLABES, Boolean.toString(TraceAnalysisTool.shortLabels));
				componentPlotAllocationSeqDiagr = new SequenceDiagramPlugin(componentPlotAllocationSeqDiagrConfig);
				analysisInstance.registerFilter(componentPlotAllocationSeqDiagr);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAllocationSeqDiagr, AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);
				analysisInstance.connect(eventTrace2ExecutionTraceFilter, EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAllocationSeqDiagr, AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);
				analysisInstance.connect(componentPlotAllocationSeqDiagr, AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME,
						TraceAnalysisTool.SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotAllocationSeqDiagr);
			}
			SequenceDiagramPlugin componentPlotAssemblySeqDiagr = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYSEQDS)) {
				numRequestedTasks++;
				final Configuration componentPlotAssemblySeqDiagrConfig = new Configuration();
				componentPlotAssemblySeqDiagrConfig.setProperty(AbstractPlugin.CONFIG_NAME, Constants.PLOTASSEMBLYSEQDIAGR_COMPONENT_NAME);
				componentPlotAssemblySeqDiagrConfig.setProperty(SequenceDiagramPlugin.CONFIG_OUTPUT_FN_BASE,
						TraceAnalysisTool.outputDir + File.separator + TraceAnalysisTool.outputFnPrefix + Constants.ASSEMBLY_SEQUENCE_DIAGRAM_FN_PREFIX);
				componentPlotAssemblySeqDiagrConfig.setProperty(SequenceDiagramPlugin.CONFIG_OUTPUT_SDMODE, SequenceDiagramPlugin.SDModes.ASSEMBLY.toString());
				componentPlotAssemblySeqDiagrConfig.setProperty(SequenceDiagramPlugin.CONFIG_OUTPUT_SHORTLABES, Boolean.toString(TraceAnalysisTool.shortLabels));
				componentPlotAssemblySeqDiagr = new SequenceDiagramPlugin(componentPlotAssemblySeqDiagrConfig);
				analysisInstance.registerFilter(componentPlotAssemblySeqDiagr);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblySeqDiagr, AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);
				analysisInstance.connect(eventTrace2ExecutionTraceFilter, EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblySeqDiagr, AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);
				analysisInstance.connect(componentPlotAssemblySeqDiagr, AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME,
						TraceAnalysisTool.SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotAssemblySeqDiagr);
			}
			ComponentDependencyGraphPluginAllocation componentPlotAllocationComponentDepGraph = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONCOMPONENTDEPG)) {
				numRequestedTasks++;
				final Configuration componentPlotAllocationComponentDepGraphConfig = new Configuration();
				componentPlotAllocationComponentDepGraphConfig.setProperty(AbstractPlugin.CONFIG_NAME,
						Constants.PLOTALLOCATIONCOMPONENTDEPGRAPH_COMPONENT_NAME);
				componentPlotAllocationComponentDepGraphConfig.setProperty(ComponentDependencyGraphPluginAllocation.CONFIG_OUTPUT_FN_BASE,
						TraceAnalysisTool.outputDir + File.separator + TraceAnalysisTool.outputFnPrefix + Constants.ALLOCATION_COMPONENT_DEPENDENCY_GRAPH_FN_PREFIX);
				componentPlotAllocationComponentDepGraphConfig.setProperty(ComponentDependencyGraphPluginAllocation.CONFIG_INCLUDE_WEIGHTS, Boolean.TRUE.toString());
				componentPlotAllocationComponentDepGraphConfig.setProperty(ComponentDependencyGraphPluginAllocation.CONFIG_SHORTLABELS,
						Boolean.toString(TraceAnalysisTool.shortLabels));
				componentPlotAllocationComponentDepGraphConfig.setProperty(ComponentDependencyGraphPluginAllocation.CONFIG_SELFLOOPS,
						Boolean.toString(TraceAnalysisTool.includeSelfLoops));
				componentPlotAllocationComponentDepGraph = new ComponentDependencyGraphPluginAllocation(
						componentPlotAllocationComponentDepGraphConfig);
				analysisInstance.registerFilter(componentPlotAllocationComponentDepGraph);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAllocationComponentDepGraph, AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);
				analysisInstance.connect(eventTrace2ExecutionTraceFilter, EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAllocationComponentDepGraph, AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);
				analysisInstance.connect(componentPlotAllocationComponentDepGraph, AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME,
						TraceAnalysisTool.SYSTEM_ENTITY_FACTORY);

				allTraceProcessingComponents.add(componentPlotAllocationComponentDepGraph);
			}
			ComponentDependencyGraphPluginAssembly componentPlotAssemblyComponentDepGraph = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYCOMPONENTDEPG)) {
				numRequestedTasks++;
				final Configuration componentPlotAssemblyComponentDepGraphConfig = new Configuration();
				componentPlotAssemblyComponentDepGraphConfig.setProperty(AbstractPlugin.CONFIG_NAME,
						Constants.PLOTASSEMBLYCOMPONENTDEPGRAPH_COMPONENT_NAME);
				componentPlotAssemblyComponentDepGraphConfig.setProperty(ComponentDependencyGraphPluginAssembly.CONFIG_SHORT_LABELS,
						Boolean.toString(TraceAnalysisTool.shortLabels));
				componentPlotAssemblyComponentDepGraphConfig.setProperty(ComponentDependencyGraphPluginAssembly.CONFIG_INCLUDE_WEIGHTS, Boolean.toString(true));
				componentPlotAssemblyComponentDepGraphConfig.setProperty(ComponentDependencyGraphPluginAssembly.CONFIG_INCLUDE_SELF_LOOPS,
						Boolean.toString(TraceAnalysisTool.includeSelfLoops));
				componentPlotAssemblyComponentDepGraphConfig.setProperty(ComponentDependencyGraphPluginAssembly.CONFIG_DOT_OUTPUT_FILE, new File(
						TraceAnalysisTool.outputDir
								+ File.separator + TraceAnalysisTool.outputFnPrefix
								+ Constants.ASSEMBLY_COMPONENT_DEPENDENCY_GRAPH_FN_PREFIX).getAbsolutePath());

				componentPlotAssemblyComponentDepGraph = new ComponentDependencyGraphPluginAssembly(componentPlotAssemblyComponentDepGraphConfig);

				final String[] nodeDecorations = TraceAnalysisTool.cmdl.getOptionValues(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYCOMPONENTDEPG);
				TraceAnalysisTool.addDecorators(nodeDecorations, componentPlotAssemblyComponentDepGraph);

				analysisInstance.registerFilter(componentPlotAssemblyComponentDepGraph);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblyComponentDepGraph, AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);
				analysisInstance.connect(eventTrace2ExecutionTraceFilter, EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblyComponentDepGraph, AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);
				analysisInstance.connect(componentPlotAssemblyComponentDepGraph, AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME,
						TraceAnalysisTool.SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotAssemblyComponentDepGraph);
			}
			ContainerDependencyGraphPlugin componentPlotContainerDepGraph = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG)) {
				numRequestedTasks++;
				final Configuration componentPlotContainerDepGraphConfig = new Configuration();
				componentPlotContainerDepGraphConfig.setProperty(AbstractPlugin.CONFIG_NAME,
						Constants.PLOTCONTAINERDEPGRAPH_COMPONENT_NAME);
				componentPlotContainerDepGraphConfig.setProperty(ContainerDependencyGraphPlugin.CONFIG_SHORT_LABELS,
						Boolean.toString(TraceAnalysisTool.shortLabels));
				componentPlotContainerDepGraphConfig.setProperty(ContainerDependencyGraphPlugin.CONFIG_INCLUDE_WEIGHTS, Boolean.toString(true));
				componentPlotContainerDepGraphConfig.setProperty(ContainerDependencyGraphPlugin.CONFIG_INCLUDE_SELF_LOOPS,
						Boolean.toString(TraceAnalysisTool.includeSelfLoops));
				componentPlotContainerDepGraphConfig.setProperty(ContainerDependencyGraphPlugin.CONFIG_DOT_OUTPUT_FILE, new File(TraceAnalysisTool.outputDir
						+ File.separator + TraceAnalysisTool.outputFnPrefix
						+ Constants.CONTAINER_DEPENDENCY_GRAPH_FN_PREFIX).getAbsolutePath());

				componentPlotContainerDepGraph = new ContainerDependencyGraphPlugin(componentPlotContainerDepGraphConfig);
				analysisInstance.registerFilter(componentPlotContainerDepGraph);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotContainerDepGraph, AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);
				analysisInstance.connect(eventTrace2ExecutionTraceFilter, EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotContainerDepGraph, AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);
				analysisInstance.connect(componentPlotContainerDepGraph, AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME,
						TraceAnalysisTool.SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotContainerDepGraph);
			}
			OperationDependencyGraphPluginAllocation componentPlotAllocationOperationDepGraph = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONOPERATIONDEPG)) {
				numRequestedTasks++;

				final Configuration componentPlotAllocationOperationDepGraphConfig = new Configuration();
				componentPlotAllocationOperationDepGraphConfig
						.setProperty(AbstractPlugin.CONFIG_NAME, Constants.PLOTALLOCATIONOPERATIONDEPGRAPH_COMPONENT_NAME);
				componentPlotAllocationOperationDepGraphConfig
						.setProperty(OperationDependencyGraphPluginAllocation.CONFIG_SHORT_LABELS, Boolean.toString(TraceAnalysisTool.shortLabels));
				componentPlotAllocationOperationDepGraphConfig.setProperty(OperationDependencyGraphPluginAllocation.CONFIG_INCLUDE_WEIGHTS, Boolean.toString(true));
				componentPlotAllocationOperationDepGraphConfig.setProperty(OperationDependencyGraphPluginAllocation.CONFIG_INCLUDE_SELF_LOOPS,
						Boolean.toString(TraceAnalysisTool.includeSelfLoops));
				componentPlotAllocationOperationDepGraphConfig.setProperty(OperationDependencyGraphPluginAllocation.CONFIG_DOT_OUTPUT_FILE, new File(
						TraceAnalysisTool.outputDir + File.separator + TraceAnalysisTool.outputFnPrefix
								+ Constants.ALLOCATION_OPERATION_DEPENDENCY_GRAPH_FN_PREFIX).getAbsolutePath());

				componentPlotAllocationOperationDepGraph = new OperationDependencyGraphPluginAllocation(componentPlotAllocationOperationDepGraphConfig);
				analysisInstance.registerFilter(componentPlotAllocationOperationDepGraph);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAllocationOperationDepGraph, AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);
				analysisInstance.connect(eventTrace2ExecutionTraceFilter, EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAllocationOperationDepGraph, AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);
				analysisInstance.connect(componentPlotAllocationOperationDepGraph, AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME,
						TraceAnalysisTool.SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotAllocationOperationDepGraph);
			}
			OperationDependencyGraphPluginAssembly componentPlotAssemblyOperationDepGraph = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYOPERATIONDEPG)) {
				numRequestedTasks++;

				final Configuration componentPlotAssemblyOperationDepGraphConfig = new Configuration();
				componentPlotAssemblyOperationDepGraphConfig
						.setProperty(AbstractPlugin.CONFIG_NAME, Constants.PLOTASSEMBLYOPERATIONDEPGRAPH_COMPONENT_NAME);
				componentPlotAssemblyOperationDepGraphConfig.setProperty(ComponentDependencyGraphPluginAssembly.CONFIG_SHORT_LABELS,
						Boolean.toString(TraceAnalysisTool.shortLabels));
				componentPlotAssemblyOperationDepGraphConfig.setProperty(OperationDependencyGraphPluginAssembly.CONFIG_INCLUDE_WEIGHTS, Boolean.toString(true));
				componentPlotAssemblyOperationDepGraphConfig.setProperty(OperationDependencyGraphPluginAssembly.CONFIG_INCLUDE_SELF_LOOPS,
						Boolean.toString(TraceAnalysisTool.includeSelfLoops));
				componentPlotAssemblyOperationDepGraphConfig.setProperty(OperationDependencyGraphPluginAssembly.CONFIG_DOT_OUTPUT_FILE, new File(
						TraceAnalysisTool.outputDir
								+ File.separator + TraceAnalysisTool.outputFnPrefix
								+ Constants.ASSEMBLY_OPERATION_DEPENDENCY_GRAPH_FN_PREFIX).getAbsolutePath());

				componentPlotAssemblyOperationDepGraph = new OperationDependencyGraphPluginAssembly(componentPlotAssemblyOperationDepGraphConfig);
				final String[] nodeDecorations = TraceAnalysisTool.cmdl.getOptionValues(Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYOPERATIONDEPG);
				TraceAnalysisTool.addDecorators(nodeDecorations, componentPlotAssemblyOperationDepGraph);

				analysisInstance.registerFilter(componentPlotAssemblyOperationDepGraph);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblyOperationDepGraph, AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);
				analysisInstance.connect(eventTrace2ExecutionTraceFilter, EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblyOperationDepGraph, AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);
				analysisInstance.connect(componentPlotAssemblyOperationDepGraph, AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME,
						TraceAnalysisTool.SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotAssemblyOperationDepGraph);
			}
			TraceCallTreePlugin componentPlotTraceCallTrees = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTCALLTREES)) {
				numRequestedTasks++;
				final Configuration componentPlotTraceCallTreesConfig = new Configuration();
				componentPlotTraceCallTreesConfig.setProperty(AbstractPlugin.CONFIG_NAME, Constants.PLOTCALLTREE_COMPONENT_NAME);
				componentPlotTraceCallTrees = new TraceCallTreePlugin(componentPlotTraceCallTreesConfig,
						TraceAnalysisTool.ALLOCATION_COMPONENT_OPERATION_PAIR_FACTORY, new File(TraceAnalysisTool.outputDir
								+ File.separator + TraceAnalysisTool.outputFnPrefix + Constants.CALL_TREE_FN_PREFIX).getCanonicalPath(),
						TraceAnalysisTool.shortLabels);
				analysisInstance.registerFilter(componentPlotTraceCallTrees);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotTraceCallTrees, AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);
				analysisInstance.connect(eventTrace2ExecutionTraceFilter, EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotTraceCallTrees, AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);
				analysisInstance.connect(componentPlotTraceCallTrees, AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME,
						TraceAnalysisTool.SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotTraceCallTrees);
			}
			AggregatedAllocationComponentOperationCallTreePlugin componentPlotAggregatedCallTree = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTAGGREGATEDALLOCATIONCALLTREE)) {
				numRequestedTasks++;
				final Configuration componentPlotAggregatedCallTreeConfig = new Configuration();
				componentPlotAggregatedCallTreeConfig
						.setProperty(AbstractPlugin.CONFIG_NAME,
								Constants.PLOTAGGREGATEDALLOCATIONCALLTREE_COMPONENT_NAME);
				componentPlotAggregatedCallTreeConfig.setProperty(AggregatedCallTreePlugin.CONFIG_INCLUDE_WEIGHTS, Boolean.toString(true));
				componentPlotAggregatedCallTreeConfig.setProperty(AggregatedCallTreePlugin.CONFIG_SHORT_LABELS, Boolean.toString(TraceAnalysisTool.shortLabels));
				componentPlotAggregatedCallTree = new AggregatedAllocationComponentOperationCallTreePlugin(componentPlotAggregatedCallTreeConfig);
				componentPlotAggregatedCallTree.setAllocationComponentOperationPairFactory(TraceAnalysisTool.ALLOCATION_COMPONENT_OPERATION_PAIR_FACTORY);
				componentPlotAggregatedCallTree.setDotOutputFile(new File(TraceAnalysisTool.outputDir + File.separator + TraceAnalysisTool.outputFnPrefix
						+ Constants.AGGREGATED_ALLOCATION_CALL_TREE_FN_PREFIX));
				analysisInstance.registerFilter(componentPlotAggregatedCallTree);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAggregatedCallTree, AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);
				analysisInstance.connect(eventTrace2ExecutionTraceFilter, EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAggregatedCallTree, AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);
				analysisInstance.connect(componentPlotAggregatedCallTree, AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME,
						TraceAnalysisTool.SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotAggregatedCallTree);
			}
			AggregatedAssemblyComponentOperationCallTreePlugin componentPlotAssemblyCallTree = null;
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTAGGREGATEDASSEMBLYCALLTREE)) {
				numRequestedTasks++;
				final Configuration componentPlotAssemblyCallTreeConfig = new Configuration();
				componentPlotAssemblyCallTreeConfig.setProperty(AbstractPlugin.CONFIG_NAME,
						Constants.PLOTAGGREGATEDASSEMBLYCALLTREE_COMPONENT_NAME);
				componentPlotAssemblyCallTreeConfig.setProperty(AggregatedCallTreePlugin.CONFIG_INCLUDE_WEIGHTS, Boolean.toString(true));
				componentPlotAssemblyCallTreeConfig.setProperty(AggregatedCallTreePlugin.CONFIG_SHORT_LABELS, Boolean.toString(TraceAnalysisTool.shortLabels));
				componentPlotAssemblyCallTree = new AggregatedAssemblyComponentOperationCallTreePlugin(componentPlotAssemblyCallTreeConfig);
				componentPlotAssemblyCallTree.setAssemblyComponentOperationPairFactory(TraceAnalysisTool.ASSEMBLY_COMPONENT_OPERATION_PAIR_FACTORY);
				componentPlotAssemblyCallTree.setDotOutputFile(new File(TraceAnalysisTool.outputDir
						+ File.separator + TraceAnalysisTool.outputFnPrefix + Constants.AGGREGATED_ASSEMBLY_CALL_TREE_FN_PREFIX));
				analysisInstance.registerFilter(componentPlotAssemblyCallTree);
				analysisInstance.connect(mtReconstrFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblyCallTree, AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);
				analysisInstance.connect(eventTrace2ExecutionTraceFilter, EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
						componentPlotAssemblyCallTree, AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);
				analysisInstance.connect(componentPlotAssemblyCallTree, AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME,
						TraceAnalysisTool.SYSTEM_ENTITY_FACTORY);
				allTraceProcessingComponents.add(componentPlotAssemblyCallTree);
			}
			if (retVal && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_ALLOCATIONEQUIVCLASSREPORT)) {
				numRequestedTasks++;
				// the actual execution of the task is performed below
			}

			if (numRequestedTasks == 0) {
				TraceAnalysisTool.LOG.warn("No task requested");
				TraceAnalysisTool.printUsage();
				System.err.println("");
				System.err.println("No task requested");
				System.err.println("");
				return false;
			}

			int numErrorCount = 0;
			try {
				analysisInstance.run();
			} catch (final Exception ex) { // NOPMD // NOCS (FindBugs reports that Exception is never thrown; but wontfix (#44)!)
				throw new Exception("Error occured while running analysis", ex);
			} finally {
				for (final AbstractTraceProcessingPlugin c : allTraceProcessingComponents) {
					numErrorCount += c.getErrorCount();
					c.printStatusMessage();
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

			// TODO: turn into plugin with output code in terminate(..) method
			// See ticket http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/170
			final String systemEntitiesHtmlFn = new File(TraceAnalysisTool.outputDir + File.separator + TraceAnalysisTool.outputFnPrefix + "system-entities")
					.getAbsolutePath();
			TraceAnalysisTool.SYSTEM_ENTITY_FACTORY.saveSystemToHTMLFile(systemEntitiesHtmlFn);
			System.out.println("");
			System.out.println("#");
			System.out.println("# Plugin: " + "System Entity Factory");
			System.out.println("Wrote table of system entities to file '" + systemEntitiesHtmlFn + ".html'");
			if (!retVal) {
				System.err.println("A task failed");
			}
		} catch (final Exception ex) { // NOCS (IllegalCatchCheck) // NOPMD
			System.err.println("An error occured: " + ex.getMessage());
			System.err.println("");
			TraceAnalysisTool.LOG.error("Exception", ex);
			retVal = false;
		} finally {
			if (numRequestedTasks > 0) {
				if (mtReconstrFilter != null) {
					mtReconstrFilter.printStatusMessage();
				}
				if (eventRecordTraceGenerationFilter != null) {
					eventRecordTraceGenerationFilter.printStatusMessage();
				}
				if (eventTrace2ExecutionTraceFilter != null) {
					eventTrace2ExecutionTraceFilter.printStatusMessage();
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

		} catch (final Exception exc) { // NOCS (IllegalCatchCheck) // NOPMD
			System.err.println("An error occured. See 'kieker.log' for details");
			TraceAnalysisTool.LOG.error(Arrays.toString(args), exc);
		}
	}

	private static boolean writeTraceEquivalenceReport(final String outputFnPrefixL, final TraceEquivalenceClassFilter traceEquivFilter) throws IOException {
		boolean retVal = true;
		final String outputFn = new File(outputFnPrefixL).getCanonicalPath();
		PrintStream ps = null;
		try {
			ps = new PrintStream(new FileOutputStream(outputFn), false, TraceAnalysisTool.ENCODING);
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
			TraceAnalysisTool.LOG.error("File not found", e);
			retVal = false;
		} finally {
			if (ps != null) {
				ps.close();
			}
		}

		return retVal;
	}
}
