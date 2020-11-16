/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.trace.analysis.tt.trace.reconstruction;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import kieker.common.util.dataformat.LoggingTimestampConverter;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.Execution;
import kieker.model.system.model.ExecutionTrace;
import kieker.model.system.model.InvalidExecutionTrace;
import kieker.model.system.model.MessageTrace;
import kieker.model.system.model.exceptions.InvalidTraceException;
import kieker.tools.trace.analysis.filter.executionRecordTransformation.ExecutionEventProcessingException;
import kieker.tools.trace.analysis.tt.AbstractTraceProcessingFilter;

import teetime.framework.OutputPort;

/**
 * This is a trace reconstruction filter.
 * TODO documentation.
 *
 * @author Andre van Hoorn
 * @author Reiner Jung -- ported to teetime
 *
 * @since 1.1
 */
public class TraceReconstructionFilter extends AbstractTraceProcessingFilter<Execution> {

	private final OutputPort<MessageTrace> messageTraceOutputPort = this.createOutputPort(MessageTrace.class);
	private final OutputPort<ExecutionTrace> executionTraceOutputPort = this.createOutputPort(ExecutionTrace.class);
	private final OutputPort<InvalidExecutionTrace> invalidExecutionTraceOutputPort = this
			.createOutputPort(InvalidExecutionTrace.class);

	/** The used time unit. */
	private final TimeUnit timeunit;

	/** TraceId x trace. */
	private final Map<Long, ExecutionTrace> pendingTraces = new Hashtable<>(); // NOPMD (UseConcurrentHashMap)
	/** We need to keep track of invalid trace's IDs. */
	private final Set<Long> invalidTraces = new TreeSet<>();
	private volatile long minTin = -1;
	private volatile long maxTout = -1;
	private volatile boolean terminated;
	private final boolean ignoreInvalidTraces; // false
	private final long maxTraceDuration;

	private boolean traceProcessingErrorOccured; // false

	/** Pending traces sorted by tin timestamps. */
	private final NavigableSet<ExecutionTrace> timeoutMap = new TreeSet<>(new Comparator<ExecutionTrace>() {

		/** Order traces by tins */
		@Override
		public int compare(final ExecutionTrace t1, final ExecutionTrace t2) {
			if (t1 == t2) { // NOPMD (no equals)
				return 0;
			}
			final long t1LowestTin = t1.getTraceAsSortedExecutionSet().first().getTin();
			final long t2LowestTin = t2.getTraceAsSortedExecutionSet().first().getTin();

			// Multiple traces may have an equal tin timestamp value. In order to provide an
			// absolute ordering of the keys, we take the traceId as a second ordering
			// key.
			if (t1LowestTin != t2LowestTin) {
				return t1LowestTin < t2LowestTin ? -1 : 1; // NOCS
			}
			return t1.getTraceId() < t2.getTraceId() ? -1 : 1; // NOCS
		}
	});

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param repository
	 *            the system model repository
	 * @param timeunit
	 *            the used time unit, e.g., NANOSECONDS
	 * @param ignoreInvalidTraces
	 *            set whether invalid traces should be ignored
	 *            (true), or the filter must terminate (false)
	 * @param maxTraceDuration
	 *            max time duration for a trace, if null
	 *            Long.MAX_VALUE
	 */
	public TraceReconstructionFilter(final SystemModelRepository repository, final TimeUnit timeunit,
			final boolean ignoreInvalidTraces, final Long maxTraceDuration) {
		super(repository);

		this.timeunit = timeunit;
		this.maxTraceDuration = this.timeunit.convert(maxTraceDuration == null ? Long.MAX_VALUE : maxTraceDuration, // NOCS
				timeunit);
		this.ignoreInvalidTraces = ignoreInvalidTraces;

		if (this.maxTraceDuration < 0) {
			throw new IllegalArgumentException(
					"value maxTraceDurationMillis must not be negative (found: " + this.maxTraceDuration + ")");
		}
	}

