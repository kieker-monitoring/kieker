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

import kieker.analysis.behavior.acceptance.matcher.IEntryCallAcceptanceMatcher;
import kieker.analysis.behavior.events.EntryCallEvent;
import kieker.analysis.behavior.model.UserBehaviorEdge;
import kieker.analysis.behavior.signature.processor.ITraceSignatureProcessor;
import kieker.analysis.generic.CountingStage;
import kieker.analysis.generic.DynamicEventDispatcher;
import kieker.analysis.generic.IEventMatcher;
import kieker.analysis.generic.ImplementsEventMatcher;
import kieker.analysis.generic.graph.IGraph;
import kieker.analysis.generic.graph.INode;
import kieker.common.exception.ConfigurationException;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.session.ISessionEvent;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * Generate models from observations.
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class ModelGenerationCompositeStage extends CompositeStage {

	private final OutputPort<IGraph<INode, UserBehaviorEdge>> modelOutputPort;

	private final InputPort<Object> inputPort;

	/**
	 * Generating user behavior graphs.
	 *
	 * @param entryCallMatcher
	 *            matcher to decide which events are actually part of the user behavior
	 * @param traceSignatureProcessor
	 *            processing the event's class and operation signature
	 * @param userSessionTimeout
	 *            timeout for observe sessions
	 * @throws ConfigurationException
	 */
	public ModelGenerationCompositeStage(final IEntryCallAcceptanceMatcher entryCallMatcher,
			final ITraceSignatureProcessor traceSignatureProcessor, final Long userSessionTimeout)
			throws ConfigurationException {

		final DynamicEventDispatcher eventDispatcher = new DynamicEventDispatcher(null, true, true, false);
		final CreateEntryLevelEventStage createEntryLevelEventStage = new CreateEntryLevelEventStage(true); // TODO make this true/false an option
		final IEventMatcher<IFlowRecord> flowRecordMatcher = new ImplementsEventMatcher<>(IFlowRecord.class, null);

		final UserSessionGeneratorCompositeStage sessionGenerator = new UserSessionGeneratorCompositeStage(
				entryCallMatcher, traceSignatureProcessor, userSessionTimeout);
		final UserSessionToBehaviorModelTransformation sessionToModel = new UserSessionToBehaviorModelTransformation();

		this.inputPort = eventDispatcher.getInputPort();

		eventDispatcher.registerOutput(flowRecordMatcher);

		final CountingStage<EntryCallEvent> callCounter = new CountingStage<>(true, 10, "entry");

		this.connectPorts(flowRecordMatcher.getOutputPort(), createEntryLevelEventStage.getInputPort());
		this.connectPorts(createEntryLevelEventStage.getOutputPort(), callCounter.getInputPort());
		this.connectPorts(callCounter.getRelayedEventsOutputPort(), sessionGenerator.getInputPort());

		final IEventMatcher<ISessionEvent> sessionMatcher = new ImplementsEventMatcher<>(ISessionEvent.class, null);
		eventDispatcher.registerOutput(sessionMatcher);

		this.connectPorts(sessionMatcher.getOutputPort(), sessionGenerator.getSessionEventInputPort());

		this.connectPorts(sessionGenerator.getSessionOutputPort(), sessionToModel.getInputPort());

		this.modelOutputPort = this.createOutputPort(sessionToModel.getOutputPort());
	}

	public OutputPort<IGraph<INode, UserBehaviorEdge>> getModelOutputPort() {
		return this.modelOutputPort;
	}

	public InputPort<Object> getInputPort() {
		return this.inputPort;
	}

}
