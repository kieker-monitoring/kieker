package kieker.loganalysis.plugins;

/*
 * kieker.loganalysis.plugins.DependencyGraphPlugin.java
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
 */

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.TreeSet;
import kieker.loganalysis.datamodel.MessageTrace;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import kieker.loganalysis.datamodel.AdjacencyMatrix;
import kieker.loganalysis.datamodel.ExecutionTrace;
import kieker.loganalysis.datamodel.InvalidTraceException;
import kieker.loganalysis.datamodel.Message;

/**
 * Refactored copy from LogAnalysis-legacy tool
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr,
 */
public class DependencyGraphPlugin {

    private static final Log log = LogFactory.getLog(DependencyGraphPlugin.class);
 
    private DependencyGraphPlugin() {
    }

    private static void dotFromAdjacencyMatrix(AdjacencyMatrix adjMatrix, PrintStream ps) {
        // preamble:
        ps.println("digraph G {");
        StringBuilder edgestringBuilder = new StringBuilder();
        long[][] matrix = adjMatrix.getMatrixAsArray();
        String[] componentNames = adjMatrix.getComponentNames();
        for (int i = 0; i < matrix.length; i++) {
            edgestringBuilder.append("\n").append(i).append("[label =\"").append(componentNames[i]).append("\",shape=box];");
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int k = 0; k < matrix[i].length; k++) {
                if (matrix[i][k] > 0) {
                    edgestringBuilder.append("\n").append(i).append("->").append(k).append("[label = ").append(matrix[i][k]).append(",style=dashed,arrowhead=open,").append("weight =").append(matrix[i][k]).append(" ]");
                }
            }
        }
        ps.println(edgestringBuilder.toString());
        ps.println("}");
    }

    public static void writeDotFromExecutionTraces(Collection<ExecutionTrace> eTraces, String outputFilename, TreeSet<Long> traceIds) throws InvalidTraceException {
    AdjacencyMatrix adjMatrix = new AdjacencyMatrix();
    PrintStream ps = System.out;
        try {
            ps = new PrintStream(new FileOutputStream(outputFilename));
        } catch (FileNotFoundException e) {
            log.error("File not found.", e);
        }
        for (ExecutionTrace eTrace : eTraces) {
            MessageTrace msgTrace = eTrace.toMessageTrace();
            if (traceIds == null || traceIds.contains(eTrace.getTraceId())) {
            for (Message m : msgTrace.getSequenceAsVector()) {
                if (!m.callMessage) {
                    continue;
                }
                adjMatrix.addDependency(m.getSenderComponentName(), m.getReceiverComponentName());
            }}
        }
        dotFromAdjacencyMatrix(adjMatrix, ps);
        ps.flush();
        ps.close();
    }
}
