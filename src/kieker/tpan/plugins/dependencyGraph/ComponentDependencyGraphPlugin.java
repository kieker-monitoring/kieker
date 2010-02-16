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
import kieker.tpan.plugins.traceReconstruction.AbstractTpanMessageTraceProcessingComponent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import kieker.tpan.datamodel.AllocationComponentInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import kieker.tpan.datamodel.Message;
import kieker.tpan.datamodel.MessageTrace;
import kieker.tpan.datamodel.SynchronousReplyMessage;
import kieker.tpan.datamodel.factories.SystemEntityFactory;

/**
 * Refactored copy from LogAnalysis-legacy tool
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr,
 */
public class ComponentDependencyGraphPlugin extends AbstractDependencyGraphPlugin<AllocationComponentInstance> {

    private static final Log log = LogFactory.getLog(ComponentDependencyGraphPlugin.class);
    private DependencyGraph<AllocationComponentInstance> dependencyGraph;

    public ComponentDependencyGraphPlugin(final String name,
            final SystemEntityFactory systemEntityFactory) {
        super(name, systemEntityFactory);
        this.dependencyGraph = 
                new DependencyGraph<AllocationComponentInstance>(
                systemEntityFactory.getAllocationFactory().rootAllocationComponent.getId(),
                systemEntityFactory.getAllocationFactory().rootAllocationComponent);
    }

    protected String nodeLabel(final DependencyGraphNode<AllocationComponentInstance> node,
            final boolean shortLabels) {
        AllocationComponentInstance component = (AllocationComponentInstance) node.getAllocationComponent();
        if (component == super.getSystemEntityFactory().getAllocationFactory().rootAllocationComponent) {
            return "$";
        }

        String resourceContainerName = component.getExecutionContainer().getName();
        String assemblyComponentName = component.getAssemblyComponent().getName();
        String componentTypePackagePrefx = component.getAssemblyComponent().getType().getPackageName();
        String componentTypeIdentifier = component.getAssemblyComponent().getType().getTypeName();

        StringBuilder strBuild = new StringBuilder(resourceContainerName).append("::").append(assemblyComponentName).append(":");
        if (!shortLabels) {
            strBuild.append(componentTypePackagePrefx);
        } else {
            strBuild.append("..");
        }
        strBuild.append(componentTypeIdentifier);
        return strBuild.toString();
    }

    protected void dotEdges(Collection<DependencyGraphNode<AllocationComponentInstance>> nodes,
            PrintStream ps, final boolean shortLabels) {
        StringBuilder strBuild = new StringBuilder();
        for (DependencyGraphNode node : nodes) {
            strBuild.append(node.getId()).append("[label =\"")
                    .append(nodeLabel(node, shortLabels)).append("\",shape=box];\n");
        }
        ps.println(strBuild.toString());
    }

    /** Traverse tree recursively and generate dot code for vertices. */
    protected void dotVerticesFromSubTree(final DependencyGraphNode n,
        final PrintStream ps, final boolean includeWeights) {
        for (DependencyEdge outgoingDependency : (Collection<DependencyEdge>)n.getOutgoingDependencies()) {
            DependencyGraphNode destNode = outgoingDependency.getDestination();
            StringBuilder strBuild = new StringBuilder();
            strBuild.append("\n").append(n.getId()).append("->")
                    .append(destNode.getId()).append("[style=dashed,arrowhead=open");
            if (includeWeights) {
                strBuild.append(",label = ").append(outgoingDependency.getOutgoingWeight()).append(", weight =").append((outgoingDependency).getOutgoingWeight());
            }
            strBuild.append(" ]");
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
