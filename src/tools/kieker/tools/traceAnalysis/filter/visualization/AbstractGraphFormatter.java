/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.filter.visualization;

import java.util.Iterator;

import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraph;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractVertex;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractVertexDecoration;
import kieker.tools.traceAnalysis.filter.visualization.graph.Color;

/**
 * Abstract superclass for all graph formatters.
 * 
 * @author Holger Knoche
 * 
 * @param <G>
 *            The graph type this formatter is for
 * 
 * @since 1.6
 */
public abstract class AbstractGraphFormatter<G extends AbstractGraph<?, ?, ?>> {

	/**
	 * Creates a formatted representation of the given graph.
	 * 
	 * @param graph
	 *            The graph to format
	 * @param includeWeights
	 *            Determines whether to include weights or not.
	 * @param useShortLabels
	 *            Determines whether to use short labels or not.
	 * @param plotLoops
	 *            Determines whether to plot loops or not.
	 * 
	 * @return A formatted representation of the graph
	 */
	@SuppressWarnings("unchecked")
	public String createFormattedRepresentation(final AbstractGraph<?, ?, ?> graph, final boolean includeWeights, final boolean useShortLabels,
			final boolean plotLoops) {
		return this.formatGraph((G) graph, includeWeights, useShortLabels, plotLoops);
	}

	/**
	 * This method encapsulates the concrete graph formatting.
	 * 
	 * @param graph
	 *            The input graph to format
	 * @param includeWeights
	 *            Determines whether to include weights or not.
	 * @param useShortLabels
	 *            Determines whether to use short labels or not.
	 * @param plotLoops
	 *            Determines whether to plot loops or not.
	 * 
	 * @return A textual specification of the input graph
	 */
	protected abstract String formatGraph(G graph, final boolean includeWeights, final boolean useShortLabels, final boolean plotLoops);

	private static String getFormattedDecorations(final AbstractVertex<?, ?, ?> vertex) {
		synchronized (vertex) {
			final StringBuilder builder = new StringBuilder();
			final Iterator<AbstractVertexDecoration> decorationsIter = vertex.getDecorations().iterator();

			while (decorationsIter.hasNext()) {
				final String currentDecorationText = decorationsIter.next().createFormattedOutput();

				if ((currentDecorationText == null) || (currentDecorationText.length() == 0)) {
					continue;
				}

				builder.append(currentDecorationText);

				if (decorationsIter.hasNext()) {
					builder.append("\\n");
				}
			}

			return builder.toString();
		}
	}

	/**
	 * Utility function to format the decorations attached to a vertex.
	 * 
	 * @param builder
	 *            The builder to send the output to
	 * @param vertex
	 *            The vertex to work with
	 */
	public static void formatDecorations(final StringBuilder builder, final AbstractVertex<?, ?, ?> vertex) {
		final String decorations = AbstractGraphFormatter.getFormattedDecorations(vertex);
		if (decorations.length() != 0) { // decorations cannot be null (getFormattedDecorations never returns null)
			builder.append("\\n");
			builder.append(decorations);
		}
	}

	/**
	 * Returns the default file name suggested by this formatter.
	 * 
	 * @return See above
	 */
	public abstract String getDefaultFileName();

	/**
	 * Returns the dot (graphviz) representation of the given color.
	 * 
	 * @param color
	 *            The color to convert
	 * @return The dot representation of the given color
	 */
	public static String getDotRepresentation(final Color color) {
		return String.format("#%06x", color.getRGB());
	}
}
