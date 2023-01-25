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
package kieker.analysis.generic.graph.mtree.nodes;

import kieker.analysis.generic.graph.mtree.MTree;

/**
 *
 * @author Reiner Jung
 * @since 2.0.0
 *
 */
public final class NodeFactory {

	private NodeFactory() {
		// factory class
	}

	public static <R> RootNode<R> createRootNode(final MTree<R> mtree, final R data) {
		final RootNode<R> node = new RootNode<R>(mtree, data);
		node.rootness = new RootNodeTrait<R>(node);
		node.leafness = new NonLeafNodeTrait<R>(node);
		return node;
	}

	public static <R> RootLeafNode<R> createRootLeafNode(final MTree<R> mtree, final R data) {
		final RootLeafNode<R> node = new RootLeafNode<R>(mtree, data);
		node.rootness = new RootNodeTrait<R>(node);
		node.leafness = new LeafNodeTrait<R>(node);
		return node;
	}

	public static <R> LeafNode<R> createLeafNode(final MTree<R> mtree, final R data) {
		final LeafNode<R> node = new LeafNode<R>(mtree, data);
		node.rootness = new NonRootNodeTrait<R>(node);
		node.leafness = new LeafNodeTrait<R>(node);
		return node;
	}

	public static <R> InternalNode<R> createInternalNode(final MTree<R> mtree, final R data) {
		final InternalNode<R> node = new InternalNode<R>(mtree, data);
		node.rootness = new NonRootNodeTrait<R>(node);
		node.leafness = new NonLeafNodeTrait<R>(node);
		return node;
	}
}
