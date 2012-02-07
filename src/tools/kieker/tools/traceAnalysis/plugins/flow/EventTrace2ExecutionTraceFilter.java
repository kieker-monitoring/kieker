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

package kieker.tools.traceAnalysis.plugins.flow;

import java.util.Stack;

import kieker.analysis.plugin.port.InputPort;
import kieker.analysis.plugin.port.OutputPort;
import kieker.analysis.plugin.port.Plugin;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.flow.AfterOperationEvent;
import kieker.common.record.flow.BeforeOperationEvent;
import kieker.common.record.flow.CallOperationEvent;
import kieker.common.record.flow.TraceEvent;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;

/**
 * TODO: work in progress
 * 
 * @author Andre van Hoorn
 * 
 */
@Plugin(
		outputPorts = { @OutputPort(name = EventTrace2ExecutionTraceFilter.OUTPUT_EXECUTION_TRACE, description = "Outputs transformed execution traces", eventTypes = { ExecutionTrace.class }) })
public class EventTrace2ExecutionTraceFilter {
	private static final Log LOG = LogFactory.getLog(EventTrace2ExecutionTraceFilter.class);

	public static final String INPUT_EVENT_TRACE = "inputEventTrace";
	public static final String OUTPUT_EXECUTION_TRACE = "outputExecutionTrace";

	@InputPort(description = "Receives event record traces to be transformed", eventTypes = { TraceEvent.class })
	public void inputEventTrace(final EventRecordTrace eventTrace) {
		final Stack<TraceEvent> stack = new Stack<TraceEvent>();

		final long lastOrderIndex = -1; // used to check for ascending order indices
		final long curEoi = -1;
		final long curEss = -1;

		for (final TraceEvent e : eventTrace) {
			/* Make sure that order indices increment by 1 */
			if (e.getOrderIndex() != lastOrderIndex + 1) {
				EventTrace2ExecutionTraceFilter.LOG.error("Trace events' order indices must increment by one: " +
						" Found " + lastOrderIndex + " followed by " + e.getOrderIndex() + " event (" + e + ")");
				// TODO: output broken trace
			}

			if ((e instanceof CallOperationEvent) || (e instanceof BeforeOperationEvent)) {
				stack.push(e);
			}

			//

			if (e instanceof AfterOperationEvent) { // This will result in one or more Executions
				final AfterOperationEvent afterOpEvent = (AfterOperationEvent) e;

				final TraceEvent pushedEvent = stack.peek();
			}
		}
	}
}
