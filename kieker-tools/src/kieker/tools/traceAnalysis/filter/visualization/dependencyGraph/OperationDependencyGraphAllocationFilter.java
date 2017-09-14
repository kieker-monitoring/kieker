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

package kieker.tools.traceAnalysis.filter.visualization.dependencyGraph;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.Constants;
import kieker.tools.traceAnalysis.filter.AbstractMessageTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.IGraphOutputtingFilter;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraph;
import kieker.tools.traceAnalysis.systemModel.AbstractMessage;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.SynchronousReplyMessage;
import kieker.tools.traceAnalysis.systemModel.repository.AbstractSystemSubRepository;
import kieker.tools.traceAnalysis.systemModel.repository.AllocationComponentOperationPairFactory;
import kieker.tools.traceAnalysis.systemModel.repository.AllocationRepository;
import kieker.tools.traceAnalysis.systemModel.repository.OperationRepository;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.tools.traceAnalysis.systemModel.util.AllocationComponentOperationPair;

/**
 * Refactored copy from LogAnalysis-legacy tool<br>
 * 
 * This class has exactly one input port named "in". The data which is send to
 * this plugin is not delegated in any way.
 * 
 * @author Andre van Hoorn, Lena Stoever, Matthias Rohr,
 * 
 * @since 1.1
 */
@Plugin(repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class),
		outputPorts = @OutputPort(name = IGraphOutputtingFilter.OUTPUT_PORT_NAME_GRAPH, eventTypes = { AbstractGraph.class }))
public class OperationDependencyGraphAllocationFilter extends AbstractDependencyGraphFilter<AllocationComponentOperationPair> {

	private static final String CONFIGURATION_NAME = Constants.PLOTALLOCATIONOPERATIONDEPGRAPH_COMPONENT_NAME;

	/**
	 * Creates a new filter using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration to use.
	 * @param projectContext
	 *            The project context to use.
	 */
	public OperationDependencyGraphAllocationFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext, new OperationAllocationDependencyGraph(new AllocationComponentOperationPair(
				AbstractSystemSubRepository.ROOT_ELEMENT_ID, OperationRepository.ROOT_OPERATION, AllocationRepository.ROOT_ALLOCATION_COMPONENT)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@InputPort(name = AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES, description = "Receives the message traces to be processed",
			eventTypes = { MessageTrace.class })
	public void inputMessageTraces(final MessageTrace t) {
		for (final AbstractMessage m : t.getSequenceAsVector()) {
			if (m instanceof SynchronousReplyMessage) {
				continue;
			}
			final AllocationComponent senderComponent = m.getSendingExecution().getAllocationComponent();
			final AllocationComponent receiverComponent = m.getReceivingExecution().getAllocationComponent();
			final int rootOperationId = OperationRepository.ROOT_OPERATION.getId();
			final Operation senderOperation = m.getSendingExecution().getOperation();
			final Operation receiverOperation = m.getReceivingExecution().getOperation();
			// The following two get-calls to the factory return s.th. in either case
			final AllocationComponentOperationPairFactory pairFactory = this.getSystemEntityFactory().getAllocationPairFactory();

			AllocationComponentOperationPair senderPair;
			if (senderOperation.getId() == rootOperationId) {
				senderPair = this.getGraph().getRootNode().getEntity();
			} else {
				senderPair = pairFactory.getPairInstanceByPair(senderComponent, senderOperation);
			}

			AllocationComponentOperationPair receiverPair;
			if (receiverOperation.getId() == rootOperationId) {
				receiverPair = this.getGraph().getRootNode().getEntity();
			} else {
				receiverPair = pairFactory.getPairInstanceByPair(receiverComponent, receiverOperation);
			}

			DependencyGraphNode<AllocationComponentOperationPair> senderNode = this.getGraph().getNode(senderPair
					.getId());
			DependencyGraphNode<AllocationComponentOperationPair> receiverNode = this.getGraph().getNode(receiverPair.getId());
			if (senderNode == null) {
				senderNode = new DependencyGraphNode<AllocationComponentOperationPair>(senderPair.getId(), senderPair, t.getTraceInformation(),
						this.getOriginRetentionPolicy());

				if (m.getSendingExecution().isAssumed()) {
					senderNode.setAssumed();
				}

				this.getGraph().addNode(senderNode.getId(), senderNode);
			} else {
				this.handleOrigin(senderNode, t.getTraceInformation());
			}

			if (receiverNode == null) {
				receiverNode = new DependencyGraphNode<AllocationComponentOperationPair>(receiverPair.getId(), receiverPair, t.getTraceInformation(),
						this.getOriginRetentionPolicy());

				if (m.getReceivingExecution().isAssumed()) {
					receiverNode.setAssumed();
				}

				this.getGraph().addNode(receiverNode.getId(), receiverNode);
			} else {
				this.handleOrigin(receiverNode, t.getTraceInformation());
			}

			final boolean assumed = this.isDependencyAssumed(senderNode, receiverNode);

			senderNode.addOutgoingDependency(receiverNode, assumed, t.getTraceInformation(), this.getOriginRetentionPolicy());
			receiverNode.addIncomingDependency(senderNode, assumed, t.getTraceInformation(), this.getOriginRetentionPolicy());

			this.invokeDecorators(m, senderNode, receiverNode);
		}
		this.reportSuccess(t.getTraceId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getConfigurationName() {
		return CONFIGURATION_NAME;
	}

}
