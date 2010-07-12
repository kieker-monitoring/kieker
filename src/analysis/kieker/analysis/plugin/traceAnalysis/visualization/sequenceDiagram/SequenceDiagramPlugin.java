package kieker.analysis.plugin.traceAnalysis.visualization.sequenceDiagram;

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
import kieker.analysis.datamodel.AllocationComponent;
import kieker.analysis.datamodel.Message;
import kieker.analysis.datamodel.MessageTrace;
import kieker.analysis.datamodel.Signature;
import kieker.analysis.datamodel.SynchronousCallMessage;
import kieker.analysis.datamodel.SynchronousReplyMessage;
import kieker.analysis.datamodel.repository.SystemModelRepository;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.IInputPort;
import kieker.analysis.plugin.traceAnalysis.AbstractMessageTraceProcessingPlugin;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Refactored copy from LogAnalysis-legacy tool
 * 
 * @author Andre van Hoorn, Nils Sommer
 */
public class SequenceDiagramPlugin extends AbstractMessageTraceProcessingPlugin {

    private static final Log log = LogFactory.getLog(SequenceDiagramPlugin.class);
    private final String outputFnBase;
    private final boolean shortLabels;

    public SequenceDiagramPlugin(final String name, final SystemModelRepository systemEntityFactory,
            final String outputFnBase, final boolean shortLabels) {
        super(name, systemEntityFactory);
        this.outputFnBase = outputFnBase;
        this.shortLabels = shortLabels;
    }

    @Override
    public void printStatusMessage() {
        super.printStatusMessage();
        final int numPlots = this.getSuccessCount();
        final long lastSuccessTracesId = this.getLastTraceIdSuccess();
        System.out.println("Wrote " + numPlots + " sequence diagram"
                + (numPlots > 1 ? "s" : "") + " to file"
                + (numPlots > 1 ? "s" : "") + " with name pattern '"
                + outputFnBase + "-<traceId>.pic'");
        System.out.println("Pic files can be converted using the pic2plot tool (package plotutils)");
        System.out.println("Example: pic2plot -T svg " + outputFnBase
                + "-"
                + ((numPlots > 0) ? lastSuccessTracesId : "<traceId>")
                + ".pic > " + outputFnBase + "-"
                + ((numPlots > 0) ? lastSuccessTracesId : "<traceId>")
                + ".svg");
    }

    @Override
    public boolean execute() {
        return true; // no need to do anything here
    }

    @Override
    public void terminate(final boolean error) {
        // no need to do anything here
    }

    @Override
    public IInputPort<MessageTrace> getMessageTraceInputPort() {
        return this.messageTraceInputPort;
    }
    private final IInputPort<MessageTrace> messageTraceInputPort =
            new AbstractInputPort<MessageTrace>("Message traces") {

                @Override
                public void newEvent(MessageTrace mt) {
                    try {
                        SequenceDiagramPlugin.writePicForMessageTrace(getSystemEntityFactory(), mt, outputFnBase + "-"
                                + mt.getTraceId() + ".pic",
                                shortLabels);
                        reportSuccess(mt.getTraceId());
                    } catch (final FileNotFoundException ex) {
                        reportError(mt.getTraceId());
                        log.error("File not found", ex);
                        //throw new TraceProcessingException("File not found", ex);
                    }
                }
            };

    private static String componentLabel(//final SystemEntityFactory systemEntityFactory,
            final AllocationComponent component, final boolean shortLabels) {
//        if (component == systemEntityFactory.getAllocationFactory().rootAllocationComponent) {
//            return "$";
//        }

//        String resourceContainerName = component.getExecutionContainer().getName();
        String assemblyComponentName = component.getAssemblyComponent().getName();
        String componentTypePackagePrefx = component.getAssemblyComponent().getType().getPackageName();
        String componentTypeIdentifier = component.getAssemblyComponent().getType().getTypeName();

        StringBuilder strBuild = //new StringBuilder(resourceContainerName).append("::").
                new StringBuilder(assemblyComponentName).append(":");
        if (!shortLabels) {
            strBuild.append(componentTypePackagePrefx);
        } else {
            strBuild.append("..");
        }
        strBuild.append(componentTypeIdentifier);
        return strBuild.toString();
    }

    private static void picFromMessageTrace(final SystemModelRepository systemEntityFactory,
            final MessageTrace messageTrace, final PrintStream ps,
            final boolean shortLabels) {
        // dot node ID x component instance
        Vector<Message> messages = messageTrace.getSequenceAsVector();
        //preamble:
        ps.println(".PS");
        ps.println("copy \"lib/sequence.pic\";");
        ps.println("boxwid = 1.1;");
        ps.println("movewid = 0.5;");

        TreeSet<Integer> plottedComponentIds = new TreeSet<Integer>();

        final AllocationComponent rootAllocationComponent = systemEntityFactory.getAllocationFactory().rootAllocationComponent;
        final String rootDotId = "O" + rootAllocationComponent.getId();
        ps.println("actor(O" + rootAllocationComponent.getId()
                + ",\"\");");
        plottedComponentIds.add(rootAllocationComponent.getId());
        for (Message me : messages) {
            AllocationComponent senderComponent = me.getSendingExecution().getAllocationComponent();
            AllocationComponent receiverComponent = me.getReceivingExecution().getAllocationComponent();
            if (!plottedComponentIds.contains(senderComponent.getId())) {
                ps.println("object(O" + senderComponent.getId()
                        + ",\"" + senderComponent.getExecutionContainer().getName() + "::\",\"" + componentLabel(senderComponent, shortLabels) + "\");");
                plottedComponentIds.add(senderComponent.getId());
            }
            if (!plottedComponentIds.contains(receiverComponent.getId())) {
                ps.println("object(O" + receiverComponent.getId()
                        + ",\"" + receiverComponent.getExecutionContainer().getName() + "::\",\"" + componentLabel(receiverComponent, shortLabels) + "\");");
                plottedComponentIds.add(receiverComponent.getId());
            }
        }
        //ps.println("step()");
        ps.println("step()");
        ps.println("active(" + rootDotId + ");");
        //ps.println("step();");
        boolean first = true;
        for (Message me : messages) {
            AllocationComponent senderComponent = me.getSendingExecution().getAllocationComponent();
            AllocationComponent receiverComponent = me.getReceivingExecution().getAllocationComponent();
            String senderDotId = "O" + senderComponent.getId();
            String receiverDotId = "O" + receiverComponent.getId();
            if (me instanceof SynchronousCallMessage) {
                Signature sig = me.getReceivingExecution().getOperation().getSignature();
                StringBuilder msgLabel = new StringBuilder(sig.getName());
                msgLabel.append("(");
                String[] paramList = sig.getParamTypeList();
                if (paramList != null && paramList.length > 0) {
                    msgLabel.append("..");
                }
                msgLabel.append(")");

                //if (method.indexOf('(') != -1) {
                //    method = me.receiver.opname;
                //}
                //ps.println("step();");
                if (first == true) {
                    ps.println("async();");
                    first = false;
                } else {
                    ps.println("sync();");
                }
                ps.println("message(" + senderDotId
                        + "," + receiverDotId
                        + ", \"" + msgLabel.toString()
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

    public static void writePicForMessageTrace(final SystemModelRepository systemEntityFactory,
            MessageTrace msgTrace, String outputFilename, final boolean shortLabels) throws FileNotFoundException {
        PrintStream ps = new PrintStream(new FileOutputStream(outputFilename));
        picFromMessageTrace(systemEntityFactory, msgTrace, ps, shortLabels);
        ps.flush();
        ps.close();
    }
}
