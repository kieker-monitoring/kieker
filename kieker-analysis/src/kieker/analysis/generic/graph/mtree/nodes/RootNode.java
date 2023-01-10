package kieker.analysis.generic.graph.mtree.nodes;

import kieker.analysis.exception.InternalErrorException;
import kieker.analysis.generic.graph.mtree.MTree;
import kieker.analysis.generic.graph.mtree.exceptions.DataNotFoundException;
import kieker.analysis.generic.graph.mtree.exceptions.NodeUnderCapacityException;
import kieker.analysis.generic.graph.mtree.exceptions.RootNodeReplacementException;

public final class RootNode<T> extends AbstractNode<T> {

	public RootNode(final MTree<T> mtree, final T data) {
		super(mtree, data, new RootNodeTrait<T>(), new NonLeafNodeTrait<T>());
	}

	@Override
	public void removeData(final T data, final double distance)
			throws RootNodeReplacementException, NodeUnderCapacityException, DataNotFoundException, InternalErrorException {
		try {
			super.removeData(data, distance);
		} catch (final NodeUnderCapacityException e) {
			// Promote the only child to root
			final AbstractNode<T> theChild = (AbstractNode<T>) this.children.values().iterator().next();
			final AbstractNode<T> newRoot;
			if (theChild instanceof InternalNode<?>) {
				newRoot = new RootNode<T>(this.getMTree(), theChild.getData());
			} else {
				assert theChild instanceof LeafNode<?>;
				newRoot = new RootLeafNode<T>(this.getMTree(), theChild.getData());
			}

			for (final IndexItem<T> grandchild : theChild.children.values()) {
				final double newDistance = this.getMTree().getDistanceFunction().calculate(newRoot.getData(), grandchild.getData());
				newRoot.addChild(grandchild, newDistance);
			}
			theChild.children.clear();

			throw new RootNodeReplacementException(newRoot);
		}
	}

	@Override
	protected int getMinCapacity() {
		return 2;
	}

	@Override
	public void checkMinCapacity() {
		assert this.children.size() >= 2;
	}
}
