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
import kieker.tpan.datamodel.SynchronousReplyMessage;
import kieker.tpan.datamodel.factories.SystemEntityFactory;
import kieker.tpan.plugins.util.dot.DotFactory;

/**
 * Refactored copy from LogAnalysis-legacy tool
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr,
 */
public class ComponentDependencyGraphPlugin extends AbstractDependencyGraphPlugin<AllocationComponentInstance> {

    private static final Log log = LogFactory.getLog(ComponentDependencyGraphPlugin.class);
    private final String COMPONENT_NODE_ID_PREFIX = "component";
    private final String CONTAINER_NODE_ID_PREFIX = "container";

    public ComponentDependencyGraphPlugin(final String name,
            final SystemEntityFactory systemEntityFactory) {
        super(name, systemEntityFactory,
                new DependencyGraph(
                systemEntityFactory.getAllocationFactory().rootAllocationComponent.getId(),
                systemEntityFactory.getAllocationFactory().rootAllocationComponent));
    }

    private String componentNodeLabel(final DependencyGraphNode<AllocationComponentInstance> node,
            final boolean shortLabels) {
        AllocationComponentInstance component = (AllocationComponentInstance) node.getEntity();
        if (component == super.getSystemEntityFactory().getAllocationFactory().rootAllocationComponent) {
            return "$";
        }

        //String resourceContainerName = component.getExecutionContainer().getName();
        String assemblyComponentName = component.getAssemblyComponent().getName();
        String componentTypePackagePrefx = component.getAssemblyComponent().getType().getPackageName();
        String componentTypeIdentifier = component.getAssemblyComponent().getType().getTypeName();

        StringBuilder strBuild = new StringBuilder();//(resourceContainerName).append("::")
        strBuild.append(assemblyComponentName).append(":");
        if (!shortLabels) {
            strBuild.append(componentTypePackagePrefx).append(".");
        } else {
            strBuild.append("..");
        }
        strBuild.append(componentTypeIdentifier);
        return strBuild.toString();
    }

    protected void dotEdges(Collection<DependencyGraphNode<AllocationComponentInstance>> nodes,
            PrintStream ps, final boolean shortLabels) {

        /* Execution container ID x DependencyGraphNode  */
        Hashtable<Integer, Collection<DependencyGraphNode<AllocationComponentInstance>>> component2containerMapping =
                new Hashtable<Integer, Collection<DependencyGraphNode<AllocationComponentInstance>>>();

        for (DependencyGraphNode<AllocationComponentInstance> node : nodes) {
            int containerId = node.getEntity().getExecutionContainer().getId();
            Collection<DependencyGraphNode<AllocationComponentInstance>> containedComponents =
                    component2containerMapping.get(containerId);
            if (containedComponents == null) {
                containedComponents =
                        new ArrayList<DependencyGraphNode<AllocationComponentInstance>>();
                component2containerMapping.put(containerId, containedComponents);
            }
            containedComponents.add(node);
        }

        ExecutionContainer rootContainer = this.getSystemEntityFactory().getExecutionEnvironmentFactory().rootExecutionContainer;
        int rootContainerId = rootContainer.getId();
        AllocationComponentInstance rootComponent = this.getSystemEntityFactory().getAllocationFactory().rootAllocationComponent;
        StringBuilder strBuild = new StringBuilder();
        for (Entry<Integer, Collection<DependencyGraphNode<AllocationComponentInstance>>> entry : component2containerMapping.entrySet()) {
            int curContainerId = entry.getKey();
            ExecutionContainer curContainer = this.getSystemEntityFactory().getExecutionEnvironmentFactory().getExecutionContainerByContainerId(curContainerId);
            if (curContainerId == rootContainerId) {
                strBuild.append(DotFactory.createNode("",
                        COMPONENT_NODE_ID_PREFIX + rootComponent.getId(),
                        componentNodeLabel(this.dependencyGraph.getRootNode(), shortLabels),
                        DotFactory.DOT_SHAPE_OVAL,
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
                        curContainer.getName(),
                        DotFactory.DOT_SHAPE_BOX, // shape
                        null, // style
                        null, // framecolor
                        null, // fillcolor
                        null, // fontcolor
                        DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
                        null));  // misc
                // dot code for contained components
                for (DependencyGraphNode node : entry.getValue()) {
                    strBuild.append(DotFactory.createNode("",
                            COMPONENT_NODE_ID_PREFIX + node.getId(),
                            componentNodeLabel(node, shortLabels),
                            DotFactory.DOT_SHAPE_BOX,
                            null, // style
                            null, // framecolor
                            null, // fillcolor
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

    /** Traverse tree recursively and generate dot code for vertices. */
    protected void dotVerticesFromSubTree(final DependencyGraphNode n,
            final PrintStream ps, final boolean includeWeights) {
        for (DependencyEdge outgoingDependency : (Collection<DependencyEdge>) n.getOutgoingDependencies()) {
            DependencyGraphNode destNode = outgoingDependency.getDestination();
            StringBuilder strBuild = new StringBuilder();
            if (includeWeights) {
                strBuild.append(DotFactory.createConnection(
                        "",
                        COMPONENT_NODE_ID_PREFIX + n.getId(),
                        COMPONENT_NODE_ID_PREFIX + destNode.getId(),
                        "" + outgoingDependency.getOutgoingWeight(),
                        DotFactory.DOT_STYLE_DASHED,
                        DotFactory.DOT_ARROWHEAD_OPEN));
            } else {
                strBuild.append(DotFactory.createConnection(
                        "",
                        COMPONENT_NODE_ID_PREFIX + n.getId(),
                        COMPONENT_NODE_ID_PREFIX + destNode.getId(),
                        DotFactory.DOT_STYLE_DASHED,
                        DotFactory.DOT_ARROWHEAD_OPEN));
            }
            dotVerticesFromSubTree(destNode, ps, includeWeights);
            ps.println(strBuild.toString());
        }
    }

    public void newTrace(MessageTrace t) {
        for (Message m : t.getSequenceAsVector()) {
            if (m instanceof SynchronousReplyMessage) {
                continue;
            }
            AllocationComponentInstance senderComponent = m.getSendingExecution().getAllocationComponent();
            AllocationComponentInstance receiverComponent = m.getReceivingExecution().getAllocationComponent();
            DependencyGraphNode<AllocationComponentInstance> senderNode = this.dependencyGraph.getNode(senderComponent.getId());
            DependencyGraphNode<AllocationComponentInstance> receiverNode = this.dependencyGraph.getNode(receiverComponent.getId());
            if (senderNode == null) {
                senderNode = new DependencyGraphNode<AllocationComponentInstance>(senderComponent.getId(), senderComponent);
                this.dependencyGraph.addNode(senderNode.getId(), senderNode);
            }
            if (receiverNode == null) {
                receiverNode = new DependencyGraphNode<AllocationComponentInstance>(receiverComponent.getId(), receiverComponent);
                this.dependencyGraph.addNode(receiverNode.getId(), receiverNode);
            }
            senderNode.addOutgoingDependency(receiverNode);
            receiverNode.addIncomingDependency(senderNode);
        }
        this.reportSuccess(t.getTraceId());
    }
}
