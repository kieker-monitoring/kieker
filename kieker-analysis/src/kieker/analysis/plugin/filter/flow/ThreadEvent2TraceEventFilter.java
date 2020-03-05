/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.plugin.filter.flow;

import java.util.HashMap;
import java.util.Map;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.thread.AfterFailedThreadBasedEvent;
import kieker.common.record.flow.thread.AfterThreadBasedEvent;
import kieker.common.record.flow.thread.BeforeThreadBasedEvent;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.misc.ThreadMetaData;

/**
 * @author Christian Wulf (chw)
 *
 * @since 1.13
 */
@Plugin(name = "Thread Event to Trace Event Filter (Event)", description = "Filter to transform threadId-based events to traceId-based events", outputPorts = {
	@OutputPort(name = ThreadEvent2TraceEventFilter.OUTPUT_PORT_NAME_DEFAULT, description = "Outputs monitoring records", eventTypes = {
		IMonitoringRecord.class }) })
public class ThreadEvent2TraceEventFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_DEFAULT = "defaultInputPort";
	public static final String OUTPUT_PORT_NAME_DEFAULT = "defaultOutputPort";

	private final Map<Long, String> hostNames = new HashMap<>(); // NOPMD (not thread-safe)
	private final Map<Long, MonitoredTrace> monitoredTraces = new HashMap<>(); // NOPMD (not thread-safe)
	private int currentTraceId; // NOPMD (not thread-safe)

	public ThreadEvent2TraceEventFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@InputPort(name = INPUT_PORT_NAME_DEFAULT, description = "Input port for a threadId-based event", eventTypes = {
		IMonitoringRecord.class })
	public void readInput(final IMonitoringRecord event) {
		if (event instanceof BeforeThreadBasedEvent) {
			final BeforeThreadBasedEvent originalEvent = (BeforeThreadBasedEvent) event;

			final MonitoredTrace monitoredTrace = this.getOrCreateMonitoredThread(originalEvent.getLoggingTimestamp(),
					originalEvent.getThreadId());

			final BeforeOperationEvent newEvent = new BeforeOperationEvent(originalEvent.getTimestamp(),
					monitoredTrace.identifier, originalEvent.getOrderIndex(), originalEvent.getOperationSignature(),
					originalEvent.getClassSignature());
			newEvent.setLoggingTimestamp(originalEvent.getLoggingTimestamp());

			super.deliver(OUTPUT_PORT_NAME_DEFAULT, newEvent);
		} else if (event instanceof AfterThreadBasedEvent) {
			final AfterThreadBasedEvent originalEvent = (AfterThreadBasedEvent) event;

			final MonitoredTrace monitoredTrace = this.getMonitoredThread(originalEvent.getThreadId());

			final AfterOperationEvent newEvent = new AfterOperationEvent(originalEvent.getTimestamp(),
					monitoredTrace.identifier, originalEvent.getOrderIndex(), originalEvent.getOperationSignature(),
					originalEvent.getClassSignature());
			newEvent.setLoggingTimestamp(originalEvent.getLoggingTimestamp());

			super.deliver(OUTPUT_PORT_NAME_DEFAULT, newEvent);
		} else if (event instanceof AfterFailedThreadBasedEvent) {
			final AfterFailedThreadBasedEvent originalEvent = (AfterFailedThreadBasedEvent) event;

			final MonitoredTrace monitoredTrace = this.getMonitoredThread(originalEvent.getThreadId());

			final AfterOperationFailedEvent newEvent = new AfterOperationFailedEvent(originalEvent.getTimestamp(),
					monitoredTrace.identifier, originalEvent.getOrderIndex(), originalEvent.getOperationSignature(),
					originalEvent.getClassSignature(), originalEvent.getCause());
			newEvent.setLoggingTimestamp(originalEvent.getLoggingTimestamp());

			super.deliver(OUTPUT_PORT_NAME_DEFAULT, newEvent);
		} else if (event instanceof ThreadMetaData) {
			final ThreadMetaData threadMetaData = (ThreadMetaData) event;

			final long threadId = threadMetaData.getThreadId();
			final String hostName = threadMetaData.getHostname();
			this.hostNames.put(threadId, hostName);
		} else {
			// pass through all other record types
			super.deliver(OUTPUT_PORT_NAME_DEFAULT, event);
		}
	}

	private MonitoredTrace getOrCreateMonitoredThread(final long beforeEventLoggingTimestamp, final long threadId) {
		final MonitoredTrace monitoredTrace;
		if (!this.monitoredTraces.containsKey(threadId)) {
			final int uniqueTraceId = this.currentTraceId++; // generates a unique trace id
			monitoredTrace = new MonitoredTrace(uniqueTraceId);
			monitoredTrace.currentStackSize = 0;
			this.monitoredTraces.put(threadId, monitoredTrace);

			// The synthesize logging timestamp for the trace meta data must smaller than the first before event.
			// Hence, we subtract 1 from the before event.
			final long synthesizedLoggingTimestamp = beforeEventLoggingTimestamp - 1;
			final String hostName = this.hostNames.get(threadId);
			final TraceMetadata traceMetadata = new TraceMetadata(monitoredTrace.identifier, threadId, "<NO SESSION>",
					hostName, -1, -1);
			traceMetadata.setLoggingTimestamp(synthesizedLoggingTimestamp);

			super.deliver(OUTPUT_PORT_NAME_DEFAULT, traceMetadata);
		} else {
			monitoredTrace = this.monitoredTraces.get(threadId);
		}

		monitoredTrace.currentStackSize++;
		return monitoredTrace;
	}

	private MonitoredTrace getMonitoredThread(final long threadId) {
		final MonitoredTrace monitoredTrace = this.monitoredTraces.get(threadId);
		monitoredTrace.currentStackSize--;
		if (monitoredTrace.currentStackSize == 0) {
			// remove the monitored thread to trigger the generation of a new trace id upon the next before event
			this.monitoredTraces.remove(threadId);
		}
		return monitoredTrace;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration(this.configuration);
	}

	private static class MonitoredTrace {
		public final int identifier; // NOCS (private field)
		public int currentStackSize; // NOCS (private field)

		public MonitoredTrace(final int identifier) {
			this.identifier = identifier;
		}
	}
}
