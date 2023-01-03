/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.generic.sink.graph.dot;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.google.common.graph.MutableNetwork;

import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.generic.sink.graph.dot.attributes.DotClusterAttribute;
import kieker.analysis.generic.sink.graph.dot.attributes.DotEdgeAttribute;
import kieker.analysis.generic.sink.graph.dot.attributes.DotGraphAttribute;
import kieker.analysis.generic.sink.graph.dot.attributes.DotNodeAttribute;

/**
 * This class specifies how attributes (for graphs, vertices and edges) are mapped
 * to a dot graph.
 *
 * To create a {@link DotExportMapper} use the {@link DotExportMapper.Builder}.
 *
 * @param <N>
 *            node type
 * @param <E>
 *            edge type
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class DotExportMapper<N extends INode, E extends IEdge> {

	//
	// BETTER Even if EnumMaps are very efficient in terms of lookup, etc. they require
	// a constant memory space, which is the size of the enums list. Moreover, we only
	// want to iterate over the map and do not perform any lookups etc. A more efficient
	// way would be to use a List of Pairs of constant size.
	//
	// PMD: No concurrent access intended for the following attributes
	protected final Map<DotGraphAttribute, Function<MutableNetwork<N, E>, String>> graphAttributes = new EnumMap<>(
			DotGraphAttribute.class); // NOPMD (see above)
	protected final Map<DotNodeAttribute, Function<MutableNetwork<N, E>, String>> defaultNodeAttributes = new EnumMap<>(
			DotNodeAttribute.class); // NOPMD (see above)
	protected final Map<DotEdgeAttribute, Function<MutableNetwork<N, E>, String>> defaultEdgeAttributes = new EnumMap<>(
			DotEdgeAttribute.class); // NOPMD (see above)
	protected final Map<DotNodeAttribute, Function<N, String>> nodeAttributes = new EnumMap<>(DotNodeAttribute.class); // NOPMD (see above)
	protected final Map<DotEdgeAttribute, Function<E, String>> edgeAttributes = new EnumMap<>(DotEdgeAttribute.class); // NOPMD (see above)
	protected final Map<DotClusterAttribute, Function<N, String>> clusterAttributes = new EnumMap<>(DotClusterAttribute.class); // NOPMD (see above)

	protected DotExportMapper() {
		// Create an empty export configuration
	}

	public Set<Map.Entry<DotGraphAttribute, Function<MutableNetwork<N, E>, String>>> getGraphAttributes() {
		return Collections.unmodifiableSet(this.graphAttributes.entrySet());
	}

	public Set<Map.Entry<DotNodeAttribute, Function<MutableNetwork<N, E>, String>>> getDefaultNodeAttributes() {
		return Collections.unmodifiableSet(this.defaultNodeAttributes.entrySet());
	}

	public Set<Map.Entry<DotEdgeAttribute, Function<MutableNetwork<N, E>, String>>> getDefaultEdgeAttributes() {
		return Collections.unmodifiableSet(this.defaultEdgeAttributes.entrySet());
	}

	public Set<Map.Entry<DotNodeAttribute, Function<N, String>>> getNodeAttributes() {
		return Collections.unmodifiableSet(this.nodeAttributes.entrySet());
	}

	public Set<Map.Entry<DotEdgeAttribute, Function<E, String>>> getEdgeAttributes() {
		return Collections.unmodifiableSet(this.edgeAttributes.entrySet());
	}

	public Set<Map.Entry<DotClusterAttribute, Function<N, String>>> getClusterAttributes() {
		return Collections.unmodifiableSet(this.clusterAttributes.entrySet());
	}

}
