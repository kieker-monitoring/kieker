package kieker.tpan.plugins.dependencyGraph;

import java.util.Collection;
import java.util.TreeMap;
import kieker.tpan.datamodel.AllocationComponentInstance;
import kieker.tpan.datamodel.Execution;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn, Lena St&ouml;ver
 */
public class DependencyGraph<T> {

    private static final Log LOG = LogFactory.getLog(DependencyGraph.class);
    private TreeMap<Integer, DependencyGraphNode<T>> nodes =
            new TreeMap<Integer, DependencyGraphNode<T>>();
    private DependencyGraphNode<T> rootNode;

    public DependencyGraph(int rootNodeId, T rootEntity) {
        this.rootNode = new DependencyGraphNode(rootNodeId, rootEntity);
        this.nodes.put(rootNodeId, rootNode);
    }

    protected final DependencyGraphNode<T> getNode (int i){
        return this.nodes.get(i);
    }

    protected final void addNode (int i, DependencyGraphNode<T> node){
        this.nodes.put(i, node);
    }

    public final DependencyGraphNode<T> getRootNode() {
        return this.rootNode;
    }

    public Collection<DependencyGraphNode<T>> getNodes() {
        return nodes.values();
    }
}
