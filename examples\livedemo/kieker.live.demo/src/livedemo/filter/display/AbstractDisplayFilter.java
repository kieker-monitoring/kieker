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

package livedemo.filter.display;

import java.util.Date;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
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

import org.primefaces.model.chart.ChartModel;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
@Plugin(configuration =
		@Property(name = AbstractDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES, defaultValue = AbstractDisplayFilter.CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES))
public abstract class AbstractDisplayFilter<T extends IMonitoringRecord, C extends ChartModel> extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_RECORDS = "inputPortRecords";
	public static final String INPUT_PORT_NAME_TIMESTAMPS = "inputPortTimestamps";

	public static final String CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES = "numberOfEntries";
	public static final String CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES = "100";

	public static final String CONFIG_PROPERTY_VALUE_RESPONSETIME_TIMEUNIT = "NANOSECONDS";

	private static final Log LOG = LogFactory.getLog(AbstractDisplayFilter.class);

	private final int numberOfEntries;
	private final TimeUnit timeunit;
	private final Deque<T> records;
	private final C chartModel;

	public AbstractDisplayFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.numberOfEntries = configuration.getIntProperty(AbstractDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES);
		final String recordTimeunitProperty = projectContext.getProperty(IProjectContext.CONFIG_PROPERTY_NAME_RECORDS_TIME_UNIT);
		TimeUnit recordTimeunit;
		try {
			recordTimeunit = TimeUnit.valueOf(recordTimeunitProperty);
		} catch (final IllegalArgumentException ex) { // already caught in AnalysisController, should never happen
			AbstractDisplayFilter.LOG.warn(recordTimeunitProperty + " is no valid TimeUnit! Using NANOSECONDS instead.");
			recordTimeunit = TimeUnit.NANOSECONDS;
		}
		this.timeunit = recordTimeunit;

		this.chartModel = this.createChartModel(this.numberOfEntries);
		this.records = new ConcurrentLinkedDeque<T>();
	}

	@InputPort(name = AbstractDisplayFilter.INPUT_PORT_NAME_RECORDS, eventTypes = { IMonitoringRecord.class })
	public void inputRecords(final T record) {
		this.records.add(record);
	}

	@InputPort(name = ClassLoadingDisplayFilter.INPUT_PORT_NAME_TIMESTAMPS, eventTypes = { Long.class })
	public synchronized void inputTimeEvents(final Long timestamp) {
		final Date date = new Date(TimeUnit.MILLISECONDS.convert(timestamp, this.timeunit));
		final String minutesAndSeconds = date.toString().substring(14, 19);

		this.fillChartModelWithRecordData(this.chartModel, this.records, minutesAndSeconds, this.numberOfEntries);

		this.records.clear();
	}

	public synchronized C getChartModel() {
		return this.chartModel;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(AbstractDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES, String.valueOf(this.numberOfEntries));
		return configuration;
	}

	abstract protected C createChartModel(int numberOfEntries);

	abstract protected void fillChartModelWithRecordData(C chartModel, Deque<T> records, String minutesAndSeconds, int numberOfEntries);

}
