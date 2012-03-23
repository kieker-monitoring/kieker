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

package kieker.tools.traceAnalysis.filter.visualization.dependencyGraph;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.traceAnalysis.filter.AbstractMessageTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.visualization.util.dot.DotFactory;
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
@Plugin(repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisFilter.SYSTEM_MODEL_REPOSITORY_NAME, repositoryType = SystemModelRepository.class))
public class ContainerDependencyGraphFilter extends AbstractDependencyGraphFilter<ExecutionContainer> {

	public static final String CONFIG_DOT_OUTPUT_FILE = "dotOutputFile";
	public static final String CONFIG_INCLUDE_WEIGHTS = "includeWeights";
	public static final String CONFIG_SHORT_LABELS = "shortLabels";
	public static final String CONFIG_INCLUDE_SELF_LOOPS = "includeSelfLoops";
	private static final Log LOG = LogFactory.getLog(ContainerDependencyGraphFilter.class);

	private final File dotOutputFile;
	private final boolean includeWeights;
	private final boolean shortLabels;
	private final boolean includeSelfLoops;

	public ContainerDependencyGraphFilter(final Configuration configuration) {
		// TODO Check type conversion
		super(configuration);
		super.setDependencyGraph(new DependencyGraph<ExecutionContainer>(
				ExecutionEnvironmentRepository.ROOT_EXECUTION_CONTAINER.getId(),
				ExecutionEnvironmentRepository.ROOT_EXECUTION_CONTAINER));

		this.dotOutputFile = new File(configuration.getStringProperty(ContainerDependencyGraphFilter.CONFIG_DOT_OUTPUT_FILE));
		this.includeWeights = configuration.getBooleanProperty(ContainerDependencyGraphFilter.CONFIG_INCLUDE_WEIGHTS);
		this.shortLabels = configuration.getBooleanProperty(ContainerDependencyGraphFilter.CONFIG_SHORT_LABELS);
		this.includeSelfLoops = configuration.getBooleanProperty(ContainerDependencyGraphFilter.CONFIG_INCLUDE_SELF_LOOPS);
	}

	@Override
	protected void dotEdges(final Collection<DependencyGraphNode<ExecutionContainer>> nodes, final PrintStream ps, final boolean shortLabelsL) {

		final ExecutionContainer rootContainer = ExecutionEnvironmentRepository.ROOT_EXECUTION_CONTAINER;
		final int rootContainerId = rootContainer.getId();
		final StringBuilder strBuild = new StringBuilder();
		for (final DependencyGraphNode<ExecutionContainer> node : nodes) {
			final ExecutionContainer curContainer = node.getEntity();
			final int curContainerId = node.getId();
			strBuild.append(DotFactory.createNode("", this.getNodeId(node), (curContainerId == rootContainerId) ? "$" // NOCS (AvoidInlineConditionalsCheck)
					: AbstractDependencyGraphFilter.STEREOTYPE_EXECUTION_CONTAINER + "\\n" + curContainer.getName(),
					(curContainerId == rootContainerId) ? DotFactory.DOT_SHAPE_NONE : DotFactory.DOT_SHAPE_BOX3D, // NOCS (AvoidInlineConditionalsCheck)
					(curContainerId == rootContainerId) ? null : DotFactory.DOT_STYLE_FILLED, // style // NOPMD // NOCS (AvoidInlineConditionalsCheck)
					null, // framecolor
					(curContainerId == rootContainerId) ? null : DotFactory.DOT_FILLCOLOR_WHITE, // fillcolor // NOPMD // NOCS (AvoidInlineConditionalsCheck)
					null, // fontcolor
					DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
					null, // imagefilename
					null // misc
					));
			strBuild.append("\n");
		}
		ps.println(strBuild.toString());
	}

	/**
	 * Saves the dependency graph to the dot file if error is not true.
	 * 
	 * @param error
	 */

	@Override
	public void terminate(final boolean error) {
		if (!error) {
			try {
				this.saveToDotFile(this.dotOutputFile.getCanonicalPath(), this.includeWeights, this.shortLabels, this.includeSelfLoops);
			} catch (final IOException ex) {
				ContainerDependencyGraphFilter.LOG.error("IOException while saving to dot file", ex);
			}
		}
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(ContainerDependencyGraphFilter.CONFIG_DOT_OUTPUT_FILE, "");
		configuration.setProperty(ContainerDependencyGraphFilter.CONFIG_INCLUDE_WEIGHTS, Boolean.FALSE.toString());
		configuration.setProperty(ContainerDependencyGraphFilter.CONFIG_INCLUDE_SELF_LOOPS, Boolean.FALSE.toString());
		configuration.setProperty(ContainerDependencyGraphFilter.CONFIG_SHORT_LABELS, Boolean.FALSE.toString());

		return configuration;
	}

	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(ContainerDependencyGraphFilter.CONFIG_DOT_OUTPUT_FILE, this.dotOutputFile.getAbsolutePath());
		configuration.setProperty(ContainerDependencyGraphFilter.CONFIG_INCLUDE_WEIGHTS, Boolean.toString(this.includeWeights));
		configuration.setProperty(ContainerDependencyGraphFilter.CONFIG_INCLUDE_SELF_LOOPS, Boolean.toString(this.includeSelfLoops));
		configuration.setProperty(ContainerDependencyGraphFilter.CONFIG_SHORT_LABELS, Boolean.toString(this.shortLabels));

		return configuration;
	}

	@Override
	@InputPort(
			name = AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME,
			description = "Message traces",
			eventTypes = { MessageTrace.class })
	public void msgTraceInput(final MessageTrace t) {
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
				senderNode = new DependencyGraphNode<ExecutionContainer>(senderContainer.getId(), senderContainer);
				ContainerDependencyGraphFilter.this.dependencyGraph.addNode(senderContainer.getId(), senderNode);
			}
			if (receiverNode == null) {
				receiverNode = new DependencyGraphNode<ExecutionContainer>(receiverContainer.getId(), receiverContainer);
				ContainerDependencyGraphFilter.this.dependencyGraph.addNode(receiverContainer.getId(), receiverNode);
			}
			senderNode.addOutgoingDependency(receiverNode);
			receiverNode.addIncomingDependency(senderNode);
		}
		ContainerDependencyGraphFilter.this.reportSuccess(t.getTraceId());
	}

}
