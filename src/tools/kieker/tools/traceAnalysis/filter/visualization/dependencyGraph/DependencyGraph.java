/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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
import java.util.TreeMap;

/**
 * 
 * @param <T>
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver
 */
public class DependencyGraph<T> {

	// private static final Log LOG = LogFactory.getLog(DependencyGraph.class);

	private final Map<Integer, DependencyGraphNode<T>> nodes = new TreeMap<Integer, DependencyGraphNode<T>>(); // NOPMD (UseConcurrentHashMap)
	private final DependencyGraphNode<T> rootNode;

	public DependencyGraph(final int rootNodeId, final T rootEntity) {
		this.rootNode = new DependencyGraphNode<T>(rootNodeId, rootEntity);
		this.nodes.put(rootNodeId, this.rootNode);
	}

	protected final DependencyGraphNode<T> getNode(final int i) {
		return this.nodes.get(i);
	}

	protected final void addNode(final int i, final DependencyGraphNode<T> node) {
		this.nodes.put(i, node);
	}

	public final DependencyGraphNode<T> getRootNode() {
		return this.rootNode;
	}

	public Collection<DependencyGraphNode<T>> getNodes() {
		return this.nodes.values();
	}

	/** Return number of nodes */
	public int size() {
		return this.nodes.size();
	}
}
