/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.common.util.EMap;
import org.mosim.refactorlizar.architecture.evaluation.graphs.Node;

import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.analysis.generic.graph.IGraphElementSelector;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.deployment.DeploymentPackage;

import teetime.stage.basic.AbstractTransformation;

/**
 * Compute a graph where every node is connected with every edge.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class AllenDeployedMaximalInterconnectedGraphStage
		extends AbstractTransformation<ModelRepository, Graph<Node<DeployedComponent>>> {

	private final IGraphElementSelector selector;

	public AllenDeployedMaximalInterconnectedGraphStage(final IGraphElementSelector selector) {
		this.selector = selector;
	}

	@Override
	protected void execute(final ModelRepository repository) throws Exception {
		final DeploymentModel deploymentModel = repository.getModel(DeploymentPackage.Literals.DEPLOYMENT_MODEL);
		final MutableGraph<Node<DeployedComponent>> graph = GraphBuilder.undirected().allowsSelfLoops(true).build();

		this.createNodes(graph, deploymentModel.getContexts());
		this.interconnectAllNodes(graph);

		this.outputPort.send(graph);
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

	private void interconnectAllNodes(final MutableGraph<Node<DeployedComponent>> graph) {
		final Set<Node<DeployedComponent>> processedNodes = new HashSet<>();
		graph.nodes().forEach(source -> {
			processedNodes.add(source);
			graph.nodes().stream().filter(target -> !processedNodes.contains(target)).forEach(target -> {
				graph.putEdge(source, target);
			});
		});
	}
}
