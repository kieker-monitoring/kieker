package kieker.tpan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import java.util.TreeSet;
import kieker.common.logReader.LogReaderExecutionException;
import kieker.common.logReader.RecordConsumerExecutionException;
import kieker.common.logReader.filesystemReader.FSMergeReader;
import kieker.tpan.datamodel.ExecutionTrace;
import kieker.tpan.datamodel.InvalidTraceException;
import kieker.tpan.datamodel.MessageTrace;
import kieker.tpan.logReader.JMSReader;
import kieker.tpan.plugins.AbstractTpanExecutionTraceProcessingComponent;
import kieker.tpan.plugins.AbstractTpanMessageTraceProcessingComponent;
import kieker.tpan.plugins.AbstractTpanTraceProcessingComponent;
import kieker.tpan.plugins.DependencyGraphPlugin;
import kieker.tpan.plugins.SequenceDiagramPlugin;
import kieker.tpan.plugins.TraceProcessingException;
import kieker.tpan.recordConsumer.BriefJavaFxInformer;
import kieker.tpan.recordConsumer.TraceReconstructionFilter;

import kieker.tpan.recordConsumer.MonitoringRecordTypeLogger;
import kieker.tpmon.core.TpmonController;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2009 Kieker Project
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
/**
 * @author Andre van Hoorn, Matthias Rohr
 * History
 * 2009-07-01 (AvH) Initial version
 * 2009-07-25 (MR) Checks against invalid input dir
 */
public class TpanTool {

    private static final Log log = LogFactory.getLog(TpanTool.class);
    private static final String SEQUENCE_DIAGRAM_FN_PREFIX = "sequenceDiagram";
    private static final String DEPENDENCY_GRAPH_FN_PREFIX = "dependencyGraph";
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
    private static TreeSet<Long> selectedTraces = null;
    private static boolean traceEquivClassMode = true; // false;
    private static boolean considerHostname = true;
    private static boolean ignoreInvalidTraces = false;
    private static final String CMD_OPT_NAME_INPUTDIRS = "inputdirs";
    private static final String CMD_OPT_NAME_OUTPUTDIR = "outputdir";
    private static final String CMD_OPT_NAME_OUTPUTFNPREFIX = "output-filename-prefix";
    private static final String CMD_OPT_NAME_SELECTTRACES = "select-traces";
    private static final String CMD_OPT_NAME_TRACEEQUIVCLASSMODE = "trace-equivalence-mode";
    private static final String CMD_OPT_NAME_NOHOSTNAMES = "ignore-hostnames";
    private static final String CMD_OPT_NAME_IGNOREINVALIDTRACES = "ignore-invalid-traces";
    private static final String CMD_OPT_NAME_TASK_PLOTSEQD = "plot-Sequence-Diagram";
    private static final String CMD_OPT_NAME_TASK_PLOTDEPG = "plot-Dependency-Graph";
    private static final String CMD_OPT_NAME_TASK_PRINTMSGTRACE = "print-Message-Trace";
    private static final String CMD_OPT_NAME_TASK_PRINTEXECTRACE = "print-Execution-Trace";
    private static final String CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACE = "print-invalid-Execution-Trace";
    private static final String CMD_OPT_NAME_TASK_EQUIVCLASSREPORT = "print-Equivalence-Classes";
//    private static final String CMD_OPT_NAME_TASK_INITJMSREADER = "init-basic-JMS-reader";
//    private static final String CMD_OPT_NAME_TASK_INITJMSREADERJFX = "init-basic-JMS-readerJavaFx";

    static {
        // TODO: OptionGroups?

        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_INPUTDIRS).withArgName("dir1 ... dirN").hasArgs().isRequired(false).withDescription("Log directories to read data from").withValueSeparator('=').create("i"));
        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_OUTPUTDIR).withArgName("dir").hasArg(true).isRequired(true).withDescription("Directory for the generated file(s)").withValueSeparator('=').create("o"));
        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_OUTPUTFNPREFIX).withArgName("dir").hasArg(true).isRequired(false).withDescription("Prefix for output filenames\n").withValueSeparator('=').create("p"));

        //OptionGroup cmdlOptGroupTask = new OptionGroup();
        //cmdlOptGroupTask.isRequired();
        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTSEQD).hasArg(false).withDescription("Generate and store sequence diagrams (.pic files)").create());
        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTDEPG).hasArg(false).withDescription("Generate and store dependency graphs (.dot files)").create());
        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PRINTMSGTRACE).hasArg(false).withDescription("Save message trace representations of valid traces (.txt files)").create());
        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PRINTEXECTRACE).hasArg(false).withDescription("Save execution trace representations of valid traces (.txt files)").create());
        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACE).hasArg(false).withDescription("Save execution trace representations of invalid trace artifacts (.txt files)").create());
        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_EQUIVCLASSREPORT).hasArg(false).withDescription("Output an overview about the trace equivalence classes").create());


        /* These tasks should be moved to a dedicated tool, since this tool covers trace analysis */
