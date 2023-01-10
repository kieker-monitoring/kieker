package kieker.analysis.generic.graph.mtree;

import kieker.analysis.exception.InternalErrorException;
import kieker.analysis.generic.graph.mtree.exceptions.DataNotFoundException;
import kieker.analysis.generic.graph.mtree.exceptions.NodeUnderCapacityException;
import kieker.analysis.generic.graph.mtree.nodes.AbstractNode;
import kieker.analysis.generic.graph.mtree.nodes.IndexItem;

public interface ILeafness<DATA> {
	void doAddData(DATA data, double distance) throws InternalErrorException;

	void addChild(IndexItem<DATA> child, double distance) throws InternalErrorException;

	void doRemoveData(DATA data, double distance) throws DataNotFoundException, NodeUnderCapacityException, InternalErrorException;

	AbstractNode<DATA> newSplitNodeReplacement(DATA data);

	void checkChildClass(IndexItem<DATA> child);
}
