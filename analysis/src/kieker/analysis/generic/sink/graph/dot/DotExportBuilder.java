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

import java.util.function.Function;

import com.google.common.graph.MutableNetwork;

import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.generic.sink.graph.dot.attributes.DotClusterAttribute;
import kieker.analysis.generic.sink.graph.dot.attributes.DotEdgeAttribute;
import kieker.analysis.generic.sink.graph.dot.attributes.DotGraphAttribute;
import kieker.analysis.generic.sink.graph.dot.attributes.DotNodeAttribute;

/**
 * @param <N>
 *            node type
 * @param <E>
 *            edge type
 * @author Sören
 * @since 2.0.0
 *
 */
public class DotExportBuilder<N extends INode, E extends IEdge> {

	private final DotExportMapper<N, E> configuration = new DotExportMapper<>();

	public DotExportBuilder() {
		// create a new builder
	}

	public void addGraphAttribute(final DotGraphAttribute attribute, final Function<MutableNetwork<N, E>, String> function) {
		this.configuration.graphAttributes.put(attribute, function);
	}

	public void addDefaultNodeAttribute(final DotNodeAttribute attribute, final Function<MutableNetwork<N, E>, String> function) {
		this.configuration.defaultNodeAttributes.put(attribute, function);
	}

	public void addDefaultEdgeAttribute(final DotEdgeAttribute attribute, final Function<MutableNetwork<N, E>, String> function) {
		this.configuration.defaultEdgeAttributes.put(attribute, function);
	}

	public void addNodeAttribute(final DotNodeAttribute attribute, final Function<N, String> function) {
		this.configuration.nodeAttributes.put(attribute, function);
	}

	public void addEdgeAttribute(final DotEdgeAttribute attribute, final Function<E, String> function) {
		this.configuration.edgeAttributes.put(attribute, function);
	}

	public void addClusterAttribute(final DotClusterAttribute attribute, final Function<N, String> function) {
		this.configuration.clusterAttributes.put(attribute, function);
	}

	public DotExportMapper<N, E> build() {
		return this.configuration;
	}

}
