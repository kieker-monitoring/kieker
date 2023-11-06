/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.visualization.trace.call.tree;

import kieker.model.repository.AbstractRepository;
import kieker.model.repository.AssemblyComponentOperationPairFactory;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.AssemblyComponent;
import kieker.model.system.model.Execution;
import kieker.model.system.model.MessageTrace;
import kieker.model.system.model.Operation;
import kieker.model.system.model.SynchronousCallMessage;
import kieker.model.system.model.util.AssemblyComponentOperationPair;
import kieker.tools.trace.analysis.filter.visualization.graph.IOriginRetentionPolicy;
import kieker.tools.trace.analysis.filter.visualization.graph.NoOriginRetentionPolicy;

/**
 * @author Andre van Hoorn
 *         ~/Projects/Kieker/kieker/
 * @since 1.1
 */
public class AggregatedAssemblyComponentOperationCallTreeFilter
		extends AbstractAggregatedCallTreeFilter<AssemblyComponentOperationPair> {
	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param repository
	 * @param includeWeights
	 * @param shortLabels
	 * @param dotOutputFile
	 *
	 */
	public AggregatedAssemblyComponentOperationCallTreeFilter(final SystemModelRepository repository, final boolean includeWeights, final boolean shortLabels,
			final String dotOutputFile) {
		super(repository, includeWeights, shortLabels, dotOutputFile);

		final AggregatedAssemblyComponentOperationCallTreeNode root = new AggregatedAssemblyComponentOperationCallTreeNode(
				AbstractRepository.ROOT_ELEMENT_ID, AssemblyComponentOperationPairFactory.ROOT_PAIR, true,
				null, NoOriginRetentionPolicy.createInstance());
		this.setRoot(root);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AssemblyComponentOperationPair concreteCreatePair(final SynchronousCallMessage callMsg) {
		final Execution execution = callMsg.getReceivingExecution();
		final AssemblyComponent assemblyComponent = execution.getAllocationComponent().getAssemblyComponent();
		final Operation op = execution.getOperation();
		return this.getSystemModelRepository().getAssemblyPairFactory().getPairInstanceByPair(assemblyComponent, op);
	}
}

/**
 * Used to generate "aggregatedAssemblyCallTree.dot".
 *
 * @author Andre van Hoorn
 *
 * @since 1.1
 */
class AggregatedAssemblyComponentOperationCallTreeNode
		extends AbstractAggregatedCallTreeNode<AssemblyComponentOperationPair> {

	public AggregatedAssemblyComponentOperationCallTreeNode(final int id, final AssemblyComponentOperationPair entity,
			final boolean rootNode, final MessageTrace origin, final IOriginRetentionPolicy originPolicy) {
		super(id, entity, rootNode, origin, originPolicy);
	}

	@Override
	public AbstractCallTreeNode<AssemblyComponentOperationPair> newCall(final AssemblyComponentOperationPair dstObj,
			final MessageTrace trace, final IOriginRetentionPolicy originPolicy) {
		final AssemblyComponentOperationPair destination = dstObj;
		WeightedDirectedCallTreeEdge<AssemblyComponentOperationPair> edgeToTargetNode = this.childMap
				.get(destination.getId());

		final AbstractCallTreeNode<AssemblyComponentOperationPair> targetNode;
		if (edgeToTargetNode != null) {
			targetNode = edgeToTargetNode.getTarget();
			originPolicy.handleOrigin(edgeToTargetNode, trace);
			originPolicy.handleOrigin(targetNode, trace);
		} else {
			targetNode = new AggregatedAssemblyComponentOperationCallTreeNode(destination.getId(), destination, false,
					trace, originPolicy); // !rootNode
			edgeToTargetNode = new WeightedDirectedCallTreeEdge<>(this, targetNode, trace, originPolicy);
			this.childMap.put(destination.getId(), edgeToTargetNode);
			super.appendChildEdge(edgeToTargetNode);
		}

		edgeToTargetNode.getTargetWeight().incrementAndGet();

		return targetNode;
	}

	@Override
	public String getIdentifier() {
		return null;
	}
}
