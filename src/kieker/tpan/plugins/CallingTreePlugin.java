package kieker.tpan.plugins;

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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;
import kieker.tpan.datamodel.MessageTrace;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import kieker.tpan.datamodel.CallingTreeNode;
import kieker.tpan.datamodel.CallingTreeOperationHashKey;
import kieker.tpan.datamodel.Message;

/**
 * Plugin providing the creation of calling trees both for individual traces 
 * and an aggregated form mulitple traces.
 *
 * @author Andre van Hoorn
 */
public class CallingTreePlugin extends AbstractTpanMessageTraceProcessingComponent {

    private static final Log log = LogFactory.getLog(CallingTreePlugin.class);
    private final CallingTreeNode root =
            new CallingTreeNode(null, new CallingTreeOperationHashKey("$", "", ""));
    private final boolean considerHost;

    public CallingTreePlugin(final String name, final boolean considerHost) {
        super(name);
        this.considerHost = considerHost;
    }

    /** Traverse tree recursively and generate dot code for edges. */
    private static void dotEdgesFromSubTree(CallingTreeNode n,
            Hashtable<CallingTreeNode, Integer> nodeIds,
            IntContainer nextNodeId, PrintStream ps, final boolean considerHost) {
        StringBuilder strBuild = new StringBuilder();
        nodeIds.put(n, nextNodeId.i);
        strBuild.append(nextNodeId.i++).append("[label =\"").append((n.getParent() == null) ? "$" : n.getLabel(true, considerHost)).append("\",shape=oval];");
        ps.println(strBuild.toString());
        for (CallingTreeNode child : n.getChildren()) {
            dotEdgesFromSubTree(child, nodeIds, nextNodeId, ps, considerHost);
        }
    }

    /** Traverse tree recursively and generate dot code for vertices. */
    private static void dotVerticesFromSubTree(final CallingTreeNode n,
            final Hashtable<CallingTreeNode, Integer> nodeIds,
            final PrintStream ps, final boolean includeWeights) {
        int thisId = nodeIds.get(n);
        for (CallingTreeNode child : n.getChildren()) {
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

    private static void dotFromCallingTree(final CallingTreeNode root, final PrintStream ps, final boolean includeWeights, final boolean considerHost) {
        // preamble:
        ps.println("digraph G {");
        StringBuilder edgestringBuilder = new StringBuilder();

        Hashtable<CallingTreeNode, Integer> nodeIds = new Hashtable<CallingTreeNode, Integer>();

        dotEdgesFromSubTree(root, nodeIds, new IntContainer(0), ps, considerHost);
        dotVerticesFromSubTree(root, nodeIds, ps, includeWeights);

        ps.println(edgestringBuilder.toString());
        ps.println("}");
    }
    private int numGraphsSaved = 0;

    private static void saveTreeToDotFile(final CallingTreeNode root, final String outputFnBase, final boolean includeWeights, final boolean considerHost) throws FileNotFoundException {
        PrintStream ps = new PrintStream(new FileOutputStream(outputFnBase + ".dot"));
        dotFromCallingTree(root, ps, includeWeights, considerHost);
        ps.flush();
        ps.close();
    }

    public void saveTreeToDotFile(final String outputFnBase, final boolean includeWeights) throws FileNotFoundException {
        saveTreeToDotFile(this.root, outputFnBase, includeWeights, considerHost);
        this.numGraphsSaved++;
        this.printMessage(new String[]{
                    "Wrote calling tree to file '" + outputFnBase + ".dot" + "'",
                    "Dot file can be converted using the dot tool",
                    "Example: dot -T svg " + outputFnBase + ".dot" + " > " + outputFnBase + ".svg"
                });
    }

    private static void addTraceToTree(final CallingTreeNode root, final MessageTrace t) throws TraceProcessingException {
        Stack<CallingTreeNode> curStack = new Stack<CallingTreeNode>();

        Vector<Message> msgTraceVec = t.getSequenceAsVector();
        CallingTreeNode curNode = root;
        curStack.push(curNode);
        for (final Message m : msgTraceVec) {
            if (m.callMessage) {
                curNode = curStack.peek();
                CallingTreeNode child =
                        curNode.getChildForName(m.receiver.componentName,
                        m.receiver.opname, m.receiver.vmName);
                curNode = child;
                curStack.push(curNode);
            } else {
                curNode = curStack.pop();
            }
        }
        if (curStack.pop() != root) {
            log.fatal("Stack not empty after processing trace");
            throw new TraceProcessingException("Stack not empty after processing trace");
        }
    }

    public void newTrace(final MessageTrace t) throws TraceProcessingException {
        try {
            addTraceToTree(root, t);
            this.reportSuccess(t.getTraceId());
        } catch (TraceProcessingException ex) {
            log.error("TraceProcessingException", ex);
            this.reportError(t.getTraceId());
        }
    }

    public static void writeDotForMessageTrace(final MessageTrace msgTrace, final String outputFilename, final boolean includeWeights, final boolean considerHost) throws FileNotFoundException {
        final CallingTreeNode root = new CallingTreeNode(null, new CallingTreeOperationHashKey("$", "", ""));
        saveTreeToDotFile(root, outputFilename, includeWeights, considerHost);
    }

    @Override
    public void printStatusMessage() {
        super.printStatusMessage();
        System.out.println("Saved " + this.numGraphsSaved + " calling tree" + (this.numGraphsSaved > 1 ? "s" : ""));
    }

    @Override
    public void cleanup() {
        // no cleanup required
    }
}

class IntContainer {

    public int i = 0;

    public IntContainer(int initVal) {
        this.i = initVal;
    }
}
