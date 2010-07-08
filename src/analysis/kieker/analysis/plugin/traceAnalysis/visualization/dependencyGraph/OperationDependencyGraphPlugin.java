package kieker.analysis.plugin.traceAnalysis.visualization.dependencyGraph;

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
import kieker.analysis.datamodel.util.AllocationComponentOperationPair;
import kieker.analysis.datamodel.repository.AllocationComponentOperationPairFactory;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map.Entry;
import kieker.analysis.datamodel.AllocationComponent;
import kieker.analysis.datamodel.ExecutionContainer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import kieker.analysis.datamodel.Message;
import kieker.analysis.datamodel.MessageTrace;
import kieker.analysis.datamodel.Operation;
import kieker.analysis.datamodel.Signature;
import kieker.analysis.datamodel.SynchronousReplyMessage;
import kieker.analysis.datamodel.repository.AbstractSystemSubRepository;
import kieker.analysis.datamodel.repository.SystemModelRepository;
import kieker.analysis.plugin.util.dot.DotFactory;

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
            final SystemModelRepository systemEntityFactory) {
        super(name, systemEntityFactory,
                new DependencyGraph<AllocationComponentOperationPair>(
                AbstractSystemSubRepository.ROOT_ELEMENT_ID,
                new AllocationComponentOperationPair(AbstractSystemSubRepository.ROOT_ELEMENT_ID,
                systemEntityFactory.getOperationFactory().rootOperation, systemEntityFactory.getAllocationFactory().rootAllocationComponent)));
        this.pairFactory = new AllocationComponentOperationPairFactory(systemEntityFactory);
    }

    private String containerNodeLabel(final ExecutionContainer container) {
        return String.format("%s\\n%s", STEREOTYPE_EXECUTION_CONTAINER, container.getName());
    }

    private String componentNodeLabel(final AllocationComponent component,
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
        Hashtable<Integer, Collection<AllocationComponent>> containerId2componentMapping =
                new Hashtable<Integer, Collection<AllocationComponent>>();
        Hashtable<Integer, Collection<DependencyGraphNode<AllocationComponentOperationPair>>> componentId2pairMapping =
                new Hashtable<Integer, Collection<DependencyGraphNode<AllocationComponentOperationPair>>>();

        // Derive container / component / operation hieraŕchy
        for (DependencyGraphNode<AllocationComponentOperationPair> pairNode : nodes) {
            AllocationComponent curComponent = pairNode.getEntity().getAllocationComponent();
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
                Collection<AllocationComponent> containedComponents =
                        containerId2componentMapping.get(containerId);
                if (containedComponents == null) {
                    containedComponents = new ArrayList<AllocationComponent>();
                    containerId2componentMapping.put(containerId, containedComponents);
                }
                containedComponents.add(curComponent);
            }
            containedPairs.add(pairNode);
        }

        ExecutionContainer rootContainer = this.getSystemEntityFactory().getExecutionEnvironmentFactory().rootExecutionContainer;
        int rootContainerId = rootContainer.getId();
        StringBuilder strBuild = new StringBuilder();
        for (Entry<Integer, Collection<AllocationComponent>> containerComponentEntry : containerId2componentMapping.entrySet()) {
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
                        DotFactory.DOT_STYLE_FILLED, // style
                        null, // framecolor
                        DotFactory.DOT_FILLCOLOR_WHITE, // fillcolor
                        null, // fontcolor
                        DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
                        null));  // misc
                // dot code for contained components
                for (AllocationComponent curComponent : containerComponentEntry.getValue()) {
                    int curComponentId = curComponent.getId();
                    strBuild.append(DotFactory.createCluster("",
                            COMPONENT_NODE_ID_PREFIX + curComponentId,
                            componentNodeLabel(curComponent, shortLabels),
                            DotFactory.DOT_SHAPE_BOX,
                            DotFactory.DOT_STYLE_FILLED, // style
                            null, // framecolor
                            DotFactory.DOT_FILLCOLOR_WHITE, // fillcolor
                            null, // fontcolor
                            DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
                            null // misc
                            ));
                    for (DependencyGraphNode<AllocationComponentOperationPair> curPair : componentId2pairMapping.get(curComponentId)) {
                        Signature sig = curPair.getEntity().getOperation().getSignature();
                        StringBuilder opLabel = new StringBuilder(sig.getName());
                        opLabel.append("(");
                        String[] paramList = sig.getParamTypeList();
                        if (paramList != null && paramList.length > 0) {
                            opLabel.append("..");
                        }
                        opLabel.append(")");
                        strBuild.append(DotFactory.createNode("",
                                getNodeId(curPair),
                                opLabel.toString(),
                                DotFactory.DOT_SHAPE_OVAL,
                                DotFactory.DOT_STYLE_FILLED, // style
                                null, // framecolor
                                DotFactory.DOT_FILLCOLOR_WHITE, // fillcolor
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

    public void newEvent(MessageTrace t) {
        for (Message m : t.getSequenceAsVector()) {
            if (m instanceof SynchronousReplyMessage) {
                continue;
            }
            AllocationComponent senderComponent = m.getSendingExecution().getAllocationComponent();
            AllocationComponent receiverComponent = m.getReceivingExecution().getAllocationComponent();
            int rootOperationId = this.getSystemEntityFactory().getOperationFactory().rootOperation.getId();
            Operation senderOperation = m.getSendingExecution().getOperation();
            Operation receiverOperation = m.getReceivingExecution().getOperation();
            /* The following two get-calls to the factory return s.th. in either case */
            AllocationComponentOperationPair senderPair =
                    (senderOperation.getId() == rootOperationId) ? this.dependencyGraph.getRootNode().getEntity() : this.pairFactory.getPairInstanceByPair(senderComponent, senderOperation);
            AllocationComponentOperationPair receiverPair =
                    (receiverOperation.getId() == rootOperationId) ? this.dependencyGraph.getRootNode().getEntity() : this.pairFactory.getPairInstanceByPair(receiverComponent, receiverOperation);

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

    public boolean execute() {
        return true; // no need to do anything here
    }

    public void terminate(boolean error) {
// no need to do anything here
    }
}
