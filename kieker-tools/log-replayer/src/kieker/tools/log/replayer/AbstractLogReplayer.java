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

package kieker.tools.log.replayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.filter.record.RealtimeRecordDelayFilter;
import kieker.analysis.plugin.filter.select.TimestampFilter;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationConstants;
import kieker.tools.log.replayer.filter.MonitoringRecordLoggerFilter;

/**
 * Replays a monitoring log to a {@link kieker.monitoring.core.controller.IMonitoringController} with a given {@link Configuration}.
 * The {@link AbstractLogReplayer} can filter by timestamp and replay in real-time.
 *
 * @author Andre van Hoorn
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
	private final double realtimeAccelerationFactor;
	private final boolean keepOriginalLoggingTimestamps;
	private final int numRealtimeWorkerThreads;

	/**
	 * @param monitoringConfigurationFile
	 *            The name of the {@code monitoring.properties} file.
	 * @param realtimeMode
	 *            Determines whether to use real time mode or not.
	 * @param realtimeAccelerationFactor
	 *            Determines whether to accelerate (value > 1.0) or slow down (<1.0) the replay in realtime mode by the given factor.
	 *            Choose a value of 1.0 for "real" realtime mode (i.e., no acceleration/slow down)
	 * @param keepOriginalLoggingTimestamps
	 *            Determines whether the original logging timestamps will be used of whether the timestamps will be modified.
	 * @param numRealtimeWorkerThreads
	 *            Determines how many realtime worker threads should be used.
	 * @param ignoreRecordsBeforeTimestamp
	 *            The lower limit for the time stamps of the records.
	 * @param ignoreRecordsAfterTimestamp
	 *            The upper limit for the time stamps of the records.
	 */
	public AbstractLogReplayer(final String monitoringConfigurationFile, final boolean realtimeMode, final double realtimeAccelerationFactor,
			final boolean keepOriginalLoggingTimestamps, final int numRealtimeWorkerThreads, final long ignoreRecordsBeforeTimestamp,
			final long ignoreRecordsAfterTimestamp) {
		this.realtimeMode = realtimeMode;
		this.realtimeAccelerationFactor = realtimeAccelerationFactor; // ignored if realtimeMode == false
		this.keepOriginalLoggingTimestamps = keepOriginalLoggingTimestamps;
		this.numRealtimeWorkerThreads = numRealtimeWorkerThreads;
		if (this.numRealtimeWorkerThreads <= 0) {
			LOGGER.warn("numRealtimeWorkerThreads == {}", numRealtimeWorkerThreads);
		}
		this.ignoreRecordsBeforeTimestamp = ignoreRecordsBeforeTimestamp;
		this.ignoreRecordsAfterTimestamp = ignoreRecordsAfterTimestamp;
		this.monitoringConfigurationFile = monitoringConfigurationFile;
		if (this.monitoringConfigurationFile == null) {
			LOGGER.warn("No path to a 'monitoring.properties' passed; default configuration will be used.");
		}
	}

	/**
	 * Replays the monitoring log terminates after the last record was passed to the configured {@link kieker.monitoring.core.controller.IMonitoringController}.
	 *
	 * @return true on success; false otherwise
	 */
	public boolean replay() {
		boolean success = true;
		try {
			final IAnalysisController analysisInstance = new AnalysisController();

			// Initializing the reader
			final AbstractReaderPlugin reader = this.createReader(analysisInstance);

			// These two variables will be updated while plugging together the configuration
			AbstractPlugin lastFilter = reader;
			String lastOutputPortName = this.readerOutputPortName();

			// (Potentially) initializing the timestamp filter
			{ // NOCS (nested Block)
				final Configuration timestampFilterConfiguration = new Configuration();

				boolean atLeastOneTimestampGiven = false;
				if (this.ignoreRecordsBeforeTimestamp > MIN_TIMESTAMP) {
					atLeastOneTimestampGiven = true;
					timestampFilterConfiguration.setProperty(TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_BEFORE_TIMESTAMP,
							Long.toString(this.ignoreRecordsBeforeTimestamp));
				}
				if (this.ignoreRecordsAfterTimestamp < MAX_TIMESTAMP) {
					atLeastOneTimestampGiven = true;
					timestampFilterConfiguration.setProperty(TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_AFTER_TIMESTAMP,
							Long.toString(this.ignoreRecordsAfterTimestamp));
				}

				if (atLeastOneTimestampGiven) {
					final TimestampFilter timestampFilter = new TimestampFilter(timestampFilterConfiguration, analysisInstance);

					analysisInstance.connect(lastFilter, lastOutputPortName, timestampFilter, TimestampFilter.INPUT_PORT_NAME_ANY_RECORD);
					lastFilter = timestampFilter;
					lastOutputPortName = TimestampFilter.OUTPUT_PORT_NAME_WITHIN_PERIOD;
				} else { // NOCS NOPMD (EmptyIfStmt)
					// nothing to do; lastFilter and lastOutputPortName keep their values
				}
			}

			// (Potentially) initializing delay filter
			if (this.realtimeMode) {
				final Configuration delayFilterConfiguration = new Configuration();
				delayFilterConfiguration.setProperty(RealtimeRecordDelayFilter.CONFIG_PROPERTY_NAME_NUM_WORKERS, Integer.toString(this.numRealtimeWorkerThreads));
				delayFilterConfiguration.setProperty(RealtimeRecordDelayFilter.CONFIG_PROPERTY_NAME_ACCELERATION_FACTOR,
						Double.toString(this.realtimeAccelerationFactor));
				final RealtimeRecordDelayFilter rtFilter = new RealtimeRecordDelayFilter(delayFilterConfiguration, analysisInstance);

				analysisInstance.connect(lastFilter, lastOutputPortName, rtFilter, RealtimeRecordDelayFilter.INPUT_PORT_NAME_RECORDS);
				lastFilter = rtFilter;
				lastOutputPortName = RealtimeRecordDelayFilter.OUTPUT_PORT_NAME_RECORDS;
			}

			// And finally, we'll add the MonitoringRecordLoggerFilter
			final Configuration recordLoggerConfig = new Configuration();
			if (this.monitoringConfigurationFile != null) {
				recordLoggerConfig.setProperty(MonitoringRecordLoggerFilter.CONFIG_PROPERTY_NAME_MONITORING_PROPS_FN, this.monitoringConfigurationFile);
			}
			recordLoggerConfig.setProperty(
					ConfigurationConstants.AUTO_SET_LOGGINGTSTAMP,
					Boolean.toString(!this.keepOriginalLoggingTimestamps));
			final MonitoringRecordLoggerFilter recordLogger = new MonitoringRecordLoggerFilter(recordLoggerConfig, analysisInstance);

			analysisInstance.connect(lastFilter, lastOutputPortName, recordLogger, MonitoringRecordLoggerFilter.INPUT_PORT_NAME_RECORD);

			analysisInstance.run();
		} catch (final IllegalStateException e) {
			LOGGER.error("An error occurred while replaying", e);
			success = false;
		} catch (final AnalysisConfigurationException e) {
			LOGGER.error("An error occurred while replaying", e);
			success = false;
		}
		return success;
	}

	/**
	 * Implementing classes returns the name of the reader's output port which provides the {@link kieker.common.record.IMonitoringRecord}s from the monitoring log.
	 *
	 * @return The name of the reader's output port.
	 */
	protected abstract String readerOutputPortName();

	/**
	 * Implementing classes return the reader to be used for reading the monitoring log.
	 *
	 * @param analysisInstance
	 *            The analysis controller which will be the parent of the reader.
	 *
	 * @return The reader which can be used to read the monitoring log.
	 */
	protected abstract AbstractReaderPlugin createReader(final IAnalysisController analysisInstance);
}
