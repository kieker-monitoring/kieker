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

import kieker.analysis.behavior.clustering.IModelGenerationFilterFactory;
import kieker.analysis.behavior.clustering.UserSessionOperationCleanupStage;
import kieker.analysis.behavior.data.PayloadAwareEntryCallEvent;
import kieker.analysis.behavior.data.UserSession;
import kieker.common.records.session.ISessionEvent;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * This is based on UserSessionGeneratorCompositeStage.
 *
 * @author Lars Jürgensen
 *
 */
// TODO check naming for stage
public class UserSessionGeneratorCompositeStage extends CompositeStage {

	private final UserSessionOperationCleanupStage userSessionOperationCleanupStage;
	private final EntryCallSequenceStage entryCallSequence;

	/**
	 * Create the user session generator stage.
	 *
	 * @param entryCallMatcher
	 *            matcher to check on entry calls to filter out requests that do not belong to the behavior, e.g., loading images
	 * @param cleanupRewriter
	 *            cleanup rewriter
	 * @param filterRulesFactory
	 *            filter rules factory
	 */
	public UserSessionGeneratorCompositeStage(final IEntryCallAcceptanceMatcher entryCallMatcher, final ITraceSignatureCleanupRewriter cleanupRewriter,
			final IModelGenerationFilterFactory filterRulesFactory) {

		/** -- create stages. -- */
		/** Create EntryCallSequence */
		this.entryCallSequence = new EntryCallSequenceStage();
		/** Create SessionAcceptanceFilter */
		final SessionAcceptanceFilter sessionAcceptanceFilter = new SessionAcceptanceFilter(entryCallMatcher);
		/** Create TraceOperationsCleanupFilter */
		final TraceOperationCleanupFilter traceOperationCleanupFilter = new TraceOperationCleanupFilter(
				cleanupRewriter);
		/** Create UserSessionOperationsFilter */
		this.userSessionOperationCleanupStage = new UserSessionOperationCleanupStage(filterRulesFactory.createFilter());

		/** Connect all ports */
		this.connectPorts(this.entryCallSequence.getUserSessionOutputPort(), sessionAcceptanceFilter.getInputPort());
		this.connectPorts(sessionAcceptanceFilter.getOutputPort(), traceOperationCleanupFilter.getInputPort());
		this.connectPorts(traceOperationCleanupFilter.getOutputPort(),
				this.userSessionOperationCleanupStage.getInputPort());
	}

	public InputPort<PayloadAwareEntryCallEvent> getInputPort() {
		return this.entryCallSequence.getEntryCallInputPort();
	}

	public InputPort<ISessionEvent> getSessionEventInputPort() {
		return this.entryCallSequence.getSessionEventInputPort();
	}

	public OutputPort<UserSession> getSessionOutputPort() {
		return this.userSessionOperationCleanupStage.getOutputPort();
	}

}
