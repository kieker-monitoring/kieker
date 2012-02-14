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
import java.util.List;
import java.util.Stack;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.flow.AfterOperationEvent;
import kieker.common.record.flow.BeforeOperationEvent;
import kieker.common.record.flow.CallOperationEvent;
import kieker.common.record.flow.TraceEvent;
import kieker.tools.traceAnalysis.plugins.AbstractTraceAnalysisPlugin;
import kieker.tools.traceAnalysis.plugins.AbstractTraceProcessingPlugin;
import kieker.tools.traceAnalysis.plugins.traceReconstruction.InvalidTraceException;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
@Plugin(
		outputPorts = {
			@OutputPort(name = EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE, description = "Outputs transformed execution traces", eventTypes = { ExecutionTrace.class }),
			@OutputPort(name = EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, description = "Outputs transformed message traces", eventTypes = { MessageTrace.class })
		},
		repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, repositoryType = SystemModelRepository.class))
public class EventTrace2ExecutionTraceFilter extends AbstractTraceProcessingPlugin {

	private static final Log LOG = LogFactory.getLog(EventTrace2ExecutionTraceFilter.class);

	public static final String INPUT_PORT_NAME = "inputEventTrace";
	public static final String OUTPUT_PORT_NAME_EXECUTION_TRACE = "outputExecutionTrace";
	public static final String OUTPUT_PORT_NAME_MESSAGE_TRACE = "outputMessageTrace";

	public EventTrace2ExecutionTraceFilter(final Configuration configuration) {
		super(configuration);
	}

	@InputPort(name = EventTrace2ExecutionTraceFilter.INPUT_PORT_NAME, description = "Receives event record traces to be transformed", eventTypes = { EventRecordTrace.class })
	public void inputEventTrace(final EventRecordTrace eventTrace) {
		final Stack<TraceEvent> eventStack = new Stack<TraceEvent>();

		final ExecutionTrace execTrace = new ExecutionTrace(eventTrace.getTraceId());

		long lastOrderIndex = -1; // used to check for ascending order indices
		final Stack<Integer> eoiStack = new Stack<Integer>();
		int nextEoi = 0;
		int curEss = -1;

		for (final TraceEvent e : eventTrace) {
			/* Make sure that order indices increment by 1 */
			if (e.getOrderIndex() != (lastOrderIndex + 1)) {
				EventTrace2ExecutionTraceFilter.LOG.error("Trace events' order indices must increment by one: " +
						" Found " + lastOrderIndex + " followed by " + e.getOrderIndex() + " event (" + e + ")");
				EventTrace2ExecutionTraceFilter.LOG.error("Terminating processing of event record trace with ID " + eventTrace.getTraceId());
				// TODO: output broken event record trace
				return;
			} else {
				lastOrderIndex = e.getOrderIndex(); // i.e., lastOrderIndex++
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
							// EventTrace2ExecutionTraceFilter.LOG.info("Current Event: " + curBeforeOperationEvent.toString() + ", redundant call discarded: "
							// + peekCallEvent.toString());
							/* Redundant call event on top of stack */
							eventStack.pop();
							eoiStack.pop();
							nextEoi--;
							curEss--;
						}
					}
				}
			} else if (e instanceof CallOperationEvent) {
				if (!eventStack.isEmpty()) {
					final TraceEvent peekEvent = eventStack.peek();
					final CallOperationEvent curCallEvent = (CallOperationEvent) e;
					if (peekEvent instanceof CallOperationEvent) {
						final CallOperationEvent peekCallEvent = (CallOperationEvent) eventStack.peek();
						// TODO: Consider hostname etc.
						if (curCallEvent.getCallerOperationName().equals(peekCallEvent.getCallerOperationName())) {
							/* If two subsequent calls from the same caller, we assume the first call to have returned */
							eventStack.pop();
							final Execution execution = this.callOperationToExecution(peekCallEvent, execTrace.getTraceId(),
									eoiStack.pop(), curEss--, peekCallEvent.getTimestamp(), e.getTimestamp());
							try {
								execTrace.add(execution);
							} catch (final InvalidTraceException ex) {
								EventTrace2ExecutionTraceFilter.LOG.error("Failed to add execution " + execution + " to trace" + execTrace, ex);
								// TODO: perhaps output broken event record trace
								return;
							}
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
						EventTrace2ExecutionTraceFilter.LOG.error("Failed to add execution " + exec + " to trace" + execTrace, ex);
						return;
					}
				}
			} else {
				EventTrace2ExecutionTraceFilter.LOG.warn("Trace Events of type " + e.getClass().getName() + " not supported, yet");
				// EventTrace2ExecutionTraceFilter.LOG.error("Terminating processing of event record trace with ID " + eventTrace.getTraceId());
				// TODO: output broken event record trace
			}
		}

		super.deliver(EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE, execTrace);
		try {
			super.deliver(EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, execTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION));
			super.reportSuccess(execTrace.getTraceId());
		} catch (final InvalidTraceException ex) {
			// TODO send to new output port for defect traces
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

		final FQComponentNameSignaturePair fqComponentNameSignaturePair = super.splitOperationSignatureStr(event.getOperationName());

		return super.createExecutionByEntityNames(hostName, fqComponentNameSignaturePair.getFqClassname(), fqComponentNameSignaturePair.getSignature(),
				traceId,
				sessionId, eoi, ess, tin, tout);
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

		final FQComponentNameSignaturePair fqComponentNameSignaturePair = super.splitOperationSignatureStr(event.getCalleeOperationName());
		return super.createExecutionByEntityNames(hostName, fqComponentNameSignaturePair.getFqClassname(),
				fqComponentNameSignaturePair.getSignature(), traceId, sessionId, eoi, ess, tin, tout);
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration(null);
	}

}
