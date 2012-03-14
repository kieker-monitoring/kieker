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

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.AbstractTraceEventVisitor;
import kieker.common.record.flow.trace.SplitEvent;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.flow.trace.operation.CallOperationEvent;
import kieker.tools.traceAnalysis.plugins.AbstractTraceAnalysisPlugin;
import kieker.tools.traceAnalysis.plugins.AbstractTraceProcessingPlugin;
import kieker.tools.traceAnalysis.plugins.traceReconstruction.InvalidTraceException;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * 
 * @author Andre van Hoorn, Holger Knoche
 * 
 */
@Plugin(
		outputPorts = {
			@OutputPort(name = EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE, description = "Outputs transformed execution traces", eventTypes = { ExecutionTrace.class }),
			@OutputPort(name = EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, description = "Outputs transformed message traces", eventTypes = { MessageTrace.class })
		},
		repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, repositoryType = SystemModelRepository.class))
public class EventTrace2ExecutionTraceFilter extends AbstractTraceProcessingPlugin {

	private static class ExecutionInformation {

		private final int executionIndex;
		private final int stackDepth;

		public ExecutionInformation(final int executionIndex, final int stackDepth) {
			this.executionIndex = executionIndex;
			this.stackDepth = stackDepth;
		}

		public int getExecutionIndex() {
			return this.executionIndex;
		}

		public int getStackDepth() {
			return this.stackDepth;
		}

	}

	private static class FilterState {

		private final Stack<AbstractTraceEvent> eventStack = new Stack<AbstractTraceEvent>();
		private final Stack<ExecutionInformation> executionStack = new Stack<ExecutionInformation>();

		private int nextExecutionIndex = 0;
		private int currentStackDepth = 0;

		public void pushEvent(final AbstractTraceEvent event) {
			this.eventStack.push(event);
		}

		public AbstractTraceEvent peekEvent() {
			return this.eventStack.peek();
		}

		public AbstractTraceEvent popEvent() {
			return this.eventStack.pop();
		}

		public boolean isEventStackEmpty() {
			return this.eventStack.isEmpty();
		}

		public void registerExecution(final AbstractTraceEvent cause) {
			this.pushEvent(cause);
			final ExecutionInformation executionInformation = new ExecutionInformation(this.nextExecutionIndex++, this.currentStackDepth++);
			this.executionStack.push(executionInformation);
		}

		public ExecutionInformation popExecution() {
			this.currentStackDepth--;
			return this.executionStack.pop();
		}
	}

	private class EventProcessor implements AbstractTraceEventVisitor {

		private final FilterState filterState;
		private final ExecutionTrace executionTrace;
		private final EventRecordStream eventStream;

		public EventProcessor(final FilterState filterState, final EventRecordStream eventStream, final ExecutionTrace executionTrace) {
			this.filterState = filterState;
			this.eventStream = eventStream;
			this.executionTrace = executionTrace;
		}

		public void processEvent(final AbstractTraceEvent event) {
			event.accept(this);
		}

		private void handleUnsupportedEvent(final AbstractTraceEvent event) {
			EventTrace2ExecutionTraceFilter.LOG.warn("Trace Events of type " + event.getClass().getName() + " not supported yet.");
		}

		private BeforeOperationEvent getMatchingBeforeEventFor(final AfterOperationEvent afterOperationEvent) throws InvalidEventTraceException {
			final AbstractTraceEvent potentialBeforeEvent = (this.filterState.isEventStackEmpty()) ? null : this.filterState.popEvent();

			if ((potentialBeforeEvent == null) || !(potentialBeforeEvent instanceof BeforeOperationEvent)) {
				final String message = "Didn't find corresponding BeforeOperationEvent for AfterOperationEvent " + afterOperationEvent + " (found: )"
						+ potentialBeforeEvent + ".";
				throw new InvalidEventTraceException(message);
			}

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

		private void closeOpenCalls(final AfterOperationEvent lastEvent) {
			while (!this.filterState.isEventStackEmpty()) {
				final AbstractTraceEvent nextEvent = this.filterState.peekEvent();

				if (!(nextEvent instanceof CallOperationEvent)) {
					break;
				}

				final CallOperationEvent currentCallEvent = (CallOperationEvent) this.filterState.popEvent();
				final ExecutionInformation executionInformation = this.filterState.popExecution();
				final Execution execution = EventTrace2ExecutionTraceFilter.this.callOperationToExecution(
						currentCallEvent,
						this.executionTrace.getTraceId(),
						executionInformation.getExecutionIndex(),
						executionInformation.getStackDepth(),
						currentCallEvent.getTimestamp(),
						lastEvent.getTimestamp(),
						true);

				this.registerExecution(execution);
			}
		}

		@Override
		public void handleAfterOperationEvent(final AfterOperationEvent afterOperationEvent) {
			final BeforeOperationEvent beforeOperationEvent = this.getMatchingBeforeEventFor(afterOperationEvent);

			final AbstractTraceEvent potentialCallEvent = (this.filterState.isEventStackEmpty()) ? null : this.filterState.peekEvent();
			final boolean definiteCall = ((potentialCallEvent == null)
					|| ((potentialCallEvent instanceof CallOperationEvent) && ((CallOperationEvent) potentialCallEvent)
					.callsReferencedOperationOf(afterOperationEvent)));

			if (definiteCall && !this.filterState.isEventStackEmpty()) {
				this.filterState.popEvent();
			}

			final ExecutionInformation executionInformation = this.filterState.popExecution();
			final Execution execution = EventTrace2ExecutionTraceFilter.this.beforeOperationToExecution(
					beforeOperationEvent,
					this.executionTrace.getTraceId(),
					executionInformation.getExecutionIndex(),
					executionInformation.getStackDepth(),
					beforeOperationEvent.getTimestamp(),
					afterOperationEvent.getTimestamp(),
					!definiteCall);

			this.registerExecution(execution);

			// Close remaining open calls
			this.closeOpenCalls(afterOperationEvent);
		}

		@Override
		public void handleAfterOperationFailedEvent(final AfterOperationFailedEvent afterOperationFailedEvent) {
			this.handleUnsupportedEvent(afterOperationFailedEvent);
		}

		@Override
		public void handleBeforeOperationEvent(final BeforeOperationEvent beforeOperationEvent) {
			this.filterState.registerExecution(beforeOperationEvent);
		}

		@Override
		public void handleCallOperationEvent(final CallOperationEvent callOperationEvent) {
			final AbstractTraceEvent nextEvent = this.eventStream.lookahead(1);

			if ((nextEvent == null)
					|| !(nextEvent instanceof BeforeOperationEvent)
					|| !callOperationEvent.callsReferencedOperationOf((BeforeOperationEvent) nextEvent)) {
				this.filterState.registerExecution(callOperationEvent);
			}
			else {
				this.filterState.pushEvent(callOperationEvent);
			}
		}

		@Override
		public void handleSplitEvent(final SplitEvent splitEvent) {
			this.handleUnsupportedEvent(splitEvent);
		}
	}

