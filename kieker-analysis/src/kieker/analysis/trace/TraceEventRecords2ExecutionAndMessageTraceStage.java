/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.trace;

import java.util.Stack;

import org.slf4j.Logger;

import kieker.analysis.stage.flow.TraceEventRecords;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.concurrency.SplitEvent;
import kieker.common.record.flow.trace.operation.AbstractOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.flow.trace.operation.CallOperationEvent;
import kieker.common.record.flow.trace.operation.constructor.AfterConstructorEvent;
import kieker.common.record.flow.trace.operation.constructor.AfterConstructorFailedEvent;
import kieker.common.record.flow.trace.operation.constructor.BeforeConstructorEvent;
import kieker.common.record.flow.trace.operation.constructor.CallConstructorEvent;
import kieker.common.util.signature.ClassOperationSignaturePair;
import kieker.common.util.signature.Signature;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.Execution;
import kieker.model.system.model.ExecutionTrace;
import kieker.model.system.model.InvalidExecutionTrace;
import kieker.model.system.model.MessageTrace;
import kieker.model.system.model.exceptions.InvalidTraceException;

import teetime.framework.OutputPort;

/**
 * Transforms incoming TraceEventRecords into execution and message traces.
 *
 * Has three output ports for valid execution traces, message traces and invalid
 * execution traces.
 *
 * @author Andre van Hoorn, Holger Knoche, Jan Waller
 * @author Reiner Jung -- teetime port
 *
 * @since 1.6
 */
public class TraceEventRecords2ExecutionAndMessageTraceStage extends AbstractTraceProcessingStage<TraceEventRecords> {

	private final OutputPort<ExecutionTrace> executionTraceOutputPort = this.createOutputPort(ExecutionTrace.class);
	private final OutputPort<MessageTrace> messageTraceOutputPort = this.createOutputPort(MessageTrace.class);
	private final OutputPort<InvalidExecutionTrace> invalidExecutionTraceOutputPort = this
			.createOutputPort(InvalidExecutionTrace.class);

	private final boolean enhanceJavaConstructors;
	private final boolean enhanceCallDetection;
	private final boolean ignoreAssumedCalls;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 *
	 * @param repository
	 *            access to the model repository
	 * @param enhanceJavaConstructors
	 * @param enhanceCallDetection
	 * @param ignoreAssumedCalls
	 */
	public TraceEventRecords2ExecutionAndMessageTraceStage(final SystemModelRepository repository,
			final boolean enhanceJavaConstructors, final boolean enhanceCallDetection,
			final boolean ignoreAssumedCalls) {
		super(repository);

		this.enhanceJavaConstructors = enhanceJavaConstructors;
		this.enhanceCallDetection = enhanceCallDetection;
		this.ignoreAssumedCalls = ignoreAssumedCalls;
	}

	@Override
	protected void onTerminating() {
		this.logger.debug("Terminating {}", this.getClass().getCanonicalName());
		super.onTerminating();
	}

