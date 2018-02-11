/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.filter.visualization.callTree;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.common.util.signature.Signature;
import kieker.tools.traceAnalysis.filter.AbstractMessageTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.traceReconstruction.TraceProcessingException;
import kieker.tools.traceAnalysis.filter.visualization.graph.NoOriginRetentionPolicy;
import kieker.tools.traceAnalysis.filter.visualization.util.dot.DotFactory;
import kieker.tools.traceAnalysis.systemModel.AbstractMessage;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.SynchronousCallMessage;
import kieker.tools.traceAnalysis.systemModel.SynchronousReplyMessage;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.tools.traceAnalysis.systemModel.util.AllocationComponentOperationPair;
import kieker.tools.traceAnalysis.systemModel.util.AssemblyComponentOperationPair;

/**
 * Plugin providing the creation of calling trees both for individual traces and an aggregated form multiple traces.
 * 
 * @param <T>
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.1
 */
@Plugin(repositoryPorts = { @RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class) })
public abstract class AbstractCallTreeFilter<T> extends AbstractMessageTraceProcessingFilter {

	private static final String ENCODING = "UTF-8";

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public AbstractCallTreeFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	private static final String assemblyComponentOperationPairNodeLabel(final AbstractCallTreeNode<AssemblyComponentOperationPair> node, final boolean shortLabels) {
		final AssemblyComponentOperationPair p = node.getEntity();
		final AssemblyComponent component = p.getAssemblyComponent();
		final Operation operation = p.getOperation();
		final String assemblyComponentName = component.getName();
		final String componentTypePackagePrefx = component.getType().getPackageName();
		final String componentTypeIdentifier = component.getType().getTypeName();

		final StringBuilder strBuild = new StringBuilder(assemblyComponentName).append(":");
		if (!shortLabels) {
			strBuild.append(componentTypePackagePrefx).append('.');
		} else {
			strBuild.append("..");
		}
		strBuild.append(componentTypeIdentifier).append("\\n.");

		final Signature sig = operation.getSignature();
		final StringBuilder opLabel = new StringBuilder(sig.getName());
		opLabel.append('(');
		final String[] paramList = sig.getParamTypeList();
		if (paramList.length > 0) {
			opLabel.append("..");
		}
		opLabel.append(')');

		strBuild.append(opLabel.toString());
		return strBuild.toString();
	}

	private static final String allocationComponentOperationPairNodeLabel(final AbstractCallTreeNode<AllocationComponentOperationPair> node,
			final boolean shortLabels) {
		final AllocationComponentOperationPair p = node.getEntity();
		final AllocationComponent component = p.getAllocationComponent();
		final Operation operation = p.getOperation();
		final String resourceContainerName = component.getExecutionContainer().getName();
		final String assemblyComponentName = component.getAssemblyComponent().getName();
		final String componentTypePackagePrefx = component.getAssemblyComponent().getType().getPackageName();
		final String componentTypeIdentifier = component.getAssemblyComponent().getType().getTypeName();

		final StringBuilder strBuild = new StringBuilder(resourceContainerName).append("::\\n").append(assemblyComponentName).append(":");
		if (!shortLabels) {
			strBuild.append(componentTypePackagePrefx).append('.');
		} else {
			strBuild.append("..");
		}
		strBuild.append(componentTypeIdentifier).append("\\n.");

		final Signature sig = operation.getSignature();
		final StringBuilder opLabel = new StringBuilder(sig.getName());
		opLabel.append('(');
		final String[] paramList = sig.getParamTypeList();
		if (paramList.length > 0) {
			opLabel.append("..");
		}
		opLabel.append(')');

		strBuild.append(opLabel.toString());

		return strBuild.toString();
	}

	@SuppressWarnings("unchecked")
	// javac reports unchecked casts
	protected static final String nodeLabel(final AbstractCallTreeNode<?> node, final boolean shortLabels) {
		if (node.getEntity() instanceof AllocationComponentOperationPair) {
			return AbstractCallTreeFilter.allocationComponentOperationPairNodeLabel((AbstractCallTreeNode<AllocationComponentOperationPair>) node, shortLabels);
		} else if (node.getEntity() instanceof AssemblyComponentOperationPair) {
			return AbstractCallTreeFilter.assemblyComponentOperationPairNodeLabel((AbstractCallTreeNode<AssemblyComponentOperationPair>) node, shortLabels);
		} else {
			throw new UnsupportedOperationException("Node type not supported: " + node.getEntity().getClass().getName());
		}
	}

