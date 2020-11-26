/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.trace.analysis.filter.visualization.callTree;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.tools.trace.analysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.trace.analysis.filter.visualization.graph.IOriginRetentionPolicy;
import kieker.tools.trace.analysis.filter.visualization.graph.NoOriginRetentionPolicy;
import kieker.tools.trace.analysis.systemModel.AssemblyComponent;
import kieker.tools.trace.analysis.systemModel.Execution;
import kieker.tools.trace.analysis.systemModel.MessageTrace;
import kieker.tools.trace.analysis.systemModel.Operation;
import kieker.tools.trace.analysis.systemModel.SynchronousCallMessage;
import kieker.tools.trace.analysis.systemModel.repository.AbstractSystemSubRepository;
import kieker.tools.trace.analysis.systemModel.repository.AssemblyComponentOperationPairFactory;
import kieker.tools.trace.analysis.systemModel.repository.SystemModelRepository;
import kieker.tools.trace.analysis.systemModel.util.AssemblyComponentOperationPair;

/**
 * @author Andre van Hoorn
 *         ~/Projects/Kieker/kieker/
 * @since 1.1
 * @deprecated 1.15 ported to teetime
 */
@Deprecated
@Plugin(description = "Uses the incoming data to enrich the connected repository with data for the aggregated assembly component operation call tree",
		repositoryPorts = {
			@RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class) })
public class AggregatedAssemblyComponentOperationCallTreeFilter
		extends AbstractAggregatedCallTreeFilter<AssemblyComponentOperationPair> {
	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public AggregatedAssemblyComponentOperationCallTreeFilter(final Configuration configuration,
			final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean init() {
		final boolean success = super.init();
		if (success) {
			final AggregatedAssemblyComponentOperationCallTreeNode root = new AggregatedAssemblyComponentOperationCallTreeNode(
					AbstractSystemSubRepository.ROOT_ELEMENT_ID, AssemblyComponentOperationPairFactory.ROOT_PAIR, true,
					null, NoOriginRetentionPolicy.createInstance());
			super.setRoot(root);
		}
		return success;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AssemblyComponentOperationPair concreteCreatePair(final SynchronousCallMessage callMsg) {
		final Execution execution = callMsg.getReceivingExecution();
		final AssemblyComponent assemblyComponent = execution.getAllocationComponent().getAssemblyComponent();
		final Operation op = execution.getOperation();
		return this.getSystemEntityFactory().getAssemblyPairFactory().getPairInstanceByPair(assemblyComponent, op);
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
