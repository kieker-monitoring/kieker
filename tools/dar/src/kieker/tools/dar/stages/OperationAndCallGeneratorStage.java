/***************************************************************************
 * Copyright (C) 2023 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.dar.stages;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import kieker.analysis.architecture.recovery.events.CallEvent;
import kieker.analysis.architecture.recovery.events.OperationEvent;
import kieker.analysis.architecture.recovery.signature.AbstractSignatureProcessor;
import kieker.analysis.code.CodeUtils;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.flow.trace.operation.CallOperationEvent;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Produce operation and operation call events based on flow records. This stage requires
 * structurally intact traces.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
// TODO merge this class after fixing with Kieker code base
public class OperationAndCallGeneratorStage extends AbstractConsumerStage<IFlowRecord> {

	private final Map<Long, TraceData> traceDataMap = new ConcurrentHashMap<>();

	private final OutputPort<OperationEvent> operationOutputPort = this.createOutputPort(OperationEvent.class);
	private final OutputPort<CallEvent> callOutputPort = this.createOutputPort(CallEvent.class);
	private final boolean createEntryCall;

	private final List<AbstractSignatureProcessor> processors;

	private final boolean removeMetaDataOnCompletedTraces;

	/**
	 * Create stage.
	 *
	 * @param createEntryCall
	 *            true, if the caller of the entry calls should be synthesized
	 * @param processors
	 *            process signature strings
	 * @param removeMetaDataOnCompletedTraces
	 *            assume that the meta data of a trace is no longe needed when it is completed
	 */
	public OperationAndCallGeneratorStage(final boolean createEntryCall,
			final List<AbstractSignatureProcessor> processors, final boolean removeMetaDataOnCompletedTraces) {
		super();
		this.createEntryCall = createEntryCall;
		this.processors = processors;
		this.removeMetaDataOnCompletedTraces = removeMetaDataOnCompletedTraces;
	}

	/**
	 * Create stage.
	 *
	 * @param createEntryCall
	 *            true, if the caller of the entry calls should be synthesized
	 * @param removeMetaDataOnCompletedTraces
	 *            assume that the meta data of a trace is no longe needed when it is completed
	 */
	public OperationAndCallGeneratorStage(final boolean createEntryCall,
			final boolean removeMetaDataOnCompletedTraces) {
		this(createEntryCall, new ArrayList<>(), removeMetaDataOnCompletedTraces);
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
			this.logger.error(
					"Received CallOperationEvent which cannot be handled by the {}. Sanitize the trace before processing it.",
					this.getClass().getSimpleName());
		}
	}

	private void processTraceMetadata(final TraceMetadata traceMetadata) {
		this.traceDataMap.put(traceMetadata.getTraceId(), new TraceData(traceMetadata, new Stack<>()));
	}

	private void processBeforeOperationEvent(final BeforeOperationEvent beforeOperationEvent) {
		final TraceData traceData = this.traceDataMap.get(beforeOperationEvent.getTraceId());

		if (traceData == null) {
			this.logger.error("Found {} with trace id {}, but no trace metadata found. Time index: {}",
					beforeOperationEvent.getClass().getCanonicalName(), beforeOperationEvent.getTraceId(),
					beforeOperationEvent.getTimestamp());
		} else {
			String componentSignature = beforeOperationEvent.getClassSignature();
			String operationSignature = beforeOperationEvent.getOperationSignature();

			for (final AbstractSignatureProcessor processor : this.processors) {
				processor.processSignatures(componentSignature, operationSignature);
				componentSignature = processor.getComponentSignature();
				operationSignature = processor.getOperationSignature();
			}

			final OperationEvent newEvent = new OperationEvent(traceData.getMetadata().getHostname(),
					componentSignature, operationSignature);
			if (!traceData.getOperationStack().empty()) {
				this.operationOutputPort.send(newEvent);
			} else if (this.createEntryCall) {
				final OperationEvent triggerEvent = new OperationEvent("external", CodeUtils.UNKNOWN_COMPONENT,
						CodeUtils.UNKNOWN_OPERATION);
				this.operationOutputPort.send(triggerEvent);
				this.operationOutputPort.send(newEvent);
				traceData.getOperationStack().push(triggerEvent);
				traceData.getStartTimeStack().push(Instant.ofEpochSecond(0, beforeOperationEvent.getTimestamp()));
			} else {
				this.operationOutputPort.send(newEvent);
			}
			traceData.getOperationStack().push(newEvent);
			traceData.getStartTimeStack().push(Instant.ofEpochSecond(0, beforeOperationEvent.getTimestamp()));
		}
	}

	private void processAfterOperationEvent(final AfterOperationEvent afterOperationEvent) {
		final TraceData traceData = this.traceDataMap.get(afterOperationEvent.getTraceId());

		if (traceData == null) {
			this.logger.error("Found {} with trace id {}, but no trace metadata found. Time index: {}",
					afterOperationEvent.getClass().getCanonicalName(), afterOperationEvent.getTraceId(),
					afterOperationEvent.getTimestamp());
		} else {
			final Stack<OperationEvent> stack = traceData.getOperationStack();
			if (!stack.isEmpty()) {
				final OperationEvent lastEvent = stack.pop();

				String componentSignature = afterOperationEvent.getClassSignature();
				String operationSignature = afterOperationEvent.getOperationSignature();

				for (final AbstractSignatureProcessor processor : this.processors) {
					processor.processSignatures(componentSignature, operationSignature);
					componentSignature = processor.getComponentSignature();
					operationSignature = processor.getOperationSignature();
				}

				if (!lastEvent.getComponentSignature().equals(componentSignature)
						|| !lastEvent.getOperationSignature().equals(operationSignature)) {
					this.logger.error("Broken trace, expected {}:{}, but got {}:{}", lastEvent.getComponentSignature(),
							lastEvent.getOperationSignature(), componentSignature, operationSignature);
				}
				if (stack.isEmpty()) { // trace is complete
					if (this.removeMetaDataOnCompletedTraces) {
						this.traceDataMap.remove(afterOperationEvent.getTraceId());
					}
				} else {
					final OperationEvent previousEvent = stack.peek();
					final Instant end = Instant.ofEpochSecond(0, afterOperationEvent.getTimestamp());
					this.callOutputPort.send(new CallEvent(previousEvent, lastEvent,
							Duration.between(traceData.getStartTimeStack().pop(), end)));
				}
			} else {
				this.logger.error(
						"Trace stack error. AfterOperationEvent without a previous BeforeOperationEvent, found {}:{}",
						afterOperationEvent.getClassSignature(), afterOperationEvent.getOperationSignature());
			}
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
				this.callOutputPort.send(new CallEvent(previousEvent, lastEvent,
						Duration.between(traceData.getStartTimeStack().pop(), end)));
			}
		}
		if (this.removeMetaDataOnCompletedTraces) {
			this.traceDataMap.remove(afterOperationFailedEvent.getTraceId());
		}
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
	 * @since 2.0.0
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
