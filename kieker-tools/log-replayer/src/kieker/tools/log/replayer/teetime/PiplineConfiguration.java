/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package kieker.tools.log.replayer.teetime;

import kieker.analysis.stage.select.timestampfilter.TimestampFilter;
import kieker.common.record.IMonitoringRecord;
import kieker.tools.source.LogsReaderCompositeStage;

import teetime.framework.Configuration;
import teetime.framework.OutputPort;

/**
 * Configuration for the log replayer.
 *
 * @author Reiner Jung
 * @since 1.16
 */
public class PiplineConfiguration extends Configuration {

	private final DataSendStage consumer;

	/**
	 * Construct the replayer configuration.
	 *
	 * @param parameter
	 *            configuration parameter object
	 */
	public PiplineConfiguration(final Settings parameter) {
		final LogsReaderCompositeStage reader = new LogsReaderCompositeStage(parameter.getDataLocation(), parameter.getVerbose(), 8192);
		OutputPort<IMonitoringRecord> outputPort = reader.getOutputPort();

		if (parameter.getIgnoreBeforeDate() != null || parameter.getIgnoreAfterDate() != null) {
			final long ignoreBeforeDate = parameter.getIgnoreBeforeDate() != null ? parameter.getIgnoreBeforeDate() : Long.MIN_VALUE;
			final long ignoreAfterDate = parameter.getIgnoreAfterDate() != null ? parameter.getIgnoreAfterDate() : Long.MAX_VALUE;
			final TimestampFilter timestampFilter = new TimestampFilter(ignoreBeforeDate, ignoreAfterDate);
			this.connectPorts(outputPort, timestampFilter.getMonitoringRecordsCombinedInputPort());
			outputPort = timestampFilter.getRecordsWithinTimePeriodOutputPort();
		}

		if (parameter.isTimeRelative()) {
			final RewriteTime rewriteTime = new RewriteTime();
			this.connectPorts(outputPort, rewriteTime.getInputPort());
			outputPort = rewriteTime.getOutputPort();
		}

		if (!parameter.isNoDelay()) {
			final ReplayControlStage delayStage = new ReplayControlStage(1000 * 1000, parameter.getDelayFactor(),
					parameter.getShowRecordCount());
			this.connectPorts(outputPort, delayStage.getInputPort());
			outputPort = delayStage.getOutputPort();
		}

		this.consumer = new DataSendStage(parameter.getHostname(), parameter.getOutputPort());
		this.connectPorts(outputPort, this.consumer.getInputPort());
	}

	public DataSendStage getCounter() {
		return this.consumer;
	}

	public boolean isOutputConnected() {
		return this.consumer.isOutputConnected();
	}

}