	@Override
	protected void execute(final TraceEventRecords traceEventRecords) throws Exception {
		final TraceMetadata trace = traceEventRecords.getTraceMetadata();
		if (trace == null) {
			this.logger.error("Trace is missing from TraceEvents");
			return;
		}
		final long traceId = trace.getTraceId();
		final ExecutionTrace executionTrace = new ExecutionTrace(traceId, trace.getSessionId());
		final TraceEventRecordHandler traceEventRecordHandler = new TraceEventRecordHandler(this.logger, trace,
				executionTrace, this.getSystemModelRepository(), this.enhanceJavaConstructors,
				this.enhanceCallDetection, this.ignoreAssumedCalls);
		int expectedOrderIndex = -1;
		for (final AbstractTraceEvent event : traceEventRecords.getTraceEvents()) {
			expectedOrderIndex += 1; // increment in each iteration -> 0 is the first real value
			if (event.getOrderIndex() != expectedOrderIndex) {
				this.logger.error("Found event with wrong orderIndex. Found: {} expected: {}", event.getOrderIndex(),
						(expectedOrderIndex - 1));
				continue; // simply ignore wrong event
			}
			if (event.getTraceId() != traceId) {
				this.logger.error("Found event with wrong traceId. Found: {} expected: {}", event.getTraceId(),
						traceId);
				continue; // simply ignore wrong event
			}
			try { // handle all cases (more specific classes must be handled before less
					// specific ones)
					// Before Events
				if (BeforeConstructorEvent.class.isAssignableFrom(event.getClass())) {
					traceEventRecordHandler.handleBeforeConstructorEvent((BeforeConstructorEvent) event);
				} else if (BeforeOperationEvent.class.isAssignableFrom(event.getClass())) {
					traceEventRecordHandler.handleBeforeOperationEvent((BeforeOperationEvent) event);
					// After Events
				} else if (AfterConstructorFailedEvent.class.isAssignableFrom(event.getClass())) {
					traceEventRecordHandler.handleAfterConstructorFailedEvent((AfterConstructorFailedEvent) event);
				} else if (AfterOperationFailedEvent.class.isAssignableFrom(event.getClass())) {
					traceEventRecordHandler.handleAfterOperationFailedEvent((AfterOperationFailedEvent) event);
				} else if (AfterConstructorEvent.class.isAssignableFrom(event.getClass())) {
					traceEventRecordHandler.handleAfterConstructorEvent((AfterConstructorEvent) event);
				} else if (AfterOperationEvent.class.isAssignableFrom(event.getClass())) {
					traceEventRecordHandler.handleAfterOperationEvent((AfterOperationEvent) event);
					// CallOperation Events
				} else if (CallConstructorEvent.class.isAssignableFrom(event.getClass())) {
					traceEventRecordHandler.handleCallConstructorEvent((CallConstructorEvent) event);
				} else if (CallOperationEvent.class.isAssignableFrom(event.getClass())) {
					traceEventRecordHandler.handleCallOperationEvent((CallOperationEvent) event);
					// SplitEvent
				} else if (SplitEvent.class.isAssignableFrom(event.getClass())) {
					this.logger.warn("Events of type 'SplitEvent' are currently not handled and ignored.");
				} else {
					this.logger.warn("Events of type '{}' are currently not handled and ignored.",
							event.getClass().getName());
				}
			} catch (final InvalidTraceException ex) {
				this.logger.error("Failed to reconstruct trace.", ex);
				this.invalidExecutionTraceOutputPort.send(new InvalidExecutionTrace(executionTrace));
				return;
			}
		}
		try {
			traceEventRecordHandler.finish();
			final MessageTrace messageTrace = executionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);
			this.executionTraceOutputPort.send(executionTrace);
			this.messageTraceOutputPort.send(messageTrace);
			super.reportSuccess(executionTrace.getTraceId());
		} catch (final InvalidTraceException ex) {
			this.logger.warn("Failed to convert to message trace: {}", ex.getMessage()); // do not pass 'ex' to log.warn
																							// because this makes the
																							// output verbose (#584)
			this.invalidExecutionTraceOutputPort.send(new InvalidExecutionTrace(executionTrace));
		}
	}

	public OutputPort<ExecutionTrace> getExecutionTraceOutputPort() {
		return this.executionTraceOutputPort;
	}

	public OutputPort<InvalidExecutionTrace> getInvalidExecutionTraceOutputPort() {
		return this.invalidExecutionTraceOutputPort;
	}

	public OutputPort<MessageTrace> getMessageTraceOutputPort() {
		return this.messageTraceOutputPort;
	}

	/**
	 * This class encapsulates the trace's state, that is, the current event and
	 * execution stacks.
	 *
	 * @author Andre van Hoorn, Holger Knoche, Jan Waller
	 */
	private static class TraceEventRecordHandler {
		private final SystemModelRepository systemModelRepository;
		private final TraceMetadata trace;
		private final ExecutionTrace executionTrace;
		private final Stack<AbstractTraceEvent> eventStack = new Stack<>();
		private final Stack<ExecutionInformation> executionStack = new Stack<>();

		private final boolean enhanceJavaConstructors;
		private final boolean enhanceCallDetection;

		private int orderindex;
		private final boolean ignoreAssumedCalls;
		private final Logger logger;

		public TraceEventRecordHandler(final Logger logger, final TraceMetadata trace,
				final ExecutionTrace executionTrace, final SystemModelRepository systemModelRepository,
				final boolean enhanceJavaConstructors, final boolean enhanceCallDetection,
				final boolean ignoreAssumedCalls) {
			this.trace = trace;
			this.executionTrace = executionTrace;
			this.systemModelRepository = systemModelRepository;
			this.enhanceJavaConstructors = enhanceJavaConstructors;
			this.enhanceCallDetection = enhanceCallDetection;
			this.ignoreAssumedCalls = ignoreAssumedCalls;
			this.logger = logger;
		}

		/**
		 * Finished the current execution. All open executions are finished and
		 * correctly added (if possible).
		 *
		 * @throws InvalidTraceException
		 */
		public void finish() throws InvalidTraceException {
			final Stack<AbstractTraceEvent> tmpEventStack = new Stack<>();
			final Stack<ExecutionInformation> tmpExecutionStack = new Stack<>();
			if (!this.eventStack.isEmpty()) {
				final long lastTimeStamp = this.eventStack.peek().getTimestamp();
				while (!this.eventStack.isEmpty()) { // reverse order
					tmpEventStack.push(this.eventStack.pop());
					tmpExecutionStack.push(this.executionStack.pop());
				}
				while (!tmpEventStack.isEmpty()) { // create executions (in reverse order)
					final AbstractTraceEvent currentEvent = tmpEventStack.pop();
					final ExecutionInformation executionInformation = tmpExecutionStack.pop();
					if (currentEvent instanceof CallOperationEvent) {
						this.finishExecution(((CallOperationEvent) currentEvent).getCalleeOperationSignature(),
								((CallOperationEvent) currentEvent).getCalleeClassSignature(), this.trace.getTraceId(),
								this.trace.getSessionId(), this.trace.getHostname(), executionInformation.getEoi(),
								executionInformation.getEss(), currentEvent.getTimestamp(), lastTimeStamp,
								!this.ignoreAssumedCalls, currentEvent instanceof CallConstructorEvent);
					} else {
						throw new InvalidTraceException(
								"Only CallOperationEvents are expected to be remaining, but found: "
										+ currentEvent.getClass().getSimpleName());
					}
				}
			}
		}

		/**
		 * This method delivers the object on top of the stack without removing it, if
		 * it exists and null otherwise.
		 *
		 * @return The object on top if it exists or null otherwise.
		 */
		private AbstractTraceEvent peekEvent() {
			if (this.eventStack.isEmpty()) {
				return null;
			}
			return this.eventStack.peek();
		}

		private void registerExecution(final AbstractTraceEvent cause) {
			this.eventStack.push(cause);
			final ExecutionInformation executionInformation = new ExecutionInformation(this.orderindex++,
					this.executionStack.size());
			this.executionStack.push(executionInformation);
		}

		private void finishExecution(final String operationSignature, final String classSignature, final long traceId,
				final String sessionId, final String hostname, final int eoi, final int ess, final long tin,
				final long tout, final boolean assumed, final boolean constructor) throws InvalidTraceException {
			final ClassOperationSignaturePair fqComponentNameSignaturePair = ClassOperationSignaturePair
					.splitOperationSignatureStr(operationSignature, constructor && this.enhanceJavaConstructors);
			final String executionContext;
			if (classSignature.length() == 0) {
				executionContext = fqComponentNameSignaturePair.getFqClassname();
			} else {
				executionContext = classSignature;
			}
			final Execution execution = AbstractTraceAnalysisStage.createExecutionByEntityNames(
					this.systemModelRepository, hostname, executionContext,
					fqComponentNameSignaturePair.getFqClassname(), fqComponentNameSignaturePair.getSignature(), traceId,
					sessionId, eoi, ess, tin, tout, assumed);
			try {
				this.executionTrace.add(execution);
			} catch (final InvalidTraceException ex) {
				throw new InvalidTraceException(
						"Failed to add execution " + execution + " to trace " + this.executionTrace + ".", ex);
			}
		}

		/**
		 * Removes all remaining call events from the top of the event stack. For each
		 * removed call statement, an assumed execution is generated using the timestamp
		 * of the last recursive operation event.
		 *
		 * @param lastEvent
		 *            The last processed operation event
		 * @throws InvalidTraceException
		 */
		private void closeOpenCalls(final AbstractOperationEvent lastEvent) throws InvalidTraceException {
			final Stack<CallOperationEvent> tmpEventStack = new Stack<>();
			final Stack<ExecutionInformation> tmpExecutionStack = new Stack<>();
			while (true) {
				if (this.eventStack.isEmpty()) {
					break; // we are done
				}
				final AbstractTraceEvent prevEvent = this.eventStack.peek();
				if (!(prevEvent instanceof CallOperationEvent)) {
					break; // we are done
				}
				tmpEventStack.push((CallOperationEvent) this.eventStack.pop());
				tmpExecutionStack.push(this.executionStack.pop());
				if (lastEvent.getOperationSignature()
						.equals(((CallOperationEvent) prevEvent).getOperationSignature())) {
					while (!tmpEventStack.isEmpty()) { // create executions (in reverse order)
						final CallOperationEvent currentCallEvent = tmpEventStack.pop();
						final ExecutionInformation executionInformation = tmpExecutionStack.pop();
						this.finishExecution(currentCallEvent.getCalleeOperationSignature(),
								currentCallEvent.getCalleeClassSignature(), this.trace.getTraceId(),
								this.trace.getSessionId(), this.trace.getHostname(), executionInformation.getEoi(),
								executionInformation.getEss(), currentCallEvent.getTimestamp(),
								lastEvent.getTimestamp(), !this.ignoreAssumedCalls,
								currentCallEvent instanceof CallConstructorEvent);
					}
					return;
				}
			}
			while (!tmpEventStack.isEmpty()) {
				this.eventStack.push(tmpEventStack.pop());
				this.executionStack.push(tmpExecutionStack.pop());
			}
		}

		private void handleBeforeEvent(final BeforeOperationEvent beforeOperationEvent,
				final Class<? extends CallOperationEvent> callClass) throws InvalidTraceException {
			final AbstractTraceEvent prevEvent = this.peekEvent();
			if (this.isPrevEventMatchingCall(beforeOperationEvent, prevEvent, callClass)) {
				this.eventStack.push(beforeOperationEvent);
			} else {
				this.closeOpenCalls(beforeOperationEvent);
				this.registerExecution(beforeOperationEvent);
			}
		}

		public void handleBeforeOperationEvent(final BeforeOperationEvent beforeOperationEvent)
				throws InvalidTraceException {
			this.handleBeforeEvent(beforeOperationEvent, CallOperationEvent.class);
		}

		public void handleBeforeConstructorEvent(final BeforeConstructorEvent beforeConstructorEvent)
				throws InvalidTraceException {
			this.handleBeforeEvent(beforeConstructorEvent, CallConstructorEvent.class);
		}

		private boolean isPrevEventMatchingCall(final BeforeOperationEvent beforeOperationEvent,
				final AbstractTraceEvent prevEvent, final Class<? extends CallOperationEvent> callClass) {
			if ((prevEvent != null) && callClass.isAssignableFrom(prevEvent.getClass())
					&& (prevEvent.getOrderIndex() == (beforeOperationEvent.getOrderIndex() - 1))) {
				if (this.callsReferencedOperationOf((CallOperationEvent) prevEvent, beforeOperationEvent)) {
					return true;
				} else if (this.enhanceCallDetection) { // perhaps we don't find a perfect match, but can guess one!
					final boolean isConstructor = beforeOperationEvent instanceof BeforeConstructorEvent;
					final CallOperationEvent callEvent = (CallOperationEvent) prevEvent;
					final Signature callSignature = ClassOperationSignaturePair
							.splitOperationSignatureStr(callEvent.getCalleeOperationSignature(),
									isConstructor && this.enhanceJavaConstructors)
							.getSignature();
					final Signature afterSignature = ClassOperationSignaturePair
							.splitOperationSignatureStr(beforeOperationEvent.getOperationSignature(),
									isConstructor && this.enhanceJavaConstructors)
							.getSignature();
					if (callSignature.equals(afterSignature)
							&& callEvent.getCalleeClassSignature().equals(beforeOperationEvent.getClassSignature())) {
						this.logger.debug("Guessed call of \n\t{}\n\t{}", callEvent, beforeOperationEvent);
						return true;
					}
				}
			}
			return false;
		}

		/**
		 * Check if a previous event callee is the present operation.
		 */
		private boolean callsReferencedOperationOf(final CallOperationEvent prevEvent,
				final BeforeOperationEvent presentEvent) {
			return prevEvent.getCalleeOperationSignature().equals(presentEvent.getOperationSignature())
					&& prevEvent.getCalleeClassSignature().equals(presentEvent.getClassSignature());
		}

		private void handleAfterEvent(final AfterOperationEvent afterOperationEvent,
				final Class<? extends BeforeOperationEvent> beforeClass,
				final Class<? extends CallOperationEvent> callClass) throws InvalidTraceException {
			this.closeOpenCalls(afterOperationEvent);
			// Obtain the matching before-operation event from the stack
			final AbstractTraceEvent potentialBeforeEvent = this.peekEvent();
			// The element at the top of the stack needs to be a before-operation event...
			if ((potentialBeforeEvent == null) || !beforeClass.isAssignableFrom(potentialBeforeEvent.getClass())) {
				throw new InvalidTraceException("Didn't find corresponding " + beforeClass.getName() + " for "
						+ afterOperationEvent.getClass().getName() + " " + afterOperationEvent.toString() + " (found: "
						+ potentialBeforeEvent + ").");
			}
			// ... and must reference the same operation as the given after-operation event.
			if (!this.refersToSameOperationAs(afterOperationEvent, (BeforeOperationEvent) potentialBeforeEvent)) {
				throw new InvalidTraceException("Components of before (" + potentialBeforeEvent + ") " + "and after ("
						+ afterOperationEvent + ") events do not match.");
			}
			final BeforeOperationEvent beforeOperationEvent = (BeforeOperationEvent) this.eventStack.pop();
			// Look for a call event at the top of the stack
			final AbstractTraceEvent prevEvent = this.peekEvent();
			// A definite call occurs if either the stack is empty (entry into the trace) or
			// if a matching call event is found
			final boolean definiteCall = (prevEvent == null)
					|| this.isPrevEventMatchingCall(beforeOperationEvent, prevEvent, callClass);
			// If a matching call event was found, it must be removed from the stack
			if (definiteCall && !this.eventStack.isEmpty()) {
				this.eventStack.pop();
			}
			final ExecutionInformation executionInformation = this.executionStack.pop();
			this.finishExecution(beforeOperationEvent.getOperationSignature(), beforeOperationEvent.getClassSignature(),
					this.trace.getTraceId(), this.trace.getSessionId(), this.trace.getHostname(),
					executionInformation.getEoi(), executionInformation.getEss(), beforeOperationEvent.getTimestamp(),
					afterOperationEvent.getTimestamp(), !(definiteCall || this.ignoreAssumedCalls),
					beforeOperationEvent instanceof BeforeConstructorEvent);
		}

		private boolean refersToSameOperationAs(final AfterOperationEvent afterOperationEvent,
				final BeforeOperationEvent potentialBeforeEvent) {
			return afterOperationEvent.getOperationSignature().equals(potentialBeforeEvent.getOperationSignature())
					&& afterOperationEvent.getClassSignature().equals(potentialBeforeEvent.getClassSignature());
		}

		public void handleAfterOperationEvent(final AfterOperationEvent afterOperationEvent)
				throws InvalidTraceException {
			this.handleAfterEvent(afterOperationEvent, BeforeOperationEvent.class, CallOperationEvent.class);
		}

		public void handleAfterOperationFailedEvent(final AfterOperationFailedEvent afterOperationEvent)
				throws InvalidTraceException {
			this.handleAfterEvent(afterOperationEvent, BeforeOperationEvent.class, CallOperationEvent.class);
		}

		public void handleAfterConstructorEvent(final AfterConstructorEvent afterConstructorEvent)
				throws InvalidTraceException {
			this.handleAfterEvent(afterConstructorEvent, BeforeConstructorEvent.class, CallConstructorEvent.class);
		}

		public void handleAfterConstructorFailedEvent(final AfterConstructorFailedEvent afterConstructorEvent)
				throws InvalidTraceException {
			this.handleAfterEvent(afterConstructorEvent, BeforeConstructorEvent.class, CallConstructorEvent.class);
		}

		public void handleCallOperationEvent(final CallOperationEvent callOperationEvent) throws InvalidTraceException {
			this.closeOpenCalls(callOperationEvent);
			this.registerExecution(callOperationEvent);
		}

		public void handleCallConstructorEvent(final CallConstructorEvent callConstructorEvent)
				throws InvalidTraceException {
			this.handleCallOperationEvent(callConstructorEvent);
		}

		/**
		 * This class stores information about a specific execution.
		 */
		private static class ExecutionInformation {
			private final int eoi;
			private final int ess;

			/**
			 * Creates a new instance of this class using the given parameters.
			 *
			 * @param executionIndex
			 *            The execution order index.
			 * @param stackDepth
			 *            The execution stack size.
			 */
			public ExecutionInformation(final int executionIndex, final int stackDepth) {
				this.eoi = executionIndex;
				this.ess = stackDepth;
			}

			/**
			 * Returns the execution's execution order index.
			 *
			 * @return See above
			 */
			public int getEoi() {
				return this.eoi;
			}

			/**
			 * Returns the stack depth at which the execution occurred.
			 *
			 * @return See above
			 */
			public int getEss() {
				return this.ess;
			}
		}
	}

}
