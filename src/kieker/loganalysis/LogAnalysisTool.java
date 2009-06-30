package kieker.loganalysis;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;

import java.util.TreeSet;
import kieker.common.logReader.filesystemReader.FilesystemReader;
import kieker.loganalysis.datamodel.ExecutionSequence;
import kieker.loganalysis.plugins.DependencyGraphPlugin;
import kieker.loganalysis.plugins.SequenceDiagramPlugin;
import kieker.loganalysis.recordConsumer.ExecutionSequenceRepositoryFiller;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * kieker.loganalysis.LogAnalysisTool
 *
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
 *
 * @author Andre van Hoorn
 */
public class LogAnalysisTool {

    private static final Log log = LogFactory.getLog(LogAnalysisTool.class);
    private static final String SEQUENCE_DIAGRAM_FN_PREFIX = "sequenceDiagram";
    private static final String DEPENDENCY_GRAPH_FN_PREFIX = "dependencyGraph";
    private static CommandLine cmdl = null;
    private static final CommandLineParser cmdlParser = new BasicParser();
    private static final HelpFormatter cmdHelpFormatter = new HelpFormatter();
    private static final Options cmdlOpts = new Options();
    private static String inputDir = null;
    private static String outputDir = null;
    private static String task = null;
    private static String outputFnPrefix = null;
    private static TreeSet<Long> selectedTraces = null;


    static {
        cmdlOpts.addOption(OptionBuilder.withLongOpt("inputdir").withArgName("dir").hasArg(true).isRequired(true).withDescription("Log directory to read data from").withValueSeparator('=').create("i"));
        cmdlOpts.addOption(OptionBuilder.withLongOpt("outputdir").withArgName("dir").hasArg(true).isRequired(true).withDescription("Directory for the generated file(s)").withValueSeparator('=').create("o"));
        cmdlOpts.addOption(OptionBuilder.withLongOpt("output-filename-prefix").withArgName("dir").hasArg(true).isRequired(false).withDescription("Prefix for output filenames\n").withValueSeparator('=').create("p"));

        //OptionGroup cmdlOptGroupTask = new OptionGroup();
        //cmdlOptGroupTask.isRequired();
        cmdlOpts.addOption(OptionBuilder.withLongOpt("plot-Sequence-Diagram").hasArg(false).withDescription("Generate sequence diagrams (.pic format) from log data").create());
        cmdlOpts.addOption(OptionBuilder.withLongOpt("plot-Dependency-Graph").hasArg(false).withDescription("Generate a dependency graph (.dot format) from log data").create());
        //cmdlOpts.addOptionGroup(cmdlOptGroupTask);

        cmdlOpts.addOption(OptionBuilder.withLongOpt("select-traces").withArgName("id0,...,idn").hasArgs().isRequired(false).withDescription("Consider only the traces identified by the comma-separated list of trace IDs. Defaults to all traces.").create("t"));
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
        cmdHelpFormatter.printHelp(LogAnalysisTool.class.getName(), cmdlOpts);
    }

