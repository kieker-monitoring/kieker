package kieker.tpan.plugins.sequenceDiagram;

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
import java.util.TreeSet;
import java.util.Vector;
import kieker.tpan.datamodel.system.AllocationComponentInstance;
import kieker.tpan.datamodel.system.Message;
import kieker.tpan.datamodel.system.MessageTrace;
import kieker.tpan.datamodel.system.SynchronousCallMessage;
import kieker.tpan.datamodel.system.SynchronousReplyMessage;
import kieker.tpan.datamodel.system.factories.SystemEntityFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Refactored copy from LogAnalysis-legacy tool
 * 
 * @author Andre van Hoorn, Nils Sommer
 */
public class SequenceDiagramPlugin {

    private static final Log log = LogFactory.getLog(SequenceDiagramPlugin.class);

    private SequenceDiagramPlugin() {
    }

    private static String componentLabel(final SystemEntityFactory systemEntityFactory,
            final AllocationComponentInstance component, final boolean shortLabels) {
        if (component == systemEntityFactory.getAllocationFactory().rootAllocationComponent) {
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

    private static void picFromMessageTrace(final SystemEntityFactory systemEntityFactory,
            final MessageTrace messageTrace, final PrintStream ps, final boolean considerHost,
            final boolean shortLabels) {
        // dot node ID x component instance
        Vector<Message> messages = messageTrace.getSequenceAsVector();
        //preamble:
        ps.println(".PS");
        ps.println("copy \"lib/sequence.pic\";");
        ps.println("boxwid = 1.1;");
        ps.println("movewid = 0.5;");

        TreeSet<Integer> plottedComponentIds = new TreeSet<Integer>();

        final AllocationComponentInstance rootAllocationComponent = systemEntityFactory.getAllocationFactory().rootAllocationComponent;
        final String rootDotId = "O" + rootAllocationComponent.getId();
        ps.println("object(O" + rootAllocationComponent.getId()
                + ",\"" + componentLabel(systemEntityFactory, rootAllocationComponent, shortLabels) + "\");");
        plottedComponentIds.add(rootAllocationComponent.getId());
        for (Message me : messages) {
            AllocationComponentInstance senderComponent = me.getSendingExecution().getAllocationComponent();
            AllocationComponentInstance receiverComponent = me.getReceivingExecution().getAllocationComponent();
            if (!plottedComponentIds.contains(senderComponent.getId())) {
                ps.println("object(O" + senderComponent.getId()
                        + ",\"" + componentLabel(systemEntityFactory, senderComponent, shortLabels) + "\");");
                plottedComponentIds.add(senderComponent.getId());
            }
            if (!plottedComponentIds.contains(receiverComponent.getId())) {
                ps.println("object(O" + receiverComponent.getId()
                        + ",\"" + componentLabel(systemEntityFactory, receiverComponent, shortLabels) + "\");");
                plottedComponentIds.add(receiverComponent.getId());
            }
        }
        ps.println("step()");
        ps.println("active(" + rootDotId + ");");
        ps.println("step();");
        boolean first = true;
        for (Message me : messages) {
            AllocationComponentInstance senderComponent = me.getSendingExecution().getAllocationComponent();
            AllocationComponentInstance receiverComponent = me.getReceivingExecution().getAllocationComponent();
            String senderDotId = "O" + senderComponent.getId();
            String receiverDotId = "O" + receiverComponent.getId();
            if (me instanceof SynchronousCallMessage) {
                String method = me.getReceivingExecution().getOperation().getSignature().getName();
                //if (method.indexOf('(') != -1) {
                //    method = me.receiver.opname;
                //}
                ps.println("step();");
                if (first == true) {
                    ps.println("async();");
                    first = false;
                } else {
                    ps.println("sync();");
                }
                ps.println("message(" + senderDotId
                        + "," + receiverDotId
                        + ", \"" + method
                        + "\");");
                ps.println("active(" + receiverDotId + ");");
                ps.println("step();");
            } else if (me instanceof SynchronousReplyMessage) {
                ps.println("step();");
                ps.println("async();");
                ps.println("rmessage(" + senderDotId
                        + "," + receiverDotId
                        + ", \"\");");
                ps.println("inactive(" + senderDotId + ");");
            } else {
                log.error("Message type not supported: " + me.getClass().getName());
            }
        }
        ps.println("inactive(" + rootDotId + ");");
        ps.println("step();");

        for (int i : plottedComponentIds) {
            ps.println("complete(O" + i + ");");
        }
        ps.println("complete(" + rootDotId + ");");

        ps.println(".PE");
    }

    public static void writePicForMessageTrace(final SystemEntityFactory systemEntityFactory,
            MessageTrace msgTrace, String outputFilename, final boolean considerHost, final boolean shortLabels) throws FileNotFoundException {
        PrintStream ps = new PrintStream(new FileOutputStream(outputFilename));
        picFromMessageTrace(systemEntityFactory, msgTrace, ps, considerHost, shortLabels);
        ps.flush();
        ps.close();
    }
}
