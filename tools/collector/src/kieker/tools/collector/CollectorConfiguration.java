/***************************************************************************
 * Copyright 2026 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.collector;

import java.io.File;
import java.io.IOException;

import kieker.analysis.generic.sink.DataSink;
import kieker.analysis.generic.time.TimestampFilter;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationConstants;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.tools.collector.stages.AdjustTimeStage;
import kieker.tools.collector.stages.ReplayControlStage;
import kieker.tools.collector.stages.time.adjuster.BranchingRecordTimeAdjuster;
import kieker.tools.collector.stages.time.adjuster.FlowEventTimeAdjuster;
import kieker.tools.collector.stages.time.adjuster.OperationExecutionRecordTimeAdjuster;
import kieker.analysis.generic.source.ISourceCompositeStage;
import kieker.common.exception.ConfigurationException;
import kieker.tools.common.CommonConfigurationKeys;
import kieker.tools.source.LogsReaderCompositeStage;
import kieker.tools.source.SourceStageFactory;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.Configuration;
import teetime.framework.OutputPort;
import teetime.stage.Counter;


/**
 * Analysis configuration for the data collector.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 *
 */
public class CollectorConfiguration extends Configuration {

	final String SOURCE_STAGE = LogsReaderCompositeStage.class.getCanonicalName();

	private final DataSink consumer;
	private final ISourceCompositeStage sourceStage;
	private final Counter<IMonitoringRecord> counter;

	/**
	 * Configure analysis.
	 *
	 * @param configuration
	 *            configuration for the collector
	 * @throws ConfigurationException
	 *             on configuration error
	 * @throws IOException
	 *             on io errors reading files
	 */
	public CollectorConfiguration(final Settings settings)
			throws ConfigurationException, IOException {
		final kieker.common.configuration.Configuration configuration;
		configuration = ConfigurationFactory.createConfigurationFromFile(settings.getKiekerMonitoringProperties());
		if (settings.getDataLocation() != null) {
			configuration.setProperty(CommonConfigurationKeys.SOURCE_STAGE, SOURCE_STAGE);
			String filenames = "";
			for (File f : settings.getDataLocation()) {
				if (filenames.isEmpty()) {
					filenames = f.getCanonicalPath();
				} else {
					filenames += ":" + f.getCanonicalPath();
				}
			}
			configuration.setProperty(LogsReaderCompositeStage.LOG_DIRECTORIES, filenames);
		}

		configuration.setProperty(ConfigurationConstants.AUTO_SET_LOGGINGTSTAMP, settings.isTimeRelative());


		this.sourceStage = SourceStageFactory.createSourceCompositeStage(configuration);
		OutputPort<IMonitoringRecord> outputPort = this.sourceStage.getOutputPort();

		if ((settings.getIgnoreBeforeDate() != null) || (settings.getIgnoreAfterDate() != null)) {
			final long ignoreBeforeDate = settings.getIgnoreBeforeDate() != null ? settings.getIgnoreBeforeDate() : Long.MIN_VALUE; // NOCS
			final long ignoreAfterDate = settings.getIgnoreAfterDate() != null ? settings.getIgnoreAfterDate() : Long.MAX_VALUE; // NOCS
			final TimestampFilter timestampFilter = new TimestampFilter(ignoreBeforeDate, ignoreAfterDate);
			this.connectPorts(outputPort, timestampFilter.getMonitoringRecordsCombinedInputPort());
			outputPort = timestampFilter.getRecordsWithinTimePeriodOutputPort();
		}

		if (settings.isTimeRelative()) {
			final AdjustTimeStage rewriteTime = new AdjustTimeStage(new FlowEventTimeAdjuster(), new OperationExecutionRecordTimeAdjuster(),
					new BranchingRecordTimeAdjuster());
			this.connectPorts(outputPort, rewriteTime.getInputPort());
			outputPort = rewriteTime.getOutputPort();
		}

		if (!settings.isNoDelay()) {
			final ReplayControlStage delayStage = new ReplayControlStage(1000 * 1000, settings.getDelayFactor(),
					settings.getShowRecordCount());
			this.connectPorts(outputPort, delayStage.getInputPort());
			outputPort = delayStage.getOutputPort();
		}

		this.counter = new Counter<>();
		this.connectPorts(outputPort, this.counter.getInputPort());

		this.consumer = new DataSink(configuration);
		this.connectPorts(this.counter.getOutputPort(), this.consumer.getInputPort());
	}

	public ISourceCompositeStage getSourceStage() {
		return this.sourceStage;
	}

	public Counter<IMonitoringRecord> getCounter() {
		return this.counter;
	}
}
