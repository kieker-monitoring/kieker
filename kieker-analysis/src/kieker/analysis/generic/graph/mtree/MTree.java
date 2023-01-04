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
package kieker.analysis.generic.graph.mtree;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Set;

import kieker.analysis.generic.graph.mtree.ISplitFunction.SplitResult;

/**
 * The main class that implements the M-Tree.
 *
 * @param <T>
 *            The type of data that will be indexed by the M-Tree. Objects of
 *            this type are stored in HashMaps and HashSets, so their
 *            {@code hashCode()} and {@code equals()} methods must be consistent.
 *
 * @author Eduardo R. D'Avila
 * @since 2.0.0
 */
public class MTree<T> {

	/**
	 * The default minimum capacity of nodes in an M-Tree, when not specified in
	 * the constructor call.
	 */
	public static final int DEFAULT_MIN_NODE_CAPACITY = 50;

	protected int minNodeCapacity;
	protected int maxNodeCapacity;
	protected IDistanceFunction<? super T> distanceFunction;
	protected ISplitFunction<T> splitFunction;
	protected AbstractNode root;

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
		this(DEFAULT_MIN_NODE_CAPACITY, distanceFunction, splitFunction);
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
		this(minNodeCapacity, 2 * minNodeCapacity - 1, distanceFunction, splitFunction);
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
		if (minNodeCapacity < 2
				|| maxNodeCapacity <= minNodeCapacity
				|| distanceFunction == null) {
			throw new IllegalArgumentException();
		}

		final ISplitFunction<T> localSplitFunction;
		if (existingSplitFunction == null) {
			localSplitFunction = new ComposedSplitFunction<>(
					new PromotionFunctions.RandomPromotion<T>(),
					new PartitionFunctions.BalancedPartition<T>());
		} else {
			localSplitFunction = existingSplitFunction;
		}

		this.minNodeCapacity = minNodeCapacity;
		this.maxNodeCapacity = maxNodeCapacity;
		this.distanceFunction = distanceFunction;
		this.splitFunction = localSplitFunction;
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
	 */
	public void add(final T data) {
		if (this.root == null) {
			this.root = new RootLeafNode(data);
			try {
				this.root.addData(data, 0);
			} catch (final SplitNodeReplacement e) {
				throw new RuntimeException("Should never happen!");
			}
		} else {
			double distance = this.distanceFunction.calculate(data, this.root.getData());
			try {
				this.root.addData(data, distance);
			} catch (final SplitNodeReplacement e) {
				final AbstractNode newRoot = new RootNode(data);
				this.root = newRoot;
				for (final Object newNode2 : e.newNodes) {
					@SuppressWarnings("unchecked")
					final AbstractNode newNode = (AbstractNode) newNode2;
					distance = this.distanceFunction.calculate(this.root.getData(), newNode.getData());
					this.root.addChild(newNode, distance);
				}
			}
		}
	}

