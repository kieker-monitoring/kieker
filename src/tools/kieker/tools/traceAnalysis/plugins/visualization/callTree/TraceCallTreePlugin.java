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

package kieker.tools.traceAnalysis.plugins.visualization.callTree;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Stack;

import kieker.analysis.plugin.port.InputPort;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.traceAnalysis.plugins.AbstractMessageTraceProcessingPlugin;
import kieker.tools.traceAnalysis.plugins.traceReconstruction.TraceProcessingException;
import kieker.tools.traceAnalysis.plugins.visualization.util.IntContainer;
import kieker.tools.traceAnalysis.plugins.visualization.util.dot.DotFactory;
import kieker.tools.traceAnalysis.systemModel.AbstractMessage;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.Signature;
import kieker.tools.traceAnalysis.systemModel.SynchronousCallMessage;
import kieker.tools.traceAnalysis.systemModel.SynchronousReplyMessage;
import kieker.tools.traceAnalysis.systemModel.repository.AbstractRepository;
import kieker.tools.traceAnalysis.systemModel.repository.AbstractSystemSubRepository;
import kieker.tools.traceAnalysis.systemModel.repository.AllocationComponentOperationPairFactory;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * Plugin providing the creation of calling trees both for individual traces
 * and an aggregated form mulitple traces.<br>
 * 
 * This class has exactly one input port named "in". The data which is send to
 * this plugin is not delegated in any way.
 * 
 * @author Andre van Hoorn
 */
public class TraceCallTreePlugin extends AbstractMessageTraceProcessingPlugin {

	private static final Log LOG = LogFactory.getLog(TraceCallTreePlugin.class);
	private final CallTreeNode root;
	private final AllocationComponentOperationPairFactory allocationComponentOperationPairFactory;
	private final SystemModelRepository systemEntityFactory;
	private final String outputFnBase;
	private final boolean shortLabels;

	// TODO Change constructor to plugin-default-constructor
	public TraceCallTreePlugin(final Configuration configuration, final AbstractRepository repositories[],
			final AllocationComponentOperationPairFactory allocationComponentOperationPairFactory,
			final String outputFnBase, final boolean shortLabels) {
		super(configuration, repositories);
		this.allocationComponentOperationPairFactory = allocationComponentOperationPairFactory;
		// TODO Check type conversion
		this.systemEntityFactory = (SystemModelRepository) repositories[0];
		this.root = new CallTreeNode(null, // null: root node has no parent
				new CallTreeOperationHashKey(this.systemEntityFactory.getAllocationFactory().getRootAllocationComponent(),
						this.systemEntityFactory.getOperationFactory().getRootOperation()));
		this.outputFnBase = outputFnBase;
		this.shortLabels = shortLabels;
	}

	private static final String nodeLabel(final CallTreeNode node, final boolean shortLabels) {
		if (node.isRootNode()) {
			return "$";
		}

		final AllocationComponent component = node.getAllocationComponent();
		final Operation operation = node.getOperation();
		final String resourceContainerName = component.getExecutionContainer().getName();
		final String assemblyComponentName = component.getAssemblyComponent().getName();
		final String componentTypePackagePrefx = component.getAssemblyComponent().getType().getPackageName();
		final String componentTypeIdentifier = component.getAssemblyComponent().getType().getTypeName();

		final StringBuilder strBuild = new StringBuilder(resourceContainerName).append("::\\n").append(assemblyComponentName).append(":");
		if (!shortLabels) {
			strBuild.append(componentTypePackagePrefx);
		} else {
			strBuild.append("..");
		}
		strBuild.append(componentTypeIdentifier).append(".");

		final Signature sig = operation.getSignature();
		final StringBuilder opLabel = new StringBuilder(sig.getName());
		opLabel.append("(");
		final String[] paramList = sig.getParamTypeList();
		if ((paramList != null) && (paramList.length > 0)) {
			opLabel.append("..");
		}
		opLabel.append(")");

		strBuild.append(opLabel.toString());
		return strBuild.toString();
	}

