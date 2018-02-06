package kieker.analysisteetime.util.graph.util.dot;

/**
 * This class defines some constants which are used in dot graphs. These are,
 * for example, specific key words and symbols.
 *
 * @author Sören Henning
 *
 * @since 1.13
 */
public final class DotGraph {

	public static final String START_GRAPH_BRACKET = "{";

	public static final String END_GRAPH_BRACKET = "}";

	public static final String START_ATTRS_BRACKET = "[";

	public static final String END_ATTRS_BRACKET = "]";

	public static final String ATTR_CONNECTOR = "=";

	public static final String DIRECTED_START_TOKEN = "digraph";

	public static final String UNDIRECTED_START_TOKEN = "graph";

	public static final String SUB_START_TOKEN = "subgraph";

	public static final String NODE = "node";

	public static final String EDGE = "edge";

	public static final String CLUSTER_PREFIX = "cluster_";

	public static final String DIRECTED_EDGE_CONNECTOR = "->";

	public static final String UNDIRECTED_EDGE_CONNECTOR = "--";

	private DotGraph() {}

}
