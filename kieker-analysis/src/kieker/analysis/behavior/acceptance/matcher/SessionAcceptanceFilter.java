/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.behavior.acceptance.matcher;

import kieker.analysis.behavior.data.UserSession;
import kieker.analysis.behavior.events.EntryCallEvent;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Tests whether a trace contains only operations which are considered valid trace elements. In
 * effect it ignores invalid sessions.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class SessionAcceptanceFilter extends AbstractConsumerStage<UserSession> {

	private final OutputPort<UserSession> outputPort = this.createOutputPort();
	private final IEntryCallAcceptanceMatcher matcher;

	/**
	 * Create an acceptance filter with an external matcher.
	 *
	 * @param matcher
	 *            a acceptance matcher
	 */
	public SessionAcceptanceFilter(final IEntryCallAcceptanceMatcher matcher) {
		this.matcher = matcher;
	}

	@Override
	protected void execute(final UserSession session) throws Exception {
		for (int i = 0; i < session.getEvents().size(); i++) {
			final EntryCallEvent call = session.getEvents().get(i);
			if (!this.matcher.match(call)) {
				session.getEvents().remove(i);
				i--; // NOCS
			}
		}
		if (session.getEvents().size() > 0) {
			this.outputPort.send(session);
		}
	}

	public OutputPort<UserSession> getOutputPort() {
		return this.outputPort;
	}

}
