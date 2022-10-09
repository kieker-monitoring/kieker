package kieker.analysis.behavior.mtree;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Set;

import kieker.analysis.behavior.mtree.SplitFunction.SplitResult;

/**
 * The main class that implements the M-Tree.
 *
 * @param <DATA>
 *            The type of data that will be indexed by the M-Tree. Objects of
 *            this type are stored in HashMaps and HashSets, so their
 *            {@code hashCode()} and {@code equals()} methods must be consistent.
 */
public class MTree<DATA> {

	/**
	 * The type of the results for nearest-neighbor queries.
	 */
	public class ResultItem {
		private ResultItem(final DATA data, final double distance) {
			this.data = data;
			this.distance = distance;
		}

		/** A nearest-neighbor. */
		public DATA data;

		/**
		 * The distance from the nearest-neighbor to the query data object
		 * parameter.
		 */
		public double distance;
	}

	// Exception classes
	private static class SplitNodeReplacement extends Exception {
		// A subclass of Throwable cannot be generic. :-(
		// So, we have newNodes declared as Object[] instead of Node[].
		private final Object newNodes[];

		private SplitNodeReplacement(final Object... newNodes) {
			this.newNodes = newNodes;
		}
	}

	private static class RootNodeReplacement extends Exception {
		// A subclass of Throwable cannot be generic. :-(
		// So, we have newRoot declared as Object instead of Node.
		private final Object newRoot;

		private RootNodeReplacement(final Object newRoot) {
			this.newRoot = newRoot;
		}
	}

	private static class NodeUnderCapacity extends Exception {
	}

	private static class DataNotFound extends Exception {
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
	public class Query implements Iterable<ResultItem> {

		private class ResultsIterator implements Iterator<ResultItem> {

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

			private ResultItem nextResultItem = null;
			private boolean finished = false;
			private final PriorityQueue<ItemWithDistances<Node>> pendingQueue = new PriorityQueue<>();
			private double nextPendingMinDistance;
			private final PriorityQueue<ItemWithDistances<Entry>> nearestQueue = new PriorityQueue<>();
			private int yieldedCount;

			private ResultsIterator() {
				if (MTree.this.root == null) {
					this.finished = true;
					return;
				}

				final double distance = MTree.this.distanceFunction.calculate(Query.this.data, MTree.this.root.data);
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

					final ItemWithDistances<Node> pending = this.pendingQueue.poll();
					final Node node = pending.item;

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
									final Node childNode = (Node) child;
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
						this.nextResultItem = new ResultItem(nextNearest.item.data, nextNearest.distance);
						++this.yieldedCount;
						return true;
					}
				}

				return false;
			}

		}

		private Query(final DATA data, final double range, final int limit) {
			this.data = data;
			this.range = range;
			this.limit = limit;
		}

		@Override
		public Iterator<ResultItem> iterator() {
			return new ResultsIterator();
		}

