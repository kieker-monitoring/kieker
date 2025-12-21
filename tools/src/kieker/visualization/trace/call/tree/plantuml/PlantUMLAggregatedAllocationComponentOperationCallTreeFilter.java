/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.visualization.trace.call.tree.plantuml;

import kieker.model.repository.AllocationComponentOperationPairFactory;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.AllocationComponent;
import kieker.model.system.model.MessageTrace;
import kieker.model.system.model.Operation;
import kieker.model.system.model.SynchronousCallMessage;
import kieker.model.system.model.util.AllocationComponentOperationPair;
import kieker.tools.trace.analysis.filter.visualization.graph.IOriginRetentionPolicy;
import kieker.tools.trace.analysis.filter.visualization.graph.NoOriginRetentionPolicy;
import kieker.tools.trace.analysis.systemModel.repository.AbstractSystemSubRepository;
import kieker.visualization.trace.call.tree.AbstractAggregatedCallTreeFilter;
import kieker.visualization.trace.call.tree.AbstractAggregatedCallTreeNode;
import kieker.visualization.trace.call.tree.AbstractCallTreeNode;
import kieker.visualization.trace.call.tree.GraphFormat;
import kieker.visualization.trace.call.tree.WeightedDirectedCallTreeEdge;

/**
 * PlantUML call tree filter aggregating by allocation component-operation pairs.
 * 
 * @author Yorrick Josuttis
 */
public class PlantUMLAggregatedAllocationComponentOperationCallTreeFilter extends AbstractAggregatedCallTreeFilter<AllocationComponentOperationPair> {

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param repository
	 *            system model repository
	 * @param includeWeights
	 *            include weights ingraph
	 * @param shortLabels
	 *            use short labels
	 * @param dotOutputFile
	 *            output file name
	 */
	public PlantUMLAggregatedAllocationComponentOperationCallTreeFilter(final SystemModelRepository repository, final boolean includeWeights,
			final boolean shortLabels, final String dotOutputFile) {
		super(repository, includeWeights, shortLabels, dotOutputFile, GraphFormat.PLANTUML);

		this.setRoot(new PlantUMLAggregatedAllocationComponentOperationCallTreeNode(AbstractSystemSubRepository.ROOT_ELEMENT_ID,
				AllocationComponentOperationPairFactory.ROOT_PAIR, true, null, NoOriginRetentionPolicy.createInstance()));
	}

	@Override
	protected AllocationComponentOperationPair concreteCreatePair(final SynchronousCallMessage callMsg) {
		final AllocationComponent allocationComponent = callMsg.getReceivingExecution().getAllocationComponent();
		final Operation op = callMsg.getReceivingExecution().getOperation();

		return PlantUMLAggregatedAllocationComponentOperationCallTreeFilter.this.getSystemModelRepository()
				.getAllocationPairFactory().getPairInstanceByPair(allocationComponent, op); // will never be null!
	}
}

/**
 * Used to generate "aggregatedAllocationCallTree.dot".
 * 
 * @author Yorrick Josuttis
 */
class PlantUMLAggregatedAllocationComponentOperationCallTreeNode extends AbstractAggregatedCallTreeNode<AllocationComponentOperationPair> {

	public PlantUMLAggregatedAllocationComponentOperationCallTreeNode(final int id, final AllocationComponentOperationPair entity, final boolean rootNode,
			final MessageTrace origin, final IOriginRetentionPolicy originPolicy) {
		super(id, entity, rootNode, origin, originPolicy);
	}

	@Override
	public AbstractCallTreeNode<AllocationComponentOperationPair> newCall(final AllocationComponentOperationPair dstObj, final MessageTrace origin,
			final IOriginRetentionPolicy originPolicy) {
		final AllocationComponentOperationPair destination = dstObj;
		WeightedDirectedCallTreeEdge<AllocationComponentOperationPair> e = this.childMap.get(destination.getId());
		final AbstractCallTreeNode<AllocationComponentOperationPair> n;
		if (e != null) {
			n = e.getTarget();
			originPolicy.handleOrigin(e, origin);
			originPolicy.handleOrigin(n, origin);
		} else {
			n = new PlantUMLAggregatedAllocationComponentOperationCallTreeNode(destination.getId(), destination, false, origin, originPolicy); // !
			// rootNode
			e = new WeightedDirectedCallTreeEdge<>(this, n, origin, originPolicy);
			this.childMap.put(destination.getId(), e);
			super.appendChildEdge(e);
		}
		e.getTargetWeight().incrementAndGet();
		return n;
	}

	@Override
	public String getIdentifier() {
		return null;
	}
}
