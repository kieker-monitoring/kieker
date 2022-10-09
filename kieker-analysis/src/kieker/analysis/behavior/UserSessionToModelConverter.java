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
package kieker.analysis.behavior;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.behavior.data.EntryCallEvent;
import kieker.analysis.behavior.data.PayloadAwareEntryCallEvent;
import kieker.analysis.behavior.data.UserSession;
import kieker.analysis.behavior.model.BehaviorModel;
import kieker.analysis.behavior.model.Edge;
import kieker.analysis.behavior.model.Node;

import teetime.stage.basic.AbstractTransformation;

/**
 * Converts User Sessions into {@link BehaviorModel}s.
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class UserSessionToModelConverter extends AbstractTransformation<UserSession, BehaviorModel> {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserSessionToModelConverter.class);

	public UserSessionToModelConverter() {
		// default constructor
	}

	@Override
	protected void execute(final UserSession session) throws Exception {

		UserSessionToModelConverter.LOGGER.info("Received user session");

		// sort events by the time they occurred
		session.sortEventsBy(UserSession.SORT_ENTRY_CALL_EVENTS_BY_ENTRY_TIME);
		final List<EntryCallEvent> entryCalls = session.getEvents();

		this.outputPort.send(UserSessionToModelConverter.eventsToModel(entryCalls));
		UserSessionToModelConverter.LOGGER.info("Created BehaviorModelGED");

	}

	/**
	 * Converts a list of events into a behavior model.
	 *
	 * @param events
	 *            The list of events
	 * @return The behavior model
	 */
	public static BehaviorModel eventsToModel(final List<EntryCallEvent> events) {
		final BehaviorModel model = new BehaviorModel();

		final Iterator<EntryCallEvent> iterator = events.iterator();

		// start with the node "init"
		Node currentNode = new Node("Init");
		model.getNodes().put("Init", currentNode);

		Node lastNode = currentNode;

		// for all events
		while (iterator.hasNext()) {
			final PayloadAwareEntryCallEvent event = (PayloadAwareEntryCallEvent) iterator.next();

			// current node is an existing node with the same name or if non-existing a new node
			currentNode = model.getNodes().get(event.getOperationSignature());
			if (currentNode == null) {
				currentNode = new Node(event.getOperationSignature());
			}

			// add node to model
			model.getNodes().put(event.getOperationSignature(), currentNode);

			// add edge to model
			UserSessionToModelConverter.addEdge(event, model, lastNode, currentNode);
			lastNode = currentNode;
		}
		return model;

	}

	/**
	 * Adds an edge to a model, if the edge does not exist already.
	 *
	 * @param event
	 * @param model
	 * @param source
	 * @param target
	 */
	public static void addEdge(final PayloadAwareEntryCallEvent event, final BehaviorModel model,
			final Node source, final Node target) {
		final Edge matchingEdge = source.getOutgoingEdges().get(target);

		// if edge does not exist yet
		if (matchingEdge == null) {
			final Edge newEdge = new Edge(event, source, target);
			// add references to the model and the nodes
			source.getOutgoingEdges().put(target, newEdge);
			target.getIngoingEdges().put(source, newEdge);
			model.getEdges().add(newEdge);
		} else { // if edge already exists
			matchingEdge.addEvent(event);
		}
	}

}
