package kieker.tpan.plugin.traceAnalysis.visualization.callTree;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2010 Kieker Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 */
import kieker.tpan.datamodel.AllocationComponent;
import kieker.tpan.datamodel.util.AllocationComponentOperationPair;
import kieker.tpan.datamodel.Operation;
import kieker.tpan.datamodel.SynchronousCallMessage;
import kieker.tpan.datamodel.factories.AbstractSystemSubFactory;
import kieker.tpan.datamodel.factories.AllocationComponentOperationPairFactory;
import kieker.tpan.datamodel.factories.SystemEntityFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class AggregatedAllocationComponentOperationCallTreePlugin
        extends AggregatedCallTreePlugin<AllocationComponentOperationPair> {

    public AggregatedAllocationComponentOperationCallTreePlugin(
            final String name,
            AllocationComponentOperationPairFactory allocationComponentOperationPairFactory,
            SystemEntityFactory systemEntityFactory) {
        super(name, systemEntityFactory,
                new AggregatedAllocationComponentOperationCallTreeNode(
                AbstractSystemSubFactory.ROOT_ELEMENT_ID, systemEntityFactory,
                allocationComponentOperationPairFactory, allocationComponentOperationPairFactory.rootPair, true));
    }
}

class AggregatedAllocationComponentOperationCallTreeNode extends AbstractAggregatedCallTreeNode<AllocationComponentOperationPair> {

    private final AllocationComponentOperationPairFactory pairFactory;

    public AggregatedAllocationComponentOperationCallTreeNode(final int id,
            final SystemEntityFactory systemEntityFactory,
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
