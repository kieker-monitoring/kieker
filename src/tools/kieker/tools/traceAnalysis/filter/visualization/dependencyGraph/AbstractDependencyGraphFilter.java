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

package kieker.tools.traceAnalysis.filter.visualization.dependencyGraph;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.AbstractMessageTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.visualization.util.dot.DotFactory;
import kieker.tools.traceAnalysis.systemModel.AbstractMessage;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * Refactored copy from LogAnalysis-legacy tool
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr,
 */
@Plugin(repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisFilter.SYSTEM_MODEL_REPOSITORY_NAME, repositoryType = SystemModelRepository.class))
public abstract class AbstractDependencyGraphFilter<T> extends AbstractMessageTraceProcessingFilter {

	// private static final Log log = LogFactory.getLog(AbstractDependencyGraphPlugin.class);

	public static final String STEREOTYPE_EXECUTION_CONTAINER = "<<execution container>>";
	public static final String STEREOTYPE_ASSEMBLY_COMPONENT = "<<assembly component>>";
	public static final String STEREOTYPE_ALLOCATION_COMPONENT = "<<deployment component>>";

	private static final String NODE_PREFIX = "depNode_";

	private static final String ENCODING = "UTF-8";

	protected volatile DependencyGraph<T> dependencyGraph;
	private int numGraphsSaved = 0;

	private final List<NodeDecorator> decorators = new ArrayList<NodeDecorator>();

	public AbstractDependencyGraphFilter(final Configuration configuration) {
		super(configuration);
	}

	public void setDependencyGraph(final DependencyGraph<T> dependencyGraph) {
		this.dependencyGraph = dependencyGraph;
	}

	protected abstract void dotEdges(Collection<DependencyGraphNode<T>> nodes, PrintStream ps, final boolean shortLabels);

	protected final String getNodeId(final DependencyGraphNode<T> n) {
		return AbstractDependencyGraphFilter.NODE_PREFIX + n.getId();
	}

	protected void createEdgesForNode(final DependencyGraphNode<T> node, final Collection<WeightedBidirectionalDependencyGraphEdge<T>> edges, final PrintStream ps,
			final boolean includeWeights,
			final boolean plotSelfLoops) {

		for (final WeightedBidirectionalDependencyGraphEdge<T> currentEdge : edges) {
			final String lineStyle = (currentEdge.isAssumed()) ? DotFactory.DOT_STYLE_DASHED : DotFactory.DOT_STYLE_SOLID;

			final DependencyGraphNode<T> destNode = currentEdge.getDestination();
			if ((node == destNode) && !plotSelfLoops) {
				continue;
			}
			final StringBuilder strBuild = new StringBuilder(1024); // NOPMD (new in Loop)
			if (includeWeights) {
				strBuild.append(DotFactory.createConnection("", this.getNodeId(node), this.getNodeId(destNode),
						Integer.toString(currentEdge.getOutgoingWeight()), lineStyle, DotFactory.DOT_ARROWHEAD_OPEN));
			} else {
				strBuild.append(DotFactory.createConnection("", this.getNodeId(node), this.getNodeId(destNode), lineStyle,
						DotFactory.DOT_ARROWHEAD_OPEN));
			}
			ps.println(strBuild.toString());
		}
	}

	protected void dotVertices(final Collection<DependencyGraphNode<T>> nodes, final PrintStream ps, final boolean includeWeights, final boolean plotSelfLoops) {
		for (final DependencyGraphNode<T> curNode : nodes) {
			this.createEdgesForNode(curNode, curNode.getOutgoingDependencies(), ps, includeWeights, plotSelfLoops);
			this.createEdgesForNode(curNode, curNode.getAssumedOutgoingDependencies(), ps, includeWeights, plotSelfLoops);
		}
	}

	private void graphToDot(final PrintStream ps, final boolean includeWeights, final boolean shortLabels, final boolean plotSelfLoops) {
		// preamble:
		ps.println("digraph G {\n rankdir=" + DotFactory.DOT_DOT_RANKDIR_LR + ";");

		this.dotEdges(this.dependencyGraph.getNodes(), ps, shortLabels);
		this.dotVertices(this.dependencyGraph.getNodes(), ps, includeWeights, plotSelfLoops);
		ps.println("}");
	}

	public final void saveToDotFile(final String outputFnBase, final boolean includeWeights, final boolean shortLabels, final boolean plotSelfLoops)
			throws FileNotFoundException, UnsupportedEncodingException {
		final PrintStream ps = new PrintStream(new FileOutputStream(outputFnBase + ".dot"), false, AbstractDependencyGraphFilter.ENCODING);
		this.graphToDot(ps, includeWeights, shortLabels, plotSelfLoops);
		ps.flush();
		ps.close();
		this.numGraphsSaved++;
		this.printMessage(new String[] { "Wrote dependency graph to file '" + outputFnBase + ".dot" + "'", "Dot file can be converted using the dot tool",
			"Example: dot -T svg " + outputFnBase + ".dot" + " > " + outputFnBase + ".svg", });
	}

	@Override
	public void printStatusMessage() {
		super.printStatusMessage();
		this.stdOutPrintln("Saved " + this.numGraphsSaved + " dependency graph" + (this.numGraphsSaved > 1 ? "s" : "")); // NOCS
	}

	public void addDecorator(final NodeDecorator decorator) {
		this.decorators.add(decorator);
	}

	protected void invokeDecorators(final AbstractMessage message, final DependencyGraphNode<?> sourceNode, final DependencyGraphNode<?> targetNode) {
		for (final NodeDecorator currentDecorator : this.decorators) {
			currentDecorator.processMessage(message, sourceNode, targetNode);
		}
	}

	protected StringBuilder addDecorationText(final StringBuilder output, final DependencyGraphNode<?> node) {
		final String decorations = node.getFormattedDecorations();
		if ((decorations != null) && (decorations.length() != 0)) {
			output.append("\\n");
			output.append(decorations);
		}

		return output;
	}

	protected String getNodeFillColor(final DependencyGraphNode<?> node) {
		return (node.isAssumed()) ? DotFactory.DOT_FILLCOLOR_GRAY : DotFactory.DOT_FILLCOLOR_WHITE;
	}

	protected boolean isDependencyAssumed(final DependencyGraphNode<?> source, final DependencyGraphNode<?> target) {
		return (source.isAssumed() || target.isAssumed());
	}
}
