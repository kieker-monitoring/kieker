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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import kieker.analysis.plugin.port.InputPort;
import kieker.analysis.plugin.port.OutputPort;
import kieker.analysis.plugin.port.Plugin;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.flow.AfterOperationEvent;
import kieker.common.record.flow.BeforeOperationEvent;
import kieker.common.record.flow.CallOperationEvent;
import kieker.common.record.flow.TraceEvent;
import kieker.tools.traceAnalysis.plugins.AbstractTraceAnalysisPlugin;
import kieker.tools.traceAnalysis.plugins.traceReconstruction.InvalidTraceException;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;

/**
 * TODO: work in progress
 * 
 * @author Andre van Hoorn
 * 
 */
@Plugin(
		outputPorts = { @OutputPort(name = EventTrace2ExecutionTraceFilter.OUTPUT_EXECUTION_TRACE, description = "Outputs transformed execution traces", eventTypes = { ExecutionTrace.class }) })
public class EventTrace2ExecutionTraceFilter extends AbstractTraceAnalysisPlugin {

	private static final Log LOG = LogFactory.getLog(EventTrace2ExecutionTraceFilter.class);

	public static final String INPUT_EVENT_TRACE = "inputEventTrace";
	public static final String OUTPUT_EXECUTION_TRACE = "outputExecutionTrace";

	public EventTrace2ExecutionTraceFilter(final Configuration configuration, final Map<String, AbstractRepository> repositories) {
		super(configuration, repositories);
		// TODO: extract repository
	}

	@InputPort(description = "Receives event record traces to be transformed", eventTypes = { TraceEvent.class })
	public void inputEventTrace(final EventRecordTrace eventTrace) {
		final Stack<TraceEvent> eventStack = new Stack<TraceEvent>();

		final ExecutionTrace execTrace = new ExecutionTrace(eventTrace.getTraceId());

		final long lastOrderIndex = -1; // used to check for ascending order indices
		final Stack<Integer> eoiStack = new Stack<Integer>();
		int nextEoi = 0;
		int curEss = -1;

		for (final TraceEvent e : eventTrace) {
			/* Make sure that order indices increment by 1 */
			if (e.getOrderIndex() != lastOrderIndex + 1) {
				EventTrace2ExecutionTraceFilter.LOG.error("Trace events' order indices must increment by one: " +
						" Found " + lastOrderIndex + " followed by " + e.getOrderIndex() + " event (" + e + ")");
				EventTrace2ExecutionTraceFilter.LOG.error("Terminating processing of event record trace with ID " + eventTrace.getTraceId());
				// TODO: output broken event record trace
				return;
			}

			if (e instanceof BeforeOperationEvent) {
				/*
				 * This step serves to detect (and remove) redundant call events, i.e., a call event
				 * followed by a before event from the call event's callee operation.
				 */
				final BeforeOperationEvent curBeforeOperationEvent = (BeforeOperationEvent) e;
				if (!eventStack.isEmpty()) {
					final TraceEvent peekEvent = eventStack.peek();
					if (peekEvent instanceof CallOperationEvent) {
						final CallOperationEvent peekCallEvent = (CallOperationEvent) peekEvent;
						// TODO: we need to consider the host name as well
						if (peekCallEvent.getCalleeOperationName().equals(curBeforeOperationEvent.getOperationName())) {
							/* Redundant call event on top of stack */
							eventStack.pop();
							eoiStack.pop();
							nextEoi--;
							curEss--;
						}
					}
				}
			}

			if ((e instanceof CallOperationEvent) || (e instanceof BeforeOperationEvent)) {
				eoiStack.push(nextEoi++);
				eventStack.push(e);
				curEss++;
			} else if (e instanceof AfterOperationEvent) { // This will result in one or more Executions
				final AfterOperationEvent afterOpEvent = (AfterOperationEvent) e;

				final List<Execution> execsToAdd = new ArrayList<Execution>(2);

				/* 1. The peek event must be the Before event corresponding to e */
				{
					final TraceEvent poppedEvent = eventStack.pop();
					if (!(poppedEvent instanceof BeforeOperationEvent)) {
						EventTrace2ExecutionTraceFilter.LOG.error("Didn't find corresponding BeforeOperationEvent for AfterOperationEvent " + afterOpEvent +
								" (found: )" + poppedEvent);
						EventTrace2ExecutionTraceFilter.LOG.error("Terminating processing of event record trace with ID " + eventTrace.getTraceId());
						// TODO: output broken event record trace
						return;
					}
					final BeforeOperationEvent poppedBeforeEvent = (BeforeOperationEvent) poppedEvent;
					// TODO: we need to consider the host name as well
					if (!afterOpEvent.getOperationName().equals(poppedBeforeEvent.getOperationName())) {
						EventTrace2ExecutionTraceFilter.LOG.error("Components of before (" + poppedBeforeEvent + ") " +
								"and after (" + afterOpEvent + ") events do not match");
						EventTrace2ExecutionTraceFilter.LOG.error("Terminating processing of event record trace with ID " + eventTrace.getTraceId());
						// TODO: output broken event record trace
						return;
					}

					final Execution execution = this.beforeOperationToExecution(poppedBeforeEvent, execTrace.getTraceId(),
							eoiStack.pop(), curEss--, poppedBeforeEvent.getTimestamp(), e.getTimestamp());
					execsToAdd.add(execution);
				}

				/* 2. Now, we might find a CallOperationEvent for which we need to create an Execution */
				if (!eventStack.isEmpty() && (eventStack.peek() instanceof CallOperationEvent)) {
					final CallOperationEvent poppedCallEvent = (CallOperationEvent) eventStack.pop();
					final Execution execution = this.callOperationToExecution(poppedCallEvent, execTrace.getTraceId(),
							eoiStack.pop(), curEss--, poppedCallEvent.getTimestamp(), e.getTimestamp());
					execsToAdd.add(execution);
				}

				/* Add created execution to trace */
				for (final Execution exec : execsToAdd) {
					try {
						execTrace.add(exec);
					} catch (final InvalidTraceException ex) {
						EventTrace2ExecutionTraceFilter.LOG.error("Failed to add execution " + exec + " to trace" + exec);
					}
				}
			} else {
				EventTrace2ExecutionTraceFilter.LOG.error("Trace Events of type " + e.getClass().getName() + " not supported, yet");
				EventTrace2ExecutionTraceFilter.LOG.error("Terminating processing of event record trace with ID " + eventTrace.getTraceId());
				// TODO: output broken event record trace
				return;
			}
		}
	}

