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
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;

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
public class ComponentDependencyGraphPluginAssembly extends AbstractDependencyGraphPlugin<AssemblyComponent> {

    private static final Log log = LogFactory.getLog(ComponentDependencyGraphPluginAssembly.class);
    private final File dotOutputFile;
    private final boolean includeWeights;
    private final boolean shortLabels;
    private final boolean includeSelfLoops;

    public ComponentDependencyGraphPluginAssembly(
            final String name,
            final SystemModelRepository systemEntityFactory,
            final File dotOutputFile,
            final boolean includeWeights,
            final boolean shortLabels,
            final boolean includeSelfLoops) {
        super(name, systemEntityFactory,
                new DependencyGraph<AssemblyComponent>(
                systemEntityFactory.getAssemblyFactory().rootAssemblyComponent.getId(),
                systemEntityFactory.getAssemblyFactory().rootAssemblyComponent));
        this.dotOutputFile = dotOutputFile;
        this.includeWeights = includeWeights;
        this.shortLabels = shortLabels;
        this.includeSelfLoops = includeSelfLoops;
    }

    private String nodeLabel(AssemblyComponent curComponent){
        if (this.shortLabels){
            return STEREOTYPE_ASSEMBLY_COMPONENT + "\\n" + curComponent.getName()+":.."+curComponent.getType().getTypeName();
        } else {
            return STEREOTYPE_ASSEMBLY_COMPONENT + "\\n" + curComponent.getName()+":"+curComponent.getType().getFullQualifiedName();
        }
    }

    protected void dotEdges(Collection<DependencyGraphNode<AssemblyComponent>> nodes,
            PrintStream ps, final boolean shortLabels) {

        AssemblyComponent rootComponent = this.getSystemEntityFactory().getAssemblyFactory().rootAssemblyComponent;
        int rootComponentId = rootComponent.getId();
        StringBuilder strBuild = new StringBuilder();
        // dot code for contained components
        for (DependencyGraphNode<AssemblyComponent> node : nodes) {
            AssemblyComponent curComponent = node.getEntity();
            int curComponentId = node.getId();
            strBuild.append(DotFactory.createNode("",
                    getNodeId(node),
                    (curComponentId == rootComponentId) ? "$" : nodeLabel(curComponent),
                    (curComponentId == rootComponentId) ? DotFactory.DOT_SHAPE_NONE : DotFactory.DOT_SHAPE_BOX,
                    (curComponentId == rootComponentId) ? null : DotFactory.DOT_STYLE_FILLED, // style
                    null, // framecolor
                    (curComponentId == rootComponentId) ? null : DotFactory.DOT_FILLCOLOR_WHITE, // fillcolor
                    null, // fontcolor
                    DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
                    null, // imagefilename
                    null // misc
                    )).toString();
            //strBuild.append(node.getId()).append("[label =\"").append(componentNodeLabel(node, shortLabels)).append("\",shape=box];\n");
            // See ticket http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/201
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
        if (!error) {
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

    @Override
    public IInputPort<MessageTrace> getMessageTraceInputPort() {
        return this.messageTraceInputPort;
    }
    private final IInputPort<MessageTrace> messageTraceInputPort =
            new AbstractInputPort<MessageTrace>("Message traces") {

                @Override
                public void newEvent(MessageTrace t) {
                    for (Message m : t.getSequenceAsVector()) {
                        if (m instanceof SynchronousReplyMessage) {
                            continue;
                        }
                        AssemblyComponent senderComponent = m.getSendingExecution().getAllocationComponent().getAssemblyComponent();
                        AssemblyComponent receiverComponent = m.getReceivingExecution().getAllocationComponent().getAssemblyComponent();
                        DependencyGraphNode<AssemblyComponent> senderNode = dependencyGraph.getNode(senderComponent.getId());
                        DependencyGraphNode<AssemblyComponent> receiverNode = dependencyGraph.getNode(receiverComponent.getId());
                        if (senderNode == null) {
                            senderNode = new DependencyGraphNode<AssemblyComponent>(senderComponent.getId(), senderComponent);
                            dependencyGraph.addNode(senderNode.getId(), senderNode);
                        }
                        if (receiverNode == null) {
                            receiverNode = new DependencyGraphNode<AssemblyComponent>(receiverComponent.getId(), receiverComponent);
                            dependencyGraph.addNode(receiverNode.getId(), receiverNode);
                        }
                        senderNode.addOutgoingDependency(receiverNode);
                        receiverNode.addIncomingDependency(senderNode);
                    }
                    reportSuccess(t.getTraceId());
                }
            };
}
