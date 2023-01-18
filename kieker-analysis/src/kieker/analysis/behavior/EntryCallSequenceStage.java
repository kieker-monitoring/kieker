/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kieker.analysis.behavior.events.EntryCallEvent;
import kieker.analysis.behavior.model.UserSession;
import kieker.common.record.session.ISessionEvent;
import kieker.common.record.session.SessionEndEvent;
import kieker.common.record.session.SessionStartEvent;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * Represents the EntryCallSequence Transformation in the paper <i>Run-time Architecture Models for
 * Dynamic Adaptation and Evolution of Cloud Applications</i>.
 * The EntryCallSequenceStage
 *
 * @author Robert Heinrich
 * @author Alessandro Guisa
 * @author Christoph Dornieden
 *
 * @since 2.0.0
 */
public final class EntryCallSequenceStage extends AbstractStage {
	/** map of sessions. */
	private final Map<String, UserSession> sessions = new HashMap<>();
	/** output ports. */
	private final OutputPort<UserSession> userSessionOutputPort = this.createOutputPort();

	private final InputPort<EntryCallEvent> entryCallInputPort = this.createInputPort();
	private final InputPort<ISessionEvent> sessionEventInputPort = this.createInputPort();
	private final Long userSessionTimeout;

	/**
	 * Create this filter.
	 *
	 */
	public EntryCallSequenceStage(final Long userSessionTimeout) {
		this.userSessionTimeout = userSessionTimeout;
	}

	@Override
	protected void execute() {
		this.processSessionEvent(this.sessionEventInputPort.receive());
		this.processEntryCallEvent(this.entryCallInputPort.receive());

		if (this.userSessionTimeout != null) {
			this.removeExpiredSessions();
		}
	}

	private void processEntryCallEvent(final EntryCallEvent event) {
		if (event != null) {
			/**
			 * add the event to the corresponding user session in case the user session is not yet
			 * available, create one.
			 */
			final String userSessionId = UserSession.createUserSessionId(event);
			UserSession userSession = this.sessions.get(userSessionId);
			if (userSession == null) {
				userSession = new UserSession(event.getHostname(), event.getSessionId());
				this.sessions.put(userSessionId, userSession);
				// TODO this should trigger a warning, as the session should be create by a session event
			}
			userSession.add(event, true);
		}
	}

	private void processSessionEvent(final ISessionEvent sessionEvent) {
		if (sessionEvent != null) {
			if (sessionEvent instanceof SessionStartEvent) {
				this.sessions.put(UserSession.createUserSessionId(sessionEvent),
						new UserSession(sessionEvent.getHostname(), sessionEvent.getSessionId()));
			}
			if (sessionEvent instanceof SessionEndEvent) {
				final UserSession session = this.sessions.get(UserSession.createUserSessionId(sessionEvent));
				if (session != null) {
					this.userSessionOutputPort.send(session);
					this.sessions.remove(sessionEvent.getSessionId());
				}
			}
		}
	}

	/**
	 * removes all expired sessions from the filter and sends them to
	 * tBehaviorModelPreperationOutputPort.
	 */
	private void removeExpiredSessions() {
		final long timeNow = System.currentTimeMillis() * 1000000;
		final Set<String> sessionsToRemove = new HashSet<>();

		for (final String sessionId : this.sessions.keySet()) {
			final UserSession session = this.sessions.get(sessionId);
			final long exitTime = session.getExitTime();

			final boolean isExpired = exitTime + this.userSessionTimeout < timeNow;

			if (isExpired) {
				this.userSessionOutputPort.send(session);
				sessionsToRemove.add(sessionId);
			}
		}
		for (final String sessionId : sessionsToRemove) {
			this.sessions.remove(sessionId);
		}
	}

	@Override
	public void onTerminating() {
		for (final UserSession session : this.sessions.values()) {
			this.userSessionOutputPort.send(session);
		}
		super.onTerminating();
	}

	/**
	 * @return output port
	 */
	public OutputPort<UserSession> getUserSessionOutputPort() {
		return this.userSessionOutputPort;
	}

	public InputPort<EntryCallEvent> getEntryCallInputPort() {
		return this.entryCallInputPort;
	}

	public InputPort<ISessionEvent> getSessionEventInputPort() {
		return this.sessionEventInputPort;
	}

}
