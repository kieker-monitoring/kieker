package kieker.tpan.plugins.callTree;

import kieker.tpan.datamodel.factories.AssemblyComponentOperationPairFactory;
import kieker.tpan.datamodel.AssemblyComponentOperationPair;
import kieker.tpan.datamodel.AssemblyComponentInstance;
import kieker.tpan.datamodel.Operation;
import kieker.tpan.datamodel.SynchronousCallMessage;

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
public class AggregatedAssemblyComponentOperationCallTreeNode extends AbstractAggregatedCallTreeNode<AssemblyComponentOperationPair> {

    private final AssemblyComponentOperationPairFactory pairFactory;

    public AggregatedAssemblyComponentOperationCallTreeNode(final int id,
            final AssemblyComponentOperationPairFactory pairFactory,
            final AssemblyComponentOperationPair entity) {
        super(id, entity);
        this.pairFactory = pairFactory;
    }

    @Override
    public AbstractCallTreeNode<AssemblyComponentOperationPair> newCall(SynchronousCallMessage callMsg) {
        AssemblyComponentInstance AssemblyComponent =
                callMsg.getReceivingExecution().getAllocationComponent().getAssemblyComponent();
        Operation op = callMsg.getReceivingExecution().getOperation();
        AssemblyComponentOperationPair destination = // will never be null!
                this.pairFactory.getPairInstanceByPair(AssemblyComponent, op);
        WeightedDirectedCallTreeEdge<AssemblyComponentOperationPair> e =
                this.childMap.get(destination.getId());
        AbstractCallTreeNode<AssemblyComponentOperationPair> n;
        if (e != null) {
            n = e.getDestination();
        } else {
            n = new AggregatedAssemblyComponentOperationCallTreeNode(destination.getId(),
                    pairFactory, destination);
            e = new WeightedDirectedCallTreeEdge<AssemblyComponentOperationPair>(this, n);
            this.childMap.put(destination.getId(), e);
            super.appendChildEdge(e);
        }
        e.incOutgoingWeight();
        return n;
    }
}
