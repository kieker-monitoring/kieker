/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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
 ***************************************************************************/

package kieker.tools.traceAnalysis.plugins.visualization.callTree;

import java.io.File;

import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.SynchronousCallMessage;
import kieker.tools.traceAnalysis.systemModel.repository.AbstractSystemSubRepository;
import kieker.tools.traceAnalysis.systemModel.repository.AllocationComponentOperationPairFactory;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.tools.traceAnalysis.systemModel.util.AllocationComponentOperationPair;

/**
 * 
 * @author Andre van Hoorn
 */
public class AggregatedAllocationComponentOperationCallTreePlugin extends AggregatedCallTreePlugin<AllocationComponentOperationPair> {

	public AggregatedAllocationComponentOperationCallTreePlugin(final String name,
			final AllocationComponentOperationPairFactory allocationComponentOperationPairFactory, final SystemModelRepository systemEntityFactory,
			final File dotOutputFile, final boolean includeWeights, final boolean shortLabels) {
		super(name, systemEntityFactory, new AggregatedAllocationComponentOperationCallTreeNode(AbstractSystemSubRepository.ROOT_ELEMENT_ID, systemEntityFactory,
				allocationComponentOperationPairFactory, allocationComponentOperationPairFactory.rootPair, true), // root node
				dotOutputFile, includeWeights, shortLabels);
	}
}

class AggregatedAllocationComponentOperationCallTreeNode extends AbstractAggregatedCallTreeNode<AllocationComponentOperationPair> {

	private final AllocationComponentOperationPairFactory pairFactory;

	public AggregatedAllocationComponentOperationCallTreeNode(final int id, final SystemModelRepository systemEntityFactory,
			final AllocationComponentOperationPairFactory pairFactory, final AllocationComponentOperationPair entity, final boolean rootNode) {
		super(id, systemEntityFactory, entity, rootNode);
		this.pairFactory = pairFactory;
	}

	@Override
	public AbstractCallTreeNode<AllocationComponentOperationPair> newCall(final SynchronousCallMessage callMsg) {
		final AllocationComponent allocationComponent = callMsg.getReceivingExecution().getAllocationComponent();
		final Operation op = callMsg.getReceivingExecution().getOperation();
		final AllocationComponentOperationPair destination = // will never be null!
		this.pairFactory.getPairInstanceByPair(allocationComponent, op);
		WeightedDirectedCallTreeEdge<AllocationComponentOperationPair> e = this.childMap.get(destination.getId());
		AbstractCallTreeNode<AllocationComponentOperationPair> n;
		if (e != null) {
			n = e.getDestination();
		} else {
			n = new AggregatedAllocationComponentOperationCallTreeNode(destination.getId(), this.getSystemEntityFactory(), this.pairFactory, destination, false); // !
																																									// rootNode
			e = new WeightedDirectedCallTreeEdge<AllocationComponentOperationPair>(this, n);
			this.childMap.put(destination.getId(), e);
			super.appendChildEdge(e);
		}
		e.incOutgoingWeight();
		return n;
	}
}
