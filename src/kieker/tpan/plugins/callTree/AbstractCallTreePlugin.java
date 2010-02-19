package kieker.tpan.plugins.callTree;

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
import kieker.tpan.plugins.traceReconstruction.TraceProcessingException;
import kieker.tpan.plugins.traceReconstruction.AbstractTpanMessageTraceProcessingComponent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import kieker.tpan.datamodel.AllocationComponentInstance;
import kieker.tpan.datamodel.Message;
import kieker.tpan.datamodel.MessageTrace;
import kieker.tpan.datamodel.Operation;
import kieker.tpan.datamodel.SynchronousCallMessage;
import kieker.tpan.datamodel.SynchronousReplyMessage;
import kieker.tpan.datamodel.factories.SystemEntityFactory;
import kieker.tpan.plugins.dependencyGraph.AllocationComponentOperationPair;
import kieker.tpan.plugins.util.dot.DotFactory;

/**
 * Plugin providing the creation of calling trees both for individual traces 
 * and an aggregated form mulitple traces.
 *
 * @author Andre van Hoorn
 */
public abstract class AbstractCallTreePlugin<T> extends AbstractTpanMessageTraceProcessingComponent {

    private static final Log log = LogFactory.getLog(AbstractCallTreePlugin.class);

    public AbstractCallTreePlugin(final String name, SystemEntityFactory systemEntityFactory,
            final boolean aggregated) {
        super(name, systemEntityFactory);
    }

    private static final String allocationComponentOperationPairNodeLabel(
            final AbstractCallTreeNode<AllocationComponentOperationPair> node, final boolean shortLabels) {
        AllocationComponentOperationPair p =
                (AllocationComponentOperationPair) node.getEntity();
        AllocationComponentInstance component = p.getAllocationComponent();
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
        strBuild.append(componentTypeIdentifier).append(".");
        strBuild.append(operation.getSignature().getName());
        return strBuild.toString();
    }

    protected static final String nodeLabel(
            final AbstractCallTreeNode<?> node, final boolean shortLabels) {

        if (node instanceof AbstractCallTreeNode<AllocationComponentOperationPair>){
            
        }
    }

    /** Traverse tree recursively and generate dot code for edges. */
    private static void dotEdgesFromSubTree(final SystemEntityFactory systemEntityFactory,
            AbstractCallTreeNode<?> n,
            Hashtable<AbstractCallTreeNode<?>, Integer> nodeIds,
            IntContainer nextNodeId, PrintStream ps, final boolean shortLabels) {
        StringBuilder strBuild = new StringBuilder();
        nodeIds.put(n, nextNodeId.i);
        strBuild.append(nextNodeId.i++).append("[label =\"").append(allocationComponentOperationPairNodeLabel(n, shortLabels)).append("\",shape=" + DotFactory.DOT_SHAPE_NONE + "];");
        ps.println(strBuild.toString());
        for (WeightedDirectedCallTreeEdge<?> child : n.getChildEdges()) {
            dotEdgesFromSubTree(systemEntityFactory, child.getDestination(), nodeIds, nextNodeId, ps, shortLabels);
        }
    }

    /** Traverse tree recursively and generate dot code for vertices. */
    private static void dotVerticesFromSubTree(final AbstractCallTreeNode<?> n,
            final Hashtable<AbstractCallTreeNode<?>, Integer> nodeIds,
            final PrintStream ps, final boolean includeWeights) {
        int thisId = nodeIds.get(n);
        for (WeightedDirectedCallTreeEdge<?> child : n.getChildEdges()) {
            StringBuilder strBuild = new StringBuilder();
            int childId = nodeIds.get(child.getDestination());
            strBuild.append("\n").append(thisId).append("->").append(childId).append("[style=solid,arrowhead=none");
            if (includeWeights) {
                strBuild.append(",label = ").append("").append(", weight =").append("");
            }
            strBuild.append(" ]");
            ps.println(strBuild.toString());
            dotVerticesFromSubTree(child.getDestination(), nodeIds, ps, includeWeights);
        }
    }

    private static void dotFromCallingTree(final SystemEntityFactory systemEntityFactory,
            final AbstractCallTreeNode<?> root, final PrintStream ps,
            final boolean includeWeights, final boolean shortLabels) {
        // preamble:
        ps.println("digraph G {");
        StringBuilder edgestringBuilder = new StringBuilder();

        Hashtable<AbstractCallTreeNode<?>, Integer> nodeIds = new Hashtable<AbstractCallTreeNode<?>, Integer>();

        dotEdgesFromSubTree(systemEntityFactory, root, nodeIds, new IntContainer(0), ps, shortLabels);
        dotVerticesFromSubTree(root, nodeIds, ps, includeWeights);

        ps.println(edgestringBuilder.toString());
        ps.println("}");
    }

    protected static void saveTreeToDotFile(final SystemEntityFactory systemEntityFactory,
            final AbstractCallTreeNode<?> root, final String outputFnBase, final boolean includeWeights,
            final boolean shortLabels) throws FileNotFoundException {
        PrintStream ps = new PrintStream(new FileOutputStream(outputFnBase + ".dot"));
        dotFromCallingTree(systemEntityFactory, root, ps, includeWeights, shortLabels);
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

    public static void writeDotForMessageTrace(final SystemEntityFactory systemEntityFactory,
            final AbstractCallTreeNode root,
            final MessageTrace msgTrace, final String outputFilename, final boolean includeWeights,
            final boolean shortLabels) throws FileNotFoundException, TraceProcessingException {
        addTraceToTree(root, msgTrace, false); // false: no aggregation
        saveTreeToDotFile(systemEntityFactory, root, outputFilename, includeWeights, shortLabels);
    }
}

class IntContainer {

    public int i = 0;

    public IntContainer(int initVal) {
        this.i = initVal;
    }
}
