package kieker.analysisteetime.util.graph.export.dot;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.util.dot.DotGraphWriter;
import kieker.analysisteetime.util.graph.util.dot.attributes.DotEdgeAttribute;
import kieker.analysisteetime.util.graph.util.dot.attributes.DotGraphAttribute;
import kieker.analysisteetime.util.graph.util.dot.attributes.DotNodeAttribute;

public class DotExporter extends DotElementExporter {

	public DotExporter(final Graph graph, final Writer writer) {
		this(graph, writer, new SimpleDotExportConfiguration());
	}

	public DotExporter(final Graph graph, final Writer writer, final DotExportConfiguration configuration) {
		super(graph, new DotGraphWriter(writer), configuration);
	}

	@Override
	protected void beforeTransformation() {
		try {
			this.dotGraphWriter.start(this.graph.getName());

			for (final Entry<DotGraphAttribute, Function<Graph, String>> attribute : this.configuration.getGraphAttributes()) {
				this.dotGraphWriter.addGraphAttribute(attribute.getKey().toString(), attribute.getValue().apply(this.graph));
			}

			final Map<String, String> defaultNodeAttributes = new HashMap<>(); // NOPMD (no concurrent access intended)
			for (final Entry<DotNodeAttribute, Function<Graph, String>> attribute : this.configuration.getDefaultNodeAttributes()) {
				defaultNodeAttributes.put(attribute.getKey().toString(), attribute.getValue().apply(this.graph));
			}
			this.dotGraphWriter.addDefaultNodeAttributes(defaultNodeAttributes);

			final Map<String, String> defaultEdgeAttributes = new HashMap<>(); // NOPMD (no concurrent access intended)
			for (final Entry<DotEdgeAttribute, Function<Graph, String>> attribute : this.configuration.getDefaultEdgeAttributes()) {
				defaultEdgeAttributes.put(attribute.getKey().toString(), attribute.getValue().apply(this.graph));
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
