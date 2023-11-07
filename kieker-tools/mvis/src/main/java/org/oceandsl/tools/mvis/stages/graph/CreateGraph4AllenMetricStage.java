/***************************************************************************
 * Copyright (C) 2021 OceanDSL (https://oceandsl.uni-kiel.de)
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
package org.oceandsl.tools.mvis.stages.graph;

import java.util.ArrayList;
import java.util.List;

import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import org.mosim.refactorlizar.architecture.evaluation.graphs.Node;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.deployment.DeploymentPackage;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.Invocation;

import teetime.stage.basic.AbstractTransformation;

import org.oceandsl.analysis.metrics.entropy.KiekerNode;

/**
 * Derive a graph compatible for the Allen metric from the architecture model.
 *
 * @author Reiner Jung
 * @since 1.1
 */
public class CreateGraph4AllenMetricStage
        extends AbstractTransformation<ModelRepository, Graph<Node<DeployedComponent>>> {

    @Override
    protected void execute(final ModelRepository element) throws Exception {
        final Graph<Node<DeployedComponent>> graph = this.computeGraph(element);
        this.outputPort.send(graph);
    }

    private Graph<Node<DeployedComponent>> computeGraph(final ModelRepository repository) {
        final MutableGraph<Node<DeployedComponent>> graph = this.createGraph();

        final DeploymentModel deploymentModel = repository.getModel(DeploymentPackage.Literals.DEPLOYMENT_MODEL);
        final ExecutionModel executionModel = repository.getModel(ExecutionPackage.Literals.EXECUTION_MODEL);

        for (final DeployedOperation operation : this.collectAllOperations(deploymentModel)) {
            graph.addNode(new KiekerNode<>(operation));
            this.getReferencedMembers(operation, executionModel).forEach(
                    calledOperation -> graph.putEdge(new KiekerNode<>(calledOperation), new KiekerNode<>(operation)));
        }
        return graph;
    }

    private Iterable<DeployedOperation> getReferencedMembers(final DeployedOperation operation,
            final ExecutionModel executionModel) {
        final List<DeployedOperation> callees = new ArrayList<>();
        for (final Invocation invocation : executionModel.getInvocations().values()) {
            if (invocation.getCaller().equals(operation)) {
                callees.add(invocation.getCallee());
            }
        }
        return callees;
    }

    private List<DeployedOperation> collectAllOperations(final DeploymentModel deploymentModel) {
        final List<DeployedOperation> operations = new ArrayList<>();
        for (final DeploymentContext context : deploymentModel.getContexts().values()) {
            for (final DeployedComponent component : context.getComponents().values()) {
                for (final DeployedOperation operation : component.getOperations().values()) {
                    operations.add(operation);
                }
            }
        }
        return operations;
    }

    private MutableGraph<Node<DeployedComponent>> createGraph() {
        return GraphBuilder.undirected().allowsSelfLoops(true).build();
    }

}
