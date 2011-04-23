package kieker.tools.traceAnalysis.plugins.visualization.dependencyGraph;

import java.util.Collection;
import java.util.TreeMap;

/**
 *
 * @author Andre van Hoorn
 */
public class DependencyGraphNode<T> {

    private final T entity;
    private final int id;
    private final TreeMap<Integer, WeightedBidirectionalDependencyGraphEdge<T>> incomingDependencies = new TreeMap<Integer, WeightedBidirectionalDependencyGraphEdge<T>>();
    private final TreeMap<Integer, WeightedBidirectionalDependencyGraphEdge<T>> outgoingDependencies = new TreeMap<Integer, WeightedBidirectionalDependencyGraphEdge<T>>();

    public DependencyGraphNode(final int id, final T entity) {
        this.id = id;
        this.entity = entity;
    }

    public final T getEntity() {
        return this.entity;
    }

    public final Collection<WeightedBidirectionalDependencyGraphEdge<T>> getIncomingDependencies() {
        return this.incomingDependencies.values();
    }

    public final Collection<WeightedBidirectionalDependencyGraphEdge<T>> getOutgoingDependencies() {
        return this.outgoingDependencies.values();
    }

    public void addOutgoingDependency(DependencyGraphNode<T> destination) {
        WeightedBidirectionalDependencyGraphEdge<T> e = this.outgoingDependencies.get(destination.getId());
        if (e == null) {
            e = new WeightedBidirectionalDependencyGraphEdge<T>();
            e.setSource(this);
            e.setDestination(destination);
            this.outgoingDependencies.put(destination.getId(), e);
        }
        e.incOutgoingWeight();
    }

    public void addIncomingDependency(DependencyGraphNode<T> source) {
        WeightedBidirectionalDependencyGraphEdge<T> e = this.incomingDependencies.get(source.getId());
        if (e == null) {
            e = new WeightedBidirectionalDependencyGraphEdge<T>();
            e.setSource(this);
            e.setDestination(source);
            this.incomingDependencies.put(source.getId(), e);
        }
        e.incIncomingWeight();
    }

    public final int getId() {
        return this.id;
    }
}
