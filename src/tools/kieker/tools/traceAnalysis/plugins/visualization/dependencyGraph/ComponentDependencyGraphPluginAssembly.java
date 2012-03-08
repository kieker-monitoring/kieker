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

package kieker.tools.traceAnalysis.plugins.visualization.dependencyGraph;

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
import kieker.tools.traceAnalysis.plugins.AbstractMessageTraceProcessingPlugin;
import kieker.tools.traceAnalysis.plugins.AbstractTraceAnalysisPlugin;
import kieker.tools.traceAnalysis.plugins.visualization.util.dot.DotFactory;
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
@Plugin(repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, repositoryType = SystemModelRepository.class))
public class ComponentDependencyGraphPluginAssembly extends AbstractDependencyGraphPlugin<AssemblyComponent> {

	public static final String CONFIG_DOT_OUTPUT_FILE = ComponentDependencyGraphPluginAssembly.class.getName() + ".dotOutputFile";
	public static final String CONFIG_INCLUDE_WEIGHTS = ComponentDependencyGraphPluginAssembly.class.getName() + ".includeWeights";
	public static final String CONFIG_SHORT_LABELS = ComponentDependencyGraphPluginAssembly.class.getName() + ".shortLabels";
	public static final String CONFIG_INCLUDE_SELF_LOOPS = ComponentDependencyGraphPluginAssembly.class.getName() + ".includeSelfLoops";

	private static final Log LOG = LogFactory.getLog(ComponentDependencyGraphPluginAssembly.class);
	private final File dotOutputFile;
	private final boolean includeWeights;
	private final boolean shortLabels;
	private final boolean includeSelfLoops;

	public ComponentDependencyGraphPluginAssembly(final Configuration configuration) {
		// TODO Check type conversion
		super(configuration,
				new DependencyGraph<AssemblyComponent>(
						AssemblyRepository.ROOT_ASSEMBLY_COMPONENT.getId(),
						AssemblyRepository.ROOT_ASSEMBLY_COMPONENT));
		this.dotOutputFile = new File(configuration.getStringProperty(ComponentDependencyGraphPluginAssembly.CONFIG_DOT_OUTPUT_FILE));
		this.includeWeights = configuration.getBooleanProperty(ComponentDependencyGraphPluginAssembly.CONFIG_INCLUDE_WEIGHTS);
		this.shortLabels = configuration.getBooleanProperty(ComponentDependencyGraphPluginAssembly.CONFIG_SHORT_LABELS);
		this.includeSelfLoops = configuration.getBooleanProperty(ComponentDependencyGraphPluginAssembly.CONFIG_INCLUDE_SELF_LOOPS);
	}

	private String nodeLabel(final AssemblyComponent curComponent) {
		if (this.shortLabels) {
			return AbstractDependencyGraphPlugin.STEREOTYPE_ASSEMBLY_COMPONENT + "\\n" + curComponent.getName() + ":.." + curComponent.getType().getTypeName();
		} else {
			return AbstractDependencyGraphPlugin.STEREOTYPE_ASSEMBLY_COMPONENT + "\\n" + curComponent.getName() + ":"
					+ curComponent.getType().getFullQualifiedName();
		}
	}

