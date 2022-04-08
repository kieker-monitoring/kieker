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
package kieker.analysis.trace;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import kieker.model.system.model.AbstractSession;
import kieker.model.system.model.ExecutionTrace;
import kieker.model.system.model.ExecutionTraceBasedSession;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * This filter reconstructs sessions from execution or message traces.
 *
 * @author Holger Knoche
 * @author Reiner Jung -- teetime port
 *
 * @since 1.10
 *
 */
public class SessionReconstructionFilter extends AbstractConsumerStage<ExecutionTrace> {

	/**
	 * Default size for all priority queues. This is only needed because there is no
	 * priority queue constructor that only takes a comparator.
	 */
	private static final int DEFAULT_QUEUE_SIZE = 16;

	private final OutputPort<ExecutionTraceBasedSession> outputPort = this
			.createOutputPort(ExecutionTraceBasedSession.class);

	private final long maxThinkTime;

	private final ConcurrentHashMap<String, ExecutionTraceBasedSession> openExecutionBasedSessions = new ConcurrentHashMap<>();
	private final PriorityQueue<ExecutionTraceBasedSession> executionSessionTimeoutQueue = new PriorityQueue<>(
			SessionReconstructionFilter.DEFAULT_QUEUE_SIZE, new SessionEndTimestampComparator());

	/**
	 * Creates a new session reconstruction filter using the given configuration.
	 *
	 * @param timeunit
	 *            used time unit, e.g., NANOSECONDS
	 * @param maxThinkTime
	 *            max time to collect session information (null implies
	 *            Long.MAX_VALUE)
	 */
	public SessionReconstructionFilter(final TimeUnit timeunit, final Long maxThinkTime) {
		this.maxThinkTime = timeunit.convert(maxThinkTime == 0 ? Long.MAX_VALUE : maxThinkTime, timeunit); // NOCS

		if (this.maxThinkTime < 0) {
			throw new IllegalArgumentException("maxThinkTime must not be negative (found: " + this.maxThinkTime + ")");
		}
	}

	public OutputPort<ExecutionTraceBasedSession> getOutputPort() {
		return this.outputPort;
	}

	private void dispatchCompletedSession(final ExecutionTraceBasedSession session) {
		session.setCompleted();
		this.outputPort.send(session);
	}

	private void processTimeouts(final long currentTime, final PriorityQueue<ExecutionTraceBasedSession> timeoutQueue,
			final Map<String, ExecutionTraceBasedSession> openSessions) {
		while (!timeoutQueue.isEmpty()) {
			final ExecutionTraceBasedSession session = timeoutQueue.peek();
			final long currentThinkTime = (currentTime - session.getEndTimestamp());

			// The current session timed out
			if (currentThinkTime > this.maxThinkTime) {
				timeoutQueue.remove();
				openSessions.remove(session.getSessionId());
				this.dispatchCompletedSession(session);
			} else { // If the current session has not timed out, we are done due to the ordering of
						// the queue
				break;
			}
		}
	}

	private void closeAndDispatchAllSessions(final PriorityQueue<ExecutionTraceBasedSession> timeoutQueue,
			final Map<String, ExecutionTraceBasedSession> openSessions) {
		synchronized (this) {
			while (!timeoutQueue.isEmpty()) {
				final ExecutionTraceBasedSession session = timeoutQueue.poll();
				openSessions.remove(session.getSessionId());
				this.dispatchCompletedSession(session);
			}
		}
	}

	private void closeAndDispatchRemainingSessions() {
		this.closeAndDispatchAllSessions(this.executionSessionTimeoutQueue, this.openExecutionBasedSessions);
	}

	/**
	 * Processes an incoming execution.
	 *
	 * @param executionTrace
	 *            The execution trace to process.
	 */
	@Override
	protected void execute(final ExecutionTrace executionTrace) throws Exception {
		synchronized (this) {
			// Purge timed-out sessions before processing the next trace
			final long currentTimestamp = executionTrace.getStartTimestamp();
			this.processTimeouts(currentTimestamp, this.executionSessionTimeoutQueue, this.openExecutionBasedSessions);

			// Add the trace to the appropriate session and create it if necessary
			boolean existingSession = true;
			final String sessionId = executionTrace.getSessionId();
			ExecutionTraceBasedSession session = this.openExecutionBasedSessions.get(sessionId);
			if (session == null) {
				session = new ExecutionTraceBasedSession(sessionId);
				final ExecutionTraceBasedSession previousSession = this.openExecutionBasedSessions
						.putIfAbsent(sessionId, session);
				existingSession = previousSession != null;
			}

			session.addTrace(executionTrace);

			// Update the changed session's position in the timeout queue
			if (existingSession) {
				this.executionSessionTimeoutQueue.remove(session);
			}
			this.executionSessionTimeoutQueue.add(session);
		}
	}

	@Override
	public void onTerminating() {
		this.closeAndDispatchRemainingSessions();
		super.onTerminating();
	}

	private static class SessionEndTimestampComparator implements Comparator<AbstractSession<?>>, Serializable {

		private static final long serialVersionUID = -5631887288009598075L;

		// Avoid warning when calling (default) constructor
		public SessionEndTimestampComparator() {
			super();
		}

		@Override
		public int compare(final AbstractSession<?> o1, final AbstractSession<?> o2) {
			final long endTimestamp1 = o1.getEndTimestamp();
			final long endTimestamp2 = o2.getEndTimestamp();

			if (endTimestamp1 == endTimestamp2) {
				return 0;
			} else if (endTimestamp1 < endTimestamp2) {
				return -1;
			} else {
				return 1;
			}
		}

	}

}
