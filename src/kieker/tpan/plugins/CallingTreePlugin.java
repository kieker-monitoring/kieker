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

    int i = 0;

    /** Traverse tree recursively and generate dot code for edges. */
    private void dotEdgesFromSubTree(CallingTreeNode n,
            Hashtable<CallingTreeNode, Integer> nodeIds,
            Integer nextNodeId, PrintStream ps) {
        StringBuilder strBuild = new StringBuilder();
        nodeIds.put(n, i);
        strBuild.append(i++).append("[label =\"")
                .append((n==root)?"$":n.getLabel(true, considerHost)).append("\",shape=oval];");
        ps.println(strBuild.toString());
        for (CallingTreeNode child : n.getChildren()) {
            dotEdgesFromSubTree(child, nodeIds, nextNodeId, ps);
        }
    }

    /** Traverse tree recursively and generate dot code for vertices. */
    private void dotVerticesFromSubTree(CallingTreeNode n,
            Hashtable<CallingTreeNode, Integer> nodeIds,
            PrintStream ps, boolean includeWeights) {
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

    private void dotFromCallingTree(PrintStream ps, final boolean includeWeights) {
        // preamble:
        ps.println("digraph G {");
        StringBuilder edgestringBuilder = new StringBuilder();

        Hashtable<CallingTreeNode, Integer> nodeIds = new Hashtable<CallingTreeNode, Integer>();

        dotEdgesFromSubTree(root, nodeIds, new Integer(0), ps);
        dotVerticesFromSubTree(root, nodeIds, ps, includeWeights);

        ps.println(edgestringBuilder.toString());
        ps.println("}");
    }
    private int numGraphsSaved = 0;

    public void saveToDotFile(final String outputFnBase, final boolean includeWeights) throws FileNotFoundException {
        PrintStream ps = new PrintStream(new FileOutputStream(outputFnBase + ".dot"));
        this.dotFromCallingTree(ps, includeWeights);
        ps.flush();
        ps.close();
        this.numGraphsSaved++;
        this.printMessage(new String[]{
                    "Wrote calling tree to file '" + outputFnBase + ".dot" + "'",
                    "Dot file can be converted using the dot tool",
                    "Example: dot -T svg " + outputFnBase + ".dot" + " > " + outputFnBase + ".svg"
                });
    }

    public void newTrace(MessageTrace t) throws TraceProcessingException {
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
            this.reportError(t.getTraceId());
            throw new TraceProcessingException("Stack not empty after processing trace");
        }
        this.reportSuccess(t.getTraceId());
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
