/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.tools.traceAnalysis.plugins.visualization.sequenceDiagram;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;

import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.traceAnalysis.plugins.AbstractMessageTraceProcessingPlugin;
import kieker.tools.traceAnalysis.systemModel.AbstractMessage;
import kieker.tools.traceAnalysis.systemModel.AbstractTrace;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.Signature;
import kieker.tools.traceAnalysis.systemModel.SynchronousCallMessage;
import kieker.tools.traceAnalysis.systemModel.SynchronousReplyMessage;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * Refactored copy from LogAnalysis-legacy tool<br>
 * 
 * This class has exactly one input port named "in". The data which is send to
 * this plugin is not delegated in any way.
 * 
 * @author Andre van Hoorn, Nils Sommer
 */
public class SequenceDiagramPlugin extends AbstractMessageTraceProcessingPlugin {

	private static final Log LOG = LogFactory.getLog(SequenceDiagramPlugin.class);

	/**
	 * Path to the sequence.pic macros used to plot UML sequence diagrams. The
	 * file must be in the classpath -- typically inside the jar.
	 */
	private static final String SEQUENCE_PIC_PATH = "META-INF/sequence.pic";
	private static final String SEQUENCE_PIC_CONTENT;

	private final String outputFnBase;
	private final boolean shortLabels;

	/*
	 * Read Spinellis' UML macros from file META-INF/sequence.pic to the String
	 * variable sequencePicContent. This contents are copied to every sequence
	 * diagram .pic file
	 */
	static {
		final StringBuilder sb = new StringBuilder();
		boolean error = true;
		BufferedReader reader = null;

		try {
			final InputStream is = SequenceDiagramPlugin.class.getClassLoader().getResourceAsStream(SequenceDiagramPlugin.SEQUENCE_PIC_PATH);
			String line;
			reader = new BufferedReader(new InputStreamReader(is));
			while ((line = reader.readLine()) != null) { // NOPMD (assign)
				sb.append(line).append("\n");
			}
			error = false;
		} catch (final IOException exc) {
			SequenceDiagramPlugin.LOG.error("Error while reading " + SequenceDiagramPlugin.SEQUENCE_PIC_PATH, exc);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (final IOException ex) {
				SequenceDiagramPlugin.LOG.error("Failed to close input stream", ex);
			}
			if (error) {
				/* sequence.pic must be provided on execution of pic2plot */
				SEQUENCE_PIC_CONTENT = "copy \"sequence.pic\";"; // NOCS (this)
			} else {
				SEQUENCE_PIC_CONTENT = sb.toString(); // NOCS (this)
			}
		}
	}

	public static enum SDModes {

		ASSEMBLY, ALLOCATION
	}

	private final SDModes sdmode;

	public SequenceDiagramPlugin(final String name, final SystemModelRepository systemEntityFactory, final SDModes sdmode, final String outputFnBase,
			final boolean shortLabels) {
		super(name, systemEntityFactory);
		this.sdmode = sdmode;
		this.outputFnBase = outputFnBase;
		this.shortLabels = shortLabels;
		
		/* Register the input port. */
		super.registerInputPort("in", messageTraceInputPort);
	}

