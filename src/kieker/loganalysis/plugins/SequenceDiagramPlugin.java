package kieker.loganalysis.plugins;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;
import kieker.loganalysis.datamodel.Message;
import kieker.loganalysis.datamodel.MessageSequence;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Refactored copy from LogAnalysis-legacy tool
 * 
 * @author Matthias Rohr, Andre van Hoorn
 */
public class SequenceDiagramPlugin {
    private static final Log log = LogFactory.getLog(SequenceDiagramPlugin.class);
    
    HashMap<String, String> distinctObjects = null;
    int currentObjIndex = 0;
    PrintStream pr = System.out;

    public SequenceDiagramPlugin() {
    }

    /**
     * Use this constructor in case you do not want to use system.out
     * for output
     * @param printStream
     */
    public SequenceDiagramPlugin(PrintStream printStream) {
        this.pr = printStream;
    }

    public void processMessageTraces(Collection<MessageSequence> messageTraces) {
        for (MessageSequence mt : messageTraces) {
            distinctObjects = new HashMap<String, String>();
            currentObjIndex = 0;
            processMessageTrace(mt);
        }
    }

    public void picFromMessageTrace(MessageSequence messageTrace) {
        Vector<Message> messages = messageTrace.getSequenceAsVector();
        //preamble:
        pr.println(".PS");
        pr.println("copy \"sequence.pic\";");
        pr.println("boxwid = 1.1;");
        pr.println("movewid = 0.5;");


        // get distinct objects. should be enough to check all senders,
        // as returns have senders too.
        for (Message me : messages) {
            String name = me.sender;
            if (addToDistinctObjects(name)) {
                String shortComponentName = name;
                if (shortComponentName.indexOf('.') != -1) {
                    int index = 0;
                    for (index = shortComponentName.length() - 1; index > 0; index--) {
                        if (shortComponentName.charAt(index) == '.') {
                            break;
                        }
                    }
                    shortComponentName = shortComponentName.substring(index + 1);
                }
                pr.println("object(" + distinctObjects.get(name) +
                        ",\"" + shortComponentName + "\");");
            }
        }
        pr.println("step()");
        pr.println("active(O0);");
        pr.println("step();");
        boolean first = true;
        for (Message me : messages) {
            if (me.callMessage) {
                //String method = me.getReceiver().getOperation().getMethodname();
                String method = me.receiver;
                if (method.indexOf('(') != -1) {
                    method = me.execution.opname;
                }
                pr.println("step();");
                if (first == true) {
                    pr.println("async();");
                    first = false;
                } else {
                    pr.println("sync();");
                }
                pr.println("message(" + distinctObjects.get(me.sender) +
                        "," + distinctObjects.get(me.receiver) +
                        ", \"" + method +
                        "\");");
                pr.println("active(" + distinctObjects.get(me.receiver) + ");");
                pr.println("step();");
            } else {
                pr.println("step();");
                pr.println("async();");
                pr.println("rmessage(" + distinctObjects.get(me.sender) +
                        "," + distinctObjects.get(me.receiver) +
                        ", \"\");");
                pr.println("inactive(" + distinctObjects.get(me.sender) + ");");
            }
        }
        pr.println("inactive(O0);");
        pr.println("step();");

        for (Object objs : distinctObjects.values()) {
            pr.println("complete(" + objs + ");");
        }
        pr.println("complete(O0);");

        pr.println(".PE");
    }
    /*
     * for (Message me: messages) {          
    if (!first) System.out.print(",");
    if   (me.isCall()) System.out.print("(CALL, "+me.getSender().getOperation().getName()+""+me.getSender().getExecutionId()+", "+me.getReciever().getOperation().getName()+""+me.getReciever().getExecutionId()+")");
    else               System.out.print("(RETURN, "+me.getSender().getOperation().getName()+""+me.getSender().getExecutionId()+", "+me.getReciever().getOperation().getName()+""+me.getReciever().getExecutionId()+")");
    first = false;
    }
     */

    private boolean addToDistinctObjects(String senderOperation) {
        if (distinctObjects.containsKey(senderOperation)) {
            return false;
        } else {
            distinctObjects.put(senderOperation, "O" + currentObjIndex);
        }
        currentObjIndex++;
        return true;

    }

    public void processMessageTrace(MessageSequence msgTrace) {
            distinctObjects = new HashMap<String, String>();
            currentObjIndex = 0;
            String fileName = "/tmp/seqDia" + msgTrace.traceId + ".pic";
            try {
                pr = new PrintStream(new FileOutputStream(fileName));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            picFromMessageTrace(msgTrace);
            pr.flush();
            pr.close();
            System.out.println("wrote output to " + fileName);
    }
}
