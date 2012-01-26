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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import kieker.analysis.plugin.port.InputPort;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.traceAnalysis.plugins.AbstractTraceAnalysisPlugin;
import kieker.tools.traceAnalysis.plugins.visualization.util.dot.DotFactory;
import kieker.tools.traceAnalysis.systemModel.AbstractMessage;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.Signature;
import kieker.tools.traceAnalysis.systemModel.SynchronousReplyMessage;
import kieker.tools.traceAnalysis.systemModel.repository.AbstractSystemSubRepository;
import kieker.tools.traceAnalysis.systemModel.repository.AllocationComponentOperationPairFactory;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.tools.traceAnalysis.systemModel.util.AllocationComponentOperationPair;

/**
 * Refactored copy from LogAnalysis-legacy tool<br>
 * 
 * This class has exactly one input port named "in". The data which is send to
 * this plugin is not delegated in any way.
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr,
 */
public class OperationDependencyGraphPluginAllocation extends AbstractDependencyGraphPlugin<AllocationComponentOperationPair> {

	public static final String CONFIG_DOT_OUTPUT_FILE = OperationDependencyGraphPluginAllocation.class.getName() + ".dotOutputFile";
	public static final String CONFIG_INCLUDE_WEIGHTS = OperationDependencyGraphPluginAllocation.class.getName() + ".includeWeights";
	public static final String CONFIG_SHORT_LABELS = OperationDependencyGraphPluginAllocation.class.getName() + ".shortLabels";
	public static final String CONFIG_INCLUDE_SELF_LOOPS = OperationDependencyGraphPluginAllocation.class.getName() + ".includeSelfLoops";

	private static final Log LOG = LogFactory.getLog(OperationDependencyGraphPluginAllocation.class);
	private static final String COMPONENT_NODE_ID_PREFIX = "component_";
	private static final String CONTAINER_NODE_ID_PREFIX = "container_";
	private final AllocationComponentOperationPairFactory pairFactory;
	private final File dotOutputFile;
	private final boolean includeWeights;
	private final boolean shortLabels;
	private final boolean includeSelfLoops;

	public OperationDependencyGraphPluginAllocation(final Configuration configuration, final Map<String, AbstractRepository> repositories) {
		// TODO Check type conversion
		super(configuration, repositories, new DependencyGraph<AllocationComponentOperationPair>(AbstractSystemSubRepository.ROOT_ELEMENT_ID,
				new AllocationComponentOperationPair(AbstractSystemSubRepository.ROOT_ELEMENT_ID,
						((SystemModelRepository) repositories.get(AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME)).getOperationFactory()
								.getRootOperation(),
						((SystemModelRepository) repositories.get(AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME)).getAllocationFactory()
								.getRootAllocationComponent())));
		this.pairFactory = new AllocationComponentOperationPairFactory(
				((SystemModelRepository) repositories.get(AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME)));
		this.dotOutputFile = new File(this.configuration.getStringProperty(OperationDependencyGraphPluginAllocation.CONFIG_DOT_OUTPUT_FILE));
		this.includeWeights = this.configuration.getBooleanProperty(OperationDependencyGraphPluginAllocation.CONFIG_INCLUDE_WEIGHTS);
		this.shortLabels = this.configuration.getBooleanProperty(OperationDependencyGraphPluginAllocation.CONFIG_SHORT_LABELS);
		this.includeSelfLoops = this.configuration.getBooleanProperty(OperationDependencyGraphPluginAllocation.CONFIG_INCLUDE_SELF_LOOPS);
	}

	private String containerNodeLabel(final ExecutionContainer container) {
		return String.format("%s\\n%s", AbstractDependencyGraphPlugin.STEREOTYPE_EXECUTION_CONTAINER, container.getName());
	}

