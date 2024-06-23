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
package kieker.analysis.generic.clustering.mtree.nodes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import kieker.analysis.exception.InternalErrorException;
import kieker.analysis.generic.clustering.mtree.DistanceFunctionFactory;
import kieker.analysis.generic.clustering.mtree.IDistanceFunction;
import kieker.analysis.generic.clustering.mtree.ILeafness;
import kieker.analysis.generic.clustering.mtree.IRootness;
import kieker.analysis.generic.clustering.mtree.ISplitFunction.SplitResult;
import kieker.analysis.generic.clustering.mtree.MTree;
import kieker.analysis.generic.clustering.mtree.utils.Pair;

/**
 * @param <T>
 *            data element type
 *
 * @author Eduardo R. D'Avila
 *
 * @since 2.0.0
 */
public abstract class AbstractNode<T> extends IndexItem<T> {

	protected IRootness rootness;
	protected ILeafness<T> leafness;

	private final Map<T, IndexItem<T>> children = new HashMap<>();
	private final MTree<T> mtree;

	protected AbstractNode(final MTree<T> mtree, final T data) {
		super(data);

		this.mtree = mtree;
	}

	public Map<T, IndexItem<T>> getChildren() {
		return this.children;
	}

	public final void addData(final T data, final double distance) throws InternalErrorException {
		this.doAddData(data, distance);
	}

	@Override
	public int check() {
		super.check();
		this.checkMinCapacity();
		this.checkMaxCapacity();

		int childHeight = -1;
		for (final Map.Entry<T, IndexItem<T>> e : this.children.entrySet()) {
			final T data = e.getKey();
			final IndexItem<T> child = e.getValue();
			assert child.getData().equals(data);

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

	protected void doAddData(final T data, final double distance) throws InternalErrorException {
		this.leafness.doAddData(data, distance);
	}

	protected boolean doRemoveData(final T data, final double distance) throws InternalErrorException {
		return this.leafness.doRemoveData(data, distance);
	}

	public final boolean isMaxCapacityExceeded() throws InternalErrorException {
		return (this.children.size() > this.mtree.getMaxNodeCapacity());
	}

	public final Pair<AbstractNode<T>> splitNodes() throws InternalErrorException {
		final IDistanceFunction<? super T> cachedDistanceFunction = DistanceFunctionFactory.cached(this.mtree.getDistanceFunction());
		final SplitResult<T> splitResult = this.mtree.getSplitFunction().process(this.children.keySet(), cachedDistanceFunction);

		final AbstractNode<T> newNode0 = this.createNewNode(splitResult, cachedDistanceFunction, 0);
		final AbstractNode<T> newNode1 = this.createNewNode(splitResult, cachedDistanceFunction, 1);

		assert this.children.isEmpty();

		return new Pair<>(newNode0, newNode1);
	}

	private AbstractNode<T> createNewNode(final SplitResult<T> splitResult, final IDistanceFunction<? super T> distanceFunction, final int resultIndex)
			throws InternalErrorException {
		final T promotedData = splitResult.getPromoted().get(resultIndex);
		final Set<T> partition = splitResult.getPartitions().get(resultIndex);

		final AbstractNode<T> newNode = this.newSplitNodeReplacement(promotedData);
		for (final T data : partition) {
			final IndexItem<T> child = this.children.get(data);
			this.children.remove(data);
			final double distance = distanceFunction.calculate(promotedData, data);
			newNode.addChild(child, distance);
		}

		return newNode;
	}

	protected AbstractNode<T> newSplitNodeReplacement(final T data) {
		return this.leafness.newSplitNodeReplacement(data);
	}

	public void addChild(final IndexItem<T> child, final double distance) throws InternalErrorException {
		this.leafness.addChild(child, distance);
	}

	public boolean removeData(final T data, final double distance)
			throws InternalErrorException {
		return this.doRemoveData(data, distance);
	}

	public boolean isNodeUnderCapacity() throws InternalErrorException {
		return this.children.size() < this.getMinCapacity();
	}

	protected int getMinCapacity() throws InternalErrorException {
		return this.rootness.getMinCapacity();
	}

	public void updateMetrics(final IndexItem<T> child, final double distance) {
		child.setDistanceToParent(distance);
		this.updateRadius(child);
	}

	public void updateRadius(final IndexItem<T> child) {
		this.radius = Math.max(this.radius, child.getDistanceToParent() + child.getRadius());
	}

	public void checkMinCapacity() {
		this.rootness.checkMinCapacity();
	}

	private void checkMaxCapacity() {
		assert this.children.size() <= this.mtree.getMaxNodeCapacity();
	}

	private void checkChildClass(final IndexItem<T> child) {
		this.leafness.checkChildClass(child);
	}

	private void checkChildMetrics(final IndexItem<T> child) {
		final double dist = this.mtree.getDistanceFunction().calculate(child.getData(), this.getData());
		assert child.getDistanceToParent() == dist;

		final double sum = child.getDistanceToParent() + child.getRadius();
		assert sum <= this.radius;
	}

	@Override
	protected void checkDistanceToParent() {
		this.rootness.checkDistanceToParent();
	}

	public MTree<T> getMTree() {
		return this.mtree;
	}
}
