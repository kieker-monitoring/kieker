package kieker.tools.traceAnalysis;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2010 Kieker Project
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
 * ==================================================
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.Vector;
import java.util.Map.Entry;

import kieker.common.util.LoggingTimestampConverter;
import kieker.analysis.AnalysisInstance;
import kieker.analysis.datamodel.Execution;
import kieker.analysis.datamodel.ExecutionTrace;
import kieker.analysis.datamodel.repository.AllocationComponentOperationPairFactory;
import kieker.analysis.datamodel.repository.SystemModelRepository;
import kieker.analysis.plugin.IAnalysisPlugin;
import kieker.analysis.plugin.traceAnalysis.AbstractExecutionTraceProcessingPlugin;
import kieker.analysis.plugin.traceAnalysis.AbstractInvalidExecutionTraceProcessingPlugin;
import kieker.analysis.plugin.traceAnalysis.AbstractMessageTraceProcessingPlugin;
import kieker.analysis.plugin.traceAnalysis.AbstractTraceProcessingPlugin;
import kieker.analysis.plugin.traceAnalysis.executionFilter.TraceIdFilter;
import kieker.analysis.plugin.traceAnalysis.executionFilter.TimestampFilter;
import kieker.analysis.plugin.traceAnalysis.executionRecordTransformation.ExecutionRecordTransformationPlugin;
import kieker.analysis.plugin.traceAnalysis.traceFilter.TraceEquivalenceClassFilter;
import kieker.analysis.plugin.traceAnalysis.traceFilter.TraceEquivalenceClassFilter.TraceEquivalenceClassModes;
import kieker.analysis.plugin.traceAnalysis.traceReconstruction.TraceReconstructionPlugin;
import kieker.analysis.plugin.traceAnalysis.traceWriter.ExecutionTraceWriterPlugin;
import kieker.analysis.plugin.traceAnalysis.traceWriter.InvalidExecutionTraceWriterPlugin;
import kieker.analysis.plugin.traceAnalysis.traceWriter.MessageTraceWriterPlugin;
import kieker.analysis.plugin.traceAnalysis.visualization.callTree.AggregatedAllocationComponentOperationCallTreePlugin;
import kieker.analysis.plugin.traceAnalysis.visualization.callTree.TraceCallTreePlugin;
import kieker.analysis.plugin.traceAnalysis.visualization.dependencyGraph.ComponentDependencyGraphPlugin;
import kieker.analysis.plugin.traceAnalysis.visualization.dependencyGraph.ContainerDependencyGraphPlugin;
import kieker.analysis.plugin.traceAnalysis.visualization.dependencyGraph.OperationDependencyGraphPlugin;
import kieker.analysis.plugin.traceAnalysis.visualization.sequenceDiagram.SequenceDiagramPlugin;
import kieker.analysis.reader.filesystem.FSReader;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 * @author Andre van Hoorn, Matthias Rohr
 */
public class TraceAnalysisTool {

    private static final Log log = LogFactory.getLog(TraceAnalysisTool.class);
    private static final SystemModelRepository systemEntityFactory = new SystemModelRepository();
    private static final AllocationComponentOperationPairFactory allocationComponentOperationPairFactory = new AllocationComponentOperationPairFactory(
            TraceAnalysisTool.systemEntityFactory);
    private static CommandLine cmdl = null;
    private static final CommandLineParser cmdlParser = new BasicParser();
    private static String[] inputDirs = null;
    private static String outputDir = null;
    private static String outputFnPrefix = null;
    private static TreeSet<Long> selectedTraces = null; // null means select all
    private static TraceEquivalenceClassModes traceEquivalenceClassMode = TraceEquivalenceClassModes.DISABLED;
    private static boolean shortLabels = true;
    private static boolean includeSelfLoops = false;
    private static boolean ignoreInvalidTraces = false;
    private static int maxTraceDurationMillis = TraceReconstructionPlugin.MAX_DURATION_MILLIS; // infinite
    private static long ignoreExecutionsBeforeTimestamp = TimestampFilter.MIN_TIMESTAMP;
    private static long ignoreExecutionsAfterTimestamp = TimestampFilter.MAX_TIMESTAMP;
    public static final String DATE_FORMAT_PATTERN_CMD_USAGE_HELP = Constants.DATE_FORMAT_PATTERN.replaceAll("'", ""); // only for usage info
    // private static final String CMD_OPT_NAME_TASK_INITJMSREADER =
    // "init-basic-JMS-reader";
    // private static final String CMD_OPT_NAME_TASK_INITJMSREADERJFX =
    // "init-basic-JMS-readerJavaFx";

    private static boolean parseArgs(final String[] args) {
        try {
            TraceAnalysisTool.cmdl = TraceAnalysisTool.cmdlParser.parse(
                    Constants.CMDL_OPTIONS, args);
        } catch (final ParseException e) {
            TraceAnalysisTool.printUsage();
            System.err.println("\nError parsing arguments: " + e.getMessage());
            return false;
        }
        return true;
    }