		private final DATA data;
		private final double range;
		private final int limit;
	}

	/**
	 * The default minimum capacity of nodes in an M-Tree, when not specified in
	 * the constructor call.
	 */
	public static final int DEFAULT_MIN_NODE_CAPACITY = 50;

	protected int minNodeCapacity;
	protected int maxNodeCapacity;
	protected DistanceFunction<? super DATA> distanceFunction;
	protected SplitFunction<DATA> splitFunction;
	protected Node root;

	/**
	 * Constructs an M-Tree with the specified distance function.
	 *
	 * @param distanceFunction
	 *            The object used to calculate the distance between
	 *            two data objects.
	 */
	public MTree(final DistanceFunction<? super DATA> distanceFunction,
			final SplitFunction<DATA> splitFunction) {
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
			final DistanceFunction<? super DATA> distanceFunction,
			final SplitFunction<DATA> splitFunction) {
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
	 * @param splitFunction
	 *            The object used to process the split of nodes if
	 *            they are full when a new child must be added.
	 */
	public MTree(final int minNodeCapacity, final int maxNodeCapacity,
			final DistanceFunction<? super DATA> distanceFunction,
			SplitFunction<DATA> splitFunction) {
		if (minNodeCapacity < 2 || maxNodeCapacity <= minNodeCapacity ||
				distanceFunction == null) {
			throw new IllegalArgumentException();
		}

		if (splitFunction == null) {
			splitFunction = new ComposedSplitFunction<>(
					new PromotionFunctions.RandomPromotion<DATA>(),
					new PartitionFunctions.BalancedPartition<DATA>());
		}

		this.minNodeCapacity = minNodeCapacity;
		this.maxNodeCapacity = maxNodeCapacity;
		this.distanceFunction = distanceFunction;
		this.splitFunction = splitFunction;
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
	public void add(final DATA data) {
		if (this.root == null) {
			this.root = new RootLeafNode(data);
			try {
				this.root.addData(data, 0);
			} catch (final SplitNodeReplacement e) {
				throw new RuntimeException("Should never happen!");
			}
		} else {
			double distance = this.distanceFunction.calculate(data, this.root.data);
			try {
				this.root.addData(data, distance);
			} catch (final SplitNodeReplacement e) {
				final Node newRoot = new RootNode(data);
				this.root = newRoot;
				for (final Object newNode2 : e.newNodes) {
					@SuppressWarnings("unchecked")
					final Node newNode = (Node) newNode2;
					distance = this.distanceFunction.calculate(this.root.data, newNode.data);
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
	public boolean remove(final DATA data) {
		if (this.root == null) {
			return false;
		}

		final double distanceToRoot = this.distanceFunction.calculate(data, this.root.data);
		try {
			this.root.removeData(data, distanceToRoot);
		} catch (final RootNodeReplacement e) {
			@SuppressWarnings("unchecked")
			final Node newRoot = (Node) e.newRoot;
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
	public Query getNearestByRange(final DATA queryData, final double range) {
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
	public Query getNearestByLimit(final DATA queryData, final int limit) {
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
	public Query getNearest(final DATA queryData, final double range, final int limit) {
		return new Query(queryData, range, limit);
	}

	/**
	 * Performs a nearest-neighbor query on the M-Tree, without constraints.
	 *
	 * @param queryData
	 *            The query data object.
	 * @return A {@link Query} object used to iterate on the results.
	 */
	public Query getNearest(final DATA queryData) {
		return new Query(queryData, Double.POSITIVE_INFINITY, Integer.MAX_VALUE);
	}

	protected void _check() {
		if (this.root != null) {
			this.root._check();
		}
	}

	private class IndexItem {
		DATA data;
		protected double radius;
		double distanceToParent;

		private IndexItem(final DATA data) {
			this.data = data;
			this.radius = 0;
			this.distanceToParent = -1;
		}

		int _check() {
			this._checkRadius();
			this._checkDistanceToParent();
			return 1;
		}

		private void _checkRadius() {
			assert this.radius >= 0;
		}

		protected void _checkDistanceToParent() {
			assert !(this instanceof MTree.RootLeafNode);
			assert !(this instanceof MTree.RootNode);
			assert this.distanceToParent >= 0;
		}
	}

	private abstract class Node extends IndexItem {

		protected Map<DATA, IndexItem> children = new HashMap<>();
		protected Rootness rootness;
		protected Leafness<DATA> leafness;

		private <R extends NodeTrait & Rootness, L extends NodeTrait & Leafness<DATA>> Node(final DATA data, final R rootness, final L leafness) {
			super(data);

			rootness.thisNode = this;
			this.rootness = rootness;

			leafness.thisNode = this;
			this.leafness = leafness;
		}

		private final void addData(final DATA data, final double distance) throws SplitNodeReplacement {
			this.doAddData(data, distance);
			this.checkMaxCapacity();
		}

		@Override
		int _check() {
			super._check();
			this._checkMinCapacity();
			this._checkMaxCapacity();

			int childHeight = -1;
			for (final Map.Entry<DATA, IndexItem> e : this.children.entrySet()) {
				final DATA data = e.getKey();
				final IndexItem child = e.getValue();
				assert child.data.equals(data);

				this._checkChildClass(child);
				this._checkChildMetrics(child);

				final int height = child._check();
				if (childHeight < 0) {
					childHeight = height;
				} else {
					assert childHeight == height;
				}
			}

			return childHeight + 1;
		}

		protected void doAddData(final DATA data, final double distance) {
			this.leafness.doAddData(data, distance);
		}

		protected void doRemoveData(final DATA data, final double distance) throws DataNotFound {
			this.leafness.doRemoveData(data, distance);
		}

		private final void checkMaxCapacity() throws SplitNodeReplacement {
			if (this.children.size() > MTree.this.maxNodeCapacity) {
				final DistanceFunction<? super DATA> cachedDistanceFunction = DistanceFunctions.cached(MTree.this.distanceFunction);
				final SplitResult<DATA> splitResult = MTree.this.splitFunction.process(this.children.keySet(), cachedDistanceFunction);

				Node newNode0 = null;
				Node newNode1 = null;
				for (int i = 0; i < 2; ++i) {
					final DATA promotedData = splitResult.promoted.get(i);
					final Set<DATA> partition = splitResult.partitions.get(i);

					final Node newNode = this.newSplitNodeReplacement(promotedData);
					for (final DATA data : partition) {
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

		protected Node newSplitNodeReplacement(final DATA data) {
			return this.leafness.newSplitNodeReplacement(data);
		}

		protected void addChild(final IndexItem child, final double distance) {
			this.leafness.addChild(child, distance);
		}

		void removeData(final DATA data, final double distance) throws RootNodeReplacement, NodeUnderCapacity, DataNotFound {
			this.doRemoveData(data, distance);
			if (this.children.size() < this.getMinCapacity()) {
				throw new NodeUnderCapacity();
			}
		}

		protected int getMinCapacity() {
			return this.rootness.getMinCapacity();
		}

		private void updateMetrics(final IndexItem child, final double distance) {
			child.distanceToParent = distance;
			this.updateRadius(child);
		}

		private void updateRadius(final IndexItem child) {
			this.radius = Math.max(this.radius, child.distanceToParent + child.radius);
		}

		void _checkMinCapacity() {
			this.rootness._checkMinCapacity();
		}

		private void _checkMaxCapacity() {
			assert this.children.size() <= MTree.this.maxNodeCapacity;
		}

		private void _checkChildClass(final IndexItem child) {
			this.leafness._checkChildClass(child);
		}

		private void _checkChildMetrics(final IndexItem child) {
			final double dist = MTree.this.distanceFunction.calculate(child.data, this.data);
			assert child.distanceToParent == dist;

			final double sum = child.distanceToParent + child.radius;
			assert sum <= this.radius;
		}

		@Override
		protected void _checkDistanceToParent() {
			this.rootness._checkDistanceToParent();
		}

		private MTree<DATA> mtree() {
			return MTree.this;
		}
	}

	private abstract class NodeTrait {
		protected Node thisNode;
	}

	private interface Leafness<DATA> {
		void doAddData(DATA data, double distance);

		void addChild(MTree<DATA>.IndexItem child, double distance);

		void doRemoveData(DATA data, double distance) throws DataNotFound;

		MTree<DATA>.Node newSplitNodeReplacement(DATA data);

		void _checkChildClass(MTree<DATA>.IndexItem child);
	}

	private interface Rootness {
		int getMinCapacity();

		void _checkDistanceToParent();

		void _checkMinCapacity();
	}

	private class RootNodeTrait extends NodeTrait implements Rootness {

		@Override
		public int getMinCapacity() {
			throw new RuntimeException("Should not be called!");
		}

		@Override
		public void _checkDistanceToParent() {
			assert this.thisNode.distanceToParent == -1;
		}

		@Override
		public void _checkMinCapacity() {
			this.thisNode._checkMinCapacity();
		}

	};

	private class NonRootNodeTrait extends NodeTrait implements Rootness {

		@Override
		public int getMinCapacity() {
			return MTree.this.minNodeCapacity;
		}

		@Override
		public void _checkMinCapacity() {
			assert this.thisNode.children.size() >= this.thisNode.mtree().minNodeCapacity;
		}

		@Override
		public void _checkDistanceToParent() {
			assert this.thisNode.distanceToParent >= 0;
		}
	};

	private class LeafNodeTrait extends NodeTrait implements Leafness<DATA> {

		@Override
		public void doAddData(final DATA data, final double distance) {
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
		public Node newSplitNodeReplacement(final DATA data) {
			return this.thisNode.mtree().new LeafNode(data);
		}

		@Override
		public void doRemoveData(final DATA data, final double distance) throws DataNotFound {
			if (this.thisNode.children.remove(data) == null) {
				throw new DataNotFound();
			}
		}

		@Override
		public void _checkChildClass(final IndexItem child) {
			assert child instanceof MTree.Entry;
		}
	}

	class NonLeafNodeTrait extends NodeTrait implements Leafness<DATA> {

		@Override
		public void doAddData(final DATA data, double distance) {
			class CandidateChild {
				Node node;
				double distance;
				double metric;

				private CandidateChild(final Node node, final double distance, final double metric) {
					this.node = node;
					this.distance = distance;
					this.metric = metric;
				}
			}

			CandidateChild minRadiusIncreaseNeeded = new CandidateChild(null, -1.0, Double.POSITIVE_INFINITY);
			CandidateChild nearestDistance = new CandidateChild(null, -1.0, Double.POSITIVE_INFINITY);

			for (final IndexItem item : this.thisNode.children.values()) {
				@SuppressWarnings("unchecked")
				final Node child = (Node) item;
				final double childDistance = this.thisNode.mtree().distanceFunction.calculate(child.data, data);
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

			final CandidateChild chosen = nearestDistance.node != null
					? nearestDistance
					: minRadiusIncreaseNeeded;

			final Node child = chosen.node;
			try {
				child.addData(data, chosen.distance);
				this.thisNode.updateRadius(child);
			} catch (final SplitNodeReplacement e) {
				// Replace current child with new nodes
				final IndexItem itemIndex = this.thisNode.children.remove(child.data);
				assert itemIndex != null;

				for (final Object newNode : e.newNodes) {
					@SuppressWarnings("unchecked")
					final Node newChild = (Node) newNode;
					distance = this.thisNode.mtree().distanceFunction.calculate(this.thisNode.data, newChild.data);
					this.thisNode.addChild(newChild, distance);
				}
			}
		}

		@Override
		public void addChild(final IndexItem newChild_, double distance) {
			@SuppressWarnings("unchecked")
			Node newChild = (Node) newChild_;

			class ChildWithDistance {
				Node child;
				double distance;

				private ChildWithDistance(final Node child, final double distance) {
					this.child = child;
					this.distance = distance;
				}
			}

			final Deque<ChildWithDistance> newChildren = new ArrayDeque<>();
			newChildren.addFirst(new ChildWithDistance(newChild, distance));

			while (!newChildren.isEmpty()) {
				final ChildWithDistance cwd = newChildren.removeFirst();

				newChild = cwd.child;
				distance = cwd.distance;
				if (this.thisNode.children.containsKey(newChild.data)) {
					@SuppressWarnings("unchecked")
					final Node existingChild = (Node) this.thisNode.children.get(newChild.data);
					assert existingChild.data.equals(newChild.data);

					// Transfer the _children_ of the newChild to the existingChild
					for (final IndexItem grandchild : newChild.children.values()) {
						existingChild.addChild(grandchild, grandchild.distanceToParent);
					}
					newChild.children.clear();

					try {
						existingChild.checkMaxCapacity();
					} catch (final SplitNodeReplacement e) {
						final IndexItem indexItem = this.thisNode.children.remove(existingChild.data);
						assert indexItem != null;

						for (final Object newNode2 : e.newNodes) {
							@SuppressWarnings("unchecked")
							final Node newNode = (Node) newNode2;
							distance = this.thisNode.mtree().distanceFunction.calculate(this.thisNode.data, newNode.data);
							newChildren.addFirst(new ChildWithDistance(newNode, distance));
						}
					}
				} else {
					this.thisNode.children.put(newChild.data, newChild);
					this.thisNode.updateMetrics(newChild, distance);
				}
			}
		}

		@Override
		public Node newSplitNodeReplacement(final DATA data) {
			return new InternalNode(data);
		}

		@Override
		public void doRemoveData(final DATA data, final double distance) throws DataNotFound {
			for (final IndexItem childItem : this.thisNode.children.values()) {
				@SuppressWarnings("unchecked")
				final Node child = (Node) childItem;
				if (Math.abs(distance - child.distanceToParent) <= child.radius) {
					final double distanceToChild = this.thisNode.mtree().distanceFunction.calculate(data, child.data);
					if (distanceToChild <= child.radius) {
						try {
							child.removeData(data, distanceToChild);
							this.thisNode.updateRadius(child);
							return;
						} catch (final DataNotFound e) {
							// If DataNotFound was thrown, then the data was not found in the child
						} catch (final NodeUnderCapacity e) {
							final Node expandedChild = this.balanceChildren(child);
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

		private Node balanceChildren(final Node theChild) {
			// Tries to find anotherChild which can donate a grand-child to theChild.

			Node nearestDonor = null;
			double distanceNearestDonor = Double.POSITIVE_INFINITY;

			Node nearestMergeCandidate = null;
			double distanceNearestMergeCandidate = Double.POSITIVE_INFINITY;

			for (final IndexItem child : this.thisNode.children.values()) {
				@SuppressWarnings("unchecked")
				final Node anotherChild = (Node) child;
				if (anotherChild == theChild) {
					continue;
				}

				final double distance = this.thisNode.mtree().distanceFunction.calculate(theChild.data, anotherChild.data);
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
					final double distance = this.thisNode.mtree().distanceFunction.calculate(grandchild.data, nearestMergeCandidate.data);
					nearestMergeCandidate.addChild(grandchild, distance);
				}

				final IndexItem removed = this.thisNode.children.remove(theChild.data);
				assert removed != null;
				return nearestMergeCandidate;
			} else {
				// Donate
				// Look for the nearest grandchild
				IndexItem nearestGrandchild = null;
				double nearestGrandchildDistance = Double.POSITIVE_INFINITY;
				for (final IndexItem grandchild : nearestDonor.children.values()) {
					final double distance = this.thisNode.mtree().distanceFunction.calculate(grandchild.data, theChild.data);
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
		public void _checkChildClass(final IndexItem child) {
			assert child instanceof MTree.InternalNode
					|| child instanceof MTree.LeafNode;
		}
	}

	private class RootLeafNode extends Node {

		private RootLeafNode(final DATA data) {
			super(data, new RootNodeTrait(), new LeafNodeTrait());
		}

		@Override
		void removeData(final DATA data, final double distance) throws RootNodeReplacement, DataNotFound {
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
		void _checkMinCapacity() {
			assert this.children.size() >= 1;
		}
	}

	private class RootNode extends Node {

		private RootNode(final DATA data) {
			super(data, new RootNodeTrait(), new NonLeafNodeTrait());
		}

		@Override
		void removeData(final DATA data, double distance) throws RootNodeReplacement, NodeUnderCapacity, DataNotFound {
			try {
				super.removeData(data, distance);
			} catch (final NodeUnderCapacity e) {
				// Promote the only child to root
				@SuppressWarnings("unchecked")
				final Node theChild = (Node) this.children.values().iterator().next();
				Node newRoot;
				if (theChild instanceof MTree.InternalNode) {
					newRoot = new RootNode(theChild.data);
				} else {
					assert theChild instanceof MTree.LeafNode;
					newRoot = new RootLeafNode(theChild.data);
				}

				for (final IndexItem grandchild : theChild.children.values()) {
					distance = MTree.this.distanceFunction.calculate(newRoot.data, grandchild.data);
					newRoot.addChild(grandchild, distance);
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
		void _checkMinCapacity() {
			assert this.children.size() >= 2;
		}
	}

	private class InternalNode extends Node {
		private InternalNode(final DATA data) {
			super(data, new NonRootNodeTrait(), new NonLeafNodeTrait());
		}
	};

	private class LeafNode extends Node {

		public LeafNode(final DATA data) {
			super(data, new NonRootNodeTrait(), new LeafNodeTrait());
		}
	}

	private class Entry extends IndexItem {
		private Entry(final DATA data) {
			super(data);
		}
	}
}
