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

package kieker.tools.traceAnalysis.filter.visualization.traceColoring;

import java.util.Map;

import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.IGraphOutputtingFilter;
import kieker.tools.traceAnalysis.filter.visualization.AbstractGraphFilter;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractEdge;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraph;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraph.Visitor;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraphElement;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractVertex;
import kieker.tools.traceAnalysis.filter.visualization.graph.Color;
import kieker.tools.traceAnalysis.repository.TraceColorRepository;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;

/**
 * This filter sets the color of nodes and edges which belong to a single trace according to a trace
 * coloring schema defined in a color repository (see {@link TraceColorRepository}). Element that belong
 * to multiple traces get the collision color defined in the repository.
 * 
 * @author Holger Knoche
 * 
 * @param <V>
 *            The type of the graph's vertices
 * @param <E>
 *            The type of the graph's edges
 */
@Plugin(name = "Trace coloring filter",
		description = "Colors graph elements that can uniquely associated to a trace according to the color repository",
		repositoryPorts = @RepositoryPort(name = TraceColoringFilter.COLOR_REPOSITORY_NAME, repositoryType = TraceColorRepository.class),
		outputPorts = @OutputPort(name = IGraphOutputtingFilter.OUTPUT_PORT_NAME_GRAPH, eventTypes = { AbstractGraph.class }))
public class TraceColoringFilter<V extends AbstractVertex<V, E, MessageTrace>, E extends AbstractEdge<V, E, MessageTrace>> extends
		AbstractGraphFilter<AbstractGraph<V, E, MessageTrace>, V, E, MessageTrace> implements Visitor<V, E> {

	public static final String COLOR_REPOSITORY_NAME = "colorRepository";

	private Map<Long, Color> colorMap;
	private Color defaultColor;
	private Color collisionColor;

	/**
	 * Creates a new filter using the given configuration.
	 * 
	 * @param configuration
	 *            The configuration to use for this filter
	 */
	public TraceColoringFilter(final Configuration configuration) {
		super(configuration);
	}

	private void initialize() {
		final TraceColorRepository colorRepository = (TraceColorRepository) super.getRepository(COLOR_REPOSITORY_NAME);
		this.colorMap = colorRepository.getColorMap();
		this.defaultColor = colorRepository.getDefaultColor();
		this.collisionColor = colorRepository.getCollisionColor();
	}

	private void handleGraphElement(final AbstractGraphElement<MessageTrace> element) {
		if (element.getOrigins().size() != 1) {
			element.setColor(this.collisionColor);
		}
		else {
			final MessageTrace trace = element.getOrigins().iterator().next();
			final long traceId = trace.getTraceId();
			final Color color = (this.colorMap.containsKey(traceId)) ? this.colorMap.get(traceId) : this.defaultColor;
			element.setColor(color);
		}
	}

	public void visitVertex(final V vertex) {
		this.handleGraphElement(vertex);
	}

	public void visitEdge(final E edge) {
		this.handleGraphElement(edge);
	}

	@Override
	protected AbstractGraph<V, E, MessageTrace> performConcreteGraphProcessing(final AbstractGraph<V, E, MessageTrace> graph) {
		this.initialize();

		graph.traverse(this);

		return graph;
	}

}
