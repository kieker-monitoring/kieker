package kieker.loganalysis;

import java.io.File;
import java.util.Collection;
import java.util.Enumeration;
import java.util.TreeSet;
import kieker.common.logReader.filesystemReader.FilesystemReader;
import kieker.loganalysis.datamodel.ExecutionSequence;
import kieker.loganalysis.plugins.DependencyGraphPlugin;
import kieker.loganalysis.plugins.SequenceDiagramPlugin;
import kieker.loganalysis.recordConsumer.ExecutionSequenceRepositoryFiller;
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

    public static void main(String args[]) {
        log.info("Hi, this is Kieker.LogAnalysis");

        String inputDir = System.getProperty("inputDir");
        if (inputDir == null || inputDir.length() == 0 || inputDir.equals("${inputDir}")) {
            log.error("No input dir found!");
            log.error("Provide an input dir as system property.");
            log.error("Example to read all tpmon-* files from /tmp:\n" +
                    "                    ant -DinputDir=/tmp/ run-logAnalysis    ");
            System.exit(1);
        } else {
            log.info("Reading all tpmon-* files from " + inputDir);
        }

        LogAnalysisTool.task_genSequenceDiagramsForTraceSet(inputDir, inputDir, null);
        LogAnalysisTool.task_genDependencyGraphsForTraceSet(inputDir, inputDir+File.separator+DEPENDENCY_GRAPH_FN_PREFIX + ".pic");

        log.info("Bye, this was Kieker.LogAnalysis");
        System.exit(0);
    }

    /**
     * Reads the traces from the directory inputDirName and write the
     * sequence diagrams for traces with IDs given in traceSet to
     * the directory outputDirname.
     * If  traceSet is null, a sequence diagram for each trace is generated.
     *
     * @param inputDirName
     * @param outputDirname
     * @param traceSet
     */
    private static void task_genSequenceDiagramsForTraceSet(String inputDirName, String outputDirname, TreeSet<Long> traceIds) {
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
        while (seqEnum.hasMoreElements()) {
            ExecutionSequence t = seqEnum.nextElement();
            if (traceIds == null || traceIds.contains(t.getTraceId())) {
                //String fileName = "/tmp/seqDia" + msgTrace.traceId + ".pic";
                SequenceDiagramPlugin.writeDotForMessageTrace(t.toMessageSequence(), outputDirname + File.separator + SEQUENCE_DIAGRAM_FN_PREFIX + "-" + t.getTraceId() + ".pic");
            }
        }
        log.info("Wrote sequence diagrams to directory '" + outputDirname + "'");
            System.out.println("Pic file can be converted using pic2plot tool (package plotutils)");
            System.out.println("Eaxample: pic2plot -T svg" + SEQUENCE_DIAGRAM_FN_PREFIX+"-3.pic > " + SEQUENCE_DIAGRAM_FN_PREFIX+"-3.svg");

    }

    /**
     * Reads the traces from the directory inputDirName and write the
     * sequence diagrams for traces with IDs given in traceSet to
     * the directory outputDirname.
     * If  traceSet is null, a sequence diagram for each trace is generated.
     *
     * @param inputDirName
     * @param outputDirname
     * @param traceSet
     */
    private static void task_genDependencyGraphsForTraceSet(String inputDirName, String outputFilename) {
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
        DependencyGraphPlugin.writeDotFromExecutionTraces(seqEnum, outputFilename);
        log.info("Wrote dependency graph to file '" + outputFilename + "'");
        System.out.println("Dot file can be converted using dot tool");
        System.out.println("For example: dot -T svg " + outputFilename + " > " + DEPENDENCY_GRAPH_FN_PREFIX + ".svg");
    }
}