	/** Traverse tree recursively and generate dot code for edges. */
	private static void dotEdgesFromSubTree(final SystemModelRepository systemEntityFactory, final CallTreeNode n, final Map<CallTreeNode, Integer> nodeIds,
			final IntContainer nextNodeId, final PrintStream ps, final boolean shortLabels) {
		final StringBuilder strBuild = new StringBuilder();
		nodeIds.put(n, nextNodeId.getValue());
		strBuild.append(nextNodeId.getAndIncValue()).append("[label =\"").append(TraceCallTreePlugin.nodeLabel(n, shortLabels))
				.append("\",shape=" + DotFactory.DOT_SHAPE_NONE + ",style=" + DotFactory.DOT_STYLE_FILLED + ",fillcolor=" + DotFactory.DOT_FILLCOLOR_WHITE + "];");
		ps.println(strBuild.toString());
		for (final CallTreeNode child : n.getChildren()) {
			TraceCallTreePlugin.dotEdgesFromSubTree(systemEntityFactory, child, nodeIds, nextNodeId, ps, shortLabels);
		}
	}

	/** Traverse tree recursively and generate dot code for vertices. */
	private static void dotVerticesFromSubTree(final CallTreeNode n, final Map<CallTreeNode, Integer> nodeIds, final PrintStream ps,
			final boolean includeWeights) {
		final int thisId = nodeIds.get(n);
		for (final CallTreeNode child : n.getChildren()) {
			final StringBuilder strBuild = new StringBuilder(); // NOPMD (new in loop)
			final int childId = nodeIds.get(child);
			strBuild.append("\n").append(thisId).append("->").append(childId).append("[style=solid,arrowhead=none");
			if (includeWeights) {
				strBuild.append(",label = ").append("").append(", weight =").append("");
			}
			strBuild.append(" ]");
			ps.println(strBuild.toString());
			TraceCallTreePlugin.dotVerticesFromSubTree(child, nodeIds, ps, includeWeights);
		}
	}

	private static void dotFromCallingTree(final SystemModelRepository systemEntityFactory, final CallTreeNode root, final PrintStream ps,
			final boolean includeWeights, final boolean shortLabels) {
		// preamble:
		ps.println("digraph G {");
		final StringBuilder edgestringBuilder = new StringBuilder();

		final Map<CallTreeNode, Integer> nodeIds = new Hashtable<CallTreeNode, Integer>(); // NOPMD (UseConcurrentHashMap)

		TraceCallTreePlugin.dotEdgesFromSubTree(systemEntityFactory, root, nodeIds, new IntContainer(0), ps, shortLabels);
		TraceCallTreePlugin.dotVerticesFromSubTree(root, nodeIds, ps, includeWeights);

		ps.println(edgestringBuilder.toString());
		ps.println("}");
	}

	private static void saveTreeToDotFile(final SystemModelRepository systemEntityFactory, final CallTreeNode root, final String outputFnBase,
			final boolean includeWeights, final boolean shortLabels) throws FileNotFoundException {
		final PrintStream ps = new PrintStream(new FileOutputStream(outputFnBase + ".dot"));
		TraceCallTreePlugin.dotFromCallingTree(systemEntityFactory, root, ps, includeWeights, shortLabels);
		ps.flush();
		ps.close();
	}

	public void saveTreeToDotFile(final String outputFnBaseL, final boolean includeWeightsL, final boolean shortLabelsL) throws FileNotFoundException {
		TraceCallTreePlugin.saveTreeToDotFile(this.systemEntityFactory, this.root, outputFnBaseL, includeWeightsL, shortLabelsL);
		this.printMessage(new String[] { "Wrote call tree to file '" + outputFnBaseL + ".dot" + "'", "Dot file can be converted using the dot tool",
			"Example: dot -T svg " + outputFnBaseL + ".dot" + " > " + outputFnBaseL + ".svg", });
	}

