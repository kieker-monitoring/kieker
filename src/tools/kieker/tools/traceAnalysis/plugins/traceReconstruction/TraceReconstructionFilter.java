/***************************************************************************
 * Copyright 2011 by
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

package kieker.tools.traceAnalysis.plugins.traceReconstruction;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.Set;
import java.util.TreeSet;

import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.IInputPort;
import kieker.analysis.plugin.configuration.IOutputPort;
import kieker.analysis.plugin.configuration.OutputPort;
import kieker.tools.traceAnalysis.plugins.AbstractTraceProcessingPlugin;
import kieker.tools.traceAnalysis.plugins.executionRecordTransformation.ExecutionEventProcessingException;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.InvalidExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.tools.util.LoggingTimestampConverter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Andre van Hoorn
 */
public class TraceReconstructionFilter extends AbstractTraceProcessingPlugin {

	private static final Log LOG = LogFactory.getLog(TraceReconstructionFilter.class);
	private static final long MAX_DURATION_NANOS = Long.MAX_VALUE;
	public static final int MAX_DURATION_MILLIS = Integer.MAX_VALUE;
	/** TraceId x trace */
	private final Hashtable<Long, ExecutionTrace> pendingTraces = new Hashtable<Long, ExecutionTrace>();
	/** We need to keep track of invalid trace's IDs */
	private final Set<Long> invalidTraces = new TreeSet<Long>();
	private volatile long minTin = -1;
	private volatile long maxTout = -1;
	private volatile boolean terminate = false;
	private final boolean ignoreInvalidTraces;
	private final Execution rootExecution;
	private final long maxTraceDurationNanos;
	/** Pending traces sorted by tin timestamps */
	private final TreeSet<ExecutionTrace> timeoutMap = new TreeSet<ExecutionTrace>(new Comparator<ExecutionTrace>() {

		/** Order traces by tins */
		@Override
		public int compare(final ExecutionTrace t1, final ExecutionTrace t2) {
			if (t1 == t2) {
				return 0;
			}
			final long t1LowestTin = t1.getTraceAsSortedExecutionSet().first().getTin();
			final long t2LowestTin = t2.getTraceAsSortedExecutionSet().first().getTin();

			/**
			 * Multiple traces may have an equal tin timestamp value.
			 * In order to provide an absolute ordering of the keys,
			 * we take the traceId as a second ordering key.
			 */
			if (t1LowestTin != t2LowestTin) {
				return t1LowestTin < t2LowestTin ? -1 : 1; // NOCS
			}
			return t1.getTraceId() < t2.getTraceId() ? -1 : 1; // NOCS
		}
	});

