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
import kieker.common.record.flow.trace.Trace;
import kieker.common.record.flow.trace.concurrency.SplitEvent;
import kieker.common.record.flow.trace.operation.AbstractOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.flow.trace.operation.CallOperationEvent;
import kieker.common.record.flow.trace.operation.constructor.AfterConstructorEvent;
import kieker.common.record.flow.trace.operation.constructor.AfterConstructorFailedEvent;
import kieker.common.record.flow.trace.operation.constructor.BeforeConstructorEvent;
import kieker.common.util.ClassOperationSignaturePair;
import kieker.common.util.Signature;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.traceReconstruction.InvalidTraceException;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * @author Andre van Hoorn, Holger Knoche, Jan Waller
 */
@Plugin(
		outputPorts = {
			@OutputPort(name = TraceEvents2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE,
					description = "Outputs transformed execution traces",
					eventTypes = { ExecutionTrace.class }),
			@OutputPort(name = TraceEvents2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
					description = "Outputs transformed message traces",
					eventTypes = { MessageTrace.class }) },
		repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class))
public class TraceEvents2ExecutionAndMessageTraceFilter extends AbstractTraceProcessingFilter {
	public static final String INPUT_PORT_NAME_EVENT_TRACE = "traceEvents";
	public static final String OUTPUT_PORT_NAME_EXECUTION_TRACE = "executionTrace";
	public static final String OUTPUT_PORT_NAME_MESSAGE_TRACE = "messageTrace";

	public static final String CONFIG_ENHANCE_JAVA_CONSTRUCTORS = "enhanceJavaConstructors";

	private static final Log LOG = LogFactory.getLog(TraceEvents2ExecutionAndMessageTraceFilter.class);

	private final boolean enhanceJavaConstructors;

