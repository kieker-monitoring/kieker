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
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.flow.trace.operation.CallOperationEvent;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * @author Reiner Jung
 * @since 1.15
 */
public class OperationAndCallGeneratorStage extends AbstractConsumerStage<IFlowRecord> {

	private final Map<Long, TraceMetadata> traceMetadataMap = new HashMap<>();
	private final Map<Long, Stack<OperationEvent>> traceStacksMap = new HashMap<>();

	private final OutputPort<OperationEvent> operationOutputPort = this.createOutputPort(OperationEvent.class);
	private final OutputPort<CallEvent> callOutputPort = this.createOutputPort(CallEvent.class);

	/**
	 * Create stage.
	 */
	public OperationAndCallGeneratorStage() {
		// empty default constructor
	}

	@Override
	protected void execute(final IFlowRecord element) {
		if (element instanceof TraceMetadata) {
			final TraceMetadata traceMetadata = (TraceMetadata) element;
			this.traceMetadataMap.put(traceMetadata.getTraceId(), traceMetadata);
			this.traceStacksMap.put(traceMetadata.getTraceId(), new Stack<OperationEvent>());
		} else if (element instanceof BeforeOperationEvent) {
			final BeforeOperationEvent beforeOperationEvent = (BeforeOperationEvent) element;
			final Stack<OperationEvent> stack = this.traceStacksMap.get(beforeOperationEvent.getTraceId());
			final TraceMetadata traceMetadata = this.traceMetadataMap.get(beforeOperationEvent.getTraceId());

			final OperationEvent newEvent = new OperationEvent(traceMetadata.getHostname(),
					beforeOperationEvent.getClassSignature(),
					beforeOperationEvent.getOperationSignature());
			final OperationEvent lastEvent = stack.peek();

			stack.push(newEvent);

			if (lastEvent != null) {
				this.callOutputPort.send(new CallEvent(lastEvent, newEvent));
			}
			this.operationOutputPort.send(newEvent);
		} else if (element instanceof AfterOperationEvent) {
			final AfterOperationEvent afterOperationEvent = (AfterOperationEvent) element;
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
			} else {
				this.logger.error("Trace stack error. AfterOperationEvent without a previous BeforeOperationEvent, found {}:{}",
						afterOperationEvent.getClassSignature(), afterOperationEvent.getOperationSignature());
			}
		} else if (element instanceof CallOperationEvent) {
			this.logger.error("Received CallOperationEvent which cannot be handled by the {}. Sanitize the trace before processing it.",
					this.getClass().getSimpleName());
		}
	}

	public OutputPort<CallEvent> getCallOutputPort() {
		return this.callOutputPort;
	}

	public OutputPort<OperationEvent> getOperationOutputPort() {
		return this.operationOutputPort;
	}
}
