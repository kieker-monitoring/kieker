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
package kieker.analysis.metrics.graph.entropy;

import java.util.Map.Entry;
import java.util.Optional;

import org.eclipse.emf.common.util.EMap;
import org.mosim.refactorlizar.architecture.evaluation.graphs.Node;

import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.analysis.exception.InternalErrorException;
import kieker.analysis.generic.graph.EGraphGenerationMode;
import kieker.analysis.generic.graph.IGraphElementSelector;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.deployment.DeploymentPackage;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.Invocation;
import kieker.model.analysismodel.execution.OperationDataflow;
import kieker.model.analysismodel.execution.StorageDataflow;
import kieker.model.analysismodel.execution.Tuple;

import teetime.stage.basic.AbstractTransformation;

/**
 * @author Reiner Jung
 * @since 2.0.0
 */
public class AllenDeployedArchitectureGraphStage
		extends AbstractTransformation<ModelRepository, Graph<Node<DeployedComponent>>> {

	private final IGraphElementSelector selector;
	private final EGraphGenerationMode graphGeneratioMode;

	public AllenDeployedArchitectureGraphStage(final IGraphElementSelector selector,
			final EGraphGenerationMode graphGeneratioMode) {
		this.selector = selector;
		this.graphGeneratioMode = graphGeneratioMode;
	}

	@Override
	protected void execute(final ModelRepository repository) throws Exception {
		final DeploymentModel deploymentModel = repository.getModel(DeploymentPackage.Literals.DEPLOYMENT_MODEL);
		final ExecutionModel executionModel = repository.getModel(ExecutionPackage.Literals.EXECUTION_MODEL);
		final MutableGraph<Node<DeployedComponent>> graph = GraphBuilder.undirected().allowsSelfLoops(true).build();

		try {
			this.selector.setRepository(repository);

			this.createNodes(graph, deploymentModel.getContexts());
			this.processInvocations(graph, executionModel.getInvocations());
			this.processOperationDataflows(graph, executionModel.getOperationDataflows());
			this.processStorageDataflows(graph, executionModel.getStorageDataflows());
			this.outputPort.send(graph);
		} catch (final InternalErrorException e) {
			this.logger.error(e.getLocalizedMessage());
		}
	}

	private void createNodes(final MutableGraph<Node<DeployedComponent>> graph,
			final EMap<String, DeploymentContext> contexts) {
		for (final Entry<String, DeploymentContext> context : contexts) {
			for (final Entry<String, DeployedComponent> component : context.getValue().getComponents()) {
				for (final Entry<String, DeployedOperation> operation : component.getValue().getOperations()) {
					if (this.selector.nodeIsSelected(operation.getValue())) {
						final Node<DeployedComponent> node = new KiekerNode<>(operation.getValue());
						graph.addNode(node);
					}
				}
				for (final Entry<String, DeployedStorage> storage : component.getValue().getStorages()) {
					if (this.selector.nodeIsSelected(storage.getValue())) {
						final Node<DeployedComponent> node = new KiekerNode<>(storage.getValue());
						graph.addNode(node);
					}
				}
			}
		}
	}

	private void processInvocations(final MutableGraph<Node<DeployedComponent>> graph,
			final EMap<Tuple<DeployedOperation, DeployedOperation>, Invocation> invocations) throws InternalError {
		for (final Entry<Tuple<DeployedOperation, DeployedOperation>, Invocation> entry : invocations) {
			if (this.selector.edgeIsSelected(entry.getValue())) {
				final Node<DeployedComponent> source = this.findOperationNode(graph, entry.getValue().getCaller());
				final Node<DeployedComponent> target = this.findOperationNode(graph, entry.getValue().getCallee());

				switch (this.graphGeneratioMode) { // NOPMD
				case ADD_NODES_FOR_EDGES:
					graph.putEdge(this.getOrCreateNode(graph, source, entry.getValue().getCaller()),
							this.getOrCreateNode(graph, target, entry.getValue().getCallee()));
					break;
				case ONLY_EDGES_FOR_NODES:
					if ((source != null) && (target != null)) {
						graph.putEdge(source, target);
					}
					break;
				default:
					throw new InternalError("Illegal graph generation mode " + this.graphGeneratioMode.name());
				}
			}
		}
	}

	private void processOperationDataflows(final MutableGraph<Node<DeployedComponent>> graph,
			final EMap<Tuple<DeployedOperation, DeployedOperation>, OperationDataflow> operationDataflows)
			throws InternalError {
		for (final Entry<Tuple<DeployedOperation, DeployedOperation>, OperationDataflow> entry : operationDataflows) {
			if (this.selector.edgeIsSelected(entry.getValue())) {
				final Node<DeployedComponent> source = this.findOperationNode(graph, entry.getValue().getCaller());
				final Node<DeployedComponent> target = this.findOperationNode(graph, entry.getValue().getCallee());

				switch (this.graphGeneratioMode) { // NOPMD
				case ADD_NODES_FOR_EDGES:
					graph.putEdge(this.getOrCreateNode(graph, source, entry.getValue().getCaller()),
							this.getOrCreateNode(graph, target, entry.getValue().getCallee()));
					break;
				case ONLY_EDGES_FOR_NODES:
					if ((source != null) && (target != null)) {
						graph.putEdge(source, target);
					}
					break;
				default:
					throw new InternalError("Illegal graph generation mode " + this.graphGeneratioMode.name());
				}
			}
		}
	}

	private void processStorageDataflows(final MutableGraph<Node<DeployedComponent>> graph,
			final EMap<Tuple<DeployedOperation, DeployedStorage>, StorageDataflow> storageDataflows)
			throws InternalError {
		for (final Entry<Tuple<DeployedOperation, DeployedStorage>, StorageDataflow> entry : storageDataflows) {
			if (this.selector.edgeIsSelected(entry.getValue())) {
				final Node<DeployedComponent> source = this.findOperationNode(graph, entry.getValue().getCode());
				final Node<DeployedComponent> target = this.findStorageNode(graph, entry.getValue().getStorage());

				switch (this.graphGeneratioMode) { // NOPMD
				case ADD_NODES_FOR_EDGES:
					graph.putEdge(this.getOrCreateNode(graph, source, entry.getValue().getCode()),
							this.getOrCreateStorageNode(graph, target, entry.getValue().getStorage()));
					break;
				case ONLY_EDGES_FOR_NODES:
					if ((source != null) && (target != null)) {
						graph.putEdge(source, target);
					}
					break;
				default:
					throw new InternalError("Illegal graph generation mode " + this.graphGeneratioMode.name());
				}
			}
		}
	}

	private Node<DeployedComponent> getOrCreateStorageNode(final MutableGraph<Node<DeployedComponent>> graph,
			final Node<DeployedComponent> node, final DeployedStorage storage) {

		if (node == null) {
			final KiekerNode<DeployedComponent, DeployedStorage> newNode = new KiekerNode<>(storage);
			graph.addNode(newNode);
			return newNode;
		} else {
			return node;
		}
	}

	private Node<DeployedComponent> getOrCreateNode(final MutableGraph<Node<DeployedComponent>> graph,
			final Node<DeployedComponent> node, final DeployedOperation operation) {
		if (node == null) {
			final KiekerNode<DeployedComponent, DeployedOperation> newNode = new KiekerNode<>(operation);
			graph.addNode(newNode);
			return newNode;
		} else {
			return node;
		}
	}

	private Node<DeployedComponent> findOperationNode(final Graph<Node<DeployedComponent>> graph,
			final DeployedOperation operation) {
		final Optional<Node<DeployedComponent>> operationNode = graph.nodes().stream()
				.filter(node -> ((KiekerNode<DeployedComponent, ?>) node).getMember() instanceof DeployedOperation)
				.filter(node -> !node.getModule().getOperations().isEmpty())
				.filter(node -> ((KiekerNode<DeployedComponent, DeployedOperation>) node).getMember().equals(operation))
				.findFirst();

		if (operationNode.isPresent()) {
			return operationNode.get();
		} else {
			return null;
		}
	}

	private Node<DeployedComponent> findStorageNode(final Graph<Node<DeployedComponent>> graph,
			final DeployedStorage storage) {
		for (final Node<DeployedComponent> node : graph.nodes()) {
			if (node.getModule().getOperations().isEmpty()) {

				final KiekerNode<DeployedComponent, DeployedStorage> kiekerNode = (KiekerNode<DeployedComponent, DeployedStorage>) node;
				if (kiekerNode.getMember().equals(storage)) {
					return kiekerNode;
				}
			}
		}

		return null;
	}

}
