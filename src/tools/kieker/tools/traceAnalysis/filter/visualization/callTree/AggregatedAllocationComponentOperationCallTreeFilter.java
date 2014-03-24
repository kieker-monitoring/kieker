/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.filter.visualization.callTree;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.visualization.graph.IOriginRetentionPolicy;
import kieker.tools.traceAnalysis.filter.visualization.graph.NoOriginRetentionPolicy;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.SynchronousCallMessage;
import kieker.tools.traceAnalysis.systemModel.repository.AbstractSystemSubRepository;
import kieker.tools.traceAnalysis.systemModel.repository.AllocationComponentOperationPairFactory;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.tools.traceAnalysis.systemModel.util.AllocationComponentOperationPair;

/**
 * @author Andre van Hoorn
 * 
 * @since 1.1
 */
@Plugin(description = "Uses the incoming data to enrich the connected repository with data for the aggregated allocation component operation call tree",
		repositoryPorts = {
			@RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class)
		})
public class AggregatedAllocationComponentOperationCallTreeFilter extends AbstractAggregatedCallTreeFilter<AllocationComponentOperationPair> {

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public AggregatedAllocationComponentOperationCallTreeFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean init() {
		final boolean success = super.init();
		if (success) {
			super.setRoot(new AggregatedAllocationComponentOperationCallTreeNode(AbstractSystemSubRepository.ROOT_ELEMENT_ID,
					AllocationComponentOperationPairFactory.ROOT_PAIR, true, null, NoOriginRetentionPolicy.createInstance()));
		}
		return success;
	}

	@Override
	protected AllocationComponentOperationPair concreteCreatePair(final SynchronousCallMessage callMsg) {
		final AllocationComponent allocationComponent = callMsg.getReceivingExecution().getAllocationComponent();
		final Operation op = callMsg.getReceivingExecution().getOperation();
		final AllocationComponentOperationPair destination = AggregatedAllocationComponentOperationCallTreeFilter.this.getSystemEntityFactory()
				.getAllocationPairFactory().getPairInstanceByPair(allocationComponent, op); // will never be null!
		return destination;
	}
}

/**
 * @author Andre van Hoorn
 * 
 * @since 1.1
 */
class AggregatedAllocationComponentOperationCallTreeNode extends AbstractAggregatedCallTreeNode<AllocationComponentOperationPair> {

	public AggregatedAllocationComponentOperationCallTreeNode(final int id, final AllocationComponentOperationPair entity, final boolean rootNode,
			final MessageTrace origin, final IOriginRetentionPolicy originPolicy) {
		super(id, entity, rootNode, origin, originPolicy);
	}

	@Override
	public AbstractCallTreeNode<AllocationComponentOperationPair> newCall(final AllocationComponentOperationPair dstObj, final MessageTrace origin,
			final IOriginRetentionPolicy originPolicy) {
		final AllocationComponentOperationPair destination = dstObj;
		WeightedDirectedCallTreeEdge<AllocationComponentOperationPair> e = this.childMap.get(destination.getId());
		AbstractCallTreeNode<AllocationComponentOperationPair> n;
		if (e != null) {
			n = e.getTarget();
			originPolicy.handleOrigin(e, origin);
			originPolicy.handleOrigin(n, origin);
		} else {
			n = new AggregatedAllocationComponentOperationCallTreeNode(destination.getId(), destination, false, origin, originPolicy); // !
			// rootNode
			e = new WeightedDirectedCallTreeEdge<AllocationComponentOperationPair>(this, n, origin, originPolicy);
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