	/**
	 * Traverse tree recursively and generate dot code for edges.
	 * 
	 * @param n
	 *            The root node to start with.
	 * @param nodeIds
	 *            The map containing the node IDs.
	 * @param nextNodeId
	 *            The counter for the IDs of the nodes.
	 * @param ps
	 *            The stream in which the dot code will be written.
	 * @param shortLabels
	 *            Determines whether to use short labels or not.
	 */
	private static void dotEdgesFromSubTree(final AbstractCallTreeNode<?> n,
			final Map<AbstractCallTreeNode<?>, Integer> nodeIds, final AtomicInteger nextNodeId, final PrintStream ps, final boolean shortLabels) {
		final int newNodeId = nextNodeId.getAndIncrement();
		nodeIds.put(n, newNodeId);

		final StringBuilder strBuild = new StringBuilder(64);

		final String labelText = n.isRootNode() ? SystemModelRepository.ROOT_NODE_LABEL // NOCS
				: AbstractCallTreeFilter.nodeLabel(n, shortLabels); // NOCS

		strBuild.append(newNodeId)
				.append("[label =\"")
				.append(labelText) // NOCS
				.append("\",shape=" + DotFactory.DOT_SHAPE_NONE + "];");
		ps.println(strBuild.toString());

		// ensure a deterministic order in n.getChildEdges() varies among multiple runs
		final List<WeightedDirectedCallTreeEdge<?>> sortedChildren = new ArrayList<WeightedDirectedCallTreeEdge<?>>(n.getChildEdges());
		final Comparator<? super WeightedDirectedCallTreeEdge<?>> comparator = new Comparator<WeightedDirectedCallTreeEdge<?>>() {
			@Override
			public int compare(WeightedDirectedCallTreeEdge<?> o1, WeightedDirectedCallTreeEdge<?> o2) {
				return Integer.compare(o1.getTarget().getId(), o2.getTarget().getId());
			}
		};
		Collections.sort(sortedChildren, comparator);

		for (final WeightedDirectedCallTreeEdge<?> child : sortedChildren) {
			final AbstractCallTreeNode<?> targetNode = child.getTarget();
			AbstractCallTreeFilter.dotEdgesFromSubTree(targetNode, nodeIds, nextNodeId, ps, shortLabels);
		}
	}

	/**
	 * Traverse tree recursively and generate dot code for vertices.
	 * 
	 * @param n
	 *            The root node.
	 * @param eoiCounter
	 *            The counter for the execution order index.
	 * @param nodeIds
	 *            The map containing the node IDs.
	 * @param ps
	 *            The stream on which the generated code will be printed.
	 * @param includeWeights
	 *            Determines whether to include weights or not.
	 */
	private static void dotVerticesFromSubTree(final AbstractCallTreeNode<?> n, final AtomicInteger eoiCounter,
			final Map<AbstractCallTreeNode<?>, Integer> nodeIds, final PrintStream ps, final boolean includeWeights) {
		final int thisId = nodeIds.get(n);
		for (final WeightedDirectedCallTreeEdge<?> child : n.getChildEdges()) {
			final StringBuilder strBuild = new StringBuilder(1024);
			final int childId = nodeIds.get(child.getTarget());
			strBuild.append('\n').append(thisId).append("->").append(childId).append("[style=solid,arrowhead=none");
			if (includeWeights) {
				strBuild.append(",label=\"").append(child.getTargetWeight().get()).append('"');
			} else if (eoiCounter != null) {
				strBuild.append(",label=\"").append(eoiCounter.getAndIncrement()).append(".\"");
			}
			strBuild.append(" ]");
			ps.println(strBuild.toString());
			AbstractCallTreeFilter.dotVerticesFromSubTree(child.getTarget(), eoiCounter, nodeIds, ps, includeWeights);
		}
	}

	/**
	 * This method converts the given tree completely into dot code.
	 * 
	 * @param root
	 *            The root of the tree.
	 * @param ps
	 *            The stream in which the dot code will be written.
	 * @param includeWeights
	 *            Determines whether to include weights or not.
	 * @param includeEois
	 *            Determines whether to include the execution order indices or not.
	 * @param shortLabels
	 *            Determines whether to use short labels or not.
	 */
	private static void dotFromCallingTree(final AbstractCallTreeNode<?> root, final PrintStream ps,
			final boolean includeWeights, final boolean includeEois, final boolean shortLabels) {
		// preamble:
		ps.println("digraph G {");
		final StringBuilder edgestringBuilder = new StringBuilder();

		final Map<AbstractCallTreeNode<?>, Integer> nodeIds = new Hashtable<AbstractCallTreeNode<?>, Integer>(); // NOPMD (not synchronized)

		AbstractCallTreeFilter.dotEdgesFromSubTree(root, nodeIds, new AtomicInteger(0), ps, shortLabels);
		AbstractCallTreeFilter.dotVerticesFromSubTree(root, includeEois ? new AtomicInteger(1) : null, nodeIds, ps, includeWeights); // NOPMD NOCS (null)

		ps.println(edgestringBuilder.toString());
		ps.println("}");
	}

