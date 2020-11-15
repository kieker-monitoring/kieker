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

package kieker.tools.trace.analysis.filter.visualization.dependencyGraph;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.tools.trace.analysis.filter.AbstractMessageTraceProcessingFilter;
import kieker.tools.trace.analysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.trace.analysis.filter.IGraphOutputtingFilter;
import kieker.tools.trace.analysis.filter.visualization.VisualizationConstants;
import kieker.tools.trace.analysis.filter.visualization.graph.AbstractGraph;
import kieker.tools.trace.analysis.systemModel.AbstractMessage;
import kieker.tools.trace.analysis.systemModel.AllocationComponent;
import kieker.tools.trace.analysis.systemModel.ExecutionContainer;
import kieker.tools.trace.analysis.systemModel.MessageTrace;
import kieker.tools.trace.analysis.systemModel.SynchronousReplyMessage;
import kieker.tools.trace.analysis.systemModel.repository.ExecutionEnvironmentRepository;
import kieker.tools.trace.analysis.systemModel.repository.SystemModelRepository;

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
		outputPorts = @OutputPort(name = IGraphOutputtingFilter.OUTPUT_PORT_NAME_GRAPH, eventTypes = AbstractGraph.class))
public class ContainerDependencyGraphFilter extends AbstractDependencyGraphFilter<ExecutionContainer> {

	private static final String CONFIGURATION_NAME = VisualizationConstants.PLOTCONTAINERDEPGRAPH_COMPONENT_NAME;

	/**
	 * Creates a new filter using the given parameters.
	 *
	 * @param configuration
	 *            The configuration to use.
	 * @param projectContext
	 *            The project context to use.
	 */
	public ContainerDependencyGraphFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext, new ContainerDependencyGraph(ExecutionEnvironmentRepository.ROOT_EXECUTION_CONTAINER));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@InputPort(
			name = AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES,
			description = "Receives the message traces to be processed",
			eventTypes = MessageTrace.class)
	public void inputMessageTraces(final MessageTrace t) {
		for (final AbstractMessage m : t.getSequenceAsVector()) {
			if (m instanceof SynchronousReplyMessage) {
				continue;
			}
			final AllocationComponent senderComponent = m.getSendingExecution().getAllocationComponent();
			final AllocationComponent receiverComponent = m.getReceivingExecution().getAllocationComponent();
			final ExecutionContainer senderContainer = senderComponent.getExecutionContainer();
			final ExecutionContainer receiverContainer = receiverComponent.getExecutionContainer();
			DependencyGraphNode<ExecutionContainer> senderNode = this.getGraph().getNode(senderContainer.getId());
			DependencyGraphNode<ExecutionContainer> receiverNode = this.getGraph().getNode(receiverContainer.getId());

			if (senderNode == null) {
				senderNode = new DependencyGraphNode<>(senderContainer.getId(), senderContainer, t.getTraceInformation(),
						this.getOriginRetentionPolicy());
				this.getGraph().addNode(senderContainer.getId(), senderNode);
			} else {
				this.handleOrigin(senderNode, t.getTraceInformation());
			}

			if (receiverNode == null) {
				receiverNode = new DependencyGraphNode<>(receiverContainer.getId(), receiverContainer, t.getTraceInformation(),
						this.getOriginRetentionPolicy());
				this.getGraph().addNode(receiverContainer.getId(), receiverNode);
			} else {
				this.handleOrigin(receiverNode, t.getTraceInformation());
			}

			senderNode.addOutgoingDependency(receiverNode, t.getTraceInformation(), this.getOriginRetentionPolicy());
			receiverNode.addIncomingDependency(senderNode, t.getTraceInformation(), this.getOriginRetentionPolicy());
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