	/**
	 * 
	 * @param event
	 * @param traceId
	 * @param eoi
	 * @param ess
	 * @param tin
	 * @param tout
	 * @return
	 */
	private Execution beforeOperationToExecution(final BeforeOperationEvent event, final long traceId, final int eoi, final int ess,
			final long tin, final long tout) {
		// TODO: hostname and sessionid
		final String hostName = "<HOST>";
		final String sessionId = "<SESSION-ID>";

		final String[] fqOpSplit = this.splitFQOperationName(event.getOperationName());
		final String fqClassName = fqOpSplit[0];
		final String opSignature = fqOpSplit[1];

		return super.createExecutionByEntityNames(hostName, fqClassName, opSignature, traceId, sessionId, eoi, ess, tin, tout);
	}

	/**
	 * 
	 * @param event
	 * @param traceId
	 * @param eoi
	 * @param ess
	 * @param tin
	 * @param tout
	 * @return
	 */
	private Execution callOperationToExecution(final CallOperationEvent event, final long traceId, final int eoi, final int ess,
			final long tin, final long tout) {
		// TODO: hostname and sessionid
		final String hostName = "<HOST>";
		final String sessionId = "<SESSION-ID>";

		final String[] fqOpSplit = this.splitFQOperationName(event.getOperationName());
		final String fqClassName = fqOpSplit[0];
		final String opSignature = fqOpSplit[1];

		return super.createExecutionByEntityNames(hostName, fqClassName, opSignature, traceId, sessionId, eoi, ess, tin, tout);
	}

	/**
	 * Splits a fully qualified operation of format <code>"a.b.c.D.op(args)"</code> into
	 * an array with two elements: {"a.b.c.D", "op(args")}
	 * 
	 * @param fqOpname
	 * @return
	 */
	protected String[] splitFQOperationName(final String fqOpname) {
		final String fqClassName;
		final String opSignature;

		final int posParen = fqOpname.lastIndexOf('(');
		int posDot;
		if (posParen != -1) {
			posDot = fqOpname.substring(0, posParen).lastIndexOf('.');
		} else {
			posDot = fqOpname.lastIndexOf('.');
		}
		if (posDot == -1) {
			fqClassName = "";
			opSignature = fqOpname;
		} else {
			fqClassName = fqOpname.substring(0, posDot);
			opSignature = fqOpname.substring(posDot + 1);
		}

		return new String[] { fqClassName, opSignature };
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration(null);
	}

	@Override
	public boolean execute() {
		return true; // do nothing
	}

	@Override
	public void terminate(final boolean error) {
		// do nothing
	}

	@Override
	protected Map<String, AbstractRepository> getDefaultRepositories() {
		return new HashMap<String, AbstractRepository>();
	}

	@Override
	public Map<String, AbstractRepository> getCurrentRepositories() {
		return new HashMap<String, AbstractRepository>();
	}
}
