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

import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.traceAnalysis.plugins.visualization.util.dot.DotFactory;
import kieker.tools.traceAnalysis.systemModel.AbstractMessage;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.SynchronousReplyMessage;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * Refactored copy from LogAnalysis-legacy tool
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr,
 */
public class ComponentDependencyGraphPluginAssembly extends AbstractDependencyGraphPlugin<AssemblyComponent> {

	private static final Log LOG = LogFactory.getLog(ComponentDependencyGraphPluginAssembly.class);
	private final File dotOutputFile;
	private final boolean includeWeights;
	private final boolean shortLabels;
	private final boolean includeSelfLoops;

	public ComponentDependencyGraphPluginAssembly(final String name, final SystemModelRepository systemEntityFactory, final File dotOutputFile,
			final boolean includeWeights, final boolean shortLabels, final boolean includeSelfLoops) {
		super(name, systemEntityFactory, new DependencyGraph<AssemblyComponent>(systemEntityFactory.getAssemblyFactory().getRootAssemblyComponent().getId(),
				systemEntityFactory.getAssemblyFactory().getRootAssemblyComponent()));
		this.dotOutputFile = dotOutputFile;
		this.includeWeights = includeWeights;
		this.shortLabels = shortLabels;
		this.includeSelfLoops = includeSelfLoops;
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

		final AssemblyComponent rootComponent = this.getSystemEntityFactory().getAssemblyFactory().getRootAssemblyComponent();
		final int rootComponentId = rootComponent.getId();
		final StringBuilder strBuild = new StringBuilder();
		// dot code for contained components
		for (final DependencyGraphNode<AssemblyComponent> node : nodes) {
			final AssemblyComponent curComponent = node.getEntity();
			final int curComponentId = node.getId();
			strBuild.append(DotFactory.createNode("", this.getNodeId(node), (curComponentId == rootComponentId) ? "$" : this.nodeLabel(curComponent), // NOCS
					(curComponentId == rootComponentId) ? DotFactory.DOT_SHAPE_NONE : DotFactory.DOT_SHAPE_BOX, // NOCS
					(curComponentId == rootComponentId) ? null : DotFactory.DOT_STYLE_FILLED, // style // NOCS // NOPMD
					null, // framecolor
					(curComponentId == rootComponentId) ? null : DotFactory.DOT_FILLCOLOR_WHITE, // fillcolor // NOCS //NOPMD
					null, // fontcolor
					DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
					null, // imagefilename
					null // misc
					));
			strBuild.append("\n");
		}
		ps.println(strBuild.toString());
	}

	@Override
	public boolean execute() {
		return true; // no need to do anything here
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
	public AbstractInputPort getMessageTraceInputPort() {
		return this.messageTraceInputPort;
	}

	private final AbstractInputPort messageTraceInputPort = new AbstractInputPort("Message traces", null) {

		@Override
		public void newEvent(final Object obj) {
			final MessageTrace t = (MessageTrace) obj;
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
					ComponentDependencyGraphPluginAssembly.this.dependencyGraph.addNode(senderNode.getId(), senderNode);
				}
				if (receiverNode == null) {
					receiverNode = new DependencyGraphNode<AssemblyComponent>(receiverComponent.getId(), receiverComponent);
					ComponentDependencyGraphPluginAssembly.this.dependencyGraph.addNode(receiverNode.getId(), receiverNode);
				}
				senderNode.addOutgoingDependency(receiverNode);
				receiverNode.addIncomingDependency(senderNode);
			}
			ComponentDependencyGraphPluginAssembly.this.reportSuccess(t.getTraceId());
		}
	};
}
