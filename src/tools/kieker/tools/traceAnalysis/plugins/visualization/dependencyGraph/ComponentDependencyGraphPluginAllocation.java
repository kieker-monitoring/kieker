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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import kieker.analysis.plugin.port.InputPort;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.traceAnalysis.plugins.visualization.util.dot.DotFactory;
import kieker.tools.traceAnalysis.systemModel.AbstractMessage;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.SynchronousReplyMessage;
import kieker.tools.traceAnalysis.systemModel.repository.AbstractRepository;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * Refactored copy from LogAnalysis-legacy tool<br>
 * 
 * This class has exactly one input port named "in". The data which is send to
 * this plugin is not delegated in any way.
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr,
 */
public class ComponentDependencyGraphPluginAllocation extends AbstractDependencyGraphPlugin<AllocationComponent> {

	private static final Log LOG = LogFactory.getLog(ComponentDependencyGraphPluginAllocation.class);
	private static final String CONTAINER_NODE_ID_PREFIX = "container";
	private final File dotOutputFile;
	private final boolean includeWeights;
	private final boolean shortLabels;
	private final boolean includeSelfLoops;

	// TODO Change constructor to plugin-default-constructor
	public ComponentDependencyGraphPluginAllocation(final Configuration configuration, final AbstractRepository repositories[], final File dotOutputFile,
			final boolean includeWeights, final boolean shortLabels, final boolean includeSelfLoops) {
		// TODO Check type conversion
		super(configuration, repositories, new DependencyGraph<AllocationComponent>(((SystemModelRepository) repositories[0]).getAllocationFactory()
				.getRootAllocationComponent()
				.getId(),
				((SystemModelRepository) repositories[0]).getAllocationFactory().getRootAllocationComponent()));
		this.dotOutputFile = dotOutputFile;
		this.includeWeights = includeWeights;
		this.shortLabels = shortLabels;
		this.includeSelfLoops = includeSelfLoops;
	}

	private String componentNodeLabel(final DependencyGraphNode<AllocationComponent> node, final boolean shortLabelsL) {
		final AllocationComponent component = node.getEntity();
		if (component == super.getSystemEntityFactory().getAllocationFactory().getRootAllocationComponent()) {
			return "$";
		}

		final String assemblyComponentName = component.getAssemblyComponent().getName();
		final String componentTypePackagePrefx = component.getAssemblyComponent().getType().getPackageName();
		final String componentTypeIdentifier = component.getAssemblyComponent().getType().getTypeName();

		final StringBuilder strBuild = new StringBuilder(AbstractDependencyGraphPlugin.STEREOTYPE_ALLOCATION_COMPONENT);
		strBuild.append("\\n");
		strBuild.append(assemblyComponentName).append(":");
		if (!shortLabelsL) {
			strBuild.append(componentTypePackagePrefx).append(".");
		} else {
			strBuild.append("..");
		}
		strBuild.append(componentTypeIdentifier);
		return strBuild.toString();
	}

