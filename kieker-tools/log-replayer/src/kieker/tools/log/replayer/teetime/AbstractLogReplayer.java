/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.log.replayer.teetime;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.stage.events.delayfilter.RealtimeRecordDelayFilter;
import kieker.analysis.stage.select.timestampfilter.TimestampFilter;
import kieker.common.record.IMonitoringRecord;
import kieker.tools.log.replayer.teetime.filter.MonitoringRecordLoggerFilter;

import teetime.framework.AbstractProducerStage;
import teetime.framework.Configuration;
import teetime.framework.Execution;

/**
 * Replays a monitoring log to a {@link kieker.monitoring.core.controller.IMonitoringController} with a given teetime configuration.
 * The {@link AbstractLogReplayer} can filter by timestamp and replay in real-time.
 *
 * @author Andre van Hoorn, Lars Bluemke
 *
 * @since 1.6
 */
public abstract class AbstractLogReplayer {

	public static final long MAX_TIMESTAMP = Long.MAX_VALUE;
	public static final long MIN_TIMESTAMP = 0;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLogReplayer.class);

	private final long ignoreRecordsBeforeTimestamp;
	private final long ignoreRecordsAfterTimestamp;

	private final String monitoringConfigurationFile;

	private final boolean realtimeMode;
	private final TimeUnit realtimeTimeunit;
	private final double realtimeAccelerationFactor;
	private final boolean keepOriginalLoggingTimestamps;

	/**
	 * @param monitoringConfigurationFile
	 *            The name of the {@code monitoring.properties} file.
	 * @param realtimeMode
	 *            Determines whether to use real time mode or not.
	 * @param realtimeTimeunit
	 *            The time unit to be used in realtime mode.
	 * @param realtimeAccelerationFactor
	 *            Determines whether to accelerate (value > 1.0) or slow down (<1.0) the replay in realtime mode by the given factor.
	 *            Choose a value of 1.0 for "real" realtime mode (i.e., no acceleration/slow down)
	 * @param keepOriginalLoggingTimestamps
	 *            Determines whether the original logging timestamps will be used of whether the timestamps will be modified.
	 * @param ignoreRecordsBeforeTimestamp
	 *            The lower limit for the time stamps of the records.
	 * @param ignoreRecordsAfterTimestamp
	 *            The upper limit for the time stamps of the records.
	 */
	public AbstractLogReplayer(
			final String monitoringConfigurationFile,
			final boolean realtimeMode,
			final TimeUnit realtimeTimeunit,
			final double realtimeAccelerationFactor,
			final boolean keepOriginalLoggingTimestamps,
			final long ignoreRecordsBeforeTimestamp,
			final long ignoreRecordsAfterTimestamp) {
		this.realtimeMode = realtimeMode;
		this.realtimeTimeunit = realtimeTimeunit;
		this.realtimeAccelerationFactor = realtimeAccelerationFactor; // ignored if realtimeMode == false
		this.keepOriginalLoggingTimestamps = keepOriginalLoggingTimestamps;
		this.ignoreRecordsBeforeTimestamp = ignoreRecordsBeforeTimestamp;
		this.ignoreRecordsAfterTimestamp = ignoreRecordsAfterTimestamp;
		this.monitoringConfigurationFile = monitoringConfigurationFile;
		if (monitoringConfigurationFile == null) {
			LOGGER.warn("No path to a 'monitoring.properties' passed; default configuration will be used.");
		}
	}

	/**
	 * Replays the monitoring log terminates after the last record was passed to the configured {@link kieker.monitoring.core.controller.IMonitoringController}.
	 */
	public void replay() {
		final LogReplayerConfiguration configuration;

		final AbstractProducerStage<IMonitoringRecord> reader = this.createReader();

		final MonitoringRecordLoggerFilter recordLogger = new MonitoringRecordLoggerFilter(this.monitoringConfigurationFile, this.keepOriginalLoggingTimestamps);

		if (this.isAtLeastOneTimestampGiven() && !this.realtimeMode) {
			final TimestampFilter timestampFilter = new TimestampFilter(this.ignoreRecordsBeforeTimestamp, this.ignoreRecordsAfterTimestamp);
			configuration = new LogReplayerConfiguration(reader, timestampFilter, recordLogger);
		} else if (!this.isAtLeastOneTimestampGiven() && this.realtimeMode) {
			final RealtimeRecordDelayFilter realtimeRecordDelayFilter = new RealtimeRecordDelayFilter(this.realtimeTimeunit, this.realtimeAccelerationFactor);
			configuration = new LogReplayerConfiguration(reader, realtimeRecordDelayFilter, recordLogger);
		} else if (this.isAtLeastOneTimestampGiven() && this.realtimeMode) {
			final TimestampFilter timestampFilter = new TimestampFilter(this.ignoreRecordsBeforeTimestamp, this.ignoreRecordsAfterTimestamp);
			final RealtimeRecordDelayFilter realtimeRecordDelayFilter = new RealtimeRecordDelayFilter(this.realtimeTimeunit, this.realtimeAccelerationFactor);
			configuration = new LogReplayerConfiguration(reader, timestampFilter, realtimeRecordDelayFilter, recordLogger);
		} else {
			configuration = new LogReplayerConfiguration(reader, recordLogger);
		}

		final Execution<LogReplayerConfiguration> execution = new Execution<>(configuration);
		execution.executeBlocking();
	}

	private boolean isAtLeastOneTimestampGiven() {
		return ((this.ignoreRecordsBeforeTimestamp > MIN_TIMESTAMP) || (this.ignoreRecordsAfterTimestamp < MAX_TIMESTAMP));
	}

	/**
	 * Implementing classes return the reader to be used for reading the monitoring log.
	 *
	 * @return The reader which can be used to read the monitoring log.
	 */
	protected abstract AbstractProducerStage<IMonitoringRecord> createReader();

	/**
	 * Provides different predefined configurations for the {@link AbstractLogReplayer}.
	 *
	 * @author Lars Bluemke
	 *
	 * @since 1.13
	 */
	private static class LogReplayerConfiguration extends Configuration {

		public LogReplayerConfiguration(final AbstractProducerStage<IMonitoringRecord> reader, final MonitoringRecordLoggerFilter recordLogger) {
			this.connectPorts(reader.getOutputPort(), recordLogger.getInputPort());
		}

		public LogReplayerConfiguration(final AbstractProducerStage<IMonitoringRecord> reader, final TimestampFilter timestampFilter,
				final MonitoringRecordLoggerFilter recordLogger) {
			this.connectPorts(reader.getOutputPort(), timestampFilter.getMonitoringRecordsCombinedInputPort());
			this.connectPorts(timestampFilter.getRecordsWithinTimePeriodOutputPort(), recordLogger.getInputPort());
		}

		public LogReplayerConfiguration(final AbstractProducerStage<IMonitoringRecord> reader, final RealtimeRecordDelayFilter realtimeRecordDelayFilter,
				final MonitoringRecordLoggerFilter recordLogger) {
			this.connectPorts(reader.getOutputPort(), realtimeRecordDelayFilter.getInputPort());
			this.connectPorts(realtimeRecordDelayFilter.getOutputPort(), recordLogger.getInputPort());
		}

		public LogReplayerConfiguration(final AbstractProducerStage<IMonitoringRecord> reader, final TimestampFilter timestampFilter,
				final RealtimeRecordDelayFilter realtimeRecordDelayFilter,
				final MonitoringRecordLoggerFilter recordLogger) {
			this.connectPorts(reader.getOutputPort(), timestampFilter.getMonitoringRecordsCombinedInputPort());
			this.connectPorts(timestampFilter.getRecordsWithinTimePeriodOutputPort(), realtimeRecordDelayFilter.getInputPort());
			this.connectPorts(realtimeRecordDelayFilter.getOutputPort(), recordLogger.getInputPort());
		}
	}
}
