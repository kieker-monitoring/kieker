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

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.AbstractMessageTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.systemModel.AbstractMessage;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.SynchronousReplyMessage;
import kieker.tools.traceAnalysis.systemModel.repository.AllocationRepository;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * Refactored copy from LogAnalysis-legacy tool<br>
 * 
 * This class has exactly one input port named "in". The data which is send to
 * this plugin is not delegated in any way.
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr, Jan Waller
 */
@Plugin(repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class),
		outputPorts = @OutputPort(name = ComponentDependencyGraphAllocationFilter.OUTPUT_PORT_NAME, eventTypes = { ComponentAllocationDependencyGraph.class }))
public class ComponentDependencyGraphAllocationFilter extends AbstractDependencyGraphFilter<AllocationComponent> {
	public static final String CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE = "dotOutputFn";
	public static final String CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS = "includeWeights";
	public static final String CONFIG_PROPERTY_NAME_SHORTLABELS = "shortLabels";
	public static final String CONFIG_PROPERTY_NAME_SELFLOOPS = "selfLoops";

	public static final String OUTPUT_PORT_NAME = "graphOutput";

	public static final String CONFIG_PROPERTY_VALUE_OUTPUT_FN_BASE_DEFAULT = "AllocationComponentDependencyGraph";

	private final String dotOutputFile;
	private final boolean includeWeights;
	private final boolean shortLabels;
	private final boolean includeSelfLoops;

	public ComponentDependencyGraphAllocationFilter(final Configuration configuration) {
		// TODO Check type conversion??
		super(configuration);
		super.setDependencyGraph(new ComponentAllocationDependencyGraph(AllocationRepository.ROOT_ALLOCATION_COMPONENT));
		this.dotOutputFile = configuration.getStringProperty(CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE);
		this.includeWeights = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS);
		this.shortLabels = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_SHORTLABELS);
		this.includeSelfLoops = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_SELFLOOPS);
	}

	@Override
	public void terminate(final boolean error) {
		this.deliver(OUTPUT_PORT_NAME, this.dependencyGraph);
	}

	@Override
	protected final Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE,
				CONFIG_PROPERTY_VALUE_OUTPUT_FN_BASE_DEFAULT);
		configuration.setProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, Boolean.TRUE.toString());
		configuration.setProperty(CONFIG_PROPERTY_NAME_SHORTLABELS, Boolean.TRUE.toString());
		configuration.setProperty(CONFIG_PROPERTY_NAME_SELFLOOPS, Boolean.FALSE.toString());
		return configuration;
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
			}
			else {
				senderNode.addOrigin(t);
			}

			if (receiverNode == null) {
				receiverNode = new DependencyGraphNode<AllocationComponent>(receiverComponent.getId(), receiverComponent, t);

				if (m.getReceivingExecution().isAssumed()) {
					receiverNode.setAssumed();
				}

				ComponentDependencyGraphAllocationFilter.this.dependencyGraph.addNode(receiverNode.getId(), receiverNode);
			}
			else {
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
