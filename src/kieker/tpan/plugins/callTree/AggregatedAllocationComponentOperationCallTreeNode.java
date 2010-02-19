package kieker.tpan.plugins.callTree;

import kieker.tpan.datamodel.AllocationComponentInstance;
import kieker.tpan.datamodel.Operation;
import kieker.tpan.datamodel.SynchronousCallMessage;
import kieker.tpan.plugins.dependencyGraph.AllocationComponentOperationPair;
import kieker.tpan.plugins.dependencyGraph.AllocationComponentOperationPairFactory;

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
/**
 *
 * @author Andre van Hoorn
 */
public class AggregatedAllocationComponentOperationCallTreeNode extends AbstractAggregatedCallTreeNode<AllocationComponentOperationPair> {

    private final AllocationComponentOperationPairFactory pairFactory;

    public AggregatedAllocationComponentOperationCallTreeNode(final int id,
            final AllocationComponentOperationPairFactory pairFactory,
            final AllocationComponentOperationPair entity) {
        super(id, entity);
        this.pairFactory = pairFactory;
    }

    @Override
    public AbstractCallTreeNode<AllocationComponentOperationPair> newCall(SynchronousCallMessage callMsg) {
        AllocationComponentInstance allocationComponent =
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
                    pairFactory, destination);
            e = new WeightedDirectedCallTreeEdge<AllocationComponentOperationPair>(this, n);
            this.childMap.put(destination.getId(), e);
            super.appendChildEdge(e);
        }
        e.incOutgoingWeight();
        return n;
    }
}
