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

import kieker.analysis.behavior.data.PayloadAwareEntryCallEvent;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

import teetime.stage.basic.AbstractTransformation;

/**
 * Creates entry level events from trace information.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
// make this generic for EntryCallEvent and PayloadAwareEntryCallEvent
public class CreateEntryLevelEventStage extends AbstractTransformation<IFlowRecord, PayloadAwareEntryCallEvent> {

	private final Map<Long, TraceMetadata> registeredTraces = new HashMap<>();
	private final Map<Long, BeforeOperationEvent> registeredBeforeOperationEvents = new HashMap<>();

	private final boolean waitForCompleteTrace;

	public CreateEntryLevelEventStage(final boolean waitForCompleteTrace) {
		this.waitForCompleteTrace = waitForCompleteTrace;
	}

	@Override
	protected void execute(final IFlowRecord element) throws Exception {
		if (element instanceof TraceMetadata) {
			this.registeredTraces.put(((TraceMetadata) element).getTraceId(), (TraceMetadata) element);
		}
		if (this.waitForCompleteTrace) {
			if (element instanceof BeforeOperationEvent) {
				final BeforeOperationEvent beforeOperationEvent = (BeforeOperationEvent) element;
				if (beforeOperationEvent.getOrderIndex() == 0) {
					if (this.registeredTraces.containsKey(beforeOperationEvent.getTraceId())) {
						this.registeredBeforeOperationEvents.put(beforeOperationEvent.getTraceId(), beforeOperationEvent);
					} else {
						this.logger.error("Received BeforeOperationEvent for unknown trace {}", beforeOperationEvent.getTraceId());
					}
				}
			} else if (element instanceof AfterOperationEvent) {
				final AfterOperationEvent afterOperationEvent = (AfterOperationEvent) element;
				if (afterOperationEvent.getOrderIndex() == 0) {
					if (this.registeredTraces.containsKey(afterOperationEvent.getTraceId())) {
						this.outputPort.send(this.createPayloadAwareEntryCallEvent(afterOperationEvent));
					} else {
						this.logger.error("Received AfterOperationEvent for unknown trace {}", afterOperationEvent.getTraceId());
					}
				}
			} else {
				if (element instanceof BeforeOperationEvent) {
					final BeforeOperationEvent beforeOperationEvent = (BeforeOperationEvent) element;
					if (beforeOperationEvent.getOrderIndex() == 0) {
						if (this.registeredTraces.containsKey(beforeOperationEvent.getTraceId())) {
							this.outputPort.send(this.createPayloadAwareEntryCallEvent(beforeOperationEvent));
							this.registeredTraces.remove(beforeOperationEvent.getTraceId());
						} else {
							this.logger.error("Received BeforeOperationEvent for unknown trace {}", beforeOperationEvent.getTraceId());
						}
					}
				}
			}
		}
	}

	// TODO this must be made changeable to EntryCallEvent and PAyloadAwareEntryCallEvent
	private PayloadAwareEntryCallEvent createPayloadAwareEntryCallEvent(final AfterOperationEvent afterOperationEvent) {
		final BeforeOperationEvent beforeOperationEvent = this.registeredBeforeOperationEvents.get(afterOperationEvent.getTraceId());
		final TraceMetadata traceMetadata = this.registeredTraces.get(afterOperationEvent.getTraceId());

		final String[] parameters = new String[0];
		final String[] values = new String[0];
		final int requestType = 0;
		return new PayloadAwareEntryCallEvent(beforeOperationEvent.getTimestamp(), afterOperationEvent.getTimestamp(),
				beforeOperationEvent.getOperationSignature(), beforeOperationEvent.getClassSignature(),
				traceMetadata.getSessionId(), traceMetadata.getHostname(), parameters, values, requestType);
	}

	// TODO this must be made changeable to EntryCallEvent and PAyloadAwareEntryCallEvent
	private PayloadAwareEntryCallEvent createPayloadAwareEntryCallEvent(final BeforeOperationEvent beforeOperationEvent) {
		final TraceMetadata traceMetadata = this.registeredTraces.get(beforeOperationEvent.getTraceId());

		final String[] parameters = new String[0];
		final String[] values = new String[0];
		final int requestType = 0;
		return new PayloadAwareEntryCallEvent(beforeOperationEvent.getTimestamp(), beforeOperationEvent.getTimestamp(),
				beforeOperationEvent.getOperationSignature(), beforeOperationEvent.getClassSignature(),
				traceMetadata.getSessionId(), traceMetadata.getHostname(), parameters, values, requestType);
	}

}
