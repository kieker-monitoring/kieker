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
package kieker.tools.log.replayer;

import kieker.analysis.generic.sink.DataSink;
import kieker.analysis.generic.time.TimestampFilter;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationConstants;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.tools.log.replayer.stages.AdjustTimeStage;
import kieker.tools.log.replayer.stages.OpenTelemetryStage;
import kieker.tools.log.replayer.stages.ReplayControlStage;
import kieker.tools.log.replayer.stages.time.adjuster.BranchingRecordTimeAdjuster;
import kieker.tools.log.replayer.stages.time.adjuster.FlowEventTimeAdjuster;
import kieker.tools.log.replayer.stages.time.adjuster.OperationExecutionRecordTimeAdjuster;
import kieker.tools.source.LogsReaderCompositeStage;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.Configuration;
import teetime.framework.OutputPort;
import teetime.stage.Counter;

/**
 * Configuration for the log replayer.
 *
 * @author Reiner Jung
 * @since 1.16
 */
public class TeetimeConfiguration extends Configuration {

	private Counter<IMonitoringRecord> counter = new Counter<IMonitoringRecord>();

	/**
	 * Construct the replayer configuration.
	 *
	 * @param parameter
	 *            configuration parameter object
	 */
	public TeetimeConfiguration(final Settings parameter) {
		final LogsReaderCompositeStage reader = new LogsReaderCompositeStage(parameter.getDataLocation(), parameter.isVerbose(), 8192);
		OutputPort<IMonitoringRecord> outputPort = reader.getOutputPort();
		


		if ((parameter.getIgnoreBeforeDate() != null) || (parameter.getIgnoreAfterDate() != null)) {
			final long ignoreBeforeDate = parameter.getIgnoreBeforeDate() != null ? parameter.getIgnoreBeforeDate() : Long.MIN_VALUE; // NOCS
			final long ignoreAfterDate = parameter.getIgnoreAfterDate() != null ? parameter.getIgnoreAfterDate() : Long.MAX_VALUE; // NOCS
			final TimestampFilter timestampFilter = new TimestampFilter(ignoreBeforeDate, ignoreAfterDate);
			this.connectPorts(outputPort, timestampFilter.getMonitoringRecordsCombinedInputPort());
			outputPort = timestampFilter.getRecordsWithinTimePeriodOutputPort();
		}

		if (parameter.isTimeRelative()) {
			final AdjustTimeStage rewriteTime = new AdjustTimeStage(new FlowEventTimeAdjuster(), new OperationExecutionRecordTimeAdjuster(),
					new BranchingRecordTimeAdjuster());
			this.connectPorts(outputPort, rewriteTime.getInputPort());
			outputPort = rewriteTime.getOutputPort();
		}

		if (!parameter.isNoDelay()) {
			final ReplayControlStage delayStage = new ReplayControlStage(1000 * 1000, parameter.getDelayFactor(),
					parameter.getShowRecordCount());
			this.connectPorts(outputPort, delayStage.getInputPort());
			outputPort = delayStage.getOutputPort();
		}

		this.counter = new Counter<>();
		this.connectPorts(outputPort, this.counter.getInputPort());
		
		OpenTelemetryStage stage = new OpenTelemetryStage();
		this.connectPorts(this.counter.getOutputPort(), stage.getInputPort());

		final kieker.common.configuration.Configuration configuration;
		if (parameter.getKiekerMonitoringProperties() != null) {
			configuration = ConfigurationFactory.createConfigurationFromFile(parameter.getKiekerMonitoringProperties());
		} else {
			configuration = ConfigurationFactory.createDefaultConfiguration();
		}

		configuration.setProperty(ConfigurationConstants.AUTO_SET_LOGGINGTSTAMP, parameter.isTimeRelative());

		final AbstractConsumerStage<IMonitoringRecord> consumer = new DataSink(configuration);
//		this.connectPorts(this.counter.getOutputPort(), consumer.getInputPort());
	}

	public Counter<IMonitoringRecord> getCounter() {
		return this.counter;
	}
}
