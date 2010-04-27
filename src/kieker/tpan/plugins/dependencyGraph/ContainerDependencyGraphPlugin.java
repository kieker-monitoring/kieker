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
public class ContainerDependencyGraphPlugin extends AbstractDependencyGraphPlugin<ExecutionContainer> {

    private static final Log log = LogFactory.getLog(ContainerDependencyGraphPlugin.class);

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
                    getNodeId(node),
                    (curContainerId == rootContainerId) ? "$" : STEREOTYPE_EXECUTION_CONTAINER + "\\n" + curContainer.getName(),
                    (curContainerId == rootContainerId) ? DotFactory.DOT_SHAPE_NONE : DotFactory.DOT_SHAPE_BOX3D,
                    (curContainerId == rootContainerId) ? null : DotFactory.DOT_STYLE_FILLED, // style
                    null, // framecolor
                    (curContainerId == rootContainerId) ? null : DotFactory.DOT_FILLCOLOR_WHITE, // fillcolor
                    null, // fontcolor
                    DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
                    null, // imagefilename
                    null // misc
                    )).toString();
            strBuild.append("\n");
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
            ExecutionContainer senderContainer = senderComponent.getExecutionContainer();
            ExecutionContainer receiverContainer = receiverComponent.getExecutionContainer();
            DependencyGraphNode<ExecutionContainer> senderNode = this.dependencyGraph.getNode(senderContainer.getId());
            DependencyGraphNode<ExecutionContainer> receiverNode = this.dependencyGraph.getNode(receiverContainer.getId());

            if (senderNode == null) {
                senderNode = new DependencyGraphNode<ExecutionContainer>(senderContainer.getId(), senderContainer);
                this.dependencyGraph.addNode(senderContainer.getId(), senderNode);
            }
            if (receiverNode == null) {
                receiverNode = new DependencyGraphNode<ExecutionContainer>(receiverContainer.getId(), receiverContainer);
                this.dependencyGraph.addNode(receiverContainer.getId(), receiverNode);
            }
            senderNode.addOutgoingDependency(receiverNode);
            receiverNode.addIncomingDependency(senderNode);
        }
        this.reportSuccess(t.getTraceId());
    }
}
