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
package kieker.tools.trace.analysis.filter.traceReconstruction;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.analysis.trace.execution.ExecutionEventProcessingException;
import kieker.common.configuration.Configuration;
import kieker.tools.trace.analysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.trace.analysis.filter.AbstractTraceProcessingFilter;
import kieker.tools.trace.analysis.systemModel.Execution;
import kieker.tools.trace.analysis.systemModel.ExecutionTrace;
import kieker.tools.trace.analysis.systemModel.InvalidExecutionTrace;
import kieker.tools.trace.analysis.systemModel.MessageTrace;
import kieker.tools.trace.analysis.systemModel.repository.SystemModelRepository;
import kieker.tools.util.LoggingTimestampConverter;

/**
 * @author Andre van Hoorn
 *
 * @since 1.1
 * @deprecated 1.15 ported to teetime
 */
@Deprecated
@Plugin(description = "Uses the incoming data to enrich the connected repository with the reconstructed traces", outputPorts = {
	@OutputPort(name = TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, description = "Reconstructed Message Traces",
			eventTypes = MessageTrace.class),
	@OutputPort(name = TraceReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE, description = "Reconstructed Execution Traces",
			eventTypes = ExecutionTrace.class),
	@OutputPort(name = TraceReconstructionFilter.OUTPUT_PORT_NAME_INVALID_EXECUTION_TRACE, description = "Invalid Execution Traces",
			eventTypes = InvalidExecutionTrace.class)
}, repositoryPorts = {
	@RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class)
}, configuration = {
	@Property(name = TraceReconstructionFilter.CONFIG_PROPERTY_NAME_TIMEUNIT, defaultValue = TraceReconstructionFilter.CONFIG_PROPERTY_VALUE_TIMEUNIT),
	@Property(name = TraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION,
			defaultValue = TraceReconstructionFilter.CONFIG_PROPERTY_VALUE_MAX_TRACE_DURATION),
	@Property(name = TraceReconstructionFilter.CONFIG_PROPERTY_NAME_IGNORE_INVALID_TRACES, defaultValue = "true")
})
public class TraceReconstructionFilter extends AbstractTraceProcessingFilter {

	/** This is the name of the input port receiving new executions. */
	public static final String INPUT_PORT_NAME_EXECUTIONS = "executions";

	/** This is the name of the output port delivering the reconstructed message traces. */
	public static final String OUTPUT_PORT_NAME_MESSAGE_TRACE = "messageTraces";
	/** This is the name of the output port delivering the reconstructed execution traces. */
	public static final String OUTPUT_PORT_NAME_EXECUTION_TRACE = "executionTraces";
	/** This is the name of the output port delivering the reconstructed, but invalid executions traces. */
	public static final String OUTPUT_PORT_NAME_INVALID_EXECUTION_TRACE = "invalidExecutionTraces";
	/** This is the name of the property determining the used time unit. */
	public static final String CONFIG_PROPERTY_NAME_TIMEUNIT = "timeunit";
	/** This is the name of the property determining the maximal duration of a trace. */
	public static final String CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION = "maxTraceDuration";
	/** This is the name of the property determining whether to ignore invalid traces or not. */
	public static final String CONFIG_PROPERTY_NAME_IGNORE_INVALID_TRACES = "ignoreInvalidTraces";
	/** This is the default used time unit. */
	public static final String CONFIG_PROPERTY_VALUE_TIMEUNIT = "NANOSECONDS"; // TimeUnit.NANOSECONDS.name()
	/** This is the default value for the maximal duration of a trace. */
	public static final String CONFIG_PROPERTY_VALUE_MAX_TRACE_DURATION = "9223372036854775807"; // Long.toString(Long.MAX_VALUE)

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

