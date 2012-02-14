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

import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.plugins.AbstractTraceAnalysisPlugin;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
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
@Plugin(repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, repositoryType = SystemModelRepository.class))
public class AggregatedAssemblyComponentOperationCallTreePlugin extends AggregatedCallTreePlugin<AssemblyComponentOperationPair> {

	// TODO Change constructor to plugin-default-constructor
	public AggregatedAssemblyComponentOperationCallTreePlugin(final Configuration configuration,
			final AssemblyComponentOperationPairFactory assemblyComponentOperationPairFactory,
			final File dotOutputFile, final boolean includeWeights, final boolean shortLabels) {
		// TODO Check type conversion
		super(configuration, new AggregatedAssemblyComponentOperationCallTreeNode(AbstractSystemSubRepository.ROOT_ELEMENT_ID,
				AssemblyComponentOperationPairFactory.ROOT_PAIR, true), // root node
				dotOutputFile, includeWeights, shortLabels);
	}

	@Override
	protected AssemblyComponentOperationPair concreteCreatePair(final SynchronousCallMessage callMsg) {
		final AssemblyComponent assemblyComponent = callMsg.getReceivingExecution().getAllocationComponent().getAssemblyComponent();
		final Operation op = callMsg.getReceivingExecution().getOperation();
		final AssemblyComponentOperationPair destination = this.getSystemEntityFactory().getAssemblyPairFactory().getPairInstanceByPair(assemblyComponent, op);
		return destination;
	}
}

class AggregatedAssemblyComponentOperationCallTreeNode extends AbstractAggregatedCallTreeNode<AssemblyComponentOperationPair> {

	public AggregatedAssemblyComponentOperationCallTreeNode(final int id, final AssemblyComponentOperationPair entity, final boolean rootNode) {
		super(id, entity, rootNode);
	}

	@Override
	public AbstractCallTreeNode<AssemblyComponentOperationPair> newCall(final Object dstObj) {
		final AssemblyComponentOperationPair destination = (AssemblyComponentOperationPair) dstObj;
		WeightedDirectedCallTreeEdge<AssemblyComponentOperationPair> e = this.childMap.get(destination.getId());
		AbstractCallTreeNode<AssemblyComponentOperationPair> n;
		if (e != null) {
			n = e.getDestination();
		} else {
			n = new AggregatedAssemblyComponentOperationCallTreeNode(destination.getId(), destination, false); // !rootNode
			e = new WeightedDirectedCallTreeEdge<AssemblyComponentOperationPair>(this, n);
			this.childMap.put(destination.getId(), e);
			super.appendChildEdge(e);
		}
		e.incOutgoingWeight();
		return n;
	}
}
