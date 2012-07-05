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
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.traceAnalysis.filter.AbstractMessageTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.visualization.util.dot.DotFactory;
import kieker.tools.traceAnalysis.systemModel.AbstractMessage;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.SynchronousReplyMessage;
import kieker.tools.traceAnalysis.systemModel.repository.AssemblyRepository;
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
		outputPorts = @OutputPort(name = ComponentDependencyGraphAssemblyFilter.OUTPUT_PORT_NAME))
public class ComponentDependencyGraphAssemblyFilter extends AbstractDependencyGraphFilter<AssemblyComponent> {

	public static final String CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE = "dotOutputFn";
	public static final String CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS = "includeWeights";
	public static final String CONFIG_PROPERTY_NAME_SHORT_LABELS = "shortLabels";
	public static final String CONFIG_PROPERTY_NAME_INCLUDE_SELF_LOOPS = "includeSelfLoops";

	public static final String OUTPUT_PORT_NAME = "graphOutput";

	private static final Log LOG = LogFactory.getLog(ComponentDependencyGraphAssemblyFilter.class);
	private final File dotOutputFile;
	private final boolean includeWeights;
	private final boolean shortLabels;
	private final boolean includeSelfLoops;

	public ComponentDependencyGraphAssemblyFilter(final Configuration configuration) {
		// TODO Check type conversion
		super(configuration);
		super.setDependencyGraph(new ComponentAssemblyDependencyGraph(AssemblyRepository.ROOT_ASSEMBLY_COMPONENT));
		this.dotOutputFile = new File(configuration.getStringProperty(CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE));
		this.includeWeights = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS);
		this.shortLabels = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_SHORT_LABELS);
		this.includeSelfLoops = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_INCLUDE_SELF_LOOPS);
	}

	private String nodeLabel(final DependencyGraphNode<?> node, final AssemblyComponent curComponent) {
		final StringBuilder builder = new StringBuilder();

		if (this.shortLabels) {
			builder.append(AbstractDependencyGraphFilter.STEREOTYPE_ASSEMBLY_COMPONENT + "\\n" + curComponent.getName() + ":.."
					+ curComponent.getType().getTypeName());
		} else {
			builder.append(AbstractDependencyGraphFilter.STEREOTYPE_ASSEMBLY_COMPONENT + "\\n" + curComponent.getName() + ":"
					+ curComponent.getType().getFullQualifiedName());
		}

		this.addDecorationText(builder, node);

		return builder.toString();
	}

	@Override
	protected void dotEdges(final Collection<DependencyGraphNode<AssemblyComponent>> nodes, final PrintStream ps, final boolean shortLabelsL) {

		final AssemblyComponent rootComponent = AssemblyRepository.ROOT_ASSEMBLY_COMPONENT;
		final int rootComponentId = rootComponent.getId();
		final StringBuilder strBuild = new StringBuilder();
		// dot code for contained components
		for (final DependencyGraphNode<AssemblyComponent> node : nodes) {
			final AssemblyComponent curComponent = node.getEntity();
			final int curComponentId = node.getId();
			strBuild.append(DotFactory.createNode("", this.getNodeId(node), (curComponentId == rootComponentId) ? "$" : this.nodeLabel(node, curComponent), // NOCS
					(curComponentId == rootComponentId) ? DotFactory.DOT_SHAPE_NONE : DotFactory.DOT_SHAPE_BOX, // NOCS
					(curComponentId == rootComponentId) ? null : DotFactory.DOT_STYLE_FILLED, // style // NOCS // NOPMD (null)
					null, // framecolor
					(curComponentId == rootComponentId) ? null : this.getNodeFillColor(node), // fillcolor // NOCS //NOPMD (null)
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
				super.deliver(OUTPUT_PORT_NAME, this.dependencyGraph);
			} catch (final IOException ex) {
				LOG.error("IOException while saving to dot file", ex);
			}
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
			description = "Receives message traces to be processed",
			eventTypes = { MessageTrace.class })
	public void inputMessageTraces(final MessageTrace t) {
		for (final AbstractMessage m : t.getSequenceAsVector()) {
			if (m instanceof SynchronousReplyMessage) {
				continue;
			}
			final AssemblyComponent senderComponent = m.getSendingExecution().getAllocationComponent().getAssemblyComponent();
			final AssemblyComponent receiverComponent = m.getReceivingExecution().getAllocationComponent().getAssemblyComponent();
			DependencyGraphNode<AssemblyComponent> senderNode = ComponentDependencyGraphAssemblyFilter.this.dependencyGraph.getNode(senderComponent.getId());
			DependencyGraphNode<AssemblyComponent> receiverNode = ComponentDependencyGraphAssemblyFilter.this.dependencyGraph.getNode(receiverComponent.getId());
			if (senderNode == null) {
				senderNode = new DependencyGraphNode<AssemblyComponent>(senderComponent.getId(), senderComponent, t);

				if (m.getSendingExecution().isAssumed()) {
					senderNode.setAssumed();
				}

				ComponentDependencyGraphAssemblyFilter.this.dependencyGraph.addNode(senderNode.getId(), senderNode);
			}
			else {
				senderNode.addOrigin(t);
			}

			if (receiverNode == null) {
				receiverNode = new DependencyGraphNode<AssemblyComponent>(receiverComponent.getId(), receiverComponent, t);

				if (m.getReceivingExecution().isAssumed()) {
					receiverNode.setAssumed();
				}

				ComponentDependencyGraphAssemblyFilter.this.dependencyGraph.addNode(receiverNode.getId(), receiverNode);
			}
			else {
				receiverNode.addOrigin(t);
			}

			final boolean assumed = this.isDependencyAssumed(senderNode, receiverNode);

			senderNode.addOutgoingDependency(receiverNode, assumed, t);
			receiverNode.addIncomingDependency(senderNode, assumed, t);

			this.invokeDecorators(m, senderNode, receiverNode);
		}
		ComponentDependencyGraphAssemblyFilter.this.reportSuccess(t.getTraceId());
	}
}
