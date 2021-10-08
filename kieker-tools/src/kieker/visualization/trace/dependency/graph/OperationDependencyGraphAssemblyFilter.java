/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

import kieker.model.repository.AbstractRepository;
import kieker.model.repository.AssemblyComponentOperationPairFactory;
import kieker.model.repository.AssemblyRepository;
import kieker.model.repository.OperationRepository;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.AbstractMessage;
import kieker.model.system.model.AssemblyComponent;
import kieker.model.system.model.MessageTrace;
import kieker.model.system.model.Operation;
import kieker.model.system.model.SynchronousReplyMessage;
import kieker.model.system.model.util.AssemblyComponentOperationPair;

/**
 * Refactored copy from LogAnalysis-legacy tool<br>
 *
 * This class has exactly one input port named "in". The data which is send to this plugin is not delegated in any way.
 *
 * @author Andre van Hoorn, Lena Stoever, Matthias Rohr,
 *
 * @since 1.2
 */
public class OperationDependencyGraphAssemblyFilter extends AbstractDependencyGraphFilter<AssemblyComponentOperationPair> {

	/**
	 * Creates a new filter using the given parameters.
	 *
	 * @param repository
	 *            system model respository
	 * @param timeUnit
	 *            time unit
	 */
	public OperationDependencyGraphAssemblyFilter(final SystemModelRepository repository, final TimeUnit timeUnit) {
		super(repository, timeUnit, new OperationAssemblyDependencyGraph(new AssemblyComponentOperationPair(AbstractRepository.ROOT_ELEMENT_ID,
				OperationRepository.ROOT_OPERATION,
				AssemblyRepository.ROOT_ASSEMBLY_COMPONENT)));
	}

	@Override
	protected void execute(final MessageTrace t) throws Exception {
		for (final AbstractMessage m : t.getSequenceAsVector()) {
			if (m instanceof SynchronousReplyMessage) {
				continue;
			}
			final AssemblyComponent senderComponent = m.getSendingExecution().getAllocationComponent().getAssemblyComponent();
			final AssemblyComponent receiverComponent = m.getReceivingExecution().getAllocationComponent().getAssemblyComponent();
			final int rootOperationId = OperationRepository.ROOT_OPERATION.getId();
			final Operation senderOperation = m.getSendingExecution().getOperation();
			final Operation receiverOperation = m.getReceivingExecution().getOperation();
			// The following two get-calls to the factory return s.th. in either case
			final AssemblyComponentOperationPairFactory pairFactory = this.getSystemModelRepository().getAssemblyPairFactory();

			final AssemblyComponentOperationPair senderPair;
			if (senderOperation.getId() == rootOperationId) {
				senderPair = this.getGraph().getRootNode().getEntity();
			} else {
				senderPair = pairFactory.getPairInstanceByPair(senderComponent, senderOperation);
			}

			final AssemblyComponentOperationPair receiverPair;
			if (receiverOperation.getId() == rootOperationId) {
				receiverPair = this.getGraph().getRootNode().getEntity();
			} else {
				receiverPair = pairFactory.getPairInstanceByPair(receiverComponent, receiverOperation);
			}

			DependencyGraphNode<AssemblyComponentOperationPair> senderNode = this.getGraph().getNode(senderPair.getId());
			DependencyGraphNode<AssemblyComponentOperationPair> receiverNode = this.getGraph().getNode(receiverPair.getId());
			if (senderNode == null) {
				senderNode = new DependencyGraphNode<>(senderPair.getId(), senderPair, t.getTraceInformation(),
						this.getOriginRetentionPolicy());

				if (m.getSendingExecution().isAssumed()) {
					senderNode.setAssumed();
				}

				this.getGraph().addNode(senderNode.getId(), senderNode);
			} else {
				this.handleOrigin(senderNode, t.getTraceInformation());
			}

			if (receiverNode == null) {
				receiverNode = new DependencyGraphNode<>(receiverPair.getId(), receiverPair, t.getTraceInformation(),
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
