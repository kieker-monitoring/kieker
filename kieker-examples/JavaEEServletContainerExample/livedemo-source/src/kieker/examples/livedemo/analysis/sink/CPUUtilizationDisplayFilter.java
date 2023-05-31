/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.system.CPUUtilizationRecord;
import kieker.examples.livedemo.analysis.util.LimitedHashMap;
import kieker.examples.livedemo.view.util.Model;

/**
 * @author Bjoern Weissenfels
 * 
 * @since 1.10
 */
@Plugin(configuration =
		@Property(name = CPUUtilizationDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES,
				defaultValue = CPUUtilizationDisplayFilter.CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES))
public class CPUUtilizationDisplayFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_RECORDS = "inputPortRecords";

	public static final String CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES = "numberOfEntries";
	public static final String CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES = "100";

	public static final String CONFIG_PROPERTY_VALUE_RESPONSETIME_TIMEUNIT = "NANOSECONDS";

	private static final Logger LOGGER = LoggerFactory.getLogger(CPUUtilizationDisplayFilter.class);

	private static final String TOTAL_UTILIZATION = "totalUtilization";
	private static final String IDLE = "idle";
	private static final String IRQ = "irq";
	private static final String NICE = "nice";
	private static final String SYSTEM = "system";
	private static final String USER = "user";

	private final int numberOfEntries;
	private final TimeUnit timeunit;

	private final List<Model<CartesianChartModel>> models;
	private final Set<String> ids = new HashSet<String>();

	public CPUUtilizationDisplayFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.numberOfEntries = configuration.getIntProperty(CPUUtilizationDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES);
		final String recordTimeunitProperty = projectContext.getProperty(IProjectContext.CONFIG_PROPERTY_NAME_RECORDS_TIME_UNIT);
		TimeUnit recordTimeunit;
		try {
			recordTimeunit = TimeUnit.valueOf(recordTimeunitProperty);
		} catch (final IllegalArgumentException ex) { // already caught in AnalysisController, should never happen
			CPUUtilizationDisplayFilter.LOGGER.warn("{} is no valid TimeUnit! Using NANOSECONDS instead.", recordTimeunitProperty);
			recordTimeunit = TimeUnit.NANOSECONDS;
		}
		this.timeunit = recordTimeunit;

		this.models = Collections.synchronizedList(new ArrayList<Model<CartesianChartModel>>());
	}

	public List<Model<CartesianChartModel>> getModels() {
		return this.models;
	}

	@InputPort(name = CPUUtilizationDisplayFilter.INPUT_PORT_NAME_RECORDS, eventTypes = { CPUUtilizationRecord.class })
	public synchronized void inputRecords(final CPUUtilizationRecord record) {
		final Date date = new Date(TimeUnit.MILLISECONDS.convert(record.getLoggingTimestamp(), this.timeunit));
		final String minutesAndSeconds = date.toString().substring(14, 19);

		this.fillChartModelWithRecordData(record, minutesAndSeconds, this.numberOfEntries);
	}

	private void fillChartModelWithRecordData(final CPUUtilizationRecord record, final String minutesAndSeconds, final int numberOfEntries) {
		final String id = record.getHostname() + " - " + record.getCpuID();
		if (this.ids.contains(id)) {
			for (final Model<CartesianChartModel> model : this.models) {
				if (model.getName().equals(id)) {
					final CartesianChartModel chartModel = model.getModel();
					for (final ChartSeries series : chartModel.getSeries()) {
						if (CPUUtilizationDisplayFilter.TOTAL_UTILIZATION.equals(series.getLabel())) {
							series.getData().put(minutesAndSeconds, record.getTotalUtilization() * 100);
						} else if (CPUUtilizationDisplayFilter.IDLE.equals(series.getLabel())) {
							series.getData().put(minutesAndSeconds, record.getIdle() * 100);
						} else if (CPUUtilizationDisplayFilter.IRQ.equals(series.getLabel())) {
							series.getData().put(minutesAndSeconds, record.getIrq() * 100);
						} else if (CPUUtilizationDisplayFilter.NICE.equals(series.getLabel())) {
							series.getData().put(minutesAndSeconds, record.getNice() * 100);
						} else if (CPUUtilizationDisplayFilter.SYSTEM.equals(series.getLabel())) {
							series.getData().put(minutesAndSeconds, record.getSystem() * 100);
						} else if (CPUUtilizationDisplayFilter.USER.equals(series.getLabel())) {
							series.getData().put(minutesAndSeconds, record.getUser() * 100);
						}
					}
				}
			}
		} else {
			this.ids.add(id);
			final CartesianChartModel chartModel = new CartesianChartModel();
			final Map<Object, Number> totalUtilizationMap = new LimitedHashMap<Object, Number>(this.numberOfEntries);
			final Map<Object, Number> idleMap = new LimitedHashMap<Object, Number>(this.numberOfEntries);
			final Map<Object, Number> irqMap = new LimitedHashMap<Object, Number>(this.numberOfEntries);
			final Map<Object, Number> niceMap = new LimitedHashMap<Object, Number>(this.numberOfEntries);
			final Map<Object, Number> systemMap = new LimitedHashMap<Object, Number>(this.numberOfEntries);
			final Map<Object, Number> userMap = new LimitedHashMap<Object, Number>(this.numberOfEntries);

			totalUtilizationMap.put(minutesAndSeconds, record.getTotalUtilization() * 100);
			idleMap.put(minutesAndSeconds, record.getIdle() * 100);
			irqMap.put(minutesAndSeconds, record.getIrq() * 100);
			niceMap.put(minutesAndSeconds, record.getNice() * 100);
			systemMap.put(minutesAndSeconds, record.getSystem() * 100);
			userMap.put(minutesAndSeconds, record.getUser() * 100);

			final ChartSeries totalUtilizationSeries = new ChartSeries(CPUUtilizationDisplayFilter.TOTAL_UTILIZATION);
			totalUtilizationSeries.setData(totalUtilizationMap);
			chartModel.addSeries(totalUtilizationSeries);
			final ChartSeries idleSeries = new ChartSeries(CPUUtilizationDisplayFilter.IDLE);
			idleSeries.setData(idleMap);
			chartModel.addSeries(idleSeries);
			final ChartSeries irqSeries = new ChartSeries(CPUUtilizationDisplayFilter.IRQ);
			irqSeries.setData(irqMap);
			chartModel.addSeries(irqSeries);
			final ChartSeries niceSeries = new ChartSeries(CPUUtilizationDisplayFilter.NICE);
			niceSeries.setData(niceMap);
			chartModel.addSeries(niceSeries);
			final ChartSeries systemSeries = new ChartSeries(CPUUtilizationDisplayFilter.SYSTEM);
			systemSeries.setData(systemMap);
			chartModel.addSeries(systemSeries);
			final ChartSeries userSeries = new ChartSeries(CPUUtilizationDisplayFilter.USER);
			userSeries.setData(userMap);
			chartModel.addSeries(userSeries);

			final Model<CartesianChartModel> model = new Model<CartesianChartModel>(chartModel, id);
			this.models.add(model);
		}
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(AbstractNonAggregatingDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES, String.valueOf(this.numberOfEntries));
		return configuration;
	}

}
