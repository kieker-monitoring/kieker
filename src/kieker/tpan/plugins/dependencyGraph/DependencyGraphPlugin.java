package kieker.tpan.plugins.dependencyGraph;

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
import kieker.tpan.plugins.traceReconstruction.AbstractTpanMessageTraceProcessingComponent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import kieker.tpan.datamodel.AllocationComponentInstance;
import kieker.tpan.datamodel.Execution;
import kieker.tpan.datamodel.Message;
import kieker.tpan.datamodel.MessageTrace;
import kieker.tpan.datamodel.SynchronousReplyMessage;
import kieker.tpan.datamodel.factories.SystemEntityFactory;

/**
 * Refactored copy from LogAnalysis-legacy tool
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr,
 */
public class DependencyGraphPlugin extends AbstractTpanMessageTraceProcessingComponent {

    private static final Log log = LogFactory.getLog(DependencyGraphPlugin.class);
    private AdjacencyMatrix adjMatrix;

    public DependencyGraphPlugin(final String name,
            final SystemEntityFactory systemEntityFactory) {
        super(name, systemEntityFactory);
        this.adjMatrix = new AdjacencyMatrix(systemEntityFactory);
    }

    private String nodeLabel(final AllocationComponentDependencyNode node,
            final boolean shortLabels){
        AllocationComponentInstance component = node.getAllocationComponent();
        if (component == super.getSystemEntityFactory().getAllocationFactory().rootAllocationComponent){
            return "$";
        }

        String resourceContainerName = component.getExecutionContainer().getName();
        String assemblyComponentName = component.getAssemblyComponent().getName();
        String componentTypePackagePrefx = component.getAssemblyComponent().getType().getPackageName();
        String componentTypeIdentifier = component.getAssemblyComponent().getType().getTypeName();

        StringBuilder strBuild = new StringBuilder(resourceContainerName).append("::")
                .append(assemblyComponentName).append(":");
        if (!shortLabels){
            strBuild.append(componentTypePackagePrefx);
        } else {
            strBuild.append("..");
        }
        strBuild.append(componentTypeIdentifier);
        return strBuild.toString();
    }

    private void dotEdges(Collection<AllocationComponentDependencyNode> nodes,
            PrintStream ps, final boolean shortLabels) {
        StringBuilder strBuild = new StringBuilder();
        for (AllocationComponentDependencyNode node : nodes) {
            strBuild.append(node.getId()).append("[label =\"")
                    .append(nodeLabel(node, shortLabels)).append("\",shape=box];\n");
        }
        ps.println(strBuild.toString());
    }

    /** Traverse tree recursively and generate dot code for vertices. */
    private void dotVerticesFromSubTree(final AllocationComponentDependencyNode n,
        final PrintStream ps, final boolean includeWeights) {
        for (AllocationComponentDependencyEdge outgoingDependency : n.getOutgoingDependencies()) {
            AllocationComponentDependencyNode destNode = outgoingDependency.getDestination();
            StringBuilder strBuild = new StringBuilder();
            strBuild.append("\n").append(n.getId()).append("->")
                    .append(destNode.getId()).append("[style=dashed,arrowhead=open");
            if (includeWeights) {
                strBuild.append(",label = ").append(outgoingDependency.getOutgoingWeight()).append(", weight =").append(outgoingDependency.getOutgoingWeight());
            }
            strBuild.append(" ]");
            dotVerticesFromSubTree(destNode, ps, includeWeights);
            ps.println(strBuild.toString());
        }
    }

    private void dotFromAdjacencyMatrix(
            final PrintStream ps, final boolean includeWeights,
            final boolean shortLabels) {
        // preamble:
        ps.println("digraph G {");
        StringBuilder edgestringBuilder = new StringBuilder();

        dotEdges(this.adjMatrix.getAllocationComponentNodes(), ps,
                shortLabels);
        dotVerticesFromSubTree(this.adjMatrix.getAllocationComponentDependenciesRootNode(),
                ps, includeWeights);

        ps.println(edgestringBuilder.toString());
        ps.println("}");
    }

    private int numGraphsSaved = 0;

    public void saveToDotFile(final String outputFnBase, final boolean includeWeights,
            final boolean considerHost, final boolean shortLabels) throws FileNotFoundException {
        PrintStream ps = new PrintStream(new FileOutputStream(outputFnBase + ".dot"));
        this.dotFromAdjacencyMatrix(ps, includeWeights, shortLabels);
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
            if (m instanceof SynchronousReplyMessage) {
                continue;
            }
            Execution senderExecution = m.getSendingExecution();
            Execution receiverExecution = m.getReceivingExecution();
            adjMatrix.addDependency(senderExecution, receiverExecution);
      log.info("New dependency" + senderExecution.getOperation() + "->" + receiverExecution.getOperation());
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
