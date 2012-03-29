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

import java.util.Stack;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.IAbstractTraceEventVisitor;
import kieker.common.record.flow.trace.concurrency.SplitEvent;
import kieker.common.record.flow.trace.operation.AbstractOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.flow.trace.operation.CallOperationEvent;
import kieker.common.util.ClassOperationSignaturePair;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.traceReconstruction.InvalidTraceException;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * This filter converts an event-based trace to an execution-based trace.
 * 
 * @author Andre van Hoorn, Holger Knoche
 * 
 */
@Plugin(
		outputPorts = {
			@OutputPort(name = EventTrace2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE, description = "Outputs transformed execution traces", eventTypes = { ExecutionTrace.class }),
			@OutputPort(name = EventTrace2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, description = "Outputs transformed message traces", eventTypes = { MessageTrace.class })
		},
		repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class))
public class EventTrace2ExecutionAndMessageTraceFilter extends AbstractTraceProcessingFilter {

	private static final Log LOG = LogFactory.getLog(EventTrace2ExecutionAndMessageTraceFilter.class);

	public static final String INPUT_PORT_NAME_EVENT_TRACE = "eventTrace";

	public static final String OUTPUT_PORT_NAME_EXECUTION_TRACE = "executionTrace";
	public static final String OUTPUT_PORT_NAME_MESSAGE_TRACE = "messageTrace";

	/**
	 * This class stores information about a specific execution.
	 */
	private static class ExecutionInformation {

		private final int executionIndex;
		private final int stackDepth;

		public ExecutionInformation(final int executionIndex, final int stackDepth) {
			this.executionIndex = executionIndex;
			this.stackDepth = stackDepth;
		}

		/**
		 * Returns the execution's execution order index.
		 * 
		 * @return See above
		 */
		public int getExecutionIndex() {
			return this.executionIndex;
		}

		/**
		 * Returns the stack depth at which the execution occurred.
		 * 
		 * @return See above
		 */
		public int getStackDepth() {
			return this.stackDepth;
		}

	}

	/**
	 * This class encapsulates the filter's state, that is, the current event and execution stacks.
	 */
	private static class FilterState {

		private final Stack<AbstractTraceEvent> eventStack = new Stack<AbstractTraceEvent>();
		private final Stack<ExecutionInformation> executionStack = new Stack<ExecutionInformation>();

		private int nextExecutionIndex = 0;
		private int currentStackDepth = 0;

		/**
		 * Pushes the given event onto the event stack.
		 * 
		 * @param event
		 *            The event to push
		 */
		public void pushEvent(final AbstractTraceEvent event) {
			this.eventStack.push(event);
		}

		/**
		 * Returns the event currently on top of the event stack.
		 * 
		 * @return See above
		 */
		public AbstractTraceEvent peekEvent() {
			return this.eventStack.peek();
		}

		/**
		 * Pops the event off the top of the stack.
		 * 
		 * @return The topmost event from the event stack
		 */
		public AbstractTraceEvent popEvent() {
			return this.eventStack.pop();
		}

		/**
		 * Returns whether the event stack is empty.
		 * 
		 * @return <code>True</code> if the stack is empty, <code>false</code> otherwise
		 */
		public boolean isEventStackEmpty() {
			return this.eventStack.isEmpty();
		}

		/**
		 * Registers an execution caused by the given event. Note that in the process, the given event
		 * is pushed onto the event stack.
		 * 
		 * @param cause
		 *            The event which caused the execution
		 */
		public void registerExecution(final AbstractTraceEvent cause) {
			this.pushEvent(cause);
			final ExecutionInformation executionInformation = new ExecutionInformation(this.nextExecutionIndex++, this.currentStackDepth++);
			this.executionStack.push(executionInformation);
		}

		public void pushExecution(final ExecutionInformation executionInformation) {
			this.currentStackDepth++;
			this.executionStack.push(executionInformation);
		}

		/**
		 * Pops the execution from the top of the execution stack.
		 * 
		 * @return The topmost execution from the execution stack
		 */
		public ExecutionInformation popExecution() {
			this.currentStackDepth--;
			return this.executionStack.pop();
		}
	}

