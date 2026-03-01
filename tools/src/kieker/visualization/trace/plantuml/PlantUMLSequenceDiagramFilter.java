/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
 ***************************************************************************/
package kieker.visualization.trace.plantuml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.plugin.trace.AbstractMessageTraceProcessingFilter;
import kieker.common.util.signature.Signature;
import kieker.model.repository.AllocationRepository;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.AbstractMessage;
import kieker.model.system.model.AllocationComponent;
import kieker.model.system.model.AssemblyComponent;
import kieker.model.system.model.MessageTrace;
import kieker.model.system.model.SynchronousCallMessage;
import kieker.model.system.model.SynchronousReplyMessage;

import teetime.framework.OutputPort;

/**
 * Filter for generating PlantUML sequence diagrams from message traces. This
 * class has exactly one input port named "in". The data which is send to this
 * plugin is not delegated in any way.
 *
 * @author Yorrick Josuttis
 */
@SuppressWarnings("PMD.TooManyMethods")
public class PlantUMLSequenceDiagramFilter extends AbstractMessageTraceProcessingFilter {

    private static final String ENCODING = "UTF-8";

    private static final String FILE_NOT_FOUND_MESSAGE = "File not found";
    private static final String ENCODING_NOT_SUPPORTED_MESSAGE = "Encoding not supported";
    private static final String INVALID_MODE_MESSAGE = "Invalid mode: {}";
    private static final String MESSAGE_TYPE_NOT_SUPPORTED_MESSAGE = "Message type not supported: {}";

    private static final Logger LOGGER = LoggerFactory
            .getLogger(PlantUMLSequenceDiagramFilter.class.getCanonicalName());

    private final String outputFnBase;
    private final boolean shortLabels;

    private final OutputPort<File> outputPort = this.createOutputPort();

    /**
     * Modes for sequence diagram generation.
     *
     * @author Yorrick Josuttis
     */
    public enum SDModes {
        ASSEMBLY,
        ALLOCATION
    }

    private final SDModes sdmode;

    /**
     * Creates a new instance of this class using the given parameters.
     *
     * @param repository
     *            system model repository
     * @param sdmode
     *            sequence diagram mode
     * @param outputFnBase
     *            output file name base
     * @param shortLabels
     *            use short labels
     */
    public PlantUMLSequenceDiagramFilter(final SystemModelRepository repository, final SDModes sdmode, final String outputFnBase,
            final boolean shortLabels) {
        super(repository);
        this.sdmode = sdmode;
        this.outputFnBase = outputFnBase;
        this.shortLabels = shortLabels;
    }

    /**
     * Processes a message trace and generates the corresponding PlantUML sequence diagram.
     */
    @Override
    protected void execute(final MessageTrace mt) throws Exception {
        try {
            final File file = writePlantUMLForMessageTrace(mt, this.sdmode, this.outputFnBase + "-" + mt.getTraceId() + ".puml", this.shortLabels);
            this.reportSuccess(mt.getTraceId());
            outputPort.send(file);
        } catch (final FileNotFoundException ex) {
            this.reportError(mt.getTraceId());
            LOGGER.error(FILE_NOT_FOUND_MESSAGE, ex);
        } catch (final UnsupportedEncodingException ex) {
            this.reportError(mt.getTraceId());
            LOGGER.error(ENCODING_NOT_SUPPORTED_MESSAGE, ex);
        }
    }

    /**
     * Converts a given message trace to its PlantUML representation and writes it to the provided print stream.
     * 
     * @param messageTrace the message trace
     * @param sdMode the sequence diagram mode
     * @param ps the print stream
     * @param shortLabels whether to use short labels
     * 
     * @return the PlantUML representation of the message trace
     */
    private static void convertToPlantUML(final MessageTrace messageTrace, final SDModes sdMode,
            final PrintStream ps, final boolean shortLabels) {
        final Iterable<AbstractMessage> messages = messageTrace.getSequenceAsVector();
        ps.println(PlantUMLUtils.START_PUML);

        ps.println(PlantUMLUtils.PLANTUML_PREAMBLE);

        final Set<Integer> plottedComponentIds = new TreeSet<>();
        final AllocationComponent rootAllocationComponent = AllocationRepository.ROOT_ALLOCATION_COMPONENT;
        final String rootDotId = PlantUMLUtils.dotId(rootAllocationComponent.getId());
        ps.println(PlantUMLUtils.actor(rootDotId, "\" \""));
        plottedComponentIds.add(rootAllocationComponent.getId());

        generateParticipants(messages, sdMode, ps, shortLabels, plottedComponentIds);

        ps.println(PlantUMLUtils.step());
        ps.println(PlantUMLUtils.activate(rootDotId));

        processMessages(messages, sdMode, ps, rootDotId, plottedComponentIds);

        ps.println(PlantUMLUtils.END_PUML);
    }

