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
package kieker.visualization.trace.dependency.graph;

import java.util.concurrent.TimeUnit;

import kieker.model.repository.ExecutionEnvironmentRepository;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.AbstractMessage;
import kieker.model.system.model.AllocationComponent;
import kieker.model.system.model.ExecutionContainer;
import kieker.model.system.model.MessageTrace;
import kieker.model.system.model.SynchronousReplyMessage;

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
public class ContainerDependencyGraphFilter extends AbstractDependencyGraphFilter<ExecutionContainer> {

	/**
	 * Creates a new filter using the given parameters.
	 *
	 * @param repository
	 *            system model repository
	 * @param timeUnit
	 *            teim unit
	 */
	public ContainerDependencyGraphFilter(final SystemModelRepository repository, final TimeUnit timeUnit) {
		super(repository, timeUnit, new ContainerDependencyGraph(ExecutionEnvironmentRepository.ROOT_EXECUTION_CONTAINER));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void execute(final MessageTrace t) throws Exception {
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

	@Override
	protected void onTerminating() {
		this.getOutputPort().send(this.getGraph());
		super.onTerminating();
	}

}
