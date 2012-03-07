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

import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.tools.traceAnalysis.plugins.AbstractTraceAnalysisPlugin;
import kieker.tools.traceAnalysis.plugins.AbstractTraceProcessingPlugin;
import kieker.tools.traceAnalysis.plugins.executionRecordTransformation.ExecutionEventProcessingException;
import kieker.tools.traceAnalysis.plugins.traceReconstruction.InvalidTraceException;
import kieker.tools.traceAnalysis.plugins.traceReconstruction.TraceReconstructionFilter;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.tools.util.LoggingTimestampConverter;

/**
 * TODO: The implementation of this plugin was based on the {@link TraceReconstructionFilter}. We should check for possible abstractions later on.
 * TODO: Note that we are currently not evaluating if traces are valid
 * We should import the invalid traces handling from {@link TraceReconstructionFilter}
 * 
 * @author Andre van Hoorn
 * 
 */
@Plugin(
		outputPorts = @OutputPort(name = EventRecordTraceGenerationFilter.OUTPUT_PORT_NAME, description = "Outputs the generated traces", eventTypes = { EventRecordTrace.class }),
		repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, repositoryType = SystemModelRepository.class))
public class EventRecordTraceGenerationFilter extends AbstractTraceProcessingPlugin {
	private static final Log LOG = LogFactory.getLog(EventRecordTraceGenerationFilter.class);

	public static final String INPUT_PORT_NAME = "inputTraceEvent";
	public static final String OUTPUT_PORT_NAME = "outputEventRecordTrace";

	public static final String CONFIG_MAX_TRACE_DURATION_MILLIS = "maxTraceDurationMillis";

	private static final long MAX_DURATION_NANOS = Long.MAX_VALUE;

	private final long maxTraceDurationNanos;
	private final long maxTraceDurationMillis;

	private volatile boolean terminated = false;

	/** TraceId x trace */
	private final Map<Long, EventRecordTrace> pendingTraces = new Hashtable<Long, EventRecordTrace>(); // NOPMD (UseConcurrentHashMap)

	/**
	 * Minimum {@link AbstractTraceEvent#getTimestamp()} of the received {@link AbstractTraceEvent}s
	 */
	private volatile long minTstamp = -1;

	/**
	 * Maximum {@link AbstractTraceEvent#getTimestamp()} of the received {@link AbstractTraceEvent}s
	 */
	private volatile long maxTstamp = -1;

	/** Pending traces sorted by timestamps */
	private final NavigableSet<EventRecordTrace> timeoutMap = new TreeSet<EventRecordTrace>(new Comparator<EventRecordTrace>() {

		/** Order traces by tins */
		@Override
		public int compare(final EventRecordTrace t1, final EventRecordTrace t2) {
			if (t1 == t2) { // NOPMD
				return 0;
			}
			final long t1minTstamp = t1.getMinTimestamp();
			final long t2minTstamp = t2.getMinTimestamp();

			if (t1minTstamp != t2minTstamp) {
				return t1minTstamp < t2minTstamp ? -1 : 1; // NOCS
			}
			return t1.getTraceId() < t2.getTraceId() ? -1 : 1; // NOCS
		}
	});

	/**
	 * 
	 * @param configuration
	 * @param repositories
	 */
	public EventRecordTraceGenerationFilter(final Configuration configuration) {
		super(configuration);

		/* Load from the configuration. */
		this.maxTraceDurationMillis = configuration.getLongProperty(EventRecordTraceGenerationFilter.CONFIG_MAX_TRACE_DURATION_MILLIS);

		if (this.maxTraceDurationMillis < 0) {
			throw new IllegalArgumentException("value maxTraceDurationMillis must not be negative (found: " + this.maxTraceDurationMillis + ")");
		}
		if (this.maxTraceDurationMillis == AbstractTraceProcessingPlugin.MAX_DURATION_MILLIS) {
			this.maxTraceDurationNanos = EventRecordTraceGenerationFilter.MAX_DURATION_NANOS;
		} else {
			this.maxTraceDurationNanos = this.maxTraceDurationMillis * (1000 * 1000); // NOCS (MagicNumberCheck)
		}
	}

