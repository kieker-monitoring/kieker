package kieker.tools.traceAnalysis.plugins.visualization.sequenceDiagram;

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
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.Message;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.Signature;
import kieker.tools.traceAnalysis.systemModel.SynchronousCallMessage;
import kieker.tools.traceAnalysis.systemModel.SynchronousReplyMessage;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.IInputPort;
import kieker.tools.traceAnalysis.plugins.AbstractMessageTraceProcessingPlugin;
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
    /** Path to the sequence.pic macros used to plot UML sequence diagrams.
     * The file must be in the classpath -- typically inside the jar. */
    private final static String sequencePicPath = "META-INF/sequence.pic";
    private final static String sequencePicContent;

    /* Read Spinellis' UML macros from file META-INF/sequence.pic to the
     * String variable sequencePicContent. This contents are copied to
     * every sequence diagram .pic file */
    static {
        InputStream is =
                SequenceDiagramPlugin.class.getClassLoader().getResourceAsStream(sequencePicPath);
        StringBuilder sb = new StringBuilder();
        String line;
        boolean error = true;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            error = false;
        } catch (IOException exc) {
            log.error("Error while reading " + sequencePicPath, exc);
        } finally {
            if (error) {
                /* sequence.pic must be provided on execution of pic2plot */
                sequencePicContent = "copy \"sequence.pic\";";
            } else {
                sequencePicContent = sb.toString();
            }
            try {
                is.close();
            } catch (IOException ex) {
                log.error("Failed to close input stream", ex);
            }
        }
    }

    public enum SDModes {

        ASSEMBLY, ALLOCATION
    }
    private final SDModes sdmode;

    public SequenceDiagramPlugin(final String name, final SystemModelRepository systemEntityFactory,
            final SDModes sdmode,
            final String outputFnBase, final boolean shortLabels) {
        super(name, systemEntityFactory);
        this.sdmode = sdmode;
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
                        SequenceDiagramPlugin.writePicForMessageTrace(getSystemEntityFactory(), mt,
                                sdmode,
                                outputFnBase + "-"
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

    private static String assemblyComponentLabel(//final SystemEntityFactory systemEntityFactory,
            final AssemblyComponent component, final boolean shortLabels) {
//        if (component == systemEntityFactory.getAllocationFactory().rootAllocationComponent) {
//            return "$";
//        }

//        String resourceContainerName = component.getExecutionContainer().getName();
        String assemblyComponentName = component.getName();
        String componentTypePackagePrefx = component.getType().getPackageName();
        String componentTypeIdentifier = component.getType().getTypeName();

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

    private static String allocationComponentLabel(//final SystemEntityFactory systemEntityFactory,
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

    /**
     * It is important NOT to use the println method but print and a manuel
     * linebreak by printing the character \n. The pic2plot tool can only
     * process pic files with UNIX line breaks.
     *
     * @param systemEntityFactory
     * @param messageTrace
     * @param sdMode
     * @param ps
     * @param shortLabels
     */
    private static void picFromMessageTrace(final SystemModelRepository systemEntityFactory,
            final MessageTrace messageTrace, final SDModes sdMode, final PrintStream ps,
            final boolean shortLabels) {
        // dot node ID x component instance
        Vector<Message> messages = messageTrace.getSequenceAsVector();
        //preamble:
        ps.print(".PS"+"\n");
        //ps.print("copy \"lib/sequence.pic\";"+"\n");
        ps.print(sequencePicContent+"\n");
        ps.print("boxwid = 1.1;"+"\n");
        ps.print("movewid = 0.5;"+"\n");

        TreeSet<Integer> plottedComponentIds = new TreeSet<Integer>();

        final AllocationComponent rootAllocationComponent = systemEntityFactory.getAllocationFactory().rootAllocationComponent;
        final String rootDotId = "O" + rootAllocationComponent.getId();
        ps.print("actor(O" + rootAllocationComponent.getId()
                + ",\"\");"+"\n");
        plottedComponentIds.add(rootAllocationComponent.getId());

        if (sdMode == SDModes.ALLOCATION) {
            for (Message me : messages) {
                AllocationComponent senderComponent = me.getSendingExecution().getAllocationComponent();
                AllocationComponent receiverComponent = me.getReceivingExecution().getAllocationComponent();
                if (!plottedComponentIds.contains(senderComponent.getId())) {
                    ps.print("object(O" + senderComponent.getId()
                            + ",\"" + senderComponent.getExecutionContainer().getName() + "::\",\"" + allocationComponentLabel(senderComponent, shortLabels) + "\");"+"\n");
                    plottedComponentIds.add(senderComponent.getId());
                }
                if (!plottedComponentIds.contains(receiverComponent.getId())) {
                    ps.print("object(O" + receiverComponent.getId()
                            + ",\"" + receiverComponent.getExecutionContainer().getName() + "::\",\"" + allocationComponentLabel(receiverComponent, shortLabels) + "\");"+"\n");
                    plottedComponentIds.add(receiverComponent.getId());
                }
            }
        } else if (sdMode == SDModes.ASSEMBLY) {
            for (Message me : messages) {
                AssemblyComponent senderComponent = me.getSendingExecution().getAllocationComponent().getAssemblyComponent();
                AssemblyComponent receiverComponent = me.getReceivingExecution().getAllocationComponent().getAssemblyComponent();
                if (!plottedComponentIds.contains(senderComponent.getId())) {
                    ps.print("object(O" + senderComponent.getId()
                            + ",\"\",\"" + assemblyComponentLabel(senderComponent, shortLabels) + "\");"+"\n");
                    plottedComponentIds.add(senderComponent.getId());
                }
                if (!plottedComponentIds.contains(receiverComponent.getId())) {
                    ps.print("object(O" + receiverComponent.getId()
                            + ",\"\",\"" + assemblyComponentLabel(receiverComponent, shortLabels) + "\");"+"\n");
                    plottedComponentIds.add(receiverComponent.getId());
                }
            }
        } else { // needs to be adjusted if a new mode is introduced
            log.error("Invalid mode: " + sdMode);
        }


        //ps.print("step()"+"\n");
        ps.print("step()"+"\n");
        ps.print("active(" + rootDotId + ");"+"\n");
        //ps.print("step();"+"\n");
        boolean first = true;
        for (Message me : messages) {
            String senderDotId = "-1";
            String receiverDotId = "-1";

            if (sdMode == SDModes.ALLOCATION) {
                AllocationComponent senderComponent = me.getSendingExecution().getAllocationComponent();
                AllocationComponent receiverComponent = me.getReceivingExecution().getAllocationComponent();
                senderDotId = "O" + senderComponent.getId();
                receiverDotId = "O" + receiverComponent.getId();
            } else if (sdMode == SDModes.ASSEMBLY) {
                AssemblyComponent senderComponent = me.getSendingExecution().getAllocationComponent().getAssemblyComponent();
                AssemblyComponent receiverComponent = me.getReceivingExecution().getAllocationComponent().getAssemblyComponent();
                senderDotId = "O" + senderComponent.getId();
                receiverDotId = "O" + receiverComponent.getId();
            } else { // needs to be adjusted if a new mode is introduced
                log.error("Invalid mode: " + sdMode);
            }

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
                //ps.print("step();"+"\n");
                if (first == true) {
                    ps.print("async();"+"\n");
                    first = false;
                } else {
                    ps.print("sync();"+"\n");
                }
                ps.print("message(" + senderDotId
                        + "," + receiverDotId
                        + ", \"" + msgLabel.toString()
                        + "\");"+"\n");
                ps.print("active(" + receiverDotId + ");"+"\n");
                ps.print("step();"+"\n");
            } else if (me instanceof SynchronousReplyMessage) {
                ps.print("step();"+"\n");
                ps.print("async();"+"\n");
                ps.print("rmessage(" + senderDotId
                        + "," + receiverDotId
                        + ", \"\");"+"\n");
                ps.print("inactive(" + senderDotId + ");"+"\n");
            } else {
                log.error("Message type not supported: " + me.getClass().getName());
            }
        }
        ps.print("inactive(" + rootDotId + ");"+"\n");
        ps.print("step();"+"\n");

        for (int i : plottedComponentIds) {
            ps.print("complete(O" + i + ");"+"\n");
        }
        ps.print("complete(" + rootDotId + ");"+"\n");

        ps.print(".PE"+"\n");
    }

    public static void writePicForMessageTrace(final SystemModelRepository systemEntityFactory,
            MessageTrace msgTrace, final SDModes sdMode, String outputFilename, final boolean shortLabels) throws FileNotFoundException {
        PrintStream ps = new PrintStream(new FileOutputStream(outputFilename));
        picFromMessageTrace(systemEntityFactory, msgTrace, sdMode, ps, shortLabels);
        ps.flush();
        ps.close();
    }
}