    /**
     * Generates the assembly component label.
     */
    private static String assemblyComponentLabel(final AssemblyComponent component, final boolean shortLabels) {
        final String assemblyComponentName = component.getName();
        final String componentTypePackagePrefix = component.getType().getPackageName();
        final String componentTypeIdentifier = component.getType().getTypeName();

        final StringBuilder stringBuilder = new StringBuilder(assemblyComponentName).append(':');
        if (!shortLabels) {
            stringBuilder.append(componentTypePackagePrefix).append('.');
        } else {
            stringBuilder.append("..");
        }
        stringBuilder.append(componentTypeIdentifier);
        return stringBuilder.toString();
    }

    /**
     * Generates the allocation component label.
     */
    private static String allocationComponentLabel(final AllocationComponent component, final boolean shortLabels) {
        final String assemblyComponentName = component.getAssemblyComponent().getName();
        final String componentTypePackagePrefix = component.getAssemblyComponent().getType().getPackageName();
        final String componentTypeIdentifier = component.getAssemblyComponent().getType().getTypeName();

        final StringBuilder stringBuilder = new StringBuilder(assemblyComponentName).append(':');
        if (!shortLabels) {
            stringBuilder.append(componentTypePackagePrefix).append(':');
        } else {
            stringBuilder.append("..");
        }
        stringBuilder.append(componentTypeIdentifier);
        return stringBuilder.toString();
    }

    /**
     * Generates the participants.
     */
    private static void generateParticipants(final Iterable<AbstractMessage> messages, final SDModes sdMode,
            final PrintStream ps, final boolean shortLabels, final Set<Integer> plottedComponentIds) {
        if (null == sdMode) {
            LOGGER.error(INVALID_MODE_MESSAGE, sdMode);
            return;
        }

        switch (sdMode) {
            case ALLOCATION:
                generateAllocationParticipants(messages, ps, shortLabels, plottedComponentIds);
                break;
            case ASSEMBLY:
                generateAssemblyParticipants(messages, ps, shortLabels, plottedComponentIds);
                break;
            default:
                LOGGER.error(INVALID_MODE_MESSAGE, sdMode);
                break;
        }
    }

    /**
     * Generates the allocation participants.
     */
    private static void generateAllocationParticipants(final Iterable<AbstractMessage> messages,
            final PrintStream ps, final boolean shortLabels, final Set<Integer> plottedComponentIds) {
        for (final AbstractMessage me : messages) {
            final AllocationComponent senderComponent = me.getSendingExecution().getAllocationComponent();
            final AllocationComponent receiverComponent = me.getReceivingExecution().getAllocationComponent();

            if (!plottedComponentIds.contains(senderComponent.getId())) {
                final String senderDotId = PlantUMLUtils.dotId(senderComponent.getId());
                final String senderLabel = PlantUMLUtils.label(
                        senderComponent.getExecutionContainer().getName() + "::",
                        allocationComponentLabel(senderComponent, shortLabels));
                ps.println(PlantUMLUtils.participant(senderDotId, senderLabel));
                plottedComponentIds.add(senderComponent.getId());
            }
            if (!plottedComponentIds.contains(receiverComponent.getId())) {
                final String receiverDotId = PlantUMLUtils.dotId(receiverComponent.getId());
                final String receiverLabel = PlantUMLUtils.label(
                        receiverComponent.getExecutionContainer().getName() + "::",
                        allocationComponentLabel(receiverComponent, shortLabels));
                ps.println(PlantUMLUtils.participant(receiverDotId, receiverLabel));
                plottedComponentIds.add(receiverComponent.getId());
            }
        }
    }

    /**
     * Generates the assembly participants.
     */
    private static void generateAssemblyParticipants(final Iterable<AbstractMessage> messages,
            final PrintStream ps, final boolean shortLabels, final Set<Integer> plottedComponentIds) {
        for (final AbstractMessage me : messages) {
            final AssemblyComponent senderComponent = me.getSendingExecution().getAllocationComponent()
                    .getAssemblyComponent();
            final AssemblyComponent receiverComponent = me.getReceivingExecution().getAllocationComponent()
                    .getAssemblyComponent();

            if (!plottedComponentIds.contains(senderComponent.getId())) {
                final String senderDotId = PlantUMLUtils.dotId(senderComponent.getId());
                final String senderLabel = PlantUMLUtils.label(null, assemblyComponentLabel(senderComponent, shortLabels));
                ps.println(PlantUMLUtils.participant(senderDotId, senderLabel));
                plottedComponentIds.add(senderComponent.getId());
            }
            if (!plottedComponentIds.contains(receiverComponent.getId())) {
                final String receiverDotId = PlantUMLUtils.dotId(receiverComponent.getId());
                final String receiverLabel = PlantUMLUtils.label(null, assemblyComponentLabel(receiverComponent, shortLabels));
                ps.println(PlantUMLUtils.participant(receiverDotId, receiverLabel));
                plottedComponentIds.add(receiverComponent.getId());
            }
        }
    }

