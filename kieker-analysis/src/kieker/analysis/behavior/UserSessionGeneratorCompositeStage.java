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
import kieker.analysis.behavior.acceptance.matcher.SessionAcceptanceFilter;
import kieker.analysis.behavior.events.EntryCallEvent;
import kieker.analysis.behavior.model.UserSession;
import kieker.analysis.behavior.signature.processor.ITraceSignatureProcessor;
import kieker.analysis.behavior.signature.processor.TraceSignatureProcessorFilter;
import kieker.common.record.session.ISessionEvent;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * Create user sessions based on @{link EntryCallEvent}s.
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class UserSessionGeneratorCompositeStage extends CompositeStage {

	private final EntryCallSequenceStage entryCallSequence;
	private final TraceSignatureProcessorFilter traceOperationCleanupFilter;

	/**
	 * Create the user session generator stage.
	 *
	 * @param entryCallMatcher
	 *            matcher to check on entry calls to filter out requests that do not belong to the behavior, e.g., loading images
	 * @param traceSignatureProcessor
	 *            cleanup rewriter
	 * @param userSessionTimeout
	 *            defines after how many nano seconds a session is considered to be timed out
	 */
	public UserSessionGeneratorCompositeStage(final IEntryCallAcceptanceMatcher entryCallMatcher,
			final ITraceSignatureProcessor traceSignatureProcessor, final Long userSessionTimeout) {

		/** -- create stages. -- */
		/** Create EntryCallSequence */
		this.entryCallSequence = new EntryCallSequenceStage(userSessionTimeout);
		/** Create SessionAcceptanceFilter */
		final SessionAcceptanceFilter sessionAcceptanceFilter = new SessionAcceptanceFilter(entryCallMatcher);
		/** Create TraceOperationsCleanupFilter */
		this.traceOperationCleanupFilter = new TraceSignatureProcessorFilter(
				traceSignatureProcessor);

		/** Connect all ports */
		this.connectPorts(this.entryCallSequence.getUserSessionOutputPort(), sessionAcceptanceFilter.getInputPort());
		this.connectPorts(sessionAcceptanceFilter.getOutputPort(), this.traceOperationCleanupFilter.getInputPort());
	}

	public InputPort<EntryCallEvent> getInputPort() {
		return this.entryCallSequence.getEntryCallInputPort();
	}

	public InputPort<ISessionEvent> getSessionEventInputPort() {
		return this.entryCallSequence.getSessionEventInputPort();
	}

	public OutputPort<UserSession> getSessionOutputPort() {
		return this.traceOperationCleanupFilter.getOutputPort();
	}

}