	@Override
	protected void dotEdges(final Collection<DependencyGraphNode<AssemblyComponent>> nodes, final PrintStream ps, final boolean shortLabelsL) {

		final AssemblyComponent rootComponent = AssemblyRepository.ROOT_ASSEMBLY_COMPONENT;
		final int rootComponentId = rootComponent.getId();
		final StringBuilder strBuild = new StringBuilder();
		// dot code for contained components
		for (final DependencyGraphNode<AssemblyComponent> node : nodes) {
			final String fillColor = (node.isAssumed()) ? DotFactory.DOT_FILLCOLOR_GRAY : DotFactory.DOT_FILLCOLOR_WHITE;

			final AssemblyComponent curComponent = node.getEntity();
			final int curComponentId = node.getId();
			strBuild.append(DotFactory.createNode("", this.getNodeId(node), (curComponentId == rootComponentId) ? "$" : this.nodeLabel(curComponent), // NOCS
					(curComponentId == rootComponentId) ? DotFactory.DOT_SHAPE_NONE : DotFactory.DOT_SHAPE_BOX, // NOCS
					(curComponentId == rootComponentId) ? null : DotFactory.DOT_STYLE_FILLED, // style // NOCS // NOPMD
					null, // framecolor
					(curComponentId == rootComponentId) ? null : fillColor, // fillcolor // NOCS //NOPMD
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
				ComponentDependencyGraphPluginAssembly.LOG.error("IOException", ex);
			}
		}
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(ComponentDependencyGraphPluginAssembly.CONFIG_DOT_OUTPUT_FILE, "");
		configuration.setProperty(ComponentDependencyGraphPluginAssembly.CONFIG_INCLUDE_WEIGHTS, Boolean.FALSE.toString());
		configuration.setProperty(ComponentDependencyGraphPluginAssembly.CONFIG_INCLUDE_SELF_LOOPS, Boolean.FALSE.toString());
		configuration.setProperty(ComponentDependencyGraphPluginAssembly.CONFIG_SHORT_LABELS, Boolean.FALSE.toString());

		return configuration;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(ComponentDependencyGraphPluginAssembly.CONFIG_DOT_OUTPUT_FILE, this.dotOutputFile.getAbsolutePath());
		configuration.setProperty(ComponentDependencyGraphPluginAssembly.CONFIG_INCLUDE_WEIGHTS, Boolean.toString(this.includeWeights));
		configuration.setProperty(ComponentDependencyGraphPluginAssembly.CONFIG_INCLUDE_SELF_LOOPS, Boolean.toString(this.includeSelfLoops));
		configuration.setProperty(ComponentDependencyGraphPluginAssembly.CONFIG_SHORT_LABELS, Boolean.toString(this.shortLabels));

		return configuration;
	}

	@Override
	@InputPort(
			name = AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME,
			description = "Message traces",
			eventTypes = { MessageTrace.class })
	public void msgTraceInput(final MessageTrace t) {
		for (final AbstractMessage m : t.getSequenceAsVector()) {
			if (m instanceof SynchronousReplyMessage) {
				continue;
			}
			final AssemblyComponent senderComponent = m.getSendingExecution().getAllocationComponent().getAssemblyComponent();
			final AssemblyComponent receiverComponent = m.getReceivingExecution().getAllocationComponent().getAssemblyComponent();
			DependencyGraphNode<AssemblyComponent> senderNode = ComponentDependencyGraphPluginAssembly.this.dependencyGraph.getNode(senderComponent.getId());
			DependencyGraphNode<AssemblyComponent> receiverNode = ComponentDependencyGraphPluginAssembly.this.dependencyGraph.getNode(receiverComponent.getId());
			if (senderNode == null) {
				senderNode = new DependencyGraphNode<AssemblyComponent>(senderComponent.getId(), senderComponent);

				if (m.getSendingExecution().isAssumed()) {
					senderNode.setAssumed();
				}

				ComponentDependencyGraphPluginAssembly.this.dependencyGraph.addNode(senderNode.getId(), senderNode);
			}
			if (receiverNode == null) {
				receiverNode = new DependencyGraphNode<AssemblyComponent>(receiverComponent.getId(), receiverComponent);

				if (m.getReceivingExecution().isAssumed()) {
					receiverNode.setAssumed();
				}

				ComponentDependencyGraphPluginAssembly.this.dependencyGraph.addNode(receiverNode.getId(), receiverNode);
			}

			final boolean assumed = (senderNode.isAssumed() || receiverNode.isAssumed());

			senderNode.addOutgoingDependency(receiverNode, assumed);
			receiverNode.addIncomingDependency(senderNode, assumed);
		}
		ComponentDependencyGraphPluginAssembly.this.reportSuccess(t.getTraceId());
	}

}
