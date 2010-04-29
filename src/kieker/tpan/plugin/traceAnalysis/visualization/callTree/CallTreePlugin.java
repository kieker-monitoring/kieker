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
import kieker.tpan.plugin.traceAnalysis.traceReconstruction.TraceProcessingException;
import kieker.tpan.plugin.traceAnalysis.traceReconstruction.AbstractMessageTraceProcessingPlugin;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import kieker.tpan.datamodel.AllocationComponent;
import kieker.tpan.datamodel.Message;
import kieker.tpan.datamodel.MessageTrace;
import kieker.tpan.datamodel.Operation;
import kieker.tpan.datamodel.Signature;
import kieker.tpan.datamodel.SynchronousCallMessage;
import kieker.tpan.datamodel.SynchronousReplyMessage;
import kieker.tpan.datamodel.factories.SystemEntityFactory;
import kieker.tpan.plugins.util.IntContainer;
import kieker.tpan.plugins.util.dot.DotFactory;

/**
 * Plugin providing the creation of calling trees both for individual traces 
 * and an aggregated form mulitple traces.
 *
 * @author Andre van Hoorn
 */
public class CallTreePlugin extends AbstractMessageTraceProcessingPlugin {

    private static final Log log = LogFactory.getLog(CallTreePlugin.class);
    private final CallTreeNode root;
    private final boolean aggregated;
    private final SystemEntityFactory systemEntityFactory;

    public CallTreePlugin(final String name, SystemEntityFactory systemEntityFactory,
            final boolean aggregated) {
        super(name, systemEntityFactory);
        this.systemEntityFactory = systemEntityFactory;
        root = new CallTreeNode(null,
                new CallTreeOperationHashKey(this.systemEntityFactory.getAllocationFactory().rootAllocationComponent,
                this.systemEntityFactory.getOperationFactory().rootOperation));
        this.aggregated = aggregated;
    }

