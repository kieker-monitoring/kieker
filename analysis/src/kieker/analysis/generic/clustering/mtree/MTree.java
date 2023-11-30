/***************************************************************************
 * Copyright (c) 2012-2013 Eduardo R. D'Avila (https://github.com/erdavila)
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
package kieker.analysis.generic.clustering.mtree;

import kieker.analysis.exception.InternalErrorException;
import kieker.analysis.generic.clustering.mtree.nodes.AbstractNode;
import kieker.analysis.generic.clustering.mtree.nodes.IndexItem;
import kieker.analysis.generic.clustering.mtree.nodes.InternalNode;
import kieker.analysis.generic.clustering.mtree.nodes.LeafNode;
import kieker.analysis.generic.clustering.mtree.nodes.NodeFactory;
import kieker.analysis.generic.clustering.mtree.query.Query;
import kieker.analysis.generic.clustering.mtree.utils.Pair;

/**
 * The main class that implements the M-Tree.
 *
 * @param <T>
 *            The type of data that will be indexed by the M-Tree. Objects of
 *            this type are stored in HashMaps and HashSets, so their
 *            {@code hashCode()} and {@code equals()} methods must be consistent.
 *
 * @author Eduardo R. D'Avila -- initial contribution
 * @author Reiner Jung
 * @since 2.0.0
 */
public class MTree<T> {

	/**
	 * The default minimum capacity of nodes in an M-Tree, when not specified in
	 * the constructor call.
	 */
	public static final int DEFAULT_MIN_NODE_CAPACITY = 50;

	private int minNodeCapacity;
	private int maxNodeCapacity;
	private IDistanceFunction<? super T> distanceFunction;
	private ISplitFunction<T> splitFunction;
	private AbstractNode<T> root;

	/**
	 * Constructs an M-Tree with the specified distance function.
	 *
	 * @param distanceFunction
	 *            The object used to calculate the distance between
	 *            two data objects.
	 * @param splitFunction
	 *            split function
	 */
	public MTree(final IDistanceFunction<? super T> distanceFunction,
			final ISplitFunction<T> splitFunction) {
		this(MTree.DEFAULT_MIN_NODE_CAPACITY, distanceFunction, splitFunction);
	}

	/**
	 * Constructs an M-Tree with the specified minimum node capacity and
	 * distance function.
	 *
	 * @param minNodeCapacity
	 *            The minimum capacity for the nodes of the tree.
	 * @param distanceFunction
	 *            The object used to calculate the distance between
	 *            two data objects.
	 * @param splitFunction
	 *            The object used to process the split of nodes if
	 *            they are full when a new child must be added.
	 */
	public MTree(final int minNodeCapacity,
			final IDistanceFunction<? super T> distanceFunction,
			final ISplitFunction<T> splitFunction) {
		this(minNodeCapacity, (2 * minNodeCapacity) - 1, distanceFunction, splitFunction);
	}

	/**
	 * Constructs an M-Tree with the specified minimum and maximum node
	 * capacities and distance function.
	 *
	 * @param minNodeCapacity
	 *            The minimum capacity for the nodes of the tree.
	 * @param maxNodeCapacity
	 *            The maximum capacity for the nodes of the tree.
	 * @param distanceFunction
	 *            The object used to calculate the distance between
	 *            two data objects.
	 * @param existingSplitFunction
	 *            The object used to process the split of nodes if
	 *            they are full when a new child must be added.
	 */
	public MTree(final int minNodeCapacity, final int maxNodeCapacity,
			final IDistanceFunction<? super T> distanceFunction,
			final ISplitFunction<T> existingSplitFunction) {
		if ((minNodeCapacity < 2)
				|| (maxNodeCapacity <= minNodeCapacity)
				|| (distanceFunction == null)) {
			throw new IllegalArgumentException();
		}

		if (existingSplitFunction == null) {
			this.splitFunction = new ComposedSplitFunction<>(
					new RandomPromotionFunction<>(),
					new BalancedPartitionFunction<>());
		} else {
			this.splitFunction = existingSplitFunction;
		}

		this.minNodeCapacity = minNodeCapacity;
		this.maxNodeCapacity = maxNodeCapacity;
		this.distanceFunction = distanceFunction;

		this.root = null;
	}

	/**
	 * Adds and indexes a data object.
	 *
	 * <p>
	 * An object that is already indexed should not be added. There is no
	 * validation regarding this, and the behavior is undefined if done.
	 *
	 * @param data
	 *            The data object to index.
	 * @throws InternalErrorException
	 *             on internal error
	 */
	public void add(final T data) throws InternalErrorException {
		if (this.root == null) {
			this.root = NodeFactory.createRootLeafNode(this, data);
			this.root.addData(data, 0);
			if (this.root.isMaxCapacityExceeded()) {
				throw new InternalErrorException("Node capacity exceeded when adding initial root node.");
			}
		} else {
			final double distance = this.distanceFunction.calculate(data, this.root.getData());
			this.root.addData(data, distance);
			if (this.root.isMaxCapacityExceeded()) {
				final Pair<AbstractNode<T>> newNodes = this.root.splitNodes();
				this.createNewRootAfterSplit(newNodes, data);
			}
		}
	}

