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
import kieker.analysis.plugin.traceAnalysis.executionRecordTransformation.ExecutionRecordTransformationPlugin1;
import kieker.analysis.plugin.traceAnalysis.traceReconstruction.InvalidTraceException;
import kieker.analysis.plugin.traceAnalysis.traceReconstruction.TraceProcessingException;
import kieker.analysis.plugin.traceAnalysis.traceReconstruction.TraceReconstructionPlugin1;
import kieker.analysis.plugin.traceAnalysis.traceReconstruction.TraceReconstructionPlugin1.TraceEquivalenceClassModes;
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
public class TraceAnalysisTool1 {

    private static final Log log = LogFactory.getLog(TraceAnalysisTool1.class);
    private static final SystemModelRepository systemEntityFactory = new SystemModelRepository();
    private static final AllocationComponentOperationPairFactory allocationComponentOperationPairFactory = new AllocationComponentOperationPairFactory(
            TraceAnalysisTool1.systemEntityFactory);
    private static final AssemblyComponentOperationPairFactory assemblyComponentOperationPairFactory = new AssemblyComponentOperationPairFactory(
            TraceAnalysisTool1.systemEntityFactory);
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
    private static int maxTraceDurationMillis = TraceReconstructionPlugin1.MAX_DURATION_MILLIS; // infinite
    private static long ignoreRecordsBeforeTimestamp = TraceReconstructionPlugin1.MIN_TIMESTAMP;
    private static long ignoreRecordsAfterTimestamp = TraceReconstructionPlugin1.MAX_TIMESTAMP;
    public static final String DATE_FORMAT_PATTERN_CMD_USAGE_HELP = Constants.DATE_FORMAT_PATTERN.replaceAll("'", ""); // only for usage info
    // private static final String CMD_OPT_NAME_TASK_INITJMSREADER =
    // "init-basic-JMS-reader";
    // private static final String CMD_OPT_NAME_TASK_INITJMSREADERJFX =
    // "init-basic-JMS-readerJavaFx";
    private static final Vector<Option> options = new Vector<Option>();

