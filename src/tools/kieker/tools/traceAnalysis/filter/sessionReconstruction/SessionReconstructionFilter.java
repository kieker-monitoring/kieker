/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.filter.sessionReconstruction;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;
import java.util.PriorityQueue;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.systemModel.AbstractSession;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.ExecutionTraceBasedSession;
import kieker.tools.traceAnalysis.systemModel.MessageTraceBasedSession;

/**
 * This filter reconstructs sessions from execution or message traces.
 * 
 * @author Holger Knoche
 * 
 */
@Plugin(description = "Reconstructs sessions from execution or message traces",
		outputPorts = {
			@OutputPort(name = SessionReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE_SESSIONS, description = "Reconstructed execution trace-based sessions",
					eventTypes = { ExecutionTraceBasedSession.class }),
			@OutputPort(name = SessionReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE_SESSIONS, description = "Reconstructed message trace-based sessions",
					eventTypes = { MessageTraceBasedSession.class })
		},
		configuration = {
			@Property(name = SessionReconstructionFilterConfiguration.CONFIGURATION_NAME_MAX_THINK_TIME, defaultValue = "500000")
		})
public class SessionReconstructionFilter extends AbstractFilterPlugin {

	/**
	 * Name of the input port accepting execution traces.
	 */
	public static final String INPUT_PORT_NAME_EXECUTION_TRACES = "executionTraces";

	/**
	 * Name of the input port accepting message traces.
	 */
	public static final String INPUT_PORT_NAME_MESSAGE_TRACES = "messageTraces";

	/**
	 * Name of the output port dispatching execution trace-based sessions.
	 */
	public static final String OUTPUT_PORT_NAME_EXECUTION_TRACE_SESSIONS = "executionTraceSessions";

	/**
	 * Name of the output port dispatching message trace-based sessions.
	 */
	public static final String OUTPUT_PORT_NAME_MESSAGE_TRACE_SESSIONS = "messageTraceSessions";

	/**
	 * Default size for all priority queues. This is only needed because there is no priority queue
	 * constructor that only takes a comparator.
	 */
	private static final int DEFAULT_QUEUE_SIZE = 16;

	private final SessionReconstructionFilterConfiguration configuration;
	private final long maxThinkTime;

	private final Map<String, ExecutionTraceBasedSession> openExecutionBasedSessions = new Hashtable<String, ExecutionTraceBasedSession>();
	private final PriorityQueue<ExecutionTraceBasedSession> executionSessionTimeoutQueue =
			new PriorityQueue<ExecutionTraceBasedSession>(DEFAULT_QUEUE_SIZE, new AbstractSessionEndTimestampComparator());

	/**
	 * Creates a new session reconstruction filter using the given configuration.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component. The component will be registered.
	 */
	public SessionReconstructionFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.configuration = new SessionReconstructionFilterConfiguration(configuration);
		this.maxThinkTime = this.configuration.getMaxThinkTime() * 1000000L;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration(this.configuration.getWrappedConfiguration());
	}

	private <T extends AbstractSession<?>> void dispatchCompletedSession(final T session, final String outputPortName) {
		session.setCompleted();
		this.deliver(outputPortName, session);
	}

	private <T extends AbstractSession<?>> void processTimeouts(final long currentTime, final String outputPortName, final PriorityQueue<T> timeoutQueue,
			final Map<String, T> openSessions) {
		while (!timeoutQueue.isEmpty()) {
			final T session = timeoutQueue.peek();
			final long currentThinkTime = (currentTime - session.getEndTimestamp());

			// The current session timed out
			if (currentThinkTime > this.maxThinkTime) {
				timeoutQueue.remove();
				openSessions.remove(session.getSessionId());
				this.dispatchCompletedSession(session, outputPortName);
			}
			// If the current session has not timed out, we are done due to the ordering of the queue
			else {
				break;
			}
		}
	}

	private <T extends AbstractSession<?>> void closeAndDispatchAllSessions(final PriorityQueue<T> timeoutQueue, final Map<String, T> openSessions,
			final String outputPortName) {
		synchronized (openSessions) {
			while (!timeoutQueue.isEmpty()) {
				final T session = timeoutQueue.poll();
				openSessions.remove(session.getSessionId());

				this.dispatchCompletedSession(session, outputPortName);
			}
		}
	}

	private void closeAndDispatchRemainingSessions() {
		this.closeAndDispatchAllSessions(this.executionSessionTimeoutQueue, this.openExecutionBasedSessions, OUTPUT_PORT_NAME_EXECUTION_TRACE_SESSIONS);
	}

	/**
	 * Processes an incoming execution.
	 * 
	 * @param executionTrace
	 *            The execution trace to process.
	 */
	@InputPort(name = INPUT_PORT_NAME_EXECUTION_TRACES, description = "Receives execution traces", eventTypes = { ExecutionTrace.class })
	public void processExecutionTrace(final ExecutionTrace executionTrace) {
		synchronized (this.openExecutionBasedSessions) {
			// Purge timed-out sessions before processing the next trace
			final long currentTimestamp = executionTrace.getStartTimestamp();
			this.processTimeouts(currentTimestamp, OUTPUT_PORT_NAME_EXECUTION_TRACE_SESSIONS, this.executionSessionTimeoutQueue, this.openExecutionBasedSessions);

			// Add the trace to the appropriate session and create it if necessary
			boolean existingSession = true;
			final String sessionId = executionTrace.getSessionId();
			ExecutionTraceBasedSession session = this.openExecutionBasedSessions.get(sessionId);
			if (session == null) {
				session = new ExecutionTraceBasedSession(sessionId);
				this.openExecutionBasedSessions.put(sessionId, session);
				existingSession = false;
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
	public void terminate(final boolean previousError) {
		if (!previousError) {
			this.closeAndDispatchRemainingSessions();
		}
	}

	private static class AbstractSessionEndTimestampComparator implements Comparator<AbstractSession<?>> {

		// Avoid warning when calling (default) constructor
		public AbstractSessionEndTimestampComparator() {
			super();
		}

		public int compare(final AbstractSession<?> o1, final AbstractSession<?> o2) {
			final long endTimestamp1 = o1.getEndTimestamp();
			final long endTimestamp2 = o2.getEndTimestamp();

			if (endTimestamp1 == endTimestamp2) {
				return 0;
			}
			else if (endTimestamp1 < endTimestamp2) {
				return -1;
			}
			else {
				return 1;
			}
		}

	}

}
