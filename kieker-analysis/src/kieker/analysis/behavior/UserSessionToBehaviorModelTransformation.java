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

import java.util.List;
import java.util.Optional;

import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;

import kieker.analysis.behavior.data.UserSession;
import kieker.analysis.behavior.events.EntryCallEvent;
import kieker.analysis.behavior.model.UserBehaviorEdge;
import kieker.analysis.generic.graph.IGraph;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.generic.graph.impl.GraphImpl;
import kieker.analysis.generic.graph.impl.NodeImpl;

import teetime.stage.basic.AbstractTransformation;

/**
 * Converts User Sessions into {@link BehaviorModel}s.
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class UserSessionToBehaviorModelTransformation extends AbstractTransformation<UserSession, IGraph<INode, UserBehaviorEdge>> {

	public UserSessionToBehaviorModelTransformation() {
		// default constructor
	}

	@Override
	protected void execute(final UserSession session) throws Exception {
		this.logger.info("Received user session");

		// sort events by the time they occurred
		session.sortEventsBy(UserSession.SORT_ENTRY_CALL_EVENTS_BY_ENTRY_TIME);
		final List<EntryCallEvent> entryCalls = session.getEvents();

		this.outputPort.send(new GraphImpl<>(session.getSessionId(), this.eventsToModel(entryCalls)));
		this.logger.debug("Created BehaviorModel");

	}

	/**
	 * Converts a list of events into a behavior model.
	 *
	 * @param events
	 *            The list of events
	 * @return The behavior model
	 */
	public MutableNetwork<INode, UserBehaviorEdge> eventsToModel(final List<EntryCallEvent> events) {
		final MutableNetwork<INode, UserBehaviorEdge> model = NetworkBuilder.directed().allowsSelfLoops(true).build();

		// start with the node "init"
		INode currentNode = new NodeImpl("Init");
		model.addNode(currentNode);

		INode lastNode = currentNode;

		// for all events
		for (final EntryCallEvent event : events) {
			// current node is an existing node with the same name or if non-existing a new node
			final Optional<INode> currentNodeOptional = this.findNode(model, event.getOperationSignature());
			if (!currentNodeOptional.isPresent()) {
				currentNode = new NodeImpl(event.getOperationSignature());
				// add node to model
				model.addNode(currentNode);
			} else {
				currentNode = currentNodeOptional.get();
			}

			// add edge to model
			this.addEdge(event, model, lastNode, currentNode);
			lastNode = currentNode;
		}
		return model;
	}

	private Optional<INode> findNode(final MutableNetwork<INode, UserBehaviorEdge> model, final String signature) {
		return model.nodes().stream().filter(node -> node.getId().equals(signature)).findFirst();
	}

	/**
	 * Adds an edge to a model, if the edge does not exist already.
	 *
	 * @param event
	 * @param model
	 * @param source
	 * @param target
	 */
	public void addEdge(final EntryCallEvent event, final MutableNetwork<INode, UserBehaviorEdge> model,
			final INode source, final INode target) {
		final Optional<UserBehaviorEdge> matchingEdgeOptional = model.edgeConnecting(source, target);

		// if edge does not exist yet
		if (!matchingEdgeOptional.isPresent()) {
			model.addEdge(source, target, new UserBehaviorEdge(source.getId() + ":" + target.getId(), event));
		} else { // if edge already exists
			matchingEdgeOptional.get().addEvent(event);
		}
	}

}
