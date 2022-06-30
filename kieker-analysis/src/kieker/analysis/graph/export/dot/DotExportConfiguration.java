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

package kieker.analysis.graph.export.dot;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.google.common.graph.MutableNetwork;

import kieker.analysis.graph.IEdge;
import kieker.analysis.graph.INode;
import kieker.analysis.graph.util.dot.attributes.DotClusterAttribute;
import kieker.analysis.graph.util.dot.attributes.DotEdgeAttribute;
import kieker.analysis.graph.util.dot.attributes.DotGraphAttribute;
import kieker.analysis.graph.util.dot.attributes.DotNodeAttribute;

/**
 * This class specifies how attributes (for graphs, vertices and edges) are mapped
 * to a dot graph.
 *
 * To create a {@link DotExportConfiguration} use the {@link DotExportConfiguration.Builder}.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class DotExportConfiguration {

	//
	// BETTER Even if EnumMaps are very efficient in terms of lookup, etc. they require
	// a constant memory space, which is the size of the enums list. Moreover, we only
	// want to iterate over the map and do not perform any lookups etc. A more efficient
	// way would be to use a List of Pairs of constant size.
	//
	// PMD: No concurrent access intended for the following attributes
	protected final Map<DotGraphAttribute, Function<MutableNetwork<INode, IEdge>, String>> graphAttributes = new EnumMap<>(
			DotGraphAttribute.class); // NOPMD (see above)
	protected final Map<DotNodeAttribute, Function<MutableNetwork<INode, IEdge>, String>> defaultNodeAttributes = new EnumMap<>(
			DotNodeAttribute.class); // NOPMD (see above)
	protected final Map<DotEdgeAttribute, Function<MutableNetwork<INode, IEdge>, String>> defaultEdgeAttributes = new EnumMap<>(
			DotEdgeAttribute.class); // NOPMD (see above)
	protected final Map<DotNodeAttribute, Function<INode, String>> nodeAttributes = new EnumMap<>(DotNodeAttribute.class); // NOPMD (see above)
	protected final Map<DotEdgeAttribute, Function<IEdge, String>> edgeAttributes = new EnumMap<>(DotEdgeAttribute.class); // NOPMD (see above)
	protected final Map<DotClusterAttribute, Function<INode, String>> clusterAttributes = new EnumMap<>(DotClusterAttribute.class); // NOPMD (see above)

	protected DotExportConfiguration() {
		// Create an empty export configuration
	}

	public Set<Map.Entry<DotGraphAttribute, Function<MutableNetwork<INode, IEdge>, String>>> getGraphAttributes() {
		return Collections.unmodifiableSet(this.graphAttributes.entrySet());
	}

	public Set<Map.Entry<DotNodeAttribute, Function<MutableNetwork<INode, IEdge>, String>>> getDefaultNodeAttributes() {
		return Collections.unmodifiableSet(this.defaultNodeAttributes.entrySet());
	}

	public Set<Map.Entry<DotEdgeAttribute, Function<MutableNetwork<INode, IEdge>, String>>> getDefaultEdgeAttributes() {
		return Collections.unmodifiableSet(this.defaultEdgeAttributes.entrySet());
	}

	public Set<Map.Entry<DotNodeAttribute, Function<INode, String>>> getNodeAttributes() {
		return Collections.unmodifiableSet(this.nodeAttributes.entrySet());
	}

	public Set<Map.Entry<DotEdgeAttribute, Function<IEdge, String>>> getEdgeAttributes() {
		return Collections.unmodifiableSet(this.edgeAttributes.entrySet());
	}

	public Set<Map.Entry<DotClusterAttribute, Function<INode, String>>> getClusterAttributes() {
		return Collections.unmodifiableSet(this.clusterAttributes.entrySet());
	}

	/**
	 * @since 1.14
	 */
	public static class Builder {

		private final DotExportConfiguration configuration = new DotExportConfiguration();

		public Builder() {
			// create a new builder
		}

		public void addGraphAttribute(final DotGraphAttribute attribute, final Function<MutableNetwork<INode, IEdge>, String> function) {
			this.configuration.graphAttributes.put(attribute, function);
		}

		public void addDefaultNodeAttribute(final DotNodeAttribute attribute, final Function<MutableNetwork<INode, IEdge>, String> function) {
			this.configuration.defaultNodeAttributes.put(attribute, function);
		}

		public void addDefaultEdgeAttribute(final DotEdgeAttribute attribute, final Function<MutableNetwork<INode, IEdge>, String> function) {
			this.configuration.defaultEdgeAttributes.put(attribute, function);
		}

		public void addNodeAttribute(final DotNodeAttribute attribute, final Function<INode, String> function) {
			this.configuration.nodeAttributes.put(attribute, function);
		}

		public void addEdgeAttribute(final DotEdgeAttribute attribute, final Function<IEdge, String> function) {
			this.configuration.edgeAttributes.put(attribute, function);
		}

		public void addClusterAttribute(final DotClusterAttribute attribute, final Function<INode, String> function) {
			this.configuration.clusterAttributes.put(attribute, function);
		}

		public DotExportConfiguration build() {
			return this.configuration;
		}

	}
}