    private static void printUsage() {
        Constants.CMD_HELP_FORMATTER.printHelp(80,
                TraceAnalysisTool.class.getName(), "",
                Constants.CMDL_OPTIONS, "", true);
    }

    private static boolean initFromArgs() {
        TraceAnalysisTool.inputDirs = TraceAnalysisTool.cmdl.getOptionValues(Constants.CMD_OPT_NAME_INPUTDIRS);

        TraceAnalysisTool.outputDir = TraceAnalysisTool.cmdl.getOptionValue(Constants.CMD_OPT_NAME_OUTPUTDIR)
                + File.separator;
        TraceAnalysisTool.outputFnPrefix = TraceAnalysisTool.cmdl.getOptionValue(Constants.CMD_OPT_NAME_OUTPUTFNPREFIX, "");
        if (TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_SELECTTRACES)) { /*
             * Parse
             * list of
             * trace Ids
             */
            final String[] traceIdList = TraceAnalysisTool.cmdl.getOptionValues(Constants.CMD_OPT_NAME_SELECTTRACES);
            TraceAnalysisTool.selectedTraces = new TreeSet<Long>();
            final int numSelectedTraces = traceIdList.length;
            try {
                for (final String idStr : traceIdList) {
                    TraceAnalysisTool.selectedTraces.add(Long.valueOf(idStr));
                }
                TraceAnalysisTool.log.info(numSelectedTraces + " trace"
                        + (numSelectedTraces > 1 ? "s" : "") + " selected");
            } catch (final Exception e) {
                System.err.println("\nFailed to parse list of trace IDs: "
                        + traceIdList + "(" + e.getMessage() + ")");
                TraceAnalysisTool.log.error(
                        "Failed to parse list of trace IDs: " + traceIdList, e);
                return false;
            }
        }

