/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.generic.graph;

import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;

import kieker.analysis.generic.graph.impl.EdgeImpl;
import kieker.analysis.generic.graph.impl.GraphImpl;
import kieker.analysis.generic.graph.impl.NodeImpl;

/**
 * Factory for Kieker graph elements.
 *
 * @author Reiner Jung
 * @since 2.0.0
 *
 */
public final class GraphFactory {

	private GraphFactory() {
		// do not instantiate, is a factory
	}

	public static <N extends INode, E extends IEdge> IGraph<N, E> createGraph(final String id, final MutableNetwork<N, E> graph) {
		return new GraphImpl<>(id, graph);
	}

	public static <N extends INode, E extends IEdge> IGraph<N, E> createGraph(final String name) {
		final MutableNetwork<N, E> graph = NetworkBuilder.directed().allowsParallelEdges(true).allowsSelfLoops(true).build();
		return GraphFactory.createGraph(name, graph);
	}

	public static INode createNode(final String id) {
		return new NodeImpl(id);
	}

	public static IEdge createEdge(final String id) {
		return new EdgeImpl(id);
	}

}