	/**
	 * This utility class provides the core event handling functionality.
	 */
	private class EventProcessor implements IAbstractTraceEventVisitor {

		private final FilterState filterState;
		private final ExecutionTrace executionTrace;
		private final EventRecordTrace eventTrace;

		/**
		 * Creates a new event processor using the given context data.
		 * 
		 * @param filterState
		 *            The filter state object to work with
		 * @param eventTrace
		 *            The event trace from which trace meta information, such as hostname, can be retrieved
		 * @param executionTrace
		 *            The execution trace to store the generated executions in
		 */
		public EventProcessor(final FilterState filterState, final EventRecordTrace eventTrace, final ExecutionTrace executionTrace) {
			this.filterState = filterState;
			this.eventTrace = eventTrace;
			this.executionTrace = executionTrace;
		}

		/**
		 * Processes the given event. In the process, the filter state and the execution trace may be modified.
		 * Note that this method requires that the {@link #eventStream} is located at the respective event, that is,
		 * <code>event == eventStream.currentElement()</code>.
		 * 
		 * @param event
		 *            The event to process
		 */
		public void processEvent(final AbstractTraceEvent event) {
			event.accept(this);
		}

		private void handleUnsupportedEvent(final AbstractTraceEvent event) {
			EventTrace2ExecutionAndMessageTraceFilter.LOG.warn("Trace Events of type " + event.getClass().getName() + " not supported yet.");
		}

		/**
		 * Fetches the matching before-operation event for the given after-operation event from the top of the stack,
		 * if such an event is available.
		 * 
		 * @param afterOperationEvent
		 *            The after-operation event for which the match is required
		 * @return The matching before-operation event for the given event, if available
		 * @throws InvalidEventTraceException
		 *             If no matching event is found at the top of the stack
		 */
		private BeforeOperationEvent getMatchingBeforeEventFor(final AfterOperationEvent afterOperationEvent) throws InvalidEventTraceException {
			final AbstractTraceEvent potentialBeforeEvent = (this.filterState.isEventStackEmpty()) ? null : this.filterState.popEvent(); // NOPMD (null)

			// The element at the top of the stack needs to be a before-operation event...
			if ((potentialBeforeEvent == null) || !(potentialBeforeEvent instanceof BeforeOperationEvent)) {
				final String message = "Didn't find corresponding BeforeOperationEvent for AfterOperationEvent " + afterOperationEvent + " (found: )"
						+ potentialBeforeEvent + ".";
				throw new InvalidEventTraceException(message);
			}

			// ... and must reference the same operation as the given after-operation event.
			if (!afterOperationEvent.refersToSameOperationAs((BeforeOperationEvent) potentialBeforeEvent)) {
				final String message = "Components of before (" + potentialBeforeEvent + ") " + "and after (" + afterOperationEvent + ") events do not match.";
				throw new InvalidEventTraceException(message);
			}

			return (BeforeOperationEvent) potentialBeforeEvent;
		}

		private void registerExecution(final Execution execution) {
			try {
				this.executionTrace.add(execution);
			} catch (final InvalidTraceException e) {
				final String message = "Failed to add execution " + execution + " to trace" + this.executionTrace + ".";
				throw new InvalidEventTraceException(message, e);
			}
		}

