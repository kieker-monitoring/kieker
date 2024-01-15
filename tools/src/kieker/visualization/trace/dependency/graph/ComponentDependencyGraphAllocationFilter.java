/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

import kieker.model.repository.AllocationRepository;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.AbstractMessage;
import kieker.model.system.model.AllocationComponent;
import kieker.model.system.model.MessageTrace;
import kieker.model.system.model.SynchronousReplyMessage;

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
public class ComponentDependencyGraphAllocationFilter extends AbstractDependencyGraphFilter<AllocationComponent> {

	/**
	 * Creates a new filter using the given configuration.
	 *
	 * @param repository
	 *            model repository
	 * @param timeUnit
	 *            used time unit
	 */
	public ComponentDependencyGraphAllocationFilter(final SystemModelRepository repository, final TimeUnit timeUnit) {
		super(repository, timeUnit, new ComponentAllocationDependencyGraph(AllocationRepository.ROOT_ALLOCATION_COMPONENT));
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
			DependencyGraphNode<AllocationComponent> senderNode = this.getGraph().getNode(senderComponent.getId());
			DependencyGraphNode<AllocationComponent> receiverNode = this.getGraph().getNode(receiverComponent.getId());

			if (senderNode == null) {
				senderNode = new DependencyGraphNode<>(senderComponent.getId(), senderComponent, t.getTraceInformation(),
						this.getOriginRetentionPolicy());

				if (m.getSendingExecution().isAssumed()) {
					senderNode.setAssumed();
				}

				this.getGraph().addNode(senderNode.getId(), senderNode);
			} else {
				this.handleOrigin(senderNode, t.getTraceInformation());
			}

			if (receiverNode == null) {
				receiverNode = new DependencyGraphNode<>(receiverComponent.getId(), receiverComponent, t.getTraceInformation(),
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

	@Override
	protected void onTerminating() {
		this.getOutputPort().send(this.getGraph());
		super.onTerminating();
	}
}
