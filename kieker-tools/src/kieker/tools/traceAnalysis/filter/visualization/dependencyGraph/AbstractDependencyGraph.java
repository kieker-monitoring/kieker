/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.filter.visualization.dependencyGraph;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraph;
import kieker.tools.traceAnalysis.filter.visualization.graph.NoOriginRetentionPolicy;
import kieker.tools.traceAnalysis.systemModel.ISystemModelElement;
import kieker.tools.traceAnalysis.systemModel.TraceInformation;

/**
 * Abstract superclass for dependency graphs.
 * 
 * @param <T>
 *            The type of the nodes' payload
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver
 * 
 * @since 1.6
 */
public abstract class AbstractDependencyGraph<T extends ISystemModelElement> extends
		AbstractGraph<DependencyGraphNode<T>, WeightedBidirectionalDependencyGraphEdge<T>, TraceInformation> {

	private final Map<Integer, DependencyGraphNode<T>> nodes = new ConcurrentHashMap<Integer, DependencyGraphNode<T>>(); // NOPMD (UseConcurrentHashMap)
	private final DependencyGraphNode<T> rootNode;

	/**
	 * Creates a new dependency graph with the given root entity.
	 * 
	 * @param rootEntity
	 *            The entity from which the root node originates
	 */
	public AbstractDependencyGraph(final T rootEntity) {
		this.rootNode = new DependencyGraphNode<T>(DependencyGraphNode.ROOT_NODE_ID, rootEntity, null, NoOriginRetentionPolicy.createInstance());
		this.nodes.put(DependencyGraphNode.ROOT_NODE_ID, this.rootNode);
	}

	/**
	 * Delivers the node with the given key.
	 * 
	 * @param i
	 *            The key to search for.
	 * 
	 * @return The corresponding node to the given key if it exists, null otherwise.
	 */
	protected final DependencyGraphNode<T> getNode(final int i) {
		return this.nodes.get(i);
	}

	/**
	 * Adds a node to this graph.
	 * 
	 * @param i
	 *            The key of the node.
	 * @param node
	 *            The node itself.
	 */
	protected final void addNode(final int i, final DependencyGraphNode<T> node) {
		this.nodes.put(i, node);
	}

	/**
	 * Returns this graph's root node.
	 * 
	 * @return See above
	 */
	public final DependencyGraphNode<T> getRootNode() {
		return this.rootNode;
	}

	/**
	 * Returns all nodes contained in this graph.
	 * 
	 * @return See above
	 */
	public Collection<DependencyGraphNode<T>> getNodes() {
		return this.nodes.values();
	}

	/**
	 * Returns the number of nodes contained in this graph.
	 * 
	 * @return See above
	 */
	public int size() {
		return this.nodes.size();
	}

	@Override
	public Collection<DependencyGraphNode<T>> getVertices() {
		return this.nodes.values();
	}
}
