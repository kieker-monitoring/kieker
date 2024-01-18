/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package kieker.analysis.generic.clustering.optics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import kieker.analysis.generic.clustering.mtree.MTree;
import kieker.analysis.generic.clustering.mtree.query.Query;
import kieker.analysis.generic.clustering.mtree.query.ResultItem;

/**
 * An implementation of the OPTICS algorithm. A detailed explanation of the algorithm can be found
 * in the paper "OPTICS: ordering points to identify the clustering structure".
 *
 * @param <T>
 *            model type
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class OPTICS<T> {
	// This comparator, checks from which model the reachability distance is bigger.
	// It is used, to keep the priority queue in order
	private final Comparator<OpticsData<T>> reachComparator = new Comparator<>() {

		@Override
		public int compare(final OpticsData<T> model1, final OpticsData<T> model2) {
			return (int) (model1.getReachabilityDistance() - model2.getReachabilityDistance());
		}
	};

	private final int minPTs;
	private final double maxDistance;
	private final MTree<OpticsData<T>> mtree;
	private final List<OpticsData<T>> models;
	private final List<OpticsData<T>> resultList = new ArrayList<>();

	/**
	 *
	 * @param mtree
	 *            The M-Tree with the behavior models to be clustered
	 * @param maxDistance
	 *            The maximal distance two neighbors can have (the epsilon value of the algorithm)
	 * @param minPTs
	 *            The minimal amount of neighbors a object must have to be called a core-object
	 * @param models
	 *            A list of all models to be clustered. They have to be the same as the models in
	 *            the M-Tree
	 */
	public OPTICS(final MTree<OpticsData<T>> mtree, final double maxDistance, final int minPTs,
			final List<OpticsData<T>> models) {
		this.mtree = mtree;
		this.maxDistance = maxDistance;
		this.minPTs = minPTs;
		this.models = models;
	}

	private double reachabilityDistance(final OpticsData<T> model1, final OpticsData<T> model2) {
		final double coreDistance = model1.getCoreDistance();
		if (coreDistance == OpticsData.UNDEFINED) {
			return OpticsData.UNDEFINED;
		}
		final double distance = model1.distanceTo(model2);
		return Math.max(distance, coreDistance);
	}

	/**
	 * Updates the core distance of a model. The core-distance is the epsilon value (radius) an
	 * object must have, so it has minPts neighbors. If the result is larger than maxDistance the
	 * core distance is UNDEFINED.
	 *
	 * @param model
	 *            The model, of which the core distance should be updated.
	 */
	private void updateCoreDistance(final OpticsData<T> model) {

		int resultAmount = 0;
		OpticsData<T> last = null;

		final Query<OpticsData<T>> v = this.getMtree()
				.getNearest(model, this.getMaxDistance(), this.getMinPTs());

		for (final ResultItem<OpticsData<T>> element : v) {
			resultAmount++;
			last = element.getData();
		}

		if (resultAmount < this.getMinPTs()) {
			model.setCoreDistance(OpticsData.UNDEFINED);
		} else {
			model.setCoreDistance(model.distanceTo(last));
		}

	}

	private List<OpticsData<T>> getNeighbors(final OpticsData<T> model) {
		final Query<OpticsData<T>> query = this.mtree.getNearestByRange(model, this.maxDistance);
		final List<OpticsData<T>> neighbors = new ArrayList<>();

		for (final ResultItem<OpticsData<T>> element : query) {
			neighbors.add(element.getData());
		}

		return neighbors;

	}

	/**
	 * This calculates the OPTICS result.
	 *
	 * @return An ordered list of the behavior models. The reachability distances of the models are
	 *         important for the evaluation.
	 */
	public List<OpticsData<T>> calculate() {
		for (final OpticsData<T> model : this.models) {
			if (!model.isVisited()) {
				this.expandClusterOrder(model);
			}
		}

		return this.resultList;
	}

	/**
	 * Updates the reachablity distances of all unvisited neighbors around one centermodel and and
	 * puts it in the priorityQueue (if it isn't already in it).
	 *
	 * @param neighbors
	 *            All neighbors of the center model
	 * @param centerModel
	 *            The model, from which the update is initialized
	 * @param seeds
	 *            The current Priority Queue
	 */
	private void update(final List<OpticsData<T>> neighbors, final OpticsData<T> centerModel,
			final PriorityQueue<OpticsData<T>> seeds) {

		for (final OpticsData<T> model : neighbors) {
			if (!model.isVisited()) {

				final double newReachDistance = this.reachabilityDistance(centerModel, model);

				if (model.getReachabilityDistance() == OpticsData.UNDEFINED) {
					model.setReachabilityDistance(newReachDistance);
					seeds.add(model);
				} else {
					if (newReachDistance < model.getReachabilityDistance()) {
						model.setReachabilityDistance(newReachDistance);

						// Update the position of the model in priority queue. This can be done by
						// removing and adding it back in
						seeds.remove(model);
						seeds.add(model);
					}
				}

			}
		}
	}

	/**
	 * Expands the cluster order by adding the next model together with close neighbors to the
	 * result.
	 *
	 * @param model1
	 *            An unvisited behavior model.
	 */
	private void expandClusterOrder(final OpticsData<T> model1) {
		final List<OpticsData<T>> neighbors1 = this.getNeighbors(model1);

		model1.setVisited(true);
		model1.setReachabilityDistance(OpticsData.UNDEFINED);
		this.updateCoreDistance(model1);
		this.resultList.add(model1);

		if (model1.getCoreDistance() != OpticsData.UNDEFINED) {
			final PriorityQueue<OpticsData<T>> seeds = new PriorityQueue<>(5, this.reachComparator);
			this.update(neighbors1, model1, seeds);
			while (!seeds.isEmpty()) {

				final OpticsData<T> model2 = seeds.poll();
				final List<OpticsData<T>> neighbors2 = this.getNeighbors(model2);
				this.updateCoreDistance(model2);

				model2.setVisited(true);
				this.resultList.add(model2);
				if (model2.getCoreDistance() != OpticsData.UNDEFINED) {
					this.update(neighbors2, model2, seeds);
				}
			}

		}
	}

	public int getMinPTs() {
		return this.minPTs;
	}

	public double getMaxDistance() {
		return this.maxDistance;
	}

	public MTree<OpticsData<T>> getMtree() {
		return this.mtree;
	}

}
