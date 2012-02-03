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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;

import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.plugins.AbstractMessageTraceProcessingPlugin;
import kieker.tools.traceAnalysis.plugins.visualization.util.dot.DotFactory;

/**
 * Refactored copy from LogAnalysis-legacy tool
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr,
 */
public abstract class AbstractDependencyGraphPlugin<T> extends AbstractMessageTraceProcessingPlugin {

	// private static final Log log = LogFactory.getLog(AbstractDependencyGraphPlugin.class);

	public static final String STEREOTYPE_EXECUTION_CONTAINER = "<<execution container>>";
	public static final String STEREOTYPE_ASSEMBLY_COMPONENT = "<<assembly component>>";
	public static final String STEREOTYPE_ALLOCATION_COMPONENT = "<<deployment component>>";

	private static final String NODE_PREFIX = "depNode_";

	private static final String ENCODING = "UTF-8";

	protected final DependencyGraph<T> dependencyGraph;
	private int numGraphsSaved = 0;

	// TODO Change constructor to plugin-default-constructor
	public AbstractDependencyGraphPlugin(final Configuration configuration, final Map<String, AbstractRepository> repositories,
			final DependencyGraph<T> dependencyGraph) {
		super(configuration, repositories);
		this.dependencyGraph = dependencyGraph;
	}

	protected abstract void dotEdges(Collection<DependencyGraphNode<T>> nodes, PrintStream ps, final boolean shortLabels);

	protected final String getNodeId(final DependencyGraphNode<T> n) {
		return AbstractDependencyGraphPlugin.NODE_PREFIX + n.getId();
	}

	protected void dotVertices(final Collection<DependencyGraphNode<T>> nodes, final PrintStream ps, final boolean includeWeights, final boolean plotSelfLoops) {
		for (final DependencyGraphNode<T> curNode : nodes) {
			for (final WeightedBidirectionalDependencyGraphEdge<T> outgoingDependency : curNode.getOutgoingDependencies()) {
				final DependencyGraphNode<T> destNode = outgoingDependency.getDestination();
				if ((curNode == destNode) && !plotSelfLoops) {
					continue;
				}
				final StringBuilder strBuild = new StringBuilder(1024); // NOPMD (new in Loop)
				if (includeWeights) {
					strBuild.append(DotFactory.createConnection("", this.getNodeId(curNode), this.getNodeId(destNode),
							Integer.toString(outgoingDependency.getOutgoingWeight()), DotFactory.DOT_STYLE_DASHED, DotFactory.DOT_ARROWHEAD_OPEN));
				} else {
					strBuild.append(DotFactory.createConnection("", this.getNodeId(curNode), this.getNodeId(destNode), DotFactory.DOT_STYLE_DASHED,
							DotFactory.DOT_ARROWHEAD_OPEN));
				}
				ps.println(strBuild.toString());
			}
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
		final PrintStream ps = new PrintStream(new FileOutputStream(outputFnBase + ".dot"), false, AbstractDependencyGraphPlugin.ENCODING);
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
		System.out.println("Saved " + this.numGraphsSaved + " dependency graph" + (this.numGraphsSaved > 1 ? "s" : "")); // NOCS
	}

}