//        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_INITJMSREADER).hasArg(false).withDescription("Creates a jms reader and shows incomming data in the command line").create());
//        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_INITJMSREADERJFX).hasArg(false).withDescription("Creates a jms reader and shows incomming data in the command line and visualizes with javafx").create());

        //cmdlOpts.addOptionGroup(cmdlOptGroupTask);
        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_SELECTTRACES).withArgName("id0,...,idn").hasArgs().isRequired(false).withDescription("Consider only the traces identified by the comma-separated list of trace IDs. Defaults to all traces.").create("t"));
        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_TRACEEQUIVCLASSMODE).hasArg(false).isRequired(false).withDescription("If selected, the performed tasks performed on representatives of equivalence classes only.").create());
        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_NOHOSTNAMES).hasArg(false).isRequired(false).withDescription("If selected, the hostnames of the executions are NOT considered.").create());
        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_IGNOREINVALIDTRACES).hasArg(false).isRequired(false).withDescription("If selected, the execution aborts on the occurence of an invalid trace.").create());
    }

    private static boolean parseArgs(String[] args) {
        try {
            cmdl = cmdlParser.parse(cmdlOpts, args);
        } catch (ParseException e) {
            System.err.println("Error parsing arguments: " + e.getMessage());
            printUsage();
            return false;
        }
        return true;
    }

    private static void printUsage() {
        cmdHelpFormatter.printHelp(TpanTool.class.getName(), cmdlOpts);
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
                System.err.println("Failed to parse list of trace IDs: " + traceIdList + "(" + e.getMessage() + ")");
                return false;
            }
        }

        considerHostname = !cmdl.hasOption(CMD_OPT_NAME_NOHOSTNAMES);
        ignoreInvalidTraces = cmdl.hasOption(CMD_OPT_NAME_IGNOREINVALIDTRACES);
        traceEquivClassMode = cmdl.hasOption(CMD_OPT_NAME_TRACEEQUIVCLASSMODE);

        return true;
    }

    private static boolean dispatchTasks() {
        boolean retVal = true;
        int numRequestedTasks = 0;

        final String TRACERECONSTR_COMPONENT_NAME = "Trace reconstruction";
        final String PRINTMSGTRACE_COMPONENT_NAME = "Print message traces";
        final String PRINTEXECTRACE_COMPONENT_NAME = "Print execution traces";
        final String PRINTINVALIDEXECTRACE_COMPONENT_NAME = "Print invalid execution traces";
        final String PLOTDEPGRAPH_COMPONENT_NAME = "Dependency graph";
        final String PLOTSEQDIAGR_COMPONENT_NAME = "Sequence diagram";

        TraceReconstructionFilter mtReconstrFilter = null;
        try {
            List<AbstractTpanMessageTraceProcessingComponent> msgTraceProcessingComponents = new ArrayList<AbstractTpanMessageTraceProcessingComponent>();
            List<AbstractTpanExecutionTraceProcessingComponent> execTraceProcessingComponents = new ArrayList<AbstractTpanExecutionTraceProcessingComponent>();
            List<AbstractTpanExecutionTraceProcessingComponent> invalidTraceProcessingComponents = new ArrayList<AbstractTpanExecutionTraceProcessingComponent>();
            // fill list of msgTraceProcessingComponents:
            AbstractTpanMessageTraceProcessingComponent componentPrintMsgTrace = null;
            if (cmdl.hasOption(CMD_OPT_NAME_TASK_PRINTMSGTRACE)) {
                numRequestedTasks++;
                componentPrintMsgTrace = 
                        task_createMessageTraceDumpComponent(PRINTMSGTRACE_COMPONENT_NAME,
                        outputDir + File.separator + outputFnPrefix);
                msgTraceProcessingComponents.add(componentPrintMsgTrace);
            }
            AbstractTpanExecutionTraceProcessingComponent componentPrintExecTrace = null;
            if (cmdl.hasOption(CMD_OPT_NAME_TASK_PRINTEXECTRACE)) {
                numRequestedTasks++;
                componentPrintExecTrace = 
                        task_createExecutionTraceDumpComponent(PRINTEXECTRACE_COMPONENT_NAME,
                        outputDir + File.separator + outputFnPrefix + EXECUTION_TRACES_FN_PREFIX + ".txt", false);
                execTraceProcessingComponents.add(componentPrintExecTrace);
            }
            AbstractTpanExecutionTraceProcessingComponent componentPrintInvalidTrace = null;
            if (cmdl.hasOption(CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACE)) {
                numRequestedTasks++;
                componentPrintInvalidTrace = 
                        task_createExecutionTraceDumpComponent(PRINTINVALIDEXECTRACE_COMPONENT_NAME,
                        outputDir + File.separator + outputFnPrefix + INVALID_TRACES_FN_PREFIX + ".txt", true);
                invalidTraceProcessingComponents.add(componentPrintInvalidTrace);
            }
            AbstractTpanMessageTraceProcessingComponent componentPlotSeqDiagr = null;
            if (retVal && cmdl.hasOption(CMD_OPT_NAME_TASK_PLOTSEQD)) {
                numRequestedTasks++;
                componentPlotSeqDiagr = 
                        task_createSequenceDiagramPlotComponent(PLOTSEQDIAGR_COMPONENT_NAME,
                        outputDir + File.separator + outputFnPrefix);
                msgTraceProcessingComponents.add(componentPlotSeqDiagr);
            }
            DependencyGraphPlugin componentPlotDepGraph = null;
            if (retVal && cmdl.hasOption(CMD_OPT_NAME_TASK_PLOTDEPG)) {
                numRequestedTasks++;
                componentPlotDepGraph =
                        task_createDependencyGraphPlotComponent(PLOTDEPGRAPH_COMPONENT_NAME);
                msgTraceProcessingComponents.add(componentPlotDepGraph);
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
                    new TraceReconstructionFilter(TRACERECONSTR_COMPONENT_NAME, 2000, ignoreInvalidTraces, traceEquivClassMode, considerHostname, selectedTraces);
            for (AbstractTpanMessageTraceProcessingComponent c : msgTraceProcessingComponents) {
                mtReconstrFilter.addMessageTraceListener(c);
            }
            for (AbstractTpanExecutionTraceProcessingComponent c : execTraceProcessingComponents) {
                mtReconstrFilter.addExecutionTraceListener(c);
            }
            for (AbstractTpanExecutionTraceProcessingComponent c : invalidTraceProcessingComponents) {
                mtReconstrFilter.addInvalidExecutionTraceArtifactListener(c);
            }
            analysisInstance.addRecordConsumer(mtReconstrFilter);

            int numErrorCount = 0;
            try {
                analysisInstance.run();

                if (componentPlotDepGraph != null) {
                    componentPlotDepGraph.saveToDotFile(new File(outputDir + File.separator + outputFnPrefix + DEPENDENCY_GRAPH_FN_PREFIX).getCanonicalPath(), !traceEquivClassMode);
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
                retVal = task_genTraceEquivalenceReportForTraceSet(outputDir + File.separator + outputFnPrefix, mtReconstrFilter);
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
            if (true && ((!parseArgs(args) || !initFromArgs() || !dispatchTasks()))) {
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
        AbstractTpanMessageTraceProcessingComponent sqdWriter = new AbstractTpanMessageTraceProcessingComponent(name) {

            public void newTrace(MessageTrace t) throws TraceProcessingException {
                try {
                    SequenceDiagramPlugin.writeDotForMessageTrace(t, outputFnBase + "-" + t.getTraceId() + ".pic", considerHostname);
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
     * dependency graph for traces with IDs given in traceSet to
     * the directory outputFnPrefix.
     * If  traceSet is null, a dependency graph containing the information
     * of all traces is generated.
     *
     * @param inputDirName
     * @param outputFnPrefix
     * @param traceSet
     */
    private static DependencyGraphPlugin task_createDependencyGraphPlotComponent(final String name) {
        DependencyGraphPlugin depGraph = new DependencyGraphPlugin(name, considerHostname);
        return depGraph;
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
        AbstractTpanMessageTraceProcessingComponent mtWriter = new AbstractTpanMessageTraceProcessingComponent(name) {

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
        AbstractTpanExecutionTraceProcessingComponent etWriter = new AbstractTpanExecutionTraceProcessingComponent(name) {

            final PrintStream ps = new PrintStream(new FileOutputStream(myOutputFn));

            public void newTrace(ExecutionTrace t) {
                ps.println(t);
                this.reportSuccess(t.getTraceId());
            }

            @Override
            public void printStatusMessage() {
                super.printStatusMessage();
                int numTraces = this.getSuccessCount();
                System.out.println("Wrote " + numTraces + " execution trace" + (artifactMode?" artifact":"") + (numTraces > 1 ? "s" : "") + " to file '" + myOutputFn + "'");
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
                ps.println("Class " + numClasses++ + " ; cardinality: " + e.getValue() + "; # executions: " + t.getLength() + "; representative: " + t.getTraceId());
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
