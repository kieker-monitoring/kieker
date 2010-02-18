package kieker.tpan.plugins.dependencyGraph;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2010 Kieker Project
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
 * ==================================================
 */
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map.Entry;
import kieker.tpan.datamodel.AllocationComponentInstance;
import kieker.tpan.datamodel.ExecutionContainer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import kieker.tpan.datamodel.Message;
import kieker.tpan.datamodel.MessageTrace;
import kieker.tpan.datamodel.Operation;
import kieker.tpan.datamodel.SynchronousReplyMessage;
import kieker.tpan.datamodel.factories.AbstractSystemSubFactory;
import kieker.tpan.datamodel.factories.SystemEntityFactory;
import kieker.tpan.plugins.util.dot.DotFactory;

/**
 * Refactored copy from LogAnalysis-legacy tool
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr,
 */
public class OperationDependencyGraphPlugin extends AbstractDependencyGraphPlugin<AllocationComponentOperationPair> {

    private static final Log log = LogFactory.getLog(OperationDependencyGraphPlugin.class);
    private final AllocationComponentOperationPairFactory pairFactory;
    private final String COMPONENT_NODE_ID_PREFIX = "component_";
    private final String CONTAINER_NODE_ID_PREFIX = "container_";

    public OperationDependencyGraphPlugin(final String name,
            final SystemEntityFactory systemEntityFactory) {
        super(name, systemEntityFactory,
                new DependencyGraph<AllocationComponentOperationPair>(
                AbstractSystemSubFactory.ROOT_ELEMENT_ID,
                new AllocationComponentOperationPair(AbstractSystemSubFactory.ROOT_ELEMENT_ID,
                systemEntityFactory.getOperationFactory().rootOperation, systemEntityFactory.getAllocationFactory().rootAllocationComponent)));
        this.pairFactory = new AllocationComponentOperationPairFactory(systemEntityFactory, super.dependencyGraph.getRootNode().getEntity());
    }

    private String containerNodeLabel(final ExecutionContainer container) {
        return String.format("%s\\n%s", STEREOTYPE_EXECUTION_CONTAINER, container.getName());
    }

    private String componentNodeLabel(final AllocationComponentInstance component,
            final boolean shortLabels) {
        //String resourceContainerName = component.getExecutionContainer().getName();
        String assemblyComponentName = component.getAssemblyComponent().getName();
        String componentTypePackagePrefx = component.getAssemblyComponent().getType().getPackageName();
        String componentTypeIdentifier = component.getAssemblyComponent().getType().getTypeName();

        StringBuilder strBuild = new StringBuilder(STEREOTYPE_ALLOCATION_COMPONENT + "\\n");
        strBuild.append(assemblyComponentName).append(":");
        if (!shortLabels) {
            strBuild.append(componentTypePackagePrefx).append(".");
        } else {
            strBuild.append("..");
        }
        strBuild.append(componentTypeIdentifier);
        return strBuild.toString();
    }

