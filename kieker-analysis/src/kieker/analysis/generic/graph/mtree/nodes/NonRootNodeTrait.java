package kieker.analysis.generic.graph.mtree.nodes;

import kieker.analysis.generic.graph.mtree.IRootness;

public class NonRootNodeTrait<T> extends AbstractNodeTrait<T> implements IRootness {

	public NonRootNodeTrait() {
		super();
	}

	@Override
	public int getMinCapacity() {
		return this.thisNode.getMTree().getMinNodeCapacity();
	}

	@Override
	public void checkMinCapacity() {
		assert this.thisNode.children.size() >= this.thisNode.getMTree().getMinNodeCapacity();
	}

	@Override
	public void checkDistanceToParent() {
		assert this.thisNode.getDistanceToParent() >= 0;
	}
};