	@Override
	public void printStatusMessage() {
		super.printStatusMessage();
		final int numPlots = this.getSuccessCount();
		final long lastSuccessTracesId = this.getLastTraceIdSuccess();
		System.out.println("Wrote " + numPlots + " sequence diagram" + (numPlots > 1 ? "s" : "") // NOCS (AvoidInlineConditionalsCheck)
				+ " to file" + (numPlots > 1 ? "s" : "") + " with name pattern '" + this.outputFnBase + "-<traceId>.pic'"); // NOCS (AvoidInlineConditionalsCheck)
		System.out.println("Pic files can be converted using the pic2plot tool (package plotutils)");
		System.out.println("Example: pic2plot -T svg " + this.outputFnBase + "-" + ((numPlots > 0) ? lastSuccessTracesId : "<traceId>") // NOCS
																																		// (AvoidInlineConditionalsCheck)
				+ ".pic > " + this.outputFnBase + "-" + ((numPlots > 0) ? lastSuccessTracesId : "<traceId>") + ".svg"); // NOCS (AvoidInlineConditionalsCheck)
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
	public AbstractInputPort getMessageTraceInputPort() {
		return this.messageTraceInputPort;
	}

	private final AbstractInputPort messageTraceInputPort = new AbstractInputPort("Message traces",
			Collections.unmodifiableCollection(new CopyOnWriteArrayList<Class<?>>(
					new Class<?>[] { MessageTrace.class }))) {

		@Override
		public void newEvent(final Object mt) {
			try {
				SequenceDiagramPlugin.writePicForMessageTrace(SequenceDiagramPlugin.this.getSystemEntityFactory(), (MessageTrace) mt,
						SequenceDiagramPlugin.this.sdmode,
						SequenceDiagramPlugin.this.outputFnBase + "-" + ((AbstractTrace) mt).getTraceId() + ".pic", SequenceDiagramPlugin.this.shortLabels);
				SequenceDiagramPlugin.this.reportSuccess(((AbstractTrace) mt).getTraceId());
			} catch (final FileNotFoundException ex) {
				SequenceDiagramPlugin.this.reportError(((AbstractTrace) mt).getTraceId());
				SequenceDiagramPlugin.LOG.error("File not found", ex);
			}
		}
	};

	private static String assemblyComponentLabel(final AssemblyComponent component, final boolean shortLabels) {
		final String assemblyComponentName = component.getName();
		final String componentTypePackagePrefx = component.getType().getPackageName();
		final String componentTypeIdentifier = component.getType().getTypeName();

		final StringBuilder strBuild = new StringBuilder(assemblyComponentName).append(":");
		if (!shortLabels) {
			strBuild.append(componentTypePackagePrefx).append(".");
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

		final StringBuilder strBuild = new StringBuilder(assemblyComponentName).append(":");
		if (!shortLabels) {
			strBuild.append(componentTypePackagePrefx).append(".");
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
	 * @param systemEntityFactory
	 * @param messageTrace
	 * @param sdMode
	 * @param ps
	 * @param shortLabels
	 */
	private static void picFromMessageTrace(final SystemModelRepository systemEntityFactory, final MessageTrace messageTrace, final SDModes sdMode,
			final PrintStream ps, final boolean shortLabels) {
		// See ticket http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/208
		// dot node ID x component instance
		final Collection<AbstractMessage> messages = messageTrace.getSequenceAsVector();
		// preamble:
		ps.print(".PS" + "\n");
		ps.print(SequenceDiagramPlugin.SEQUENCE_PIC_CONTENT + "\n");
		ps.print("boxwid = 1.1;" + "\n");
		ps.print("movewid = 0.5;" + "\n");

		final Set<Integer> plottedComponentIds = new TreeSet<Integer>();

		final AllocationComponent rootAllocationComponent = systemEntityFactory.getAllocationFactory().getRootAllocationComponent();
		final String rootDotId = "O" + rootAllocationComponent.getId();
		ps.print("actor(O" + rootAllocationComponent.getId() + ",\"\");" + "\n");
		plottedComponentIds.add(rootAllocationComponent.getId());

		if (sdMode == SequenceDiagramPlugin.SDModes.ALLOCATION) {
			for (final AbstractMessage me : messages) {
				final AllocationComponent senderComponent = me.getSendingExecution().getAllocationComponent();
				final AllocationComponent receiverComponent = me.getReceivingExecution().getAllocationComponent();
				if (!plottedComponentIds.contains(senderComponent.getId())) {
					ps.print("object(O" + senderComponent.getId() + ",\"" + senderComponent.getExecutionContainer().getName() + "::\",\"" // NOPMD
							+ SequenceDiagramPlugin.allocationComponentLabel(senderComponent, shortLabels) + "\");" + "\n"); // NOPMD
					plottedComponentIds.add(senderComponent.getId());
				}
				if (!plottedComponentIds.contains(receiverComponent.getId())) {
					ps.print("object(O" + receiverComponent.getId() + ",\"" + receiverComponent.getExecutionContainer().getName() + "::\",\""
							+ SequenceDiagramPlugin.allocationComponentLabel(receiverComponent, shortLabels) + "\");" + "\n");
					plottedComponentIds.add(receiverComponent.getId());
				}
			}
		} else if (sdMode == SequenceDiagramPlugin.SDModes.ASSEMBLY) {
			for (final AbstractMessage me : messages) {
				final AssemblyComponent senderComponent = me.getSendingExecution().getAllocationComponent().getAssemblyComponent();
				final AssemblyComponent receiverComponent = me.getReceivingExecution().getAllocationComponent().getAssemblyComponent();
				if (!plottedComponentIds.contains(senderComponent.getId())) {
					ps.print("object(O" + senderComponent.getId() + ",\"\",\"" + SequenceDiagramPlugin.assemblyComponentLabel(senderComponent, shortLabels) + "\");"
							+ "\n");
					plottedComponentIds.add(senderComponent.getId());
				}
				if (!plottedComponentIds.contains(receiverComponent.getId())) {
					ps.print("object(O" + receiverComponent.getId() + ",\"\",\"" + SequenceDiagramPlugin.assemblyComponentLabel(receiverComponent, shortLabels)
							+ "\");" + "\n");
					plottedComponentIds.add(receiverComponent.getId());
				}
			}
		} else { // needs to be adjusted if a new mode is introduced
			SequenceDiagramPlugin.LOG.error("Invalid mode: " + sdMode);
		}

		ps.print("step()" + "\n");
		ps.print("active(" + rootDotId + ");" + "\n");
		boolean first = true;
		for (final AbstractMessage me : messages) {
			String senderDotId = "-1";
			String receiverDotId = "-1";

			if (sdMode == SequenceDiagramPlugin.SDModes.ALLOCATION) {
				final AllocationComponent senderComponent = me.getSendingExecution().getAllocationComponent();
				final AllocationComponent receiverComponent = me.getReceivingExecution().getAllocationComponent();
				senderDotId = "O" + senderComponent.getId();
				receiverDotId = "O" + receiverComponent.getId();
			} else if (sdMode == SequenceDiagramPlugin.SDModes.ASSEMBLY) {
				final AssemblyComponent senderComponent = me.getSendingExecution().getAllocationComponent().getAssemblyComponent();
				final AssemblyComponent receiverComponent = me.getReceivingExecution().getAllocationComponent().getAssemblyComponent();
				senderDotId = "O" + senderComponent.getId();
				receiverDotId = "O" + receiverComponent.getId();
			} else { // needs to be adjusted if a new mode is introduced
				SequenceDiagramPlugin.LOG.error("Invalid mode: " + sdMode);
			}

			if (me instanceof SynchronousCallMessage) {
				final Signature sig = me.getReceivingExecution().getOperation().getSignature();
				final StringBuilder msgLabel = new StringBuilder(sig.getName()); // NOPMD (new in loop)
				msgLabel.append("(");
				final String[] paramList = sig.getParamTypeList();
				if ((paramList != null) && (paramList.length > 0)) {
					msgLabel.append("..");
				}
				msgLabel.append(")");

				if (first) {
					ps.print("async();" + "\n");
					first = false;
				} else {
					ps.print("sync();" + "\n");
				}
				ps.print("message(" + senderDotId + "," + receiverDotId + ", \"" + msgLabel.toString() + "\");" + "\n");
				ps.print("active(" + receiverDotId + ");" + "\n");
				ps.print("step();" + "\n");
			} else if (me instanceof SynchronousReplyMessage) {
				ps.print("step();" + "\n");
				ps.print("async();" + "\n");
				ps.print("rmessage(" + senderDotId + "," + receiverDotId + ", \"\");" + "\n");
				ps.print("inactive(" + senderDotId + ");" + "\n");
			} else {
				SequenceDiagramPlugin.LOG.error("Message type not supported: " + me.getClass().getName());
			}
		}
		ps.print("inactive(" + rootDotId + ");" + "\n");
		ps.print("step();" + "\n");

		for (final int i : plottedComponentIds) {
			ps.print("complete(O" + i + ");" + "\n");
		}
		ps.print("complete(" + rootDotId + ");" + "\n");

		ps.print(".PE" + "\n");
	}

	public static void writePicForMessageTrace(final SystemModelRepository systemEntityFactory, final MessageTrace msgTrace, final SDModes sdMode,
			final String outputFilename, final boolean shortLabels) throws FileNotFoundException {
		final PrintStream ps = new PrintStream(new FileOutputStream(outputFilename));
		SequenceDiagramPlugin.picFromMessageTrace(systemEntityFactory, msgTrace, sdMode, ps, shortLabels);
		ps.flush();
		ps.close();
	}

	@Override
	protected Properties getDefaultProperties() {
		return new Properties();
	}
}
