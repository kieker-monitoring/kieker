package kieker.tools.traceAnalysis.plugins.visualization.callTree;

import java.util.TreeMap;
import kieker.tools.traceAnalysis.systemModel.SynchronousCallMessage;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 *
 * @author Andre van Hoorn
 */
public abstract class AbstractAggregatedCallTreeNode<T> extends AbstractCallTreeNode<T> {

    /** For faster lookup of existing children */
    protected final TreeMap<Integer, WeightedDirectedCallTreeEdge<T>> childMap =
            new TreeMap<Integer, WeightedDirectedCallTreeEdge<T>>();

    public AbstractAggregatedCallTreeNode(
            final int id,
            final SystemModelRepository systemEntityFactory,
            final T entity,
            final boolean rootNode) {
        super(id, systemEntityFactory, entity, rootNode);
    }

    public abstract AbstractCallTreeNode<T> newCall(SynchronousCallMessage callMsg);
}
