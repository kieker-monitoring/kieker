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

import kieker.analysisteetime.plugin.filter.record.delayfilter.RealtimeRecordDelayFilter;
import kieker.analysisteetime.plugin.filter.select.timestampfilter.TimestampFilter;
import kieker.analysisteetime.plugin.reader.AbstractReader;
import kieker.toolsteetime.logReplayer.filter.MonitoringRecordLoggerFilter;

import teetime.framework.Configuration;

/**
 * Provides different predefined configurations for the {@link AbstractLogReplayer}.
 *
 * @author Lars Bluemke
 *
 * @since 1.13
 */
public class LogReplayerConfiguration extends Configuration {

	public LogReplayerConfiguration(final AbstractReader reader, final MonitoringRecordLoggerFilter recordLogger) {
		this.connectPorts(reader.getOutputPort(), recordLogger.getInputPort());
	}

	public LogReplayerConfiguration(final AbstractReader reader, final TimestampFilter timestampFilter, final MonitoringRecordLoggerFilter recordLogger) {
		this.connectPorts(reader.getOutputPort(), timestampFilter.getMonitoringRecordsCombinedInputPort());
		this.connectPorts(timestampFilter.getRecordsWithinTimePeriodOutputPort(), recordLogger.getInputPort());
	}

	public LogReplayerConfiguration(final AbstractReader reader, final RealtimeRecordDelayFilter realtimeRecordDelayFilter,
			final MonitoringRecordLoggerFilter recordLogger) {
		this.connectPorts(reader.getOutputPort(), realtimeRecordDelayFilter.getInputPort());
		this.connectPorts(realtimeRecordDelayFilter.getOutputPort(), recordLogger.getInputPort());
	}

	public LogReplayerConfiguration(final AbstractReader reader, final TimestampFilter timestampFilter, final RealtimeRecordDelayFilter realtimeRecordDelayFilter,
			final MonitoringRecordLoggerFilter recordLogger) {
		this.connectPorts(reader.getOutputPort(), timestampFilter.getMonitoringRecordsCombinedInputPort());
		this.connectPorts(timestampFilter.getRecordsWithinTimePeriodOutputPort(), realtimeRecordDelayFilter.getInputPort());
		this.connectPorts(realtimeRecordDelayFilter.getOutputPort(), recordLogger.getInputPort());
	}

}