    protected void dotEdges(Collection<DependencyGraphNode<AllocationComponentOperationPair>> nodes,
            PrintStream ps, final boolean shortLabels) {

        /* Execution container ID x contained components  */
        Hashtable<Integer, Collection<AllocationComponentInstance>> containerId2componentMapping =
                new Hashtable<Integer, Collection<AllocationComponentInstance>>();
        Hashtable<Integer, Collection<DependencyGraphNode<AllocationComponentOperationPair>>> componentId2pairMapping =
                new Hashtable<Integer, Collection<DependencyGraphNode<AllocationComponentOperationPair>>>();

        // Derive container / component / operation hieraŕchy
        for (DependencyGraphNode<AllocationComponentOperationPair> pairNode : nodes) {
            AllocationComponentInstance curComponent = pairNode.getEntity().getAllocationComponent();
            ExecutionContainer curContainer = curComponent.getExecutionContainer();
            int componentId = curComponent.getId();
            int containerId = curContainer.getId();

            Collection<DependencyGraphNode<AllocationComponentOperationPair>> containedPairs =
                    componentId2pairMapping.get(componentId);
            if (containedPairs == null) {
                // component not yet registered
                containedPairs =
                        new ArrayList<DependencyGraphNode<AllocationComponentOperationPair>>();
                componentId2pairMapping.put(componentId, containedPairs);
                Collection<AllocationComponentInstance> containedComponents =
                        containerId2componentMapping.get(containerId);
                if (containedComponents == null) {
                    containedComponents = new ArrayList<AllocationComponentInstance>();
                    containerId2componentMapping.put(containerId, containedComponents);
                }
                containedComponents.add(curComponent);
            }
            containedPairs.add(pairNode);
        }

        ExecutionContainer rootContainer = this.getSystemEntityFactory().getExecutionEnvironmentFactory().rootExecutionContainer;
        int rootContainerId = rootContainer.getId();
        StringBuilder strBuild = new StringBuilder();
        for (Entry<Integer, Collection<AllocationComponentInstance>> containerComponentEntry : containerId2componentMapping.entrySet()) {
            int curContainerId = containerComponentEntry.getKey();
            ExecutionContainer curContainer = this.getSystemEntityFactory().getExecutionEnvironmentFactory().getExecutionContainerByContainerId(curContainerId);

            if (curContainerId == rootContainerId) {
                strBuild.append(DotFactory.createNode("",
                        getNodeId(this.dependencyGraph.getRootNode()),
                        "$",
                        DotFactory.DOT_SHAPE_NONE,
                        null, // style
                        null, // framecolor
                        null, // fillcolor
                        null, // fontcolor
                        DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
                        null, // imagefilename
                        null // misc
                        )).toString();
            } else {
                strBuild.append(DotFactory.createCluster("",
                        CONTAINER_NODE_ID_PREFIX + curContainer.getId(),
                        containerNodeLabel(curContainer),
                        DotFactory.DOT_SHAPE_BOX, // shape
                        null, // style
                        null, // framecolor
                        null, // fillcolor
                        null, // fontcolor
                        DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
                        null));  // misc
                // dot code for contained components
                for (AllocationComponentInstance curComponent : containerComponentEntry.getValue()) {
                    int curComponentId = curComponent.getId();
                    strBuild.append(DotFactory.createCluster("",
                            COMPONENT_NODE_ID_PREFIX + curComponentId,
                            componentNodeLabel(curComponent, shortLabels),
                            DotFactory.DOT_SHAPE_BOX,
                            null, // style
                            null, // framecolor
                            null, // fillcolor
                            null, // fontcolor
                            DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
                            null // misc
                            ));
                    for (DependencyGraphNode<AllocationComponentOperationPair> curPair : componentId2pairMapping.get(curComponentId)) {
                        strBuild.append(DotFactory.createNode("",
                                getNodeId(curPair),
                                curPair.getEntity().getOperation().getSignature().getName().toString(),
                                DotFactory.DOT_SHAPE_OVAL,
                                null, // style
                                null, // framecolor
                                null, // fillcolor
                                null, // fontcolor
                                DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
                                null, // imagefilename
                                null // misc
                                )).toString();
                    }
                    strBuild.append("}\n");
                }
                strBuild.append("}\n");
            }
        }
        ps.println(strBuild.toString());
    }

    public void newTrace(MessageTrace t) {
        for (Message m : t.getSequenceAsVector()) {
            if (m instanceof SynchronousReplyMessage) {
                continue;
            }
            AllocationComponentInstance senderComponent = m.getSendingExecution().getAllocationComponent();
            AllocationComponentInstance receiverComponent = m.getReceivingExecution().getAllocationComponent();
            int rootOperationId = this.getSystemEntityFactory().getOperationFactory().rootOperation.getId();
            Operation senderOperation = m.getSendingExecution().getOperation();
            Operation receiverOperation = m.getReceivingExecution().getOperation();
            final String senderPairFactoryName = senderComponent.getId() + "-" + senderOperation.getId();
            final String receiverPairFactoryName = receiverComponent.getId() + "-" + receiverOperation.getId();
            AllocationComponentOperationPair senderPair = (senderOperation.getId() == rootOperationId) ? this.dependencyGraph.getRootNode().getEntity() : this.pairFactory.getPairByFactoryName(senderPairFactoryName);
            AllocationComponentOperationPair receiverPair = (receiverOperation.getId() == rootOperationId) ? this.dependencyGraph.getRootNode().getEntity() : this.pairFactory.getPairByFactoryName(receiverPairFactoryName);
            if (senderPair == null) {
                senderPair = this.pairFactory.createAndRegisterPair(senderPairFactoryName, senderOperation, receiverComponent);
            }
            if (receiverPair == null) {
                receiverPair = this.pairFactory.createAndRegisterPair(receiverPairFactoryName, receiverOperation, receiverComponent);
            }

            DependencyGraphNode<AllocationComponentOperationPair> senderNode = this.dependencyGraph.getNode(senderPair.getId());
            DependencyGraphNode<AllocationComponentOperationPair> receiverNode = this.dependencyGraph.getNode(receiverPair.getId());
            if (senderNode == null) {
                senderNode = new DependencyGraphNode<AllocationComponentOperationPair>(senderPair.getId(), senderPair);
                this.dependencyGraph.addNode(senderNode.getId(), senderNode);
            }
            if (receiverNode == null) {
                receiverNode = new DependencyGraphNode<AllocationComponentOperationPair>(receiverPair.getId(), receiverPair);
                this.dependencyGraph.addNode(receiverNode.getId(), receiverNode);
            }
            senderNode.addOutgoingDependency(receiverNode);
            receiverNode.addIncomingDependency(senderNode);
        }
        this.reportSuccess(t.getTraceId());
    }
}
