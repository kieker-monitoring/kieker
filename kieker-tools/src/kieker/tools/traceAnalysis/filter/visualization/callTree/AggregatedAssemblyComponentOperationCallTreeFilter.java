/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.SynchronousCallMessage;
import kieker.tools.traceAnalysis.systemModel.repository.AbstractSystemSubRepository;
import kieker.tools.traceAnalysis.systemModel.repository.AssemblyComponentOperationPairFactory;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.tools.traceAnalysis.systemModel.util.AssemblyComponentOperationPair;

/**
 * @author Andre van Hoorn
 * 
 * @since 1.1
 */
@Plugin(description = "Uses the incoming data to enrich the connected repository with data for the aggregated assembly component operation call tree",
		repositoryPorts = {
			@RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class)
		})
public class AggregatedAssemblyComponentOperationCallTreeFilter extends AbstractAggregatedCallTreeFilter<AssemblyComponentOperationPair> {

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public AggregatedAssemblyComponentOperationCallTreeFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean init() {
		final boolean success = super.init();
		if (success) {
			super.setRoot(new AggregatedAssemblyComponentOperationCallTreeNode(AbstractSystemSubRepository.ROOT_ELEMENT_ID,
					AssemblyComponentOperationPairFactory.ROOT_PAIR, true, null, NoOriginRetentionPolicy.createInstance()));
		}
		return success;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AssemblyComponentOperationPair concreteCreatePair(final SynchronousCallMessage callMsg) {
		final AssemblyComponent assemblyComponent = callMsg.getReceivingExecution().getAllocationComponent().getAssemblyComponent();
		final Operation op = callMsg.getReceivingExecution().getOperation();
		return this.getSystemEntityFactory().getAssemblyPairFactory().getPairInstanceByPair(assemblyComponent, op);
	}
}

/**
 * @author Andre van Hoorn
 * 
 * @since 1.1
 */
class AggregatedAssemblyComponentOperationCallTreeNode extends AbstractAggregatedCallTreeNode<AssemblyComponentOperationPair> {

	public AggregatedAssemblyComponentOperationCallTreeNode(final int id, final AssemblyComponentOperationPair entity, final boolean rootNode,
			final MessageTrace origin, final IOriginRetentionPolicy originPolicy) {
		super(id, entity, rootNode, origin, originPolicy);
	}

	@Override
	public AbstractCallTreeNode<AssemblyComponentOperationPair> newCall(final AssemblyComponentOperationPair dstObj, final MessageTrace origin,
			final IOriginRetentionPolicy originPolicy) {
		final AssemblyComponentOperationPair destination = dstObj;
		WeightedDirectedCallTreeEdge<AssemblyComponentOperationPair> e = this.childMap.get(destination.getId());
		AbstractCallTreeNode<AssemblyComponentOperationPair> n;
		if (e != null) {
			n = e.getTarget();
			originPolicy.handleOrigin(e, origin);
			originPolicy.handleOrigin(n, origin);
		} else {
			n = new AggregatedAssemblyComponentOperationCallTreeNode(destination.getId(), destination, false, origin, originPolicy); // !rootNode
			e = new WeightedDirectedCallTreeEdge<AssemblyComponentOperationPair>(this, n, origin, originPolicy);
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
