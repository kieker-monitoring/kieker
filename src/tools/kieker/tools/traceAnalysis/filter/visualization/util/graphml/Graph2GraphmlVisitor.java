package kieker.tools.traceAnalysis.filter.visualization.util.graphml;

import java.util.HashMap;
import java.util.Map;

import org.graphdrawing.graphml.xmlns.DataType;
import org.graphdrawing.graphml.xmlns.EdgeType;
import org.graphdrawing.graphml.xmlns.GraphType;
import org.graphdrawing.graphml.xmlns.GraphmlType;
import org.graphdrawing.graphml.xmlns.KeyForType;
import org.graphdrawing.graphml.xmlns.KeyType;
import org.graphdrawing.graphml.xmlns.KeyTypeType;
import org.graphdrawing.graphml.xmlns.NodeType;
import org.graphdrawing.graphml.xmlns.ObjectFactory;

import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.DependencyGraphNode;
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.WeightedBidirectionalDependencyGraphEdge;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraph.IGraphVisitor;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraphElement;
import kieker.tools.traceAnalysis.filter.visualization.util.NamingConventions;
import kieker.tools.traceAnalysis.systemModel.util.AllocationComponentOperationPair;

public class Graph2GraphmlVisitor implements
		IGraphVisitor<DependencyGraphNode<AllocationComponentOperationPair>, WeightedBidirectionalDependencyGraphEdge<AllocationComponentOperationPair>> {

	private static final String ASSUMED_KEY = "assumed";
	private static final String DESCRIPTION_KEY = "description";
	private static final String COLOR_KEY = "color";
	private static final String WEIGHT_KEY = "weight";
	private static final String OPERATION_SIGNATURE_KEY = "operation signature";

	private final ObjectFactory objectFactory = new ObjectFactory();
	private final GraphmlType graphml;
	private final GraphType graph;
	private final boolean includeWeights;
	private final boolean useShortLabels;
	private final boolean plotLoops;

	private final Map<String, String> graphmlNodes = new HashMap<String, String>();

	public Graph2GraphmlVisitor(final boolean includeWeights, final boolean useShortLabels, final boolean plotLoops) {
		this.includeWeights = includeWeights;
		this.useShortLabels = useShortLabels;
		this.plotLoops = plotLoops;

		this.graphml = this.createKiekerGraph();

		this.graph = this.objectFactory.createGraphType();
		this.graphml.getGraphOrData().add(this.graph);
	}

	public void visitVertex(final DependencyGraphNode<AllocationComponentOperationPair> vertex) {
		final NodeType graphmlNode = this.objectFactory.createNodeType();
		graphmlNode.setId(vertex.getIdentifier());

		final DataType description = this.createAttribute(vertex, DESCRIPTION_KEY, vertex.getDescription());
		graphmlNode.getDataOrPort().add(description);
		final DataType color = this.createAttribute(vertex, COLOR_KEY, Integer.toString(vertex.getColor().getRGB()));
		graphmlNode.getDataOrPort().add(color);

		vertex.getEntity().getAllocationComponent();
		// TODO

		vertex.getOrigins();
		// TODO

		final String operationSignature = NamingConventions.createOperationSignature(vertex.getEntity().getOperation());
		final DataType operationSignatureAttribute = this.createAttribute(vertex, OPERATION_SIGNATURE_KEY, operationSignature);
		graphmlNode.getDataOrPort().add(operationSignatureAttribute);

		this.graph.getDataOrNodeOrEdge().add(graphmlNode);

		this.graphmlNodes.put(vertex.getIdentifier(), graphmlNode.getId());
	}

	public void visitEdge(final WeightedBidirectionalDependencyGraphEdge<AllocationComponentOperationPair> edge) {
		if (!this.plotLoops && edge.getSource().equals(edge.getTarget())) {
			return;
		}

		final EdgeType graphmlEdge = this.objectFactory.createEdgeType();
		graphmlEdge.setId(edge.getIdentifier());

		final String sourceGraphmlNodeId = this.graphmlNodes.get(edge.getSource().getIdentifier());
		final String targetGraphmlNodeId = this.graphmlNodes.get(edge.getTarget().getIdentifier());
		graphmlEdge.setSource(sourceGraphmlNodeId);
		graphmlEdge.setTarget(targetGraphmlNodeId);

		final DataType assumed = this.createAttribute(edge, ASSUMED_KEY, Boolean.toString(edge.isAssumed()));
		graphmlEdge.getData().add(assumed);
		final DataType description = this.createAttribute(edge, DESCRIPTION_KEY, edge.getDescription());
		graphmlEdge.getData().add(description);
		final DataType color = this.createAttribute(edge, COLOR_KEY, Integer.toString(edge.getColor().getRGB()));
		graphmlEdge.getData().add(color);
		if (this.includeWeights) {
			final DataType weight = this.createAttribute(edge, WEIGHT_KEY, Integer.toString(edge.getWeight().get()));
			graphmlEdge.getData().add(weight);
		}

		this.graph.getDataOrNodeOrEdge().add(graphmlEdge);
	}

	public GraphmlType getGraphml() {
		return this.graphml;
	}

	private GraphmlType createKiekerGraph() {
		final GraphmlType graphml = this.objectFactory.createGraphmlType();

		KeyType key = this.createKeyDefinition(ASSUMED_KEY, KeyForType.EDGE, ASSUMED_KEY, KeyTypeType.BOOLEAN);
		graphml.getKey().add(key);
		key = this.createKeyDefinition(DESCRIPTION_KEY, KeyForType.ALL, DESCRIPTION_KEY, KeyTypeType.STRING);
		graphml.getKey().add(key);
		key = this.createKeyDefinition(COLOR_KEY, KeyForType.ALL, COLOR_KEY, KeyTypeType.INT);
		graphml.getKey().add(key);
		key = this.createKeyDefinition(WEIGHT_KEY, KeyForType.EDGE, WEIGHT_KEY, KeyTypeType.INT);
		graphml.getKey().add(key);
		key = this.createKeyDefinition(OPERATION_SIGNATURE_KEY, KeyForType.NODE, OPERATION_SIGNATURE_KEY, KeyTypeType.STRING);
		graphml.getKey().add(key);

		return graphml;
	}

	private KeyType createKeyDefinition(final String identifier, final KeyForType entityType, final String attributeName,
			final KeyTypeType attributeType) {
		final KeyType key = this.objectFactory.createKeyType();
		key.setId(identifier);
		key.setFor(entityType);
		key.setAttrName(attributeName);
		key.setAttrType(attributeType);
		return key;
	}

	private DataType createAttribute(final AbstractGraphElement<?> kiekerGraphElement, final String key, final String value) {
		final DataType attribute = this.objectFactory.createDataType();
		attribute.setId(kiekerGraphElement.getIdentifier() + key);
		attribute.setKey(key);
		attribute.setContent(value);
		return attribute;
	}

}
