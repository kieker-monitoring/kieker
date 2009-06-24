package kieker.loganalysis.plugins;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import kieker.loganalysis.datamodel.MessageSequence;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import kieker.loganalysis.datamodel.AdjacencyMatrix;
import kieker.loganalysis.datamodel.ExecutionSequence;
import kieker.loganalysis.datamodel.Message;

/**
 * Refactored copy from LogAnalysis-legacy tool
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr,
 */
public class DependencyGraphPlugin {

    private static final Log log = LogFactory.getLog(DependencyGraphPlugin.class);
    HashMap<String, String> distinctObjects = new HashMap<String, String>();
    int currentObjIndex = 0;
    PrintStream pr = System.out;
    AdjacencyMatrix adjMatrix = new AdjacencyMatrix();

    public DependencyGraphPlugin() {
    }

    /**
     * Use this constructor in case you do not want to use system.out
     * for output
     * @param printStream
     */
    public DependencyGraphPlugin(PrintStream printStream) {
        this.pr = printStream;
    }

    private void dotFromAdjacencyMatrix() {
        // preamble:
        pr.println("digraph G {");
        StringBuilder edgestringBuilder = new StringBuilder();
        long[][] matrix = this.adjMatrix.getMatrixAsArray();
        String[] componentNames = this.adjMatrix.getComponentNames();
        for (int i = 0; i < matrix.length; i++) {
            edgestringBuilder.append("\n").append(i).append("[label =\"").append(componentNames[i]).append("\",shape=box];");
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int k = 0; k < matrix[i].length; k++) {
                if (matrix[i][k] > 0) {
                    edgestringBuilder.append("\n").append(i).append("->").append(k).append("[label = ").append(matrix[i][k]).append(",style=dashed,arrowhead=open,").append("weight =").append(matrix[i][k]).append(" ]");
                }
            }
        }
        pr.println(edgestringBuilder.toString());
        pr.println("}");
    }

    public void processExecutionTraces(Collection<ExecutionSequence> eTraces) {
        distinctObjects = new HashMap<String, String>();
        currentObjIndex = 0;
        String fileName = "/tmp/dependencyGraph.dot";
        try {
            pr = new PrintStream(new FileOutputStream(fileName));
        } catch (FileNotFoundException e) {
            log.error("File not found.", e);
        }
        for (ExecutionSequence eTrace : eTraces) {
            MessageSequence msgTrace = eTrace.toMessageSequence();
            for (Message m : msgTrace.getSequenceAsVector()) {
                if (!m.callMessage) {
                    continue;
                }
                log.info("Adding dependency: (" + m.getSenderComponentName() + "," + m.getReceiverComponentName() + "" + ")");
                this.adjMatrix.addDependency(m.getSenderComponentName(), m.getReceiverComponentName());
            }
        }
        this.dotFromAdjacencyMatrix();
        pr.flush();
        pr.close();
        System.out.println("wrote output to " + fileName);
        System.out.println("Dot file can be converted using dot tool");
        System.out.println("Command: dot -T [svg|ps|...]" + fileName);
    }

    public void processMessageTraces(Collection<MessageSequence> msgTraces) {
        distinctObjects = new HashMap<String, String>();
        currentObjIndex = 0;
        String fileName = "/tmp/dependencyGraph.dot";
        try {
            pr = new PrintStream(new FileOutputStream(fileName));
        } catch (FileNotFoundException e) {
            log.error("File not found.", e);
        }
        for (MessageSequence msgTrace : msgTraces) {
            for (Message m : msgTrace.getSequenceAsVector()) {
                if (!m.callMessage) {
                    continue;
                }
                log.info("Adding dependency: (" + m.getSenderComponentName() + "," + m.getReceiverComponentName() + "" + ")");
                this.adjMatrix.addDependency(m.sender, m.receiver);
            }
        }
        this.dotFromAdjacencyMatrix();
        pr.flush();
        pr.close();
        System.out.println("wrote output to " + fileName);
        System.out.println("Dot file can be converted using dot tool");
        System.out.println("Command: dot -T [svg|ps|...]" + fileName);
    }
}
