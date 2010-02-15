package kieker.tpan.plugins;

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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import kieker.tpan.datamodel.MessageTrace;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import kieker.tpan.datamodel.AdjacencyMatrix;
import kieker.tpan.datamodel.Message;
import kieker.tpan.datamodel.system.factories.SystemEntityFactory;

/**
 * Refactored copy from LogAnalysis-legacy tool
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr,
 */
public class DependencyGraphPlugin extends AbstractTpanMessageTraceProcessingComponent {

    private static final Log log = LogFactory.getLog(DependencyGraphPlugin.class);
    private AdjacencyMatrix adjMatrix = new AdjacencyMatrix();
    private final boolean considerHost;

    public DependencyGraphPlugin(final String name, SystemEntityFactory systemEntityFactory, final boolean considerHost) {
        super(name, systemEntityFactory);
        this.considerHost = considerHost;
    }

    private void dotFromAdjacencyMatrix(AdjacencyMatrix adjMatrix, PrintStream ps, final boolean includeWeights) {
        // preamble:
        ps.println("digraph G {");
        StringBuilder edgestringBuilder = new StringBuilder();
        long[][] matrix = adjMatrix.getMatrixAsArray();
        String[] componentNames = adjMatrix.getComponentNames();
        for (int i = 0; i < matrix.length; i++) {
            edgestringBuilder.append("\n").append(i).append("[label =\"")
                    .append(componentNames[i]).append("\",shape=box];");
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int k = 0; k < matrix[i].length; k++) {
                if (matrix[i][k] > 0) {
                    edgestringBuilder.append("\n").append(i).append("->").append(k)
                            .append("[style=dashed,arrowhead=open");
                    if (includeWeights){
                        edgestringBuilder.append(",label = ").append(matrix[i][k]).append(", weight =").append(matrix[i][k]);
                    }
                    edgestringBuilder.append(" ]");
                }
            }
        }
        ps.println(edgestringBuilder.toString());
        ps.println("}");
    }

    private int numGraphsSaved = 0;

    public void saveToDotFile(final String outputFnBase, final boolean includeWeights) throws FileNotFoundException {
        PrintStream ps = new PrintStream(new FileOutputStream(outputFnBase + ".dot"));
        this.dotFromAdjacencyMatrix(adjMatrix, ps, includeWeights);
        ps.flush();
        ps.close();
        this.numGraphsSaved++;
        this.printMessage(new String[] {
        "Wrote dependency graph to file '" + outputFnBase + ".dot" + "'",
        "Dot file can be converted using the dot tool",
        "Example: dot -T svg " + outputFnBase + ".dot" + " > " + outputFnBase + ".svg"
        });
    }

    public void newTrace(MessageTrace t) {
        for (Message m : t.getSequenceAsVector()) {
            if (!m.callMessage) {
                continue;
            }
            String senderLabel = m.getSenderLabel(considerHost);
            String receiverLabel = m.getReceiverLabel(considerHost);

            adjMatrix.addDependency(senderLabel, receiverLabel);
        }
        this.reportSuccess(t.getTraceId());
    }

    @Override
    public void printStatusMessage() {
        super.printStatusMessage();
        System.out.println("Saved " + this.numGraphsSaved + " dependency graph" + (this.numGraphsSaved > 1 ? "s" : ""));
    }

    @Override
    public void cleanup() {
        // no cleanup required
    }
}
