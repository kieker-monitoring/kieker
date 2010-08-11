package kieker.tools.traceAnalysis.plugins.visualization.dependencyGraph;

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
import java.io.File;
import java.io.IOException;
import kieker.tools.traceAnalysis.systemModel.util.AllocationComponentOperationPair;
import kieker.tools.traceAnalysis.systemModel.repository.AllocationComponentOperationPairFactory;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map.Entry;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;
import kieker.analysis.plugin.configuration.IInputPort;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import kieker.tools.traceAnalysis.systemModel.Message;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.Signature;
import kieker.tools.traceAnalysis.systemModel.SynchronousReplyMessage;
import kieker.tools.traceAnalysis.systemModel.repository.AbstractSystemSubRepository;
import kieker.tools.traceAnalysis.systemModel.repository.AssemblyComponentOperationPairFactory;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.tools.traceAnalysis.systemModel.util.AssemblyComponentOperationPair;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.tools.traceAnalysis.plugins.visualization.util.dot.DotFactory;

/**
 * Refactored copy from LogAnalysis-legacy tool
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr,
 */
public class OperationDependencyGraphPluginAssembly extends AbstractDependencyGraphPlugin<AssemblyComponentOperationPair> {

    private static final Log log = LogFactory.getLog(OperationDependencyGraphPluginAssembly.class);
    private final AssemblyComponentOperationPairFactory pairFactory;
    private final String COMPONENT_NODE_ID_PREFIX = "component_";
    private final File dotOutputFile;
    private final boolean includeWeights;
    private final boolean shortLabels;
    private final boolean includeSelfLoops;

    public OperationDependencyGraphPluginAssembly(
            final String name,
            final SystemModelRepository systemEntityFactory,
            final File dotOutputFile,
            final boolean includeWeights,
            final boolean shortLabels,
            final boolean includeSelfLoops) {
        super(name, systemEntityFactory,
                new DependencyGraph<AssemblyComponentOperationPair>(
                AbstractSystemSubRepository.ROOT_ELEMENT_ID,
                new AssemblyComponentOperationPair(AbstractSystemSubRepository.ROOT_ELEMENT_ID,
                systemEntityFactory.getOperationFactory().rootOperation, systemEntityFactory.getAssemblyFactory().rootAssemblyComponent)));
        this.pairFactory = new AssemblyComponentOperationPairFactory(systemEntityFactory);
        this.dotOutputFile = dotOutputFile;
        this.includeWeights = includeWeights;
        this.shortLabels = shortLabels;
        this.includeSelfLoops = includeSelfLoops;
    }

    private String containerNodeLabel(final ExecutionContainer container) {
        return String.format("%s\\n%s", STEREOTYPE_EXECUTION_CONTAINER, container.getName());
    }

    private String componentNodeLabel(final AssemblyComponent component,
            final boolean shortLabels) {
        //String resourceContainerName = component.getExecutionContainer().getName();
        String assemblyComponentName = component.getName();
        String componentTypePackagePrefx = component.getType().getPackageName();
        String componentTypeIdentifier = component.getType().getTypeName();

        StringBuilder strBuild = new StringBuilder(STEREOTYPE_ASSEMBLY_COMPONENT + "\\n");
        strBuild.append(assemblyComponentName).append(":");
        if (!shortLabels) {
            strBuild.append(componentTypePackagePrefx).append(".");
        } else {
            strBuild.append("..");
        }
        strBuild.append(componentTypeIdentifier);
        return strBuild.toString();
    }

    protected void dotEdges(Collection<DependencyGraphNode<AssemblyComponentOperationPair>> nodes,
            PrintStream ps, final boolean shortLabels) {

        /* Component ID x contained operations  */
        Hashtable<Integer, Collection<DependencyGraphNode<AssemblyComponentOperationPair>>> componentId2pairMapping =
                new Hashtable<Integer, Collection<DependencyGraphNode<AssemblyComponentOperationPair>>>();

        // Derive component / operation hierarchy
        for (DependencyGraphNode<AssemblyComponentOperationPair> pairNode : nodes) {
            AssemblyComponent curComponent = pairNode.getEntity().getAssemblyComponent();
            int componentId = curComponent.getId();

            Collection<DependencyGraphNode<AssemblyComponentOperationPair>> containedPairs =
                    componentId2pairMapping.get(componentId);
            if (containedPairs == null) {
                // component not yet registered
                containedPairs =
                        new ArrayList<DependencyGraphNode<AssemblyComponentOperationPair>>();
                componentId2pairMapping.put(componentId, containedPairs);
            }
            containedPairs.add(pairNode);
        }

        AssemblyComponent rootComponent = this.getSystemEntityFactory().getAssemblyFactory().rootAssemblyComponent;
        int rootComponentId = rootComponent.getId();
        StringBuilder strBuild = new StringBuilder();
        for (Entry<Integer, Collection<DependencyGraphNode<AssemblyComponentOperationPair>>> componentOperationEntry : componentId2pairMapping.entrySet()) {
            int curComponentId = componentOperationEntry.getKey();
            AssemblyComponent curComponent = this.getSystemEntityFactory().getAssemblyFactory().lookupAssemblyComponentById(curComponentId);

            if (curComponentId == rootComponentId) {
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
                        COMPONENT_NODE_ID_PREFIX + curComponentId,
                        componentNodeLabel(curComponent, this.shortLabels),
                        DotFactory.DOT_SHAPE_BOX, // shape
                        DotFactory.DOT_STYLE_FILLED, // style
                        null, // framecolor
                        DotFactory.DOT_FILLCOLOR_WHITE, // fillcolor
                        null, // fontcolor
                        DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
                        null));  // misc
                for (DependencyGraphNode<AssemblyComponentOperationPair> curPair : componentOperationEntry.getValue()) {
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
                        int rootOperationId = getSystemEntityFactory().getOperationFactory().rootOperation.getId();
                        Operation senderOperation = m.getSendingExecution().getOperation();
                        Operation receiverOperation = m.getReceivingExecution().getOperation();
                        /* The following two get-calls to the factory return s.th. in either case */
                        AssemblyComponentOperationPair senderPair =
                                (senderOperation.getId() == rootOperationId) ? dependencyGraph.getRootNode().getEntity() : pairFactory.getPairInstanceByPair(senderComponent, senderOperation);
                        AssemblyComponentOperationPair receiverPair =
                                (receiverOperation.getId() == rootOperationId) ? dependencyGraph.getRootNode().getEntity() : pairFactory.getPairInstanceByPair(receiverComponent, receiverOperation);

                        DependencyGraphNode<AssemblyComponentOperationPair> senderNode = dependencyGraph.getNode(senderPair.getId());
                        DependencyGraphNode<AssemblyComponentOperationPair> receiverNode = dependencyGraph.getNode(receiverPair.getId());
                        if (senderNode == null) {
                            senderNode = new DependencyGraphNode<AssemblyComponentOperationPair>(senderPair.getId(), senderPair);
                            dependencyGraph.addNode(senderNode.getId(), senderNode);
                        }
                        if (receiverNode == null) {
                            receiverNode = new DependencyGraphNode<AssemblyComponentOperationPair>(receiverPair.getId(), receiverPair);
                            dependencyGraph.addNode(receiverNode.getId(), receiverNode);
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
