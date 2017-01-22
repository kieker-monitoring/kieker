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
			dotGraphWriter.start(graph.getName());

			for (Entry<DotGraphAttribute, Function<Graph, String>> attribute : configuration.getGraphAttributes().entrySet()) {
				dotGraphWriter.addGraphAttribute(attribute.getKey().toString(), attribute.getValue().apply(graph));
			}

			Map<String, String> defaultNodeAttributes = new HashMap<>();
			for (Entry<DotNodeAttribute, Function<Graph, String>> attribute : configuration.getDefaultNodeAttributes().entrySet()) {
				defaultNodeAttributes.put(attribute.getKey().toString(), attribute.getValue().apply(graph));
			}
			dotGraphWriter.addDefaultNodeAttributes(defaultNodeAttributes);

			Map<String, String> defaultEdgeAttributes = new HashMap<>();
			for (Entry<DotEdgeAttribute, Function<Graph, String>> attribute : configuration.getDefaultEdgeAttributes().entrySet()) {
				defaultEdgeAttributes.put(attribute.getKey().toString(), attribute.getValue().apply(graph));
			}
			dotGraphWriter.addDefaultEdgeAttributes(defaultEdgeAttributes);

		} catch (IOException e) {
			handleIOException(e);
		}
	}

	@Override
	protected void afterTransformation() {
		try {
			dotGraphWriter.finish();
		} catch (IOException e) {
			handleIOException(e);
		}
	}

}