	/**
	 * Removes a data object from the M-Tree.
	 *
	 * @param data
	 *            The data object to be removed.
	 * @return {@code true} if and only if the object was found.
	 */
	public boolean remove(final T data) {
		if (this.root == null) {
			return false;
		}

		final double distanceToRoot = this.distanceFunction.calculate(data, this.root.getData());
		try {
			this.root.removeData(data, distanceToRoot);
		} catch (final RootNodeReplacement e) {
			@SuppressWarnings("unchecked")
			final AbstractNode newRoot = (AbstractNode) e.newRoot;
			this.root = newRoot;
		} catch (final DataNotFound e) {
			return false;
		} catch (final NodeUnderCapacity e) {
			throw new RuntimeException("Should have never happened", e);
		}
		return true;
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
	public Query getNearestByRange(final T queryData, final double range) {
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
	public Query getNearestByLimit(final T queryData, final int limit) {
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
	public Query getNearest(final T queryData, final double range, final int limit) {
		return new Query(queryData, range, limit);
	}

	/**
	 * Performs a nearest-neighbor query on the M-Tree, without constraints.
	 *
	 * @param queryData
	 *            The query data object.
	 * @return A {@link Query} object used to iterate on the results.
	 */
	public Query getNearest(final T queryData) {
		return new Query(queryData, Double.POSITIVE_INFINITY, Integer.MAX_VALUE);
	}

	protected void check() {
		if (this.root != null) {
			this.root.check();
		}
	}

	private class IndexItem { // NOCS cannot be declared final
		protected double radius;
		private final T data;
		private double distanceToParent;

		private IndexItem(final T data) {
			this.data = data;
			this.radius = 0;
			this.distanceToParent = -1;
		}

		public T getData() {
			return this.data;
		}

		public double getDistanceToParent() {
			return this.distanceToParent;
		}

		public void setDistanceToParent(final double distance) {
			this.distanceToParent = distance;
		}

		int check() {
			this.checkRadius();
			this.checkDistanceToParent();
			return 1;
		}

		private void checkRadius() {
			assert this.radius >= 0;
		}

		protected void checkDistanceToParent() {
			assert !(this instanceof MTree.RootLeafNode);
			assert !(this instanceof MTree.RootNode);
			assert this.distanceToParent >= 0;
		}

	}

	private abstract class AbstractNode extends IndexItem {

		protected Map<T, IndexItem> children = new HashMap<>();
		protected IRootness rootness;
		protected ILeafness<T> leafness;

		private <R extends AbstractNodeTrait & IRootness, L extends AbstractNodeTrait & ILeafness<T>> AbstractNode(final T data,
				final R rootness, final L leafness) {
			super(data);

			rootness.thisNode = this;
			this.rootness = rootness;

			leafness.thisNode = this;
			this.leafness = leafness;
		}

		private final void addData(final T data, final double distance) throws SplitNodeReplacement {
			this.doAddData(data, distance);
			this.checkMaxCapacity();
		}

		@Override
		int check() {
			super.check();
			this.checkMinCapacity();
			this.checkMaxCapacity2();

			int childHeight = -1;
			for (final Map.Entry<T, IndexItem> e : this.children.entrySet()) {
				final T data = e.getKey();
				final IndexItem child = e.getValue();
				assert child.data.equals(data);

				this.checkChildClass(child);
				this.checkChildMetrics(child);

				final int height = child.check();
				if (childHeight < 0) {
					childHeight = height;
				} else {
					assert childHeight == height;
				}
			}

			return childHeight + 1;
		}

		protected void doAddData(final T data, final double distance) {
			this.leafness.doAddData(data, distance);
		}

		protected void doRemoveData(final T data, final double distance) throws DataNotFound {
			this.leafness.doRemoveData(data, distance);
		}

		private final void checkMaxCapacity() throws SplitNodeReplacement {
			if (this.children.size() > MTree.this.maxNodeCapacity) {
				final IDistanceFunction<? super T> cachedDistanceFunction = DistanceFunctions.cached(MTree.this.distanceFunction);
				final SplitResult<T> splitResult = MTree.this.splitFunction.process(this.children.keySet(), cachedDistanceFunction);

				AbstractNode newNode0 = null;
				AbstractNode newNode1 = null;
				for (int i = 0; i < 2; ++i) {
					final T promotedData = splitResult.getPromoted().get(i);
					final Set<T> partition = splitResult.getPartitions().get(i);

					final AbstractNode newNode = this.newSplitNodeReplacement(promotedData);
					for (final T data : partition) {
						final IndexItem child = this.children.get(data);
						this.children.remove(data);
						final double distance = cachedDistanceFunction.calculate(promotedData, data);
						newNode.addChild(child, distance);
					}

					if (i == 0) {
						newNode0 = newNode;
					} else {
						newNode1 = newNode;
					}
				}
				assert this.children.isEmpty();

				throw new SplitNodeReplacement(newNode0, newNode1);
			}

		}

		protected AbstractNode newSplitNodeReplacement(final T data) {
			return this.leafness.newSplitNodeReplacement(data);
		}

		protected void addChild(final IndexItem child, final double distance) {
			this.leafness.addChild(child, distance);
		}

		void removeData(final T data, final double distance) throws RootNodeReplacement, NodeUnderCapacity, DataNotFound {
			this.doRemoveData(data, distance);
			if (this.children.size() < this.getMinCapacity()) {
				throw new NodeUnderCapacity();
			}
		}

		protected int getMinCapacity() {
			return this.rootness.getMinCapacity();
		}

		private void updateMetrics(final IndexItem child, final double distance) {
			child.setDistanceToParent(distance);
			this.updateRadius(child);
		}

		private void updateRadius(final IndexItem child) {
			this.radius = Math.max(this.radius, child.distanceToParent + child.radius);
		}

		void checkMinCapacity() {
			this.rootness.checkMinCapacity();
		}

		private void checkMaxCapacity2() {
			assert this.children.size() <= MTree.this.maxNodeCapacity;
		}

		private void checkChildClass(final IndexItem child) {
			this.leafness.checkChildClass(child);
		}

		private void checkChildMetrics(final IndexItem child) {
			final double dist = MTree.this.distanceFunction.calculate(child.data, this.getData());
			assert child.distanceToParent == dist;

			final double sum = child.distanceToParent + child.radius;
			assert sum <= this.radius;
		}

		@Override
		protected void checkDistanceToParent() {
			this.rootness.checkDistanceToParent();
		}

		private MTree<T> mtree() {
			return MTree.this;
		}
	}

	private abstract class AbstractNodeTrait {
		protected AbstractNode thisNode;
	}

	private interface ILeafness<DATA> {
		void doAddData(DATA data, double distance);

		void addChild(MTree<DATA>.IndexItem child, double distance);

		void doRemoveData(DATA data, double distance) throws DataNotFound;

		MTree<DATA>.AbstractNode newSplitNodeReplacement(DATA data);

		void checkChildClass(MTree<DATA>.IndexItem child);
	}

	private interface IRootness {
		int getMinCapacity();

		void checkDistanceToParent();

		void checkMinCapacity();
	}

	private class RootNodeTrait extends AbstractNodeTrait implements IRootness {

		public RootNodeTrait() {
			// default constructor
		}

		@Override
		public int getMinCapacity() {
			throw new RuntimeException("Should not be called!");
		}

		@Override
		public void checkDistanceToParent() {
			assert this.thisNode.getDistanceToParent() == -1;
		}

		@Override
		public void checkMinCapacity() {
			this.thisNode.checkMinCapacity();
		}

	};

	private class NonRootNodeTrait extends AbstractNodeTrait implements IRootness {

		public NonRootNodeTrait() {
			// default constructor
		}

		@Override
		public int getMinCapacity() {
			return MTree.this.minNodeCapacity;
		}

		@Override
		public void checkMinCapacity() {
			assert this.thisNode.children.size() >= this.thisNode.mtree().minNodeCapacity;
		}

		@Override
		public void checkDistanceToParent() {
			assert this.thisNode.getDistanceToParent() >= 0;
		}
	};

	private class LeafNodeTrait extends AbstractNodeTrait implements ILeafness<T> {

		public LeafNodeTrait() {
			// default constructor
		}

		@Override
		public void doAddData(final T data, final double distance) {
			final Entry entry = this.thisNode.mtree().new Entry(data);
			assert !this.thisNode.children.containsKey(data);
			this.thisNode.children.put(data, entry);
			assert this.thisNode.children.containsKey(data);
			this.thisNode.updateMetrics(entry, distance);
		}

		@Override
		public void addChild(final IndexItem child, final double distance) {
			assert !this.thisNode.children.containsKey(child.data);
			this.thisNode.children.put(child.data, child);
			assert this.thisNode.children.containsKey(child.data);
			this.thisNode.updateMetrics(child, distance);
		}

		@Override
		public AbstractNode newSplitNodeReplacement(final T data) {
			return this.thisNode.mtree().new LeafNode(data);
		}

		@Override
		public void doRemoveData(final T data, final double distance) throws DataNotFound {
			if (this.thisNode.children.remove(data) == null) {
				throw new DataNotFound();
			}
		}

		@Override
		public void checkChildClass(final IndexItem child) {
			assert child instanceof MTree.Entry;
		}
	}

	class NonLeafNodeTrait extends AbstractNodeTrait implements ILeafness<T> {

		public NonLeafNodeTrait() {
			// nothing to be done
		}

		@Override
		public void doAddData(final T data, final double distance) {
			final class CandidateChild {
				private final AbstractNode node;
				private final double distance;
				private final double metric;

				private CandidateChild(final AbstractNode node, final double distance, final double metric) {
					this.node = node;
					this.distance = distance;
					this.metric = metric;
				}

				public AbstractNode getNode() {
					return this.node;
				}

				public double getDistance() {
					return this.distance;
				}

				public double getMetric() {
					return this.metric;
				}
			}

			CandidateChild minRadiusIncreaseNeeded = new CandidateChild(null, -1.0, Double.POSITIVE_INFINITY);
			CandidateChild nearestDistance = new CandidateChild(null, -1.0, Double.POSITIVE_INFINITY);

			for (final IndexItem item : this.thisNode.children.values()) {
				@SuppressWarnings("unchecked")
				final AbstractNode child = (AbstractNode) item;
				final double childDistance = this.thisNode.mtree().distanceFunction.calculate(child.getData(), data);
				if (childDistance > child.radius) {
					final double radiusIncrease = childDistance - child.radius;
					if (radiusIncrease < minRadiusIncreaseNeeded.metric) {
						minRadiusIncreaseNeeded = new CandidateChild(child, childDistance, radiusIncrease);
					}
				} else {
					if (childDistance < nearestDistance.metric) {
						nearestDistance = new CandidateChild(child, childDistance, childDistance);
					}
				}
			}

			final CandidateChild chosen = nearestDistance.node != null ? nearestDistance : minRadiusIncreaseNeeded; // NOCS

			final AbstractNode child = chosen.node;
			try {
				child.addData(data, chosen.distance);
				this.thisNode.updateRadius(child);
			} catch (final SplitNodeReplacement e) {
				// Replace current child with new nodes
				final IndexItem itemIndex = this.thisNode.children.remove(child.getData());
				assert itemIndex != null;

				for (final Object newNode : e.newNodes) {
					@SuppressWarnings("unchecked")
					final AbstractNode newChild = (AbstractNode) newNode;
					final double newDistance = this.thisNode.mtree().distanceFunction.calculate(this.thisNode.getData(), newChild.getData());
					this.thisNode.addChild(newChild, newDistance);
				}
			}
		}

		@Override
		public void addChild(final IndexItem inputNewChild, final double inputDistance) {
			double distance = inputDistance;
			@SuppressWarnings("unchecked")
			AbstractNode newChild = (AbstractNode) inputNewChild;

			final class ChildWithDistance {
				private final AbstractNode child;
				private final double distance;

				private ChildWithDistance(final AbstractNode child, final double distance) {
					this.child = child;
					this.distance = distance;
				}

				public AbstractNode getChild() {
					return this.child;
				}

				public double getDistance() {
					return this.distance;
				}
			}

			final Deque<ChildWithDistance> newChildren = new ArrayDeque<>();
			newChildren.addFirst(new ChildWithDistance(newChild, distance));

			while (!newChildren.isEmpty()) {
				final ChildWithDistance cwd = newChildren.removeFirst();

				newChild = cwd.child;
				distance = cwd.distance;
				if (this.thisNode.children.containsKey(newChild.getData())) {
					@SuppressWarnings("unchecked")
					final AbstractNode existingChild = (AbstractNode) this.thisNode.children.get(newChild.getData());
					assert existingChild.getData().equals(newChild.getData());

					// Transfer the _children_ of the newChild to the existingChild
					for (final IndexItem grandchild : newChild.children.values()) {
						existingChild.addChild(grandchild, grandchild.distanceToParent);
					}
					newChild.children.clear();

					try {
						existingChild.checkMaxCapacity();
					} catch (final SplitNodeReplacement e) {
						final IndexItem indexItem = this.thisNode.children.remove(existingChild.getData());
						assert indexItem != null;

						for (final Object newNode2 : e.newNodes) {
							@SuppressWarnings("unchecked")
							final AbstractNode newNode = (AbstractNode) newNode2;
							final double newDistance = this.thisNode.mtree().distanceFunction.calculate(this.thisNode.getData(),
									newNode.getData());
							newChildren.addFirst(new ChildWithDistance(newNode, newDistance));
						}
					}
				} else {
					this.thisNode.children.put(newChild.getData(), newChild);
					this.thisNode.updateMetrics(newChild, distance);
				}
			}
		}

		@Override
		public AbstractNode newSplitNodeReplacement(final T data) {
			return new InternalNode(data);
		}

		@Override
		public void doRemoveData(final T data, final double distance) throws DataNotFound {
			for (final IndexItem childItem : this.thisNode.children.values()) {
				@SuppressWarnings("unchecked")
				final AbstractNode child = (AbstractNode) childItem;
				if (Math.abs(distance - child.getDistanceToParent()) <= child.radius) {
					final double distanceToChild = this.thisNode.mtree().distanceFunction.calculate(data, child.getData());
					if (distanceToChild <= child.radius) {
						try {
							child.removeData(data, distanceToChild);
							this.thisNode.updateRadius(child);
							return;
						} catch (final DataNotFound e) {
							// If DataNotFound was thrown, then the data was not found in the child
						} catch (final NodeUnderCapacity e) {
							final AbstractNode expandedChild = this.balanceChildren(child);
							this.thisNode.updateRadius(expandedChild);
							return;
						} catch (final RootNodeReplacement e) {
							throw new RuntimeException("Should never happen!");
						}
					}
				}
			}

			throw new DataNotFound();
		}

		private AbstractNode balanceChildren(final AbstractNode theChild) {
			// Tries to find anotherChild which can donate a grand-child to theChild.

			AbstractNode nearestDonor = null;
			double distanceNearestDonor = Double.POSITIVE_INFINITY;

			AbstractNode nearestMergeCandidate = null;
			double distanceNearestMergeCandidate = Double.POSITIVE_INFINITY;

			for (final IndexItem child : this.thisNode.children.values()) {
				@SuppressWarnings("unchecked")
				final AbstractNode anotherChild = (AbstractNode) child;
				if (anotherChild == theChild) {
					continue;
				}

				final double distance = this.thisNode.mtree().distanceFunction.calculate(theChild.getData(), anotherChild.getData());
				if (anotherChild.children.size() > anotherChild.getMinCapacity()) {
					if (distance < distanceNearestDonor) {
						distanceNearestDonor = distance;
						nearestDonor = anotherChild;
					}
				} else {
					if (distance < distanceNearestMergeCandidate) {
						distanceNearestMergeCandidate = distance;
						nearestMergeCandidate = anotherChild;
					}
				}
			}

			if (nearestDonor == null) {
				// Merge
				for (final IndexItem grandchild : theChild.children.values()) {
					final double distance = this.thisNode.mtree().distanceFunction.calculate(grandchild.getData(),
							nearestMergeCandidate.getData());
					nearestMergeCandidate.addChild(grandchild, distance);
				}

				final IndexItem removed = this.thisNode.children.remove(theChild.getData());
				assert removed != null;
				return nearestMergeCandidate;
			} else {
				// Donate
				// Look for the nearest grandchild
				IndexItem nearestGrandchild = null;
				double nearestGrandchildDistance = Double.POSITIVE_INFINITY;
				for (final IndexItem grandchild : nearestDonor.children.values()) {
					final double distance = this.thisNode.mtree().distanceFunction.calculate(grandchild.data, theChild.getData());
					if (distance < nearestGrandchildDistance) {
						nearestGrandchildDistance = distance;
						nearestGrandchild = grandchild;
					}
				}

				final IndexItem indexItem = nearestDonor.children.remove(nearestGrandchild.data);
				assert indexItem != null;
				theChild.addChild(nearestGrandchild, nearestGrandchildDistance);
				return theChild;
			}
		}

		@Override
		public void checkChildClass(final IndexItem child) {
			assert child instanceof MTree.InternalNode
					|| child instanceof MTree.LeafNode;
		}
	}

	private final class RootLeafNode extends AbstractNode {

		private RootLeafNode(final T data) {
			super(data, new RootNodeTrait(), new LeafNodeTrait());
		}

		@Override
		void removeData(final T data, final double distance) throws RootNodeReplacement, DataNotFound {
			try {
				super.removeData(data, distance);
			} catch (final NodeUnderCapacity e) {
				assert this.children.isEmpty();
				throw new RootNodeReplacement(null);
			}
		}

		@Override
		protected int getMinCapacity() {
			return 1;
		}

		@Override
		void checkMinCapacity() {
			assert this.children.size() >= 1;
		}
	}

	private final class RootNode extends AbstractNode {

		private RootNode(final T data) {
			super(data, new RootNodeTrait(), new NonLeafNodeTrait());
		}

		@Override
		void removeData(final T data, final double distance) throws RootNodeReplacement, NodeUnderCapacity, DataNotFound {
			try {
				super.removeData(data, distance);
			} catch (final NodeUnderCapacity e) {
				// Promote the only child to root
				@SuppressWarnings("unchecked")
				final AbstractNode theChild = (AbstractNode) this.children.values().iterator().next();
				final AbstractNode newRoot;
				if (theChild instanceof MTree.InternalNode) {
					newRoot = new RootNode(theChild.getData());
				} else {
					assert theChild instanceof MTree.LeafNode;
					newRoot = new RootLeafNode(theChild.getData());
				}

				for (final IndexItem grandchild : theChild.children.values()) {
					final double newDistance = MTree.this.distanceFunction.calculate(newRoot.getData(), grandchild.data);
					newRoot.addChild(grandchild, newDistance);
				}
				theChild.children.clear();

				throw new RootNodeReplacement(newRoot);
			}
		}

		@Override
		protected int getMinCapacity() {
			return 2;
		}

		@Override
		void checkMinCapacity() {
			assert this.children.size() >= 2;
		}
	}

	private final class InternalNode extends AbstractNode {
		private InternalNode(final T data) {
			super(data, new NonRootNodeTrait(), new NonLeafNodeTrait());
		}
	};

	private class LeafNode extends AbstractNode {

		public LeafNode(final T data) {
			super(data, new NonRootNodeTrait(), new LeafNodeTrait());
		}
	}

	private final class Entry extends IndexItem {
		private Entry(final T data) {
			super(data);
		}
	}

	/**
	 * The type of the results for nearest-neighbor queries.
	 */
	public final class ResultItem {

		/** A nearest-neighbor. */
		private final T data;

		/**
		 * The distance from the nearest-neighbor to the query data object
		 * parameter.
		 */
		private final double distance;

		private ResultItem(final T data, final double distance) {
			this.data = data;
			this.distance = distance;
		}

		public T getData() {
			return this.data;
		}

		public double getDistance() {
			return this.distance;
		}
	}

	// Exception classes
	private static final class SplitNodeReplacement extends Exception {
		// A subclass of Throwable cannot be generic. :-(
		// So, we have newNodes declared as Object[] instead of Node[].
		private final Object[] newNodes;

		private SplitNodeReplacement(final Object... newNodes) {
			this.newNodes = newNodes;
		}
	}

	private static final class RootNodeReplacement extends Exception {
		// A subclass of Throwable cannot be generic. :-(
		// So, we have newRoot declared as Object instead of Node.
		private final Object newRoot;

		private RootNodeReplacement(final Object newRoot) {
			this.newRoot = newRoot;
		}
	}

	private static final class NodeUnderCapacity extends Exception {
		public NodeUnderCapacity() {
			// default constructor
		}
	}

	private static final class DataNotFound extends Exception {
		public DataNotFound() {
			// default constructor
		}
	}

	/**
	 * An {@link Iterable} class which can be iterated to fetch the results of a
	 * nearest-neighbors query.
	 *
	 * <p>
	 * The neighbors are presented in non-decreasing order from the {@code
	 * queryData} argument to the {@link MTree#getNearest(Object, double, int)
	 * getNearest*()}
	 * call.
	 *
	 * <p>
	 * The query on the M-Tree is executed during the iteration, as the
	 * results are fetched. It means that, by the time when the <i>n</i>-th
	 * result is fetched, the next result may still not be known, and the
	 * resources allocated were only the necessary to identify the <i>n</i>
	 * first results.
	 */
	public final class Query implements Iterable<ResultItem> {

		private final T data;
		private final double range;
		private final int limit;

		private Query(final T data, final double range, final int limit) {
			this.data = data;
			this.range = range;
			this.limit = limit;
		}

		@Override
		public Iterator<ResultItem> iterator() {
			return new ResultsIterator();
		}

		private final class ResultsIterator implements Iterator<ResultItem> {

			private ResultItem nextResultItem = null;
			private boolean finished = false;
			private final PriorityQueue<ItemWithDistances<AbstractNode>> pendingQueue = new PriorityQueue<>();
			private double nextPendingMinDistance;
			private final PriorityQueue<ItemWithDistances<Entry>> nearestQueue = new PriorityQueue<>();
			private int yieldedCount;

			private ResultsIterator() {
				if (MTree.this.root == null) {
					this.finished = true;
					return;
				}

				final double distance = MTree.this.distanceFunction.calculate(Query.this.data, MTree.this.root.getData());
				final double minDistance = Math.max(distance - MTree.this.root.radius, 0.0);

				this.pendingQueue.add(new ItemWithDistances<>(MTree.this.root, distance, minDistance));
				this.nextPendingMinDistance = minDistance;
			}

			@Override
			public boolean hasNext() {
				if (this.finished) {
					return false;
				}

				if (this.nextResultItem == null) {
					this.fetchNext();
				}

				if (this.nextResultItem == null) {
					this.finished = true;
					return false;
				} else {
					return true;
				}
			}

			@Override
			public ResultItem next() {
				if (this.hasNext()) {
					final ResultItem next = this.nextResultItem;
					this.nextResultItem = null;
					return next;
				} else {
					throw new NoSuchElementException();
				}
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

			private void fetchNext() {
				assert !this.finished;

				if (this.finished || this.yieldedCount >= Query.this.limit) {
					this.finished = true;
					return;
				}

				while (!this.pendingQueue.isEmpty() || !this.nearestQueue.isEmpty()) {
					if (this.prepareNextNearest()) {
						return;
					}

					assert !this.pendingQueue.isEmpty();

					final ItemWithDistances<AbstractNode> pending = this.pendingQueue.poll();
					final AbstractNode node = pending.item;

					for (final IndexItem child : node.children.values()) {
						if (Math.abs(pending.distance - child.distanceToParent) - child.radius <= Query.this.range) {
							final double childDistance = MTree.this.distanceFunction.calculate(Query.this.data, child.data);
							final double childMinDistance = Math.max(childDistance - child.radius, 0.0);
							if (childMinDistance <= Query.this.range) {
								if (child instanceof MTree.Entry) {
									@SuppressWarnings("unchecked")
									final Entry entry = (Entry) child;
									this.nearestQueue.add(new ItemWithDistances<>(entry, childDistance, childMinDistance));
								} else {
									@SuppressWarnings("unchecked")
									final AbstractNode childNode = (AbstractNode) child;
									this.pendingQueue.add(new ItemWithDistances<>(childNode, childDistance, childMinDistance));
								}
							}
						}
					}

					if (this.pendingQueue.isEmpty()) {
						this.nextPendingMinDistance = Double.POSITIVE_INFINITY;
					} else {
						this.nextPendingMinDistance = this.pendingQueue.peek().minDistance;
					}
				}

				this.finished = true;
			}

			private boolean prepareNextNearest() {
				if (!this.nearestQueue.isEmpty()) {
					final ItemWithDistances<Entry> nextNearest = this.nearestQueue.peek();
					if (nextNearest.distance <= this.nextPendingMinDistance) {
						this.nearestQueue.poll();
						this.nextResultItem = new ResultItem(nextNearest.item.getData(), nextNearest.distance);
						++this.yieldedCount;
						return true;
					}
				}

				return false;
			}

			private class ItemWithDistances<U> implements Comparable<ItemWithDistances<U>> {
				private final U item;
				private final double distance;
				private final double minDistance;

				public ItemWithDistances(final U item, final double distance, final double minDistance) {
					this.item = item;
					this.distance = distance;
					this.minDistance = minDistance;
				}

				@Override
				public int compareTo(final ItemWithDistances<U> that) {
					if (this.minDistance < that.minDistance) {
						return -1;
					} else if (this.minDistance > that.minDistance) {
						return +1;
					} else {
						return 0;
					}
				}
			}
		}
	}

}
