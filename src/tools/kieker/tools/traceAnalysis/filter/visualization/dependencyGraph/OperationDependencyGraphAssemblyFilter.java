/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.filter.visualization.dependencyGraph;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.Constants;
import kieker.tools.traceAnalysis.filter.AbstractGraphProducingFilter;
import kieker.tools.traceAnalysis.filter.AbstractMessageTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraph;
import kieker.tools.traceAnalysis.systemModel.AbstractMessage;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.SynchronousReplyMessage;
import kieker.tools.traceAnalysis.systemModel.repository.AbstractSystemSubRepository;
import kieker.tools.traceAnalysis.systemModel.repository.AssemblyComponentOperationPairFactory;
import kieker.tools.traceAnalysis.systemModel.repository.AssemblyRepository;
import kieker.tools.traceAnalysis.systemModel.repository.OperationRepository;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.tools.traceAnalysis.systemModel.util.AssemblyComponentOperationPair;

/**
 * Refactored copy from LogAnalysis-legacy tool<br>
 * 
 * This class has exactly one input port named "in". The data which is send to this plugin is not delegated in any way.
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr,
 */
@Plugin(repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class),
		outputPorts = @OutputPort(name = AbstractGraphProducingFilter.OUTPUT_PORT_NAME_GRAPH, eventTypes = { AbstractGraph.class }))
public class OperationDependencyGraphAssemblyFilter extends AbstractDependencyGraphFilter<AssemblyComponentOperationPair> {

	private static final String CONFIGURATION_NAME = Constants.PLOTASSEMBLYOPERATIONDEPGRAPH_COMPONENT_NAME;

	/**
	 * Creates a new instance of this class using the given configuration.
	 * 
	 * @param configuration
	 *            The configuration used to initialize this instance.
	 */
	public OperationDependencyGraphAssemblyFilter(final Configuration configuration) {
		/* Call the mandatory "default" constructor. */
		super(configuration, new OperationAssemblyDependencyGraph(new AssemblyComponentOperationPair(AbstractSystemSubRepository.ROOT_ELEMENT_ID,
				OperationRepository.ROOT_OPERATION,
				AssemblyRepository.ROOT_ASSEMBLY_COMPONENT)));
	}

	@Override
	@InputPort(
			name = AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES,
			description = "Receives the message traces to be processed",
			eventTypes = { MessageTrace.class })
	public void inputMessageTraces(final MessageTrace t) {
		for (final AbstractMessage m : t.getSequenceAsVector()) {
			if (m instanceof SynchronousReplyMessage) {
				continue;
			}
			final AssemblyComponent senderComponent = m.getSendingExecution().getAllocationComponent().getAssemblyComponent();
			final AssemblyComponent receiverComponent = m.getReceivingExecution().getAllocationComponent().getAssemblyComponent();
			final int rootOperationId = OperationRepository.ROOT_OPERATION.getId();
			final Operation senderOperation = m.getSendingExecution().getOperation();
			final Operation receiverOperation = m.getReceivingExecution().getOperation();
			/* The following two get-calls to the factory return s.th. in either case */
			final AssemblyComponentOperationPairFactory pairFactory = this.getSystemEntityFactory().getAssemblyPairFactory();
			final AssemblyComponentOperationPair senderPair = (senderOperation.getId() == rootOperationId) ? this.getGraph().getRootNode().getEntity()
					: pairFactory.getPairInstanceByPair(senderComponent, senderOperation);
			final AssemblyComponentOperationPair receiverPair = (receiverOperation.getId() == rootOperationId) ? this.getGraph().getRootNode().getEntity()
					: pairFactory.getPairInstanceByPair(receiverComponent, receiverOperation);

			DependencyGraphNode<AssemblyComponentOperationPair> senderNode = this.getGraph().getNode(senderPair.getId());
			DependencyGraphNode<AssemblyComponentOperationPair> receiverNode = this.getGraph().getNode(receiverPair.getId());
			if (senderNode == null) {
				senderNode = new DependencyGraphNode<AssemblyComponentOperationPair>(senderPair.getId(), senderPair, t);

				if (m.getSendingExecution().isAssumed()) {
					senderNode.setAssumed();
				}

				this.getGraph().addNode(senderNode.getId(), senderNode);
			} else {
				senderNode.addOrigin(t);
			}

			if (receiverNode == null) {
				receiverNode = new DependencyGraphNode<AssemblyComponentOperationPair>(receiverPair.getId(), receiverPair, t);

				if (m.getReceivingExecution().isAssumed()) {
					receiverNode.setAssumed();
				}

				this.getGraph().addNode(receiverNode.getId(), receiverNode);
			} else {
				receiverNode.addOrigin(t);
			}

			final boolean assumed = this.isDependencyAssumed(senderNode, receiverNode);

			senderNode.addOutgoingDependency(receiverNode, assumed, t);
			receiverNode.addIncomingDependency(senderNode, assumed, t);

			this.invokeDecorators(m, senderNode, receiverNode);
		}
		this.reportSuccess(t.getTraceId());
	}

	@Override
	public String getConfigurationName() {
		return CONFIGURATION_NAME;
	}

}
