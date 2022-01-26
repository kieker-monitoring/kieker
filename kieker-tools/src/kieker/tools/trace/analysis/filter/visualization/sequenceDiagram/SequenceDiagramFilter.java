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

package kieker.tools.trace.analysis.filter.visualization.sequenceDiagram;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.common.util.signature.Signature;
import kieker.tools.trace.analysis.filter.AbstractMessageTraceProcessingFilter;
import kieker.tools.trace.analysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.trace.analysis.systemModel.AbstractMessage;
import kieker.tools.trace.analysis.systemModel.AbstractTrace;
import kieker.tools.trace.analysis.systemModel.AllocationComponent;
import kieker.tools.trace.analysis.systemModel.AssemblyComponent;
import kieker.tools.trace.analysis.systemModel.MessageTrace;
import kieker.tools.trace.analysis.systemModel.SynchronousCallMessage;
import kieker.tools.trace.analysis.systemModel.SynchronousReplyMessage;
import kieker.tools.trace.analysis.systemModel.repository.AllocationRepository;
import kieker.tools.trace.analysis.systemModel.repository.SystemModelRepository;

/**
 * Refactored copy from LogAnalysis-legacy tool<br>
 *
 * This class has exactly one input port named "in". The data which is send to
 * this plugin is not delegated in any way.
 *
 * @author Andre van Hoorn, Nils Sommer, Jan Waller
 *
 * @since 0.95a
 * @deprecated 1.15 ported to teetime
 */
@Deprecated
@Plugin(description = "A filter allowing to write the incoming data into a sequence diagram", repositoryPorts = {
	@RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class)
}, configuration = {
	@Property(name = SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_FN_BASE, defaultValue = SequenceDiagramFilter.CONFIG_PROPERTY_VALUE_OUTPUT_FN_BASE_DEFAULT),
	@Property(name = SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_SHORTLABES, defaultValue = "true"),
	@Property(name = SequenceDiagramFilter.CONFIG_PROPERTY_NAME_OUTPUT_SDMODE, defaultValue = "ASSEMBLY") // SDModes.ASSEMBLY.toString())
})
public class SequenceDiagramFilter extends AbstractMessageTraceProcessingFilter {
	/** The name of the configuration determining the used output filename base. */
	public static final String CONFIG_PROPERTY_NAME_OUTPUT_FN_BASE = "filename";
	/** The name of the configuration determining whether to use short labels or not. */
	public static final String CONFIG_PROPERTY_NAME_OUTPUT_SHORTLABES = "shortLabels";
	/** The name of the configuration determining the used mode. */
	public static final String CONFIG_PROPERTY_NAME_OUTPUT_SDMODE = "SDMode";

	/** This constant determines the default used output filename base. */
	public static final String CONFIG_PROPERTY_VALUE_OUTPUT_FN_BASE_DEFAULT = "SequenceDiagram";

	private static final Logger LOGGER = LoggerFactory.getLogger(SequenceDiagramFilter.class.getCanonicalName());

	/**
	 * Path to the sequence.pic macros used to plot UML sequence diagrams. The
	 * file must be in the classpath -- typically inside the jar.
	 */
	private static final String SEQUENCE_PIC_PATH = "META-INF/sequence.pic";
	private static final String SEQUENCE_PIC_CONTENT;
	private static final String ENCODING = "UTF-8";

	private final String outputFnBase;
	private final boolean shortLabels;
	private final SDModes sdmode;

	// Read Spinellis' UML macros from file META-INF/sequence.pic to the String variable sequencePicContent. This contents are copied to every sequence diagram .pic
	// file
	static {
		final StringBuilder sb = new StringBuilder();
		boolean error = true;
		BufferedReader reader = null;

		try {
			final InputStream is = SequenceDiagramFilter.class.getClassLoader().getResourceAsStream(SEQUENCE_PIC_PATH);
			String line;
			reader = new BufferedReader(new InputStreamReader(is, ENCODING));
			while ((line = reader.readLine()) != null) { // NOPMD (assign)
				sb.append(line).append('\n');
			}
			error = false;
		} catch (final IOException exc) {
			AbstractTraceAnalysisFilter.LOGGER.error("Error while reading {}", SEQUENCE_PIC_PATH, exc);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (final IOException ex) {
				AbstractTraceAnalysisFilter.LOGGER.error("Failed to close input stream", ex);
			}
			if (error) {
				// sequence.pic must be provided on execution of pic2plot
				SEQUENCE_PIC_CONTENT = "copy \"sequence.pic\";"; // NOCS (this)
			} else {
				SEQUENCE_PIC_CONTENT = sb.toString(); // NOCS (this)
			}
		}
	}

