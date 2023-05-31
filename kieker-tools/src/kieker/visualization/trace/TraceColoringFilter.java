/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.visualization.trace;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.model.system.model.TraceInformation;
import kieker.tools.trace.analysis.filter.visualization.graph.AbstractEdge;
import kieker.tools.trace.analysis.filter.visualization.graph.AbstractGraph;
import kieker.tools.trace.analysis.filter.visualization.graph.AbstractGraph.IGraphVisitor;
import kieker.tools.trace.analysis.filter.visualization.graph.AbstractGraphElement;
import kieker.tools.trace.analysis.filter.visualization.graph.AbstractVertex;
import kieker.tools.trace.analysis.filter.visualization.graph.Color;
import kieker.tools.trace.analysis.filter.visualization.graph.IOriginRetentionPolicy;
import kieker.tools.trace.analysis.filter.visualization.graph.SpecificOriginRetentionPolicy;
import kieker.tools.trace.analysis.repository.TraceColorRepository;

/**
 * This filter sets the color of nodes and edges which belong to a single trace according to a trace
 * coloring schema defined in a color repository (see {@link TraceColorRepository}). Element that belong
 * to multiple traces get the collision color defined in the repository.
 *
 * @author Holger Knoche
 * @author Reiner Jung -- teetime port
 *
 * @param <V>
 *            The type of the graph's vertices
 * @param <E>
 *            The type of the graph's edges
 *
 * @since 1.6
 */
public class TraceColoringFilter<V extends AbstractVertex<V, E, TraceInformation>, E extends AbstractEdge<V, E, TraceInformation>> extends
		AbstractGraphFilter<AbstractGraph<V, E, TraceInformation>, V, E, TraceInformation> implements IGraphVisitor<V, E> {

	private final Map<Long, Color> colorMap;
	private final Color defaultColor;
	private final Color collisionColor;

	/**
	 * Creates a new filter using the given configuration.
	 *
	 * @param colorRepository
	 *            the color repository with data for coloring traces
	 */
	public TraceColoringFilter(final TraceColorRepository colorRepository) {
		super();
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
		graph.traverse(this);

		return graph;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IOriginRetentionPolicy getDesiredOriginRetentionPolicy() throws AnalysisConfigurationException {
		final Set<TraceInformation> desiredTraces = new HashSet<>();
		for (final Long traceId : this.colorMap.keySet()) {
			desiredTraces.add(new TraceInformation(traceId, null));
		}

		return SpecificOriginRetentionPolicy.createInstance(desiredTraces);
	}
}
