package kieker.analysis.generic.graph.mtree.nodes;

import kieker.analysis.generic.graph.mtree.MTree;

public final class InternalNode<T> extends AbstractNode<T> {

	public InternalNode(final MTree<T> mtree, final T data) {
		super(mtree, data, new NonRootNodeTrait<T>(), new NonLeafNodeTrait<T>());
	}
}