	/**
	 * @author Andre van Hoorn
	 *
	 * @since 1.2
	 */
	public static enum SDModes {
		/** The assembly mode for the sequence diagrams. */
		ASSEMBLY,
		/** The allocation mode for the sequence diagrams. */
		ALLOCATION
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration to use for this filter.
	 * @param projectContext
	 *            The project context to use for this filter.
	 */
	public SequenceDiagramFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.sdmode = SDModes.valueOf(configuration.getStringProperty(CONFIG_PROPERTY_NAME_OUTPUT_SDMODE));
		this.outputFnBase = configuration.getStringProperty(CONFIG_PROPERTY_NAME_OUTPUT_FN_BASE);
		this.shortLabels = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_OUTPUT_SHORTLABES);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void printStatusMessage() {
		super.printStatusMessage();
		final int numPlots = this.getSuccessCount();
		final long lastSuccessTracesId = this.getLastTraceIdSuccess();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Wrote " + numPlots + " sequence diagram" + (numPlots > 1 ? "s" : "") // NOCS (AvoidInlineConditionalsCheck)
					+ " to file" + (numPlots > 1 ? "s" : "") + " with name pattern '" + this.outputFnBase + "-<traceId>.pic'"); // NOCS (Inline Conditional)
			LOGGER.debug("Pic files can be converted using the pic2plot tool (package plotutils)");
			LOGGER.debug("Example: pic2plot -T svg " + this.outputFnBase + "-" + ((numPlots > 0) ? lastSuccessTracesId : "<traceId>") // NOCS (Inline Conditional)
					+ ".pic > " + this.outputFnBase + "-" + ((numPlots > 0) ? lastSuccessTracesId : "<traceId>") + ".svg"); // NOCS (AvoidInlineConditionalsCheck)
		}
	}

	@Override
	@InputPort(name = AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES, description = "Receives the message traces to be processed",
			eventTypes = MessageTrace.class)
	public void inputMessageTraces(final MessageTrace mt) {
		try {
			SequenceDiagramFilter.writePicForMessageTrace(mt,
					SequenceDiagramFilter.this.sdmode,
					SequenceDiagramFilter.this.outputFnBase + "-" + ((AbstractTrace) mt).getTraceId() + ".pic", SequenceDiagramFilter.this.shortLabels);
			SequenceDiagramFilter.this.reportSuccess(((AbstractTrace) mt).getTraceId());
		} catch (final FileNotFoundException ex) {
			SequenceDiagramFilter.this.reportError(((AbstractTrace) mt).getTraceId());
			this.logger.error("File not found", ex);
		} catch (final UnsupportedEncodingException ex) {
			SequenceDiagramFilter.this.reportError(((AbstractTrace) mt).getTraceId());
			this.logger.error("Encoding not supported", ex);
		}
	}

	private static String assemblyComponentLabel(final AssemblyComponent component, final boolean shortLabels) {
		final String assemblyComponentName = component.getName();
		final String componentTypePackagePrefx = component.getType().getPackageName();
		final String componentTypeIdentifier = component.getType().getTypeName();

		final StringBuilder strBuild = new StringBuilder(assemblyComponentName).append(':');
		if (!shortLabels) {
			strBuild.append(componentTypePackagePrefx).append('.');
		} else {
			strBuild.append("..");
		}
		strBuild.append(componentTypeIdentifier);
		return strBuild.toString();
	}

	private static String allocationComponentLabel(final AllocationComponent component, final boolean shortLabels) {
		final String assemblyComponentName = component.getAssemblyComponent().getName();
		final String componentTypePackagePrefx = component.getAssemblyComponent().getType().getPackageName();
		final String componentTypeIdentifier = component.getAssemblyComponent().getType().getTypeName();

		final StringBuilder strBuild = new StringBuilder(assemblyComponentName).append(':');
		if (!shortLabels) {
			strBuild.append(componentTypePackagePrefx).append('.');
		} else {
			strBuild.append("..");
		}
		strBuild.append(componentTypeIdentifier);
		return strBuild.toString();
	}

	/**
	 * It is important NOT to use the println method but print and a manual
	 * linebreak by printing the character \n. The pic2plot tool can only
	 * process pic files with UNIX line breaks.
	 *
	 * @param messageTrace
	 *            The message trace to convert.
	 * @param sdMode
	 *            The mode of the sequence diagram (allocation, assembly).
	 * @param ps
	 *            The print stream which will be used to print the pic code.
	 * @param shortLabels
	 *            Determines whether to use short labels or not.
	 */
	private static void picFromMessageTrace(final MessageTrace messageTrace, final SDModes sdMode,
			final PrintStream ps, final boolean shortLabels) {
		// dot node ID x component instance
		final Iterable<AbstractMessage> messages = messageTrace.getSequenceAsVector();
		// preamble:
		ps.print(".PS" + "\n");
		ps.print(SEQUENCE_PIC_CONTENT + "\n");
		ps.print("boxwid = 1.1;" + "\n");
		ps.print("movewid = 0.5;" + "\n");

		final Set<Integer> plottedComponentIds = new TreeSet<>();

		final AllocationComponent rootAllocationComponent = AllocationRepository.ROOT_ALLOCATION_COMPONENT;
		final String rootDotId = "O" + rootAllocationComponent.getId();
		ps.print("actor(O" + rootAllocationComponent.getId() + ",\"\");" + "\n");
		plottedComponentIds.add(rootAllocationComponent.getId());

		if (sdMode == SDModes.ALLOCATION) {
			for (final AbstractMessage me : messages) {
				final AllocationComponent senderComponent = me.getSendingExecution().getAllocationComponent();
				final AllocationComponent receiverComponent = me.getReceivingExecution().getAllocationComponent();
				if (!plottedComponentIds.contains(senderComponent.getId())) {
					ps.print("object(O" + senderComponent.getId() + ",\"" + senderComponent.getExecutionContainer().getName() + "::\",\""
							+ SequenceDiagramFilter.allocationComponentLabel(senderComponent, shortLabels) + "\");" + "\n");
					plottedComponentIds.add(senderComponent.getId());
				}
				if (!plottedComponentIds.contains(receiverComponent.getId())) {
					ps.print("object(O" + receiverComponent.getId() + ",\"" + receiverComponent.getExecutionContainer().getName() + "::\",\""
							+ SequenceDiagramFilter.allocationComponentLabel(receiverComponent, shortLabels) + "\");" + "\n");
					plottedComponentIds.add(receiverComponent.getId());
				}
			}
		} else if (sdMode == SDModes.ASSEMBLY) {
			for (final AbstractMessage me : messages) {
				final AssemblyComponent senderComponent = me.getSendingExecution().getAllocationComponent().getAssemblyComponent();
				final AssemblyComponent receiverComponent = me.getReceivingExecution().getAllocationComponent().getAssemblyComponent();
				if (!plottedComponentIds.contains(senderComponent.getId())) {
					ps.print("object(O" + senderComponent.getId() + ",\"\",\"" + SequenceDiagramFilter.assemblyComponentLabel(senderComponent, shortLabels) + "\");"
							+ "\n");
					plottedComponentIds.add(senderComponent.getId());
				}
				if (!plottedComponentIds.contains(receiverComponent.getId())) {
					ps.print("object(O" + receiverComponent.getId() + ",\"\",\"" + SequenceDiagramFilter.assemblyComponentLabel(receiverComponent, shortLabels)
							+ "\");" + "\n");
					plottedComponentIds.add(receiverComponent.getId());
				}
			}
		} else { // needs to be adjusted if a new mode is introduced
			LOGGER.error("Invalid mode: {}", sdMode);
		}

		ps.print("step();" + "\n");
		ps.print("active(" + rootDotId + ");" + "\n");
		boolean first = true;
		for (final AbstractMessage me : messages) {
			String senderDotId = "-1";
			String receiverDotId = "-1";

			if (sdMode == SDModes.ALLOCATION) {
				final AllocationComponent senderComponent = me.getSendingExecution().getAllocationComponent();
				final AllocationComponent receiverComponent = me.getReceivingExecution().getAllocationComponent();
				senderDotId = "O" + senderComponent.getId();
				receiverDotId = "O" + receiverComponent.getId();
			} else if (sdMode == SDModes.ASSEMBLY) {
				final AssemblyComponent senderComponent = me.getSendingExecution().getAllocationComponent().getAssemblyComponent();
				final AssemblyComponent receiverComponent = me.getReceivingExecution().getAllocationComponent().getAssemblyComponent();
				senderDotId = "O" + senderComponent.getId();
				receiverDotId = "O" + receiverComponent.getId();
			} else { // needs to be adjusted if a new mode is introduced
				LOGGER.error("Invalid mode: {}", sdMode);
			}

			if (me instanceof SynchronousCallMessage) {
				final Signature sig = me.getReceivingExecution().getOperation().getSignature();
				final StringBuilder msgLabel = new StringBuilder(sig.getName());
				msgLabel.append('(');
				final String[] paramList = sig.getParamTypeList();
				if (paramList.length > 0) {
					msgLabel.append("..");
				}
				msgLabel.append(')');

				if (first) {
					ps.print("async();\n");
					first = false;
				} else {
					ps.print("sync();\n");
				}
				ps.print("message(" + senderDotId + "," + receiverDotId + ", \"" + msgLabel.toString() + "\");\n");
				ps.print("active(" + receiverDotId + ");\n");
				ps.print("step();\n");
			} else if (me instanceof SynchronousReplyMessage) {
				ps.print("step();" + "\n");
				ps.print("async();" + "\n");
				ps.print("rmessage(" + senderDotId + "," + receiverDotId + ", \"\");\n");
				ps.print("inactive(" + senderDotId + ");\n");
			} else {
				LOGGER.error("Message type not supported: {}", me.getClass().getName());
			}
		}
		ps.print("inactive(" + rootDotId + ");\n");
		ps.print("step();\n");

		for (final int i : plottedComponentIds) {
			ps.print("complete(O" + i + ");\n");
		}
		ps.print("complete(" + rootDotId + ");\n");

		ps.print(".PE\n");
	}

	/**
	 * This method writes the pic code for the given message trace into the given file.
	 *
	 * @param msgTrace
	 *            The message trace to convert.
	 * @param sdMode
	 *            The mode of the sequence diagram (allocation, assembly).
	 * @param shortLabels
	 *            Determines whether to use short labels or not.
	 * @param outputFilename
	 *            The name of the file in which the code will be written.
	 *
	 * @throws FileNotFoundException
	 *             If the given file is somehow invalid.
	 * @throws UnsupportedEncodingException
	 *             If the used default encoding is not supported.
	 */
	public static void writePicForMessageTrace(final MessageTrace msgTrace, final SDModes sdMode,
			final String outputFilename, final boolean shortLabels) throws FileNotFoundException, UnsupportedEncodingException {
		final PrintStream ps = new PrintStream(new FileOutputStream(outputFilename), false, ENCODING);
		SequenceDiagramFilter.picFromMessageTrace(msgTrace, sdMode, ps, shortLabels);
		ps.flush();
		ps.close();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = super.getCurrentConfiguration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_OUTPUT_FN_BASE, this.outputFnBase);
		configuration.setProperty(CONFIG_PROPERTY_NAME_OUTPUT_SHORTLABES, Boolean.toString(this.shortLabels));
		configuration.setProperty(CONFIG_PROPERTY_NAME_OUTPUT_SDMODE, this.sdmode.toString());

		return configuration;
	}
}
