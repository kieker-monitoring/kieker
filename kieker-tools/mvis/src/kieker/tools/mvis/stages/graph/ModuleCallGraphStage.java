/***************************************************************************
 * Copyright (C) 2021 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.mvis.stages.graph;

import java.util.Collection;
import java.util.Optional;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.analysis.generic.graph.GraphFactory;
import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.IGraph;
import kieker.analysis.generic.graph.INode;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.execution.EDirection;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.Invocation;
import kieker.model.analysismodel.execution.OperationDataflow;
import kieker.model.analysismodel.execution.StorageDataflow;
import kieker.tools.mvis.FullyQualifiedNamesFactory;

import teetime.stage.basic.AbstractTransformation;

import org.oceandsl.analysis.graph.EGraphGenerationMode;
import org.oceandsl.analysis.graph.IGraphElementSelector;

/**
 * Compute a graph based on the module structure of the architecture limited to nodes and modules
 * which belong to a specific measurement source.
 *
 * @author Reiner Jung
 * @since 1.1
 */
public class ModuleCallGraphStage extends AbstractTransformation<ModelRepository, IGraph<INode, IEdge>> {

    private final IGraphElementSelector selector;
    private final EGraphGenerationMode graphGeneratioMode;

    /**
     * @param selector
     *            graph element selector
     * @param graphGeneratioMode
     *            mode of adding additional edges/nodes
     */
    public ModuleCallGraphStage(final IGraphElementSelector selector, final EGraphGenerationMode graphGeneratioMode) {
        this.selector = selector;
        this.graphGeneratioMode = graphGeneratioMode;
    }

    @Override
    protected void execute(final ModelRepository repository) throws Exception {
        final ExecutionModel executionModel = repository.getModel(ExecutionPackage.Literals.EXECUTION_MODEL);

        final IGraph<INode, IEdge> graph = GraphFactory.createGraph(repository.getName());

        this.processInvocations(graph, executionModel.getInvocations().values());
        this.processOperationDataflow(graph, executionModel.getOperationDataflows().values());
        this.processStorageDataflow(graph, executionModel.getStorageDataflows().values());

        this.outputPort.send(graph);
    }

    private void processInvocations(final IGraph<INode, IEdge> graph, final Collection<Invocation> invocations) {
        for (final Invocation invocation : invocations) {
            final boolean sourceSelected = this.selector.nodeIsSelected(invocation.getCaller().getComponent());
            final boolean targetSelected = this.selector.nodeIsSelected(invocation.getCallee().getComponent());
            if (sourceSelected) {
                this.addVertexIfAbsent(graph, invocation.getCaller().getComponent());
            }
            if (targetSelected) {
                this.addVertexIfAbsent(graph, invocation.getCallee().getComponent());
            }
            switch (this.graphGeneratioMode) {
            case ONLY_EDGES_FOR_NODES:
                this.processInvocationsOnlyEdgesForNodes(graph, sourceSelected, targetSelected, invocation);
                break;
            case ADD_NODES_FOR_EDGES:
                this.processInvocationsAddNodesForEdges(graph, sourceSelected, targetSelected, invocation);
                break;
            default:
                throw new InternalError("Illegal graph generation mode " + this.graphGeneratioMode.name());
            }
        }
    }

    private void processInvocationsOnlyEdgesForNodes(final IGraph<INode, IEdge> graph, final boolean sourceSelected,
            final boolean targetSelected, final Invocation invocation) {
        if (sourceSelected && targetSelected && this.selector.edgeIsSelected(invocation)) {
            this.addEdge(graph, invocation.getCaller().getComponent(), invocation.getCallee().getComponent(),
                    EDirection.READ);
        }
    }

    private void processInvocationsAddNodesForEdges(final IGraph<INode, IEdge> graph, final boolean sourceSelected,
            final boolean targetSelected, final Invocation invocation) {
        if (this.selector.edgeIsSelected(invocation)) {
            if (!sourceSelected) {
                this.addVertexIfAbsent(graph, invocation.getCaller().getComponent());
            }
            if (!targetSelected) {
                this.addVertexIfAbsent(graph, invocation.getCallee().getComponent());
            }
            this.addEdge(graph, invocation.getCaller().getComponent(), invocation.getCallee().getComponent(),
                    EDirection.READ);
        }
    }

    private void processOperationDataflow(final IGraph<INode, IEdge> graph,
            final Collection<OperationDataflow> dataflows) {
        for (final OperationDataflow operationDataflow : dataflows) {
            final boolean sourceSelected = this.selector.nodeIsSelected(operationDataflow.getCaller().getComponent());
            final boolean targetSelected = this.selector.nodeIsSelected(operationDataflow.getCallee().getComponent());
            if (sourceSelected) {
                this.addVertexIfAbsent(graph, operationDataflow.getCaller().getComponent());
            }
            if (targetSelected) {
                this.addVertexIfAbsent(graph, operationDataflow.getCallee().getComponent());
            }
            switch (this.graphGeneratioMode) {
            case ONLY_EDGES_FOR_NODES:
                this.processOperationDataflowOnlyEdgesForNodes(graph, sourceSelected, targetSelected,
                        operationDataflow);
                break;
            case ADD_NODES_FOR_EDGES:
                this.processOperationDataflowAddNodesForEdges(graph, sourceSelected, targetSelected, operationDataflow);
                break;
            default:
                throw new InternalError("Illegal graph generation mode " + this.graphGeneratioMode.name());
            }
        }
    }

