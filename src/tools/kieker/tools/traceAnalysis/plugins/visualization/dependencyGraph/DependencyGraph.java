package kieker.tools.traceAnalysis.plugins.visualization.dependencyGraph;

import java.util.Collection;
import java.util.TreeMap;

/**
 *
 * @author Andre van Hoorn, Lena St&ouml;ver
 */
public class DependencyGraph<T> {

    //private static final Log LOG = LogFactory.getLog(DependencyGraph.class);
	
    private final TreeMap<Integer, DependencyGraphNode<T>> nodes =
            new TreeMap<Integer, DependencyGraphNode<T>>();
    private final DependencyGraphNode<T> rootNode;

    public DependencyGraph(final int rootNodeId, final T rootEntity) {
        this.rootNode = new DependencyGraphNode<T>(rootNodeId, rootEntity);
        this.nodes.put(rootNodeId, this.rootNode);
    }

    protected final DependencyGraphNode<T> getNode (final int i){
        return this.nodes.get(i);
    }

    protected final void addNode (final int i, final DependencyGraphNode<T> node){
        this.nodes.put(i, node);
    }

    public final DependencyGraphNode<T> getRootNode() {
        return this.rootNode;
    }

    public Collection<DependencyGraphNode<T>> getNodes() {
        return this.nodes.values();
    }

    /** Return number of nodes */
    public int size(){ return this.nodes.size(); }
}
