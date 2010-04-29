package kieker.tpan.plugin.traceAnalysis.visualization.dependencyGraph;

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
import kieker.tpan.plugin.traceAnalysis.traceReconstruction.AbstractMessageTraceProcessingPlugin;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import kieker.tpan.datamodel.MessageTrace;
import kieker.tpan.datamodel.factories.SystemEntityFactory;
import kieker.tpan.plugins.util.dot.DotFactory;

/**
 * Refactored copy from LogAnalysis-legacy tool
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr,
 */
public abstract class AbstractDependencyGraphPlugin<T> extends AbstractMessageTraceProcessingPlugin {

    private static final Log log = LogFactory.getLog(AbstractDependencyGraphPlugin.class);
    protected final DependencyGraph<T> dependencyGraph;

    public static final String STEREOTYPE_EXECUTION_CONTAINER="<<node>>";
    public static final String STEREOTYPE_ALLOCATION_COMPONENT="<<component>>";

    private static final String NODE_PREFIX="depNode_";

    public AbstractDependencyGraphPlugin(final String name,
            final SystemEntityFactory systemEntityFactory, 
            final DependencyGraph<T> dependencyGraph){
        super(name, systemEntityFactory);
        this.dependencyGraph = dependencyGraph;
    }

    protected abstract void dotEdges(Collection<DependencyGraphNode<T>> nodes,
            PrintStream ps, final boolean shortLabels);

    protected final String getNodeId (DependencyGraphNode<T> n){
        return NODE_PREFIX+n.getId();
    }

    protected void dotVertices(final Collection<DependencyGraphNode<T>> nodes,
            final PrintStream ps, final boolean includeWeights, final boolean plotSelfLoops) {
        for (DependencyGraphNode<T> curNode : nodes) {
            for (WeightedBidirectionalDependencyGraphEdge<T> outgoingDependency : curNode.getOutgoingDependencies()) {
                DependencyGraphNode<T> destNode =
                         outgoingDependency.getDestination();
                if (curNode == destNode && !plotSelfLoops) {
                    continue;
                }
                StringBuilder strBuild = new StringBuilder();
                if (includeWeights) {
                    strBuild.append(DotFactory.createConnection(
                            "",
                            getNodeId(curNode),
                            getNodeId(destNode),
                            "" + outgoingDependency.getOutgoingWeight(),
                            DotFactory.DOT_STYLE_DASHED,
                            DotFactory.DOT_ARROWHEAD_OPEN));
                } else {
                    strBuild.append(DotFactory.createConnection(
                            "",
                            getNodeId(curNode),
                            getNodeId(destNode),
                            DotFactory.DOT_STYLE_DASHED,
                            DotFactory.DOT_ARROWHEAD_OPEN));
                }
                ps.println(strBuild.toString());
            }
        }
    }

    private void graphToDot(
            final PrintStream ps, final boolean includeWeights,
            final boolean shortLabels, final boolean plotSelfLoops) {
        // preamble:
        ps.println("digraph G {\n rankdir="+DotFactory.DOT_DOT_RANKDIR_LR+";");

        dotEdges(this.dependencyGraph.getNodes(), ps,
                shortLabels);
        dotVertices(this.dependencyGraph.getNodes(), ps, includeWeights,
                plotSelfLoops);
        ps.println("}");
    }
    private int numGraphsSaved = 0;

    public final void saveToDotFile(final String outputFnBase, final boolean includeWeights,
            final boolean shortLabels, final boolean plotSelfLoops) throws FileNotFoundException {
        PrintStream ps = new PrintStream(new FileOutputStream(outputFnBase + ".dot"));
        this.graphToDot(ps, includeWeights, shortLabels, plotSelfLoops);
        ps.flush();
        ps.close();
        this.numGraphsSaved++;
        this.printMessage(new String[]{
                    "Wrote dependency graph to file '" + outputFnBase + ".dot" + "'",
                    "Dot file can be converted using the dot tool",
                    "Example: dot -T svg " + outputFnBase + ".dot" + " > " + outputFnBase + ".svg"
                });
    }

    @Override
    public void printStatusMessage() {
        super.printStatusMessage();
        System.out.println("Saved " + this.numGraphsSaved + " dependency graph" + (this.numGraphsSaved > 1 ? "s" : ""));
    }
}
