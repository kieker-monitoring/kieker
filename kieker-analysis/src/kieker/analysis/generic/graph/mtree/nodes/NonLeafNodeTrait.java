package kieker.analysis.generic.graph.mtree.nodes;

import java.util.ArrayDeque;
import java.util.Deque;

import kieker.analysis.exception.InternalErrorException;
import kieker.analysis.generic.graph.mtree.ILeafness;
import kieker.analysis.generic.graph.mtree.exceptions.DataNotFoundException;
import kieker.analysis.generic.graph.mtree.exceptions.NodeUnderCapacityException;
import kieker.analysis.generic.graph.mtree.exceptions.RootNodeReplacementException;
import kieker.analysis.generic.graph.mtree.exceptions.SplitNodeReplacementException;

public class NonLeafNodeTrait<T> extends AbstractNodeTrait<T> implements ILeafness<T> {

	public NonLeafNodeTrait() {
		// nothing to be done
	}

	@Override
	public void doAddData(final T data, final double distance) throws InternalErrorException {

		final class CandidateChild {
			private final AbstractNode<T> node;
			private final double distance;
			private final double metric;

			private CandidateChild(final AbstractNode<T> node, final double distance, final double metric) {
				this.node = node;
				this.distance = distance;
				this.metric = metric;
			}

			public AbstractNode<T> getNode() {
				return this.node;
			}

			public double getDistance() {
				return this.distance;
			}

			public double getMetric() {
				return this.metric;
			}
		}

		CandidateChild minRadiusIncreaseNeeded = new CandidateChild(null, -1.0, Double.POSITIVE_INFINITY);
		CandidateChild nearestDistance = new CandidateChild(null, -1.0, Double.POSITIVE_INFINITY);

		for (final IndexItem<T> item : this.thisNode.children.values()) {
			final AbstractNode<T> child = (AbstractNode<T>) item;
			final double childDistance = this.thisNode.getMTree().getDistanceFunction().calculate(child.getData(), data);
			if (childDistance > child.radius) {
				final double radiusIncrease = childDistance - child.radius;
				if (radiusIncrease < minRadiusIncreaseNeeded.metric) {
					minRadiusIncreaseNeeded = new CandidateChild(child, childDistance, radiusIncrease);
				}
			} else {
				if (childDistance < nearestDistance.metric) {
					nearestDistance = new CandidateChild(child, childDistance, childDistance);
				}
			}
		}

		final CandidateChild chosen = nearestDistance.node != null ? nearestDistance : minRadiusIncreaseNeeded; // NOCS

		final AbstractNode<T> child = chosen.node;
		try {
			child.addData(data, chosen.distance);
			this.thisNode.updateRadius(child);
		} catch (final SplitNodeReplacementException e) {
			// Replace current child with new nodes
			final IndexItem<T> itemIndex = this.thisNode.children.remove(child.getData());
			assert itemIndex != null;

			for (final Object newNode : e.getNewNodes()) {
				@SuppressWarnings("unchecked")
				final AbstractNode<T> newChild = (AbstractNode<T>) newNode;
				final double newDistance = this.thisNode.getMTree().getDistanceFunction().calculate(this.thisNode.getData(), newChild.getData());
				this.thisNode.addChild(newChild, newDistance);
			}
		}
	}

	@Override
	public void addChild(final IndexItem<T> inputNewChild, final double inputDistance) throws InternalErrorException {
		double distance = inputDistance;
		AbstractNode<T> newChild = (AbstractNode<T>) inputNewChild;

		final class ChildWithDistance {
			private final AbstractNode<T> child;
			private final double distance;

			private ChildWithDistance(final AbstractNode<T> child, final double distance) {
				this.child = child;
				this.distance = distance;
			}

			public AbstractNode<T> getChild() {
				return this.child;
			}

			public double getDistance() {
				return this.distance;
			}
		}

		final Deque<ChildWithDistance> newChildren = new ArrayDeque<>();
		newChildren.addFirst(new ChildWithDistance(newChild, distance));

		while (!newChildren.isEmpty()) {
			final ChildWithDistance cwd = newChildren.removeFirst();

			newChild = cwd.child;
			distance = cwd.distance;
			if (this.thisNode.children.containsKey(newChild.getData())) {
				final AbstractNode<T> existingChild = (AbstractNode<T>) this.thisNode.children.get(newChild.getData());
				assert existingChild.getData().equals(newChild.getData());

				// Transfer the _children_ of the newChild to the existingChild
				for (final IndexItem<T> grandchild : newChild.children.values()) {
					existingChild.addChild(grandchild, grandchild.getDistanceToParent());
				}
				newChild.children.clear();

				try {
					existingChild.checkMaxCapacity();
				} catch (final SplitNodeReplacementException e) {
					final IndexItem<T> indexItem = this.thisNode.children.remove(existingChild.getData());
					assert indexItem != null;

					for (final Object newNode2 : e.getNewNodes()) {
						@SuppressWarnings("unchecked")
						final AbstractNode<T> newNode = (AbstractNode<T>) newNode2;
						final double newDistance = this.thisNode.getMTree().getDistanceFunction().calculate(this.thisNode.getData(),
								newNode.getData());
						newChildren.addFirst(new ChildWithDistance(newNode, newDistance));
					}
				}
			} else {
				this.thisNode.children.put(newChild.getData(), newChild);
				this.thisNode.updateMetrics(newChild, distance);
			}
		}
	}