    /**
     * Processes the messages and writes them to the provided print stream.
     */
    private static void processMessages(final Iterable<AbstractMessage> messages, final SDModes sdMode,
            final PrintStream ps, final String rootDotId, final Set<Integer> plottedComponentIds) {
        boolean firstMessage = true;
        for (final AbstractMessage me : messages) {
            final String senderDotId = getSenderDotId(me, sdMode);
            final String receiverDotId = getReceiverDotId(me, sdMode);

            if (me instanceof SynchronousCallMessage) {
                firstMessage = processCallMessage(me, ps, senderDotId, receiverDotId, firstMessage);
            } else if (me instanceof SynchronousReplyMessage) {
                processReplyMessage(ps, senderDotId, receiverDotId);
            } else {
                LOGGER.error(MESSAGE_TYPE_NOT_SUPPORTED_MESSAGE, me.getClass().getName());
            }
        }

        ps.println(PlantUMLUtils.deactivate(rootDotId));
        ps.println(PlantUMLUtils.step());

        for (final int id : plottedComponentIds) {
            ps.println(PlantUMLUtils.deactivate(PlantUMLUtils.dotId(id)));
        }
        ps.println(PlantUMLUtils.deactivate(rootDotId));
    }

    /**
     * Gets the sender dot id.
     * 
     * @param me the message
     * @param sdMode the sequence diagram mode
     * 
     * @return the sender dot id
     */
    private static String getSenderDotId(final AbstractMessage me, final SDModes sdMode) {
        if (null == sdMode) {
            LOGGER.error(INVALID_MODE_MESSAGE, sdMode);
            return PlantUMLUtils.dotId(-1);
        }
        switch (sdMode) {
            case ALLOCATION:
                return PlantUMLUtils.dotId(me.getSendingExecution().getAllocationComponent().getId());
            case ASSEMBLY:
                return PlantUMLUtils.dotId(me.getSendingExecution().getAllocationComponent().getAssemblyComponent().getId());
            default:
                LOGGER.error(INVALID_MODE_MESSAGE, sdMode);
                return PlantUMLUtils.dotId(-1);
        }
    }

    /**
     * Gets the receiver dot id.
     * 
     * @param me the message
     * @param sdMode the sequence diagram mode
     * 
     * @return the receiver dot id
     */
    private static String getReceiverDotId(final AbstractMessage me, final SDModes sdMode) {
        if (null == sdMode) {
            LOGGER.error(INVALID_MODE_MESSAGE, sdMode);
            return PlantUMLUtils.dotId(-1);
        }
        switch (sdMode) {
            case ALLOCATION:
                return PlantUMLUtils.dotId(me.getReceivingExecution().getAllocationComponent().getId());
            case ASSEMBLY:
                return PlantUMLUtils.dotId(me.getReceivingExecution().getAllocationComponent().getAssemblyComponent().getId());
            default:
                LOGGER.error(INVALID_MODE_MESSAGE, sdMode);
                return PlantUMLUtils.dotId(-1);
        }
    }

    /**
     * Processes a call message.
     *
     * @return false after processing the first message, true otherwise
     */
    private static boolean processCallMessage(final AbstractMessage me, final PrintStream ps,
            final String senderDotId, final String receiverDotId, final boolean firstMessage) {
        final Signature sig = me.getReceivingExecution().getOperation().getSignature();
        final StringBuilder msgLabel = new StringBuilder(sig.getName());
        msgLabel.append('(');
        final String[] paramList = sig.getParamTypeList();
        if (paramList.length > 0) {
            msgLabel.append("..");
        }
        msgLabel.append(')');

        if (firstMessage) {
            ps.println(PlantUMLUtils.asynchronousMessage(senderDotId, receiverDotId, msgLabel.toString()));
        } else {
            ps.println(PlantUMLUtils.synchronousMessage(senderDotId, receiverDotId, msgLabel.toString()));
        }
        ps.println(PlantUMLUtils.activate(receiverDotId));
        ps.println(PlantUMLUtils.step());
        return false;
    }

    /**
     * Processes a reply message.
     */
    private static void processReplyMessage(final PrintStream ps, final String senderDotId,
            final String receiverDotId) {
        ps.println(PlantUMLUtils.step());
        ps.println(PlantUMLUtils.asynchronousReturnMessage(senderDotId, receiverDotId, ""));
        ps.println(PlantUMLUtils.deactivate(senderDotId));
    }

    /**
     * Writes the PlantUML representation of a given message trace to a file.
     * 
     * @param messageTrace the message trace
     * @param sdMode the sequence diagram mode
     * @param outputFilename the output file name
     * @param shortLabels whether to use short labels
     * 
     * @return the output file
     */
    public static File writePlantUMLForMessageTrace(final MessageTrace messageTrace, final SDModes sdMode,
            final String outputFilename, final boolean shortLabels) throws IOException {
        final File outputFile = new File(outputFilename);
        try (PrintStream ps = new PrintStream(Files.newOutputStream(Paths.get(outputFilename)), false, ENCODING)) {
            convertToPlantUML(messageTrace, sdMode, ps, shortLabels);
            ps.flush();
        }
        return outputFile;
    }

    /**
     * Returns the output port.
     * 
     * @return the outputPort
     */
    public OutputPort<File> getOutputPort() {
        return this.outputPort;
    }

}