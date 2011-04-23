package kieker.tools.traceAnalysis.plugins.visualization.callTree;

import java.io.File;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.util.AllocationComponentOperationPair;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.SynchronousCallMessage;
import kieker.tools.traceAnalysis.systemModel.repository.AbstractSystemSubRepository;
import kieker.tools.traceAnalysis.systemModel.repository.AllocationComponentOperationPairFactory;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 *
 * @author Andre van Hoorn
 */
public class AggregatedAllocationComponentOperationCallTreePlugin
        extends AggregatedCallTreePlugin<AllocationComponentOperationPair> {

    public AggregatedAllocationComponentOperationCallTreePlugin(
            final String name,
            final AllocationComponentOperationPairFactory allocationComponentOperationPairFactory,
            final SystemModelRepository systemEntityFactory,
            final File dotOutputFile,
            final boolean includeWeights,
            final boolean shortLabels) {
        super(name, systemEntityFactory,
                new AggregatedAllocationComponentOperationCallTreeNode(
                AbstractSystemSubRepository.ROOT_ELEMENT_ID, systemEntityFactory,
                allocationComponentOperationPairFactory,
                allocationComponentOperationPairFactory.rootPair, true), // root node
                dotOutputFile,
                includeWeights,
                shortLabels);
    }
}

class AggregatedAllocationComponentOperationCallTreeNode extends AbstractAggregatedCallTreeNode<AllocationComponentOperationPair> {

    private final AllocationComponentOperationPairFactory pairFactory;

    public AggregatedAllocationComponentOperationCallTreeNode(final int id,
            final SystemModelRepository systemEntityFactory,
            final AllocationComponentOperationPairFactory pairFactory,
            final AllocationComponentOperationPair entity,
            final boolean rootNode) {
        super(id, systemEntityFactory, entity, rootNode);
        this.pairFactory = pairFactory;
    }

    @Override
    public AbstractCallTreeNode<AllocationComponentOperationPair> newCall(SynchronousCallMessage callMsg) {
        AllocationComponent allocationComponent =
                callMsg.getReceivingExecution().getAllocationComponent();
        Operation op = callMsg.getReceivingExecution().getOperation();
        AllocationComponentOperationPair destination = // will never be null!
                this.pairFactory.getPairInstanceByPair(allocationComponent, op);
        WeightedDirectedCallTreeEdge<AllocationComponentOperationPair> e =
                this.childMap.get(destination.getId());
        AbstractCallTreeNode<AllocationComponentOperationPair> n;
        if (e != null) {
            n = e.getDestination();
        } else {
            n = new AggregatedAllocationComponentOperationCallTreeNode(destination.getId(),
                    this.getSystemEntityFactory(), pairFactory, destination, false); // ! rootNode
            e = new WeightedDirectedCallTreeEdge<AllocationComponentOperationPair>(this, n);
            this.childMap.put(destination.getId(), e);
            super.appendChildEdge(e);
        }
        e.incOutgoingWeight();
        return n;
    }
}