        TraceAnalysisTool.shortLabels = TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_SHORTLABELS);
        TraceAnalysisTool.ignoreInvalidTraces = TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_IGNOREINVALIDTRACES);

        final String traceEquivClassModeStr = TraceAnalysisTool.cmdl.getOptionValue(Constants.CMD_OPT_NAME_TRACEEQUIVCLASSMODE,
                null);
        if (traceEquivClassModeStr == null
                || traceEquivClassModeStr.equals(Constants.TRACE_EQUIVALENCE_MODE_STR_DISABLED)) {
            TraceAnalysisTool.traceEquivalenceClassMode = TraceEquivalenceClassModes.DISABLED;
        } else {
            if (traceEquivClassModeStr.equals(Constants.TRACE_EQUIVALENCE_MODE_STR_ALLOCATION)) {
                TraceAnalysisTool.traceEquivalenceClassMode = TraceEquivalenceClassModes.ALLOCATION;
            } else if (traceEquivClassModeStr.equals(Constants.TRACE_EQUIVALENCE_MODE_STR_ASSEMBLY)) {
                TraceAnalysisTool.traceEquivalenceClassMode = TraceEquivalenceClassModes.ASSEMBLY;
            } else {
                TraceAnalysisTool.log.error("Invalid value for property "
                        + Constants.CMD_OPT_NAME_TRACEEQUIVCLASSMODE + ":"
                        + traceEquivClassModeStr);
                return false;
            }
        }

        final String maxTraceDurationStr = TraceAnalysisTool.cmdl.getOptionValue(Constants.CMD_OPT_NAME_MAXTRACEDURATION,
                TraceAnalysisTool.maxTraceDurationMillis + "");
        try {
            TraceAnalysisTool.maxTraceDurationMillis = Integer.parseInt(maxTraceDurationStr);
        } catch (final NumberFormatException exc) {
            System.err.println("\nFailed to parse int value of property "
                    + Constants.CMD_OPT_NAME_MAXTRACEDURATION
                    + " (must be an integer): " + maxTraceDurationStr);
            TraceAnalysisTool.log.error(
                    "Failed to parse int value of property "
                    + Constants.CMD_OPT_NAME_MAXTRACEDURATION
                    + " (must be an integer):" + maxTraceDurationStr,
                    exc);
            return false;
        }

        final DateFormat m_ISO8601UTC = new SimpleDateFormat(
                Constants.DATE_FORMAT_PATTERN);
        m_ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            final String ignoreRecordsBeforeTimestampString = TraceAnalysisTool.cmdl.getOptionValue(
                    Constants.CMD_OPT_NAME_IGNOREEXECUTIONSBEFOREDATE,
                    null);
            final String ignoreRecordsAfterTimestampString = TraceAnalysisTool.cmdl.getOptionValue(
                    Constants.CMD_OPT_NAME_IGNOREEXECUTIONSAFTERDATE, null);
            if (ignoreRecordsBeforeTimestampString != null) {
                final Date ignoreBeforeDate = m_ISO8601UTC.parse(ignoreRecordsBeforeTimestampString);
                TraceAnalysisTool.ignoreExecutionsBeforeTimestamp = ignoreBeforeDate.getTime()
                        * (1000 * 1000);
                TraceAnalysisTool.log.info("Ignoring records before "
                        + m_ISO8601UTC.format(ignoreBeforeDate) + " ("
                        + TraceAnalysisTool.ignoreExecutionsBeforeTimestamp + ")");
            }
            if (ignoreRecordsAfterTimestampString != null) {
                final Date ignoreAfterDate = m_ISO8601UTC.parse(ignoreRecordsAfterTimestampString);
                TraceAnalysisTool.ignoreExecutionsAfterTimestamp = ignoreAfterDate.getTime()
                        * (1000 * 1000);
                TraceAnalysisTool.log.info("Ignoring records after "
                        + m_ISO8601UTC.format(ignoreAfterDate) + " ("
                        + TraceAnalysisTool.ignoreExecutionsAfterTimestamp + ")");
            }
        } catch (final java.text.ParseException ex) {
            System.err.println("Error parsing date/time string. Please use the following pattern: "
                    + TraceAnalysisTool.DATE_FORMAT_PATTERN_CMD_USAGE_HELP);
            TraceAnalysisTool.log.error(
                    "Error parsing date/time string. Please use the following pattern: "
                    + TraceAnalysisTool.DATE_FORMAT_PATTERN_CMD_USAGE_HELP,
                    ex);
            return false;
        }
        return true;
    }

    private static void dumpConfiguration() {
        final List<Option> myOpts =
                new Vector<Option>(Constants.SORTED_OPTION_LIST);

        System.out.println("#");
        System.out.println("# Configuration");
        for (final Option o : Constants.SORTED_OPTION_LIST) {
            final String longOpt = o.getLongOpt();
            String val = "<null>";
            boolean dumpedOp = false;
            if (longOpt.equals(Constants.CMD_OPT_NAME_INPUTDIRS)) {
                val = Constants.stringArrToStringList(TraceAnalysisTool.inputDirs);
                dumpedOp = true;
            } else if (longOpt.equals(Constants.CMD_OPT_NAME_OUTPUTDIR)) {
                val = TraceAnalysisTool.outputDir;
                dumpedOp = true;
            } else if (longOpt.equals(Constants.CMD_OPT_NAME_OUTPUTFNPREFIX)) {
                val = TraceAnalysisTool.outputFnPrefix;
                dumpedOp = true;
            } else if (longOpt.equals(Constants.CMD_OPT_NAME_TASK_EQUIVCLASSREPORT)
                    || longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTSEQDS)
                    || longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTCOMPONENTDEPG)
                    || longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG)
                    || longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTOPERATIONDEPG)
                    || longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTAGGREGATEDCALLTREE)
                    || longOpt.equals(Constants.CMD_OPT_NAME_TASK_PLOTCALLTREES)
                    || longOpt.equals(Constants.CMD_OPT_NAME_TASK_PRINTEXECTRACES)
                    || longOpt.equals(Constants.CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES)
                    || longOpt.equals(Constants.CMD_OPT_NAME_TASK_PRINTMSGTRACES)) {
                val = TraceAnalysisTool.cmdl.hasOption(longOpt) ? "true"
                        : "false";
                dumpedOp = true;
            } else if (longOpt.equals(Constants.CMD_OPT_NAME_SELECTTRACES)) {
                if (TraceAnalysisTool.selectedTraces != null) {
                    val = TraceAnalysisTool.selectedTraces.toString();
                } else {
                    val = "<select all>";
                }

                dumpedOp = true;
            } else if (longOpt.equals(Constants.CMD_OPT_NAME_TRACEEQUIVCLASSMODE)) {
                if (TraceAnalysisTool.traceEquivalenceClassMode == TraceEquivalenceClassModes.ALLOCATION) {
                    val = Constants.TRACE_EQUIVALENCE_MODE_STR_ALLOCATION;
                } else if (TraceAnalysisTool.traceEquivalenceClassMode == TraceEquivalenceClassModes.ASSEMBLY) {
                    val = Constants.TRACE_EQUIVALENCE_MODE_STR_ASSEMBLY;
                } else if (TraceAnalysisTool.traceEquivalenceClassMode == TraceEquivalenceClassModes.DISABLED) {
                    val = Constants.TRACE_EQUIVALENCE_MODE_STR_DISABLED;
                }
                dumpedOp = true;
            } else if (longOpt.equals(Constants.CMD_OPT_NAME_SHORTLABELS)) {
                val = TraceAnalysisTool.shortLabels ? "true" : "false";
                dumpedOp = true;
            } else if (longOpt.equals(Constants.CMD_OPT_NAME_IGNOREINVALIDTRACES)) {
                val = TraceAnalysisTool.ignoreInvalidTraces ? "true" : "false";
                dumpedOp = true;
            } else if (longOpt.equals(Constants.CMD_OPT_NAME_MAXTRACEDURATION)) {
                val = TraceAnalysisTool.maxTraceDurationMillis + " ms";
                dumpedOp = true;
            } else if (longOpt.equals(Constants.CMD_OPT_NAME_IGNOREEXECUTIONSBEFOREDATE)) {
                val = LoggingTimestampConverter.convertLoggingTimestampToUTCString(TraceAnalysisTool.ignoreExecutionsBeforeTimestamp)
                        + " ("
                        + LoggingTimestampConverter.convertLoggingTimestampLocalTimeZoneString(TraceAnalysisTool.ignoreExecutionsBeforeTimestamp)
                        + ")";
                dumpedOp = true;
            } else if (longOpt.equals(Constants.CMD_OPT_NAME_IGNOREEXECUTIONSAFTERDATE)) {
                val = LoggingTimestampConverter.convertLoggingTimestampToUTCString(TraceAnalysisTool.ignoreExecutionsAfterTimestamp)
                        + " ("
                        + LoggingTimestampConverter.convertLoggingTimestampLocalTimeZoneString(TraceAnalysisTool.ignoreExecutionsAfterTimestamp)
                        + ")";
                dumpedOp = true;
            } else {
                val = TraceAnalysisTool.cmdl.getOptionValues(longOpt).toString();
                TraceAnalysisTool.log.warn("Unformatted confguration output for option "
                        + longOpt);
            }
            System.out.println("--" + longOpt + ": " + val);
            if (dumpedOp) {
                myOpts.remove(o);
            }
        }
    }

    private static boolean dispatchTasks() {
        boolean retVal = true;
        int numRequestedTasks = 0;

        TraceReconstructionPlugin mtReconstrFilter = null;
        try {
            Execution rootExecution =
            new Execution(
                TraceAnalysisTool.systemEntityFactory.getOperationFactory().rootOperation,
                TraceAnalysisTool.systemEntityFactory.getAllocationFactory().rootAllocationComponent,
                -1, "-1", -1, -1, -1, -1);

            final AnalysisInstance analysisInstance = new AnalysisInstance();
            analysisInstance.setLogReader(new FSReader(
                    TraceAnalysisTool.inputDirs));

            final ExecutionRecordTransformationPlugin execRecTransformer = new ExecutionRecordTransformationPlugin(
                    Constants.EXEC_TRACE_RECONSTR_COMPONENT_NAME,
                    TraceAnalysisTool.systemEntityFactory);
            analysisInstance.registerPlugin(execRecTransformer);

            final TimestampFilter executionFilterByTimestamp =
                    new TimestampFilter(TraceAnalysisTool.ignoreExecutionsBeforeTimestamp,
                    TraceAnalysisTool.ignoreExecutionsAfterTimestamp);
            execRecTransformer.getExecutionOutputPort().subscribe(executionFilterByTimestamp.getExecutionInputPort());
            analysisInstance.registerPlugin(executionFilterByTimestamp);

            final TraceIdFilter executionFilterByTraceId =
                    new TraceIdFilter(TraceAnalysisTool.selectedTraces);
            executionFilterByTimestamp.getExecutionOutputPort().subscribe(executionFilterByTraceId.getExecutionInputPort());
            analysisInstance.registerPlugin(executionFilterByTraceId);

            mtReconstrFilter = new TraceReconstructionPlugin(
                    Constants.TRACERECONSTR_COMPONENT_NAME,
                    TraceAnalysisTool.systemEntityFactory,
                    rootExecution,
                    TraceAnalysisTool.maxTraceDurationMillis,
                    TraceAnalysisTool.ignoreInvalidTraces);
            executionFilterByTraceId.getExecutionOutputPort().subscribe(mtReconstrFilter.getExecutionInputPort());
            analysisInstance.registerPlugin(mtReconstrFilter);

            final List<AbstractTraceProcessingPlugin> allTraceProcessingComponents = new ArrayList<AbstractTraceProcessingPlugin>();

            final TraceEquivalenceClassFilter traceEquivClassFilter =
                    new TraceEquivalenceClassFilter(
                    Constants.TRACEEEQUIVCLASS_COMPONENT_NAME,
                    systemEntityFactory,
                    rootExecution,
                    traceEquivalenceClassMode);
            mtReconstrFilter.getExecutionTraceOutputPort().subscribe(traceEquivClassFilter.getExecutionTraceInputPort());
            analysisInstance.registerPlugin(traceEquivClassFilter);
            allTraceProcessingComponents.add(traceEquivClassFilter);

            // fill list of msgTraceProcessingComponents:
            MessageTraceWriterPlugin componentPrintMsgTrace = null;
            if (TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PRINTMSGTRACES)) {
                numRequestedTasks++;
                componentPrintMsgTrace =
                        new MessageTraceWriterPlugin(
                        Constants.PRINTMSGTRACE_COMPONENT_NAME,
                        TraceAnalysisTool.systemEntityFactory,
                        new File(TraceAnalysisTool.outputDir + File.separator + TraceAnalysisTool.outputFnPrefix + Constants.MESSAGE_TRACES_FN_PREFIX + ".txt").getCanonicalPath());
                mtReconstrFilter.getMessageTraceOutputPort().subscribe(componentPrintMsgTrace.getMessageTraceInputPort());
                analysisInstance.registerPlugin(componentPrintMsgTrace);
                allTraceProcessingComponents.add(componentPrintMsgTrace);
            }
            ExecutionTraceWriterPlugin componentPrintExecTrace = null;
            if (TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PRINTEXECTRACES)) {
                numRequestedTasks++;
                componentPrintExecTrace =
                        new ExecutionTraceWriterPlugin(
                        Constants.PRINTEXECTRACE_COMPONENT_NAME,
                        TraceAnalysisTool.systemEntityFactory,
                        new File(TraceAnalysisTool.outputDir + File.separator + TraceAnalysisTool.outputFnPrefix + Constants.EXECUTION_TRACES_FN_PREFIX + ".txt").getCanonicalPath());
                mtReconstrFilter.getExecutionTraceOutputPort().subscribe(componentPrintExecTrace.getExecutionTraceInputPort());
                analysisInstance.registerPlugin(componentPrintExecTrace);
                allTraceProcessingComponents.add(componentPrintExecTrace);
            }
            InvalidExecutionTraceWriterPlugin componentPrintInvalidTrace = null;
            if (TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES)) {
                numRequestedTasks++;
                componentPrintInvalidTrace =
                        new InvalidExecutionTraceWriterPlugin(
                        Constants.PRINTINVALIDEXECTRACE_COMPONENT_NAME,
                        TraceAnalysisTool.systemEntityFactory,
                        new File(TraceAnalysisTool.outputDir + File.separator + TraceAnalysisTool.outputFnPrefix + Constants.INVALID_TRACES_FN_PREFIX + ".txt").getCanonicalPath());
                mtReconstrFilter.getInvalidExecutionTraceOutputPort().subscribe(componentPrintInvalidTrace.getInvalidExecutionTraceInputPort());
                analysisInstance.registerPlugin(componentPrintInvalidTrace);
                allTraceProcessingComponents.add(componentPrintInvalidTrace);
            }
            SequenceDiagramPlugin componentPlotSeqDiagr = null;
            if (retVal
                    && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTSEQDS)) {
                numRequestedTasks++;
                componentPlotSeqDiagr = new SequenceDiagramPlugin(
                        Constants.PLOTSEQDIAGR_COMPONENT_NAME, TraceAnalysisTool.systemEntityFactory,
                        new File(TraceAnalysisTool.outputDir + File.separator + TraceAnalysisTool.outputFnPrefix + Constants.SEQUENCE_DIAGRAM_FN_PREFIX).getCanonicalPath(), shortLabels);
                mtReconstrFilter.getMessageTraceOutputPort().subscribe(componentPlotSeqDiagr.getMessageTraceInputPort());
                analysisInstance.registerPlugin(componentPlotSeqDiagr);
                allTraceProcessingComponents.add(componentPlotSeqDiagr);
            }
            ComponentDependencyGraphPlugin componentPlotComponentDepGraph = null;
            if (retVal
                    && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTCOMPONENTDEPG)) {
                numRequestedTasks++;
                componentPlotComponentDepGraph =
                        new ComponentDependencyGraphPlugin(
                        Constants.PLOTCOMPONENTDEPGRAPH_COMPONENT_NAME, TraceAnalysisTool.systemEntityFactory);
                mtReconstrFilter.getMessageTraceOutputPort().subscribe(componentPlotComponentDepGraph.getMessageTraceInputPort());
                analysisInstance.registerPlugin(componentPlotComponentDepGraph);
                allTraceProcessingComponents.add(componentPlotComponentDepGraph);
            }
            ContainerDependencyGraphPlugin componentPlotContainerDepGraph = null;
            if (retVal
                    && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG)) {
                numRequestedTasks++;
                componentPlotContainerDepGraph =
                        new ContainerDependencyGraphPlugin(Constants.PLOTCONTAINERDEPGRAPH_COMPONENT_NAME, TraceAnalysisTool.systemEntityFactory);
                mtReconstrFilter.getMessageTraceOutputPort().subscribe(componentPlotContainerDepGraph.getMessageTraceInputPort());
                analysisInstance.registerPlugin(componentPlotContainerDepGraph);
                allTraceProcessingComponents.add(componentPlotContainerDepGraph);
            }
            OperationDependencyGraphPlugin componentPlotOperationDepGraph = null;
            if (retVal
                    && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTOPERATIONDEPG)) {
                numRequestedTasks++;
                componentPlotOperationDepGraph =
                        new OperationDependencyGraphPlugin(Constants.PLOTOPERATIONDEPGRAPH_COMPONENT_NAME, TraceAnalysisTool.systemEntityFactory);
                mtReconstrFilter.getMessageTraceOutputPort().subscribe(componentPlotOperationDepGraph.getMessageTraceInputPort());
                analysisInstance.registerPlugin(componentPlotOperationDepGraph);
                allTraceProcessingComponents.add(componentPlotOperationDepGraph);
            }
            TraceCallTreePlugin componentPlotTraceCallTrees = null;
            if (retVal
                    && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTCALLTREES)) {
                numRequestedTasks++;
                componentPlotTraceCallTrees =
                        new TraceCallTreePlugin(
                        Constants.PLOTCALLTREE_COMPONENT_NAME,
                        TraceAnalysisTool.allocationComponentOperationPairFactory,
                        TraceAnalysisTool.systemEntityFactory,
                        new File(TraceAnalysisTool.outputDir + File.separator
                        + TraceAnalysisTool.outputFnPrefix
                        + Constants.CALL_TREE_FN_PREFIX).getCanonicalPath(),
                        shortLabels);
                mtReconstrFilter.getMessageTraceOutputPort().subscribe(componentPlotTraceCallTrees.getMessageTraceInputPort());
                analysisInstance.registerPlugin(componentPlotTraceCallTrees);
                allTraceProcessingComponents.add(componentPlotTraceCallTrees);
            }
            AggregatedAllocationComponentOperationCallTreePlugin componentPlotAggregatedCallTree = null;
            if (retVal
                    && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTAGGREGATEDCALLTREE)) {
                numRequestedTasks++;
                componentPlotAggregatedCallTree =
                        new AggregatedAllocationComponentOperationCallTreePlugin(
                        Constants.PLOTAGGREGATEDCALLTREE_COMPONENT_NAME,
                        TraceAnalysisTool.allocationComponentOperationPairFactory,
                        TraceAnalysisTool.systemEntityFactory);
                mtReconstrFilter.getMessageTraceOutputPort().subscribe(componentPlotAggregatedCallTree.getMessageTraceInputPort());
                analysisInstance.registerPlugin(componentPlotAggregatedCallTree);
                allTraceProcessingComponents.add(componentPlotAggregatedCallTree);
            }
            if (retVal
                    && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_EQUIVCLASSREPORT)) {
                numRequestedTasks++;
                // the actual execution of the task is performed below
            }

            if (numRequestedTasks == 0) {
                System.err.println("No task requested");
                TraceAnalysisTool.printUsage();
                return false;
            }

            int numErrorCount = 0;
            try {
                analysisInstance.run();

                // TODO: move the following code to the plugin's terminate methods.
                if (componentPlotComponentDepGraph != null) {
                    componentPlotComponentDepGraph.saveToDotFile(
                            new File(
                            TraceAnalysisTool.outputDir
                            + File.separator
                            + TraceAnalysisTool.outputFnPrefix
                            + Constants.COMPONENT_DEPENDENCY_GRAPH_FN_PREFIX).getCanonicalPath(),
                            (TraceAnalysisTool.traceEquivalenceClassMode == TraceEquivalenceClassModes.DISABLED),
                            TraceAnalysisTool.shortLabels,
                            TraceAnalysisTool.includeSelfLoops);
                }
                if (componentPlotContainerDepGraph != null) {
                    componentPlotContainerDepGraph.saveToDotFile(
                            new File(
                            TraceAnalysisTool.outputDir
                            + File.separator
                            + TraceAnalysisTool.outputFnPrefix
                            + Constants.CONTAINER_DEPENDENCY_GRAPH_FN_PREFIX).getCanonicalPath(),
                            (TraceAnalysisTool.traceEquivalenceClassMode == TraceEquivalenceClassModes.DISABLED),
                            TraceAnalysisTool.shortLabels,
                            TraceAnalysisTool.includeSelfLoops);
                }
                if (componentPlotOperationDepGraph != null) {
                    componentPlotOperationDepGraph.saveToDotFile(
                            new File(
                            TraceAnalysisTool.outputDir
                            + File.separator
                            + TraceAnalysisTool.outputFnPrefix
                            + Constants.OPERATION_DEPENDENCY_GRAPH_FN_PREFIX).getCanonicalPath(),
                            (TraceAnalysisTool.traceEquivalenceClassMode == TraceEquivalenceClassModes.DISABLED),
                            TraceAnalysisTool.shortLabels,
                            TraceAnalysisTool.includeSelfLoops);
                }

                if (componentPlotAggregatedCallTree != null) {
                    componentPlotAggregatedCallTree.saveTreeToDotFile(new File(
                            TraceAnalysisTool.outputDir + File.separator
                            + TraceAnalysisTool.outputFnPrefix
                            + Constants.AGGREGATED_CALL_TREE_FN_PREFIX).getCanonicalPath(), true,
                            TraceAnalysisTool.shortLabels); // includeWeights
                }
            } catch (final Exception exc) {
                TraceAnalysisTool.log.error(
                        "Error occured while running analysis", exc);
                throw new Exception("Error occured while running analysis", exc);
            } finally {
                for (final AbstractTraceProcessingPlugin c : allTraceProcessingComponents) {
                    numErrorCount += c.getErrorCount();
                    c.printStatusMessage();
                }
            }

            if (!TraceAnalysisTool.ignoreInvalidTraces && numErrorCount > 0) {
                TraceAnalysisTool.log.error(numErrorCount
                        + " errors occured in trace processing components");
                throw new Exception(numErrorCount
                        + " errors occured in trace processing components");
            }

            if (retVal
                    && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_EQUIVCLASSREPORT)) {
                retVal = TraceAnalysisTool.writeTraceEquivalenceReport(
                        TraceAnalysisTool.outputDir + File.separator
                        + TraceAnalysisTool.outputFnPrefix,
                        traceEquivClassFilter);
            }

            // TODO: turn into plugin with output code in terminate method
            final String systemEntitiesHtmlFn = new File(
                    TraceAnalysisTool.outputDir + File.separator
                    + TraceAnalysisTool.outputFnPrefix
                    + "system-entities").getAbsolutePath();
            TraceAnalysisTool.systemEntityFactory.saveSystemToHTMLFile(systemEntitiesHtmlFn);
            System.out.println("");
            System.out.println("#");
            System.out.println("# Plugin: " + "System Entity Factory");
            System.out.println("Wrote table of system entities to file '"
                    + systemEntitiesHtmlFn + ".html'");

            // TODO: these tasks should be moved to a decicated tool
            // if (retVal && cmdl.hasOption(CMD_OPT_NAME_TASK_INITJMSREADER)) {
            // numRequestedTasks++;
            // retVal = task_initBasicJmsReader("tcp://127.0.0.1:3035/",
            // "queue1");
            // System.out.println("Finished to start task_initBasicJmsReader");
            // }
            // if (retVal && cmdl.hasOption(CMD_OPT_NAME_TASK_INITJMSREADERJFX))
            // {
            // numRequestedTasks++;
            // retVal = task_initBasicJmsReaderJavaFx("tcp://127.0.0.1:3035/",
            // "queue1");
            // System.out.println("Finished to start task_initBasicJmsReader");
            // }

            if (!retVal) {
                System.err.println("A task failed");
            }
        } catch (final Exception ex) {
            System.err.println("An error occured: " + ex.getMessage());
            System.err.println("");
            TraceAnalysisTool.log.error("Exception", ex);
            retVal = false;
        } finally {
            if (mtReconstrFilter != null) {
                mtReconstrFilter.printStatusMessage();
            }
            System.out.println("");
            System.out.println("See 'kieker.log' for details");
        }

        return retVal;
    }

    public static void main(final String args[]) {
        try {
            if (!TraceAnalysisTool.parseArgs(args)
                    || !TraceAnalysisTool.initFromArgs()) {
                System.exit(1);
            }

            TraceAnalysisTool.dumpConfiguration();

            if (!TraceAnalysisTool.dispatchTasks()) {
                System.exit(1);
            }

        } catch (final Exception exc) {
            System.err.println("An error occured. See log for details");
            TraceAnalysisTool.log.fatal(args, exc);
        }
    }

    private static boolean writeTraceEquivalenceReport(
            final String outputFnPrefix, final TraceEquivalenceClassFilter traceEquivFilter)
            throws IOException {
        boolean retVal = true;
        final String outputFn = new File(outputFnPrefix
                + Constants.TRACE_EQUIV_CLASSES_FN_PREFIX + ".txt").getCanonicalPath();
        PrintStream ps = null;
        try {
            ps = new PrintStream(new FileOutputStream(outputFn));
            int numClasses = 0;
            final HashMap<ExecutionTrace, Integer> classMap = traceEquivFilter.getEquivalenceClassMap();
            for (final Entry<ExecutionTrace, Integer> e : classMap.entrySet()) {
                final ExecutionTrace t = e.getKey();
                ps.println("Class " + numClasses++ + " ; cardinality: "
                        + e.getValue() + "; # executions: " + t.getLength()
                        + "; representative: " + t.getTraceId()
                        + "; max. stack depth: " + t.getMaxStackDepth());
            }
            System.out.println("");
            System.out.println("#");
            System.out.println("# Plugin: " + "Trace equivalence report");
            System.out.println("Wrote " + numClasses + " equivalence class"
                    + (numClasses > 1 ? "es" : "") + " to file '" + outputFn
                    + "'");
        } catch (final FileNotFoundException e) {
            TraceAnalysisTool.log.error("File not found", e);
            retVal = false;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }

        return retVal;
    }