		/**
		 * Removes all remaining call events from the top of the event stack. For each removed call statement, an assumed
		 * execution is generated using the timestamp of the last recursive operation event.
		 * 
		 * @param lastEvent
		 *            The last processed after-operation event
		 */
		private void closeOpenCalls(final AbstractOperationEvent lastEvent) {
			final FilterState tmpFilterState = new FilterState();

			while (true) {
				if (this.filterState.isEventStackEmpty()) {
					break; // we are done
				}

				final AbstractTraceEvent prevEvent = this.filterState.peekEvent();
				if (!(prevEvent instanceof CallOperationEvent)) {
					break; // we are done
				}

				tmpFilterState.pushEvent(this.filterState.popEvent());
				tmpFilterState.pushExecution(this.filterState.popExecution());
				if (lastEvent.getOperationSignature().equals(((CallOperationEvent) prevEvent).getOperationSignature())) {
					while (!tmpFilterState.isEventStackEmpty()) { // create executions (in reverse order)
						final CallOperationEvent currentCallEvent = (CallOperationEvent) tmpFilterState.popEvent();
						final ExecutionInformation executionInformation = tmpFilterState.popExecution();
						final Execution execution = EventTrace2ExecutionAndMessageTraceFilter.this.callOperationToExecution(
								currentCallEvent,
								this.executionTrace.getTraceId(),
								this.eventTrace.getSessionId(),
								this.eventTrace.getHostname(),
								executionInformation.getExecutionIndex(),
								executionInformation.getStackDepth(),
								currentCallEvent.getTimestamp(),
								lastEvent.getTimestamp(),
								true);
						this.registerExecution(execution);
					}
					return;
				}
			}

			while (!tmpFilterState.isEventStackEmpty()) {
				this.filterState.pushEvent(tmpFilterState.popEvent());
				this.filterState.pushExecution(tmpFilterState.popExecution());
			}

		}

		public void handleAfterOperationEvent(final AfterOperationEvent afterOperationEvent) {
			this.closeOpenCalls(afterOperationEvent);

			// Obtain the matching before-operation event from the stack
			final BeforeOperationEvent beforeOperationEvent = this.getMatchingBeforeEventFor(afterOperationEvent);

			// Look for a call event at the top of the stack
			final AbstractTraceEvent prevEvent = (this.filterState.isEventStackEmpty()) ? null : this.filterState.peekEvent(); // NOPMD (null)
			// A definite call occurs if either the stack is empty (entry into the trace) or if a matching call event is found
			final boolean definiteCall = (prevEvent == null)
					|| ((prevEvent instanceof CallOperationEvent) && ((CallOperationEvent) prevEvent).callsReferencedOperationOf(afterOperationEvent));
			// If a matching call event was found, it must be removed from the stack
			if (definiteCall && !this.filterState.isEventStackEmpty()) {
				this.filterState.popEvent();
			}

			final ExecutionInformation executionInformation = this.filterState.popExecution();
			final Execution execution = EventTrace2ExecutionAndMessageTraceFilter.this.beforeOperationToExecution(
					beforeOperationEvent,
					this.executionTrace.getTraceId(),
					this.eventTrace.getSessionId(),
					this.eventTrace.getHostname(),
					executionInformation.getExecutionIndex(),
					executionInformation.getStackDepth(),
					beforeOperationEvent.getTimestamp(),
					afterOperationEvent.getTimestamp(),
					!definiteCall);
			this.registerExecution(execution);
		}

		public void handleAfterOperationFailedEvent(final AfterOperationFailedEvent afterOperationFailedEvent) {
			this.handleUnsupportedEvent(afterOperationFailedEvent);
		}

		public void handleBeforeOperationEvent(final BeforeOperationEvent beforeOperationEvent) {
			final AbstractTraceEvent prevEvent = this.filterState.isEventStackEmpty() ? null : this.filterState.peekEvent(); // NOPMD (null)
			if ((prevEvent != null) && (prevEvent instanceof CallOperationEvent)
					&& (((CallOperationEvent) prevEvent).callsReferencedOperationOf(beforeOperationEvent))) {
				this.filterState.pushEvent(beforeOperationEvent);
			} else {
				this.closeOpenCalls(beforeOperationEvent);
				this.filterState.registerExecution(beforeOperationEvent);
			}
		}

		public void handleCallOperationEvent(final CallOperationEvent callOperationEvent) {
			this.closeOpenCalls(callOperationEvent);
			this.filterState.registerExecution(callOperationEvent);
		}

		public void handleSplitEvent(final SplitEvent splitEvent) {
			this.handleUnsupportedEvent(splitEvent);
		}
	}

	/**
	 * Internal exception type which denotes that an invalid event trace was encountered.
	 */
	private static class InvalidEventTraceException extends RuntimeException {

