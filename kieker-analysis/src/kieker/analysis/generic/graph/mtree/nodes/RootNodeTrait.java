package kieker.analysis.generic.graph.mtree.nodes;

import kieker.analysis.exception.InternalErrorException;
import kieker.analysis.generic.graph.mtree.IRootness;

public class RootNodeTrait<T> extends AbstractNodeTrait<T> implements IRootness {

	public RootNodeTrait() {
		// default constructor
	}

	@Override
	public int getMinCapacity() throws InternalErrorException {
		throw new InternalErrorException("Should not be called!");
	}

	@Override
	public void checkDistanceToParent() {
		assert this.thisNode.getDistanceToParent() == -1;
	}

	@Override
	public void checkMinCapacity() {
		this.thisNode.checkMinCapacity();
	}
}