	/**
	 * This method saves the given tree as valid dot code into the given file.
	 * 
	 * @param root
	 *            The root of the tree.
	 * @param outputFn
	 *            The file in which the code will be written.
	 * @param includeWeights
	 *            Determines whether to include weights or not.
	 * @param includeEois
	 *            Determines whether to include the execution order indices or not.
	 * @param shortLabels
	 *            Determines whether to use short labels or not.
	 * @throws FileNotFoundException
	 *             If the given file is somehow invalid.
	 * 
	 * @throws UnsupportedEncodingException
	 *             If the default encoding is not supported.
	 */
	protected static void saveTreeToDotFile(final AbstractCallTreeNode<?> root, final String outputFn,
			final boolean includeWeights, final boolean includeEois, final boolean shortLabels) throws FileNotFoundException, UnsupportedEncodingException {
		final PrintStream ps = new PrintStream(new FileOutputStream(outputFn), false, ENCODING);
		AbstractCallTreeFilter.dotFromCallingTree(root, ps, includeWeights, includeEois, shortLabels);
		ps.flush();
		ps.close();
	}

	/**
	 * Adds the given trace to the given tree.
	 * 
	 * @param root
	 *            The root of the call tree.
	 * @param t
	 *            The trace to add.
	 * @param pairFactory
	 *            The factory creating the necessary pairs for the tree.
	 * @param aggregated
	 *            Determines whether the tree is aggregated or not.
	 * 
	 * @throws TraceProcessingException
	 *             If the message type is not supported or the trace is somehow invalid.
	 * 
	 * @param <T>
	 *            The type of the tree.
	 */
	protected static <T> void addTraceToTree(final AbstractCallTreeNode<T> root, final MessageTrace t, final IPairFactory<T> pairFactory, final boolean aggregated)
			throws TraceProcessingException {
		final Stack<AbstractCallTreeNode<T>> curStack = new Stack<AbstractCallTreeNode<T>>();

		final Collection<AbstractMessage> msgTraceVec = t.getSequenceAsVector();
		AbstractCallTreeNode<T> curNode = root;
		curStack.push(curNode);
		for (final AbstractMessage m : msgTraceVec) {
			if (m instanceof SynchronousCallMessage) {
				curNode = curStack.peek();
				AbstractCallTreeNode<T> child;
				child = curNode.newCall(pairFactory.createPair((SynchronousCallMessage) m), t, NoOriginRetentionPolicy.createInstance());
				curNode = child;
				curStack.push(curNode);
			} else if (m instanceof SynchronousReplyMessage) {
				curNode = curStack.pop();
			} else {
				throw new TraceProcessingException("Message type not supported:" + m.getClass().getName());
			}
		}
		if (curStack.pop() != root) {
			throw new TraceProcessingException("Stack not empty after processing trace");
		}
	}

	public static <T> void writeDotForMessageTrace(final AbstractCallTreeNode<T> root, final IPairFactory<T> pairFactory, final MessageTrace msgTrace,
			final String outputFilename, final boolean includeWeights, final boolean shortLabels) throws FileNotFoundException, TraceProcessingException,
			UnsupportedEncodingException {

		AbstractCallTreeFilter.<T> addTraceToTree(root, msgTrace, pairFactory, false); // false: no aggregation
		AbstractCallTreeFilter.saveTreeToDotFile(root, outputFilename, includeWeights, true, shortLabels); // includeEois
	}

	/**
	 * @author Andre van Hoorn
	 * 
	 * @since 1.5
	 */
	public interface IPairFactory<T> {

		/**
		 * This method creates an actual pair using the given call message.
		 * 
		 * @param callMsg
		 *            The call message containing the necessary information to create the pair.
		 * 
		 * @return The actual pair.
		 * 
		 * @since 1.5
		 */
		public T createPair(final SynchronousCallMessage callMsg);
	}
}
