/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
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
import kieker.tools.traceAnalysis.systemModel.repository.AllocationRepository;
import kieker.tools.traceAnalysis.systemModel.repository.ExecutionEnvironmentRepository;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * Refactored copy from LogAnalysis-legacy tool<br>
 * 
 * This class has exactly one input port named "in". The data which is send to
 * this plugin is not delegated in any way.
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr, Jan Waller
 */
@Plugin(description = "Uses the incoming data to enrich the connected repository with data for the component allocation dependency graph",
		repositoryPorts = {
			@RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class)
		},
		configuration = {
			@Property(name = ComponentDependencyGraphAllocationFilter.CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE, defaultValue = ComponentDependencyGraphAllocationFilter.CONFIG_PROPERTY_VALUE_OUTPUT_FN_BASE_DEFAULT),
			@Property(name = ComponentDependencyGraphAllocationFilter.CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, defaultValue = "true"),
			@Property(name = ComponentDependencyGraphAllocationFilter.CONFIG_PROPERTY_NAME_SHORTLABELS, defaultValue = "true"),
			@Property(name = ComponentDependencyGraphAllocationFilter.CONFIG_PROPERTY_NAME_SELFLOOPS, defaultValue = "false")
		})
public class ComponentDependencyGraphAllocationFilter extends AbstractDependencyGraphFilter<AllocationComponent> {

	public static final String CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE = "dotOutputFn";
	public static final String CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS = "includeWeights";
	public static final String CONFIG_PROPERTY_NAME_SHORTLABELS = "shortLabels";
	public static final String CONFIG_PROPERTY_NAME_SELFLOOPS = "selfLoops";

	public static final String CONFIG_PROPERTY_VALUE_OUTPUT_FN_BASE_DEFAULT = "AllocationComponentDependencyGraph";

	private static final String CONTAINER_NODE_ID_PREFIX = "container";

	private static final Log LOG = LogFactory.getLog(ComponentDependencyGraphAllocationFilter.class);

	private final String dotOutputFile;
	private final boolean includeWeights;
	private final boolean shortLabels;
	private final boolean includeSelfLoops;

	public ComponentDependencyGraphAllocationFilter(final Configuration configuration) {
		// TODO Check type conversion??
		super(configuration, new DependencyGraph<AllocationComponent>(AllocationRepository.ROOT_ALLOCATION_COMPONENT.getId(),
				AllocationRepository.ROOT_ALLOCATION_COMPONENT));
		this.dotOutputFile = configuration.getStringProperty(CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE);
		this.includeWeights = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS);
		this.shortLabels = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_SHORTLABELS);
		this.includeSelfLoops = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_SELFLOOPS);
	}

	private String componentNodeLabel(final DependencyGraphNode<AllocationComponent> node, final boolean shortLabelsL) {
		final AllocationComponent component = node.getEntity();
		if (component == AllocationRepository.ROOT_ALLOCATION_COMPONENT) {
			return "$";
		}

		final String assemblyComponentName = component.getAssemblyComponent().getName();
		final String componentTypePackagePrefx = component.getAssemblyComponent().getType().getPackageName();
		final String componentTypeIdentifier = component.getAssemblyComponent().getType().getTypeName();

		final StringBuilder strBuild = new StringBuilder(AbstractDependencyGraphFilter.STEREOTYPE_ALLOCATION_COMPONENT);
		strBuild.append("\\n");
		strBuild.append(assemblyComponentName).append(':');
		if (!shortLabelsL) {
			strBuild.append(componentTypePackagePrefx).append('.');
		} else {
			strBuild.append("..");
		}
		strBuild.append(componentTypeIdentifier);

		this.addDecorationText(strBuild, node);

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
				containedComponents = new ArrayList<DependencyGraphNode<AllocationComponent>>();
				component2containerMapping.put(containerId, containedComponents);
			}
			containedComponents.add(node);
		}
		final ExecutionContainer rootContainer = ExecutionEnvironmentRepository.ROOT_EXECUTION_CONTAINER;
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
				strBuild.append(DotFactory.createCluster("", CONTAINER_NODE_ID_PREFIX + curContainer.getId(),
						AbstractDependencyGraphFilter.STEREOTYPE_EXECUTION_CONTAINER + "\\n" + curContainer.getName(), DotFactory.DOT_SHAPE_BOX, // shape
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
							this.getNodeFillColor(node), // fillcolor
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

	/**
	 * Saves the dependency graph to the dot file if error is not true.
	 * 
	 * @param error
	 */

	@Override
	public void terminate(final boolean error) {
		if (!error) {
			try {
				this.saveToDotFile(this.dotOutputFile, this.includeWeights, this.shortLabels, this.includeSelfLoops);
			} catch (final IOException ex) {
				LOG.error("IOException while saving to dot file", ex);
			}
		}
	}

	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE, this.dotOutputFile);
		configuration.setProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, Boolean.toString(this.includeWeights));
		configuration.setProperty(CONFIG_PROPERTY_NAME_SHORTLABELS, Boolean.toString(this.shortLabels));
		configuration.setProperty(CONFIG_PROPERTY_NAME_SELFLOOPS, Boolean.toString(this.includeSelfLoops));
		return configuration;
	}

	@Override
	@InputPort(name = AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES, description = "Message traces", eventTypes = { MessageTrace.class })
	public void inputMessageTraces(final MessageTrace t) {
		for (final AbstractMessage m : t.getSequenceAsVector()) {
			if (m instanceof SynchronousReplyMessage) {
				continue;
			}
			final AllocationComponent senderComponent = m.getSendingExecution().getAllocationComponent();
			final AllocationComponent receiverComponent = m.getReceivingExecution().getAllocationComponent();
			DependencyGraphNode<AllocationComponent> senderNode = ComponentDependencyGraphAllocationFilter.this.dependencyGraph.getNode(senderComponent.getId());
			DependencyGraphNode<AllocationComponent> receiverNode = ComponentDependencyGraphAllocationFilter.this.dependencyGraph.getNode(receiverComponent
					.getId());
			if (senderNode == null) {
				senderNode = new DependencyGraphNode<AllocationComponent>(senderComponent.getId(), senderComponent, t);

				if (m.getSendingExecution().isAssumed()) {
					senderNode.setAssumed();
				}

				ComponentDependencyGraphAllocationFilter.this.dependencyGraph.addNode(senderNode.getId(), senderNode);
			} else {
				senderNode.addOrigin(t);
			}

			if (receiverNode == null) {
				receiverNode = new DependencyGraphNode<AllocationComponent>(receiverComponent.getId(), receiverComponent, t);

				if (m.getReceivingExecution().isAssumed()) {
					receiverNode.setAssumed();
				}

				ComponentDependencyGraphAllocationFilter.this.dependencyGraph.addNode(receiverNode.getId(), receiverNode);
			} else {
				receiverNode.addOrigin(t);
			}

			final boolean assumed = this.isDependencyAssumed(senderNode, receiverNode);

			senderNode.addOutgoingDependency(receiverNode, assumed, t);
			receiverNode.addIncomingDependency(senderNode, assumed, t);

			this.invokeDecorators(m, senderNode, receiverNode);
		}
		ComponentDependencyGraphAllocationFilter.this.reportSuccess(t.getTraceId());
	}
}
