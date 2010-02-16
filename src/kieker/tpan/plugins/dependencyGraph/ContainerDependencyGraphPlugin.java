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
import java.util.Collection;
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
public class ContainerDependencyGraphPlugin extends AbstractDependencyGraphPlugin<ExecutionContainer> {

    private static final Log log = LogFactory.getLog(ContainerDependencyGraphPlugin.class);
    private final String CONTAINER_NODE_ID_PREFIX = "container";

    public ContainerDependencyGraphPlugin(final String name,
            final SystemEntityFactory systemEntityFactory) {
        super(name, systemEntityFactory,
                new DependencyGraph<ExecutionContainer>(
                systemEntityFactory.getExecutionEnvironmentFactory().rootExecutionContainer.getId(),
                systemEntityFactory.getExecutionEnvironmentFactory().rootExecutionContainer));
    }

    protected void dotEdges(Collection<DependencyGraphNode<ExecutionContainer>> nodes,
            PrintStream ps, final boolean shortLabels) {

        ExecutionContainer rootContainer = this.getSystemEntityFactory().getExecutionEnvironmentFactory().rootExecutionContainer;
        int rootContainerId = rootContainer.getId();
        StringBuilder strBuild = new StringBuilder();
        for (DependencyGraphNode<ExecutionContainer> node : nodes) {
            ExecutionContainer curContainer = node.getEntity();
            int curContainerId = node.getId();
            strBuild.append(DotFactory.createNode("",
                    CONTAINER_NODE_ID_PREFIX + node.getId(),
                    (curContainerId == rootContainerId) ? "$" : STEREOTYPE_EXECUTION_CONTAINER + "\\n" + curContainer.getName(),
                    (curContainerId == rootContainerId) ? DotFactory.DOT_SHAPE_OVAL : DotFactory.DOT_SHAPE_BOX3D,
                    null, // style
                    null, // framecolor
                    null, // fillcolor
                    null, // fontcolor
                    DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
                    null, // imagefilename
                    null // misc
                    )).toString();
            strBuild.append("\n");
        }
        ps.println(strBuild.toString());
    }

    /** Traverse tree recursively and generate dot code for vertices. */
    protected void dotVerticesFromSubTree(final DependencyGraphNode<ExecutionContainer> n,
            final PrintStream ps, final boolean includeWeights) {
        for (DependencyEdge outgoingDependency : n.getOutgoingDependencies()) {
            DependencyGraphNode<ExecutionContainer> destNode =
                    (DependencyGraphNode<ExecutionContainer>) outgoingDependency.getDestination();
            StringBuilder strBuild = new StringBuilder();
            if (includeWeights) {
                strBuild.append(DotFactory.createConnection(
                        "",
                        CONTAINER_NODE_ID_PREFIX + n.getId(),
                        CONTAINER_NODE_ID_PREFIX + destNode.getId(),
                        "" + outgoingDependency.getOutgoingWeight(),
                        DotFactory.DOT_STYLE_DASHED,
                        DotFactory.DOT_ARROWHEAD_OPEN));
            } else {
                strBuild.append(DotFactory.createConnection(
                        "",
                        CONTAINER_NODE_ID_PREFIX + n.getId(),
                        CONTAINER_NODE_ID_PREFIX + destNode.getId(),
                        DotFactory.DOT_STYLE_DASHED,
                        DotFactory.DOT_ARROWHEAD_OPEN));
            }
            if (n != destNode) {
                dotVerticesFromSubTree(destNode, ps, includeWeights);
            }
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
            ExecutionContainer senderContainer = senderComponent.getExecutionContainer();
            ExecutionContainer receiverContainer = receiverComponent.getExecutionContainer();
            DependencyGraphNode<ExecutionContainer> senderNode = this.dependencyGraph.getNode(senderContainer.getId());
            DependencyGraphNode<ExecutionContainer> receiverNode = this.dependencyGraph.getNode(receiverContainer.getId());

            if (senderNode == null) {
                senderNode = new DependencyGraphNode<ExecutionContainer>(senderContainer.getId(), senderContainer);
                System.out.println("Adding node: " + senderNode + " id: " + senderNode.getId());
                this.dependencyGraph.addNode(senderContainer.getId(), senderNode);
            }
            if (receiverNode == null) {
                receiverNode = new DependencyGraphNode<ExecutionContainer>(receiverContainer.getId(), receiverContainer);
                System.out.println("Adding node: " + receiverNode + " id: " + receiverNode.getId());
                this.dependencyGraph.addNode(receiverContainer.getId(), receiverNode);
            }
            senderNode.addOutgoingDependency(receiverNode);
            receiverNode.addIncomingDependency(senderNode);
            System.out.println("Size: " + this.dependencyGraph.size());
        }
        this.reportSuccess(t.getTraceId());
    }
}
