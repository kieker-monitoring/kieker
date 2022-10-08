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
package kieker.analysis.behavior.clustering;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import kieker.analysis.behavior.mtree.MTree;

/**
 * An implementation of the OPTICS algorithm. A detailed explanation of the algorithm can be found
 * in the paper "OPTICS: ordering points to identify the clustering structure"
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class OPTICS {
	// This comparator, checks from which model the reachability distance is bigger.
	// It is used, to keep the priority queue in order
	private static Comparator<OpticsData> reachComparator = new Comparator<OpticsData>() {

		@Override
		public int compare(final OpticsData model1, final OpticsData model2) {
			return (int) (model1.getReachabilityDistance() - model2.getReachabilityDistance());
		}
	};

	private final int minPTs;
	private final double maxDistance;
	private final MTree<OpticsData> mtree;
	private final List<OpticsData> models;
	private final List<OpticsData> resultList = new ArrayList<>();

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
	public OPTICS(final MTree<OpticsData> mtree, final double maxDistance, final int minPTs,
			final List<OpticsData> models) {
		this.mtree = mtree;
		this.maxDistance = maxDistance;
		this.minPTs = minPTs;
		this.models = models;
	}

	private double reachabilityDistance(final OpticsData model1, final OpticsData model2) {
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
	private void updateCoreDistance(final OpticsData model) {

		final Iterator<MTree<OpticsData>.ResultItem> results = this.getMtree()
				.getNearest(model, this.getMaxDistance(), this.getMinPTs()).iterator();

		int resultAmount = 0;
		OpticsData last = null;

		while (results.hasNext()) {
			resultAmount++;
			last = results.next().data;
		}

		if (resultAmount < this.getMinPTs()) {
			model.setCoreDistance(OpticsData.UNDEFINED);
		} else {

			model.setCoreDistance(model.distanceTo(last));

		}

	}

	private List<OpticsData> getNeighbors(final OpticsData model) {
		final MTree<OpticsData>.Query query = this.mtree.getNearestByRange(model, this.maxDistance);
		final Iterator<MTree<OpticsData>.ResultItem> it = query.iterator();

		final List<OpticsData> neighbors = new ArrayList<>();

		while (it.hasNext()) {
			neighbors.add(it.next().data);
		}

		return neighbors;

	}

	/**
	 * This calculates the OPTICS result.
	 *
	 * @return An ordered list of the behavior models. The reachability distances of the models are
	 *         important for the evaluation.
	 */
	public List<OpticsData> calculate() {

		for (final OpticsData model : this.models) {

			if (!model.isVisited()) {

				this.expandClusterOrder(model);

			}
		}

		return this.resultList;
	}

	/**
	 * Updates the reachablity distances of all unvisited neighbors around one centermodel and and
	 * puts it in the priorityQueue (if it isn't already in it)
	 *
	 * @param neighbors
	 *            All neighbors of the center model
	 * @param centerModel
	 *            The model, from which the update is initialized
	 * @param seeds
	 *            The current Priority Queue
	 */
	private void update(final List<OpticsData> neighbors, final OpticsData centerModel,
			final PriorityQueue<OpticsData> seeds) {

		for (final OpticsData model : neighbors) {
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
	private void expandClusterOrder(final OpticsData model1) {
		final List<OpticsData> neighbors1 = this.getNeighbors(model1);

		model1.setVisited(true);
		model1.setReachabilityDistance(OpticsData.UNDEFINED);
		this.updateCoreDistance(model1);
		this.resultList.add(model1);

		if (model1.getCoreDistance() != OpticsData.UNDEFINED) {
			final PriorityQueue<OpticsData> seeds = new PriorityQueue<>(5, OPTICS.reachComparator);

			this.update(neighbors1, model1, seeds);

			while (!seeds.isEmpty()) {

				final OpticsData model2 = seeds.poll();
				// TODO better naming
				final List<OpticsData> neighbors2 = this.getNeighbors(model2);

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

	public MTree<OpticsData> getMtree() {
		return this.mtree;
	}

}