	public TraceReconstructionFilter(final String name, final SystemModelRepository systemEntityFactory, final long maxTraceDurationMillis,
			final boolean ignoreInvalidTraces) {
		super(name, systemEntityFactory);
		this.rootExecution = systemEntityFactory.getRootExecution();
		if (maxTraceDurationMillis < 0) {
			throw new IllegalArgumentException("value maxTraceDurationMillis must not be negative (found: " + maxTraceDurationMillis + ")");
		}
		if (maxTraceDurationMillis == TraceReconstructionFilter.MAX_DURATION_MILLIS) {
			this.maxTraceDurationNanos = TraceReconstructionFilter.MAX_DURATION_NANOS;
		} else {
			this.maxTraceDurationNanos = maxTraceDurationMillis * (1000 * 1000);
		}
		this.ignoreInvalidTraces = ignoreInvalidTraces;
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
	public boolean execute() {
		return true; // no need to do anything here
	}

	private void newExecution(final Execution execution) {
		final long traceId = execution.getTraceId();

		this.minTin = ((this.minTin < 0) || (execution.getTin() < this.minTin)) ? execution.getTin() : this.minTin; // NOCS
		this.maxTout = execution.getTout() > this.maxTout ? execution.getTout() : this.maxTout; // NOCS

		ExecutionTrace executionTrace = this.pendingTraces.get(traceId);
		if (executionTrace != null) { /* trace (artifacts) exists already; */
			if (!this.timeoutMap.remove(executionTrace)) { /* remove from timeoutMap. Will be re-added below */
				TraceReconstructionFilter.LOG.fatal("Missing entry for trace in timeoutMap: " + executionTrace);
				TraceReconstructionFilter.LOG.fatal("pendingTraces and timeoutMap are now longer consistent!");
				this.reportError(traceId);
			}
		} else { /* create and add new trace */
			executionTrace = new ExecutionTrace(traceId);
			this.pendingTraces.put(traceId, executionTrace);
		}
		try {
			executionTrace.add(execution);
			if (!this.timeoutMap.add(executionTrace)) { // (re-)add trace to timeoutMap
				TraceReconstructionFilter.LOG.error("Equal entry existed in timeoutMap already:" + executionTrace);
			}
			this.processTimeoutQueue();
		} catch (final InvalidTraceException ex) { // this would be a bug!
			TraceReconstructionFilter.LOG.fatal("Attempt to add record to wrong trace", ex);
		} catch (final ExecutionEventProcessingException ex) {
			TraceReconstructionFilter.LOG.error("ExecutionEventProcessingException occured while processing " + "the timeout queue. ", ex);
		}
	}

	/**
	 * Transforms the execution trace is delivers the trace to the output ports
	 * of this filter (message trace and execution trace output ports, or invalid
	 * execution trace output port respectively).
	 * 
	 * @param executionTrace
	 * @throws ExecutionEventProcessingException
	 *             if the passed execution trace is
	 *             invalid and this filter is configured to fail on the occurrence of invalid
	 *             traces.
	 */
	private void processExecutionTrace(final ExecutionTrace executionTrace) throws ExecutionEventProcessingException {
		final long curTraceId = executionTrace.getTraceId();
		try {
			/*
			 * If the polled trace is invalid, the following method
			 * toMessageTrace(..) throws an exception
			 */
			final MessageTrace mt = executionTrace.toMessageTrace(this.rootExecution);

			/*
			 * Transformation successful and the trace is for itself valid.
			 * However, this trace may actually contain the [0,0] execution
			 * and thus complete a trace that has timed out before and has
			 * thus been considered an invalid trace.
			 */
			if (!this.invalidTraces.contains(mt.getTraceId())) {
				/* Not completing part of an invalid trace */
				this.messageTraceOutputPort.deliver(mt);
				this.executionTraceOutputPort.deliver(executionTrace);
				this.reportSuccess(curTraceId);
			} else {
				/* mt is the completing part of an invalid trace */
				this.invalidExecutionTraceOutputPort.deliver(new InvalidExecutionTrace(executionTrace));
				// the statistics have been updated on the first
				// occurrence of artifacts of this trace
			}
		} catch (final InvalidTraceException ex) {
			/* Transformation failed (i.e., trace invalid) */
			this.invalidExecutionTraceOutputPort.deliver(new InvalidExecutionTrace(executionTrace));
			if (!this.invalidTraces.contains(curTraceId)) {
				// only once per traceID (otherwise, we would report all
				// trace fragments)
				this.reportError(curTraceId);
				this.invalidTraces.add(curTraceId);
				if (!this.ignoreInvalidTraces) {
					TraceReconstructionFilter.LOG.error("Failed to transform execution trace to message trace (ID:" + curTraceId + "): " + executionTrace, ex);
					throw new ExecutionEventProcessingException("Failed to transform execution trace to message trace (ID:" + curTraceId + "): " + executionTrace,
							ex);
				}
			}
		}
	}

	/**
	 * Processes the pending traces in the timeout queue: Either those,
	 * that timed out are all, if the filter was requested to terminate.
	 * 
	 * @throws ExecutionEventProcessingException
	 */
	private void processTimeoutQueue() throws ExecutionEventProcessingException {
		synchronized (this.timeoutMap) {
			while (!this.timeoutMap.isEmpty() && (this.terminate || ((this.maxTout - this.timeoutMap.first().getMinTin()) > this.maxTraceDurationNanos))) {
				final ExecutionTrace polledTrace = this.timeoutMap.pollFirst();
				final long curTraceId = polledTrace.getTraceId();
				this.pendingTraces.remove(curTraceId);
				this.processExecutionTrace(polledTrace);
			}
		}
	}

	/**
	 * Return the number of nanoseconds after which a pending trace is
	 * considered to have timed out
	 * 
	 * @return the timeout duration for a pending trace in nanoseconds
	 */
	public final long getMaxTraceDurationNanos() {
		return this.maxTraceDurationNanos;
	}

	/**
	 * Terminates the filter (internally, all pending traces are processed).
	 * 
	 * @param error
	 */
	@Override
	public void terminate(final boolean error) {
		try {
			this.terminate = true;
			if (!error) {
				this.processTimeoutQueue();
			} else {
				TraceReconstructionFilter.LOG.info("terminate called with error flag set; won't process timeoutqueue any more.");
			}
		} catch (final ExecutionEventProcessingException ex) {
			TraceReconstructionFilter.LOG.error("Error processing queue", ex);
		}
	}

	@Override
	public void printStatusMessage() {
		super.printStatusMessage();
		final String minTinStr = new StringBuilder().append(this.minTin).append(" (")
				.append(LoggingTimestampConverter.convertLoggingTimestampToUTCString(this.minTin)).append(",")
				.append(LoggingTimestampConverter.convertLoggingTimestampLocalTimeZoneString(this.minTin)).append(")").toString();
		final String maxToutStr = new StringBuilder().append(this.maxTout).append(" (")
				.append(LoggingTimestampConverter.convertLoggingTimestampToUTCString(this.maxTout)).append(",")
				.append(LoggingTimestampConverter.convertLoggingTimestampLocalTimeZoneString(this.maxTout)).append(")").toString();
		System.out.println("First timestamp: " + minTinStr);
		System.out.println("Last timestamp: " + maxToutStr);
	}

	public IInputPort<Execution> getExecutionInputPort() {
		return this.executionInputPort;
	}

	private final IInputPort<Execution> executionInputPort = new AbstractInputPort<Execution>("Execution input") {

		@Override
		public void newEvent(final Execution event) {
			TraceReconstructionFilter.this.newExecution(event);
		}
	};

	public IOutputPort<MessageTrace> getMessageTraceOutputPort() {
		return this.messageTraceOutputPort;
	}

	private final OutputPort<MessageTrace> messageTraceOutputPort = new OutputPort<MessageTrace>("Reconstructed Message Traces");

	public IOutputPort<ExecutionTrace> getExecutionTraceOutputPort() {
		return this.executionTraceOutputPort;
	}

	private final OutputPort<ExecutionTrace> executionTraceOutputPort = new OutputPort<ExecutionTrace>("Reconstructed Execution Traces");

	public IOutputPort<InvalidExecutionTrace> getInvalidExecutionTraceOutputPort() {
		return this.invalidExecutionTraceOutputPort;
	}

	private final OutputPort<InvalidExecutionTrace> invalidExecutionTraceOutputPort = new OutputPort<InvalidExecutionTrace>("Invalid Execution Traces");
}
