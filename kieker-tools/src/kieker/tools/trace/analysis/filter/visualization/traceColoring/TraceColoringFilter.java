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

package kieker.tools.trace.analysis.filter.visualization.traceColoring;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import kieker.analysis.IProjectContext;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.tools.trace.analysis.filter.IGraphOutputtingFilter;
import kieker.tools.trace.analysis.filter.visualization.AbstractGraphFilter;
import kieker.tools.trace.analysis.filter.visualization.graph.AbstractEdge;
import kieker.tools.trace.analysis.filter.visualization.graph.AbstractGraph;
import kieker.tools.trace.analysis.filter.visualization.graph.AbstractGraphElement;
import kieker.tools.trace.analysis.filter.visualization.graph.AbstractVertex;
import kieker.tools.trace.analysis.filter.visualization.graph.Color;
import kieker.tools.trace.analysis.filter.visualization.graph.IOriginRetentionPolicy;
import kieker.tools.trace.analysis.filter.visualization.graph.SpecificOriginRetentionPolicy;
import kieker.tools.trace.analysis.filter.visualization.graph.AbstractGraph.IGraphVisitor;
import kieker.tools.trace.analysis.repository.TraceColorRepository;
import kieker.tools.trace.analysis.systemModel.TraceInformation;

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
 * 
 * @since 1.6
 */
@Plugin(name = "Trace coloring filter",
		description = "Colors graph elements that can uniquely associated to a trace according to the color repository",
		repositoryPorts = @RepositoryPort(name = TraceColoringFilter.COLOR_REPOSITORY_PORT_NAME, repositoryType = TraceColorRepository.class),
		outputPorts = @OutputPort(name = IGraphOutputtingFilter.OUTPUT_PORT_NAME_GRAPH, eventTypes = { AbstractGraph.class }))
public class TraceColoringFilter<V extends AbstractVertex<V, E, TraceInformation>, E extends AbstractEdge<V, E, TraceInformation>> extends
		AbstractGraphFilter<AbstractGraph<V, E, TraceInformation>, V, E, TraceInformation> implements IGraphVisitor<V, E> {

	/**
	 * Port name at which the color repository must be connected.
	 */
	public static final String COLOR_REPOSITORY_PORT_NAME = "colorRepository";

	private Map<Long, Color> colorMap;
	private Color defaultColor;
	private Color collisionColor;

	/**
	 * Creates a new filter using the given configuration.
	 * 
	 * @param configuration
	 *            The configuration to use for this filter.
	 * @param projectContext
	 *            The project context to use for this filter.
	 */
	public TraceColoringFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	private void initialize() {
		final TraceColorRepository colorRepository = (TraceColorRepository) super.getRepository(COLOR_REPOSITORY_PORT_NAME);
		this.colorMap = colorRepository.getColorMap();
		this.defaultColor = colorRepository.getDefaultColor();
		this.collisionColor = colorRepository.getCollisionColor();
	}

	private void handleGraphElement(final AbstractGraphElement<TraceInformation> element) {
		TraceInformation relevantTraceInformation = null;
		int relevantOrigins = 0;

		// Count the relevant origins from the origin set
		final Iterator<TraceInformation> origins = element.getOrigins().iterator();
		while (origins.hasNext()) {
			final TraceInformation traceInformation = origins.next();
			final long traceId = traceInformation.getTraceId();

			if (this.colorMap.containsKey(traceId)) {
				relevantTraceInformation = traceInformation;
				relevantOrigins++;
			}
		}

		// Choose the color depending on the number of relevant origins
		if (relevantOrigins == 0) {
			element.setColor(this.defaultColor);
		} else if (relevantOrigins > 1) {
			element.setColor(this.collisionColor);
		} else {
			final long traceId = relevantTraceInformation.getTraceId();
			final Color color = this.colorMap.get(traceId);

			element.setColor(color);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visitVertex(final V vertex) {
		this.handleGraphElement(vertex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visitEdge(final E edge) {
		this.handleGraphElement(edge);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AbstractGraph<V, E, TraceInformation> performConcreteGraphProcessing(final AbstractGraph<V, E, TraceInformation> graph) {
		this.initialize();

		graph.traverse(this);

		return graph;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IOriginRetentionPolicy getDesiredOriginRetentionPolicy() throws AnalysisConfigurationException {
		final TraceColorRepository colorRepository = (TraceColorRepository) super.getRepository(COLOR_REPOSITORY_PORT_NAME);

		final Set<TraceInformation> desiredTraces = new HashSet<TraceInformation>();
		for (final Long traceId : colorRepository.getColorMap().keySet()) {
			desiredTraces.add(new TraceInformation(traceId, null));
		}

		return SpecificOriginRetentionPolicy.createInstance(desiredTraces);
	}
}
