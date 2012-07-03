/***************************************************************************
 * Copyright 2012 by
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

package kieker.tools.traceAnalysis.filter.visualization.callTree;

import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.SynchronousCallMessage;
import kieker.tools.traceAnalysis.systemModel.repository.AbstractSystemSubRepository;
import kieker.tools.traceAnalysis.systemModel.repository.AssemblyComponentOperationPairFactory;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.tools.traceAnalysis.systemModel.util.AssemblyComponentOperationPair;

/**
 * 
 * @author Andre van Hoorn
 */
@Plugin(repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class))
public class AggregatedAssemblyComponentOperationCallTreeFilter extends AbstractAggregatedCallTreeFilter<AssemblyComponentOperationPair> {

	public AggregatedAssemblyComponentOperationCallTreeFilter(final Configuration configuration) {
		super(configuration);
	}

	public void setAssemblyComponentOperationPairFactory(final AssemblyComponentOperationPairFactory assemblyComponentOperationPairFactory) {
		super.setRoot(new AggregatedAssemblyComponentOperationCallTreeNode(AbstractSystemSubRepository.ROOT_ELEMENT_ID,
				AssemblyComponentOperationPairFactory.ROOT_PAIR, true, null));
	}

	@Override
	protected AssemblyComponentOperationPair concreteCreatePair(final SynchronousCallMessage callMsg) {
		final AssemblyComponent assemblyComponent = callMsg.getReceivingExecution().getAllocationComponent().getAssemblyComponent();
		final Operation op = callMsg.getReceivingExecution().getOperation();
		return this.getSystemEntityFactory().getAssemblyPairFactory().getPairInstanceByPair(assemblyComponent, op);
	}
}

/**
 * @author Andre van Hoorn
 */
class AggregatedAssemblyComponentOperationCallTreeNode extends AbstractAggregatedCallTreeNode<AssemblyComponentOperationPair> {

	public AggregatedAssemblyComponentOperationCallTreeNode(final int id, final AssemblyComponentOperationPair entity, final boolean rootNode,
			final MessageTrace origin) {
		super(id, entity, rootNode, origin);
	}

	@Override
	public AbstractCallTreeNode<AssemblyComponentOperationPair> newCall(final Object dstObj, final MessageTrace origin) {
		final AssemblyComponentOperationPair destination = (AssemblyComponentOperationPair) dstObj;
		WeightedDirectedCallTreeEdge<AssemblyComponentOperationPair> e = this.childMap.get(destination.getId());
		AbstractCallTreeNode<AssemblyComponentOperationPair> n;
		if (e != null) {
			n = e.getTarget();
			e.addOrigin(origin);
			n.addOrigin(origin);
		} else {
			n = new AggregatedAssemblyComponentOperationCallTreeNode(destination.getId(), destination, false, origin); // !rootNode
			e = new WeightedDirectedCallTreeEdge<AssemblyComponentOperationPair>(this, n, origin);
			this.childMap.put(destination.getId(), e);
			super.appendChildEdge(e);
		}
		e.getTargetWeight().increase();
		return n;
	}
}