	@Override
	protected void dotEdges(final Collection<DependencyGraphNode<AllocationComponent>> nodes, final PrintStream ps, final boolean shortLabelsL) {

		/* Execution container ID x DependencyGraphNode */
		final Map<Integer, Collection<DependencyGraphNode<AllocationComponent>>> component2containerMapping = new Hashtable<Integer, Collection<DependencyGraphNode<AllocationComponent>>>(); // NOPMD

		for (final DependencyGraphNode<AllocationComponent> node : nodes) {
			final int containerId = node.getEntity().getExecutionContainer().getId();
			Collection<DependencyGraphNode<AllocationComponent>> containedComponents = component2containerMapping.get(containerId);
			if (containedComponents == null) {
				containedComponents = new ArrayList<DependencyGraphNode<AllocationComponent>>(); // NOPMD (new in loop)
				component2containerMapping.put(containerId, containedComponents);
			}
			containedComponents.add(node);
		}

		final ExecutionContainer rootContainer = this.getSystemEntityFactory().getExecutionEnvironmentFactory().getRootExecutionContainer();
		final int rootContainerId = rootContainer.getId();
		final StringBuilder strBuild = new StringBuilder();
		for (final Entry<Integer, Collection<DependencyGraphNode<AllocationComponent>>> entry : component2containerMapping.entrySet()) {
			final int curContainerId = entry.getKey();
			final ExecutionContainer curContainer = this.getSystemEntityFactory().getExecutionEnvironmentFactory()
					.lookupExecutionContainerByContainerId(curContainerId);
			if (curContainerId == rootContainerId) {
				strBuild.append(DotFactory.createNode("", this.getNodeId(this.dependencyGraph.getRootNode()),
						this.componentNodeLabel(this.dependencyGraph.getRootNode(), shortLabelsL), DotFactory.DOT_SHAPE_NONE, null, // style
						null, // framecolor
						null, // fillcolor
						null, // fontcolor
						DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
						null, // imagefilename
						null // misc
						));
			} else {
				strBuild.append(DotFactory.createCluster("", ComponentDependencyGraphPluginAllocation.CONTAINER_NODE_ID_PREFIX + curContainer.getId(),
						AbstractDependencyGraphPlugin.STEREOTYPE_EXECUTION_CONTAINER + "\\n" + curContainer.getName(), DotFactory.DOT_SHAPE_BOX, // shape
						DotFactory.DOT_STYLE_FILLED, // style
						null, // framecolor
						DotFactory.DOT_FILLCOLOR_WHITE, // fillcolor
						null, // fontcolor
						DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
						null)); // misc
				// dot code for contained components
				for (final DependencyGraphNode<AllocationComponent> node : entry.getValue()) {
					strBuild.append(DotFactory.createNode("", this.getNodeId(node), this.componentNodeLabel(node, shortLabelsL), DotFactory.DOT_SHAPE_BOX,
							DotFactory.DOT_STYLE_FILLED, // style
							null, // framecolor
							DotFactory.DOT_FILLCOLOR_WHITE, // fillcolor
							null, // fontcolor
							DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
							null, // imagefilename
							null // misc
							));
				}
				strBuild.append("}\n");
			}
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
				ComponentDependencyGraphPluginAllocation.LOG.error("IOException", ex);
			}
		}
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		// TODO: Save the current configuration

		return configuration;
	}

	@Override
	@InputPort(description = "Message traces", eventTypes = { MessageTrace.class })
	public void msgTraceInput(final Object obj) {
		final MessageTrace t = (MessageTrace) obj;
		for (final AbstractMessage m : t.getSequenceAsVector()) {
			if (m instanceof SynchronousReplyMessage) {
				continue;
			}
			final AllocationComponent senderComponent = m.getSendingExecution().getAllocationComponent();
			final AllocationComponent receiverComponent = m.getReceivingExecution().getAllocationComponent();
			DependencyGraphNode<AllocationComponent> senderNode = ComponentDependencyGraphPluginAllocation.this.dependencyGraph.getNode(senderComponent.getId());
			DependencyGraphNode<AllocationComponent> receiverNode = ComponentDependencyGraphPluginAllocation.this.dependencyGraph.getNode(receiverComponent
					.getId());
			if (senderNode == null) {
				senderNode = new DependencyGraphNode<AllocationComponent>(senderComponent.getId(), senderComponent);
				ComponentDependencyGraphPluginAllocation.this.dependencyGraph.addNode(senderNode.getId(), senderNode);
			}
			if (receiverNode == null) {
				receiverNode = new DependencyGraphNode<AllocationComponent>(receiverComponent.getId(), receiverComponent);
				ComponentDependencyGraphPluginAllocation.this.dependencyGraph.addNode(receiverNode.getId(), receiverNode);
			}
			senderNode.addOutgoingDependency(receiverNode);
			receiverNode.addIncomingDependency(senderNode);
		}
		ComponentDependencyGraphPluginAllocation.this.reportSuccess(t.getTraceId());
	}

	@Override
	protected AbstractRepository[] getDefaultRepositories() {
		return new AbstractRepository[0];
	}
}
