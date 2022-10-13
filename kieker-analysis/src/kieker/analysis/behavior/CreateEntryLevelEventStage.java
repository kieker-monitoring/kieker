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
package kieker.analysis.behavior;

import java.util.HashMap;
import java.util.Map;

import kieker.analysis.behavior.data.EntryCallEvent;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.flow.trace.operation.EntryLevelBeforeOperationEvent;

import teetime.stage.basic.AbstractTransformation;

/**
 * Creates entry level events from trace information.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class CreateEntryLevelEventStage extends AbstractTransformation<IFlowRecord, EntryCallEvent> {

	private final Map<Long, TraceMetadata> registeredTraces = new HashMap<>();
	private final Map<Long, BeforeOperationEvent> registeredBeforeOperationEvents = new HashMap<>();

	private final boolean waitForCompleteTrace;

	public CreateEntryLevelEventStage(final boolean waitForCompleteTrace) {
		this.waitForCompleteTrace = waitForCompleteTrace;
	}

	@Override
	protected void execute(final IFlowRecord element) throws Exception {
		if (element instanceof TraceMetadata) {
			this.registerTraceMetadata((TraceMetadata) element);
		} else {
			if (this.waitForCompleteTrace) {
				if (element instanceof BeforeOperationEvent) {
					final BeforeOperationEvent beforeOperationEvent = (BeforeOperationEvent) element;
					if (beforeOperationEvent.getOrderIndex() == 0) {
						if (this.containsTrace(beforeOperationEvent.getTraceId())) {
							this.registerBeforeOperationEvent(beforeOperationEvent);
						} else {
							this.logger.error("Received BeforeOperationEvent for unknown trace {}", beforeOperationEvent.getTraceId());
						}
					}
				} else if (element instanceof AfterOperationEvent) {
					final AfterOperationEvent afterOperationEvent = (AfterOperationEvent) element;
					if (afterOperationEvent.getOrderIndex() == 0) {
						if (this.containsTrace(afterOperationEvent.getTraceId())) {
							this.outputPort.send(this.createEntryCallEvent(afterOperationEvent));
							this.registeredBeforeOperationEvents.remove(afterOperationEvent.getTraceId());
							this.registeredTraces.remove(afterOperationEvent.getTraceId());
						} else {
							this.logger.error("Received AfterOperationEvent for unknown trace {}", afterOperationEvent.getTraceId());
						}
					}
				}
			} else {
				if (element instanceof BeforeOperationEvent) {
					final BeforeOperationEvent beforeOperationEvent = (BeforeOperationEvent) element;
					if (beforeOperationEvent.getOrderIndex() == 0) {
						if (this.containsTrace(beforeOperationEvent.getTraceId())) {
							this.outputPort.send(this.createEntryCallEvent(beforeOperationEvent));
							this.registeredTraces.remove(beforeOperationEvent.getTraceId());
						} else {
							this.logger.error("Received BeforeOperationEvent for unknown trace {}", beforeOperationEvent.getTraceId());
						}
					}
				}
			}
		}
	}

	private void registerTraceMetadata(final TraceMetadata traceMetadata) {
		this.registeredTraces.put(traceMetadata.getTraceId(), traceMetadata);
	}

	public boolean containsTrace(final Long traceId) {
		return this.registeredTraces.containsKey(traceId);
	}

	private void registerBeforeOperationEvent(final BeforeOperationEvent beforeOperationEvent) {
		this.registeredBeforeOperationEvents.put(beforeOperationEvent.getTraceId(), beforeOperationEvent);
	}

	/**
	 * Create {@link EntryCallEvent} or {@link EntryCallEvent} based on the before event alone. Here execution time is always zero, as the
	 * afterOperationEvent is ignored.
	 *
	 * @param beforeOperationEvent
	 *            before operation event
	 * @return returns an entry call event.
	 */
	private EntryCallEvent createEntryCallEvent(final BeforeOperationEvent beforeOperationEvent) {
		final TraceMetadata traceMetadata = this.registeredTraces.get(beforeOperationEvent.getTraceId());
		if (beforeOperationEvent instanceof EntryLevelBeforeOperationEvent) {
			final EntryLevelBeforeOperationEvent entryLevelbeforeOperationEvent = (EntryLevelBeforeOperationEvent) beforeOperationEvent;
			return new EntryCallEvent(beforeOperationEvent.getTimestamp(), beforeOperationEvent.getTimestamp(),
					beforeOperationEvent.getOperationSignature(), beforeOperationEvent.getClassSignature(),
					traceMetadata.getSessionId(), traceMetadata.getHostname(), entryLevelbeforeOperationEvent.getParameters(),
					entryLevelbeforeOperationEvent.getValues(),
					entryLevelbeforeOperationEvent.getRequestType());
		} else {
			return new EntryCallEvent(beforeOperationEvent.getTimestamp(), beforeOperationEvent.getTimestamp(),
					beforeOperationEvent.getOperationSignature(), beforeOperationEvent.getClassSignature(),
					traceMetadata.getSessionId(), traceMetadata.getHostname(), new String[0], new String[0], 0);
		}
	}

	/**
	 * Create {@link EntryCallEvent} or {@link EntryCallEvent} based on the before and after event. Thus this create events with a useable execution
	 * time.
	 *
	 * @param beforeOperationEvent
	 *            before operation event
	 * @return returns an entry call event.
	 */
	private EntryCallEvent createEntryCallEvent(final AfterOperationEvent afterOperationEvent) {
		final TraceMetadata traceMetadata = this.registeredTraces.get(afterOperationEvent.getTraceId());
		final BeforeOperationEvent beforeOperationEvent = this.registeredBeforeOperationEvents.get(afterOperationEvent.getTraceId());
		if (beforeOperationEvent instanceof EntryLevelBeforeOperationEvent) {
			final EntryLevelBeforeOperationEvent entryLevelbeforeOperationEvent = (EntryLevelBeforeOperationEvent) beforeOperationEvent;
			return new EntryCallEvent(beforeOperationEvent.getTimestamp(), afterOperationEvent.getTimestamp(),
					beforeOperationEvent.getOperationSignature(), beforeOperationEvent.getClassSignature(),
					traceMetadata.getSessionId(), traceMetadata.getHostname(), entryLevelbeforeOperationEvent.getParameters(),
					entryLevelbeforeOperationEvent.getValues(),
					entryLevelbeforeOperationEvent.getRequestType());
		} else {
			return new EntryCallEvent(beforeOperationEvent.getTimestamp(), afterOperationEvent.getTimestamp(),
					beforeOperationEvent.getOperationSignature(), beforeOperationEvent.getClassSignature(),
					traceMetadata.getSessionId(), traceMetadata.getHostname(), new String[0], new String[0], 0);
		}
	}

}