			// Multiple traces may have an equal tin timestamp value. In order to provide an absolute ordering
			// of the keys, we take the traceId as a second ordering key.
			if (t1LowestTin != t2LowestTin) {
				return t1LowestTin < t2LowestTin ? -1 : 1; // NOCS
			}
			return t1.getTraceId() < t2.getTraceId() ? -1 : 1; // NOCS
		}
	});

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public TraceReconstructionFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.timeunit = super.recordsTimeUnitFromProjectContext;

		final String configTimeunitProperty = configuration.getStringProperty(CONFIG_PROPERTY_NAME_TIMEUNIT);
		TimeUnit configTimeunit;
		try {
			configTimeunit = TimeUnit.valueOf(configTimeunitProperty);
		} catch (final IllegalArgumentException ex) {
			this.logger.warn("{} is no valid TimeUnit! Using inherited value of {} instead.", configTimeunitProperty, this.timeunit.name());
			configTimeunit = this.timeunit;
		}

		// Load from the configuration.
		this.maxTraceDuration = this.timeunit.convert(configuration.getLongProperty(CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION), configTimeunit);
		this.ignoreInvalidTraces = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_IGNORE_INVALID_TRACES);

		if (this.maxTraceDuration < 0) {
			throw new IllegalArgumentException("value maxTraceDurationMillis must not be negative (found: " + this.maxTraceDuration + ")");
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean init() {
		return true; // no need to do anything here
	}

	/**
	 * This method represents the input port of this filter.
	 *
	 * @param execution
	 *            The next execution.
	 */
	@InputPort(name = INPUT_PORT_NAME_EXECUTIONS, description = "Receives the executions to be processed",
			eventTypes = Execution.class)
	public void inputExecutions(final Execution execution) {
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
	 * Transforms the execution trace is delivers the trace to the output ports
	 * of this filter (message trace and execution trace output ports, or invalid
	 * execution trace output port respectively).
	 *
	 * @param executionTrace
	 *            The execution trace to transform.
	 *
	 * @throws ExecutionEventProcessingException
	 *             if the passed execution trace is invalid and this filter is
	 *             configured to fail on the occurrence of invalid traces.
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
				super.deliver(OUTPUT_PORT_NAME_MESSAGE_TRACE, messageTrace);
				super.deliver(OUTPUT_PORT_NAME_EXECUTION_TRACE, executionTrace);
				this.reportSuccess(curTraceId);
			} else {
				// mt is the completing part of an invalid trace
				super.deliver(OUTPUT_PORT_NAME_INVALID_EXECUTION_TRACE, new InvalidExecutionTrace(executionTrace));
				// the statistics have been updated on the first
				// occurrence of artifacts of this trace
			}
		} catch (final InvalidTraceException ex) {
			// Transformation failed (i.e., trace invalid)
			super.deliver(OUTPUT_PORT_NAME_INVALID_EXECUTION_TRACE, new InvalidExecutionTrace(executionTrace));
			final String transformationError = "Failed to transform execution trace to message trace (ID: " + curTraceId
					+ "). \n" + "Reason: " + ex.getMessage() + "\n Trace: " + executionTrace;
			if (!this.invalidTraces.contains(curTraceId)) {
				// only once per traceID (otherwise, we would report all
				// trace fragments)
				this.reportError(curTraceId);
				this.invalidTraces.add(curTraceId);
				if (!this.ignoreInvalidTraces) {
					this.traceProcessingErrorOccured = true;
					this.logger.warn("Note that this filter was configured to terminate at the *first* occurence of an invalid trace \n"
							+ "If this is not the desired behavior, set the configuration property "
							+ "{} to 'true'", CONFIG_PROPERTY_NAME_IGNORE_INVALID_TRACES);
					throw new ExecutionEventProcessingException(transformationError, ex);
				} else {
					this.logger.error(transformationError); // do not pass 'ex' to log.error because this makes the output verbose (#584)
				}
			} else {
				this.logger.warn("Found additional fragment for trace already marked invalid: {}", transformationError);
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
			while (!this.timeoutMap.isEmpty() && (this.terminated || ((this.maxTout - this.timeoutMap.first().getMinTin()) > this.maxTraceDuration))) {
				final ExecutionTrace polledTrace = this.timeoutMap.pollFirst();
				final long curTraceId = polledTrace.getTraceId();
				this.pendingTraces.remove(curTraceId);
				this.processExecutionTrace(polledTrace);
			}
		}
	}

	/**
	 * Return the number of timeunits after which a pending trace is considered to have timed out.
	 *
	 * @return the timeout duration for a pending trace in timeunits
	 */
	public final long getMaxTraceDuration() {
		synchronized (this) {
			return this.maxTraceDuration;
		}
	}

	/**
	 * Terminates the filter (internally, all pending traces are processed).
	 *
	 * @param error
	 *            Determines whether the plugin is terminated due to an error or not.
	 */
	@Override
	public void terminate(final boolean error) {
		synchronized (this) {
			try {
				this.terminated = true;
				if (!error || (this.traceProcessingErrorOccured && !this.ignoreInvalidTraces)) {
					this.processTimeoutQueue();
				} else {
					this.logger.info("Terminate called with error an flag set or a trace processing"
							+ " occurred; won't process timeoutqueue any more.");
				}
			} catch (final ExecutionEventProcessingException ex) {
				this.traceProcessingErrorOccured = true;
				this.logger.error("Error processing timeout queue", ex);
			}
		}
	}

	@Override
	public void printStatusMessage() {
		synchronized (this) {
			super.printStatusMessage();
			if ((this.getSuccessCount() > 0) || (this.getErrorCount() > 0)) {
				final String minTinStr = new StringBuilder().append(this.minTin).append(" (")
						.append(LoggingTimestampConverter.convertLoggingTimestampToUTCString(this.timeunit.toNanos(this.minTin)))
						.append(',')
						.append(LoggingTimestampConverter.convertLoggingTimestampLocalTimeZoneString(this.minTin))
						.append(')').toString();
				final String maxToutStr = new StringBuilder().append(this.maxTout).append(" (")
						.append(LoggingTimestampConverter.convertLoggingTimestampToUTCString(this.timeunit.toNanos(this.maxTout)))
						.append(',')
						.append(LoggingTimestampConverter.convertLoggingTimestampLocalTimeZoneString(this.maxTout))
						.append(')').toString();
				LOGGER.debug("First timestamp: {}", minTinStr);
				LOGGER.debug("Last timestamp: {}", maxToutStr);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = super.getCurrentConfiguration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_TIMEUNIT, this.timeunit.name());
		configuration.setProperty(CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION, Long.toString(this.maxTraceDuration));
		configuration.setProperty(CONFIG_PROPERTY_NAME_IGNORE_INVALID_TRACES, Boolean.toString(this.ignoreInvalidTraces));

		return configuration;
	}
}
