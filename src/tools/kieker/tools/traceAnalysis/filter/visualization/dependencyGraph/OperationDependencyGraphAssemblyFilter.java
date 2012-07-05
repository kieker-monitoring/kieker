/***************************************************************************
 * Copyright 2012 by
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

package kieker.tools.traceAnalysis.filter.visualization.dependencyGraph;

import java.io.File;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.AbstractMessageTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraph;
import kieker.tools.traceAnalysis.systemModel.AbstractMessage;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.SynchronousReplyMessage;
import kieker.tools.traceAnalysis.systemModel.repository.AbstractSystemSubRepository;
import kieker.tools.traceAnalysis.systemModel.repository.AssemblyComponentOperationPairFactory;
import kieker.tools.traceAnalysis.systemModel.repository.AssemblyRepository;
import kieker.tools.traceAnalysis.systemModel.repository.OperationRepository;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.tools.traceAnalysis.systemModel.util.AssemblyComponentOperationPair;

/**
 * Refactored copy from LogAnalysis-legacy tool<br>
 * 
 * This class has exactly one input port named "in". The data which is send to this plugin is not delegated in any way.
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr,
 */
@Plugin(repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class),
		outputPorts = @OutputPort(name = OperationDependencyGraphAssemblyFilter.OUTPUT_PORT_NAME, eventTypes = { AbstractGraph.class }))
public class OperationDependencyGraphAssemblyFilter extends AbstractDependencyGraphFilter<AssemblyComponentOperationPair> {
	public static final String CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE = "dotOutputFn";
	public static final String CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS = "includeWeights";
	public static final String CONFIG_PROPERTY_NAME_SHORT_LABELS = "shortLabels";
	public static final String CONFIG_PROPERTY_NAME_INCLUDE_SELF_LOOPS = "includeSelfLoops";

	public static final String OUTPUT_PORT_NAME = "graphOutput";

	/**
	 * This is the default dot output name used for the default configuration of this instance.
	 */
	private static final String DEFAULT_DOT_OUTPUT_FILE = "output.dot";

	private final File dotOutputFile;
	private final boolean includeWeights;
	private final boolean shortLabels;
	private final boolean includeSelfLoops;

	/**
	 * Creates a new instance of this class using the given configuration.
	 * 
	 * @param configuration
	 *            The configuration used to initialize this instance.
	 */
	public OperationDependencyGraphAssemblyFilter(final Configuration configuration) {
		/* Call the mandatory "default" constructor. */
		super(configuration);

		/* Initialize the necessary fields from the inherited class. */
		super.setDependencyGraph(new OperationAssemblyDependencyGraph(new AssemblyComponentOperationPair(AbstractSystemSubRepository.ROOT_ELEMENT_ID,
				OperationRepository.ROOT_OPERATION,
				AssemblyRepository.ROOT_ASSEMBLY_COMPONENT)));

		/* Initialize from the given configuration. */
		this.dotOutputFile = new File(this.configuration.getStringProperty(CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE));
		this.includeWeights = this.configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS);
		this.shortLabels = this.configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_SHORT_LABELS);
		this.includeSelfLoops = this.configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_INCLUDE_SELF_LOOPS);
	}

	@Override
	public void terminate(final boolean error) {
		if (!error) {
			this.deliver(OUTPUT_PORT_NAME, this.dependencyGraph);
		}
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE,
				DEFAULT_DOT_OUTPUT_FILE);
		configuration.setProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, Boolean.toString(true));
		configuration.setProperty(CONFIG_PROPERTY_NAME_SHORT_LABELS, Boolean.toString(true));
		configuration.setProperty(CONFIG_PROPERTY_NAME_INCLUDE_SELF_LOOPS, Boolean.toString(true));

		return configuration;
	}

	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE, this.dotOutputFile.getAbsolutePath());
		configuration.setProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, Boolean.toString(this.includeWeights));
		configuration.setProperty(CONFIG_PROPERTY_NAME_SHORT_LABELS, Boolean.toString(this.shortLabels));
		configuration.setProperty(CONFIG_PROPERTY_NAME_INCLUDE_SELF_LOOPS, Boolean.toString(this.includeSelfLoops));

		return configuration;
	}

	@Override
	@InputPort(
			name = AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES,
			description = "Receives the message traces to be processed",
			eventTypes = { MessageTrace.class })
	public void inputMessageTraces(final MessageTrace t) {
		for (final AbstractMessage m : t.getSequenceAsVector()) {
			if (m instanceof SynchronousReplyMessage) {
				continue;
			}
			final AssemblyComponent senderComponent = m.getSendingExecution().getAllocationComponent().getAssemblyComponent();
			final AssemblyComponent receiverComponent = m.getReceivingExecution().getAllocationComponent().getAssemblyComponent();
			final int rootOperationId = OperationRepository.ROOT_OPERATION.getId();
			final Operation senderOperation = m.getSendingExecution().getOperation();
			final Operation receiverOperation = m.getReceivingExecution().getOperation();
			/* The following two get-calls to the factory return s.th. in either case */
			final AssemblyComponentOperationPairFactory pairFactory = this.getSystemEntityFactory().getAssemblyPairFactory();
			final AssemblyComponentOperationPair senderPair = (senderOperation.getId() == rootOperationId) ? OperationDependencyGraphAssemblyFilter.this.dependencyGraph // NOCS
					.getRootNode().getEntity()
					: pairFactory.getPairInstanceByPair(senderComponent, senderOperation);
			final AssemblyComponentOperationPair receiverPair = (receiverOperation.getId() == rootOperationId) ? OperationDependencyGraphAssemblyFilter.this.dependencyGraph // NOCS
					.getRootNode().getEntity()
					: pairFactory.getPairInstanceByPair(receiverComponent, receiverOperation);

			DependencyGraphNode<AssemblyComponentOperationPair> senderNode = OperationDependencyGraphAssemblyFilter.this.dependencyGraph.getNode(senderPair
					.getId());
			DependencyGraphNode<AssemblyComponentOperationPair> receiverNode = OperationDependencyGraphAssemblyFilter.this.dependencyGraph.getNode(receiverPair
					.getId());
			if (senderNode == null) {
				senderNode = new DependencyGraphNode<AssemblyComponentOperationPair>(senderPair.getId(), senderPair, t);

				if (m.getSendingExecution().isAssumed()) {
					senderNode.setAssumed();
				}

				OperationDependencyGraphAssemblyFilter.this.dependencyGraph.addNode(senderNode.getId(), senderNode);
			}
			else {
				senderNode.addOrigin(t);
			}

			if (receiverNode == null) {
				receiverNode = new DependencyGraphNode<AssemblyComponentOperationPair>(receiverPair.getId(), receiverPair, t);

				if (m.getReceivingExecution().isAssumed()) {
					receiverNode.setAssumed();
				}

				OperationDependencyGraphAssemblyFilter.this.dependencyGraph.addNode(receiverNode.getId(), receiverNode);
			}
			else {
				receiverNode.addOrigin(t);
			}

			final boolean assumed = this.isDependencyAssumed(senderNode, receiverNode);

			senderNode.addOutgoingDependency(receiverNode, assumed, t);
			receiverNode.addIncomingDependency(senderNode, assumed, t);

			this.invokeDecorators(m, senderNode, receiverNode);
		}
		OperationDependencyGraphAssemblyFilter.this.reportSuccess(t.getTraceId());
	}

}
