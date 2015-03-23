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

package kieker.examples.livedemo.analysis.sink;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 * 
 * @param <T>
 *            The type of the monitoring records processed by the filter.
 * @param <C>
 *            The type of the model used by the filter.
 */
@Plugin(configuration =
		@Property(name = AbstractNonAggregatingDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES, defaultValue = AbstractNonAggregatingDisplayFilter.CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES))
public abstract class AbstractNonAggregatingDisplayFilter<T extends IMonitoringRecord, C> extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_RECORDS = "inputPortRecords";

	public static final String CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES = "numberOfEntries";
	public static final String CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES = "100";

	public static final String CONFIG_PROPERTY_VALUE_RESPONSETIME_TIMEUNIT = "NANOSECONDS";

	private static final Log LOG = LogFactory.getLog(AbstractNonAggregatingDisplayFilter.class);

	private final int numberOfEntries;
	private final TimeUnit timeunit;
	private final C chartModel;

	public AbstractNonAggregatingDisplayFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.numberOfEntries = configuration.getIntProperty(AbstractNonAggregatingDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES);
		final String recordTimeunitProperty = projectContext.getProperty(IProjectContext.CONFIG_PROPERTY_NAME_RECORDS_TIME_UNIT);
		TimeUnit recordTimeunit;
		try {
			recordTimeunit = TimeUnit.valueOf(recordTimeunitProperty);
		} catch (final IllegalArgumentException ex) { // already caught in AnalysisController, should never happen
			AbstractNonAggregatingDisplayFilter.LOG.warn(recordTimeunitProperty + " is no valid TimeUnit! Using NANOSECONDS instead.");
			recordTimeunit = TimeUnit.NANOSECONDS;
		}
		this.timeunit = recordTimeunit;

		this.chartModel = this.createChartModel(this.numberOfEntries);
	}

	@InputPort(name = AbstractNonAggregatingDisplayFilter.INPUT_PORT_NAME_RECORDS, eventTypes = { IMonitoringRecord.class })
	public synchronized void inputRecords(final T record) {
		final Date date = new Date(TimeUnit.MILLISECONDS.convert(record.getLoggingTimestamp(), this.timeunit));
		final String minutesAndSeconds = date.toString().substring(14, 19);

		this.fillChartModelWithRecordData(this.chartModel, record, minutesAndSeconds, this.numberOfEntries);
	}

	public synchronized C getChartModel() {
		return this.chartModel;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(AbstractNonAggregatingDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES, String.valueOf(this.numberOfEntries));
		return configuration;
	}

	protected abstract C createChartModel(int numberOfEntries); // NOCS (hidden field)

	protected abstract void fillChartModelWithRecordData(C chartModel, T record, String minutesAndSeconds, int numberOfEntries); // NOCS (hidden field)

}
