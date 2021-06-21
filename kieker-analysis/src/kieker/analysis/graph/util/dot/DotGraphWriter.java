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

package kieker.analysis.graph.util.dot;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.stream.Collectors;

import kieker.analysis.graph.util.IndentWriter;

/**
 * Class to build and write a DOT Graph to a writer.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class DotGraphWriter {

	private final IndentWriter writer;
	private DotWriterState state = DotWriterState.CREATED;
	private int openSubgraphs; // = 0

	private DotGraphType graphType = DotGraphType.DIRECTED;

	public DotGraphWriter(final Writer writer) {
		this.writer = new IndentWriter(writer);
	}

	public void start(final String name) throws IOException {
		this.start(DotGraphType.DIRECTED, name);
	}

	public void start(final DotGraphType newGraphType, final String name) throws IOException {
		this.checkState(DotWriterState.CREATED);

		this.graphType = newGraphType;

		final String openToken;
		if (this.graphType == DotGraphType.UNDIRECTED) {
			openToken = DotGraphConstants.UNDIRECTED_START_TOKEN;
		} else {
			openToken = DotGraphConstants.DIRECTED_START_TOKEN;
		}
		this.writer.writeln(openToken + ' ' + '"' + name + '"' + ' ' + DotGraphConstants.START_GRAPH_BRACKET);
		this.writer.indent();
		this.state = DotWriterState.STARTED;
	}

	public void finish() throws IOException {
		this.checkState(DotWriterState.STARTED);

		if (this.openSubgraphs > 0) {
			throw new IllegalStateException("There are unclosed subgraphs.");
		}

		this.writer.unindent();
		this.writer.writeln(DotGraphConstants.END_GRAPH_BRACKET);
		this.state = DotWriterState.FINISHED;

		this.writer.close();
	}

	public void addDefaultNodeAttributes(final Map<String, String> attributes) throws IOException {
		this.checkState(DotWriterState.STARTED);

		if ((attributes != null) && !attributes.isEmpty()) {
			this.writer.writeln(DotGraphConstants.NODE + ' ' + this.assembleAttributes(attributes));
		}
	}

	public void addDefaultEdgeAttributes(final Map<String, String> attributes) throws IOException {
		this.checkState(DotWriterState.STARTED);

		if ((attributes != null) && !attributes.isEmpty()) {
			this.writer.writeln(DotGraphConstants.EDGE + ' ' + this.assembleAttributes(attributes));
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

		final String edgeConnector;
		if (this.graphType == DotGraphType.UNDIRECTED) {
			edgeConnector = DotGraphConstants.UNDIRECTED_EDGE_CONNECTOR;
		} else {
			edgeConnector = DotGraphConstants.DIRECTED_EDGE_CONNECTOR;
		}

		if ((attributes == null) || attributes.isEmpty()) {
			this.writer.writeln('"' + sourceId + '"' + ' ' + edgeConnector + ' ' + '"' + targetId + '"');
		} else {
			this.writer.writeln('"' + sourceId + '"' + ' ' + edgeConnector + ' ' + '"' + targetId + '"' + ' ' + this.assembleAttributes(attributes));
		}
	}

	public void addSubgraphStart(final String name) throws IOException {
		this.checkState(DotWriterState.STARTED);

		this.writer.writeln(DotGraphConstants.SUB_START_TOKEN + ' ' + '"' + name + '"' + ' ' + DotGraphConstants.START_GRAPH_BRACKET);
		this.writer.indent();
		this.openSubgraphs++;
	}

	public void addSubgraphStop() throws IOException {
		this.checkState(DotWriterState.STARTED);

		if (this.openSubgraphs == 0) {
			throw new IllegalStateException("There is no subgraph to close.");
		}

		this.writer.unindent();
		this.writer.writeln(DotGraphConstants.END_GRAPH_BRACKET);
		this.openSubgraphs--;
	}

	public void addClusterStart(final String name) throws IOException {
		this.addSubgraphStart(DotGraphConstants.CLUSTER_PREFIX + name);
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
		return DotGraphConstants.START_ATTRS_BRACKET
				+ attributes.entrySet().stream()
						.map(e -> this.assembleAttribute(e.getKey(), e.getValue()))
						.collect(Collectors.joining(","))
				+ DotGraphConstants.END_ATTRS_BRACKET;
	}

	private String assembleAttribute(final String key, final String value) {
		return key + DotGraphConstants.ATTR_CONNECTOR + '"' + value + '"';
	}

	/**
	 * States of Dot graph writer
	 */
	private enum DotWriterState {
		CREATED, STARTED, FINISHED
	}

}
