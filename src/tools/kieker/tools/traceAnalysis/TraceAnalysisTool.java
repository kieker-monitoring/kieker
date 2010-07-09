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
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.Vector;
import java.util.Map.Entry;

import kieker.common.util.LoggingTimestampConverter;
import kieker.analysis.AnalysisInstance;
import kieker.analysis.datamodel.ExecutionTrace;
import kieker.analysis.datamodel.InvalidExecutionTrace;
import kieker.analysis.datamodel.MessageTrace;
import kieker.analysis.datamodel.repository.AbstractSystemSubRepository;
import kieker.analysis.datamodel.repository.AllocationComponentOperationPairFactory;
import kieker.analysis.datamodel.repository.AssemblyComponentOperationPairFactory;
import kieker.analysis.datamodel.repository.SystemModelRepository;
import kieker.analysis.plugin.IAnalysisPlugin;
import kieker.analysis.plugin.MonitoringRecordConsumerException;
import kieker.analysis.plugin.javaFx.BriefJavaFxInformer;
import kieker.analysis.plugin.traceAnalysis.AbstractExecutionTraceProcessingPlugin;
import kieker.analysis.plugin.traceAnalysis.AbstractInvalidExecutionTraceProcessingPlugin;
import kieker.analysis.plugin.traceAnalysis.AbstractMessageTraceProcessingPlugin;
import kieker.analysis.plugin.traceAnalysis.AbstractTraceProcessingPlugin;
import kieker.analysis.plugin.traceAnalysis.executionRecordTransformation.ExecutionRecordTransformationPlugin;
import kieker.analysis.plugin.traceAnalysis.traceReconstruction.InvalidTraceException;
import kieker.analysis.plugin.traceAnalysis.traceReconstruction.TraceProcessingException;
import kieker.analysis.plugin.traceAnalysis.traceReconstruction.TraceReconstructionPlugin;
import kieker.analysis.plugin.traceAnalysis.traceReconstruction.TraceReconstructionPlugin.TraceEquivalenceClassModes;
import kieker.analysis.plugin.traceAnalysis.visualization.callTree.AbstractCallTreePlugin;
import kieker.analysis.plugin.traceAnalysis.visualization.callTree.AggregatedAllocationComponentOperationCallTreePlugin;
import kieker.analysis.plugin.traceAnalysis.visualization.callTree.TraceCallTreeNode;
import kieker.analysis.plugin.traceAnalysis.visualization.dependencyGraph.ComponentDependencyGraphPlugin;
import kieker.analysis.plugin.traceAnalysis.visualization.dependencyGraph.ContainerDependencyGraphPlugin;
import kieker.analysis.plugin.traceAnalysis.visualization.dependencyGraph.OperationDependencyGraphPlugin;
import kieker.analysis.plugin.traceAnalysis.visualization.sequenceDiagram.SequenceDiagramPlugin;
import kieker.analysis.plugin.util.MonitoringRecordTypeLogger;
import kieker.analysis.plugin.util.event.EventProcessingException;
import kieker.analysis.reader.JMSReader;
import kieker.analysis.reader.MonitoringLogReaderException;
import kieker.analysis.reader.filesystem.FSReader;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
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
    private static final AssemblyComponentOperationPairFactory assemblyComponentOperationPairFactory = new AssemblyComponentOperationPairFactory(
            TraceAnalysisTool.systemEntityFactory);
    private static CommandLine cmdl = null;
    private static final CommandLineParser cmdlParser = new BasicParser();
    private static final HelpFormatter cmdHelpFormatter = new HelpFormatter();
    private static final Options cmdlOpts = new Options();
    private static String[] inputDirs = null;
    private static String outputDir = null;
    private static String outputFnPrefix = null;
    private static TreeSet<Long> selectedTraces = null; // null means select all
    private static TraceEquivalenceClassModes traceEquivalenceClassMode = TraceEquivalenceClassModes.DISABLED;
    private static boolean shortLabels = true;
    private static boolean includeSelfLoops = false;
    private static boolean ignoreInvalidTraces = false;
    private static int maxTraceDurationMillis = TraceReconstructionPlugin.MAX_DURATION_MILLIS; // infinite
    private static long ignoreRecordsBeforeTimestamp = TraceReconstructionPlugin.MIN_TIMESTAMP;
    private static long ignoreRecordsAfterTimestamp = TraceReconstructionPlugin.MAX_TIMESTAMP;
    public static final String DATE_FORMAT_PATTERN_CMD_USAGE_HELP = Constants.DATE_FORMAT_PATTERN.replaceAll("'", ""); // only for usage info
    // private static final String CMD_OPT_NAME_TASK_INITJMSREADER =
    // "init-basic-JMS-reader";
    // private static final String CMD_OPT_NAME_TASK_INITJMSREADERJFX =
    // "init-basic-JMS-readerJavaFx";
    private static final Vector<Option> options = new Vector<Option>();

    static {
        // TODO: OptionGroups?

        TraceAnalysisTool.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_INPUTDIRS).withArgName("dir1 ... dirN").hasArgs().isRequired(true).withDescription(
                "Log directories to read data from").withValueSeparator('=').create("i"));
        TraceAnalysisTool.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_OUTPUTDIR).withArgName("dir").hasArg(
                true).isRequired(true).withDescription(
                "Directory for the generated file(s)").withValueSeparator('=').create("o"));
        TraceAnalysisTool.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_OUTPUTFNPREFIX).withArgName("prefix").hasArg(true).isRequired(false).withDescription(
                "Prefix for output filenames\n").withValueSeparator('=').create("p"));

        // OptionGroup cmdlOptGroupTask = new OptionGroup();
        // cmdlOptGroupTask.isRequired();
        TraceAnalysisTool.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_TASK_PLOTSEQDS).hasArg(false).withDescription(
                "Generate and store sequence diagrams (.pic files)").create());
        TraceAnalysisTool.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_TASK_PLOTCOMPONENTDEPG).hasArg(false).withDescription(
                "Generate and store a component dependency graph (.dot file)").create());
        TraceAnalysisTool.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG).hasArg(false).withDescription(
                "Generate and store a container dependency graph (.dot file)").create());
        TraceAnalysisTool.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_TASK_PLOTOPERATIONDEPG).hasArg(false).withDescription(
                "Generate and store an operation dependency graph (.dot file)").create());
        TraceAnalysisTool.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_TASK_PLOTAGGREGATEDCALLTREE).hasArg(
                false).withDescription(
                "Generate and store an aggregated call tree (.dot files)").create());
        TraceAnalysisTool.options.add(OptionBuilder.withLongOpt(Constants.CMD_OPT_NAME_TASK_PLOTCALLTREES).hasArg(false).withDescription(
                "Generate and store call trees for the selected traces (.dot files)").create());
        TraceAnalysisTool.options.add(OptionBuilder.withLongOpt(Constants.CMD_OPT_NAME_TASK_PRINTMSGTRACES).hasArg(false).withDescription(
                "Save message trace representations of valid traces (.txt files)").create());
        TraceAnalysisTool.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_TASK_PRINTEXECTRACES).hasArg(false).withDescription(
                "Save execution trace representations of valid traces (.txt files)").create());
        TraceAnalysisTool.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES).hasArg(false).withDescription(
                "Save a execution trace representations of invalid trace artifacts (.txt files)").create());
        TraceAnalysisTool.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_TASK_EQUIVCLASSREPORT).hasArg(false).withDescription(
                "Output an overview about the trace equivalence classes").create());

        /*
         * These tasks should be moved to a dedicated tool, since this tool
         * covers trace analysis
         */
        // cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_INITJMSREADER).hasArg(false).withDescription("Creates a jms reader and shows incomming data in the command line").create());
        // cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_INITJMSREADERJFX).hasArg(false).withDescription("Creates a jms reader and shows incomming data in the command line and visualizes with javafx").create());

        // cmdlOpts.addOptionGroup(cmdlOptGroupTask);
        TraceAnalysisTool.options.add(OptionBuilder.withLongOpt(Constants.CMD_OPT_NAME_SELECTTRACES).withArgName("id0 ... idn").hasArgs().isRequired(false).withDescription(
                "Consider only the traces identified by the comma-separated list of trace IDs. Defaults to all traces.").create());
        TraceAnalysisTool.options.add(OptionBuilder.withLongOpt(Constants.CMD_OPT_NAME_TRACEEQUIVCLASSMODE).withArgName(
                String.format(
                "%s|%s|%s",
                Constants.TRACE_EQUIVALENCE_MODE_STR_ALLOCATION,
                Constants.TRACE_EQUIVALENCE_MODE_STR_ASSEMBLY,
                Constants.TRACE_EQUIVALENCE_MODE_STR_DISABLED)).hasArg(true).isRequired(false).withDescription(
                "If selected, the selected tasks are performed on representatives of the equivalence classes only.").create());
        TraceAnalysisTool.options.add(OptionBuilder.withLongOpt(Constants.CMD_OPT_NAME_IGNOREINVALIDTRACES).hasArg(false).isRequired(false).withDescription(
                "If selected, the execution aborts on the occurence of an invalid trace.").create());
        TraceAnalysisTool.options.add(OptionBuilder.withLongOpt(Constants.CMD_OPT_NAME_MAXTRACEDURATION).withArgName("duration in ms").hasArg().isRequired(false).withDescription(
                "Threshold (in milliseconds) after which an incomplete trace becomes invalid. Defaults to infinity.").create());
        TraceAnalysisTool.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_IGNORERECORDSBEFOREDATE).withArgName(
                TraceAnalysisTool.DATE_FORMAT_PATTERN_CMD_USAGE_HELP).hasArg().isRequired(false).withDescription(
                "Records logged before this date (UTC timezone) are ignored.").create());
        TraceAnalysisTool.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_IGNORERECORDSAFTERDATE).withArgName(
                TraceAnalysisTool.DATE_FORMAT_PATTERN_CMD_USAGE_HELP).hasArg().isRequired(false).withDescription(
                "Records logged after this date (UTC timezone) are ignored.").create());
        TraceAnalysisTool.options.add(OptionBuilder.withLongOpt(Constants.CMD_OPT_NAME_SHORTLABELS).hasArg(false).isRequired(false).withDescription(
                "If selected, the hostnames of the executions are NOT considered.").create());
        for (final Option o : TraceAnalysisTool.options) {
            TraceAnalysisTool.cmdlOpts.addOption(o);
        }
        TraceAnalysisTool.cmdHelpFormatter.setOptionComparator(new Comparator() {

            public int compare(final Object o1, final Object o2) {
                if (o1 == o2) {
                    return 0;
                }
                final int posO1 = TraceAnalysisTool.options.indexOf(o1);
                final int posO2 = TraceAnalysisTool.options.indexOf(o2);
                if (posO1 < posO2) {
                    return -1;
                }
                if (posO1 > posO2) {
                    return 1;
                }
                return 0;
            }
        });
    }

    private static boolean parseArgs(final String[] args) {
        try {
            TraceAnalysisTool.cmdl = TraceAnalysisTool.cmdlParser.parse(
                    TraceAnalysisTool.cmdlOpts, args);
        } catch (final ParseException e) {
            TraceAnalysisTool.printUsage();
            System.err.println("\nError parsing arguments: " + e.getMessage());
            return false;
        }
        return true;
    }

    private static void printUsage() {
        TraceAnalysisTool.cmdHelpFormatter.printHelp(80,
                TraceAnalysisTool.class.getName(), "",
                TraceAnalysisTool.cmdlOpts, "", true);
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
                    Constants.CMD_OPT_NAME_IGNORERECORDSBEFOREDATE,
                    null);
            final String ignoreRecordsAfterTimestampString = TraceAnalysisTool.cmdl.getOptionValue(
                    Constants.CMD_OPT_NAME_IGNORERECORDSAFTERDATE, null);
            if (ignoreRecordsBeforeTimestampString != null) {
                final Date ignoreBeforeDate = m_ISO8601UTC.parse(ignoreRecordsBeforeTimestampString);
                TraceAnalysisTool.ignoreRecordsBeforeTimestamp = ignoreBeforeDate.getTime()
                        * (1000 * 1000);
                TraceAnalysisTool.log.info("Ignoring records before "
                        + m_ISO8601UTC.format(ignoreBeforeDate) + " ("
                        + TraceAnalysisTool.ignoreRecordsBeforeTimestamp + ")");
            }
            if (ignoreRecordsAfterTimestampString != null) {
                final Date ignoreAfterDate = m_ISO8601UTC.parse(ignoreRecordsAfterTimestampString);
                TraceAnalysisTool.ignoreRecordsAfterTimestamp = ignoreAfterDate.getTime()
                        * (1000 * 1000);
                TraceAnalysisTool.log.info("Ignoring records after "
                        + m_ISO8601UTC.format(ignoreAfterDate) + " ("
                        + TraceAnalysisTool.ignoreRecordsAfterTimestamp + ")");
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

    private static String stringArrToStringList(final String[] strs) {
        final StringBuilder strB = new StringBuilder();
        boolean first = true;
        for (final String s : strs) {
            if (!first) {
                strB.append(", ");
            } else {
                first = false;
            }
            strB.append(s);
        }
        return strB.toString();
    }

    private static void dumpConfiguration() {
        final Vector<Option> myOpts = new Vector<Option>(
                TraceAnalysisTool.options);

        System.out.println("#");
        System.out.println("# Configuration");
        for (final Option o : TraceAnalysisTool.options) {
            final String longOpt = o.getLongOpt();
            String val = "<null>";
            boolean dumpedOp = false;
            if (longOpt.equals(Constants.CMD_OPT_NAME_INPUTDIRS)) {
                val = TraceAnalysisTool.stringArrToStringList(TraceAnalysisTool.inputDirs);
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
            } else if (longOpt.equals(Constants.CMD_OPT_NAME_IGNORERECORDSBEFOREDATE)) {
                val = LoggingTimestampConverter.convertLoggingTimestampToUTCString(TraceAnalysisTool.ignoreRecordsBeforeTimestamp)
                        + " ("
                        + LoggingTimestampConverter.convertLoggingTimestampLocalTimeZoneString(TraceAnalysisTool.ignoreRecordsBeforeTimestamp)
                        + ")";
                dumpedOp = true;
            } else if (longOpt.equals(Constants.CMD_OPT_NAME_IGNORERECORDSAFTERDATE)) {
                val = LoggingTimestampConverter.convertLoggingTimestampToUTCString(TraceAnalysisTool.ignoreRecordsAfterTimestamp)
                        + " ("
                        + LoggingTimestampConverter.convertLoggingTimestampLocalTimeZoneString(TraceAnalysisTool.ignoreRecordsAfterTimestamp)
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
    // this was moved to here from the inside of dispathTasks()
    private static final String EXEC_TRACE_RECONSTR_COMPONENT_NAME = "Execution record transformation";
    private static final String TRACERECONSTR_COMPONENT_NAME = "Trace reconstruction";
    private static final String PRINTMSGTRACE_COMPONENT_NAME = "Print message traces";
    private static final String PRINTEXECTRACE_COMPONENT_NAME = "Print execution traces";
    private static final String PRINTINVALIDEXECTRACE_COMPONENT_NAME = "Print invalid execution traces";
    private static final String PLOTCOMPONENTDEPGRAPH_COMPONENT_NAME = "Component dependency graph";
    private static final String PLOTCONTAINERDEPGRAPH_COMPONENT_NAME = "Container dependency graph";
    private static final String PLOTOPERATIONDEPGRAPH_COMPONENT_NAME = "Operation dependency graph";
    private static final String PLOTSEQDIAGR_COMPONENT_NAME = "Sequence diagrams";
    private static final String PLOTAGGREGATEDCALLTREE_COMPONENT_NAME = "Aggregated call tree";
    private static final String PLOTCALLTREE_COMPONENT_NAME = "Trace call trees";

    private static boolean dispatchTasks() {
        boolean retVal = true;
        int numRequestedTasks = 0;

        TraceReconstructionPlugin mtReconstrFilter = null;
        try {
            final List<AbstractMessageTraceProcessingPlugin> msgTraceProcessingComponents = new ArrayList<AbstractMessageTraceProcessingPlugin>();
            final List<AbstractExecutionTraceProcessingPlugin> execTraceProcessingComponents = new ArrayList<AbstractExecutionTraceProcessingPlugin>();
            final List<AbstractInvalidExecutionTraceProcessingPlugin> invalidExecTraceProcessingComponents = new ArrayList<AbstractInvalidExecutionTraceProcessingPlugin>();
            // fill list of msgTraceProcessingComponents:
            AbstractMessageTraceProcessingPlugin componentPrintMsgTrace = null;
            if (TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PRINTMSGTRACES)) {
                numRequestedTasks++;
                componentPrintMsgTrace = TraceAnalysisTool.task_createMessageTraceDumpComponent(
                        TraceAnalysisTool.PRINTMSGTRACE_COMPONENT_NAME,
                        TraceAnalysisTool.outputDir + File.separator
                        + TraceAnalysisTool.outputFnPrefix);
                msgTraceProcessingComponents.add(componentPrintMsgTrace);
            }
            AbstractExecutionTraceProcessingPlugin componentPrintExecTrace = null;
            if (TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PRINTEXECTRACES)) {
                numRequestedTasks++;
                componentPrintExecTrace = TraceAnalysisTool.task_createExecutionTraceDumpComponent(
                        TraceAnalysisTool.PRINTEXECTRACE_COMPONENT_NAME,
                        TraceAnalysisTool.outputDir + File.separator
                        + TraceAnalysisTool.outputFnPrefix
                        + Constants.EXECUTION_TRACES_FN_PREFIX
                        + ".txt", false);
                execTraceProcessingComponents.add(componentPrintExecTrace);
            }
            AbstractInvalidExecutionTraceProcessingPlugin componentPrintInvalidTrace = null;
            if (TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES)) {
                numRequestedTasks++;
                componentPrintInvalidTrace = TraceAnalysisTool.task_createInvalidExecutionTraceDumpComponent(
                        TraceAnalysisTool.PRINTINVALIDEXECTRACE_COMPONENT_NAME,
                        TraceAnalysisTool.outputDir + File.separator
                        + TraceAnalysisTool.outputFnPrefix
                        + Constants.INVALID_TRACES_FN_PREFIX
                        + ".txt", true);
                invalidExecTraceProcessingComponents.add(componentPrintInvalidTrace);
            }
            AbstractMessageTraceProcessingPlugin componentPlotSeqDiagr = null;
            if (retVal
                    && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTSEQDS)) {
                numRequestedTasks++;
                componentPlotSeqDiagr = TraceAnalysisTool.task_createSequenceDiagramPlotComponent(
                        TraceAnalysisTool.PLOTSEQDIAGR_COMPONENT_NAME,
                        TraceAnalysisTool.outputDir + File.separator
                        + TraceAnalysisTool.outputFnPrefix);
                msgTraceProcessingComponents.add(componentPlotSeqDiagr);
            }
            ComponentDependencyGraphPlugin componentPlotComponentDepGraph = null;
            if (retVal
                    && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTCOMPONENTDEPG)) {
                numRequestedTasks++;
                componentPlotComponentDepGraph = TraceAnalysisTool.task_createComponentDependencyGraphPlotComponent(TraceAnalysisTool.PLOTCOMPONENTDEPGRAPH_COMPONENT_NAME);
                msgTraceProcessingComponents.add(componentPlotComponentDepGraph);
            }
            ContainerDependencyGraphPlugin componentPlotContainerDepGraph = null;
            if (retVal
                    && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG)) {
                numRequestedTasks++;
                componentPlotContainerDepGraph = TraceAnalysisTool.task_createContainerDependencyGraphPlotComponent(TraceAnalysisTool.PLOTCONTAINERDEPGRAPH_COMPONENT_NAME);
                msgTraceProcessingComponents.add(componentPlotContainerDepGraph);
            }
            OperationDependencyGraphPlugin componentPlotOperationDepGraph = null;
            if (retVal
                    && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTOPERATIONDEPG)) {
                numRequestedTasks++;
                componentPlotOperationDepGraph = TraceAnalysisTool.task_createOperationDependencyGraphPlotComponent(TraceAnalysisTool.PLOTOPERATIONDEPGRAPH_COMPONENT_NAME);
                msgTraceProcessingComponents.add(componentPlotOperationDepGraph);
            }
            AbstractMessageTraceProcessingPlugin componentPlotCallTrees = null;
            if (retVal
                    && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTCALLTREES)) {
                numRequestedTasks++;
                componentPlotCallTrees = TraceAnalysisTool.task_createCallTreesPlotComponent(
                        TraceAnalysisTool.PLOTCALLTREE_COMPONENT_NAME,
                        TraceAnalysisTool.outputDir + File.separator
                        + TraceAnalysisTool.outputFnPrefix);
                msgTraceProcessingComponents.add(componentPlotCallTrees);
            }
            AggregatedAllocationComponentOperationCallTreePlugin componentPlotAggregatedCallTree = null;
            if (retVal
                    && TraceAnalysisTool.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTAGGREGATEDCALLTREE)) {
                numRequestedTasks++;
                componentPlotAggregatedCallTree = TraceAnalysisTool.task_createAggregatedCallTreePlotComponent(TraceAnalysisTool.PLOTAGGREGATEDCALLTREE_COMPONENT_NAME);
                msgTraceProcessingComponents.add(componentPlotAggregatedCallTree);
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

            final List<AbstractTraceProcessingPlugin> allTraceProcessingComponents = new ArrayList<AbstractTraceProcessingPlugin>();
            allTraceProcessingComponents.addAll(msgTraceProcessingComponents);
            allTraceProcessingComponents.addAll(execTraceProcessingComponents);
            allTraceProcessingComponents.addAll(invalidExecTraceProcessingComponents);
            final AnalysisInstance analysisInstance = new AnalysisInstance();
            analysisInstance.setLogReader(new FSReader(
                    TraceAnalysisTool.inputDirs));
            // analysisInstance.setLogReader(new
            // JMSReader("tcp://localhost:3035/","queue1"));

            mtReconstrFilter = new TraceReconstructionPlugin(
                    TraceAnalysisTool.TRACERECONSTR_COMPONENT_NAME,
                    TraceAnalysisTool.systemEntityFactory,
                    TraceAnalysisTool.maxTraceDurationMillis,
                    TraceAnalysisTool.ignoreInvalidTraces,
                    TraceAnalysisTool.traceEquivalenceClassMode,
                    TraceAnalysisTool.selectedTraces,
                    TraceAnalysisTool.ignoreRecordsBeforeTimestamp,
                    TraceAnalysisTool.ignoreRecordsAfterTimestamp);
            for (final AbstractMessageTraceProcessingPlugin c : msgTraceProcessingComponents) {
                mtReconstrFilter.getMessageTraceEventProviderPort().addListener(c);
            }
            for (final AbstractExecutionTraceProcessingPlugin c : execTraceProcessingComponents) {
                mtReconstrFilter.getExecutionTraceEventProviderPort().addListener(c);
            }
            for (final AbstractInvalidExecutionTraceProcessingPlugin c : invalidExecTraceProcessingComponents) {
                mtReconstrFilter.getInvalidExecutionTraceEventPort().addListener(c);
            }

            final ExecutionRecordTransformationPlugin execRecTransformer = new ExecutionRecordTransformationPlugin(
                    TraceAnalysisTool.EXEC_TRACE_RECONSTR_COMPONENT_NAME,
                    TraceAnalysisTool.systemEntityFactory);
            execRecTransformer.addListener(mtReconstrFilter);
            analysisInstance.registerPlugin(execRecTransformer);

            for (final IAnalysisPlugin c : allTraceProcessingComponents) {
                analysisInstance.registerPlugin(c);
            }
            analysisInstance.registerPlugin(mtReconstrFilter);
            // END test with new meta-model

            int numErrorCount = 0;
            try {
                analysisInstance.run();

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
                retVal = TraceAnalysisTool.task_genTraceEquivalenceReportForTraceSet(
                        TraceAnalysisTool.outputDir + File.separator
                        + TraceAnalysisTool.outputFnPrefix,
                        mtReconstrFilter);
            }

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

    /**
     * Reads the traces from the directory inputDirName and write the sequence
     * diagrams for traces with IDs given in traceSet to the directory
     * outputFnPrefix. If traceSet is null, a sequence diagram for each trace is
     * generated.
     *
     * @param inputDirName
     * @param outputFnPrefix
     * @param traceSet
     */
    private static AbstractMessageTraceProcessingPlugin task_createSequenceDiagramPlotComponent(
            final String name, final String outputFnPrefix) throws IOException {
        final String outputFnBase = new File(outputFnPrefix
                + Constants.SEQUENCE_DIAGRAM_FN_PREFIX).getCanonicalPath();
        final AbstractMessageTraceProcessingPlugin sqdWriter = new AbstractMessageTraceProcessingPlugin(
                name, TraceAnalysisTool.systemEntityFactory) {

            public void newEvent(final MessageTrace t)
                    throws EventProcessingException {
                try {
                    SequenceDiagramPlugin.writePicForMessageTrace(this.getSystemEntityFactory(), t, outputFnBase + "-"
                            + t.getTraceId() + ".pic",
                            TraceAnalysisTool.shortLabels);
                    this.reportSuccess(t.getTraceId());
                } catch (final FileNotFoundException ex) {
                    this.reportError(t.getTraceId());
                    throw new TraceProcessingException("File not found", ex);
                }
            }

            @Override
            public void printStatusMessage() {
                super.printStatusMessage();
                final int numPlots = this.getSuccessCount();
                final long lastSuccessTracesId = this.getLastTraceIdSuccess();
                System.out.println("Wrote " + numPlots + " sequence diagram"
                        + (numPlots > 1 ? "s" : "") + " to file"
                        + (numPlots > 1 ? "s" : "") + " with name pattern '"
                        + outputFnBase + "-<traceId>.pic'");
                System.out.println("Pic files can be converted using the pic2plot tool (package plotutils)");
                System.out.println("Example: pic2plot -T svg " + outputFnBase
                        + "-"
                        + ((numPlots > 0) ? lastSuccessTracesId : "<traceId>")
                        + ".pic > " + outputFnBase + "-"
                        + ((numPlots > 0) ? lastSuccessTracesId : "<traceId>")
                        + ".svg");
            }

            public boolean execute() {
                return true; // no need to do anything here
            }

            public void terminate(final boolean error) {
                // no need to do anything here
            }
        };
        return sqdWriter;
    }

    /**
     * Reads the traces from the directory inputDirName and write the dependency
     * graph to the directory outputFnPrefix. If traceSet is null, a dependency
     * graph containing the information of all traces is generated.
     *
     * @param inputDirName
     * @param outputFnPrefix
     * @param traceSet
     */
    private static ComponentDependencyGraphPlugin task_createComponentDependencyGraphPlotComponent(
            final String name) {
        final ComponentDependencyGraphPlugin depGraph = new ComponentDependencyGraphPlugin(
                name, TraceAnalysisTool.systemEntityFactory);
        return depGraph;
    }

    private static ContainerDependencyGraphPlugin task_createContainerDependencyGraphPlotComponent(
            final String name) {
        final ContainerDependencyGraphPlugin depGraph = new ContainerDependencyGraphPlugin(
                name, TraceAnalysisTool.systemEntityFactory);
        return depGraph;
    }

    private static OperationDependencyGraphPlugin task_createOperationDependencyGraphPlotComponent(
            final String name) {
        final OperationDependencyGraphPlugin depGraph = new OperationDependencyGraphPlugin(
                name, TraceAnalysisTool.systemEntityFactory);
        return depGraph;
    }

    /**
     * Reads the traces from the directory inputDirName and write the call tree
     * for traces to the directory outputFnPrefix. If traceSet is null, a call
     * tree containing the information of all traces is generated.
     *
     * @param inputDirName
     * @param outputFnPrefix
     * @param traceSet
     */
    private static AggregatedAllocationComponentOperationCallTreePlugin task_createAggregatedCallTreePlotComponent(
            final String name) {
        final AggregatedAllocationComponentOperationCallTreePlugin callTree = new AggregatedAllocationComponentOperationCallTreePlugin(
                name,
                TraceAnalysisTool.allocationComponentOperationPairFactory,
                TraceAnalysisTool.systemEntityFactory);
        return callTree;
    }

    private static AbstractMessageTraceProcessingPlugin task_createCallTreesPlotComponent(
            final String name, final String outputFnPrefix) throws IOException {
        final String outputFnBase = new File(outputFnPrefix
                + Constants.CALL_TREE_FN_PREFIX).getCanonicalPath();

        final AbstractMessageTraceProcessingPlugin ctWriter = new AbstractMessageTraceProcessingPlugin(
                name, TraceAnalysisTool.systemEntityFactory) {

            public void newEvent(final MessageTrace t)
                    throws EventProcessingException {
                try {
                    final TraceCallTreeNode rootNode = new TraceCallTreeNode(
                            AbstractSystemSubRepository.ROOT_ELEMENT_ID,
                            TraceAnalysisTool.systemEntityFactory,
                            TraceAnalysisTool.allocationComponentOperationPairFactory,
                            TraceAnalysisTool.allocationComponentOperationPairFactory.rootPair,
                            true); // rootNode
                    AbstractCallTreePlugin.writeDotForMessageTrace(
                            TraceAnalysisTool.systemEntityFactory, rootNode, t,
                            outputFnBase + "-" + t.getTraceId(), false,
                            TraceAnalysisTool.shortLabels); // no weights
                    this.reportSuccess(t.getTraceId());
                } catch (final FileNotFoundException ex) {
                    this.reportError(t.getTraceId());
                    throw new TraceProcessingException("File not found", ex);
                }
            }

            @Override
            public void printStatusMessage() {
                super.printStatusMessage();
                final int numPlots = this.getSuccessCount();
                final long lastSuccessTracesId = this.getLastTraceIdSuccess();
                System.out.println("Wrote " + numPlots + " call tree"
                        + (numPlots > 1 ? "s" : "") + " to file"
                        + (numPlots > 1 ? "s" : "") + " with name pattern '"
                        + outputFnBase + "-<traceId>.dot'");
                System.out.println("Dot files can be converted using the dot tool");
                System.out.println("Example: dot -T svg " + outputFnBase + "-"
                        + ((numPlots > 0) ? lastSuccessTracesId : "<traceId>")
                        + ".dot > " + outputFnBase + "-"
                        + ((numPlots > 0) ? lastSuccessTracesId : "<traceId>")
                        + ".svg");
            }

            public boolean execute() {
                return true; // no need to do anything here
            }

            public void terminate(final boolean error) {
                // no need to do anything here
            }
        };
        return ctWriter;
    }

    /**
     * Reads the traces from the directory inputDirName and write the message
     * trace representation for traces with IDs given in traceSet to the
     * directory outputFnPrefix. If traceSet is null, a message trace for each
     * trace is generated.
     *
     * @param inputDirName
     * @param outputFnPrefix
     * @param traceSet
     */
    private static AbstractMessageTraceProcessingPlugin task_createMessageTraceDumpComponent(
            final String name, final String outputFnPrefix) throws IOException,
            InvalidTraceException, MonitoringLogReaderException,
            MonitoringRecordConsumerException {
        final String outputFn = new File(outputFnPrefix
                + Constants.MESSAGE_TRACES_FN_PREFIX + ".txt").getCanonicalPath();
        final AbstractMessageTraceProcessingPlugin mtWriter = new AbstractMessageTraceProcessingPlugin(
                name, TraceAnalysisTool.systemEntityFactory) {

            PrintStream ps = new PrintStream(new FileOutputStream(outputFn));

            public void newEvent(final MessageTrace t)
                    throws EventProcessingException {
                this.reportSuccess(t.getTraceId());
                this.ps.println(t);
            }

            @Override
            public void printStatusMessage() {
                super.printStatusMessage();
                final int numTraces = this.getSuccessCount();
                System.out.println("Wrote " + numTraces + " message trace"
                        + (numTraces > 1 ? "s" : "") + " to file '" + outputFn
                        + "'");
            }

            @Override
            public void terminate(final boolean error) {
                if (this.ps != null) {
                    this.ps.close();
                }
            }

            public boolean execute() {
                return true; // no need to do anything here
            }
        };
        return mtWriter;
    }

    /**
     * Reads the traces from the directory inputDirName and write the execution
     * trace representation for traces with IDs given in traceSet to the
     * directory outputFnPrefix. If traceSet is null, an execution trace for
     * each trace is generated.
     *
     * @param inputDirName
     * @param outputFnPrefix
     * @param traceSet
     */
    private static AbstractExecutionTraceProcessingPlugin task_createExecutionTraceDumpComponent(
            final String name, final String outputFn, final boolean artifactMode)
            throws IOException, MonitoringLogReaderException,
            MonitoringRecordConsumerException {
        final String myOutputFn = new File(outputFn).getCanonicalPath();
        final AbstractExecutionTraceProcessingPlugin etWriter = new AbstractExecutionTraceProcessingPlugin(
                name, TraceAnalysisTool.systemEntityFactory) {

            final PrintStream ps = new PrintStream(new FileOutputStream(
                    myOutputFn));

            public void newEvent(final ExecutionTrace t)
                    throws EventProcessingException {
                this.ps.println(t);
                this.reportSuccess(t.getTraceId());
            }

            @Override
            public void printStatusMessage() {
                super.printStatusMessage();
                final int numTraces = this.getSuccessCount();
                System.out.println("Wrote " + numTraces + " execution trace"
                        + (artifactMode ? " artifact" : "")
                        + (numTraces > 1 ? "s" : "") + " to file '"
                        + myOutputFn + "'");
            }

            @Override
            public void terminate(final boolean error) {
                if (this.ps != null) {
                    this.ps.close();
                }
            }

            public boolean execute() {
                return true; // no need to do anything here
            }
        };
        return etWriter;
    }

    /**
     * Reads the traces from the directory inputDirName and write the execution
     * trace representation for traces with IDs given in traceSet to the
     * directory outputFnPrefix. If traceSet is null, an execution trace for
     * each trace is generated.
     *
     * @param inputDirName
     * @param outputFnPrefix
     * @param traceSet
     */
    private static AbstractInvalidExecutionTraceProcessingPlugin task_createInvalidExecutionTraceDumpComponent(
            final String name, final String outputFn, final boolean artifactMode)
            throws IOException, MonitoringLogReaderException,
            MonitoringRecordConsumerException {
        final String myOutputFn = new File(outputFn).getCanonicalPath();
        final AbstractInvalidExecutionTraceProcessingPlugin etWriter = new AbstractInvalidExecutionTraceProcessingPlugin(
                name, TraceAnalysisTool.systemEntityFactory) {

            final PrintStream ps = new PrintStream(new FileOutputStream(
                    myOutputFn));

            public void newEvent(final InvalidExecutionTrace t)
                    throws EventProcessingException {
                this.ps.println(t.getInvalidExecutionTrace());
                this.reportSuccess(t.getInvalidExecutionTrace().getTraceId());
            }

            @Override
            public void printStatusMessage() {
                super.printStatusMessage();
                final int numTraces = this.getSuccessCount();
                System.out.println("Wrote " + numTraces + " execution trace"
                        + (artifactMode ? " artifact" : "")
                        + (numTraces > 1 ? "s" : "") + " to file '"
                        + myOutputFn + "'");
            }

            @Override
            public void terminate(final boolean error) {
                if (this.ps != null) {
                    this.ps.close();
                }
            }

            public boolean execute() {
                return true; // no need to do anything here
            }
        };
        return etWriter;
    }

    private static boolean task_genTraceEquivalenceReportForTraceSet(
            final String outputFnPrefix, final TraceReconstructionPlugin trf)
            throws IOException, MonitoringLogReaderException,
            MonitoringRecordConsumerException {
        boolean retVal = true;
        final String outputFn = new File(outputFnPrefix
                + Constants.TRACE_EQUIV_CLASSES_FN_PREFIX + ".txt").getCanonicalPath();
        PrintStream ps = null;
        try {
            ps = new PrintStream(new FileOutputStream(outputFn));
            int numClasses = 0;
            final HashMap<ExecutionTrace, Integer> classMap = trf.getEquivalenceClassMap();
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

    private static boolean task_initBasicJmsReader(final String jmsProviderUrl,
            final String jmsDestination) throws IOException,
            MonitoringLogReaderException, MonitoringRecordConsumerException {
        final boolean retVal = true;

        TraceAnalysisTool.log.info("Trying to start JMS Listener to "
                + jmsProviderUrl + " " + jmsDestination);
        /* Read log data and collect execution traces */
        final AnalysisInstance analysisInstance = new AnalysisInstance();
        analysisInstance.setLogReader(new JMSReader(jmsProviderUrl,
                jmsDestination));

        final MonitoringRecordTypeLogger recordTypeLogger = new MonitoringRecordTypeLogger();
        analysisInstance.registerPlugin(recordTypeLogger);

        // MessageTraceRepository seqRepConsumer = new MessageTraceRepository();
        // analysisInstance.addRecordConsumer(seqRepConsumer);

        /* @Matthias: Deactivated this, since the ant task didn't run (Andre) */
        // BriefJavaFxInformer bjfx = new BriefJavaFxInformer();
        // analysisInstance.addRecordConsumer(bjfx);

        analysisInstance.run();
        return retVal;
    }

    private static boolean task_initBasicJmsReaderJavaFx(
            final String jmsProviderUrl, final String jmsDestination)
            throws IOException, MonitoringLogReaderException,
            MonitoringRecordConsumerException {
        final boolean retVal = true;

        TraceAnalysisTool.log.info("Trying to start JMS Listener to "
                + jmsProviderUrl + " " + jmsDestination);
        /* Read log data and collect execution traces */
        final AnalysisInstance analysisInstance = new AnalysisInstance();
        analysisInstance.setLogReader(new JMSReader(jmsProviderUrl,
                jmsDestination));

        final MonitoringRecordTypeLogger recordTypeLogger = new MonitoringRecordTypeLogger();
        analysisInstance.registerPlugin(recordTypeLogger);

        // MessageTraceRepository seqRepConsumer = new MessageTraceRepository();
        // analysisInstance.addRecordConsumer(seqRepConsumer);

        /* @Matthias: Deactivated this, since the ant task didn't run (Andre) */
        final BriefJavaFxInformer bjfx = new BriefJavaFxInformer();

        analysisInstance.run();
        return retVal;
    }

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
    public static void createMessageTraceFiltersAndRegisterMessageTraceListener(
            AnalysisInstance tpanInstance,
            final BriefJavaFxInformer messageTraceListener) {
        if (tpanInstance == null) {
            tpanInstance = new AnalysisInstance();
        }

        TraceReconstructionPlugin mtReconstrFilter = null;
        mtReconstrFilter = new TraceReconstructionPlugin(
                TraceAnalysisTool.TRACERECONSTR_COMPONENT_NAME,
                TraceAnalysisTool.systemEntityFactory, 60 * 1000, // maxTraceDurationMillis,
                true, // ignoreInvalidTraces,
                TraceEquivalenceClassModes.DISABLED, // traceEquivalenceClassMode,
                // // = every trace
                // passes, not only
                // unique trace classes
                null, // selectedTraces, // null means all
                TraceAnalysisTool.ignoreRecordsBeforeTimestamp, // default
                // Long.MIN
                TraceAnalysisTool.ignoreRecordsAfterTimestamp); // default
        // Long.MAX
        mtReconstrFilter.getMessageTraceEventProviderPort().addListener(
                messageTraceListener);
        mtReconstrFilter.getInvalidExecutionTraceEventPort().addListener(
                messageTraceListener.getJfxBrokenExecutionTraceReceiver()); // i
        // know
        // that
        // its
        // dirty

        TraceReconstructionPlugin uniqueMtReconstrFilter = null;
        uniqueMtReconstrFilter = new TraceReconstructionPlugin(
                TraceAnalysisTool.TRACERECONSTR_COMPONENT_NAME,
                TraceAnalysisTool.systemEntityFactory, 60 * 1000, // maxTraceDurationMillis,
                true, // ignoreInvalidTraces,
                TraceEquivalenceClassModes.ALLOCATION, // traceEquivalenceClassMode,
                // // = every trace
                // passes, not only
                // unique trace classes
                null, // selectedTraces, // null means all
                TraceAnalysisTool.ignoreRecordsBeforeTimestamp, // default
                // Long.MIN
                TraceAnalysisTool.ignoreRecordsAfterTimestamp); // default
        // Long.MAX
        uniqueMtReconstrFilter.getMessageTraceEventProviderPort().addListener(
                messageTraceListener.getJfxUniqueTr()); // i know that its
        // dirty; i (andre) like
        // it because it's
        // basically a port

        final ExecutionRecordTransformationPlugin execRecTransformer = new ExecutionRecordTransformationPlugin(
                TraceAnalysisTool.EXEC_TRACE_RECONSTR_COMPONENT_NAME,
                TraceAnalysisTool.systemEntityFactory);
        execRecTransformer.addListener(mtReconstrFilter);
        execRecTransformer.addListener(uniqueMtReconstrFilter);
        tpanInstance.registerPlugin(execRecTransformer);
        System.out.println("MessageTraceListener registered");
    }
}
