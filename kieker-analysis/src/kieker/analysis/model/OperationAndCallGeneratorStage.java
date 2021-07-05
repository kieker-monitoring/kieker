/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import kieker.analysis.model.data.CallEvent;
import kieker.analysis.model.data.OperationEvent;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.flow.trace.operation.CallOperationEvent;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Produce operation and operation call events based on flow records. This stage requires structurally intact traces.
 *
 * @author Reiner Jung
 * @since 1.15
 */
public class OperationAndCallGeneratorStage extends AbstractConsumerStage<IFlowRecord> {

	private final Map<Long, TraceMetadata> traceMetadataMap = new HashMap<>();
	private final Map<Long, Stack<OperationEvent>> traceStacksMap = new HashMap<>();

	private final OutputPort<OperationEvent> operationOutputPort = this.createOutputPort(OperationEvent.class);
	private final OutputPort<CallEvent> callOutputPort = this.createOutputPort(CallEvent.class);
	private final boolean createEntryCall;

	/**
	 * Create stage.
	 */
	public OperationAndCallGeneratorStage(final boolean createEntryCall) {
		this.createEntryCall = createEntryCall;
	}

	@Override
	protected void execute(final IFlowRecord element) {
		if (element instanceof TraceMetadata) {
			this.processTraceMetadata((TraceMetadata) element);
		} else if (element instanceof BeforeOperationEvent) {
			this.processBeforeOperationEvent((BeforeOperationEvent) element);
		} else if (element instanceof AfterOperationFailedEvent) {
			this.processAfterOperationFailedEvent((AfterOperationFailedEvent) element);
		} else if (element instanceof AfterOperationEvent) {
			this.processAfterOperationEvent((AfterOperationEvent) element);
		} else if (element instanceof CallOperationEvent) {
			this.logger.error("Received CallOperationEvent which cannot be handled by the {}. Sanitize the trace before processing it.",
					this.getClass().getSimpleName());
		}
	}

	private void processTraceMetadata(final TraceMetadata traceMetadata) {
		this.traceMetadataMap.put(traceMetadata.getTraceId(), traceMetadata);
		this.traceStacksMap.put(traceMetadata.getTraceId(), new Stack<OperationEvent>());
	}

	private void processBeforeOperationEvent(final BeforeOperationEvent beforeOperationEvent) {
		final Stack<OperationEvent> stack = this.traceStacksMap.get(beforeOperationEvent.getTraceId());
		final TraceMetadata traceMetadata = this.traceMetadataMap.get(beforeOperationEvent.getTraceId());

		final OperationEvent newEvent = new OperationEvent(traceMetadata.getHostname(),
				beforeOperationEvent.getClassSignature(),
				beforeOperationEvent.getOperationSignature());
		this.operationOutputPort.send(newEvent);
		if (!stack.empty()) {
			this.callOutputPort.send(new CallEvent(stack.peek(), newEvent));
		} else if (this.createEntryCall) {
			final OperationEvent triggerEvent = new OperationEvent("external", "<unknown>", "<unknown>");
			this.operationOutputPort.send(triggerEvent);
			this.callOutputPort.send(new CallEvent(triggerEvent, newEvent));
		}
		stack.push(newEvent);
	}

	private void processAfterOperationEvent(final AfterOperationEvent afterOperationEvent) {
		final Stack<OperationEvent> stack = this.traceStacksMap.get(afterOperationEvent.getTraceId());
		if (!stack.isEmpty()) {
			final OperationEvent lastEvent = stack.pop();
			if (!lastEvent.getComponentSignature().equals(afterOperationEvent.getClassSignature())
					|| !lastEvent.getOperationSignature().equals(afterOperationEvent.getOperationSignature())) {
				this.logger.error("Broken trace, expected {}:{}, but got {}:{}",
						lastEvent.getComponentSignature(),
						lastEvent.getOperationSignature(),
						afterOperationEvent.getClassSignature(),
						afterOperationEvent.getOperationSignature());
			}
			if (stack.isEmpty()) { // trace is complete
				this.traceStacksMap.remove(afterOperationEvent.getTraceId());
				this.traceMetadataMap.remove(afterOperationEvent.getTraceId());
			}
		} else {
			this.logger.error("Trace stack error. AfterOperationEvent without a previous BeforeOperationEvent, found {}:{}",
					afterOperationEvent.getClassSignature(), afterOperationEvent.getOperationSignature());
		}
	}

	/**
	 * A failed event indicates the end of a trace. Thus, the stack becomes invalid.
	 *
	 * @param afterOperationFailedEvent
	 *            failed event to be processed
	 */
	private void processAfterOperationFailedEvent(final AfterOperationFailedEvent afterOperationFailedEvent) {
		this.traceStacksMap.remove(afterOperationFailedEvent.getTraceId());
		this.traceMetadataMap.remove(afterOperationFailedEvent.getTraceId());
	}

	public OutputPort<CallEvent> getCallOutputPort() {
		return this.callOutputPort;
	}

	public OutputPort<OperationEvent> getOperationOutputPort() {
		return this.operationOutputPort;
	}
}
