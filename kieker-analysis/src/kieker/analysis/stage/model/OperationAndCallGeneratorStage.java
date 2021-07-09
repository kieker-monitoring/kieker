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
package kieker.analysis.stage.model;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import kieker.analysis.stage.model.data.CallEvent;
import kieker.analysis.stage.model.data.OperationEvent;
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

	private final Map<Long, TraceData> traceDataMap = new ConcurrentHashMap<>();

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
		this.traceDataMap.put(traceMetadata.getTraceId(), new TraceData(traceMetadata, new Stack<OperationEvent>()));
	}

	private void processBeforeOperationEvent(final BeforeOperationEvent beforeOperationEvent) {
		final TraceData traceData = this.traceDataMap.get(beforeOperationEvent.getTraceId());

		final OperationEvent newEvent = new OperationEvent(traceData.getMetadata().getHostname(),
				beforeOperationEvent.getClassSignature(),
				beforeOperationEvent.getOperationSignature());
		if (!traceData.getOperationStack().empty()) {
			this.operationOutputPort.send(newEvent);
		} else {
			if (this.createEntryCall) {
				final OperationEvent triggerEvent = new OperationEvent("external", "<unknown>", "<unknown>");
				this.operationOutputPort.send(triggerEvent);
				this.operationOutputPort.send(newEvent);
				traceData.getOperationStack().push(triggerEvent);
				traceData.getStartTimeStack().push(Instant.ofEpochSecond(0, beforeOperationEvent.getTimestamp()));
			} else {
				this.operationOutputPort.send(newEvent);
			}
		}
		traceData.getOperationStack().push(newEvent);
		traceData.getStartTimeStack().push(Instant.ofEpochSecond(0, beforeOperationEvent.getTimestamp()));
	}

	private void processAfterOperationEvent(final AfterOperationEvent afterOperationEvent) {
		final TraceData traceData = this.traceDataMap.get(afterOperationEvent.getTraceId());
		final Stack<OperationEvent> stack = traceData.getOperationStack();
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
				this.traceDataMap.remove(afterOperationEvent.getTraceId());
			} else {
				final OperationEvent previousEvent = stack.peek();
				final Instant end = Instant.ofEpochSecond(0, afterOperationEvent.getTimestamp());
				this.callOutputPort.send(new CallEvent(previousEvent, lastEvent, Duration.between(traceData.getStartTimeStack().pop(), end)));
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
		final TraceData traceData = this.traceDataMap.get(afterOperationFailedEvent.getTraceId());
		final Stack<OperationEvent> stack = traceData.getOperationStack();
		if (!stack.isEmpty()) {
			final OperationEvent lastEvent = stack.pop();
			if (!stack.isEmpty()) {
				final OperationEvent previousEvent = stack.peek();
				final Instant end = Instant.ofEpochSecond(0, afterOperationFailedEvent.getTimestamp());
				this.callOutputPort.send(new CallEvent(previousEvent, lastEvent, Duration.between(traceData.getStartTimeStack().pop(), end)));
			}
		}
		this.traceDataMap.remove(afterOperationFailedEvent.getTraceId());
	}

	public OutputPort<CallEvent> getCallOutputPort() {
		return this.callOutputPort;
	}

	public OutputPort<OperationEvent> getOperationOutputPort() {
		return this.operationOutputPort;
	}

	/**
	 *
	 * @author Reiner Jung
	 * @since 1.15
	 */
	private final class TraceData {

		private final TraceMetadata metadata;
		private final Stack<OperationEvent> operationStack;
		private final Stack<Instant> startTimeStack;

		private TraceData(final TraceMetadata metadata, final Stack<OperationEvent> operationStack) {
			this.metadata = metadata;
			this.operationStack = operationStack;
			this.startTimeStack = new Stack<>();
		}

		public TraceMetadata getMetadata() {
			return this.metadata;
		}

		public Stack<OperationEvent> getOperationStack() {
			return this.operationStack;
		}

		public Stack<Instant> getStartTimeStack() {
			return this.startTimeStack;
		}

	}
}