	private String componentNodeLabel(final AllocationComponent component, final boolean shortLabelsL) {
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
	protected void dotEdges(final Collection<DependencyGraphNode<AllocationComponentOperationPair>> nodes, final PrintStream ps, final boolean shortLabelsL) {

		/* Execution container ID x contained components */
		final Map<Integer, Collection<AllocationComponent>> containerId2componentMapping = new Hashtable<Integer, Collection<AllocationComponent>>(); // NOPMD
		final Map<Integer, Collection<DependencyGraphNode<AllocationComponentOperationPair>>> componentId2pairMapping = new Hashtable<Integer, Collection<DependencyGraphNode<AllocationComponentOperationPair>>>(); // NOPMD

		// Derive container / component / operation hieraŕchy
		for (final DependencyGraphNode<AllocationComponentOperationPair> pairNode : nodes) {
			final AllocationComponent curComponent = pairNode.getEntity().getAllocationComponent();
			final ExecutionContainer curContainer = curComponent.getExecutionContainer();
			final int componentId = curComponent.getId();
			final int containerId = curContainer.getId();

			Collection<DependencyGraphNode<AllocationComponentOperationPair>> containedPairs = componentId2pairMapping.get(componentId);
			if (containedPairs == null) {
				// component not yet registered
				containedPairs = new ArrayList<DependencyGraphNode<AllocationComponentOperationPair>>(); // NOPMD (new in loop)
				componentId2pairMapping.put(componentId, containedPairs);
				Collection<AllocationComponent> containedComponents = containerId2componentMapping.get(containerId);
				if (containedComponents == null) {
					containedComponents = new ArrayList<AllocationComponent>(); // NOPMD (new in loop)
					containerId2componentMapping.put(containerId, containedComponents);
				}
				containedComponents.add(curComponent);
			}
			containedPairs.add(pairNode);
		}

		final ExecutionContainer rootContainer = this.getSystemEntityFactory().getExecutionEnvironmentFactory().getRootExecutionContainer();
		final int rootContainerId = rootContainer.getId();
		final StringBuilder strBuild = new StringBuilder();
		for (final Entry<Integer, Collection<AllocationComponent>> containerComponentEntry : containerId2componentMapping.entrySet()) {
			final int curContainerId = containerComponentEntry.getKey();
			final ExecutionContainer curContainer = this.getSystemEntityFactory().getExecutionEnvironmentFactory()
					.lookupExecutionContainerByContainerId(curContainerId);

			if (curContainerId == rootContainerId) {
				strBuild.append(DotFactory.createNode("", this.getNodeId(this.dependencyGraph.getRootNode()), "$", DotFactory.DOT_SHAPE_NONE, null, // style
						null, // framecolor
						null, // fillcolor
						null, // fontcolor
						DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
						null, // imagefilename
						null // misc
						));
			} else {
				strBuild.append(DotFactory.createCluster("", OperationDependencyGraphPluginAllocation.CONTAINER_NODE_ID_PREFIX + curContainer.getId(),
						this.containerNodeLabel(curContainer), DotFactory.DOT_SHAPE_BOX, // shape
						DotFactory.DOT_STYLE_FILLED, // style
						null, // framecolor
						DotFactory.DOT_FILLCOLOR_WHITE, // fillcolor
						null, // fontcolor
						DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
						null)); // misc
				// dot code for contained components
				for (final AllocationComponent curComponent : containerComponentEntry.getValue()) {
					final int curComponentId = curComponent.getId();
					strBuild.append(DotFactory.createCluster("", OperationDependencyGraphPluginAllocation.COMPONENT_NODE_ID_PREFIX + curComponentId,
							this.componentNodeLabel(curComponent, shortLabelsL), DotFactory.DOT_SHAPE_BOX, DotFactory.DOT_STYLE_FILLED, // style
							null, // framecolor
							DotFactory.DOT_FILLCOLOR_WHITE, // fillcolor
							null, // fontcolor
							DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
							null // misc
							));
					for (final DependencyGraphNode<AllocationComponentOperationPair> curPair : componentId2pairMapping.get(curComponentId)) { // NOCS (NestedFor)
						final Signature sig = curPair.getEntity().getOperation().getSignature();
						final StringBuilder opLabel = new StringBuilder(sig.getName()); // NOPMD (new in loop)
						opLabel.append("(");
						final String[] paramList = sig.getParamTypeList();
						if ((paramList != null) && (paramList.length > 0)) {
							opLabel.append("..");
						}
						opLabel.append(")");
						strBuild.append(DotFactory.createNode("", this.getNodeId(curPair), opLabel.toString(), DotFactory.DOT_SHAPE_OVAL,
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
				OperationDependencyGraphPluginAllocation.LOG.error("IOException", ex);
			}
		}
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.put(OperationDependencyGraphPluginAllocation.CONFIG_DOT_OUTPUT_FILE, "");
		configuration.put(OperationDependencyGraphPluginAllocation.CONFIG_INCLUDE_WEIGHTS, false);
		configuration.put(OperationDependencyGraphPluginAllocation.CONFIG_INCLUDE_SELF_LOOPS, false);
		configuration.put(OperationDependencyGraphPluginAllocation.CONFIG_SHORT_LABELS, false);

		return configuration;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.put(OperationDependencyGraphPluginAllocation.CONFIG_DOT_OUTPUT_FILE, this.dotOutputFile.getAbsolutePath());
		configuration.put(OperationDependencyGraphPluginAllocation.CONFIG_INCLUDE_WEIGHTS, this.includeWeights);
		configuration.put(OperationDependencyGraphPluginAllocation.CONFIG_INCLUDE_SELF_LOOPS, this.includeSelfLoops);
		configuration.put(OperationDependencyGraphPluginAllocation.CONFIG_SHORT_LABELS, this.shortLabels);

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
			final int rootOperationId = OperationDependencyGraphPluginAllocation.this.getSystemEntityFactory().getOperationFactory().getRootOperation().getId();
			final Operation senderOperation = m.getSendingExecution().getOperation();
			final Operation receiverOperation = m.getReceivingExecution().getOperation();
			/* The following two get-calls to the factory return s.th. in either case */
			final AllocationComponentOperationPair senderPair = (senderOperation.getId() == rootOperationId) ? OperationDependencyGraphPluginAllocation.this.dependencyGraph // NOCS
					.getRootNode().getEntity()
					: OperationDependencyGraphPluginAllocation.this.pairFactory.getPairInstanceByPair(senderComponent, senderOperation);
			final AllocationComponentOperationPair receiverPair = (receiverOperation.getId() == rootOperationId) ? OperationDependencyGraphPluginAllocation.this.dependencyGraph // NOCS
					.getRootNode().getEntity()
					: OperationDependencyGraphPluginAllocation.this.pairFactory.getPairInstanceByPair(receiverComponent, receiverOperation);

			DependencyGraphNode<AllocationComponentOperationPair> senderNode = OperationDependencyGraphPluginAllocation.this.dependencyGraph.getNode(senderPair
					.getId());
			DependencyGraphNode<AllocationComponentOperationPair> receiverNode = OperationDependencyGraphPluginAllocation.this.dependencyGraph
					.getNode(receiverPair.getId());
			if (senderNode == null) {
				senderNode = new DependencyGraphNode<AllocationComponentOperationPair>(senderPair.getId(), senderPair);
				OperationDependencyGraphPluginAllocation.this.dependencyGraph.addNode(senderNode.getId(), senderNode);
			}
			if (receiverNode == null) {
				receiverNode = new DependencyGraphNode<AllocationComponentOperationPair>(receiverPair.getId(), receiverPair);
				OperationDependencyGraphPluginAllocation.this.dependencyGraph.addNode(receiverNode.getId(), receiverNode);
			}
			senderNode.addOutgoingDependency(receiverNode);
			receiverNode.addIncomingDependency(senderNode);
		}
		OperationDependencyGraphPluginAllocation.this.reportSuccess(t.getTraceId());
	}

	@Override
	protected Map<String, AbstractRepository> getDefaultRepositories() {
		return new HashMap<String, AbstractRepository>();
	}
}