    private void processOperationDataflowOnlyEdgesForNodes(final IGraph<INode, IEdge> graph,
            final boolean sourceSelected, final boolean targetSelected, final OperationDataflow operationDataflow) {
        if (sourceSelected && targetSelected && this.selector.edgeIsSelected(operationDataflow)) {
            this.addEdge(graph, operationDataflow.getCaller().getComponent(),
                    operationDataflow.getCallee().getComponent(), operationDataflow.getDirection());
        }
    }

    private void processOperationDataflowAddNodesForEdges(final IGraph<INode, IEdge> graph,
            final boolean sourceSelected, final boolean targetSelected, final OperationDataflow operationDataflow) {
        if (this.selector.edgeIsSelected(operationDataflow)) {
            if (!sourceSelected) {
                this.addVertexIfAbsent(graph, operationDataflow.getCaller().getComponent());
            }
            if (!targetSelected) {
                this.addVertexIfAbsent(graph, operationDataflow.getCallee().getComponent());
            }
            this.addEdge(graph, operationDataflow.getCaller().getComponent(),
                    operationDataflow.getCallee().getComponent(), operationDataflow.getDirection());
        }
    }

    private void processStorageDataflow(final IGraph<INode, IEdge> graph, final Collection<StorageDataflow> dataflows) {
        for (final StorageDataflow storageDataflow : dataflows) {
            final boolean sourceSelected = this.selector.nodeIsSelected(storageDataflow.getCode().getComponent());
            final boolean targetSelected = this.selector.nodeIsSelected(storageDataflow.getStorage().getComponent());
            if (sourceSelected) {
                this.addVertexIfAbsent(graph, storageDataflow.getCode().getComponent());
            }
            if (targetSelected) {
                this.addVertexIfAbsent(graph, storageDataflow.getStorage().getComponent());
            }
            switch (this.graphGeneratioMode) {
            case ONLY_EDGES_FOR_NODES:
                this.processStorageDataflowOnlyEdgesForNodes(graph, sourceSelected, targetSelected, storageDataflow);
                break;
            case ADD_NODES_FOR_EDGES:
                this.processStorageDataflowAddNodesForEdges(graph, sourceSelected, targetSelected, storageDataflow);
                break;
            default:
                throw new InternalError("Illegal graph generation mode " + this.graphGeneratioMode.name());
            }
        }
    }

    private void processStorageDataflowOnlyEdgesForNodes(final IGraph<INode, IEdge> graph, final boolean sourceSelected,
            final boolean targetSelected, final StorageDataflow storageDataflow) {
        if (sourceSelected && targetSelected && this.selector.edgeIsSelected(storageDataflow)) {
            this.addEdge(graph, storageDataflow.getCode().getComponent(), storageDataflow.getStorage().getComponent(),
                    storageDataflow.getDirection());
        }
    }

    private void processStorageDataflowAddNodesForEdges(final IGraph<INode, IEdge> graph, final boolean sourceSelected,
            final boolean targetSelected, final StorageDataflow storageDataflow) {
        if (this.selector.edgeIsSelected(storageDataflow)) {
            if (!sourceSelected) {
                this.addVertexIfAbsent(graph, storageDataflow.getCode().getComponent());
            }
            if (!targetSelected) {
                this.addVertexIfAbsent(graph, storageDataflow.getStorage().getComponent());
            }
            this.addEdge(graph, storageDataflow.getCode().getComponent(), storageDataflow.getStorage().getComponent(),
                    storageDataflow.getDirection());
        }
    }

    private void addEdge(final IGraph<INode, IEdge> graph, final DeployedComponent source,
            final DeployedComponent target, final EDirection direction) {
        final Optional<INode> sourceNode = this.findNode(graph, source);
        final Optional<INode> targetNode = this.findNode(graph, target);
        switch (direction) {
        case WRITE:
            graph.getGraph().addEdge(sourceNode.get(), targetNode.get(), GraphFactory.createEdge(null));
            break;
        case READ:
            graph.getGraph().addEdge(targetNode.get(), sourceNode.get(), GraphFactory.createEdge(null));
            break;
        case BOTH:
            graph.getGraph().addEdge(targetNode.get(), sourceNode.get(), GraphFactory.createEdge(null));
            graph.getGraph().addEdge(sourceNode.get(), targetNode.get(), GraphFactory.createEdge(null));
            break;
        default:
            break;
        }
    }

    private Optional<INode> findNode(final IGraph<INode, IEdge> graph, final DeployedComponent component) {
        final String fullyQualifiedName = FullyQualifiedNamesFactory.createFullyQualifiedName(component);
        return graph.findNode(fullyQualifiedName);
    }

    private void addVertexIfAbsent(final IGraph<INode, IEdge> graph, final DeployedComponent component) {
        final Optional<INode> node = this.findNode(graph, component);
        if (!node.isPresent()) {
            final INode newNode = GraphFactory
                    .createNode(FullyQualifiedNamesFactory.createFullyQualifiedName(component));
            graph.getGraph().addNode(newNode);
        }
    }

}
