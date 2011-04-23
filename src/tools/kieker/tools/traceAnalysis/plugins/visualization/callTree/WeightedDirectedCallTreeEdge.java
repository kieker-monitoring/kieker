package kieker.tools.traceAnalysis.plugins.visualization.callTree;

/**
 *
 * @author Andre van Hoorn
 */
public class WeightedDirectedCallTreeEdge<T> {

    private AbstractCallTreeNode<T> source;
    private AbstractCallTreeNode<T> destination;

    private WeightedDirectedCallTreeEdge() {
    }

    ;

    public WeightedDirectedCallTreeEdge(
            final AbstractCallTreeNode<T> source,
            final AbstractCallTreeNode<T> destination) {
        this.source = source;
        this.destination = destination;
    }

    public final AbstractCallTreeNode<T> getDestination() {
        return this.destination;
    }

    public final AbstractCallTreeNode<T> getSource() {
        return this.source;
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
