/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.toolsteetime.logReplayer;

import java.util.concurrent.TimeUnit;

import kieker.analysisteetime.plugin.filter.record.delayfilter.RealtimeRecordDelayFilter;
import kieker.analysisteetime.plugin.filter.select.timestampfilter.TimestampFilter;
import kieker.analysisteetime.plugin.reader.AbstractReader;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.toolsteetime.logReplayer.filter.MonitoringRecordLoggerFilter;

import teetime.framework.Configuration;
import teetime.framework.Execution;

/**
 * Replays a monitoring log to a {@link kieker.monitoring.core.controller.IMonitoringController} with a given {@link Configuration}.
 * The {@link AbstractLogReplayer} can filter by timestamp and replay in real-time.
 *
 * @author Andre van Hoorn, Lars Bluemke
 *
 * @since 1.6
 */
public abstract class AbstractLogReplayer {

	public static final long MAX_TIMESTAMP = Long.MAX_VALUE;
	public static final long MIN_TIMESTAMP = 0;

	private static final Log LOG = LogFactory.getLog(AbstractLogReplayer.class);

	private final long ignoreRecordsBeforeTimestamp;
	private final long ignoreRecordsAfterTimestamp;

	private final IMonitoringController monitoringController;
	private final String monitoringConfigurationFile;

	private final boolean realtimeMode;
	private final TimeUnit realtimeTimeunit;
	private final double realtimeAccelerationFactor;
	private final long realtimeWarnNegativeSchedTime;

	/**
	 * @param monitoringController
	 *            The {@link IMonitoringController}.
	 * @param monitoringConfigurationFile
	 *            The name of the {@code monitoring.properties} file.
	 * @param realtimeMode
	 *            Determines whether to use real time mode or not.
	 * @param realtimeTimeunit
	 *            The time unit to be used in realtime mode.
	 * @param realtimeAccelerationFactor
	 *            Determines whether to accelerate (value > 1.0) or slow down (<1.0) the replay in realtime mode by the given factor.
	 *            Choose a value of 1.0 for "real" realtime mode (i.e., no acceleration/slow down)
	 * @param realtimeWarnNegativeSchedTime
	 *            A time bound to configure a warning when a record is forwarded too late in realtime mode.
	 * @param ignoreRecordsBeforeTimestamp
	 *            The lower limit for the time stamps of the records.
	 * @param ignoreRecordsAfterTimestamp
	 *            The upper limit for the time stamps of the records.
	 */
	public AbstractLogReplayer(final IMonitoringController monitoringController, final String monitoringConfigurationFile, final boolean realtimeMode,
			final TimeUnit realtimeTimeunit,
			final double realtimeAccelerationFactor, final long realtimeWarnNegativeSchedTime, final long ignoreRecordsBeforeTimestamp,
			final long ignoreRecordsAfterTimestamp) {
		this.realtimeMode = realtimeMode;
		this.realtimeTimeunit = realtimeTimeunit;
		this.realtimeAccelerationFactor = realtimeAccelerationFactor; // ignored if realtimeMode == false
		this.realtimeWarnNegativeSchedTime = realtimeWarnNegativeSchedTime; // ignored if realtimeMode == false
		this.ignoreRecordsBeforeTimestamp = ignoreRecordsBeforeTimestamp;
		this.ignoreRecordsAfterTimestamp = ignoreRecordsAfterTimestamp;
		this.monitoringController = monitoringController;
		this.monitoringConfigurationFile = monitoringConfigurationFile;
		if (this.monitoringConfigurationFile == null) {
			LOG.warn("No path to a 'monitoring.properties' passed; default configuration will be used.");
		}
	}

	/**
	 * Replays the monitoring log terminates after the last record was passed to the configured {@link kieker.monitoring.core.controller.IMonitoringController}.
	 */
	public void replay() {
		LogReplayerConfiguration configuration;

		final AbstractReader reader = this.createReader();

		final MonitoringRecordLoggerFilter recordLogger = new MonitoringRecordLoggerFilter(this.monitoringController);

		if (this.isAtLeastOneTimestampGiven() && !this.realtimeMode) {
			final TimestampFilter timestampFilter = new TimestampFilter(this.ignoreRecordsBeforeTimestamp, this.ignoreRecordsAfterTimestamp);
			configuration = new LogReplayerConfiguration(reader, timestampFilter, recordLogger);
		} else if (!this.isAtLeastOneTimestampGiven() && this.realtimeMode) {
			final RealtimeRecordDelayFilter realtimeRecordDelayFilter = new RealtimeRecordDelayFilter(this.realtimeTimeunit, this.realtimeAccelerationFactor,
					this.realtimeWarnNegativeSchedTime);
			configuration = new LogReplayerConfiguration(reader, realtimeRecordDelayFilter, recordLogger);
		} else if (this.isAtLeastOneTimestampGiven() && this.realtimeMode) {
			final TimestampFilter timestampFilter = new TimestampFilter(this.ignoreRecordsBeforeTimestamp, this.ignoreRecordsAfterTimestamp);
			final RealtimeRecordDelayFilter realtimeRecordDelayFilter = new RealtimeRecordDelayFilter(this.realtimeTimeunit, this.realtimeAccelerationFactor,
					this.realtimeWarnNegativeSchedTime);
			configuration = new LogReplayerConfiguration(reader, timestampFilter, realtimeRecordDelayFilter, recordLogger);
		} else {
			configuration = new LogReplayerConfiguration(reader, recordLogger);
		}

		final Execution<LogReplayerConfiguration> execution = new Execution<LogReplayerConfiguration>(configuration);
		execution.executeBlocking();
	}

	private boolean isAtLeastOneTimestampGiven() {
		if ((this.ignoreRecordsBeforeTimestamp > MIN_TIMESTAMP) || (this.ignoreRecordsAfterTimestamp < MAX_TIMESTAMP)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Implementing classes return the reader to be used for reading the monitoring log.
	 *
	 * @return The reader which can be used to read the monitoring log.
	 */
	protected abstract AbstractReader createReader();
}
