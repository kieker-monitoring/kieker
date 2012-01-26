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
import java.util.Map;

import kieker.analysis.repository.AbstractRepository;
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
public class AggregatedAssemblyComponentOperationCallTreePlugin extends AggregatedCallTreePlugin<AssemblyComponentOperationPair> {

	// TODO Change constructor to plugin-default-constructor
	public AggregatedAssemblyComponentOperationCallTreePlugin(final Configuration configuration, final Map<String, AbstractRepository> repositories,
			final AssemblyComponentOperationPairFactory assemblyComponentOperationPairFactory,
			final File dotOutputFile, final boolean includeWeights, final boolean shortLabels) {
		// TODO Check type conversion
		super(configuration, repositories, new AggregatedAssemblyComponentOperationCallTreeNode(AbstractSystemSubRepository.ROOT_ELEMENT_ID,
				(SystemModelRepository) repositories.get(AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME),
				assemblyComponentOperationPairFactory, assemblyComponentOperationPairFactory.getRootPair(), true), // root node
				dotOutputFile, includeWeights, shortLabels);
	}
}

class AggregatedAssemblyComponentOperationCallTreeNode extends AbstractAggregatedCallTreeNode<AssemblyComponentOperationPair> {

	private final AssemblyComponentOperationPairFactory pairFactory;

	public AggregatedAssemblyComponentOperationCallTreeNode(final int id, final SystemModelRepository systemEntityFactory,
			final AssemblyComponentOperationPairFactory pairFactory, final AssemblyComponentOperationPair entity, final boolean rootNode) {
		super(id, systemEntityFactory, entity, rootNode);
		this.pairFactory = pairFactory;
	}

	@Override
	public AbstractCallTreeNode<AssemblyComponentOperationPair> newCall(final SynchronousCallMessage callMsg) {
		final AssemblyComponent assemblyComponent = callMsg.getReceivingExecution().getAllocationComponent().getAssemblyComponent();
		final Operation op = callMsg.getReceivingExecution().getOperation();
		final AssemblyComponentOperationPair destination = this.pairFactory.getPairInstanceByPair(assemblyComponent, op); // will never be null!
		WeightedDirectedCallTreeEdge<AssemblyComponentOperationPair> e = this.childMap.get(destination.getId());
		AbstractCallTreeNode<AssemblyComponentOperationPair> n;
		if (e != null) {
			n = e.getDestination();
		} else {
			n = new AggregatedAssemblyComponentOperationCallTreeNode(destination.getId(), super.getSystemEntityFactory(), this.pairFactory, destination, false); // !
																																									// rootNode
			e = new WeightedDirectedCallTreeEdge<AssemblyComponentOperationPair>(this, n);
			this.childMap.put(destination.getId(), e);
			super.appendChildEdge(e);
		}
		e.incOutgoingWeight();
		return n;
	}
}
