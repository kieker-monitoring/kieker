package kieker.tpan.tools;

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
import java.util.Map.Entry;
import java.util.TimeZone;

import java.util.TreeSet;
import java.util.Vector;
import kieker.common.logReader.LogReaderExecutionException;
import kieker.common.logReader.RecordConsumerExecutionException;
import kieker.common.logReader.filesystemReader.FSMergeReader;
import kieker.tpan.TpanInstance;
import kieker.tpan.plugins.traceReconstruction.InvalidTraceException;
import kieker.tpan.datamodel.ExecutionTrace;
import kieker.tpan.datamodel.MessageTrace;
import kieker.tpan.datamodel.factories.AbstractSystemSubFactory;
import kieker.tpan.datamodel.factories.AllocationComponentOperationPairFactory;
import kieker.tpan.datamodel.factories.AssemblyComponentOperationPairFactory;
import kieker.tpan.datamodel.factories.SystemEntityFactory;
import kieker.tpan.logReader.JMSReader;
import kieker.tpan.plugins.callTree.AbstractCallTreePlugin;
import kieker.tpan.plugins.callTree.AggregatedAllocationComponentOperationCallTreePlugin;
import kieker.tpan.plugins.traceReconstruction.AbstractTpanExecutionTraceProcessingComponent;
import kieker.tpan.plugins.traceReconstruction.AbstractTpanMessageTraceProcessingComponent;
import kieker.tpan.plugins.traceReconstruction.AbstractTpanTraceProcessingComponent;
import kieker.tpan.plugins.callTree.TraceCallTreeNode;
import kieker.tpan.plugins.dependencyGraph.ComponentDependencyGraphPlugin;
import kieker.tpan.plugins.dependencyGraph.ContainerDependencyGraphPlugin;
import kieker.tpan.plugins.dependencyGraph.OperationDependencyGraphPlugin;
import kieker.tpan.plugins.sequenceDiagram.SequenceDiagramPlugin;
import kieker.tpan.plugins.traceReconstruction.TraceProcessingException;
import kieker.tpan.plugins.traceReconstruction.TraceReconstructionFilter;
import kieker.tpan.plugins.traceReconstruction.TraceReconstructionFilter.TraceEquivalenceClassModes;
import kieker.tpan.recordConsumer.BriefJavaFxInformer;
import kieker.tpan.recordConsumer.executionRecordTransformation.ExecutionRecordTransformer;

import kieker.tpan.recordConsumer.MonitoringRecordTypeLogger;
import kieker.tpmon.core.TpmonController;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Andre van Hoorn, Matthias Rohr
 */
public class TraceAnalysisTool {

    private static final Log log = LogFactory.getLog(TraceAnalysisTool.class);
    private static final SystemEntityFactory systemEntityFactory = new SystemEntityFactory();
    private static final AllocationComponentOperationPairFactory allocationComponentOperationPairFactory =
            new AllocationComponentOperationPairFactory(systemEntityFactory);
    private static final AssemblyComponentOperationPairFactory assemblyComponentOperationPairFactory =
            new AssemblyComponentOperationPairFactory(systemEntityFactory);
    private static final String SEQUENCE_DIAGRAM_FN_PREFIX = "sequenceDiagram";
    private static final String COMPONENT_DEPENDENCY_GRAPH_FN_PREFIX = "componentDependencyGraph";
    private static final String CONTAINER_DEPENDENCY_GRAPH_FN_PREFIX = "containerDependencyGraph";
    private static final String OPERATION_DEPENDENCY_GRAPH_FN_PREFIX = "operationDependencyGraph";
    private static final String AGGREGATED_CALL_TREE_FN_PREFIX = "aggregatedCallTree";
    private static final String CALL_TREE_FN_PREFIX = "callTree";
    private static final String MESSAGE_TRACES_FN_PREFIX = "messageTraces";
    private static final String EXECUTION_TRACES_FN_PREFIX = "executionTraces";
    private static final String INVALID_TRACES_FN_PREFIX = "invalidTraceArtifacts";
    private static final String TRACE_EQUIV_CLASSES_FN_PREFIX = "traceEquivClasses";
    private static CommandLine cmdl = null;
    private static final CommandLineParser cmdlParser = new BasicParser();
    private static final HelpFormatter cmdHelpFormatter = new HelpFormatter();
    private static final Options cmdlOpts = new Options();
    private static String[] inputDirs = null;
    private static String outputDir = null;
    private static String outputFnPrefix = null;
    private static TreeSet<Long> selectedTraces = null; // null means select all
    private static TraceEquivalenceClassModes traceEquivalenceClassMode = TraceEquivalenceClassModes.DISABLED;
    private static final String TRACE_EQUIVALENCE_MODE_STR_DISABLED = "disabled";
    private static final String TRACE_EQUIVALENCE_MODE_STR_ASSEMBLY = "assembly";
    private static final String TRACE_EQUIVALENCE_MODE_STR_ALLOCATION = "allocation";
    private static boolean shortLabels = true;
    private static boolean includeSelfLoops = false;
    private static boolean ignoreInvalidTraces = false;
    private static int maxTraceDurationMillis = TraceReconstructionFilter.MAX_DURATION_MILLIS; // infinite
    private static long ignoreRecordsBeforeTimestamp = TraceReconstructionFilter.MIN_TIMESTAMP;
    private static long ignoreRecordsAfterTimestamp = TraceReconstructionFilter.MAX_TIMESTAMP;
    private static final String DATE_FORMAT_PATTERN = "yyyyMMdd'-'HHmmss";
    private static final String DATE_FORMAT_PATTERN_CMD_USAGE_HELP = DATE_FORMAT_PATTERN.replaceAll("'", ""); // only for usage info
    private static final String CMD_OPT_NAME_INPUTDIRS = "inputdirs";
    private static final String CMD_OPT_NAME_OUTPUTDIR = "outputdir";
    private static final String CMD_OPT_NAME_OUTPUTFNPREFIX = "output-filename-prefix";
    private static final String CMD_OPT_NAME_SELECTTRACES = "select-traces";
    private static final String CMD_OPT_NAME_TRACEEQUIVCLASSMODE = "trace-equivalence-mode";
    private static final String CMD_OPT_NAME_SHORTLABELS = "short-labels";
    private static final String CMD_OPT_NAME_IGNOREINVALIDTRACES = "ignore-invalid-traces";
    private static final String CMD_OPT_NAME_TASK_PLOTSEQDS = "plot-Sequence-Diagrams";
    private static final String CMD_OPT_NAME_TASK_PLOTCOMPONENTDEPG = "plot-Component-Dependency-Graph";
    private static final String CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG = "plot-Container-Dependency-Graph";
    private static final String CMD_OPT_NAME_TASK_PLOTOPERATIONDEPG = "plot-Operation-Dependency-Graph";
    private static final String CMD_OPT_NAME_TASK_PLOTAGGREGATEDCALLTREE = "plot-Aggregated-Call-Tree";
    private static final String CMD_OPT_NAME_TASK_PLOTCALLTREES = "plot-Call-Trees";
    private static final String CMD_OPT_NAME_TASK_PRINTMSGTRACES = "print-Message-Traces";
    private static final String CMD_OPT_NAME_TASK_PRINTEXECTRACES = "print-Execution-Traces";
    private static final String CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES = "print-invalid-Execution-Traces";
    private static final String CMD_OPT_NAME_TASK_EQUIVCLASSREPORT = "print-Equivalence-Classes";
    private static final String CMD_OPT_NAME_MAXTRACEDURATION = "max-trace-duration";
    private static final String CMD_OPT_NAME_IGNORERECORDSBEFOREDATE = "ignore-records-before-date";
    private static final String CMD_OPT_NAME_IGNORERECORDSAFTERDATE = "ignore-records-after-date";
    //    private static final String CMD_OPT_NAME_TASK_INITJMSREADER = "init-basic-JMS-reader";
//    private static final String CMD_OPT_NAME_TASK_INITJMSREADERJFX = "init-basic-JMS-readerJavaFx";
    private static final Vector<Option> options = new Vector<Option>();

