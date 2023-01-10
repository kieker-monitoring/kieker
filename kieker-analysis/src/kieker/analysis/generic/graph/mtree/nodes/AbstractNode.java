package kieker.analysis.generic.graph.mtree.nodes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import kieker.analysis.exception.InternalErrorException;
import kieker.analysis.generic.graph.mtree.DistanceFunctionFactory;
import kieker.analysis.generic.graph.mtree.IDistanceFunction;
import kieker.analysis.generic.graph.mtree.ILeafness;
import kieker.analysis.generic.graph.mtree.IRootness;
import kieker.analysis.generic.graph.mtree.ISplitFunction.SplitResult;
import kieker.analysis.generic.graph.mtree.MTree;
import kieker.analysis.generic.graph.mtree.exceptions.DataNotFoundException;
import kieker.analysis.generic.graph.mtree.exceptions.NodeUnderCapacityException;
import kieker.analysis.generic.graph.mtree.exceptions.RootNodeReplacementException;
import kieker.analysis.generic.graph.mtree.exceptions.SplitNodeReplacementException;

public abstract class AbstractNode<T> extends IndexItem<T> {

	protected Map<T, IndexItem<T>> children = new HashMap<>();
	protected IRootness rootness;
	protected ILeafness<T> leafness;
	private final MTree<T> mtree;

	public <R extends AbstractNodeTrait<T> & IRootness, L extends AbstractNodeTrait<T> & ILeafness<T>> AbstractNode(final MTree<T> mtree, final T data,
			final R rootness, final L leafness) {
		super(data);

		rootness.thisNode = this;
		this.rootness = rootness;

		leafness.thisNode = this;
		this.leafness = leafness;

		this.mtree = mtree;
	}

	public Map<T, IndexItem<T>> getChildren() {
		return this.children;
	}

	public final void addData(final T data, final double distance) throws SplitNodeReplacementException, InternalErrorException {
		this.doAddData(data, distance);
		this.checkMaxCapacity();
	}

	@Override
	public int check() {
		super.check();
		this.checkMinCapacity();
		this.checkMaxCapacity2();

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

	protected void doRemoveData(final T data, final double distance) throws DataNotFoundException, NodeUnderCapacityException, InternalErrorException {
		this.leafness.doRemoveData(data, distance);
	}

	final void checkMaxCapacity() throws SplitNodeReplacementException, InternalErrorException {
		if (this.children.size() > this.mtree.getMaxNodeCapacity()) {
			final IDistanceFunction<? super T> cachedDistanceFunction = DistanceFunctionFactory.cached(this.mtree.getDistanceFunction());
			final SplitResult<T> splitResult = this.mtree.getSplitFunction().process(this.children.keySet(), cachedDistanceFunction);

			AbstractNode<T> newNode0 = null;
			AbstractNode<T> newNode1 = null;
			for (int i = 0; i < 2; ++i) {
				final T promotedData = splitResult.getPromoted().get(i);
				final Set<T> partition = splitResult.getPartitions().get(i);

				final AbstractNode<T> newNode = this.newSplitNodeReplacement(promotedData);
				for (final T data : partition) {
					final IndexItem<T> child = this.children.get(data);
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

			throw new SplitNodeReplacementException(newNode0, newNode1);
		}

	}

	protected AbstractNode<T> newSplitNodeReplacement(final T data) {
		return this.leafness.newSplitNodeReplacement(data);
	}

	public void addChild(final IndexItem<T> child, final double distance) throws InternalErrorException {
		this.leafness.addChild(child, distance);
	}

	public void removeData(final T data, final double distance)
			throws RootNodeReplacementException, NodeUnderCapacityException, DataNotFoundException, InternalErrorException {
		this.doRemoveData(data, distance);
		if (this.children.size() < this.getMinCapacity()) {
			throw new NodeUnderCapacityException();
		}
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

	private void checkMaxCapacity2() {
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
