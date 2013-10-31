/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

import kieker.analysis.IProjectContext;
import kieker.analysis.analysisComponent.AbstractAnalysisComponent;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.analysis.plugin.filter.flow.TraceEventRecords;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
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
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.traceReconstruction.InvalidTraceException;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.InvalidExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * @author Andre van Hoorn, Holger Knoche, Jan Waller
 * 
 * @since 1.6
 */
@Plugin(description = "Transforms incoming TraceEventRecords into execution and message traces",
		outputPorts = {
			@OutputPort(name = TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
					description = "Outputs transformed execution traces", eventTypes = { ExecutionTrace.class }),
			@OutputPort(name = TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
					description = "Outputs transformed message traces", eventTypes = { MessageTrace.class }),
			@OutputPort(name = TraceEventRecords2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_INVALID_EXECUTION_TRACE,
					description = "Invalid Execution Traces", eventTypes = { InvalidExecutionTrace.class }) },
		repositoryPorts = {
			@RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class)
		},
		configuration = {
			@Property(name = TraceEventRecords2ExecutionAndMessageTraceFilter.CONFIG_ENHANCE_JAVA_CONSTRUCTORS, defaultValue = "true"),
			@Property(name = TraceEventRecords2ExecutionAndMessageTraceFilter.CONFIG_ENHANCE_CALL_DETECTION, defaultValue = "true")
		})
public class TraceEventRecords2ExecutionAndMessageTraceFilter extends AbstractTraceProcessingFilter {

	/** This is the name of the input port receiving new trace events. */
	public static final String INPUT_PORT_NAME_EVENT_TRACE = "traceEvents";

	/** This is the name of the output port delivering the execution traces. */
	public static final String OUTPUT_PORT_NAME_EXECUTION_TRACE = "executionTrace";
	/** This is the name of the output port delivering the message traces. */
	public static final String OUTPUT_PORT_NAME_MESSAGE_TRACE = "messageTrace";
	/** This is the name of the output port delivering invalid traces. */
	public static final String OUTPUT_PORT_NAME_INVALID_EXECUTION_TRACE = "invalidTrace";

	public static final String CONFIG_ENHANCE_JAVA_CONSTRUCTORS = "enhanceJavaConstructors";
	public static final String CONFIG_ENHANCE_CALL_DETECTION = "enhanceCallDetection";

