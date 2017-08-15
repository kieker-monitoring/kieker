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
import kieker.tools.traceAnalysis.systemModel.SynchronousReplyMessage;
import kieker.tools.traceAnalysis.systemModel.repository.AllocationRepository;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * Refactored copy from LogAnalysis-legacy tool<br>
 * 
 * This class has exactly one input port named "in". The produced graph is emitted through
 * the single output port.
 * 
 * @author Andre van Hoorn, Lena Stoever, Matthias Rohr, Jan Waller
 * 
 * 
 * @since 0.95a
 */
@Plugin(repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class),
		outputPorts = @OutputPort(name = IGraphOutputtingFilter.OUTPUT_PORT_NAME_GRAPH, eventTypes = { AbstractGraph.class }))
public class ComponentDependencyGraphAllocationFilter extends AbstractDependencyGraphFilter<AllocationComponent> {

	private static final String CONFIGURATION_NAME = Constants.PLOTALLOCATIONCOMPONENTDEPGRAPH_COMPONENT_NAME;

	/**
	 * Creates a new filter using the given configuration.
	 * 
	 * @param configuration
	 *            The configuration to use.
	 * @param projectContext
	 *            The project context to use.
	 */
	public ComponentDependencyGraphAllocationFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext, new ComponentAllocationDependencyGraph(AllocationRepository.ROOT_ALLOCATION_COMPONENT));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@InputPort(name = AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES, description = "Message traces", eventTypes = { MessageTrace.class })
	public void inputMessageTraces(final MessageTrace t) {
		for (final AbstractMessage m : t.getSequenceAsVector()) {
			if (m instanceof SynchronousReplyMessage) {
				continue;
			}
			final AllocationComponent senderComponent = m.getSendingExecution().getAllocationComponent();
			final AllocationComponent receiverComponent = m.getReceivingExecution().getAllocationComponent();
			DependencyGraphNode<AllocationComponent> senderNode = this.getGraph().getNode(senderComponent.getId());
			DependencyGraphNode<AllocationComponent> receiverNode = this.getGraph().getNode(receiverComponent.getId());

			if (senderNode == null) {
				senderNode = new DependencyGraphNode<AllocationComponent>(senderComponent.getId(), senderComponent, t.getTraceInformation(),
						this.getOriginRetentionPolicy());

				if (m.getSendingExecution().isAssumed()) {
					senderNode.setAssumed();
				}

				this.getGraph().addNode(senderNode.getId(), senderNode);
			} else {
				this.handleOrigin(senderNode, t.getTraceInformation());
			}

			if (receiverNode == null) {
				receiverNode = new DependencyGraphNode<AllocationComponent>(receiverComponent.getId(), receiverComponent, t.getTraceInformation(),
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
