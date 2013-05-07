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

package kieker.analysis.plugin.reader.timer;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.misc.TimestampRecord;

/**
 * This plugin provides the current (system) time in regular intervals. The time is delivered to the two output ports as both a timestamp and a
 * {@link TimestampRecord} instance.<br>
 * <br/>
 * 
 * The reader can be configured to be either blocking or non-blocking. In the first mode the read-method doesn't return and waits instead for a call to the terminate
 * method. In the second mode the read method does return immediately, resulting in the termination of the whole analysis, if no other reader exists.<br>
 * <br/>
 * 
 * The sent timestamps are created using {@link System#nanoTime()} as a time source, which is being converted to the global time unit (as defined in the
 * configuration from the given {@link IProjectContext}).
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.8
 */
@Plugin(
		description = "Delivers the current (system) time in regular intervals",
		outputPorts = {
			@OutputPort(name = TimeReader.OUTPUT_PORT_NAME_TIMESTAMPS, eventTypes = Long.class),
			@OutputPort(name = TimeReader.OUTPUT_PORT_NAME_TIMESTAMP_RECORDS, eventTypes = TimestampRecord.class)
		},
		configuration = {
			@Property(name = TimeReader.CONFIG_PROPERTY_NAME_UPDATE_INTERVAL_NS, defaultValue = TimeReader.CONFIG_PROPERTY_VALUE_UPDATE_INTERVAL_NS,
					description = "Determines the update interval in nano seconds."),
			@Property(name = TimeReader.CONFIG_PROPERTY_NAME_DELAY_NS, defaultValue = TimeReader.CONFIG_PROPERTY_VALUE_DELAY_NS,
					description = "Determines the initial delay in nano seconds."),
			@Property(name = TimeReader.CONFIG_PROPERTY_NAME_BLOCKING_READ, defaultValue = TimeReader.CONFIG_PROPERTY_VALUE_BLOCKING_READ,
					description = "Determines whether the read method of this reader returns immediately or not.")
		})
public final class TimeReader extends AbstractReaderPlugin {

	public static final String OUTPUT_PORT_NAME_TIMESTAMPS = "timestamps";
	public static final String OUTPUT_PORT_NAME_TIMESTAMP_RECORDS = "timestampRecords";

	public static final String CONFIG_PROPERTY_NAME_UPDATE_INTERVAL_NS = "updateIntervalNS";
	public static final String CONFIG_PROPERTY_VALUE_UPDATE_INTERVAL_NS = "1000000000";

	public static final String CONFIG_PROPERTY_NAME_DELAY_NS = "delayNS";
	public static final String CONFIG_PROPERTY_VALUE_DELAY_NS = "0";

	public static final String CONFIG_PROPERTY_NAME_BLOCKING_READ = "blockingRead";
	public static final String CONFIG_PROPERTY_VALUE_BLOCKING_READ = "true";

	private static final Log LOG = LogFactory.getLog(TimeReader.class);

	private volatile boolean terminated = false;

	private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
	private volatile ScheduledFuture<?> result;

	private final long initialDelay;
	private final long period;
	private final boolean blockingRead;
	private final TimeUnit timeunit;

	/**
	 * Creates a new timer using the given configuration.
	 * 
	 * @param configuration
	 *            The configuration containing the properties to initialize this timer.
	 * @param projectContext
	 *            The project context.
	 */
	public TimeReader(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.initialDelay = configuration.getLongProperty(CONFIG_PROPERTY_NAME_DELAY_NS);
		this.period = configuration.getLongProperty(CONFIG_PROPERTY_NAME_UPDATE_INTERVAL_NS);
		this.blockingRead = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_BLOCKING_READ);

		final String recordTimeunitProperty = projectContext.getProperty(IProjectContext.CONFIG_PROPERTY_NAME_RECORDS_TIME_UNIT);
		TimeUnit recordTimeunit;
		try {
			recordTimeunit = TimeUnit.valueOf(recordTimeunitProperty);
		} catch (final IllegalArgumentException ex) { // already caught in AnalysisController, should never happen
			LOG.warn(recordTimeunitProperty + " is no valid TimeUnit! Using NANOSECONDS instead.");
			recordTimeunit = TimeUnit.NANOSECONDS;
		}
		this.timeunit = recordTimeunit;
	}

	/**
	 * {@inheritDoc}
	 */
	public void terminate(final boolean error) {
		if (!this.terminated) {
			LOG.info("Shutdown of TimeReader requested.");

			this.executorService.shutdown();
			try {
				this.terminated = this.executorService.awaitTermination(5, TimeUnit.SECONDS);
			} catch (final InterruptedException ex) {
				// ignore
			}
			if (!this.terminated) {
				// problems shutting down
				this.result.cancel(true);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean read() {
		this.result = this.executorService.scheduleAtFixedRate(new TimestampEventTask(), this.initialDelay, this.period, TimeUnit.NANOSECONDS);
		if (this.blockingRead) {
			try {
				this.result.get();
			} catch (final ExecutionException ex) {
				this.terminate(true);
				throw new RuntimeException(ex.getCause()); // NOPMD (throw RunTimeException)
			} catch (final InterruptedException ignore) { // NOPMD (ignore exception)
				// ignore this one
			} catch (final CancellationException ignore) { // NOPMD (ignore exception)
				// ignore this one, too
			}
		}
		this.terminate(false);
		return true;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_DELAY_NS, Long.toString(this.initialDelay));
		configuration.setProperty(CONFIG_PROPERTY_NAME_UPDATE_INTERVAL_NS, Long.toString(this.period));
		configuration.setProperty(CONFIG_PROPERTY_NAME_BLOCKING_READ, Boolean.toString(this.blockingRead));

		return configuration;
	}

	/**
	 * Sends the current system time as a new timestamp event.
	 */
	protected void sendTimestampEvent() {
		final long timestamp = this.convertTimestamp(System.nanoTime());

		super.deliver(OUTPUT_PORT_NAME_TIMESTAMPS, timestamp);
		super.deliver(OUTPUT_PORT_NAME_TIMESTAMP_RECORDS, new TimestampRecord(timestamp));
	}

	private long convertTimestamp(final long timestampNS) {
		return this.timeunit.convert(timestampNS, TimeUnit.NANOSECONDS);
	}

	/**
	 * A simple helper class used to send the current system time.
	 * 
	 * @author Nils Christian Ehmke
	 * 
	 * @since 1.8
	 */
	protected class TimestampEventTask implements Runnable {

		public TimestampEventTask() {
			// empty default constructor
		}

		public void run() {
			TimeReader.this.sendTimestampEvent();
		}

	}
}
