package kieker.analysis.generic.graph.mtree.nodes;

import kieker.analysis.exception.InternalErrorException;
import kieker.analysis.generic.graph.mtree.MTree;
import kieker.analysis.generic.graph.mtree.exceptions.DataNotFoundException;
import kieker.analysis.generic.graph.mtree.exceptions.NodeUnderCapacityException;
import kieker.analysis.generic.graph.mtree.exceptions.RootNodeReplacementException;

public final class RootLeafNode<T> extends AbstractNode<T> {

	public RootLeafNode(final MTree<T> mtree, final T data) {
		super(mtree, data, new RootNodeTrait<T>(), new LeafNodeTrait<T>());
	}

	@Override
	public void removeData(final T data, final double distance) throws RootNodeReplacementException, DataNotFoundException, InternalErrorException {
		try {
			super.removeData(data, distance);
		} catch (final NodeUnderCapacityException e) {
			assert this.children.isEmpty();
			throw new RootNodeReplacementException(null);
		}
	}

	@Override
	protected int getMinCapacity() {
		return 1;
	}

	@Override
	public void checkMinCapacity() {
		assert this.children.size() >= 1;
	}
}
