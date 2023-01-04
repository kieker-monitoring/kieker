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
package kieker.analysis.generic.graph.traversal;

import java.util.ArrayList;
import java.util.List;

import com.google.common.graph.MutableNetwork;

import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.INode;

/**
 * @author Sören Henning
 *
 * @param <N>
 *            node type
 * @param <E>
 *            edge type
 *
 * @since 1.14
 */
public abstract class AbstractGraphTraverser<N extends INode, E extends IEdge> {

	protected List<INodeVisitor<N>> nodeVisitors;
	protected List<IEdgeVisitor<E>> edgeVisitors;

	public AbstractGraphTraverser() {
		this.nodeVisitors = new ArrayList<>();
		this.edgeVisitors = new ArrayList<>();
	}

	public AbstractGraphTraverser(final INodeVisitor<N> nodeVisitor, final IEdgeVisitor<E> edgeVisitor) {
		this();
		this.nodeVisitors.add(nodeVisitor);
		this.edgeVisitors.add(edgeVisitor);
	}

	public AbstractGraphTraverser(final List<INodeVisitor<N>> nodeVisitors, final List<IEdgeVisitor<E>> edgeVisitors) {
		this.nodeVisitors = nodeVisitors;
		this.edgeVisitors = edgeVisitors;
	}

	public List<INodeVisitor<N>> getNodeVisitors() {
		return this.nodeVisitors;
	}

	public void setNodeVisitors(final List<INodeVisitor<N>> nodeVisitors) {
		this.nodeVisitors = nodeVisitors;
	}

	public void addNodeVisitor(final INodeVisitor<N> nodeVisitor) {
		this.nodeVisitors.add(nodeVisitor);
	}

	public List<IEdgeVisitor<E>> getEdgeVisitors() {
		return this.edgeVisitors;
	}

	public void setEdgeVisitors(final List<IEdgeVisitor<E>> edgeVisitors) {
		this.edgeVisitors = edgeVisitors;
	}

	public void addEdgeVisitor(final IEdgeVisitor<E> edgeVisitor) {
		this.edgeVisitors.add(edgeVisitor);
	}

	public abstract void traverse(final MutableNetwork<N, E> graph);

}