    static {
        // TODO: OptionGroups?

        options.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_INPUTDIRS).withArgName("dir1 ... dirN").hasArgs().isRequired(true).withDescription("Log directories to read data from").withValueSeparator('=').create("i"));
        options.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_OUTPUTDIR).withArgName("dir").hasArg(true).isRequired(true).withDescription("Directory for the generated file(s)").withValueSeparator('=').create("o"));
        options.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_OUTPUTFNPREFIX).withArgName("prefix").hasArg(true).isRequired(false).withDescription("Prefix for output filenames\n").withValueSeparator('=').create("p"));

        //OptionGroup cmdlOptGroupTask = new OptionGroup();
        //cmdlOptGroupTask.isRequired();
        options.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTSEQDS).hasArg(false).withDescription("Generate and store sequence diagrams (.pic files)").create());
        options.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTCOMPONENTDEPG).hasArg(false).withDescription("Generate and store a component dependency graph (.dot file)").create());
        options.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG).hasArg(false).withDescription("Generate and store a container dependency graph (.dot file)").create());
        options.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTOPERATIONDEPG).hasArg(false).withDescription("Generate and store an operation dependency graph (.dot file)").create());
        options.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTAGGREGATEDCALLTREE).hasArg(false).withDescription("Generate and store an aggregated call tree (.dot files)").create());
        options.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTCALLTREES).hasArg(false).withDescription("Generate and store call trees for the selected traces (.dot files)").create());
        options.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PRINTMSGTRACES).hasArg(false).withDescription("Save message trace representations of valid traces (.txt files)").create());
        options.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PRINTEXECTRACES).hasArg(false).withDescription("Save execution trace representations of valid traces (.txt files)").create());
        options.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES).hasArg(false).withDescription("Save a execution trace representations of invalid trace artifacts (.txt files)").create());
        options.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_EQUIVCLASSREPORT).hasArg(false).withDescription("Output an overview about the trace equivalence classes").create());

        /* These tasks should be moved to a dedicated tool, since this tool covers trace analysis */
