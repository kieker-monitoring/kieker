package kieker.tools.traceAnalysis.plugins.visualization.callTree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kieker.tools.traceAnalysis.systemModel.SynchronousCallMessage;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 *
 * @author Andre van Hoorn
 */
public abstract class AbstractCallTreeNode<T> {

    private final T entity;
    private final int id;
    private final SystemModelRepository systemEntityFactory;

    private final boolean rootNode;

    private final List<WeightedDirectedCallTreeEdge<T>> childEdges =
            new ArrayList<WeightedDirectedCallTreeEdge<T>>();

    public AbstractCallTreeNode(
            final int id,
            final SystemModelRepository systemEntityFactory,
            final T entity,
            final boolean rootNode) {
        this.id = id;
        this.systemEntityFactory = systemEntityFactory;
        this.rootNode = rootNode;
        this.entity = entity;
    }

    public final T getEntity() {
        return this.entity;
    }

    public final Collection<WeightedDirectedCallTreeEdge<T>> getChildEdges() {
        return this.childEdges;
    }

    /** Append edge to *sorted* list of children */
    protected final void appendChildEdge(WeightedDirectedCallTreeEdge<T> destination){
        this.childEdges.add(destination);
    }

    public abstract AbstractCallTreeNode<T> newCall(SynchronousCallMessage callMsg);

    public final int getId() {
        return this.id;
    }

    public final boolean isRootNode(){
        return this.rootNode;
    }

    protected final SystemModelRepository getSystemEntityFactory() {
        return systemEntityFactory;
    }
}