	/**
	 * Returns a set of the IDs of invalid traces.
	 *
	 * @return a set of the IDs of invalid traces
	 */
	public Set<Long> getInvalidTraces() {
		return this.invalidTraces;
	}

	/**
	 * Returns the minimum tin timestamp of a processed execution.
	 *
	 * @return the minimum tin timestamp of a processed execution
	 */
	public final long getMinTin() {
		return this.minTin;
	}

	/**
	 * Returns the maximum tout timestamp of a processed execution.
	 *
	 * @return the maximum tout timestamp of a processed execution
	 */
	public final long getMaxTout() {
		return this.maxTout;
	}

	@Override
	protected void execute(final Execution execution) throws Exception {
		synchronized (this) {
			if (this.terminated || (this.traceProcessingErrorOccured && !this.ignoreInvalidTraces)) {
				return;
			}

			final long traceId = execution.getTraceId();

			this.minTin = ((this.minTin < 0) || (execution.getTin() < this.minTin)) ? execution.getTin() : this.minTin; // NOCS
			this.maxTout = execution.getTout() > this.maxTout ? execution.getTout() : this.maxTout; // NOCS

			ExecutionTrace executionTrace = this.pendingTraces.get(traceId);
			if (executionTrace != null) { // trace (artifacts) exists already;
				if (!this.timeoutMap.remove(executionTrace)) { // remove from timeoutMap. Will be re-added below
					this.logger.error(
							"Missing entry for trace in timeoutMap: {} PendingTraces and timeoutMap are now longer consistent!",
							executionTrace);
					this.reportError(traceId);
				}
			} else { // create and add new trace
				executionTrace = new ExecutionTrace(traceId, execution.getSessionId());
				this.pendingTraces.put(traceId, executionTrace);
			}
			try {
				executionTrace.add(execution);
				if (!this.timeoutMap.add(executionTrace)) { // (re-)add trace to timeoutMap
					this.logger.error("Equal entry existed in timeoutMap already: {}", executionTrace);
				}
				this.processTimeoutQueue();
			} catch (final InvalidTraceException ex) { // this would be a bug!
				this.logger.error("Attempt to add record to wrong trace", ex);
			} catch (final ExecutionEventProcessingException ex) {
				this.logger.error("ExecutionEventProcessingException occured while processing the timeout queue.", ex);
			}
		}

	}

