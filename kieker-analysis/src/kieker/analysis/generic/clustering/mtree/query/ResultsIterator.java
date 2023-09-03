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
package kieker.analysis.generic.clustering.mtree.query;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

import kieker.analysis.generic.clustering.mtree.nodes.AbstractNode;
import kieker.analysis.generic.clustering.mtree.nodes.Entry;
import kieker.analysis.generic.clustering.mtree.nodes.IndexItem;

/**
 * @param <T>
 *            data type for result items
 *
 * @author Eduardo R. D'Avila
 * @since 2.0.0
 */
public final class ResultsIterator<T> implements Iterator<ResultItem<T>> {

	private ResultItem<T> nextResultItem = null;
	private boolean finished = false;
	private final PriorityQueue<ItemWithDistances<AbstractNode<T>>> pendingQueue = new PriorityQueue<>();
	private double nextPendingMinDistance;
	private final PriorityQueue<ItemWithDistances<Entry<T>>> nearestQueue = new PriorityQueue<>();
	private int yieldedCount;
	private Query<T> query;

	public ResultsIterator(final Query<T> query) {
		this.query = query;
		if (this.query.getMTree().getRoot() == null) {
			this.finished = true;
			return;
		}

		final double distance = this.query.getMTree().getDistanceFunction().calculate(this.query.getData(), this.query.getMTree().getRoot().getData());
		final double minDistance = Math.max(distance - this.query.getMTree().getRoot().getRadius(), 0.0);

		this.pendingQueue.add(new ItemWithDistances<>(this.query.getMTree().getRoot(), distance, minDistance));
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
	public ResultItem<T> next() {
		if (this.hasNext()) {
			final ResultItem<T> next = this.nextResultItem;
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

		if (this.finished || (this.yieldedCount >= this.query.getLimit())) {
			this.finished = true;
			return;
		}

		while (!this.pendingQueue.isEmpty() || !this.nearestQueue.isEmpty()) {
			if (this.prepareNextNearest()) {
				return;
			}

			assert !this.pendingQueue.isEmpty();

			final ItemWithDistances<AbstractNode<T>> pending = this.pendingQueue.poll();
			final AbstractNode<T> node = pending.item;

			for (final IndexItem<T> child : node.getChildren().values()) {
				if ((Math.abs(pending.distance - child.getDistanceToParent()) - child.getRadius()) <= this.query.getRange()) {
					final double childDistance = this.query.getMTree().getDistanceFunction().calculate(this.query.getData(), child.getData());
					final double childMinDistance = Math.max(childDistance - child.getRadius(), 0.0);
					if (childMinDistance <= this.query.getRange()) {
						if (child instanceof Entry) {
							final Entry<T> entry = (Entry<T>) child;
							this.nearestQueue.add(new ItemWithDistances<>(entry, childDistance, childMinDistance));
						} else {
							final AbstractNode<T> childNode = (AbstractNode<T>) child;
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
			final ItemWithDistances<Entry<T>> nextNearest = this.nearestQueue.peek();
			if (nextNearest.distance <= this.nextPendingMinDistance) {
				this.nearestQueue.poll();
				this.nextResultItem = new ResultItem<T>(nextNearest.item.getData(), nextNearest.distance);
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
