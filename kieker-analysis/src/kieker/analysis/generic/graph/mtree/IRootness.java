package kieker.analysis.generic.graph.mtree;

import kieker.analysis.exception.InternalErrorException;

public interface IRootness {
	int getMinCapacity() throws InternalErrorException;

	void checkDistanceToParent();

	void checkMinCapacity();
}
