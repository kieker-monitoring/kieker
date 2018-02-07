/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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
