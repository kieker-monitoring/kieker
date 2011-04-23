package kieker.tools.traceAnalysis.plugins.visualization.dependencyGraph;

/**
 *
 * @author Andre van Hoorn
 */
public class WeightedBidirectionalDependencyGraphEdge<T> {
    private DependencyGraphNode<T> source;
    private DependencyGraphNode<T> destination;

    public WeightedBidirectionalDependencyGraphEdge() { };

    public WeightedBidirectionalDependencyGraphEdge (
            final DependencyGraphNode<T> source,
            final DependencyGraphNode<T> destination){
        this.source = source;
        this.destination = destination;
    }

    public final DependencyGraphNode<T> getDestination() {
        return this.destination;
    }

    public final DependencyGraphNode<T> getSource() {
        return this.source;
    }

    public final void setDestination(DependencyGraphNode<T> destination) {
        this.destination = destination;
    }

    public final void setSource(DependencyGraphNode<T> source) {
        this.source = source;
    }

        private int outgoingWeight = 0;
    private int incomingWeight = 0;

    public final int getIncomingWeight() {
        return this.incomingWeight;
    }

    public final int getOutgoingWeight() {
        return this.outgoingWeight;
    }

    public final void incOutgoingWeight() {
        this.outgoingWeight++;
    }

    public final void incIncomingWeight() {
        this.incomingWeight++;
    }
}
