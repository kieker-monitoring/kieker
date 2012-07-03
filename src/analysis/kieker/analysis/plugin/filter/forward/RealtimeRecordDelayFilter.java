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

package kieker.analysis.plugin.filter.forward;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

/**
 * Forwards incoming {@link IMonitoringRecord}s with delays computed from the {@link IMonitoringRecord#getLoggingTimestamp()} value (assumed to be in nanoseconds
 * resolution). For example, after initialization, if records with logging timestamps 3000 and 4500 nanos are received, the first record is forwarded immediately;
 * the second will be forwarded 1500 nanos later.
 * 
 * TODO: Configurable number of worker threads?
 * TODO: Timer with higher precision (Currently milliseconds)?
 * 
 * @author Andre van Hoorn
 * 
 */
@Plugin(
		outputPorts = {
			@OutputPort(name = RealtimeRecordDelayFilter.OUTPUT_PORT_NAME_RECORDS, eventTypes = { IMonitoringRecord.class }, description = "Outputs the delayed records")
		})
public class RealtimeRecordDelayFilter extends AbstractFilterPlugin {
	private static final Log LOG = LogFactory.getLog(RealtimeRecordDelayFilter.class);

	public static final String INPUT_PORT_NAME_RECORDS = "inputRecords";
	public static final String OUTPUT_PORT_NAME_RECORDS = "outputRecords";

	/**
	 * The number of additional seconds to wait before execute the termination (after all records have been forwarded)
	 */
	public static final String CONFIG_PROPERTY_NAME_ADDITIONAL_SHUTDOWN_DELAY_SECONDS = "additionalShutdownDelaySeconds";

	private static final long WARN_ON_NEGATIVE_SCHED_TIME_NANOS = TimeUnit.NANOSECONDS.convert(2, TimeUnit.SECONDS);

	private final ScheduledThreadPoolExecutor executor;
	private final long shutdownDelayNanos;

	private volatile long startTime = -1;
	private volatile long firstLoggingTimestamp;

	private volatile long latestSchedulingTimeNanos = -1;

	public RealtimeRecordDelayFilter(final Configuration configuration) {
		super(configuration);
		this.shutdownDelayNanos =
				TimeUnit.NANOSECONDS.convert(this.configuration.getLongProperty(CONFIG_PROPERTY_NAME_ADDITIONAL_SHUTDOWN_DELAY_SECONDS), TimeUnit.SECONDS);

		this.executor = new ScheduledThreadPoolExecutor(1); // one worker
		this.executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(true);
		this.executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
	}

	@InputPort(name = INPUT_PORT_NAME_RECORDS, eventTypes = { IMonitoringRecord.class }, description = "Receives the records to be delayed")
	public final void inputRecord(final IMonitoringRecord monitoringRecord) {
		final long currentTimeNanos = this.currentTimeNanos();

		synchronized (this) {
			if (this.startTime == -1) { // init on first record
				this.firstLoggingTimestamp = monitoringRecord.getLoggingTimestamp();
				this.startTime = currentTimeNanos;
			}

			/*
			 * Compute scheduling time
			 */
			long schedTimeNanosFromNow = (monitoringRecord.getLoggingTimestamp() - this.firstLoggingTimestamp) // relative to 1st record
					- (currentTimeNanos - this.startTime); // substract elapsed time
			if (schedTimeNanosFromNow < -WARN_ON_NEGATIVE_SCHED_TIME_NANOS) {
				final long schedTimeSeconds = TimeUnit.SECONDS.convert(schedTimeNanosFromNow, TimeUnit.NANOSECONDS);
				LOG.warn("negative scheduling time: " + schedTimeNanosFromNow + " (nanos) / " + schedTimeSeconds + " (seconds)-> scheduling with a delay of 0");
			}
			if (schedTimeNanosFromNow < 0) {
				schedTimeNanosFromNow = 0; // i.e., schedule immediately
			}

			final long absSchedTime = currentTimeNanos + schedTimeNanosFromNow;
			if (absSchedTime > this.latestSchedulingTimeNanos) {
				this.latestSchedulingTimeNanos = absSchedTime;
			}

			/*
			 * Schedule
			 */
			this.executor.schedule(new Runnable() {

				public void run() {
					RealtimeRecordDelayFilter.this.deliver(OUTPUT_PORT_NAME_RECORDS, monitoringRecord);
				}
			}, schedTimeNanosFromNow, TimeUnit.NANOSECONDS);
		}
	}

	/**
	 * Returns the current time in nanoseconds since 1970.
	 * 
	 * TODO: Note that we only have a timer resolution of milliseconds here!
	 * 
	 * @return
	 */
	private long currentTimeNanos() {
		return TimeUnit.NANOSECONDS.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}

	@Override
	public void terminate(final boolean error) {
		// TODO: Termination delay? Wait for pending jobs to be processed?

		this.executor.shutdown();

		if (!error) {
			final long shutdownDelaySecondsFromNow =
					TimeUnit.SECONDS.convert((this.latestSchedulingTimeNanos - this.currentTimeNanos()) + this.shutdownDelayNanos, TimeUnit.NANOSECONDS);
			try {
				LOG.info("Awaiting termination delay of " + shutdownDelaySecondsFromNow + " seconds ...");
				if (!this.executor.awaitTermination(shutdownDelaySecondsFromNow, TimeUnit.SECONDS)) {
					LOG.error("Termination delay triggerred before all scheduled records sent");
				}
			} catch (final InterruptedException e) {
				LOG.error("Interrupted while awaiting termination delay", e);
			}
		}
	}

	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_ADDITIONAL_SHUTDOWN_DELAY_SECONDS, Long.toString(this.shutdownDelayNanos));
		return configuration;
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_ADDITIONAL_SHUTDOWN_DELAY_SECONDS, Long.toString(5)); // 5 seconds default
		return configuration;
	}
}
