package kieker.analysis.generic.graph.mtree.nodes;

import kieker.analysis.generic.graph.mtree.ILeafness;
import kieker.analysis.generic.graph.mtree.exceptions.DataNotFoundException;

public class LeafNodeTrait<T> extends AbstractNodeTrait<T> implements ILeafness<T> {

	public LeafNodeTrait() {
		// default constructor
	}

	@Override
	public void doAddData(final T data, final double distance) {
		final Entry<T> entry = new Entry<T>(data);
		assert !this.thisNode.children.containsKey(data);
		this.thisNode.children.put(data, entry);
		assert this.thisNode.children.containsKey(data);
		this.thisNode.updateMetrics(entry, distance);
	}

	@Override
	public void addChild(final IndexItem<T> child, final double distance) {
		assert !this.thisNode.children.containsKey(child.getData());
		this.thisNode.children.put(child.getData(), child);
		assert this.thisNode.children.containsKey(child.getData());
		this.thisNode.updateMetrics(child, distance);
	}

	@Override
	public AbstractNode<T> newSplitNodeReplacement(final T data) {
		return new LeafNode<T>(this.thisNode.getMTree(), data);
	}

	@Override
	public void doRemoveData(final T data, final double distance) throws DataNotFoundException {
		if (this.thisNode.children.remove(data) == null) {
			throw new DataNotFoundException();
		}
	}

	@Override
	public void checkChildClass(final IndexItem<T> child) {
		assert child instanceof Entry;
	}
}
