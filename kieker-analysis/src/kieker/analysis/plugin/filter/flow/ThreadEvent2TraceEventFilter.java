package kieker.analysis.plugin.filter.flow;

import java.util.*;

import kieker.analysis.*;
import kieker.analysis.plugin.annotation.*;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.flow.*;
import kieker.common.record.flow.thread.*;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.*;

/**
 * @author Christian Wulf (chw)
 *
 * @since 1.13
 */
@Plugin(name = "Thread Event to Trace Event Filter (Event)", description = "Filter to transform threadId-based events to traceId-based events", outputPorts = {
		@OutputPort(name = ThreadEvent2TraceEventFilter.OUTPUT_PORT_NAME_DEFAULT, description = "Outputs traceId-based events", eventTypes = {
				IEventRecord.class }) })
public class ThreadEvent2TraceEventFilter extends AbstractFilterPlugin {

	private static class MonitoredTrace {
		final int identifier;
		int currentStackSize;

		public MonitoredTrace(int identifier) {
			this.identifier = identifier;
		}
	}

	public static final String INPUT_PORT_NAME_DEFAULT = "defaultInputPort";
	public static final String OUTPUT_PORT_NAME_DEFAULT = "defaultOutputPort";

	private final Map<Long, MonitoredTrace> monitoredTraces = new HashMap<>();
	private int currentTraceId;

	public ThreadEvent2TraceEventFilter(Configuration configuration, IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@InputPort(name = INPUT_PORT_NAME_DEFAULT, description = "Input port for a threadId-based event", eventTypes = {
			IThreadBasedRecord.class })
	public void readInput(IThreadBasedRecord event) {
		if (event instanceof BeforeThreadBasedEvent) {
			BeforeThreadBasedEvent beforeEvent = (BeforeThreadBasedEvent) event;

			MonitoredTrace monitoredTrace = getOrCreateMonitoredThread(beforeEvent.getThreadId());

			BeforeOperationEvent newEvent = new BeforeOperationEvent(beforeEvent.getTimestamp(),
					monitoredTrace.identifier, beforeEvent.getOrderIndex(), beforeEvent.getOperationSignature(),
					beforeEvent.getClassSignature());
			super.deliver(OUTPUT_PORT_NAME_DEFAULT, newEvent);
		} else if (event instanceof AfterThreadBasedEvent) {
			AfterThreadBasedEvent afterEvent = (AfterThreadBasedEvent) event;

			MonitoredTrace monitoredTrace = getMonitoredThread(afterEvent.getThreadId());

			AfterOperationEvent newEvent = new AfterOperationEvent(afterEvent.getTimestamp(), monitoredTrace.identifier,
					afterEvent.getOrderIndex(), afterEvent.getOperationSignature(), afterEvent.getClassSignature());
			super.deliver(OUTPUT_PORT_NAME_DEFAULT, newEvent);
		} else if (event instanceof AfterFailedThreadBasedEvent) {
			AfterFailedThreadBasedEvent afterEvent = (AfterFailedThreadBasedEvent) event;

			MonitoredTrace monitoredTrace = getMonitoredThread(afterEvent.getThreadId());

			AfterOperationFailedEvent newEvent = new AfterOperationFailedEvent(afterEvent.getTimestamp(),
					monitoredTrace.identifier, afterEvent.getOrderIndex(), afterEvent.getOperationSignature(),
					afterEvent.getClassSignature(), afterEvent.getCause());
			super.deliver(OUTPUT_PORT_NAME_DEFAULT, newEvent);
		} else {
			// pass through all other record types
			super.deliver(OUTPUT_PORT_NAME_DEFAULT, event);
		}
	}

	private MonitoredTrace getOrCreateMonitoredThread(final long threadId) {
		MonitoredTrace monitoredTrace;
		if (!this.monitoredTraces.containsKey(threadId)) {
			final int uniqueTraceId = this.currentTraceId++;	// generates a unique trace id
			monitoredTrace = new MonitoredTrace(uniqueTraceId);
			monitoredTrace.currentStackSize = 0;
			this.monitoredTraces.put(threadId, monitoredTrace);

			String hostName = "UNKNOWN HOSTNAME"; // FIXME receive host name from somewhere
			TraceMetadata traceMetadata = new TraceMetadata(monitoredTrace.identifier, threadId, "<NO SESSION>",
					hostName, -1, -1);
			super.deliver(OUTPUT_PORT_NAME_DEFAULT, traceMetadata);
		} else {
			monitoredTrace = this.monitoredTraces.get(threadId);
		}

		monitoredTrace.currentStackSize++;
		return monitoredTrace;
	}

	private MonitoredTrace getMonitoredThread(final long threadId) {
		MonitoredTrace monitoredTrace = this.monitoredTraces.get(threadId);
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
}
