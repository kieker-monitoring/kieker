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

package kieker.tools.traceAnalysis.filter.visualization.callTree;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Map;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.util.Signature;
import kieker.tools.traceAnalysis.filter.AbstractMessageTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.traceReconstruction.TraceProcessingException;
import kieker.tools.traceAnalysis.filter.visualization.callTree.AbstractCallTreeFilter.IPairFactory;
import kieker.tools.traceAnalysis.filter.visualization.util.IntContainer;
import kieker.tools.traceAnalysis.filter.visualization.util.dot.DotFactory;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.SynchronousCallMessage;
import kieker.tools.traceAnalysis.systemModel.repository.AbstractSystemSubRepository;
import kieker.tools.traceAnalysis.systemModel.repository.AllocationComponentOperationPairFactory;
import kieker.tools.traceAnalysis.systemModel.repository.AllocationRepository;
import kieker.tools.traceAnalysis.systemModel.repository.OperationRepository;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.tools.traceAnalysis.systemModel.util.AllocationComponentOperationPair;

/**
 * Plugin providing the creation of calling trees both for individual traces
 * and an aggregated form for multiple traces.<br>
 * 
 * This class has exactly one input port named "in". The data which is sent to
 * this plugin is not delegated in any way.
 * 
 * @author Andre van Hoorn
 */
@Plugin(description = "A filter allowing to write the incoming data into a calling tree",
		repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class))
public class TraceCallTreeFilter extends AbstractMessageTraceProcessingFilter {

	public static final String CONFIG_PROPERTY_NAME_OUTPUT_FILENAME = "dotOutputFn";
	public static final String CONFIG_PROPERTY_NAME_SHORT_LABELS = "shortLabels";

	public static final String CONFIG_PROPERTY_VALUE_OUTPUT_FILENAME_DEFAULT = "traceCalltree.dot";
	public static final String CONFIG_PROPERTY_VALUE_SHORT_LABELS_DEFAULT = Boolean.TRUE.toString();

	private static final String ENCODING = "UTF-8";

	private static final Log LOG = LogFactory.getLog(TraceCallTreeFilter.class);

	private final CallTreeNode root;
	private final String dotOutputFn;
	private final boolean shortLabels;

	public TraceCallTreeFilter(final Configuration configuration) {
		/* Call the inherited mandatory "default" constructor. */
		super(configuration);

		// Initialize the fields based on the given parameters. */
		this.root = new CallTreeNode(null, // null: root node has no parent
				new CallTreeOperationHashKey(AllocationRepository.ROOT_ALLOCATION_COMPONENT, OperationRepository.ROOT_OPERATION));
		this.shortLabels = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_SHORT_LABELS);
		this.dotOutputFn = configuration.getProperty(CONFIG_PROPERTY_NAME_OUTPUT_FILENAME);
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
		if (paramList.length > 0) {
			opLabel.append("..");
		}
		opLabel.append(")");