	public TraceEvents2ExecutionAndMessageTraceFilter(final Configuration configuration) {
		super(configuration);
		this.enhanceJavaConstructors = configuration.getBooleanProperty(CONFIG_ENHANCE_JAVA_CONSTRUCTORS);
	}

	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_ENHANCE_JAVA_CONSTRUCTORS, String.valueOf(this.enhanceJavaConstructors));
		return configuration;
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_ENHANCE_JAVA_CONSTRUCTORS, String.valueOf(true));
		return configuration;
	}

	@InputPort(name = INPUT_PORT_NAME_EVENT_TRACE, description = "Receives TraceEvents to be transformed", eventTypes = { TraceEvents.class })
	public void inputTraceEvents(final TraceEvents traceEvents) {
		final Trace trace = traceEvents.getTrace();
		if (trace == null) {
			LOG.error("Trace is missing from TraceEvents");
			return;
		}
		final long traceId = trace.getTraceId();
		final ExecutionTrace executionTrace = new ExecutionTrace(traceId, trace.getSessionId());
		final TraceEventHandler traceEventHandler = new TraceEventHandler(trace, executionTrace, this.getSystemEntityFactory(), this.enhanceJavaConstructors);
		int expectedOrderIndex = 0;
		for (final AbstractTraceEvent event : traceEvents.getTraceEvents()) {
			if (event.getOrderIndex() != expectedOrderIndex++) {
				LOG.error("Found event with wrong orderIndex. Found: " + event.getOrderIndex() + " expected: " + (expectedOrderIndex - 1));
				continue; // simply ignore wrong event
			}
			if (event.getTraceId() != traceId) {
				LOG.error("Found event with wrong traceId. Found: " + event.getTraceId() + " expected: " + traceId);
				continue; // simply ignore wrong event
			}
			try { // handle all cases (in order of inheritance!!!)
				if (event instanceof BeforeConstructorEvent) {
					// TODO: use an own handler?
					traceEventHandler.handleBeforeOperationEvent((BeforeConstructorEvent) event);
				} else if (event instanceof AfterConstructorFailedEvent) {
					// TODO: use an own handler?
					traceEventHandler.handleAfterConstructorEvent((AfterConstructorEvent) event);
				} else if (event instanceof AfterConstructorEvent) {
					traceEventHandler.handleAfterConstructorEvent((AfterConstructorEvent) event);
				} else if (event instanceof BeforeOperationEvent) {
					traceEventHandler.handleBeforeOperationEvent((BeforeOperationEvent) event);
				} else if (event instanceof AfterOperationFailedEvent) {
					// TODO: use an own handler?
					traceEventHandler.handleAfterOperationEvent((AfterOperationEvent) event);
				} else if (event instanceof AfterOperationEvent) {
					traceEventHandler.handleAfterOperationEvent((AfterOperationEvent) event);
				} else if (event instanceof CallOperationEvent) {
					traceEventHandler.handleCallOperationEvent((CallOperationEvent) event);
				} else if (event instanceof SplitEvent) {
					LOG.warn("Events of type 'SplitEvent' are currently not handled and ignored.");
				} else {
					LOG.warn("Events of type '" + event.getClass().getName() + "' are currently not handled and ignored.");
				}
			} catch (final InvalidTraceException ex) {
				LOG.error("Failed to reconstruct trace.", ex);
				return;
			}
		}
		super.deliver(OUTPUT_PORT_NAME_EXECUTION_TRACE, executionTrace);
		try {
			super.deliver(OUTPUT_PORT_NAME_MESSAGE_TRACE, executionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION));
			super.reportSuccess(executionTrace.getTraceId());
		} catch (final InvalidTraceException ex) {
			LOG.warn("Failed to convert to message trace.", ex);
			// TODO: send to new output port for defect traces
		}
	}

	/**
	 * This class encapsulates the trace's state, that is, the current event and execution stacks.
	 */
	private static class TraceEventHandler {
		private final SystemModelRepository systemModelRepository;
		private final Trace trace;
		private final ExecutionTrace executionTrace;
		private final Stack<AbstractTraceEvent> eventStack = new Stack<AbstractTraceEvent>();
		private final Stack<ExecutionInformation> executionStack = new Stack<ExecutionInformation>();

		private final boolean enhanceJavaConstructors;

		private int eoi = 0;

		public TraceEventHandler(final Trace trace, final ExecutionTrace executionTrace, final SystemModelRepository systemModelRepository,
				final boolean enhanceJavaConstructors) {
			this.trace = trace;
			this.executionTrace = executionTrace;
			this.systemModelRepository = systemModelRepository;
			this.enhanceJavaConstructors = enhanceJavaConstructors;
		}

		private AbstractTraceEvent peekEvent() {
			if (this.eventStack.isEmpty()) {
				return null;
			}
			return this.eventStack.peek();
		}

		private void registerExecution(final AbstractTraceEvent cause) {
			this.eventStack.push(cause);
			final ExecutionInformation executionInformation = new ExecutionInformation(this.eoi++, this.executionStack.size());
			this.executionStack.push(executionInformation);
		}

		private void finishExecution(final String operationSignature, final String classSignature, final long traceId, final String sessionId,
				final String hostname, final int eoi, final int ess, final long tin, final long tout, final boolean assumed, final boolean constructor)
				throws InvalidTraceException {
			final ClassOperationSignaturePair fqComponentNameSignaturePair = ClassOperationSignaturePair.splitOperationSignatureStr(operationSignature);
			final String classname;
			// FIXME: just another set of default cases! super calls aren't detected now!
			if (classSignature.length() == 0) {
				classname = fqComponentNameSignaturePair.getFqClassname();
			} else {
				classname = classSignature;
			}
			Signature signature = fqComponentNameSignaturePair.getSignature();
			if (constructor && this.enhanceJavaConstructors) {
				final String[] origModifiers = signature.getModifier();
				final String origReturnType = signature.getReturnType();
				final String[] modifiers;
				if (origReturnType == Signature.NO_RETURN_TYPE) { // == is the correct method!
					modifiers = origModifiers;
				} else {
					modifiers = new String[origModifiers.length + 1];
					System.arraycopy(origModifiers, 0, modifiers, 0, origModifiers.length);
					modifiers[origModifiers.length] = origReturnType;
				}
				signature = new Signature("new " + signature.getName(), modifiers, null, signature.getParamTypeList());
			}
			final Execution execution = AbstractTraceAnalysisFilter.createExecutionByEntityNames(this.systemModelRepository,
					hostname, classname, signature, traceId, sessionId, eoi, ess, tin, tout, assumed);
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
								executionInformation.getEOI(),
								executionInformation.getESS(),
								currentCallEvent.getTimestamp(),
								lastEvent.getTimestamp(),
								true, false);
					}
					return;
				}
			}
			while (!tmpEventStack.isEmpty()) {
				this.eventStack.push(tmpEventStack.pop());
				this.executionStack.push(tmpExecutionStack.pop());
			}
		}

		public void handleBeforeOperationEvent(final BeforeOperationEvent beforeOperationEvent) throws InvalidTraceException {
			final AbstractTraceEvent prevEvent = this.peekEvent();
			if ((prevEvent != null)
					&& (prevEvent instanceof CallOperationEvent)
					&& (((CallOperationEvent) prevEvent).callsReferencedOperationOf(beforeOperationEvent))) {
				this.eventStack.push(beforeOperationEvent);
			} else {
				this.closeOpenCalls(beforeOperationEvent);
				this.registerExecution(beforeOperationEvent);
			}
		}

		public void handleAfterOperationEvent(final AfterOperationEvent afterOperationEvent) throws InvalidTraceException {
			this.closeOpenCalls(afterOperationEvent);
			// Obtain the matching before-operation event from the stack
			final AbstractTraceEvent potentialBeforeEvent = this.peekEvent();
			// The element at the top of the stack needs to be a before-operation event...
			if ((potentialBeforeEvent == null) || !(potentialBeforeEvent instanceof BeforeOperationEvent)) {
				throw new InvalidTraceException("Didn't find corresponding BeforeOperationEvent for AfterOperationEvent " + afterOperationEvent + " (found: )"
						+ potentialBeforeEvent + ".");
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
			final boolean definiteCall = (prevEvent == null)
					|| ((prevEvent instanceof CallOperationEvent) && ((CallOperationEvent) prevEvent).callsReferencedOperationOf(afterOperationEvent));
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
					executionInformation.getEOI(),
					executionInformation.getESS(),
					beforeOperationEvent.getTimestamp(),
					afterOperationEvent.getTimestamp(),
					!definiteCall, false);
		}

		public void handleCallOperationEvent(final CallOperationEvent callOperationEvent) throws InvalidTraceException {
			this.closeOpenCalls(callOperationEvent);
			this.registerExecution(callOperationEvent);
		}

		public void handleAfterConstructorEvent(final AfterConstructorEvent afterConstructorEvent) throws InvalidTraceException {
			this.closeOpenCalls(afterConstructorEvent);
			// Obtain the matching before-constructor event from the stack
			final AbstractTraceEvent potentialBeforeEvent = this.peekEvent();
			// The element at the top of the stack needs to be a before-operation event...
			if ((potentialBeforeEvent == null) || !(potentialBeforeEvent instanceof BeforeConstructorEvent)) {
				throw new InvalidTraceException("Didn't find corresponding BeforeConstructorEvent for AfterConstructorEvent " + afterConstructorEvent + " (found: )"
						+ potentialBeforeEvent + ".");
			}
			// ... and must reference the same operation as the given after-operation event.
			if (!afterConstructorEvent.refersToSameOperationAs((BeforeConstructorEvent) potentialBeforeEvent)) {
				throw new InvalidTraceException("Components of before (" + potentialBeforeEvent + ") " + "and after (" + afterConstructorEvent
						+ ") events do not match.");
			}
			final BeforeConstructorEvent beforeConstructorEvent = (BeforeConstructorEvent) this.eventStack.pop();
			// Look for a call event at the top of the stack
			final AbstractTraceEvent prevEvent = this.peekEvent();
			// A definite call occurs if either the stack is empty (entry into the trace) or if a matching call event is found
			final boolean definiteCall = (prevEvent == null)
					|| ((prevEvent instanceof CallOperationEvent) && ((CallOperationEvent) prevEvent).callsReferencedOperationOf(afterConstructorEvent));
			// If a matching call event was found, it must be removed from the stack
			if (definiteCall && !this.eventStack.isEmpty()) {
				this.eventStack.pop();
			}
			final ExecutionInformation executionInformation = this.executionStack.pop();
			this.finishExecution(
					beforeConstructorEvent.getOperationSignature(),
					beforeConstructorEvent.getClassSignature(),
					this.trace.getTraceId(),
					this.trace.getSessionId(),
					this.trace.getHostname(),
					executionInformation.getEOI(),
					executionInformation.getESS(),
					beforeConstructorEvent.getTimestamp(),
					afterConstructorEvent.getTimestamp(),
					!definiteCall, true);
		}

		/**
		 * This class stores information about a specific execution.
		 */
		private static class ExecutionInformation {
			private final int eoi;
			private final int ess;

			public ExecutionInformation(final int executionIndex, final int stackDepth) {
				this.eoi = executionIndex;
				this.ess = stackDepth;
			}

			/**
			 * Returns the execution's execution order index.
			 * 
			 * @return See above
			 */
			public int getEOI() {
				return this.eoi;
			}

			/**
			 * Returns the stack depth at which the execution occurred.
			 * 
			 * @return See above
			 */
			public int getESS() {
				return this.ess;
			}
		}
	}
}
