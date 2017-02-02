package kieker.analysisteetime.util.graph.export.dot;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import kieker.analysisteetime.util.graph.Edge;
import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.Vertex;
import kieker.analysisteetime.util.graph.util.dot.attributes.DotClusterAttribute;
import kieker.analysisteetime.util.graph.util.dot.attributes.DotEdgeAttribute;
import kieker.analysisteetime.util.graph.util.dot.attributes.DotGraphAttribute;
import kieker.analysisteetime.util.graph.util.dot.attributes.DotNodeAttribute;

/**
 * This class specifies how attributes (for graphs, vertices and edges) are mapped
 * to a dot graph.
 *
 * @author Sören Henning
 *
 * @since 1.13
 */
public class DotExportConfiguration {

	final private Map<DotGraphAttribute, Function<Graph, String>> graphAttributes = new EnumMap<>(DotGraphAttribute.class);
	final private Map<DotNodeAttribute, Function<Graph, String>> defaultNodeAttributes = new EnumMap<>(DotNodeAttribute.class);
	final private Map<DotEdgeAttribute, Function<Graph, String>> defaultEdgeAttributes = new EnumMap<>(DotEdgeAttribute.class);
	final private Map<DotNodeAttribute, Function<Vertex, String>> nodeAttributes = new EnumMap<>(DotNodeAttribute.class);
	final private Map<DotEdgeAttribute, Function<Edge, String>> edgeAttributes = new EnumMap<>(DotEdgeAttribute.class);
	final private Map<DotClusterAttribute, Function<Vertex, String>> clusterAttributes = new EnumMap<>(DotClusterAttribute.class);

	public Map<DotGraphAttribute, Function<Graph, String>> getGraphAttributes() {
		return this.graphAttributes;
	}

	public Map<DotNodeAttribute, Function<Graph, String>> getDefaultNodeAttributes() {
		return this.defaultNodeAttributes;
	}

	public Map<DotEdgeAttribute, Function<Graph, String>> getDefaultEdgeAttributes() {
		return this.defaultEdgeAttributes;
	}

	public Map<DotNodeAttribute, Function<Vertex, String>> getNodeAttributes() {
		return this.nodeAttributes;
	}

	public Map<DotEdgeAttribute, Function<Edge, String>> getEdgeAttributes() {
		return this.edgeAttributes;
	}

	public Map<DotClusterAttribute, Function<Vertex, String>> getClusterAttributes() {
		return this.clusterAttributes;
	}

	public void addGraphAttribute(final DotGraphAttribute attribute, final Function<Graph, String> function) {
		this.graphAttributes.put(attribute, function);
	}

	public void addDefaultNodeAttribute(final DotNodeAttribute attribute, final Function<Graph, String> function) {
		this.defaultNodeAttributes.put(attribute, function);
	}

	public void addDefaultEdgeAttribute(final DotEdgeAttribute attribute, final Function<Graph, String> function) {
		this.defaultEdgeAttributes.put(attribute, function);
	}

	public void addNodeAttribute(final DotNodeAttribute attribute, final Function<Vertex, String> function) {
		this.nodeAttributes.put(attribute, function);
	}

	public void addEdgeAttribute(final DotEdgeAttribute attribute, final Function<Edge, String> function) {
		this.edgeAttributes.put(attribute, function);
	}

	public void addClusterAttribute(final DotClusterAttribute attribute, final Function<Vertex, String> function) {
		this.clusterAttributes.put(attribute, function);
	}
}