		strBuild.append(opLabel.toString());
		return strBuild.toString();
	}

	/** Traverse tree recursively and generate dot code for edges. */
	private static void dotEdgesFromSubTree(final CallTreeNode n, final Map<CallTreeNode, Integer> nodeIds,
			final IntContainer nextNodeId, final PrintStream ps, final boolean shortLabels) {
		final StringBuilder strBuild = new StringBuilder();
		nodeIds.put(n, nextNodeId.getValue());
		strBuild.append(nextNodeId.getAndIncValue()).append("[label =\"").append(TraceCallTreeFilter.nodeLabel(n, shortLabels))
				.append("\",shape=" + DotFactory.DOT_SHAPE_NONE + ",style=" + DotFactory.DOT_STYLE_FILLED + ",fillcolor=" + DotFactory.DOT_FILLCOLOR_WHITE + "];");
		ps.println(strBuild.toString());
		for (final CallTreeNode child : n.getChildren()) {
			TraceCallTreeFilter.dotEdgesFromSubTree(child, nodeIds, nextNodeId, ps, shortLabels);
		}
	}

	/** Traverse tree recursively and generate dot code for vertices. */
	private static void dotVerticesFromSubTree(final CallTreeNode n, final Map<CallTreeNode, Integer> nodeIds, final PrintStream ps,
			final boolean includeWeights) {
		final int thisId = nodeIds.get(n);
		for (final CallTreeNode child : n.getChildren()) {
			final StringBuilder strBuild = new StringBuilder();
			final int childId = nodeIds.get(child);
			strBuild.append("\n").append(thisId).append("->").append(childId).append("[style=solid,arrowhead=none");
			if (includeWeights) {
				strBuild.append(",label = ").append("").append(", weight =").append("");
			}
			strBuild.append(" ]");
			ps.println(strBuild.toString());
			TraceCallTreeFilter.dotVerticesFromSubTree(child, nodeIds, ps, includeWeights);
		}
	}

	private static void dotFromCallingTree(final CallTreeNode root, final PrintStream ps,
			final boolean includeWeights, final boolean shortLabels) {
		// preamble:
		ps.println("digraph G {");
		final StringBuilder edgestringBuilder = new StringBuilder();

		final Map<CallTreeNode, Integer> nodeIds = new Hashtable<CallTreeNode, Integer>(); // NOPMD (UseConcurrentHashMap)

		TraceCallTreeFilter.dotEdgesFromSubTree(root, nodeIds, new IntContainer(0), ps, shortLabels);
		TraceCallTreeFilter.dotVerticesFromSubTree(root, nodeIds, ps, includeWeights);

		ps.println(edgestringBuilder.toString());
		ps.println("}");
	}

	private static void saveTreeToDotFile(final CallTreeNode root, final String outputFnBase,
			final boolean includeWeights, final boolean shortLabels) throws FileNotFoundException, UnsupportedEncodingException {
		final PrintStream ps = new PrintStream(new FileOutputStream(outputFnBase + ".dot"), false, ENCODING);
		TraceCallTreeFilter.dotFromCallingTree(root, ps, includeWeights, shortLabels);
		ps.flush();
		ps.close();
	}

	public void saveTreeToDotFile(final String outputFnBaseL, final boolean includeWeightsL, final boolean shortLabelsL) throws FileNotFoundException,
			UnsupportedEncodingException {
		TraceCallTreeFilter.saveTreeToDotFile(this.root, outputFnBaseL, includeWeightsL, shortLabelsL);
		this.printMessage(new String[] { "Wrote call tree to file '" + outputFnBaseL + ".dot" + "'", "Dot file can be converted using the dot tool",
			"Example: dot -T svg " + outputFnBaseL + ".dot" + " > " + outputFnBaseL + ".svg", });
	}

	@Override
	public void printStatusMessage() {
		synchronized (this) {
			super.printStatusMessage();
			final int numPlots = this.getSuccessCount();
			final long lastSuccessTracesId = this.getLastTraceIdSuccess();
			this.stdOutPrintln("Wrote " + numPlots + " call tree" + (numPlots > 1 ? "s" : "") + " to file" + (numPlots > 1 ? "s" : "") + " with name pattern '" // NOCS
					+ this.dotOutputFn + "-<traceId>.dot'");
			this.stdOutPrintln("Dot files can be converted using the dot tool");
			this.stdOutPrintln("Example: dot -T svg " + this.dotOutputFn + "-" + ((numPlots > 0) ? lastSuccessTracesId : "<traceId>") + ".dot > " // NOCS
					+ this.dotOutputFn + "-" + ((numPlots > 0) ? lastSuccessTracesId : "<traceId>") + ".svg"); // NOCS
		}
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_SHORT_LABELS, CONFIG_PROPERTY_VALUE_SHORT_LABELS_DEFAULT);
		configuration.setProperty(CONFIG_PROPERTY_NAME_OUTPUT_FILENAME, CONFIG_PROPERTY_VALUE_OUTPUT_FILENAME_DEFAULT);
		return configuration;
	}

	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_SHORT_LABELS, Boolean.toString(this.shortLabels));
		configuration.setProperty(CONFIG_PROPERTY_NAME_OUTPUT_FILENAME, this.dotOutputFn);
		return configuration;
	}

	@Override
	@InputPort(
			name = AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES,
			description = "Receives the message traces to be processed",
			eventTypes = { MessageTrace.class })
	public void inputMessageTraces(final MessageTrace mt) {
		try {
			final TraceCallTreeNode rootNode =
					new TraceCallTreeNode(AbstractSystemSubRepository.ROOT_ELEMENT_ID, AllocationComponentOperationPairFactory.ROOT_PAIR, true, mt); // rootNode
			AbstractCallTreeFilter.writeDotForMessageTrace(rootNode, new IPairFactory() {

				public Object createPair(final SynchronousCallMessage callMsg) {
					final AllocationComponent allocationComponent = callMsg.getReceivingExecution().getAllocationComponent();
					final Operation op = callMsg.getReceivingExecution().getOperation();
					final AllocationComponentOperationPair destination = TraceCallTreeFilter.this.getSystemEntityFactory().getAllocationPairFactory()
							.getPairInstanceByPair(allocationComponent, op); // will never be null!
					return destination;
				}
			}, mt, TraceCallTreeFilter.this.dotOutputFn + "-" + mt.getTraceId() + ".dot", false, TraceCallTreeFilter.this.shortLabels); // no weights
			TraceCallTreeFilter.this.reportSuccess(mt.getTraceId());
		} catch (final TraceProcessingException ex) {
			TraceCallTreeFilter.this.reportError(mt.getTraceId());
			LOG.error("TraceProcessingException", ex);
		} catch (final FileNotFoundException ex) {
			TraceCallTreeFilter.this.reportError(mt.getTraceId());
			LOG.error("File not found", ex);
		} catch (final UnsupportedEncodingException ex) {
			TraceCallTreeFilter.this.reportError(mt.getTraceId());
			LOG.error("Encoding not supported", ex);
		}
	}
}
