package kieker.analysis.generic.graph.mtree.nodes;

import kieker.analysis.generic.graph.mtree.MTree;

public class LeafNode<T> extends AbstractNode<T> {

	public LeafNode(final MTree<T> mtree, final T data) {
		super(mtree, data, new NonRootNodeTrait<T>(), new LeafNodeTrait<T>());
	}
}
