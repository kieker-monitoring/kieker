package kieker.tpan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import kieker.common.logReader.LogReaderExecutionException;
import kieker.common.logReader.RecordConsumerExecutionException;
import kieker.common.logReader.filesystemReader.FSReader;
import kieker.tpan.datamodel.ExecutionTrace;
import kieker.tpan.datamodel.InvalidTraceException;
import kieker.tpan.datamodel.MessageTrace;
import kieker.tpan.logReader.JMSReader;
import kieker.tpan.plugins.DependencyGraphPlugin;
import kieker.tpan.plugins.SequenceDiagramPlugin;
import kieker.tpan.recordConsumer.BriefJavaFxInformer;
import kieker.tpan.datamodel.MessageTraceRepository;
import kieker.tpan.recordConsumer.IExecutionTraceReceiver;
import kieker.tpan.recordConsumer.IMessageTraceReceiver;
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
    private static CommandLine cmdl = null;
    private static final CommandLineParser cmdlParser = new BasicParser();
    private static final HelpFormatter cmdHelpFormatter = new HelpFormatter();
    private static final Options cmdlOpts = new Options();
    private static String inputDir = null;
    private static String outputDir = null;
    private static String task = null;
    private static String outputFnPrefix = null;
    private static TreeSet<Long> selectedTraces = null;
    private static boolean onlyEquivClasses = true; // false;
    private static boolean considerHostname = true;

    private static final String CMD_OPT_NAME_INPUTDIR = "inputdir";
    private static final String CMD_OPT_NAME_OUTPUTDIR = "outputdir";
    private static final String CMD_OPT_NAME_OUTPUTFNPREFIX = "output-filename-prefix";
    private static final String CMD_OPT_NAME_SELECTTRACES = "select-traces";
    private static final String CMD_OPT_NAME_TRACEEQUIVCLASSES = "trace-equivalence-classes";
    private static final String CMD_OPT_NAME_CONSIDERHOSTNAMES = "consider-hostname";

    private static final String CMD_OPT_NAME_PLOTSEQD = "plot-Sequence-Diagram";
    private static final String CMD_OPT_NAME_PLOTDEPG = "plot-Dependency-Graph";
    private static final String CMD_OPT_NAME_PRINTMSGTRACE = "print-Message-Trace";
    private static final String CMD_OPT_NAME_PRINTEXECTRACE = "print-Execution-Trace";
    private static final String CMD_OPT_NAME_INITJMSREADER = "init-basic-JMS-reader";
    private static final String CMD_OPT_NAME_INITJMSREADERJFX = "init-basic-JMS-readerJavaFx";

    static {
        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_INPUTDIR).withArgName("dir").hasArg(true).isRequired(false).withDescription("Log directory to read data from").withValueSeparator('=').create("i"));
        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_OUTPUTDIR).withArgName("dir").hasArg(true).isRequired(true).withDescription("Directory for the generated file(s)").withValueSeparator('=').create("o"));
        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_OUTPUTFNPREFIX).withArgName("dir").hasArg(true).isRequired(false).withDescription("Prefix for output filenames\n").withValueSeparator('=').create("p"));

        //OptionGroup cmdlOptGroupTask = new OptionGroup();
        //cmdlOptGroupTask.isRequired();
        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_PLOTSEQD).hasArg(false).withDescription("Generate sequence diagrams (.pic format) from log data").create());
        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_PLOTDEPG).hasArg(false).withDescription("Generate a dependency graph (.dot format) from log data").create());
        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_PRINTMSGTRACE).hasArg(false).withDescription("Generate a message trace representation from log data").create());
        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_PRINTEXECTRACE).hasArg(false).withDescription("Generate an execution trace representation from log data").create());
        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_INITJMSREADER).hasArg(false).withDescription("Creates a jms reader and shows incomming data in the command line").create());
        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_INITJMSREADERJFX).hasArg(false).withDescription("Creates a jms reader and shows incomming data in the command line and visualizes with javafx").create());

        //cmdlOpts.addOptionGroup(cmdlOptGroupTask);
        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_SELECTTRACES).withArgName("id0,...,idn").hasArgs().isRequired(false).withDescription("Consider only the traces identified by the comma-separated list of trace IDs. Defaults to all traces.").create("t"));
        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_TRACEEQUIVCLASSES).withArgName("true|false").hasArgs().isRequired(false).withDescription("Detect trace equivalence classes and perform actions on representatives (defaults to false).").create());
        cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_CONSIDERHOSTNAMES).withArgName("true|false").hasArgs().isRequired(false).withDescription("Consider only the traces identified by the comma-separated list of trace IDs. Defaults to all traces (defaults to true).").create("t"));
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
        inputDir = cmdl.getOptionValue(CMD_OPT_NAME_INPUTDIR) + File.separator;
        if (inputDir.equals("${inputDir}/")) {
            log.error("Invalid input dir '" + inputDir + "'. Add it as command-line parameter to you ant call (e.g., ant run-tpan -DinputDir=/tmp) or to a properties file.");
        }
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

        String considerHostnameOptValStr = cmdl.getOptionValue(CMD_OPT_NAME_CONSIDERHOSTNAMES, "true");
        if (!(considerHostnameOptValStr.equals("true") || considerHostnameOptValStr.equals("false"))) {
            System.err.println("Invalid value for option " + CMD_OPT_NAME_CONSIDERHOSTNAMES + ": '" + considerHostnameOptValStr + "'");
            return false;
        }
        considerHostname = considerHostnameOptValStr.equals("true");

        String onlyEquivClassesOptValStr = cmdl.getOptionValue(CMD_OPT_NAME_TRACEEQUIVCLASSES, "false");
        if (!(onlyEquivClassesOptValStr.equals("true") || onlyEquivClassesOptValStr.equals("false"))) {
            System.err.println("Invalid value for option " + CMD_OPT_NAME_TRACEEQUIVCLASSES + ": '" + onlyEquivClassesOptValStr + "'");
            return false;
        }
        onlyEquivClasses = onlyEquivClassesOptValStr.equals("true");
 

        return true;
    }

    private static boolean dispatchTasks() {
        boolean retVal = true;
        int numRequestedTasks = 0;

        try {

            if (retVal && cmdl.hasOption(CMD_OPT_NAME_PRINTMSGTRACE)) {
                numRequestedTasks++;
                retVal = task_genMessageTracesForTraceSet(inputDir, outputDir + File.separator + outputFnPrefix, selectedTraces);
            }
            if (retVal && cmdl.hasOption(CMD_OPT_NAME_PRINTEXECTRACE)) {
                numRequestedTasks++;
                retVal = task_genExecutionTracesForTraceSet(inputDir, outputDir + File.separator + outputFnPrefix, selectedTraces);
            }
            if (retVal && cmdl.hasOption(CMD_OPT_NAME_PLOTSEQD)) {
                numRequestedTasks++;
                retVal = task_genSequenceDiagramsForTraceSet(inputDir, outputDir + File.separator + outputFnPrefix, selectedTraces);
            }
            if (retVal && cmdl.hasOption(CMD_OPT_NAME_PLOTDEPG)) {
                numRequestedTasks++;
                retVal = task_genDependencyGraphsForTraceSet(inputDir, outputDir + File.separator + outputFnPrefix, selectedTraces);
            }
            if (retVal && cmdl.hasOption(CMD_OPT_NAME_INITJMSREADER)) {
                numRequestedTasks++;
                retVal = task_initBasicJmsReader("tcp://127.0.0.1:3035/", "queue1");
                System.out.println("Finished to start task_initBasicJmsReader");
            }
            if (retVal && cmdl.hasOption(CMD_OPT_NAME_INITJMSREADERJFX)) {
                numRequestedTasks++;
                retVal = task_initBasicJmsReaderJavaFx("tcp://127.0.0.1:3035/", "queue1");
                System.out.println("Finished to start task_initBasicJmsReader");
            }

            if (!retVal) {
                System.err.println("A task failed");
            }
        } catch (Exception ex) {
            System.err.println("An error occured: " + ex.getMessage());
            System.err.println("");
            System.err.println("See 'kieker.log' for details");
            log.error("Exception", ex);
            retVal = false;
        }

        if (numRequestedTasks == 0) {
            System.err.println("No task requested");
            printUsage();
            retVal = false;
        }

        return retVal;
    }

    public static void main(String args[]) {
        try {
            if (true && ((!parseArgs(args) || !initFromArgs() || !dispatchTasks()))) {
                System.exit(1);
            }

            //Thread.sleep(2000);

            /* As long as we have a dependency from logAnalysis to tpmon,
             * we need t terminate tpmon explicitly. */
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
    private static boolean task_genSequenceDiagramsForTraceSet(final String inputDirName, final String outputFnPrefix, final TreeSet<Long> traceIds) throws IOException, InvalidTraceException, LogReaderExecutionException, RecordConsumerExecutionException {
        log.info("Reading traces from directory '" + inputDirName + "'");
        /* Read log data and collect execution traces */
        TpanInstance analysisInstance = new TpanInstance();
        //analysisInstance.setLogReader(new FSReader(inputDirName));
        analysisInstance.setLogReader(new FSReader(inputDirName));

        final AtomicBoolean retVal = new AtomicBoolean(true);
        final AtomicLong lastTraceId = new AtomicLong(-1);
        final AtomicInteger numPlots = new AtomicInteger(0);
        final String outputFnBase = new File(outputFnPrefix + SEQUENCE_DIAGRAM_FN_PREFIX).getCanonicalPath();
        IMessageTraceReceiver sqdWriter = new IMessageTraceReceiver() {

            // TODO: handle erros appropriately
            public void newTrace(MessageTrace t) {
                numPlots.incrementAndGet();
                try {
                    SequenceDiagramPlugin.writeDotForMessageTrace(t, outputFnBase + "-" + t.getTraceId() + ".pic", considerHostname);
                } catch (FileNotFoundException ex) {
                    log.error("FileNotFoundException: ", ex);
                    retVal.set(false);
                }
                lastTraceId.set(t.getTraceId());
            }
        };
        TraceReconstructionFilter mtReconstrFilter = new TraceReconstructionFilter(-1, false, onlyEquivClasses, considerHostname, traceIds);
        mtReconstrFilter.addMessageTraceListener(sqdWriter);
        analysisInstance.addRecordConsumer(mtReconstrFilter);
        analysisInstance.run();

        if (numPlots.intValue() > 0) {
            System.out.println("Wrote " + numPlots.intValue() + " sequence diagram" + (numPlots.intValue() > 1 ? "s" : "") + " to file" + (numPlots.intValue() > 1 ? "s" : "") + " with name pattern '" + outputFnBase + "-<traceId>.pic'");
            System.out.println("Pic files can be converted using the pic2plot tool (package plotutils)");
            System.out.println("Example: pic2plot -T svg " + outputFnBase + "-" + ((numPlots.intValue() > 0) ? lastTraceId : "<traceId>") + ".pic > " + outputFnBase + "-" + ((numPlots.intValue() > 0) ? lastTraceId : "<traceId>") + ".svg");
        } else {
            System.out.println("Wrote 0 sequence diagrams");
        }
        return retVal.get();
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
    private static boolean task_genDependencyGraphsForTraceSet(String inputDirName, String outputFnPrefix, TreeSet<Long> traceIds) throws IOException, InvalidTraceException, LogReaderExecutionException, RecordConsumerExecutionException {
        boolean retVal = true;
        log.info("Reading traces from directory '" + inputDirName + "'");
        /* Read log data and collect execution traces */
        TpanInstance analysisInstance = new TpanInstance();
        //analysisInstance.setLogReader(new FSReader(inputDirName));
        analysisInstance.setLogReader(new FSReader(inputDirName));
        final MessageTraceRepository mtRepo = new MessageTraceRepository();
        IMessageTraceReceiver repoFiller = new IMessageTraceReceiver() {

            // TODO: handle erros appropriately
            public void newTrace(MessageTrace t) {
                mtRepo.newTrace(t);
            }
        };
        TraceReconstructionFilter mtReconstrFilter = new TraceReconstructionFilter(-1, false, onlyEquivClasses, considerHostname, traceIds);
        mtReconstrFilter.addMessageTraceListener(repoFiller);

        analysisInstance.addRecordConsumer(mtReconstrFilter);
        analysisInstance.run();

        /* Generate and output dependency graphs */
        Collection<MessageTrace> mTraces = mtRepo.getMessageTraceRepository().values();
        String outputFnBase = new File(outputFnPrefix + DEPENDENCY_GRAPH_FN_PREFIX).getCanonicalPath();
        DependencyGraphPlugin.writeDotFromMessageTraces(mTraces, outputFnBase + ".dot", considerHostname);
        System.out.println("Wrote dependency graph to file '" + outputFnBase + ".dot" + "'");
        System.out.println("Dot file can be converted using the dot tool");
        System.out.println("Example: dot -T svg " + outputFnBase + ".dot" + " > " + outputFnBase + ".svg");
        return retVal;
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
    private static boolean task_genMessageTracesForTraceSet(String inputDirName, String outputFnPrefix, final TreeSet<Long> traceIds) throws IOException, InvalidTraceException, LogReaderExecutionException, RecordConsumerExecutionException {
        boolean retVal = true;
        log.info("Reading traces from directory '" + inputDirName + "'");
        /* Read log data and collect execution traces */
        TpanInstance analysisInstance = new TpanInstance();
        analysisInstance.setLogReader(new FSReader(inputDirName));

        final AtomicInteger numTraces = new AtomicInteger(0);

        String outputFn = new File(outputFnPrefix + MESSAGE_TRACES_FN_PREFIX + ".txt").getCanonicalPath();
        PrintStream ps = null;
        try {
            final PrintStream myPs = new PrintStream(new FileOutputStream(outputFn));
            ps = myPs;
            IMessageTraceReceiver mtWriter = new IMessageTraceReceiver() {

                public void newTrace(MessageTrace t) {
                    numTraces.incrementAndGet();
                    myPs.println(t);
                }
            };
            TraceReconstructionFilter mtReconstrFilter = new TraceReconstructionFilter(-1, false, onlyEquivClasses, considerHostname, traceIds);
            mtReconstrFilter.addMessageTraceListener(mtWriter);
            analysisInstance.addRecordConsumer(mtReconstrFilter);
            analysisInstance.run();

            System.out.println("Wrote " + numTraces.intValue() + " messageTraces" + (numTraces.intValue() > 1 ? "s" : "") + " to file '" + outputFn + "'");
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
    private static boolean task_genExecutionTracesForTraceSet(String inputDirName, String outputFnPrefix, final TreeSet<Long> traceIds) throws IOException, LogReaderExecutionException, RecordConsumerExecutionException {
        boolean retVal = true;
        log.info("Reading traces from directory '" + inputDirName + "'");
        /* Read log data and collect execution traces */
        TpanInstance analysisInstance = new TpanInstance();
        analysisInstance.setLogReader(new FSReader(inputDirName));

        final AtomicInteger numTraces = new AtomicInteger(0);

        String outputFn = new File(outputFnPrefix + EXECUTION_TRACES_FN_PREFIX + ".txt").getCanonicalPath();
        PrintStream ps = null;
        try {
            final PrintStream myPs = new PrintStream(new FileOutputStream(outputFn));
            ps = myPs;
            IExecutionTraceReceiver etWriter = new IExecutionTraceReceiver() {

                public void newTrace(ExecutionTrace t) {
                    numTraces.incrementAndGet();
                    myPs.println(t);
                }
            };
            TraceReconstructionFilter mtReconstrFilter = new TraceReconstructionFilter(-1, false, onlyEquivClasses, considerHostname, traceIds);
            mtReconstrFilter.addExecutionTraceListener(etWriter);
            analysisInstance.addRecordConsumer(mtReconstrFilter);
            analysisInstance.run();

            System.out.println("Wrote " + numTraces.intValue() + " executionTraces" + (numTraces.intValue() > 1 ? "s" : "") + " to file '" + outputFn + "'");
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