	/**
	 * Transforms the execution trace is delivers the trace to the output ports of
	 * this filter (message trace and execution trace output ports, or invalid
	 * execution trace output port respectively).
	 *
	 * @param executionTrace
	 *            The execution trace to transform.
	 *
	 * @throws ExecutionEventProcessingException
	 *             if the passed execution trace is
	 *             invalid and this filter is
	 *             configured to fail on the
	 *             occurrence of invalid traces.
	 */
	private void processExecutionTrace(final ExecutionTrace executionTrace) throws ExecutionEventProcessingException {
		final long curTraceId = executionTrace.getTraceId();
		try {
			// If the polled trace is invalid, the following method toMessageTrace(..)
			// throws an exception
			final MessageTrace messageTrace = executionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);

			// Transformation successful and the trace is for itself valid. However, this
			// trace may actually contain the [0,0] execution and thus complete a trace
			// that has timed out before and has thus been considered an invalid trace.
			if (!this.invalidTraces.contains(messageTrace.getTraceId())) {
				// Not completing part of an invalid trace
				this.messageTraceOutputPort.send(messageTrace);
				this.executionTraceOutputPort.send(executionTrace);
				this.reportSuccess(curTraceId);
			} else {
				// mt is the completing part of an invalid trace
				this.invalidExecutionTraceOutputPort.send(new InvalidExecutionTrace(executionTrace));
				// the statistics have been updated on the first
				// occurrence of artifacts of this trace
			}
		} catch (final InvalidTraceException ex) {
			// Transformation failed (i.e., trace invalid)
			this.invalidExecutionTraceOutputPort.send(new InvalidExecutionTrace(executionTrace));
			final String transformationError = "Failed to transform execution trace to message trace (ID: " + curTraceId
					+ "). \n" + "Reason: " + ex.getMessage() + "\n Trace: " + executionTrace;
			if (!this.invalidTraces.contains(curTraceId)) {
				// only once per traceID (otherwise, we would report all
				// trace fragments)
				this.reportError(curTraceId);
				this.invalidTraces.add(curTraceId);
				if (!this.ignoreInvalidTraces) {
					this.traceProcessingErrorOccured = true;
					this.logger.warn("Filter configurred to terminate on first invalid trace.");
					throw new ExecutionEventProcessingException(transformationError, ex);
				} else {
					this.logger.error(transformationError); // do not pass 'ex' to log.error because this makes the
															// output verbose (#584)
				}
			} else {
				this.logger.warn("Found additional fragment for trace already marked invalid: {}", transformationError);
			}
		}
	}

	/**
	 * Processes the pending traces in the timeout queue: Either those, that timed
	 * out are all, if the filter was requested to terminate.
	 *
	 * @throws ExecutionEventProcessingException
	 */
	private void processTimeoutQueue() throws ExecutionEventProcessingException {
		synchronized (this.timeoutMap) {
			while (!this.timeoutMap.isEmpty() && (this.terminated
					|| ((this.maxTout - this.timeoutMap.first().getMinTin()) > this.maxTraceDuration))) {
				final ExecutionTrace polledTrace = this.timeoutMap.pollFirst();
				final long curTraceId = polledTrace.getTraceId();
				this.pendingTraces.remove(curTraceId);
				this.processExecutionTrace(polledTrace);
			}
		}
	}

	/**
	 * Return the number of timeunits after which a pending trace is considered to
	 * have timed out.
	 *
	 * @return the timeout duration for a pending trace in timeunits
	 */
	public final long getMaxTraceDuration() {
		synchronized (this) {
			return this.maxTraceDuration;
		}
	}

	@Override
	protected void onTerminating() {
		synchronized (this) {
			try {
				this.terminated = true;
				this.processTimeoutQueue();
			} catch (final ExecutionEventProcessingException ex) {
				this.traceProcessingErrorOccured = true;
				this.logger.error("Error processing timeout queue", ex);
			}
		}
		super.onTerminating();
	}

	@Override
	public void printStatusMessage() {
		synchronized (this) {
			super.printStatusMessage();
			if ((this.getSuccessCount() > 0) || (this.getErrorCount() > 0)) {
				final String minTinStr = new StringBuilder().append(this.minTin).append(" (")
						.append(LoggingTimestampConverter
								.convertLoggingTimestampToUTCString(this.timeunit.toNanos(this.minTin)))
						.append(',')
						.append(LoggingTimestampConverter.convertLoggingTimestampLocalTimeZoneString(this.minTin))
						.append(')').toString();
				final String maxToutStr = new StringBuilder().append(this.maxTout).append(" (")
						.append(LoggingTimestampConverter
								.convertLoggingTimestampToUTCString(this.timeunit.toNanos(this.maxTout)))
						.append(',')
						.append(LoggingTimestampConverter.convertLoggingTimestampLocalTimeZoneString(this.maxTout))
						.append(')').toString();
				this.logger.debug("First timestamp: {}", minTinStr);
				this.logger.debug("Last timestamp: {}", maxToutStr);
			}
		}
	}

	public OutputPort<MessageTrace> getMessageTraceOutputPort() {
		return this.messageTraceOutputPort;
	}

	public OutputPort<ExecutionTrace> getExecutionTraceOutputPort() {
		return this.executionTraceOutputPort;
	}

	public OutputPort<InvalidExecutionTrace> getInvalidExecutionTraceOutputPort() {
		return this.invalidExecutionTraceOutputPort;
	}

}
