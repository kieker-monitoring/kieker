/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.graph.export.dot;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import kieker.analysis.graph.Direction;
import kieker.analysis.graph.IEdge;
import kieker.analysis.graph.IGraph;
import kieker.analysis.graph.IVertex;
import kieker.analysis.graph.export.AbstractTransformer;
import kieker.analysis.graph.util.dot.DotGraphWriter;
import kieker.analysis.graph.util.dot.attributes.DotClusterAttribute;
import kieker.analysis.graph.util.dot.attributes.DotEdgeAttribute;
import kieker.analysis.graph.util.dot.attributes.DotNodeAttribute;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
class DotElementExporter extends AbstractTransformer<Void> {

	protected final DotGraphWriter dotGraphWriter;
	protected final DotExportConfiguration configuration;

	protected DotElementExporter(final IGraph graph, final DotGraphWriter dotGraphWriter, final DotExportConfiguration configuration) {
		super(graph);
		this.dotGraphWriter = dotGraphWriter;
		this.configuration = configuration;
	}

	@Override
	protected void transformVertex(final IVertex vertex) {
		try {
			if (vertex.hasChildGraph()) {
				final IGraph childGraph = vertex.getChildGraph();

				this.dotGraphWriter.addClusterStart(vertex.getId().toString());

				for (final Entry<DotClusterAttribute, Function<IVertex, String>> attribute : this.configuration.getClusterAttributes()) {
					this.dotGraphWriter.addGraphAttribute(attribute.getKey().toString(), attribute.getValue().apply(vertex));
				}

				final DotElementExporter childGraphWriter = new DotElementExporter(childGraph, this.dotGraphWriter, this.configuration);
				childGraphWriter.transform();

				this.dotGraphWriter.addClusterStop();
			} else {
				this.dotGraphWriter.addNode(vertex.getId().toString(), this.getAttributes(vertex));
			}
		} catch (final IOException e) {
			this.handleIOException(e);
		}
	}

	@Override
	protected void transformEdge(final IEdge edge) {
		try {
			final String sourceId = edge.getVertex(Direction.OUT).getId().toString();
			final String targetId = edge.getVertex(Direction.IN).getId().toString();

			this.dotGraphWriter.addEdge(sourceId, targetId, this.getAttributes(edge));
		} catch (final IOException e) {
			this.handleIOException(e);
		}

	}

	protected void handleIOException(final IOException ioException) {
		throw new IllegalStateException(ioException);
	}

	protected Map<String, String> getAttributes(final IEdge edge) {
		final Map<String, String> attributes = new HashMap<>(); // NOPMD (no concurrent access intended)
		for (final Entry<DotEdgeAttribute, Function<IEdge, String>> entry : this.configuration.getEdgeAttributes()) {
			final String value = entry.getValue().apply(edge);
			if (value != null) {
				attributes.put(entry.getKey().toString(), value);
			}
		}
		return attributes;
	}

	protected Map<String, String> getAttributes(final IVertex vertex) {
		final Map<String, String> attributes = new HashMap<>(); // NOPMD (no concurrent access intended)
		for (final Entry<DotNodeAttribute, Function<IVertex, String>> entry : this.configuration.getNodeAttributes()) {
			final String value = entry.getValue().apply(vertex);
			if (value != null) {
				attributes.put(entry.getKey().toString(), value);
			}
		}
		return attributes;
	}

	@Override
	protected void beforeTransformation() {
		// Do nothing

	}

	@Override
	protected void afterTransformation() {
		// Do nothing
	}

	@Override
	protected Void getTransformation() {
		return null;
	}

}