//    private static boolean task_initBasicJmsReader(final String jmsProviderUrl,
//            final String jmsDestination) throws IOException,
//            MonitoringLogReaderException, MonitoringRecordConsumerException {
//        final boolean retVal = true;
//
//        TraceAnalysisTool.log.info("Trying to start JMS Listener to "
//                + jmsProviderUrl + " " + jmsDestination);
//        /* Read log data and collect execution traces */
//        final AnalysisInstance analysisInstance = new AnalysisInstance();
//        analysisInstance.setLogReader(new JMSReader(jmsProviderUrl,
//                jmsDestination));
//
//        final MonitoringRecordTypeLogger recordTypeLogger = new MonitoringRecordTypeLogger();
//        analysisInstance.registerPlugin(recordTypeLogger);
//
//        // MessageTraceRepository seqRepConsumer = new MessageTraceRepository();
//        // analysisInstance.addRecordConsumer(seqRepConsumer);
//
//        /* @Matthias: Deactivated this, since the ant task didn't run (Andre) */
//        // BriefJavaFxInformer bjfx = new BriefJavaFxInformer();
//        // analysisInstance.addRecordConsumer(bjfx);
//
//        analysisInstance.run();
//        return retVal;
//    }
//    private static boolean task_initBasicJmsReaderJavaFx(
//            final String jmsProviderUrl, final String jmsDestination)
//            throws IOException, MonitoringLogReaderException,
//            MonitoringRecordConsumerException {
//        final boolean retVal = true;
//
//        TraceAnalysisTool.log.info("Trying to start JMS Listener to "
//                + jmsProviderUrl + " " + jmsDestination);
//        /* Read log data and collect execution traces */
//        final AnalysisInstance analysisInstance = new AnalysisInstance();
//        analysisInstance.setLogReader(new JMSReader(jmsProviderUrl,
//                jmsDestination));
//
//        final MonitoringRecordTypeLogger recordTypeLogger = new MonitoringRecordTypeLogger();
//        analysisInstance.registerPlugin(recordTypeLogger);
//
//        // MessageTraceRepository seqRepConsumer = new MessageTraceRepository();
//        // analysisInstance.addRecordConsumer(seqRepConsumer);
//
//        /* @Matthias: Deactivated this, since the ant task didn't run (Andre) */
//        final BriefJavaFxInformer bjfx = new BriefJavaFxInformer();
//
//        analysisInstance.run();
//        return retVal;
//    }
    /**
     * This method is used to initialize a typical set of filters, required for
     * message trace analysis. Every new trace object is passed to the
     * messageTraceListener.
     *
     * Kieker Live Analysis adds itself as listener.
     *
     * TO BE REMOVED INTO
     *
     * You'll need a tpanInstance (with a reader) before invoking this method.
     */
