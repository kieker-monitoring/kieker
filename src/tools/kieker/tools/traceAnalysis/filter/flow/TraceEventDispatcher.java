/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.tools.traceAnalysis.filter.flow;

import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.concurrency.SplitEvent;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.flow.trace.operation.CallOperationEvent;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public class TraceEventDispatcher {
	/**
	 * 
	 * @author Holger Knoche
	 * 
	 */
	public static interface IAbstractTraceEventVisitor {

		public void handleAfterOperationEvent(AfterOperationEvent afterOperationEvent);

		public void handleAfterOperationFailedEvent(AfterOperationFailedEvent afterOperationFailedEvent);

		public void handleBeforeOperationEvent(BeforeOperationEvent beforeOperationEvent);

		public void handleCallOperationEvent(CallOperationEvent callOperationEvent);

		public void handleSplitEvent(SplitEvent splitEvent);

		public void handleUnsupportedEvent(AbstractTraceEvent unsupportedEvent);

	}

	private final IAbstractTraceEventVisitor visitor;

	public TraceEventDispatcher(final IAbstractTraceEventVisitor visitor) {
		this.visitor = visitor;
	}

	public void dispatch(final AbstractTraceEvent event) {
		if (event instanceof SplitEvent) {
			this.visitor.handleSplitEvent((SplitEvent) event);
		} else if (event instanceof AfterOperationEvent) {
			this.visitor.handleAfterOperationEvent((AfterOperationEvent) event);
		} else if (event instanceof AfterOperationFailedEvent) {
			this.visitor.handleAfterOperationFailedEvent((AfterOperationFailedEvent) event);
		} else if (event instanceof BeforeOperationEvent) {
			this.visitor.handleBeforeOperationEvent((BeforeOperationEvent) event);
		} else if (event instanceof CallOperationEvent) {
			this.visitor.handleCallOperationEvent((CallOperationEvent) event);
		} else {
			this.visitor.handleUnsupportedEvent(event);
		}
	}
}
