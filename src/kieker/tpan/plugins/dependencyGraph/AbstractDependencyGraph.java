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
public abstract class AbstractDependencyGraph<T> {

    private static final Log LOG = LogFactory.getLog(AbstractDependencyGraph.class);
    private TreeMap<Integer, DependencyNode<T>> nodes =
            new TreeMap<Integer, DependencyNode<T>>();
    private DependencyNode<T> rootNode;

    public AbstractDependencyGraph(int rootNodeId, T rootEntity) {
        this.rootNode = new DependencyNode(rootNodeId, rootEntity);
        this.nodes.put(rootNodeId, rootNode);
    }

    protected final DependencyNode<T> getNode (int i){
        return this.nodes.get(i);
    }

    protected final void setNode (int i, DependencyNode<T> node){
        this.nodes.put(i, node);
    }

    public abstract void addDependency(Execution sendingExecution, Execution receivingExecution);

    public final DependencyNode<T> getRootNode() {
        return this.rootNode;
    }

    public Collection<DependencyNode<T>> getNodes() {
        return nodes.values();
    }
}
