/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.tools.traceAnalysis.plugins.visualization.dependencyGraph;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;
import kieker.analysis.plugin.configuration.IInputPort;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import kieker.tools.traceAnalysis.systemModel.Message;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.SynchronousReplyMessage;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.tools.traceAnalysis.plugins.visualization.util.dot.DotFactory;

/**
 * Refactored copy from LogAnalysis-legacy tool
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr,
 */
public class ContainerDependencyGraphPlugin extends AbstractDependencyGraphPlugin<ExecutionContainer> {

    private static final Log log = LogFactory.getLog(ContainerDependencyGraphPlugin.class);

    private final File dotOutputFile;
    private final boolean includeWeights;
    private final boolean shortLabels;
    private final boolean includeSelfLoops;

    public ContainerDependencyGraphPlugin(final String name,
            final SystemModelRepository systemEntityFactory,
            final File dotOutputFile,
            final boolean includeWeights,
            final boolean shortLabels,
            final boolean includeSelfLoops) {
        super(name, systemEntityFactory,
                new DependencyGraph<ExecutionContainer>(
                systemEntityFactory.getExecutionEnvironmentFactory().rootExecutionContainer.getId(),
                systemEntityFactory.getExecutionEnvironmentFactory().rootExecutionContainer));
        this.dotOutputFile = dotOutputFile;
        this.includeWeights = includeWeights;
        this.shortLabels = shortLabels;
        this.includeSelfLoops = includeSelfLoops;
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
                    ));
            strBuild.append("\n");
        }
        ps.println(strBuild.toString());
    }

    @Override
    public boolean execute() {
        return true; // no need to do anything here
    }

    /**
     * Saves the dependency graph to the dot file if error is not true.
     *
     * @param error
     */
    @Override
    public void terminate(boolean error) {
        if (!error){
            try {
                this.saveToDotFile(
                        this.dotOutputFile.getCanonicalPath(),
                        this.includeWeights,
                        this.shortLabels,
                        this.includeSelfLoops);
            } catch (IOException ex) {
               log.error("IOException", ex);
            }
        }
    }
    
    private final IInputPort<MessageTrace> messageTraceInputPort =
            new AbstractInputPort<MessageTrace>("Message traces") {

                @Override
                public void newEvent(MessageTrace t) {
                    for (Message m : t.getSequenceAsVector()) {
                        if (m instanceof SynchronousReplyMessage) {
                            continue;
                        }
                        AllocationComponent senderComponent = m.getSendingExecution().getAllocationComponent();
                        AllocationComponent receiverComponent = m.getReceivingExecution().getAllocationComponent();
                        ExecutionContainer senderContainer = senderComponent.getExecutionContainer();
                        ExecutionContainer receiverContainer = receiverComponent.getExecutionContainer();
                        DependencyGraphNode<ExecutionContainer> senderNode = dependencyGraph.getNode(senderContainer.getId());
                        DependencyGraphNode<ExecutionContainer> receiverNode = dependencyGraph.getNode(receiverContainer.getId());

                        if (senderNode == null) {
                            senderNode = new DependencyGraphNode<ExecutionContainer>(senderContainer.getId(), senderContainer);
                            dependencyGraph.addNode(senderContainer.getId(), senderNode);
                        }
                        if (receiverNode == null) {
                            receiverNode = new DependencyGraphNode<ExecutionContainer>(receiverContainer.getId(), receiverContainer);
                            dependencyGraph.addNode(receiverContainer.getId(), receiverNode);
                        }
                        senderNode.addOutgoingDependency(receiverNode);
                        receiverNode.addIncomingDependency(senderNode);
                    }
                    reportSuccess(t.getTraceId());
                }
            };

    @Override
    public IInputPort<MessageTrace> getMessageTraceInputPort() {
        return this.messageTraceInputPort;
    }
}
