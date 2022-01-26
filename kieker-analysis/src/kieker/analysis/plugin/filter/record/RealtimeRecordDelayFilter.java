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

package kieker.analysis.plugin.filter.record;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

/**
 * Forwards incoming {@link IMonitoringRecord}s with delays computed from the
 * {@link kieker.common.record.IMonitoringRecord#getLoggingTimestamp()} value
 * (assumed to be in the configured resolution). For example, after
 * initialization, if records with logging timestamps 3000 and 4500 nanos are
 * received, the first record is forwarded immediately; the second will be
 * forwarded 1500 nanos later. The acceleration factor can be used to
 * accelerate/slow down the replay (default 1.0, which means no
 * acceleration/slow down).
 *
 * @author Andre van Hoorn, Robert von Massow, Jan Waller
 *
 * @since 1.6
 */
@Plugin(description = "Forwards incoming records with delays computed from the timestamp values", outputPorts = {
	@OutputPort(name = RealtimeRecordDelayFilter.OUTPUT_PORT_NAME_RECORDS,
			eventTypes = IMonitoringRecord.class, description = "Outputs the delayed records") },
		configuration = {
			@Property(name = RealtimeRecordDelayFilter.CONFIG_PROPERTY_NAME_NUM_WORKERS, defaultValue = "1"),
			@Property(name = RealtimeRecordDelayFilter.CONFIG_PROPERTY_NAME_ADDITIONAL_SHUTDOWN_DELAY_SECONDS, defaultValue = "5"),
			@Property(name = RealtimeRecordDelayFilter.CONFIG_PROPERTY_NAME_WARN_NEGATIVE_DELAY_SECONDS, defaultValue = "2"),
			@Property(name = RealtimeRecordDelayFilter.CONFIG_PROPERTY_NAME_TIMER, defaultValue = "MILLISECONDS"),
			@Property(name = RealtimeRecordDelayFilter.CONFIG_PROPERTY_NAME_ACCELERATION_FACTOR, defaultValue = "1") // CONFIG_PROPERTY_ACCELERATION_FACTOR_DEFAULT
		})
public class RealtimeRecordDelayFilter extends AbstractFilterPlugin {

	/** The name of the input port receiving the records. */
	public static final String INPUT_PORT_NAME_RECORDS = "inputRecords";
	/** The name of the output port delivering the delayed records. */
	public static final String OUTPUT_PORT_NAME_RECORDS = "outputRecords";

	/**
	 * The number of threads to be used for the internal
	 * {@link java.util.concurrent.ThreadPoolExecutor}, processing the scheduled
	 * {@link IMonitoringRecord}s.
	 */
	public static final String CONFIG_PROPERTY_NAME_NUM_WORKERS = "numWorkers";

	/**
	 * The number of additional seconds to wait before execute the termination
	 * (after all records have been forwarded).
	 */
	public static final String CONFIG_PROPERTY_NAME_ADDITIONAL_SHUTDOWN_DELAY_SECONDS = "additionalShutdownDelaySeconds";

	/**
	 * The number of seconds of negative scheduling time that produces a warning.
	 */
	public static final String CONFIG_PROPERTY_NAME_WARN_NEGATIVE_DELAY_SECONDS = "warnOnNegativeSchedTimeSeconds";

	/**
	 * The precision of the used timer (MILLISECONDS or NANOSECONDS).
	 */
	public static final String CONFIG_PROPERTY_NAME_TIMER = "timerPrecision";

	/**
	 * Factor to use for accelerating/slowing down the replay.
	 */
	public static final String CONFIG_PROPERTY_NAME_ACCELERATION_FACTOR = "accelerationFactor";

	public static final double CONFIG_PROPERTY_ACCELERATION_FACTOR_DEFAULT = 1;

	private final TimeUnit timeunit;

	private final String strTimerOrigin;
	private final TimerWithPrecision timer;

	private final double accelerationFactor;

	private final long warnOnNegativeSchedTimeOrigin;
	private final long warnOnNegativeSchedTime;

	private final int numWorkers;

	private final ScheduledThreadPoolExecutor executor;
	private final long shutdownDelay;

	private volatile long startTime = -1;
	private volatile long firstLoggingTimestamp;

	private volatile long latestSchedulingTime = -1;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public RealtimeRecordDelayFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.timeunit = super.recordsTimeUnitFromProjectContext;

		this.strTimerOrigin = configuration.getStringProperty(CONFIG_PROPERTY_NAME_TIMER);
		TimerWithPrecision tmpTimer;
		try {
			tmpTimer = TimerWithPrecision.valueOf(this.strTimerOrigin);
		} catch (final IllegalArgumentException ex) {
			this.logger.warn("{} is no valid timer precision! Using MILLISECONDS instead.", this.strTimerOrigin);
			tmpTimer = TimerWithPrecision.MILLISECONDS;
		}
		this.timer = tmpTimer;

		double accelerationFactorTmp = configuration.getDoubleProperty(CONFIG_PROPERTY_NAME_ACCELERATION_FACTOR);
		if (accelerationFactorTmp <= 0.0) {
			this.logger.warn("Acceleration factor must be > 0. Using default: {}",
					CONFIG_PROPERTY_ACCELERATION_FACTOR_DEFAULT);
			accelerationFactorTmp = 1;
		}
		this.accelerationFactor = accelerationFactorTmp;