	/**
	 * Removes a data object from the M-Tree.
	 *
	 * @param data
	 *            The data object to be removed.
	 * @return {@code true} if and only if the object was found.
	 * @throws InternalErrorException
	 *             on internal error
	 */
	public boolean remove(final T data) throws InternalErrorException {
		if (this.root == null) {
			return false;
		}

		final double distanceToRoot = this.distanceFunction.calculate(data, this.root.getData());

		if (this.root.removeData(data, distanceToRoot)) {
			if (this.root.isNodeUnderCapacity()) {
				if (this.root.getChildren().values().size() > 0) {
					this.root = this.createNewRootAfterRemove(this.root);
				} else {
					this.root = null;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Performs a nearest-neighbors query on the M-Tree, constrained by distance.
	 *
	 * @param queryData
	 *            The query data object.
	 * @param range
	 *            The maximum distance from {@code queryData} to fetched
	 *            neighbors.
	 * @return A {@link Query} object used to iterate on the results.
	 */
	public Query<T> getNearestByRange(final T queryData, final double range) {
		return this.getNearest(queryData, range, Integer.MAX_VALUE);
	}

	/**
	 * Performs a nearest-neighbors query on the M-Tree, constrained by the
	 * number of neighbors.
	 *
	 * @param queryData
	 *            The query data object.
	 * @param limit
	 *            The maximum number of neighbors to fetch.
	 * @return A {@link Query} object used to iterate on the results.
	 */
	public Query<T> getNearestByLimit(final T queryData, final int limit) {
		return this.getNearest(queryData, Double.POSITIVE_INFINITY, limit);
	}

	/**
	 * Performs a nearest-neighbor query on the M-Tree, constrained by distance
	 * and/or the number of neighbors.
	 *
	 * @param queryData
	 *            The query data object.
	 * @param range
	 *            The maximum distance from {@code queryData} to fetched
	 *            neighbors.
	 * @param limit
	 *            The maximum number of neighbors to fetch.
	 * @return A {@link Query} object used to iterate on the results.
	 */
	public Query<T> getNearest(final T queryData, final double range, final int limit) {
		return new Query<>(this, queryData, range, limit);
	}

	/**
	 * Performs a nearest-neighbor query on the M-Tree, without constraints.
	 *
	 * @param queryData
	 *            The query data object.
	 * @return A {@link Query} object used to iterate on the results.
	 */
	public Query<T> getNearest(final T queryData) {
		return new Query<>(this, queryData, Double.POSITIVE_INFINITY, Integer.MAX_VALUE);
	}

	public int getMaxNodeCapacity() {
		return this.maxNodeCapacity;
	}

	public int getMinNodeCapacity() {
		return this.minNodeCapacity;
	}

	public ISplitFunction<T> getSplitFunction() {
		return this.splitFunction;
	}

	public IDistanceFunction<? super T> getDistanceFunction() {
		return this.distanceFunction;
	}

	public AbstractNode<T> getRoot() {
		return this.root;
	}

	protected void check() {
		if (this.root != null) {
			this.root.check();
		}
	}

	private AbstractNode<T> createNewRootAfterRemove(final AbstractNode<T> oldRoot) throws InternalErrorException {
		// Promote the only child to root
		final AbstractNode<T> theChild = (AbstractNode<T>) oldRoot.getChildren().values().iterator().next();
		final AbstractNode<T> newRoot;
		if (theChild instanceof InternalNode<?>) {
			newRoot = NodeFactory.createRootNode(this, theChild.getData());
		} else {
			assert theChild instanceof LeafNode<?>;
			newRoot = NodeFactory.createRootLeafNode(this, theChild.getData());
		}

		for (final IndexItem<T> grandchild : theChild.getChildren().values()) {
			final double newDistance = this.distanceFunction.calculate(newRoot.getData(), grandchild.getData());
			newRoot.addChild(grandchild, newDistance);
		}
		theChild.getChildren().clear();

		return newRoot;
	}

	private void createNewRootAfterSplit(final Pair<AbstractNode<T>> nodes, final T data) throws InternalErrorException {
		this.root = NodeFactory.createRootNode(this, data);
		this.computeDistances(nodes.getFirst());
		this.computeDistances(nodes.getSecond());
	}

	private void computeDistances(final AbstractNode<T> node) throws InternalErrorException {
		final double distance = this.distanceFunction.calculate(this.root.getData(), node.getData());
		this.root.addChild(node, distance);
	}

}
