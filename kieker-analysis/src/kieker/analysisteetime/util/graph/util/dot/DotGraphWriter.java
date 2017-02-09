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
		this.start(DotGraphType.DIRECTED, name);
	}

	public void start(final DotGraphType graphType, final String name) throws IOException {
		this.checkState(DotWriterState.CREATED);

		this.graphType = graphType;

		String openToken;
		if (graphType == DotGraphType.UNDIRECTED) {
			openToken = DotGraph.UNDIRECTED_START_TOKEN;
		} else {
			openToken = DotGraph.DIRECTED_START_TOKEN;
		}
		this.writer.writeln(openToken + ' ' + '"' + name + '"' + ' ' + DotGraph.START_GRAPH_BRACKET);
		this.writer.indent();
		this.state = DotWriterState.STARTED;
	}

	public void finish() throws IOException {
		this.checkState(DotWriterState.STARTED);

		if (this.openSubgraphs > 0) {
			throw new IllegalStateException("There are unclosed subgraphs.");
		}

		this.writer.unindent();
		this.writer.writeln(DotGraph.END_GRAPH_BRACKET);
		this.state = DotWriterState.FINISHED;

		this.writer.close();
	}

	public void addDefaultNodeAttributes(final Map<String, String> attributes) throws IOException {
		this.checkState(DotWriterState.STARTED);

		if ((attributes != null) && !attributes.isEmpty()) {
			this.writer.writeln(DotGraph.NODE + ' ' + this.assembleAttributes(attributes));
		}
	}

	public void addDefaultEdgeAttributes(final Map<String, String> attributes) throws IOException {
		this.checkState(DotWriterState.STARTED);

		if ((attributes != null) && !attributes.isEmpty()) {
			this.writer.writeln(DotGraph.EDGE + ' ' + this.assembleAttributes(attributes));
		}
	}

	public void addGraphAttribute(final String key, final String value) throws IOException {
		this.checkState(DotWriterState.STARTED);

		this.writer.writeln(this.assembleAttribute(key, value));
	}

	public void addNode(final String id) throws IOException {
		this.addNode(id, null);
	}

	public void addNode(final String id, final Map<String, String> attributes) throws IOException {
		this.checkState(DotWriterState.STARTED);

		if ((attributes == null) || attributes.isEmpty()) {
			this.writer.writeln('"' + id + '"');
		} else {
			this.writer.writeln('"' + id + '"' + ' ' + this.assembleAttributes(attributes));
		}
	}

	public void addEdge(final String sourceId, final String targetId) throws IOException {
		this.addEdge(sourceId, targetId, null);
	}

	public void addEdge(final String sourceId, final String targetId, final Map<String, String> attributes) throws IOException {
		this.checkState(DotWriterState.STARTED);

		String edgeConnector;
		if (this.graphType == DotGraphType.UNDIRECTED) {
			edgeConnector = DotGraph.UNDIRECTED_EDGE_CONNECTOR;
		} else {
			edgeConnector = DotGraph.DIRECTED_EDGE_CONNECTOR;
		}

		if ((attributes == null) || attributes.isEmpty()) {
			this.writer.writeln('"' + sourceId + '"' + ' ' + edgeConnector + ' ' + '"' + targetId + '"');
		} else {
			this.writer.writeln('"' + sourceId + '"' + ' ' + edgeConnector + ' ' + '"' + targetId + '"' + ' ' + this.assembleAttributes(attributes));
		}
	}

	public void addSubgraphStart(final String name) throws IOException {
		this.checkState(DotWriterState.STARTED);

		this.writer.writeln(DotGraph.SUB_START_TOKEN + ' ' + '"' + name + '"' + ' ' + DotGraph.START_GRAPH_BRACKET);
		this.writer.indent();
		this.openSubgraphs++;
	}

	public void addSubgraphStop() throws IOException {
		this.checkState(DotWriterState.STARTED);

		if (this.openSubgraphs == 0) {
			throw new IllegalStateException("There is no subgraph to close.");
		}

		this.writer.unindent();
		this.writer.writeln(DotGraph.END_GRAPH_BRACKET);
		this.openSubgraphs--;
	}

	public void addClusterStart(final String name) throws IOException {
		this.addSubgraphStart(DotGraph.CLUSTER_PREFIX + name);
	}

	public void addClusterStop() throws IOException {
		this.addSubgraphStop();
	}

	private void checkState(final DotWriterState expectedState) {
		if (this.state != expectedState) {
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
						.map(e -> this.assembleAttribute(e.getKey(), e.getValue()))
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