    private static boolean initFromArgs() {
        inputDir = cmdl.getOptionValue("inputdir") + File.separator;
        outputDir = cmdl.getOptionValue("outputdir") + File.separator;
        outputFnPrefix = cmdl.getOptionValue("output-filename-prefix", "");
        if (cmdl.hasOption("select-traces")) { /* Parse liste of trace Ids */
            String[] traceIdList = cmdl.getOptionValues("select-traces");
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
        return true;
    }

    private static boolean dispatchTasks() {
        boolean retval = false;
        int numRequestedTasks = 0;

        try {
            if (cmdl.hasOption("plot-Sequence-Diagram")) {
                numRequestedTasks++;
                task_genSequenceDiagramsForTraceSet(inputDir, outputDir + File.separator + outputFnPrefix, selectedTraces);
            }
            if (cmdl.hasOption("plot-Dependency-Graph")) {
                numRequestedTasks++;
                task_genDependencyGraphsForTraceSet(inputDir, outputDir + File.separator + outputFnPrefix, selectedTraces);
            }
        } catch (Exception ex) {
            System.err.println("An error occured: " + ex);
            ex.printStackTrace();
            return false;
        }

        if (numRequestedTasks == 0) {
            System.err.println("No task requested");
            printUsage();
            return false;
        }

        return retval;
    }

    public static void main(String args[]) {
        if (!parseArgs(args) || !initFromArgs() || !dispatchTasks()) {
            System.exit(1);
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
    private static void task_genSequenceDiagramsForTraceSet(String inputDirName, String outputFnPrefix, final TreeSet<Long> traceIds) throws IOException {
        log.info("Reading traces from directory '" + inputDirName + "'");
        /* Read log data and collect execution traces */
        LogAnalysisInstance analysisInstance = new LogAnalysisInstance();
        //analysisInstance.addLogReader(new FSReader(inputDirName));
        analysisInstance.addLogReader(new FilesystemReader(inputDirName));
        ExecutionSequenceRepositoryFiller seqRepConsumer = new ExecutionSequenceRepositoryFiller();
        analysisInstance.addConsumer(seqRepConsumer);
        analysisInstance.run();

        /* Generate and output sequence diagrams */
        Enumeration<ExecutionSequence> seqEnum = seqRepConsumer.getExecutionSequenceRepository().repository.elements();
        int numPlots = 0;
        long lastTraceId = -1;
        String outputFnBase = new File(outputFnPrefix + SEQUENCE_DIAGRAM_FN_PREFIX).getCanonicalPath();
        while (seqEnum.hasMoreElements()) {
            ExecutionSequence t = seqEnum.nextElement();
            Long id = t.getTraceId();
            if (traceIds == null || traceIds.contains(id)) {
                //String fileName = "/tmp/seqDia" + msgTrace.traceId + ".pic";
                SequenceDiagramPlugin.writeDotForMessageTrace(t.toMessageSequence(), outputFnBase + "-" + id + ".pic");
                numPlots++;
                lastTraceId = t.getTraceId();
            }
        }
        if (numPlots > 0) {
            System.out.println("Wrote " + numPlots + " sequence diagram" + (numPlots > 1 ? "s" : "") + " to file" + (numPlots > 1 ? "s" : "") + " with name pattern '" + outputFnBase + "-<traceId>.pic'");
            System.out.println("Pic files can be converted using the pic2plot tool (package plotutils)");
            System.out.println("Example: pic2plot -T svg " + outputFnBase + "-" + ((numPlots > 0) ? lastTraceId : "<traceId>") + ".pic > " + outputFnBase + "-" + ((numPlots > 0) ? lastTraceId : "<traceId>") + ".svg");
        } else {
            System.out.println("Wrote 0 sequence diagrams");
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
    private static void task_genDependencyGraphsForTraceSet(String inputDirName, String outputFnPrefix, TreeSet<Long> traceIds) throws IOException {
        log.info("Reading traces from directory '" + inputDirName + "'");
        /* Read log data and collect execution traces */
        LogAnalysisInstance analysisInstance = new LogAnalysisInstance();
        //analysisInstance.addLogReader(new FSReader(inputDirName));
        analysisInstance.addLogReader(new FilesystemReader(inputDirName));
        ExecutionSequenceRepositoryFiller seqRepConsumer = new ExecutionSequenceRepositoryFiller();
        analysisInstance.addConsumer(seqRepConsumer);
        analysisInstance.run();

        /* Generate and output sequence diagrams */
        Collection<ExecutionSequence> seqEnum = seqRepConsumer.getExecutionSequenceRepository().repository.values();
        String outputFnBase = new File(outputFnPrefix + DEPENDENCY_GRAPH_FN_PREFIX).getCanonicalPath();
        DependencyGraphPlugin.writeDotFromExecutionTraces(seqEnum, outputFnBase + ".pic", traceIds);
        System.out.println("Wrote dependency graph to file '" + outputFnBase + ".pic" + "'");
        System.out.println("Dot file can be converted using the dot tool");
        System.out.println("Example: dot -T svg " + outputFnBase + ".pic" + " > " + outputFnBase + ".svg");
    }
}
