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
import kieker.analysis.plugin.configuration.IInputPort;
import kieker.tools.traceAnalysis.plugins.visualization.util.dot.DotFactory;
import kieker.tools.traceAnalysis.systemModel.AbstractMessage;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.SynchronousReplyMessage;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Refactored copy from LogAnalysis-legacy tool
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr,
 */
public class ContainerDependencyGraphPlugin extends AbstractDependencyGraphPlugin<ExecutionContainer> {

	private static final Log LOG = LogFactory.getLog(ContainerDependencyGraphPlugin.class);

	private final File dotOutputFile;
	private final boolean includeWeights;
	private final boolean shortLabels;
	private final boolean includeSelfLoops;

	public ContainerDependencyGraphPlugin(final String name, final SystemModelRepository systemEntityFactory, final File dotOutputFile,
			final boolean includeWeights, final boolean shortLabels, final boolean includeSelfLoops) {
		super(name, systemEntityFactory, new DependencyGraph<ExecutionContainer>(
				systemEntityFactory.getExecutionEnvironmentFactory().getRootExecutionContainer().getId(),
				systemEntityFactory.getExecutionEnvironmentFactory().getRootExecutionContainer()));
		this.dotOutputFile = dotOutputFile;
		this.includeWeights = includeWeights;
		this.shortLabels = shortLabels;
		this.includeSelfLoops = includeSelfLoops;
	}

	@Override
	protected void dotEdges(final Collection<DependencyGraphNode<ExecutionContainer>> nodes, final PrintStream ps, final boolean shortLabels) {

		final ExecutionContainer rootContainer = this.getSystemEntityFactory().getExecutionEnvironmentFactory().getRootExecutionContainer();
		final int rootContainerId = rootContainer.getId();
		final StringBuilder strBuild = new StringBuilder();
		for (final DependencyGraphNode<ExecutionContainer> node : nodes) {
			final ExecutionContainer curContainer = node.getEntity();
			final int curContainerId = node.getId();
			strBuild.append(DotFactory.createNode("", this.getNodeId(node), (curContainerId == rootContainerId) ? "$" // NOCS (AvoidInlineConditionalsCheck)
					: AbstractDependencyGraphPlugin.STEREOTYPE_EXECUTION_CONTAINER + "\\n" + curContainer.getName(),
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
				ContainerDependencyGraphPlugin.LOG.error("IOException", ex);
			}
		}
	}

	private final IInputPort<MessageTrace> messageTraceInputPort = new AbstractInputPort<MessageTrace>("Message traces") {

		@Override
		public void newEvent(final MessageTrace t) {
			for (final AbstractMessage m : t.getSequenceAsVector()) {
				if (m instanceof SynchronousReplyMessage) {
					continue;
				}
				final AllocationComponent senderComponent = m.getSendingExecution().getAllocationComponent();
				final AllocationComponent receiverComponent = m.getReceivingExecution().getAllocationComponent();
				final ExecutionContainer senderContainer = senderComponent.getExecutionContainer();
				final ExecutionContainer receiverContainer = receiverComponent.getExecutionContainer();
				DependencyGraphNode<ExecutionContainer> senderNode = ContainerDependencyGraphPlugin.this.dependencyGraph.getNode(senderContainer.getId());
				DependencyGraphNode<ExecutionContainer> receiverNode = ContainerDependencyGraphPlugin.this.dependencyGraph.getNode(receiverContainer.getId());

				if (senderNode == null) {
					senderNode = new DependencyGraphNode<ExecutionContainer>(senderContainer.getId(), senderContainer);
					ContainerDependencyGraphPlugin.this.dependencyGraph.addNode(senderContainer.getId(), senderNode);
				}
				if (receiverNode == null) {
					receiverNode = new DependencyGraphNode<ExecutionContainer>(receiverContainer.getId(), receiverContainer);
					ContainerDependencyGraphPlugin.this.dependencyGraph.addNode(receiverContainer.getId(), receiverNode);
				}
				senderNode.addOutgoingDependency(receiverNode);
				receiverNode.addIncomingDependency(senderNode);
			}
			ContainerDependencyGraphPlugin.this.reportSuccess(t.getTraceId());
		}
	};

	@Override
	public IInputPort<MessageTrace> getMessageTraceInputPort() {
		return this.messageTraceInputPort;
	}
}
