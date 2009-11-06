package kieker.tpan.plugins;

/*
 * kieker.loganalysis.plugins.SequenceDiagramPlugin.java
 *
 * ==================LICENCE=========================
 * Copyright 2006-2009 Kieker Project
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
import java.util.HashMap;
import java.util.Vector;
import kieker.tpan.datamodel.Message;
import kieker.tpan.datamodel.MessageTrace;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Refactored copy from LogAnalysis-legacy tool
 * 
 * @author Nils Sommer, Andre van Hoorn
 */
public class SequenceDiagramPlugin {

    private static final Log log = LogFactory.getLog(SequenceDiagramPlugin.class);

    private SequenceDiagramPlugin() {
    }

    private static void picFromMessageTrace(MessageTrace messageTrace,  PrintStream ps) {
        HashMap<String, String> distinctObjects = new HashMap<String, String>();
        int nextObjIndex = 0;
        Vector<Message> messages = messageTrace.getSequenceAsVector();
        //preamble:
        ps.println(".PS");
        ps.println("copy \"lib/sequence.pic\";");
        ps.println("boxwid = 1.1;");
        ps.println("movewid = 0.5;");

        // get distinct objects. should be enough to check all senders,
        // as returns have senders too.
        //log.info("Trace " + messageTrace.traceId + " contains " + messages.size() + " messages.");
        for (Message me : messages) {
            String name = me.getSenderComponentName();
            if (!distinctObjects.containsKey(name)) {
                distinctObjects.put(name, "O"+(nextObjIndex++));
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
                ps.println("object(" + distinctObjects.get(name) +
                        ",\"" + shortComponentName + "\");");
            }
        }
        ps.println("step()");
        ps.println("active(O0);");
        ps.println("step();");
        boolean first = true;
        for (Message me : messages) {
            if (me.callMessage) {
                //String method = me.getReceiver().getOperation().getMethodname();
                String method = me.receiver.opname;
                if (method.indexOf('(') != -1) {
                    method = me.receiver.opname;
                }
                ps.println("step();");
                if (first == true) {
                    ps.println("async();");
                    first = false;
                } else {
                    ps.println("sync();");
                }
                ps.println("message(" + distinctObjects.get(me.getSenderComponentName()) +
                        "," + distinctObjects.get(me.getReceiverComponentName()) +
                        ", \"" + method +
                        "\");");
                ps.println("active(" + distinctObjects.get(me.getReceiverComponentName()) + ");");
                ps.println("step();");
            } else {
                ps.println("step();");
                ps.println("async();");
                ps.println("rmessage(" + distinctObjects.get(me.getSenderComponentName()) +
                        "," + distinctObjects.get(me.getReceiverComponentName()) +
                        ", \"\");");
                ps.println("inactive(" + distinctObjects.get(me.getSenderComponentName()) + ");");
            }
        }
        ps.println("inactive(O0);");
        ps.println("step();");

        for (Object objs : distinctObjects.values()) {
            ps.println("complete(" + objs + ");");
        }
        ps.println("complete(O0);");

        ps.println(".PE");
    }

    public static void writeDotForMessageTrace(MessageTrace msgTrace, String outputFilename) throws FileNotFoundException {
        PrintStream ps = new PrintStream(new FileOutputStream(outputFilename));
        picFromMessageTrace(msgTrace, ps);
        ps.flush();
        ps.close();
    }
}