	private static class InvalidEventTraceException extends RuntimeException {

		private static final long serialVersionUID = -6187153581658321041L;

		public InvalidEventTraceException(final String message) {
			super(message);
		}

		public InvalidEventTraceException(final String message, final Throwable cause) {
			super(message, cause);
		}

	}

	private static final Log LOG = LogFactory.getLog(EventTrace2ExecutionTraceFilter.class);

	public static final String INPUT_PORT_NAME = "inputEventTrace";
	public static final String OUTPUT_PORT_NAME_EXECUTION_TRACE = "outputExecutionTrace";
	public static final String OUTPUT_PORT_NAME_MESSAGE_TRACE = "outputMessageTrace";

	public EventTrace2ExecutionTraceFilter(final Configuration configuration) {
		super(configuration);
	}

	@InputPort(name = EventTrace2ExecutionTraceFilter.INPUT_PORT_NAME, description = "Receives event record traces to be transformed", eventTypes = { EventRecordTrace.class })
	public void inputEventTrace(final EventRecordTrace eventTrace) {
		final ExecutionTrace execTrace = new ExecutionTrace(eventTrace.getTraceId());
		final EventRecordStream eventStream = new EventRecordStream(eventTrace);
		final FilterState state = new FilterState();
		final EventProcessor eventProcessor = new EventProcessor(state, eventStream, execTrace);

		long lastOrderIndex = -1; // used to check for ascending order indices

		try {
			while (true) {
				final AbstractTraceEvent currentEvent = eventStream.currentElement();

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

				eventProcessor.processEvent(currentEvent);
				eventStream.consume();
			}
		} catch (final InvalidEventTraceException e) {
			EventTrace2ExecutionTraceFilter.LOG.error(e.getMessage() + "\n"
					+ "Terminating processing of event record trace with ID " + execTrace.getTraceId(), e);
		}

		super.deliver(EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE, execTrace);
		try {
			super.deliver(EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, execTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION));
			super.reportSuccess(execTrace.getTraceId());
		} catch (final InvalidTraceException ex) {
			// TODO send to new output port for defect traces
		}
	}

	private void printInvalidIndexMessage(final AbstractTraceEvent event, final ExecutionTrace executionTrace, final long lastIndex) {
		EventTrace2ExecutionTraceFilter.LOG.error("Trace events' order indices must increment by one: " +
				" Found " + lastIndex + " followed by " + event.getOrderIndex() + " event (" + event + ")");
		EventTrace2ExecutionTraceFilter.LOG.error("Terminating processing of event record trace with ID " + executionTrace.getTraceId());
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
			final long tin, final long tout, final boolean assumed) {
		// TODO: hostname and sessionid
		final String hostName = "<HOST>";
		final String sessionId = "<SESSION-ID>";

		final FQComponentNameSignaturePair fqComponentNameSignaturePair = super.splitOperationSignatureStr(event.getOperationSignature());

		return super.createExecutionByEntityNames(hostName, fqComponentNameSignaturePair.getFqClassname(), fqComponentNameSignaturePair.getSignature(),
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
	private Execution callOperationToExecution(final CallOperationEvent event, final long traceId, final int eoi, final int ess,
			final long tin, final long tout, final boolean assumed) {
		// TODO: hostname and sessionid
		final String hostName = "<HOST>";
		final String sessionId = "<SESSION-ID>";

		final FQComponentNameSignaturePair fqComponentNameSignaturePair = super.splitOperationSignatureStr(event.getCalleeOperationSiganture());
		return super.createExecutionByEntityNames(hostName, fqComponentNameSignaturePair.getFqClassname(),
				fqComponentNameSignaturePair.getSignature(), traceId, sessionId, eoi, ess, tin, tout, assumed);
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