	@InputPort(name = EventRecordTraceGenerationFilter.INPUT_PORT_NAME, description = "Receives new trace events", eventTypes = { AbstractTraceEvent.class })
	public void inputTraceEvent(final AbstractTraceEvent event) {
		final long traceId = event.getTraceId();

		/* Update minimum and maximum timestamps */
		if ((this.minTstamp == -1) /* unset */|| (event.getTimestamp() < this.minTstamp)) {
			this.minTstamp = event.getTimestamp();
		}
		if (event.getTimestamp() > this.maxTstamp) {
			this.maxTstamp = event.getTimestamp();
		}

		EventRecordTrace eventRecordTrace = this.pendingTraces.get(traceId);
		if (eventRecordTrace != null) { /* trace (artifacts) exists already; */
			if (!this.timeoutMap.remove(eventRecordTrace)) { /* remove from timeoutMap. Will be re-added below */
				EventRecordTraceGenerationFilter.LOG.error("Missing entry for trace in timeoutMap: " + eventRecordTrace
						+ ". PendingTraces and timeoutMap are now longer consistent!");
				this.reportError(traceId);
			}
		} else { /* create and add new trace */
			eventRecordTrace = new EventRecordTrace(traceId);
			this.pendingTraces.put(traceId, eventRecordTrace);
		}

		try {
			eventRecordTrace.add(event);
			if (!this.timeoutMap.add(eventRecordTrace)) { // (re-)add trace to timeoutMap
				EventRecordTraceGenerationFilter.LOG.error("Equal entry existed in timeoutMap already:" + eventRecordTrace);
			}
			this.processTimeoutQueue();
		} catch (final InvalidTraceException ex) { // this would be a bug!
			EventRecordTraceGenerationFilter.LOG.error("Attempt to add record to wrong trace", ex);
		} catch (final ExecutionEventProcessingException ex) {
			EventRecordTraceGenerationFilter.LOG.error("ExecutionEventProcessingException occured while processing " + "the timeout queue. ", ex);
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
			while (!this.timeoutMap.isEmpty() && (this.terminated || ((this.maxTstamp - this.timeoutMap.first().getMinTimestamp()) > this.maxTraceDurationNanos))) {
				final EventRecordTrace polledTrace = this.timeoutMap.pollFirst();
				final long curTraceId = polledTrace.getTraceId();
				this.pendingTraces.remove(curTraceId);
				super.deliver(EventRecordTraceGenerationFilter.OUTPUT_PORT_NAME, polledTrace);
				this.reportSuccess(curTraceId);
			}
		}
	}

	/**
	 * Returns the minimum timestamp of the received {@link AbstractTraceEvent}s.
	 * 
	 * @return the timestamp
	 */
	public final long getMinTimestamp() {
		return this.minTstamp;
	}

	/**
	 * Returns the maximum timestamp of the received {@link AbstractTraceEvent}s.
	 * 
	 * @return the timestamp
	 */
	public final long getMaxTimestamp() {
		return this.maxTstamp;
	}

	/**
	 * Terminates the filter (internally, all pending traces are processed).
	 * 
	 * @param error
	 */
	@Override
	public void terminate(final boolean error) {
		try {
			this.terminated = true;
			if (!error) {
				this.processTimeoutQueue();
			} else {
				EventRecordTraceGenerationFilter.LOG.info("terminate called with error flag set; won't process timeoutqueue any more.");
			}
		} catch (final ExecutionEventProcessingException ex) {
			EventRecordTraceGenerationFilter.LOG.error("Error processing queue", ex);
		}
	}

	@Override
	public void printStatusMessage() {
		super.printStatusMessage();
		if ((this.getSuccessCount() > 0) || (this.getErrorCount() > 0)) {
			final String minTinStr = new StringBuilder().append(this.minTstamp).append(" (")
					.append(LoggingTimestampConverter.convertLoggingTimestampToUTCString(this.minTstamp)).append(",")
					.append(LoggingTimestampConverter.convertLoggingTimestampLocalTimeZoneString(this.minTstamp)).append(")").toString();
			final String maxToutStr = new StringBuilder().append(this.maxTstamp).append(" (")
					.append(LoggingTimestampConverter.convertLoggingTimestampToUTCString(this.maxTstamp)).append(",")
					.append(LoggingTimestampConverter.convertLoggingTimestampLocalTimeZoneString(this.maxTstamp)).append(")").toString();
			System.out.println("First timestamp: " + minTinStr);
			System.out.println("Last timestamp: " + maxToutStr);
		}
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(EventRecordTraceGenerationFilter.CONFIG_MAX_TRACE_DURATION_MILLIS,
				Long.toString(AbstractTraceProcessingPlugin.MAX_DURATION_MILLIS));

		return configuration;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(EventRecordTraceGenerationFilter.CONFIG_MAX_TRACE_DURATION_MILLIS, Long.toString(this.maxTraceDurationMillis));
		return configuration;
	}

}
