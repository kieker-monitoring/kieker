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
package kieker.analysis.behavior.clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import kieker.analysis.behavior.events.EntryCallEvent;
import kieker.analysis.behavior.model.EventGroup;
import kieker.analysis.behavior.model.UserBehaviorEdge;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.generic.graph.clustering.BasicCostFunction;

/**
 * Special cost function for user behavior.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class UserBehaviorCostFunction extends BasicCostFunction<INode, UserBehaviorEdge> {

	private final double eventGroupInsertCost;
	private final IParameterWeighting weighting; // the WEIGTHING assigns events a insertion and duplication costs.

	public UserBehaviorCostFunction(final double nodeInsertionCost, final double edgeInsertionCost, final double eventGroupInsertCost,
			final IParameterWeighting parameterWeighting) {
		super(nodeInsertionCost, edgeInsertionCost);
		this.eventGroupInsertCost = eventGroupInsertCost;
		this.weighting = parameterWeighting;
	}

	@Override
	public double computeEdgeInsertionCost(final UserBehaviorEdge edge) {
		double distance = super.computeEdgeInsertionCost(edge);

		for (final EventGroup eventGroup : edge.getEventGroups()) {
			distance += this.eventGroupInsertionCost(eventGroup);
		}

		return distance;
	}

	/**
	 * Calculates the distance between two edges. This includes the distance between the contained
	 * eventgroups
	 *
	 */
	@Override
	public double edgeAnnotationDistance(final UserBehaviorEdge edge1, final UserBehaviorEdge edge2) {
		double distance = super.edgeAnnotationDistance(edge1, edge2);

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

				if (this.haveSameValues(event, potentialMatch)) {

					matches1.add(potentialMatch);
				}
			}
			unvisitedEvents1.removeAll(matches1);

			// find equal events in other event group
			for (final EntryCallEvent potentialMatch : unvisitedEvents2) {
				if (this.haveSameValues(event, potentialMatch)) {
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
				if (this.haveSameValues(event1, event2)) {
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
	 * calculates the insertion cost of an eventgroup including the insertion cost of the events.
	 *
	 * @param eventGroup
	 *            event group
	 */
	private double eventGroupInsertionCost(final EventGroup eventGroup) {
		double distance = this.eventGroupInsertCost;

		final Queue<EntryCallEvent> queue = new LinkedList<>(eventGroup.getEvents());

		final List<EntryCallEvent> equalEvents = new ArrayList<>();

		while (!queue.isEmpty()) {
			final EntryCallEvent event1 = queue.poll();
			// look for equal events, as they can be duplicated, instead of created
			for (final EntryCallEvent event2 : queue) {
				if (this.haveSameValues(event1, event2)) {
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
	 * checks if two events share the same values.
	 *
	 * @return True, if both events share the exact same values, false otherwise
	 */
	private boolean haveSameValues(final EntryCallEvent event1,
			final EntryCallEvent event2) {
		return Arrays.equals(event1.getValues(), event2.getValues());
	}

}
