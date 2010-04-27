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
import kieker.tpan.datamodel.AllocationComponent;
import kieker.tpan.datamodel.ExecutionContainer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import kieker.tpan.datamodel.Message;
import kieker.tpan.datamodel.MessageTrace;
import kieker.tpan.datamodel.SynchronousReplyMessage;
import kieker.tpan.datamodel.factories.SystemEntityFactory;
import kieker.tpan.plugins.util.dot.DotFactory;

/**
 * Refactored copy from LogAnalysis-legacy tool
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr,
 */
public class ComponentDependencyGraphPlugin extends AbstractDependencyGraphPlugin<AllocationComponent> {

    private static final Log log = LogFactory.getLog(ComponentDependencyGraphPlugin.class);
    private final String CONTAINER_NODE_ID_PREFIX = "container";

    public ComponentDependencyGraphPlugin(final String name,
            final SystemEntityFactory systemEntityFactory) {
        super(name, systemEntityFactory,
                new DependencyGraph<AllocationComponent>(
                systemEntityFactory.getAllocationFactory().rootAllocationComponent.getId(),
                systemEntityFactory.getAllocationFactory().rootAllocationComponent));
    }

    private String componentNodeLabel(final DependencyGraphNode<AllocationComponent> node,
            final boolean shortLabels) {
        AllocationComponent component = node.getEntity();
        if (component == super.getSystemEntityFactory().getAllocationFactory().rootAllocationComponent) {
            return "$";
        }

        //String resourceContainerName = component.getExecutionContainer().getName();
        String assemblyComponentName = component.getAssemblyComponent().getName();
        String componentTypePackagePrefx = component.getAssemblyComponent().getType().getPackageName();
        String componentTypeIdentifier = component.getAssemblyComponent().getType().getTypeName();

        StringBuilder strBuild = new StringBuilder(STEREOTYPE_ALLOCATION_COMPONENT + "\\n");//(resourceContainerName).append("::")
        strBuild.append(assemblyComponentName).append(":");
        if (!shortLabels) {
            strBuild.append(componentTypePackagePrefx).append(".");
        } else {
            strBuild.append("..");
        }
        strBuild.append(componentTypeIdentifier);
        return strBuild.toString();
    }

    protected void dotEdges(Collection<DependencyGraphNode<AllocationComponent>> nodes,
            PrintStream ps, final boolean shortLabels) {

        /* Execution container ID x DependencyGraphNode  */
        Hashtable<Integer, Collection<DependencyGraphNode<AllocationComponent>>> component2containerMapping =
                new Hashtable<Integer, Collection<DependencyGraphNode<AllocationComponent>>>();

        for (DependencyGraphNode<AllocationComponent> node : nodes) {
            int containerId = node.getEntity().getExecutionContainer().getId();
            Collection<DependencyGraphNode<AllocationComponent>> containedComponents =
                    component2containerMapping.get(containerId);
            if (containedComponents == null) {
                containedComponents =
                        new ArrayList<DependencyGraphNode<AllocationComponent>>();
                component2containerMapping.put(containerId, containedComponents);
            }
            containedComponents.add(node);
        }

        ExecutionContainer rootContainer = this.getSystemEntityFactory().getExecutionEnvironmentFactory().rootExecutionContainer;
        int rootContainerId = rootContainer.getId();
        AllocationComponent rootComponent = this.getSystemEntityFactory().getAllocationFactory().rootAllocationComponent;
        StringBuilder strBuild = new StringBuilder();
        for (Entry<Integer, Collection<DependencyGraphNode<AllocationComponent>>> entry : component2containerMapping.entrySet()) {
            int curContainerId = entry.getKey();
            ExecutionContainer curContainer = this.getSystemEntityFactory().getExecutionEnvironmentFactory().getExecutionContainerByContainerId(curContainerId);
            if (curContainerId == rootContainerId) {
                strBuild.append(DotFactory.createNode("",
                        getNodeId(this.dependencyGraph.getRootNode()),
                        componentNodeLabel(this.dependencyGraph.getRootNode(), shortLabels),
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
                        STEREOTYPE_EXECUTION_CONTAINER + "\\n" + curContainer.getName(),
                        DotFactory.DOT_SHAPE_BOX, // shape
                        DotFactory.DOT_STYLE_FILLED, // style
                        null, // framecolor
                        DotFactory.DOT_FILLCOLOR_WHITE, // fillcolor
                        null, // fontcolor
                        DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
                        null));  // misc
                // dot code for contained components
                for (DependencyGraphNode<AllocationComponent> node : entry.getValue()) {
                    strBuild.append(DotFactory.createNode("",
                            getNodeId(node),
                            componentNodeLabel(node, shortLabels),
                            DotFactory.DOT_SHAPE_BOX,
                            DotFactory.DOT_STYLE_FILLED, // style
                            null, // framecolor
                            DotFactory.DOT_FILLCOLOR_WHITE, // fillcolor
                            null, // fontcolor
                            DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
                            null, // imagefilename
                            null // misc
                            )).toString();
                    //strBuild.append(node.getId()).append("[label =\"").append(componentNodeLabel(node, shortLabels)).append("\",shape=box];\n");
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
            AllocationComponent senderComponent = m.getSendingExecution().getAllocationComponent();
            AllocationComponent receiverComponent = m.getReceivingExecution().getAllocationComponent();
            DependencyGraphNode<AllocationComponent> senderNode = this.dependencyGraph.getNode(senderComponent.getId());
            DependencyGraphNode<AllocationComponent> receiverNode = this.dependencyGraph.getNode(receiverComponent.getId());
            if (senderNode == null) {
                senderNode = new DependencyGraphNode<AllocationComponent>(senderComponent.getId(), senderComponent);
                this.dependencyGraph.addNode(senderNode.getId(), senderNode);
            }
            if (receiverNode == null) {
                receiverNode = new DependencyGraphNode<AllocationComponent>(receiverComponent.getId(), receiverComponent);
                this.dependencyGraph.addNode(receiverNode.getId(), receiverNode);
            }
            senderNode.addOutgoingDependency(receiverNode);
            receiverNode.addIncomingDependency(senderNode);
        }
        this.reportSuccess(t.getTraceId());
    }
}