	private final boolean enhanceJavaConstructors;
	private final boolean enhanceCallDetection;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public TraceEventRecords2ExecutionAndMessageTraceFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.enhanceJavaConstructors = configuration.getBooleanProperty(CONFIG_ENHANCE_JAVA_CONSTRUCTORS);
		this.enhanceCallDetection = configuration.getBooleanProperty(CONFIG_ENHANCE_CALL_DETECTION);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_ENHANCE_JAVA_CONSTRUCTORS, String.valueOf(this.enhanceJavaConstructors));
		configuration.setProperty(CONFIG_ENHANCE_CALL_DETECTION, String.valueOf(this.enhanceCallDetection));
		return configuration;
	}

	/**
	 * This method represents the input port, processing incoming trace event records.
	 * 
	 * @param traceEventRecords
	 *            The next trace event record.
	 */
	@InputPort(name = INPUT_PORT_NAME_EVENT_TRACE, description = "Receives TraceEvents to be transformed", eventTypes = { TraceEventRecords.class })
	public void inputTraceEvents(final TraceEventRecords traceEventRecords) {
		final TraceMetadata trace = traceEventRecords.getTraceMetadata();
		if (trace == null) {
			this.log.error("Trace is missing from TraceEvents");
			return;
		}
		final long traceId = trace.getTraceId();
		final ExecutionTrace executionTrace = new ExecutionTrace(traceId, trace.getSessionId());
		final TraceEventRecordHandler traceEventRecordHandler = new TraceEventRecordHandler(trace, executionTrace, this.getSystemEntityFactory(),
				this.enhanceJavaConstructors, this.enhanceCallDetection);
		int expectedOrderIndex = -1;
		for (final AbstractTraceEvent event : traceEventRecords.getTraceEvents()) {
			expectedOrderIndex += 1; // increment in each iteration -> 0 is the first real value
			if (event.getOrderIndex() != expectedOrderIndex) {
				this.log.error("Found event with wrong orderIndex. Found: " + event.getOrderIndex() + " expected: " + (expectedOrderIndex - 1));
				continue; // simply ignore wrong event
			}
			if (event.getTraceId() != traceId) {
				this.log.error("Found event with wrong traceId. Found: " + event.getTraceId() + " expected: " + traceId);
				continue; // simply ignore wrong event
			}
			try { // handle all cases
				if (BeforeOperationEvent.class.equals(event.getClass())) {
					traceEventRecordHandler.handleBeforeOperationEvent((BeforeOperationEvent) event);
				} else if (AfterOperationEvent.class.equals(event.getClass())) {
					traceEventRecordHandler.handleAfterOperationEvent((AfterOperationEvent) event);
				} else if (AfterOperationFailedEvent.class.equals(event.getClass())) {
					traceEventRecordHandler.handleAfterOperationFailedEvent((AfterOperationFailedEvent) event);
				} else if (BeforeConstructorEvent.class.equals(event.getClass())) {
					traceEventRecordHandler.handleBeforeConstructorEvent((BeforeConstructorEvent) event);
				} else if (AfterConstructorEvent.class.equals(event.getClass())) {
					traceEventRecordHandler.handleAfterConstructorEvent((AfterConstructorEvent) event);
				} else if (AfterConstructorFailedEvent.class.equals(event.getClass())) {
					traceEventRecordHandler.handleAfterConstructorFailedEvent((AfterConstructorFailedEvent) event);
				} else if (CallOperationEvent.class.equals(event.getClass())) {
					traceEventRecordHandler.handleCallOperationEvent((CallOperationEvent) event);
				} else if (CallConstructorEvent.class.equals(event.getClass())) {
					traceEventRecordHandler.handleCallConstructorEvent((CallConstructorEvent) event);
				} else if (SplitEvent.class.equals(event.getClass())) {
					this.log.warn("Events of type 'SplitEvent' are currently not handled and ignored.");
				} else {
					this.log.warn("Events of type '" + event.getClass().getName() + "' are currently not handled and ignored.");
				}
			} catch (final InvalidTraceException ex) {
				this.log.error("Failed to reconstruct trace.", ex);
				super.deliver(OUTPUT_PORT_NAME_INVALID_EXECUTION_TRACE, new InvalidExecutionTrace(executionTrace));
				return;
			}
		}
		try {
			traceEventRecordHandler.finish();
			final MessageTrace messageTrace = executionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);
			super.deliver(OUTPUT_PORT_NAME_EXECUTION_TRACE, executionTrace);
			super.deliver(OUTPUT_PORT_NAME_MESSAGE_TRACE, messageTrace);
			super.reportSuccess(executionTrace.getTraceId());
		} catch (final InvalidTraceException ex) {
			this.log.warn("Failed to convert to message trace: " + ex.getMessage()); // do not pass 'ex' to log.warn because this makes the output verbose (#584)
			super.deliver(OUTPUT_PORT_NAME_INVALID_EXECUTION_TRACE, new InvalidExecutionTrace(executionTrace));
		}

	}

	protected static Log getLOG() {
		return AbstractAnalysisComponent.LOG;
	}

	/**
	 * This class encapsulates the trace's state, that is, the current event and execution stacks.
	 * 
	 * @author Andre van Hoorn, Holger Knoche, Jan Waller
	 */
	private static class TraceEventRecordHandler {
		private final SystemModelRepository systemModelRepository;
		private final TraceMetadata trace;
		private final ExecutionTrace executionTrace;
		private final Stack<AbstractTraceEvent> eventStack = new Stack<AbstractTraceEvent>();
		private final Stack<ExecutionInformation> executionStack = new Stack<ExecutionInformation>();

		private final boolean enhanceJavaConstructors;
		private final boolean enhanceCallDetection;

		private int orderindex;

		public TraceEventRecordHandler(final TraceMetadata trace, final ExecutionTrace executionTrace, final SystemModelRepository systemModelRepository,
				final boolean enhanceJavaConstructors, final boolean enhanceCallDetection) {
			this.trace = trace;
			this.executionTrace = executionTrace;
			this.systemModelRepository = systemModelRepository;
			this.enhanceJavaConstructors = enhanceJavaConstructors;
			this.enhanceCallDetection = enhanceCallDetection;
		}

		/**
		 * Finished the current execution. All open executions are finished and correctly added (if possible).
		 * 
		 * @throws InvalidTraceException
		 */
		public void finish() throws InvalidTraceException {
			final Stack<AbstractTraceEvent> tmpEventStack = new Stack<AbstractTraceEvent>();
			final Stack<ExecutionInformation> tmpExecutionStack = new Stack<ExecutionInformation>();
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
						this.finishExecution(
								((CallOperationEvent) currentEvent).getCalleeOperationSignature(),
								((CallOperationEvent) currentEvent).getCalleeClassSignature(),
								this.trace.getTraceId(),
								this.trace.getSessionId(),
								this.trace.getHostname(),
								executionInformation.getEoi(),
								executionInformation.getEss(),
								currentEvent.getTimestamp(),
								lastTimeStamp,
								true, currentEvent instanceof CallConstructorEvent);
					} else {
						throw new InvalidTraceException("Only CallOperationEvents are expected to be remaining, but found: "
								+ currentEvent.getClass().getSimpleName());
					}
				}
			}
		}

		/**
		 * This method delivers the object on top of the stack without removing it, if it exists and null otherwise.
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
			final ExecutionInformation executionInformation = new ExecutionInformation(this.orderindex++, this.executionStack.size());
			this.executionStack.push(executionInformation);
		}

		private void finishExecution(final String operationSignature, final String classSignature, final long traceId, final String sessionId,
				final String hostname, final int eoi, final int ess, final long tin, final long tout, final boolean assumed, final boolean constructor) throws
				InvalidTraceException {
			final ClassOperationSignaturePair fqComponentNameSignaturePair = ClassOperationSignaturePair.splitOperationSignatureStr(operationSignature,
					constructor && this.enhanceJavaConstructors);
			final String executionContext;
			if (classSignature.length() == 0) {
				executionContext = fqComponentNameSignaturePair.getFqClassname();
			} else {
				executionContext = classSignature;
			}
			final Execution execution = AbstractTraceAnalysisFilter.createExecutionByEntityNames(this.systemModelRepository,
					hostname,
					executionContext,
					fqComponentNameSignaturePair.getFqClassname(), fqComponentNameSignaturePair.getSignature(),
					traceId, sessionId, eoi, ess,
					tin, tout, assumed);
			try {
				this.executionTrace.add(execution);
			} catch (final InvalidTraceException ex) {
				throw new InvalidTraceException("Failed to add execution " + execution + " to trace " + this.executionTrace + ".", ex);
			}
		}

		/**
		 * Removes all remaining call events from the top of the event stack. For each removed call statement, an assumed
		 * execution is generated using the timestamp of the last recursive operation event.
		 * 
		 * @param lastEvent
		 *            The last processed operation event
		 * @throws InvalidTraceException
		 */
		private void closeOpenCalls(final AbstractOperationEvent lastEvent) throws InvalidTraceException {
			final Stack<CallOperationEvent> tmpEventStack = new Stack<CallOperationEvent>();
			final Stack<ExecutionInformation> tmpExecutionStack = new Stack<ExecutionInformation>();
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
				if (lastEvent.getOperationSignature().equals(((CallOperationEvent) prevEvent).getOperationSignature())) {
					while (!tmpEventStack.isEmpty()) { // create executions (in reverse order)
						final CallOperationEvent currentCallEvent = tmpEventStack.pop();
						final ExecutionInformation executionInformation = tmpExecutionStack.pop();
						this.finishExecution(
								currentCallEvent.getCalleeOperationSignature(),
								currentCallEvent.getCalleeClassSignature(),
								this.trace.getTraceId(),
								this.trace.getSessionId(),
								this.trace.getHostname(),
								executionInformation.getEoi(),
								executionInformation.getEss(),
								currentCallEvent.getTimestamp(),
								lastEvent.getTimestamp(),
								true, currentCallEvent instanceof CallConstructorEvent);
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

		public void handleBeforeOperationEvent(final BeforeOperationEvent beforeOperationEvent) throws InvalidTraceException {
			this.handleBeforeEvent(beforeOperationEvent, CallOperationEvent.class);
		}

		public void handleBeforeConstructorEvent(final BeforeConstructorEvent beforeConstructorEvent) throws InvalidTraceException {
			this.handleBeforeEvent(beforeConstructorEvent, CallConstructorEvent.class);
		}

		private boolean isPrevEventMatchingCall(final BeforeOperationEvent beforeOperationEvent, final AbstractTraceEvent prevEvent,
				final Class<? extends CallOperationEvent> callClass) {
			if ((prevEvent != null) && prevEvent.getClass().equals(callClass) && (prevEvent.getOrderIndex() == (beforeOperationEvent.getOrderIndex() - 1))) {
				if (((CallOperationEvent) prevEvent).callsReferencedOperationOf(beforeOperationEvent)) {
					return true;
				} else if (this.enhanceCallDetection) { // perhaps we don't find a perfect match, but can guess one!
					final boolean isConstructor = beforeOperationEvent instanceof BeforeConstructorEvent;
					final CallOperationEvent callEvent = (CallOperationEvent) prevEvent;
					final Signature callSignature = ClassOperationSignaturePair.splitOperationSignatureStr(callEvent.getCalleeOperationSignature(),
							isConstructor && this.enhanceJavaConstructors).getSignature();
					final Signature afterSignature = ClassOperationSignaturePair.splitOperationSignatureStr(beforeOperationEvent.getOperationSignature(),
							isConstructor && this.enhanceJavaConstructors).getSignature();
					if (callSignature.equals(afterSignature)
							&& callEvent.getCalleeClassSignature().equals(beforeOperationEvent.getClassSignature())) {
						if (TraceEventRecords2ExecutionAndMessageTraceFilter.getLOG().isDebugEnabled()) {
							TraceEventRecords2ExecutionAndMessageTraceFilter.getLOG().debug("Guessed call of \n\t" + callEvent + "\n\t" + beforeOperationEvent);
						}
						return true;
					}
				}
			}
			return false;
		}

		private void handleAfterEvent(final AfterOperationEvent afterOperationEvent,
				final Class<? extends BeforeOperationEvent> beforeClass,
				final Class<? extends CallOperationEvent> callClass) throws InvalidTraceException {
			this.closeOpenCalls(afterOperationEvent);
			// Obtain the matching before-operation event from the stack
			final AbstractTraceEvent potentialBeforeEvent = this.peekEvent();
			// The element at the top of the stack needs to be a before-operation event...
			if ((potentialBeforeEvent == null) || !potentialBeforeEvent.getClass().equals(beforeClass)) {
				throw new InvalidTraceException("Didn't find corresponding "
						+ beforeClass.getName() + " for " + afterOperationEvent.getClass().getName() + " "
						+ afterOperationEvent.toString() + " (found: " + potentialBeforeEvent + ").");
			}
			// ... and must reference the same operation as the given after-operation event.
			if (!afterOperationEvent.refersToSameOperationAs((BeforeOperationEvent) potentialBeforeEvent)) {
				throw new InvalidTraceException("Components of before (" + potentialBeforeEvent + ") " + "and after (" + afterOperationEvent
						+ ") events do not match.");
			}
			final BeforeOperationEvent beforeOperationEvent = (BeforeOperationEvent) this.eventStack.pop();
			// Look for a call event at the top of the stack
			final AbstractTraceEvent prevEvent = this.peekEvent();
			// A definite call occurs if either the stack is empty (entry into the trace) or if a matching call event is found
			final boolean definiteCall = (prevEvent == null) || this.isPrevEventMatchingCall(beforeOperationEvent, prevEvent, callClass);
			// If a matching call event was found, it must be removed from the stack
			if (definiteCall && !this.eventStack.isEmpty()) {
				this.eventStack.pop();
			}
			final ExecutionInformation executionInformation = this.executionStack.pop();
			this.finishExecution(
					beforeOperationEvent.getOperationSignature(),
					beforeOperationEvent.getClassSignature(),
					this.trace.getTraceId(),
					this.trace.getSessionId(),
					this.trace.getHostname(),
					executionInformation.getEoi(),
					executionInformation.getEss(),
					beforeOperationEvent.getTimestamp(),
					afterOperationEvent.getTimestamp(),
					!definiteCall, beforeOperationEvent instanceof BeforeConstructorEvent);
		}

		public void handleAfterOperationEvent(final AfterOperationEvent afterOperationEvent) throws InvalidTraceException {
			this.handleAfterEvent(afterOperationEvent, BeforeOperationEvent.class, CallOperationEvent.class);
		}

		public void handleAfterOperationFailedEvent(final AfterOperationFailedEvent afterOperationEvent) throws InvalidTraceException {
			this.handleAfterEvent(afterOperationEvent, BeforeOperationEvent.class, CallOperationEvent.class);
		}

		public void handleAfterConstructorEvent(final AfterConstructorEvent afterConstructorEvent) throws InvalidTraceException {
			this.handleAfterEvent(afterConstructorEvent, BeforeConstructorEvent.class, CallConstructorEvent.class);
		}

		public void handleAfterConstructorFailedEvent(final AfterConstructorFailedEvent afterConstructorEvent) throws InvalidTraceException {
			this.handleAfterEvent(afterConstructorEvent, BeforeConstructorEvent.class, CallConstructorEvent.class);
		}

		public void handleCallOperationEvent(final CallOperationEvent callOperationEvent) throws InvalidTraceException {
			this.closeOpenCalls(callOperationEvent);
			this.registerExecution(callOperationEvent);
		}

		public void handleCallConstructorEvent(final CallConstructorEvent callConstructorEvent) throws InvalidTraceException {
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
