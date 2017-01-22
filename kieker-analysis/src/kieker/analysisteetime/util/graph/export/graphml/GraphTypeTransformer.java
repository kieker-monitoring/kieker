package kieker.analysisteetime.util.graph.export.graphml;

import java.util.List;

import org.graphdrawing.graphml.DataType;
import org.graphdrawing.graphml.EdgeType;
import org.graphdrawing.graphml.GraphEdgedefaultType;
import org.graphdrawing.graphml.GraphType;
import org.graphdrawing.graphml.NodeType;

import kieker.analysisteetime.util.graph.Direction;
import kieker.analysisteetime.util.graph.Edge;
import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.Vertex;
import kieker.analysisteetime.util.graph.export.AbstractTransformer;

@Deprecated
public class GraphTypeTransformer extends AbstractTransformer<GraphType> {

	private final GraphType graphType;

	public GraphTypeTransformer(final Graph graph) {
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
		}

		if (vertex.hasChildGraph()) {
			Graph childGraph = vertex.getChildGraph();
			GraphTypeTransformer graphTypeTransformer = new GraphTypeTransformer(childGraph);
			GraphType childGraphType = graphTypeTransformer.transform();
			nodeType.setGraph(childGraphType);
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
		}

		graphType.getDataOrNodeOrEdge().add(edgeType);
	}

	@Override
	protected GraphType getTransformation() {
		return graphType;
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