//        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_INITJMSREADER).hasArg(false).withDescription("Creates a jms reader and shows incomming data in the command line").create());
//        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_INITJMSREADERJFX).hasArg(false).withDescription("Creates a jms reader and shows incomming data in the command line and visualizes with javafx").create());

        //cmdlOpts.addOptionGroup(cmdlOptGroupTask);
        options.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_SELECTTRACES).withArgName("id0 ... idn").hasArgs().isRequired(false).withDescription("Consider only the traces identified by the comma-separated list of trace IDs. Defaults to all traces.").create());
        options.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TRACEEQUIVCLASSMODE).withArgName(String.format("%s|%s|%s", TRACE_EQUIVALENCE_MODE_STR_ALLOCATION, TRACE_EQUIVALENCE_MODE_STR_ASSEMBLY, TRACE_EQUIVALENCE_MODE_STR_DISABLED)).hasArg(true).isRequired(false).withDescription("If selected, the selected tasks are performed on representatives of the equivalence classes only.").create());
        options.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_IGNOREINVALIDTRACES).hasArg(false).isRequired(false).withDescription("If selected, the execution aborts on the occurence of an invalid trace.").create());
        options.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_MAXTRACEDURATION).withArgName("duration in ms").hasArg().isRequired(false).withDescription("Threshold (in milliseconds) after which an incomplete trace becomes invalid. Defaults to infinity.").create());
        options.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_IGNORERECORDSBEFOREDATE).withArgName(DATE_FORMAT_PATTERN_CMD_USAGE_HELP).hasArg().isRequired(false).withDescription("Records logged before this date (UTC timezone) are ignored.").create());
        options.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_IGNORERECORDSAFTERDATE).withArgName(DATE_FORMAT_PATTERN_CMD_USAGE_HELP).hasArg().isRequired(false).withDescription("Records logged after this date (UTC timezone) are ignored.").create());
        options.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_SHORTLABELS).hasArg(false).isRequired(false).withDescription("If selected, the hostnames of the executions are NOT considered.").create());
        for (Option o : options) {
            cmdlOpts.addOption(o);
        }
        cmdHelpFormatter.setOptionComparator(new Comparator() {

            public int compare(Object o1, Object o2) {
                if (o1 == o2) {
                    return 0;
                }
                int posO1 = options.indexOf(o1);
                int posO2 = options.indexOf(o2);
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

    private static boolean parseArgs(String[] args) {
        try {
            cmdl = cmdlParser.parse(cmdlOpts, args);
        } catch (ParseException e) {
            printUsage();
            System.err.println("\nError parsing arguments: " + e.getMessage());
            return false;
        }
        return true;
    }

    private static void printUsage() {
        cmdHelpFormatter.printHelp(80, TraceAnalysisTool.class.getName(), "", cmdlOpts, "", true);
    }

    private static boolean initFromArgs() {
        inputDirs = cmdl.getOptionValues(CMD_OPT_NAME_INPUTDIRS);

        outputDir = cmdl.getOptionValue(CMD_OPT_NAME_OUTPUTDIR) + File.separator;
        outputFnPrefix = cmdl.getOptionValue(CMD_OPT_NAME_OUTPUTFNPREFIX, "");
        if (cmdl.hasOption(CMD_OPT_NAME_SELECTTRACES)) { /* Parse liste of trace Ids */
            String[] traceIdList = cmdl.getOptionValues(CMD_OPT_NAME_SELECTTRACES);
            selectedTraces = new TreeSet<Long>();
            int numSelectedTraces = traceIdList.length;
            try {
                for (String idStr : traceIdList) {
                    selectedTraces.add(Long.valueOf(idStr));
                }
                log.info(numSelectedTraces + " trace" + (numSelectedTraces > 1 ? "s" : "") + " selected");
            } catch (Exception e) {
                System.err.println("\nFailed to parse list of trace IDs: " + traceIdList + "(" + e.getMessage() + ")");
                log.error("Failed to parse list of trace IDs: " + traceIdList, e);
                return false;
            }
        }

        shortLabels = cmdl.hasOption(CMD_OPT_NAME_SHORTLABELS);
        ignoreInvalidTraces = cmdl.hasOption(CMD_OPT_NAME_IGNOREINVALIDTRACES);

        String traceEquivClassModeStr = cmdl.getOptionValue(CMD_OPT_NAME_TRACEEQUIVCLASSMODE, null);
        if (traceEquivClassModeStr == null || traceEquivClassModeStr.equals(TRACE_EQUIVALENCE_MODE_STR_DISABLED)) {
            traceEquivalenceClassMode = TraceEquivalenceClassModes.DISABLED;
        } else {
            if (traceEquivClassModeStr.equals(TRACE_EQUIVALENCE_MODE_STR_ALLOCATION)) {
                traceEquivalenceClassMode = TraceEquivalenceClassModes.ALLOCATION;
            } else if (traceEquivClassModeStr.equals(TRACE_EQUIVALENCE_MODE_STR_ASSEMBLY)) {
                traceEquivalenceClassMode = TraceEquivalenceClassModes.ASSEMBLY;
            } else {
                log.error("Invalid value for property " + CMD_OPT_NAME_TRACEEQUIVCLASSMODE + ":" + traceEquivClassModeStr);
                return false;
            }
        }

        String maxTraceDurationStr = cmdl.getOptionValue(CMD_OPT_NAME_MAXTRACEDURATION, maxTraceDurationMillis + "");
        try {
            maxTraceDurationMillis = Integer.parseInt(maxTraceDurationStr);
        } catch (NumberFormatException exc) {
            System.err.println("\nFailed to parse int value of property "
                    + CMD_OPT_NAME_MAXTRACEDURATION + " (must be an integer): "
                    + maxTraceDurationStr);
            log.error("Failed to parse int value of property "
                    + CMD_OPT_NAME_MAXTRACEDURATION
                    + " (must be an integer):" + maxTraceDurationStr, exc);
            return false;
        }

        DateFormat m_ISO8601UTC = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        m_ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            String ignoreRecordsBeforeTimestampString = cmdl.getOptionValue(CMD_OPT_NAME_IGNORERECORDSBEFOREDATE, null);
            String ignoreRecordsAfterTimestampString = cmdl.getOptionValue(CMD_OPT_NAME_IGNORERECORDSAFTERDATE, null);
            if (ignoreRecordsBeforeTimestampString != null) {
                Date ignoreBeforeDate = m_ISO8601UTC.parse(ignoreRecordsBeforeTimestampString);
                ignoreRecordsBeforeTimestamp = ignoreBeforeDate.getTime() * (1000 * 1000);
                log.info("Ignoring records before " + m_ISO8601UTC.format(ignoreBeforeDate)
                        + " (" + ignoreRecordsBeforeTimestamp + ")");
            }
            if (ignoreRecordsAfterTimestampString != null) {
                Date ignoreAfterDate = m_ISO8601UTC.parse(ignoreRecordsAfterTimestampString);
                ignoreRecordsAfterTimestamp = ignoreAfterDate.getTime() * (1000 * 1000);
                log.info("Ignoring records after " + m_ISO8601UTC.format(ignoreAfterDate)
                        + " (" + ignoreRecordsAfterTimestamp + ")");
            }
        } catch (java.text.ParseException ex) {
            System.err.println("Error parsing date/time string. Please use the following pattern: " + DATE_FORMAT_PATTERN_CMD_USAGE_HELP);
            log.error("Error parsing date/time string. Please use the following pattern: " + DATE_FORMAT_PATTERN_CMD_USAGE_HELP, ex);
            return false;
        }
        return true;
    }

    private static String stringArrToStringList(String[] strs) {
        StringBuilder strB = new StringBuilder();
        boolean first = true;
        for (String s : strs) {
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
        Vector<Option> myOpts = new Vector<Option>(options);

        System.out.println("#");
        System.out.println("# Configuration");
        for (Option o : options) {
            String longOpt = o.getLongOpt();
            String val = "<null>";
            boolean dumpedOp = false;
            if (longOpt.equals(CMD_OPT_NAME_INPUTDIRS)) {
                val = stringArrToStringList(inputDirs);
                dumpedOp = true;
            } else if (longOpt.equals(CMD_OPT_NAME_OUTPUTDIR)) {
                val = outputDir;
                dumpedOp = true;
            } else if (longOpt.equals(CMD_OPT_NAME_OUTPUTFNPREFIX)) {
                val = outputFnPrefix;
                dumpedOp = true;
            } else if (longOpt.equals(CMD_OPT_NAME_TASK_EQUIVCLASSREPORT)
                    || longOpt.equals(CMD_OPT_NAME_TASK_PLOTSEQDS)
                    || longOpt.equals(CMD_OPT_NAME_TASK_PLOTCOMPONENTDEPG)
                    || longOpt.equals(CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG)
                    || longOpt.equals(CMD_OPT_NAME_TASK_PLOTOPERATIONDEPG)
                    || longOpt.equals(CMD_OPT_NAME_TASK_PLOTAGGREGATEDCALLTREE)
                    || longOpt.equals(CMD_OPT_NAME_TASK_PLOTCALLTREES)
                    || longOpt.equals(CMD_OPT_NAME_TASK_PRINTEXECTRACES)
                    || longOpt.equals(CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES)
                    || longOpt.equals(CMD_OPT_NAME_TASK_PRINTMSGTRACES)) {
                val = cmdl.hasOption(longOpt) ? "true" : "false";
                dumpedOp = true;
            } else if (longOpt.equals(CMD_OPT_NAME_SELECTTRACES)) {
                if (selectedTraces != null) {
                    val = selectedTraces.toString();
                } else {
                    val = "<select all>";
                }

                dumpedOp = true;
            } else if (longOpt.equals(CMD_OPT_NAME_TRACEEQUIVCLASSMODE)) {
                if (traceEquivalenceClassMode == TraceEquivalenceClassModes.ALLOCATION) {
                    val = TRACE_EQUIVALENCE_MODE_STR_ALLOCATION;
                } else if (traceEquivalenceClassMode == TraceEquivalenceClassModes.ASSEMBLY) {
                    val = TRACE_EQUIVALENCE_MODE_STR_ASSEMBLY;
                } else if (traceEquivalenceClassMode == TraceEquivalenceClassModes.DISABLED) {
                    val = TRACE_EQUIVALENCE_MODE_STR_DISABLED;
                }
                dumpedOp = true;
            } else if (longOpt.equals(CMD_OPT_NAME_SHORTLABELS)) {
                val = shortLabels ? "true" : "false";
                dumpedOp = true;
            } else if (longOpt.equals(CMD_OPT_NAME_IGNOREINVALIDTRACES)) {
                val = ignoreInvalidTraces ? "true" : "false";
                dumpedOp = true;
            } else if (longOpt.equals(CMD_OPT_NAME_MAXTRACEDURATION)) {
                val = maxTraceDurationMillis + " ms";
                dumpedOp = true;
            } else if (longOpt.equals(CMD_OPT_NAME_IGNORERECORDSBEFOREDATE)) {
                val = LoggingTimestampConverterTool.convertLoggingTimestampToUTCString(ignoreRecordsBeforeTimestamp)
                        + " (" + LoggingTimestampConverterTool.convertLoggingTimestampLocalTimeZoneString(ignoreRecordsBeforeTimestamp) + ")";
                dumpedOp = true;
            } else if (longOpt.equals(CMD_OPT_NAME_IGNORERECORDSAFTERDATE)) {
                val = LoggingTimestampConverterTool.convertLoggingTimestampToUTCString(ignoreRecordsAfterTimestamp)
                        + " (" + LoggingTimestampConverterTool.convertLoggingTimestampLocalTimeZoneString(ignoreRecordsAfterTimestamp) + ")";
                dumpedOp = true;
            } else {
                val = cmdl.getOptionValues(longOpt).toString();
                log.warn("Unformatted confguration output for option " + longOpt);
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

        final String TRACERECONSTR_COMPONENT_NAME = "Trace reconstruction";
        final String PRINTMSGTRACE_COMPONENT_NAME = "Print message traces";
        final String PRINTEXECTRACE_COMPONENT_NAME = "Print execution traces";
        final String PRINTINVALIDEXECTRACE_COMPONENT_NAME = "Print invalid execution traces";
        final String PLOTCOMPONENTDEPGRAPH_COMPONENT_NAME = "Component dependency graph";
        final String PLOTCONTAINERDEPGRAPH_COMPONENT_NAME = "Container dependency graph";
        final String PLOTOPERATIONDEPGRAPH_COMPONENT_NAME = "Operation dependency graph";
        final String PLOTSEQDIAGR_COMPONENT_NAME = "Sequence diagrams";
        final String PLOTAGGREGATEDCALLTREE_COMPONENT_NAME = "Aggregated call trees";
        final String PLOTCALLTREE_COMPONENT_NAME = "Call trees";


        TraceReconstructionFilter mtReconstrFilter = null;
        try {
            List<AbstractTpanMessageTraceProcessingComponent> msgTraceProcessingComponents = new ArrayList<AbstractTpanMessageTraceProcessingComponent>();
            List<AbstractTpanExecutionTraceProcessingComponent> execTraceProcessingComponents = new ArrayList<AbstractTpanExecutionTraceProcessingComponent>();
            List<AbstractTpanExecutionTraceProcessingComponent> invalidTraceProcessingComponents = new ArrayList<AbstractTpanExecutionTraceProcessingComponent>();
            // fill list of msgTraceProcessingComponents:
            AbstractTpanMessageTraceProcessingComponent componentPrintMsgTrace = null;
            if (cmdl.hasOption(CMD_OPT_NAME_TASK_PRINTMSGTRACES)) {
                numRequestedTasks++;
                componentPrintMsgTrace =
                        task_createMessageTraceDumpComponent(PRINTMSGTRACE_COMPONENT_NAME,
                        outputDir + File.separator + outputFnPrefix);
                msgTraceProcessingComponents.add(componentPrintMsgTrace);
            }
            AbstractTpanExecutionTraceProcessingComponent componentPrintExecTrace = null;
            if (cmdl.hasOption(CMD_OPT_NAME_TASK_PRINTEXECTRACES)) {
                numRequestedTasks++;
                componentPrintExecTrace =
                        task_createExecutionTraceDumpComponent(PRINTEXECTRACE_COMPONENT_NAME,
                        outputDir + File.separator + outputFnPrefix + EXECUTION_TRACES_FN_PREFIX + ".txt", false);
                execTraceProcessingComponents.add(componentPrintExecTrace);
            }
            AbstractTpanExecutionTraceProcessingComponent componentPrintInvalidTrace = null;
            if (cmdl.hasOption(CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES)) {
                numRequestedTasks++;
                componentPrintInvalidTrace =
                        task_createExecutionTraceDumpComponent(PRINTINVALIDEXECTRACE_COMPONENT_NAME,
                        outputDir + File.separator + outputFnPrefix + INVALID_TRACES_FN_PREFIX + ".txt", true);
                invalidTraceProcessingComponents.add(componentPrintInvalidTrace);
            }
            AbstractTpanMessageTraceProcessingComponent componentPlotSeqDiagr = null;
            if (retVal && cmdl.hasOption(CMD_OPT_NAME_TASK_PLOTSEQDS)) {
                numRequestedTasks++;
                componentPlotSeqDiagr =
                        task_createSequenceDiagramPlotComponent(PLOTSEQDIAGR_COMPONENT_NAME,
                        outputDir + File.separator + outputFnPrefix);
                msgTraceProcessingComponents.add(componentPlotSeqDiagr);
            }
            ComponentDependencyGraphPlugin componentPlotComponentDepGraph = null;
            if (retVal && cmdl.hasOption(CMD_OPT_NAME_TASK_PLOTCOMPONENTDEPG)) {
                numRequestedTasks++;
                componentPlotComponentDepGraph =
                        task_createComponentDependencyGraphPlotComponent(PLOTCOMPONENTDEPGRAPH_COMPONENT_NAME);
                msgTraceProcessingComponents.add(componentPlotComponentDepGraph);
            }
            ContainerDependencyGraphPlugin componentPlotContainerDepGraph = null;
            if (retVal && cmdl.hasOption(CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG)) {
                numRequestedTasks++;
                componentPlotContainerDepGraph =
                        task_createContainerDependencyGraphPlotComponent(PLOTCONTAINERDEPGRAPH_COMPONENT_NAME);
                msgTraceProcessingComponents.add(componentPlotContainerDepGraph);
            }
            OperationDependencyGraphPlugin componentPlotOperationDepGraph = null;
            if (retVal && cmdl.hasOption(CMD_OPT_NAME_TASK_PLOTOPERATIONDEPG)) {
                numRequestedTasks++;
                componentPlotOperationDepGraph =
                        task_createOperationDependencyGraphPlotComponent(PLOTOPERATIONDEPGRAPH_COMPONENT_NAME);
                msgTraceProcessingComponents.add(componentPlotOperationDepGraph);
            }
            AbstractTpanMessageTraceProcessingComponent componentPlotCallTrees = null;
            if (retVal && cmdl.hasOption(CMD_OPT_NAME_TASK_PLOTCALLTREES)) {
                numRequestedTasks++;
                componentPlotCallTrees =
                        task_createCallTreesPlotComponent(PLOTCALLTREE_COMPONENT_NAME,
                        outputDir + File.separator + outputFnPrefix);
                msgTraceProcessingComponents.add(componentPlotCallTrees);
            }
            AggregatedAllocationComponentOperationCallTreePlugin componentPlotAggregatedCallTree = null;
            if (retVal && cmdl.hasOption(CMD_OPT_NAME_TASK_PLOTAGGREGATEDCALLTREE)) {
                numRequestedTasks++;
                componentPlotAggregatedCallTree =
                        task_createAggregatedCallTreePlotComponent(PLOTAGGREGATEDCALLTREE_COMPONENT_NAME);
                msgTraceProcessingComponents.add(componentPlotAggregatedCallTree);
            }
            if (retVal && cmdl.hasOption(CMD_OPT_NAME_TASK_EQUIVCLASSREPORT)) {
                numRequestedTasks++;
                // the actual execution of the task is performed below
            }


            if (numRequestedTasks == 0) {
                System.err.println("No task requested");
                printUsage();
                return false;
            }

            List<AbstractTpanTraceProcessingComponent> allTraceProcessingComponents = new ArrayList<AbstractTpanTraceProcessingComponent>();
            allTraceProcessingComponents.addAll(msgTraceProcessingComponents);
            allTraceProcessingComponents.addAll(execTraceProcessingComponents);
            allTraceProcessingComponents.addAll(invalidTraceProcessingComponents);
            TpanInstance analysisInstance = new TpanInstance();
            //analysisInstance.setLogReader(new FSReader(inputDirs));
            analysisInstance.setLogReader(new FSMergeReader(inputDirs));

            mtReconstrFilter =
                    new TraceReconstructionFilter(TRACERECONSTR_COMPONENT_NAME, systemEntityFactory,
                    maxTraceDurationMillis, ignoreInvalidTraces, traceEquivalenceClassMode,
                    selectedTraces, ignoreRecordsBeforeTimestamp,
                    ignoreRecordsAfterTimestamp);
            for (AbstractTpanMessageTraceProcessingComponent c : msgTraceProcessingComponents) {
                mtReconstrFilter.addMessageTraceListener(c);
            }
            for (AbstractTpanExecutionTraceProcessingComponent c : execTraceProcessingComponents) {
                mtReconstrFilter.addExecutionTraceListener(c);
            }
            for (AbstractTpanExecutionTraceProcessingComponent c : invalidTraceProcessingComponents) {
                mtReconstrFilter.addInvalidExecutionTraceArtifactListener(c);
            }

            ExecutionRecordTransformer execRecTransformer = new ExecutionRecordTransformer(systemEntityFactory);
            execRecTransformer.addListener(mtReconstrFilter);
            analysisInstance.addRecordConsumer(execRecTransformer);
            // END test with new meta-model

            int numErrorCount = 0;
            try {
                analysisInstance.run();

                if (componentPlotComponentDepGraph != null) {
                    componentPlotComponentDepGraph.saveToDotFile(new File(outputDir + File.separator + outputFnPrefix + COMPONENT_DEPENDENCY_GRAPH_FN_PREFIX).getCanonicalPath(),
                            (traceEquivalenceClassMode == TraceEquivalenceClassModes.DISABLED), shortLabels, includeSelfLoops);
                }
                if (componentPlotContainerDepGraph != null) {
                    componentPlotContainerDepGraph.saveToDotFile(new File(outputDir + File.separator + outputFnPrefix + CONTAINER_DEPENDENCY_GRAPH_FN_PREFIX).getCanonicalPath(),
                            (traceEquivalenceClassMode == TraceEquivalenceClassModes.DISABLED), shortLabels, includeSelfLoops);
                }
                if (componentPlotOperationDepGraph != null) {
                    componentPlotOperationDepGraph.saveToDotFile(new File(outputDir + File.separator + outputFnPrefix + OPERATION_DEPENDENCY_GRAPH_FN_PREFIX).getCanonicalPath(),
                            (traceEquivalenceClassMode == TraceEquivalenceClassModes.DISABLED), shortLabels, includeSelfLoops);
                }

                if (componentPlotAggregatedCallTree != null) {
                    componentPlotAggregatedCallTree.saveTreeToDotFile(
                            new File(outputDir + File.separator + outputFnPrefix + AGGREGATED_CALL_TREE_FN_PREFIX).getCanonicalPath(), true, shortLabels); // includeWeights
                }
            } catch (Exception exc) {
                log.error("Error occured while running analysis", exc);
                throw new Exception("Error occured while running analysis", exc);
            } finally {
                for (AbstractTpanTraceProcessingComponent c : allTraceProcessingComponents) {
                    numErrorCount += c.getErrorCount();
                    c.printStatusMessage();
                    c.cleanup();
                }
            }

            if (!ignoreInvalidTraces && numErrorCount > 0) {
                log.error(numErrorCount + " errors occured in trace processing components");
                throw new Exception(numErrorCount + " errors occured in trace processing components");
            }

            if (retVal && cmdl.hasOption(CMD_OPT_NAME_TASK_EQUIVCLASSREPORT)) {
                retVal = task_genTraceEquivalenceReportForTraceSet(outputDir + File.separator + outputFnPrefix,
                        mtReconstrFilter);
            }

            // TODO: these tasks should be moved to a decicated tool
//            if (retVal && cmdl.hasOption(CMD_OPT_NAME_TASK_INITJMSREADER)) {
//                numRequestedTasks++;
//                retVal = task_initBasicJmsReader("tcp://127.0.0.1:3035/", "queue1");
//                System.out.println("Finished to start task_initBasicJmsReader");
//            }
//            if (retVal && cmdl.hasOption(CMD_OPT_NAME_TASK_INITJMSREADERJFX)) {
//                numRequestedTasks++;
//                retVal = task_initBasicJmsReaderJavaFx("tcp://127.0.0.1:3035/", "queue1");
//                System.out.println("Finished to start task_initBasicJmsReader");
//            }

            if (!retVal) {
                System.err.println("A task failed");
            }
        } catch (Exception ex) {
            System.err.println("An error occured: " + ex.getMessage());
            System.err.println("");
            log.error("Exception", ex);
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

    public static void main(String args[]) {
        try {
            if (!parseArgs(args) || !initFromArgs()) {
                System.exit(1);
            }

            dumpConfiguration();

            if (!dispatchTasks()) {
                System.exit(1);
            }

            /* As long as we have a dependency from logAnalysis to tpmon,
             * we need to terminate tpmon explicitly. */
            TpmonController.getInstance().terminateMonitoring();
        } catch (Exception exc) {
            System.err.println("An error occured. See log for details");
            log.fatal(args, exc);
        }
    }

    /**
     * Reads the traces from the directory inputDirName and write the
     * sequence diagrams for traces with IDs given in traceSet to
     * the directory outputFnPrefix.
     * If  traceSet is null, a sequence diagram for each trace is generated.
     *
     * @param inputDirName
     * @param outputFnPrefix
     * @param traceSet
     */
    private static AbstractTpanMessageTraceProcessingComponent task_createSequenceDiagramPlotComponent(final String name, final String outputFnPrefix) throws IOException {
        final String outputFnBase = new File(outputFnPrefix + SEQUENCE_DIAGRAM_FN_PREFIX).getCanonicalPath();
        AbstractTpanMessageTraceProcessingComponent sqdWriter = new AbstractTpanMessageTraceProcessingComponent(name, systemEntityFactory) {

            public void newTrace(MessageTrace t) throws TraceProcessingException {
                try {
                    SequenceDiagramPlugin.writePicForMessageTrace(this.getSystemEntityFactory(), t, outputFnBase + "-" + t.getTraceId() + ".pic", shortLabels);
                    this.reportSuccess(t.getTraceId());
                } catch (FileNotFoundException ex) {
                    this.reportError(t.getTraceId());
                    throw new TraceProcessingException("File not found", ex);
                }
            }

            @Override
            public void printStatusMessage() {
                super.printStatusMessage();
                int numPlots = this.getSuccessCount();
                long lastSuccessTracesId = this.getLastTraceIdSuccess();
                System.out.println("Wrote " + numPlots + " sequence diagram" + (numPlots > 1 ? "s" : "") + " to file" + (numPlots > 1 ? "s" : "") + " with name pattern '" + outputFnBase + "-<traceId>.pic'");
                System.out.println("Pic files can be converted using the pic2plot tool (package plotutils)");
                System.out.println("Example: pic2plot -T svg " + outputFnBase + "-" + ((numPlots > 0) ? lastSuccessTracesId : "<traceId>") + ".pic > " + outputFnBase + "-" + ((numPlots > 0) ? lastSuccessTracesId : "<traceId>") + ".svg");
            }

            @Override
            public void cleanup() {
                // nothing to do
            }
        };
        return sqdWriter;
    }

    /**
     * Reads the traces from the directory inputDirName and write the
     * dependency graph to the directory outputFnPrefix.
     * If  traceSet is null, a dependency graph containing the information
     * of all traces is generated.
     *
     * @param inputDirName
     * @param outputFnPrefix
     * @param traceSet
     */
    private static ComponentDependencyGraphPlugin task_createComponentDependencyGraphPlotComponent(final String name) {
        ComponentDependencyGraphPlugin depGraph = new ComponentDependencyGraphPlugin(name, systemEntityFactory);
        return depGraph;
    }

    private static ContainerDependencyGraphPlugin task_createContainerDependencyGraphPlotComponent(final String name) {
        ContainerDependencyGraphPlugin depGraph = new ContainerDependencyGraphPlugin(name, systemEntityFactory);
        return depGraph;
    }

    private static OperationDependencyGraphPlugin task_createOperationDependencyGraphPlotComponent(final String name) {
        OperationDependencyGraphPlugin depGraph = new OperationDependencyGraphPlugin(name, systemEntityFactory);
        return depGraph;
    }

    /**
     * Reads the traces from the directory inputDirName and write the
     * call tree for traces to the directory outputFnPrefix.
     * If  traceSet is null, a call tree containing the information
     * of all traces is generated.
     *
     * @param inputDirName
     * @param outputFnPrefix
     * @param traceSet
     */
    private static AggregatedAllocationComponentOperationCallTreePlugin task_createAggregatedCallTreePlotComponent(final String name) {
        AggregatedAllocationComponentOperationCallTreePlugin callTree =
                new AggregatedAllocationComponentOperationCallTreePlugin(name, allocationComponentOperationPairFactory, systemEntityFactory);
        return callTree;
    }

    private static AbstractTpanMessageTraceProcessingComponent task_createCallTreesPlotComponent(final String name, final String outputFnPrefix) throws IOException {
        final String outputFnBase = new File(outputFnPrefix + CALL_TREE_FN_PREFIX).getCanonicalPath();

        AbstractTpanMessageTraceProcessingComponent ctWriter = new AbstractTpanMessageTraceProcessingComponent(name, systemEntityFactory) {

            public void newTrace(MessageTrace t) throws TraceProcessingException {
                try {
                final TraceCallTreeNode rootNode =
                new TraceCallTreeNode(AbstractSystemSubFactory.ROOT_ELEMENT_ID,
                systemEntityFactory, allocationComponentOperationPairFactory,
                allocationComponentOperationPairFactory.rootPair, true); // rootNode
                    AbstractCallTreePlugin.writeDotForMessageTrace(systemEntityFactory, rootNode, t, outputFnBase + "-" + t.getTraceId(), false, shortLabels); // no weights
                    this.reportSuccess(t.getTraceId());
                } catch (FileNotFoundException ex) {
                    this.reportError(t.getTraceId());
                    throw new TraceProcessingException("File not found", ex);
                }
            }

            @Override
            public void printStatusMessage() {
                super.printStatusMessage();
                int numPlots = this.getSuccessCount();
                long lastSuccessTracesId = this.getLastTraceIdSuccess();
                System.out.println("Wrote " + numPlots + " call tree" + (numPlots > 1 ? "s" : "") + " to file" + (numPlots > 1 ? "s" : "") + " with name pattern '" + outputFnBase + "-<traceId>.dot'");
                System.out.println("Dot files can be converted using the dot tool");
                System.out.println("Example: dot -T svg " + outputFnBase + "-" + ((numPlots > 0) ? lastSuccessTracesId : "<traceId>") + ".dot > " + outputFnBase + "-" + ((numPlots > 0) ? lastSuccessTracesId : "<traceId>") + ".svg");
            }

            @Override
            public void cleanup() {
                // nothing to do
            }
        };
        return ctWriter;
    }

    /**
     * Reads the traces from the directory inputDirName and write the
     * message trace representation for traces with IDs given in traceSet
     * to the directory outputFnPrefix.
     * If  traceSet is null, a message trace for each trace is generated.
     *
     * @param inputDirName
     * @param outputFnPrefix
     * @param traceSet
     */
    private static AbstractTpanMessageTraceProcessingComponent task_createMessageTraceDumpComponent(final String name, String outputFnPrefix) throws IOException, InvalidTraceException, LogReaderExecutionException, RecordConsumerExecutionException {
        final String outputFn = new File(outputFnPrefix + MESSAGE_TRACES_FN_PREFIX + ".txt").getCanonicalPath();
        AbstractTpanMessageTraceProcessingComponent mtWriter = new AbstractTpanMessageTraceProcessingComponent(name, systemEntityFactory) {

            PrintStream ps = new PrintStream(new FileOutputStream(outputFn));

            public void newTrace(MessageTrace t) {
                this.reportSuccess(t.getTraceId());
                ps.println(t);
            }

            @Override
            public void printStatusMessage() {
                super.printStatusMessage();
                int numTraces = this.getSuccessCount();
                System.out.println("Wrote " + numTraces + " message trace" + (numTraces > 1 ? "s" : "") + " to file '" + outputFn + "'");
            }

            @Override
            public void cleanup() {
                if (ps != null) {
                    ps.close();
                }
            }
        };
        return mtWriter;
    }

    /**
     * Reads the traces from the directory inputDirName and write the
     * execution trace representation for traces with IDs given in traceSet
     * to the directory outputFnPrefix.
     * If  traceSet is null, an execution trace for each trace is generated.
     *
     * @param inputDirName
     * @param outputFnPrefix
     * @param traceSet
     */
    private static AbstractTpanExecutionTraceProcessingComponent task_createExecutionTraceDumpComponent(final String name, final String outputFn, final boolean artifactMode) throws IOException, LogReaderExecutionException, RecordConsumerExecutionException {
        final String myOutputFn = new File(outputFn).getCanonicalPath();
        AbstractTpanExecutionTraceProcessingComponent etWriter = new AbstractTpanExecutionTraceProcessingComponent(name, systemEntityFactory) {

            final PrintStream ps = new PrintStream(new FileOutputStream(myOutputFn));

            public void newTrace(ExecutionTrace t) {
                ps.println(t);
                this.reportSuccess(t.getTraceId());
            }

            @Override
            public void printStatusMessage() {
                super.printStatusMessage();
                int numTraces = this.getSuccessCount();
                System.out.println("Wrote " + numTraces + " execution trace" + (artifactMode ? " artifact" : "") + (numTraces > 1 ? "s" : "") + " to file '" + myOutputFn + "'");
            }

            @Override
            public void cleanup() {
                if (ps != null) {
                    ps.close();
                }
            }
        };
        return etWriter;
    }

    private static boolean task_genTraceEquivalenceReportForTraceSet(final String outputFnPrefix, final TraceReconstructionFilter trf) throws IOException, LogReaderExecutionException, RecordConsumerExecutionException {
        boolean retVal = true;
        String outputFn = new File(outputFnPrefix + TRACE_EQUIV_CLASSES_FN_PREFIX + ".txt").getCanonicalPath();
        PrintStream ps = null;
        try {
            ps = new PrintStream(new FileOutputStream(outputFn));
            int numClasses = 0;
            HashMap<ExecutionTrace, Integer> classMap = trf.getEquivalenceClassMap();
            for (Entry<ExecutionTrace, Integer> e : classMap.entrySet()) {
                ExecutionTrace t = e.getKey();
                ps.println("Class " + numClasses++
                        + " ; cardinality: " + e.getValue()
                        + "; # executions: " + t.getLength()
                        + "; representative: " + t.getTraceId()
                        + "; max. stack depth: " + t.getMaxStackDepth());
            }
            System.out.println("");
            System.out.println("#");
            System.out.println("# Plugin: " + "Trace equivalence report");
            System.out.println("Wrote " + numClasses + " equivalence class" + (numClasses > 1 ? "es" : "") + " to file '" + outputFn + "'");
        } catch (FileNotFoundException e) {
            log.error("File not found", e);
            retVal = false;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }

        return retVal;
    }

    private static boolean task_initBasicJmsReader(String jmsProviderUrl, String jmsDestination) throws IOException, LogReaderExecutionException, RecordConsumerExecutionException {
        boolean retVal = true;

        /** As long as we have a dependency to tpmon, 
         *  we load it explicitly in order to avoid 
         *  later delays.*/
        TpmonController ctrl = TpmonController.getInstance();

        log.info("Trying to start JMS Listener to " + jmsProviderUrl + " " + jmsDestination);
        /* Read log data and collect execution traces */
        TpanInstance analysisInstance = new TpanInstance();
        analysisInstance.setLogReader(new JMSReader(jmsProviderUrl, jmsDestination));

        MonitoringRecordTypeLogger recordTypeLogger = new MonitoringRecordTypeLogger();
        analysisInstance.addRecordConsumer(recordTypeLogger);

        //MessageTraceRepository seqRepConsumer = new MessageTraceRepository();
        //analysisInstance.addRecordConsumer(seqRepConsumer);

        /*@Matthias: Deactivated this, since the ant task didn't run (Andre) */
        //BriefJavaFxInformer bjfx = new BriefJavaFxInformer();
        //analysisInstance.addRecordConsumer(bjfx);

        analysisInstance.run();
        return retVal;
    }

    private static boolean task_initBasicJmsReaderJavaFx(String jmsProviderUrl, String jmsDestination) throws IOException, LogReaderExecutionException, RecordConsumerExecutionException {
        boolean retVal = true;

        /** As long as we have a dependency to tpmon,
         *  we load it explicitly in order to avoid
         *  later delays.*/
        TpmonController ctrl = TpmonController.getInstance();

        log.info("Trying to start JMS Listener to " + jmsProviderUrl + " " + jmsDestination);
        /* Read log data and collect execution traces */
        TpanInstance analysisInstance = new TpanInstance();
        analysisInstance.setLogReader(new JMSReader(jmsProviderUrl, jmsDestination));

        MonitoringRecordTypeLogger recordTypeLogger = new MonitoringRecordTypeLogger();
        analysisInstance.addRecordConsumer(recordTypeLogger);

        //MessageTraceRepository seqRepConsumer = new MessageTraceRepository();
        //analysisInstance.addRecordConsumer(seqRepConsumer);

        /*@Matthias: Deactivated this, since the ant task didn't run (Andre) */
        BriefJavaFxInformer bjfx = new BriefJavaFxInformer();
        analysisInstance.addRecordConsumer(bjfx);

        analysisInstance.run();
        return retVal;
    }
}