//    public static void createMessageTraceFiltersAndRegisterMessageTraceListener(
//            AnalysisInstance tpanInstance,
//            final BriefJavaFxInformer messageTraceListener) {
//        if (tpanInstance == null) {
//            tpanInstance = new AnalysisInstance();
//        }
//
//        TraceReconstructionPlugin mtReconstrFilter = null;
//        mtReconstrFilter = new TraceReconstructionPlugin(
//                TraceAnalysisTool.TRACERECONSTR_COMPONENT_NAME,
//                TraceAnalysisTool.systemEntityFactory, 60 * 1000, // maxTraceDurationMillis,
//                true, // ignoreInvalidTraces,
//                TraceEquivalenceClassModes.DISABLED, // traceEquivalenceClassMode,
//                // // = every trace
//                // passes, not only
//                // unique trace classes
//                null, // selectedTraces, // null means all
//                TraceAnalysisTool.ignoreRecordsBeforeTimestamp, // default
//                // Long.MIN
//                TraceAnalysisTool.ignoreRecordsAfterTimestamp); // default
//        // Long.MAX
//        mtReconstrFilter.getMessageTraceOutputPort().subsribe(messageTraceListener.getMessageTraceInputPort());
//        mtReconstrFilter.getInvalidExecutionTraceOutputPort().subsribe(messageTraceListener.getJfxBrokenExecutionTraceInputPort()); // i
//        // know
//        // that
//        // its
//        // dirty
//
//        TraceReconstructionPlugin uniqueMtReconstrFilter = null;
//        uniqueMtReconstrFilter = new TraceReconstructionPlugin(
//                TraceAnalysisTool.TRACERECONSTR_COMPONENT_NAME,
//                TraceAnalysisTool.systemEntityFactory, 60 * 1000, // maxTraceDurationMillis,
//                true, // ignoreInvalidTraces,
//                TraceEquivalenceClassModes.ALLOCATION, // traceEquivalenceClassMode,
//                // // = every trace
//                // passes, not only
//                // unique trace classes
//                null, // selectedTraces, // null means all
//                TraceAnalysisTool.ignoreRecordsBeforeTimestamp, // default
//                // Long.MIN
//                TraceAnalysisTool.ignoreRecordsAfterTimestamp); // default
//        // Long.MAX
//        uniqueMtReconstrFilter.getMessageTraceOutputPort().subsribe(messageTraceListener.getJfxUniqueMessageTraceInputPort()); // i know that its
//        // dirty; i (andre) like
//        // it because it's
//        // basically a port
//
//        final ExecutionRecordTransformationPlugin execRecTransformer = new ExecutionRecordTransformationPlugin(
//                TraceAnalysisTool.EXEC_TRACE_RECONSTR_COMPONENT_NAME,
//                TraceAnalysisTool.systemEntityFactory);
//        execRecTransformer.getExecutionOutputPort().subsribe(mtReconstrFilter.getExecutionInputPort());
//        execRecTransformer.getExecutionOutputPort().subsribe(uniqueMtReconstrFilter.getExecutionInputPort());
//        tpanInstance.registerPlugin(execRecTransformer);
//        System.out.println("MessageTraceListener registered");
//    }
}
