/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.architecture.trace.flow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.architecture.recovery.events.OperationEvent;
import kieker.analysis.generic.IControlEventMatcher;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

/**
 * Control event matcher for flow events, controlled by OperationEvents.
 *
 * @author Reiner Jung
 * @since 1.15
 */
public class FlowTraceEventMatcher implements IControlEventMatcher<OperationEvent, IFlowRecord> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FlowTraceEventMatcher.class);

	public FlowTraceEventMatcher() {
		// default constructor.
	}

	@Override
	public boolean requiresControlEvent(final IFlowRecord baseEvent) {
		return !(baseEvent instanceof TraceMetadata);
	}

	@Override
	public boolean checkControlEvent(final OperationEvent controlEvent, final IFlowRecord baseEvent) {
		if (baseEvent instanceof BeforeOperationEvent) {
			final BeforeOperationEvent beforeOperationEvent = (BeforeOperationEvent) baseEvent;
			return controlEvent.getComponentSignature().equals(beforeOperationEvent.getClassSignature())
					&& controlEvent.getOperationSignature().equals(beforeOperationEvent.getOperationSignature());
		} else if (baseEvent instanceof AfterOperationEvent) {
			final AfterOperationEvent afterOperationEvent = (AfterOperationEvent) baseEvent;
			return controlEvent.getComponentSignature().equals(afterOperationEvent.getClassSignature())
					&& controlEvent.getOperationSignature().equals(afterOperationEvent.getOperationSignature());
		} else {
			FlowTraceEventMatcher.LOGGER.error("Events of type {} are not supported.", baseEvent.getClass().getSimpleName());
		}
		return false;
	}

	@Override
	public boolean keepControlEvent(final IFlowRecord baseEvent) {
		return baseEvent instanceof BeforeOperationEvent;
	}
}
