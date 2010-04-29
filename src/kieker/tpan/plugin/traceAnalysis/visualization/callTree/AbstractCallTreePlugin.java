package kieker.tpan.plugin.traceAnalysis.visualization.callTree;

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
import kieker.tpan.datamodel.util.AssemblyComponentOperationPair;
import kieker.tpan.plugin.traceAnalysis.traceReconstruction.TraceProcessingException;
import kieker.tpan.plugin.traceAnalysis.AbstractMessageTraceProcessingPlugin;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import kieker.tpan.datamodel.AllocationComponent;
import kieker.tpan.datamodel.AssemblyComponent;
import kieker.tpan.datamodel.Message;
import kieker.tpan.datamodel.MessageTrace;
import kieker.tpan.datamodel.Operation;
import kieker.tpan.datamodel.Signature;
import kieker.tpan.datamodel.SynchronousCallMessage;
import kieker.tpan.datamodel.SynchronousReplyMessage;
import kieker.tpan.datamodel.repository.SystemModelRepository;
import kieker.tpan.datamodel.util.AllocationComponentOperationPair;
import kieker.tpan.plugins.util.IntContainer;
import kieker.tpan.plugins.util.dot.DotFactory;

/**
 * Plugin providing the creation of calling trees both for individual traces 
 * and an aggregated form mulitple traces.
 *
 * @author Andre van Hoorn
 */
public abstract class AbstractCallTreePlugin<T> extends AbstractMessageTraceProcessingPlugin {

    private static final Log log = LogFactory.getLog(AbstractCallTreePlugin.class);

    public AbstractCallTreePlugin(final String name, SystemModelRepository systemEntityFactory) {
        super(name, systemEntityFactory);
    }

    private static final String assemblyComponentOperationPairNodeLabel(
            final AbstractCallTreeNode<AssemblyComponentOperationPair> node, final boolean shortLabels) {
       AssemblyComponentOperationPair p = node.getEntity();
        AssemblyComponent component = p.getAssemblyComponent();
        Operation operation = p.getOperation();
        String assemblyComponentName = component.getName();
        String componentTypePackagePrefx = component.getType().getPackageName();
        String componentTypeIdentifier = component.getType().getTypeName();

        StringBuilder strBuild = new StringBuilder(assemblyComponentName).append(":");
        if (!shortLabels) {
            strBuild.append(componentTypePackagePrefx);
        } else {
            strBuild.append("..");
        }
        strBuild.append(componentTypeIdentifier).append("\\n.");

        Signature sig = operation.getSignature();
        StringBuilder opLabel = new StringBuilder(sig.getName());
        opLabel.append("(");
        String[] paramList = sig.getParamTypeList();
        if (paramList != null && paramList.length > 0) {
            opLabel.append("..");
        }
        opLabel.append(")");

        strBuild.append(opLabel.toString());
        return strBuild.toString();
    }

    private static final String allocationComponentOperationPairNodeLabel(
            final AbstractCallTreeNode<AllocationComponentOperationPair> node, final boolean shortLabels) {
        AllocationComponentOperationPair p = node.getEntity();
        AllocationComponent component = p.getAllocationComponent();
        Operation operation = p.getOperation();
        String resourceContainerName = component.getExecutionContainer().getName();
        String assemblyComponentName = component.getAssemblyComponent().getName();
        String componentTypePackagePrefx = component.getAssemblyComponent().getType().getPackageName();
        String componentTypeIdentifier = component.getAssemblyComponent().getType().getTypeName();

        StringBuilder strBuild = new StringBuilder(resourceContainerName).append("::\\n").append(assemblyComponentName).append(":");
        if (!shortLabels) {
            strBuild.append(componentTypePackagePrefx);
        } else {
            strBuild.append("..");
        }
        strBuild.append(componentTypeIdentifier).append("\\n.");

        Signature sig = operation.getSignature();
        StringBuilder opLabel = new StringBuilder(sig.getName());
        opLabel.append("(");
        String[] paramList = sig.getParamTypeList();
        if (paramList != null && paramList.length > 0) {
            opLabel.append("..");
        }
        opLabel.append(")");

        strBuild.append(opLabel.toString());
        
        return strBuild.toString();
    }

    protected static final String nodeLabel(
            final AbstractCallTreeNode node, final boolean shortLabels) {
        if (node.getEntity() instanceof AllocationComponentOperationPair) {
            return allocationComponentOperationPairNodeLabel((AbstractCallTreeNode<AllocationComponentOperationPair>)node, shortLabels);
        } else if (node.getEntity() instanceof AssemblyComponentOperationPair) {
            return assemblyComponentOperationPairNodeLabel((AbstractCallTreeNode<AssemblyComponentOperationPair>)node, shortLabels);
        } else {
            throw new UnsupportedOperationException("Node type not supported: " +
                    node.getEntity().getClass().getName());
        }
    }

