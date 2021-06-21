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

package kieker.analysis.graph.export.graphml;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.graphdrawing.graphml.DataType;
import org.graphdrawing.graphml.EdgeType;
import org.graphdrawing.graphml.GraphEdgedefaultType;
import org.graphdrawing.graphml.GraphType;
import org.graphdrawing.graphml.GraphmlType;
import org.graphdrawing.graphml.KeyForType;
import org.graphdrawing.graphml.KeyType;
import org.graphdrawing.graphml.KeyTypeType;
import org.graphdrawing.graphml.NodeType;

import kieker.analysis.graph.Direction;
import kieker.analysis.graph.IEdge;
import kieker.analysis.graph.IGraph;
import kieker.analysis.graph.IVertex;
import kieker.analysis.graph.export.AbstractTransformer;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class GraphMLTransformer extends AbstractTransformer<GraphmlType> {

	private final GraphType graphType;
	private final Set<String> nodeKeys = new HashSet<>();
	private final Set<String> edgeKeys = new HashSet<>();

	public GraphMLTransformer(final IGraph graph) {
		super(graph);
		this.graphType = new GraphType();
		this.graphType.setEdgedefault(GraphEdgedefaultType.DIRECTED);
		this.graphType.setId(graph.getName());
	}

	@Override
	protected void transformVertex(final IVertex vertex) {
		final NodeType nodeType = new NodeType();
		nodeType.setId(vertex.getId().toString());
		final List<Object> dataOrPort = nodeType.getDataOrPort();
		for (final String propertyKey : vertex.getPropertyKeys()) {
			final DataType dataType = new DataType();
			dataType.setKey(propertyKey);
			dataType.setContent(vertex.getProperty(propertyKey).toString());
			dataOrPort.add(dataType);
			this.nodeKeys.add(propertyKey);
		}

		if (vertex.hasChildGraph()) {
			final IGraph childGraph = vertex.getChildGraph();
			final GraphMLTransformer graphmlTypeTransformer = new GraphMLTransformer(childGraph);
			final GraphmlType childGraphmlType = graphmlTypeTransformer.transform();
			for (final Object childGraphType : childGraphmlType.getGraphOrData()) {
				if (childGraphType instanceof GraphType) {
					nodeType.setGraph((GraphType) childGraphType);
				}
			}
			for (final KeyType keyType : childGraphmlType.getKey()) {
				final KeyForType keyForType = keyType.getFor();
				switch (keyForType) {
				case NODE:
					this.nodeKeys.add(keyType.getAttrName());
					break;
				case EDGE:
					this.edgeKeys.add(keyType.getAttrName());
					break;
				default:
					break;
				}
			}
		}

		this.graphType.getDataOrNodeOrEdge().add(nodeType);
	}

	@Override
	protected void transformEdge(final IEdge edge) {
		final EdgeType edgeType = new EdgeType();
		edgeType.setId(edge.getId().toString());
		edgeType.setSource(edge.getVertex(Direction.OUT).getId().toString());
		edgeType.setTarget(edge.getVertex(Direction.IN).getId().toString());
		final List<DataType> data = edgeType.getData();
		for (final String propertyKey : edge.getPropertyKeys()) {
			final DataType dataType = new DataType();
			dataType.setKey(propertyKey);
			dataType.setContent(edge.getProperty(propertyKey).toString());
			data.add(dataType);
			this.edgeKeys.add(propertyKey);
		}

		this.graphType.getDataOrNodeOrEdge().add(edgeType);
	}

	@Override
	protected GraphmlType getTransformation() {
		final GraphmlType graphmlType = new GraphmlType();
		for (final String key : this.nodeKeys) {
			final KeyType keyType = new KeyType();
			keyType.setId(key);
			keyType.setFor(KeyForType.NODE);
			keyType.setAttrName(key);
			keyType.setAttrType(KeyTypeType.STRING);
			graphmlType.getKey().add(keyType);
		}
		for (final String key : this.edgeKeys) {
			final KeyType keyType = new KeyType();
			keyType.setId(key);
			keyType.setFor(KeyForType.EDGE);
			keyType.setAttrName(key);
			keyType.setAttrType(KeyTypeType.STRING);
			graphmlType.getKey().add(keyType);
		}
		graphmlType.getGraphOrData().add(this.graphType);
		return graphmlType;
	}

	@Override
	protected void beforeTransformation() {
		// Do nothing
	}

	@Override
	protected void afterTransformation() {
		// Do nothing
	}

}