	@Override
	public AbstractNode<T> newSplitNodeReplacement(final T data) {
		return new InternalNode<T>(this.thisNode.getMTree(), data);
	}

	@Override
	public void doRemoveData(final T data, final double distance) throws DataNotFoundException,
			NodeUnderCapacityException, InternalErrorException {
		for (final IndexItem<T> childItem : this.thisNode.children.values()) {
			final AbstractNode<T> child = (AbstractNode<T>) childItem;
			if (Math.abs(distance - child.getDistanceToParent()) <= child.radius) {
				final double distanceToChild = this.thisNode.getMTree().getDistanceFunction().calculate(data, child.getData());
				if (distanceToChild <= child.radius) {
					try {
						child.removeData(data, distanceToChild);
						this.thisNode.updateRadius(child);
						return;
					} catch (final DataNotFoundException e) {
						// If DataNotFound was thrown, then the data was not found in the child
					} catch (final NodeUnderCapacityException e) {
						final AbstractNode<T> expandedChild = this.balanceChildren(child);
						this.thisNode.updateRadius(expandedChild);
						return;
					} catch (final RootNodeReplacementException e) {
						throw new InternalErrorException("Should never happen!");
					}
				}
			}
		}

		throw new DataNotFoundException();
	}

	private AbstractNode<T> balanceChildren(final AbstractNode<T> theChild) throws InternalErrorException {
		// Tries to find anotherChild which can donate a grand-child to theChild.

		AbstractNode<T> nearestDonor = null;
		double distanceNearestDonor = Double.POSITIVE_INFINITY;

		AbstractNode<T> nearestMergeCandidate = null;
		double distanceNearestMergeCandidate = Double.POSITIVE_INFINITY;

		for (final IndexItem<T> child : this.thisNode.children.values()) {
			final AbstractNode<T> anotherChild = (AbstractNode<T>) child;
			if (anotherChild == theChild) {
				continue;
			}

			final double distance = this.thisNode.getMTree().getDistanceFunction().calculate(theChild.getData(), anotherChild.getData());
			if (anotherChild.children.size() > anotherChild.getMinCapacity()) {
				if (distance < distanceNearestDonor) {
					distanceNearestDonor = distance;
					nearestDonor = anotherChild;
				}
			} else {
				if (distance < distanceNearestMergeCandidate) {
					distanceNearestMergeCandidate = distance;
					nearestMergeCandidate = anotherChild;
				}
			}
		}

		if (nearestDonor == null) {
			// Merge
			for (final IndexItem<T> grandchild : theChild.children.values()) {
				final double distance = this.thisNode.getMTree().getDistanceFunction().calculate(grandchild.getData(),
						nearestMergeCandidate.getData());
				nearestMergeCandidate.addChild(grandchild, distance);
			}

			final IndexItem<T> removed = this.thisNode.children.remove(theChild.getData());
			assert removed != null;
			return nearestMergeCandidate;
		} else {
			// Donate
			// Look for the nearest grandchild
			IndexItem<T> nearestGrandchild = null;
			double nearestGrandchildDistance = Double.POSITIVE_INFINITY;
			for (final IndexItem<T> grandchild : nearestDonor.children.values()) {
				final double distance = this.thisNode.getMTree().getDistanceFunction().calculate(grandchild.getData(), theChild.getData());
				if (distance < nearestGrandchildDistance) {
					nearestGrandchildDistance = distance;
					nearestGrandchild = grandchild;
				}
			}

			final IndexItem<T> indexItem = nearestDonor.children.remove(nearestGrandchild.getData());
			assert indexItem != null;
			theChild.addChild(nearestGrandchild, nearestGrandchildDistance);
			return theChild;
		}
	}

	@Override
	public void checkChildClass(final IndexItem<T> child) {
		assert (child instanceof InternalNode) || (child instanceof LeafNode);
	}
}
