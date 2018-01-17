package kieker.analysisteetime.util.graph.export.graphml;

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

import kieker.analysisteetime.util.graph.Direction;
import kieker.analysisteetime.util.graph.Edge;
import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.Vertex;
import kieker.analysisteetime.util.graph.export.AbstractTransformer;

public class GraphMLTransformer extends AbstractTransformer<GraphmlType> {

	private final GraphType graphType;
	private final Set<String> nodeKeys = new HashSet<>();
	private final Set<String> edgeKeys = new HashSet<>();

	public GraphMLTransformer(final Graph graph) {
		super(graph);
		graphType = new GraphType();
		graphType.setEdgedefault(GraphEdgedefaultType.DIRECTED);
		graphType.setId(graph.getName());
	}

	@Override
	protected void transformVertex(final Vertex vertex) {

		NodeType nodeType = new NodeType();
		nodeType.setId(vertex.getId().toString());
		List<Object> dataOrPort = nodeType.getDataOrPort();
		for (final String propertyKey : vertex.getPropertyKeys()) {
			DataType dataType = new DataType();
			dataType.setKey(propertyKey);
			dataType.setContent(vertex.getProperty(propertyKey).toString());
			dataOrPort.add(dataType);
			nodeKeys.add(propertyKey);
		}

		if (vertex.hasChildGraph()) {
			Graph childGraph = vertex.getChildGraph();
			GraphMLTransformer graphmlTypeTransformer = new GraphMLTransformer(childGraph);
			GraphmlType childGraphmlType = graphmlTypeTransformer.transform();
			for (Object childGraphType : childGraphmlType.getGraphOrData()) {
				if (childGraphType instanceof GraphType) {
					nodeType.setGraph((GraphType) childGraphType);
				}
			}
			for (KeyType keyType : childGraphmlType.getKey()) {
				KeyForType keyForType = keyType.getFor();
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

		graphType.getDataOrNodeOrEdge().add(nodeType);
	}

	@Override
	protected void transformEdge(final Edge edge) {
		EdgeType edgeType = new EdgeType();
		edgeType.setId(edge.getId().toString());
		edgeType.setSource(edge.getVertex(Direction.OUT).getId().toString());
		edgeType.setTarget(edge.getVertex(Direction.IN).getId().toString());
		List<DataType> data = edgeType.getData();
		for (final String propertyKey : edge.getPropertyKeys()) {
			DataType dataType = new DataType();
			dataType.setKey(propertyKey);
			dataType.setContent(edge.getProperty(propertyKey).toString());
			data.add(dataType);
			this.edgeKeys.add(propertyKey);
		}

		graphType.getDataOrNodeOrEdge().add(edgeType);
	}

	@Override
	protected GraphmlType getTransformation() {
		GraphmlType graphmlType = new GraphmlType();
		for (String key : this.nodeKeys) {
			KeyType keyType = new KeyType();
			keyType.setId(key);
			keyType.setFor(KeyForType.NODE);
			keyType.setAttrName(key);
			keyType.setAttrType(KeyTypeType.STRING);
			graphmlType.getKey().add(keyType);
		}
		for (String key : this.edgeKeys) {
			KeyType keyType = new KeyType();
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