    /** Traverse tree recursively and generate dot code for edges. */
    private static void dotEdgesFromSubTree(final SystemModelRepository systemEntityFactory,
            AbstractCallTreeNode<?> n,
            Hashtable<AbstractCallTreeNode<?>, Integer> nodeIds,
            IntContainer nextNodeId, PrintStream ps, final boolean shortLabels) {
        StringBuilder strBuild = new StringBuilder();
        nodeIds.put(n, nextNodeId.i);
        strBuild.append(nextNodeId.i++).append("[label =\"")
                .append(n.isRootNode()? "$" : nodeLabel(n, shortLabels))
                .append("\",shape=" + DotFactory.DOT_SHAPE_NONE + "];");
        ps.println(strBuild.toString());
        for (WeightedDirectedCallTreeEdge<?> child : n.getChildEdges()) {
            dotEdgesFromSubTree(systemEntityFactory, child.getDestination(), nodeIds, nextNodeId, ps, shortLabels);
        }
    }

    /** Traverse tree recursively and generate dot code for vertices. */
    private static void dotVerticesFromSubTree(final AbstractCallTreeNode<?> n,
            final IntContainer eoiCounter,
            final Hashtable<AbstractCallTreeNode<?>, Integer> nodeIds,
            final PrintStream ps, final boolean includeWeights) {
        int thisId = nodeIds.get(n);
        for (WeightedDirectedCallTreeEdge<?> child : n.getChildEdges()) {
            StringBuilder strBuild = new StringBuilder();
            int childId = nodeIds.get(child.getDestination());
            strBuild.append("\n").append(thisId).append("->").append(childId).append("[style=solid,arrowhead=none");
            if (includeWeights) {
                strBuild.append(",label=\"").append(child.getOutgoingWeight()).append("\"");
            } else if (eoiCounter != null){
                strBuild.append(",label=\"").append(eoiCounter.i++).append(".\"");
            }
            strBuild.append(" ]");
            ps.println(strBuild.toString());
            dotVerticesFromSubTree(child.getDestination(), eoiCounter, nodeIds, ps, includeWeights);
        }
    }

    private static void dotFromCallingTree(final SystemModelRepository systemEntityFactory,
            final AbstractCallTreeNode<?> root, final PrintStream ps,
            final boolean includeWeights,
            final boolean includeEois,
            final boolean shortLabels) {
        // preamble:
        ps.println("digraph G {");
        StringBuilder edgestringBuilder = new StringBuilder();

        Hashtable<AbstractCallTreeNode<?>, Integer> nodeIds = new Hashtable<AbstractCallTreeNode<?>, Integer>();

        dotEdgesFromSubTree(systemEntityFactory, root, nodeIds, new IntContainer(0), ps, shortLabels);
        dotVerticesFromSubTree(root, includeEois?new IntContainer(1):null, nodeIds, ps, includeWeights);

        ps.println(edgestringBuilder.toString());
        ps.println("}");
    }

    protected static void saveTreeToDotFile(final SystemModelRepository systemEntityFactory,
            final AbstractCallTreeNode<?> root, final String outputFnBase, final boolean includeWeights,
            final boolean includeEois, final boolean shortLabels) throws FileNotFoundException {
        PrintStream ps = new PrintStream(new FileOutputStream(outputFnBase + ".dot"));
        dotFromCallingTree(systemEntityFactory, root, ps, includeWeights, includeEois, shortLabels);
        ps.flush();
        ps.close();
    }

    protected static void addTraceToTree(final AbstractCallTreeNode<?> root,
            final MessageTrace t, final boolean aggregated)
            throws TraceProcessingException {
        Stack<AbstractCallTreeNode<?>> curStack = new Stack<AbstractCallTreeNode<?>>();

        Vector<Message> msgTraceVec = t.getSequenceAsVector();
        AbstractCallTreeNode curNode = root;
        curStack.push(curNode);
        for (final Message m : msgTraceVec) {
            if (m instanceof SynchronousCallMessage) {
                curNode = curStack.peek();
                AbstractCallTreeNode<?> child;
                child = curNode.newCall((SynchronousCallMessage) m);
                curNode = child;
                curStack.push(curNode);
            } else if (m instanceof SynchronousReplyMessage) {
                curNode = curStack.pop();
            } else {
                throw new TraceProcessingException("Message type not supported:" + m.getClass().getName());
            }
        }
        if (curStack.pop() != root) {
            log.fatal("Stack not empty after processing trace");
            throw new TraceProcessingException("Stack not empty after processing trace");
        }
    }

    public static void writeDotForMessageTrace(final SystemModelRepository systemEntityFactory,
            final AbstractCallTreeNode root,
            final MessageTrace msgTrace, final String outputFilename, final boolean includeWeights,
            final boolean shortLabels) throws FileNotFoundException, TraceProcessingException {
        addTraceToTree(root, msgTrace, false); // false: no aggregation
        saveTreeToDotFile(systemEntityFactory, root, outputFilename, includeWeights,
                true, shortLabels); // includeEois
    }
}