		private static final long serialVersionUID = -6187153581658321041L;

		public InvalidEventTraceException(final String message) {
			super(message);
		}

		public InvalidEventTraceException(final String message, final Throwable cause) {
			super(message, cause);
		}

	}

	public EventTrace2ExecutionAndMessageTraceFilter(final Configuration configuration) {
		super(configuration);
	}

	@InputPort(name = EventTrace2ExecutionAndMessageTraceFilter.INPUT_PORT_NAME_EVENT_TRACE, description = "Receives event record traces to be transformed", eventTypes = { EventRecordTrace.class })
	public void inputEventTrace(final EventRecordTrace eventTrace) {
		final ExecutionTrace execTrace = new ExecutionTrace(eventTrace.getTraceId(), eventTrace.getSessionId());
		final EventRecordStream eventStream = new EventRecordStream(eventTrace);
		final FilterState state = new FilterState();
		final EventProcessor eventProcessor = new EventProcessor(state, eventTrace, execTrace);

		long lastOrderIndex = -1; // used to check for ascending order indices

		try {
			while (true) {
				final AbstractTraceEvent currentEvent = eventStream.currentElement();

				// Exit if the event stream has reached its end
				if (currentEvent == null) {
					break;
				}

				/* Make sure that order indices increment by 1 */
				if (currentEvent.getOrderIndex() != (lastOrderIndex + 1)) {
					this.printInvalidIndexMessage(currentEvent, execTrace, lastOrderIndex);
					// TODO: output broken event record trace
					return;
				} else {
					lastOrderIndex = currentEvent.getOrderIndex(); // i.e., lastOrderIndex++
				}

				// Process and consume the current event
				eventProcessor.processEvent(currentEvent);
				eventStream.consume();
			}
		} catch (final InvalidEventTraceException e) {
			EventTrace2ExecutionAndMessageTraceFilter.LOG.error(e.getMessage() + "\n"
					+ "Terminating processing of event record trace with ID " + execTrace.getTraceId(), e);
		}

		super.deliver(EventTrace2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE, execTrace);
		try {
			super.deliver(EventTrace2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, execTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION));
			super.reportSuccess(execTrace.getTraceId());
		} catch (final InvalidTraceException ex) {
			// FIXME: send to new output port for defect traces
		}
	}

	private void printInvalidIndexMessage(final AbstractTraceEvent event, final ExecutionTrace executionTrace, final long lastIndex) {
		EventTrace2ExecutionAndMessageTraceFilter.LOG.error("Trace events' order indices must increment by one: "
				+ " Found " + lastIndex + " followed by " + event.getOrderIndex() + " event (" + event + ")");
		EventTrace2ExecutionAndMessageTraceFilter.LOG.error("Terminating processing of event record trace with ID " + executionTrace.getTraceId());
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
	private Execution beforeOperationToExecution(final BeforeOperationEvent event, final long traceId, final String sessionId, final String hostname, final int eoi,
			final int ess,
			final long tin, final long tout, final boolean assumed) {

		final ClassOperationSignaturePair fqComponentNameSignaturePair = ClassOperationSignaturePair.splitOperationSignatureStr(event.getOperationSignature());

		return super.createExecutionByEntityNames(hostname, fqComponentNameSignaturePair.getFqClassname(), fqComponentNameSignaturePair.getSignature(),
				traceId, sessionId, eoi, ess, tin, tout, assumed);
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
	private Execution callOperationToExecution(final CallOperationEvent event, final long traceId, final String sessionId, final String hostname, final int eoi,
			final int ess,
			final long tin, final long tout, final boolean assumed) {

		final ClassOperationSignaturePair fqComponentNameSignaturePair = ClassOperationSignaturePair.splitOperationSignatureStr(event.getCalleeOperationSignature());
		return super.createExecutionByEntityNames(hostname, fqComponentNameSignaturePair.getFqClassname(),
				fqComponentNameSignaturePair.getSignature(), traceId, sessionId, eoi, ess, tin, tout, assumed);
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	public Configuration getCurrentConfiguration() {
		return new Configuration(null);
	}

}
