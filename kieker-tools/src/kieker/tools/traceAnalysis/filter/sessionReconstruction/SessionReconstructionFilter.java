/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

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

/**
 * This filter reconstructs sessions from execution or message traces.
 *
 *
 * @author Holger Knoche
 * @since 1.10
 *
 */
@Plugin(description = "Reconstructs sessions from execution or message traces", outputPorts = {
	@OutputPort(name = SessionReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE_SESSIONS, description = "Reconstructed execution trace-based sessions", eventTypes = {
		ExecutionTraceBasedSession.class })
}, configuration = {
	@Property(name = SessionReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_THINK_TIME, defaultValue = "500000"),
	@Property(name = SessionReconstructionFilter.CONFIG_PROPERTY_NAME_TIMEUNIT, defaultValue = SessionReconstructionFilter.CONFIG_PROPERTY_VALUE_TIMEUNIT)
})
public class SessionReconstructionFilter extends AbstractFilterPlugin {

	/**
	 * Name of the input port accepting execution traces.
	 */
	public static final String INPUT_PORT_NAME_EXECUTION_TRACES = "executionTraces";

	/**
	 * Name of the output port dispatching execution trace-based sessions.
	 */
	public static final String OUTPUT_PORT_NAME_EXECUTION_TRACE_SESSIONS = "executionTraceSessions";

	/** This is the name of the property determining the used time unit. */
	public static final String CONFIG_PROPERTY_NAME_TIMEUNIT = "timeunit";

	/** This is the default used time unit. */
	public static final String CONFIG_PROPERTY_VALUE_TIMEUNIT = "NANOSECONDS"; // TimeUnit.NANOSECONDS.name()

	/**
	 * This is name of the configuration parameter which accepts the maximum think time
	 * (i.e. the time interval after which a new session is started)
	 */
	public static final String CONFIG_PROPERTY_NAME_MAX_THINK_TIME = "maxThinkTime";

	/** This is the default value for the maximal think time. */
	public static final String CONFIG_PROPERTY_VALUE_MAX_THINK_TIME = "9223372036854775807"; // Long.toString(Long.MAX_VALUE)

	/**
	 * Default size for all priority queues. This is only needed because there is no priority queue
	 * constructor that only takes a comparator.
	 */
	private static final int DEFAULT_QUEUE_SIZE = 16;

	private final TimeUnit timeunit;

	private final long maxThinkTime;

	private final ConcurrentHashMap<String, ExecutionTraceBasedSession> openExecutionBasedSessions = new ConcurrentHashMap<>();
	private final PriorityQueue<ExecutionTraceBasedSession> executionSessionTimeoutQueue = new PriorityQueue<>(DEFAULT_QUEUE_SIZE,
			new SessionEndTimestampComparator());

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

		this.timeunit = super.recordsTimeUnitFromProjectContext;

		final String configTimeunitProperty = configuration.getStringProperty(CONFIG_PROPERTY_NAME_TIMEUNIT);
		TimeUnit configTimeunit;
		try {
			configTimeunit = TimeUnit.valueOf(configTimeunitProperty);
		} catch (final IllegalArgumentException ex) {
			this.logger.warn("{} is no valid TimeUnit! Using inherited value of {} instead.", configTimeunitProperty, this.timeunit.name());
			configTimeunit = this.timeunit;
		}

		this.maxThinkTime = this.timeunit.convert(configuration.getLongProperty(CONFIG_PROPERTY_NAME_MAX_THINK_TIME),
				configTimeunit);

		if (this.maxThinkTime < 0) {
			throw new IllegalArgumentException("value " + CONFIG_PROPERTY_VALUE_MAX_THINK_TIME + " must not be negative (found: " + this.maxThinkTime + ")");
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_TIMEUNIT, this.timeunit.name());
		configuration.setProperty(CONFIG_PROPERTY_NAME_MAX_THINK_TIME, Long.toString(this.maxThinkTime));

		return configuration;
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
			} else { // If the current session has not timed out, we are done due to the ordering of the queue
				break;
			}
		}
	}

	private <T extends AbstractSession<?>> void closeAndDispatchAllSessions(final PriorityQueue<T> timeoutQueue, final Map<String, T> openSessions,
			final String outputPortName) {
		synchronized (this) {
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
		synchronized (this) {
			// Purge timed-out sessions before processing the next trace
			final long currentTimestamp = executionTrace.getStartTimestamp();
			this.processTimeouts(currentTimestamp, OUTPUT_PORT_NAME_EXECUTION_TRACE_SESSIONS, this.executionSessionTimeoutQueue, this.openExecutionBasedSessions);

			// Add the trace to the appropriate session and create it if necessary
			boolean existingSession = true;
			final String sessionId = executionTrace.getSessionId();
			ExecutionTraceBasedSession session = this.openExecutionBasedSessions.get(sessionId);
			if (session == null) {
				session = new ExecutionTraceBasedSession(sessionId);
				final ExecutionTraceBasedSession previousSession = this.openExecutionBasedSessions.putIfAbsent(sessionId, session);
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
	public void terminate(final boolean previousError) {
		if (!previousError) {
			this.closeAndDispatchRemainingSessions();
		}
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