	private static void addTraceToTree(final CallTreeNode root, final MessageTrace t, final boolean aggregated) throws TraceProcessingException {
		final Stack<CallTreeNode> curStack = new Stack<CallTreeNode>();

		final Collection<AbstractMessage> msgTraceVec = t.getSequenceAsVector();
		CallTreeNode curNode = root;
		curStack.push(curNode);
		for (final AbstractMessage m : msgTraceVec) {
			if (m instanceof SynchronousCallMessage) {
				curNode = curStack.peek();
				CallTreeNode child;
				if (aggregated) {
					child = curNode.getChild(m.getReceivingExecution().getAllocationComponent(), m.getReceivingExecution().getOperation());
				} else {
					child = curNode.createNewChild(m.getReceivingExecution().getAllocationComponent(), m.getReceivingExecution().getOperation());
				}
				curNode = child;
				curStack.push(curNode);
			} else if (m instanceof SynchronousReplyMessage) {
				curNode = curStack.pop();
			} else {
				throw new TraceProcessingException("Message type not supported:" + m.getClass().getName());
			}
		}
		if (curStack.pop() != root) {
			final String errorMsg = "Stack not empty after processing trace";
			TraceCallTreePlugin.LOG.error(errorMsg);
			throw new TraceProcessingException(errorMsg);
		}
	}

	public static void writeDotForMessageTrace(final SystemModelRepository systemEntityFactory, final MessageTrace msgTrace, final String outputFilename,
			final boolean includeWeights, final boolean shortLabels) throws FileNotFoundException, TraceProcessingException {
		final CallTreeNode root = new CallTreeNode(null, new CallTreeOperationHashKey(systemEntityFactory.getAllocationFactory().getRootAllocationComponent(),
				systemEntityFactory.getOperationFactory().getRootOperation()));
		TraceCallTreePlugin.addTraceToTree(root, msgTrace, false); // false: no aggregation
		TraceCallTreePlugin.saveTreeToDotFile(systemEntityFactory, root, outputFilename, includeWeights, shortLabels);
	}

	@Override
	public void printStatusMessage() {
		super.printStatusMessage();
		final int numPlots = this.getSuccessCount();
		final long lastSuccessTracesId = this.getLastTraceIdSuccess();
		System.out.println("Wrote " + numPlots + " call tree" + (numPlots > 1 ? "s" : "") + " to file" + (numPlots > 1 ? "s" : "") + " with name pattern '" // NOCS
				+ this.outputFnBase + "-<traceId>.dot'");
		System.out.println("Dot files can be converted using the dot tool");
		System.out.println("Example: dot -T svg " + this.outputFnBase + "-" + ((numPlots > 0) ? lastSuccessTracesId : "<traceId>") + ".dot > " // NOCS
				+ this.outputFnBase + "-" + ((numPlots > 0) ? lastSuccessTracesId : "<traceId>") + ".svg"); // NOCS
	}

	@Override
	public boolean execute() {
		return true; // no need to do anything here
	}

	@Override
	public void terminate(final boolean error) {
		// no need to do anything here
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
		final MessageTrace mt = (MessageTrace) obj;
		try {
			final TraceCallTreeNode rootNode = new TraceCallTreeNode(AbstractSystemSubRepository.ROOT_ELEMENT_ID, TraceCallTreePlugin.this.systemEntityFactory,
					TraceCallTreePlugin.this.allocationComponentOperationPairFactory,
					TraceCallTreePlugin.this.allocationComponentOperationPairFactory.getRootPair(),
					true); // rootNode
			AbstractCallTreePlugin.writeDotForMessageTrace(TraceCallTreePlugin.this.systemEntityFactory, rootNode, mt, TraceCallTreePlugin.this.outputFnBase
					+ "-" + mt.getTraceId(), false, TraceCallTreePlugin.this.shortLabels); // no weights
			TraceCallTreePlugin.this.reportSuccess(mt.getTraceId());
		} catch (final TraceProcessingException ex) {
			TraceCallTreePlugin.this.reportError(mt.getTraceId());
			TraceCallTreePlugin.LOG.error("TraceProcessingException", ex);
		} catch (final FileNotFoundException ex) {
			TraceCallTreePlugin.this.reportError(mt.getTraceId());
			TraceCallTreePlugin.LOG.error("File not found", ex);
		}
	}

	@Override
	protected AbstractRepository[] getDefaultRepositories() {
		return new AbstractRepository[0];
	}

}
