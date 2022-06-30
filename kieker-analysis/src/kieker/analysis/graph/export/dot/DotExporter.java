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

package kieker.analysis.graph.export.dot;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import com.google.common.graph.MutableNetwork;

import kieker.analysis.graph.IEdge;
import kieker.analysis.graph.IGraph;
import kieker.analysis.graph.INode;
import kieker.analysis.graph.util.dot.DotGraphWriter;
import kieker.analysis.graph.util.dot.attributes.DotEdgeAttribute;
import kieker.analysis.graph.util.dot.attributes.DotGraphAttribute;
import kieker.analysis.graph.util.dot.attributes.DotNodeAttribute;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class DotExporter extends DotElementExporter {

	public DotExporter(final IGraph<INode, IEdge> graph, final Writer writer) {
		this(graph, writer, new SimpleDotExportConfiguration());
	}

	public DotExporter(final IGraph<INode, IEdge> graph, final Writer writer, final DotExportConfiguration configuration) {
		super(graph, new DotGraphWriter(writer), configuration);
	}

	@Override
	protected void beforeTransformation() {
		try {
			this.dotGraphWriter.start(this.graph.getLabel());

			for (final Entry<DotGraphAttribute, Function<MutableNetwork<INode, IEdge>, String>> attribute : this.configuration.getGraphAttributes()) {
				this.dotGraphWriter.addGraphAttribute(attribute.getKey().toString(), attribute.getValue().apply(this.graph.getGraph()));
			}

			final Map<String, String> defaultNodeAttributes = new HashMap<>(); // NOPMD (no concurrent access intended)
			for (final Entry<DotNodeAttribute, Function<MutableNetwork<INode, IEdge>, String>> attribute : this.configuration.getDefaultNodeAttributes()) {
				defaultNodeAttributes.put(attribute.getKey().toString(), attribute.getValue().apply(this.graph.getGraph()));
			}
			this.dotGraphWriter.addDefaultNodeAttributes(defaultNodeAttributes);

			final Map<String, String> defaultEdgeAttributes = new HashMap<>(); // NOPMD (no concurrent access intended)
			for (final Entry<DotEdgeAttribute, Function<MutableNetwork<INode, IEdge>, String>> attribute : this.configuration.getDefaultEdgeAttributes()) {
				defaultEdgeAttributes.put(attribute.getKey().toString(), attribute.getValue().apply(this.graph.getGraph()));
			}
			this.dotGraphWriter.addDefaultEdgeAttributes(defaultEdgeAttributes);

		} catch (final IOException e) {
			this.handleIOException(e);
		}
	}

	@Override
	protected void afterTransformation() {
		try {
			this.dotGraphWriter.finish();
		} catch (final IOException e) {
			this.handleIOException(e);
		}
	}

}
