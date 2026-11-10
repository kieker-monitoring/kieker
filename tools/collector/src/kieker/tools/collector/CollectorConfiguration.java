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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import kieker.analysis.generic.sink.DataSink;
import kieker.analysis.generic.source.ISourceCompositeStage;
import kieker.analysis.generic.time.TimestampFilter;
import kieker.common.exception.ConfigurationException;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationConstants;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.tools.collector.stages.AdjustTimeStage;
import kieker.tools.collector.stages.ReplayControlStage;
import kieker.tools.collector.stages.time.adjuster.BranchingRecordTimeAdjuster;
import kieker.tools.collector.stages.time.adjuster.FlowEventTimeAdjuster;
import kieker.tools.collector.stages.time.adjuster.OperationExecutionRecordTimeAdjuster;
import kieker.tools.common.CommonConfigurationKeys;
import kieker.tools.source.LogsReaderCompositeStage;
import kieker.tools.source.SourceStageFactory;

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
	
	/** The format pattern which is used to print the date. */
	public static final String DATE_FORMAT_PATTERN = "yyyyMMdd'-'HHmmss";

	/** Date format pattern used for information. */
	public static final String HUMAN_READABLE_DATE_FORMAT = DATE_FORMAT_PATTERN.replaceAll("'", "") + " | timestamp"; // only for usage info

	/** date format provider. */
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.US);

	static {
		DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC")); // NOPMD synchronized access
	}

	private static final String SOURCE_STAGE = LogsReaderCompositeStage.class.getCanonicalName();

	private final DataSink consumer;
	private final ISourceCompositeStage sourceStage;
	private final Counter<IMonitoringRecord> counter;

	/**
	 * Configure analysis.
	 *
	 * @param settings
	 *            settings specified on command line
	 * @throws ConfigurationException
	 *             on configuration error
	 * @throws IOException
	 *             on io errors reading files
	 */
	public CollectorConfiguration(final Settings settings)
			throws ConfigurationException, IOException {
		final kieker.common.configuration.Configuration configuration;
		if (settings.getKiekerMonitoringProperties() != null) {
			configuration = ConfigurationFactory.createConfigurationFromFile(settings.getKiekerMonitoringProperties());
		} else {
			configuration = ConfigurationFactory.createDefaultConfiguration();
		}
		
		final Long ignoreBeforeDate = this.makeDateValue(configuration.getStringProperty(CollectorConfigurationKeys.IGNORE_RECORDS_BEFORE),
				settings.getIgnoreBeforeDate(),
				Long.MIN_VALUE);
		final Long ignoreAfterDate = this.makeDateValue(configuration.getStringProperty(CollectorConfigurationKeys.IGNORE_RECORDS_AFTER),
				settings.getIgnoreAfterDate(),
				Long.MAX_VALUE);
		
		if (settings.getDataLocation() != null) {
			configuration.setProperty(CommonConfigurationKeys.SOURCE_STAGE, SOURCE_STAGE);
			String[] filenames = new String[settings.getDataLocation().size()];
			int i = 0;
			for (File f : settings.getDataLocation()) {
				filenames[i++] = f.getCanonicalPath();
			}
			configuration.setStringArrayProperty(LogsReaderCompositeStage.LOG_DIRECTORIES, filenames);
		}

		Boolean timeRewrite = configuration.getBooleanProperty(CollectorConfigurationKeys.TIME_REWRITE);
		if (settings.isTimeRewrite()) {
			timeRewrite = settings.isTimeRewrite();
		}
		
		configuration.setProperty(ConfigurationConstants.AUTO_SET_LOGGINGTSTAMP, timeRewrite);

		this.sourceStage = SourceStageFactory.createSourceCompositeStage(configuration);
		OutputPort<IMonitoringRecord> outputPort = this.sourceStage.getOutputPort();

		// if min and max values are set, no filtering is necessary
		if (!(ignoreBeforeDate == Long.MIN_VALUE && ignoreAfterDate == Long.MAX_VALUE)) {
			final TimestampFilter timestampFilter = new TimestampFilter(ignoreBeforeDate, ignoreAfterDate);
			this.connectPorts(outputPort, timestampFilter.getMonitoringRecordsCombinedInputPort());
			outputPort = timestampFilter.getRecordsWithinTimePeriodOutputPort();
		}
				
		if (timeRewrite) {
			final AdjustTimeStage rewriteTime = new AdjustTimeStage(new FlowEventTimeAdjuster(), new OperationExecutionRecordTimeAdjuster(),
					new BranchingRecordTimeAdjuster());
			this.connectPorts(outputPort, rewriteTime.getInputPort());
			outputPort = rewriteTime.getOutputPort();
		}

		Boolean noDelay = configuration.getBooleanProperty(CollectorConfigurationKeys.NO_DELAY);
		if (settings.isNoDelay()) {
			noDelay = settings.isNoDelay();
		}
		
		if (!noDelay) {
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
	
	private Long makeDateValue(final String value, final Long setting, final Long defaultValue) {
		final Long dateValue = this.convertDate(value);
		if (setting != null) {
			return setting;
		}
		if (dateValue == null) {
			return defaultValue;
		} else {
			return dateValue;
		}
	}
	
	private Long convertDate(final String value) {
		long result;
		try {
			result = Long.parseLong(value);
		} catch (final NumberFormatException ex) {
			try {
				final Date ignoreBeforeDate = DATE_FORMAT.parse(value); // NOPMD concurrent access not possible
				result = ignoreBeforeDate.getTime() * (1000 * 1000);
			} catch (final java.text.ParseException ex2) {
				return null;
			}
		}
		return result;
	}

	public ISourceCompositeStage getSourceStage() {
		return this.sourceStage;
	}

	public Counter<IMonitoringRecord> getCounter() {
		return this.counter;
	}
}
