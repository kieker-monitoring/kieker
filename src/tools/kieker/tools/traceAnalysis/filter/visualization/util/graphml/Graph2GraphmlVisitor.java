/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.OperationAllocationDependencyGraph;
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.ResponseTimeDecoration;
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.WeightedBidirectionalDependencyGraphEdge;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraph.IGraphVisitor;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraphElement;
import kieker.tools.traceAnalysis.filter.visualization.util.NamingConventions;
import kieker.tools.traceAnalysis.systemModel.util.AllocationComponentOperationPair;

/**
 * @author Christian Wulf
 * 
 * @since 1.9
 */
public class Graph2GraphmlVisitor implements
		IGraphVisitor<DependencyGraphNode<AllocationComponentOperationPair>, WeightedBidirectionalDependencyGraphEdge<AllocationComponentOperationPair>> {

	private final ObjectFactory objectFactory = new ObjectFactory();
	private final GraphmlType graphml;
	private final GraphType graph;
	private final boolean includeWeights;
	private final boolean useShortLabels;
	private final boolean plotLoops;

	private final Map<String, NodeType> components = new HashMap<String, NodeType>(); // NOPMD (hashmap)
	private final Map<String, String> graphmlNodes = new HashMap<String, String>(); // NOPMD (hashmap)

	public Graph2GraphmlVisitor(final boolean includeWeights, final boolean useShortLabels, final boolean plotLoops) {
		this.includeWeights = includeWeights;
		this.useShortLabels = useShortLabels;
		this.plotLoops = plotLoops;

		this.graphml = this.createKiekerGraph();

		this.graph = this.objectFactory.createGraphType();
		this.graphml.getGraphOrData().add(this.graph);
	}

	public void buildAssemblyComponentNodes(final OperationAllocationDependencyGraph kiekerGraph) {
		for (final DependencyGraphNode<AllocationComponentOperationPair> vertex : kiekerGraph.getVertices()) {
			final NodeType allocationComponentNode = this.objectFactory.createNodeType();
			allocationComponentNode.setGraph(this.objectFactory.createGraphType());
			allocationComponentNode.getGraph().setId(NamingConventions.createSubgraphId());

			final String componentId = NamingConventions.createComponentId(vertex.getEntity().getAllocationComponent().getId());
			allocationComponentNode.setId(componentId);

			final String label = NamingConventions.createAllocationComponentNodeLabel(vertex.getEntity().getAllocationComponent(), this.useShortLabels);
			final DataType labelAttribute = this.createAttribute(vertex, GraphmlKey.LABEL, label);
			allocationComponentNode.getDataOrPort().add(labelAttribute);

			// allocationComponentNode.setDesc(label + "-node");
			// allocationComponentNode.getGraph().setDesc(label + "-graph");

			this.graph.getDataOrNodeOrEdge().add(allocationComponentNode);

			this.components.put(componentId, allocationComponentNode);
		}
	}

	public void visitVertex(final DependencyGraphNode<AllocationComponentOperationPair> vertex) {
		final NodeType graphmlNode = this.objectFactory.createNodeType();
		graphmlNode.setId(NamingConventions.createNodeId(vertex.getId()));

		final DataType description = this.createAttribute(vertex, GraphmlKey.DESCRIPTION, vertex.getDescription());
		graphmlNode.getDataOrPort().add(description);
		final DataType color = this.createAttribute(vertex, GraphmlKey.COLOR, Integer.toString(vertex.getColor().getRGB()));
		graphmlNode.getDataOrPort().add(color);

		final ResponseTimeDecoration responseTimeDecoration = vertex.getDecoration(ResponseTimeDecoration.class);
		if (responseTimeDecoration != null) {
			DataType attribute = this.createAttribute(vertex, GraphmlKey.AVG_RESPONSE_TIMES, Double.toString(responseTimeDecoration.getAverageResponseTime()));
			graphmlNode.getDataOrPort().add(attribute);
			attribute = this.createAttribute(vertex, GraphmlKey.MIN_RESPONSE_TIMES, Double.toString(responseTimeDecoration.getMinimalResponseTime()));
			graphmlNode.getDataOrPort().add(attribute);
			attribute = this.createAttribute(vertex, GraphmlKey.MAX_RESPONSE_TIMES, Double.toString(responseTimeDecoration.getMaximalResponseTime()));
			graphmlNode.getDataOrPort().add(attribute);
		}

		final String operationSignature = NamingConventions.createOperationSignature(vertex.getEntity().getOperation());
		final DataType operationSignatureAttribute = this.createAttribute(vertex, GraphmlKey.OPERATION_SIGNATURE, operationSignature);
		graphmlNode.getDataOrPort().add(operationSignatureAttribute);

		final String componentId = NamingConventions.createComponentId(vertex.getEntity().getAllocationComponent().getId());
		final NodeType componentNode = this.components.get(componentId);
		componentNode.getGraph().getDataOrNodeOrEdge().add(graphmlNode);

		this.graphmlNodes.put(NamingConventions.createNodeId(vertex.getId()), graphmlNode.getId());
	}

	public void visitEdge(final WeightedBidirectionalDependencyGraphEdge<AllocationComponentOperationPair> edge) {
		if (!this.plotLoops && edge.getSource().equals(edge.getTarget())) {
			return;
		}

		final EdgeType graphmlEdge = this.objectFactory.createEdgeType();
		graphmlEdge.setId(edge.getIdentifier());

		final String sourceGraphmlNodeId = this.graphmlNodes.get(NamingConventions.createNodeId(edge.getSource().getId()));
		final String targetGraphmlNodeId = this.graphmlNodes.get(NamingConventions.createNodeId(edge.getTarget().getId()));
		graphmlEdge.setSource(sourceGraphmlNodeId);
		graphmlEdge.setTarget(targetGraphmlNodeId);

		final DataType assumed = this.createAttribute(edge, GraphmlKey.ASSUMED, Boolean.toString(edge.isAssumed()));
		graphmlEdge.getData().add(assumed);
		final DataType description = this.createAttribute(edge, GraphmlKey.DESCRIPTION, edge.getDescription());
		graphmlEdge.getData().add(description);
		final DataType color = this.createAttribute(edge, GraphmlKey.COLOR, Integer.toString(edge.getColor().getRGB()));
		graphmlEdge.getData().add(color);
		if (this.includeWeights) {
			final DataType weight = this.createAttribute(edge, GraphmlKey.WEIGHT, Integer.toString(edge.getTargetWeight().get()));
			graphmlEdge.getData().add(weight);
		}

		this.graph.getDataOrNodeOrEdge().add(graphmlEdge);
	}

	public GraphmlType getGraphml() {
		return this.graphml;
	}

	private GraphmlType createKiekerGraph() {
		final GraphmlType graphmlType = this.objectFactory.createGraphmlType();

		for (final GraphmlKey keyDefinition : GraphmlKey.values()) {
			final KeyType graphmlKey = this.createGraphmlKey(keyDefinition.keyName, keyDefinition.entitytype, keyDefinition.keyName, keyDefinition.attributeType);
			graphmlType.getKey().add(graphmlKey);
		}

		return graphmlType;
	}

	private KeyType createGraphmlKey(final String identifier, final KeyForType entityType, final String attributeName,
			final KeyTypeType attributeType) {
		final KeyType key = this.objectFactory.createKeyType();
		key.setId(identifier);
		key.setFor(entityType);
		key.setAttrName(attributeName);
		key.setAttrType(attributeType);
		return key;
	}

	private DataType createAttribute(final DependencyGraphNode<?> vertex, final GraphmlKey key, final String value) {
		return this.createAttribute(Integer.toString(vertex.getId()), key.toString(), value);
	}

	private DataType createAttribute(final AbstractGraphElement<?> kiekerGraphElement, final GraphmlKey key, final String value) {
		return this.createAttribute(kiekerGraphElement.getIdentifier(), key.toString(), value);
	}

	private DataType createAttribute(final String identifier, final String key, final String value) {
		final DataType attribute = this.objectFactory.createDataType();
		// attribute.setId(identifier);
		attribute.setKey(key);
		attribute.setContent(value);
		return attribute;
	}

	private static enum GraphmlKey {

		ASSUMED("assumed", KeyForType.EDGE, KeyTypeType.BOOLEAN),
		DESCRIPTION("description", KeyForType.ALL, KeyTypeType.STRING),
		COLOR("color", KeyForType.ALL, KeyTypeType.INT),
		WEIGHT("weight", KeyForType.EDGE, KeyTypeType.INT),
		OPERATION_SIGNATURE("operation signature", KeyForType.NODE, KeyTypeType.STRING),
		LABEL("label", KeyForType.NODE, KeyTypeType.STRING),
		AVG_RESPONSE_TIMES("avg response times", KeyForType.NODE, KeyTypeType.DOUBLE),
		MIN_RESPONSE_TIMES("min response times", KeyForType.NODE, KeyTypeType.DOUBLE),
		MAX_RESPONSE_TIMES("max response times", KeyForType.NODE, KeyTypeType.DOUBLE);

		private final String keyName;
		private final KeyForType entitytype;
		private final KeyTypeType attributeType;

		private GraphmlKey(final String keyName, final KeyForType entitytype, final KeyTypeType attributeType) {
			this.keyName = keyName;
			this.entitytype = entitytype;
			this.attributeType = attributeType;
		}

		@Override
		public String toString() {
			return this.keyName;
		}
	}

}
