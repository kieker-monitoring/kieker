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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import kieker.analysis.behavior.data.EntryCallEvent;
import kieker.analysis.behavior.model.BehaviorModel;
import kieker.analysis.behavior.model.Edge;
import kieker.analysis.behavior.model.EventGroup;
import kieker.analysis.behavior.model.Node;
import kieker.analysis.behavior.mtree.IDistanceFunction;

/**
 * This class calculates a custom graph edit distance between two Behavior Models.
 *
 * The following operations are allowed:
 *
 * insert/delete nodes; insert/delete edges; insert/delete EventGroups; insert/delete
 * Events; duplicate/remove duplicate events
 *
 * Insertion and Deletion always costs the same to satisfy the symmetry property.
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class GraphEditDistance implements IDistanceFunction<BehaviorModel> {

	// The operation costs. They can be changed by the "setConfiguration" function
	private final double nodeInsertCost;
	private final double edgeInsertCost;
	private final double eventGroupInsertCost;

	/**
	 * the WEIGTHING assigns events a insertion and duplication costs.
	 */
	private final IParameterWeighting weighting;

	// the models to be compared
	private BehaviorModel model1;
	private BehaviorModel model2;

	/**
	 * This can be used, to set the node, edge and eventgroup insertion cost for the Graph Edit
	 * Distance Algorithm to the values in the configuration.
	 */
	public GraphEditDistance(final double nodeInsertCost, final double edgeInsertCost, final double eventGroupInsertCost, final IParameterWeighting weighting) {
		this.nodeInsertCost = nodeInsertCost;
		this.edgeInsertCost = edgeInsertCost;
		this.eventGroupInsertCost = eventGroupInsertCost;
		this.weighting = weighting;
	}

	public GraphEditDistance() {
		this.weighting = new NaiveParameterWeighting();
		this.nodeInsertCost = 30;
		this.edgeInsertCost = 15;
		this.eventGroupInsertCost = 10;
	}

	/**
	 * Calculates the Graph Edit Distance between two objects
	 *
	 * @param modelA
	 *            The first model.
	 * @param modelB
	 *            The second model.
	 */
	@Override
	public double calculate(final BehaviorModel modelA, final BehaviorModel modelB) {
		this.model1 = modelA;
		this.model2 = modelB;

		double distance = 0;

		// check if nodes from model1 are in model2
		for (final Map.Entry<String, Node> pair : this.model1.getNodes().entrySet()) {
			final String signature = pair.getKey();
			final Node node = pair.getValue();

			final Node match = this.model2.getNodes().get(signature);

			if (match == null) { // node only occurs in one objects => must be inserted
				distance += this.nodeInsertionCost(node);
			} else { // node occurs in both objects => must be compared
				distance += this.nodeDistance(node, match);
			}
		}

		// check if nodes from model2 are in model1
		for (final Map.Entry<String, Node> pair : this.model2.getNodes().entrySet()) {
			final String signature = pair.getKey();
			final Node node = pair.getValue();

			final Node match = this.model1.getNodes().get(signature);

			// node only occurs in one objects => must be inserted
			if (match == null) {
				distance += this.nodeInsertionCost(node);
			}
		}
		return distance;
	}

	/**
	 * Calculates the distance between two nodes. This includes the distance between the ingoing
	 * edges
	 */
	private double nodeDistance(final Node node1, final Node node2) {
		double distance = 0;
		for (final Edge edge : node1.getIngoingEdges().values()) {
			final Node source1 = edge.getSource();
			final Node source2 = this.model2.getNodes().get(source1.getName());

			final Edge match = node2.getIngoingEdges().get(source2);

			if (match == null) { // edge only occurs in one node => must be inserted
				distance += this.edgeInsertionCost(edge);
			} else { // edge occurs in both nodes => must be compared
				distance += this.edgeDistance(edge, match);
			}
		}
		for (final Edge edge : node2.getIngoingEdges().values()) {
			final Node source2 = edge.getSource();
			final Node source1 = this.model1.getNodes().get(source2.getName());
			final Edge match = node1.getIngoingEdges().get(source1);

			// edge only occurs in one node => must be inserted
			if (match == null) {
				distance += this.edgeInsertionCost(edge);
			}
		}
		return distance;
	}

	/**
	 * Calculates the distance between two edges. This includes the distance between the contained
	 * eventgroups
	 *
	 */
	private double edgeDistance(final Edge edge1, final Edge edge2) {
		double distance = 0;

		for (final EventGroup eventGroup1 : edge1.getEventGroups()) {
			boolean success = false;
			for (final EventGroup eventGroup2 : edge2.getEventGroups()) {
				if (eventGroup1.hasSameParameters(eventGroup2)) {
					// matching eventGroup found => must be compared
					distance += this.eventGroupDistance(eventGroup1, eventGroup2);
					success = true;
					break;
				}
			}
			// no matching eventGroup found => must be inserted
			if (!success) {
				distance += this.eventGroupInsertionCost(eventGroup1);
			}
		}

		for (final EventGroup eventGroup2 : edge2.getEventGroups()) {
			boolean success = false;
			for (final EventGroup eventGroup1 : edge1.getEventGroups()) {
				if (eventGroup1.hasSameParameters(eventGroup2)) {
					success = true;
					break;
				}
			}
			// if not necessary, as success should always be false
			if (!success) {
				// no matching eventGroup found => must be inserted
				distance += this.eventGroupInsertionCost(eventGroup2);
			}
		}

		return distance;
	}

	/**
	 * Calculates the distance between two event groups. This includes the distance between the
	 * contained events
	 *
	 */
	private double eventGroupDistance(final EventGroup eventGroup1, final EventGroup eventGroup2) {
		double distance = 0;

		// at first all events are unvisited
		final Queue<EntryCallEvent> unvisitedEvents1 = new LinkedList<>(eventGroup1.getEvents());
		final Queue<EntryCallEvent> unvisitedEvents2 = new LinkedList<>(eventGroup2.getEvents());

		while (!unvisitedEvents1.isEmpty()) {

			final List<EntryCallEvent> matches1 = new ArrayList<>();
			final List<EntryCallEvent> matches2 = new ArrayList<>();

			final EntryCallEvent event = unvisitedEvents1.poll();
			matches1.add(event);

			// find equal events in same event group
			for (final EntryCallEvent potentialMatch : unvisitedEvents1) {

				if (GraphEditDistance.haveSameValues(event, potentialMatch)) {

					matches1.add(potentialMatch);
				}
			}
			unvisitedEvents1.removeAll(matches1);

			// find equal events in other event group
			for (final EntryCallEvent potentialMatch : unvisitedEvents2) {
				if (GraphEditDistance.haveSameValues(event, potentialMatch)) {
					matches2.add(potentialMatch);
				}
			}
			unvisitedEvents2.removeAll(matches2);

			// events have to be created in other eventgroup
			if (matches2.isEmpty()) {
				distance += this.weighting.getInsertCost(event.getParameters());
				distance += this.weighting.getDuplicateCost(event.getParameters()) * (matches1.size() - 1);
			} else { // event occurs in both groups, so it has to be duplicated, till amount is the
						// same
				final int amountDifference = Math.abs(matches1.size() - matches2.size());
				distance += this.weighting.getDuplicateCost(event.getParameters()) * amountDifference;
			}

		}

		// maybe some events are left in the second group, which weren't in the first group
		while (!unvisitedEvents2.isEmpty()) {

			final EntryCallEvent event1 = unvisitedEvents2.poll();

			final List<EntryCallEvent> equalEvents = new ArrayList<>();

			for (final EntryCallEvent event2 : unvisitedEvents2) {
				if (GraphEditDistance.haveSameValues(event1, event2)) {

					equalEvents.add(event2);
				}
			}
			unvisitedEvents2.removeAll(equalEvents);

			distance += equalEvents.size() * this.weighting.getDuplicateCost(event1.getParameters());
			distance += this.weighting.getInsertCost(event1.getParameters());

		}
		return distance;
	}

	/**
	 * calculates the insertion cost of a node including the insertion cost of the ingoing edges
	 */
	private double nodeInsertionCost(final Node node) {
		double distance = this.nodeInsertCost;

		for (final Edge edge : node.getIngoingEdges().values()) {
			distance += this.edgeInsertionCost(edge);
		}
		return distance;
	}

	/**
	 * calculates the insertion cost of an edge including the insertion cost of the contained
	 * eventgroups
	 */
	private double edgeInsertionCost(final Edge edge) {
		double distance = this.edgeInsertCost;
		for (final EventGroup eventGroup : edge.getEventGroups()) {
			distance += this.eventGroupInsertionCost(eventGroup);
		}
		return distance;
	}

	/**
	 * calculates the insertion cost of an eventgroup including the insertion cost of the events
	 */
	private double eventGroupInsertionCost(final EventGroup eventGroup) {
		double distance = this.eventGroupInsertCost;

		final Queue<EntryCallEvent> queue = new LinkedList<>(eventGroup.getEvents());

		final List<EntryCallEvent> equalEvents = new ArrayList<>();

		while (!queue.isEmpty()) {
			final EntryCallEvent event1 = queue.poll();
			// look for equal events, as they can be duplicated, instead of created
			for (final EntryCallEvent event2 : queue) {
				if (GraphEditDistance.haveSameValues(event1, event2)) {
					equalEvents.add(event2);
				}
			}
			queue.removeAll(equalEvents);
			distance += equalEvents.size() * this.weighting.getDuplicateCost(event1.getParameters());
			distance += this.weighting.getInsertCost(event1.getParameters());
		}

		return distance;
	}

	/**
	 * checks if two events share the same values
	 *
	 * @return True, if both events share the exact same values, false otherwise
	 */
	private static boolean haveSameValues(final EntryCallEvent event1,
			final EntryCallEvent event2) {
		return Arrays.equals(event1.getValues(), event2.getValues());
	}
}