		this.warnOnNegativeSchedTimeOrigin = this.configuration
				.getLongProperty(CONFIG_PROPERTY_NAME_WARN_NEGATIVE_DELAY_SECONDS);
		this.warnOnNegativeSchedTime = this.timeunit.convert(this.warnOnNegativeSchedTimeOrigin, TimeUnit.SECONDS);

		this.numWorkers = configuration.getIntProperty(CONFIG_PROPERTY_NAME_NUM_WORKERS);
		this.shutdownDelay = this.timeunit.convert(
				this.configuration.getLongProperty(CONFIG_PROPERTY_NAME_ADDITIONAL_SHUTDOWN_DELAY_SECONDS),
				TimeUnit.SECONDS);

		this.executor = new ScheduledThreadPoolExecutor(this.numWorkers);
		this.executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(true);
		this.executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
	}

	/**
	 * This method represents the input port of this filter.
	 *
	 * @param monitoringRecord
	 *            The next monitoring record.
	 */
	@InputPort(name = INPUT_PORT_NAME_RECORDS, eventTypes = IMonitoringRecord.class,
			description = "Receives the records to be delayed")
	public final void inputRecord(final IMonitoringRecord monitoringRecord) {
		final long currentTime = this.timer.getCurrentTime(this.timeunit);

		synchronized (this) {
			if (this.startTime == -1) { // init on first record
				this.firstLoggingTimestamp = monitoringRecord.getLoggingTimestamp();
				this.startTime = currentTime;
			}

			// Compute scheduling time (without acceleration)
			long schedTimeFromNow = (monitoringRecord.getLoggingTimestamp() - this.firstLoggingTimestamp) // relative to
																											// 1st
																											// record
					- (currentTime - this.startTime); // subtract elapsed time
			schedTimeFromNow /= this.accelerationFactor;
			if (schedTimeFromNow < -this.warnOnNegativeSchedTime) {
				final long schedTimeSeconds = TimeUnit.SECONDS.convert(schedTimeFromNow, this.timeunit);
				this.logger.warn("negative scheduling time: {} ({}) / {} (seconds)-> scheduling with a delay of 0",
						schedTimeFromNow, this.timeunit.toString(), schedTimeSeconds);
			}
			if (schedTimeFromNow < 0) {
				schedTimeFromNow = 0; // i.e., schedule immediately
			}

			final long absSchedTime = currentTime + schedTimeFromNow;
			if (absSchedTime > this.latestSchedulingTime) {
				this.latestSchedulingTime = absSchedTime;
			}

			// Schedule
			this.executor.schedule(new Runnable() {

				@Override
				public void run() {
					RealtimeRecordDelayFilter.this.deliverIndirect(OUTPUT_PORT_NAME_RECORDS, monitoringRecord);
				}
			}, schedTimeFromNow, this.timeunit);
		}
	}

	final boolean deliverIndirect(final String outputPortName, final Object data) { // NOPMD package for inner class
		return this.deliver(outputPortName, data);
	}

	@Override
	public void terminate(final boolean error) {
		this.executor.shutdown();

		if (!error) {
			long shutdownDelaySecondsFromNow = TimeUnit.SECONDS.convert(
					(this.latestSchedulingTime - this.timer.getCurrentTime(this.timeunit)) + this.shutdownDelay,
					this.timeunit);
			if (shutdownDelaySecondsFromNow < 0) {
				shutdownDelaySecondsFromNow = 0;
			}
			shutdownDelaySecondsFromNow += 2; // Add a buffer for the timeout. Having exactly the second for the last
												// event is unnecessarily tight.
			try {
				this.logger.info("Awaiting termination delay of {} seconds ...", shutdownDelaySecondsFromNow);
				if (!this.executor.awaitTermination(shutdownDelaySecondsFromNow, TimeUnit.SECONDS)) {
					this.logger.error("Termination delay triggerred before all scheduled records sent");
				}
			} catch (final InterruptedException e) {
				this.logger.error("Interrupted while awaiting termination delay", e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_WARN_NEGATIVE_DELAY_SECONDS,
				Long.toString(this.warnOnNegativeSchedTimeOrigin));
		configuration.setProperty(CONFIG_PROPERTY_NAME_NUM_WORKERS, Integer.toString(this.numWorkers));
		configuration.setProperty(CONFIG_PROPERTY_NAME_TIMER, this.strTimerOrigin);
		configuration.setProperty(CONFIG_PROPERTY_NAME_ACCELERATION_FACTOR, Double.toString(this.accelerationFactor));

		configuration.setProperty(CONFIG_PROPERTY_NAME_ADDITIONAL_SHUTDOWN_DELAY_SECONDS,
				Long.toString(TimeUnit.SECONDS.convert(this.shutdownDelay, this.timeunit)));

		return configuration;
	}

	/**
	 * @author Jan Waller
	 */
	private static enum TimerWithPrecision {
		MILLISECONDS {
			@Override
			public long getCurrentTime(final TimeUnit timeunit) {
				return timeunit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
			}

		},
		NANOSECONDS {
			@Override
			public long getCurrentTime(final TimeUnit timeunit) {
				return timeunit.convert(System.nanoTime(), TimeUnit.NANOSECONDS);
			}
		};

		public abstract long getCurrentTime(TimeUnit timeunit);
	}
}
