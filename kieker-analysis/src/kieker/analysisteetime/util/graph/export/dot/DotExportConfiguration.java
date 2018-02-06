package kieker.analysisteetime.util.graph.export.dot;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
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
 * To create a {@link DotExportConfiguration} use the {@link DotExportConfiguration.Builder}.
 *
 * @author Sören Henning
 *
 * @since 1.13
 */
public class DotExportConfiguration {

	/*
	 * BETTER Even if EnumMaps are very efficient in terms of lookup, etc. they require
	 * a constant memory space, which is the size of the enums list. Moreover, we only
	 * want to iterate over the map and do not perform any lookups etc. A more efficient
	 * way would be to use a List of Pairs of constant size.
	 */
	// PMD: No concurrent access intended for the following attributes
	final protected Map<DotGraphAttribute, Function<Graph, String>> graphAttributes = new EnumMap<>(DotGraphAttribute.class); // NOPMD (see above)
	final protected Map<DotNodeAttribute, Function<Graph, String>> defaultNodeAttributes = new EnumMap<>(DotNodeAttribute.class); // NOPMD (see above)
	final protected Map<DotEdgeAttribute, Function<Graph, String>> defaultEdgeAttributes = new EnumMap<>(DotEdgeAttribute.class); // NOPMD (see above)
	final protected Map<DotNodeAttribute, Function<Vertex, String>> nodeAttributes = new EnumMap<>(DotNodeAttribute.class); // NOPMD (see above)
	final protected Map<DotEdgeAttribute, Function<Edge, String>> edgeAttributes = new EnumMap<>(DotEdgeAttribute.class); // NOPMD (see above)
	final protected Map<DotClusterAttribute, Function<Vertex, String>> clusterAttributes = new EnumMap<>(DotClusterAttribute.class); // NOPMD (see above)

	protected DotExportConfiguration() {
		// Create an empty export configuration
	}

	public Set<Map.Entry<DotGraphAttribute, Function<Graph, String>>> getGraphAttributes() {
		return Collections.unmodifiableSet(this.graphAttributes.entrySet());
	}

	public Set<Map.Entry<DotNodeAttribute, Function<Graph, String>>> getDefaultNodeAttributes() {
		return Collections.unmodifiableSet(this.defaultNodeAttributes.entrySet());
	}

	public Set<Map.Entry<DotEdgeAttribute, Function<Graph, String>>> getDefaultEdgeAttributes() {
		return Collections.unmodifiableSet(this.defaultEdgeAttributes.entrySet());
	}

	public Set<Map.Entry<DotNodeAttribute, Function<Vertex, String>>> getNodeAttributes() {
		return Collections.unmodifiableSet(this.nodeAttributes.entrySet());
	}

	public Set<Map.Entry<DotEdgeAttribute, Function<Edge, String>>> getEdgeAttributes() {
		return Collections.unmodifiableSet(this.edgeAttributes.entrySet());
	}

	public Set<Map.Entry<DotClusterAttribute, Function<Vertex, String>>> getClusterAttributes() {
		return Collections.unmodifiableSet(this.clusterAttributes.entrySet());
	}

	public static class Builder {

		private final DotExportConfiguration configuration = new DotExportConfiguration();

		public void addGraphAttribute(final DotGraphAttribute attribute, final Function<Graph, String> function) {
			this.configuration.graphAttributes.put(attribute, function);
		}

		public void addDefaultNodeAttribute(final DotNodeAttribute attribute, final Function<Graph, String> function) {
			this.configuration.defaultNodeAttributes.put(attribute, function);
		}

		public void addDefaultEdgeAttribute(final DotEdgeAttribute attribute, final Function<Graph, String> function) {
			this.configuration.defaultEdgeAttributes.put(attribute, function);
		}

		public void addNodeAttribute(final DotNodeAttribute attribute, final Function<Vertex, String> function) {
			this.configuration.nodeAttributes.put(attribute, function);
		}

		public void addEdgeAttribute(final DotEdgeAttribute attribute, final Function<Edge, String> function) {
			this.configuration.edgeAttributes.put(attribute, function);
		}

		public void addClusterAttribute(final DotClusterAttribute attribute, final Function<Vertex, String> function) {
			this.configuration.clusterAttributes.put(attribute, function);
		}

		public DotExportConfiguration build() {
			return this.configuration;
		}

	}
}