    static {
        // TODO: OptionGroups?

        TraceAnalysisTool1.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_INPUTDIRS).withArgName("dir1 ... dirN").hasArgs().isRequired(true).withDescription(
                "Log directories to read data from").withValueSeparator('=').create("i"));
        TraceAnalysisTool1.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_OUTPUTDIR).withArgName("dir").hasArg(
                true).isRequired(true).withDescription(
                "Directory for the generated file(s)").withValueSeparator('=').create("o"));
        TraceAnalysisTool1.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_OUTPUTFNPREFIX).withArgName("prefix").hasArg(true).isRequired(false).withDescription(
                "Prefix for output filenames\n").withValueSeparator('=').create("p"));

        // OptionGroup cmdlOptGroupTask = new OptionGroup();
        // cmdlOptGroupTask.isRequired();
        TraceAnalysisTool1.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_TASK_PLOTSEQDS).hasArg(false).withDescription(
                "Generate and store sequence diagrams (.pic files)").create());
        TraceAnalysisTool1.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_TASK_PLOTCOMPONENTDEPG).hasArg(false).withDescription(
                "Generate and store a component dependency graph (.dot file)").create());
        TraceAnalysisTool1.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG).hasArg(false).withDescription(
                "Generate and store a container dependency graph (.dot file)").create());
        TraceAnalysisTool1.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_TASK_PLOTOPERATIONDEPG).hasArg(false).withDescription(
                "Generate and store an operation dependency graph (.dot file)").create());
        TraceAnalysisTool1.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_TASK_PLOTAGGREGATEDCALLTREE).hasArg(
                false).withDescription(
                "Generate and store an aggregated call tree (.dot files)").create());
        TraceAnalysisTool1.options.add(OptionBuilder.withLongOpt(Constants.CMD_OPT_NAME_TASK_PLOTCALLTREES).hasArg(false).withDescription(
                "Generate and store call trees for the selected traces (.dot files)").create());
        TraceAnalysisTool1.options.add(OptionBuilder.withLongOpt(Constants.CMD_OPT_NAME_TASK_PRINTMSGTRACES).hasArg(false).withDescription(
                "Save message trace representations of valid traces (.txt files)").create());
        TraceAnalysisTool1.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_TASK_PRINTEXECTRACES).hasArg(false).withDescription(
                "Save execution trace representations of valid traces (.txt files)").create());
        TraceAnalysisTool1.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES).hasArg(false).withDescription(
                "Save a execution trace representations of invalid trace artifacts (.txt files)").create());
        TraceAnalysisTool1.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_TASK_EQUIVCLASSREPORT).hasArg(false).withDescription(
                "Output an overview about the trace equivalence classes").create());

        /*
         * These tasks should be moved to a dedicated tool, since this tool
         * covers trace analysis
         */
        // cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_INITJMSREADER).hasArg(false).withDescription("Creates a jms reader and shows incomming data in the command line").create());
        // cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_INITJMSREADERJFX).hasArg(false).withDescription("Creates a jms reader and shows incomming data in the command line and visualizes with javafx").create());

        // cmdlOpts.addOptionGroup(cmdlOptGroupTask);
        TraceAnalysisTool1.options.add(OptionBuilder.withLongOpt(Constants.CMD_OPT_NAME_SELECTTRACES).withArgName("id0 ... idn").hasArgs().isRequired(false).withDescription(
                "Consider only the traces identified by the comma-separated list of trace IDs. Defaults to all traces.").create());
        TraceAnalysisTool1.options.add(OptionBuilder.withLongOpt(Constants.CMD_OPT_NAME_TRACEEQUIVCLASSMODE).withArgName(
                String.format(
                "%s|%s|%s",
                Constants.TRACE_EQUIVALENCE_MODE_STR_ALLOCATION,
                Constants.TRACE_EQUIVALENCE_MODE_STR_ASSEMBLY,
                Constants.TRACE_EQUIVALENCE_MODE_STR_DISABLED)).hasArg(true).isRequired(false).withDescription(
                "If selected, the selected tasks are performed on representatives of the equivalence classes only.").create());
        TraceAnalysisTool1.options.add(OptionBuilder.withLongOpt(Constants.CMD_OPT_NAME_IGNOREINVALIDTRACES).hasArg(false).isRequired(false).withDescription(
                "If selected, the execution aborts on the occurence of an invalid trace.").create());
        TraceAnalysisTool1.options.add(OptionBuilder.withLongOpt(Constants.CMD_OPT_NAME_MAXTRACEDURATION).withArgName("duration in ms").hasArg().isRequired(false).withDescription(
                "Threshold (in milliseconds) after which an incomplete trace becomes invalid. Defaults to infinity.").create());
        TraceAnalysisTool1.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_IGNORERECORDSBEFOREDATE).withArgName(
                TraceAnalysisTool1.DATE_FORMAT_PATTERN_CMD_USAGE_HELP).hasArg().isRequired(false).withDescription(
                "Records logged before this date (UTC timezone) are ignored.").create());
        TraceAnalysisTool1.options.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_IGNORERECORDSAFTERDATE).withArgName(
                TraceAnalysisTool1.DATE_FORMAT_PATTERN_CMD_USAGE_HELP).hasArg().isRequired(false).withDescription(
                "Records logged after this date (UTC timezone) are ignored.").create());
        TraceAnalysisTool1.options.add(OptionBuilder.withLongOpt(Constants.CMD_OPT_NAME_SHORTLABELS).hasArg(false).isRequired(false).withDescription(
                "If selected, the hostnames of the executions are NOT considered.").create());
        for (final Option o : TraceAnalysisTool1.options) {
            TraceAnalysisTool1.cmdlOpts.addOption(o);
        }
        TraceAnalysisTool1.cmdHelpFormatter.setOptionComparator(new Comparator() {

            public int compare(final Object o1, final Object o2) {
                if (o1 == o2) {
                    return 0;
                }
                final int posO1 = TraceAnalysisTool1.options.indexOf(o1);
                final int posO2 = TraceAnalysisTool1.options.indexOf(o2);
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
            TraceAnalysisTool1.cmdl = TraceAnalysisTool1.cmdlParser.parse(
                    TraceAnalysisTool1.cmdlOpts, args);
        } catch (final ParseException e) {
            TraceAnalysisTool1.printUsage();
            System.err.println("\nError parsing arguments: " + e.getMessage());
            return false;
        }
        return true;
    }

    private static void printUsage() {
        TraceAnalysisTool1.cmdHelpFormatter.printHelp(80,
                TraceAnalysisTool1.class.getName(), "",
                TraceAnalysisTool1.cmdlOpts, "", true);
    }

    private static boolean initFromArgs() {
        TraceAnalysisTool1.inputDirs = TraceAnalysisTool1.cmdl.getOptionValues(Constants.CMD_OPT_NAME_INPUTDIRS);

        TraceAnalysisTool1.outputDir = TraceAnalysisTool1.cmdl.getOptionValue(Constants.CMD_OPT_NAME_OUTPUTDIR)
                + File.separator;
        TraceAnalysisTool1.outputFnPrefix = TraceAnalysisTool1.cmdl.getOptionValue(Constants.CMD_OPT_NAME_OUTPUTFNPREFIX, "");
        if (TraceAnalysisTool1.cmdl.hasOption(Constants.CMD_OPT_NAME_SELECTTRACES)) { /*
             * Parse
             * list of
             * trace Ids
             */
            final String[] traceIdList = TraceAnalysisTool1.cmdl.getOptionValues(Constants.CMD_OPT_NAME_SELECTTRACES);
            TraceAnalysisTool1.selectedTraces = new TreeSet<Long>();
            final int numSelectedTraces = traceIdList.length;
            try {
                for (final String idStr : traceIdList) {
                    TraceAnalysisTool1.selectedTraces.add(Long.valueOf(idStr));
                }
                TraceAnalysisTool1.log.info(numSelectedTraces + " trace"
                        + (numSelectedTraces > 1 ? "s" : "") + " selected");
            } catch (final Exception e) {
                System.err.println("\nFailed to parse list of trace IDs: "
                        + traceIdList + "(" + e.getMessage() + ")");
                TraceAnalysisTool1.log.error(
                        "Failed to parse list of trace IDs: " + traceIdList, e);
                return false;
            }
        }

        TraceAnalysisTool1.shortLabels = TraceAnalysisTool1.cmdl.hasOption(Constants.CMD_OPT_NAME_SHORTLABELS);
        TraceAnalysisTool1.ignoreInvalidTraces = TraceAnalysisTool1.cmdl.hasOption(Constants.CMD_OPT_NAME_IGNOREINVALIDTRACES);

        final String traceEquivClassModeStr = TraceAnalysisTool1.cmdl.getOptionValue(Constants.CMD_OPT_NAME_TRACEEQUIVCLASSMODE,
                null);
        if (traceEquivClassModeStr == null
                || traceEquivClassModeStr.equals(Constants.TRACE_EQUIVALENCE_MODE_STR_DISABLED)) {
            TraceAnalysisTool1.traceEquivalenceClassMode = TraceEquivalenceClassModes.DISABLED;
        } else {
            if (traceEquivClassModeStr.equals(Constants.TRACE_EQUIVALENCE_MODE_STR_ALLOCATION)) {
                TraceAnalysisTool1.traceEquivalenceClassMode = TraceEquivalenceClassModes.ALLOCATION;
            } else if (traceEquivClassModeStr.equals(Constants.TRACE_EQUIVALENCE_MODE_STR_ASSEMBLY)) {
                TraceAnalysisTool1.traceEquivalenceClassMode = TraceEquivalenceClassModes.ASSEMBLY;
            } else {
                TraceAnalysisTool1.log.error("Invalid value for property "
                        + Constants.CMD_OPT_NAME_TRACEEQUIVCLASSMODE + ":"
                        + traceEquivClassModeStr);
                return false;
            }
        }

        final String maxTraceDurationStr = TraceAnalysisTool1.cmdl.getOptionValue(Constants.CMD_OPT_NAME_MAXTRACEDURATION,
                TraceAnalysisTool1.maxTraceDurationMillis + "");
        try {
            TraceAnalysisTool1.maxTraceDurationMillis = Integer.parseInt(maxTraceDurationStr);
        } catch (final NumberFormatException exc) {
            System.err.println("\nFailed to parse int value of property "
                    + Constants.CMD_OPT_NAME_MAXTRACEDURATION
                    + " (must be an integer): " + maxTraceDurationStr);
            TraceAnalysisTool1.log.error(
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
            final String ignoreRecordsBeforeTimestampString = TraceAnalysisTool1.cmdl.getOptionValue(
                    Constants.CMD_OPT_NAME_IGNORERECORDSBEFOREDATE,
                    null);
            final String ignoreRecordsAfterTimestampString = TraceAnalysisTool1.cmdl.getOptionValue(
                    Constants.CMD_OPT_NAME_IGNORERECORDSAFTERDATE, null);
            if (ignoreRecordsBeforeTimestampString != null) {
                final Date ignoreBeforeDate = m_ISO8601UTC.parse(ignoreRecordsBeforeTimestampString);
                TraceAnalysisTool1.ignoreRecordsBeforeTimestamp = ignoreBeforeDate.getTime()
                        * (1000 * 1000);
                TraceAnalysisTool1.log.info("Ignoring records before "
                        + m_ISO8601UTC.format(ignoreBeforeDate) + " ("
                        + TraceAnalysisTool1.ignoreRecordsBeforeTimestamp + ")");
            }
            if (ignoreRecordsAfterTimestampString != null) {
                final Date ignoreAfterDate = m_ISO8601UTC.parse(ignoreRecordsAfterTimestampString);
                TraceAnalysisTool1.ignoreRecordsAfterTimestamp = ignoreAfterDate.getTime()
                        * (1000 * 1000);
                TraceAnalysisTool1.log.info("Ignoring records after "
                        + m_ISO8601UTC.format(ignoreAfterDate) + " ("
                        + TraceAnalysisTool1.ignoreRecordsAfterTimestamp + ")");
            }
        } catch (final java.text.ParseException ex) {
            System.err.println("Error parsing date/time string. Please use the following pattern: "
                    + TraceAnalysisTool1.DATE_FORMAT_PATTERN_CMD_USAGE_HELP);
            TraceAnalysisTool1.log.error(
                    "Error parsing date/time string. Please use the following pattern: "
                    + TraceAnalysisTool1.DATE_FORMAT_PATTERN_CMD_USAGE_HELP,
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
                TraceAnalysisTool1.options);

        System.out.println("#");
        System.out.println("# Configuration");
        for (final Option o : TraceAnalysisTool1.options) {
            final String longOpt = o.getLongOpt();
            String val = "<null>";
            boolean dumpedOp = false;
            if (longOpt.equals(Constants.CMD_OPT_NAME_INPUTDIRS)) {
                val = TraceAnalysisTool1.stringArrToStringList(TraceAnalysisTool1.inputDirs);
                dumpedOp = true;
            } else if (longOpt.equals(Constants.CMD_OPT_NAME_OUTPUTDIR)) {
                val = TraceAnalysisTool1.outputDir;
                dumpedOp = true;
            } else if (longOpt.equals(Constants.CMD_OPT_NAME_OUTPUTFNPREFIX)) {
                val = TraceAnalysisTool1.outputFnPrefix;
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
                val = TraceAnalysisTool1.cmdl.hasOption(longOpt) ? "true"
                        : "false";
                dumpedOp = true;
            } else if (longOpt.equals(Constants.CMD_OPT_NAME_SELECTTRACES)) {
                if (TraceAnalysisTool1.selectedTraces != null) {
                    val = TraceAnalysisTool1.selectedTraces.toString();
                } else {
                    val = "<select all>";
                }

                dumpedOp = true;
            } else if (longOpt.equals(Constants.CMD_OPT_NAME_TRACEEQUIVCLASSMODE)) {
                if (TraceAnalysisTool1.traceEquivalenceClassMode == TraceEquivalenceClassModes.ALLOCATION) {
                    val = Constants.TRACE_EQUIVALENCE_MODE_STR_ALLOCATION;
                } else if (TraceAnalysisTool1.traceEquivalenceClassMode == TraceEquivalenceClassModes.ASSEMBLY) {
                    val = Constants.TRACE_EQUIVALENCE_MODE_STR_ASSEMBLY;
                } else if (TraceAnalysisTool1.traceEquivalenceClassMode == TraceEquivalenceClassModes.DISABLED) {
                    val = Constants.TRACE_EQUIVALENCE_MODE_STR_DISABLED;
                }
                dumpedOp = true;
            } else if (longOpt.equals(Constants.CMD_OPT_NAME_SHORTLABELS)) {
                val = TraceAnalysisTool1.shortLabels ? "true" : "false";
                dumpedOp = true;
            } else if (longOpt.equals(Constants.CMD_OPT_NAME_IGNOREINVALIDTRACES)) {
                val = TraceAnalysisTool1.ignoreInvalidTraces ? "true" : "false";
                dumpedOp = true;
            } else if (longOpt.equals(Constants.CMD_OPT_NAME_MAXTRACEDURATION)) {
                val = TraceAnalysisTool1.maxTraceDurationMillis + " ms";
                dumpedOp = true;
            } else if (longOpt.equals(Constants.CMD_OPT_NAME_IGNORERECORDSBEFOREDATE)) {
                val = LoggingTimestampConverter.convertLoggingTimestampToUTCString(TraceAnalysisTool1.ignoreRecordsBeforeTimestamp)
                        + " ("
                        + LoggingTimestampConverter.convertLoggingTimestampLocalTimeZoneString(TraceAnalysisTool1.ignoreRecordsBeforeTimestamp)
                        + ")";
                dumpedOp = true;
            } else if (longOpt.equals(Constants.CMD_OPT_NAME_IGNORERECORDSAFTERDATE)) {
                val = LoggingTimestampConverter.convertLoggingTimestampToUTCString(TraceAnalysisTool1.ignoreRecordsAfterTimestamp)
                        + " ("
                        + LoggingTimestampConverter.convertLoggingTimestampLocalTimeZoneString(TraceAnalysisTool1.ignoreRecordsAfterTimestamp)
                        + ")";
                dumpedOp = true;
            } else {
                val = TraceAnalysisTool1.cmdl.getOptionValues(longOpt).toString();
                TraceAnalysisTool1.log.warn("Unformatted confguration output for option "
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

        TraceReconstructionPlugin1 mtReconstrFilter = null;
        try {
            final List<AbstractMessageTraceProcessingPlugin> msgTraceProcessingComponents = new ArrayList<AbstractMessageTraceProcessingPlugin>();
            final List<AbstractExecutionTraceProcessingPlugin> execTraceProcessingComponents = new ArrayList<AbstractExecutionTraceProcessingPlugin>();
            final List<AbstractInvalidExecutionTraceProcessingPlugin> invalidExecTraceProcessingComponents = new ArrayList<AbstractInvalidExecutionTraceProcessingPlugin>();
            // fill list of msgTraceProcessingComponents:
            AbstractMessageTraceProcessingPlugin componentPrintMsgTrace = null;
            if (TraceAnalysisTool1.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PRINTMSGTRACES)) {
                numRequestedTasks++;
                componentPrintMsgTrace = TraceAnalysisTool1.task_createMessageTraceDumpComponent(
                        TraceAnalysisTool1.PRINTMSGTRACE_COMPONENT_NAME,
                        TraceAnalysisTool1.outputDir + File.separator
                        + TraceAnalysisTool1.outputFnPrefix);
                msgTraceProcessingComponents.add(componentPrintMsgTrace);
            }
            AbstractExecutionTraceProcessingPlugin componentPrintExecTrace = null;
            if (TraceAnalysisTool1.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PRINTEXECTRACES)) {
                numRequestedTasks++;
                componentPrintExecTrace = TraceAnalysisTool1.task_createExecutionTraceDumpComponent(
                        TraceAnalysisTool1.PRINTEXECTRACE_COMPONENT_NAME,
                        TraceAnalysisTool1.outputDir + File.separator
                        + TraceAnalysisTool1.outputFnPrefix
                        + Constants.EXECUTION_TRACES_FN_PREFIX
                        + ".txt", false);
                execTraceProcessingComponents.add(componentPrintExecTrace);
            }
            AbstractInvalidExecutionTraceProcessingPlugin componentPrintInvalidTrace = null;
            if (TraceAnalysisTool1.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES)) {
                numRequestedTasks++;
                componentPrintInvalidTrace = TraceAnalysisTool1.task_createInvalidExecutionTraceDumpComponent(
                        TraceAnalysisTool1.PRINTINVALIDEXECTRACE_COMPONENT_NAME,
                        TraceAnalysisTool1.outputDir + File.separator
                        + TraceAnalysisTool1.outputFnPrefix
                        + Constants.INVALID_TRACES_FN_PREFIX
                        + ".txt", true);
                invalidExecTraceProcessingComponents.add(componentPrintInvalidTrace);
            }
            AbstractMessageTraceProcessingPlugin componentPlotSeqDiagr = null;
            if (retVal
                    && TraceAnalysisTool1.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTSEQDS)) {
                numRequestedTasks++;
                componentPlotSeqDiagr = TraceAnalysisTool1.task_createSequenceDiagramPlotComponent(
                        TraceAnalysisTool1.PLOTSEQDIAGR_COMPONENT_NAME,
                        TraceAnalysisTool1.outputDir + File.separator
                        + TraceAnalysisTool1.outputFnPrefix);
                msgTraceProcessingComponents.add(componentPlotSeqDiagr);
            }
            ComponentDependencyGraphPlugin componentPlotComponentDepGraph = null;
            if (retVal
                    && TraceAnalysisTool1.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTCOMPONENTDEPG)) {
                numRequestedTasks++;
                componentPlotComponentDepGraph = TraceAnalysisTool1.task_createComponentDependencyGraphPlotComponent(TraceAnalysisTool1.PLOTCOMPONENTDEPGRAPH_COMPONENT_NAME);
                msgTraceProcessingComponents.add(componentPlotComponentDepGraph);
            }
            ContainerDependencyGraphPlugin componentPlotContainerDepGraph = null;
            if (retVal
                    && TraceAnalysisTool1.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG)) {
                numRequestedTasks++;
                componentPlotContainerDepGraph = TraceAnalysisTool1.task_createContainerDependencyGraphPlotComponent(TraceAnalysisTool1.PLOTCONTAINERDEPGRAPH_COMPONENT_NAME);
                msgTraceProcessingComponents.add(componentPlotContainerDepGraph);
            }
            OperationDependencyGraphPlugin componentPlotOperationDepGraph = null;
            if (retVal
                    && TraceAnalysisTool1.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTOPERATIONDEPG)) {
                numRequestedTasks++;
                componentPlotOperationDepGraph = TraceAnalysisTool1.task_createOperationDependencyGraphPlotComponent(TraceAnalysisTool1.PLOTOPERATIONDEPGRAPH_COMPONENT_NAME);
                msgTraceProcessingComponents.add(componentPlotOperationDepGraph);
            }
            AbstractMessageTraceProcessingPlugin componentPlotCallTrees = null;
            if (retVal
                    && TraceAnalysisTool1.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTCALLTREES)) {
                numRequestedTasks++;
                componentPlotCallTrees = TraceAnalysisTool1.task_createCallTreesPlotComponent(
                        TraceAnalysisTool1.PLOTCALLTREE_COMPONENT_NAME,
                        TraceAnalysisTool1.outputDir + File.separator
                        + TraceAnalysisTool1.outputFnPrefix);
                msgTraceProcessingComponents.add(componentPlotCallTrees);
            }
            AggregatedAllocationComponentOperationCallTreePlugin componentPlotAggregatedCallTree = null;
            if (retVal
                    && TraceAnalysisTool1.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_PLOTAGGREGATEDCALLTREE)) {
                numRequestedTasks++;
                componentPlotAggregatedCallTree = TraceAnalysisTool1.task_createAggregatedCallTreePlotComponent(TraceAnalysisTool1.PLOTAGGREGATEDCALLTREE_COMPONENT_NAME);
                msgTraceProcessingComponents.add(componentPlotAggregatedCallTree);
            }
            if (retVal
                    && TraceAnalysisTool1.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_EQUIVCLASSREPORT)) {
                numRequestedTasks++;
                // the actual execution of the task is performed below
            }

            if (numRequestedTasks == 0) {
                System.err.println("No task requested");
                TraceAnalysisTool1.printUsage();
                return false;
            }

            final List<AbstractTraceProcessingPlugin> allTraceProcessingComponents = new ArrayList<AbstractTraceProcessingPlugin>();
            allTraceProcessingComponents.addAll(msgTraceProcessingComponents);
            allTraceProcessingComponents.addAll(execTraceProcessingComponents);
            allTraceProcessingComponents.addAll(invalidExecTraceProcessingComponents);
            final AnalysisInstance analysisInstance = new AnalysisInstance();
            analysisInstance.setLogReader(new FSReader(
                    TraceAnalysisTool1.inputDirs));
            // analysisInstance.setLogReader(new
            // JMSReader("tcp://localhost:3035/","queue1"));

            mtReconstrFilter = new TraceReconstructionPlugin1(
                    TraceAnalysisTool1.TRACERECONSTR_COMPONENT_NAME,
                    TraceAnalysisTool1.systemEntityFactory,
                    TraceAnalysisTool1.maxTraceDurationMillis,
                    TraceAnalysisTool1.ignoreInvalidTraces,
                    TraceAnalysisTool1.traceEquivalenceClassMode,
                    TraceAnalysisTool1.selectedTraces,
                    TraceAnalysisTool1.ignoreRecordsBeforeTimestamp,
                    TraceAnalysisTool1.ignoreRecordsAfterTimestamp);
            for (final AbstractMessageTraceProcessingPlugin c : msgTraceProcessingComponents) {
                mtReconstrFilter.getMessageTraceEventProviderPort().addListener(c);
            }
            for (final AbstractExecutionTraceProcessingPlugin c : execTraceProcessingComponents) {
                mtReconstrFilter.getExecutionTraceEventProviderPort().addListener(c);
            }
            for (final AbstractInvalidExecutionTraceProcessingPlugin c : invalidExecTraceProcessingComponents) {
                mtReconstrFilter.getInvalidExecutionTraceEventPort().addListener(c);
            }

            final ExecutionRecordTransformationPlugin1 execRecTransformer = new ExecutionRecordTransformationPlugin1(
                    TraceAnalysisTool1.EXEC_TRACE_RECONSTR_COMPONENT_NAME,
                    TraceAnalysisTool1.systemEntityFactory);
            execRecTransformer.getExecutionOutputPort().subsribe(mtReconstrFilter.getExecutionInputPort());
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
                            TraceAnalysisTool1.outputDir
                            + File.separator
                            + TraceAnalysisTool1.outputFnPrefix
                            + Constants.COMPONENT_DEPENDENCY_GRAPH_FN_PREFIX).getCanonicalPath(),
                            (TraceAnalysisTool1.traceEquivalenceClassMode == TraceEquivalenceClassModes.DISABLED),
                            TraceAnalysisTool1.shortLabels,
                            TraceAnalysisTool1.includeSelfLoops);
                }
                if (componentPlotContainerDepGraph != null) {
                    componentPlotContainerDepGraph.saveToDotFile(
                            new File(
                            TraceAnalysisTool1.outputDir
                            + File.separator
                            + TraceAnalysisTool1.outputFnPrefix
                            + Constants.CONTAINER_DEPENDENCY_GRAPH_FN_PREFIX).getCanonicalPath(),
                            (TraceAnalysisTool1.traceEquivalenceClassMode == TraceEquivalenceClassModes.DISABLED),
                            TraceAnalysisTool1.shortLabels,
                            TraceAnalysisTool1.includeSelfLoops);
                }
                if (componentPlotOperationDepGraph != null) {
                    componentPlotOperationDepGraph.saveToDotFile(
                            new File(
                            TraceAnalysisTool1.outputDir
                            + File.separator
                            + TraceAnalysisTool1.outputFnPrefix
                            + Constants.OPERATION_DEPENDENCY_GRAPH_FN_PREFIX).getCanonicalPath(),
                            (TraceAnalysisTool1.traceEquivalenceClassMode == TraceEquivalenceClassModes.DISABLED),
                            TraceAnalysisTool1.shortLabels,
                            TraceAnalysisTool1.includeSelfLoops);
                }

                if (componentPlotAggregatedCallTree != null) {
                    componentPlotAggregatedCallTree.saveTreeToDotFile(new File(
                            TraceAnalysisTool1.outputDir + File.separator
                            + TraceAnalysisTool1.outputFnPrefix
                            + Constants.AGGREGATED_CALL_TREE_FN_PREFIX).getCanonicalPath(), true,
                            TraceAnalysisTool1.shortLabels); // includeWeights
                }
            } catch (final Exception exc) {
                TraceAnalysisTool1.log.error(
                        "Error occured while running analysis", exc);
                throw new Exception("Error occured while running analysis", exc);
            } finally {
                for (final AbstractTraceProcessingPlugin c : allTraceProcessingComponents) {
                    numErrorCount += c.getErrorCount();
                    c.printStatusMessage();
                }
            }

            if (!TraceAnalysisTool1.ignoreInvalidTraces && numErrorCount > 0) {
                TraceAnalysisTool1.log.error(numErrorCount
                        + " errors occured in trace processing components");
                throw new Exception(numErrorCount
                        + " errors occured in trace processing components");
            }

            if (retVal
                    && TraceAnalysisTool1.cmdl.hasOption(Constants.CMD_OPT_NAME_TASK_EQUIVCLASSREPORT)) {
                retVal = TraceAnalysisTool1.task_genTraceEquivalenceReportForTraceSet(
                        TraceAnalysisTool1.outputDir + File.separator
                        + TraceAnalysisTool1.outputFnPrefix,
                        mtReconstrFilter);
            }

            final String systemEntitiesHtmlFn = new File(
                    TraceAnalysisTool1.outputDir + File.separator
                    + TraceAnalysisTool1.outputFnPrefix
                    + "system-entities").getAbsolutePath();
            TraceAnalysisTool1.systemEntityFactory.saveSystemToHTMLFile(systemEntitiesHtmlFn);
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
            TraceAnalysisTool1.log.error("Exception", ex);
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
            if (!TraceAnalysisTool1.parseArgs(args)
                    || !TraceAnalysisTool1.initFromArgs()) {
                System.exit(1);
            }

            TraceAnalysisTool1.dumpConfiguration();

            if (!TraceAnalysisTool1.dispatchTasks()) {
                System.exit(1);
            }

        } catch (final Exception exc) {
            System.err.println("An error occured. See log for details");
            TraceAnalysisTool1.log.fatal(args, exc);
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
                name, TraceAnalysisTool1.systemEntityFactory) {

            public void newEvent(final MessageTrace t)
                    throws EventProcessingException {
                try {
                    SequenceDiagramPlugin.writePicForMessageTrace(this.getSystemEntityFactory(), t, outputFnBase + "-"
                            + t.getTraceId() + ".pic",
                            TraceAnalysisTool1.shortLabels);
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
                name, TraceAnalysisTool1.systemEntityFactory);
        return depGraph;
    }

    private static ContainerDependencyGraphPlugin task_createContainerDependencyGraphPlotComponent(
            final String name) {
        final ContainerDependencyGraphPlugin depGraph = new ContainerDependencyGraphPlugin(
                name, TraceAnalysisTool1.systemEntityFactory);
        return depGraph;
    }

    private static OperationDependencyGraphPlugin task_createOperationDependencyGraphPlotComponent(
            final String name) {
        final OperationDependencyGraphPlugin depGraph = new OperationDependencyGraphPlugin(
                name, TraceAnalysisTool1.systemEntityFactory);
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
                TraceAnalysisTool1.allocationComponentOperationPairFactory,
                TraceAnalysisTool1.systemEntityFactory);
        return callTree;
    }

    private static AbstractMessageTraceProcessingPlugin task_createCallTreesPlotComponent(
            final String name, final String outputFnPrefix) throws IOException {
        final String outputFnBase = new File(outputFnPrefix
                + Constants.CALL_TREE_FN_PREFIX).getCanonicalPath();

        final AbstractMessageTraceProcessingPlugin ctWriter = new AbstractMessageTraceProcessingPlugin(
                name, TraceAnalysisTool1.systemEntityFactory) {

            public void newEvent(final MessageTrace t)
                    throws EventProcessingException {
                try {
                    final TraceCallTreeNode rootNode = new TraceCallTreeNode(
                            AbstractSystemSubRepository.ROOT_ELEMENT_ID,
                            TraceAnalysisTool1.systemEntityFactory,
                            TraceAnalysisTool1.allocationComponentOperationPairFactory,
                            TraceAnalysisTool1.allocationComponentOperationPairFactory.rootPair,
                            true); // rootNode
                    AbstractCallTreePlugin.writeDotForMessageTrace(
                            TraceAnalysisTool1.systemEntityFactory, rootNode, t,
                            outputFnBase + "-" + t.getTraceId(), false,
                            TraceAnalysisTool1.shortLabels); // no weights
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
                name, TraceAnalysisTool1.systemEntityFactory) {

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
                name, TraceAnalysisTool1.systemEntityFactory) {

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
                name, TraceAnalysisTool1.systemEntityFactory) {

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
            final String outputFnPrefix, final TraceReconstructionPlugin1 trf)
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
            TraceAnalysisTool1.log.error("File not found", e);
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

        TraceAnalysisTool1.log.info("Trying to start JMS Listener to "
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

        TraceAnalysisTool1.log.info("Trying to start JMS Listener to "
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

        TraceReconstructionPlugin1 mtReconstrFilter = null;
        mtReconstrFilter = new TraceReconstructionPlugin1(
                TraceAnalysisTool1.TRACERECONSTR_COMPONENT_NAME,
                TraceAnalysisTool1.systemEntityFactory, 60 * 1000, // maxTraceDurationMillis,
                true, // ignoreInvalidTraces,
                TraceEquivalenceClassModes.DISABLED, // traceEquivalenceClassMode,
                // // = every trace
                // passes, not only
                // unique trace classes
                null, // selectedTraces, // null means all
                TraceAnalysisTool1.ignoreRecordsBeforeTimestamp, // default
                // Long.MIN
                TraceAnalysisTool1.ignoreRecordsAfterTimestamp); // default
        // Long.MAX
        mtReconstrFilter.getMessageTraceEventProviderPort().addListener(
                messageTraceListener);
        mtReconstrFilter.getInvalidExecutionTraceEventPort().addListener(
                messageTraceListener.getJfxBrokenExecutionTraceReceiver()); // i
        // know
        // that
        // its
        // dirty

        TraceReconstructionPlugin1 uniqueMtReconstrFilter = null;
        uniqueMtReconstrFilter = new TraceReconstructionPlugin1(
                TraceAnalysisTool1.TRACERECONSTR_COMPONENT_NAME,
                TraceAnalysisTool1.systemEntityFactory, 60 * 1000, // maxTraceDurationMillis,
                true, // ignoreInvalidTraces,
                TraceEquivalenceClassModes.ALLOCATION, // traceEquivalenceClassMode,
                // // = every trace
                // passes, not only
                // unique trace classes
                null, // selectedTraces, // null means all
                TraceAnalysisTool1.ignoreRecordsBeforeTimestamp, // default
                // Long.MIN
                TraceAnalysisTool1.ignoreRecordsAfterTimestamp); // default
        // Long.MAX
        uniqueMtReconstrFilter.getMessageTraceEventProviderPort().addListener(
                messageTraceListener.getJfxUniqueTr()); // i know that its
        // dirty; i (andre) like
        // it because it's
        // basically a port

        final ExecutionRecordTransformationPlugin1 execRecTransformer = new ExecutionRecordTransformationPlugin1(
                TraceAnalysisTool1.EXEC_TRACE_RECONSTR_COMPONENT_NAME,
                TraceAnalysisTool1.systemEntityFactory);
        execRecTransformer.getExecutionOutputPort().subsribe(mtReconstrFilter.getExecutionInputPort());
        execRecTransformer.getExecutionOutputPort().subsribe(uniqueMtReconstrFilter.getExecutionInputPort());
        tpanInstance.registerPlugin(execRecTransformer);
        System.out.println("MessageTraceListener registered");
    }
}
