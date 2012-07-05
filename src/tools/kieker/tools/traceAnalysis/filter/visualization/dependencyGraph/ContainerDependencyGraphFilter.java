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
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.SynchronousReplyMessage;
import kieker.tools.traceAnalysis.systemModel.repository.ExecutionEnvironmentRepository;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * Refactored copy from LogAnalysis-legacy tool<br>
 * 
 * This class has exactly one input port named "in". The data which is send to
 * this plugin is not delegated in any way.
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr,
 */
@Plugin(repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class),
		outputPorts = @OutputPort(name = ContainerDependencyGraphFilter.OUTPUT_PORT_NAME, eventTypes = { AbstractGraph.class }))
public class ContainerDependencyGraphFilter extends AbstractDependencyGraphFilter<ExecutionContainer> {
	public static final String CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE = "dotOutputFn";
	public static final String CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS = "includeWeights";
	public static final String CONFIG_PROPERTY_NAME_SHORT_LABELS = "shortLabels";
	public static final String CONFIG_PROPERTY_NAME_INCLUDE_SELF_LOOPS = "includeSelfLoops";

	public static final String OUTPUT_PORT_NAME = "graphOutput";

	private final File dotOutputFile;
	private final boolean includeWeights;
	private final boolean shortLabels;
	private final boolean includeSelfLoops;

	public ContainerDependencyGraphFilter(final Configuration configuration) {
		// TODO Check type conversion
		super(configuration);
		super.setDependencyGraph(new ContainerDependencyGraph(ExecutionEnvironmentRepository.ROOT_EXECUTION_CONTAINER));

		this.dotOutputFile = new File(configuration.getStringProperty(CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE));
		this.includeWeights = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS);
		this.shortLabels = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_SHORT_LABELS);
		this.includeSelfLoops = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_INCLUDE_SELF_LOOPS);
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

		configuration.setProperty(CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE, "");
		configuration.setProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, Boolean.FALSE.toString());
		configuration.setProperty(CONFIG_PROPERTY_NAME_INCLUDE_SELF_LOOPS, Boolean.FALSE.toString());
		configuration.setProperty(CONFIG_PROPERTY_NAME_SHORT_LABELS, Boolean.FALSE.toString());

		return configuration;
	}

	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE, this.dotOutputFile.getAbsolutePath());
		configuration.setProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, Boolean.toString(this.includeWeights));
		configuration.setProperty(CONFIG_PROPERTY_NAME_INCLUDE_SELF_LOOPS, Boolean.toString(this.includeSelfLoops));
		configuration.setProperty(CONFIG_PROPERTY_NAME_SHORT_LABELS, Boolean.toString(this.shortLabels));

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
			final AllocationComponent senderComponent = m.getSendingExecution().getAllocationComponent();
			final AllocationComponent receiverComponent = m.getReceivingExecution().getAllocationComponent();
			final ExecutionContainer senderContainer = senderComponent.getExecutionContainer();
			final ExecutionContainer receiverContainer = receiverComponent.getExecutionContainer();
			DependencyGraphNode<ExecutionContainer> senderNode = ContainerDependencyGraphFilter.this.dependencyGraph.getNode(senderContainer.getId());
			DependencyGraphNode<ExecutionContainer> receiverNode = ContainerDependencyGraphFilter.this.dependencyGraph.getNode(receiverContainer.getId());

			if (senderNode == null) {
				senderNode = new DependencyGraphNode<ExecutionContainer>(senderContainer.getId(), senderContainer, t);
				ContainerDependencyGraphFilter.this.dependencyGraph.addNode(senderContainer.getId(), senderNode);
			}
			else {
				senderNode.addOrigin(t);
			}

			if (receiverNode == null) {
				receiverNode = new DependencyGraphNode<ExecutionContainer>(receiverContainer.getId(), receiverContainer, t);
				ContainerDependencyGraphFilter.this.dependencyGraph.addNode(receiverContainer.getId(), receiverNode);
			}
			else {
				receiverNode.addOrigin(t);
			}

			senderNode.addOutgoingDependency(receiverNode, t);
			receiverNode.addIncomingDependency(senderNode, t);
		}
		ContainerDependencyGraphFilter.this.reportSuccess(t.getTraceId());
	}

}