    private static final String nodeLabel(final CallTreeNode node, final boolean shortLabels) {
        if (node.isRootNode()) {
            return "$";
        }

        AllocationComponent component = node.getAllocationComponent();
        Operation operation = node.getOperation();
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

    /** Traverse tree recursively and generate dot code for edges. */
    private static void dotEdgesFromSubTree(final SystemEntityFactory systemEntityFactory,
            CallTreeNode n,
            Hashtable<CallTreeNode, Integer> nodeIds,
            IntContainer nextNodeId, PrintStream ps, final boolean shortLabels) {
        StringBuilder strBuild = new StringBuilder();
        nodeIds.put(n, nextNodeId.i);
        strBuild.append(nextNodeId.i++).append("[label =\"").append(nodeLabel(n, shortLabels)).append("\",shape=" + DotFactory.DOT_SHAPE_NONE
                + ",style=" + DotFactory.DOT_STYLE_FILLED
                + ",fillcolor=" + DotFactory.DOT_FILLCOLOR_WHITE + "];");
        ps.println(strBuild.toString());
        for (CallTreeNode child : n.getChildren()) {
            dotEdgesFromSubTree(systemEntityFactory, child, nodeIds, nextNodeId, ps, shortLabels);
        }
    }

    /** Traverse tree recursively and generate dot code for vertices. */
    private static void dotVerticesFromSubTree(final CallTreeNode n,
            final Hashtable<CallTreeNode, Integer> nodeIds,
            final PrintStream ps, final boolean includeWeights) {
        int thisId = nodeIds.get(n);
        for (CallTreeNode child : n.getChildren()) {
            StringBuilder strBuild = new StringBuilder();
            int childId = nodeIds.get(child);
            strBuild.append("\n").append(thisId).append("->").append(childId).append("[style=solid,arrowhead=none");
            if (includeWeights) {
                strBuild.append(",label = ").append("").append(", weight =").append("");
            }
            strBuild.append(" ]");
            ps.println(strBuild.toString());
            dotVerticesFromSubTree(child, nodeIds, ps, includeWeights);
        }
    }

    private static void dotFromCallingTree(final SystemEntityFactory systemEntityFactory,
            final CallTreeNode root, final PrintStream ps,
            final boolean includeWeights, final boolean shortLabels) {
        // preamble:
        ps.println("digraph G {");
        StringBuilder edgestringBuilder = new StringBuilder();

        Hashtable<CallTreeNode, Integer> nodeIds = new Hashtable<CallTreeNode, Integer>();

        dotEdgesFromSubTree(systemEntityFactory, root, nodeIds, new IntContainer(0), ps, shortLabels);
        dotVerticesFromSubTree(root, nodeIds, ps, includeWeights);

        ps.println(edgestringBuilder.toString());
        ps.println("}");
    }
    private int numGraphsSaved = 0;

    private static void saveTreeToDotFile(final SystemEntityFactory systemEntityFactory,
            final CallTreeNode root, final String outputFnBase, final boolean includeWeights,
            final boolean shortLabels) throws FileNotFoundException {
        PrintStream ps = new PrintStream(new FileOutputStream(outputFnBase + ".dot"));
        dotFromCallingTree(systemEntityFactory, root, ps, includeWeights, shortLabels);
        ps.flush();
        ps.close();
    }

    public void saveTreeToDotFile(final String outputFnBase, final boolean includeWeights,
            final boolean shortLabels) throws FileNotFoundException {
        saveTreeToDotFile(this.systemEntityFactory, this.root, outputFnBase, includeWeights, shortLabels);
        this.numGraphsSaved++;
        this.printMessage(new String[]{
                    "Wrote call tree to file '" + outputFnBase + ".dot" + "'",
                    "Dot file can be converted using the dot tool",
                    "Example: dot -T svg " + outputFnBase + ".dot" + " > " + outputFnBase + ".svg"
                });
    }

    private static void addTraceToTree(final CallTreeNode root,
            final MessageTrace t, final boolean aggregated)
            throws TraceProcessingException {
        Stack<CallTreeNode> curStack = new Stack<CallTreeNode>();

        Vector<Message> msgTraceVec = t.getSequenceAsVector();
        CallTreeNode curNode = root;
        curStack.push(curNode);
        for (final Message m : msgTraceVec) {
            if (m instanceof SynchronousCallMessage) {
                curNode = curStack.peek();
                CallTreeNode child;
                if (aggregated) {
                    child = curNode.getChild(m.getReceivingExecution().getAllocationComponent(),
                            m.getReceivingExecution().getOperation());
                } else {
                    child = curNode.createNewChild(m.getReceivingExecution().getAllocationComponent(),
                            m.getReceivingExecution().getOperation());
                }
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

    public void newEvent(MessageTrace t) {
        try {
            addTraceToTree(root, t, this.aggregated);
            this.reportSuccess(t.getTraceId());
        } catch (TraceProcessingException ex) {
            log.error("TraceProcessingException", ex);
            this.reportError(t.getTraceId());
        }
    }

    public static void writeDotForMessageTrace(final SystemEntityFactory systemEntityFactory,
            final MessageTrace msgTrace, final String outputFilename, final boolean includeWeights,
            final boolean shortLabels) throws FileNotFoundException, TraceProcessingException {
        final CallTreeNode root = new CallTreeNode(null,
                new CallTreeOperationHashKey(systemEntityFactory.getAllocationFactory().rootAllocationComponent,
                systemEntityFactory.getOperationFactory().rootOperation));
        addTraceToTree(root, msgTrace, false); // false: no aggregation
        saveTreeToDotFile(systemEntityFactory, root, outputFilename, includeWeights, shortLabels);
    }

    @Override
    public void printStatusMessage() {
        super.printStatusMessage();
        System.out.println("Saved " + this.numGraphsSaved + " call tree" + (this.numGraphsSaved > 1 ? "s" : ""));
    }

    public boolean execute() {
        return true; // no need to do anything here
    }

    public void terminate(boolean error) {
        // no need to do anything here
    }
}
