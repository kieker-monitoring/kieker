package kieker.tools.traceAnalysis.plugins.visualization.dependencyGraph;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;

import kieker.tools.traceAnalysis.plugins.AbstractMessageTraceProcessingPlugin;
import kieker.tools.traceAnalysis.plugins.visualization.util.dot.DotFactory;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * Refactored copy from LogAnalysis-legacy tool
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr,
 */
public abstract class AbstractDependencyGraphPlugin<T> extends AbstractMessageTraceProcessingPlugin {

    //private static final Log log = LogFactory.getLog(AbstractDependencyGraphPlugin.class);

    protected final DependencyGraph<T> dependencyGraph;

    public static final String STEREOTYPE_EXECUTION_CONTAINER="<<execution container>>";
    public static final String STEREOTYPE_ASSEMBLY_COMPONENT="<<assembly component>>";
    public static final String STEREOTYPE_ALLOCATION_COMPONENT="<<deployment component>>";

    private static final String NODE_PREFIX="depNode_";

    public AbstractDependencyGraphPlugin(final String name,
            final SystemModelRepository systemEntityFactory,
            final DependencyGraph<T> dependencyGraph){
        super(name, systemEntityFactory);
        this.dependencyGraph = dependencyGraph;
    }

    protected abstract void dotEdges(Collection<DependencyGraphNode<T>> nodes,
            PrintStream ps, final boolean shortLabels);

    protected final String getNodeId (final DependencyGraphNode<T> n){
        return AbstractDependencyGraphPlugin.NODE_PREFIX+n.getId();
    }

    protected void dotVertices(final Collection<DependencyGraphNode<T>> nodes,
            final PrintStream ps, final boolean includeWeights, final boolean plotSelfLoops) {
        for (final DependencyGraphNode<T> curNode : nodes) {
            for (final WeightedBidirectionalDependencyGraphEdge<T> outgoingDependency : curNode.getOutgoingDependencies()) {
                final DependencyGraphNode<T> destNode =
                         outgoingDependency.getDestination();
                if ((curNode == destNode) && !plotSelfLoops) {
                    continue;
                }
                final StringBuilder strBuild = new StringBuilder();
                if (includeWeights) {
                    strBuild.append(DotFactory.createConnection(
                            "",
                            this.getNodeId(curNode),
                            this.getNodeId(destNode),
                            "" + outgoingDependency.getOutgoingWeight(),
                            DotFactory.DOT_STYLE_DASHED,
                            DotFactory.DOT_ARROWHEAD_OPEN));
                } else {
                    strBuild.append(DotFactory.createConnection(
                            "",
                            this.getNodeId(curNode),
                            this.getNodeId(destNode),
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

        this.dotEdges(this.dependencyGraph.getNodes(), ps,
                shortLabels);
        this.dotVertices(this.dependencyGraph.getNodes(), ps, includeWeights,
                plotSelfLoops);
        ps.println("}");
    }
    private int numGraphsSaved = 0;

    public final void saveToDotFile(final String outputFnBase, final boolean includeWeights,
            final boolean shortLabels, final boolean plotSelfLoops) throws FileNotFoundException {
        final PrintStream ps = new PrintStream(new FileOutputStream(outputFnBase + ".dot"));
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
