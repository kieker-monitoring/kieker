/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package kieker.analysis.generic.graph.mtree.nodes;

import java.util.ArrayDeque;
import java.util.Deque;

import kieker.analysis.exception.InternalErrorException;
import kieker.analysis.generic.graph.mtree.ILeafness;
import kieker.analysis.generic.graph.mtree.utils.Pair;

/**
 * @author Eduardo R. D'Avila
 * @since 2.0.0
 */
public class NonLeafNodeTrait<T> extends AbstractNodeTrait<T> implements ILeafness<T> {

	public NonLeafNodeTrait(final AbstractNode<T> thisNode) {
		super(thisNode);
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
		}

		CandidateChild minRadiusIncreaseNeeded = new CandidateChild(null, -1.0, Double.POSITIVE_INFINITY);
		CandidateChild nearestDistance = new CandidateChild(null, -1.0, Double.POSITIVE_INFINITY);

		for (final IndexItem<T> item : this.thisNode.getChildren().values()) {
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

		child.addData(data, chosen.distance);
		if (child.isMaxCapacityExceeded()) {
			final Pair<AbstractNode<T>> newNodes = child.splitNodes();
			// Replace current child with new nodes
			final IndexItem<T> itemIndex = this.thisNode.getChildren().remove(child.getData());
			assert itemIndex != null;

			this.computeDistances2(newNodes.getFirst());
			this.computeDistances2(newNodes.getSecond());
		} else {
			this.thisNode.updateRadius(child);
		}

	}

	private void computeDistances2(final AbstractNode<T> node) throws InternalErrorException {
		final double newDistance = this.thisNode.getMTree().getDistanceFunction().calculate(this.thisNode.getData(), node.getData());
		this.thisNode.addChild(node, newDistance);
	}

	@Override
	public void addChild(final IndexItem<T> inputNewChildNode, final double inputDistance) throws InternalErrorException {
		double distance = inputDistance;
		AbstractNode<T> newChildNode = (AbstractNode<T>) inputNewChildNode;

		final Deque<ChildWithDistance> newChildren = new ArrayDeque<>();
		newChildren.addFirst(new ChildWithDistance(newChildNode, distance));

		while (!newChildren.isEmpty()) {
			final ChildWithDistance cwd = newChildren.removeFirst();

			newChildNode = cwd.child;
			distance = cwd.distance;
			if (this.thisNode.getChildren().containsKey(newChildNode.getData())) {
				final AbstractNode<T> existingChild = (AbstractNode<T>) this.thisNode.getChildren().get(newChildNode.getData());
				assert existingChild.getData().equals(newChildNode.getData());

				// Transfer the _children_ of the newChild to the existingChild
				for (final IndexItem<T> grandchild : newChildNode.getChildren().values()) {
					existingChild.addChild(grandchild, grandchild.getDistanceToParent());
				}
				newChildNode.getChildren().clear();

				if (existingChild.isMaxCapacityExceeded()) {
					final Pair<AbstractNode<T>> newNodes = existingChild.splitNodes();

					final IndexItem<T> indexItem = this.thisNode.getChildren().remove(existingChild.getData());
					assert indexItem != null;

					this.computeDistances(newNodes.getFirst(), newChildren);
					this.computeDistances(newNodes.getSecond(), newChildren);
				}
			} else {
				this.thisNode.getChildren().put(newChildNode.getData(), newChildNode);
				this.thisNode.updateMetrics(newChildNode, distance);
			}
		}
	}

	private void computeDistances(final AbstractNode<T> node, final Deque<ChildWithDistance> newChildren) {
		final double newDistance = this.thisNode.getMTree().getDistanceFunction().calculate(this.thisNode.getData(),
				node.getData());
		newChildren.addFirst(new ChildWithDistance(node, newDistance));
	}

	@Override
	public AbstractNode<T> newSplitNodeReplacement(final T data) {
		return NodeFactory.createInternalNode(this.thisNode.getMTree(), data);
	}

	@Override
	public boolean doRemoveData(final T data, final double distance) throws InternalErrorException {
		for (final IndexItem<T> childItem : this.thisNode.getChildren().values()) {
			final AbstractNode<T> child = (AbstractNode<T>) childItem;
			if (Math.abs(distance - child.getDistanceToParent()) <= child.radius) {
				final double distanceToChild = this.thisNode.getMTree().getDistanceFunction().calculate(data, child.getData());
				if (distanceToChild <= child.radius) {
					final boolean dataRemoved = child.removeData(data, distanceToChild);
					if (dataRemoved) {
						if (child.isNodeUnderCapacity()) {
							final AbstractNode<T> expandedChild = this.balanceChildren(child);
							this.thisNode.updateRadius(expandedChild);
						} else {
							this.thisNode.updateRadius(child);
						}
						return true;
					}
				}
			}
		}

		return false;
	}

	private AbstractNode<T> balanceChildren(final AbstractNode<T> theChild) throws InternalErrorException {
		// Tries to find anotherChild which can donate a grand-child to theChild.

		AbstractNode<T> nearestDonor = null;
		double distanceNearestDonor = Double.POSITIVE_INFINITY;

		AbstractNode<T> nearestMergeCandidate = null;
		double distanceNearestMergeCandidate = Double.POSITIVE_INFINITY;

		for (final IndexItem<T> child : this.thisNode.getChildren().values()) {
			final AbstractNode<T> anotherChild = (AbstractNode<T>) child;
			if (anotherChild == theChild) {
				continue;
			}

			final double distance = this.thisNode.getMTree().getDistanceFunction().calculate(theChild.getData(), anotherChild.getData());
			if (anotherChild.getChildren().size() > anotherChild.getMinCapacity()) {
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
			for (final IndexItem<T> grandchild : theChild.getChildren().values()) {
				final double distance = this.thisNode.getMTree().getDistanceFunction().calculate(grandchild.getData(),
						nearestMergeCandidate.getData());
				nearestMergeCandidate.addChild(grandchild, distance);
			}

			final IndexItem<T> removed = this.thisNode.getChildren().remove(theChild.getData());
			assert removed != null;
			return nearestMergeCandidate;
		} else {
			// Donate
			// Look for the nearest grandchild
			IndexItem<T> nearestGrandchild = null;
			double nearestGrandchildDistance = Double.POSITIVE_INFINITY;
			for (final IndexItem<T> grandchild : nearestDonor.getChildren().values()) {
				final double distance = this.thisNode.getMTree().getDistanceFunction().calculate(grandchild.getData(), theChild.getData());
				if (distance < nearestGrandchildDistance) {
					nearestGrandchildDistance = distance;
					nearestGrandchild = grandchild;
				}
			}

			final IndexItem<T> indexItem = nearestDonor.getChildren().remove(nearestGrandchild.getData());
			assert indexItem != null;
			theChild.addChild(nearestGrandchild, nearestGrandchildDistance);
			return theChild;
		}
	}

	@Override
	public void checkChildClass(final IndexItem<T> child) {
		assert (child instanceof InternalNode) || (child instanceof LeafNode);
	}

	final class ChildWithDistance {
		private final AbstractNode<T> child;
		private final double distance;

		private ChildWithDistance(final AbstractNode<T> child, final double distance) {
			this.child = child;
			this.distance = distance;
		}
	}
}
