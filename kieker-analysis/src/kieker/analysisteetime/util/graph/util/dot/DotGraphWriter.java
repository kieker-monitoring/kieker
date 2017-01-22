package kieker.analysisteetime.util.graph.util.dot;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.stream.Collectors;

import kieker.analysisteetime.util.graph.util.IndentWriter;

/**
 * Class to build and write a DOT Graph to a writer.
 *
 * @author Sören Henning
 *
 */
public class DotGraphWriter {

	private final IndentWriter writer;
	private DotWriterState state = DotWriterState.CREATED;
	private int openSubgraphs = 0;

	private DotGraphType graphType = DotGraphType.DIRECTED;

	public DotGraphWriter(final Writer writer) {
		this.writer = new IndentWriter(writer);
	}

	public void start(final String name) throws IOException {
		start(DotGraphType.DIRECTED, name);
	}

	public void start(final DotGraphType graphType, final String name) throws IOException {
		checkState(DotWriterState.CREATED);

		this.graphType = graphType;

		String openToken;
		if (graphType == DotGraphType.UNDIRECTED) {
			openToken = DotGraph.UNDIRECTED_START_TOKEN;
		} else {
			openToken = DotGraph.DIRECTED_START_TOKEN;
		}
		writer.writeln(openToken + ' ' + '"' + name + '"' + ' ' + DotGraph.START_GRAPH_BRACKET);
		writer.indent();
		state = DotWriterState.STARTED;
	}

	public void finish() throws IOException {
		checkState(DotWriterState.STARTED);

		if (openSubgraphs > 0) {
			throw new IllegalStateException("There are unclosed subgraphs.");
		}

		writer.unindent();
		writer.writeln(DotGraph.END_GRAPH_BRACKET);
		state = DotWriterState.FINISHED;

		writer.close();
	}

	public void addDefaultNodeAttributes(final Map<String, String> attributes) throws IOException {
		checkState(DotWriterState.STARTED);

		if (attributes != null && !attributes.isEmpty()) {
			writer.writeln(DotGraph.NODE + ' ' + assembleAttributes(attributes));
		}
	}

	public void addDefaultEdgeAttributes(final Map<String, String> attributes) throws IOException {
		checkState(DotWriterState.STARTED);

		if (attributes != null && !attributes.isEmpty()) {
			writer.writeln(DotGraph.EDGE + ' ' + assembleAttributes(attributes));
		}
	}

	public void addGraphAttribute(final String key, final String value) throws IOException {
		checkState(DotWriterState.STARTED);

		writer.writeln(assembleAttribute(key, value));
	}

	public void addNode(final String id) throws IOException {
		addNode(id, null);
	}

	public void addNode(final String id, final Map<String, String> attributes) throws IOException {
		checkState(DotWriterState.STARTED);

		if (attributes == null || attributes.isEmpty()) {
			writer.writeln('"' + id + '"');
		} else {
			writer.writeln('"' + id + '"' + ' ' + assembleAttributes(attributes));
		}
	}

	public void addEdge(final String sourceId, final String targetId) throws IOException {
		addEdge(sourceId, targetId, null);
	}

	public void addEdge(final String sourceId, final String targetId, final Map<String, String> attributes) throws IOException {
		checkState(DotWriterState.STARTED);

		String edgeConnector;
		if (graphType == DotGraphType.UNDIRECTED) {
			edgeConnector = DotGraph.UNDIRECTED_EDGE_CONNECTOR;
		} else {
			edgeConnector = DotGraph.DIRECTED_EDGE_CONNECTOR;
		}

		if (attributes == null || attributes.isEmpty()) {
			writer.writeln('"' + sourceId + '"' + ' ' + edgeConnector + ' ' + '"' + targetId + '"');
		} else {
			writer.writeln('"' + sourceId + '"' + ' ' + edgeConnector + ' ' + '"' + targetId + '"' + ' ' + assembleAttributes(attributes));
		}
	}

	public void addSubgraphStart(final String name) throws IOException {
		checkState(DotWriterState.STARTED);

		writer.writeln(DotGraph.SUB_START_TOKEN + ' ' + '"' + name + '"' + ' ' + DotGraph.START_GRAPH_BRACKET);
		writer.indent();
		openSubgraphs++;
	}

	public void addSubgraphStop() throws IOException {
		checkState(DotWriterState.STARTED);

		if (openSubgraphs == 0) {
			throw new IllegalStateException("There is no subgraph to close.");
		}

		writer.unindent();
		writer.writeln(DotGraph.END_GRAPH_BRACKET);
		openSubgraphs--;
	}

	public void addClusterStart(final String name) throws IOException {
		addSubgraphStart(DotGraph.CLUSTER_PREFIX + name);
	}

	public void addClusterStop() throws IOException {
		addSubgraphStop();
	}

	private void checkState(final DotWriterState expectedState) {
		if (state != expectedState) {
			switch (expectedState) {
			case CREATED:
				throw new IllegalStateException("The writing has already been started.");
			case STARTED:
				throw new IllegalStateException("The writing has never started or already been finished.");
			case FINISHED:
				throw new IllegalStateException("The writing has not been finished.");
			default:
				throw new IllegalStateException();
			}
		}
	}

	private String assembleAttributes(final Map<String, String> attributes) {
		return DotGraph.START_ATTRS_BRACKET
				+ attributes.entrySet().stream()
						.map(e -> assembleAttribute(e.getKey(), e.getValue()))
						.collect(Collectors.joining(","))
				+ DotGraph.END_ATTRS_BRACKET;
	}

	private String assembleAttribute(final String key, final String value) {
		return key + DotGraph.ATTR_CONNECTOR + '"' + value + '"';
	}

	private enum DotWriterState {
		CREATED, STARTED, FINISHED
	}

}
