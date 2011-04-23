package kieker.tools.traceAnalysis.plugins.visualization.callTree;

import kieker.tools.traceAnalysis.systemModel.util.AllocationComponentOperationPair;
import kieker.tools.traceAnalysis.systemModel.SynchronousCallMessage;
import kieker.tools.traceAnalysis.systemModel.repository.AllocationComponentOperationPairFactory;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 *
 * @author Andre van Hoorn
 */
public class TraceCallTreeNode extends AbstractCallTreeNode<AllocationComponentOperationPair> {

    private final AllocationComponentOperationPairFactory pairFactory;

    public TraceCallTreeNode(
            final int id,
            final SystemModelRepository systemEntityFactory,
            final AllocationComponentOperationPairFactory pairFactory,
            final AllocationComponentOperationPair entity,
            final boolean rootNode) {
        super(id, systemEntityFactory, entity, rootNode);
        this.pairFactory = pairFactory;
    }

    @Override
    public AbstractCallTreeNode<AllocationComponentOperationPair> newCall(SynchronousCallMessage callMsg) {
        AllocationComponentOperationPair destPair =
                this.pairFactory.getPairInstanceByPair(
                callMsg.getReceivingExecution().getAllocationComponent(),
                callMsg.getReceivingExecution().getOperation());
        TraceCallTreeNode destNode = 
                new TraceCallTreeNode(destPair.getId(), this.getSystemEntityFactory(), this.pairFactory, destPair, false);
        WeightedDirectedCallTreeEdge<AllocationComponentOperationPair> e =
                new WeightedDirectedCallTreeEdge<AllocationComponentOperationPair>(this, destNode);
        super.appendChildEdge(e);
        return destNode;
    }
}
